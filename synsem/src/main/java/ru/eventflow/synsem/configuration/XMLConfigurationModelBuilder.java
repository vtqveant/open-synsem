package ru.eventflow.synsem.configuration;

import opennlp.ccg.grammar.Grammar;
import ru.eventflow.synsem.modelchecker.HybridFrame;
import ru.eventflow.synsem.synsem.Configuration;
import ru.eventflow.synsem.synsem.GrammarLoader;
import ru.eventflow.synsem.synsem.HLDSModelBuilder;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;

public class XMLConfigurationModelBuilder {

    public static final String MODEL_NAME = "XML Configuration Model";

    public enum Modality {
        ROOM("room"), DEVICE("device"), MODIFIER("modifier"), ANCHOR("anchor"), NUM("num");

        private String label;

        private Modality(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    private HybridFrame frame;

    private HLDSModelBuilder hldsModelBuilder;

    public XMLConfigurationModelBuilder(InputStream in) {
        this(null, in);
    }

    // TODO refac
    public XMLConfigurationModelBuilder(Grammar grammar, InputStream in) {
        try {
            if (grammar == null) {
                grammar = new GrammarLoader().load(Configuration.getInstance().getGrammarUrl());
            }

            Unmarshaller unmarshaller = JAXBContext.newInstance(XMLConfiguration.class).createUnmarshaller();
            XMLConfiguration config = (XMLConfiguration) unmarshaller.unmarshal(new StreamSource(in));

            frame = new HybridFrame(MODEL_NAME);
            hldsModelBuilder = new HLDSModelBuilder(grammar, frame); // a builder for parts of a model coming from HLDS
            generateModel(frame, config.getLocations(), config.getDevices());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public HybridFrame getFrame() {
        return frame;
    }

    /**
     * 1. The set of modalities is predefined, they are {room, device, modifier, anchor}
     * 2. There is a root node
     * 3. Both devices and rooms and certain modifiers are worlds in a frame
     * 4. Ids are world labels, titles are nominals (lowercased), names are propositional symbols
     * 5. Other information is omitted for now
     *
     * @return
     */
    private HybridFrame generateModel(HybridFrame frame, Location[] locations, Device[] devices) {

        // root node
        frame.addWorld("n_root");
        frame.setNominalAssign("root", "n_root");

        for (Location location : locations) {
            String roomNodeLabel = "l_" + location.getId();    // r_ is room
            frame.addWorld(roomNodeLabel);

            for (String name : location.getNames()) {
                frame.setPropSym(name.toLowerCase(), roomNodeLabel);
            }

            frame.setAccessible(Modality.ROOM.getLabel(), "n_root", roomNodeLabel);

            String nominalLabel = String.valueOf(location.getId());
            frame.setNominalAssign(nominalLabel, roomNodeLabel);
        }

        for (Device device : devices) {
            String deviceNodeLabel = String.valueOf(device.getId());
            frame.addWorld(deviceNodeLabel);
            frame.setAccessible(Modality.DEVICE.getLabel(), "n_root", deviceNodeLabel);

            String nominalLabel = "n_" + device.getId();  // n_ is nominal
            frame.setNominalAssign(nominalLabel, deviceNodeLabel);

            frame.setPropSym(String.valueOf(device.getId()), deviceNodeLabel);

            String modifierLabel = "m_" + device.getId(); // m_ is modifier
            frame.addWorld(modifierLabel);
            frame.setAccessible(Modality.MODIFIER.getLabel(), deviceNodeLabel, modifierLabel);
            frame.setAccessible(Modality.ANCHOR.getLabel(), modifierLabel, "l_" + device.getLocation()); // r_ is room

            for (String name : device.getNames()) {
                hldsModelBuilder.buildAndAttachToFrame(deviceNodeLabel, name);
            }
        }

        return frame;
    }


}
