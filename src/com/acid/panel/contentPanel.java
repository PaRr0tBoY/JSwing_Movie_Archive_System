package com.acid.panel;

import javax.swing.*;
import java.awt.*;

/**
 * @author R机械223李明阳
 * 提供背景图片绘制功能
 */
public class contentPanel extends JPanel {
    private Image backgroundImage;

    public contentPanel(String imagePath) {
        // 加载背景图片
        backgroundImage = new ImageIcon(imagePath).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 绘制背景图片
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}