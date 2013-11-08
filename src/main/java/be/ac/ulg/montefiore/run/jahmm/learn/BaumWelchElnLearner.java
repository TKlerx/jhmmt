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
package be.ac.ulg.montefiore.run.jahmm.learn;

import static be.ac.ulg.montefiore.run.jahmm.ForwardBackwardElnCalculator.LOG_OF_0;
import static be.ac.ulg.montefiore.run.jahmm.ForwardBackwardElnCalculator.eexp;
import static be.ac.ulg.montefiore.run.jahmm.ForwardBackwardElnCalculator.eln;
import static be.ac.ulg.montefiore.run.jahmm.ForwardBackwardElnCalculator.elnproduct;
import static be.ac.ulg.montefiore.run.jahmm.ForwardBackwardElnCalculator.elnsum;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.util.Precision;

import be.ac.ulg.montefiore.run.jahmm.ForwardBackwardCalculator;
import be.ac.ulg.montefiore.run.jahmm.ForwardBackwardElnCalculator;
import be.ac.ulg.montefiore.run.jahmm.Hmm;
import be.ac.ulg.montefiore.run.jahmm.Observation;
import be.ac.ulg.montefiore.run.jahmm.ObservationInteger;
import be.ac.ulg.montefiore.run.jahmm.Opdf;
import be.ac.ulg.montefiore.run.jahmm.OpdfInteger;

/**
 * An implementation of the Baum-Welch learning algorithm. It uses a scaling
 * mechanism so as to avoid underflows.
 * <p>
 * For more information on the scaling procedure, read <i>Rabiner</i> and
 * <i>Juang</i>'s <i>Fundamentals of speech recognition</i> (Prentice Hall,
 * 1993).
 */
public class BaumWelchElnLearner extends BaumWelchLearner {
	/**
	 * Initializes a Baum-Welch algorithm implementation.
	 */
	public BaumWelchElnLearner() {
	}

	@Override
	protected <O extends Observation> ForwardBackwardElnCalculator generateForwardBackwardCalculator(List<? extends O> sequence, Hmm<O> hmm) {
		return new ForwardBackwardElnCalculator(sequence, hmm);
	}

	/*
	 * Here, the xi (and, thus, gamma) values are not divided by the probability
	 * of the sequence because this probability might be too small and induce an
	 * underflow. xi[t][i][j] still can be interpreted as P[q_t = i and q_(t+1)
	 * = j | obsSeq, hmm] because we assume that the scaling factors are such
	 * that their product is equal to the inverse of the probability of the
	 * sequence.
	 */
	@Override
	protected <O extends Observation> double[][][] estimateXi(List<? extends O> sequence, ForwardBackwardCalculator fbc, Hmm<O> hmm) {
		if (sequence.size() <= 1)
			throw new IllegalArgumentException("Observation sequence too " + "short");

		double xi[][][] = new double[sequence.size() - 1][hmm.nbStates()][hmm.nbStates()];

		if (!(fbc instanceof ForwardBackwardElnCalculator)) {
			throw new IllegalStateException("only run BWL Eln Calc with an ForwardBackwardElnCalc");
		}

		for (int t = 0; t < sequence.size() - 1; t++) {
			double normalizer = LOG_OF_0;

			for (int i = 0; i < hmm.nbStates(); i++) {
				for (int j = 0; j < hmm.nbStates(); j++) {
					xi[t][i][j] = elnproduct(fbc.alphaElement(t, i),
							elnproduct(eln(hmm.getAij(i, j)), elnproduct(eln(hmm.getOpdf(j).probability(sequence.get(t + 1))), fbc.betaElement(t + 1, j))));
					normalizer = elnsum(normalizer, xi[t][i][j]);
				}
			}
			for (int i = 0; i < hmm.nbStates(); i++) {
				for (int j = 0; j < hmm.nbStates(); j++) {
					xi[t][i][j] = elnproduct(xi[t][i][j], -normalizer);
				}
			}

		}
		// check for sum of xis beeing 1 respectively
		// lnSum beeing 0
		for (int t = 0; t < sequence.size() - 1; t++) {
			for (int i = 0; i < hmm.nbStates(); i++) {
				double elnSum = LOG_OF_0;
				for (int j = 0; j < hmm.nbStates(); j++) {
					elnSum = elnsum(elnSum, xi[t][i][j]);
				}
				if (!Precision.equals(elnSum, 0)) {
					System.err.println("xi sum (in baum welch eln learner)=" + elnSum + " but should be zero for t=" + t + ", i=" + i);
					System.out.println(sequence);
					throw new IllegalStateException();
				}
			}
		}
		return xi;
	}

