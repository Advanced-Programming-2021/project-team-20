package test.maven.controller;

import test.maven.model.User;
import test.maven.view.View;
import com.google.gson.*;
import java.io.*;

public class Main {
  public static void main(String[] args) throws Exception {

    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder.create();
    User user = new User(null, null, null);
    System.out.println(gson.toJson(user));
    String x = gson.toJson(user);
    File filee = new File("Resourses\\" + "salam");
    System.out.println(filee.mkdir());
    
    View view = new View();
    view.infiniteLoop();

  }
}
