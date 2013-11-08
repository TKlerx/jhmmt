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

import java.io.IOException;
import java.io.StreamTokenizer;

import be.ac.ulg.montefiore.run.jahmm.Observation;


/**
 * Reads an observation up to (and including) the semi-colon.
 * <p>
 * The syntax of each observation must follow the rules given in
 * {@link ObservationSequencesReader ObservationSequencesReader} (<i>e.g.</i> 
 * the backslash character is only used to escape a new line and can't appear
 * in an observation).
 */
public abstract class ObservationReader<O extends Observation>
{	
	/**
	 * Reads an
	 * {@link be.ac.ulg.montefiore.run.jahmm.Observation Observation} (followed
	 * by a semi-colon) out of a {@link java.io.StreamTokenizer
	 * StreamTokenizer}.
	 * <p>
	 * The stream tokenizer syntax table must be set according to
	 * of <code>ObservationSequencesReader.initSyntaxTable(StreamTokenizer)
	 * </code> before the call to this method and reset to this state when it
	 * returns.
	 *
	 * @param st A stream tokenizer.
	 * @return An ObservationInteger.
	 */
	public abstract O read(StreamTokenizer st)
	throws IOException, FileFormatException;
}
