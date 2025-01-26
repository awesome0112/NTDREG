package utils.versionCompareUtil.model.ast.dependency;

import mrmathami.cia.java.tree.dependency.JavaDependency;

import java.io.Serializable;

public class DependencyCountTable implements Serializable {
    private Integer USE = 0;
    private Integer MEMBER = 0;
    private Integer INHERITANCE = 0;
    private Integer INVOCATION = 0;
    private Integer OVERRIDE = 0;
    private Integer SPRING = 0;

    public DependencyCountTable() {
    }

    public DependencyCountTable(mrmathami.cia.java.jdt.tree.dependency.DependencyCountTable dependencyCountTable) {
        this.USE = dependencyCountTable.getCount(JavaDependency.USE);
        this.MEMBER = dependencyCountTable.getCount(JavaDependency.MEMBER);
        this.INHERITANCE = dependencyCountTable.getCount(JavaDependency.INHERITANCE);
        this.INVOCATION = dependencyCountTable.getCount(JavaDependency.INVOCATION);
        this.OVERRIDE = dependencyCountTable.getCount(JavaDependency.OVERRIDE);
        this.SPRING = 0;
    }

    public DependencyCountTable(Integer USE, Integer MEMBER, Integer INHERITANCE, Integer INVOCATION, Integer OVERRIDE) {
        this.USE = USE;
        this.MEMBER = MEMBER;
        this.INHERITANCE = INHERITANCE;
        this.INVOCATION = INVOCATION;
        this.OVERRIDE = OVERRIDE;
        this.SPRING = 0;
    }

    public DependencyCountTable(Integer USE, Integer MEMBER, Integer INHERITANCE, Integer INVOCATION, Integer OVERRIDE, Integer SPRING) {
        this.USE = USE;
        this.MEMBER = MEMBER;
        this.INHERITANCE = INHERITANCE;
        this.INVOCATION = INVOCATION;
        this.OVERRIDE = OVERRIDE;
        this.SPRING = SPRING;
    }

    public Integer getUSE() {
        return USE;
    }

    public void setUSE(Integer USE) {
        this.USE = USE;
    }

    public Integer getMEMBER() {
        return MEMBER;
    }

    public void setMEMBER(Integer MEMBER) {
        this.MEMBER = MEMBER;
    }

    public Integer getINHERITANCE() {
        return INHERITANCE;
    }

    public void setINHERITANCE(Integer INHERITANCE) {
        this.INHERITANCE = INHERITANCE;
    }

    public Integer getINVOCATION() {
        return INVOCATION;
    }

    public void setINVOCATION(Integer INVOCATION) {
        this.INVOCATION = INVOCATION;
    }

    public Integer getOVERRIDE() {
        return OVERRIDE;
    }

    public void setOVERRIDE(Integer OVERRIDE) {
        this.OVERRIDE = OVERRIDE;
    }

    public Integer getSPRING() {
        return SPRING;
    }

    public void setSPRING(Integer SPRING) {
        this.SPRING = SPRING;
    }

}
