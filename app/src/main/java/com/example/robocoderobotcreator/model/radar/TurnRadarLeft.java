package com.example.robocoderobotcreator.model.radar;


import com.example.robocoderobotcreator.model.Category;
import com.example.robocoderobotcreator.model.ParametrizedBlock;

public class TurnRadarLeft extends ParametrizedBlock {
    public final Category CATEGORY = Category.RADAR;

    private String code = "turnRadarLeft($);";

    public TurnRadarLeft() {
        super(Category.RADAR);
    }

    @Override
    public String getCode() {
        return code;
    }
}
