package project.View;

public class Person {

    private int ranking;
    private String username = null;
    private int score;

    public Person() {
    }

    public Person(int ranking, String username, int score) {
        this.ranking = ranking;
        this.username = username;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String firstName) {
        this.username = firstName;
    }

}
