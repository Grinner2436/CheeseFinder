package com.grinner.view;

import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * 惊鸿一瞥，你看了一眼迷宫，心中记住的印象
 */
public class Glance extends JPanel {
    private Image image;

    public Glance(@NotNull Project project) {
        image = new Image(project);
        this.setLayout(new BorderLayout());
        this.add(image, BorderLayout.CENTER);
        this.setAutoscrolls(true);
    }
}
