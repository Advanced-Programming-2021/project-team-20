
import controller.non_duel.storage.Storage;
import view.View;

public class Main {
  public static void main(String[] args) {
    Storage storage = new Storage();
    storage.startProgram();
    View view = new View();
    view.infiniteLoop();
    storage.endProgram();
  }

}
