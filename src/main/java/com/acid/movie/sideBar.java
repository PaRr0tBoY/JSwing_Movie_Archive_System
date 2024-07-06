package com.acid.movie;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import com.acid.dialog.*;
import com.acid.user.Main;
import com.acid.user.userArchiveTable;
import com.acid.util.jdbcUtil;
import com.acid.listener.tfFocusListener;
import com.acid.user.login;
import com.formdev.flatlaf.extras.FlatSVGIcon;


/**
 * @author R机械223李明阳
 * 侧栏类，属主要功能面板
 */
public class sideBar extends JPanel {
    public JTextField SearchTextField;
    private JButton searchButton;
    private JPanel contentPanel;
    private JPanel dynamicButtonPanel;
    private JScrollPane scrollButton;
    public JComboBox classifyBox;
    private JComboBox sortBox;
    private JLabel hintLable;

    private Connection con;
    private PreparedStatement pstmt;
    public ResultSet rs;
    private String sql;
    private String Usql;
    private String Msql;
    private movieArchiveTable mat;
    private userArchiveTable umat;
    private JTabbedPane jtp;
    public editUserDialog eud;
    private JPanel utb;

    private tfFocusListener focusInputBox;

    private JButton plusButton;
    private JButton subButton;
    private JButton editButton;
    private JButton starButton;
    private JButton unStarButton;

    public sideBar(){
        setSize(1070, 555);
        setBackground(Color.decode(Main.bgColor));
        init();
        setVisible(true);
        setLayout(null);
    }
    public void setControl(movieArchiveTable mat,JTabbedPane jtp){
        this.mat = mat;
        this.jtp = jtp;
    }
    public void setUmat(userArchiveTable umat){
        this.umat = umat;
    }


