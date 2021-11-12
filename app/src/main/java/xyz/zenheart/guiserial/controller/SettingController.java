package xyz.zenheart.guiserial.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.DirectoryChooser;
import javafx.util.StringConverter;
import xyz.zenheart.guiserial.ApplicationMain;
import xyz.zenheart.guiserial.enums.DataBaseEnum;
import xyz.zenheart.guiserial.pojo.entity.SettingEntity;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * <p>项目名称: cgenerator </p>
 * <p>描述: 配置页面 </p>
 * <p>创建时间: 2021/9/23 </p>
 * <p>公司信息: 维之星研发部</p>
 *
 * @author CKM
 * @version v1.0
 */
public class SettingController implements Initializable {
    @FXML
    private TextField directoryLocation;
    @FXML
    private Button directoryChooser;
    @FXML
    private TextField databaseUrl;
    @FXML
    private TextField databaseName;
    @FXML
    private ChoiceBox<DataBaseEnum> databaseType;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private TextField schema;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Tooltip tooltip = new Tooltip();
        tooltip.setText("请输入");

        databaseUrl.setFocusTraversable(false);
        databaseUrl.setTooltip(tooltip);

        databaseType.setItems(FXCollections.observableArrayList(DataBaseEnum.values()));
        databaseType.getSelectionModel().selectFirst();
        databaseType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            System.out.println(oldValue.getName());
            System.out.println(newValue.getName());
        });
        databaseType.setConverter(new DataBaseConverter());

        directoryChooser.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Choose Folder");
            File directory = directoryChooser.showDialog(ApplicationMain.stage);
            if (directory != null) {
                System.out.println(directory.getAbsolutePath());
                directoryLocation.setText(directory.getAbsolutePath());
            }
        });
    }

    static class DataBaseConverter extends StringConverter<DataBaseEnum> {
        public DataBaseConverter() {
        }

        @Override
        public String toString(DataBaseEnum object) {
            return object.getName();
        }

        @Override
        public DataBaseEnum fromString(String name) {
            return DataBaseEnum.getByName(name);
        }
    }

    @FXML
    private void saveConfigEvent(ActionEvent event) {
        SettingEntity setting = ApplicationMain.SETTING.get();
        setting.setDirectoryLocation(directoryLocation.getText());
        setting.setDatabaseUrl(databaseUrl.getText());
        setting.setDatabaseName(databaseName.getText());
        setting.setDatabaseType(databaseType.getValue().getName());
        setting.setUsername(username.getText());
        setting.setPassword(password.getText());
        setting.setSchema(schema.getText());
        System.out.println("111111111111111111");
    }
}
