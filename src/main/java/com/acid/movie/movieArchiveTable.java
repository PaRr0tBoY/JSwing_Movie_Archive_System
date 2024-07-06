package com.acid.movie;

import com.acid.dialog.editMovieDialog;
import com.acid.user.Main;
import com.acid.util.jdbcUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.sql.*;

/**
 * @author R机械223李明阳
 * 将数据库导入到可视化表格，另有清空表格、更新表格等方法
 */
public class movieArchiveTable extends JPanel {
    String [] columnNames;
    String [][] rawData;
    int count;

    private final JTable table;
    private final JScrollPane scrollPane;
    private ResultSetMetaData metaData;
    private final DefaultTableModel model;
    private Connection con = null;
    private Statement sql;
    private ResultSet rs;
    private PreparedStatement pstmt;
    private JPanel imgPanel;

    private Image temp1;
    private ImageIcon icon;
    private JLabel imgLable;
    private final JLabel hintLable;
    private final JLabel countLable;

    public movieArchiveTable() {
        setTable();
        /*
        imgPanel = new JPanel();
        imgPanel.setBounds(0,500,800,260);

        icon = new ImageIcon("img/bannermovie2.png");
        temp1 = icon.getImage().getScaledInstance(imgPanel.getWidth(), imgPanel.getHeight(), icon.getImage().SCALE_DEFAULT);
        icon = new ImageIcon(temp1);
        imgLable = new JLabel(icon);
        imgPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));

        imgPanel.add(imgLable);
        add(imgPanel);
        */
        hintLable = new JLabel("<html><strong>没有查询到任何结果<strong><html>");
        hintLable.setBounds(290,588,160,50);
        hintLable.setForeground(Color.white);
        hintLable.setFont(new Font("楷体", Font.BOLD, 14));
        hintLable.setVisible(false);

        countLable = new JLabel("本次共查询到28个结果");
        countLable.setBounds(290,588,160,50);
        countLable.setForeground(Color.white);
        countLable.setFont(new Font("楷体", Font.BOLD, 14));
        countLable.setVisible(false);


        //model = new DefaultTableModel(rawData, columnNames);
        model = new DefaultTableModel(rawData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 设置表格不可编辑
            }
        };
        table = new JTable(model);
        // 禁用表格的列移动
        table.getTableHeader().setReorderingAllowed(false);
        // 禁用列的排序功能
        //table.setRowSorter(null);
        // 移除表格的选择模型，禁止选择
        //table.setRowSelectionAllowed(false);
        //table.setColumnSelectionAllowed(false);
        //table.setCellSelectionEnabled(false);

        this.setLayout(null);
        scrollPane = new JScrollPane(table);
        //scrollPane.setViewportView(table);
        scrollPane.setBounds(0,0,800,600);
        add(scrollPane);
        add(hintLable);
        setBackground(Color.decode(Main.bgColor));
        // 添加双击事件监听器
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // 双击事件
                    int row = table.getSelectedRow();
                    int col = table.getSelectedColumn();
                    if (row != -1 && col != -1) {
                        String movieTitle = (String) table.getValueAt(row, 1); // 假设电影标题在第二列
                        String encodedMovieTitle = null;
                        try {
                            encodedMovieTitle = URLEncoder.encode(movieTitle, "UTF-8");
                        } catch (UnsupportedEncodingException ex) {
                            throw new RuntimeException(ex);
                        }
                        String url = "https://search.douban.com/movie/subject_search?search_text=" + encodedMovieTitle;
                        System.out.println(url);
                        try {
                            Desktop.getDesktop().browse(new URI(url));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(movieArchiveTable.this, "无法打开链接：" + url);
                        }
                    }
                }
            }
        });
    }
    //更新表方法
    public void updateTable(ResultSet resultSet) {
        try {
            model.setRowCount(0);

            con = jdbcUtil.ConnectDB("movielist","root","admin123");
            String sql = "SELECT * FROM movies";
            pstmt = con.prepareStatement(sql);
            if (resultSet == null){
                resultSet = pstmt.executeQuery();
            }
            metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            columnNames = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                columnNames[i] = metaData.getColumnName(i + 1);
            }
            while (resultSet.next()) {
                String[] row = new String[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    row[i] = resultSet.getString(i + 1);
                }
                model.addRow(row);
            }
            // 根据表格行数显示或隐藏提示标签
            if (model.getRowCount() == 0) {
                hintLable.setVisible(true);
                countLable.setVisible(false);
                //System.out.println("标签可见");
            } else {
                hintLable.setVisible(false);
                count = model.getRowCount();
                countLable.setText("<html><strong>本次共查询到<strong><html>" + count + "个结果");
                add(countLable);
                countLable.setVisible(true);
                //System.out.println("标签不可见");
            }

            model.fireTableDataChanged(); // 刷新表格视图
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            jdbcUtil.result(con, null, resultSet);
        }
    }

    public void setTable() {
        try{
            con = jdbcUtil.ConnectDB("movielist","root","admin123");
            sql = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rs = sql.executeQuery("SELECT * FROM movies");
            metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            columnNames = new String[columnCount];
            for(int i = 0; i < columnCount; i++){
                columnNames[i] = metaData.getColumnName(i+1);
            }
            rs.last();
            int rowCount = rs.getRow();
            rs.first();
            rawData = new String[rowCount][columnCount];
            for(int i = 0; i < rowCount; i++){
                for(int j = 0; j < columnCount; j++){
                    rawData[i][j] = rs.getString(j+1);
                }
                rs.next();
            }
        } catch(SQLException e3){
            e3.printStackTrace();
            JOptionPane.showMessageDialog(movieArchiveTable.this, "数据库操作失败：" + e3.getMessage());
        } finally {
            jdbcUtil.result(con, null, rs);
        }
    }
    //获取光标选中的行编号
    public int getSelectedRowID() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            return Integer.parseInt((String) table.getValueAt(selectedRow, 0)); // 假设ID在第一列
        } else {
            return -1;
        }
    }
    public void openEditDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String[] rowData = new String[table.getColumnCount()];
            for (int i = 0; i < table.getColumnCount(); i++) {
                rowData[i] = (String) table.getValueAt(selectedRow, i);
            }
            new editMovieDialog(this, rowData);
        } else {
            JOptionPane.showMessageDialog(null, "请选择电影进行编辑");
        }
    }
}


