package xyz.zenheart.guiserial.modules.mysql.service;

import xyz.zenheart.guiserial.pojo.entity.TableInfoEntity;

import java.util.List;

/**
 * <p>项目名称: cgenerator </p>
 * <p>描述: mysql表信息接口 </p>
 * <p>创建时间: 2021/9/22 </p>
 * <p>公司信息: 维之星研发部</p>
 *
 * @author CKM
 * @version v1.0
 */
public interface IMysqlTableInfoService {

    List<TableInfoEntity> queryTableInfo(String schema);
}
