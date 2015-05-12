package com.github.djarosz.spring.csv;

import java.beans.PropertyEditor;
import java.util.Map;
import org.springframework.beans.BeanWrapper;
import org.springframework.util.CollectionUtils;

/**
 * Created: 2013-09-03 10:19
 *
 * @author Dawid Jarosz <dawid.jarosz@javart.eu>
 */
public class AbstractRecordConverter<T> {

	protected CSVRecordDescriptor<T> recordDescriptor;

	public CSVRecordDescriptor<T> getRecordDescriptor() {
		return recordDescriptor;
	}

	public void setRecordDescriptor(CSVRecordDescriptor<T> recordDescriptor) {
		this.recordDescriptor = recordDescriptor;
	}

	protected void registerPropertyEditors(BeanWrapper wrapper) {
		Map<Class<?>, PropertyEditor> globalEditors = recordDescriptor.getTypePropertyEditors();

		if (globalEditors != null) {
			for (Map.Entry<Class<?>, PropertyEditor> e : globalEditors.entrySet()) {
				wrapper.registerCustomEditor(e.getKey(), e.getValue());
			}
		}

		for (CSVFieldDescriptor f : recordDescriptor.getFields()) {
			if (f.getPropertyEditor() != null) {
				wrapper.registerCustomEditor(wrapper.getPropertyType(f.getName()), f.getName(), f.getPropertyEditor());
			}
		}
	}

	protected PropertyEditor findCustomPropertyEditor(BeanWrapper beanWrapper, String propertyName) {
		Class<?> propertyType = beanWrapper.getPropertyType(propertyName);
		PropertyEditor propertyEditor = null;

		if (recordDescriptor.getFieldPropertyEditors() != null) {
			propertyEditor = recordDescriptor.getFieldPropertyEditors().get(propertyName);
		}

		if (propertyEditor == null && !CollectionUtils.isEmpty(recordDescriptor.getTypePropertyEditors())) {
			propertyEditor = recordDescriptor.getTypePropertyEditors().get(propertyType);
		}

		if (propertyEditor == null) {
			propertyEditor = beanWrapper.findCustomEditor(propertyType, propertyName);
		}

		return propertyEditor;
	}
}
