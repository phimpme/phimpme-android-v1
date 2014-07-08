/*******************************************************************************
 * Copyright (c) 2010 Stefano Norcia.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl-3.html
 ******************************************************************************/
package com.joooid.android.model;

import java.util.ArrayList;

public class Tag {

	public static final Integer NUMTAG = 10;
	
	public static final Integer BOLD = 1;
	public static final Integer ITALIC = 2;
	public static final Integer UNDERLINE = 3;
	public static final Integer QUOTES = 4;
	public static final Integer PARAGRAPH = 5;
	public static final Integer BREAK = 6;
	public static final Integer RULER = 7;
	public static final Integer H1 = 8;
	public static final Integer H2 = 9;
	public static final Integer H3 = 10;

	public static ArrayList<Tag> tagArray;
	
	String tag;
	String name;
	Integer type;

	boolean isEnd;
	
	public Tag(String tag, String name,  Integer type, boolean isEnd) {
		super();
		this.tag = tag;
		this.name = name;
		this.type = type;
		this.isEnd = isEnd;
	}
	
	public static ArrayList<Tag> createTagItems(){

		ArrayList<Tag> items= new ArrayList<Tag>();
		
		for (int i = 1; i < Tag.NUMTAG +1; i++){
						
				switch (i){
					case 1: 
						Tag tag1 = new Tag("b", "bold", Tag.BOLD, true); 
						items.add(tag1);
						break;
					case 2: 
						Tag tag2 = new Tag("i", "italic", Tag.ITALIC, true); 
						items.add(tag2);
						break;
					case 3: 
						Tag tag3 = new Tag("u", "underline", Tag.UNDERLINE, true); 
						items.add(tag3);
						break;
					case 4: 
						Tag tag4 = new Tag("q", "quotes", Tag.QUOTES, true); 
						items.add(tag4);
						break;
					case 5: 
						Tag tag5 = new Tag("p", "paragraph", Tag.PARAGRAPH, true); 
						items.add(tag5);
						break;
					case 6: 
						Tag tag6 = new Tag("br /", "line break", Tag.BREAK, false); 
						items.add(tag6);
						break;
					case 7: 
						Tag tag7 = new Tag("hr /", "ruler", Tag.RULER, false); 
						items.add(tag7);
						break;
					case 8: 
						Tag tag8 = new Tag("h1", "header 1", Tag.H1, false); 
						items.add(tag8);
						break;
					case 9: 
						Tag tag9 = new Tag("h2", "header 2", Tag.H2, false); 
						items.add(tag9);
						break;
					case 10: 
						Tag tag10 = new Tag("h3", "header 3", Tag.H3, false); 
						items.add(tag10);
						break;
					}
				}
		
		return items;
		
	}
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
