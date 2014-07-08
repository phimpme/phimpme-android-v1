/*******************************************************************************
 * Copyright (c) 2010 Stefano Norcia.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl-3.html
 ******************************************************************************/
package com.joooid.android.xmlrpc;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public interface IXMLRPCSerializer {
	String TAG_NAME = "name";
	String TAG_MEMBER = "member";
	String TAG_VALUE = "value";
	String TAG_DATA = "data";
	
	String TYPE_INT = "int";
	String TYPE_I4 = "i4";
	String TYPE_I8 = "i8";
	String TYPE_DOUBLE = "double";
	String TYPE_BOOLEAN = "boolean";
	String TYPE_STRING = "string";
	String TYPE_DATE_TIME_ISO8601 = "dateTime.iso8601";
	String TYPE_BASE64 = "base64";
	String TYPE_ARRAY = "array";
	String TYPE_STRUCT = "struct";
	
	String DATETIME_FORMAT = "yyyyMMdd'T'HH:mm:ss";

	void serialize(XmlSerializer serializer, Object object) throws IOException;
	Object deserialize(XmlPullParser parser) throws XmlPullParserException, IOException;
}
