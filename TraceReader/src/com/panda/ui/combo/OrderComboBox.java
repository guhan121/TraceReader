package com.panda.ui.combo;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JComboBox;

import com.panda.ui.TraceFrame;

public class OrderComboBox extends JComboBox implements ItemListener{
	static final String LIST[] = {"线程列表", "方法列表"};
	TraceFrame frame;
	public OrderComboBox(TraceFrame frame){
		super(LIST);
		this.frame=frame;
		this.addItemListener(this);
	}
	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		if(frame.getTraceThreads()==null){
			return;
		}
		if(this.getSelectedItem().toString().equals("线程列表")){
			// List<Map.Entry<String,TraceThread>> list = new ArrayList<Map.Entry<String,TraceThread>>(traceThreads.getThreadId_thread_map().entrySet());
			 Collections.sort(frame.getTraceThreads().threadId_List,new Comparator<String>() {
					@Override
					public int compare(String o1, String o2) {
						// TODO Auto-generated method stub
						return o1.compareTo(o2);
					}
		            
		      });
		     if(frame.getTraceThreads()!=null){
		    	 frame.updateUI();
		    	// model.
		     }
		}else if(this.getSelectedItem().toString().equals("方法列表")){
			//List<Map.Entry<String,TraceThread>> list = new ArrayList<Map.Entry<String,TraceThread>>(traceThreads.getThreadId_thread_map().entrySet());
		     Collections.sort(frame.getTraceThreads().threadId_List,new Comparator<String>() {
					@Override
					public int compare(String o1, String o2) {
						// TODO Auto-generated method stub
						return frame.getTraceThreads().getThreadId_thread_map().get(o2).getMethodLogs().size()-frame.getTraceThreads().getThreadId_thread_map().get(o1).getMethodLogs().size();
					}
		            
		      });
		     if(frame.getTraceThreads()!=null){
		    	 frame.updateUI();
		    	// model.
		     }
		}
	} 
}
