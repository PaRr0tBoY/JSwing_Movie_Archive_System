package com.acid.listener;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.SoftBevelBorder;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * @author R机械223李明阳
 * 封装对于文本框的聚焦监听器
 */
public class tfFocusListener extends FocusAdapter {
        JTextField inputBox;
        String hintText;
        public void setInputBox(JTextField inputBox,String hintText) {
            this.inputBox=inputBox;
            this.hintText=hintText;
        }
        @Override
        public void focusGained(FocusEvent e) {
            // 当输入框获得焦点时，如果输入框中的文本为默认文本，则清空文本
            if (inputBox.getText().equals(hintText)) {
                inputBox.setText("");
            }
            inputBox.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
        }

        @Override
        public void focusLost(FocusEvent e) {
            // 当输入框失去焦点时，如果输入框中没有输入任何内容，则恢复默认文本
            if (inputBox.getText().isEmpty()) {
                inputBox.setText(hintText);
            }
            inputBox.setBorder(new EtchedBorder());
        }
}
