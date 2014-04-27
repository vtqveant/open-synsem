package ru.eventflow.synsem.synsem;

import opennlp.ccg.grammar.Grammar;
import opennlp.ccg.hylo.*;
import opennlp.ccg.parse.ParseException;
import opennlp.ccg.parse.Parser;
import opennlp.ccg.synsem.Category;
import opennlp.ccg.synsem.LF;
import opennlp.ccg.synsem.Sign;
import ru.eventflow.synsem.modelchecker.HybridFrame;

import java.io.IOException;
import java.util.*;

/**
 * This is a component which builds a HybridFrame from an HLDS representation obtained with a CCG parser.
 * This is rather straightforward as regards model building procedure, so nothing clever here (no tableaux or anything).
 * <p/>
 * TODO But still this is a mess.
 */
public class HLDSModelBuilder {

    private Parser parser;

    Map<String, String> localNominals = new HashMap<String, String>();

    HybridFrame frame;

    /**
     * When attaching to an existing frame (i.e. when adding to an XML configuration), take only first N category.
     * Otherwise use regular parse results.
     * TODO provide an API for this functionality
     */
    private boolean nominalPhraseOnly;

    public HLDSModelBuilder(Grammar grammar) throws IOException {
        this(grammar, new HybridFrame("HLDSModelBuilder"));
        nominalPhraseOnly = false;
    }

    public HLDSModelBuilder(Grammar grammar, HybridFrame frame) throws IOException {
        if (grammar == null) {
            grammar = new GrammarLoader().load(Configuration.getInstance().getGrammarUrl());
        }
        parser = new Parser(grammar);
        this.frame = frame;
        nominalPhraseOnly = true;
    }

    public HybridFrame getFrame() {
        return frame;
    }

    public void build(String orthography) {
        frame.addWorld("n_root");
        frame.setPropSym("root", "n_root");
        buildAndAttachToFrame("n_root", orthography);
    }

    /**
     * Parses an orthography and attaches the resulting subgraph to the world with rootWorldLabel
     *
     * @param rootWorldLabel
     * @param orthography
     */
    public void buildAndAttachToFrame(String rootWorldLabel, String orthography) {
        localNominals.clear();
        List<SatOp> parts = parse(orthography, nominalPhraseOnly);

        // HL formula is a poset, tackling them is tricky, dirty, and recursive
        Nominal root = findRoot(parts);
        processPoset(frame, root, parts, rootWorldLabel);
    }

    private Nominal findRoot(List<SatOp> parts) {
        if (parts.size() == 0) return null;

        Nominal root = null;
        for (SatOp part : parts) {
            if (part == null) continue;
            root = part.getNominal();
            if (HyloHelper.isRoot(root, parts)) break;
        }
        return root;
    }

    /**
     * TODO refac
     *
     * @param parts
     * @param worldLabel
     */
    public void processPoset(HybridFrame frame, Nominal root, List<SatOp> parts, String worldLabel) {
        if (parts.size() == 0) return;

        for (int i = 0; i < parts.size(); i++) {
            SatOp part = parts.get(i);
            if (part == null) continue;
            boolean result;
            if (part.getNominal().equals(root)) {
                result = parseSatOp(frame, worldLabel, part);
            } else {
                String parentLabel = part.getNominal().toString();
                result = parseSatOp(frame, localNominals.get(parentLabel), part);
            }
            if (result) {
                parts.set(i, null);
                processPoset(frame, root, parts, worldLabel);
            }
        }
    }


    /**
     * Attach to head, pass oneself as a head for recursive calls.
     * All labels are guaranteed to exist, so we can go in one sweep.
     *
     * @param fromWorldLabel
     * @param op
     */
    private boolean parseSatOp(HybridFrame frame, String fromWorldLabel, LF op) {
        if (fromWorldLabel == null) {
            return false;
        }
        if (op instanceof NominalVar) {
            return true;
        } else if (op instanceof Proposition) {
            String propSymLabel = op.toString();
            frame.setPropSym(propSymLabel, fromWorldLabel);
        } else {
            LF arg = ((SatOp) op).getArg();
            if (arg instanceof Diamond) {
                String modalityLabel = ((Diamond) arg).modalOpString().replace(">", "").replace("<", "").toLowerCase();
                String toWorldLabel = random();
                localNominals.put(((Diamond) arg).getArg().toString(), toWorldLabel);
                frame.setAccessible(modalityLabel, fromWorldLabel, toWorldLabel);

                parseSatOp(frame, toWorldLabel, ((Diamond) arg).getArg());
            }
            if (arg instanceof Proposition) {
                String propSymLabel = arg.toString();
                frame.setPropSym(propSymLabel, fromWorldLabel);
            }
        }
        return true;
    }

    /**
     * Relatively heavy-weight, but I don't have a global id table and i don't know which ids may be there in the frame I'm attaching to.
     * So this should help reduce that probability of collisions.
     */
    private static String random() {
        return UUID.randomUUID().toString();
    }

    private List<SatOp> parse(String orthography, boolean nominalPhraseOnly) {
        System.out.println("Query: " + orthography);
        List<SatOp> parts = new ArrayList<SatOp>();

        try {
            parser.parse(orthography);
            List<Sign> parses = parser.getResult();
            Sign[] results = new Sign[parses.size()];
            parses.toArray(results);

            // iterate through parses
            for (Sign sign : results) {
                Category cat = sign.getCategory();

                // take the first simple category (N)
                if (nominalPhraseOnly && !cat.getSupertag().toLowerCase().equals("n")) {
                    continue;
                }

                LF lf = cat.getLF();
                parts.addAll(HyloHelper.flatten(lf));
                break;
            }
        } catch (ParseException e) {
            System.err.println("Unable to parse: \"" + orthography + "\"");
        }

        return parts;
    }

}
