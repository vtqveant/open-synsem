package ru.eventflow.synsem.client.ui.view;

import ru.eventflow.synsem.client.ui.Defaults;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class MessagesSliderView extends SliderPanel {

    private static final int LIMIT = 1000;

    private final JTextArea textArea;
    private final Element root;
    private int count = 0;

    public MessagesSliderView() {
        super();

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setMargin(new Insets(2, 5, 2, 5));
        textArea.setFont(Defaults.SMALL_FONT);
        root = textArea.getDocument().getDefaultRootElement();

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void addRecord(String record) {
        textArea.append(record + '\n');
        count++;

        if (count > LIMIT) {
            try {
                Element first = root.getElement(0);
                textArea.getDocument().remove(first.getStartOffset(), first.getEndOffset());
                count--;
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ImageIcon getIcon() {
        return new ImageIcon(ClassLoader.getSystemResource("images/log.png"));
    }

    @Override
    public String getTitle() {
        return "Messages";
    }

    @Override
    public List<AbstractButton> getButtons() {
        List<AbstractButton> buttons = new ArrayList<AbstractButton>();

        ImageIcon trashIcon = new ImageIcon(ClassLoader.getSystemResource("images/trash.gif"));
        JButton clearBtn = new JButton(trashIcon);
        clearBtn.setSelected(true);
        clearBtn.setToolTipText("Clear");
        clearBtn.setFocusable(false);
        clearBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
                count = 0;
            }
        });
        buttons.add(clearBtn);

        return buttons;
    }
}
