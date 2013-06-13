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

import be.ac.ulg.montefiore.run.jahmm.CentroidFactory;
import be.ac.ulg.montefiore.run.jahmm.Hmm;
import be.ac.ulg.montefiore.run.jahmm.Observation;
import be.ac.ulg.montefiore.run.jahmm.Opdf;
import be.ac.ulg.montefiore.run.jahmm.apps.cli.CommandLineArguments.Arguments;
import be.ac.ulg.montefiore.run.jahmm.io.*;
import be.ac.ulg.montefiore.run.jahmm.learn.BaumWelchLearner;
import be.ac.ulg.montefiore.run.jahmm.learn.BaumWelchScaledLearner;

import java.io.*;
import java.util.EnumSet;
import java.util.List;


/**
 * Applies the Baum-Welch learning algorithm.
 */
class BWActionHandler
extends ActionHandler
{
	public void act()
	throws IOException, FileFormatException, 
	AbnormalTerminationException
	{
		EnumSet<Arguments> args = EnumSet.of(
				Arguments.OPDF,
				Arguments.OUT_HMM,
				Arguments.IN_HMM,
				Arguments.IN_SEQ,
				Arguments.NB_ITERATIONS);
		CommandLineArguments.checkArgs(args);
		
		int nbIterations = Arguments.NB_ITERATIONS.getAsInt();
		OutputStream outStream = Arguments.OUT_HMM.getAsOutputStream();
		Writer hmmWriter = new OutputStreamWriter(outStream);
		InputStream hmmStream = Arguments.IN_HMM.getAsInputStream();
		InputStream seqStream = Arguments.IN_SEQ.getAsInputStream();
		Reader hmmReader = new InputStreamReader(hmmStream, Cli.CHARSET);
		Reader seqReader = new InputStreamReader(seqStream, Cli.CHARSET);
		
		learn(Types.relatedObjs(), hmmReader, seqReader, hmmWriter,
				nbIterations);
		
		hmmWriter.flush();
	}
	
	
	private <O extends Observation & CentroidFactory<O>> void
	learn(RelatedObjs<O> relatedObjs, Reader hmmFileReader,
			Reader seqFileReader, Writer hmmFileWriter,
			int nbIterations)
	throws IOException, FileFormatException
	{
		List<List<O>> seqs = relatedObjs.readSequences(seqFileReader);
		OpdfReader<? extends Opdf<O>> opdfReader = relatedObjs.opdfReader();
		OpdfWriter<? extends Opdf<O>> opdfWriter = relatedObjs.opdfWriter();
		
		Hmm<O> initHmm = HmmReader.read(hmmFileReader, opdfReader);
		BaumWelchLearner bw = new BaumWelchScaledLearner();
		bw.setNbIterations(nbIterations);
		Hmm<O> hmm = bw.learn(initHmm, seqs);
		HmmWriter.write(hmmFileWriter, opdfWriter, hmm);
	}
}
