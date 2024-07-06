package com.acid.user;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.formdev.flatlaf.ui.FlatNativeMacLibrary;

import javax.swing.*;
import java.awt.*;
import java.net.CookieManager;

/**
 * @author R机械223李明阳
 * 主类，打开登录页面
 */
public class Main {
    public static String textColor = "#3760A0";//深蓝色
    public static String textFieldColor = "#E1F5FE";//浅蓝色文本框背景
    public static String buttonColor = "#FFA500";//橙色
    public static String hintTextColor = "#517DAF";//中蓝色标签提示
    public static String bgColor = "#FFFFFF";
    public static String mainbgColor = "252B3D";
    public static String addImg = "add.svg";
    public static String removeImg = "remove.svg";
    public static String editImg = "edit.svg";
    public static String dislikeImg = "dislike.svg";
    public static String likeImg = "like.svg";



    public static void setTheme(String textColor, String textFieldColor, String buttonColor, String hintTextColor, String bgColor,String mainbgColor,String addImg,String removeImg,String editImg,String dislikeImg,String likeImg){
        Main.textColor = textColor;
        Main.textFieldColor = textFieldColor;
        Main.buttonColor = buttonColor;
        Main.hintTextColor = hintTextColor;
        Main.bgColor = bgColor;
        Main.mainbgColor = mainbgColor;
        Main.addImg = addImg;
        Main.removeImg = removeImg;
        Main.editImg = editImg;
        Main.dislikeImg = dislikeImg;
        Main.likeImg = likeImg;

        //FlatMacLightLaf.setup();
        UIManager.put( "Button.arc", 999 );
        UIManager.put( "Component.arc", 999 );
        UIManager.put( "ProgressBar.arc", 999 );
        UIManager.put( "TextComponent.arc", 999 );
        UIManager.put("RootPane.background", Color.decode(bgColor)); //设置窗口颜色
        UIManager.put("RootPane.foreground", Color.decode(textColor)); //设置窗口颜色
        UIManager.put("TabbedPane.background", Color.decode(mainbgColor)); // bgcolor
        UIManager.put("TabbedPane.selectedForeground", Color.white);
        UIManager.put("TabbedPane.selectedBackground", Color.decode(buttonColor));
        UIManager.put("TabbedPane.hoverForeground", Color.white);
        UIManager.put("TabbedPane.hoverColor", Color.decode(buttonColor));
        UIManager.put("TabbedPane.underlineColor", null);
        UIManager.put("TabbedPane.foreground", Color.decode(hintTextColor));
        //UIManager.put("TabbedPane.tabAreaBackground", Color.decode(buttonColor)); // 设置标签栏区域的背景颜色
        //UIManager.put("TabbedPane.focusColor", Color.decode(buttonColor));
        //UIManager.put("TabbedPane.selected", Color.decode(buttonColor)); // 设置选中选项卡的背景颜色
        //UIManager.put("TabbedPane.contentAreaColor", Color.decode(buttonColor)); // 设置选项卡内容区域的背景颜色
        //UIManager.put("TabbedPane.borderHightlightColor", Color.decode(bgColor)); // 设置选项卡边框高亮颜色
        UIManager.put("Panel.background", Color.decode(mainbgColor)); //设置面板的背景色
        UIManager.put("SplitPane.background", Color.decode(mainbgColor));//bg
        UIManager.put("SplitPane.dividerForeground", Color.decode(buttonColor));
        UIManager.put("Table.background", Color.decode(mainbgColor));
        UIManager.put("Table.foreground", Color.decode(textColor)); //设置表格字体颜色
        UIManager.put("Table.selectionBackground", Color.decode(buttonColor)); // 设置选中单元格的背景颜色
        UIManager.put("Table.selectionForeground", Color.white); // 设置选中单元格的前景颜色
        UIManager.put("TableHeader.background", Color.decode(mainbgColor)); // 设置表头的背景颜色
        UIManager.put("TableHeader.foreground", Color.decode(textColor)); // 设置表头的前景颜色
        UIManager.put("Table.selectionInactiveBackground", Color.decode(buttonColor));
        UIManager.put("Table.selectionInactiveForeground", Color.white);

        UIManager.put("Component.focusedBorderColor", Color.decode(textColor));
        UIManager.put("Component.disabledBorderColor", Color.decode(hintTextColor)); // 设置聚焦时的边框颜色
        UIManager.put("Component.focusColor", Color.decode(hintTextColor));
        UIManager.put("Component.focusWidth", 1);

        // 设置 JComboBox 的背景颜色
        UIManager.put("ComboBox.background", Color.decode(textFieldColor));
        UIManager.put("ComboBox.foreground", Color.decode(hintTextColor));
        UIManager.put("ComboBox.buttonBackground", Color.decode(textFieldColor));
        UIManager.put("ComboBox.selectionBackground", Color.decode(buttonColor)); // 选中项的背景颜色
        UIManager.put("ComboBox.selectionForeground", Color.white); // 选中项的文本颜色

        // 设置 JComboBox 的下箭头颜色
        UIManager.put("ComboBox.buttonBackground", Color.decode(hintTextColor)); // 黄色按钮背景
        UIManager.put("ComboBox.buttonForeground", Color.white);

        //UIManager.put("Button.background", Color.decode(buttonColor));
        UIManager.put("Button.foreground", Color.decode(bgColor));

        UIManager.put("OptionPane.background", Color.decode(mainbgColor));
        UIManager.put("OptionPane.button.Color", Color.decode(buttonColor));
        UIManager.put("CheckBox.icon.checkmarkColor", Color.decode(buttonColor));
        UIManager.put("CheckBox.icon.focusColor", Color.decode(buttonColor));
        //UIManager.put("CheckBox.icon.selectedBackground", Color.decode(buttonColor));
        UIManager.put("CheckBox.icon.background", Color.decode(textFieldColor));
        UIManager.put("MenuBar.foreground", Color.decode(textColor));
        UIManager.put("MenuBar.hoverForeground",Color.decode(textColor));
        UIManager.put("MenuBar.selectionForeground",Color.decode(textColor));
        UIManager.put("MenuItem.foreground", Color.decode(hintTextColor));
        UIManager.put("MenuItem.background", Color.decode(textFieldColor));
        UIManager.put("MenuItem.selectionBackground", Color.decode(buttonColor));
        UIManager.put("ScrollPane.smoothScrolling", true);
        UIManager.put("ScrollBar.thumb", Color.white);
        UIManager.put("ScrollBar.track", Color.decode(textFieldColor));
        UIManager.put("ScrollBar.hoverThumbWithTrack", true);
        UIManager.put("ScrollBar.thumbInsets", 5);
        UIManager.put("TitlePane.background", Color.decode(Main.bgColor)); // 背景颜色
        UIManager.put("TitlePane.foreground", Color.decode(Main.textColor)); // 文本颜色
        UIManager.put("Label.foreground", Color.decode(Main.textColor)); // 所有 JLabel 的字体颜色变为蓝色
    }

