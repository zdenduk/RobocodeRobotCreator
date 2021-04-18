package com.example.robocoderobotcreator.model.events;


import com.example.robocoderobotcreator.model.Category;
import com.example.robocoderobotcreator.model.ComboBlock;

public class WhileBlock extends ComboBlock {

    private String code = "while(true)";

    public WhileBlock() {
        super(Category.EVENTS);
    }

    @Override
    public String getCode() {
        return code;
    }
}