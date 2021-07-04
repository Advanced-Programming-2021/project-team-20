package project;

import project.controller.non_duel.storage.Storage;
import project.view.View;

public class Main {
  public static void main(String[] args) throws Exception {
    Storage storage = new Storage();
    storage.startProgram();
    View view = new View();
    view.infiniteLoop();
    storage.endProgram();

  }
}
