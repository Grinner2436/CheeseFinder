package com.grinner.view;

import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * 始作俑者，把你的奶酪给藏起来了
 */
public class Boss implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(new Maze(project), "美味的奶酪", false);
        toolWindow.getContentManager().addContent(content);
        toolWindow.getContentManager().setSelectedContent(content);
        toolWindow.setAnchor(ToolWindowAnchor.BOTTOM, null);
        DumbService.getInstance(project).smartInvokeLater(() -> {
            if (toolWindow.isDisposed() || !toolWindow.isVisible()) {
                toolWindow.show(() -> {
                    Image.focusOn("不翼而飞", new ArrayList<>());
                });
            } else {
                Image.focusOn("空空如也", new ArrayList<>());
            }
        });
    }
}
