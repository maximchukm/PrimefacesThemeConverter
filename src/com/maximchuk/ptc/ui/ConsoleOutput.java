package com.maximchuk.ptc.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConsoleOutput extends JDialog {
    private JPanel contentPane;
    private JTextArea consoleOutArea;
    private JButton hideButton;
    private JScrollPane scrollPane;

    public ConsoleOutput() {
        setContentPane(contentPane);
        setTitle("Console output");
        setSize(800, 600);

        hideButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
    }

    public void writeLine(String line) {
        consoleOutArea.append(line);
        consoleOutArea.append("\n");
    }

    public void clear() {
        consoleOutArea.setText("");
    }
}
