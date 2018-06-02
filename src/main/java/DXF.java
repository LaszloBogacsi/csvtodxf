import drawingEntities.PointDrawingEntity;
import drawingEntities.Position;
import drawingEntities.SingleTextEntity;

import java.util.List;
import java.util.stream.Collectors;

public class DXF {
    private final String HEADER = " 0\nSECTION\n 2\nENTITIES\n";
    private final String FOOTER = "\n 0\nENDSEC\n 0\nEOF";
    private final DrawingConfig config;

    public DXF(DrawingConfig config) {
        this.config = config;
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

            PointDrawingEntity point = new PointDrawingEntity(position, pointsLayerName);
            sb.append(printPointDrawingEntity(point));

            if (config.doPrintId()) {
                String id = input[0];
                SingleTextEntity pointIdText = new SingleTextEntity(position, pointIDLayerName, id, textHeight);
                sb.append("\n").append(printSingleTextDrawingEntity(pointIdText, textHeight, textHeight * 0.5 * 1));
            }

            if (config.doPrintHeight()) {
                String heightDisplayValue = input.length > 3 ? input[3] : "0";
                SingleTextEntity heightText = new SingleTextEntity(position, heightLayerName, heightDisplayValue, textHeight);
                sb.append("\n").append(printSingleTextDrawingEntity(heightText, textHeight,(textHeight * 1.5 * -1)));
            }

            if (config.doPrintCoords()) {
                String coordinateTextContent = "E=" + position.getE() + " N=" + position.getN();
                SingleTextEntity coordinateText = new SingleTextEntity(position, coordinatesLayerName, coordinateTextContent, textHeight);
                sb.append("\n").append(printSingleTextDrawingEntity(coordinateText, textHeight, (textHeight * 1.5 * -2)));
            }

            if (config.doPrintCode()) {
                String code = input.length > 4 ? input[4] : "";
                SingleTextEntity codeText = new SingleTextEntity(position, codeLayerName, code, textHeight);
                sb.append("\n").append(printSingleTextDrawingEntity(codeText, textHeight, (textHeight * 1.5 * -3)));

            }

            return sb.toString();
        }).collect(Collectors.joining("\n"));

        return HEADER + entities + FOOTER;
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
