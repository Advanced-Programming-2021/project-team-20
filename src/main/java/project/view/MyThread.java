package project.view;

public class MyThread implements Runnable {

  // to stop the thread
  private boolean exit;

  private String name;
  Thread t;

  public MyThread(String threadname) {
    name = threadname;
    t = new Thread(this, name);
    System.out.println("New thread: " + t);
    exit = false;

  }

  // execution of thread starts from run() method
  public void run() {
    int i = 0;
    while (!exit) {
      System.out.println(name + ": " + i);
      i++;
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        System.out.println("Caught:" + e);
      }
    }
    System.out.println(name + " Stopped.");
  }

  public void start() {
    t.start();
  }

  // for stopping the thread
  public void stop() {
    exit = true;
  }
}