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
package be.ac.ulg.montefiore.run.jahmm.io;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.List;

import be.ac.ulg.montefiore.run.jahmm.Opdf;


/**
 * Reads an observation distribution textual description.
 */
public abstract class OpdfReader<O extends Opdf<?>>
{	
	/**
	 * Returns the keyword identifying the distribution read.
	 * It must be the word beginning the distribution's description.
	 * 
	 * @return The keyword.
	 */
	abstract String keyword();
	
	
	/**
	 * Reads an
	 * {@link be.ac.ulg.montefiore.run.jahmm.Opdf Opdf} out of a
	 * {@link java.io.StreamTokenizer}.
	 * <p>
	 * The stream tokenizer syntax table must be set according to
	 * of <code>HmmReader.initSyntaxTable(StreamTokenizer)
	 * </code> before the call to this method and reset to this state if
	 * modified before it returns.
	 *
	 * @param st A stream tokenizer.
	 * @return An Opdf.
	 */
	public abstract O read(StreamTokenizer st)
	throws IOException, FileFormatException;
	
	
	/**
	 * Reads a sequence of numbers.  The sequence is between brackets
	 * and numbers are separated by spaces.  Empty array are not allowed.
	 * 
	 * @param st The tokenizer to read the sequence from.
	 * @param length The expected length of the sequence or a strictly negative
	 *        number if it must not be checked.
	 * @return The array read.
	 */
	static protected double[] read(StreamTokenizer st, int length)
	throws IOException, FileFormatException
	{
		List<Double> l = new ArrayList<Double>();
		HmmReader.readWords(st, "[");
		while (st.nextToken() == StreamTokenizer.TT_NUMBER)
			l.add(st.nval);
		st.pushBack();
		HmmReader.readWords(st, "]");

		if (length >= 0 && l.size() != length)
			throw new FileFormatException(st.lineno(),
					"Wrong length of number sequence");
		
		if (l.size() == 0)
			throw new FileFormatException(st.lineno(),
					"Invalid empty sequence");
		
		double[] a = new double[l.size()];
		for (int i = 0; i < a.length; i++)
			a[i] = l.get(i);
		
		return a;
	}
}
