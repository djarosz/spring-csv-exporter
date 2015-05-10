package org.github.djarosz.spring.csv;

import java.beans.PropertyEditor;

/**
 * Opisuje pojedyncze pole pliku CSV
 *
 * @author Dawid Jarosz <dawid.jarosz@javart.eu>
 */
public class CSVFieldDescriptor {

	private String name;

	private PropertyEditor propertyEditor;

	public CSVFieldDescriptor() {
	}

	public CSVFieldDescriptor(String name) {
		this.name = name;
	}

	public CSVFieldDescriptor(String name, PropertyEditor editor) {
		this.name = name;
		this.propertyEditor = editor;
	}

	/**
	 * Nazwa pola. Musi być zgodna z nazwią odpowiedniego propertiesu w {@link CSVRecordDescriptor#setBeanClass(Class)
	 *
	 * @return
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * PropertyEditor umożliwiający konwersje wartość zapisanej w String'u na wartosć obiektową
	 *
	 * @return
	 */
	public PropertyEditor getPropertyEditor() {
		return propertyEditor;
	}

	public void setPropertyEditor(PropertyEditor editor) {
		this.propertyEditor = editor;
	}

}