package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ConnectionController {

    @FXML
    private TextField hostField;

    @FXML
    private TextField portField;

    @FXML
    private Button connectButton;

    @FXML
    private void initialize() {
        connectButton.setOnAction(event -> handleConnect());
    }

    private void handleConnect() {
        String host = hostField.getText();
        int port = Integer.parseInt(portField.getText());

        try {
            App.client = new SimpleClient(host, port);
            App.client.openConnection();
            Stage stage = (Stage) connectButton.getScene().getWindow();
            stage.close();
            App.showMainWindow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
