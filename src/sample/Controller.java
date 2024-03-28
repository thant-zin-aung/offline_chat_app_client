package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Controller {

    @FXML
    public JFXTextField msgBox;
    @FXML
    public JFXTextArea msgField;
    @FXML
    public JFXButton sendMsgButton;
    @FXML
    public Label clientStatus;

    private String username;
    private final String address="192.168.99.220";
    private final int port=1666;
    public static Socket socket;
    public void initialize() {
        username = "Thant Zin Aung";
        username += ": ";
        Runnable sendToServer = new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(address,port);
                    clientStatus.setText("Connected to server");
                    BufferedReader readFromSocket = new BufferedReader(
                            new InputStreamReader(socket.getInputStream())
                    );
                    PrintWriter writeToSocket = new PrintWriter(
                            socket.getOutputStream(),true
                    );
                    msgBox.setOnKeyReleased( e -> {
                            if ( e.getCode() == KeyCode.ENTER ) {
                                String msg = username+msgBox.getText();
                                if ( !msgBox.getText().trim().equals("") ) {
                                    msgField.appendText(msg+"\n");
                                    writeToSocket.println(msg);
                                    msgBox.clear();
                                }
                            }
                        }
                    );
                    sendMsgButton.setOnAction( e -> {
                        String msg = username+msgBox.getText();
                        if ( !msgBox.getText().trim().equals("") ) {
                            msgField.appendText(msg+"\n");
                            writeToSocket.println(msg);
                            msgBox.clear();
                        }
                    });
                    String readData;
                    while (!(readData=readFromSocket.readLine()).equals("exit")) {
                        if ( readData!=null ) {
                            msgField.appendText(readData+"\n");
                        }
                    }
                } catch ( Exception e ) {
                    System.out.println(e.getMessage());
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            clientStatus.setText("Disconnected from server");
                        }
                    });
                } finally {
                    try {
                        socket.close();
                    } catch ( Exception e ) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        };

        new Thread(sendToServer).start();
    }
}















