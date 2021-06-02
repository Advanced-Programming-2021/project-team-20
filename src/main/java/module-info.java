module Graphic.Copy {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;    
    requires gson;
    requires opencsv;
    
    opens sample.View to javafx.fxml;

    exports sample.View;
}