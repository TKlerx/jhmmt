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


/**
 * Implements an exception thrown when the CLI is given wrong arguments.
 */
public class WrongArgumentsException
extends AbnormalTerminationException
{	
	/**
	 * Creates an exception thrown when the CLI is given wrong arguments.
	 */
	public WrongArgumentsException()
	{
		super("Wrong arguments. Use 'Cli -help' for help.");
	}
	
	
	/**
	 * Creates an exception thrown when the CLI is given wrong arguments.
	 *
	 * @param s A string describing the problem.
	 */
	public WrongArgumentsException(String s)
	{
		super(s + ". Use 'Cli -help' for help.");
	}
	
	
	private static final long serialVersionUID = 1;
}

