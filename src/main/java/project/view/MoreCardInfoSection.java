package project.view;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.CardType;
import project.model.cardData.General.RowOfCardLocation;
import project.model.cardData.MonsterCardData.MonsterCard;
import project.model.cardData.MonsterCardData.MonsterCardAttribute;
import project.model.modelsforview.BattleFieldView;
import project.model.modelsforview.CardView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.CardType;
import project.model.cardData.General.RowOfCardLocation;
import project.model.cardData.MonsterCardData.MonsterCard;
import project.model.cardData.MonsterCardData.MonsterCardAttribute;
import project.model.modelsforview.BattleFieldView;
import project.model.modelsforview.CardView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MoreCardInfoSection {

    public void updateCardMoreInfoSection(CardView cardView) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String cardDescription = cardView.getCard().getCardDescription();
                Rectangle cardImageForCardMoreInfo = DuelView.getCardImageForCardMoreInfo();
                cardImageForCardMoreInfo.setFill(cardView.getFill());
                BattleFieldView battleFieldView = DuelView.getBattleFieldView();
                CardLocation cardLocationSelecting = DuelView.getCardLocationSelecting();
                VBox vBox = DuelView.getvBox();
                cardImageForCardMoreInfo.setLayoutY(battleFieldView.getUpperLeftY());
                cardImageForCardMoreInfo.setLayoutY(battleFieldView.getUpperLeftY());
                cardImageForCardMoreInfo.setLayoutX(0);
                cardImageForCardMoreInfo.setWidth(300);
                cardImageForCardMoreInfo.setHeight(436);
                Label cardNameForCardMoreInfo = DuelView.getCardNameForCardMoreInfo();
                cardNameForCardMoreInfo.setMaxHeight(80);
                cardNameForCardMoreInfo.setFont(new Font(30));
                cardNameForCardMoreInfo.setLayoutX(0);
                cardNameForCardMoreInfo.setLayoutY(436 + battleFieldView.getUpperLeftY());
                Label cardAttackForCardMoreInfo = DuelView.getCardAttackForCardMoreInfo();
                Label cardDefenseForCardMoreInfo = DuelView.getCardDefenseForCardMoreInfo();
                Rectangle cardAttributeForCardMoreInfo = DuelView.getCardAttributeForCardMoreInfo();
                Label cardLevelForCardMoreInfo = DuelView.getCardLevelForCardMoreInfo();
                if (cardView.getCard().getCardType().equals(CardType.MONSTER)) {
                    cardNameForCardMoreInfo.setText(" " + cardView.getCard().getCardName());

                    if (cardLocationSelecting == null) {
                        cardAttackForCardMoreInfo.setText(" ATK : " + MonsterCard.giveATKDEFConsideringEffects("attack", new CardLocation(RowOfCardLocation.ALLY_HAND_ZONE, 1), 0));
                        cardDefenseForCardMoreInfo.setText(" DEF : " + MonsterCard.giveATKDEFConsideringEffects("defense", new CardLocation(RowOfCardLocation.ALLY_HAND_ZONE, 1), 0));

                    } else {
                        cardAttackForCardMoreInfo.setText(" ATK : " + MonsterCard.giveATKDEFConsideringEffects("attack", cardLocationSelecting, 0));
                        cardDefenseForCardMoreInfo.setText(" DEF : " + MonsterCard.giveATKDEFConsideringEffects("defense", cardLocationSelecting, 0));

                    }
                    cardLevelForCardMoreInfo.setText(" LVL: " + ((MonsterCard) cardView.getCard()).getLevel());
                    cardLevelForCardMoreInfo.setFont(new Font(22));
                    cardLevelForCardMoreInfo.setLayoutY(486 + battleFieldView.getUpperLeftY());
                    Rectangle cardLevelStarForCardMoreInfo = DuelView.getCardLevelStarForCardMoreInfo();
                    cardLevelStarForCardMoreInfo.setFill(new ImagePattern(new Image(DuelView.class.getResource("/project/cardicons/star.png").toExternalForm())));
                    cardLevelStarForCardMoreInfo.setLayoutX(80);
                    cardLevelStarForCardMoreInfo.setLayoutY(486 + battleFieldView.getUpperLeftY() + 5);
                    cardLevelStarForCardMoreInfo.setHeight(20);
                    cardLevelStarForCardMoreInfo.setWidth(20);

                    String monsterCardFamily = ((MonsterCard) cardView.getCard()).getMonsterCardFamily().toString();
                    Label cardFamilyForCardMoreInfo = DuelView.getCardFamilyForCardMoreInfo();
                    cardFamilyForCardMoreInfo.setText(" (" + monsterCardFamily.charAt(0) + monsterCardFamily.substring(1).toLowerCase() + ")");
                    cardFamilyForCardMoreInfo.setMaxHeight(30);
                    cardFamilyForCardMoreInfo.setFont(new Font(18));
                    cardFamilyForCardMoreInfo.setLayoutX(battleFieldView.getUpperLeftX() * 3 / 4 - 40);
                    cardFamilyForCardMoreInfo.setLayoutY(battleFieldView.getUpperLeftY() + 486);

                    cardAttackForCardMoreInfo.setFont(new Font(24));
                    cardAttackForCardMoreInfo.setLayoutX(0);
                    cardAttackForCardMoreInfo.setLayoutY(battleFieldView.getUpperLeftY() + 486 + 30);
                    cardAttackForCardMoreInfo.setMaxWidth(battleFieldView.getUpperLeftX() / 2);


                    cardDefenseForCardMoreInfo.setFont(new Font(24));
                    cardDefenseForCardMoreInfo.setLayoutX(battleFieldView.getUpperLeftX() / 2);
                    cardDefenseForCardMoreInfo.setLayoutY(battleFieldView.getUpperLeftY() + 486 + 30);
                    cardDefenseForCardMoreInfo.setMaxWidth(battleFieldView.getUpperLeftX() / 2);
                    //cardAttributeForCardMoreInfo.setHeight(50);

                    MonsterCardAttribute monsterCardAttribute = ((MonsterCard) cardView.getCard()).getMonsterCardAttribute();
                    System.out.println("monster card attribute is " + monsterCardAttribute.toString().toLowerCase());
                    cardAttributeForCardMoreInfo.setFill(new ImagePattern(new Image(MoreCardInfoSection.class.getResource("/project/cardicons/" + monsterCardAttribute.toString().toLowerCase() + ".png").toExternalForm())));
                    cardAttributeForCardMoreInfo.setLayoutY(486 + 50 + battleFieldView.getUpperLeftY());
                    cardAttributeForCardMoreInfo.setLayoutX(battleFieldView.getUpperLeftX() / 2 - 30);
                    cardAttributeForCardMoreInfo.setLayoutY(battleFieldView.getUpperLeftY() + 486 + 30 + 30);
//                    moreCardInfoGroup.getChildren().add(cardAttackForCardMoreInfo);
//                    moreCardInfoGroup.getChildren().add(cardDefenseForCardMoreInfo);
//                    moreCardInfoGroup.getChildren().add(cardLevelForCardMoreInfo);
//                    moreCardInfoGroup.getChildren().add(cardLevelStarForCardMoreInfo);
//                    moreCardInfoGroup.getChildren().add(cardFamilyForCardMoreInfo);
                } else if (cardView.getCard().getCardType().equals(CardType.SPELL)) {
                    cardNameForCardMoreInfo.setText(" " + cardView.getCard().getCardName());
                    cardAttributeForCardMoreInfo.setFill(new ImagePattern(new Image(DuelView.class.getResource("/project/cardicons/spell.png").toExternalForm())));
                    cardAttributeForCardMoreInfo.setLayoutY(486 + battleFieldView.getUpperLeftY());
                } else {
                    cardNameForCardMoreInfo.setText(" " + cardView.getCard().getCardName());
                    cardAttributeForCardMoreInfo.setFill(new ImagePattern(new Image(DuelView.class.getResource("/project/cardicons/trap.png").toExternalForm())));
                    cardAttributeForCardMoreInfo.setLayoutY(486 + battleFieldView.getUpperLeftY());
                }


                cardAttributeForCardMoreInfo.setWidth(60);
                cardAttributeForCardMoreInfo.setHeight(60);
                List<String> shortCardDescription = new ArrayList<>();
                shortCardDescription = Arrays.asList(cardDescription.split(" "));
                StringBuilder sentencesForEachLabel = new StringBuilder();
                int numberOFLabelUsed = 0;
                Label label;
                for (int i = 0; i < shortCardDescription.size(); i++) {
                    label = new Label();
                    label.setFont(new Font(22));

                    if (sentencesForEachLabel.length() <= 31 - shortCardDescription.get(i).length()) {
                        sentencesForEachLabel.append(shortCardDescription.get(i) + " ");
                        //addEffectToLabel(label, sentencesForEachLabel.toString());
                        //label.setText(sentencesForEachLabel.toString());
                        //label.setLayoutX(100);
                        label.setLayoutY(20 * (numberOFLabelUsed + 1));
                        //vBox.getChildren().add(label);
                        //sentencesForEachLabel.setLength(0);
                        numberOFLabelUsed++;
                    } else {
                        while (sentencesForEachLabel.length() <= 30) {
                            sentencesForEachLabel.append(" ");
                        }
                        label.setText(sentencesForEachLabel.toString());
                        //label.setLayoutX(100);
                        label.setLayoutY(20 * (numberOFLabelUsed + 1));
                        vBox.getChildren().add(label);
                        sentencesForEachLabel.setLength(0);
                        numberOFLabelUsed++;
                    }
                }
                while (sentencesForEachLabel.length() <= 30) {
                    sentencesForEachLabel.append(" ");
                }
                label = new Label();
                label.setFont(new Font(22));
                label.setText(sentencesForEachLabel.toString());
                //label.setLayoutX(100);
                label.setLayoutY(20 * (numberOFLabelUsed + 1));
                vBox.getChildren().add(label);
                sentencesForEachLabel.setLength(0);
                numberOFLabelUsed++;
                ScrollPane scrollPaneForCardMoreInfo = DuelView.getScrollPaneForCardMoreInfo();
                scrollPaneForCardMoreInfo.setPannable(true);
                scrollPaneForCardMoreInfo.setLayoutY(battleFieldView.getUpperLeftY() + 486 + 30 + 30 + 60);
                if (cardView.getCard().getCardType().equals(CardType.MONSTER)) {
                    scrollPaneForCardMoreInfo.setMaxHeight(battleFieldView.getHeight() - (486 + 30 + 20 + 70));
                    scrollPaneForCardMoreInfo.setLayoutY(battleFieldView.getUpperLeftY() + 486 + 30 + 30 + 60 + 10);
                } else {
                    scrollPaneForCardMoreInfo.setMaxHeight(battleFieldView.getHeight() - (486 + 30 + 30 + 60 + 10));

                    scrollPaneForCardMoreInfo.setLayoutY(battleFieldView.getUpperLeftY() + 486 + 30);
                }
                scrollPaneForCardMoreInfo.setLayoutX(0);
                scrollPaneForCardMoreInfo.setMaxWidth(battleFieldView.getUpperLeftX() + 100);
                scrollPaneForCardMoreInfo.setContent(vBox);

            }
        });
    }
}
