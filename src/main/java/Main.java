
import controller.non_duel.storage.Storage;
import view.View;

public class Main {
  public static void main(String[] args) throws Exception {
    //set money from 1000 * 1000 to 100 * 1000
    Storage storage = new Storage();
    storage.startProgram();
    View view = new View();
    view.infiniteLoop();
    storage.endProgram();

  }
}