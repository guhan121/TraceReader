package com.panda.ui.list;

import javax.swing.AbstractListModel;

import com.panda.ui.TraceFrame;

@SuppressWarnings("serial")
public class ThreadJListModel extends AbstractListModel{
	private TraceFrame trf;
	private String filter="";
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	public ThreadJListModel(TraceFrame trf){
		this.trf=trf;
//		System.out.println(trf.getTraceThreads().threadId_List.size());
	}
	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		if(trf.getTraceThreads()==null){
			return 0;
		}
		return trf.getTraceThreads().threadId_List.size();
	}

	@Override
	public Object getElementAt(int index) {
		// TODO Auto-generated method stub
		if(trf.getTraceThreads()==null){
			return null;
		}
		String name=trf.getTraceThreads().threadId_List.get(index)+"\t"
				+trf.getTraceThreads().getThreadId_thread_map().get(trf.getTraceThreads().threadId_List.get(index)).getCurrentName()+"("+
				trf.getTraceThreads().getThreadId_thread_map().get(trf.getTraceThreads().threadId_List.get(index)).getMethodLogs().size()+")";
		//JLabel label=new JLabel(name);
		return name;
	}

}
