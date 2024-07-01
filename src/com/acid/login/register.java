package com.acid.login;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.SoftBevelBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.acid.jdbcUtil.jdbcUtil;
import com.acid.listener.BvtnFocusListener;
import com.acid.listener.tfFocusListener;
import com.acid.panel.contentPanel;
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
    private JLabel label2;
    private JLabel titleLable;

    private JComboBox identityChoose;
    tfFocusListener focusInputBox;
    BvtnFocusListener focusBvtn;

    private JButton confirmBvtn;
    private JButton returnBvtn;

    Connection con;
    PreparedStatement pstmt;

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
        contentPanel = new contentPanel("img/bkgd.jpg");
        getContentPane().add(contentPanel);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPanel);
        contentPanel.setLayout(null);

        // 设置窗口图标
        ImageIcon icon = new ImageIcon("img/register.png");
        this.setIconImage(icon.getImage());

        //页面标题
        titleLable = new JLabel();
        titleLable.setText("<html><u>创建一个账号</u></html>");
        titleLable.setBounds(160,20,300,30);
        titleLable.setForeground(Color.decode("#517DAF"));
        titleLable.setFont(new Font("楷体", Font.BOLD, 20));
        contentPanel.add(titleLable);

        //添加用户名、密码、确认密码标签
        label1 = new JLabel("已经有账号了？去");
        label1.setBackground(Color.GRAY);
        label1.setBounds(165,221,120,23);
        contentPanel.add(label1);

        //用户身份组标签
        label2 = new JLabel("选择身份组:");
        label2.setBounds(120,66,100,28);

        //选择身份组
        identityChoose = new JComboBox();
        identityChoose.setBounds(189,70,145,25);
        identityChoose.addItem("user");
        identityChoose.addItem("admin");
        contentPanel.add(identityChoose);
        contentPanel.add(label2);

        //用户名输入框
        userName = new JTextField();
        password = new JTextField();
        passwordCheck = new JTextField();
        userName = new JTextField();
        userName.setText("请输入用户名！");
        userName.setForeground(Color.GRAY);
        userName.setBorder(new EtchedBorder());
        userName.setBounds(120,110,215,25);
        focusInputBox = new tfFocusListener();
        focusInputBox.setInputBox(userName,"请输入用户名！");
        userName.addFocusListener(focusInputBox);
        contentPanel.add(userName);

        //密码输入框
        password = new JTextField();
        password.setText("请输入密码！");
        password.setForeground(Color.GRAY);
        password.setBorder(new EtchedBorder());
        password.setBounds(120,150,106,25);
        focusInputBox = new tfFocusListener();
        focusInputBox.setInputBox(password,"请输入密码！");
        password.addFocusListener(focusInputBox);
        contentPanel.add(password);

        //确认密码输入框
        passwordCheck = new JTextField();
        passwordCheck.setText("确认密码！");
        passwordCheck.setForeground(Color.GRAY);
        passwordCheck.setBorder(new EtchedBorder());
        passwordCheck.setBounds(230,150,108,25);
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
        confirmBvtn.setBounds(185,190,80,23);
        confirmBvtn.setBackground(Color.decode("#517DAF"));
        confirmBvtn.setForeground(Color.white);
        confirmBvtn.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        focusBvtn = new BvtnFocusListener();
        focusBvtn.setBvtn(confirmBvtn);
        confirmBvtn.addFocusListener(focusBvtn);
        confirmBvtn.addActionListener(e -> {
            if(e.getSource() == confirmBvtn){
                try{
                    con = jdbcUtil.ConnectDB("movielist","root","admin123");
                    String sql = "INSERT INTO users (id,username,password,user_role) VALUES (uuid(),?,?,?)";

                    pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, userName.getText());
                    pstmt.setString(2, password.getText());
                    pstmt.setString(3, identityChoose.getSelectedItem().toString());
                    if(!"请输入用户名！".equals(userName.getText()) && !"请输入密码！".equals(password.getText()) && password.getText().equals(passwordCheck.getText())) {
                        int rowsAffected = pstmt.executeUpdate();
                        if (rowsAffected > 0) {
                            dispose();
                            login.loginedUserName = userName.getText();
                            login.loginedUserRole = identityChoose.getSelectedItem().toString();
                            new login();
                        }
                    } else{
                            JOptionPane.showMessageDialog(register.this, "账号/密码未输入或确认密码与原密码不一致。");
                        }
                } catch(SQLException e1){
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(register.this, "数据库操作失败：" + e1.getMessage());
                } finally {
                    jdbcUtil.result(con, pstmt, null);
                }
            }
        });
        contentPanel.add(confirmBvtn);

        //返回按钮
        returnBvtn = new JButton("登录");
        returnBvtn.setBounds(235,221,80,23);
        returnBvtn.setBorderPainted(false);
        returnBvtn.setContentAreaFilled(false);
        returnBvtn.setFocusPainted(false);
        returnBvtn.setOpaque(false);
        returnBvtn.setForeground(Color.darkGray);
        returnBvtn.setFont(new Font("default", Font.BOLD, 10));
        returnBvtn.addActionListener(e -> {
            if(e.getSource() == returnBvtn){
                dispose();
                new login();
            }
        });
        returnBvtn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                returnBvtn.setForeground(Color.blue);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                returnBvtn.setForeground(Color.darkGray);
            }
        });
        contentPanel.add(returnBvtn);
    }
}
