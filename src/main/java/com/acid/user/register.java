package com.acid.user;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.acid.util.jdbcUtil;
import com.acid.listener.tfFocusListener;
import com.formdev.flatlaf.extras.FlatSVGIcon;

/**
 * @author R机械223李明阳
 * 注册页面
 */
public class register extends JFrame {
    private JPanel contentPanel;

    private JTextField userName;
    private JTextField password;
    private JTextField passwordCheck;

    private JLabel label1;
    private JLabel titleLable;

    private JComboBox identityChoose;
    private JRadioButton user;
    private JRadioButton admin;
    private tfFocusListener focusInputBox;

    private JButton confirmBvtn;
    private JButton returnBvtn;

    private Connection con;
    private PreparedStatement pstmt;

    public register() {
        setTitle("用户注册");
        setSize(450, 300);
        this.setLocationRelativeTo(null);
        init();
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    private void init(){

        //添加面板容器
        contentPanel = new JPanel();
        contentPanel.setBackground(Color.decode(Main.mainbgColor));
        getContentPane().add(contentPanel);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPanel);
        contentPanel.setLayout(null);

        // 设置窗口图标
        FlatSVGIcon icon = new FlatSVGIcon("miRegister.svg", 16, 16);
        setIconImage(icon.getImage());

        //页面标题
        titleLable = new JLabel();
        titleLable.setText("创建一个账号");
        titleLable.setBounds(145,20,300,30);
        titleLable.setForeground(Color.decode(Main.textColor));
        titleLable.setFont(new Font("华文行楷", Font.PLAIN, 25));
        contentPanel.add(titleLable);

        //添加用户名、密码、确认密码标签
        label1 = new JLabel("已经有账号了？去");
        label1.setBounds(150,221,120,23);
        label1.setForeground(Color.decode(Main.hintTextColor));
        contentPanel.add(label1);

        //选择身份组
        user = new JRadioButton("爱好者", true);
        admin = new JRadioButton("管理员", false);
        user.setBounds(130,70,65,20);
        admin.setBounds(245,70,65,20);
        user.setForeground(Color.decode(Main.textColor));
        admin.setForeground(Color.decode(Main.textColor));
        contentPanel.add(user);
        contentPanel.add(admin);
        ButtonGroup group = new ButtonGroup();
        group.add(user);
        group.add(admin);
        /*
        identityChoose = new JComboBox();
        identityChoose.setBounds(169,70,145,25);
        identityChoose.addItem("请选择一个身份组");
        identityChoose.addItem("user");
        identityChoose.addItem("admin");
        contentPanel.add(identityChoose);
        */

        //用户名输入框
        userName = new JTextField();
        password = new JTextField();
        passwordCheck = new JTextField();
        userName = new JTextField();
        userName.setText("请输入用户名！");
        userName.setForeground(Color.decode(Main.textColor));
        userName.setBackground(Color.decode(Main.textFieldColor));
        //userName.setBorder(new EtchedBorder());
        userName.setBounds(110,110,215,25);
        focusInputBox = new tfFocusListener();
        focusInputBox.setInputBox(userName,"请输入用户名！");
        userName.addFocusListener(focusInputBox);
        contentPanel.add(userName);

        //密码输入框
        password = new JTextField();
        password.setText("请输入密码！");
        password.setForeground(Color.decode(Main.textColor));
        password.setBackground(Color.decode(Main.textFieldColor));
        //password.setBorder(new EtchedBorder());
        password.setBounds(110,150,105,25);
        focusInputBox = new tfFocusListener();
        focusInputBox.setInputBox(password,"请输入密码！");
        password.addFocusListener(focusInputBox);
        contentPanel.add(password);

        //确认密码输入框
        passwordCheck = new JTextField();
        passwordCheck.setText("确认密码！");
        passwordCheck.setForeground(Color.decode(Main.textColor));
        passwordCheck.setBackground(Color.decode(Main.textFieldColor));
        //passwordCheck.setBorder(new EtchedBorder());
        passwordCheck.setBounds(220,150,105,25);
        focusInputBox = new tfFocusListener();
        focusInputBox.setInputBox(passwordCheck,"确认密码！");
        passwordCheck.addFocusListener(focusInputBox);
        passwordCheck.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    confirmBvtn.doClick();
                }
            }
        });
        contentPanel.add(passwordCheck);

        //注册并登录按钮
        confirmBvtn = new JButton("注册");
        confirmBvtn.setBounds(175,190,80,23);
        confirmBvtn.setBackground(Color.decode(Main.buttonColor));
        confirmBvtn.setForeground(Color.white);
        //confirmBvtn.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        confirmBvtn.addActionListener(e -> {
            if(e.getSource() == confirmBvtn){
                new Thread(() -> {
                    try {
                        if (user.isSelected()) {
                            login.selectedIdentity = "user";
                        } else {
                            login.selectedIdentity = "admin";
                        }
                        con = jdbcUtil.ConnectDB("movielist", "root", "admin123");
                        String sql = "INSERT INTO users (id,username,password,user_role) VALUES (uuid(),?,?,?)";

                        pstmt = con.prepareStatement(sql);
                        pstmt.setString(1, userName.getText());
                        pstmt.setString(2, password.getText());
                        pstmt.setString(3, login.selectedIdentity);
                        if (!"请输入用户名！".equals(userName.getText()) && !"请输入密码！".equals(password.getText()) && password.getText().equals(passwordCheck.getText())) {
                            int rowsAffected = pstmt.executeUpdate();
                            if (rowsAffected > 0) {
                                dispose();
                                login.loginedUserName = userName.getText();
                                new login();
                            }
                        } else {
                            JOptionPane.showMessageDialog(register.this, "账号/密码未输入或确认密码与原密码不一致。");
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                        JOptionPane.showMessageDialog(register.this, "数据库操作失败：用户名可能已经被注册");
                    } finally {
                        jdbcUtil.result(con, pstmt, null);
                    }
                }).start();
            }
        });
        contentPanel.add(confirmBvtn);

        //返回按钮
        returnBvtn = new JButton("登录");
        returnBvtn.setBounds(220,222,80,23);
        returnBvtn.setBorderPainted(false);
        returnBvtn.setContentAreaFilled(false);
        returnBvtn.setFocusPainted(false);
        returnBvtn.setOpaque(false);
        returnBvtn.setForeground(Color.decode(Main.hintTextColor));
        returnBvtn.setFont(new Font("宋体", Font.BOLD, 12));
        returnBvtn.addActionListener(e -> {
            if(e.getSource() == returnBvtn){
                dispose();
                new login();
            }
        });
        returnBvtn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                returnBvtn.setForeground(Color.decode(Main.textColor));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                returnBvtn.setForeground(Color.decode(Main.hintTextColor));
            }
        });
        contentPanel.add(returnBvtn);
    }
}
