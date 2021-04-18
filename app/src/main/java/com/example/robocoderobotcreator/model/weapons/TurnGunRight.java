package com.example.robocoderobotcreator.model.weapons;

import com.example.robocoderobotcreator.model.Category;
import com.example.robocoderobotcreator.model.ParametrizedBlock;

public class TurnGunRight extends ParametrizedBlock {

    private String code = "turnGunRight($);";

    public TurnGunRight() {
        super(Category.WEAPONS);
    }

    @Override
    public String getCode() {
        return code;
    }
}
