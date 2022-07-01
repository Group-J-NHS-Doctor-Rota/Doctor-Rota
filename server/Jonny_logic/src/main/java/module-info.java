module edu.uob.prototype {
    requires javafx.controls;
    requires javafx.fxml;


    opens edu.uob.prototype to javafx.fxml;
    exports edu.uob.prototype;
}