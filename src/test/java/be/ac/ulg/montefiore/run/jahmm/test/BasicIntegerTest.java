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

package be.ac.ulg.montefiore.run.jahmm.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import be.ac.ulg.montefiore.run.jahmm.*;


public class BasicIntegerTest 
extends TestCase
{
	final static private double DELTA = 1.E-10;
	
	private Hmm<ObservationInteger> hmm;
	private List<ObservationInteger> sequence;
	private List<ObservationInteger> randomSequence;
	
	
	protected void setUp()
	{ 
		hmm = new Hmm<ObservationInteger>(5, new OpdfIntegerFactory(10));
		hmm.setOpdf(1, new OpdfInteger(6));
		
		sequence = new ArrayList<ObservationInteger>();
		for (int i = 0; i < 5; i++)
			sequence.add(new ObservationInteger(i));
		
		randomSequence = new ArrayList<ObservationInteger>();
		for (int i = 0; i < 30000; i++)
			randomSequence.
			add(new ObservationInteger((int) (Math.random()*10.)));
	}
	
	
	public void testForwardBackward()
	{	
		ForwardBackwardCalculator fbc = 
			new ForwardBackwardCalculator(sequence, hmm);
		
		assertEquals(1.8697705349794245E-5, fbc.probability(), DELTA);
		
		ForwardBackwardScaledCalculator fbsc =
			new ForwardBackwardScaledCalculator(sequence, hmm);
		
		assertEquals(1.8697705349794245E-5, fbsc.probability(), DELTA);
	}
	
	
	public void testViterbi()
	{	
		ViterbiCalculator vc = new ViterbiCalculator(sequence, hmm);
		
		assertEquals(4.1152263374485705E-8, 
				Math.exp(vc.lnProbability()), DELTA);
	}
	
	
	public void testKMeansCalculator()
	{	
		int nbClusters = 20;
		
		KMeansCalculator<ObservationInteger> kmc = new
		KMeansCalculator<ObservationInteger>(nbClusters, randomSequence);
		
		assertEquals("KMeans did not produce expected number of clusters",
				nbClusters, kmc.nbClusters());
	}
}
