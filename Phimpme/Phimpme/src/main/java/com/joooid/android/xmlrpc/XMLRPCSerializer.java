/*******************************************************************************
 * Copyright (c) 2010 Stefano Norcia.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl-3.html
 ******************************************************************************/
package com.joooid.android.xmlrpc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

//import android.util.Log;

class XMLRPCSerializer implements IXMLRPCSerializer {
	static SimpleDateFormat dateFormat = new SimpleDateFormat(DATETIME_FORMAT);

	@Override
	@SuppressWarnings("unchecked")
	public void serialize(XmlSerializer serializer, Object object) throws IOException {
		// check for scalar types:
		if (object instanceof Integer || object instanceof Short || object instanceof Byte) {
			serializer.startTag(null, TYPE_I4).text(object.toString()).endTag(null, TYPE_I4);
		} else
		if (object instanceof Long) {
			serializer.startTag(null, TYPE_I8).text(object.toString()).endTag(null, TYPE_I8);
		} else
		if (object instanceof Double || object instanceof Float) {
			serializer.startTag(null, TYPE_DOUBLE).text(object.toString()).endTag(null, TYPE_DOUBLE);
		} else
		if (object instanceof Boolean) {
			Boolean bool = (Boolean) object;
			String boolStr = bool.booleanValue() ? "1" : "0";
			serializer.startTag(null, TYPE_BOOLEAN).text(boolStr).endTag(null, TYPE_BOOLEAN);
		} else
		if (object instanceof String) {
			serializer.startTag(null, TYPE_STRING).text(object.toString()).endTag(null, TYPE_STRING);
		} else
		if (object instanceof Date || object instanceof Calendar) {
			String dateStr = dateFormat.format(object);
			serializer.startTag(null, TYPE_DATE_TIME_ISO8601).text(dateStr).endTag(null, TYPE_DATE_TIME_ISO8601);
		} else
		if (object instanceof byte[] ){
			String value = new String(Base64Coder.encode((byte[])object));
			serializer.startTag(null, TYPE_BASE64).text(value).endTag(null, TYPE_BASE64);
		} else
		if (object instanceof List) {
			serializer.startTag(null, TYPE_ARRAY).startTag(null, TAG_DATA);
			List<Object> list = (List<Object>) object;
			Iterator<Object> iter = list.iterator();
			while (iter.hasNext()) {
				Object o = iter.next();
				serializer.startTag(null, TAG_VALUE);
				serialize(serializer, o);
				serializer.endTag(null, TAG_VALUE);
			}
			serializer.endTag(null, TAG_DATA).endTag(null, TYPE_ARRAY);
		} else
		if (object instanceof Object[]) {
			serializer.startTag(null, TYPE_ARRAY).startTag(null, TAG_DATA);
			Object[] objects = (Object[]) object;
			for (int i=0; i<objects.length; i++) {
				Object o = objects[i];
				serializer.startTag(null, TAG_VALUE);
				serialize(serializer, o);
				serializer.endTag(null, TAG_VALUE);
			}
			serializer.endTag(null, TAG_DATA).endTag(null, TYPE_ARRAY);
		} else
		if (object instanceof Map) {
			serializer.startTag(null, TYPE_STRUCT);
			Map<String, Object> map = (Map<String, Object>) object;
			Iterator<Entry<String, Object>> iter = map.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, Object> entry = iter.next();
				String key = entry.getKey();
				Object value = entry.getValue();

				serializer.startTag(null, TAG_MEMBER);
				serializer.startTag(null, TAG_NAME).text(key).endTag(null, TAG_NAME);
				serializer.startTag(null, TAG_VALUE);
				serialize(serializer, value);
				serializer.endTag(null, TAG_VALUE);
				serializer.endTag(null, TAG_MEMBER);
			}
			serializer.endTag(null, TYPE_STRUCT);
		} else
		if (object instanceof XMLRPCSerializable) {
			XMLRPCSerializable serializable = (XMLRPCSerializable) object;
			serialize(serializer, serializable.getSerializable());
		} else {
			throw new IOException("Cannot serialize " + object);
		}
	}
	
	@Override
	public Object deserialize(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, null, TAG_VALUE);

		if (parser.isEmptyElementTag()) {
			// degenerated <value />, return empty string
			return "";
		}
		
		Object obj;
		boolean hasType = true;
		String typeNodeName = null;
		try {
			parser.nextTag();
			typeNodeName = parser.getName();
			if (typeNodeName.equals(TAG_VALUE) && parser.getEventType() == XmlPullParser.END_TAG) {
				// empty <value></value>, return empty string
				return "";
			}
		} catch (XmlPullParserException e) {
			hasType = false;
		}
		if (hasType) {
			if (typeNodeName.equals(TYPE_INT) || typeNodeName.equals(TYPE_I4)) {
				String value = parser.nextText();
				obj = Integer.parseInt(value);
			} else
			if (typeNodeName.equals(TYPE_I8)) {
				String value = parser.nextText();
				obj = Long.parseLong(value);
			} else
			if (typeNodeName.equals(TYPE_DOUBLE)) {
				String value = parser.nextText();
				obj = Double.parseDouble(value);
			} else
			if (typeNodeName.equals(TYPE_BOOLEAN)) {
				String value = parser.nextText();
				obj = value.equals("1") ? Boolean.TRUE : Boolean.FALSE;
			} else
			if (typeNodeName.equals(TYPE_STRING)) {
				obj = parser.nextText();
			} else
			if (typeNodeName.equals(TYPE_DATE_TIME_ISO8601)) {
				String value = parser.nextText();
				try {
					obj = dateFormat.parseObject(value);
				} catch (ParseException e) {
					throw new IOException("Cannot deserialize dateTime " + value); 
				}
			} else
			if (typeNodeName.equals(TYPE_BASE64)) {
				String value = parser.nextText();
				BufferedReader reader = new BufferedReader(new StringReader(value));
				String line;
				StringBuffer sb = new StringBuffer();
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				obj = Base64Coder.decode(sb.toString());
			} else
			if (typeNodeName.equals(TYPE_ARRAY)) {
				parser.nextTag(); // TAG_DATA (<data>)
				parser.require(XmlPullParser.START_TAG, null, TAG_DATA);
	
				parser.nextTag();
				List<Object> list = new ArrayList<Object>();
				while (parser.getName().equals(TAG_VALUE)) {
					list.add(deserialize(parser));
					parser.nextTag();
				}
				parser.require(XmlPullParser.END_TAG, null, TAG_DATA);
				parser.nextTag(); // TAG_ARRAY (</array>)
				parser.require(XmlPullParser.END_TAG, null, TYPE_ARRAY);
				obj = list.toArray();
			} else
			if (typeNodeName.equals(TYPE_STRUCT)) {
				parser.nextTag();
				Map<String, Object> map = new HashMap<String, Object>();
				while (parser.getName().equals(TAG_MEMBER)) {
					String memberName = null;
					Object memberValue = null;
					while (true) {
						parser.nextTag();
						String name = parser.getName();
						if (name.equals(TAG_NAME)) {
							memberName = parser.nextText();
						} else
						if (name.equals(TAG_VALUE)) {
							memberValue = deserialize(parser);
						} else {
							break;
						}
					}
					if (memberName != null && memberValue != null) {
						map.put(memberName, memberValue);
					}
					parser.require(XmlPullParser.END_TAG, null, TAG_MEMBER);
					parser.nextTag();
				}
				parser.require(XmlPullParser.END_TAG, null, TYPE_STRUCT);
				obj = map;
			} else {
				throw new IOException("Cannot deserialize " + parser.getName());
			}
		} else {
			// TYPE_STRING (<string>) is not required
			obj = parser.getText();
		}
		parser.nextTag(); // TAG_VALUE (</value>)
		parser.require(XmlPullParser.END_TAG, null, TAG_VALUE);
		return obj;
	}
}
