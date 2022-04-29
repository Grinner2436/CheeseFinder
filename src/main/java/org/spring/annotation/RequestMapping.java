package org.spring.annotation;

public enum RequestMapping {

    REQUEST("RequestMapping", "org.springframework.web.bind.annotation.RequestMapping"),
    GET("GetMapping", "org.springframework.web.bind.annotation.GetMapping"),
    POST("PostMapping","org.springframework.web.bind.annotation.PostMapping");

    public String name;
    public String qualifiedName;

    RequestMapping(String name, String qualifiedName) {
        this.name = name;
        this.qualifiedName = qualifiedName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }
}
