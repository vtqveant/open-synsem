package ru.eventflow.synsem;

import ru.eventflow.synsem.model.Hypothesis;
import opennlp.ccg.parse.ParseException;
import ru.eventflow.synsem.model.Query;
import ru.eventflow.synsem.synsem.Configuration;
import ru.eventflow.synsem.synsem.ReportPrinter;
import ru.eventflow.synsem.synsem.SynSemProcessor;

import java.io.*;
import java.util.List;

public class Runner {

    private static final Configuration config = Configuration.getInstance();

    public static void main(String[] args) throws IOException, ParseException {

        String grammar = config.getGrammarUrl();
        SynSemProcessor processor = new SynSemProcessor(grammar, ExampleModelBuilder.buildFrame());

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print("tccg> ");
            String input = br.readLine();
            if (input == null || input.equals("\\q") || input.equals(":q") || input.equals("exit") ||
                    input.equals("quit") || input.equals("halt.")) {
                break; // EOF
            }

            input = input.trim();
            if (input.equals("")) {
                continue;
            }

            Query q = new Query(input);
            processor.process(q);
            List<Hypothesis> hs = q.getHypotheses();
            for (Hypothesis h : hs) {
//                ReportPrinter.printReport(h);
                ReportPrinter.printSimpleReport(h);
            }

            if (config.isUseTeX()) {
                String filename = config.getTexDirectory() + "__parse.tex";
                ReportPrinter.dumpReport(hs, filename);
            }

        }
    }


}
