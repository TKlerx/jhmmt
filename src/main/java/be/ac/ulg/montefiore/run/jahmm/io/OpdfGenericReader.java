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

import be.ac.ulg.montefiore.run.jahmm.Opdf;

public class OpdfGenericReader
extends OpdfReader<Opdf<?>>
{
	String keyword()
	{
		throw new AssertionError("Cannot call method");
	}
	
	
	public Opdf<?> read(StreamTokenizer st)
	throws IOException, FileFormatException
	{
		if (st.nextToken() != StreamTokenizer.TT_WORD)
			throw new FileFormatException("Keyword expected");
		
		for (OpdfReader r : new OpdfReader[] {
				new OpdfIntegerReader(),
				new OpdfGaussianReader(),
				new OpdfGaussianMixtureReader(),
				new OpdfMultiGaussianReader() })
			if (r.keyword().equals(st.sval)) {
				st.pushBack();
				return r.read(st);
			}
		
		throw new FileFormatException("Unknown distribution");
	}
}
