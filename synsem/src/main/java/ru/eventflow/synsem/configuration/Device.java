package ru.eventflow.synsem.configuration;

import javax.xml.bind.annotation.*;
import java.util.Arrays;

@XmlType(name = "device")
public class Device {

    @XmlAttribute(required = true)
    public int id;

    @XmlElement
    public String type;

    @XmlElement
    public String description;

    @XmlElement(name="location")
    public int location;

    @XmlElement(name="variant")
    @XmlElementWrapper(name="names")
    public String[] names;

    public Device() {
    }

    public Device(int id, String type, String description, int location, String[] names) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.location = location;
        this.names = names;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public int getLocation() {
        return location;
    }

    public String[] getNames() {
        return names;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", location=" + location +
                ", names=" + Arrays.toString(names) +
                '}';
    }
}
