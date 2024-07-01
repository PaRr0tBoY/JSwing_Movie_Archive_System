package com.acid.dialog;

import com.acid.jdbcUtil.jdbcUtil;
import com.acid.listener.BvtnFocusListener;
import com.acid.listener.tfFocusListener;
import com.acid.panel.contentPanel;
import com.acid.login.login;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.SoftBevelBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author R机械223李明阳
 * 编辑账号信息弹窗的类实现
 */

public class editAccountDialog extends JDialog {
    private JPanel contentPanel;
    private JTextField UserName;
    private JTextField Password;
    private JLabel titleLable;
    private JComboBox<String> identityChoose;
    private JButton saveButton;
    private JButton cancelButton;

    tfFocusListener focusInputBox;
    BvtnFocusListener focusBvtn;

    Connection con;
    PreparedStatement pstmt;

    public editAccountDialog(JFrame parent) {
        super(parent, "编辑账号", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        init();
        setVisible(true);
    }

    private void init() {
        contentPanel = new contentPanel("img/bkgd.jpg");
        contentPanel.setLayout(null);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPanel);

        //标题
        titleLable = new JLabel();
        titleLable.setText("<html><u>编辑账号</u></html>");
        titleLable.setBounds(140,15,100,30);
        titleLable.setForeground(Color.decode("#517DAF"));
        titleLable.setFont(new Font("宋体", Font.BOLD, 20));
        contentPanel.add(titleLable);

        JLabel label1 = new JLabel("用户:");
        label1.setBounds(120, 60, 80, 25);
        contentPanel.add(label1);
        
        UserName = new JTextField(login.loginedUserName);
        UserName.setForeground(Color.GRAY);
        UserName.setBounds(150, 60, 150, 23);
        UserName.setBorder(new EtchedBorder());
        focusInputBox = new tfFocusListener();
        focusInputBox.setInputBox(UserName,login.loginedUserName);
        UserName.addFocusListener(focusInputBox);
        contentPanel.add(UserName);

        JLabel label2 = new JLabel("密码:");
        label2.setBounds(120, 110, 80, 25);
        contentPanel.add(label2);

        Password = new JTextField("123");
        Password.setForeground(Color.GRAY);
        Password.setBounds(150, 110, 150, 23);
        Password.setBorder(new EtchedBorder());
        focusInputBox = new tfFocusListener();
        focusInputBox.setInputBox(Password,"123");
        Password.addFocusListener(focusInputBox);
        contentPanel.add(Password);

        JLabel label3 = new JLabel("身份组:");
        label3.setBounds(100, 160, 80, 25);
        contentPanel.add(label3);

        identityChoose = new JComboBox<>();
        identityChoose.setBounds(150, 160, 200, 25);
        identityChoose.addItem("user");
        identityChoose.addItem("admin");
        contentPanel.add(identityChoose);

        saveButton = new JButton("保存");
        saveButton.setBounds(150, 200, 80, 25);
        saveButton.setBackground(Color.decode("#517DAF"));
        saveButton.setForeground(Color.white);
        saveButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        focusBvtn = new BvtnFocusListener();
        focusBvtn.setBvtn(saveButton);
        saveButton.addFocusListener(focusBvtn);
        saveButton.addActionListener(e -> {
            if (e.getSource() == saveButton) {
                try {
                    con = jdbcUtil.ConnectDB("movielist", "root", "admin123");
                    String sql = "UPDATE users SET username = ?,password = ?,user_role = ? WHERE username = ?";
                    pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, UserName.getText());
                    pstmt.setString(2, Password.getText());
                    pstmt.setString(3, identityChoose.getSelectedItem().toString());
                    pstmt.setString(4, login.loginedUserName);
                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0 && !Password.getText().isEmpty() && !UserName.getText().isEmpty()) {
                        login.loginedUserName = UserName.getText();
                        login.loginedUserRole = identityChoose.getSelectedItem().toString();
                        JOptionPane.showMessageDialog(editAccountDialog.this, "账号修改已保存");
                    }else{
                        JOptionPane.showMessageDialog(editAccountDialog.this, "未找到指定的账号或密码为空");
                    }
                    dispose();
                } catch (SQLException e5){
                    e5.printStackTrace();
                    JOptionPane.showMessageDialog(editAccountDialog.this, "数据库操作失败：" + e5.getMessage());
                } finally {
                    jdbcUtil.result(con,pstmt,null);
                }
            }
        });
        contentPanel.add(saveButton);

        cancelButton = new JButton("取消");
        cancelButton.setBounds(270, 200, 80, 25);
        cancelButton.setBackground(Color.decode("#517DAF"));
        cancelButton.setForeground(Color.white);
        cancelButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        focusBvtn = new BvtnFocusListener();
        focusBvtn.setBvtn(cancelButton);
        cancelButton.addFocusListener(focusBvtn);
        cancelButton.addActionListener(_ -> dispose());
        contentPanel.add(cancelButton);
    }
}