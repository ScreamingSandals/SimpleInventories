package org.screamingsandals.simpleinventories.inventory;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.util.List;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Origin {
    private final File file;
    private final List<Object> content;
    private final Type type;
    //private final FormatBuilder builder;

    public Origin(List<Object> content) {
        this(null, content, Type.INTERNAL);
    }

    public Origin(File file, List<Object> content) {
        this(file, content, Type.FILE);
    }

    /*public Origin(FormatBuilder builder, List<Object> content) {
        this(null, content, Type.BUILDER, builder);
    }*/


    public static enum Type {
        FILE,
        BUILDER,
        INTERNAL;
    }

}
