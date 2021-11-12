package xyz.zenheart.guiserial.modules.pgsql.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.zenheart.guiserial.pojo.entity.TableInfoEntity;

import java.util.List;

/**
 * <p>项目名称: cgenerator </p>
 * <p>描述: TODO </p>
 * <p>创建时间: 2021/9/22 </p>
 * <p>公司信息: 维之星研发部</p>
 *
 * @author CKM
 * @version v1.0
 */
@Mapper
//@Repository("pgsqlTableInfoMapper")
public interface IPgsqlTableInfoMapper {

    List<TableInfoEntity> queryTableInfo(@Param("schema") String schema);
}
