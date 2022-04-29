package com.grinner.view;

import com.intellij.icons.AllIcons;
import com.intellij.ide.hierarchy.HierarchyNodeDescriptor;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ui.util.CompositeAppearance;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMember;
import com.intellij.psi.presentation.java.ClassPresentationUtil;
import com.intellij.ui.treeStructure.SimpleNode;
import com.intellij.ui.treeStructure.SimpleTree;
import com.intellij.usageView.UsageTreeColors;
import com.intellij.util.PsiNavigateUtil;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.InputEvent;

/**
 * 灵光乍现，一个个的可能性答案
 */
public class Inspiration extends SimpleNode {

    //里程碑，一次成功或失败的解答
    //顶层方法
    private PsiMember milestone;


    // 神秘低语，冥冥中指引你走向目的地
    //接口URL
    private String whisper;

    //路标
    //方法路径
    private String methodReference;

    //先锋行迹
    //接口代码注释
    private String note;

    public CompositeAppearance getAppearance() {
        return appearance;
    }

    private CompositeAppearance appearance;

    protected Inspiration(SimpleNode aParent) {
        super(aParent);
    }

    public Inspiration(Project project, PsiMember milestone, String whisper, String methodReference, String note) {
        super(project);

        //为了到达milestone
        this.milestone = milestone;
        // 你跟随whisper的指示
        this.whisper = whisper;
        // 来到了名为methodReference的地方
        this.methodReference = methodReference;
        // 发现了前人留下的note
        this.note = note;
        //节点文本
        this.appearance = new CompositeAppearance();
        TextAttributes mainTextAttributes = new TextAttributes(myColor, null, null, null, Font.PLAIN);
        appearance.getEnding().addText(whisper, mainTextAttributes);
        if (note != null && !note.trim().equals("")) {
            appearance.getEnding().addText("  (" + note + ")", UsageTreeColors.NUMBER_OF_USAGES_ATTRIBUTES.toTextAttributes());
        }
        //言传身教
        getTemplatePresentation().setTooltip(methodReference);

        //图标设置为方法
        this.setIcon(AllIcons.Nodes.Method);

    }

    @Override
    public SimpleNode @NotNull [] getChildren() {
        return new SimpleNode[0];
    }

    /**
     * 单击树节点，查看note方法注释
     * 俯身贴耳，轻声低语
     * @param tree
     */
    @Override
    public void handleSelection(SimpleTree tree) {
        super.handleSelection(tree);
    }

    /**
     * 双击树节点，跳转到API方法定义
     * 触动心弦，醍醐灌顶
     * @param tree
     */
    @Override
    public void handleDoubleClickOrEnter(SimpleTree tree, InputEvent inputEvent) {
        SimpleNode selectedNode = tree.getSelectedNode();
        Inspiration inspiration = (Inspiration) selectedNode;
        PsiNavigateUtil.navigate(inspiration.getMilestone());
    }

    @Override
    public @NlsSafe String getName() {
        return whisper;
    }

    public PsiMember getMilestone() {
        return milestone;
    }
}
