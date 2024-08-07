package com.acid.dialog;

import com.acid.movie.movieArchiveSystem;
import com.acid.movie.sideBar;
import com.acid.user.userArchiveTable;
import com.acid.user.Main;
import com.acid.util.jdbcUtil;
import com.acid.listener.tfFocusListener;
import com.acid.user.login;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
    private JRadioButton user;
    private JRadioButton admin;
    private JButton saveButton;
    private JButton cancelButton;
    private JTabbedPane jtp;
    private userArchiveTable umat;
    private JPanel utb;
    private sideBar sb;

    private tfFocusListener focusInputBox;

    private Connection con;
    private PreparedStatement pstmt;
    private String role;

    public editAccountDialog(JFrame parent) {
        super(parent, "编辑账号", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        init();
        setVisible(false);
    }
    public void setPage(JTabbedPane jtp,userArchiveTable umat,JPanel utb, sideBar sb) {
        this.jtp = jtp;
        this.utb = utb;
        this.umat = umat;
        this.sb = sb;
    }
    private void init() {
        contentPanel = new JPanel();
        contentPanel.setBackground(Color.decode(Main.mainbgColor));
        contentPanel.setLayout(null);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPanel);

        //标题
        titleLable = new JLabel();
        titleLable.setText("编辑账号");
        titleLable.setBounds(150,15,100,30);
        titleLable.setForeground(Color.decode(Main.textColor));
        titleLable.setFont(new Font("华文行楷", Font.BOLD, 20));
        contentPanel.add(titleLable);

        JLabel label1 = new JLabel("用户:");
        label1.setBounds(80, 60, 80, 25);
        label1.setForeground(Color.decode(Main.textColor));
        contentPanel.add(label1);
        
        UserName = new JTextField(login.loginedUserName);
        UserName.setForeground(Color.GRAY);
        UserName.setBackground(Color.decode(Main.textFieldColor));
        UserName.setBounds(120, 60, 180, 23);
        //UserName.setBorder(new EtchedBorder());
        focusInputBox = new tfFocusListener();
        focusInputBox.setInputBox(UserName,login.loginedUserName);
        UserName.addFocusListener(focusInputBox);
        contentPanel.add(UserName);

        JLabel label2 = new JLabel("密码:");
        label2.setBounds(80, 110, 80, 25);
        label2.setForeground(Color.decode(Main.textColor));
        contentPanel.add(label2);

        Password = new JTextField(movieArchiveSystem.pwd);
        Password.setForeground(Color.GRAY);
        Password.setBackground(Color.decode(Main.textFieldColor));
        Password.setBounds(120, 110, 180, 23);
        focusInputBox = new tfFocusListener();
        focusInputBox.setInputBox(Password,movieArchiveSystem.pwd);
        Password.addFocusListener(focusInputBox);
        contentPanel.add(Password);


        JLabel label3 = new JLabel("身份:");
        label3.setBounds(80, 160, 80, 25);
        label3.setForeground(Color.decode(Main.textColor));
        contentPanel.add(label3);

        user = new JRadioButton("爱好者", true);
        admin = new JRadioButton("管理员", false);
        user.setBounds(120,160,65,20);
        admin.setBounds(240,160,65,20);
        user.setForeground(Color.decode(Main.hintTextColor));
        admin.setForeground(Color.decode(Main.hintTextColor));
        contentPanel.add(user);
        contentPanel.add(admin);
        ButtonGroup group = new ButtonGroup();
        group.add(user);
        group.add(admin);

        /*
        identityChoose = new JComboBox<>();
        identityChoose.setBounds(150, 160, 200, 25);
        identityChoose.addItem("user");
        identityChoose.addItem("admin");
        contentPanel.add(identityChoose);
         */

        saveButton = new JButton("保存");
        saveButton.setBounds(80, 200, 80, 25);
        saveButton.setBackground(Color.decode(Main.buttonColor));
        saveButton.setForeground(Color.white);
        //saveButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        saveButton.addActionListener(e -> {
            if (e.getSource() == saveButton) {
                new Thread(() -> {
                    try {
                        if (login.selectedIdentity.equals("admin")) {
                            role = "管理员";
                        } else {
                            role = "电影爱好者";
                        }
                        if (user.isSelected()) {
                            login.selectedIdentity = "user";
                        } else {
                            login.selectedIdentity = "admin";
                        }
                        con = jdbcUtil.ConnectDB("movielist", "root", "admin123");
                        String sql = "UPDATE users SET username = ?,password = ?,user_role = ? WHERE username = ?";
                        pstmt = con.prepareStatement(sql);
                        pstmt.setString(1, UserName.getText());
                        pstmt.setString(2, Password.getText());
                        pstmt.setString(3, login.selectedIdentity);
                        pstmt.setString(4, login.loginedUserName);
                        int rowsAffected = pstmt.executeUpdate();
                        if (rowsAffected > 0 && !Password.getText().isEmpty() && !UserName.getText().isEmpty()) {
                            login.loginedUserName = UserName.getText();
                            if (login.selectedIdentity == "user") {
                                jtp.remove(3);
                            } else if (login.selectedIdentity == "admin" && jtp.getTabCount() != 4) {
                                utb = new JPanel();
                                utb.setLayout(null);
                                utb.setBackground(Color.decode(Main.mainbgColor));
                                umat = new userArchiveTable();
                                umat.updateTable(null);
                                umat.setBounds(0, 0, 800, 630);
                                jtp.add("用户", utb);
                                utb.add(umat);
                                sb.eud.setPage(jtp, utb, this.umat);
                                sb.setUmat(umat);
                            }
                            JOptionPane.showMessageDialog(editAccountDialog.this, "账号修改已保存");
                        } else {
                            JOptionPane.showMessageDialog(editAccountDialog.this, "未找到指定的账号或密码为空");
                        }
                        dispose();
                    } catch (SQLException e5) {
                        e5.printStackTrace();
                        JOptionPane.showMessageDialog(editAccountDialog.this, "数据库操作失败：用户名可能已经被注册");
                    } finally {
                        jdbcUtil.result(con, pstmt, null);
                    }
                }).start();
            }
        });
        contentPanel.add(saveButton);

        cancelButton = new JButton("取消");
        cancelButton.setBounds(220, 200, 80, 25);
        cancelButton.setBackground(Color.decode(Main.buttonColor));
        cancelButton.setForeground(Color.white);
        //cancelButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        cancelButton.addActionListener(e -> dispose());
        contentPanel.add(cancelButton);
    }
}