package com.csvtodxf;

import com.csvtodxf.file.CsvLine;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class DXFTest {
    private final String HEADER = " 0\nSECTION\n 2\nENTITIES\n";
    private final String FOOTER = "\n 0\nENDSEC\n 0\nEOF";
    private final Map<String, String> DefaultLayerNames = new HashMap<>();
    private final String DEFAULT_HEIGHT = "0.0";

    @Before
    public void setUp() throws Exception {
        DefaultLayerNames.put("points", "Points");
        DefaultLayerNames.put("pointId", "Point_id");
        DefaultLayerNames.put("height", "Height");
        DefaultLayerNames.put("coords", "Coords");
        DefaultLayerNames.put("code", "Code");
        DefaultLayerNames.put("noCode", "Unknown_Code");
    }

    private String expectedResult(String entities) {
        return HEADER + entities + FOOTER;
    }

    private String writePoint(String layerName, String posE, String posN, String posH) {
        return String.format(" 0\nPOINT\n 8\n%s\n 10\n%s\n 20\n%s\n 30\n%s", layerName, posE, posN, posH);
    }

    private String writeText(String layerName, String posE, String posN, String posH, String textHeight, String text) {
        return String.format(" 0\nTEXT\n 8\n%s\n 10\n%s\n 20\n%s\n 30\n%s\n 40\n%s\n 1\n%s", layerName, posE, posN, posH, textHeight, text);
    }

// Basic tests, Happy path

    @Test
    public void shouldCreateDXFStringFromEmptyList() {
        DXF dxf = new DXF(DrawingConfig.builder().build());
        String result = dxf.createDxf(Collections.emptyList());
        assertEquals(expectedResult(""), result);
    }

    // Default Layer, 2D
    @Test
    public void shouldCreateOneLineDXFStringOnlyPoint() {
        DrawingConfig config = DrawingConfig.builder().build();
        DXF dxf = new DXF(config);
        List<CsvLine> lines = Collections.singletonList(new CsvLine("1", "1.234", "2.345"));
        String result = dxf.createDxf(lines);

        assertEquals(expectedResult(
                writePoint("Points", "1.234", "2.345", DEFAULT_HEIGHT)
        ), result);
    }

    @Test
    public void shouldCreateOneLineDXFStringPointAndPointId() {
        DrawingConfig config = DrawingConfig.builder()
                .setDoPrintId(true)
                .setTextHeight(1.0)
                .build();
        DXF dxf = new DXF(config);
        List<CsvLine> lines = Collections.singletonList(new CsvLine("1", "1.001", "5.001"));
        String result = dxf.createDxf(lines);

        assertEquals(expectedResult(
                writePoint(DefaultLayerNames.get("points"), "1.001", "5.001", DEFAULT_HEIGHT) + "\n" +
                        writeText(DefaultLayerNames.get("pointId"), "2.001", "5.501", DEFAULT_HEIGHT, "1.0", "1")
        ), result);
    }

    @Test
    public void shouldCreateOneLineDXFStringPointAndHeight() {
        DrawingConfig config = DrawingConfig.builder()
                .setDoPrintHeight(true)
                .setTextHeight(1.0)
                .build();
        DXF dxf = new DXF(config);
        List<CsvLine> lines = Collections.singletonList(new CsvLine("1", "1.001", "5.001", "10.001"));
        String result = dxf.createDxf(lines);

        assertEquals(expectedResult(
                writePoint(DefaultLayerNames.get("points"), "1.001", "5.001", DEFAULT_HEIGHT) + "\n" +
                        writeText(DefaultLayerNames.get("height"), "2.001", "5.501", DEFAULT_HEIGHT, "1.0", "10.001")
        ), result);
    }

    @Test
    public void shouldCreateOneLineDXFStringPointAndCoords() {
        DrawingConfig config = DrawingConfig.builder()
                .setDoPrintCoords(true)
                .setTextHeight(1.0)
                .build();
        DXF dxf = new DXF(config);
        List<CsvLine> lines = Collections.singletonList(new CsvLine("1", "1.001", "5.001"));
        String result = dxf.createDxf(lines);

        assertEquals(expectedResult(
                writePoint(DefaultLayerNames.get("points"), "1.001", "5.001", DEFAULT_HEIGHT) + "\n" +
                        writeText(DefaultLayerNames.get("coords"), "2.001", "5.501", DEFAULT_HEIGHT, "1.0", "E=1.001 N=5.001")
        ), result);
    }

    @Test
    public void shouldCreateOneLineDXFStringPointAndCode() {
        DrawingConfig config = DrawingConfig.builder()
                .setDoPrintCode(true)
                .setTextHeight(1.0)
                .build();
        DXF dxf = new DXF(config);
        List<CsvLine> lines = Collections.singletonList(new CsvLine("1", "1.001", "5.001", "", "test code"));
        String result = dxf.createDxf(lines);

        assertEquals(expectedResult(
                writePoint(DefaultLayerNames.get("points"), "1.001", "5.001", DEFAULT_HEIGHT) + "\n" +
                        writeText(DefaultLayerNames.get("code"), "2.001", "5.501", DEFAULT_HEIGHT, "1.0", "test code")
        ), result);
    }

    @Test
    public void shouldCreateOneLineDXFStringAllAtributes() {
        DrawingConfig config = DrawingConfig.builder()
                .setDoPrintId(true)
                .setDoPrintHeight(true)
                .setDoPrintCoords(true)
                .setDoPrintCode(true)
                .setTextHeight(1.0)
                .build();
        DXF dxf = new DXF(config);
        List<CsvLine> lines = Collections.singletonList(new CsvLine("1", "1.001", "5.001", "10.001", "test code"));
        String result = dxf.createDxf(lines);

        assertEquals(expectedResult(
                writePoint(DefaultLayerNames.get("points"), "1.001", "5.001", DEFAULT_HEIGHT) + "\n" +
                        writeText(DefaultLayerNames.get("pointId"), "2.001", "5.501", DEFAULT_HEIGHT, "1.0", "1") + "\n" +
                        writeText(DefaultLayerNames.get("height"), "2.001", "3.501", DEFAULT_HEIGHT, "1.0", "10.001") + "\n" +
                        writeText(DefaultLayerNames.get("coords"), "2.001", "2.001", DEFAULT_HEIGHT, "1.0", "E=1.001 N=5.001") + "\n" +
                        writeText(DefaultLayerNames.get("code"), "2.001", "0.501", DEFAULT_HEIGHT, "1.0", "test code")
        ), result);
    }

    // Layer by code
    @Test
    public void shouldCreateOneLineDXFStringAllAtributesLayerByCode() {
        DrawingConfig config = DrawingConfig.builder()
                .setDoPrintId(true)
                .setDoPrintHeight(true)
                .setDoPrintCoords(true)
                .setDoPrintCode(true)
                .setLayerByCode(true)
                .setTextHeight(1.0)
                .build();
        DXF dxf = new DXF(config);
        List<CsvLine> lines = Collections.singletonList(new CsvLine("1", "1.001", "5.001", "10.001", "test code"));
        String result = dxf.createDxf(lines);

        assertEquals(expectedResult(
                writePoint("test code", "1.001", "5.001", DEFAULT_HEIGHT) + "\n" +
                        writeText("test code", "2.001", "5.501", DEFAULT_HEIGHT, "1.0", "1") + "\n" +
                        writeText("test code", "2.001", "3.501", DEFAULT_HEIGHT, "1.0", "10.001") + "\n" +
                        writeText("test code", "2.001", "2.001", DEFAULT_HEIGHT, "1.0", "E=1.001 N=5.001") + "\n" +
                        writeText("test code", "2.001", "0.501", DEFAULT_HEIGHT, "1.0", "test code")
        ), result);
    }
// Default layer 3D
    @Test
    public void shouldCreateOneLineDXFStringAllAtributes3D() {
        DrawingConfig config = DrawingConfig.builder()
                .setDoPrintId(true)
                .setDoPrintHeight(true)
                .setDoPrintCoords(true)
                .setDoPrintCode(true)
                .setIs3D(true)
                .setTextHeight(1.0)
                .build();
        DXF dxf = new DXF(config);
        List<CsvLine> lines = Collections.singletonList(new CsvLine("1", "1.001", "5.001", "10.001", "test code"));
        String result = dxf.createDxf(lines);

        assertEquals(expectedResult(
                writePoint(DefaultLayerNames.get("points"), "1.001", "5.001", "10.001") + "\n" +
                        writeText(DefaultLayerNames.get("pointId"), "2.001", "5.501", "10.001", "1.0", "1") + "\n" +
                        writeText(DefaultLayerNames.get("height"), "2.001", "3.501", "10.001", "1.0", "10.001") + "\n" +
                        writeText(DefaultLayerNames.get("coords"), "2.001", "2.001", "10.001", "1.0", "E=1.001 N=5.001") + "\n" +
                        writeText(DefaultLayerNames.get("code"), "2.001", "0.501", "10.001", "1.0", "test code")
        ), result);
    }

    @Test
    public void shouldCreateMultipleLineDXFStringAllAtributes3D() {
        DrawingConfig config = DrawingConfig.builder()
                .setDoPrintId(true)
                .setDoPrintHeight(true)
                .setDoPrintCoords(true)
                .setDoPrintCode(true)
                .setIs3D(true)
                .setTextHeight(1.0)
                .build();
        DXF dxf = new DXF(config);
        List<CsvLine> lines = Arrays.asList(
                new CsvLine("1", "1.001", "5.001", "10.001", "test code1"),
                new CsvLine("1", "2.001", "6.001", "11.001", "test code2"));
        String result = dxf.createDxf(lines);

        assertEquals(expectedResult(
                writePoint(DefaultLayerNames.get("points"), "1.001", "5.001", "10.001") + "\n" +
                        writeText(DefaultLayerNames.get("pointId"), "2.001", "5.501", "10.001", "1.0", "1") + "\n" +
                        writeText(DefaultLayerNames.get("height"), "2.001", "3.501", "10.001", "1.0", "10.001") + "\n" +
                        writeText(DefaultLayerNames.get("coords"), "2.001", "2.001", "10.001", "1.0", "E=1.001 N=5.001") + "\n" +
                        writeText(DefaultLayerNames.get("code"), "2.001", "0.501", "10.001", "1.0", "test code1") + "\n" +

                        writePoint(DefaultLayerNames.get("points"), "2.001", "6.001", "11.001") + "\n" +
                        writeText(DefaultLayerNames.get("pointId"), "3.001", "6.501", "11.001", "1.0", "1") + "\n" +
                        writeText(DefaultLayerNames.get("height"), "3.001", "4.501", "11.001", "1.0", "11.001") + "\n" +
                        writeText(DefaultLayerNames.get("coords"), "3.001", "3.001", "11.001", "1.0", "E=2.001 N=6.001") + "\n" +
                        writeText(DefaultLayerNames.get("code"), "3.001", "1.501", "11.001", "1.0", "test code2")
        ), result);
    }

// Non happy path
    // printheight = true but no height value present => print default height
    @Test
    public void shouldCreateOneLineDXFStringNoHeightValuePresent() {
        DrawingConfig config = DrawingConfig.builder()
                .setDoPrintId(true)
                .setDoPrintHeight(true)
                .setDoPrintCoords(true)
                .setDoPrintCode(true)
                .setTextHeight(1.0)
                .build();
        DXF dxf = new DXF(config);
        List<CsvLine> lines = Collections.singletonList(new CsvLine("1", "1.001", "5.001", "", "test code"));
        String result = dxf.createDxf(lines);

        assertEquals(expectedResult(
                writePoint(DefaultLayerNames.get("points"), "1.001", "5.001", DEFAULT_HEIGHT) + "\n" +
                        writeText(DefaultLayerNames.get("pointId"), "2.001", "5.501", DEFAULT_HEIGHT, "1.0", "1") + "\n" +
                        writeText(DefaultLayerNames.get("height"), "2.001", "3.501", DEFAULT_HEIGHT, "1.0", "0.0") + "\n" +
                        writeText(DefaultLayerNames.get("coords"), "2.001", "2.001", DEFAULT_HEIGHT, "1.0", "E=1.001 N=5.001") + "\n" +
                        writeText(DefaultLayerNames.get("code"), "2.001", "0.501", DEFAULT_HEIGHT, "1.0", "test code")
        ), result);
    }
    // is3D = true but no height value present => entity height is default height.
    @Test
    public void shouldCreate3DOneLineDXFStringNoHeightValuePresent() {
        DrawingConfig config = DrawingConfig.builder()
                .setDoPrintId(true)
                .setDoPrintHeight(true)
                .setDoPrintCoords(true)
                .setDoPrintCode(true)
                .setIs3D(true)
                .setTextHeight(1.0)
                .build();
        DXF dxf = new DXF(config);
        List<CsvLine> lines = Collections.singletonList(new CsvLine("1", "1.001", "5.001", "", "test code"));
        String result = dxf.createDxf(lines);

        assertEquals(expectedResult(
                writePoint(DefaultLayerNames.get("points"), "1.001", "5.001", DEFAULT_HEIGHT) + "\n" +
                        writeText(DefaultLayerNames.get("pointId"), "2.001", "5.501", DEFAULT_HEIGHT, "1.0", "1") + "\n" +
                        writeText(DefaultLayerNames.get("height"), "2.001", "3.501", DEFAULT_HEIGHT, "1.0", "0.0") + "\n" +
                        writeText(DefaultLayerNames.get("coords"), "2.001", "2.001", DEFAULT_HEIGHT, "1.0", "E=1.001 N=5.001") + "\n" +
                        writeText(DefaultLayerNames.get("code"), "2.001", "0.501", DEFAULT_HEIGHT, "1.0", "test code")
        ), result);
    }
    // print code = true but no code value present => print default code ("")
    @Test
    public void shouldCreateOneLineDXFStringNoCodeValuePresent() {
        DrawingConfig config = DrawingConfig.builder()
                .setDoPrintId(true)
                .setDoPrintHeight(true)
                .setDoPrintCoords(true)
                .setDoPrintCode(true)
                .setIs3D(true)
                .setTextHeight(1.0)
                .build();
        DXF dxf = new DXF(config);
        List<CsvLine> lines = Collections.singletonList(new CsvLine("1", "1.001", "5.001", "", ""));
        String result = dxf.createDxf(lines);

        assertEquals(expectedResult(
                writePoint(DefaultLayerNames.get("points"), "1.001", "5.001", DEFAULT_HEIGHT) + "\n" +
                        writeText(DefaultLayerNames.get("pointId"), "2.001", "5.501", DEFAULT_HEIGHT, "1.0", "1") + "\n" +
                        writeText(DefaultLayerNames.get("height"), "2.001", "3.501", DEFAULT_HEIGHT, "1.0", "0.0") + "\n" +
                        writeText(DefaultLayerNames.get("coords"), "2.001", "2.001", DEFAULT_HEIGHT, "1.0", "E=1.001 N=5.001") + "\n" +
                        writeText(DefaultLayerNames.get("code"), "2.001", "0.501", DEFAULT_HEIGHT, "1.0", "")
        ), result);
    }

    // layer by code = true but no code value present => place entity on default code layer (Unknown_Code)

    @Test
    public void shouldCreateOneLineDXFStringLayerByCodeNoCodeValuePresent() {
        DrawingConfig config = DrawingConfig.builder()
                .setDoPrintId(true)
                .setDoPrintHeight(true)
                .setDoPrintCoords(true)
                .setDoPrintCode(true)
                .setIs3D(true)
                .setLayerByCode(true)
                .setTextHeight(1.0)
                .build();
        DXF dxf = new DXF(config);
        List<CsvLine> lines = Collections.singletonList(new CsvLine("1", "1.001", "5.001", "", ""));
        String result = dxf.createDxf(lines);

        assertEquals(expectedResult(
                writePoint(DefaultLayerNames.get("noCode"), "1.001", "5.001", DEFAULT_HEIGHT) + "\n" +
                        writeText(DefaultLayerNames.get("noCode"), "2.001", "5.501", DEFAULT_HEIGHT, "1.0", "1") + "\n" +
                        writeText(DefaultLayerNames.get("noCode"), "2.001", "3.501", DEFAULT_HEIGHT, "1.0", "0.0") + "\n" +
                        writeText(DefaultLayerNames.get("noCode"), "2.001", "2.001", DEFAULT_HEIGHT, "1.0", "E=1.001 N=5.001") + "\n" +
                        writeText(DefaultLayerNames.get("noCode"), "2.001", "0.501", DEFAULT_HEIGHT, "1.0", "")
        ), result);
    }
}