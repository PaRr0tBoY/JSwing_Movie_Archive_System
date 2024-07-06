package com.acid.dialog;

import com.acid.listener.tfFocusListener;
import com.acid.user.userArchiveTable;
import com.acid.user.Main;
import com.acid.user.login;
import com.acid.util.jdbcUtil;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author R机械223李明阳
 * 注册页面
 */
public class addUserDialog extends JDialog {
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
    private static userArchiveTable umat;

    public addUserDialog(userArchiveTable umat) {
        super();
        setTitle("管理员新建用户");
        setSize(450, 300);
        this.setLocationRelativeTo(null);
        init();
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                umat.updateTable(null);
            }
        });
    }

    public static void setUmat(userArchiveTable umat) {
        addUserDialog.umat = umat;
    }

    private void init() {

        //添加面板容器
        contentPanel = new JPanel();
        contentPanel.setBackground(Color.decode(Main.mainbgColor));
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPanel.setLayout(null);
        // 设置窗口图标
        FlatSVGIcon icon = new FlatSVGIcon("miRegister.svg", 16, 16);
        setIconImage(icon.getImage());

        //页面标题
        titleLable = new JLabel();
        titleLable.setText("新建一个账号");
        titleLable.setBounds(140, 20, 300, 30);
        titleLable.setForeground(Color.decode(Main.textColor));
        titleLable.setFont(new Font("华文行楷", Font.PLAIN, 25));
        contentPanel.add(titleLable);

        //选择身份组
        user = new JRadioButton("爱好者", true);
        admin = new JRadioButton("管理员", false);
        user.setBounds(130, 70, 65, 20);
        admin.setBounds(245, 70, 65, 20);
        user.setForeground(Color.decode(Main.hintTextColor));
        admin.setForeground(Color.decode(Main.hintTextColor));
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
        userName.setForeground(Color.decode(Main.hintTextColor));
        userName.setBackground(Color.decode(Main.textFieldColor));
        //userName.setBorder(new EtchedBorder());
        userName.setBounds(110, 110, 215, 25);
        focusInputBox = new tfFocusListener();
        focusInputBox.setInputBox(userName, "请输入用户名！");
        userName.addFocusListener(focusInputBox);
        contentPanel.add(userName);

        //密码输入框
        password = new JTextField();
        password.setText("请输入密码！");
        password.setForeground(Color.decode(Main.hintTextColor));
        password.setBackground(Color.decode(Main.textFieldColor));
        //password.setBorder(new EtchedBorder());
        password.setBounds(110, 150, 215, 25);
        focusInputBox = new tfFocusListener();
        focusInputBox.setInputBox(password, "请输入密码！");
        password.addFocusListener(focusInputBox);
        contentPanel.add(password);

        //新建按钮
        confirmBvtn = new JButton("新建");
        confirmBvtn.setBounds(175, 190, 80, 23);
        confirmBvtn.setBackground(Color.decode(Main.buttonColor));
        confirmBvtn.setForeground(Color.white);
        //confirmBvtn.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        confirmBvtn.addActionListener(e -> {
            if (e.getSource() == confirmBvtn) {
                new Thread(() -> {
                    try {
                        con = jdbcUtil.ConnectDB("movielist", "root", "admin123");
                        String sql = "INSERT INTO users (id,username,password,user_role) VALUES (uuid(),?,?,?)";

                        pstmt = con.prepareStatement(sql);
                        pstmt.setString(1, userName.getText());
                        pstmt.setString(2, password.getText());
                        pstmt.setString(3, login.selectedIdentity);
                        if (!"请输入用户名！".equals(userName.getText()) && !"请输入密码！".equals(password.getText())) {
                            int rowsAffected = pstmt.executeUpdate();
                            if (rowsAffected > 0) {
                                dispose();
                                JOptionPane.showMessageDialog(addUserDialog.this, "添加成功！");
                            }
                        } else {
                            JOptionPane.showMessageDialog(addUserDialog.this, "账号/密码未输入！");
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                        JOptionPane.showMessageDialog(addUserDialog.this, "数据库操作失败：用户名可能已经存在");
                    } finally {
                        jdbcUtil.result(con, pstmt, null);
                    }
                }).start();
            }
        });
        contentPanel.add(confirmBvtn);
        add(contentPanel);
    }
}