package com.grinner.action;

import com.grinner.util.AnnotationParser;
import com.grinner.util.NodeUtils;
import com.grinner.view.Image;
import com.grinner.view.Inspiration;
import com.intellij.ide.DataManager;
import com.intellij.ide.hierarchy.call.CallHierarchyNodeDescriptor;
import com.intellij.ide.hierarchy.call.CallerMethodsTreeStructure;
import com.intellij.ide.impl.DataManagerImpl;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMember;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.javadoc.PsiDocToken;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class SniffAction extends AnAction implements UpdateInBackground {
    
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Editor editor = FileEditorManager.getInstance(e.getProject()).getSelectedTextEditor();
        DataContext dataContext = DataManagerImpl.getInstance().getDataContext(editor.getComponent());
        PsiElement target = dataContext.getData(CommonDataKeys.PSI_ELEMENT);
        ApplicationManager.getApplication().invokeLater(() -> searchApis(e.getProject(), (PsiMember) target));
    }

    private void searchApis(Project project, PsiMember target) {
        if (project == null) {
            return;
        }

        if (target == null) {
            return;
        }

        CallerMethodsTreeStructure structure = new CallerMethodsTreeStructure(project, target, "ALL");

        CallHierarchyNodeDescriptor rootElement = (CallHierarchyNodeDescriptor)structure.getRootElement();
        List<CallHierarchyNodeDescriptor> allCallHierarchyMethods = new ArrayList<>();
        allCallHierarchyMethods.add(rootElement);
        //
        Map<PsiMethod, CallHierarchyNodeDescriptor> apiMethods = new HashMap<>();
        Map<String, CallHierarchyNodeDescriptor> distinctNodeDescriptorMap = new HashMap<>();
        for (int i = 0; i < allCallHierarchyMethods.size(); i++) {
            CallHierarchyNodeDescriptor currentNodeDescriptor = allCallHierarchyMethods.get(i);
            currentNodeDescriptor.update();
            String nodeName = currentNodeDescriptor.getHighlightedText().getText();
            //去重
            if (distinctNodeDescriptorMap.containsKey(nodeName)) {
                continue;
            }
            //校验
            boolean valid = NodeUtils.isValid(structure, currentNodeDescriptor);
            if (!valid) {
                continue;
            }

            PsiElement psiElement = currentNodeDescriptor.getEnclosingElement();
            //标重
            distinctNodeDescriptorMap.put(nodeName, currentNodeDescriptor);

            //处理后代 Todo 这里的操作很耗时，经常抛异常。目前不知道应该怎么搞个异步

            Object[] childElements = new Object[0];
            try {
                childElements = structure.getChildElements(currentNodeDescriptor);
            } catch (Exception e) {
                continue;
            }
            if (childElements == null) {
                continue;
            }
            if (childElements.length > 0) {
                for (int j = 0; j < childElements.length; j++) {
                    Object childElement = childElements[j];
                    CallHierarchyNodeDescriptor childDescriptor = (CallHierarchyNodeDescriptor) structure.createDescriptor(childElement, currentNodeDescriptor);
                    allCallHierarchyMethods.add(childDescriptor);
                }
            } else if(psiElement instanceof PsiMethod){
                apiMethods.put((PsiMethod) psiElement, currentNodeDescriptor);
            }
        }

        List<Inspiration> inspirations = new ArrayList<>();
        apiMethods.forEach((milestone, nodeDescriptor) -> {
            PsiClass containingClass = milestone.getContainingClass();

            String note = "";
            if (milestone.getDocComment() != null) {
                 note = Arrays.stream(milestone.getDocComment().getDescriptionElements())
                        .filter(docElement -> {
                            if (!(docElement instanceof PsiDocToken)) {
                                return false;
                            }
                            String text = docElement.getText();
                            if (text == null || text.trim().equals("")) {
                                return false;
                            }
                            return true;
                        })
                        .findFirst()
                        .map(doc -> doc.getText().trim()).orElse(null);
            }
            String methodReference = containingClass.getQualifiedName() + "#" + milestone.getName();
            String whisper = AnnotationParser.getMethodPath(milestone);
            if("".equals(whisper)) {
                //不是@Request方法
                return;
            }
            Inspiration inspiration = new Inspiration(project, milestone, whisper, methodReference, note);
            inspirations.add(inspiration);
        });
        String name = target.getName();
        String className = target.getContainingClass().getName();
        Image.focusOn(className +"." + name, inspirations);
    }
}
