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

import java.io.*;

import be.ac.ulg.montefiore.run.jahmm.Hmm;


/**
 * This class can write a Hidden Markov Models to a byte stream.
 * <p>
 * The HMM objects are simply serialized.  HMMs could thus be unreadable using
 * a different release of this library.
 */
public class HmmBinaryWriter
{	
	/**
	 * Writes a HMM to byte stream.
	 *
	 * @param stream Holds the byte stream the HMM is written to.
	 */
	static public void write(OutputStream stream, Hmm<?> hmm)
	throws IOException
	{		
		ObjectOutputStream oos = new ObjectOutputStream(stream);	
		oos.writeObject(hmm);
	}
}
