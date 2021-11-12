package xyz.zenheart.guiserial.enums;

import lombok.Getter;

/**
 * <p>项目名称: cgenerator </p>
 * <p>描述: TODO </p>
 * <p>创建时间: 2021/9/26 </p>
 * <p>公司信息: 维之星研发部</p>
 *
 * @author CKM
 * @version v1.0
 */
public enum DataBaseEnum {
    MYSQL("mysql"),
    PGSQL("pgsql");
    @Getter
    private final String name;

    DataBaseEnum(String name) {
        this.name = name;
    }

    public static DataBaseEnum getByName(String name) {
        for (DataBaseEnum data : DataBaseEnum.values()) {
            if (data.getName().equals(name)) {
                return data;
            }
        }
        return null;
    }
}
