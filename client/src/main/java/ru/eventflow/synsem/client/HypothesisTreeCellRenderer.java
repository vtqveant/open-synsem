package ru.eventflow.synsem.client;

import ru.eventflow.asr.recognition.RecognitionHypothesis;
import ru.eventflow.synsem.model.Hypothesis;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

class HypothesisTreeCellRenderer implements TreeCellRenderer {

    DefaultTreeCellRenderer defaultRenderer = new DefaultTreeCellRenderer();

    Color backgroundSelectionColor;
    Color backgroundNonSelectionColor;

    HypothesisTreeCellRenderer() {
        backgroundSelectionColor = defaultRenderer.getBackgroundSelectionColor();
        backgroundNonSelectionColor = defaultRenderer.getBackgroundNonSelectionColor();
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Component returnValue = null;
        if (value != null && (value instanceof DefaultMutableTreeNode)) {
            Object userObject = ((DefaultMutableTreeNode) value).getUserObject();

            // this is a hypothesis from ASR
            if (userObject instanceof RecognitionHypothesis) {
                RecognitionHypothesis hypothesis = (RecognitionHypothesis) userObject;
                JLabel label = new JLabel();
                label.setText(hypothesis.getOrthography());
                returnValue = label;
            }

            // TODO refac!
            // this is a hypothesis from SynSem
            if (userObject instanceof Hypothesis) {
                Hypothesis hypothesis = (Hypothesis) userObject;
                JLabel label = new JLabel();
                label.setText(hypothesis.getCategory().getSupertag() + " (" + hypothesis.getScore() + ")");
                returnValue = label;
            }
        }

        if (returnValue == null) {
            returnValue = defaultRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        }
        return returnValue;
    }
}
