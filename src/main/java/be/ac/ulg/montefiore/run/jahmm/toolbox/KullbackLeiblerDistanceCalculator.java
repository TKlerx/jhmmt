/*******************************************************************************
 * Copyright (c) 2004-2009, Jean-Marc François. All Rights Reserved.
 * Originally licensed under the New BSD license.  See the LICENSE_OLD file.
 * Copyright (c) 2013, Timo Klerx. All Rights Reserved.
 * Now licensed uder LGPL. See the LICENSE file.
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

package be.ac.ulg.montefiore.run.jahmm.toolbox;

import java.util.List;

import be.ac.ulg.montefiore.run.jahmm.*;

/**
 * Computes the distance between HMMs.
 * <p>
 * The distance metric is similar to the Kullback-Leibler distance defined on
 * distributions. More information can be found in <i>A Probabilistic Distance
 * Measure For HMMs</i> by <i>Rabiner</i> and <i>Juang</i> (AT&T Technical
 * Journal, vol. 64, Feb. 1985, pages 391-408).
 * <p>
 * This distance measure is not symetric: <code>distance(hmm1, hmm2)</code> is
 * not necessary equal to <code>distance(hmm2, hmm1)</code>. To get a symetric
 * distance definition, compute
 * <code>(distance(hmm1, hmm2) + distance(hmm2, hmm1)) / 2</code>.
 */
public class KullbackLeiblerDistanceCalculator {
	private int sequencesLength = 1000;
	private int nbSequences = 10;

	/**
	 * Computes the Kullback-Leibler distance between two HMMs.
	 * 
	 * @param hmm1
	 *            The first HMM against which the distance is computed. The
	 *            distance is mesured with regard to this HMM (this must be
	 *            defined since the Kullback-Leibler distance is not symetric).
	 * @param hmm2
	 *            The second HMM against which the distance is computed.
	 * @return The distance between <code>hmm1</code> and <code>hmm2</code> with
	 *         regard to <code>hmm1</code>
	 */
	public <O extends Observation> double distance(Hmm<O> hmm1, Hmm<? super O> hmm2) {
		double distance = 0.;

		for (int i = 0; i < nbSequences; i++) {

			List<O> oseq = new MarkovGenerator<O>(hmm1).observationSequence(sequencesLength);
			double ln1 = new ForwardBackwardScaledCalculator(oseq, hmm1).lnProbability();
			if (Double.isNaN(ln1)) {
				System.err.println("ln1 is NaN for nbSequence=" + i);
				System.err.println("ObservationSequence is: " + oseq.toString());
			}
			double ln2 = new ForwardBackwardScaledCalculator(oseq, hmm2).lnProbability();
			if (Double.isNaN(ln2)) {
				System.err.println("ln2 is NaN for nbSequence=" + i);
				System.err.println("ObservationSequence is: " + oseq.toString());
			}
			// System.out.println("ln1="+ln1);
			// System.out.println("ln2="+ln2);
			distance += (ln1 - ln2) / sequencesLength;
			if(Double.isNaN(distance)){
				System.err.println("distance became NaN");
			}
		}
		// System.out.println(distance);
		// System.out.println(nbSequences);
		return distance / nbSequences;
	}

	/**
	 * Returns the number of sequences generated to estimate a distance.
	 * 
	 * @return The number of generated sequences.
	 */
	public int getNbSequences() {
		return nbSequences;
	}

	/**
	 * Sets the number of sequences generated to estimate a distance.
	 * 
	 * @param nb
	 *            The number of generated sequences.
	 */
	public void setNbSequences(int nb) {
		this.nbSequences = nb;
	}

	/**
	 * Returns the length of sequences generated to estimate a distance.
	 * 
	 * @return The sequences length.
	 */
	public int getSequencesLength() {
		return sequencesLength;
	}

	/**
	 * Sets the length of sequences generated to estimate a distance.
	 * 
	 * @param length
	 *            The sequences length.
	 */
	public void setSequencesLength(int length) {
		this.sequencesLength = length;
	}
}
