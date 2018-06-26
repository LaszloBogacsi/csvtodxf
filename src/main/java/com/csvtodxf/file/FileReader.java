package com.csvtodxf.file;

import java.nio.file.Path;
import java.util.List;

public interface FileReader {
    List<CsvLine> readLine(Path path, String separator);
    List<CsvLine> readLine(Path path, String separator, int limit);
}
