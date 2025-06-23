module com.mycompany.aplicacaoinelta {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.mycompany.aplicacaoinelta to javafx.fxml;
    exports com.mycompany.aplicacaoinelta;
    
    // para PropertyValueFactory funcionar é necessário usar a cláusula a seguir:
    opens model.classes;
}
