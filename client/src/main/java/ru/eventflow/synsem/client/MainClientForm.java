package ru.eventflow.synsem.client;

import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import opennlp.ccg.grammar.Grammar;
import org.apache.commons.collections15.Transformer;
import ru.eventflow.asr.recognition.*;
import ru.eventflow.synsem.graph.Edge;
import ru.eventflow.synsem.graph.GraphReader;
import ru.eventflow.synsem.graph.Node;
import ru.eventflow.synsem.model.Hypothesis;
import ru.eventflow.synsem.model.Query;
import ru.eventflow.synsem.modelchecker.GraphMLWriter;
import ru.eventflow.synsem.modelchecker.HybridFrame;
import ru.eventflow.synsem.synsem.Configuration;
import ru.eventflow.synsem.synsem.GrammarLoader;
import ru.eventflow.synsem.synsem.HLDSModelBuilder;
import ru.eventflow.synsem.synsem.SynSemProcessor;

import javax.sound.sampled.AudioFileFormat;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class MainClientForm extends JFrame implements IRecognitionListener {

    private IRecognizer recognizer;
    private Grammar grammar;

    private enum State {
        READY, LISTENING, RECOGNIZING, ERROR
    }

    public static final String AUDIO_FILE = "/tmp/synsem-speech.wav";

    private JButton button;
    private JPanel rootPanel;
    private JProgressBar progress;
    private JTextField status;
    private JTree tree;
    private JComboBox combo;
    private JPanel details;

    private SynSemProcessor processor;
    private Microphone microphone;

    public MainClientForm() {
        microphone = new Microphone(AudioFileFormat.Type.WAVE);

        // init SynSem analyzer
        Configuration synsemConfiguration = Configuration.getInstance();
        try {
            String grammarUrl = synsemConfiguration.getGrammarUrl();
            grammar = new GrammarLoader().load(grammarUrl);

        } catch (IOException e) {
            e.printStackTrace();
        }
        processor = new SynSemProcessor(grammar, new HybridFrame("FIXME")); // TODO load frame from somewhere

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                onRecordAction();
            }

            @Override
            public void mouseReleased(MouseEvent event) {
                onStopAction();
            }
        });

        combo.addItem(BackendProfile.Vendor.DUMMY);
        combo.addItem(BackendProfile.Vendor.SPHINX);
        combo.addItem(BackendProfile.Vendor.GOOGLE);

        combo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                onBackendChange(actionEvent);
            }
        });

        tree.setShowsRootHandles(true);
        tree.setRootVisible(false);
        tree.setCellRenderer(new HypothesisTreeCellRenderer());
        clearTree();
    }


    public void setBackendProfile(BackendProfile.Vendor backend) {
        microphone.setAudioFormat(BackendProfile.getAudioFormat(backend));
        try {
            recognizer = BackendProfile.getRecognizer(backend);
        } catch (RecognizerException e) {
            setStatus(State.ERROR, e.getMessage());
        }
    }

    private void onRecordAction() {
        try {
            setStatus(State.LISTENING);
            microphone.captureAudioToFile(AUDIO_FILE);
        } catch (Exception e) {
            e.printStackTrace();
            setStatus(State.ERROR);
        }
    }

    private void onStopAction() {
        setStatus(State.READY);
        microphone.close();

        // start recognition
        Runnable recognizerRunnable = new RecognizerRunnable(recognizer, new File(AUDIO_FILE), this);
        new Thread(recognizerRunnable).start();
    }

    private void onBackendChange(ActionEvent event) {
        BackendProfile.Vendor vendor = (BackendProfile.Vendor) combo.getSelectedItem();
        System.out.println(vendor.toString());
        setBackendProfile(vendor);
    }

    @Override
    public void onRecognitionResult(RecognitionResult result) {
        clearTree();
        if (result.getHypotheses() != null) {

            // presentation
            DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
            for (RecognitionHypothesis h : result.getHypotheses()) {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(h);

                // synsem analysis is added to the same tree
                // TODO think again
                Query q = new Query(h.getOrthography());
                processor.process(q);
                for (Hypothesis synsemHypothesis : q.getHypotheses()) {
                    node.add(new DefaultMutableTreeNode(synsemHypothesis));
                }

                root.add(node);
            }
            tree.setModel(new DefaultTreeModel(root));

            // reinit selection listener
            tree.addTreeSelectionListener(new TreeSelectionListener() {
                @Override
                public void valueChanged(TreeSelectionEvent event) {
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                    if (selectedNode != null) {
                        Object o = selectedNode.getUserObject();
                        if (o instanceof RecognitionHypothesis) {
                            RecognitionHypothesis hypothesis = (RecognitionHypothesis) selectedNode.getUserObject();

                            JTextPane pane = new JTextPane();
                            pane.setText(hypothesis.getOrthography() + ", score = " + hypothesis.getScore() + ", confidence = " + hypothesis.getConfidence());
                            details.removeAll();
                            details.add(pane);
                            details.updateUI();
                        } else if (o instanceof Hypothesis) {
                            try {
                                // build an
                                Hypothesis h = (Hypothesis) selectedNode.getUserObject();
                                HLDSModelBuilder modelBuilder = new HLDSModelBuilder(grammar);
                                modelBuilder.build(h.getInput());
                                HybridFrame frame = modelBuilder.getFrame();
                                String graphml = GraphMLWriter.serialize(frame);

                                details.removeAll();
                                GraphZoomScrollPane graphZoomScrollPane = getGraphPane(graphml);
                                details.add(graphZoomScrollPane);
                                details.updateUI();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        } else {
            JTextPane pane = new JTextPane();
            pane.setText("no hypotheses");
            details.removeAll();
            details.add(pane);
            details.updateUI();
        }
        setStatus(State.READY);
    }

    // TODO resize listener
    public GraphZoomScrollPane getGraphPane(String graphml) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(graphml.getBytes());
        DirectedGraph<Node, Edge> graph = GraphReader.buildGraph(in);

        Layout<Node, Edge> layout = new KKLayout<Node, Edge>(graph);
        VisualizationViewer vv = new VisualizationViewer<Node, Edge>(layout);
        vv.setAutoscrolls(true);

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

        return new GraphZoomScrollPane(vv);
    }

    @Override
    public void onRecognitionStarted() {
        setStatus(State.RECOGNIZING);
    }

    @Override
    public void onRecognitionError(String message) {
        if (message != null) {
            setStatus(State.ERROR, ": " + message.toLowerCase());
        } else {
            setStatus(State.ERROR);
        }
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    private void setStatus(State state) {
        setStatus(state, "");
    }

    private void setStatus(State state, String message) {
        switch (state) {
            case LISTENING:
                progress.setIndeterminate(true);
                progress.setForeground(Color.BLUE);
                status.setText("capturing audio...");
                break;
            case RECOGNIZING:
                progress.setIndeterminate(true);
                progress.setForeground(Color.YELLOW);
                status.setText("recognizing");
                break;
            case ERROR:
                progress.setIndeterminate(false);
                progress.setForeground(Color.RED);
                status.setText("error" + message);
                break;
            case READY:
            default:
                progress.setIndeterminate(false);
                status.setText("ready");
                break;
        }
    }

    private void clearTree() {
        tree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("root")));
    }

}
