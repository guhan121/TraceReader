package com.panda.main;

import com.panda.ui.TraceFrame;
import com.panda.util.DisplayHelper;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;

import static com.panda.util.DisplayHelper.initFilter;

public class Main {
    public static void initGlobalFontSetting(Font fnt)
    {
        FontUIResource fontRes = new FontUIResource(fnt);
        for(Enumeration keys = UIManager.getDefaults().keys(); keys.hasMoreElements();)
        {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if(value instanceof FontUIResource)
            {
                UIManager.put(key, fontRes);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        // set default font for global
//        int textFontSize = 14;
//        Font font = new Font("微软雅黑", Font.PLAIN, textFontSize);
//        initGlobalFontSetting(font);


        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new TraceFrame().setVisible(true);
//            }
//        });
        TraceFrame fm = new TraceFrame();
//        initFilter();

        //System.out.print(fl.length())
//		byte[] bytes=BytesHelper.toByteArray("./test.trace");
//		Trace trace=new Trace(bytes);
//		Threads srm=new Threads(trace);
//		MethodLog toplevel=TraceThread.topMethod;
//		for(MethodLog m:toplevel.child){
//			//d(m);
//			if(m.record.threadId==1){
//				System.out.println(m.FullName+" "+toplevel.child.size());
//				printChild(m);
//				//System.out.println("===========================================");
//			}
//		}
//		for (String key : srm.threads.keySet()) {
//			System.out.println(srm.threads.get(key).threadId+"||"+srm.threads.get(key).methods.size());
//			if(srm.threads.get(key).threadId==3){
//				
//				MethodLog toplevel=TraceThread.topMethod;
//				for(MethodLog m:toplevel.child){
//					//d(m);
//				}
//			}
//		}
//		srm.threads
//		for (String key : srm.threads.keySet()) {
//		  // System.out.println(srm.threads.get(key).threadId+" "+srm.threads.get(key).methods.size());
//			   int num=1;
//			   for(int i=0;i<srm.threads.get(key).methods.size();++i){
//				   if(num>0){
//					   //System.out.println("parent:"+srm.threads.get(key).methods.get(i).parent.methodName);
//					   if(srm.threads.get(key).methods.get(i).action==0)
//						   System.out.println(srm.threads.get(key).threadId+"\t"+srm.threads.get(key).methods.get(i).methodName);
//				   }
//				   num--;
//			   }
//		}
//		srm.methods.get(0).methodName;
//		System.out.println("threads="+srm.threads.size());
//		for(int i=0;i<srm.threads.size();++i){
//				System.out.println(srm.threads.get(i+"").name);
////				System.out.println(srm.methods.get(i).methodName+" "+srm.methods.get(i).record.threadClockDiff);
//		}
//		System.out.println(srm.methods.get(0).methodName+" "+srm.methods.get(0).record.threadClockDiff);
//		System.out.println(srm.methods.get(1).methodName+" "+srm.methods.get(1).record.threadClockDiff);
//		System.out.println(srm.methods.get(2).methodName+" "+srm.methods.get(2).record.threadClockDiff);
//		System.out.println(srm.methods.get(3).methodName+" "+srm.methods.get(3).record.threadClockDiff);
//		System.out.println(srm.methods.get(srm.methods.size()-1).methodName+" "+srm.methods.get(srm.methods.size()-1).record.threadClockDiff);

    }



}
