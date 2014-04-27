package ru.eventflow.synsem.synsem;

import opennlp.ccg.synsem.LF;
import org.junit.Before;
import org.junit.Test;
import ru.eventflow.synsem.model.Hypothesis;
import ru.eventflow.synsem.model.Query;
import ru.eventflow.synsem.ExampleModelBuilder;

import java.util.ArrayList;
import java.util.List;

public class SynSemProcessorTest {

    private SynSemProcessor processor;
    private static final SemanticRoleExtractor extractor = new SemanticRoleExtractor();

    @Before
    public void setUp() throws Exception {
        processor = new SynSemProcessor("grammar.xml", ExampleModelBuilder.buildFrame());
    }

    @Test
    public void testMultipleQueries() throws Exception {
        List<Query> queries = new ArrayList<Query>();
        queries.add(new Query("включи лампу", 5000, 0));
        queries.add(new Query("включи лампочку", 4900, 0));
        queries.add(new Query("выключи лампу", 4500, 0));
        queries.add(new Query("выключи лампу и подсветку на кухне", 4000, 0));

        processor.process(queries);

        for (Query q : queries) {
            for (Hypothesis h : q.getHypotheses()) {
                ReportPrinter.printOldReport(h);
            }
        }
    }

    @Test
    public void testExtractCommands() throws Exception {
        Query q = new Query("включи красную лампу", 0, 0);
        processor.process(q);
        List<Hypothesis> hs = q.getHypotheses();
        Hypothesis h = hs.get(0);
        extractor.extractCommands(h);

        System.out.println("done.");
    }

    @Test
    public void testBuildAVM() throws Exception {
        Query q = new Query("включи красную лампу", 0, 0);
        processor.process(q);
        List<Hypothesis> hs = q.getHypotheses();
        LF lf = hs.get(0).getLF();

        String s = "";
        s += appendHeader();
        s += "\\begin{avm}\n";

        try {
            AVMConverter converter = new AVMConverter();
            converter.setStripTypes(true);
            converter.setDropNominals(true);
            s += converter.convert(lf);
            s = s.replaceAll("-", "_") + "]\\end{avm}\n";
            s += "\\end{document}";
            System.out.println(s);
        } catch (ConversionException e) {
            System.err.println(e);
        }
    }

    public static String appendHeader() {
        StringBuilder sb = new StringBuilder();
        sb.append("\\documentclass{article}\n");
        sb.append("\\usepackage[margin=0.5in]{geometry}\n");

        // russian stuff
        sb.append("\\usepackage{cmap}\n");
        sb.append("\\usepackage[T2A]{fontenc}\n");
        sb.append("\\usepackage[utf8]{inputenc}\n");
        sb.append("\\usepackage[english, russian]{babel}\n");

        sb.append("\\usepackage{avm}\n");

        sb.append("\\avmfont{\\sc}\n");
        sb.append("\\avmoptions{sorted,active}\n");
        sb.append("\\avmvalfont{\\rm}\n");
        sb.append("\\avmsortfont{\\scriptsize\\it}\n");


        sb.append("\\pagestyle{empty}\n");
        sb.append("\\begin{document}\n");
        return sb.toString();
    }


}
