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
import java.io.Writer;
import java.text.DecimalFormat;

import be.ac.ulg.montefiore.run.jahmm.ObservationInteger;
import be.ac.ulg.montefiore.run.jahmm.OpdfInteger;

/**
 * This class can write a textual description of an {@link OpdfInteger}.
 * It is compatible with {@link OpdfIntegerReader}.
 */
public class OpdfIntegerWriter
extends OpdfWriter<OpdfInteger>
{
	public void write(Writer writer, OpdfInteger opdf)
	throws IOException
	{
		String s = "IntegerOPDF [";
		DecimalFormat formatter = new DecimalFormat("#0.######");
		for (int i = 0; i < opdf.nbEntries(); i++)
			s += formatter.format(opdf.probability(new ObservationInteger(i))) + " ";
			
		writer.write(s + "]\n");
	}
}
