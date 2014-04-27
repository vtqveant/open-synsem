package ru.eventflow.synsem.configuration;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import java.util.Arrays;

@XmlType(name = "location")
public class Location {

    @XmlAttribute(required = true)
    public int id;

    @XmlElement(name="variant")
    @XmlElementWrapper(name="names")
    public String[] names;

    @XmlElement(name="id")
    @XmlElementWrapper(name="adjacent")
    public int[] adjacent;

    public Location() {
    }

    public Location(int id, String[] names, int[] adjacent) {
        this.id = id;
        this.names = names;
        this.adjacent = adjacent;
    }

    public int getId() {
        return id;
    }

    public String[] getNames() {
        return names;
    }

    public int[] getAdjacent() {
        return adjacent;
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", names=" + Arrays.toString(names) +
                ", adjacent=" + Arrays.toString(adjacent) +
                '}';
    }
}
