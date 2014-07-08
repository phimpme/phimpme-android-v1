/*******************************************************************************
 * Copyright (c) 2010 Stefano Norcia.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl-3.html
 ******************************************************************************/
package com.joooid.android.xmlrpc;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

/**
 * XMLRPCClient allows to call remote XMLRPC method.
 * 
 * <p>
 * The following table shows how XML-RPC types are mapped to java call parameters/response values.
 * </p>
 * 
 * <p>
 * <table border="2" align="center" cellpadding="5">
 * <thead><tr><th>XML-RPC Type</th><th>Call Parameters</th><th>Call Response</th></tr></thead>
 * 
 * <tbody>
 * <td>int, i4</td><td>byte<br />Byte<br />short<br />Short<br />int<br />Integer</td><td>int<br />Integer</td>
 * </tr>
 * <tr>
 * <td>i8</td><td>long<br />Long</td><td>long<br />Long</td>
 * </tr>
 * <tr>
 * <td>double</td><td>float<br />Float<br />double<br />Double</td><td>double<br />Double</td>
 * </tr>
 * <tr>
 * <td>string</td><td>String</td><td>String</td>
 * </tr>
 * <tr>
 * <td>boolean</td><td>boolean<br />Boolean</td><td>boolean<br />Boolean</td>
 * </tr>
 * <tr>
 * <td>dateTime.iso8601</td><td>java.util.Date<br />java.util.Calendar</td><td>java.util.Date</td>
 * </tr>
 * <tr>
 * <td>base64</td><td>byte[]</td><td>byte[]</td>
 * </tr>
 * <tr>
 * <td>array</td><td>java.util.List&lt;Object&gt;<br />Object[]</td><td>Object[]</td>
 * </tr>
 * <tr>
 * <td>struct</td><td>java.util.Map&lt;String, Object&gt;</td><td>java.util.Map&lt;String, Object&gt;</td>
 * </tr>
 * </tbody>
 * </table>
 * </p>
 * <p>
 * You can also pass as a parameter any object implementing XMLRPCSerializable interface. In this
 * case your object overrides getSerializable() telling how to serialize to XMLRPC protocol
 * </p>
 */

public class XMLRPCClient extends XMLRPCCommon {
//	private HttpClient client;
	private HttpPost postMethod;
	private HttpParams httpParams;
	private ConnectionClient client;
	public String responseString;
	public String errorString;
	public boolean isDebug = true;

	/**
	 * XMLRPCClient constructor. Creates new instance based on server URI
	 * @param XMLRPC server URI
	 */
	public XMLRPCClient(URI uri) {
//		postMethod = new HttpPost(uri);
//		postMethod.addHeader("Content-Type", "text/xml");
//		httpParams = postMethod.getParams();
//		HttpProtocolParams.setUseExpectContinue(httpParams, false);
//		client = (ConnectionClient) new DefaultHttpClient();
	}
	
	/**
	 * Convenience constructor. Creates new instance based on server String address
	 * @param XMLRPC server address
	 */
	public XMLRPCClient(String url) {
		this(URI.create(url));
	}

	/**
	 * Convenience XMLRPCClient constructor. Creates new instance based on server URL
	 * @param XMLRPC server URL
	 */
	public XMLRPCClient(URL url) {
		this(URI.create(url.toExternalForm()));
	}

