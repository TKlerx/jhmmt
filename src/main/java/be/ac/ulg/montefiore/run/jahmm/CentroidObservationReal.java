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

import java.util.*;


/**
 * This class represents the centroid of a set of {@link ObservationReal
 * ObservationReal}.
 */
public class CentroidObservationReal
implements Centroid<ObservationReal>
{	
	private double value;
	
	
	public CentroidObservationReal(ObservationReal o)
	{
		this.value = o.value;
	}
	
	
	public void reevaluateAdd(ObservationReal e,
			List<? extends ObservationReal> v)
	{
		value =	(value * (double) v.size() + e.value) / (v.size()+1.);
	}
	
	
	public void reevaluateRemove(ObservationReal e,
			List<? extends ObservationReal> v)
	{
		value = ((value * (double) v.size()) - e.value) / (v.size()-1.);
	}
	
	
	/**
	 * Returns the distance from this centroid to a given element.
	 * This distance is the absolute value of the difference between the
	 * value of this centroid and the value of the argument.
	 * 
	 * @param e The element, which must be an {@link ObservationReal
	 *          ObservationReal}.
	 * @return The distance to the centroid.
	 */
	public double distance(ObservationReal e)
	{
		return Math.abs(e.value - value);
	}
}
