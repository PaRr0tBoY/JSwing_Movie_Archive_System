package com.acid.panel;

import com.acid.jdbcUtil.jdbcUtil;
import com.acid.listener.BvtnFocusListener;
import com.acid.login.login;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author R机械223李明阳
 * 个人界面的实现，可以在此页面查看用户信息、收藏夹
 */
public class personal extends JPanel {
    private JPanel contentPanel;
    private JLabel nameLable;
    private JLabel userRoleLable;
    private JButton starsButton;
    private BvtnFocusListener focusBvtn;

    Connection con;
    PreparedStatement pstmt;
    ResultSet rs;
    movieArchiveTable mat;

    public personal() {
        this.setLayout(null);

        //内容面板
        contentPanel = new contentPanel("img/bkgd.jpg");
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPanel.setLayout(null);
        contentPanel.setBounds(0, 0, 800, 800);
        nameLable = new JLabel("昵称: " + login.loginedUserName);
        nameLable.setBounds(300,20,300,30);
        nameLable.setForeground(Color.decode("#517DAF"));
        nameLable.setFont(new Font("楷体", Font.BOLD, 18));
        contentPanel.add(nameLable);

        //显示用户身分组
        userRoleLable = new JLabel("身份: " + login.loginedUserRole);
        userRoleLable.setBounds(300,60,300,30);
        userRoleLable.setForeground(Color.decode("#517DAF"));
        userRoleLable.setFont(new Font("楷体", Font.BOLD, 18));
        contentPanel.add(userRoleLable);

        //收藏夹
        starsButton = new JButton("收藏夹");
        starsButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        starsButton.setBounds(300, 100, 80, 23);
        starsButton.setBackground(Color.decode("#517DAF"));
        starsButton.setForeground(Color.white);
        focusBvtn = new BvtnFocusListener();
        focusBvtn.setBvtn(starsButton);
        starsButton.addFocusListener(focusBvtn);
        mat = new movieArchiveTable();
        starsButton.addActionListener(e -> {
            if (e.getSource() == starsButton){
                try {
                    con = jdbcUtil.ConnectDB("movielist", "root", "admin123");
                    String sql = "SELECT m.* FROM favourites f JOIN users u ON f.user_id = u.id JOIN movies m ON f.movie_id = m.编号 WHERE u.id = ?";
                    pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, login.loginedUserId);
                    rs = pstmt.executeQuery();
                    mat.updateTable(rs);
                    mat.setInterectable(false);
                    mat.setBounds(0, 150, 800, 400);
                    add(mat);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        contentPanel.add(starsButton);

        this.add(contentPanel);
    }
}
