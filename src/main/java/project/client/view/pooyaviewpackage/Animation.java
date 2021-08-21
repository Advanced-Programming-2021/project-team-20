package project.client.view.pooyaviewpackage;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import project.client.modelsforview.CardView;
import project.client.modelsforview.GamePhaseButton;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardType;

import java.util.Random;

class Animation {
    Media media;
    MediaPlayer mediaPlayer;
    MediaView mediaView;
    String string;
    Rectangle helpfulRectangle;
    Rectangle hideRectangle;
    double prevTime;
    boolean firstTimeShowingCard;
    boolean supremeKingActivatingSpell;
    boolean supremeKingActivatingMonsterEffect;
    boolean supremeKingActivatingSpellAlreadyInField;
    boolean supremeKingActivatingTrap;
    boolean supremeKingSummoningMonsterInAttackPosition;
    double readyTime = 0;

    public Animation(String string) {
        this.supremeKingActivatingMonsterEffect = false;
        this.supremeKingActivatingSpell = false;
        this.supremeKingActivatingSpellAlreadyInField = false;
        this.supremeKingActivatingTrap = false;
        this.supremeKingSummoningMonsterInAttackPosition = false;
        if (string.contains("Activating")) {
            if (string.contains("Monster") && string.contains("Effect")) {
                supremeKingActivatingMonsterEffect = true;
                readyTime = 1200;
            }
            if (string.contains("Spell") && string.contains("Already") && string.contains("Field")) {
                supremeKingActivatingSpellAlreadyInField = true;
                readyTime = 1300;
            } else if (string.contains("Spell")) {
                CardView cardView = DuelView.getControllerForView().getCardViewByCardLocation(AdvancedCardMovingController.getPreviousSelected());
                if (cardView.getCard().getCardName().contains("onster") && cardView.getCard().getCardName().contains("eborn")) {
                    string = string.replaceAll("\\.mp4", "");
                    string = string + " Another.mp4";
                }
                supremeKingActivatingSpell = true;
                readyTime = 463;
            }
            if (string.contains("Trap")) {
                supremeKingActivatingTrap = true;
                readyTime = 1500;
            }
        }
        if (string.contains("Summoning")) {
            if ((new Random()).nextInt(2) == 1) {
                string = string.replaceAll("\\.mp4", "");
                string = string + " Another.mp4";
                readyTime = 3050;
            } else {
                readyTime = 3550;
            }
            supremeKingSummoningMonsterInAttackPosition = true;
        }
        this.firstTimeShowingCard = true;
        this.helpfulRectangle = null;
        this.string = string;
        //DuelView.setSupremeKingMedia(new Media(AdvancedCardMovingController.class.getResource(string).toExternalForm()));
        this.media = new Media(AdvancedCardMovingController.class.getResource(string).toExternalForm());
        this.mediaPlayer = new MediaPlayer(media);
        this.mediaView = new MediaView(mediaPlayer);
        DuelView.getAnchorPane().getChildren().add(mediaView);
        mediaView.fitHeightProperty().bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));
        mediaView.fitWidthProperty().bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
        mediaView.setPreserveRatio(false);
        mediaView.setViewOrder(10);
        hideRectangle = new Rectangle(1150, 945, 37, 40);
        hideRectangle.setFill(Color.rgb(37,0,65));
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public MediaView getMediaView() {
        return mediaView;
    }


    public void play() {
        adjustSizeToFullScreen();
        DuelView.getAnchorPane().getChildren().add(hideRectangle);
        prevTime = System.currentTimeMillis();
        if (string.contains("Summoning") || string.contains("Activating")) {
            mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {

                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {

                    long currTime = System.currentTimeMillis();
                    if (Double.compare((currTime - prevTime), readyTime) > 0 && firstTimeShowingCard) {
                        firstTimeShowingCard = false;
                        helpfulRectangle = new Rectangle(0, 400, 400, 600);
                        if (supremeKingActivatingSpell && !string.contains("Another")) {
                            PerspectiveTransform e = new PerspectiveTransform();
                            e.setUlx(680);    // Upper left
                            e.setUly(100);
                            e.setUrx(845);    // Upper right
                            e.setUry(93);
                            e.setLlx(680);      // Lower left
                            e.setLly(540);
                            e.setLrx(840);    // Lower right
                            e.setLry(520);
                            helpfulRectangle.setEffect(e);
                        } else if (supremeKingActivatingSpellAlreadyInField) {
                            PerspectiveTransform e = new PerspectiveTransform();
                            e.setUlx(180);    // Upper left
                            e.setUly(60);
                            e.setUrx(850);    // Upper right
                            e.setUry(-50);
                            e.setLlx(280);      // Lower left
                            e.setLly(1200);
                            e.setLrx(990);    // Lower right
                            e.setLry(1090);
                            helpfulRectangle.setEffect(e);
                        } else if (supremeKingActivatingTrap) {
                            //perfection complete
                            PerspectiveTransform e = new PerspectiveTransform();
                            e.setUlx(420);    // Upper left
                            e.setUly(60);
                            e.setUrx(850);    // Upper right
                            e.setUry(160);
                            e.setLlx(370);      // Lower left
                            e.setLly(1100);
                            e.setLrx(880);    // Lower right
                            e.setLry(1000);
                            helpfulRectangle.setEffect(e);
                        } else if (supremeKingActivatingMonsterEffect) {
                            PerspectiveTransform e = new PerspectiveTransform();
                            e.setUlx(220);    // Upper left
                            e.setUly(80);
                            e.setUrx(800);    // Upper right
                            e.setUry(-100);
                            e.setLlx(380);      // Lower left
                            e.setLly(1100);
                            e.setLrx(950);    // Lower right
                            e.setLry(1000);
                            helpfulRectangle.setEffect(e);
                        } else if (supremeKingSummoningMonsterInAttackPosition) {
                            PerspectiveTransform e = new PerspectiveTransform();
                            e.setUlx(0);    // Upper left
                            e.setUly(20);
                            e.setUrx(640);    // Upper right
                            e.setUry(-20);
                            e.setLlx(0);      // Lower left
                            e.setLly(1000);
                            e.setLrx(690);    // Lower right
                            e.setLry(1050);
                            helpfulRectangle.setEffect(e);
                        }
                        CardView cardView = DuelView.getControllerForView().getCardViewByCardLocation(AdvancedCardMovingController.getPreviousSelected());
                        System.out.println("card name i am showing in animation is " + cardView.getCard().getCardName());
                        Card card = cardView.getCard();
                        if (card.getCardType().equals(CardType.MONSTER)) {
                            helpfulRectangle.setFill(new ImagePattern(new Image(CardView.class.getResource("/project/cards/monsters/" + card.getCardName() + ".jpg").toExternalForm())));
                        } else {
                            helpfulRectangle.setFill(new ImagePattern(new Image(CardView.class.getResource("/project/cards/spelltraps/" + card.getCardName() + ".jpg").toExternalForm())));
                        }
                        if (!(supremeKingActivatingSpell && string.contains("Another"))) {
                            DuelView.getAnchorPane().getChildren().add(helpfulRectangle);
                        }
                        System.out.println("delta: " + (currTime - prevTime) + ", old value: " + oldValue + ", new value: " + newValue);
                    }

                }
            });
        }
        mediaPlayer.play();
        if (string.contains("Ending Turn")) {
            GamePhaseButton.updateAllGamePhaseButtonsOnce();
        }
    }

    public void adjustSizeToFullScreen() {
        //mediaView.setFitWidth(DuelView.getStage().getWidth());
        //mediaView.setFitHeight(DuelView.getStage().getHeight());
        mediaView.setViewOrder(0);
    }

    public void adjustSizeToZero() {
        mediaView.setViewOrder(10);
        if (helpfulRectangle != null) {
            helpfulRectangle.setWidth(0);
            helpfulRectangle.setHeight(0);
        }
        if (hideRectangle != null){
            hideRectangle.setWidth(0);
            hideRectangle.setHeight(0);
        }
//        mediaView.fitWidthProperty().bind();setFitHeight(0);
//        mediaView.setFitWidth(0);
    }
}
