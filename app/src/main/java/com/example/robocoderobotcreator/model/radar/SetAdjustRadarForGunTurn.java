package com.example.robocoderobotcreator.model.radar;


import com.example.robocoderobotcreator.model.Block;
import com.example.robocoderobotcreator.model.Category;

public class SetAdjustRadarForGunTurn extends Block {

    private String code = "setAdjustRadarForGunTurn(true);";

    protected SetAdjustRadarForGunTurn() {
        super(Category.RADAR);
    }

    @Override
    public String getCode() {
        return code;
    }
}
