package ru.eventflow.synsem.generator;

import opennlp.ccg.grammar.Grammar;
import opennlp.ccg.hylo.*;
import opennlp.ccg.realize.Edge;
import opennlp.ccg.synsem.LF;
import org.jdom.Document;
import org.jdom.Element;
import ru.eventflow.synsem.synsem.Configuration;
import ru.eventflow.synsem.synsem.GrammarLoader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Generate possible right contexts based on expected syntactic category and semantic role.
 * Uses OpenCCG realization mechanism.
 */
public class RightContextGenerator {


    private static final Configuration config = Configuration.getInstance();

    public static void main(String[] args) throws IOException {

        Grammar grammar = new GrammarLoader().load(config.getGrammarUrl());

//        Realizer realizer = new Realizer(grammar);
        SimplifiedRealizer realizer = new SimplifiedRealizer(grammar);

        List<SatOp> preds = buildLF(grammar);
        List<Edge> edges = realizer.realize(preds);

        for (Edge e : edges) {
            System.out.println(e.toString());
        }
    }

    private static List<SatOp> buildLF(Grammar grammar) throws IOException {
        String xml = "<lf><satop nom=\"w0:thing\"><prop name=\"лампа\"/></satop></lf>";

        ByteArrayInputStream in = new ByteArrayInputStream(xml.getBytes());
        Document doc = grammar.loadFromXml(in);

        Element rootElt = doc.getRootElement();
        Element lfElt = (rootElt.getName().equals("lf")) ? rootElt : rootElt.getChild("lf");

        HyloHelper.processChunks(lfElt);
        LF lf = HyloHelper.getLF(lfElt);

        return HyloHelper.flatten(lf);
    }

}
