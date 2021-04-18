package com.example.robocoderobotcreator.model;

public abstract class ParametrizedBlock extends Block {
    public ParametrizedBlock(Category category) {
        super(category);
        this.parameter = "";
    }

    private String parameter;

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
