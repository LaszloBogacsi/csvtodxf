package com.csvtodxf.drawingEntities;

public class SingleTextEntity extends DrawingEntity {
    private final String textContent;

    public String getTextContent() {
        return textContent;
    }

    public Double getTextHeight() {
        return textHeight;
    }

    private Double textHeight;

    public SingleTextEntity(Position position, String destinationLayer, String textContent, Double textHeight) {
        super(position, destinationLayer);
        this.textContent = textContent;
        this.textHeight = textHeight;
    }
}