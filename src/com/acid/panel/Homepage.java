package com.acid.panel;

import com.acid.login.login;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * @author R机械223李明阳
 * 主页，没有太多设计，主要是作为视觉缓冲，避免主页面一打开就是大量内容
 */
public class Homepage extends JPanel {
    private JLabel titleLable;
    private JLabel userLable;
    private JPanel contentPanel;
    public Homepage(){
        this.setLayout(null);
        //背景面版
        contentPanel = new contentPanel("img/bkgd.jpg");
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPanel.setLayout(null);
        contentPanel.setBounds(0, 0, 800, 800);
        //问好
        userLable = new JLabel("你好！"+ login.loginedUserName + " :)");
        userLable.setBounds(250,250,300,70);
        userLable.setForeground(Color.decode("#517DAF"));
        userLable.setFont(new Font("楷体", Font.BOLD, 50));
        contentPanel.add(userLable);

        //标题
        titleLable = new JLabel("欢迎使用电影档案系统！");
        titleLable.setBounds(260,325,300,30);
        titleLable.setForeground(Color.decode("#517DAF"));
        titleLable.setFont(new Font("楷体", Font.BOLD, 25));
        contentPanel.add(titleLable);
        this.add(contentPanel);
    }
}
