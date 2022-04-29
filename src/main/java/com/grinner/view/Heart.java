package com.grinner.view;

import com.intellij.openapi.util.NlsSafe;
import com.intellij.ui.treeStructure.CachingSimpleNode;
import com.intellij.ui.treeStructure.SimpleNode;
import com.intellij.ui.treeStructure.SimpleTree;
import com.intellij.util.PsiNavigateUtil;

import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * 无失本心，你的坚定内心，对找回奶酪的渴望
 */
public class Heart extends CachingSimpleNode {
    //勿忘我名
    private String name;
    private Inspiration[] inspirations;

    protected Heart(SimpleNode aParent) {
        super(aParent);
    }


    @Override
    protected SimpleNode[] buildChildren() {
        return inspirations;
    }

    @Override
    public @NlsSafe String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 灵思泉涌
     * @param inspirations
     */
    public void flash(List<Inspiration> inspirations) {
        cleanUpCache();
        if (inspirations == null) {
            inspirations = new ArrayList<>();
        }
        this.inspirations = inspirations.toArray(new Inspiration[0]);
    }
}
