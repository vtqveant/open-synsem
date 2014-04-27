package ru.eventflow.synsem.configuration;

import ru.eventflow.synsem.model.Query;
import ru.eventflow.synsem.modelchecker.GraphMLWriter;
import ru.eventflow.synsem.modelchecker.HybridFrame;
import ru.eventflow.synsem.synsem.Configuration;
import ru.eventflow.synsem.synsem.SynSemProcessor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.io.StringWriter;

public class Runner {

    public static void main(String[] args) throws JAXBException {
//        marshall();
//        unmarshall();

        /**
         * SynSem
         */
        final Configuration config = Configuration.getInstance();
        String grammar = config.getGrammarUrl();

        InputStream in = Runner.class.getResourceAsStream("/configuration/test_config.xml");
        XMLConfigurationModelBuilder cm = new XMLConfigurationModelBuilder(in);
        HybridFrame frame = cm.getFrame();
        String s = GraphMLWriter.serialize(frame);
        System.out.println(s);

        SynSemProcessor processor = new SynSemProcessor(grammar, frame);
        processor.process(new Query("включи лампу"));

    }

    public static void unmarshall() throws JAXBException {
        InputStream in = Runner.class.getResourceAsStream("/configuration/test_config.xml");
        Unmarshaller unmarshaller = JAXBContext.newInstance(XMLConfiguration.class).createUnmarshaller();
        XMLConfiguration config = (XMLConfiguration) unmarshaller.unmarshal(new StreamSource(in));
        System.out.println(config);
    }


    public static void marshall() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(XMLConfiguration.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);

        Location[] locations = new Location[3];
        locations[0] = new Location(0, new String[]{"кухня", "кухонька"}, new int[]{2});
        locations[1] = new Location(1, new String[]{"комната"}, new int[]{2});
        locations[2] = new Location(2, new String[]{"прихожая"}, new int[]{0, 1});

        Device[] devices = new Device[4];
        devices[0] = new Device(0, "lamp", null, 0, new String[]{"лампочка на кухне"});
        devices[1] = new Device(1, "lamp", null, 0, new String[]{"красная лампочка", "лампочка на кухне"});
        devices[2] = new Device(2, "lamp", null, 0, new String[]{"желтая лампа", "лампа на кухне"});
        devices[3] = new Device(3, "lamp", null, 0, new String[]{"лампочка в прихожей"});
        XMLConfiguration XMLConfiguration = new XMLConfiguration(locations, devices);

        StringWriter sw = new StringWriter();
        marshaller.marshal(XMLConfiguration, sw);
        System.out.println(sw.toString());
    }
}
