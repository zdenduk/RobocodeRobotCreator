package com.example.robocoderobotcreator.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Category {
    @JsonProperty("EVENTS")
    EVENTS,
    @JsonProperty("MOVEMENT")
    MOVEMENT,
    @JsonProperty("WEAPONS")
    WEAPONS,
    @JsonProperty("RADAR")
    RADAR
}
