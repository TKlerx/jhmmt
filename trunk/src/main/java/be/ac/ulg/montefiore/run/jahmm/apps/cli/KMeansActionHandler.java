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

package be.ac.ulg.montefiore.run.jahmm.apps.cli;

import java.io.*;
import java.util.EnumSet;
import java.util.List;

import be.ac.ulg.montefiore.run.jahmm.*;
import be.ac.ulg.montefiore.run.jahmm.apps.cli.CommandLineArguments.Arguments;
import be.ac.ulg.montefiore.run.jahmm.io.*;
import be.ac.ulg.montefiore.run.jahmm.learn.KMeansLearner;


/**
 * Applies the k-means learning algorithm.
 */
class KMeansActionHandler
extends ActionHandler
{
	public void act()
	throws FileNotFoundException, IOException, FileFormatException,
	AbnormalTerminationException
	{
		EnumSet<Arguments> args = EnumSet.of(
				Arguments.OPDF,
				Arguments.NB_STATES,
				Arguments.OUT_HMM,
				Arguments.IN_SEQ);
		CommandLineArguments.checkArgs(args);
		
		int nbStates = Arguments.NB_STATES.getAsInt();
		OutputStream outStream = Arguments.OUT_HMM.getAsOutputStream();
		Writer writer = new OutputStreamWriter(outStream);
		InputStream st = Arguments.IN_SEQ.getAsInputStream();
		Reader reader = new InputStreamReader(st);
		
		learn(nbStates, Types.relatedObjs(), reader, writer);
		
		writer.flush();
	}
	
	
	private <O extends Observation & CentroidFactory<O>> void
	learn(int nbStates, RelatedObjs<O> relatedObjs, Reader reader, 
			Writer writer)
	throws IOException, FileFormatException
	{
		OpdfFactory<? extends Opdf<O>> opdfFactory = relatedObjs.opdfFactory();
		List<List<O>> seqs = relatedObjs.readSequences(reader);
		OpdfWriter<? extends Opdf<O>> opdfWriter = relatedObjs.opdfWriter();
		
		KMeansLearner<O> kl = new KMeansLearner<O>(nbStates, opdfFactory,
				seqs);
		Hmm<O> hmm = kl.learn();
		
		HmmWriter.write(writer, opdfWriter, hmm);
	}
}
