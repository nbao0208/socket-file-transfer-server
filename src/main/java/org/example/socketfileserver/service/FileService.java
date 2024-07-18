package org.example.socketfileserver.service;


import javafx.util.Pair;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public interface FileService {
  String downloadFile(DataOutputStream outputStream, String fileName);

  boolean downloadFiles(DataOutputStream outputStream, List<String> fileNames) throws IOException;

  boolean downloadMultipleFiles(DataOutputStream dataOutputStream, List<Pair<String, Integer>> fileNames) throws IOException;
}