    public void hideComponent(){
        starButton.setVisible(false);
        unStarButton.setVisible(false);
        classifyBox.setVisible(false);
        sortBox.setVisible(false);
        scrollButton.setVisible(false);
        plusButton.setLocation(50,60);
        subButton.setLocation(115,60);
        editButton.setLocation(180,60);
        hintLable = new JLabel("昵称: " + login.loginedUserName + "身份: " + login.selectedIdentity);
        hintLable.setBounds(40,380,200,30);
        hintLable.setForeground(Color.decode(Main.hintTextColor));
        hintLable.setFont(new Font("楷体", Font.BOLD, 16));
        contentPanel.add(hintLable);
    }
    public void showComponent(){
        starButton.setVisible(true);
        unStarButton.setVisible(true);
        classifyBox.setVisible(true);
        sortBox.setVisible(true);
        scrollButton.setVisible(true);
        //hintLable.setVisible(true);
        plusButton.setLocation(10,81);
        subButton.setLocation(45,81);
        editButton.setLocation(80,81);
    }
    private void init() {

        //添加面板容器
        contentPanel = new JPanel();
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPanel.setBackground(Color.decode(Main.bgColor));
        contentPanel.setLayout(null);
        contentPanel.setBounds(0, 0, 265,663);
        this.add(contentPanel);

        JLabel easterEgg = new JLabel("Made By Acid with Heart v1.0.0 \n @2024 All right reserved.");
        easterEgg.setBounds(350,300,800,100);
        easterEgg.setForeground(Color.decode(Main.textColor));
        easterEgg.setFont(new Font("微软雅黑", Font.BOLD, 20));
        easterEgg.setVisible(true);
        this.add(easterEgg);

        //添加动态按钮面板dynamicButtonPanel
        dynamicButtonPanel = new JPanel();
        dynamicButtonPanel.setLayout(new GridLayout(0,1,1,5));
        dynamicButtonPanel.setBackground(Color.decode(Main.bgColor));

        //放入滚动面板中
        scrollButton = new JScrollPane(dynamicButtonPanel);
        scrollButton.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollButton.getVerticalScrollBar().setUnitIncrement(20);
        scrollButton.setBounds(0,125, 269, 534);
        contentPanel.add(scrollButton);

        //空状态提示词
        hintLable = new JLabel("<html><strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请选择一个分类！<strong><html>");
        hintLable.setSize(60,30);
        hintLable.setForeground(Color.decode(Main.hintTextColor));
        hintLable.setFont(new Font("楷体", Font.BOLD, 16));
        dynamicButtonPanel.add(hintLable);


        //添加搜索框
        SearchTextField = new JTextField("搜一下");
        SearchTextField.setForeground(Color.decode(Main.hintTextColor));
        SearchTextField.setBackground(Color.decode(Main.textFieldColor));
        //SearchTextField.setBorder(new EtchedBorder());
        SearchTextField.setBounds(10, 20, 150, 23);
        focusInputBox = new tfFocusListener();
        focusInputBox.setInputBox(SearchTextField,"搜一下");
        SearchTextField.addFocusListener(focusInputBox);
        SearchTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchButton.doClick();
                }
            }
        });
        contentPanel.add(SearchTextField);

        //添加搜索按钮
        searchButton = new JButton("搜索");
        //searchButton.setBorder(new LineBorder());
        searchButton.setBounds(165, 20, 80, 23);
        searchButton.setBackground(Color.decode(Main.buttonColor));
        searchButton.setForeground(Color.white);

        searchButton.addActionListener(e -> {
            if (e.getSource() == searchButton) {
                new Thread(() -> {
                    try {
                        con = jdbcUtil.ConnectDB("movielist", "root", "admin123");
                        String selectedCategory = classifyBox.getSelectedItem().toString();
                        String sql = String.format("SELECT DISTINCT %s FROM movies", selectedCategory);
                        if ("电影".equals(selectedCategory) || "导演".equals(selectedCategory) || "类型".equals(selectedCategory) || "地区".equals(selectedCategory) || "上映日期".equals(selectedCategory) || "编号".equals(selectedCategory) || "豆瓣评分".equals(selectedCategory)) {
                            String searchText = SearchTextField.getText().trim();
                            sql = String.format("SELECT * FROM movies WHERE %s LIKE ?", selectedCategory);
                            pstmt = con.prepareStatement(sql);
                            String searchTextFormatted = "%" + searchText + "%";
                            pstmt.setString(1, searchTextFormatted);
                            rs = pstmt.executeQuery();
                            mat.updateTable(rs);
                        } else if ("搜一下".equals(SearchTextField.getText()) || SearchTextField.getText().isEmpty()) {
                            sql = "SELECT * FROM movies";
                            pstmt = con.prepareStatement(sql);
                            rs = pstmt.executeQuery();
                            mat.updateTable(rs);
                            //System.out.println("重置表");
                        } else {
                            if (jtp.getSelectedIndex() == 1) {
                                Msql = "SELECT * FROM movies WHERE 电影 LIKE ? OR 导演 LIKE ? OR 类型 LIKE ? OR 上映日期 LIKE ? OR 地区 LIKE ? OR 豆瓣评分 LIKE ?";
                                pstmt = con.prepareStatement(Msql);
                                String searchText = "%" + SearchTextField.getText() + "%";
                                pstmt.setString(1, searchText);
                                pstmt.setString(2, searchText);
                                pstmt.setString(3, searchText);
                                pstmt.setString(4, searchText);
                                pstmt.setString(5, searchText);
                                pstmt.setString(6, searchText);
                                rs = pstmt.executeQuery();
                                mat.updateTable(rs);
                            } else if (jtp.getSelectedIndex() == 3) {
                                Usql = "SELECT * FROM users WHERE username LIKE ? OR user_role LIKE ?";
                                pstmt = con.prepareStatement(Usql);
                                String searchText = "%" + SearchTextField.getText() + "%";
                                pstmt.setString(1, searchText);
                                pstmt.setString(2, searchText);
                                rs = pstmt.executeQuery();
                                umat.updateTable(rs);
                            }
                            //System.out.println("更新表");
                        }
                        //System.out.println("正在执行搜索");
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        JOptionPane.showMessageDialog(null, "数据库操作失败：" + e2.getMessage());
                    } finally {
                        jdbcUtil.result(con, pstmt, rs);
                    }
                }).start();
            }
        });
        contentPanel.add(searchButton);

        //分类
        classifyBox = new JComboBox();
        classifyBox.setBounds(10, 48, 150, 23);
        classifyBox.setBackground(Color.decode(Main.textFieldColor));
        classifyBox.addItem("选取分类");
        classifyBox.addItem("编号");
        classifyBox.addItem("电影");
        classifyBox.addItem("导演");
        classifyBox.addItem("类型");
        classifyBox.addItem("上映日期");
        classifyBox.addItem("地区");
        classifyBox.addItem("豆瓣评分");

        //排序
        sortBox = new JComboBox();
        sortBox.setBounds(165, 48, 80, 23);
        sortBox.setBackground(Color.decode(Main.textFieldColor));
        sortBox.addItem("升序");
        sortBox.addItem("降序");

        sortBox.addActionListener(e -> {
            if (e.getSource() == sortBox) {
                new Thread(() -> {
                    try {
                        String selectedColumn = classifyBox.getSelectedItem().toString();
                        con = jdbcUtil.ConnectDB("movielist", "root", "admin123");
                        String sql;
                        if ("升序".equals(sortBox.getSelectedItem())) {
                            sql = "SELECT * FROM movies ORDER BY " + selectedColumn + ";";
                        } else {
                            sql = "SELECT * FROM movies ORDER BY " + selectedColumn + " DESC;";
                        }
                        pstmt = con.prepareStatement(sql);
                        rs = pstmt.executeQuery();
                        mat.updateTable(rs);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    } finally {
                        jdbcUtil.result(con, pstmt, rs);
                    }
                }).start();
            }
        });

        contentPanel.add(classifyBox);
        contentPanel.add(sortBox);

        //电影添加按钮
        plusButton = new JButton();
        plusButton.setIcon(new FlatSVGIcon(Main.addImg,25,25));
        plusButton.setBounds(10, 81, 25, 25);
        plusButton.addActionListener(e -> {
            if (e.getSource()==plusButton && "admin".equals(login.selectedIdentity)){
                if (jtp.getSelectedIndex() == 1) {
                    addMovieDialog.setMat(mat);
                    new addMovieDialog(mat);
                }else if (jtp.getSelectedIndex() == 3){
                    addUserDialog.setUmat(umat);
                    new addUserDialog(umat);
                }else{
                    JOptionPane.showMessageDialog(null, "请前往电影页或用户页执行添加操作！");
                }
            }else{
                JOptionPane.showMessageDialog(null, "用户" + login.loginedUserName + "不是管理员，无权添加电影！");
            }
        });
        contentPanel.add(plusButton);
        plusButton.setToolTipText("添加");

        //电影删除按钮
        subButton = new JButton();
        subButton.setIcon(new FlatSVGIcon(Main.removeImg,25,25));
        subButton.setBounds(45, 81, 25, 25);
        subButton.addActionListener(e -> {
            if (e.getSource() == subButton && "admin".equals(login.selectedIdentity)) {
                new Thread(() -> {
                    try {
                        if (jtp.getSelectedIndex() == 1) {
                            int selectedRowID = mat.getSelectedRowID();
                            if (selectedRowID != -1) {
                                int confirm = JOptionPane.showConfirmDialog(null, "确定要删除该电影吗？", "删除电影", JOptionPane.YES_NO_OPTION);
                                if (confirm == JOptionPane.YES_OPTION) {
                                    con = jdbcUtil.ConnectDB("movielist", "root", "admin123");
                                    String sql = "DELETE FROM movies WHERE 编号 = ?";
                                    pstmt = con.prepareStatement(sql);
                                    pstmt.setInt(1, selectedRowID);
                                    int rowsDeleted = pstmt.executeUpdate();
                                    if (rowsDeleted > 0) {
                                        JOptionPane.showMessageDialog(null, "删除成功");
                                        // 更新表格
                                        mat.updateTable(null);
                                    } else {
                                        JOptionPane.showMessageDialog(null, "删除失败，未找到该电影");
                                    }
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "请先选择要删除的电影");
                            }
                        } else if (jtp.getSelectedIndex() == 3) {
                            String selectedRowID = umat.getSelectedRowID();
                            if (selectedRowID != null) {
                                if (!login.loginedUserId.equals(selectedRowID)) {
                                    int confirm = JOptionPane.showConfirmDialog(null, "确定要删除该用户吗？", "删除用户", JOptionPane.YES_NO_OPTION);
                                    if (confirm == JOptionPane.YES_OPTION) {
                                        con = jdbcUtil.ConnectDB("movielist", "root", "admin123");
                                        String sql = "DELETE FROM users WHERE id = ?";
                                        pstmt = con.prepareStatement(sql);
                                        pstmt.setString(1, selectedRowID);
                                        int rowsDeleted = pstmt.executeUpdate();
                                        if (rowsDeleted > 0) {
                                            JOptionPane.showMessageDialog(null, "删除成功");
                                            // 更新表格
                                            umat.updateTable(null);
                                        } else {
                                            JOptionPane.showMessageDialog(null, "删除失败，未找到该用户");
                                        }
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "不允许删除处于登录状态的账号");
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "请先选择要删除的用户");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "请前往电影页或用户页执行删除操作！");
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                        JOptionPane.showMessageDialog(null, "数据库操作失败：" + e1.getMessage());
                    } finally {
                        jdbcUtil.result(con, pstmt, null);
                    }
                }).start();
            }else{
                JOptionPane.showMessageDialog(null, "用户" + login.loginedUserName + "不是管理员，无权执行删除操作！");
            }
        });
        contentPanel.add(subButton);
        subButton.setToolTipText("删除");


        //电影编辑按钮
        eud = new editUserDialog(null);
        editButton = new JButton();
        editButton.setIcon(new FlatSVGIcon(Main.editImg,25,25));
        editButton.setBounds(80, 81, 25, 25);
        editButton.addActionListener(e -> {
            String selectedRowID = umat.getSelectedRowID();
            if (e.getSource()==editButton && "admin".equals(login.selectedIdentity)){
                if (jtp.getSelectedIndex() == 1) {
                    editMovieDialog.setMat(mat);
                    mat.openEditDialog();
                }else if (jtp.getSelectedIndex() == 3){
                    if (selectedRowID != null){
                        eud.setPage(jtp, utb, this.umat);
                        eud.setVisible(true);
                    }else{
                        JOptionPane.showMessageDialog(null, "请先选择要编辑的账号！");
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "请前往电影页或用户页执行编辑操作！");
                }
            }else{
                JOptionPane.showMessageDialog(null, "用户" + login.loginedUserName + "不是管理员，无权执行编辑操作！");
            }
        });
        contentPanel.add(editButton);
        editButton.setToolTipText("编辑");

        //取消收藏按钮
        unStarButton = new JButton();
        unStarButton.setIcon(new FlatSVGIcon(Main.dislikeImg,25,25));
        unStarButton.setBounds(185, 81, 25, 25);
        unStarButton.addActionListener(e -> {
            if (e.getSource() == unStarButton) {
                new Thread(() -> {
                    if (jtp.getSelectedIndex() == 1) {
                        try {
                            int selectedRowID = mat.getSelectedRowID();
                            if (selectedRowID != -1) {
                                con = jdbcUtil.ConnectDB("movielist", "root", "admin123");
                                String sql = "DELETE FROM favourites WHERE movie_id = ? && user_id = ?";
                                pstmt = con.prepareStatement(sql);
                                pstmt.setInt(1, selectedRowID);
                                pstmt.setString(2, login.loginedUserId);
                                int rowsDeleted = pstmt.executeUpdate();

                                if (rowsDeleted > 0) {
                                    JOptionPane.showMessageDialog(null, "已将选中电影移出收藏夹");
                                    // 更新表格
                                    mat.updateTable(null);
                                } else {
                                    JOptionPane.showMessageDialog(null, "移出失败，收藏夹中未找到该电影");
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "请先选择要移出收藏夹的电影");
                            }
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                            JOptionPane.showMessageDialog(null, "数据库操作失败：" + e1.getMessage());
                        } finally {
                            jdbcUtil.result(con, pstmt, null);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "请前往电影页执行撤销收藏操作！");
                    }
                }).start();
            }
        });
        contentPanel.add(unStarButton);
        unStarButton.setToolTipText("移出收藏夹");

        //收藏按钮
        starButton = new JButton();
        starButton.setIcon(new FlatSVGIcon(Main.likeImg,25,25));
        starButton.setBounds(220, 81, 25, 25);
        starButton.addActionListener(e -> {
            if (e.getSource() ==starButton) {
                new Thread(() -> {
                    if (jtp.getSelectedIndex() == 1) {
                        try {
                            int selectedRowID = mat.getSelectedRowID();
                            if (selectedRowID != -1) {
                                con = jdbcUtil.ConnectDB("movielist", "root", "admin123");
                                String sql = "INSERT INTO favourites (user_id, movie_id, 收藏) VALUES (?, ?, 1)";
                                pstmt = con.prepareStatement(sql);
                                pstmt.setString(1, login.loginedUserId);
                                pstmt.setInt(2, selectedRowID);
                                int rowsAffected = pstmt.executeUpdate();

                                if (rowsAffected > 0) {
                                    JOptionPane.showMessageDialog(null, "已添加至收藏");
                                    // 更新表格
                                    mat.updateTable(null);
                                } else {
                                    JOptionPane.showMessageDialog(null, "收藏失败，未找到该电影");
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "请先选择要收藏的电影");
                            }
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                            JOptionPane.showMessageDialog(null, "所选电影已在收藏夹中");
                        } finally {
                            jdbcUtil.result(con, pstmt, null);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "请前往电影页执行收藏操作！");
                    }
                }).start();
            }
        });

        contentPanel.add(starButton);
        starButton.setToolTipText("收藏电影");

        // 为 classifyBox 添加选择项监听器
        classifyBox.addActionListener(e -> {
            if (e.getSource() == classifyBox
                    && !classifyBox.getSelectedItem().equals("选取分类")
                    && !classifyBox.getSelectedItem().equals("编号")
                    && !classifyBox.getSelectedItem().equals("电影")
                    && !classifyBox.getSelectedItem().equals("豆瓣评分")) {
                generateDirectorButtons();
                jtp.setSelectedIndex(1);
            }else if(classifyBox.getSelectedItem().equals("选取分类")){
                hintLableGenerate();
                SearchTextField.setText("搜一下");
                searchButton.doClick();
            }else{
                hintLableGenerate();
                jtp.setSelectedIndex(1);
            }
        });
    }

    public void hintLableGenerate(){
        //提示选取分类
        String selectedCategory = Objects.requireNonNull(classifyBox.getSelectedItem()).toString();

        if ("选取分类".equals(selectedCategory)){
            // 清空原有按钮
            dynamicButtonPanel.removeAll();
            dynamicButtonPanel.revalidate();
            dynamicButtonPanel.repaint();
            hintLable = new JLabel("<html><strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请选择一个分类<strong><html>");
            hintLable.setSize(60,30);
            hintLable.setForeground(Color.decode(Main.hintTextColor));
            hintLable.setFont(new Font("楷体", Font.BOLD, 16));
            dynamicButtonPanel.add(hintLable);
        } else if ("编号".equals(selectedCategory) || "电影".equals(selectedCategory) || "豆瓣评分".equals(selectedCategory)){
            // 清空原有按钮
            dynamicButtonPanel.removeAll();
            dynamicButtonPanel.revalidate();
            dynamicButtonPanel.repaint();
            hintLable = new JLabel("<html><strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;部分分类不支持生成按钮<strong><html>");
            hintLable.setSize(60,30);
            hintLable.setForeground(Color.decode(Main.hintTextColor));
            hintLable.setFont(new Font("楷体", Font.BOLD, 16));
            dynamicButtonPanel.add(hintLable);
        }
    }

    //动态生成按钮
    private void generateDirectorButtons() {

        // 清空原有按钮
        dynamicButtonPanel.removeAll();
        dynamicButtonPanel.revalidate();
        dynamicButtonPanel.repaint();


        String selectedCategory = classifyBox.getSelectedItem().toString();

        if ("选取分类".equals(selectedCategory)) return;

        String sql = String.format("SELECT DISTINCT %s FROM movies", selectedCategory);

        new Thread(() -> {
            try {
                con = jdbcUtil.ConnectDB("movielist", "root", "admin123");
                pstmt = con.prepareStatement(sql);
                rs = pstmt.executeQuery();

                int yPosition = 0; // 按钮初始位置

                while (rs.next() && !selectedCategory.equals("选取分类") && !selectedCategory.equals("电影") && !selectedCategory.equals("编号") && !selectedCategory.equals("豆瓣评分")) {
                    String item = rs.getString(1);
                    JButton button = new JButton(item);
                    button.setBounds(10, yPosition, 220, 25);
                    yPosition += 50; // 设置每个按钮的垂直间距
                    //button.setBorder(new LineBorder(Color.decode(Main.buttonColor)));
                    button.setBackground(Color.decode(Main.buttonColor));
                    button.setForeground(Color.white);

                    button.addActionListener(e -> showMoviesByCategory(selectedCategory, item));

                    dynamicButtonPanel.add(button);
                }

                dynamicButtonPanel.revalidate();
                dynamicButtonPanel.repaint();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(sideBar.this, "数据库操作失败：" + e.getMessage());
            } finally {
                jdbcUtil.result(con, pstmt, rs);
            }
        }).start();
    }

    //按类别显示电影
    private void showMoviesByCategory(String category, String item) {
        try {
            con = jdbcUtil.ConnectDB("movielist", "root", "admin123");
            String sql = String.format("SELECT * FROM movies WHERE %s = ?", category);
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, item);
            ResultSet rs = pstmt.executeQuery();

            if (mat != null) {
                mat.updateTable(rs); // 更新表格
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "数据库操作失败：" + e.getMessage());
        } finally {
            jdbcUtil.result(con, pstmt, rs);
        }
    }
}