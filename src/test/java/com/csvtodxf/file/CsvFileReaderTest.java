package com.csvtodxf.file;

import org.hamcrest.core.IsNot;
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

    @Test
    public void shouldReadOneLine() {
        FileReader csvFileReader = new CsvFileReader();
        Path path = Paths.get("src/test/resources/oneLine.csv");
        final String SEPARATOR = ",";
        List<CsvLine> expected = csvFileReader.readLine(path, SEPARATOR);
        List<CsvLine> actual = Collections.singletonList(new CsvLine("1", "1.234", "2.345", "10.001", "test code"));
        assertThat(expected.size(), is(not(0)));
        assertThat(expected.get(0), samePropertyValuesAs(actual.get(0)));
    }
}