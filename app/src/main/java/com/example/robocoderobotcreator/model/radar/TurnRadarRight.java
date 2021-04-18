package com.example.robocoderobotcreator.model.radar;


import com.example.robocoderobotcreator.model.Category;
import com.example.robocoderobotcreator.model.ParametrizedBlock;

public class TurnRadarRight extends ParametrizedBlock {

    private String code = "turnRadarLeft($);";

    public TurnRadarRight() {
        super(Category.RADAR);
    }

    @Override
    public String getCode() {
        return code;
    }
}