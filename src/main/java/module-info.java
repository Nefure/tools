module org.nefure.tools {

    requires transitive org.nefure.fxscaffold;
    requires org.yaml.snakeyaml;

    requires druid;
    requires mysql.connector.java;
    requires java.sql;

    opens org.nefure.tools to javafx.fxml,org.nefure.fxscaffold,javafx.controls,javafx.graphics,javafx.base;

    opens org.nefure.tools.data.imgs to org.nefure.fxscaffold;
    opens org.nefure.tools.data.style to org.nefure.fxscaffold;

    exports org.nefure.tools;
    exports org.nefure.tools.view;
    exports org.nefure.tools.config;
    exports org.nefure.tools.entity;
    exports org.nefure.tools.controller;
    exports org.nefure.tools.dao;
    exports org.nefure.tools.service;
    opens org.nefure.tools.controller to javafx.base, javafx.controls, javafx.fxml, javafx.graphics, org.nefure.fxscaffold;
    opens org.nefure.tools.entity to javafx.base, javafx.controls, javafx.fxml, javafx.graphics, org.nefure.fxscaffold;
}