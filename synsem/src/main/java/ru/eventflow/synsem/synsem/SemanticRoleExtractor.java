package ru.eventflow.synsem.synsem;

import ru.eventflow.synsem.model.Command;
import ru.eventflow.synsem.role.Location;
import ru.eventflow.synsem.role.Patient;
import ru.eventflow.synsem.role.Predicate;
import ru.eventflow.synsem.model.Formula;
import ru.eventflow.synsem.model.Hypothesis;
import opennlp.ccg.hylo.*;
import opennlp.ccg.synsem.LF;
import opennlp.ccg.unify.SimpleType;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Extract semantic roles from HLDS representation, split and flatten if necessary.
 */
public class SemanticRoleExtractor {

    public static final Logger log = Logger.getLogger(SemanticRoleExtractor.class);

    public static final String MODIFIER = "Modifier";
    public static final String PATIENT = "Patient";

    public SemanticRoleExtractor() {
    }

    /**
     * @param hypothesis
     */
    public void extractCommands(Hypothesis hypothesis) {
        LF lf = hypothesis.getLF();
        if (lf != null) {
            for (Command command : extractCommands(lf)) {
                hypothesis.addCommand(command);
            }
        }
    }

    /**
     * Command is a pair, consisting of an action (marked with a TBox.COMMAND in the CCG) and a list of patients with
     * an arbitrary set of modifiers.
     *
     * @param lf
     * @return
     */
    private List<Command> extractCommands(LF lf) {
        List<Command> commands = new ArrayList<Command>();

        List<SatOp> lfs = HyloHelper.flatten(lf);
        Map<Nominal, List<SatOp>> actions = new HashMap<Nominal, List<SatOp>>();
        for (SatOp l : lfs) {
            Nominal n = l.getNominal();
            SimpleType type = n.getType();
            if (type.getName().equals(TBox.COMMAND.getName())) {
                if (!actions.containsKey(n)) {
                    actions.put(n, new ArrayList<SatOp>());
                }
                actions.get(n).add(l);
            }
        }

        for (Map.Entry<Nominal, List<SatOp>> entry : actions.entrySet()) {
            Command command = new Command();
            for (SatOp op : entry.getValue()) {

                LF arg = op.getArg();
                if (arg instanceof Diamond) {
                    Diamond diamond = (Diamond) arg;
                    if (diamond.getMode().getName().equals(PATIENT)) {
                        Nominal diamondNominal = (Nominal) diamond.getArg();

                        // find "things" in this patient
                        List<SatOp> things = extractTBox(lfs, diamondNominal, TBox.THING.getName());

                        // find all direct modifiers of this patient and attach them to subordinate "things"
                        List<SatOp> modifiers = extractModifiers(lfs, diamondNominal);
                        for (SatOp modifier : modifiers) {
                            for (SatOp thing : things) {
                                SatOp copy = (SatOp) modifier.copy();
                                copy.setNominal(thing.getNominal());
                                lfs.add(copy); // add, so that transitive closure could pick it up later
                            }
                        }

                        // add the rest of the data to "things"
                        // TODO filter what's appropriate (think ontology mapping)
                        for (SatOp thing : things) {
                            List<SatOp> parts = transitiveClosure(lfs, thing.getNominal());
                            LF p = join(parts, thing.getNominal());
                            Patient patient = new Patient(p, thing.getNominal().getName());
                            command.addPatient(patient);
                        }
                    }
                }
                if (arg instanceof Proposition) {
                    Proposition proposition = (Proposition) arg;
                    command.setPredicate(new Predicate(proposition));
                }
            }
            commands.add(command);
        }
        return commands;
    }

    private LF join(List<SatOp> preds, Nominal root) {
        LF lf = null;
        for (SatOp p : preds) {
            lf = HyloHelper.append(lf, p);
        }
        return HyloHelper.compact(lf, root);
    }

    /**
     * Find transitive closure starting from parent, which is included in the response.
     */
    private List<SatOp> transitiveClosure(List<SatOp> everything, Nominal parent) {
        List<Nominal> proofs = new ArrayList<Nominal>();
        proofs.add(parent); // patient's nominal is a starting point

        List<SatOp> villains = new ArrayList<SatOp>();
        findVillains(everything, villains, proofs);

        return villains;
    }

    private List<SatOp> extractModifiers(List<SatOp> everything, Nominal parent) {
        List<SatOp> modifiers = new ArrayList<SatOp>();
        List<SatOp> sisters = getAllByNominal(everything, parent);
        for (SatOp sister : sisters) {
            LF arg = sister.getArg();
            if (arg instanceof Diamond) {
                Diamond diamond = (Diamond) arg;
                if (diamond.getMode().getName().equals(MODIFIER)) {
                    modifiers.add(sister);
                }
            }
        }
        return modifiers;
    }

    /**
     * Find all terms with the same nominal
     *
     * @param everything
     * @param nominal
     * @return
     */
    private List<SatOp> getAllByNominal(List<SatOp> everything, Nominal nominal) {
        List<SatOp> retval = new ArrayList<SatOp>();
        for (SatOp satOp : everything) {
            if (satOp.getNominal().equals(nominal)) {
                retval.add(satOp);
            }
        }
        return retval;
    }


    /**
     * Find all children of parent node with TBox.THING type
     *
     * @param everything
     * @param parent
     * @return
     */
    private List<SatOp> extractTBox(List<SatOp> everything, Nominal parent, String tbox) {
        List<SatOp> things = new ArrayList<SatOp>();
        List<SatOp> children = transitiveClosure(everything, parent);
        for (SatOp child : children) {
            SimpleType type = child.getNominal().getType();
            if (HyloHelper.isLexPred(child) && type.getName().equals(tbox)) {
                things.add(child);
            }
        }
        return things;
    }

    /**
     * Computes transitive closure. Rather inefficient (no TCO in JVM).
     *
     * @param suspects
     * @param villains
     * @param proofs
     */
    private void findVillains(List<SatOp> suspects, List<SatOp> villains, List<Nominal> proofs) {
        for (SatOp suspect : suspects) {
            if (!villains.contains(suspect) && proofs.contains(suspect.getNominal())) {
                villains.add(suspect);
                LF child = suspect.getArg();
                if (child instanceof Diamond) {
                    LF arg = ((Diamond) child).getArg();
                    if (arg instanceof NominalAtom) {
                        proofs.add((NominalAtom) arg);
                    }
                }
                findVillains(suspects, villains, proofs);
            }
        }
    }

    /**
     * Convert LF (HLDS) representation to HLMC format
     *
     * Example query in HLMC:
     * B x( @root(<thing>(x & лампа & <num>(sg) & <modifier>(красный-adj))))
     */
    public Formula toFormula(LF lf) {
        String s = "";
        try {
            HLConverter converter = new HLConverter();
            converter.setStripTypes(true);
            converter.setDropNominals(true);
            s = converter.convert(lf).toLowerCase();
        } catch (ConversionException e) {
            log.error(e);
        }
        return new Formula(s);
    }






}
