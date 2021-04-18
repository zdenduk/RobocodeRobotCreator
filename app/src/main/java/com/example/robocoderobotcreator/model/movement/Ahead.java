package com.example.robocoderobotcreator.model.movement;


import com.example.robocoderobotcreator.model.Category;
import com.example.robocoderobotcreator.model.ParametrizedBlock;

public class Ahead extends ParametrizedBlock {

    private String code = "ahead($);";

    public Ahead() {
        super(Category.MOVEMENT);
    }

    @Override
    public String getCode() {
        return code;
    }
}
