/*
 * Copyright (C) 2013 Surviving with Android (http://www.survivingwithandroid.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.grandma1;

import java.io.Serializable;

public class ItemDetail implements Serializable {
	
	private long id;
	private String mediname;
	private String content;
	private String descr;
	
	
	
	public ItemDetail(long id, String imgId, String name, String descr) {
		this.id = id;
		this.mediname = imgId;
		this.content = name;
		this.descr = descr;
	}
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getMedi() {
		return mediname;
	}
	public void setMediname(String med) {
		this.mediname = med;
	}
	public String getName() {
		return content;
	}
	public void setName(String name) {
		this.content = name;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}



	
}
