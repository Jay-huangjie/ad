package com.zlfcapp.batterymanager;

/*
 * 广告提供商枚举
 * 不需要的就删除，只保留需要的即可
 */
public  enum  AdProviderType {
        GDT("gdt"), CSJ("csj");
        private String type="";
         AdProviderType( String type) {
            this.type=type;
        }

    public String getType() {
        return type;
    }
}