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

import be.ac.ulg.montefiore.run.jahmm.OpdfGaussian;


/**
 * This class implements a {@link OpdfGaussian} reader.  The syntax of the
 * distribution description is the following.
 * <p>
 * The description always begins with the keyword <tt>GaussianOPDF</tt>.
 * The next (resp. last) symbol is an opening (resp. closing) bracket.
 * Between the backets are two numbers separated by a space.  The
 * first is the distribution's mean, the second the variance.
 * <p>
 * For example, reading <tt>GaussianOPDF [ .2 .3 ]</tt> returns a distribution
 * equivalent to <code>new OpdfGaussian(.2, .3)</code>.
 */
public class OpdfGaussianReader
extends OpdfReader<OpdfGaussian>
{
	String keyword()
	{
		return "GaussianOPDF";
	}
	
	public OpdfGaussian read(StreamTokenizer st)
	throws IOException,	FileFormatException {
		HmmReader.readWords(st, keyword());
		
		double[] meanVariance = OpdfReader.read(st, 2);
		
		return new OpdfGaussian(meanVariance[0], meanVariance[1]);
	}
}
