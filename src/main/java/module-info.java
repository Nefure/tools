module org.nefure.tools {

    requires transitive org.nefure.fxscaffold;
    requires org.yaml.snakeyaml;

    opens org.nefure.tools to javafx.fxml,org.nefure.fxscaffold,javafx.controls,javafx.graphics,javafx.base;

    opens org.nefure.tools.data.imgs to org.nefure.fxscaffold;
    opens org.nefure.tools.data.style to org.nefure.fxscaffold;

    exports org.nefure.tools;
    exports org.nefure.tools.view;
    exports org.nefure.tools.config;
    exports org.nefure.tools.controller;
    opens org.nefure.tools.controller to javafx.base, javafx.controls, javafx.fxml, javafx.graphics, org.nefure.fxscaffold;
}