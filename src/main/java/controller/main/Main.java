package controller.main;

import java.util.HashMap;
import java.util.Map;

import controller.addefectstocards.AddEfectsToCards;
import controller.storage.Storage;
import model.cardData.MonsterCardData.MonsterCardAttribute;
import model.cardData.MonsterCardData.MonsterCardFamily;
import view.View;

public class Main {
  public static void main(String[] args) throws Exception {

    Storage storage = new Storage();
    storage.startProgram();
    View view = new View();
    view.infiniteLoop();
    storage.endProgram();

  }

  // public static void x() {
  // // MonsterCardAttribute[] monsterCardAttributes =
  // MonsterCardAttribute.values();
  // // for (int i = 0; i < monsterCardAttributes.length; i++) {
  // //
  // System.out.println(monsterCardAttributes[i].equals(MonsterCardFamily.AQUA));
  // // }
  // AddEfectsToCards addEfectsToCards = new AddEfectsToCards();

  // Map<String, Enum<?>[]> a = new HashMap<>();//
  // addEfectsToCards.getMonsterEffects("Command Knight");
  // // MonsterCardAttribute b = (MonsterCardAttribute) a.get("1");
  // // System.out.println(b);

  // // MonsterCardAttribute monsterCardAttribute = MonsterCardAttribute.DARK;
  // Enum[] B = { MonsterCardFamily.AQUA, MonsterCardFamily.AQUA};

  // a.put("das", B);
  // // //a.put("da", MonsterCardFamily.AQUA);
  // Enum[] c = a.get("das");

  // for (int i = 0; i < a.get("das").length; i++) {
  // MonsterCardFamily monsterCardFamily = (MonsterCardFamily) c[i];
  // System.out.println(monsterCardFamily);
  // }

  // // // System.out.println(monsterCardFamily);
}
