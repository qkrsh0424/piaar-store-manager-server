package com.piaar_store_manager.server.domain.product_release.type;

import org.apache.commons.codec.binary.StringUtils;

public enum ProductReleaseObjectType {
    Basic("basic", "product release basic type"),
    M2OJ("m2oj", "join product release with many to one entity");

    private final String objectType;
    private final String description;

    ProductReleaseObjectType(String objectType, String description) {
        this.objectType = objectType;
        this.description = description;
    }

    public static ProductReleaseObjectType getObjectType(String arg) {
        for(ProductReleaseObjectType type : values()) {
            if(StringUtils.equals(type.objectType, arg)) {
                return type;
            }
        }

        // 매칭되는 오브젝트가 없다면 default 타입 리턴
        return Basic;
    }
}
