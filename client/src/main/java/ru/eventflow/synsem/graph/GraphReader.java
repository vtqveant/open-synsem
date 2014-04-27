package ru.eventflow.synsem.graph;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.io.GraphMLMetadata;
import edu.uci.ics.jung.io.GraphMLReader;
import org.apache.commons.collections15.Factory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

public class GraphReader {


    public static DirectedGraph<Node, Edge> buildGraph(InputStream is) throws ParserConfigurationException, SAXException, IOException {
        InputStreamReader reader = new InputStreamReader(is);
        return buildGraph(reader);
    }

    public static DirectedGraph<Node, Edge> buildGraph(Reader reader) throws ParserConfigurationException, SAXException, IOException {

        Factory<Node> vertexFactory = new Factory<Node>() {
            public Node create() {
                return new Node();
            }
        };

        Factory<Edge> edgeFactory = new Factory<Edge>() {
            public Edge create() {
                return new Edge();
            }
        };

        GraphMLReader<DirectedGraph<Node, Edge>, Node, Edge> graphMLReader =
                new GraphMLReader<DirectedGraph<Node, Edge>, Node, Edge>(vertexFactory, edgeFactory);
        final DirectedSparseGraph<Node, Edge> graph = new DirectedSparseGraph<Node, Edge>();

        graphMLReader.load(reader, graph);

        final Map<String, GraphMLMetadata<Node>> vertexMetadata = graphMLReader.getVertexMetadata();
        for (Node node : graph.getVertices()) {
            node.setLabel(vertexMetadata.get("label").transformer.transform(node));
            node.setNominals(vertexMetadata.get("nominals").transformer.transform(node));
            node.setPropositionalSymbols(vertexMetadata.get("propsymbols").transformer.transform(node));
        }

        final Map<String, GraphMLMetadata<Edge>> edgeMetadata = graphMLReader.getEdgeMetadata();
        for (Edge edge : graph.getEdges()) {
            edge.setModality(edgeMetadata.get("modality").transformer.transform(edge));
        }

        return graph;
    }

}
