package project.view.Components;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Person {

    private SimpleIntegerProperty ranking = new SimpleIntegerProperty();
    private SimpleStringProperty nickname = new SimpleStringProperty();
    private SimpleIntegerProperty score = new SimpleIntegerProperty();

    public Person() {
    }

    public Person(int ranking, String nickname, int score) {
        this.ranking.set(ranking);
        this.nickname.set(nickname);
        this.score.set(score);
    }

    public int getRanking() {
        return this.ranking.get();
    }

    public void setRanking(int ranking) {
        this.ranking.set(ranking);
    }

    public int getScore() {
        return this.score.get();
    }

    public void setScore(int score) {
        this.score.set(score);
    }

    public String getNickname() {
        return nickname.get();
    }


    public void setNickname(String nickname) {
        this.nickname.set(nickname);
    }

}
