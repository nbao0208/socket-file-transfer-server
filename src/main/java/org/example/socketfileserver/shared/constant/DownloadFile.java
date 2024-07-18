package org.example.socketfileserver.shared.constant;


import java.util.Map;

public class DownloadFile {
  private DownloadFile(){}

  public static final Map<String, String> AVAILABLE_FILES = Map.of(
          "test.txt","files/test.txt",
          "test2.jpg","files/test2.jpg",
          "test3.zip","files/test3.zip",
          "test4.zip","files/test4.zip",
          "test5.zip","files/test5.zip"
  );

  public static final int LIMIT_MB = 8;

  public static String filesToString(){
    StringBuilder sb = new StringBuilder();
    AVAILABLE_FILES.forEach((key,value)-> sb.append(key + "\n"));
    return sb.toString();
  }

  public static String getPathOf(String fileName){
    return AVAILABLE_FILES.get(fileName);
  }
}
