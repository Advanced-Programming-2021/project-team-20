package controller.non_duel.scoreboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import controller.non_duel.storage.Storage;
import model.User;

public class Scoreboard {

    private ArrayList<User> allUsers;
    private int rankOfPlayers;
    private StringBuilder finalResult;

    public String findCommands(String command) {
        rankOfPlayers = 1;
        finalResult = new StringBuilder();
        if (command.equals("scoreboard show")) {
            return sortUsers();
        }
       
        return "invalid command!";
    }

    private String sortUsers() {

        List<Integer> scoreOfUsers = new ArrayList<>();
        scoreOfUsers = getScoreOfUsers();

        for (int i = 0; i < scoreOfUsers.size(); i++) {
            ArrayList<User> usersWithSameScore = getUserByScore(scoreOfUsers.get(i));

            if (i > 0) {
                finalResult.append("\n");
            }

            if (usersWithSameScore.size() == 1) {
                finalResult.append(rankOfPlayers + "-" + usersWithSameScore.get(0).getNickname() + ": ");
                finalResult.append(usersWithSameScore.get(0).getScore());
                rankOfPlayers++;
            } else {
                sortUsersWithSameScore(usersWithSameScore);
                rankOfPlayers += usersWithSameScore.size();
            }
        }

        return finalResult.toString();
    }

    private void sortUsersWithSameScore(ArrayList<User> usersWithSameScroe) {

        ArrayList<String> nicknames = new ArrayList<>();
        for (int i = 0; i < usersWithSameScroe.size(); i++) {
            nicknames.add(usersWithSameScroe.get(i).getNickname());
        }

        Collections.sort(nicknames);

        for (int i = 0; i < nicknames.size(); i++) {
            if (i > 0) {
                finalResult.append("\n");
            }
            for (int j = 0; j < nicknames.size(); j++) {
                if (usersWithSameScroe.get(i).getNickname().equals(nicknames.get(j))) {
                    finalResult.append(rankOfPlayers + "-" + usersWithSameScroe.get(i).getNickname());
                    finalResult.append(": " + usersWithSameScroe.get(i).getScore());
                    break;
                }
            }
        }
    }

    private ArrayList<User> getUserByScore(int score) {

        ArrayList<User> usersWithSameScroe = new ArrayList<>();
        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).getScore() == score) {
                usersWithSameScroe.add(allUsers.get(i));
            }
        }
        return usersWithSameScroe;

    }

    private List<Integer> getScoreOfUsers() {

        allUsers = Storage.getAllUsers();
        List<Integer> scoreOfUsers = new ArrayList<>();
        for (int i = 0; i < allUsers.size(); i++) {
            scoreOfUsers.add(allUsers.get(i).getScore());
        }

        scoreOfUsers = scoreOfUsers.stream().distinct().collect(Collectors.toList());

        Collections.sort(scoreOfUsers, Collections.reverseOrder());
        return scoreOfUsers;
    }
}