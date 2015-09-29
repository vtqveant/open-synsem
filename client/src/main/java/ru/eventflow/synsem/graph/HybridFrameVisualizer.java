package ru.eventflow.synsem.graph;

import edu.uci.ics.jung.algorithms.layout.*;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import org.apache.commons.collections15.Transformer;

import javax.swing.*;
import java.io.InputStream;
import java.util.logging.Logger;

public class HybridFrameVisualizer {

    private static Logger logger = Logger.getLogger(HybridFrameVisualizer.class.getName());
    private static final String XML = "/hlds_generated.graphml";

    public static void main(String[] args) {

        try {
            InputStream is = HybridFrameVisualizer.class.getResourceAsStream(XML);
            DirectedGraph<Node, Edge> graph = GraphReader.buildGraph(is);

            for (Node f : graph.getVertices()) {
                logger.info(f.toString());
            }

            Layout<Node, Edge> layout = new KKLayout<Node, Edge>(graph);
            VisualizationViewer vv = new VisualizationViewer<Node, Edge>(layout);

            final DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();

            vv.setGraphMouse(graphMouse);

            JComboBox modeBox = graphMouse.getModeComboBox();
            modeBox.addItemListener(graphMouse.getModeListener());
            graphMouse.setMode(ModalGraphMouse.Mode.PICKING);

            vv.getRenderContext().setVertexLabelTransformer(new Transformer<Node, String>() {
                @Override
                public String transform(Node node) {
                    String retval = "";
                    String s;
                    if ((s = node.getNominals()) != null) retval += s;
                    if ((s = node.getPropositionalSymbols()) != null) retval += s;
                    return retval;
                }
            });

            vv.setVertexToolTipTransformer(new Transformer<Node, String>() {
                @Override
                public String transform(Node node) {
                    return node.getLabel();
                }
            });

            vv.getRenderContext().setEdgeLabelTransformer(new Transformer<Edge, String>() {
                @Override
                public String transform(Edge edge) {
                    return "<" + edge.getModality() + ">";
                }
            });

            vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<Edge, String>());

            JFrame jf = new JFrame();
            jf.getContentPane().add(new GraphZoomScrollPane(vv));

            jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            jf.pack();
            jf.setVisible(true);

        } catch (Exception e) {
            logger.severe("failed");
            e.printStackTrace();
        }
    }


}
