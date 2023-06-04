package edu.uniandes.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class CommandLine extends JPanel {
    private static final int COMMAND_LENGTH = 20;
    private static final int FONT_SIZE = 12;
    private final JTextField command = new JTextField(COMMAND_LENGTH);
    private final ParranderosV parent;

    {
        command.setFont(new Font(Font.MONOSPACED, Font.PLAIN, FONT_SIZE));
        command.addKeyListener(new KeyAdapter() {
            @Override public void keyTyped(KeyEvent e) {
                switch (e.getKeyChar()) {
                    case KeyEvent.VK_ENTER: executeToken(command.getText().trim());
                    case KeyEvent.VK_ESCAPE: command.setText(""); break;
                }
            }
        });
        command.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                command.setText("");
            }

            @Override public void focusLost(FocusEvent e) {
                command.setText("Enter command..");
            }
        });
    }

    CommandLine(ParranderosV parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        add(command, BorderLayout.CENTER);
    }

    private void executeToken(String token) {
        parent.actionPerformed(new ActionEvent(this, 0, token));
    }
}
