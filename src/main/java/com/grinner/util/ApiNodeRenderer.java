package com.grinner.util;

import com.grinner.view.Inspiration;
import com.intellij.icons.AllIcons;
import com.intellij.ide.tags.TagManager;
import com.intellij.ide.util.treeView.NodeRenderer;
import com.intellij.util.IconUtil;
import com.intellij.util.ui.tree.TreeUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class ApiNodeRenderer extends NodeRenderer {

    public ApiNodeRenderer() {
        setOpaque(false);
        setIconOpaque(false);
        setTransparentIconBackground(true);
    }

    @Override
    protected void doPaint(Graphics2D g) {
        super.doPaint(g);
        setOpaque(false);
    }
    @Override
    public void customizeCellRenderer(@NotNull JTree tree, Object value,
                                      boolean selected, boolean expanded, boolean leaf,
                                      int row, boolean hasFocus) {
        super.customizeCellRenderer(tree, value, selected, expanded, leaf, row, hasFocus);
        Object userObject = TreeUtil.getUserObject(value);
        if (userObject instanceof Inspiration) {
            Inspiration descriptor = (Inspiration)userObject;
            descriptor.getAppearance().customize(this);
            setIcon(descriptor.getIcon());
        }
    }
}
