package project.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import project.server.ServerController;
import project.server.controller.non_duel.storage.Storage;

public class Main {

    public static void main(String[] args) throws Exception {
        Storage storage = new Storage();
        storage.startProgram();
        ServerController.runServer();
    }
}
