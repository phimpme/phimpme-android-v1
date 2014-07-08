package com.joooid.android.xmlrpc;


import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public class TrustAllManager implements X509TrustManager { 
    @Override
	public void checkClientTrusted(X509Certificate[] cert, String authType) throws CertificateException { } 
    @Override
	public void checkServerTrusted(X509Certificate[] cert, String authType) throws CertificateException { } 
    @Override
	public X509Certificate[] getAcceptedIssuers() { return null; } 
} 
