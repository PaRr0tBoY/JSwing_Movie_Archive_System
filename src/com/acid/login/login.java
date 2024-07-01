package com.acid.login;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.acid.jdbcUtil.jdbcUtil;
import com.acid.listener.BvtnFocusListener;
import com.acid.listener.tfFocusListener;
import com.acid.panel.contentPanel;
import com.acid.panel.movieArchiveSystem;
/**
 * @author R机械223李明阳
 * 登录页面
 */
public class login extends JFrame {
    private JPanel contentPanel;
    private JTextField loginField;
    private JPasswordField passwordField;

    private JButton loginButton;
    private JButton registerButton;

    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JLabel titleLable;

    private JComboBox identityChoose;
    tfFocusListener focus;
    BvtnFocusListener focusBvtn;

    public static String loginedUserName;
    public static String loginedUserRole;
    public static String loginedUserId;

    Connection con;
    PreparedStatement pstmt;

    public login() {
        setTitle("用户登录");
        setSize(450, 300);
        this.setLocationRelativeTo(null);
        init();
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void init() {
        //添加面板容器
        contentPanel = new contentPanel("img/bkgd.jpg");
        getContentPane().add(contentPanel);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPanel);
        contentPanel.setLayout(null);
        // 设置窗口图标
        ImageIcon icon = new ImageIcon("img/login.png");
        this.setIconImage(icon.getImage());
        //页面标题
        titleLable = new JLabel();
        titleLable.setText("<html><u>欢迎访问电影档案系统</u></html>");
        titleLable.setBounds(130,20,300,30);
        titleLable.setForeground(Color.decode("#517DAF"));
        titleLable.setFont(new Font("宋体", Font.BOLD, 20));
        contentPanel.add(titleLable);
        //账号标签
        label1 = new JLabel("用户名称:");
        label1.setBounds(100,106,54,28);
        contentPanel.add(label1);
        //密码标签
        label2 = new JLabel("输入密码:");
        label2.setBounds(100,146,54,28);
        contentPanel.add(label2);
        //用户身分组标签
        label3 = new JLabel("选择身份组:");
        label3.setBounds(90,66,100,28);
        //引导标签
        label4 = new JLabel("还没有账号？去");
        label4.setBounds(180,221,120,23);
        contentPanel.add(label4);
        //选择身份组
        identityChoose = new JComboBox();
        identityChoose.setBounds(159,70,161,25);
        identityChoose.addItem("user");
        identityChoose.addItem("admin");
        contentPanel.add(identityChoose);
        contentPanel.add(label3);
        //账号输入框
        loginField = new JTextField("请输入您的用户名！");
        loginField.setForeground(Color.GRAY);
        loginField.setBounds(159,110,161,25);
        loginField.setBorder(new EtchedBorder());
        focus = new tfFocusListener();
        focus.setInputBox(loginField,"请输入您的用户名！");
        loginField.addFocusListener(focus);
        contentPanel.add(loginField);

        //密码输入框
        passwordField = new JPasswordField("123456789");
        passwordField.setForeground(Color.GRAY);
        passwordField.setBounds(159,150,161,25);
        passwordField.setBorder(new EtchedBorder());
        focus = new tfFocusListener();
        focus.setInputBox(passwordField,"123456789");
        passwordField.addFocusListener(focus);
        contentPanel.add(passwordField);
        //密码框回车触发登录按钮
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginButton.doClick();
                }
            }
        });
        contentPanel.add(passwordField);
        //登录按钮
        loginButton = new JButton("登录");
        loginButton.setBounds(200,190,80,23);
        loginButton.setBackground(Color.decode("#517DAF"));
        loginButton.setForeground(Color.white);
        loginButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        focusBvtn = new BvtnFocusListener();
        focusBvtn.setBvtn(loginButton);
        loginButton.addFocusListener(focusBvtn);
        contentPanel.add(loginButton);

        //从数据库读取用户信息
        loginButton.addActionListener(e -> {
            if(e.getSource()==loginButton){
                try{
                    con = jdbcUtil.ConnectDB("movielist","root","admin123");
                    String sql = "SELECT * FROM users WHERE username ='" + loginField.getText() + "' AND password='" + passwordField.getText() + "' AND user_role='" + identityChoose.getSelectedItem().toString() + "'";
                    pstmt = con.prepareStatement(sql);
                    ResultSet rs = pstmt.executeQuery();
                    if(rs.next()) {
                        dispose();
                        loginedUserName = loginField.getText();
                        loginedUserRole = identityChoose.getSelectedItem().toString();
                        loginedUserId = rs.getString("id");
                        new movieArchiveSystem();
                    }else{
                        JOptionPane.showMessageDialog(login.this, "用户名或密码错误，请重试");
                    }
                } catch (Exception e0){
                    e0.printStackTrace();
                } finally {
                    jdbcUtil.result(con, pstmt, null);
                }
            }
        });
        contentPanel.add(loginButton);
        //注册按钮
        registerButton = new JButton("注册");
        registerButton.setBounds(240,221,80,23);
        registerButton.setBorderPainted(false);
        registerButton.setContentAreaFilled(false);
        registerButton.setFocusPainted(false);
        registerButton.setOpaque(false);
        registerButton.setForeground(Color.darkGray);
        registerButton.setFont(new Font("SansSerif", Font.BOLD, 10));
        registerButton.addActionListener(e -> {
            if (e.getSource() == registerButton) {
                dispose();
                new register();
            }
        });
        //鼠标悬浮变色模仿超链
        registerButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                registerButton.setForeground(Color.blue);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                registerButton.setForeground(Color.darkGray);
            }
        });
        contentPanel.add(registerButton);
    }
}
