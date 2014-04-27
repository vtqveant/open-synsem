package ru.eventflow.synsem.modelchecker;

import java.io.*;
import java.util.*;

/**
 * Dumps HybridFrame in GraphML format
 */
public class GraphMLWriter {

    private static final String ATTR_LABEL = "<key id=\"label\" for=\"node\" attr.name=\"label\" attr.type=\"string\"/>\n";
    private static final String ATTR_NOMINALS = "<key id=\"nominals\" for=\"node\" attr.name=\"nominals\" attr.type=\"string\"/>\n";
    private static final String ATTR_PROPSYMBOLS = "<key id=\"propsymbols\" for=\"node\" attr.name=\"propsymbols\" attr.type=\"string\"/>\n";
    private static final String ATTR_MODALITY = "<key id=\"modality\" for=\"edge\" attr.name=\"modality\" attr.type=\"string\"/>\n";

    private static final String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "    xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\">\n" +
            ATTR_LABEL +
            ATTR_MODALITY +
            ATTR_NOMINALS +
            ATTR_PROPSYMBOLS +
            "<graph id=\"G\" edgedefault=\"directed\">\n";

    private static final String FOOTER = "</graph>\n</graphml>";

    public static String serialize(HybridFrame frame) {
        StringBuilder sb = new StringBuilder();
        sb.append(HEADER);

        for (String w : frame.getWorlds()) {
            sb.append("<node id=\""); sb.append(w); sb.append("\">\n");
            sb.append("    <data key=\"label\">"); sb.append(w); sb.append("</data>\n");
            Set<String> nominals = frame.getWorldNominals(w);
            if (nominals != null && nominals.size() > 0) {
                sb.append("    <data key=\"nominals\">"); sb.append(join(nominals)); sb.append("</data>\n");
            }
            Set<String> propSymbols = frame.getWorldPropSymbols(w);
            if (propSymbols != null && propSymbols.size() > 0) {
                sb.append("    <data key=\"propsymbols\">"); sb.append(join(propSymbols)); sb.append("</data>\n");
            }
            sb.append("</node>\n");
        }

        // modality, from, to*
        Map<String, Map<String, Set<String>>> relations = frame.getAccessibilityRelations();
        for (Map.Entry<String, Map<String, Set<String>>> entry : relations.entrySet()) {
            String modality = entry.getKey();
            for (Map.Entry<String, Set<String>> edges : entry.getValue().entrySet()) {
                String from = edges.getKey();
                for (String to : edges.getValue()) {
                    sb.append("<edge source=\""); sb.append(from); sb.append("\" target=\""); sb.append(to); sb.append("\">\n");
                    sb.append("    <data key=\"modality\">"); sb.append(modality); sb.append("</data>\n");
                    sb.append("</edge>\n");
                }
            }
        }

        sb.append(FOOTER);
        return sb.toString();
    }

    public static void serialize(HybridFrame frame, OutputStream out) throws IOException {
        out.write(serialize(frame).getBytes());
    }

    private static String join(Set<String> ss) {
        StringBuilder sb = new StringBuilder();
        List<String> strings = new ArrayList<String>(ss);
        Collections.sort(strings);
        for (int i = 0; i < strings.size(); i++) {
            sb.append(strings.get(i));
            if (i < strings.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
