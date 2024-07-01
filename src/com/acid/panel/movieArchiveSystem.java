package com.acid.panel;

import com.acid.dialog.editAccountDialog;
import com.acid.jdbcUtil.jdbcUtil;
import com.acid.login.login;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author R机械223李明阳
 * 主面板类，连接侧栏和主要内容面板
 */
public class movieArchiveSystem extends JFrame {

    private JPanel contentPanel;
    private JSplitPane splitPane;
    private JTabbedPane jtp;

    private JMenuBar menuBar;

    private JMenu accountManagementMenu;
    private JMenu aboutMenu;
    private JMenu settingMenu;

    private JMenuItem quitLogin;
    private JMenuItem deleteAccount;
    private JMenuItem editAccount;
    private JMenuItem myBlog;
    private JMenuItem about;
    private JMenuItem settings;

    public sideBar sb;
    public movieArchiveTable mat;

    private Connection con;
    private PreparedStatement pstmt;

    public movieArchiveSystem() {
        setTitle("电影档案系统");
        setSize(1085, 800);
        this.setLocationRelativeTo(null);
        init();
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void init() {
        //添加导航栏
        menuBar = new JMenuBar();
        accountManagementMenu = new JMenu("账号管理");
        settingMenu = new JMenu("设置");
        aboutMenu = new JMenu("有关");
        editAccount = new JMenuItem("编辑账号");
        quitLogin = new JMenuItem("退出登录");
        deleteAccount = new JMenuItem("删除账号");
        myBlog = new JMenuItem("我的博客");
        about = new JMenuItem("关于");
        settings = new JMenuItem("首选项");
        accountManagementMenu.add(editAccount);
        accountManagementMenu.add(quitLogin);
        accountManagementMenu.add(deleteAccount);
        aboutMenu.add(myBlog);
        aboutMenu.add(about);
        settingMenu.add(settings);
        menuBar.add(accountManagementMenu);
        menuBar.add(settingMenu);
        menuBar.add(aboutMenu);
        setJMenuBar(menuBar);

        //编辑账号菜单项
        editAccount.addActionListener(e -> {
            if (e.getSource() == editAccount) {
                new editAccountDialog(movieArchiveSystem.this);
            }
        });

        //退出账号菜单项
        quitLogin.addActionListener(e -> {
            if (e.getSource() == quitLogin) {
                int confirm = JOptionPane.showConfirmDialog(movieArchiveSystem.this, "确定要退出登录吗？", "退出登录", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    dispose();
                    new login();
                }
            }
        });

        //删除账号菜单项
        deleteAccount.addActionListener(e -> {
            if (e.getSource() == deleteAccount) {
                try {
                    int confirm = JOptionPane.showConfirmDialog(movieArchiveSystem.this, "确定要删除账号吗？", "注销", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        con = jdbcUtil.ConnectDB("movielist", "root", "admin123");
                        String sql = "DELETE FROM users WHERE username = '" + login.loginedUserName + "'";
                        pstmt = con.prepareStatement(sql);
                        int rowsAffected = pstmt.executeUpdate();
                        if (rowsAffected > 0) {
                            login.loginedUserName = null;
                            dispose();
                        }
                        dispose();
                        new login();
                    }
                }catch (SQLException e4){
                    e4.printStackTrace();
                    JOptionPane.showMessageDialog(movieArchiveSystem.this, "数据库操作失败：" + e4.getMessage());
                }
            }
        });
        myBlog.addActionListener(e -> {
            if (e.getSource() == myBlog) {
                try {
                    Desktop.getDesktop().browse(new URI("https://www.ac1d.space"));
                } catch (IOException | URISyntaxException ex) {
                    ex.printStackTrace();
                }
            }
        });
        //添加面板容器
        contentPanel = new contentPanel("img/bkgd.jpg");
        getContentPane().add(contentPanel);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPanel);
        contentPanel.setLayout(null);

        // 设置窗口图标
        ImageIcon icon = new ImageIcon("img/movie.png");
        this.setIconImage(icon.getImage());

        //添加侧栏和电影表
        sb = new sideBar();
        mat = new movieArchiveTable();
        //初始化面板，使得countlable加载
        mat.updateTable(sb.rs);
        //将sb实例和mat实例连接
        sb.setMat(mat);
        //标签页连接三个页面
        jtp = new JTabbedPane();

        jtp.add("主页",new Homepage());
        jtp.add("电影",mat);
        jtp.add("账号",new personal());
        //把侧栏和标签页添加到分屏面板
        splitPane = new JSplitPane (JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(sb);
        splitPane.setRightComponent(jtp);
        splitPane.setDividerLocation(255);
        splitPane.setBounds(0, 0, 1070, 800);
        contentPanel.add(splitPane);
    }
}
