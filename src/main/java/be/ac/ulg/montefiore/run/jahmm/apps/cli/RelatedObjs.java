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
package be.ac.ulg.montefiore.run.jahmm.apps.cli;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import be.ac.ulg.montefiore.run.jahmm.*;
import be.ac.ulg.montefiore.run.jahmm.io.*;
import be.ac.ulg.montefiore.run.jahmm.toolbox.MarkovGenerator;


/**
 * This class collects all the objects related to a specific observation
 * type.
 */
public interface RelatedObjs<O extends Observation & CentroidFactory<O>>
{
	public ObservationReader<O> observationReader();
	public ObservationWriter<O> observationWriter();
	public OpdfFactory<? extends Opdf<O>> opdfFactory();
	public OpdfReader<? extends Opdf<O>> opdfReader();
	public OpdfWriter<? extends Opdf<O>> opdfWriter();
	public List<List<O>> readSequences(Reader reader)
	throws FileFormatException, IOException;
	public MarkovGenerator<O> generator(Hmm<O> hmm);
}
