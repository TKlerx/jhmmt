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

import java.util.*;
import java.io.*;

import be.ac.ulg.montefiore.run.jahmm.*;


/**
 * This class can write a set of observation sequences to a
 * {@link java.io.Writer Writer}.
 * <p>
 * The sequences written using this file can be read using the
 * {@link ObservationSequencesReader ObservationSequencesReader} class.
 */
public class ObservationSequencesWriter
{
    /**
     * Writes a set of sequences to file.
     *
     * @param writer The writer to write to. It should use the "US-ASCII"
     *               character set.
     * @param ow The observation writer used to generate the observations. 
     * @param sequences The set of observation sequences.
     */
	static public <O extends Observation> void 
	write(Writer writer, ObservationWriter<? super O> ow,
			List<? extends List<O>> sequences)
	throws IOException
	{
		for (List<O> s : sequences)
			write(s, ow, writer);
	}
	
	
	/* 
	 * Writes the sequence 'sequence' to the writer 'writer' using the
	 * observation writer 'ow'.
	 */
	static <O extends Observation> void
	write(List<O> sequence, ObservationWriter<? super O> ow, Writer writer) 
	throws IOException
	{
		for (O o : sequence) 
			ow.write(o, writer);
		
		writer.write("\n");
    }
}
