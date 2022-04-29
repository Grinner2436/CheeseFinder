package com.grinner.util;

import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.ide.util.treeView.AbstractTreeStructure;
import com.intellij.ide.util.treeView.ValidateableNode;
import org.jetbrains.annotations.NotNull;

public class NodeUtils {
    public static boolean isValid(@NotNull AbstractTreeStructure structure, Object element) {
        if (element == null) return false;
        if (element instanceof AbstractTreeNode) {
            AbstractTreeNode<?> node = (AbstractTreeNode<?>)element;
            if (null == node.getValue()) {
                return false;
            }
        }
        if (element instanceof ValidateableNode) {
            ValidateableNode node = (ValidateableNode)element;
            if (!node.isValid()) return false;
        }
        return structure.isValid(element);
    }
}
