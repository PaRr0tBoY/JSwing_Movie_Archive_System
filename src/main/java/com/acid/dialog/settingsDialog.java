package com.acid.dialog;

import com.acid.movie.movieArchiveSystem;
import com.acid.user.userArchiveTable;
import com.acid.user.Main;
import com.acid.listener.tfFocusListener;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * 编辑账号信息弹窗的类实现
 */
public class settingsDialog extends JDialog {
    private JPanel contentPanel;
    private JTextField UserName;
    private JTextField Password;
    private JLabel titleLable;
    private JComboBox<String> identityChoose;
    private JRadioButton lightTheme;
    private JRadioButton darkTheme;
    private JButton saveButton;
    private JButton cancelButton;
    private JTabbedPane jtp;
    private userArchiveTable umat;
    private tfFocusListener focusInputBox;

    private Connection con;
    private PreparedStatement pstmt;
    private String role;
    private String selectedIdentity;
    private movieArchiveSystem mas;

    public settingsDialog(JFrame parent) {
        super(parent, "切换主题", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        init();
        setVisible(false);
    }

    public void setMas(movieArchiveSystem mas) {
        this.mas = mas;
    }

    private void init() {
        contentPanel = new JPanel();
        contentPanel.setBackground(Color.decode(Main.mainbgColor));
        contentPanel.setLayout(null);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPanel);

        // 设置窗口图标
        FlatSVGIcon icon = new FlatSVGIcon("miSettings.svg", 16, 16);
        setIconImage(icon.getImage());

        lightTheme = new JRadioButton("亮色", false);
        darkTheme = new JRadioButton("暗色", false);
        lightTheme.setBounds(120, 100, 65, 20);
        darkTheme.setBounds(235, 100, 65, 20);
        lightTheme.setForeground(Color.decode(Main.textColor));
        darkTheme.setForeground(Color.decode(Main.hintTextColor));
        contentPanel.add(lightTheme);
        contentPanel.add(darkTheme);
        ButtonGroup group = new ButtonGroup();
        group.add(lightTheme);
        group.add(darkTheme);

        lightTheme.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    mas.dispose();
                    try {
                        UIManager.setLookAndFeel(new FlatMacLightLaf());
                    } catch (Exception ex) {
                        System.err.println("Failed to initialize LaF");
                    }
                    Main.setTheme("#a0bfdd","#49587A","#6585a6","#7d93a9","#30405B","#3B4A64","arcAdd.svg","arcRemove.svg","arcEdit.svg","arcDislike.svg","arcLike.svg");
                    mas = new movieArchiveSystem();
                    mas.setVisible(true);
                }
            }
        });

        darkTheme.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    mas.dispose();
                    try {
                        UIManager.setLookAndFeel(new FlatMacDarkLaf());
                    } catch (Exception ex) {
                        System.err.println("Failed to initialize LaF");
                    }
                    Main.setTheme("#F05937","#fccec2","#e25232","#fb7656","#252B3D","#2E364F","add.svg","remove.svg","edit.svg","dislike.svg","like.svg");
                    mas = new movieArchiveSystem();
                    mas.setVisible(true);
                }
            }
        });
    }
}
