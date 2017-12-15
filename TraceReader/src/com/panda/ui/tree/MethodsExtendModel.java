package com.panda.ui.tree;

import com.panda.trace.MethodLog;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.util.List;

public class MethodsExtendModel extends DefaultTreeModel {
    private int where = -1;
    private String name;
    private List<MethodLog> methods;

    public MethodsExtendModel(TreeNode root) {
        super(root);
        // TODO Auto-generated constructor stub
    }

    public int getWhere() {
        return where;
    }

    public void setWhere(int where) {
        this.where = where;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MethodLog> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodLog> methods) {
        this.methods = methods;
    }
}
