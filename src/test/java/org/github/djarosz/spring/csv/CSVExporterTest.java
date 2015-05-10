package org.github.djarosz.spring.csv;

import static org.apache.commons.lang.StringUtils.chomp;
import static org.fest.assertions.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created: 2013-09-02 21:59
 *
 * @author Dawid Jarosz <dawid.jarosz@javart.eu>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class CSVExporterTest {

	// stringValue,integerValue,doubleValue,dateValue,booleanValue,enumValue,nestedObject.stringValue,nestedObject.dateValue
	private static final String CSV_LINE = "\"t.kowaslki ;\\\"dawid\\\"\";999;\"123.123\";\"2010-03-28T11:30:57\";true;AAA;;\"2013-03-28T11:30:57\"";

	@Autowired
	@Qualifier("toObjectWithGlobalEditors")
	private CSVRecordToObjectConverter<TestObject> toObjectWithGlobalEditors;

	@Autowired
	@Qualifier("toObjectWithFieldsDefined")
	private CSVRecordToObjectConverter<TestObject> toObjectWithFieldsDefined;

	@Autowired
	@Qualifier("toObjectWithCustomEditors")
	private CSVRecordToObjectConverter<TestObject> toObjectWithCustomEditors;

	@Autowired
	private ObjectToCSVRecordConverter<TestObject> toCsvRecord;

	@Autowired
	@Qualifier("csvExporter")
	private CSVExporter<TestObject> csvExporter;

	@Autowired
	@Qualifier("csvExporterFromFactoryBean")
	private CSVExporter<TestObject> csvExporterFromFactoryBean;

	@Autowired
	@Qualifier("csvExporterWithExpressions")
	private CSVExporter<TestObject> csvExporterWithExpressions;

	@Test
	public void testToObjectWithGlobalEditors() throws ParseException {
		performTest(toObjectWithGlobalEditors, CSV_LINE);
	}

	@Test
	public void testToObjectWithFieldsDefined() throws ParseException {
		performTest(toObjectWithFieldsDefined, CSV_LINE);
	}

	@Test
	public void testToObjectWithCustomEditors() throws ParseException {
		performTest(toObjectWithCustomEditors, CSV_LINE);
	}

	@Test
	public void testCSVWrite() throws ParseException {
		String result = toCsvRecord.toCvsRecord(performTest(toObjectWithCustomEditors, CSV_LINE));

		performTest(toObjectWithCustomEditors, result);
	}

	@Test
	public void testCSVExporter() throws Exception {
		List<TestObject> objects = Arrays.asList(
				performTest(toObjectWithCustomEditors, CSV_LINE),
				performTest(toObjectWithCustomEditors, CSV_LINE),
				performTest(toObjectWithCustomEditors, CSV_LINE)
		);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		csvExporter.export(baos, objects.iterator());

		String csvLines = new String(baos.toByteArray(), "UTF-8").replace("\"", "");
		String expectedLine = CSV_LINE.replace("\"", "");

		assertThat(csvLines).isEqualTo(expectedLine + csvExporter.getEolSeq().eol()
				+ expectedLine + csvExporter.getEolSeq().eol()
				+ expectedLine + csvExporter.getEolSeq().eol());
	}

	@Test
	public void testCSVExporterFromFactoryBean() throws Exception {
		List<TestObject> objects = Arrays.asList(
				performTest(toObjectWithCustomEditors, CSV_LINE),
				performTest(toObjectWithCustomEditors, CSV_LINE),
				performTest(toObjectWithCustomEditors, CSV_LINE)
		);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		csvExporterFromFactoryBean.export(baos, objects.iterator());

		String csvLines = new String(baos.toByteArray(), "UTF-8").replace("\"", "");
		String expectedLine = CSV_LINE.replace("\"", "");

		expectedLine += csvExporterFromFactoryBean.getEolSeq().eol()
				+ expectedLine + csvExporterFromFactoryBean.getEolSeq().eol()
				+ expectedLine + csvExporterFromFactoryBean.getEolSeq().eol();

		System.out.println(csvLines);
		System.out.println(expectedLine);

		assertThat(csvLines).isEqualTo(expectedLine);
	}

	@Test
	public void testCSVExporterWithExpresions() throws Exception {
		TestObject object = new TestObject();
		NestedTestObject nested = new NestedTestObject();
		Date date = new Date();
		String dateString = new SimpleDateFormat("yyyy-MM-dd+HHmmssSSS").format(date);

		object.setStringValue("abc");
		object.setIntegerValue(10);
		object.setBooleanValue(true);
		object.setDateValue(date);
		object.setNestedObject(nested);
		nested.setNestedStringValue("zyz");

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		csvExporterWithExpressions.export(baos, Arrays.asList(object).iterator());

		String csvLines = new String(baos.toByteArray(), "UTF-8");
		assertThat(chomp(csvLines)).isEqualTo("abc-zyz;20;statictext;true;;" + dateString + ";0;");
	}

	private TestObject performTest(CSVRecordToObjectConverter<TestObject> parser, String csvData) throws ParseException {
		TestObject data = parser.toObject(csvData);

		assertThat(data.getStringValue()).isEqualTo("t.kowaslki ;\"dawid\"");
		assertThat(data.getIntegerValue()).isEqualTo(999);
		assertThat(data.getDoubleValue()).isEqualTo(123.123);
		assertThat(data.getEnumValue()).isEqualTo(TestObject.TestEnum.AAA);
		assertThat(data.getDateValue()).isEqualTo(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse("2010-03-28T11:30:57"));
		assertThat(data.getDateValue()).isEqualTo(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse("2010-03-28T11:30:57"));
		assertThat(data.getNestedObject().getNestedDateValue()).isEqualTo(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse("2013-03-28T11:30:57"));
		assertThat(data.getNestedObject().getNestedStringValue()).isNull();

		return data;
	}
}
