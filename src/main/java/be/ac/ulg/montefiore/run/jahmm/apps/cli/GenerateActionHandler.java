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
import java.util.*;

import be.ac.ulg.montefiore.run.jahmm.*;
import be.ac.ulg.montefiore.run.jahmm.apps.cli.CommandLineArguments.Arguments;
import be.ac.ulg.montefiore.run.jahmm.io.*;
import be.ac.ulg.montefiore.run.jahmm.toolbox.MarkovGenerator;


/**
 * Generates observation sequences from a HMM and write it to file.
 */
class GenerateActionHandler
extends ActionHandler
{
	public void act()
	throws FileNotFoundException, IOException, FileFormatException,
	AbnormalTerminationException
	{
		EnumSet<Arguments> args = EnumSet.of(
				Arguments.OPDF,
				Arguments.OUT_SEQS,
				Arguments.IN_HMM);
		CommandLineArguments.checkArgs(args);
		
		InputStream hmmStream = Arguments.IN_HMM.getAsInputStream();
		Reader hmmFileReader = new InputStreamReader(hmmStream);
		OutputStream seqsStream = Arguments.OUT_SEQS.getAsOutputStream();
		Writer seqsFileWriter = new OutputStreamWriter(seqsStream);
		
		write(hmmFileReader, seqsFileWriter, Types.relatedObjs());
		
		seqsFileWriter.flush();
	}
	
	
	private <O extends Observation & CentroidFactory<O>> void
	write(Reader hmmFileReader, Writer seqsFileWriter,
			RelatedObjs<O> relatedObjs)
	throws IOException, FileFormatException
	{
		ObservationWriter<O> obsWriter = relatedObjs.observationWriter();
		OpdfReader<? extends Opdf<O>> opdfReader = relatedObjs.opdfReader();
		Hmm<O> hmm = HmmReader.read(hmmFileReader, opdfReader);
		
		MarkovGenerator<O> generator = relatedObjs.generator(hmm);
		
		List<List<O>> seqs = new ArrayList<List<O>>();
		for (int i = 0; i < 100; i++)
			seqs.add(generator.observationSequence(1000));
		
		ObservationSequencesWriter.write(seqsFileWriter, obsWriter, seqs);
	}
}
