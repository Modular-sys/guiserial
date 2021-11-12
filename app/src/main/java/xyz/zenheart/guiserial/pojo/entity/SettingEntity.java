package xyz.zenheart.guiserial.pojo.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>项目名称: cgenerator </p>
 * <p>描述: TODO </p>
 * <p>创建时间: 2021/10/12 </p>
 * <p>公司信息: 维之星研发部</p>
 *
 * @author CKM
 * @version v1.0
 */
@Data
public class SettingEntity implements Serializable {

    private String directoryLocation;

    /**
     * 数据库地址例如 127.0.0.1:3306
     */
    private String databaseUrl;

    /**
     * 数据库类型
     */
    private String databaseType;

    /**
     * 数据库名
     */
    private String databaseName;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 概要/计划/模式/纲要
     */
        private String schema;
}
