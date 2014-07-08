/*******************************************************************************
 * Copyright (c) 2010 Stefano Norcia.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl-3.html
 ******************************************************************************/
package com.joooid.android.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Article implements Serializable{

	private static final long serialVersionUID = 1L;
	Integer id ;
    Integer table_id;
	Integer userid;
    Integer state;
	Integer access;
	Integer categoryid;
	String title;
	String alias;
	String introtext;
	String fulltext;
	String date;
	String username;

    Boolean frontpage;
	
	public ArrayList<String> uploadImageName;
	public ArrayList<String> uploadImageBase64;
	
	public static final int JOOMLA_ACCESS_PUBLIC = 0;
	public static final int JOOMLA_ACCESS_REGISTERED = 1;
	public static final int JOOMLA_ACCESS_SPECIAL = 2;
	
	public static final int JOOMLA_ACCESS_15_PUBLIC = 0;
	public static final int JOOMLA_ACCESS_15_REGISTERED = 1;
	public static final int JOOMLA_ACCESS_15_SPECIAL = 2;
	
	public static final int JOOMLA_ACCESS_16_PUBLIC = 1;
	public static final int JOOMLA_ACCESS_16_REGISTERED = 2;
	public static final int JOOMLA_ACCESS_16_SPECIAL = 3;

	public static final int JOOMLA_STATE_PUBLISHED = 1;
	public static final int JOOMLA_STATE_UNPUBLISHED = 0;
	public static final int JOOMLA_STATE_TRASHED = -2;
	
    public static final int STATE_NEW_ARTICLE = 0;
    public static final int STATE_OLD_ARTICLE = 2;
    public static final int STATE_DRAFT_ARTICLE = 3;

	public Boolean getFrontpage() {
		return frontpage;
	}

	public void setFrontpage(boolean frontpage) {
		this.frontpage = frontpage;
	}

	public Article() {
		this.access = JOOMLA_ACCESS_PUBLIC; 
		this.state = JOOMLA_STATE_PUBLISHED;
		this.frontpage = false;
		this.date = "0";
		
		uploadImageName = new ArrayList<String>();
		uploadImageBase64 = new ArrayList<String>();
	}
	
	public Article(int userid, int id, int catId, String title, String alias, String intro, String full, String date, int state, int access, boolean frontpage) {
		super();
		this.title = title;
		this.alias = alias;
		this.introtext = intro;
		this.fulltext = full;
		this.userid = userid;
		this.id = id;
		this.categoryid = catId;
		this.date = date;
		this.state = state;
		this.access = access;
		this.frontpage = frontpage;
		
	}
	
	
	
	

	
	
	
	
	public Integer getTable_id() {
		return table_id;
	}

	public void setTable_id(Integer tableId) {
		table_id = tableId;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Integer getAccess(int version) {
		if (version == User.JOOMLA_16) return access + 1;
		else return access;
	}
	
	public Integer getAccess() {
		return access;
	}

	public void setAccess(Integer access) {
		this.access = access;
	}

	public String getIntrotext() {
		return introtext;
	}

	public void setIntrotext(String introtext) {
		this.introtext = introtext;
	}

	public String getFulltext() {
		return fulltext;
	}

	public void setFulltext(String fulltext) {
		this.fulltext = fulltext;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String sdate) {
		this.date = sdate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCategoryid() {
		return categoryid;
	}

	public void setCategoryid(Integer categoryid) {
		this.categoryid = categoryid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
