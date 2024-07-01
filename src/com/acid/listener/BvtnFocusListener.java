package com.acid.listener;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
/**
 * @author R机械223李明阳
 * 封装对于按钮的聚焦监听器
 */
public class BvtnFocusListener extends FocusAdapter {
    JButton bvtn;

    public void setBvtn(JButton bvtn) {
        this.bvtn = bvtn;
    }

    @Override
    public void focusGained(FocusEvent e) {
        bvtn.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
    }

    @Override
    public void focusLost(FocusEvent e) {
        bvtn.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
    }
}