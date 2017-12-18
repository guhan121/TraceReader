package com.panda.ui.drop;

import com.panda.trace.BytesHelper;
import com.panda.trace.Trace;
import com.panda.trace.TraceThread;
import com.panda.ui.TraceFrame;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.Iterator;
import java.util.List;

import static com.panda.trace.TraceFileHelper.procFile;

public class DropTargetAdapterExt extends DropTargetAdapter {
    JFrame frame;

    public DropTargetAdapterExt(JFrame frame) {
        super();
        this.frame = frame;
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        // TODO Auto-generated method stub
        try {
            Transferable tf = dtde.getTransferable();
            if (tf.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                List lt = (List) tf.getTransferData(DataFlavor.javaFileListFlavor);
                Iterator itor = lt.iterator();
                while (itor.hasNext()) {
                    File fl = (File) itor.next();
                    procFile((TraceFrame)frame,fl);
                }
                dtde.dropComplete(true);
            } else {
                dtde.rejectDrop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
