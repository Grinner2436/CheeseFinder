package com.grinner.util;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiLiteralExpression;
import com.intellij.psi.PsiMethod;
import org.spring.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.spring.annotation.RequestMapping.REQUEST;

public class AnnotationParser {

    /**
     * 从方法获得请求路径
     * @param psiMethod
     * @return
     */
    public static String getMethodPath(PsiMethod psiMethod) {

        PsiAnnotation request = psiMethod.getAnnotation(REQUEST.getName());
        if (request == null) {
            request = psiMethod.getAnnotation(REQUEST.getQualifiedName());
        }
        if (request == null) {
            PsiAnnotation[] annotations = psiMethod.getAnnotations();
            RequestMapping[] requestMappings = RequestMapping.values();
            Optional<PsiAnnotation> first = Arrays.stream(annotations).filter(annotation -> {
                boolean hasRequestMapping = Arrays.stream(requestMappings).anyMatch(requestMapping -> {
                    return requestMapping.getQualifiedName().equals(annotation.getQualifiedName());
                });
                return hasRequestMapping;
            }).findFirst();
            if (!first.isEmpty()) {
                request = first.get();
            }
        }
        if (request == null) {
            return "";
        }
        String methodPath = getPath(request);

        PsiClass psiClass = psiMethod.getContainingClass();
        PsiAnnotation classAnnotation = getTargetAnnotation(psiClass, REQUEST.getQualifiedName());
        if (classAnnotation == null) {
            return methodPath;
        }
        String classPath = getPath(classAnnotation);
        return classPath + methodPath;
    }

    private static String getPath(PsiAnnotation annotation) {
        PsiLiteralExpression pathAttribute = (PsiLiteralExpression) annotation.findDeclaredAttributeValue("value");
        if (pathAttribute == null) {
            pathAttribute = (PsiLiteralExpression) annotation.findDeclaredAttributeValue("path");
        }
        if (pathAttribute == null) {
            return "";
        }
        return pathAttribute.getValue().toString();
    }

    private static PsiAnnotation getTargetAnnotation(PsiClass psiClass, String qualifiedName) {
        List<PsiClass> allClasses = new ArrayList<>();
        allClasses.add(psiClass);
        for (int i = 0; i < allClasses.size(); i++) {
            PsiClass currentClass = allClasses.get(i);
            if (psiClass == null) {
                continue;
            }
            if ("java.lang.Object".equals(currentClass.getQualifiedName())) {
                continue;
            }
            PsiAnnotation annotation = psiClass.getAnnotation(qualifiedName);
            if (annotation != null) {
                return annotation;
            }
        }
        return null;
    }
}