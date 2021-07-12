package project.server.controller.non_duel.scoreboard;

import project.model.User;
import project.server.controller.non_duel.storage.Storage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Scoreboard {

    private static ArrayList<User> allUsers;
    private static int rankOfPlayers;
    private static StringBuilder finalResult;

    public static String findCommands(String command) {
        rankOfPlayers = 1;
        finalResult = new StringBuilder();
        if (command.equals("scoreboard show")) {
            return sortUsers();
        }

        return "invalid command!";
    }


    private static String sortUsers() {

        List<Integer> scoreOfUsers = new ArrayList<>();
        scoreOfUsers = getScoreOfUsers();

        for (int i = 0; i < scoreOfUsers.size(); i++) {
            ArrayList<User> usersWithSameScore = getUserByScore(scoreOfUsers.get(i));

            if (i > 0) {
                finalResult.append(",");

            }


            if (usersWithSameScore.size() == 1) {
                finalResult.append(rankOfPlayers + "," + usersWithSameScore.get(0).getNickname() + ",");
                finalResult.append(usersWithSameScore.get(0).getScore());

                rankOfPlayers++;
            } else {
                sortUsersWithSameScore(usersWithSameScore);
                rankOfPlayers += usersWithSameScore.size();
            }
        }

        return finalResult.toString();
    }

    private static void sortUsersWithSameScore(ArrayList<User> usersWithSameScroe) {

        ArrayList<String> nicknames = new ArrayList<>();
        for (int i = 0; i < usersWithSameScroe.size(); i++) {
            nicknames.add(usersWithSameScroe.get(i).getNickname());
        }

        Collections.sort(nicknames);

        for (int i = 0; i < nicknames.size(); i++) {
            if (i > 0) {
                finalResult.append(",");
            }
            for (int j = 0; j < nicknames.size(); j++) {
                if (usersWithSameScroe.get(i).getNickname().equals(nicknames.get(j))) {
                    finalResult.append(rankOfPlayers + "," + usersWithSameScroe.get(i).getNickname());
                    finalResult.append("," + usersWithSameScroe.get(i).getScore());
                    break;
                }
            }
        }
    }

    private static ArrayList<User> getUserByScore(int score) {

        ArrayList<User> usersWithSameScroe = new ArrayList<>();
        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).getScore() == score) {
                usersWithSameScroe.add(allUsers.get(i));
            }
        }
        return usersWithSameScroe;

    }

    private static List<Integer> getScoreOfUsers() {

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
