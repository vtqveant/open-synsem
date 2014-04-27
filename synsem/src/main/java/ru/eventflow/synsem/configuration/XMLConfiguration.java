package ru.eventflow.synsem.configuration;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;

@XmlRootElement(name = "configuration")
public class XMLConfiguration {

    @XmlElement(name="location")
    @XmlElementWrapper(name="locations")
    public Location[] locations;

    @XmlElement(name="device")
    @XmlElementWrapper(name="devices")
    public Device[] devices;

    public XMLConfiguration() {
    }

    public XMLConfiguration(Location[] locations, Device[] devices) {
        this.locations = locations;
        this.devices = devices;
    }

    public Location[] getLocations() {
        return locations;
    }

    public Device[] getDevices() {
        return devices;
    }

    @Override
    public String toString() {
        return "XMLConfiguration{" +
                "locations=" + Arrays.toString(locations) +
                ", devices=" + Arrays.toString(devices) +
                '}';
    }
}
