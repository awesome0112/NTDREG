package utils.versionCompareUtil.model.ast.annotation;

import mrmathami.cia.java.jdt.tree.annotate.Annotate;

public class JavaAnnotation {
    private String entityClass = new String();
    private String idClass = new String();
    private Integer id;
    private String name = new String();

    public JavaAnnotation() {
    }

    public JavaAnnotation(Annotate annotate) {
        this.entityClass = annotate.getEntityClass();
        this.idClass = annotate.getIdClass();
        this.id = annotate.getId();
        this.name = annotate.getName();
    }

    public String getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(String entityClass) {
        this.entityClass = entityClass;
    }

    public String getIdClass() {
        return idClass;
    }

    public void setIdClass(String idClass) {
        this.idClass = idClass;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
