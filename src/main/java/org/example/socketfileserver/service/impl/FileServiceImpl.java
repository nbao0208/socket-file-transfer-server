package org.example.socketfileserver.service.impl;

import com.google.inject.Singleton;
import javafx.util.Pair;
import org.example.socketfileserver.service.FileService;
import org.example.socketfileserver.shared.constant.DownloadFile;
import org.example.socketfileserver.shared.constant.ResponseMessage;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class FileServiceImpl implements FileService {
  @Override
  public String downloadFile(DataOutputStream dataOutputStream, String filePath) {
    File file = new File(filePath);

    try(FileInputStream fileInputStream = new FileInputStream(file)) {
      byte[] buffer = new byte[DownloadFile.LIMIT_MB];
      int readBytes = 0;
      dataOutputStream.writeLong(file.length());
      while (((readBytes=fileInputStream.read(buffer))!=-1)){
          dataOutputStream.write(buffer,0,readBytes);
          dataOutputStream.flush();
      }
    } catch (IOException e) {
      return ResponseMessage.FAILED_MESSAGE;
    }
    return ResponseMessage.SUCCESS_MESSAGE;
  }

  @Override
  public boolean downloadFiles(DataOutputStream outputStream, List<String> fileNames) throws IOException {
    List<File> files = fileNames.stream().map(fileName->{
      Path path = Paths.get(fileName);
      return path.toAbsolutePath().toFile();
    }).toList();
    List<FileInputStream> fileInputStreamList = files.stream().map(file-> {
      try {
        return new FileInputStream(file);
      } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
      }
    }).toList();
    for (File file : files) {
      System.out.println(file.getName() + ": " + file.length());
      outputStream.writeLong(file.length());
    }
    fileInputStreamList.forEach(fileInputStream -> {
      try {
        byte[] buffer = new byte[DownloadFile.LIMIT_MB];
        int readBytes = 0;
        while ((readBytes=fileInputStream.read(buffer))!=-1){
          outputStream.write(buffer,0,readBytes);
          outputStream.flush();
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
    return true;
  }

  @Override
  public boolean downloadMultipleFiles(DataOutputStream dataOutputStream, List<Pair<String, Integer>> fileNames) throws IOException {
    long totalSize = 0;
    System.out.println("FileServiceImpl====>" + fileNames);
    List<File> files = fileNames.stream().map(fileName->{
      Path path = Paths.get(fileName.getKey());
      return path.toAbsolutePath().toFile();
    }).toList();
    for (File file : files) {
      System.out.println("File length: " + file.length());
      dataOutputStream.writeLong(file.length());
      totalSize+=file.length();
    }
    System.out.println("FileServiceImpl===>" + totalSize);
    List<Pair<FileInputStream,Integer>> fileInputStreams = files.stream().map(file-> {
      try {
        return new Pair<>(new FileInputStream(file),fileNames.get(files.indexOf(file)).getValue());
      } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
      }
    }).collect(Collectors.toList());

    this.readMultipleStreams(dataOutputStream,fileInputStreams,totalSize);

    return true;
  }

  private void readMultipleStreams(DataOutputStream dataOutputStream,List<Pair<FileInputStream,Integer>> fileInputStreams, long totalSize) throws IOException {
    int readBytes = 0;
    while (totalSize>0){
      for (Pair<FileInputStream, Integer> stream : fileInputStreams) {
        byte[] buffer = new byte[stream.getValue()*DownloadFile.LIMIT_MB];
        if ((readBytes=stream.getKey().read(buffer))!=-1){
          dataOutputStream.write(buffer,0,readBytes);
          totalSize-=readBytes;
        }
      }
    }
  }

}
