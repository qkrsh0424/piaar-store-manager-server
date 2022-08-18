package com.piaar_store_manager.server.domain.product_receive.type;

import org.apache.commons.codec.binary.StringUtils;

public enum ProductReceiveObjectType {
    Basic("basic", "product receive basic type"),
    M2OJ("m2oj", "join product receive with many to one entity");
    // JoinPAO("joinPAO", "join product receive with product and option");

    private final String objectType;
    private final String description;

    ProductReceiveObjectType(String objectType, String description) {
        this.objectType = objectType;
        this.description = description;
    }

    public static ProductReceiveObjectType getObjectType(String arg) {
        for(ProductReceiveObjectType type : values()) {
            if(StringUtils.equals(type.objectType, arg)) {
                return type;
            }
        }

        // 매칭되는 오브젝트가 없다면 default 타입 리턴
        return Basic;
    }
}
