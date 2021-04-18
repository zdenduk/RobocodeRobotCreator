package com.example.robocoderobotcreator.model.movement;


import com.example.robocoderobotcreator.model.Category;
import com.example.robocoderobotcreator.model.ParametrizedBlock;

public class Back extends ParametrizedBlock {

    private String code = "back($);";

    public Back() {
        super(Category.MOVEMENT);
    }

    @Override
    public String getCode() {
        return code;
    }
}
