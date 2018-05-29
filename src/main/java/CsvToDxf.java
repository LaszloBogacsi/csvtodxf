import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvToDxf {

    private static final String SEP = ",";
    private Map<String, String> config;

    public CsvToDxf(Map<String, String> config) {

        this.config = config;
    }

    public static void main(String[] args) {
        String inputFilePath = "src/test/resources/test.csv"; // change later
        String outputFilePath = "src/main/resources/out.dxf"; // change later
        String textHeight = "1"; // change later
        String is3D = "false";
        String doPrintCode = "false";
        String doPrintheight = "false";

        Map<String, String> config = new HashMap<>();
        config.put("inputFilePath", inputFilePath);
        config.put("outputFilePath", outputFilePath);
        config.put("textHeight", textHeight);
        String doPrintCoords = "false";
        config.put("doPrintCoords", doPrintCoords);
        config.put("doPrintCode", doPrintCode);
        config.put("doPrintHeight", doPrintheight);
        config.put("is3D", is3D);

        try {
            CsvToDxf converter = new CsvToDxf(config);
            List<String> lines = converter.readLines();
            String dxf = converter.createDxf(lines);
            converter.saveToFile(dxf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public List<String> readLines() {
        List<String> lines = new ArrayList<>();
        try(Stream<String> stream = Files.lines(Paths.get(this.config.get("inputFilePath")))) {
            lines = stream.collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public String createDxf(List<String> lines) {
        String pointsLayerName = "Points";
        String pointIDLayerName = "Text_id";
        double textHeight = Double.parseDouble(this.config.get("textHeight"));
        String header = " 0\n" +
                "SECTION\n" +
                " 2\n" +
                "ENTITIES\n";
        String footer = "\n 0\n" +
                "ENDSEC\n" +
                " 0\n" +
                "EOF";

        String entities =  lines.stream().map(line -> {
            String [] input = line.split(SEP);
            String id = input[0];
            String easting = input[1];
            String northing = input[2];
            String height = input[3];
//            String code = input[4];
            double textEasting = Double.parseDouble(easting)  + textHeight;
            double textNorthing = Double.parseDouble(northing)  + (textHeight * 0.5);
            // create point
            return printPoint(easting, northing, height, pointsLayerName, 1)
                    + printPointId(easting, northing, id, pointIDLayerName, 1);
        }).collect(Collectors.joining("\n"));

        return header + entities + footer;
    }

    private String printPoint(String easting, String northing, String height, String layerName, int order) {

        return " 0\n" +
                "POINT\n" +
                " 8\n" +
                layerName + "\n" +
                " 10\n" +
                easting + "\n" +
                " 20\n" +
                northing + "\n" +
                " 30\n" +
                height + "\n";
                // create point id

    }

    private String printPointId(String easting, String northing, String id, String layerName, int order) {
        // print the point id to top right position, 0.0 height

        double textHeight = Double.parseDouble(this.config.get("textHeight"));
        double textEasting = Double.parseDouble(easting) + textHeight;
        double textNorthing = Double.parseDouble(northing)  + (textHeight * 0.5 * order);

        return " 0\n" +
                "TEXT\n" +
                " 8\n" +
                layerName + "\n" +
                " 10\n" +
                textEasting + "\n" +
                " 20\n" +
                textNorthing + "\n" +
                " 30\n" +
                "0.0\n" +
                " 40\n" +
                textHeight + "\n" +
                " 1\n" +
                id;
    }

    private String printHeight(String easting, String northing, String height, String layerName, int order) {
        // print the height information
        // order 2 -- when coords, order = 1 if no coords.

        // text position
        double textHeight = Double.parseDouble(this.config.get("textHeight"));
        double textEasting = Double.parseDouble(easting) + textHeight;
        double textNorthing = Double.parseDouble(northing)  - (textHeight * 0.5 * order);
        return  " 0\n" +
                "TEXT\n" +
                " 8\n" +
                layerName + "\n" +
                " 10\n" +
                textEasting + "\n" +
                " 20\n" +
                textNorthing + "\n" +
                " 30\n" +
                "0.0\n" +
                " 40\n" +
                textHeight + "\n" +
                " 1\n" +
                height;

    }

    private String printCoords(String easting, String northing, String layerName, int order) {
        // print the coordinates in one line
        // order = 1

        // text position
        double textHeight = Double.parseDouble(this.config.get("textHeight"));
        double textEasting = Double.parseDouble(easting) + textHeight;
        double textNorthing = Double.parseDouble(northing)  - (textHeight * 0.5 * order);
        return  " 0\n" +
                "TEXT\n" +
                " 8\n" +
                layerName + "\n" +
                // point id position
                " 10\n" +
                textEasting + "\n" +
                " 20\n" +
                textNorthing + "\n" +
                " 30\n" +
                "0.0\n" +
                " 40\n" +
                textHeight + "\n" +
                " 1\n" +
                "E=" + easting + "N=" + northing;
    }

    private String printCode(String easting, String northing, String code, String layerName, int order) {
        // print the code in one line
        // order = always the last

        // text position
        double textHeight = Double.parseDouble(this.config.get("textHeight"));
        double textEasting = Double.parseDouble(easting) + textHeight;
        double textNorthing = Double.parseDouble(northing)  - (textHeight * 0.5 * order);
        return  " 0\n" +
                "TEXT\n" +
                " 8\n" +
                layerName + "\n" +
                " 10\n" +
                textEasting + "\n" +
                " 20\n" +
                textNorthing + "\n" +
                " 30\n" +
                "0.0\n" +
                " 40\n" +
                textHeight + "\n" +
                " 1\n" +
                code;
    }

    public boolean saveToFile(String dxf) throws IOException {
        Files.write(Paths.get(this.config.get("outputFilePath")), dxf.getBytes());
        return true;
    }
}
