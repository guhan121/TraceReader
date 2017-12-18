package com.panda.ui.menu;

import com.panda.ui.TraceFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import static com.panda.trace.TraceFileHelper.procFile;

@SuppressWarnings("serial")
public class TraceMenuBar extends JMenuBar {
    final String VERSION = "V 1.2";
    final String HELP = "	使用方法：\r\n1.拖拽文件到界面，会自动解析trace文件；\r\n"
            + "2.双击某个线程名称，会以树形界面显示调用堆栈；\r\n"
            + "3.查找框支持对某些字符串查找；\r\n"
            + "4.右键点击线程列表支持过滤，复制；\r\n"
            + "5.右键点击mehtod，支持复制，显示时间，改名；";
    JFrame frame;
    private JMenu menu1, menu2, filterRule, menu4, menu5;
    private JMenuItem it1, it2;

    public TraceMenuBar(JFrame frame) {
        super();
        this.frame = frame;
        buildMenu();
    }

    private static void callFilterSetting() {
        Filter dialog = new Filter();
        Toolkit tk = dialog.getToolkit();
        Dimension dm = tk.getScreenSize();
        dialog.setLocation((int) (dm.getWidth() - 800) / 2,
                (int) (dm.getHeight() - 500) / 2);
        dialog.pack();
        dialog.setVisible(true);


    }

    private boolean buildMenu() {
        menu1 = new JMenu("文   件(F)");
        menu1.setMnemonic('f');
        it1 = new JMenuItem("打开");
        it2 = new JMenuItem("新建窗口");
        menu1.add(it1);
        menu1.add(it2);
        it1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                File fl = ((TraceFrame) frame).chooseFile();
                if (fl == null) {
                    return;
                }
                procFile((TraceFrame) frame, fl);
            }
        });
        menu2 = new JMenu("编辑");
        menu2.setMnemonic('E');
        filterRule = new JMenu("过滤规则");
//		JMenuItem version1=new JMenuItem("过滤规则");
        menu4 = new JMenu("查看");
        menu5 = new JMenu("帮助");
        JMenuItem help = new JMenuItem("帮助文档");
        JMenuItem version = new JMenuItem("版本");
        menu5.add(help);
        menu5.add(version);
        help.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, HELP);
            }
        });
        filterRule.addMouseListener(new MouseListener() {
                                        @Override
                                        public void mouseClicked(MouseEvent e) {
                                            System.out.println("mouseClicked");
//                                            callFilterSetting();
                                        }

                                        @Override
                                        public void mousePressed(MouseEvent e) {

                                        }

                                        @Override
                                        public void mouseReleased(MouseEvent e) {

                                        }

                                        @Override
                                        public void mouseEntered(MouseEvent e) {

                                        }

                                        @Override
                                        public void mouseExited(MouseEvent e) {

                                        }
                                    }
        );
        version.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, VERSION);
            }
        });
//        jb2 = new JButton("打开");
        this.add(menu1);
        this.add(menu2);
        this.add(filterRule);
        this.add(menu4);
        this.add(menu5);
        return true;
    }
}
