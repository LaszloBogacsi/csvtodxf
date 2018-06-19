package com.csvtodxf.drawingEntities;

public enum EntityType {
    POINTS("points"),
    POINT_ID("point_id"),
    HEIGHT("height"),
    COORDS("coords"),
    CODE("code");

    private final String type;

    EntityType(String type) {
        this.type = type;
    }
}
