package com.maximchuk.ptc.ui;

import com.maximchuk.ptc.entity.CssPropertyEntity;
import com.maximchuk.ptc.entity.CssPropertyEnum;
import com.maximchuk.ptc.ui.table.CssPropertyTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddCssPropertyDialog extends JDialog {
    private CssPropertyTableModel tableModel;

    private JPanel contentPane;
    private JButton addButton;
    private JButton cancelButton;
    private JLabel componentNameLabel;
    private JTextArea cssClassValueInput;
    private JLabel cssClassValue;
    private JComboBox cssPropertyComobBox;

    public AddCssPropertyDialog(final CssPropertyTableModel tableModel) {
        setSize(400, 200);
        setTitle("Add new css property");

        for (CssPropertyEnum type: CssPropertyEnum.values()) {
            if (!tableModel.isExistCssProperty(type)) {
                cssPropertyComobBox.addItem(type);
            }
        }

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(addButton);

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.addRow(
                        new CssPropertyEntity((CssPropertyEnum) cssPropertyComobBox.getSelectedItem(),
                                cssClassValueInput.getText()));
                dispose();
            }
        });
    }

}
