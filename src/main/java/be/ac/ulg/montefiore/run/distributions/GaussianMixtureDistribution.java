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

import org.apache.commons.lang3.SerializationUtils;


/**
 * This class implements a Gaussian mixture distribution.
 *
 * @author Jean-Marc Francois (based on code from Benjamin Chung)
 */
public class GaussianMixtureDistribution
implements RandomDistribution
{	
	static private final Random random = new Random();
	
	private GaussianDistribution[] distributions;
	private double proportions[];
	
	
	/**
	 * Creates a new pseudo-random, Gaussian mixture distribution.  It is
	 * made of Gaussian distributions evently distributed between 0 and 1 with
	 * a unitary variance.
	 *
	 * @param nbGaussians The number of distributions composing the mixture.
	 */
	public GaussianMixtureDistribution(int nbGaussians)
	{
		if (nbGaussians <= 0)
			throw new IllegalArgumentException("Argument must be strictly " +
					"positive");
		
		distributions = new GaussianDistribution[nbGaussians];
		proportions = new double[nbGaussians];
		double means[] = new double[nbGaussians];
		double variances[] = new double[nbGaussians];
		
		for (int i = 0; i < nbGaussians; i++)
			means[i] = (1. + 2. * (double) i) / (2. * (double) nbGaussians);
		
		Arrays.fill(variances, 1.);
		Arrays.fill(proportions, 1. / ((double) nbGaussians));
		
		for (int i = 0; i < distributions.length; i++)
			distributions[i] = new GaussianDistribution(means[i], variances[i]);
	}
	
	
	/**
	 * Creates a new pseudo-random, Gaussian mixture distribution.  The mean
	 * values, variances and proportions of each distribution is given as
	 * an argument.
	 *
	 * @param means The mean values of the Gaussian distributions.
	 * @param variances The variances of the Gaussian distributions.
	 * @param proportions The mixing proportions. This array does not have to
	 *             be normalized, but each element must be positive and the sum
	 *             of its elements must be strictly positive.
	 */
	public GaussianMixtureDistribution(double[] means, double[] variances,
			double[] proportions)
	{
		if (means.length == 0 || means.length != variances.length ||
				means.length != proportions.length)
			throw new IllegalArgumentException();
		
		distributions = new GaussianDistribution[means.length];
		this.proportions = new double[means.length];
		
		for (int i = 0; i < distributions.length; i++)
			distributions[i] = new GaussianDistribution(means[i], variances[i]);
		
		double sum = 0.;
		for (int i = 0; i < proportions.length; i++)
			sum += proportions[i];
		
		for (int i = 0; i < proportions.length; i++)
			this.proportions[i] = proportions[i] / sum;
	}
	
	
	/**
	 * Returns the number of Gaussians composing this mixture.
	 *
	 * @return The number of Gaussians composing this mixture.
	 */
	public int nbGaussians()
	{
		return distributions.length;
	}
	
	
	/**
	 * Returns the distributions composing this mixture.
	 *
	 * @return A copy of the distributions array.
	 */
	public GaussianDistribution[] distributions()
	{
		return SerializationUtils.clone(distributions);
	}
	
	
	/**
	 * Returns the proportions of the distributions in this mixture.
	 * The sum of the proportions equals 1.
	 *
	 * @return A copy of the distributions' proportions array.
	 */
	public double[] proportions() 
	{
		return proportions.clone();
	}
	
	
	public double generate() 
	{
		double r = random.nextDouble();
		double sum = 0.;	
		
		for (int i = 0; i < proportions.length; i++) {
			sum += proportions[i];
			
			if (r <= sum)
				return distributions[i].generate();
		}
		
		throw new RuntimeException("Internal error");
	}
	
	
	public double probability(double n)
	{
		double sum = 0.;
		
		for (int i = 0; i < distributions.length; i++)
			sum += distributions[i].probability(n) * proportions[i];
		
		return sum;
	}
	
	
	private static final long serialVersionUID = 2634624658500627331L;
}
