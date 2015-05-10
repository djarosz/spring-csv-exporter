package org.github.djarosz.spring.csv;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Iterator;
import org.apache.commons.lang.StringUtils;

public class CSVExporter<T> {

	protected EOLSeq eolSeq = EOLSeq.LF;

	protected String outputEncoding = "UTF-8";

	protected ObjectToCSVRecordConverter<T> converter;

	public void export(OutputStream ouputStream, Iterator<T> objectIterator) throws IOException {
		PrintWriter csvWriter = new PrintWriter(new OutputStreamWriter(ouputStream, outputEncoding));

		String header = converter.getRecordDescriptor().getHeader();
		if (StringUtils.isNotBlank(header)) {
			csvWriter.print(StringUtils.chomp(header).replaceAll("[ \t]+", " ") + eolSeq.eol());
		}

		while (objectIterator.hasNext()) {
			csvWriter.print(converter.toCvsRecord(objectIterator.next()) + eolSeq.eol());
		}

		csvWriter.close();
	}

	public ObjectToCSVRecordConverter<T> getConverter() {
		return converter;
	}

	public void setConverter(ObjectToCSVRecordConverter<T> converter) {
		this.converter = converter;
	}

	public EOLSeq getEolSeq() {
		return eolSeq;
	}

	public void setEolSeq(EOLSeq eolSeq) {
		this.eolSeq = eolSeq;
	}

	public String getOutputEncoding() {
		return outputEncoding;
	}

	public void setOutputEncoding(String outputEncoding) {
		this.outputEncoding = outputEncoding;
	}
}
