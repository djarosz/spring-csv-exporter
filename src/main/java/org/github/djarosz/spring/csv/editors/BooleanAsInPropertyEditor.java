package org.github.djarosz.spring.csv.editors;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.apache.commons.lang.StringUtils.isBlank;

import java.beans.PropertyEditorSupport;
import org.apache.commons.lang.BooleanUtils;

public class BooleanAsInPropertyEditor extends PropertyEditorSupport {

	private String trueValue = "true";

	private String falseValue = "false";

	public BooleanAsInPropertyEditor() {
	}

	public BooleanAsInPropertyEditor(String trueValue, String falseValue) {
		this.trueValue = trueValue;
		this.falseValue = falseValue;
	}

	@Override
	public String getAsText() {
		Boolean value = (Boolean) getValue();

		if (value == null) {
			return EMPTY;
		}

		return BooleanUtils.toString(value, trueValue, falseValue, EMPTY);
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (isBlank(text)) {
			setValue(null);
		}

		setValue(trueValue.equals(text));
	}

}
