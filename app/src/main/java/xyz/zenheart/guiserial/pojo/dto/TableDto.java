package xyz.zenheart.guiserial.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import xyz.zenheart.guiserial.pojo.widget.DownloadButton;
import xyz.zenheart.guiserial.pojo.widget.TableCheckbox;

/**
 * <p>项目名称: cgenerator </p>
 * <p>描述: 表信息实体对象 </p>
 * <p>创建时间: 2021/9/6 </p>
 * <p>公司信息: 维之星研发部</p>
 *
 * @author CKM
 * @version v1.0
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TableDto {
    private TableCheckbox tableCheckbox;
    private String tableName;
    private String describe;
    private DownloadButton downloadButton;
}
