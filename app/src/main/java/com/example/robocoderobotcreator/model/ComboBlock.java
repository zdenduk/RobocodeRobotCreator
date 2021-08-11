package com.example.robocoderobotcreator.model;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class ComboBlock extends Block {
    public ComboBlock(Category category) {
        super(category);
        this.blocks = new LinkedHashSet<>();
    }

    private Set<Block> blocks;

    public Set<Block> getBlocks() {
        return blocks;
    }
}
