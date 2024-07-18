package org.example.socketfileserver.server;

import javafx.util.Pair;
import org.example.socketfileserver.config.DIInjector;
import org.example.socketfileserver.controller.FileController;
import org.example.socketfileserver.shared.constant.DownloadFile;
import org.example.socketfileserver.shared.constant.ResponseMessage;
import org.example.socketfileserver.shared.constant.ServerInformation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SocketServerMultipleFiles {
  private static DataInputStream dataInputStream = null;
  private static DataOutputStream dataOutputStream = null;
  private static final FileController fileController = DIInjector.injector.getInstance(FileController.class);

  public static void main(String[] args) throws IOException {
    ServerSocket serverSocket = new ServerSocket(ServerInformation.MULTIPLE_SERVER_PORT);

    System.out.println("Server is listening on port " + serverSocket.getLocalPort());
    //Keep server always listening to client sockets
    while (true){
      Socket clientSocket = serverSocket.accept();
      System.out.println("Client connected");

      dataInputStream = new DataInputStream(clientSocket.getInputStream());
      dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
      List<Pair<String, Integer>> fileNames = new ArrayList<>();
      while (dataInputStream.available() > 0){
        fileNames.add(new Pair<>(DownloadFile.getPathOf(dataInputStream.readUTF()), dataInputStream.readInt()));
      }
      System.out.println(fileNames);

      if (!fileController.downloadMultipleFiles(dataOutputStream, fileNames)) {
        dataOutputStream.writeUTF(ResponseMessage.FAILED_MESSAGE);
      }
      clientSocket.close();
      dataOutputStream.close();
      dataInputStream.close();
      System.out.println("Client disconnected");
    }
  }
}
