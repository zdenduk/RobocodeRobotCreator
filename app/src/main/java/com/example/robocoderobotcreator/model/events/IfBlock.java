package com.example.robocoderobotcreator.model.events;

import com.example.robocoderobotcreator.model.Category;
import com.example.robocoderobotcreator.model.ComboBlock;

public class IfBlock extends ComboBlock {

    private String code = "if($)";

    public IfBlock() {
        super(Category.EVENTS);
    }

    @Override
    public String getCode() {
        return code;
    }
}
