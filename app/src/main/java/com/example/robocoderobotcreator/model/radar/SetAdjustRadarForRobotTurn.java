package com.example.robocoderobotcreator.model.radar;

import com.example.robocoderobotcreator.model.Block;
import com.example.robocoderobotcreator.model.Category;

public class SetAdjustRadarForRobotTurn extends Block {
    public final Category CATEGORY = Category.RADAR;

    private String code = "setAdjustRadarForRobotTurn(true);";

    public SetAdjustRadarForRobotTurn() {
        super(Category.RADAR);
    }

    @Override
    public String getCode() {
        return code;
    }
}