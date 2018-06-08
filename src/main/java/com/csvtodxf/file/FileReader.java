package com.csvtodxf.file;

import java.util.List;

public interface FileReader {
    List<CsvLine> readLine(String uri, String separator);
    List<CsvLine> readBeginning(String uri, int limit, String separator);
}
