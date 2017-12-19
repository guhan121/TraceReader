package com.panda.ui.menu;

import com.panda.util.DisplayHelper;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.panda.util.DisplayHelper.initFilter;

public class Filter extends JDialog {
    public static final Pattern COMPILE = Pattern.compile("\\s*|\t|\r|\n");
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextArea textArea1;
    private JTextArea textArea2;

    public Filter() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isMod1 = getMod(textArea1, DisplayHelper.FILTER_EXCLUDE_LIST);
                boolean isMod2 = getMod(textArea2, DisplayHelper.FILTER_INCLUDE_LIST);
                if (isMod1 || isMod2) {
                    /**
                     * 写文件
                     */
                    //Get the file reference
                    String first = System.getProperty("user.dir") + File.separator + "filter.txt";
                    Path path = Paths.get(first);
                    File file = new File(first);
                    try {
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        FileWriter fileWriter = new FileWriter(file);
                        fileWriter.write("");
                        fileWriter.flush();
                        fileWriter.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                    //Use try-with-resource to get auto-closeable writer instance
                    try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                        DisplayHelper.FILTER_EXCLUDE_LIST.forEach(it -> {
                            try {
                                writer.write("-" + it + System.getProperty("line.separator"));
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        });
                        DisplayHelper.FILTER_INCLUDE_LIST.forEach(it -> {
                            try {
                                writer.write("+" + it + System.getProperty("line.separator"));
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        });
                        writer.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

//        System.out.println(System.getProperty("user.dir"));
//        textArea1.append(System.getProperty("user.dir"));
        initFilter();
        DisplayHelper.FILTER_INCLUDE_LIST.forEach((String it) -> {
            textArea2.append(it + System.getProperty("line.separator"));
        });
        DisplayHelper.FILTER_EXCLUDE_LIST.forEach((String it) -> {
            textArea1.append(it + System.getProperty("line.separator"));
        });
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Matcher m = COMPILE.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 判断两个List内的元素是否相同
     * <p>
     * 此方法有bug  见Food.class
     *
     * @param list1
     * @param list2
     * @return
     */
    private static boolean getDiffrent2(List<String> list1, List<String> list2) {
        return !list1.retainAll(list2);
    }

    private static boolean getMod(JTextArea textArea1, List<String> stringList) {
        boolean isMod = false;
        Element paragraph = textArea1.getDocument().getDefaultRootElement();
        int contentCount = paragraph.getElementCount();
        List<String> list1 = new ArrayList<>();
        for (int i = 0; i < contentCount; i++) {
            Element e1 = paragraph.getElement(i);
            int rangeStart = e1.getStartOffset();
            int rangeEnd = e1.getEndOffset();
            String line = null;
            try {
                line = textArea1.getText(rangeStart, rangeEnd - rangeStart);
                if (line != null && !line.isEmpty()) {
                    list1.add(replaceBlank(line));
                }
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
        if (getDiffrent2(list1, stringList)) {
            isMod = true;
            stringList.clear();
            stringList.addAll(list1);
        }
        return isMod;
    }

    public static void main(String[] args) {
        Filter dialog = new Filter();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
