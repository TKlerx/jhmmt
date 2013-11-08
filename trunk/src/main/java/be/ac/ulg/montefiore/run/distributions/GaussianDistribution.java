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

import java.util.*;


/**
 * This class implements a Gaussian distribution.
 */
public class GaussianDistribution
implements RandomDistribution {
	
	private double mean;
	private double deviation;
	private double variance;
	private final static Random randomGenerator = new Random();
	
	
	/**
	 * Creates a new pseudo-random, Gaussian distribution with zero mean
	 * and unitary variance.
	 */
	public GaussianDistribution()
	{
		this(0., 1.);
	}
	
	
	/**
	 * Creates a new pseudo-random, Gaussian distribution.
	 *
	 * @param mean The mean value of the generated numbers.
	 * @param variance The variance of the generated numbers.
	 */
	public GaussianDistribution(double mean, double variance)
	{
		if (variance <= 0.)
			throw new IllegalArgumentException("Variance must be positive");
		
		this.mean = mean;
		this.variance = variance;
		this.deviation = Math.sqrt(variance);
	}
	
	
	/**
	 * Returns this distribution's mean value.
	 *
	 * @return This distribution's mean value.
	 */
	public double mean()
	{
		return mean;
	}
	
	
	/**
	 * Returns this distribution's variance.
	 *
	 * @return This distribution's variance.
	 */
	public double variance()
	{
		return variance;
	}
	
	
	public double generate()
	{
		return randomGenerator.nextGaussian() * deviation + mean;
	}
	
	
	public double probability(double n)
	{
		double expArg = -.5 * (n - mean) * (n - mean) / variance;
		return Math.pow(2. * Math.PI * variance, -.5) *
		Math.exp(expArg);
	}
	
	
	private static final long serialVersionUID = 9127329839769283975L;
}
