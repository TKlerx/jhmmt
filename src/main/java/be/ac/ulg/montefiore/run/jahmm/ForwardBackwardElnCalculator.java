/*******************************************************************************
 * Copyright (c) 2013, Timo Klerx. All Rights Reserved.
 * Licensed under LGPL. See the LICENSE file.
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
import java.util.List;

import org.apache.commons.math3.util.Precision;

public class ForwardBackwardElnCalculator extends ForwardBackwardCalculator {
	public static final double LOG_OF_0 = Double.NaN;
	private double lnProbability;
	private final double[] ctFactors;
	private double[][] elnAlpha;
	private double[][] elnBeta;
	private double[][] elnGamma;

	/**
	 * Computes the probability of occurence of an observation sequence given a
	 * Hidden Markov Model. This computation computes the scaled
	 * <code>alpha</code> array as a side effect.
	 * 
	 * @see #ForwardBackwardScaledCalculator(List, Hmm, EnumSet)
	 */
	public <O extends Observation> ForwardBackwardElnCalculator(List<? extends O> oseq, Hmm<O> hmm) {
		if (oseq.isEmpty())
			throw new IllegalArgumentException();
		ctFactors = null;
		// ctFactors = new double[oseq.size()];
		// Arrays.fill(ctFactors, 0.);

		computeAlpha(hmm, oseq);

		// System.out.println("Alpha: \n" + TwoDimArrayToString(elnAlpha));
		computeBeta(hmm, oseq);
		// System.out.println("beta: \n" + TwoDimArrayToString(elnBeta));
		computeGamma(hmm, oseq);

		// System.out.println("List for ForwardBackwardScaledCalc=" + oseq);
		computeProbability(hmm, oseq);
	}

	private String TwoDimArrayToString(double[][] array) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < array.length; i++) {
			sb.append(Arrays.toString(array[i]));
			sb.append('\n');
		}
		sb.deleteCharAt(sb.length() - 1);

		return sb.toString();
	}

	/* Computes the content of the scaled alpha array */
	@Override
	protected <O extends Observation> void computeAlpha(Hmm<? super O> hmm, List<O> oseq) {
		elnAlpha = new double[oseq.size()][hmm.nbStates()];
		for (int i = 0; i < hmm.nbStates(); i++) {
			elnAlpha[0][i] = elnproduct(eln(hmm.getPi(i)), eln(hmm.getOpdf(i).probability(oseq.get(0))));
		}
		for (int t = 1; t < oseq.size(); t++) {
			for (int i = 0; i < hmm.nbStates(); i++) {
				double logAlpha = LOG_OF_0;
				for (int j = 0; j < hmm.nbStates(); j++) {
					logAlpha = elnsum(logAlpha, elnproduct(elnAlpha[t - 1][j], eln(hmm.getAij(j, i))));
				}
				elnAlpha[t][i] = elnproduct(logAlpha, eln(hmm.getOpdf(i).probability(oseq.get(t))));
			}
		}

	}

	@Override
	protected <O extends Observation> void computeBeta(be.ac.ulg.montefiore.run.jahmm.Hmm<? super O> hmm, java.util.List<O> oseq) {
		elnBeta = new double[oseq.size()][hmm.nbStates()];

		for (int i = 0; i < hmm.nbStates(); i++) {
			elnBeta[oseq.size() - 1][i] = 0;
		}
		for (int t = oseq.size() - 2; t >= 0; t--) {
			for (int i = 0; i < hmm.nbStates(); i++) {
				double logBeta = LOG_OF_0;
				for (int j = 0; j < hmm.nbStates(); j++) {
					logBeta = elnsum(logBeta,
							elnproduct(eln(hmm.getAij(i, j)), elnproduct(eln(hmm.getOpdf(j).probability(oseq.get(t + 1))), elnBeta[t + 1][j])));
				}
				elnBeta[t][i] = logBeta;
			}
		}

	}

	private <O extends Observation> void computeProbability(Hmm<? super O> hmm, List<O> oseq) {

		this.lnProbability = LOG_OF_0;

		for (int i = 0; i < hmm.nbStates(); i++) {
			this.lnProbability = elnsum(this.lnProbability, this.elnGamma[oseq.size() - 1][i]);
		}
		this.probability = eexp(this.lnProbability);
		double lnSum = LOG_OF_0;
		for (int i = 0; i < hmm.nbStates(); i++) {
			lnSum = elnsum(lnSum, elnGamma[oseq.size() - 1][i]);
		}
		// if (!Precision.equals(lnSum, 0.0)) {
		// System.out.println("ln sum=" + lnSum);
		// System.out.println(Arrays.toString(elnGamma[oseq.size() - 1]));
		// System.out.println("this.lnProbability=" + this.lnProbability);
		// }
	}

	private <O extends Observation> void computeGamma(Hmm<? super O> hmm, List<O> oseq) {
		elnGamma = new double[oseq.size()][hmm.nbStates()];
		for (int t = 0; t < oseq.size(); t++) {
			double normalizer = LOG_OF_0;
			for (int i = 0; i < hmm.nbStates(); i++) {
				elnGamma[t][i] = elnproduct(elnAlpha[t][i], elnBeta[t][i]);
				normalizer = elnsum(normalizer, elnGamma[t][i]);
			}
			for (int i = 0; i < hmm.nbStates(); i++) {
				elnGamma[t][i] = elnproduct(elnGamma[t][i], -normalizer);
			}
		}
		for (int t = 0; t < oseq.size(); t++) {
			double lnSum = LOG_OF_0;
			for (int i = 0; i < hmm.nbStates(); i++) {
				lnSum = elnsum(lnSum, elnGamma[t][i]);
			}
			if (!Precision.equals(lnSum, 0.0)) {
				System.out.println("ln sum=" + lnSum + " but should be zero for t=" + t);
				throw new IllegalStateException();
			}
		}
	}

	public static double eexp(double x) {
		if (isLogZero(x)) {
			return 0;
		} else {
			return Math.exp(x);
		}
	}

	public static double eln(double x) {
		if ((Precision.equals(x, 0.0))) {
			return LOG_OF_0;
		} else if (x > 0) {
			return Math.log(x);
		} else {
			throw new IllegalArgumentException("x must be positive, but is " + x);
		}
	}

	public static boolean isLogZero(double x) {
		return Double.isNaN(x);
	}

	public static double elnsum(double elnx, double elny) {
		if (isLogZero(elnx) || isLogZero(elny)) {
			if (isLogZero(elnx)) {
				return elny;
			} else {
				return elnx;
			}
		} else {
			if (elnx > elny) {
				return elnx + eln(1 + Math.exp(elny - elnx));
			} else {
				return elny + eln(1 + Math.exp(elnx - elny));
			}
		}
	}

	public static double elnproduct(double elnx, double elny) {
		if (isLogZero(elnx) || isLogZero(elny)) {
			return LOG_OF_0;
		} else {
			return elnx + elny;
		}
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

	public double gammaElement(int t, int i) {
		if (elnGamma == null)
			throw new UnsupportedOperationException("gamma array has not " + " been computed");

		return elnGamma[t][i];
	}

	public double[][] gammaArray() {
		return elnGamma;
	}

	@Override
	public double alphaElement(int t, int i) {
		return elnAlpha[t][i];
	}

	@Override
	public double betaElement(int t, int i) {
		return elnBeta[t][i];
	}
}
