package ru.eventflow.synsem.model;

import ru.eventflow.synsem.role.Patient;
import ru.eventflow.synsem.role.Predicate;

import java.util.ArrayList;
import java.util.List;

public class Command {

    private Predicate predicate;
    private List<Patient> patients = new ArrayList<Patient>();
    private List<Formula> formulae = new ArrayList<Formula>();

    public Command() {
    }

    public Predicate getPredicate() {
        return predicate;
    }

    public void setPredicate(Predicate predicate) {
        this.predicate = predicate;
    }

    public void addPatient(Patient p) {
        patients.add(p);
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public List<Formula> getFormulae() {
        return formulae;
    }

    public void addFormula(Formula formula) {
        formulae.add(formula);
    }

    @Override
    public String toString() {
        return "Command{" +
                "predicate=" + predicate +
                ", patients=" + patients +
                ", formulae=" + formulae +
                '}';
    }
}
