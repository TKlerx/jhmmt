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

package be.ac.ulg.montefiore.run.jahmm;


/**
 * This class can build <code>OpdfMultiGaussian</code> observation probability 
 * functions.
 */
public class OpdfMultiGaussianFactory 
implements OpdfFactory<OpdfMultiGaussian>
{	
	private int dimension;
	
	
	/**
	 * Generates a new multivariate gaussian observation probability 
	 * distribution function.
	 * 
	 * @param dimension The dimension of the vectors generated by this
	 *                  object.
	 */
	public OpdfMultiGaussianFactory(int dimension)
	{
		this.dimension = dimension;
	}
	
	
	public OpdfMultiGaussian factor()
	{
		return new OpdfMultiGaussian(dimension);
	}
}
