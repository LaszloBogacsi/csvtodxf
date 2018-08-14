package com.csvtodxf;

import com.csvtodxf.drawingEntities.EntityType;
import com.csvtodxf.drawingEntities.PointDrawingEntity;
import com.csvtodxf.drawingEntities.Position;
import com.csvtodxf.drawingEntities.SingleTextEntity;
import com.csvtodxf.file.CsvLine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DXF {
    private static final double DEFAULT_HEIGHT = 0.0;
    private static final double GENERAL_SPACING = 1.5;
    private static final double FIRST_LINE_SPACING = 0.5;
    private static final double[] LINE_OFFSETS = {1, -1, -2, -3};
    private final String HEADER = " 0\nSECTION\n 2\nENTITIES\n";
    private final String FOOTER = "\n 0\nENDSEC\n 0\nEOF";
    private final DrawingConfig config;
    private static Map<EntityType, String> defaultLayerNames = initDefaultLayerNamesMap();
    private double[] linePositions;

    public DXF(DrawingConfig config) {
        this.config = config;
        this.linePositions = getLinepositions(config);
    }

    public String createDxf(List<CsvLine> lines) {

        double textHeight = this.config.getTextHeight();

        // Assumed order of parameters in line: P,E,N,H,C
        String entities = lines.stream().map(line -> {
            StringBuilder sb = new StringBuilder();
            Position position;
            if (config.isIs3D()) {
                double positionH = Double.parseDouble(line.getLineElement3().orElse(String.valueOf(DEFAULT_HEIGHT)));
                position = new Position(Double.parseDouble(line.getLineElement1()), Double.parseDouble(line.getLineElement2()), positionH);
            } else {
                position = new Position(Double.parseDouble(line.getLineElement1()), Double.parseDouble(line.getLineElement2()), DEFAULT_HEIGHT);
            }

            PointDrawingEntity point = new PointDrawingEntity(position, getLayerNameFor(EntityType.POINTS, line));
            sb.append(printPointDrawingEntity(point));

            if (config.doPrintId()) {
                String id = line.getLineElement();
                double idLinePosition = linePositions[0];
                SingleTextEntity pointIdText = new SingleTextEntity(position, getLayerNameFor(EntityType.POINT_ID, line), id, textHeight);
                sb.append("\n").append(printSingleTextDrawingEntity(pointIdText, textHeight, textHeight * idLinePosition));
            }

            if (config.doPrintHeight()) {
                double heightLinePosition = linePositions[1];
                String heightDisplayValue = line.getLineElement3().orElse(String.valueOf(DEFAULT_HEIGHT));
                SingleTextEntity heightText = new SingleTextEntity(position, getLayerNameFor(EntityType.HEIGHT, line), heightDisplayValue, textHeight);
                sb.append("\n").append(printSingleTextDrawingEntity(heightText, textHeight,(textHeight * heightLinePosition)));
            }

            if (config.doPrintCoords()) {
                double coordsLinePosition = linePositions[2];
                String coordinateTextContent = "E=" + position.getE() + " N=" + position.getN();
                SingleTextEntity coordinateText = new SingleTextEntity(position, getLayerNameFor(EntityType.COORDS, line), coordinateTextContent, textHeight);
                sb.append("\n").append(printSingleTextDrawingEntity(coordinateText, textHeight, (textHeight * coordsLinePosition)));
            }

            if (config.doPrintCode()) {
                double codeLinePosition = linePositions[3];
                String code = line.getLineElement4().orElse("");
                SingleTextEntity codeText = new SingleTextEntity(position, getLayerNameFor(EntityType.CODE, line), code, textHeight);
                sb.append("\n").append(printSingleTextDrawingEntity(codeText, textHeight, (textHeight * codeLinePosition)));
            }

            return sb.toString();
        }).collect(Collectors.joining("\n"));

        return HEADER + entities + FOOTER;
    }

    private static double[] getLinepositions(DrawingConfig config) {
        // item order is the order of printed attributes, item order matters
        boolean[] doPrintLines = {
                config.doPrintId(),
                config.doPrintHeight(),
                config.doPrintCoords(),
                config.doPrintCode()
        };

        return IntStream.range(0, LINE_OFFSETS.length).mapToDouble(position -> {
            int offsetIndex = position - numberOfFalsesBefore(position, doPrintLines);
            boolean isFirstLine = offsetIndex == 0;
            if(isFirstLine) {
                return LINE_OFFSETS[offsetIndex] * FIRST_LINE_SPACING;
            }
            return LINE_OFFSETS[offsetIndex] * GENERAL_SPACING;
        }).toArray();
    }

    private static int numberOfFalsesBefore(int i, boolean[] booleans) {
        return (int) IntStream.range(0,i).mapToObj(n -> booleans[n]).filter(isFalse -> !isFalse).count();
    }

    private static Map<EntityType, String> initDefaultLayerNamesMap() {
        Map<EntityType, String> map = new HashMap<>();
        map.put(EntityType.POINTS, "Points");
        map.put(EntityType.POINT_ID, "Point_id");
        map.put(EntityType.HEIGHT, "Height");
        map.put(EntityType.COORDS, "Coords");
        map.put(EntityType.CODE, "Code");
        return map;
    }

    private String getLayerNameFor(EntityType entityType, CsvLine line) {
        if (this.config.isLayerByCode()) {
            return line.getLineElement4().orElse("Unknown_Code");
        }
        return defaultLayerNames.get(entityType);
    }


    private String printPointDrawingEntity(PointDrawingEntity entity) {
        return StringTemplate.getPointStringTemplate()
                .replaceAll("\\{positionE}", String.valueOf(entity.getPosition().getE()))
                .replaceAll("\\{positionN}", String.valueOf(entity.getPosition().getN()))
                .replaceAll("\\{positionH}", String.valueOf(entity.getPosition().getH()))
                .replaceAll("\\{layerName}", entity.getDestinationLayer());
    }

    private String printSingleTextDrawingEntity(SingleTextEntity entity,  double offsetE, double offsetN) {
        return StringTemplate.getSingleTextStringTemplate()
                .replaceAll("\\{positionE}", String.valueOf(roundFloatingPointErrors(entity.getPosition().getE() + offsetE)))
                .replaceAll("\\{positionN}", String.valueOf(roundFloatingPointErrors(entity.getPosition().getN() + offsetN)))
                .replaceAll("\\{positionH}", String.valueOf(entity.getPosition().getH()))
                .replaceAll("\\{layerName}", entity.getDestinationLayer())
                .replaceAll("\\{textHeight}", String.valueOf(entity.getTextHeight()))
                .replaceAll("\\{text}", entity.getTextContent());
    }

    private double roundFloatingPointErrors(double number) {
        return Math.round(number * 1000) / 1000.0; // this infers the coordinates are in meter with mm precision: xx.xxx m
    }
}
