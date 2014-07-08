/*******************************************************************************
 * Copyright (c) 2010 Stefano Norcia.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl-3.html
 ******************************************************************************/
package com.joooid.android.xmlrpc;

import java.util.ArrayList;

public class MethodCall {

	private static final int TOPIC = 1;
	String methodName;
	ArrayList<Object> params = new ArrayList<Object>();
	
	public String getMethodName() { return methodName; }
	void setMethodName(String methodName) { this.methodName = methodName; }

	public ArrayList<Object> getParams() { return params; }
	void setParams(ArrayList<Object> params) { this.params = params; }

	public String getTopic() {
		return (String)params.get(TOPIC);
	}
}
