package com.csvtodxf;

import com.csvtodxf.drawingEntities.EntityType;
import com.csvtodxf.drawingEntities.PointDrawingEntity;
import com.csvtodxf.drawingEntities.Position;
import com.csvtodxf.drawingEntities.SingleTextEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DXF {
    private final String HEADER = " 0\nSECTION\n 2\nENTITIES\n";
    private final String FOOTER = "\n 0\nENDSEC\n 0\nEOF";
    private final DrawingConfig config;
    private Map<EntityType, String> defaultLayerNames = new HashMap<>();

    public DXF(DrawingConfig config) {
        this.config = config;
        initDefaultLayerNamesMap();
    }

    public String createDxf(List<String> lines) {

        String pointsLayerName = "Points";
        String pointIDLayerName = "Text_id";
        String heightLayerName = "Height";
        String coordinatesLayerName = "Coords";
        String codeLayerName = "Code";
        double textHeight = this.config.getTextHeight();

        // Assumed order of parameters in line: P,E,N,H,C
        String entities =  lines.stream().filter(line -> !line.isEmpty()).map(line -> {
            StringBuilder sb = new StringBuilder();
            String [] input = line.split(this.config.getSeparator());

            Position position;
            if (config.isIs3D()) {
                double positionH = input.length > 3 ? Double.parseDouble(input[3]) : 0.0;
                position = new Position(Double.parseDouble(input[1]), Double.parseDouble(input[2]), positionH);
            } else {
                position = new Position(Double.parseDouble(input[1]), Double.parseDouble(input[2]), 0.0);
            }

            if(this.config.isLayerByCode()) {

//                pointsLayerName = pointIDLayerName = heightLayerName = coordinatesLayerName = codeLayerName;
            }

            PointDrawingEntity point = new PointDrawingEntity(position, getLayerNameFor(EntityType.POINTS, input));
            sb.append(printPointDrawingEntity(point));

            if (config.doPrintId()) {
                String id = input[0];
                SingleTextEntity pointIdText = new SingleTextEntity(position, getLayerNameFor(EntityType.POINT_ID, input), id, textHeight);
                sb.append("\n").append(printSingleTextDrawingEntity(pointIdText, textHeight, textHeight * 0.5 * 1));
            }

            if (config.doPrintHeight()) {
                String heightDisplayValue = input.length > 3 ? input[3] : "0";
                SingleTextEntity heightText = new SingleTextEntity(position, getLayerNameFor(EntityType.HEIGHT, input), heightDisplayValue, textHeight);
                sb.append("\n").append(printSingleTextDrawingEntity(heightText, textHeight,(textHeight * 1.5 * -1)));
            }

            if (config.doPrintCoords()) {
                String coordinateTextContent = "E=" + position.getE() + " N=" + position.getN();
                SingleTextEntity coordinateText = new SingleTextEntity(position, getLayerNameFor(EntityType.COORDS, input), coordinateTextContent, textHeight);
                sb.append("\n").append(printSingleTextDrawingEntity(coordinateText, textHeight, (textHeight * 1.5 * -2)));
            }

            if (config.doPrintCode()) {
                String code = input.length > 4 ? input[4] : "";
                SingleTextEntity codeText = new SingleTextEntity(position, getLayerNameFor(EntityType.CODE, input), code, textHeight);
                sb.append("\n").append(printSingleTextDrawingEntity(codeText, textHeight, (textHeight * 1.5 * -3)));

            }

            return sb.toString();
        }).collect(Collectors.joining("\n"));

        return HEADER + entities + FOOTER;
    }

    private void initDefaultLayerNamesMap() {
        this.defaultLayerNames.put(EntityType.POINTS, "Points");
        this.defaultLayerNames.put(EntityType.POINT_ID, "Point_id");
        this.defaultLayerNames.put(EntityType.HEIGHT, "Height");
        this.defaultLayerNames.put(EntityType.COORDS, "Coords");
        this.defaultLayerNames.put(EntityType.CODE, "Code");
    }

    private String getLayerNameFor(EntityType entityType, String[] input) {
        if (this.config.isLayerByCode()) {
            if (input.length > 4) {
                return input[4];
            } else {
                return "Unknown_Code";
            }
        }
        return this.defaultLayerNames.get(entityType);
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
                .replaceAll("\\{positionE}", String.valueOf(entity.getPosition().getE() + offsetE))
                .replaceAll("\\{positionN}", String.valueOf(entity.getPosition().getN() + offsetN))
                .replaceAll("\\{positionH}", String.valueOf(entity.getPosition().getH()))
                .replaceAll("\\{layerName}", entity.getDestinationLayer())
                .replaceAll("\\{textHeight}", String.valueOf(entity.getTextHeight()))
                .replaceAll("\\{text}", entity.getTextContent());
    }
}
