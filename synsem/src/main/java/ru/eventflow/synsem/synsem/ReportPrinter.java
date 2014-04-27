package ru.eventflow.synsem.synsem;

import opennlp.ccg.grammar.Grammar;
import opennlp.ccg.hylo.HyloHelper;
import opennlp.ccg.realize.Edge;
import opennlp.ccg.synsem.LF;
import opennlp.ccg.synsem.Sign;
import opennlp.ccg.unify.FeatureStructure;
import opennlp.ccg.util.Visualizer;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import ru.eventflow.synsem.generator.SimplifiedRealizer;
import ru.eventflow.synsem.model.Command;
import ru.eventflow.synsem.model.Formula;
import ru.eventflow.synsem.model.Hypothesis;
import ru.eventflow.synsem.model.World;
import ru.eventflow.synsem.role.Location;
import ru.eventflow.synsem.role.Patient;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;


public class ReportPrinter {

    private static SimplifiedRealizer realizer;
    static {
        try {
            String grammarURL = Configuration.getInstance().getGrammarUrl();
            Grammar grammar = new GrammarLoader().load(grammarURL);
            realizer = new SimplifiedRealizer(grammar);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getFirstRealization(LF lf) {
        List<Edge> edges;
        if ((edges = realizer.realize(HyloHelper.flatten(lf))) != null) {
            return edges.get(0).getSign().getOrthography();
        }
        return "<unk>";
    }

    public static void printOldReport(Hypothesis h) {
        System.out.println("--------- HYPOTHESIS REPORT ---------");

        System.out.println();
        System.out.println(h.getLF().prettyPrint("   "));
        System.out.println();

        // todo refac
        FeatureStructure featureStructure = h.getCategory().getFeatureStructure();
        if (featureStructure != null) {
            Set<String> attributes = featureStructure.getAttributes();
            if (featureStructure.getAttributes() != null) {
                System.out.println("Attibutes:");
                for (String s : attributes) {
                    System.out.println("\t" + s + ": " + featureStructure.getValue(s));
                }
            }
        }
        System.out.println();

        System.out.println("category: " + h.getCategory());
        System.out.println("score = " + h.getScore());

        for (Command command : h.getCommands()) {
            List<Formula> fs = command.getFormulae();
            for (Formula f : fs) {
                System.out.println(">   " + command.getPredicate().toString() + " " +
                        f.getText() + " : " + flatten(f.getWorlds()));
            }
        }
        System.out.println();
    }

    public static void printSimpleReport(Hypothesis h) {
        StringBuilder sb = new StringBuilder();
        sb.append("--------- HYPOTHESIS REPORT ---------");

        sb.append('\n');
        sb.append(h.getLF().prettyPrint("   "));
        sb.append('\n');

        for (Command command : h.getCommands()) {
            sb.append("\n");
            sb.append(command.getPredicate());
            sb.append("\t[");
            for (Patient patient : command.getPatients()) {
                sb.append("\t");
                sb.append(patient.getNominal());
                Location location;
                if ((location = patient.getLocation()) != null) {
                    sb.append(" ~ ");
                    sb.append(location.toString());
                }
            }

            List<Formula> fs = command.getFormulae();
            for (Formula f : fs) {
                sb.append(">   " + command.getPredicate().toString() + " " +
                        f.getText() + " : " + flatten(f.getWorlds()));
            }
        }
        sb.append('\n');
        System.out.println(sb.toString());

    }

    public static void printReport(Hypothesis h) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("--------- HYPOTHESIS REPORT ---------");

        sb.append('\n');
        sb.append(h.getLF().prettyPrint("   "));
        sb.append('\n');

        // todo refac
        FeatureStructure featureStructure = h.getCategory().getFeatureStructure();
        if (featureStructure != null) {
            Set<String> attributes = featureStructure.getAttributes();
            if (featureStructure.getAttributes() != null) {
                sb.append("Attibutes:\n");
                for (String s : attributes) {
                    sb.append('\t' + s + ": " + featureStructure.getValue(s) + '\n');
                }
            }
        }
        sb.append('\n');

        sb.append("category: " + h.getCategory());
        sb.append("score = " + h.getScore());

        for (Command command : h.getCommands()) {
            sb.append("\n");
            sb.append(command.getPredicate());
            sb.append("\t[");
            for (Patient patient : command.getPatients()) {
                sb.append("\t");
                sb.append(patient.getNominal());
                Location location;
                if ((location = patient.getLocation()) != null) {
                    sb.append(" ~ ");
                    sb.append(location.toString());
                }
            }

//            List<Formula> fs = command.getFormulae();
//            for (Formula f : fs) {
//                System.out.println(">   " + command.getPredicate().toString() + " " +
//                        f.getText() + " : " + flatten(f.getWorlds()));
//            }
        }
        sb.append('\n');
        System.out.println(sb.toString());
    }

    private static String flatten(List<World> worlds) {
        if (worlds.size() == 0) {
            return "unsatisfiable";
        } else {
            String retval = "";
            for (World w : worlds) {
                retval += w.getLabel() + " ";
            }
            return retval;
        }
    }

    /**
     * Write to TeX
     */
    public static void dumpReport(List<Hypothesis> hs, String filename) {
        Visualizer vis = new Visualizer();
        writeHeader(filename);
        for (Hypothesis h : hs) {
            Sign sign = h.getDerivationHistory().getOutput();
            vis.saveTeXFile(sign, filename);
        }
        vis.writeFooter(filename);
        System.out.println("Saved to file " + filename);
    }

    /**
     * This is a routine from opennlp.ccg.util.Visualizer with a few fixes to correctly display Russian
     *
     * @param fileName
     * @return
     */
    private static boolean writeHeader(String fileName) {
        BufferedWriter bw;
        try {
            bw = new java.io.BufferedWriter(new FileWriter(fileName));
            bw.write("\\documentclass{article}\n");
            bw.write("\\usepackage[margin=0.5in]{geometry}\n");

            // russian stuff
            bw.write("\\usepackage{cmap}\n");
            bw.write("\\usepackage[T2A]{fontenc}\n");
            bw.write("\\usepackage[utf8]{inputenc}\n");
            bw.write("\\usepackage[english, russian]{babel}\n");

            bw.write("\\newcommand{\\deriv}[2]\n");
            bw.write("{  \\renewcommand{\\arraystretch}{.5}\n");
            bw.write("$\\begin{array}[t]{*{#1}{c}}\n");
            bw.write("     #2\n");
            bw.write("   \\end{array}$ }\n");
            bw.write("\\newcommand{\\gf}[1]{\\textsf{\\textsl{#1}}}\n");
            bw.write("\\newcommand{\\cf}[1]{\\mbox{\\ensuremath{\\cfont{#1}}}}\n");
            bw.write("\\newcommand{\\uline}[1]\n");
            bw.write("{\\mc{#1}{\\hrulefill} }\n");
            bw.write("\\newcommand{\\mc}[2]\n");
            bw.write("  {\\multicolumn{#1}{c}{#2}}\n");
            bw.write("\\newcommand{\\cfont}{\\mathsf}\n");
            bw.write("\\newcommand{\\bs}{\\backslash}\n");
            bw.write("\\newcommand{\\subsa}[1]{\\hspace{-0.75mm}_{_{#1}}}\n");
            bw.write("\\newcommand{\\subsb}[1]{\\hspace{-0.10mm}_{_{#1}}}\n");
            bw.write("\\newcommand{\\subs}[1]{\\hspace{-0.40mm}_{#1}}\n");
            bw.write("\\newcommand{\\subsf}[1]{\\hspace{-0.75mm}_{_{#1}}}\n");
            bw.write("\\newcommand{\\supsa}[1]{\\hspace{-1.75mm}^{^{#1}} }\n");
            bw.write("\\newcommand{\\supsb}[1]{\\hspace{-0.80mm}^{^{#1}}  }\n");
            bw.write("\\newcommand{\\sups}[1]{\\hspace{-0.40mm}^{#1}}\n");
            bw.write("\\pagestyle{empty}\n");
            bw.write("\\begin{document}\n");
            bw.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static void printLF(LF convertedLF) throws IOException {
        System.out.println(convertedLF.prettyPrint("  "));
        System.out.println();

        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        outputter.output(convertedLF.toXml(), System.out);
        System.out.println();
    }

}
