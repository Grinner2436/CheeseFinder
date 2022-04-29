package com.grinner.view;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.content.tabs.PinToolwindowTabAction;
import org.jetbrains.annotations.NotNull;

/**
 * 十面埋伏的迷宫，为了不让你找到奶酪，而设置的重重障碍
 */
public class Maze extends SimpleToolWindowPanel {
    public Maze(@NotNull Project project) {
        super(true, true);
        //主工具条
        final ActionManager actionManager = ActionManager.getInstance();
        DefaultActionGroup actionGroup = (DefaultActionGroup) actionManager.getAction("CheeseFinder.BodyAbility");
        ActionToolbar actionToolbar = actionManager.createActionToolbar(ActionPlaces.TOOLWINDOW_TOOLBAR_BAR,
                actionGroup,
                true);
        actionGroup.add(actionManager.getAction(IdeActions.ACTION_EXPAND_ALL));
        actionGroup.add(actionManager.getAction(IdeActions.ACTION_COLLAPSE_ALL));
        actionGroup.add(actionManager.getAction(PinToolwindowTabAction.ACTION_NAME));
        setToolbar(actionToolbar.getComponent());
        //Todo 搜索条

        //展示树
        Glance glance = new Glance(project);
        this.setContent(glance);
        this.setAutoscrolls(true);
        actionToolbar.setTargetComponent(glance);
    }

}
