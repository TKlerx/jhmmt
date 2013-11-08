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

import java.io.*;

import be.ac.ulg.montefiore.run.jahmm.io.FileFormatException;


/**
 * This class implements a command line interface for the Jahmm library.
 */
public class Cli
{
    public final static String CHARSET = "ISO-8859-1";

    /**
	 * The entry point of the CLI.
	 * 
	 * @param args Command line arguments.
	 */
	public static void main(String... args)
	throws IOException
	{
		try {
			System.exit(run(args));
		}
		catch (AbnormalTerminationException e) {
			System.err.println(e);
			System.exit(-1);
		}
	}
	
	
	static public int run(String... args)
	throws IOException, AbnormalTerminationException
	{
		// Allows this method to be called more than once
		CommandLineArguments.reset();
		
		ActionHandler.Actions action = CommandLineArguments.parseAction(args);
		if (action  == null)
			throw new WrongArgumentsException("Valid action required");
		
		ActionHandler actionHandler = null;
		
		try {
			actionHandler = action.handler().newInstance();
		} catch(Exception e) {
			throw new InternalError(e.toString());
		}
		
		actionHandler.parseArguments(args);
		
		try {
			actionHandler.act();
		} catch(FileNotFoundException e) {
			System.err.println(e);
			return -1;
		} catch(FileFormatException e) {
			System.err.println(e);
			return -1;
		}
		
		return 0;
	}
}			
