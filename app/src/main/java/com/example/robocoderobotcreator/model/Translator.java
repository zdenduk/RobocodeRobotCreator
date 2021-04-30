package com.example.robocoderobotcreator.model;

public enum Translator {
    INSTANCE;

    public String translateBlock(Block block) {
        StringBuilder sb = new StringBuilder();

        if (block instanceof ComboBlock) {
            ComboBlock cb = (ComboBlock) block;

            sb.append(block.getCode().replace("$", cb.getParameter())).append("\n");
            sb.append("{\n");
            for (Block cbBlock : cb.getBlocks()) {
                sb.append(translateBlock(cbBlock));
            }
            sb.append("}\n");
        } else if (block instanceof ParametrizedBlock) {
            ParametrizedBlock pb = (ParametrizedBlock) block;
            sb.append(block.getCode().replace("$", pb.getParameter())).append("\n");
        } else {
            sb.append(block.getCode()).append("\n");
        }

        return sb.toString();
    }

    public String translateRobotBlueprint(RobotBlueprint rb) {
        StringBuilder sb = new StringBuilder();

        sb.append("import robocode.*;\n" +
                "import java.awt.*;\n");

        sb.append("public class ").append(rb.getName()).append(" extends AdvancedRobot {\n");

        for (Block block : rb.getBlockList()) {
            sb.append(translateBlock(block));
        }

        sb.append("}");

        return sb.toString();
    }

}
