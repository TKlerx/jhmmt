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


import java.io.FileNotFoundException;
import java.io.IOException;

import be.ac.ulg.montefiore.run.jahmm.io.FileFormatException;

abstract class ActionHandler
{
	public static enum Actions {
		HELP("-help", HelpActionHandler.class),
		PRINT("print", PrintActionHandler.class),
		CREATE("create", CreateActionHandler.class),
		BW("learn-bw", BWActionHandler.class),
		KMEANS("learn-kmeans", KMeansActionHandler.class),
		GENERATE("generate", GenerateActionHandler.class),
		KL("distance-kl", KLActionHandler.class);
		
		private String argument;
		private Class<? extends ActionHandler> handler;
		
		Actions(String argument, Class<? extends ActionHandler> handler) {
			this.argument = argument;
			this.handler = handler;
		}
		
		public String toString() {
			return argument;
		}
		
		public Class<? extends ActionHandler> handler() {
			return handler;
		}
	};

	
	public void parseArguments(String args[])
	throws WrongArgumentsException
	{
		CommandLineArguments.parse(args);
	}

	
	abstract public void act()
	throws FileNotFoundException, IOException, FileFormatException,
	AbnormalTerminationException;
}
