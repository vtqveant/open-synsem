package ru.eventflow.synsem.synsem;

import org.junit.Before;
import org.junit.Test;
import ru.eventflow.synsem.model.Hypothesis;
import ru.eventflow.synsem.model.Query;
import ru.eventflow.synsem.ExampleModelBuilder;

public class FormulaTest {

    private SynSemProcessor processor;

    @Before
    public void setUp() throws Exception {
        processor = new SynSemProcessor("grammar.xml", ExampleModelBuilder.buildFrame());
    }

    // TODO
    @Test
    public void testGeneralCheck() throws Exception {
        Query q = new Query("включи лампочку", 0, 0);
        processor.process(q);
        for (Hypothesis h : q.getHypotheses()) {
            ReportPrinter.printOldReport(h);
        }
    }

}