    public static void main(String[] args) {
        //午夜黑主题
        //setTheme("#F05937","#fccec2","#e25232","#fb7656","#2E364F","#252B3D","add.svg","remove.svg","edit.svg","dislike.svg","like.svg");
        //小米色主题
        //setTheme("#4e4fc9","#c9caf1","#494ed3","#8186e1","#F3DFC8","#FEEEDA","miAdd.svg","miRemove.svg","miEdit.svg","miDislike.svg","miLike.svg");
        //moodyBlue主题
        //setTheme("#FCF0D8","#c9cbf2","#a5aae9","#979797","#4950d1","#646BD9","blueAdd.svg","blueRemove.svg","blueEdit.svg","blueDislike.svg","blueLike.svg");
        //可可主题
        //setTheme("#317da0","#8fd7e5","#265e7e","#3fa1c5","#FFF5C7","#F3F0E2","cocoAdd.svg","cocoRemove.svg","cocoEdit.svg","cocoDislike.svg","cocoLike.svg");
        //arc主题
        setTheme("#a0bfdd","#49587A","#6585a6","#7d93a9","#30405B","#3B4A64","arcAdd.svg","arcRemove.svg","arcEdit.svg","arcDislike.svg","arcLike.svg");
        FlatMacLightLaf.setup();
        new login();
        //new movieArchiveSystem();
    }
}