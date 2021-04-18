package com.example.robocoderobotcreator.model.movement;

import com.example.robocoderobotcreator.model.Category;
import com.example.robocoderobotcreator.model.ParametrizedBlock;

public class TurnLeft extends ParametrizedBlock {

    private String code = "turnLeft($);";

    public TurnLeft() {
        super(Category.MOVEMENT);
    }

    @Override
    public String getCode() {
        return code;
    }
}
