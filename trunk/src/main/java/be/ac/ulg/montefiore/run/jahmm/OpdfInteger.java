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

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

/**
 * This class represents a distribution of a finite number of positive integer
 * observations.
 */
public class OpdfInteger implements Opdf<ObservationInteger> {
	private double[] probabilities;
	private static Random masterSeed = new Random(1232343453445l);
	private final Random r;

	/**
	 * Builds a new probability distribution which operates on integer values.
	 * The probabilities are initialized so that the distribution is uniformaly
	 * distributed.
	 * 
	 * @param nbEntries
	 *            The number of values to which to associate probabilities.
	 *            Observations handled by this distribution have to be higher or
	 *            equal than 0 and strictly smaller than <code>nbEntries</code>.
	 */
	public OpdfInteger(int nbEntries) {
		r = new Random(masterSeed.nextLong());
		if (nbEntries <= 0)
			throw new IllegalArgumentException("Argument must be strictly " + "positive");

		probabilities = new double[nbEntries];

		for (int i = 0; i < nbEntries; i++)
			probabilities[i] = 1. / (nbEntries);
	}

	/**
	 * Builds a new probability distribution which operates on integer values.
	 * 
	 * @param probabilities
	 *            Array holding one probability for each possible argument value
	 *            (<i>i.e.</i> such that <code>probabilities[i]</code> is the
	 *            probability of the observation <code>i</code>.
	 */
	public OpdfInteger(double[] probabilities) {
		r = new Random(masterSeed.nextLong());
		if (probabilities.length == 0)
			throw new IllegalArgumentException("Invalid empty array");

		this.probabilities = new double[probabilities.length];

		for (int i = 0; i < probabilities.length; i++) {
			if (Double.isNaN(this.probabilities[i])) {
				throw new IllegalArgumentException("this.probabilities[i] is NaN" + this.probabilities[i] + Arrays.toString(this.probabilities));
			}
			if ((this.probabilities[i] = probabilities[i]) < 0.) {
				throw new IllegalArgumentException();
			}
		}
	}

	/**
	 * Returns how many integers are associated to probabilities by this
	 * distribution.
	 * 
	 * @return The number of integers are associated to probabilities.
	 */
	public int nbEntries() {
		return probabilities.length;
	}

	@Override
	public double probability(ObservationInteger o) {
		if (o.value > probabilities.length - 1)
			throw new IllegalArgumentException("Wrong observation value");
		if (Double.isNaN(probabilities[o.value])) {
			throw new IllegalStateException("probabilities[o.value] is NaN" + Arrays.toString(probabilities));
		}
		return probabilities[o.value];
	}

	@Override
	public ObservationInteger generate() {
		double rand = r.nextDouble();

		for (int i = 0; i < probabilities.length - 1; i++) {
			if (Double.isNaN(probabilities[i])) {
				throw new IllegalStateException("probabilities[" + i + "] is NaN" + Arrays.toString(probabilities));

			}
			if ((rand -= probabilities[i]) < 0.)
				return new ObservationInteger(i);
		}
		return new ObservationInteger(probabilities.length - 1);
	}

	@Override
	public void fit(ObservationInteger... oa) {
		fit(Arrays.asList(oa));
	}

	@Override
	public void fit(Collection<? extends ObservationInteger> co) {
		// try {
		if (co.isEmpty())
			throw new IllegalArgumentException("Empty observation set");

		for (int i = 0; i < probabilities.length; i++)
			probabilities[i] = 0.;
		for (ObservationInteger o : co)
			probabilities[o.value]++;

		for (int i = 0; i < probabilities.length; i++)
			probabilities[i] /= co.size();
		// } catch (ArrayIndexOutOfBoundsException e) {
		// System.err.println("Probabilities has length " + probabilities.length
		// + ", but the Collection looks like this: " + co);
		// throw e;
		// }
	}

	@Override
	public void fit(ObservationInteger[] o, double[] weights) {
		fit(Arrays.asList(o), weights);
	}

	@Override
	public void fit(Collection<? extends ObservationInteger> co, double[] weights) {
		if (co.isEmpty() || co.size() != weights.length)
			throw new IllegalArgumentException();

		Arrays.fill(probabilities, 0.);

		int i = 0;
		for (ObservationInteger o : co) {
			// if(Double.isNaN(weights[i])){
			// throw new IllegalStateException("weights[" + i + "] is NaN");
			//
			// }
			probabilities[o.value] += weights[i++];
		}
	}

	@Override
	public OpdfInteger clone() {
		try {
			OpdfInteger opdf = (OpdfInteger) super.clone();
			opdf.probabilities = probabilities.clone();
			return opdf;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError(e);
		}
	}

	@Override
	public String toString() {
		return toString(NumberFormat.getInstance());
	}

	@Override
	public String toString(NumberFormat numberFormat) {
		String s = "Integer distribution --- ";

		for (int i = 0; i < nbEntries();) {
			ObservationInteger oi = new ObservationInteger(i);

			s += numberFormat.format(probability(oi)) + ((++i < nbEntries()) ? " " : "");
		}

		return s;
	}

	private static final long serialVersionUID = 1L;
}
