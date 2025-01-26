package utils.versionCompareUtil.model.ast.modifier;

import mrmathami.annotations.Nonnull;

import java.util.List;

public enum JavaModifier {
    PUBLIC, PROTECTED, PRIVATE, STATIC, ABSTRACT, FINAL, NATIVE, SYNCHRONIZED, TRANSIENT, VOLATILE, STRICTFP;

    @Nonnull
    public static final List<JavaModifier> VALUE_LIST = List.of(values());

}
