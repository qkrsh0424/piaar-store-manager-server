package com.piaar_store_manager.server.domain.product_option.type;

import org.apache.commons.codec.binary.StringUtils;

public enum ProductOptionObjectType {
    Basic("basic", "product receive basic type"),
    M2OJ("m2oj", "join product receive with many to one entity");

    private final String objectType;
    private final String description;

    ProductOptionObjectType(String objectType, String description) {
        this.objectType = objectType;
        this.description = description;
    }

    public static ProductOptionObjectType getObjectType(String arg) {
        for(ProductOptionObjectType type : values()) {
            if(StringUtils.equals(type.objectType, arg)) {
                return type;
            }
        }

        // 매칭되는 오브젝트가 없다면 default 타입 리턴
        return Basic;
    }
}
