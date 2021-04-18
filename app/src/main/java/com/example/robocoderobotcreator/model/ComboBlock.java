package com.example.robocoderobotcreator.model;

import java.util.LinkedList;
import java.util.Queue;

public abstract class ComboBlock extends ParametrizedBlock {
    public ComboBlock(Category category) {
        super(category);
        this.blocks = new LinkedList<>();
    }

    private Queue<Block> blocks;

    public Queue<Block> getBlocks() {
        return blocks;
    }
}
