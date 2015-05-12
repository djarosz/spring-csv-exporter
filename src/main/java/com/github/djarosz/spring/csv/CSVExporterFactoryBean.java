package com.github.djarosz.spring.csv;

import java.beans.PropertyEditor;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * Created: 2013-09-04 10:05
 *
 * @author Dawid Jarosz <dawid.jarosz@javart.eu>
 */
public class CSVExporterFactoryBean extends AbstractFactoryBean<CSVExporter> {

	private CSVRecordDescriptor recordDescriptor = new CSVRecordDescriptor();
	private CSVExporter exporter = new CSVExporter();

	@Override
	public Class<CSVExporter> getObjectType() {
		return CSVExporter.class;
	}

	@Override
	protected CSVExporter createInstance() throws Exception {
		ObjectToCSVRecordConverter converter = new ObjectToCSVRecordConverter();

		converter.setRecordDescriptor(recordDescriptor);
		exporter.setConverter(converter);

		return exporter;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		recordDescriptor.afterPropertiesSet();
	}

	public List<CSVFieldDescriptor> getFields() {
		return recordDescriptor.getFields();
	}

	public void setEscapeChar(char escapeChar) {
		recordDescriptor.setEscapeChar(escapeChar);
	}

	public void setHeader(String header) {
		recordDescriptor.setHeader(header);
	}

	public void setFields(List<CSVFieldDescriptor> fields) {
		recordDescriptor.setFields(fields);
	}

	public void setFieldNames(List<String> fieldNames) {
		recordDescriptor.setFieldNames(fieldNames);
	}

	public void setTypePropertyEditors(Map<Class<?>, PropertyEditor> globaPropertyEditors) {
		recordDescriptor.setTypePropertyEditors(globaPropertyEditors);
	}

	public void setFieldPropertyEditors(Map<String, PropertyEditor> fieldPropertyEditors) {
		recordDescriptor.setFieldPropertyEditors(fieldPropertyEditors);
	}

	public void setFieldSeparator(char fieldSeparator) {
		recordDescriptor.setFieldSeparator(fieldSeparator);
	}

	public char getQuoteChar() {
		return recordDescriptor.getQuoteChar();
	}

	public String getHeader() {
		return recordDescriptor.getHeader();
	}

	public void setQuoteChar(char quoteChar) {
		recordDescriptor.setQuoteChar(quoteChar);
	}

	public List<String> getFieldNames() {
		return recordDescriptor.getFieldNames();
	}

	public char getEscapeChar() {
		return recordDescriptor.getEscapeChar();
	}

	public void setBeanClass(Class beanClass) {
		recordDescriptor.setBeanClass(beanClass);
	}

	public Map<Class<?>, PropertyEditor> getTypePropertyEditors() {
		return recordDescriptor.getTypePropertyEditors();
	}

	public Map<String, PropertyEditor> getFieldPropertyEditors() {
		return recordDescriptor.getFieldPropertyEditors();
	}

	public char getFieldSeparator() {
		return recordDescriptor.getFieldSeparator();
	}

	public Class getBeanClass() {
		return recordDescriptor.getBeanClass();
	}

	public boolean isStrictQuotes() {
		return recordDescriptor.isStrictQuotes();
	}

	public void setFieldNamesString(String fieldNames) {
		recordDescriptor.setFieldNamesString(fieldNames);
	}

	public void setStrictQuotes(boolean strictQuotes) {
		recordDescriptor.setStrictQuotes(strictQuotes);
	}

	public EOLSeq getEolSeq() {
		return exporter.getEolSeq();
	}

	public void setEolSeq(EOLSeq eolSeq) {
		exporter.setEolSeq(eolSeq);
	}

	public String getOutputEncoding() {
		return exporter.getOutputEncoding();
	}

	public void setOutputEncoding(String outputEncoding) {
		exporter.setOutputEncoding(outputEncoding);
	}

}
