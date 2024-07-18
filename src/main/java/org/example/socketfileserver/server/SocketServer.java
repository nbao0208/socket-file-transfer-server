package org.example.socketfileserver.server;


import com.google.inject.Singleton;
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

@Singleton
public class SocketServer {
  private static DataInputStream dataInputStream = null;
  private static DataOutputStream dataOutputStream = null;
  private static final FileController fileController = DIInjector.injector.getInstance(FileController.class);
  public static void main(String[] args) throws IOException {
      ServerSocket serverSocket = new ServerSocket(ServerInformation.SINGLE_SERVER_PORT);

      System.out.println("Server is listening on port " + serverSocket.getLocalPort());
      //Keep server always listening to client sockets
      while (true){
        Socket clientSocket = serverSocket.accept();
        System.out.println("Client connected");

        dataInputStream = new DataInputStream(clientSocket.getInputStream());
        dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
        long quantity = dataInputStream.readLong();
        List<String> fileNames = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
          fileNames.add(DownloadFile.getPathOf(dataInputStream.readUTF()));
        }

        if (!fileController.downloadFiles(dataOutputStream,fileNames)){
          dataOutputStream.writeUTF(ResponseMessage.FAILED_MESSAGE);
        }
        clientSocket.close();
        dataInputStream.close();
        dataOutputStream.close();
        System.out.println("Client disconnected");
      }
  }
}
