module PhaseOneProject {
    requires gson;
    requires opencsv;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.sql;
    requires java.desktop;


    opens project.client.view to javafx.fxml;
    exports project.client.view;
    opens project.client.view.pooyaviewpackage to javafx.fxml;
    exports project.client.view.pooyaviewpackage;
    opens project.client.view.internet to javafx.fxml;
    exports project.client.view.internet;
    exports project.client.view.newClassesForCardCreator;
    opens project.client.view.newClassesForCardCreator to javafx.fxml;
    opens project.client.view.Components to javafx.base;
}
