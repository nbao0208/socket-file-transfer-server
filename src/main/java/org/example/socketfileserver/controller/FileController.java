package org.example.socketfileserver.controller;

import com.google.inject.Singleton;
import javafx.util.Pair;
import org.example.socketfileserver.config.DIInjector;
import org.example.socketfileserver.service.FileService;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

@Singleton
public class FileController {
  private final FileService fileService = DIInjector.injector.getInstance(FileService.class);

  public String downLoadFile(DataOutputStream dataOutputStream, String fileName){
    return fileService.downloadFile(dataOutputStream,fileName);
  }

  public boolean downloadFiles(DataOutputStream dataOutputStream, List<String> fileNames) throws IOException {
    return fileService.downloadFiles(dataOutputStream,fileNames);
  }

  public boolean downloadMultipleFiles(DataOutputStream dataOutputStream, List<Pair<String, Integer>> fileNames) throws IOException {
    return fileService.downloadMultipleFiles(dataOutputStream,fileNames);
  }
}
