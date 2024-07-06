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
 * 编辑电影弹窗的类实现
 */
public class editMovieDialog extends JDialog {
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
    private static movieArchiveTable mat;

    private tfFocusListener focusInputBox;

    private Connection con;
    private PreparedStatement pstmt;

    public editMovieDialog(movieArchiveTable mat, String[] rowData) {
        super();
        setTitle("编辑电影");
        setSize(400, 300);
        setLocationRelativeTo(null);
        init(rowData);
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
        editMovieDialog.mat = mat;
    }
    private void init(String[] rowData) {
        //背景面板
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
        titleLable.setText("编辑电影");
        titleLable.setBounds(140,15,100,30);
        titleLable.setForeground(Color.decode(Main.textColor));
        titleLable.setFont(new Font("宋体", Font.BOLD, 20));
        contentPanel.add(titleLable);

        //电影编辑框
        movie = new JTextField(rowData[1]);
        movie.setForeground(Color.decode(Main.hintTextColor));
        movie.setBackground(Color.decode(Main.textFieldColor));
        movie.setBounds(25,55,150,25);
        //movie.setBorder(new EtchedBorder());
        focusInputBox = new tfFocusListener();
        focusInputBox.setInputBox(movie,rowData[1]);
        movie.addFocusListener(focusInputBox);
        contentPanel.add(movie);

        director = new JTextField(rowData[2]);
        director.setForeground(Color.decode(Main.hintTextColor));
        director.setBackground(Color.decode(Main.textFieldColor));
        director.setBounds(200,55,150,25);
        //director.setBorder(new EtchedBorder());
        focusInputBox = new tfFocusListener();
        focusInputBox.setInputBox(director,rowData[2]);
        director.addFocusListener(focusInputBox);
        contentPanel.add(director);

        type = new JTextField(rowData[3]);
        type.setForeground(Color.decode(Main.hintTextColor));
        type.setBackground(Color.decode(Main.textFieldColor));
        type.setBounds(25,105,150,25);
        //type.setBorder(new EtchedBorder());
        focusInputBox = new tfFocusListener();
        focusInputBox.setInputBox(type,rowData[3]);
        type.addFocusListener(focusInputBox);
        contentPanel.add(type);

        date = new JTextField(rowData[4]);
        date.setForeground(Color.decode(Main.hintTextColor));
        date.setBackground(Color.decode(Main.textFieldColor));
        date.setBounds(200,105,150,25);
        //date.setBorder(new EtchedBorder());
        focusInputBox = new tfFocusListener();
        focusInputBox.setInputBox(date,rowData[4]);
        date.addFocusListener(focusInputBox);
        contentPanel.add(date);

        region = new JTextField(rowData[5]);
        region.setForeground(Color.decode(Main.hintTextColor));
        region.setBackground(Color.decode(Main.textFieldColor));
        region.setBounds(25,155,150,25);
        //region.setBorder(new EtchedBorder());
        focusInputBox = new tfFocusListener();
        focusInputBox.setInputBox(region,rowData[5]);
        region.addFocusListener(focusInputBox);
        contentPanel.add(region);

        rating = new JTextField(rowData[6]);
        rating.setForeground(Color.decode(Main.hintTextColor));
        rating.setBackground(Color.decode(Main.textFieldColor));
        rating.setBounds(200,155,150,25);
        //rating.setBorder(new EtchedBorder());
        focusInputBox = new tfFocusListener();
        focusInputBox.setInputBox(rating,rowData[6]);
        rating.addFocusListener(focusInputBox);
        contentPanel.add(rating);

        saveButton = new JButton("完成");
        saveButton.setBounds(100, 200, 80, 25);
        saveButton.setBackground(Color.decode(Main.buttonColor));
        saveButton.setForeground(Color.white);
        //saveButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        saveButton.addActionListener(e -> {
            if (e.getSource() == saveButton) {
                new Thread(() -> {
                    try {
                        int selectedRowID = mat.getSelectedRowID();
                        if (selectedRowID != -1) {
                            con = jdbcUtil.ConnectDB("movielist", "root", "admin123");
                            String sql = "UPDATE movies SET 电影 = ?, 导演 = ?, 类型 = ?, 上映日期 = ?, 地区 = ?, 豆瓣评分 = ? WHERE 编号 = ?";
                            pstmt = con.prepareStatement(sql);
                            pstmt.setString(1, movie.getText());
                            pstmt.setString(2, director.getText());
                            pstmt.setString(3, type.getText());
                            pstmt.setString(4, date.getText());
                            pstmt.setString(5, region.getText());
                            pstmt.setString(6, rating.getText());
                            pstmt.setInt(7, selectedRowID);
                            int rowsAffected = pstmt.executeUpdate();
                            if (rowsAffected > 0) {
                                JOptionPane.showMessageDialog(null, "编辑成功");
                                mat.updateTable(null);
                                dispose();
                            } else {
                                JOptionPane.showMessageDialog(null, "编辑失败，请检查填写是否正确！");
                            }
                        }
                    } catch (SQLException e5) {
                        e5.printStackTrace();
                        JOptionPane.showMessageDialog(null, "数据库操作失败：" + e5.getMessage());
                    } finally {
                        jdbcUtil.result(con, pstmt, null);
                    }
                }).start();
            }
        });
        contentPanel.add(saveButton);

        cancelButton = new JButton("取消");
        cancelButton.setBounds(200, 200, 80, 25);
        cancelButton.setBackground(Color.decode(Main.buttonColor));
        cancelButton.setForeground(Color.white);
        //cancelButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        cancelButton.addActionListener(e -> dispose());
        contentPanel.add(cancelButton);
    }
}