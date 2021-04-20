package test.maven.model;

import java.util.ArrayList;

public class User {

    private String name;
    private String nickname;
    private String password;
    private int score;
    private int money;
    private ArrayList<Deck> decks;

    public User(String username, String password, String nickname) {

    }

    public ArrayList<Deck> getDecks() {
        return decks;
    }

    public int getMoney() {
        return money;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public int getScore() {
        return score;
    }

    public void setDecks(ArrayList<Deck> decks) {
        this.decks = decks;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String toString() {
        return "[name: " + name + ", nickname: " + nickname + ", password: " + password + ", score:" + score
                + ", money:" + money;
    }

}