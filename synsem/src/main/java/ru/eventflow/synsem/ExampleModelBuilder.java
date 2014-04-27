package ru.eventflow.synsem;

import ru.eventflow.synsem.modelchecker.HybridFrame;

public class ExampleModelBuilder {

    public static HybridFrame buildFrame() {

        HybridFrame frame = new HybridFrame("Demo");

        String[] worlds = new String[]{
                "root_node",
                "x1_node", "x2_node", "x3_node", "x4_node", "x5_node", "x6_node",
                "x1_color_node",
                "x1_loc_node", "x2_loc_node", "x3_loc_node", "x4_loc_node", "x5_loc_node", "x6_loc_node",
                "loc1_node", "loc2_node", "loc3_node",
                "num_sg_node", "num_pl_node"
        };
        for (String s : worlds) {
            frame.addWorld(s);
        }

        frame.setAccessible("thing", "root_node", "x1_node");
        frame.setAccessible("thing", "root_node", "x2_node");
        frame.setAccessible("thing", "root_node", "x3_node");
        frame.setAccessible("thing", "root_node", "x4_node");
        frame.setAccessible("thing", "root_node", "x5_node");
        frame.setAccessible("thing", "root_node", "x6_node");

        frame.setAccessible("modifier", "x1_node", "x1_color_node");
        frame.setAccessible("modifier", "x1_node", "x1_loc_node");
        frame.setAccessible("modifier", "x2_node", "x2_loc_node");
        frame.setAccessible("modifier", "x3_node", "x3_loc_node");
        frame.setAccessible("modifier", "x4_node", "x4_loc_node");
        frame.setAccessible("modifier", "x5_node", "x5_loc_node");
        frame.setAccessible("modifier", "x6_node", "x6_loc_node");

        frame.setAccessible("anchor", "x1_loc_node", "loc1_node");
        frame.setAccessible("anchor", "x2_loc_node", "loc1_node");
        frame.setAccessible("anchor", "x3_loc_node", "loc1_node");
        frame.setAccessible("anchor", "x4_loc_node", "loc1_node");
        frame.setAccessible("anchor", "x5_loc_node", "loc2_node");
        frame.setAccessible("anchor", "x6_loc_node", "loc2_node");

        frame.setAccessible("num", "x1_node", "num_sg_node");
        frame.setAccessible("num", "x2_node", "num_pl_node");
        frame.setAccessible("num", "x3_node", "num_sg_node");
        frame.setAccessible("num", "x4_node", "num_pl_node");
        frame.setAccessible("num", "x5_node", "num_sg_node");
        frame.setAccessible("num", "x6_node", "num_sg_node");

        frame.setAccessible("num", "loc1_node", "num_sg_node");
        frame.setAccessible("num", "loc2_node", "num_sg_node");
        frame.setAccessible("num", "loc3_node", "num_sg_node");

        frame.setNominalAssign("root", "root_node");
        frame.setNominalAssign("x1", "x1_node");
        frame.setNominalAssign("x2", "x2_node");
        frame.setNominalAssign("x3", "x3_node");
        frame.setNominalAssign("x4", "x4_node");
        frame.setNominalAssign("x5", "x5_node");
        frame.setNominalAssign("x6", "x6_node");

        frame.setPropSym("кухня", "loc1_node");
        frame.setPropSym("прихожая", "loc2_node");
        frame.setPropSym("стол", "loc3_node");

        frame.setPropSym("подсветка", "x3_node");
        frame.setPropSym("лампа", "x1_node");
        frame.setPropSym("лампа", "x6_node");
        frame.setPropSym("шторы", "x4_node");
        frame.setPropSym("лампочка", "x2_node");
        frame.setPropSym("лампочка", "x5_node");

        frame.setPropSym("sg", "num_sg_node");
        frame.setPropSym("pl", "num_pl_node");

        frame.setPropSym("красный-adj", "x1_color_node");

        frame.setPropSym("q-color", "x1_color_node");
        frame.setPropSym("e-place", "loc1_node");
        frame.setPropSym("e-place", "loc2_node");

        // TODO this is irrelevant, but without these the parser crashes, FIXME
        for (String s : worlds) {
            frame.setPropSym("на", s);
            frame.setPropSym("в", s);
        }

        return frame;
    }
}
