package controller.non_duel.main;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.*;

import controller.non_duel.storage.Storage;
import view.View;

public class Main {
  public static void main(String[] args) throws Exception {
    // HashMap<String, ArrayList<String>> a = new HashMap<>();
    // JsonObject moh = new JsonObject();
    // JsonArray jsonArray = new JsonArray();
    // moh.add("ContinuousSpellCardEffect", jsonArray);
    // moh.add("EquipSpellEffect", jsonArray);
    // moh.add("FieldSpellEffect", jsonArray);
    // moh.add("LogicalActivationRequirement", jsonArray);
    // moh.add("NormalSpellCardEffect", jsonArray);
    // moh.add("QuickSpellEffect", jsonArray);
    // moh.add("RitualSpellEffect", jsonArray);
    // moh.add("SentToGraveyardEffect", jsonArray);
    // moh.add("UserReplyForActivation", jsonArray);
    // JsonObject jsonObject = new JsonObject();
    // jsonObject.add("command knight", moh);
    // System.out.println(moh);
    Storage storage = new Storage();
    storage.startProgram();
    View view = new View();
    view.infiniteLoop();
    storage.endProgram();

  }

}
