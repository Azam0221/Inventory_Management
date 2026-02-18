package com.example.quizapp.inventorymanagement.context;


import org.apache.kafka.common.protocol.types.Field;

public class TenantContext {
    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();

    public static void setCurrentTenant(String tenantId){
        currentTenant.set(tenantId);
    }

    public static String getCurrentTenant(){
        return currentTenant.get();
    }

    public static void clear(){
        currentTenant.remove();
    }

}
