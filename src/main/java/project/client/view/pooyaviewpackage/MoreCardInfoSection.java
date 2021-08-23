package project.client.view.pooyaviewpackage;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
//import project.server.controller.duel.PreliminaryPackage.GameManager;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.CardType;
import project.model.cardData.General.RowOfCardLocation;
import project.model.cardData.MonsterCardData.MonsterCard;
import project.model.cardData.MonsterCardData.MonsterCardAttribute;
import project.client.modelsforview.BattleFieldView;
import project.client.modelsforview.CardView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MoreCardInfoSection {

    public void updateCardMoreInfoSection(CardView cardView, String description) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                int fakeTurn = Integer.parseInt(JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getFakeTurn()"));
                CardLocation cardLocation = DuelView.getControllerForView().giveCardLocationByCoordinateInView(null, cardView);
                boolean isForAlly = cardLocation.getRowOfCardLocation().toString().startsWith("ALLY") && (fakeTurn == 1) ||
                    cardLocation.getRowOfCardLocation().toString().startsWith("OPPO") && (fakeTurn == 2);
                String token = DuelView.getToken();
                int belongingTurn = Integer.parseInt(JsonCreator.getResult("give my actual turn"));
                CardLocation realCardLocation = null;
                if (belongingTurn == 2) {
                    if (cardLocation.getRowOfCardLocation().toString().contains("HAND") ||
                        cardLocation.getRowOfCardLocation().toString().contains("GRAVEYARD") ||
                        cardLocation.getRowOfCardLocation().toString().contains("SPELL_FIELD") ||
                        cardLocation.getRowOfCardLocation().toString().contains("DECK")) {
                        RowOfCardLocation rowOfCardLocation = null;
                        if (cardLocation.getRowOfCardLocation().toString().contains("ALLY")) {
                            System.out.println("CASE 1");
                            rowOfCardLocation = RowOfCardLocation.valueOf(cardLocation.getRowOfCardLocation().toString().replaceAll("ALLY", "OPPONENT"));
                        } else if (cardLocation.getRowOfCardLocation().toString().contains("OPPONENT")) {
                            System.out.println("CASE 2");
                            rowOfCardLocation = RowOfCardLocation.valueOf(cardLocation.getRowOfCardLocation().toString().replaceAll("OPPONENT", "ALLY"));
                        }
                        realCardLocation = new CardLocation(rowOfCardLocation, cardLocation.getIndex());
                    } else {
                        RowOfCardLocation rowOfCardLocation = null;
                        if (cardLocation.getRowOfCardLocation().toString().contains("ALLY")) {
                            System.out.println("CASE 3");
                            rowOfCardLocation = RowOfCardLocation.valueOf(cardLocation.getRowOfCardLocation().toString().replaceAll("ALLY", "OPPONENT"));
                        } else if (cardLocation.getRowOfCardLocation().toString().contains("OPPONENT")) {
                            System.out.println("CASE 4");
                            rowOfCardLocation = RowOfCardLocation.valueOf(cardLocation.getRowOfCardLocation().toString().replaceAll("OPPONENT", "ALLY"));
                        }
                        System.out.println("CASE 5");
                        realCardLocation = new CardLocation(rowOfCardLocation, 6 - cardLocation.getIndex());
                    }
                } else {
                    realCardLocation = cardLocation;
                }
                System.out.println("FINALLYYYY: realCardLocation = "+realCardLocation.getRowOfCardLocation() + " "+realCardLocation.getIndex());
                Rectangle cardImageForCardMoreInfo = DuelView.getCardImageForCardMoreInfo();
                cardImageForCardMoreInfo.setViewOrder(1);
                BattleFieldView battleFieldView = DuelView.getBattleFieldView();
                CardLocation cardLocationSelecting = DuelView.getCardLocationSelecting();
                Label cardNameForCardMoreInfo = DuelView.getCardNameForCardMoreInfo();
                cardNameForCardMoreInfo.setViewOrder(1);
                Label cardAttackForCardMoreInfo = DuelView.getCardAttackForCardMoreInfo();
                cardAttackForCardMoreInfo.setViewOrder(1);
                Label cardDefenseForCardMoreInfo = DuelView.getCardDefenseForCardMoreInfo();
                cardDefenseForCardMoreInfo.setViewOrder(1);
                Rectangle cardAttributeForCardMoreInfo = DuelView.getCardAttributeForCardMoreInfo();
                cardAttributeForCardMoreInfo.setViewOrder(1);
                Label cardLevelForCardMoreInfo = DuelView.getCardLevelForCardMoreInfo();
                cardLevelForCardMoreInfo.setViewOrder(1);
                Rectangle cardLevelStarForCardMoreInfo = DuelView.getCardLevelStarForCardMoreInfo();
                cardLevelStarForCardMoreInfo.setViewOrder(1);
                Label cardFamilyForCardMoreInfo = DuelView.getCardFamilyForCardMoreInfo();
                cardFamilyForCardMoreInfo.setViewOrder(1);
                ScrollPane scrollPaneForCardMoreInfo = DuelView.getScrollPaneForCardMoreInfo();
                scrollPaneForCardMoreInfo.setViewOrder(1);
                VBox vBox = DuelView.getvBox();
                vBox.setViewOrder(1);
                if (!cardView.isCanBeSeen() && !isForAlly) {
                    cardImageForCardMoreInfo.setFill(new ImagePattern(new Image(MoreCardInfoSection.class.getResource("/project/cards/card_back.png").toExternalForm())));
                    cardImageForCardMoreInfo.setLayoutY(battleFieldView.getUpperLeftY());
                    cardImageForCardMoreInfo.setLayoutY(battleFieldView.getUpperLeftY());
                    cardImageForCardMoreInfo.setLayoutX(0);
                    cardImageForCardMoreInfo.setWidth(300);
                    cardImageForCardMoreInfo.setHeight(436);

                    cardNameForCardMoreInfo.setText("");
                    cardNameForCardMoreInfo.setMaxHeight(0);
                    cardNameForCardMoreInfo.setFont(new Font(0));
                    cardNameForCardMoreInfo.setLayoutX(0);
                    cardNameForCardMoreInfo.setLayoutY(0);

                    cardLevelForCardMoreInfo.setText("");
                    cardLevelForCardMoreInfo.setFont(new Font(0));
                    cardLevelForCardMoreInfo.setLayoutY(0);

                    //cardLevelStarForCardMoreInfo.setFill(new ImagePattern(new Image(DuelView.class.getResource("/project/cardicons/star.png").toExternalForm())));
                    cardLevelStarForCardMoreInfo.setLayoutX(0);
                    cardLevelStarForCardMoreInfo.setLayoutY(0);
                    cardLevelStarForCardMoreInfo.setHeight(0);
                    cardLevelStarForCardMoreInfo.setWidth(0);

                    //String monsterCardFamily = ((MonsterCard) cardView.getCard()).getMonsterCardFamily().toString();
                    cardFamilyForCardMoreInfo.setText("");
                    cardFamilyForCardMoreInfo.setMaxHeight(0);
                    cardFamilyForCardMoreInfo.setFont(new Font(0));
                    cardFamilyForCardMoreInfo.setLayoutX(0);
                    cardFamilyForCardMoreInfo.setLayoutY(0);

                    cardAttackForCardMoreInfo.setFont(new Font(0));
                    cardAttackForCardMoreInfo.setLayoutX(0);
                    cardAttackForCardMoreInfo.setLayoutY(0);
                    cardAttackForCardMoreInfo.setMaxWidth(0);

                    cardDefenseForCardMoreInfo.setFont(new Font(0));
                    cardDefenseForCardMoreInfo.setLayoutX(0);
                    cardDefenseForCardMoreInfo.setLayoutY(0);
                    cardDefenseForCardMoreInfo.setMaxWidth(0);

                    cardAttributeForCardMoreInfo.setLayoutY(0);
                    cardAttributeForCardMoreInfo.setLayoutX(0);
                    cardAttributeForCardMoreInfo.setLayoutY(0);
                    cardAttributeForCardMoreInfo.setWidth(0);
                    cardAttributeForCardMoreInfo.setHeight(0);

                    scrollPaneForCardMoreInfo.setMaxHeight(0);
                    scrollPaneForCardMoreInfo.setMinHeight(0);
                    scrollPaneForCardMoreInfo.setLayoutY(0);
                    scrollPaneForCardMoreInfo.setLayoutX(0);
                    scrollPaneForCardMoreInfo.setMaxWidth(0);
                    scrollPaneForCardMoreInfo.setMinWidth(0);
                    vBox.getChildren().clear();
                    scrollPaneForCardMoreInfo.setContent(vBox);

                } else {
                    cardImageForCardMoreInfo.setFill(cardView.giveImageVeryNormally());


                    vBox.getChildren().clear();
                    cardImageForCardMoreInfo.setLayoutY(battleFieldView.getUpperLeftY());
                    cardImageForCardMoreInfo.setLayoutY(battleFieldView.getUpperLeftY());
                    cardImageForCardMoreInfo.setLayoutX(0);
                    cardImageForCardMoreInfo.setWidth(300);
                    cardImageForCardMoreInfo.setHeight(436);

                    String nameOfThisCard = cardView.getCard().getCardName();
                    cardNameForCardMoreInfo.setMaxHeight(80);
                    if (nameOfThisCard.length() <= 21) {
                        cardNameForCardMoreInfo.setFont(new Font(30));
                    } else {
                        cardNameForCardMoreInfo.setFont(new Font(18));
                    }
                    cardNameForCardMoreInfo.setLayoutX(0);
                    cardNameForCardMoreInfo.setLayoutY(436 + battleFieldView.getUpperLeftY());


                    if (cardView.getCard().getCardType().equals(CardType.MONSTER)) {
                        cardNameForCardMoreInfo.setText(" " + cardView.getCard().getCardName());

                        if (cardLocationSelecting == null) {
                            JsonCreator.setFirstAdditionalString(realCardLocation.getRowOfCardLocation().toString().replaceAll("RowOfCardLocation.", ""));
                            JsonCreator.setIntegerString(realCardLocation.getIndex() + "");
                            String attackInput = JsonCreator.getResult("MonsterCard.giveATKDEFConsideringEffects(\"attack\", new CardLocation(rowOfCardLocation, index), token)");
                            System.out.println("attack input = " + attackInput);
                            cardAttackForCardMoreInfo.setText(" ATK : " + attackInput);
                            JsonCreator.setIntegerString(realCardLocation.getIndex() + "");
                            JsonCreator.setFirstAdditionalString(realCardLocation.getRowOfCardLocation().toString().replaceAll("RowOfCardLocation.", ""));
                            cardDefenseForCardMoreInfo.setText(" DEF : " +
                                JsonCreator.getResult("MonsterCard.giveATKDEFConsideringEffects(\"defense\", new CardLocation(rowOfCardLocation, index), token)"));
                        } else {
                            String rowString = realCardLocation.getRowOfCardLocation().toString();
                            if (rowString.startsWith("RowOfCardLocation.")) {
                                rowString = rowString.replaceAll("RowOfCardLocation.", "");
                            }
                            JsonCreator.setFirstAdditionalString(rowString);
                            JsonCreator.setIntegerString(realCardLocation.getIndex() + "");
                            String attackInput = JsonCreator.getResult("MonsterCard.giveATKDEFConsideringEffects(\"attack\", new CardLocation(rowOfCardLocation, index), token)");
                            System.out.println("attack input = " + attackInput);
                            cardAttackForCardMoreInfo.setText(" ATK : " + attackInput);
                            String defenseInput = JsonCreator.getResult("MonsterCard.giveATKDEFConsideringEffects(\"defense\", new CardLocation(rowOfCardLocation, index), token)");
                            System.out.println("defense input = " + defenseInput);
                            cardDefenseForCardMoreInfo.setText(" DEF : " + defenseInput);

                        }
                        cardLevelForCardMoreInfo.setText(" LVL: " + ((MonsterCard) cardView.getCard()).getLevel());
                        cardLevelForCardMoreInfo.setFont(new Font(22));
                        cardLevelForCardMoreInfo.setLayoutY(486 + battleFieldView.getUpperLeftY());

                        cardLevelStarForCardMoreInfo.setFill(new ImagePattern(new Image(DuelView.class.getResource("/project/cardicons/star.png").toExternalForm())));
                        cardLevelStarForCardMoreInfo.setLayoutX(80);
                        cardLevelStarForCardMoreInfo.setLayoutY(486 + battleFieldView.getUpperLeftY() + 5);
                        cardLevelStarForCardMoreInfo.setHeight(20);
                        cardLevelStarForCardMoreInfo.setWidth(20);

                        String monsterCardFamily = ((MonsterCard) cardView.getCard()).getMonsterCardFamily().toString();
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


                    } else {

                        cardFamilyForCardMoreInfo.setText("");
                        cardFamilyForCardMoreInfo.setMaxHeight(0);
                        cardFamilyForCardMoreInfo.setFont(new Font(0));
                        cardFamilyForCardMoreInfo.setLayoutX(0);
                        cardFamilyForCardMoreInfo.setLayoutY(0);

                        cardAttackForCardMoreInfo.setFont(new Font(0));
                        cardAttackForCardMoreInfo.setLayoutX(0);
                        cardAttackForCardMoreInfo.setLayoutY(0);
                        cardAttackForCardMoreInfo.setMaxWidth(0);
                        cardAttackForCardMoreInfo.setMaxHeight(0);

                        cardDefenseForCardMoreInfo.setFont(new Font(0));
                        cardDefenseForCardMoreInfo.setLayoutX(0);
                        cardDefenseForCardMoreInfo.setLayoutY(0);
                        cardDefenseForCardMoreInfo.setMaxWidth(0);
                        cardDefenseForCardMoreInfo.setMaxHeight(0);

                        cardLevelForCardMoreInfo.setText("");
                        cardLevelForCardMoreInfo.setFont(new Font(0));
                        cardLevelForCardMoreInfo.setLayoutY(0);

                        //cardLevelStarForCardMoreInfo.setFill(new ImagePattern(new Image(DuelView.class.getResource("/project/cardicons/star.png").toExternalForm())));
                        cardLevelStarForCardMoreInfo.setLayoutX(0);
                        cardLevelStarForCardMoreInfo.setLayoutY(0);
                        cardLevelStarForCardMoreInfo.setHeight(0);
                        cardLevelStarForCardMoreInfo.setWidth(0);


                        if (cardView.getCard().getCardType().equals(CardType.SPELL)) {
                            cardNameForCardMoreInfo.setText(" " + cardView.getCard().getCardName());
                            cardAttributeForCardMoreInfo.setFill(new ImagePattern(new Image(DuelView.class.getResource("/project/cardicons/spell.png").toExternalForm())));
                            cardAttributeForCardMoreInfo.setLayoutY(486 + battleFieldView.getUpperLeftY());
                        } else {
                            cardNameForCardMoreInfo.setText(" " + cardView.getCard().getCardName());
                            cardAttributeForCardMoreInfo.setFill(new ImagePattern(new Image(DuelView.class.getResource("/project/cardicons/trap.png").toExternalForm())));
                            cardAttributeForCardMoreInfo.setLayoutY(486 + battleFieldView.getUpperLeftY());
                        }

                    }
                    cardAttributeForCardMoreInfo.setLayoutX(battleFieldView.getUpperLeftX() / 2 - 30);
                    cardAttributeForCardMoreInfo.setWidth(60);
                    cardAttributeForCardMoreInfo.setHeight(60);


                    List<String> shortCardDescription = new ArrayList<>();
                    System.out.println("CARD DESCRIPTION IS " + description);
                    shortCardDescription = Arrays.asList(description.split(" "));
                    StringBuilder sentencesForEachLabel = new StringBuilder();
                    int numberOFLabelUsed = 0;
                    Label label;
                    int numberOfCharactersPerLine = 30;
                    int fontOfCharacters = 18;
                    for (int i = 0; i < shortCardDescription.size(); i++) {
                        label = new Label();
                        label.setFont(new Font(fontOfCharacters));

                        if (sentencesForEachLabel.length() <= numberOfCharactersPerLine + 1 - shortCardDescription.get(i).length()) {
                            sentencesForEachLabel.append(shortCardDescription.get(i) + " ");
                            label.setLayoutY(fontOfCharacters * (numberOFLabelUsed + 1));
                            // numberOFLabelUsed++;
                        } else {
                            while (sentencesForEachLabel.length() <= numberOfCharactersPerLine) {
                                sentencesForEachLabel.append(" ");
                            }
                            label.setText(sentencesForEachLabel.toString());
                            //label.setLayoutX(100);
                            label.setLayoutY(fontOfCharacters * (numberOFLabelUsed + 1));
                            vBox.getChildren().add(label);
                            sentencesForEachLabel.setLength(0);
                            sentencesForEachLabel.append(shortCardDescription.get(i) + " ");
                            numberOFLabelUsed++;
                        }
                    }
                    while (sentencesForEachLabel.length() <= numberOfCharactersPerLine) {
                        sentencesForEachLabel.append(" ");
                    }
                    label = new Label();
                    label.setFont(new Font(fontOfCharacters));
                    label.setText(sentencesForEachLabel.toString());
                    //label.setLayoutX(100);
                    label.setLayoutY(fontOfCharacters * (numberOFLabelUsed + 1));
                    vBox.getChildren().add(label);
                    sentencesForEachLabel.setLength(0);
                    int realNumberOfLines = vBox.getChildren().size();
                    while (vBox.getChildren().size() < 4) {
                        vBox.getChildren().add(new Label(" "));
                    }
                    numberOFLabelUsed++;

                    scrollPaneForCardMoreInfo.setPannable(true);
                    // scrollPaneForCardMoreInfo.setLayoutY(battleFieldView.getUpperLeftY() + 486 + 30 + 30 + 60);
                    if (cardView.getCard().getCardType().equals(CardType.MONSTER)) {
                        scrollPaneForCardMoreInfo.setMaxHeight(battleFieldView.getHeight() - (486 + 30 + 20 + 70) - 10);
                        scrollPaneForCardMoreInfo.setMinHeight(battleFieldView.getHeight() - (486 + 30 + 20 + 70) - 10);
                        scrollPaneForCardMoreInfo.setLayoutY(battleFieldView.getUpperLeftY() + 486 + 30 + 30 + 60 + 10);
                        while (5 * fontOfCharacters > Math.floor(fontOfCharacters * realNumberOfLines + (fontOfCharacters / 1.8) * (vBox.getChildren().size() - realNumberOfLines))) {
                            vBox.getChildren().add(new Label(" "));
                        }
                    } else {
                        scrollPaneForCardMoreInfo.setMaxHeight(battleFieldView.getHeight() - (486 + 30 + 30 + 60 + 10) + 30 + 10);
                        scrollPaneForCardMoreInfo.setMinHeight(battleFieldView.getHeight() - (486 + 30 + 30 + 60 + 10) + 30 + 10);
                        scrollPaneForCardMoreInfo.setLayoutY(battleFieldView.getUpperLeftY() + 486 + 30 + 60);
                        while (6.5 * fontOfCharacters > Math.floor(fontOfCharacters * realNumberOfLines + (fontOfCharacters / 1.8) * (vBox.getChildren().size() - realNumberOfLines))) {
                            vBox.getChildren().add(new Label(" "));
                        }
                    }

                    scrollPaneForCardMoreInfo.setLayoutX(0);
                    scrollPaneForCardMoreInfo.setMaxWidth(battleFieldView.getUpperLeftX());
                    scrollPaneForCardMoreInfo.setMinWidth(battleFieldView.getUpperLeftX());
                    scrollPaneForCardMoreInfo.setContent(vBox);

                }
            }
        });

    }
}
