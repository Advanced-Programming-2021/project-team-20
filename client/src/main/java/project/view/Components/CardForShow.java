package project.view.Components;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

public class CardForShow {
    private String frontImage;
    private String backImage;
    private boolean flipped; //if true, front face is up
    private int i,j;

    public CardForShow(String frontImage, int i, int j) {
        this.frontImage = frontImage;
        this.i = i;
        this.j = j;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public ImagePattern getFrontImage() {
        return new ImagePattern(new Image(frontImage));
    }

    public boolean isFlipped() {
        return flipped;
    }

    public void switchFlipped() {
        flipped = !this.flipped;
    }

    public ImagePattern getBackImage() {
        return new ImagePattern(new Image(backImage));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardForShow card = (CardForShow) o;
        return this.frontImage.equals(card.frontImage);
    }

    @Override
    public String toString() {
        return " |"+(this.flipped?frontImage:backImage)+"| ";
    }
}
