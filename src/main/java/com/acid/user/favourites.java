package com.acid.user;

import com.acid.movie.movieArchiveTable;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author R机械223李明阳
 * 个人界面的实现，可以在此页面查看用户信息、收藏夹
 */
public class favourites extends JPanel {
    private JPanel contentPanel;
    private JPanel imgPanel;

    private Image temp1;
    private ImageIcon icon;

    private JLabel imgLable;
    private JLabel nameLable;
    private JLabel userRoleLable;
    private JButton starsButton;
    public static String role;

    private Connection con;
    private PreparedStatement pstmt;
    private ResultSet rs;
    private movieArchiveTable mat;

//    //更新用户名和身份组信息
//    public void updateInfo() {
//        if (login.selectedIdentity.equals("admin")) {
//            role = "管理员";
//        } else {
//            role = "电影爱好者";
//        }
//        remove(userRoleLable);
//        userRoleLable = new JLabel("昵称: " + login.loginedUserName + " 身份: " + role);
//        userRoleLable.setBounds(290,0,300,30);
//        userRoleLable.setForeground(Color.decode(Main.textColor));
//        userRoleLable.setFont(new Font("楷体", Font.BOLD, 14));
//        add(userRoleLable);
//    }

    public favourites() {
        this.setLayout(null);
        setBackground(Color.decode(Main.mainbgColor));
        /*
        //内容面板
        contentPanel = new contentPanel(Main.imgUrl);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPanel.setLayout(null);
        contentPanel.setBounds(0, 0, 800, 800);
        */
        //显示用户身分组
//        if (login.selectedIdentity.equals("admin")){
//            role = "管理员";
//        }else{
//            role = "电影爱好者";
//        }
//        userRoleLable = new JLabel("昵称: " + login.loginedUserName + " 身份: " + role);
//        userRoleLable.setBounds(290,0,300,30);
//        userRoleLable.setForeground(Color.decode(Main.textColor));
//        userRoleLable.setFont(new Font("楷体", Font.BOLD, 14));
//        add(userRoleLable);
    }
}
