module Graphic.Copy {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires opencsv;
    requires gson;
    requires java.sql;
    // requires jmf;
    requires jlayer;

    opens project.View to javafx.fxml;

    exports project.View;
}