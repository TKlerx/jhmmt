/*******************************************************************************
 * Copyright (c) 2004-2009, Jean-Marc François. All Rights Reserved.
 * Originally licensed under the New BSD license.  See the LICENSE_OLD file.
 * Copyright (c) 2013, Timo Klerx. All Rights Reserved.
 * Now licensed under LGPL. See the LICENSE file.
 * This file is part of jhmmt.
 * 
 * jhmmt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * jhmmt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with jhmmt.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package be.ac.ulg.montefiore.run.jahmm;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.util.Precision;

/**
 * This class can be used to compute the probability of a given observations
 * sequence for a given HMM.
 * <p>
 * This class implements the scaling method explained in <i>Rabiner</i> and
 * <i>Juang</i>, thus the {@link #alphaElement(int,int) alphaElement} and
 * {@link #betaElement(int,int) betaElement} return the scaled alpha and beta
 * elements. The <code>alpha</code> array must always be computed because the
 * scaling factors are computed together with it.
 * <p>
 * For more information on the scaling procedure, read <i>Rabiner</i> and
 * <i>Juang</i>'s <i>Fundamentals of speech recognition</i> (Prentice Hall,
 * 1993).
 */
public class ForwardBackwardScaledCalculator extends ForwardBackwardCalculator {
	private static final double LOG_OF_0 = Double.MIN_VALUE;
	/*
	 * Warning, the semantic of the alpha and beta elements are changed; in this
	 * class, they have their value scaled.
	 */
	// Scaling factors
	private double[] ctFactors;
	private double lnProbability;
	

	/**
	 * Computes the probability of occurence of an observation sequence given a
	 * Hidden Markov Model. The algorithms implemented use scaling to avoid
	 * underflows.
	 * 
	 * @param hmm
	 *            A Hidden Markov Model;
	 * @param oseq
	 *            An observations sequence.
	 * @param flags
	 *            How the computation should be done. See the
	 *            {@link ForwardBackwardCalculator.Computation}. The alpha array
	 *            is always computed.
	 */
	public <O extends Observation> ForwardBackwardScaledCalculator(List<? extends O> oseq, Hmm<O> hmm, EnumSet<Computation> flags) {
		if (oseq.isEmpty()) {
			throw new IllegalArgumentException();
		}

		ctFactors = new double[oseq.size()];
		Arrays.fill(ctFactors, 0.);

		computeAlpha(hmm, oseq);

		if (flags.contains(Computation.BETA)) {
			computeBeta(hmm, oseq);
		}
		// System.out.println("List for ForwardBackwardScaledCalc=" + oseq);
		computeProbability(oseq, hmm, flags);
	}

	/**
	 * Computes the probability of occurence of an observation sequence given a
	 * Hidden Markov Model. This computation computes the scaled
	 * <code>alpha</code> array as a side effect.
	 * 
	 * @see #ForwardBackwardScaledCalculator(List, Hmm, EnumSet)
	 */
	public <O extends Observation> ForwardBackwardScaledCalculator(List<? extends O> oseq, Hmm<O> hmm) {
		this(oseq, hmm, EnumSet.of(Computation.ALPHA));
	}

	/* Computes the content of the scaled alpha array */
	@Override
	protected <O extends Observation> void computeAlpha(Hmm<? super O> hmm, List<O> oseq) {
		alpha = new double[oseq.size()][hmm.nbStates()];

		for (int i = 0; i < hmm.nbStates(); i++) {
			computeAlphaInit(hmm, oseq.get(0), i);
		}
		scale(ctFactors, alpha, 0);

		Iterator<? extends O> seqIterator = oseq.iterator();
		if (seqIterator.hasNext()) {
			seqIterator.next();
		}

		for (int t = 1; t < oseq.size(); t++) {
			O observation = seqIterator.next();

			for (int i = 0; i < hmm.nbStates(); i++) {
				computeAlphaStep(hmm, observation, t, i);
			}
			scale(ctFactors, alpha, t);
		}
	}

	/*
	 * Computes the content of the scaled beta array. The scaling factors are
	 * those computed for alpha.
	 */
	@Override
	protected <O extends Observation> void computeBeta(Hmm<? super O> hmm, List<O> oseq) {
		beta = new double[oseq.size()][hmm.nbStates()];

		for (int i = 0; i < hmm.nbStates(); i++) {
			beta[oseq.size() - 1][i] = 1. / ctFactors[oseq.size() - 1];
		}

		for (int t = oseq.size() - 2; t >= 0; t--) {
			for (int i = 0; i < hmm.nbStates(); i++) {
				computeBetaStep(hmm, oseq.get(t + 1), t, i);
				beta[t][i] /= ctFactors[t];
			}
		}
	}

	/* Normalize alpha[t] and put the normalization factor in ctFactors[t] */
	private void scale(double[] ctFactors, double[][] array, int t) {
		double[] table = array[t];
		double sum = 0.;

		for (int i = 0; i < table.length; i++) {
			sum += table[i];
		}
		if (Double.isNaN(sum)) {
			System.err.println("Sum is NaN for t=" + t + "\ttable=" + Arrays.toString(table));
		}
		ctFactors[t] = sum;

		for (int i = 0; i < table.length; i++) {
			double temp = table[i] / sum;
			if (Double.isNaN(temp)) {
				table[i] = 0.;
			} else {
				table[i] = temp;
			}
		}
	}

	private <O extends Observation> void computeProbability(List<O> oseq, Hmm<? super O> hmm, EnumSet<Computation> flags) {
		lnProbability = 0.;
		double temp;
		// System.out.println("computeProbability oseq.size()="+oseq.size());
		for (int t = 0; t < oseq.size(); t++) {
			if (Precision.equals(ctFactors[t], 0.0)) {
				temp = LOG_OF_0;
				// System.out.println("Using Double.MIN_VALUE");
			} else {
				temp = Math.log(ctFactors[t]);
				// System.out.println("Using ctFactors[" + t + "]=" +
				// ctFactors[t]);
				// System.out.println("temp=" + temp);
			}
			if (Double.isNaN(temp) || Double.isInfinite(temp)) {
				System.err.println("ctFactors[t]=" + ctFactors[t] + "\ttemp=" + temp + "\tt=" + t);
			}
			lnProbability += temp;
		}
		if(-1.67 < lnProbability && lnProbability < -1.66){
			System.err.println("lnProbability is strange. perhaps not all ctFactors have values != 0.0");
		}
		if (Double.isInfinite(lnProbability)) {
			System.err.println("lnProbability is infinite");
		}
		probability = Math.exp(lnProbability);
		// System.out.println("probability in ScaledCalc="+probability);
	}

	/**
	 * Return the neperian logarithm of the probability of the sequence that
	 * generated this object.
	 * 
	 * @return The probability of the sequence of interest's neperian logarithm.
	 */
	public double lnProbability() {
		return lnProbability;
	}
}