	/**
	 * Convenience constructor. Creates new instance based on server String address
	 * @param XMLRPC server address
	 * @param HTTP Server - Basic Authentication - Username
	 * @param HTTP Server - Basic Authentication - Password
	 */	
	public XMLRPCClient(URI uri, String httpuser, String httppasswd) {
        
		postMethod = new HttpPost(uri);
		postMethod.addHeader("Content-Type", "text/xml");
		
		postMethod.addHeader("charset", "UTF-8");
		//UPDATE THE VERSION NUMBER BEFORE RELEASE! <3 Dan
		postMethod.addHeader("User-Agent", "joooid-android/2.0");
		
		httpParams = postMethod.getParams();
		HttpProtocolParams.setUseExpectContinue(httpParams, false);
		UsernamePasswordCredentials creds = new UsernamePasswordCredentials(httpuser, httppasswd);
		
		//this gets connections working over https
		if (uri.getScheme() != null){
			if(uri.getScheme().equals("https")) { 
				if(uri.getPort() == -1)
					try {
						client = new ConnectionClient(creds, 443);
					} catch (KeyManagementException e) {
						client = new ConnectionClient(creds); 
					} catch (NoSuchAlgorithmException e) {
						client = new ConnectionClient(creds); 
					} catch (KeyStoreException e) {
						client = new ConnectionClient(creds); 
					} catch (UnrecoverableKeyException e) {
						client = new ConnectionClient(creds); 
					}
					else
						try {
							client = new ConnectionClient(creds, uri.getPort());
						} catch (KeyManagementException e) {
							client = new ConnectionClient(creds); 
						} catch (NoSuchAlgorithmException e) {
							client = new ConnectionClient(creds); 
						} catch (KeyStoreException e) {
							client = new ConnectionClient(creds); 
						} catch (UnrecoverableKeyException e) {
							client = new ConnectionClient(creds); 
						} 
			} 
			else {
				client = new ConnectionClient(creds); 
			}
		}
		else{
			client = new ConnectionClient(creds);
		}
    }

	/**
	 * Convenience constructor. Creates new instance based on server String address
	 * @param XMLRPC server address
	 * @param HTTP Server - Basic Authentication - Username
	 * @param HTTP Server - Basic Authentication - Password
	 */
	public XMLRPCClient(String url, String username, String password) {
		this(URI.create(url), username, password);
	}

	/**
	 * Convenience constructor. Creates new instance based on server String address
	 * @param XMLRPC server url
	 * @param HTTP Server - Basic Authentication - Username
	 * @param HTTP Server - Basic Authentication - Password
	 */
	public XMLRPCClient(URL url, String username, String password) {
		this(URI.create(url.toExternalForm()), username, password);
	}

	/**
	 * Sets basic authentication on web request using plain credentials
	 * @param username The plain text username
	 * @param password The plain text password
	 */
	public void setBasicAuthentication(String username, String password) {
		((DefaultHttpClient) client).getCredentialsProvider().setCredentials(
		        new AuthScope(postMethod.getURI().getHost(), postMethod.getURI().getPort(),
		        		AuthScope.ANY_REALM),
		        			new UsernamePasswordCredentials(username, password));
	}

