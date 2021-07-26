package main.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.model.Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

public class Controller implements Observer {
    ServerSocket serverSocket = null;
    private final int PORT = 3001;

    @FXML
    private Button btnOpenServer;

    @FXML
    private Button btnSalir;

    @FXML
    private ListView<String> listClient;

    @FXML
    private Circle circleLed;

    @FXML
    void OpenServerOnMouseClicked(MouseEvent event) {
        byte[] ipBytes = {(byte)192,(byte)168,(byte)1, (byte)72};
        InetAddress ip = null;
        try {
            ip = InetAddress.getByAddress(ipBytes);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            serverSocket = new ServerSocket(PORT,100,ip);
            listClient.getItems().add("Server abierto: " + serverSocket.getInetAddress().getHostName());
            circleLed.setFill(Color.GREEN);
            Server server = new Server(serverSocket);
            server.addObserver(this);
            new Thread(server).start();
            TextArea log = new TextArea();
            ThreadServer serverR = new ThreadServer(serverSocket, log);
            serverR.addObserver(this);
            new Thread(serverR).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
/*        finally {
            try {
                serverSocket.close();
                listClient.getItems().add("Server cerrado");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/

    }

    @FXML
    void SalirOnMouseClicked(MouseEvent event) {
        System.exit(1);
    }

    @Override
    public void update(Observable o, Object arg) {
        Socket socket = (Socket)arg;
        listClient.getItems().add(socket.getInetAddress().getHostName());

    }
}
