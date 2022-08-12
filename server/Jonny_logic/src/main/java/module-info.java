module edu.uob.prototype {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens edu.uob.prototype to javafx.fxml;
    exports edu.uob.prototype;
}