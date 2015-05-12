package com.github.djarosz.spring.csv;

import static org.apache.commons.lang.StringUtils.strip;

import java.beans.PropertyEditor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.InitializingBean;

/**
 * Opisuje pojedynczy rekord pliku CSV
 *
 * @author Dawid Jarosz <dawid.jarosz@javart.eu>
 */
public class CSVRecordDescriptor<T> implements InitializingBean {

	private List<CSVFieldDescriptor> fields;

	private Class<T> beanClass;

	private char fieldSeparator = ';';

	private char quoteChar = '"';

	private char escapeChar = '\\';

	private boolean strictQuotes = false;

	private List<String> fieldNames;

	private String header;

	private Map<Class<?>, PropertyEditor> typePropertyEditors;

	private Map<String, PropertyEditor> fieldPropertyEditors;

	@Override
	public void afterPropertiesSet() throws Exception {
		if (fieldPropertyEditors != null) {
			for (CSVFieldDescriptor f : fields) {
				if (fieldPropertyEditors.containsKey(f.getName())) {
					f.setPropertyEditor(fieldPropertyEditors.get(f.getName()));
				}

			}
		}
	}

	public Map<String, PropertyEditor> getFieldPropertyEditors() {
		return fieldPropertyEditors;
	}

	public void setFieldPropertyEditors(Map<String, PropertyEditor> fieldPropertyEditors) {
		this.fieldPropertyEditors = fieldPropertyEditors;
	}

	/**
	 * Lista pól w pojedyńczej linie pliku CSV w kolejności w jakiej się w tym pliku pojawiają.
	 * Z każdym polem może być skojażony PropertyEditor który ma być użyty do konwersji wartośći
	 * ze Stringa na obiekt. Jeśli propertyEditor ma wartość <var>null</var> nie jest on rejestrowany
	 * i wykorzystywany jest domyślny ProeprtyEditor zarejestrowany jako {@link #setTypePropertyEditors(java.util.Map)}
	 * lub domyślny zarejestrowany w spring'u
	 *
	 * @return
	 */
	public List<CSVFieldDescriptor> getFields() {
		return fields;
	}

	public void setFields(List<CSVFieldDescriptor> fields) {
		this.fields = fields;
		this.fieldNames = new ArrayList<String>(fields.size());
		this.fieldPropertyEditors = new HashMap<String, PropertyEditor>();
		for (CSVFieldDescriptor f : fields) {
			this.fieldNames.add(f.getName());

			if (f.getPropertyEditor() != null) {
				this.fieldPropertyEditors.put(f.getName(), f.getPropertyEditor());
			}
		}
	}

	/**
	 * Lista pól w pliku CSV odzielona przecinakami.
	 * Dla wszystkich pól przyjmowane są domyślne propertyeditor'y
	 *
	 * @param fieldNames
	 */
	public void setFieldNamesString(String fieldNames) {
		List<CSVFieldDescriptor> fieldList = new ArrayList<CSVFieldDescriptor>();
		for (String name : fieldNames.split(",")) {
			fieldList.add(new CSVFieldDescriptor(strip(name)));
		}

		setFields(fieldList);
	}

	/**
	 * Klasa tworzonego beanu
	 *
	 * @return
	 */
	public Class<T> getBeanClass() {
		return beanClass;
	}

	public void setBeanClass(Class<T> beanClass) {
		this.beanClass = beanClass;
	}

	/**
	 * Separator pól w linii. Domyślnie ";"
	 *
	 * @return
	 */
	public char getFieldSeparator() {
		return fieldSeparator;
	}

	public void setFieldSeparator(char fieldSeparator) {
		this.fieldSeparator = fieldSeparator;
	}

	public char getQuoteChar() {
		return quoteChar;
	}

	public void setQuoteChar(char quoteChar) {
		this.quoteChar = quoteChar;
	}

	public char getEscapeChar() {
		return escapeChar;
	}

	public void setEscapeChar(char escapeChar) {
		this.escapeChar = escapeChar;
	}

	public boolean isStrictQuotes() {
		return strictQuotes;
	}

	public void setStrictQuotes(boolean strictQuotes) {
		this.strictQuotes = strictQuotes;
	}

	/**
	 * Zwraca listę nazw pól w kolejności w jakiej są one oczekiwane pliku wejściowym
	 *
	 * @return
	 */
	public List<String> getFieldNames() {
		return fieldNames;
	}

	public void setFieldNames(List<String> fieldNames) {
		List<CSVFieldDescriptor> fieldList = new ArrayList<CSVFieldDescriptor>();
		for (String fieldName : fieldNames) {
			fieldList.add(new CSVFieldDescriptor(strip(fieldName)));
		}

		setFields(fieldList);
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public Map<Class<?>, PropertyEditor> getTypePropertyEditors() {
		return typePropertyEditors;
	}

	public void setTypePropertyEditors(Map<Class<?>, PropertyEditor> globaPropertyEditors) {
		this.typePropertyEditors = globaPropertyEditors;
	}

}
