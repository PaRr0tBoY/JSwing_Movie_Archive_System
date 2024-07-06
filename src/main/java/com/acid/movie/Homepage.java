package com.acid.movie;

import com.acid.user.Main;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author R机械223李明阳
 * 主页，没有太多设计，主要是作为视觉缓冲，避免主页面一打开就是大量内容
 */
public class Homepage extends JPanel {
    private JLabel titleLable;
    private final JLabel hintLable;
    private final JLabel authorLable;
    private JLabel userLable;
    private final JPanel contentPanel;
    private JPanel imgPanel;

    private Image temp1;
    private ImageIcon icon;
    private JLabel imgLable;
    private final JButton movieButton;
    private final JButton starButton;
    private String role;
    private JTabbedPane jtp;

    public void setJTP(JTabbedPane jtp) {
        this.jtp = jtp;
    }

    public Homepage(){
        this.setLayout(null);
        //背景面版
        contentPanel = new JPanel();
        contentPanel.setBackground(Color.decode(Main.mainbgColor));
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPanel.setLayout(null);
        contentPanel.setBounds(0, 0, 800, 800);

        //标题
        hintLable = new JLabel("电影档案系统");
        hintLable.setBounds(240, 205,320,120);
        hintLable.setForeground(Color.decode(Main.textColor));
        hintLable.setFont(new Font("华文行楷", Font.BOLD, 50));
        contentPanel.add(hintLable);

        //作者
        authorLable = new JLabel("Proudly by Acid v1.0.0");
        authorLable.setBounds(320, 290,300,20);
        authorLable.setForeground(Color.decode(Main.hintTextColor));
        authorLable.setFont(new Font("楷体", Font.BOLD, 12));
        contentPanel.add(authorLable);


        /*
        //引导
        hintLable = new JLabel("今天想做什么？");
        hintLable.setBounds(220, 205,300,50);
        hintLable.setForeground(Color.white);
        hintLable.setFont(new Font("楷体", Font.BOLD, 35));
        contentPanel.add(hintLable);
        */

        //电影页按钮
        movieButton = new JButton("电影页");
        //movieButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        movieButton.setBackground(Color.decode(Main.buttonColor));
        movieButton.setForeground(Color.white);
        movieButton.setFont(new Font("仿宋", Font.BOLD, 15));
        movieButton.setBounds(265, 325, 120, 30);
        /*
        ImageIcon icon1 = new ImageIcon("img/.png");
        Image temp1 = icon1.getImage().getScaledInstance(movieButton.getWidth(), movieButton.getHeight(), icon1.getImage().SCALE_DEFAULT);
        icon1 = new ImageIcon(temp1);
        movieButton.setIcon(icon1);
         */
        movieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jtp != null) {
                    jtp.setSelectedIndex(1);
                }
            }
        });
        contentPanel.add(movieButton);

        //收藏夹按钮
        starButton = new JButton("收藏夹");
        //starButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        starButton.setBackground(Color.decode(Main.buttonColor));
        starButton.setForeground(Color.white);
        starButton.setFont(new Font("仿宋", Font.BOLD, 15));
        starButton.setBounds(415, 325, 120, 30);
        starButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jtp != null) {
                    jtp.setSelectedIndex(2);
                }
            }
        });
        contentPanel.add(starButton);

        this.add(contentPanel);
    }
}
