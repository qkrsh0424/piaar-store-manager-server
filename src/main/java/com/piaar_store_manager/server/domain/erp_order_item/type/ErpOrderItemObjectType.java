package com.piaar_store_manager.server.domain.erp_order_item.type;

import org.apache.commons.codec.binary.StringUtils;

public enum ErpOrderItemObjectType {
    Basic("basic", "erp order item basic type"),
    ReleaseBasic("releaseBasic", "erp release item basic type"),
    M2OJ("m2oj", "erp order item with many to one entity"),
    ReleaseM2OJ("releaseM2oj", "erp release item with many to one entity");

    private String objectType;
    private String description;

    ErpOrderItemObjectType(String objectType, String description) {
        this.objectType = objectType;
        this.description = description;
    }

    public static ErpOrderItemObjectType getObjectType(String arg) {
        for(ErpOrderItemObjectType type : values()) {
            if(StringUtils.equals(type.objectType, arg)) {
                return type;
            }
        }

        return Basic;
    }
}
