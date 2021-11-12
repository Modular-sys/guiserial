package xyz.zenheart.guiserial.pojo.widget;

import javafx.scene.Node;
import javafx.scene.control.Button;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DownloadButton extends Button {
    private Object rowData;

    public DownloadButton(String text) {
        super(text);
    }

    public DownloadButton(String text, Object rowData) {
        super(text);
        this.rowData = rowData;
    }

    public DownloadButton(String text, Node graphic) {
        super(text, graphic);
    }
}
