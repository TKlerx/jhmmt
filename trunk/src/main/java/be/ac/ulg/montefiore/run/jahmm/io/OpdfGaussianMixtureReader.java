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

import be.ac.ulg.montefiore.run.jahmm.OpdfGaussian;
import be.ac.ulg.montefiore.run.jahmm.OpdfGaussianMixture;


/**
 * This class implements a {@link OpdfGaussian} reader.  The syntax of the
 * distribution description is the following.
 * <p>
 * The description always begins with the keyword <tt>GaussianMxitureOPDF</tt>.
 * Three series of numbers between brackets and separated by a space follow;
 * numbers are separated by a space. The first the gaussians mean values.  The
 * second is the gaussians variance.  The last sequence of number gives each
 * gaussian proportion.
 * <p>
 * For example, reading <br>
 * <tt>GaussianMixtureOPDF [ [ 1.2 2. ] [ .1 .9 ] [ .4 .6 ] ]</tt> returns a
 * distribution equivalent to <br>
 * <code>new OpdfGaussianMixture(new double[] { 1.2, 2. },
 * new double[] { .1, .9 }, new double[] { .4, .6 })</code>.
 */
public class OpdfGaussianMixtureReader
extends OpdfReader<OpdfGaussianMixture>
{
	String keyword()
	{
		return "GaussianMixtureOPDF";
	}

	
	public OpdfGaussianMixture read(StreamTokenizer st)
	throws IOException,	FileFormatException {
		HmmReader.readWords(st, keyword(), "[");
		
		double[] means = OpdfReader.read(st, -1);
		double[] variances = OpdfReader.read(st, means.length);
		double[] proportions = OpdfReader.read(st, means.length);
		
		HmmReader.readWords(st, "]");
		
		return new OpdfGaussianMixture(means, variances,
				proportions);
	}
}
