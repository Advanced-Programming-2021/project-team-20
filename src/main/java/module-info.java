module PhaseOneProject {
    requires gson;
    requires opencsv;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens project.view.pooyaviewpackage to javafx.fxml;
    exports project.view.pooyaviewpackage;
    opens project.view.internet to javafx.fxml;
    exports project.view.internet;
}
