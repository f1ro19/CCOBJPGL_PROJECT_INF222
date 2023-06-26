module com.example.bike {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
            
                            
    opens com.example.bike to javafx.fxml;
    exports com.example.bike;
}