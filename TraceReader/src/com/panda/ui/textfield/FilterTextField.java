package com.panda.ui.textfield;

import com.panda.ui.TraceFrame;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/***
 * 搜索
 * @author qiantao
 */
public class FilterTextField extends JTextField {
    TraceFrame traceFrame;
    String regString;

    public FilterTextField(TraceFrame jframe) {
        this.traceFrame = jframe;
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    //System.out.println(searchField.getText());
                    regString = FilterTextField.this.getText();
                    FilterTextField.this.traceFrame.evalSearch(regString);
                }
            }

        });
    }
}
