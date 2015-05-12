package com.github.djarosz.spring.csv;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.apache.commons.lang.StringUtils.defaultString;

import java.beans.PropertyEditor;
import java.util.List;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * Klasa implementuje parsowanie linii pliku CSV i jej konwersję na obiekt.
 *
 * @author Dawid Jarosz <dawid.jarosz@javart.eu>
 */
public class ObjectToCSVRecordConverter<T> extends AbstractRecordConverter<T> {

	public static final String SPEL_DENOMIATOR = "!";

	public String toCvsRecord(T obj) {
		List<CSVFieldDescriptor> fields = recordDescriptor.getFields();
		StringBuilder sb = new StringBuilder();
		String escapeStr = EMPTY + recordDescriptor.getEscapeChar() + recordDescriptor.getQuoteChar();
		String quoteStr = EMPTY + recordDescriptor.getQuoteChar();
		boolean firstValue = true;

		for (CSVFieldDescriptor field : fields) {
			if (firstValue) {
				firstValue = false;
			} else {
				sb.append(recordDescriptor.getFieldSeparator());
			}

			String fieldName = field.getName();
			String strVal = EMPTY;

			if (isSpEL(fieldName)) {
				strVal = evalSpEL(obj, fieldName);
			} else {
				BeanWrapper wrapper = new BeanWrapperImpl(obj);
				Object value = wrapper.getPropertyValue(fieldName);
				if (value != null) {
					PropertyEditor editor = findCustomPropertyEditor(wrapper, fieldName);

					if (editor == null) {
						strVal = value.toString();
					} else {
						editor.setValue(value);
						strVal = editor.getAsText();
					}
				}
			}

			if (recordDescriptor.isStrictQuotes()
					|| strVal.indexOf(recordDescriptor.getFieldSeparator()) != -1
					|| strVal.indexOf(recordDescriptor.getQuoteChar()) != -1
					|| strVal.indexOf('\n') != -1) {
				sb.append(recordDescriptor.getQuoteChar()).append(defaultString(strVal).replace(quoteStr, escapeStr)).append(recordDescriptor.getQuoteChar());
			} else {
				sb.append(defaultString(strVal));
			}
		}

		return sb.toString();
	}

	private boolean isSpEL(String propertyPath) {
		return propertyPath.trim().startsWith(SPEL_DENOMIATOR);
	}

	private String evalSpEL(Object bean, String expressions) {
		ExpressionParser parser = new SpelExpressionParser();
		Expression exp = parser.parseExpression(expressions.trim().substring(1));
		Object value = exp.getValue(bean);

		return value == null ? EMPTY : value.toString();
	}

}
