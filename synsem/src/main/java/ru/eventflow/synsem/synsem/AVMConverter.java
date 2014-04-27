package ru.eventflow.synsem.synsem;

import opennlp.ccg.hylo.*;
import opennlp.ccg.synsem.LF;

import java.util.List;

/**
 * Converts HLDS to AVMs in TeX
 *
 * uses avm.sty
 *
 * \begin{avm}
 * [{}  pred  & `likes< \_ , \_ >'   \\
 *      tense &  pres                \\
 *      subj  &  [{} pred & `Fido']  \\
 *      obj   &  [{} pred & `bones']]
 * \end{avm}
 */
public class AVMConverter {

    /**
     * Change if you want to have types (HLMC does not support them, and I don't use them either)
     */
    private boolean stripTypes = true;

    /**
     * Suppress adding excessive nominals to the output
     */
    private boolean dropNominals = true;

    public String convert(LF lf) throws ConversionException {
        if (lf instanceof Op) {
            return op((Op) lf);
        }
        if (lf instanceof SatOp) {
            return satop((SatOp) lf);
        }
        if (lf instanceof Diamond) {
            return diamond((Diamond) lf);
        }
        if (lf instanceof Proposition) {
            return proposition((Proposition) lf);
        }
        if (lf instanceof HyloVar) {
            return hylovar((HyloVar) lf);
        }
        if (lf instanceof NominalAtom) {
            return nominalAtom((NominalAtom) lf);
        }
        if (lf instanceof NominalVar) {
            return nominalVar((NominalVar) lf);
        }
        throw new ConversionException("Unsupported LF type.");
    }

    private String diamond(Diamond lf) throws ConversionException {
        return lf.getMode().toString() + " & " + convert(lf.getArg()) + "\\\\";
    }

    private String proposition(Proposition lf) {
        return lf.toString();
    }

    private String op(Op lf) throws ConversionException {
        List<LF> args = lf.getArguments();
        if (args.size() == 1) {
            return "[{" + args.get(0).getType().getName() + "} predicate & " + convert(args.get(0));
        } else {
            String retval = "predicate & ";
            int i = 0;
            for (LF arg : args) {
                if (dropNominals && arg instanceof Nominal) {
                    continue;
                }
                if (i > 0) {
                    retval += " \\^ ";
                }
                retval += convert(arg);
                i++;
            }
            return retval;
        }
    }

    private String hylovar(HyloVar lf) {
        return "@" + lf.nameWithType();
    }

    private String satop(SatOp lf) throws ConversionException {
        String retval = "";
        if (!dropNominals) {
            retval += "(";
            if (lf.getNominal() instanceof NominalAtom) {
                retval += "nom_" + (stripTypes ? lf.getNominal().getName() : lf.getNominal().toString());
            } else {
                retval += "nomvar_" + nominalVar((NominalVar) lf.getNominal());
            }
            retval += " & ";
        }
        retval += convert(lf.getArg());
        if (!dropNominals) {
            retval += ")";
        }
        return retval;
    }

    private String nominalAtom(NominalAtom lf) {
        return (stripTypes ? lf.getName() : lf.toString());
    }

    private String nominalVar(NominalVar lf) {
        return (stripTypes ? lf.getName() : lf.nameWithType());
    }

    public void setStripTypes(boolean stripTypes) {
        this.stripTypes = stripTypes;
    }

    public void setDropNominals(boolean dropNominals) {
        this.dropNominals = dropNominals;
    }

}
