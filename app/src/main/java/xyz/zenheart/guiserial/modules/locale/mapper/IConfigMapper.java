package xyz.zenheart.guiserial.modules.locale.mapper;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import xyz.zenheart.guiserial.pojo.entity.UserEntity;

import java.util.List;

/**
 * <p>项目名称: cgenerator </p>
 * <p>描述: 配置类mapper </p>
 * <p>创建时间: 2021/9/9 </p>
 * <p>公司信息: 维之星研发部</p>
 *
 * @author CKM
 * @version v1.0
 */
@Repository("test2")
public interface IConfigMapper {

    @Select("select * from tables")
    List<UserEntity> queryAll();
}
