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
package be.ac.ulg.montefiore.run.distributions;


/**
 * This class implements an generator of exponentially distributed reals.
 */
public class ExponentialDistribution
implements RandomDistribution
{
	private double rate;
	
	
	/**
	 * Creates a new pseudo-random, exponential distribution which is the
	 * distribution of waiting times between two events of a Poisson
	 * distribution with rate <code>rate</code>.  The mean value of this
	 * distribution is <code>rate<sup>-1</sup></code>.
	 *
	 * @param rate The parameter of the distribution.
	 */
	public ExponentialDistribution(double rate)
	{
		if (rate <= 0.)
			throw new IllegalArgumentException("Argument must be strictly " +
					"positive");
		
		this.rate = rate;
	}
	
	
	/**
	 * Returns this distribution's rate.
	 *
	 * @return This distribution's rate.
	 */
	public double rate()
	{
		return rate;
	}
	
	
	public double generate()
	{
		return -Math.log(Math.random()) / rate;
	}
	
	
	public double probability(double n)
	{
		return rate * Math.exp(-n * rate);
	}

	
	private static final long serialVersionUID = 6359607459925864639L;
}
