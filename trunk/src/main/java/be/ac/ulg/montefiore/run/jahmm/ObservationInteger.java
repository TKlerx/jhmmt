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
package be.ac.ulg.montefiore.run.jahmm;

import java.text.*;


/**
 * This class holds an integer observation.
 */
public class ObservationInteger extends Observation
implements CentroidFactory<ObservationInteger>
{	
	/**
	 * The observation's value.
	 */
	final public int value;
	
	
	/**
	 * An observation that can be described by an integer.
	 *
	 * @param value The value of this observation.
	 */
	public ObservationInteger(int value)
	{
		this.value = value;
	}
	
	
	/**
	 * Returns the centroid matching this observation.
	 *
	 * @return The corresponding observation.
	 */
	public Centroid<ObservationInteger> factor()
	{
		return new CentroidObservationInteger(this);
	}	

	
	public String toString(NumberFormat numberFormat)
	{
		return numberFormat.format(value);
	}
}
