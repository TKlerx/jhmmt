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

import java.io.*;


/** 
 * This interface must be implemented by all the package's classes implementing
 * a discrete random distribution.  Distributions are not mutable.
 */
public interface DiscreteDistribution 
extends Serializable
{    
    /**
     * Generates a pseudo-random number.  The numbers generated by this function
     * are drawn according to the pseudo-random distribution described by the
     * object that implements it.
     *
     * @return A pseudo-random number.
     */
    public int generate();


    /**
     * Returns the probability of a given number.
     *
     * @param n An integer.
     */
    public double probability(int n);
}
