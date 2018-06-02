import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvToDxf {

    private static final String SEP = ",";
    private DrawingConfig config;

    public CsvToDxf(DrawingConfig config) {
        this.config = config;
    }

    public static void main(String[] args) {
        String inputFilePath = "src/test/resources/test.csv"; // change later
        String outputFilePath = "src/main/resources/out.dxf"; // change later

        DrawingConfig config =  DrawingConfig.builder()
                .setInputPath(Paths.get(inputFilePath))
                .setOutputPath(Paths.get(outputFilePath))
                .setSeparator(SEP)
                .setTextHeight(1.0)
                .setDoPrintId(true)
                .setDoPrintCoords(true)
                .setDoPrintCode(true)
                .setDoPrintHeight(true)
                .setIs3D(true)
                .build();
        try {
            CsvToDxf converter = new CsvToDxf(config);
            List<String> lines = converter.readLines();
            String dxf = new DXF(config).createDxf(lines);
            converter.saveToFile(dxf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> readLines() {
        List<String> lines = new ArrayList<>();
        try(Stream<String> stream = Files.lines(this.config.getInputPath())) {
            lines = stream.collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public boolean saveToFile(String dxf) throws IOException {
        Files.write(this.config.getOutputPath(), dxf.getBytes());
        return true;
    }
}
