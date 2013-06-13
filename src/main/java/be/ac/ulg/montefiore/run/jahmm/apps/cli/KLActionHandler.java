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

import be.ac.ulg.montefiore.run.jahmm.*;
import be.ac.ulg.montefiore.run.jahmm.apps.cli.CommandLineArguments.Arguments;
import be.ac.ulg.montefiore.run.jahmm.io.*;
import be.ac.ulg.montefiore.run.jahmm.toolbox.KullbackLeiblerDistanceCalculator;

/**
 * This class implements an action that computes the Kullback-Leibler
 * distance between two HMMs.
 */
public class KLActionHandler extends ActionHandler
{
	public void act() throws FileNotFoundException, IOException,
	FileFormatException, AbnormalTerminationException
	{
		EnumSet<Arguments> args = EnumSet.of(
				Arguments.OPDF,
				Arguments.IN_HMM,
				Arguments.IN_KL_HMM);
		CommandLineArguments.checkArgs(args);
		
		InputStream st = Arguments.IN_KL_HMM.getAsInputStream();
		Reader reader1 = new InputStreamReader(st);
		st = Arguments.IN_HMM.getAsInputStream();
		Reader reader2 = new InputStreamReader(st);
		
		distance(Types.relatedObjs(), reader1, reader2);
	}
	
	
	private <O extends Observation & CentroidFactory<O>> void
	distance(RelatedObjs<O> relatedObjs, Reader reader1, Reader reader2)
	throws IOException, FileFormatException
	{
		Hmm<O> hmm1 = HmmReader.read(reader1, relatedObjs.opdfReader());
		Hmm<O> hmm2 = HmmReader.read(reader2, relatedObjs.opdfReader());
		
		KullbackLeiblerDistanceCalculator kl = 
			new KullbackLeiblerDistanceCalculator();
		System.out.println(kl.distance(hmm1, hmm2));
	}
}
