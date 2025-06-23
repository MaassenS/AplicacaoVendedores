module com.mycompany.aplicacaovendedores {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.mycompany.aplicacaovendedores to javafx.fxml;
    exports com.mycompany.aplicacaovendedores;
    
    // para PropertyValueFactory funcionar é necessário usar a cláusula a seguir:
    opens model.classes;
}
