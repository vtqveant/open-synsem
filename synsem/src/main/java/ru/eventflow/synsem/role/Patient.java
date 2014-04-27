package ru.eventflow.synsem.role;

import opennlp.ccg.synsem.LF;

public class Patient extends BaseSemanticRole {

    Location location;
    String nominal;

    public Patient(LF lf, String nominal) {
        super(lf);
        this.nominal = nominal;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getNominal() {
        return nominal;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "location=" + location +
                '}';
    }
}
