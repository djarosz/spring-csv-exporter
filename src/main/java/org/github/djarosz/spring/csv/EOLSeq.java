package org.github.djarosz.spring.csv;

/**
 * Created: 2013-09-05 10:06
 *
 * @author Dawid Jarosz <dawid.jarosz@javart.eu>
 */
public enum EOLSeq {

	CR("\r"), LF("\n"), CRLF("\r\n");

	private String eol;

	private EOLSeq(String eol) {
		this.eol = eol;
	}

	public String eol() {
		return eol;
	}

}