package xyz.zenheart.guiserial;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import xyz.zenheart.guiserial.config.SpringConfiguration;
import xyz.zenheart.guiserial.pojo.entity.SettingEntity;
import xyz.zenheart.guiserial.utils.PathUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Objects;

/**
 * <p>项目名称: generator </p>
 * <p>描述: 代码生成器启动类 </p>
 * <p>创建时间: 2021/7/29 </p>
 * <p>公司信息: 维之星研发部</p>
 *
 * @author CKM
 * @version v1.0
 */
@Slf4j
public class ApplicationMain extends Application {

    public static final ThreadLocal<SettingEntity> SETTING = ThreadLocal.withInitial(SettingEntity::new);

    public static final AnnotationConfigApplicationContext ctx;

    public static Stage stage;

    static {
        ctx = new AnnotationConfigApplicationContext();
        ctx.register(SpringConfiguration.class);
        ctx.refresh();
    }

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(PathUtils.fxml("/Desktop.fxml"));
        stage.setTitle("代码生成器");
        stage.getIcons().add(new Image("/assets/icon/icon.jpg"));
        Scene scene = new Scene(root, 500, 400);
        scene.getStylesheets().add("/assets/css/global.css");
        stage.setScene(scene);
        stage.setResizable(false);
        ApplicationMain.stage = stage;
//        this.systemTraySetting();
        stage.show();
    }

    private void systemTraySetting() {
        Platform.setImplicitExit(false);
        java.awt.Image image = null;
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/icon/icon.jpg")));
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }

        SystemTray tray = SystemTray.getSystemTray();
        final ActionListener closeListener = e -> System.exit(0);

        ActionListener showListener = e -> Platform.runLater(stage::show);
        // create a popup menu
        PopupMenu popup = new PopupMenu();

        MenuItem showItem = new MenuItem("Show");
        showItem.addActionListener(showListener);
        popup.add(showItem);

        MenuItem closeItem = new MenuItem("Close");
        closeItem.addActionListener(closeListener);
        popup.add(closeItem);
        /// ... add other items
        // construct a TrayIcon
        TrayIcon trayIcon = new TrayIcon(image, "Title", popup);
        // set the TrayIcon properties
        trayIcon.addActionListener(showListener);
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.err.println(e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
