package com.mwq.map.mwing;

import javax.swing.tree.DefaultMutableTreeNode;

public class MTreeNode extends DefaultMutableTreeNode {

    private int id;
    private boolean load = false;

    public MTreeNode() {
        super();
    }

    public MTreeNode(Object userObject) {
        super(userObject, true);
        this.id = 0;
    }

    public MTreeNode(int id, Object userObject) {
        super(userObject, true);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isLoad() {
        return load;
    }

    public void setLoad(boolean load) {
        this.load = load;
    }
}
