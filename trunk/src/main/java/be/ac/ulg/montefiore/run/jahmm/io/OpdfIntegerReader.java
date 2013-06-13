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
import java.io.StreamTokenizer;

import be.ac.ulg.montefiore.run.jahmm.OpdfInteger;

/**
 * This class implements a {@link OpdfInteger} reader. The syntax of the
 * distribution description is the following.
 * <p>
 * The description always begins with the keyword <tt>IntegerOPDF</tt>. The
 * next (resp. last) symbol is an opening (resp. closing) bracket. Between the
 * backets is a list of numbers separated by a space. The <tt>i</tt>-th
 * number is the probability of <tt>i-1</tt>.
 * <p>
 * For example, reading <tt>IntegerOPDF [ .2 .3 .5 ]</tt> returns a
 * distribution equivalent to
 * <code>new OpdfInteger(new double[] { .2 .3 .5 })</code>.
 */
public class OpdfIntegerReader
extends OpdfReader<OpdfInteger>
{
	private final int nbEntries; // < 0 if number of entries is not checked.

	
	String keyword()
	{
		return "IntegerOPDF";
	}

	
	/**
	 * Implements a reader of distributions over integer observations.
	 */
	public OpdfIntegerReader()
	{
		nbEntries = -1;
	}
	

	/**
	 * Implements a reader of distributions over integer observations. The
	 * number of probabilities given is checked.
	 * 
	 * @param nbEntries
	 *            The number of entries that should be found (<i>i.e.</i> a
	 *            {@link FileFormatException} is triggered if the read
	 *            <code>opdf</code> is not such as
	 *            <code>opdf.nbEntries() == nbEntries</code> ).
	 */
	public OpdfIntegerReader(int nbEntries)
	{
		if (nbEntries <= 0)
			throw new IllegalArgumentException("Argument must be strictly "
					+ "positive");

		this.nbEntries = nbEntries;
	}

	
	public OpdfInteger read(StreamTokenizer st)
	throws IOException,	FileFormatException
	{
		HmmReader.readWords(st, keyword());
		
		double[] probabilities = OpdfReader.read(st, -1);
	
		if (nbEntries > 0 && probabilities.length != nbEntries)
			throw new FileFormatException(st.lineno(),
					"Invalid distribution (should " + "operate over 0..."
							+ (nbEntries - 1) + ")");
		
		return new OpdfInteger(probabilities);
	}
}
