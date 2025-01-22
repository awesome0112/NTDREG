package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class ChooseToolController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    public static boolean allowHandleStubForLib;

    @FXML
    void chooseConcolicWithStubButtonClicked(MouseEvent event) throws IOException {
        allowHandleStubForLib = false;
        root = FXMLLoader.load(getClass().getResource("/fxml/ConcolicWithStubScene.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setResizable(false);
        stage.setTitle("AS4UT");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void chooseConcolicStub4LibButtonClicked(MouseEvent event) throws IOException {
        allowHandleStubForLib = true;
        root = FXMLLoader.load(getClass().getResource("/fxml/ConcolicStub4LibScene.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setResizable(false);
        stage.setTitle("STUBLIB4UT");
        stage.setScene(scene);
        stage.show();
    }

}