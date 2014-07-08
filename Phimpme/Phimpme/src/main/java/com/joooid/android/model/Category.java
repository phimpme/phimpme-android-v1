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

public class Category implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Integer id;
	Integer parentId;
	String title;
	String alias;
	String date;
	String section;
	Integer level;
	Integer state;
	Integer access;
	String description;

	public static final int JOOMLA_ACCESS_PUBLIC = 0;
	public static final int JOOMLA_ACCESS_REGISTERED = 1;
	public static final int JOOMLA_ACCESS_SPECIAL = 2;
	
	public static final int JOOMLA_ACCESS_15_PUBLIC = 0;
	public static final int JOOMLA_ACCESS_15_REGISTERED = 1;
	public static final int JOOMLA_ACCESS_15_SPECIAL = 2;
	
	public static final int JOOMLA_15_LEVEL_SECTION = 0;
	public static final int JOOMLA_15_LEVEL_CATEGORY = 1;
	
	public static final int JOOMLA_ACCESS_16_PUBLIC = 1;
	public static final int JOOMLA_ACCESS_16_REGISTERED = 2;
	public static final int JOOMLA_ACCESS_16_SPECIAL = 3;

	public static final int JOOMLA_STATE_PUBLISHED = 1;
	public static final int JOOMLA_STATE_UNPUBLISHED = 0;
	public static final int JOOMLA_STATE_TRASHED = -2;

	public ArrayList<String> uploadImageName;
	public ArrayList<String> uploadImageBase64;
	
	public Category(){
		this.access = JOOMLA_ACCESS_PUBLIC; 
		this.state = JOOMLA_STATE_PUBLISHED;
		}
	
	public Category(Integer id, String cat, String sez) {
		super();
		this.id = id;
		this.title = cat;
		this.section = sez;
	}
	
	public Category(Integer id, String title,String alias, String path, String date, Integer state, Integer access, Integer parentId, Integer level, String description) {
		super();
		this.id = id;
		this.parentId = parentId;
		this.title = title;
		this.alias = alias;
		this.section = path;
		this.date = date;
		this.state = state;
		this.access = access;
		this.level = level;
		this.description = description;
	}
	

	public Integer getAccess(int version) {
		if (version == User.JOOMLA_16) return access + 1;
		else return access;
	}
	
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Integer getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Integer getAccess() {
		return access;
	}

	public void setAccess(int access) {
		this.access = access;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setCat(String cat) {
		this.title = cat;
	}

	public String getSez() {
		return section;
	}

	public void setSez(String sez) {
		this.section = sez;
	}
}
