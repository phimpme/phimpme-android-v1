/*******************************************************************************
 * Copyright (c) 2010 Stefano Norcia.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl-3.html
 ******************************************************************************/
package com.joooid.android.model;

import java.io.Serializable;

/**
 * @author  random
 */
public class User implements Serializable{
	
	public static final int JOOMLA_15 = 0;
	public static final int JOOMLA_16 = 1;

	private static final long serialVersionUID = 1L;
	
	private String joomlaUrl;
	private String joomlaUri;
	private String joomlaUser;
	private String joomlaPwd;
	private String httpUser;
	private String httpPwd;
	private Integer joomlaVersion;
	private String firstName;
	private String lastName;
	private String email;
	private Integer id;
	
	private boolean updated;

	
	public User(String joomlaUrl, String joomlaUri, String joomlaUser, String joomlaPwd,
			String httpUser, String httpPwd, int joomlaVersion,
			String firstName, String lastName, String email, int id) {
		super();
		this.joomlaUrl = joomlaUrl;
		this.joomlaUri = joomlaUri;
		this.joomlaUser = joomlaUser;
		this.joomlaPwd = joomlaPwd;
		this.httpUser = httpUser;
		this.httpPwd = httpPwd;
		this.joomlaVersion = joomlaVersion;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.id = id;
	}
	public Integer getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getJoomlaUrl() {
		return joomlaUrl;
	}
	public String getJoomlaUri() {
		return joomlaUri;
	}
	public void setJoomlaUrl(String joomlaUrl) {
		this.joomlaUrl = joomlaUrl;
	}
	public void setJoomlaUri(String joomlaUri) {
		this.joomlaUri = joomlaUri;
	}
	public User(){
	}
	public void setUrl(String url) {
		this.joomlaUrl = url;
	}
	public String getUser() {
		return joomlaUser;
	}
	public void setUser(String user) {
		this.joomlaUser = user;
	}
	public String getPass() {
		return joomlaPwd;
	}
	public void setPass(String pass) {
		this.joomlaPwd = pass;
	}
	public String getHttpUser() {
		return httpUser;
	}
	public void setHttpUser(String httpUser) {
		this.httpUser = httpUser;
	}
	public String getHttpPwd() {
		return httpPwd;
	}
	public void setHttpPwd(String httpPwd) {
		this.httpPwd = httpPwd;
	}
	public Integer getJoomlaVersion() {
		return joomlaVersion;
	}
	public void setJoomlaVersion(int joomlaVersion) {
		this.joomlaVersion = joomlaVersion;
	}
	public boolean isUpdated() {
		return updated;
	}
	public void setUpdated(boolean updated) {
		this.updated = updated;
	}
}
