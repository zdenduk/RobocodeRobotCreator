package com.example.robocoderobotcreator.model.movement;


import com.example.robocoderobotcreator.model.Category;
import com.example.robocoderobotcreator.model.ParametrizedBlock;

public class TurnRight extends ParametrizedBlock {

    private String code = "turnRight($);";

    public TurnRight() {
        super(Category.MOVEMENT);
    }

    @Override
    public String getCode() {
        return code;
    }
}
