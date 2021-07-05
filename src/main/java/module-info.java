module PhaseOneProject {
    requires gson;
    requires opencsv;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.sql;
  //  requires javax.image;


    opens project.view to javafx.fxml;
    exports project.view;
    opens project.view.pooyaviewpackage to javafx.fxml;
    exports project.view.pooyaviewpackage;
    opens project.view.internet to javafx.fxml;
    exports project.view.internet;
}
