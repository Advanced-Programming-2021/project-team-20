package test.maven.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import test.maven.view.View;


public class Main {
  public static void main(String[] args) throws Exception {
    View view = new View();
    view.infiniteLoop();

  }
}
