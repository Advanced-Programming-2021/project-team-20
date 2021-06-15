package project.View.Components;

public class Person {

    private int ranking;
    private String nickname = null;
    private int score;

    public Person() {
    }

    public Person(int ranking, String nickname, int score) {
        this.ranking = ranking;
        this.nickname = nickname;
        this.score = score;
    }

    public int getRanking() {
        return this.ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}