	/**
	 * Performs one iteration of the Baum-Welch algorithm. In one iteration, a
	 * new HMM is computed using a previously estimated HMM.
	 * 
	 * @param hmm
	 *            A previously estimated HMM.
	 * @param sequences
	 *            The observation sequences on which the learning is based. Each
	 *            sequence must have a length higher or equal to 2.
	 * @return A new, updated HMM.
	 */
	@Override
	public <O extends Observation> Hmm<O> iterate(Hmm<O> hmm, List<? extends List<? extends O>> sequences) {
		Hmm<O> nhmm;
		try {
			nhmm = hmm.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError();
		}

		/* gamma and xi arrays are those defined by Rabiner and Juang */
		/* allGamma[n] = gamma array associated to observation sequence n */
		double allElnGamma[][][] = new double[sequences.size()][][];

		/*
		 * a[i][j] = aijNum[i][j] / aijDen[i] aijDen[i] = expected number of
		 * transitions from state i aijNum[i][j] = expected number of
		 * transitions from state i to j
		 */
		// double aijNum[][] = new double[hmm.nbStates()][hmm.nbStates()];
		// double aijDen[] = new double[hmm.nbStates()];
		//
		// Arrays.fill(aijDen, LOG_OF_0);
		// for (int i = 0; i < hmm.nbStates(); i++) {
		// Arrays.fill(aijNum[i], LOG_OF_0);
		// }

		int g = 0;
		for (List<? extends O> obsSeq : sequences) {
			ForwardBackwardElnCalculator fbc = generateForwardBackwardCalculator(obsSeq, hmm);

			double elnXi[][][] = estimateXi(obsSeq, fbc, hmm);
			double elnGamma[][] = allElnGamma[g++] = fbc.gammaArray();

			for (int i = 0; i < hmm.nbStates(); i++) {
				for (int j = 0; j < hmm.nbStates(); j++) {
					double numerator = LOG_OF_0;
					double denominator = LOG_OF_0;
					for (int t = 0; t < obsSeq.size() - 1; t++) {
						numerator = elnsum(numerator, elnXi[t][i][j]);
						denominator = elnsum(denominator, elnGamma[t][i]);
						// aijNum[i][j] = elnsum(aijNum[i][j], elnXi[t][i][j]);
					}
					nhmm.setAij(i, j, eexp(elnproduct(numerator, -denominator)));

					// aijDen[i] = elnsum(aijDen[i], elnGamma[t][i]);
				}
			}
			// for (int i = 0; i < hmm.nbStates(); i++) {
			// for (int j = 0; j < hmm.nbStates(); j++) {
			// nhmm.setAij(i, j, eexp(elnproduct(aijNum[i][j], -aijDen[i])));
			// }
			// }
		}

		// check for sum for any state i to every state j beeing 1 respectively
		// lnSum beeing 0
		for (int i = 0; i < hmm.nbStates(); i++) {
			double elnSum = LOG_OF_0;
			for (int j = 0; j < hmm.nbStates(); j++) {
				elnSum = elnsum(elnSum, nhmm.getAij(i, j));
			}
			if (!Precision.equals(elnSum, 0)) {
				System.out.println("eln sum (in baum welch eln learner)=" + elnSum + " but should be zero for state i=" + i);
			}
		}

		/* pi computation */
		for (int i = 0; i < hmm.nbStates(); i++)
			nhmm.setPi(i, 0.);

		for (int o = 0; o < sequences.size(); o++)
			for (int i = 0; i < hmm.nbStates(); i++)
				nhmm.setPi(i, nhmm.getPi(i) + eexp(allElnGamma[o][0][i]) / sequences.size());

		/* pdfs computation */
		for (int i = 0; i < hmm.nbStates(); i++) {
			Opdf<O> opdf = nhmm.getOpdf(i);
			int entries = -1;
			if (opdf instanceof OpdfInteger) {
				entries = ((OpdfInteger) opdf).nbEntries();
			}
			List<O> observations = KMeansLearner.flat(sequences);
			double[] weights = new double[observations.size()];

			int o = 0;
			double numerator[] = new double[entries];
			Arrays.fill(numerator, LOG_OF_0);
			double denominator = LOG_OF_0;
			for (List<? extends O> obsSeq : sequences) {

				for (int t = 0; t < obsSeq.size(); t++) {
					int value = ((ObservationInteger) obsSeq.get(t)).value;
					numerator[value] = elnsum(numerator[value], allElnGamma[o][t][i]);
					denominator = elnsum(denominator, allElnGamma[o][t][i]);
				}
			}
			o++;
			double[] probs = new double[numerator.length];
			for (int k = 0; k < probs.length; k++) {
				probs[k] = eexp(elnproduct(numerator[k], denominator));
			}
			opdf.fit(observations);
			// nhmm.setOpdf(i, (Opdf<O>) new OpdfInteger(probs));
		}

		return nhmm;
	}

}
