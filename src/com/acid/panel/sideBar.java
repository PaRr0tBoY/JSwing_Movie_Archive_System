package com.acid.panel;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.SoftBevelBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.acid.dialog.addMovieDialog;
import com.acid.dialog.editMovieDialog;
import com.acid.jdbcUtil.jdbcUtil;
import com.acid.listener.BvtnFocusListener;
import com.acid.listener.tfFocusListener;
import com.acid.login.login;

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
    private JComboBox classifyBox;
    private JComboBox sortBox;
    private JLabel hintLable;

    private Connection con;
    PreparedStatement pstmt;
    ResultSet rs;
    movieArchiveTable mat;

    tfFocusListener focusInputBox;
    BvtnFocusListener focusBvtn;

    private JButton plusButton;
    private JButton subButton;
    private JButton editButton;
    private JButton starButton;
    private JButton unStarButton;


    public sideBar(){
        setSize(1070, 800);
        init();
        setVisible(true);
        setLayout(null);
    }
    public void setMat(movieArchiveTable mat){
        this.mat = mat;
    }
    private void init() {

        //添加面板容器
        contentPanel = new contentPanel("img/bkgd.jpg");
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPanel.setLayout(null);
        contentPanel.setBounds(0, 0, 260, 800);
        this.add(contentPanel);

        //添加动态按钮面板dynamicButtonPanel
        dynamicButtonPanel = new dynamicButtonPanel("img/bkgd.jpg");
        dynamicButtonPanel.setLayout(new GridLayout(0,1,1,5));

        //放入滚动面板中
        scrollButton = new JScrollPane(dynamicButtonPanel);
        scrollButton.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollButton.setBounds(0,125, 260, 615);
        contentPanel.add(scrollButton);

        //空状态提示词
        hintLable = new JLabel("<html><strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请选择一个分类！<strong><html>");
        hintLable.setBounds(125,30,60,30);
        hintLable.setForeground(Color.decode("#517DAF"));
        hintLable.setFont(new Font("楷体", Font.BOLD, 16));
        dynamicButtonPanel.add(hintLable);


        //添加搜索框
        SearchTextField = new JTextField("搜一下");
        SearchTextField.setForeground(Color.GRAY);
        SearchTextField.setBorder(new EtchedBorder());
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
        searchButton = new JButton("搜索/重置");
        searchButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        searchButton.setBounds(165, 20, 80, 23);
        searchButton.setBackground(Color.decode("#517DAF"));
        searchButton.setForeground(Color.white);
        focusBvtn = new BvtnFocusListener();
        focusBvtn.setBvtn(searchButton);
        searchButton.addFocusListener(focusBvtn);

        searchButton.addActionListener(e -> {
            if (e.getSource() == searchButton) {
                try {
                    con = jdbcUtil.ConnectDB("movielist", "root", "admin123");
                    if ("搜一下".equals(SearchTextField.getText()) || SearchTextField.getText().isEmpty()) {
                        String sql = "SELECT * FROM movies";
                        pstmt = con.prepareStatement(sql);
                        //System.out.println("重置表");
                    }else{
                        String sql = "SELECT * FROM movies WHERE 电影 LIKE '%" + SearchTextField.getText() + "%' OR 导演 LIKE '%" + SearchTextField.getText() + "%' OR 类型 LIKE '%" + SearchTextField.getText() + "%' OR 上映日期 LIKE '%" + SearchTextField.getText() + "%' OR 地区 LIKE '%" + SearchTextField.getText() + "%' OR 豆瓣评分 LIKE '%" + SearchTextField.getText() + "%'";
                        pstmt = con.prepareStatement(sql);
                        //System.out.println("更新表");
                    }
                    rs = pstmt.executeQuery();
                    mat.updateTable(rs);
                    //System.out.println("正在执行搜索");
                } catch (Exception e2) {
                    e2.printStackTrace();
                    JOptionPane.showMessageDialog(null, "数据库操作失败：" + e2.getMessage());
                }
            }
        });
        contentPanel.add(searchButton);

        //分类
        classifyBox = new JComboBox();
        classifyBox.setBounds(10, 48, 150, 23);
        classifyBox.addItem("选取分类");
        classifyBox.addItem("编号");
        classifyBox.addItem("导演");
        classifyBox.addItem("类型");
        classifyBox.addItem("上映日期");
        classifyBox.addItem("地区");
        classifyBox.addItem("豆瓣评分");

        //排序
        sortBox = new JComboBox();
        sortBox.setBounds(165, 48, 80, 23);
        sortBox.addItem("升序");
        sortBox.addItem("降序");

        sortBox.addActionListener(e -> {
            if (e.getSource() == sortBox) {
                try {
                    String selectedColumn = classifyBox.getSelectedItem().toString();
                    con = jdbcUtil.ConnectDB("movielist", "root", "admin123");
                    String sql;
                    if ("升序".equals(sortBox.getSelectedItem())) {
                        sql = "SELECT * FROM movies ORDER BY " + selectedColumn +";";
                    } else {
                        sql = "SELECT * FROM movies ORDER BY " + selectedColumn + " DESC;";
                    }
                    pstmt = con.prepareStatement(sql);
                    rs = pstmt.executeQuery();
                    mat.updateTable(rs);
                }catch (SQLException e1){
                    e1.printStackTrace();
                }
            }
        });

        contentPanel.add(classifyBox);
        contentPanel.add(sortBox);

        //电影添加按钮
        plusButton = new JButton();
        plusButton = new JButton();
        plusButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        plusButton.setBackground(Color.decode("#517DAF"));
        plusButton.setForeground(Color.white);
        plusButton.setBounds(10, 81, 25, 25);
        ImageIcon icon1 = new ImageIcon("img/plus1.png");
        Image temp1 = icon1.getImage().getScaledInstance(plusButton.getWidth(), plusButton.getHeight(), icon1.getImage().SCALE_DEFAULT);
        icon1 = new ImageIcon(temp1);
        plusButton.setIcon(icon1);
        plusButton.addActionListener(e -> {
            if (e.getSource()==plusButton && "admin".equals(login.loginedUserRole)){
                addMovieDialog.setMat(mat);
                new addMovieDialog(mat);
            }else{
                JOptionPane.showMessageDialog(null, "用户" + login.loginedUserName + "不是管理员，无权添加电影！");
            }
        });
        contentPanel.add(plusButton);
        plusButton.setToolTipText("添加电影");

        //电影删除按钮
        subButton = new JButton();
        subButton = new JButton();
        subButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        subButton.setBackground(Color.decode("#517DAF"));
        subButton.setForeground(Color.white);
        subButton.setBounds(45, 81, 25, 25);
        ImageIcon icon2 = new ImageIcon("img/sub2.png");
        Image temp2 = icon2.getImage().getScaledInstance(subButton.getWidth(), subButton.getHeight(), icon2.getImage().SCALE_DEFAULT);
        icon2 = new ImageIcon(temp2);
        subButton.setIcon(icon2);
        subButton.addActionListener(e -> {
            if (e.getSource() == subButton && "admin".equals(login.loginedUserRole)) {
                try {
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
                } catch (SQLException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "数据库操作失败：" + e1.getMessage());
                }
            }else{
                JOptionPane.showMessageDialog(null, "用户" + login.loginedUserName + "不是管理员，无权删除电影！");
            }
        });
        contentPanel.add(subButton);
        subButton.setToolTipText("删除电影");


        //电影编辑按钮
        editButton = new JButton();
        editButton = new JButton();
        editButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        editButton.setBackground(Color.decode("#517DAF"));
        editButton.setBounds(80, 81, 25, 25);
        ImageIcon icon4 = new ImageIcon("img/edit.png");
        Image temp4 = icon4.getImage().getScaledInstance(editButton.getWidth(), editButton.getHeight(), icon4.getImage().SCALE_DEFAULT);
        icon4 = new ImageIcon(temp4);
        editButton.setIcon(icon4);
        editButton.addActionListener(e -> {
            if (e.getSource()==editButton && "admin".equals(login.loginedUserRole)){
                editMovieDialog.setMat(mat);
                mat.openEditDialog();
            }else{
                JOptionPane.showMessageDialog(null, "用户" + login.loginedUserName + "不是管理员，无权编辑电影！");
            }
        });
        contentPanel.add(editButton);
        editButton.setToolTipText("编辑电影");

        //取消收藏按钮
        unStarButton = new JButton();
        unStarButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        unStarButton.setBackground(Color.red);
        unStarButton.setBounds(185, 81, 25, 25);
        ImageIcon icon5 = new ImageIcon("img/stars.png");
        Image temp5 = icon5.getImage().getScaledInstance(unStarButton.getWidth(), unStarButton.getHeight(), icon5.getImage().SCALE_DEFAULT);
        icon5 = new ImageIcon(temp5);
        unStarButton.setIcon(icon5);
        unStarButton.addActionListener(e -> {
            if (e.getSource() == unStarButton){
                try{
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
                }
            }
        });
        contentPanel.add(unStarButton);
        unStarButton.setToolTipText("移出收藏夹");

        //收藏按钮
        starButton = new JButton();
        starButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        starButton.setBackground(Color.decode("#517DAF"));
        starButton.setBounds(220, 81, 25, 25);
        ImageIcon icon3 = new ImageIcon("img/stars.png");
        Image temp3 = icon3.getImage().getScaledInstance(starButton.getWidth(), starButton.getHeight(), icon3.getImage().SCALE_DEFAULT);
        icon3 = new ImageIcon(temp3);
        starButton.setIcon(icon3);
        starButton.addActionListener(e -> {
            if (e.getSource() ==starButton){
                try{
                    int selectedRowID = mat.getSelectedRowID();
                    if (selectedRowID != -1) {
                        con = jdbcUtil.ConnectDB("movielist", "root", "admin123");
                        String sql = "INSERT INTO favourites (user_id, movie_id, 收藏) VALUES (?, ?, 1);";
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
                    JOptionPane.showMessageDialog(null,"所选电影已在收藏夹中");
                }
            }
        });
        contentPanel.add(starButton);
        starButton.setToolTipText("收藏电影");

        // 为 classifyBox 添加选择项监听器
        classifyBox.addActionListener(e -> {
            if (e.getSource() == classifyBox) {
                generateDirectorButtons();
            }
        });
    }

    //动态生成按钮
    private void generateDirectorButtons() {

        // 清空原有按钮
        dynamicButtonPanel.removeAll();
        dynamicButtonPanel.revalidate();
        dynamicButtonPanel.repaint();

        //提示选取分类
        if ("选取分类".equals(classifyBox.getSelectedItem())){
            hintLable = new JLabel("<html><strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请选择一个分类！<strong><html>");
            hintLable.setBounds(125,30,60,30);
            hintLable.setForeground(Color.decode("#517DAF"));
            hintLable.setFont(new Font("楷体", Font.BOLD, 16));
            dynamicButtonPanel.add(hintLable);
        }

        String selectedCategory = classifyBox.getSelectedItem().toString();

        if ("选取分类".equals(selectedCategory)) return;

        String sql = String.format("SELECT DISTINCT %s FROM movies", selectedCategory);

        try {
            con = jdbcUtil.ConnectDB("movielist", "root", "admin123");
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            int yPosition = 0; // 按钮初始位置

            while (rs.next()) {
                String item = rs.getString(1);
                JButton button = new JButton(item);
                button.setBounds(10, yPosition, 220, 25);
                yPosition += 50; // 设置每个按钮的垂直间距
                button.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
                button.setBackground(Color.decode("#517DAF"));
                button.setForeground(Color.white);
                focusBvtn = new BvtnFocusListener();
                focusBvtn.setBvtn(button);
                button.addFocusListener(focusBvtn);

                button.addActionListener(e -> showMoviesByCategory(selectedCategory, item));

                dynamicButtonPanel.add(button);
            }

            dynamicButtonPanel.revalidate();
            dynamicButtonPanel.repaint();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(sideBar.this, "数据库操作失败：" + e.getMessage());
        }
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
        }
    }
}