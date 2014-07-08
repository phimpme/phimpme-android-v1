/*******************************************************************************
 * Copyright (c) 2010 Stefano Norcia.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl-3.html
 ******************************************************************************/
package com.joooid.android.xmlrpc;

public class XMLRPCException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7499675036625522379L;

	public XMLRPCException(Exception e) {
		super(e);
	}

	public XMLRPCException(String string) {
		super(string);
	}
}
