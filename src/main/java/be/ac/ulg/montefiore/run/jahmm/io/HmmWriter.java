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

package be.ac.ulg.montefiore.run.jahmm.io;

import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.Locale;

import be.ac.ulg.montefiore.run.jahmm.Hmm;
import be.ac.ulg.montefiore.run.jahmm.Observation;
import be.ac.ulg.montefiore.run.jahmm.Opdf;

/**
 * Writes a HMM to a text file compatible with {@link HmmReader}.
 */
public class HmmWriter {
	
	static{
		Locale.setDefault(Locale.ENGLISH);
	}

	/**
	 * Writes a HMM description.
	 * 
	 * @param writer
	 *            The writer to write the HMM to.
	 * @param opdfWriter
	 *            The writer used to convert the observation's distributions of
	 *            the HMMs.
	 * @param hmm
	 *            The HMM to write.
	 */
	static public <O extends Observation> void write(Writer writer, OpdfWriter<? extends Opdf<O>> opdfWriter, Hmm<O> hmm) throws IOException {
		writer.write("Hmm v1.0\n\nNbStates " + hmm.nbStates() + "\n\n");

		for (int i = 0; i < hmm.nbStates(); i++) {
			writeState(writer, opdfWriter, hmm, i);
		}
		writer.flush();
	}

	@SuppressWarnings("unchecked")
	// Cannot guarantee type safety
	static private <O extends Observation, D extends Opdf<O>> void writeState(Writer writer, OpdfWriter<D> opdfWriter, Hmm<O> hmm, int stateNb)
			throws IOException {
//		NumberFormat formatter = NumberFormat.getInstance();
		
		
		DecimalFormat formatter = new DecimalFormat("#0.######");
		writer.write("State\nPi " + formatter.format(hmm.getPi(stateNb)));

		writer.write("\nA ");
		for (int i = 0; i < hmm.nbStates(); i++)
			writer.write(formatter.format(hmm.getAij(stateNb, i)) + " ");
		writer.write("\n");

		D opdf = (D) hmm.getOpdf(stateNb);
		opdfWriter.write(writer, opdf);
		writer.write("\n\n");
	}
}
