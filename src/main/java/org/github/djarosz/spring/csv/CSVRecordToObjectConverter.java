package org.github.djarosz.spring.csv;

import java.beans.PropertyEditor;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * Klasa implementuje parsowanie linii pliku CSV i jej konwersję na obiekt.
 *
 * @author Dawid Jarosz <dawid.jarosz@javart.eu>
 */
public class CSVRecordToObjectConverter<T> extends AbstractRecordConverter<T> {

	private static final int INITIAL_CVS_LINE_READ_SIZE = 150;

	public T toObject(String inputline) {
		return toObject(parseLine(inputline));
	}

	@SuppressWarnings("unchecked")
	public T toObject(String[] values) {
		BeanWrapper wrapper = new BeanWrapperImpl(recordDescriptor.getBeanClass());
		wrapper.setAutoGrowNestedPaths(true);
		List<String> fieldNames = recordDescriptor.getFieldNames();

		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				String fieldName = fieldNames.get(i);
				PropertyEditor editor = findCustomPropertyEditor(wrapper, fieldName);
				String valueStr = StringUtils.defaultString(values[i], null);
				Object value = valueStr;

				if (editor != null && value != null){
					editor.setAsText(valueStr);
					value = editor.getValue();
				}

				if (value instanceof String && StringUtils.isBlank((String) value)) {
					value = null;
				}

				wrapper.setPropertyValue(fieldName, value);
			}

			return (T) wrapper.getWrappedInstance();
		}

		return null;
	}

	/**
	 * Parses an incoming String and returns an array of elements.
	 *
	 * @param nextLine the string to parse
	 * @return the comma-tokenized list of elements, or null if nextLine is null
	 * @throws java.io.IOException if bad things happen during the read
	 */
	private String[] parseLine(String nextLine) {
		boolean strictQuotes = recordDescriptor.isStrictQuotes();
		char separator = recordDescriptor.getFieldSeparator();
		char quotechar = recordDescriptor.getQuoteChar();
		char escape = recordDescriptor.getEscapeChar();

		if (StringUtils.isBlank(nextLine)) {
			return null;
		}

		List<String> tokensOnThisLine = new ArrayList<String>();
		StringBuilder sb = new StringBuilder(INITIAL_CVS_LINE_READ_SIZE);
		boolean inQuotes = false;

		for (int i = 0; i < nextLine.length(); i++) {

			char c = nextLine.charAt(i);
			if (c == escape) {
				if (isNextCharacterEscapable(nextLine, inQuotes, i)) {
					sb.append(nextLine.charAt(i + 1));
					i++;
				}
			} else if (c == quotechar) {
				if (isNextCharacterEscapedQuote(nextLine, inQuotes, i)) {
					sb.append(nextLine.charAt(i + 1));
					i++;
				} else {
					inQuotes = !inQuotes;
					// the tricky case of an embedded quote in the middle:
					// a,bc"d"ef,g
					if (!strictQuotes) {
						if (i > 2 // not on the beginning of the line
								&& nextLine.charAt(i - 1) != separator // not at the begigning of en escape sequence
								&& nextLine.length() > (i + 1) && nextLine.charAt(i + 1) != separator) { // not at the end of an escape sequence
							sb.append(c);
						}
					}
				}
			} else if (c == separator && !inQuotes) {
				tokensOnThisLine.add(sb.toString());
				sb = new StringBuilder(INITIAL_CVS_LINE_READ_SIZE); // start work on next token
			} else {
				if (!strictQuotes || inQuotes) {
					sb.append(c);
				}
			}
		}

		// line is done - check status
		if (inQuotes) {
			throw new RuntimeException("Un-terminated quoted field at end of CSV line");
		}
		if (sb != null) {
			tokensOnThisLine.add(sb.toString());
		}

		return tokensOnThisLine.toArray(new String[tokensOnThisLine.size()]);
	}

	/**
	 * precondition: the current character is a quote or an escape
	 *
	 * @param nextLine the current line
	 * @param inQuotes true if the current context is quoted
	 * @param i        current index in line
	 * @return true if the following character is a quote
	 */
	private boolean isNextCharacterEscapedQuote(String nextLine, boolean inQuotes, int i) {
		return inQuotes // we are in quotes, therefore there can be escaped quotes in here.
				&& nextLine.length() > (i + 1) // there is indeed another  character to check.
				&& nextLine.charAt(i + 1) == recordDescriptor.getQuoteChar();
	}

	/**
	 * precondition: the current character is an escape
	 *
	 * @param nextLine the current line
	 * @param inQuotes true if the current context is quoted
	 * @param i        current index in line
	 * @return true if the following character is a quote
	 */
	protected boolean isNextCharacterEscapable(String nextLine, boolean inQuotes, int i) {
		return inQuotes // we are in quotes, therefore there can be escaped quotes in here.
				&& nextLine.length() > (i + 1) // there is indeed another character to check.
				&& (nextLine.charAt(i + 1) == recordDescriptor.getQuoteChar() || nextLine.charAt(i + 1) == recordDescriptor.getEscapeChar());
	}

}
