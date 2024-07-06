package com.acid.movie;

import com.acid.dialog.editUserDialog;
import com.acid.dialog.settingsDialog;
import com.acid.user.Main;
import com.acid.dialog.editAccountDialog;
import com.acid.user.userArchiveTable;
import com.acid.util.jdbcUtil;
import com.acid.user.login;
import com.acid.user.favourites;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author R机械223李明阳
 * 主面板类，连接侧栏和主要内容面板
 */
public class movieArchiveSystem extends JFrame {

    private JPanel contentPanel;
    private JPanel matPanel;
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

    private JLabel userRoleLable;
    private String role;

    private sideBar sb;
    private movieArchiveTable mat;
    private movieArchiveTable smat;
    private userArchiveTable umat;
    private Homepage hpg;
    private favourites psn;
    private JPanel utb;
    private editAccountDialog ead;
    private editUserDialog eud;
    private settingsDialog sd;
    public static String pwd;

    private Connection con;
    private PreparedStatement pstmt;

    public movieArchiveSystem() {
        setTitle("电影档案系统");
        setSize(1076, 698);
        this.setLocationRelativeTo(null);
        init();
        setVisible(true);
        setResizable(false);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        menuBar.setBackground(Color.decode(Main.textColor));
        setJMenuBar(menuBar);

        //首选项
        settings.addActionListener(e -> {
            if (e.getSource() == settings){
                sd = new settingsDialog(this);
                sd.setMas(this);
                sd.setVisible(true);
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
                new Thread(() -> {
                    try {
                        if (login.selectedIdentity.equals("admin")) {
                            favourites.role = "管理员";
                        } else {
                            favourites.role = "电影爱好者";
                        }
                        System.out.println(favourites.role);
                        int confirm = JOptionPane.showConfirmDialog(movieArchiveSystem.this, "警告: " + favourites.role + " " + login.loginedUserName + " 将被删除！确定执行吗？", "注销", JOptionPane.YES_NO_OPTION);
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
                    } catch (SQLException e4) {
                        e4.printStackTrace();
                        JOptionPane.showMessageDialog(movieArchiveSystem.this, "数据库操作失败：" + e4.getMessage());
                    } finally {
                        jdbcUtil.result(con, pstmt, null);
                    }
                }).start();
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
        //有关
        about.addActionListener(e -> {
            if (e.getSource() == about){
                JOptionPane.showMessageDialog(null, "Made By Acid with Heart v1.0.0 \n @2024 All right reserved.");
            }
        });
        //添加面板容器
        contentPanel = new JPanel();
        contentPanel.setBackground(Color.decode(Main.mainbgColor));
        getContentPane().add(contentPanel);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPanel);
        contentPanel.setLayout(null);

        // 设置窗口图标
        FlatSVGIcon icon = new FlatSVGIcon("movie.svg", 16, 16);
        setIconImage(icon.getImage());

        //添加侧栏和电影表
        mat = new movieArchiveTable();
        smat = new movieArchiveTable();
        umat = new userArchiveTable();
        sb = new sideBar();
        matPanel = new JPanel();
        matPanel.setVisible(true);
        matPanel.setLayout(null);
        if (login.selectedIdentity.equals("admin")){
            favourites.role = "管理员";
        }else{
            favourites.role = "电影爱好者";
        }
//        //初始化身份信息
//        userRoleLable = new JLabel("昵称: " + login.loginedUserName + " 身份: " + favourites.role);
//        userRoleLable.setBounds(290,0,300,30);
//        userRoleLable.setForeground(Color.decode(Main.textColor));
//        userRoleLable.setFont(new Font("楷体", Font.BOLD, 14));
//        matPanel.add(userRoleLable);
        matPanel.setBackground(Color.decode(Main.mainbgColor));

        //初始化面板，使得countlable加载
        mat.updateTable(sb.rs);

        //标签页连接三个页面
        jtp = new JTabbedPane();
        hpg = new Homepage();
        psn = new favourites();

        //将sb实例和mat实例连接
        sb.setControl(mat,jtp);
        sb.setUmat(umat);

        hpg.setJTP(jtp);
        jtp.add("主页",hpg);
        jtp.add("电影",matPanel);
        jtp.add("收藏",psn);

        if (login.selectedIdentity.equals("admin")) {
            utb = new JPanel();
            utb.setLayout(null);
            utb.setBackground(Color.decode(Main.mainbgColor));
            jtp.add("用户", utb);
        }

        pwd = umat.returnPwd();
        ead = new editAccountDialog(movieArchiveSystem.this);
        //编辑账号菜单项
        editAccount.addActionListener(e -> {
            if (e.getSource() == editAccount) {
                ead.setVisible(true);
            }
        });
        ead.setPage(jtp,umat,utb,sb);

        jtp.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int selectedIndex = jtp.getSelectedIndex();
                if (selectedIndex == 1) {
                    sb.showComponent();
                    mat.setBounds(0, 0, 800, 630);
                    matPanel.add(mat);
                }else if(selectedIndex == 2){
                    sb.showComponent();
                    new Thread(() -> {
                        try {
                            con = jdbcUtil.ConnectDB("movielist", "root", "admin123");
                            String sql = "SELECT m.* FROM favourites f JOIN users u ON f.user_id = u.id JOIN movies m ON f.movie_id = m.编号 WHERE u.id = ?";
                            pstmt = con.prepareStatement(sql);
                            pstmt.setString(1, login.loginedUserId);
                            ResultSet rs = pstmt.executeQuery();
                            smat.updateTable(rs);
                            smat.setBounds(0, 0, 800, 630);
                            psn.add(smat);
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        } finally {
                            jdbcUtil.result(con, pstmt, null);
                        }
                    }).start();
                }else if (selectedIndex == 3){
                    sb.classifyBox.setSelectedIndex(0);
                    sb.hideComponent();
                    try {
                        umat.updateTable(null);
                        umat.setBounds(0,0, 800, 630);
                        if (utb != null) {
                            utb.add(umat);
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    } finally {
                        jdbcUtil.result(con, pstmt, null);
                    }
                }else {
                    sb.showComponent();
                }
            }
        });
//        //配置编辑用户按钮
//        sb.eud.setPage(jtp, utb, umat);

        //添加分屏面板
        splitPane = new JSplitPane (JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(255);
        //splitPane.setBackground(Color.decode(Main.bgColor));
        /*
        splitPane.setUI(new BasicSplitPaneUI()
        {
            @Override
            public BasicSplitPaneDivider createDefaultDivider()
            {
                return new BasicSplitPaneDivider(this)
                {
                    public void setBorder(Border b) {}

                    @Override
                    public void paint(Graphics g)
                    {
                        g.setColor(Color.decode(Main.hintTextColor));
                        g.fillRect(0, 0, getSize().width, getSize().height);
                        super.paint(g);
                    }
                };
            }
        });
        */
        splitPane.setBorder(null);
        //把侧栏和标签页添加到分屏面板
        splitPane.setLeftComponent(sb);
        splitPane.setRightComponent(jtp);
        splitPane.setBounds(0, 0, 1070, 660);
//        //初始化身份信息
//        userRoleLable = new JLabel("昵称: " + login.loginedUserName + " 身份: " + personal.role);
//        userRoleLable.setBounds(800,0,300,30);
//        userRoleLable.setForeground(Color.decode(Main.textColor));
//        userRoleLable.setFont(new Font("楷体", Font.BOLD, 14));
//        contentPanel.add(userRoleLable);
        contentPanel.add(splitPane);
    }
}
