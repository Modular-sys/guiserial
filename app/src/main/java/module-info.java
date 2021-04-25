open module guiserial.main.app {
  requires javafx.controls;
  requires javafx.fxml;
  requires guiserial.core.main;
  requires spring.context;
  requires spring.boot;
  requires spring.boot.autoconfigure;

  exports org.tdom.guiserial;
}