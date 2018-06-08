package com.csvtodxf.drawingEntities;

public class DrawingEntity {

    public String getDestinationLayer() {
        return destinationLayer;
    }

    public Position getPosition() {
        return position;
    }

    private Position position;
    private String destinationLayer; // the layer the entity will belong to

    // 3D entities
    public DrawingEntity(Position position, String destinationLayer) {
        this.position = position;
        this.destinationLayer = destinationLayer;

    }
}