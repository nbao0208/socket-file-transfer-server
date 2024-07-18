package org.example.socketfileserver.config;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.example.socketfileserver.controller.FileController;
import org.example.socketfileserver.server.SocketServer;
import org.example.socketfileserver.service.FileService;
import org.example.socketfileserver.service.impl.FileServiceImpl;

public class DIConfiguration extends AbstractModule {
  @Override
  protected void configure() {
    bind(SocketServer.class).in(Singleton.class);
    bind(FileController.class).in(Singleton.class);
    bind(FileService.class).to(FileServiceImpl.class).in(Singleton.class);
  }
}
