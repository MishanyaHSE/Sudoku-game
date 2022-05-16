module lobanovich.michael.kursach {
    requires javafx.controls;
    requires javafx.fxml;


    opens lobanovich.michael.kursach to javafx.fxml;
    exports lobanovich.michael.kursach;
}