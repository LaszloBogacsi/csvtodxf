package com.csvtodxf.file;

import org.hamcrest.core.IsNot;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class CsvFileReaderTest {
    private FileReader csvFileReader;
    private String SEPARATOR;

    @Before
    public void setUp() throws Exception {
        csvFileReader = new CsvFileReader();
        SEPARATOR = ",";
    }

    @Test
    public void shouldReadOneLine() {
        Path path = Paths.get("src/test/resources/oneLine.csv");
        List<CsvLine> expected = csvFileReader.readLine(path, SEPARATOR);
        List<CsvLine> actual = Collections.singletonList(new CsvLine("1", "1.234", "2.345", "10.001", "test code"));
        assertThat(expected.size(), is(not(0)));
        assertThat(expected.get(0), samePropertyValuesAs(actual.get(0)));
    }

    @Test
    public void shouldReadMultipleLines() {
        Path path = Paths.get("src/test/resources/multipleLines.csv");
        List<CsvLine> expected = csvFileReader.readLine(path, SEPARATOR);
        List<CsvLine> actual = Arrays.asList(
                new CsvLine("1", "1.234", "2.345", "10.001", "test code"),
                new CsvLine("2", "2.234", "3.345", "11.001", "test code2"));
        assertThat(expected.size(), is(2));
        assertThat(expected.get(1), samePropertyValuesAs(actual.get(1)));
    }

    @Test
    public void shouldReadMultipleLinesWithDifferentLineLength() {
        Path path = Paths.get("src/test/resources/multipleLinesDiffLineLength.csv");
        List<CsvLine> expected = csvFileReader.readLine(path, SEPARATOR);
        List<CsvLine> actual = Arrays.asList(
                new CsvLine("1", "1.234", "2.345"),
                new CsvLine("2", "2.234", "3.345", "11.001"),
                new CsvLine("3", "3.234", "4.345", "12.001", "test code"),
                new CsvLine("4", "4.234", "5.345","", "test code2"));
        assertThat(expected.size(), is(4));
        assertThat(expected.get(0), samePropertyValuesAs(actual.get(0)));
        assertThat(expected.get(1), samePropertyValuesAs(actual.get(1)));
        assertThat(expected.get(2), samePropertyValuesAs(actual.get(2)));
        assertThat(expected.get(3), samePropertyValuesAs(actual.get(3)));
    }

    @Test
    public void shouldReadLinesWithIncorrectSeparator() {
        Path path = Paths.get("src/test/resources/oneLine.csv");
        String INCORRECT_SEPARATOR = ";";
        List<CsvLine> expected = csvFileReader.readLine(path, INCORRECT_SEPARATOR);
        List<CsvLine> actual = Collections.singletonList(
                new CsvLine("1,1.234,2.345,10.001,test code"));
        assertThat(expected.size(), is(1));
        assertThat(expected.get(0), samePropertyValuesAs(actual.get(0)));
    }

    // filters out empty lines and trims whitespace
    @Test
    public void shouldIgnoreEmptyLines() {
        Path path = Paths.get("src/test/resources/emptyLinesWhiteSpaces.csv");
        List<CsvLine> expected = csvFileReader.readLine(path, SEPARATOR);
        List<CsvLine> actual = Arrays.asList(
                new CsvLine("1", "1.234", "2.345", "10.001", "test code"),
                new CsvLine("2", "2.234", "3.345", "11.001", "test code2"));
        assertThat(expected.size(), is(2));
        assertThat(expected.get(0), samePropertyValuesAs(actual.get(0)));
        assertThat(expected.get(1), samePropertyValuesAs(actual.get(1)));
    }

    // read only the first five rows (limit)
    @Test
    public void shouldReadMultipleLinesLimited() {
        Path path = Paths.get("src/test/resources/multipleLinesLimited.csv");
        List<CsvLine> expected = csvFileReader.readLine(path, SEPARATOR, 2);
        List<CsvLine> actual = Arrays.asList(
                new CsvLine("1", "1.234", "2.345", "10.001", "test code"),
                new CsvLine("2", "2.234", "3.345", "11.001", "test code2"));
        assertThat(expected.size(), is(2));
        assertThat(expected.get(0), samePropertyValuesAs(actual.get(0)));
        assertThat(expected.get(1), samePropertyValuesAs(actual.get(1)));
    }

    // TODO: Test: a line has more than 5 elements
    // TODO: Test: can not open file -> error handling

}