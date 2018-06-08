import com.csvtodxf.CsvToDxf;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CsvToDxfTest {

    CsvToDxf converter;
    String inputFilePath = "src/test/resources/test.csv";
    String outputFilePath = "src/test/resources/out.dxf";

    @BeforeEach
    void setUp() {
        Map<String, String> config = new HashMap<>();
        config.put("inputFilePath", inputFilePath);
        config.put("outputFilePath", outputFilePath);
        config.put("textHeight", "1");
        config.put("doPrintCoords", "false");
        config.put("doPrintCode", "false");
        config.put("doPrintHeight", "false");
        config.put("is3D", "false");
        converter = new CsvToDxf(config);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.delete(Paths.get(outputFilePath));
    }

    @Test
    void canCreateDxfFileFromCsvFile() throws IOException {
        List<String> lines = converter.readLines();
        String dxf = converter.createDxf(lines);
        assertTrue(converter.saveToFile(dxf));
        assertEquals(dxf.length(), 184);
    }


}