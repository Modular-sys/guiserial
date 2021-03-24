module guiserial.main.app {
  requires javafx.controls;
  requires javafx.fxml;
  requires guiserial.core.main;

  opens org.tdom.guiserial to javafx.fxml;
  exports org.tdom.guiserial;
}