package utils.versionCompareUtil.model.ast.type;


import utils.versionCompareUtil.model.ast.annotation.JavaAnnotation;
import utils.versionCompareUtil.model.ast.node.Node;
import utils.versionCompareUtil.model.ast.utility.Utility;
import mrmathami.cia.java.jdt.tree.type.AbstractType;
import mrmathami.cia.java.jdt.tree.type.ReferenceType;
import mrmathami.cia.java.jdt.tree.type.SimpleType;
import mrmathami.cia.java.jdt.tree.type.SyntheticType;

import java.util.List;

public class JavaType {
    private String entityClass = new String();
    private String idClass = new String();
    private Integer id;
    private String describe = new String();
    private List annotates;

    private JavaType innerType;
    private List arguments;
    private List bounds;

    private Node node;

    public JavaType() {
    }

    public JavaType(AbstractType abstractType) {
        this.entityClass = abstractType.getEntityClass();
        this.idClass = abstractType.getIdClass();
//        this.id = abstractType.getId();
        this.describe = abstractType.getDescription();

        this.annotates = Utility.convertAnnotates(abstractType.getAnnotates());

        if(abstractType instanceof SimpleType) {
            if(((SimpleType) abstractType).getInnerType()!= null) {
                innerType = new JavaType(((SimpleType) abstractType).getInnerType());
            }
        }

        if(abstractType instanceof ReferenceType) {
            this.arguments = Utility.convertArguments(((ReferenceType) abstractType).getArguments());
            if(((ReferenceType) abstractType).getNode() != null) {
                this.node = new Node(((ReferenceType) abstractType).getNode());
            }
        }

        if(abstractType instanceof SyntheticType) {
            this.bounds = Utility.convertArguments(((SyntheticType) abstractType).getBounds());
        }
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

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public JavaType getInnerType() {
        return innerType;
    }

    public void setInnerType(JavaType innerType) {
        this.innerType = innerType;
    }

    public List<JavaType> getArguments() {
        return arguments;
    }

    public void setArguments(List<JavaType> arguments) {
        this.arguments = arguments;
    }

    public List<JavaType> getBounds() {
        return bounds;
    }

    public void setBounds(List<JavaType> bounds) {
        this.bounds = bounds;
    }

    public List<JavaAnnotation> getAnnotates() {
        return annotates;
    }

    public void setAnnotates(List<JavaAnnotation> annotates) {
        this.annotates = annotates;
    }
}
