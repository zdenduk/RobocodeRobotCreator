package com.example.robocoderobotcreator.model.weapons;


import com.example.robocoderobotcreator.model.Category;
import com.example.robocoderobotcreator.model.ParametrizedBlock;

public class TurnGunLeft extends ParametrizedBlock {

    private String code = "turnGunLeft($);";

    public TurnGunLeft() {
        super(Category.WEAPONS);
    }

    @Override
    public String getCode() {
        return code;
    }
}
