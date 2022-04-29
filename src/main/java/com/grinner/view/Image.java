package com.grinner.view;

import com.grinner.util.ApiNodeRenderer;
import com.intellij.ide.hierarchy.HierarchyNodeRenderer;
import com.intellij.openapi.project.Project;
import com.intellij.ui.tree.AsyncTreeModel;
import com.intellij.ui.tree.StructureTreeModel;
import com.intellij.ui.treeStructure.SimpleTree;
import com.intellij.ui.treeStructure.SimpleTreeStructure;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.TreeSelectionModel;
import java.util.List;

/**
 * 胸有成竹，你通过记忆和分析，在心理构建起的解谜路线
 */
public class Image extends SimpleTree {
    /**
     * 往来动静之间，辗转虚实境界的玄门
     */
    private static Image reality;
    private Heart heart;
    private StructureTreeModel<SimpleTreeStructure> treeModel;

    public Image(@NotNull Project project) {
        //设置数据属性
        SimpleTreeStructure treeStructure = new SimpleTreeStructure() {
            @Override
            public Object getRootElement() {
                return heart;
            }
        };
        treeModel = new StructureTreeModel<>(treeStructure, null, project);
        AsyncTreeModel asyncTreeModel = new AsyncTreeModel(treeModel, project);
        this.setModel(asyncTreeModel);
        //设置UI属性
        this.setRootVisible(true);
        this.setShowsRootHandles(true);
        this.setBorder(BorderFactory.createEmptyBorder());
        this.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        this.setCellRenderer(new ApiNodeRenderer());
        this.heart = new Heart(null);
        //往来动静之间，辗转虚实境界的玄门
        reality = this;
    }

    /**
     * 灵感来源于怦然心跳
     *
     * @param name
     * @param heartBeats
     */
    public static void focusOn(String name, List<Inspiration> heartBeats) {
        if (reality == null) {
            return;
        }
        //更新数据
        reality.heart.flash(heartBeats);
        reality.heart.setName(name);
        reality.treeModel.invalidate();
    }
}
