package com.example.robocoderobotcreator.model.weapons;

import com.example.robocoderobotcreator.model.Category;
import com.example.robocoderobotcreator.model.ParametrizedBlock;

public class Fire extends ParametrizedBlock {
    private String code = "fire($);";

    public Fire() {
        super(Category.WEAPONS);
    }

    @Override
    public String getCode() {
        return code;
    }
}
