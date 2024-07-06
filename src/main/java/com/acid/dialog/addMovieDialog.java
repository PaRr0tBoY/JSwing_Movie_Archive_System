package com.acid.dialog;

import com.acid.user.Main;
import com.acid.util.jdbcUtil;
import com.acid.listener.tfFocusListener;
import com.acid.movie.movieArchiveTable;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 * @author R机械223李明阳
 * 添加电影弹窗的类实现
 */
public class addMovieDialog extends JDialog {
    private JPanel contentPanel;
    private JTextField movie;
    private JTextField director;
    private JTextField type;
    private JTextField date;
    private JTextField region;
    private JTextField rating;
    private JLabel titleLable;
    private JButton saveButton;
    private JButton cancelButton;
    private tfFocusListener focusInputBox;
    private static movieArchiveTable mat;

    private Connection con;
    private PreparedStatement pstmt;

    public addMovieDialog(movieArchiveTable mat) {
        super();
        setTitle("添加电影");
        setSize(400, 300);
        setLocationRelativeTo(null);
        init();
        setVisible(true);
        // 添加一个WindowListener，当窗口关闭时触发mat.updateTable(null)
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                mat.updateTable(null);
            }
        });
    }
    public static void setMat(movieArchiveTable mat){
        addMovieDialog.mat = mat;
    }
    private void init() {
        contentPanel = new JPanel();
        contentPanel.setBackground(Color.decode(Main.mainbgColor));
        contentPanel.setLayout(null);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPanel);

        // 设置窗口图标
        FlatSVGIcon icon = new FlatSVGIcon("movie.svg", 16, 16);
        setIconImage(icon.getImage());

        //标题
        titleLable = new JLabel();
        titleLable.setText("添加电影");
        titleLable.setBounds(140,15,100,30);
        titleLable.setForeground(Color.decode(Main.textColor));
        titleLable.setFont(new Font("宋体", Font.BOLD, 20));
        contentPanel.add(titleLable);

        //电影名录入框
        movie = new JTextField("请填写电影");
        movie.setForeground(Color.decode(Main.hintTextColor));
        movie.setBackground(Color.decode(Main.textFieldColor));
        movie.setBounds(25,55,150,25);
        //movie.setBorder(new EtchedBorder());
        focusInputBox = new tfFocusListener();
        focusInputBox.setInputBox(movie,"请填写电影");
        movie.addFocusListener(focusInputBox);
        contentPanel.add(movie);

        //导演录入框
        director = new JTextField("请填写导演");
        director.setForeground(Color.decode(Main.hintTextColor));
        director.setBackground(Color.decode(Main.textFieldColor));
        director.setBounds(200,55,150,25);
        //director.setBorder(new EtchedBorder());
        focusInputBox = new tfFocusListener();
        focusInputBox.setInputBox(director,"请填写导演");
        director.addFocusListener(focusInputBox);
        contentPanel.add(director);

        //分类录入框
        type = new JTextField("请填写电影分类");
        type.setForeground(Color.decode(Main.hintTextColor));
        type.setBackground(Color.decode(Main.textFieldColor));
        type.setBounds(25,105,150,25);
        //type.setBorder(new EtchedBorder());
        focusInputBox = new tfFocusListener();
        focusInputBox.setInputBox(type,"请填写电影分类");
        type.addFocusListener(focusInputBox);
        contentPanel.add(type);

        //上映日期录入框
        date = new JTextField("请填写上映日期");
        date.setForeground(Color.decode(Main.hintTextColor));
        date.setBackground(Color.decode(Main.textFieldColor));
        date.setBounds(200,105,150,25);
        //date.setBorder(new EtchedBorder());
        focusInputBox = new tfFocusListener();
        focusInputBox.setInputBox(date,"请填写上映日期");
        date.addFocusListener(focusInputBox);
        contentPanel.add(date);

        //制片国家录入框
        region = new JTextField("请填写制片国家");
        region.setForeground(Color.decode(Main.hintTextColor));
        region.setBackground(Color.decode(Main.textFieldColor));
        region.setBounds(25,155,150,25);
        //region.setBorder(new EtchedBorder());
        focusInputBox = new tfFocusListener();
        focusInputBox.setInputBox(region,"请填写制片国家");
        region.addFocusListener(focusInputBox);
        contentPanel.add(region);

        //评分录入框
        rating = new JTextField("请填写豆瓣评分");
        rating.setForeground(Color.decode(Main.hintTextColor));
        rating.setBackground(Color.decode(Main.textFieldColor));
        rating.setBounds(200,155,150,25);
        //rating.setBorder(new EtchedBorder());
        focusInputBox = new tfFocusListener();
        focusInputBox.setInputBox(rating,"请填写豆瓣评分");
        rating.addFocusListener(focusInputBox);
        contentPanel.add(rating);

        //添加按钮
        saveButton = new JButton("添加");
        saveButton.setBounds(100, 200, 80, 25);
        saveButton.setBackground(Color.decode(Main.buttonColor));
        saveButton.setForeground(Color.white);
        //saveButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        saveButton.addActionListener(e -> {
            if (e.getSource() == saveButton) {
                new Thread(() -> {
                    try {
                        con = jdbcUtil.ConnectDB("movielist", "root", "admin123");
                        String sql = "INSERT INTO movies (电影, 导演, 类型, 上映日期, 地区, 豆瓣评分) VALUES (?, ?, ?, ?, ?, ?)";
                        pstmt = con.prepareStatement(sql);
                        pstmt.setString(1, movie.getText());
                        pstmt.setString(2, director.getText());
                        pstmt.setString(3, type.getText());
                        pstmt.setString(4, date.getText());
                        pstmt.setString(5, region.getText());
                        pstmt.setString(6, rating.getText());
                        int rowsAffected = pstmt.executeUpdate();
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(addMovieDialog.this, "添加成功!");
                            mat.updateTable(null);
                        } else {
                            JOptionPane.showMessageDialog(addMovieDialog.this, "添加失败，请检查填写是否正确！");
                        }
                    } catch (SQLException e5) {
                        e5.printStackTrace();
                        JOptionPane.showMessageDialog(addMovieDialog.this, "数据库操作失败：" + e5.getMessage());
                    } finally {
                        jdbcUtil.result(con, pstmt, null);
                    }
                }).start();
            }
        });
        contentPanel.add(saveButton);

        //取消按钮
        cancelButton = new JButton("取消");
        cancelButton.setBounds(200, 200, 80, 25);
        cancelButton.setBackground(Color.decode(Main.buttonColor));
        cancelButton.setForeground(Color.white);
        //cancelButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        cancelButton.addActionListener(e -> dispose());
        contentPanel.add(cancelButton);
    }
}