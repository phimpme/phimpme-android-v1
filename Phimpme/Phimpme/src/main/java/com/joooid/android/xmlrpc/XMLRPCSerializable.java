/*******************************************************************************
 * Copyright (c) 2010 Stefano Norcia.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl-3.html
 ******************************************************************************/
package com.joooid.android.xmlrpc;

/**
 * Allows to pass any XMLRPCSerializable object as input parameter.
 * When implementing getSerializable() you should return 
 * one of XMLRPC primitive types (or another XMLRPCSerializable: be careful not going into
 * recursion by passing this object reference!)  
 */
public interface XMLRPCSerializable {
	
	/**
	 * Gets XMLRPC serialization object
	 * @return object to serialize This object is most likely one of XMLRPC primitive types,
	 * however you can return also another XMLRPCSerializable
	 */
	Object getSerializable();
}
