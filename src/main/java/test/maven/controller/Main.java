package test.maven.controller;

import test.maven.controller.storage.Storage;
import test.maven.view.View;

public class Main {
  public static void main(String[] args) throws Exception {

      Storage storage = new Storage();
      storage.startProgram();
      View view = new View();
      view.infiniteLoop();
      storage.endProgram();

  }
}
