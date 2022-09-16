package com.piaar_store_manager.server.domain.product.type;

import org.apache.commons.codec.binary.StringUtils;

public enum ProductObjectType {
    Basic("basic", "product basic type"),
    M2OJ("m2oj", "join product with many to one entity"),
    FJ("fj", "join product with full join entity");

    private final String objectType;
    private final String description;

    ProductObjectType(String objectType, String description) {
        this.objectType = objectType;
        this.description = description;
    }

    public static ProductObjectType getObjectType(String arg) {
        for(ProductObjectType type : values()) {
            if(StringUtils.equals(type.objectType, arg)) {
                return type;
            }
        }

        // 매칭되는 오브젝트가 없다면 default 타입 리턴
        return Basic;
    }
}
