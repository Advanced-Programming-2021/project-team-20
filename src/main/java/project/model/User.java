package project.model;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.image.Image;

public class User {

    private String name;
    private String nickname;
    private String password;
    private String imagePath;
    private int score;
    private int money;
    private HashMap<String, Deck> decks = new HashMap<>();
    private ArrayList<String> allUselessCards = new ArrayList<>();
    private Image image;

    public User(String username, String nickname, String password, String imagePath) {
        this.name = username;
        this.nickname = nickname;
        this.password = password;
        this.imagePath = imagePath;
        this.money = 100000;
    }

    public User() {
    }

    public String getImagePath() {
        return imagePath;
    }

    public HashMap<String, Deck> getDecks() {
        return decks;
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

    public int getMoney() {
        return money;
    }

    public ArrayList<String> getAllUselessCards() {
        return allUselessCards;
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

    public void setImage(Image image) {
        this.image = image;
    }

    public void addDeckToAllDecks(String deckname, Deck deck) {
        decks.put(deckname, deck);
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void deleteDeck(String deckname) {
        Deck deck = decks.get(deckname);
        allUselessCards.addAll(deck.getMainDeck());
        allUselessCards.addAll(deck.getSideDeck());
    //    System.out.println("in delete deck method");
        decks.remove(deckname);
    }

    public boolean doesCardExistsInUselessCards(String cardName) {
        return allUselessCards.contains(cardName);
    }

    public void deleteCardFromAllUselessCards(String cardname) {
        allUselessCards.remove(cardname);
    }

    public void addCardToAllUselessCards(String cardname) {
        allUselessCards.add(cardname);
    }

    public Image getImage() {
        return image;
    }

}