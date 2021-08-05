package com.example.robocoderobotcreator.model;

import java.util.HashSet;
import java.util.Set;

public abstract class ComboBlock extends ParametrizedBlock {
    public ComboBlock(Category category) {
        super(category);
        this.blocks = new HashSet<>();
    }

    private Set<Block> blocks;

    public Set<Block> getBlocks() {
        return blocks;
    }
}
