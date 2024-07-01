package com.acid.dialog;

import com.acid.jdbcUtil.jdbcUtil;
import com.acid.listener.BvtnFocusListener;
import com.acid.listener.tfFocusListener;
import com.acid.panel.contentPanel;
import com.acid.panel.movieArchiveTable;

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
    tfFocusListener focusInputBox;
    BvtnFocusListener focusBvtn;
    private static movieArchiveTable mat;

    Connection con;
    PreparedStatement pstmt;

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
        contentPanel = new contentPanel("img/bkgd.jpg");
        contentPanel.setLayout(null);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPanel);

        // 设置窗口图标
        ImageIcon icon = new ImageIcon("img/movie.png");
        this.setIconImage(icon.getImage());

        //标题
        titleLable = new JLabel();
        titleLable.setText("<html><u>留档电影</u></html>");
        titleLable.setBounds(140,15,100,30);
        titleLable.setForeground(Color.decode("#517DAF"));
        titleLable.setFont(new Font("宋体", Font.BOLD, 20));
        contentPanel.add(titleLable);

        //电影名录入框
        movie = new JTextField("请填写电影");
        movie.setForeground(Color.GRAY);
        movie.setBounds(25,55,150,25);
        movie.setBorder(new EtchedBorder());
        focusInputBox = new tfFocusListener();
        focusInputBox.setInputBox(movie,"请填写电影");
        movie.addFocusListener(focusInputBox);
        contentPanel.add(movie);

        //导演录入框
        director = new JTextField("请填写导演");
        director.setForeground(Color.GRAY);
        director.setBounds(200,55,150,25);
        director.setBorder(new EtchedBorder());
        focusInputBox = new tfFocusListener();
        focusInputBox.setInputBox(director,"请填写导演");
        director.addFocusListener(focusInputBox);
        contentPanel.add(director);

        //分类录入框
        type = new JTextField("请填写电影分类");
        type.setForeground(Color.GRAY);
        type.setBounds(25,105,150,25);
        type.setBorder(new EtchedBorder());
        focusInputBox = new tfFocusListener();
        focusInputBox.setInputBox(type,"请填写电影分类");
        type.addFocusListener(focusInputBox);
        contentPanel.add(type);

        //上映日期录入框
        date = new JTextField("请填写上映日期");
        date.setForeground(Color.GRAY);
        date.setBounds(200,105,150,25);
        date.setBorder(new EtchedBorder());
        focusInputBox = new tfFocusListener();
        focusInputBox.setInputBox(date,"请填写上映日期");
        date.addFocusListener(focusInputBox);
        contentPanel.add(date);

        //制片国家录入框
        region = new JTextField("请填写制片国家");
        region.setForeground(Color.GRAY);
        region.setBounds(25,155,150,25);
        region.setBorder(new EtchedBorder());
        focusInputBox = new tfFocusListener();
        focusInputBox.setInputBox(region,"请填写制片国家");
        region.addFocusListener(focusInputBox);
        contentPanel.add(region);

        //评分录入框
        rating = new JTextField("请填写电影豆瓣评分");
        rating.setForeground(Color.GRAY);
        rating.setBounds(200,155,150,25);
        rating.setBorder(new EtchedBorder());
        focusInputBox = new tfFocusListener();
        focusInputBox.setInputBox(rating,"请填写电影豆瓣评分");
        rating.addFocusListener(focusInputBox);
        contentPanel.add(rating);

        //添加按钮
        saveButton = new JButton("添加");
        saveButton.setBounds(100, 200, 80, 25);
        saveButton.setBackground(Color.decode("#517DAF"));
        saveButton.setForeground(Color.white);
        saveButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        focusBvtn = new BvtnFocusListener();
        focusBvtn.setBvtn(saveButton);
        saveButton.addFocusListener(focusBvtn);
        saveButton.addActionListener(e -> {
            if (e.getSource() == saveButton) {
                try {
                    con = jdbcUtil.ConnectDB("movielist","root","admin123");
                    String sql = "INSERT INTO movies (电影, 导演, 类型, 上映日期, 地区, 豆瓣评分) VALUES (?, ?, ?, ?, ?, ?)";
                    pstmt = con.prepareStatement(sql);
                    pstmt.setString(1,movie.getText());
                    pstmt.setString(2,director.getText());
                    pstmt.setString(3,type.getText());
                    pstmt.setString(4,date.getText());
                    pstmt.setString(5,region.getText());
                    pstmt.setString(6,rating.getText());
                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(addMovieDialog.this, "添加成功!");
                        mat.updateTable(null);
                    }else{
                        JOptionPane.showMessageDialog(addMovieDialog.this, "添加失败，请检查填写是否正确！");
                    }
                } catch (SQLException e5){
                    e5.printStackTrace();
                    JOptionPane.showMessageDialog(addMovieDialog.this, "数据库操作失败：" + e5.getMessage());
                } finally {
                    jdbcUtil.result(con, pstmt, null);
                }
            }
        });
        contentPanel.add(saveButton);

        //取消按钮
        cancelButton = new JButton("取消");
        cancelButton.setBounds(200, 200, 80, 25);
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