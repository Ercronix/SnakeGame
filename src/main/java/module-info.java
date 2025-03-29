module net.ercronix.snakegame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens net.ercronix.snakegame to javafx.fxml;
    exports net.ercronix.snakegame;
}