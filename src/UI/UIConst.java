package UI;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class UIConst {

    public final  static  Boolean DEBUG = true;

    //设置软件的名字
    public final static String NAME = "班级管理系统";

    //一些颜色
    public final static Color OKBTN_COLOR = new Color(0x298CFF);

    //设置软件的主窗口
    public final static int MAIN_WOINDOWS_X = 250;
    public final static int MAIN_WOINDOWS_Y = 250;
    public final static int MAIN_WOINDOWS_WIDTH = 1250;
    public final static int MAIN_WOINDOWS_HEIGHT = 788;

    //设置登陆界面、注册
    public final static int LOGIN_WINDOWS_WIDTH = 460;
    public final static int LOGIN_WINDOWS_HEIGHT = 600;
    public final static int LOGIN_LOGO_HEIGHT = 180;
    public final static Font LOGIN_LOGO_FONT = new Font("微软雅黑",Font.BOLD,60);
    public final static Font LOGIN_TEXT_FONT = new Font("微软雅黑",Font.PLAIN,25);
    public final static String LOGIN_TEXT_HINT = "账号(学号)";
    public final static String LOGIN_PSW_HINT ="密码";
    public final static int REGISTER_WINDOWS_WIDTH =700;
    public final static int REGISTER_WINDOWS_HEIGHT = 900;
    public final static String REGISTER_SID_HINT = "学号(账号)";
    public final static String REGISTER_ID_HINT = "昵称（id）";
    public final static String REGISTER_NAME_HINT = "姓名";
    public final static String REGISTER_EMAIL_HINT = "电子邮箱";
    public final static String REGISTER_PSW_HINT = "密码";
    public final static Font LOGIN_HINT_FONT = new Font("微软雅黑",Font.PLAIN,20);
    public static final String REGISTER_ADMIN_HINT = "输入激活码成为管理员";
    public final static String ADMIN_KEY = "BAKA";

    //读取图片
    public final static ImageIcon HOMEPAGE_ENABLEICO = new ImageIcon("./img/Announcement.png");


    //ToolBar相关
    public final static int TOOLBAR_HEIGHT = 100;
    public final static int TOOLBAR_RIGHT_WIDTH =300;
    public final static Font TOOLBAR_RIGHT_ID = new Font("微软雅黑",Font.PLAIN,30);
    public final static Font TOOLBAR_NORMAL_FONT = new Font("微软雅黑",Font.BOLD,40);
    public final static Color OPAQUE_L1 = new Color(0,0,0,100);
    public final static Color OPAQUE_L2 = new Color(0,0,0,150);
    public final static Color OPAQUE_L3 = new Color(0,0,0,255);

    //HomePage相关常量
    public final static Font HOMEPAGE_TITLE_FONT = new Font("微软雅黑",Font.BOLD,50);
    public final static Font HOMEPAGE_TEXT_FONT = new Font("微软雅黑",Font.BOLD,20);
    public final static Font HOMEPAGE_LEFT_FONT = new Font("微软雅黑",Font.PLAIN,15);
    public final static int HOMEPAGE_LEFT_WIDTH = 300;
    public final static int HOMEPAGE_LEFT_BUTTON_HEIGHT = 100;

    //comPanel相关常量
    public final static int COMPAGE_LEFT_WIDTH = 250;
    public final static int COMPAGE_LEFTLABEL_HEIGHT = 60;
    public final static Font COMPAGE_LEFTLABLE_FONT = new Font("微软雅黑",Font.BOLD,20);
    public final static Font COMPAGE_LEFT_FONT = new Font("微软雅黑",Font.PLAIN,25);
    public final static ImageIcon COMPAGE_TOOLBAR_FACE = new ImageIcon("./img/smile.png");
    public final static ImageIcon COMPAGE_TOOLBAR_PICTURE = new ImageIcon("./img/folder.png");
    public final static ImageIcon COMPAGe_TOOLBAR_DRAW = new ImageIcon("./img/highlight.png");
    public final static Font COMPAGE_CHAT_FONT = new Font("微软雅黑",Font.PLAIN,17);

    //MyButton相关常量

    //PublishPanel相关常量
    public final static int PUBLISH_FRAME_WIDTH = 1000;
    public final static int PUBLISH_FRAME_HEIGHT = 750;
    public final static String PUBLISH_FRAME_TITLE = "通知";
    public final static Font PUBLISH_TITLE_FONT = new Font("微软雅黑",Font.BOLD,50);
    public final static Font PUBLISH_TEXT_FONT = new Font("微软雅黑",Font.PLAIN,20);
    public final static String PUBLISH_TITLE_HINT = "请输入标题";
    public final static String PUBLISH_TEXT_HINT = "请输入正文";

    //votePanel相关常量
    public final static int VOTE_FRAME_WIDTH = 500;
    public final static int VOTE_FRAME_HEIGHT = 670;
    public final static Font VOTE_TITLE_FONT = new Font("微软雅黑",Font.BOLD,20);
    public final static Font VOTE_OPTION_FONT = new Font("微软雅黑",Font.PLAIN,20);
    //votePage相关常量
    public final static int VOTEPAGE_FRAME_WIDTH = 550;
    public final static int VOTEPAGE_FRAME_HEIGHT = 700;
    public final static Font VOTEPAGE_TITLE_FONT = new Font("微软雅黑",Font.PLAIN,20);
    public final static Font VOTEPAGE_TEXT_FONT = new Font("微软雅黑",Font.PLAIN,17);
    public final static int VOTEPAGE_DIMENSION_WIDTH = 520;


    //comPage相关
    public final static ImageIcon SHAREPAGE_FILE = new ImageIcon("./img/文件.png");
    public final static Font SHAREPAGE_FONT = new Font("微软雅黑",Font.PLAIN,20);
    public final static Font SHAREPAGE_BOLDFONT = new Font("微软雅黑",Font.BOLD,20);
    public final static Font SHAREPAGE_MENU_FONT = new Font("微软雅黑",Font.PLAIN,17);

    //PaintFrame相关
    public final static int PAINTFRAME_WIDTH = 500;
    public final static int PAINTFRAME_HEIGHT = 500;
    public final static String PAINTFRAME_NAME = "绘图";

    //recordPanel相关
    public final static int RECORDRANEL_WIDTH = 1125;
    public final static int RECORDPANEL_HEIGHT = 625;
    public final static String RECORDPANEL_NAME = "记录";
    public final static Font RECORDPAGE_TITLE_FONT = new Font("微软雅黑",Font.BOLD,50);
    public final static Font RECORDPAGE_TEXT_FONT = new Font("微软雅黑",Font.PLAIN,20);

}
