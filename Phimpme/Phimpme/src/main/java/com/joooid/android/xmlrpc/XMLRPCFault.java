/*******************************************************************************
 * Copyright (c) 2010 Stefano Norcia.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl-3.html
 ******************************************************************************/
package com.joooid.android.xmlrpc;

public class XMLRPCFault extends XMLRPCException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5676562456612956519L;
	private String faultString;
	private int faultCode;

	public XMLRPCFault(String faultString, int faultCode) {
		super("XMLRPC Fault: " + faultString + " [code " + faultCode + "]");
		this.faultString = faultString;
		this.faultCode = faultCode;
	}
	
	public String getFaultString() {
		return faultString;
	}
	
	public int getFaultCode() {
		return faultCode;
	}
}
