package org.github.djarosz.spring.csv;

import java.util.Date;

public class TestObject {

	private String stringValue;

	private Integer integerValue;

	private Double doubleValue;

	private Date dateValue;

	private boolean booleanValue;

	private TestEnum enumValue;

	private NestedTestObject nestedObject;

	public Integer getIntegerValue() {
		return integerValue;
	}

	public void setIntegerValue(Integer integerValue) {
		this.integerValue = integerValue;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public Double getDoubleValue() {
		return doubleValue;
	}

	public void setDoubleValue(Double doubleValue) {
		this.doubleValue = doubleValue;
	}

	public Date getDateValue() {
		return dateValue;
	}

	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
	}

	public TestEnum getEnumValue() {
		return enumValue;
	}

	public void setEnumValue(TestEnum enumValue) {
		this.enumValue = enumValue;
	}

	public boolean isBooleanValue() {
		return booleanValue;
	}

	public void setBooleanValue(boolean booleanValue) {
		this.booleanValue = booleanValue;
	}

	public NestedTestObject getNestedObject() {
		return nestedObject;
	}

	public void setNestedObject(NestedTestObject nestedObject) {
		this.nestedObject = nestedObject;
	}

	public enum TestEnum {AAA, BBB, CCCC;}
}
