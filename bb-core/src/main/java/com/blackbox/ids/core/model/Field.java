package com.blackbox.ids.core.model;

public class Field 
{
	private int id;
	private String key;
	private String value;
	private FieldLocation fieldLocation;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public FieldLocation getFieldLocation() {
		return fieldLocation;
	}
	public void setFieldLocation(FieldLocation fieldLocation) {
		this.fieldLocation = fieldLocation;
	}
	
	
}