	/**
	 * Call method with optional parameters. This is general method.
	 * If you want to call your method with 0-8 parameters, you can use more
	 * convenience call() methods
	 * 
	 * @param method name of method to call
	 * @param params parameters to pass to method (may be null if method has no parameters)
	 * @return deserialized method return value
	 * @throws XMLRPCException
	 */
	@SuppressWarnings("unchecked")
	public Object callEx(String method, Object[] params) throws XMLRPCException {
		try {
			// prepare POST body
			String body = methodCall(method, params);
			if (isDebug) Log.e("XML-RPC REQUEST", body);
			
			// set POST body
			HttpEntity entity = new StringEntity(body);
			postMethod.setEntity(entity);
			HttpResponse response = client.execute(postMethod);
			if (isDebug) Log.e("XML-RPC ENDPOINT",postMethod.getURI().toString());
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				errorString = Integer.toString(statusCode) + " " +  response.getStatusLine().getReasonPhrase();
				responseString = EntityUtils.toString(response.getEntity());
				entity.consumeContent();
				throw new XMLRPCException("HTTP STATUS CODE " + statusCode);
			}
			// parse response stuff

			// setup pull parser
			XmlPullParser pullParser = XmlPullParserFactory.newInstance().newPullParser();
			responseString = EntityUtils.toString(response.getEntity());
			if (isDebug) Log.e("XML-RPC RESPONSE",responseString);

			entity.consumeContent();
			Reader reader = new StringReader(responseString);
			pullParser.setInput(reader);
			
			// lets start pulling...
			pullParser.nextTag();
			pullParser.require(XmlPullParser.START_TAG, null, Tag.METHOD_RESPONSE);
			
			pullParser.nextTag(); // either Tag.PARAMS (<params>) or Tag.FAULT (<fault>)  
			String tag = pullParser.getName();
			if (tag.equals(Tag.PARAMS)) {
				// normal response
				pullParser.nextTag(); // Tag.PARAM (<param>)
				pullParser.require(XmlPullParser.START_TAG, null, Tag.PARAM);
				pullParser.nextTag(); // Tag.VALUE (<value>)
				// no parser.require() here since its called in XMLRPCSerializer.deserialize() below
				
				// deserialize result
				Object obj = iXMLRPCSerializer.deserialize(pullParser);
//				entity.consumeContent();
				return obj;
			} else
			if (tag.equals(Tag.FAULT)) {
				// fault response
				pullParser.nextTag(); // Tag.VALUE (<value>)
				// no parser.require() here since its called in XMLRPCSerializer.deserialize() below

				// deserialize fault result
				Map<String, Object> map = (Map<String, Object>) iXMLRPCSerializer.deserialize(pullParser);
				String faultString = (String) map.get(Tag.FAULT_STRING);
				int faultCode = (Integer) map.get(Tag.FAULT_CODE);
//				entity.consumeContent();
				throw new XMLRPCFault(faultString, faultCode);
			} else {
				errorString = "BAD TAG " + tag;
//				entity.consumeContent();
				throw new XmlPullParserException("BAD TAG <" + tag + ">");
			}
			
		} catch (XMLRPCFault e) {
			errorString = e.toString();
			if (isDebug) Log.e("XML-RPC","Stacktrace :\n" + errorString + "\nResponse from server :\n" + responseString);
			throw new XMLRPCException(Constants.TASK_WS_CODE_ERROR_XML_RPC + "\n" + e.toString());
		}catch (XMLRPCException e) {
			if (isDebug) Log.e("XML-RPC","Stacktrace :\n" + errorString + "\nResponse from server :\n" + responseString);
			throw new XMLRPCException(Constants.TASK_WS_CODE_ERROR_HTTP_CODE + "\n" + e.toString());
		} catch (XmlPullParserException e) {
			if (isDebug) Log.e("XML-RPC","Stacktrace :\n" + errorString + "\nResponse from server :\n" + responseString);
			throw new XMLRPCException(Constants.TASK_WS_CODE_ERROR_XML_PARSER + "\n" + e.toString());
		} catch (Exception e) {
			if (isDebug) Log.e("XML-RPC","Stacktrace :\n" + errorString + "\nResponse from server :\n" + responseString);
			throw new XMLRPCException(Constants.TASK_WS_CODE_ERROR_RPC_CLIENT + "\n" + e.toString());
		}
	}
	
	private String methodCall(String method, Object[] params)
	throws IllegalArgumentException, IllegalStateException, IOException {
		StringWriter bodyWriter = new StringWriter();
		serializer.setOutput(bodyWriter);
		serializer.startDocument(null, null);
		serializer.startTag(null, Tag.METHOD_CALL);
		// set method name
		serializer.startTag(null, Tag.METHOD_NAME).text(method).endTag(null, Tag.METHOD_NAME);
		
		serializeParams(params);

		serializer.endTag(null, Tag.METHOD_CALL);
		serializer.endDocument();

		return bodyWriter.toString();
	}

	/**
	 * Convenience method call with no parameters
	 * 
	 * @param method name of method to call
	 * @return deserialized method return value
	 * @throws XMLRPCException
	 */
	public Object call(String method) throws XMLRPCException {
		return callEx(method, null);
	}
	
	/**
	 * Convenience method call with one parameter
	 * 
	 * @param method name of method to call
	 * @param p0 method's parameter
	 * @return deserialized method return value
	 * @throws XMLRPCException
	 */
	public Object call(String method, Object p0) throws XMLRPCException {
		Object[] params = {
			p0,
		};
		return callEx(method, params);
	}
	
	/**
	 * Convenience method call with two parameters
	 * 
	 * @param method name of method to call
	 * @param p0 method's 1st parameter
	 * @param p1 method's 2nd parameter
	 * @return deserialized method return value
	 * @throws XMLRPCException
	 */
	public Object call(String method, Object p0, Object p1) throws XMLRPCException {
		Object[] params = {
			p0, p1,
		};
		return callEx(method, params);
	}
	
	/**
	 * Convenience method call with three parameters
	 * 
	 * @param method name of method to call
	 * @param p0 method's 1st parameter
	 * @param p1 method's 2nd parameter
	 * @param p2 method's 3rd parameter
	 * @return deserialized method return value
	 * @throws XMLRPCException
	 */
	public Object call(String method, Object p0, Object p1, Object p2) throws XMLRPCException {
		Object[] params = {
			p0, p1, p2,
		};
		return callEx(method, params);
	}

	/**
	 * Convenience method call with four parameters
	 * 
	 * @param method name of method to call
	 * @param p0 method's 1st parameter
	 * @param p1 method's 2nd parameter
	 * @param p2 method's 3rd parameter
	 * @param p3 method's 4th parameter
	 * @return deserialized method return value
	 * @throws XMLRPCException
	 */
	public Object call(String method, Object p0, Object p1, Object p2, Object p3) throws XMLRPCException {
		Object[] params = {
			p0, p1, p2, p3,
		};
		return callEx(method, params);
	}

	/**
	 * Convenience method call with five parameters
	 * 
	 * @param method name of method to call
	 * @param p0 method's 1st parameter
	 * @param p1 method's 2nd parameter
	 * @param p2 method's 3rd parameter
	 * @param p3 method's 4th parameter
	 * @param p4 method's 5th parameter
	 * @return deserialized method return value
	 * @throws XMLRPCException
	 */
	public Object call(String method, Object p0, Object p1, Object p2, Object p3, Object p4) throws XMLRPCException {
		Object[] params = {
			p0, p1, p2, p3, p4,
		};
		return callEx(method, params);
	}

	/**
	 * Convenience method call with six parameters
	 * 
	 * @param method name of method to call
	 * @param p0 method's 1st parameter
	 * @param p1 method's 2nd parameter
	 * @param p2 method's 3rd parameter
	 * @param p3 method's 4th parameter
	 * @param p4 method's 5th parameter
	 * @param p5 method's 6th parameter
	 * @return deserialized method return value
	 * @throws XMLRPCException
	 */
	public Object call(String method, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) throws XMLRPCException {
		Object[] params = {
			p0, p1, p2, p3, p4, p5,
		};
		return callEx(method, params);
	}

	/**
	 * Convenience method call with seven parameters
	 * 
	 * @param method name of method to call
	 * @param p0 method's 1st parameter
	 * @param p1 method's 2nd parameter
	 * @param p2 method's 3rd parameter
	 * @param p3 method's 4th parameter
	 * @param p4 method's 5th parameter
	 * @param p5 method's 6th parameter
	 * @param p6 method's 7th parameter
	 * @return deserialized method return value
	 * @throws XMLRPCException
	 */
	public Object call(String method, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) throws XMLRPCException {
		Object[] params = {
			p0, p1, p2, p3, p4, p5, p6,
		};
		return callEx(method, params);
	}

	/**
	 * Convenience method call with eight parameters
	 * 
	 * @param method name of method to call
	 * @param p0 method's 1st parameter
	 * @param p1 method's 2nd parameter
	 * @param p2 method's 3rd parameter
	 * @param p3 method's 4th parameter
	 * @param p4 method's 5th parameter
	 * @param p5 method's 6th parameter
	 * @param p6 method's 7th parameter
	 * @param p7 method's 8th parameter
	 * @return deserialized method return value
	 * @throws XMLRPCException
	 */
	public Object call(String method, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) throws XMLRPCException {
		Object[] params = {
			p0, p1, p2, p3, p4, p5, p6, p7,
		};
		return callEx(method, params);
	}
	
	public Object call(String method, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) throws XMLRPCException {
		Object[] params = {
			p0, p1, p2, p3, p4, p5, p6, p7, p8
		};
		return callEx(method, params);
	}
	
	public Object call(String method, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) throws XMLRPCException {
		Object[] params = {
			p0, p1, p2, p3, p4, p5, p6, p7, p8, p9
		};
		return callEx(method, params);
	}
	
	public Object call(String method, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9, Object p10) throws XMLRPCException {
		Object[] params = {
			p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10
		};
		return callEx(method, params);
	}

	public Object call(String method, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9, Object p10,Object p11) throws XMLRPCException {
		Object[] params = {
			p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11
		};
		return callEx(method, params);
	}

	public Object call(String method, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9, Object p10,Object p11, Object p12) throws XMLRPCException {
		Object[] params = {
			p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12
		};
		return callEx(method, params);
	}
	
	public Object call(String method, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9, Object p10,Object p11, Object p12, Object p13) throws XMLRPCException {
		Object[] params = {
			p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13
		};
		return callEx(method, params);
	}
}
