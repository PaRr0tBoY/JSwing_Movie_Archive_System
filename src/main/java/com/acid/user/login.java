package com.acid.user;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

import com.acid.util.jdbcUtil;
import com.acid.listener.tfFocusListener;
import com.acid.movie.movieArchiveSystem;
import com.formdev.flatlaf.extras.FlatSVGIcon;

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
    private JRadioButton user;
    private JRadioButton admin;
    private tfFocusListener focusInputBox;

    public static String loginedUserName;
    public static String loginedUserId;
    public static String selectedIdentity;

    private Connection con;
    private PreparedStatement pstmt;

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
        contentPanel = new JPanel();
        //getContentPane().add(contentPanel);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        //setContentPane(contentPanel);
        contentPanel.setLayout(null);
        contentPanel.setBackground(Color.decode(Main.mainbgColor));

        // 设置窗口图标
        FlatSVGIcon icon = new FlatSVGIcon("miLogin.svg", 16, 16);
        setIconImage(icon.getImage());

        //页面标题
        titleLable = new JLabel();
        titleLable.setText("欢迎访问电影档案系统");
        titleLable.setBounds(95,20,300,30);
        titleLable.setForeground(Color.decode(Main.textColor));
        titleLable.setFont(new Font("华文行楷", Font.PLAIN, 25));
        contentPanel.add(titleLable);

        /*
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
         */

        //引导标签
        label4 = new JLabel("还没有账号？去");
        label4.setBounds(165,221,120,23);
        label4.setForeground(Color.decode(Main.hintTextColor));
        contentPanel.add(label4);
        //选择身份组
        user = new JRadioButton("爱好者", true);
        admin = new JRadioButton("管理员", false);
        user.setBounds(130,70,65,20);//115
        admin.setBounds(245,70,65,20);
        user.setForeground(Color.decode(Main.textColor));
        admin.setForeground(Color.decode(Main.textColor));
        contentPanel.add(user);
        contentPanel.add(admin);
        ButtonGroup group = new ButtonGroup();
        group.add(user);
        group.add(admin);

        //账号输入框
        loginField = new JTextField("请输入您的用户名！");
        loginField.setForeground(Color.decode(Main.hintTextColor));
        loginField.setBackground(Color.decode(Main.textFieldColor));
        loginField.setBounds(110,110,215,25);
        //loginField.setBorder(new EtchedBorder());
        focusInputBox = new tfFocusListener();
        focusInputBox.setInputBox(loginField,"请输入您的用户名！");
        loginField.addFocusListener(focusInputBox);
        contentPanel.add(loginField);

        //密码输入框
        passwordField = new JPasswordField("123456789");
        passwordField.setForeground(Color.decode(Main.hintTextColor));
        passwordField.setBackground(Color.decode(Main.textFieldColor));
        passwordField.setBounds(110,150,215,25);
        //passwordField.setBorder(new EtchedBorder());
        focusInputBox = new tfFocusListener();
        focusInputBox.setInputBox(passwordField,"123456789");
        passwordField.addFocusListener(focusInputBox);
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
        loginButton.setBounds(175,190,80,23);
        loginButton.setBackground(Color.decode(Main.buttonColor));//517DAF
        loginButton.setForeground(Color.white);
        //loginButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        contentPanel.add(loginButton);
        //从数据库读取用户信息
        loginButton.addActionListener(e -> {
            if(e.getSource()==loginButton){
                new Thread(() -> {
                    try{
                        if(user.isSelected()){
                            selectedIdentity = "user";
                            //System.out.println(selectedIdentity);
                        }else {
                            selectedIdentity = "admin";
                            //System.out.println("管理员");
                        }
                        con = jdbcUtil.ConnectDB("movielist","root","admin123");
                        String sql = "SELECT * FROM users WHERE username ='" + loginField.getText() + "' AND password='" + passwordField.getText() + "' AND user_role='" + selectedIdentity + "'";
                        pstmt = con.prepareStatement(sql);
                        ResultSet rs = pstmt.executeQuery();
                        if(rs.next()) {
                            dispose();
                            loginedUserName = loginField.getText();
                            loginedUserId = rs.getString("id");
                            new movieArchiveSystem();
                        }else{
                            JOptionPane.showMessageDialog(login.this, "用户名、密码或身份组错误，请重试");
                        }
                    } catch (Exception e0){
                        e0.printStackTrace();
                    } finally {
                        jdbcUtil.result(con, pstmt, null);
                    }
                }).start();
            }
        });
        contentPanel.add(loginButton);
        //注册按钮
        registerButton = new JButton("注册");
        registerButton.setBounds(225,222,80,23);
        registerButton.setBorderPainted(false);
        registerButton.setContentAreaFilled(false);
        registerButton.setFocusPainted(false);
        registerButton.setOpaque(false);
        registerButton.setForeground(Color.decode(Main.hintTextColor));
        registerButton.setFont(new Font("宋体", Font.BOLD, 12));
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
                registerButton.setForeground(Color.decode(Main.textColor));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                registerButton.setForeground(Color.decode(Main.hintTextColor));
            }
        });
        contentPanel.add(registerButton);
        add(contentPanel);
    }
}
