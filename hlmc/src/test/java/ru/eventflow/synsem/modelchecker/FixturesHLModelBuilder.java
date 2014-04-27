package ru.eventflow.synsem.modelchecker;

public class FixturesHLModelBuilder {

    public static HybridFrame build() {

        HybridFrame frame = new HybridFrame("Dummy");

        frame.addWorld("w1");
        frame.addWorld("w2");

        frame.setNominalAssign("n1", "w1");
        frame.setAccessible("m1", "w1", "w2");
        frame.setPropSym("p1", "w1");
        frame.setPropSym("p3", "w1");
        frame.setPropSym("p2", "w2");
        frame.setPropSym("p3", "w2");

        // so in this model we should have
        //
        //  @n1(p1)
        //  @n1(<m1>p2)
        //  etc.

        return frame;
    }

    /**
     * Fixture: model-00.xml
     *
     * @return
     */
    public static HybridFrame buildFixtureZero() {
        HybridFrame frame = new HybridFrame("model-00.xml");

        frame.addWorld("w1");
        frame.addWorld("w2");
        frame.addWorld("w3");

        frame.setNominalAssign("i", "w1");
        frame.setNominalAssign("j", "w2");
        frame.setNominalAssign("k", "w2");

        frame.setAccessible("pi1", "w1", "w2");
        frame.setAccessible("pi1", "w1", "w3");
        frame.setAccessible("pi2", "w2", "w3");

        frame.setPropSym("p", "w1");
        frame.setPropSym("p", "w2");
        frame.setPropSym("q", "w3");
        frame.setPropSym("r", "w1");
        frame.setPropSym("r", "w3");
        
        return frame;
    }

    /**
     * Fixture: model-01.xml
     * This is a model from (Franceschet, de Rijke, 2008)
     *
     * @return
     */
    public static HybridFrame buildFixtureOne() {
        HybridFrame frame = new HybridFrame("model-01.xml");

        frame.addWorld("root_node");
        frame.addWorld("index_node");
        frame.addWorld("b1");
        frame.addWorld("b2");
        frame.addWorld("a1");
        frame.addWorld("a11");
        frame.addWorld("a12");
        frame.addWorld("t1");
        frame.addWorld("a21");
        frame.addWorld("t2");
        frame.addWorld("d2");
        frame.addWorld("a31");
        frame.addWorld("t3");
        frame.addWorld("d3");

        frame.setNominalAssign("root", "root_node");
        frame.setNominalAssign("bind_x", "b1");

        frame.setAccessible("biblio", "root_node", "index_node");
        frame.setAccessible("book", "index_node", "b1");
        frame.setAccessible("book", "index_node", "b2");
        frame.setAccessible("paper", "index_node", "a1");
        frame.setAccessible("cites", "b1", "b1");
        frame.setAccessible("cites", "b2", "a1");
        frame.setAccessible("cites", "a1", "b2");
        frame.setAccessible("author", "b1", "a11");
        frame.setAccessible("author", "b1", "a12");
        frame.setAccessible("author", "b2", "a21");
        frame.setAccessible("author", "a1", "a31");
        frame.setAccessible("title", "b1", "t1");
        frame.setAccessible("title", "b2", "t2");
        frame.setAccessible("title", "a1", "t3");
        frame.setAccessible("date", "b2", "d2");
        frame.setAccessible("date", "a1", "d3");

        frame.setPropSym("marx", "a11");
        frame.setPropSym("de_rijke", "a12");
        frame.setPropSym("hybrid_logics", "t1");
        frame.setPropSym("franceschet", "a21");
        frame.setPropSym("model_checking", "t2");
        frame.setPropSym("model_checking", "t3");
        frame.setPropSym("year_2000", "d2");
        frame.setPropSym("year_2000", "d3");
        frame.setPropSym("afanasiev", "a31");

        return frame;
    }
    
    
}
