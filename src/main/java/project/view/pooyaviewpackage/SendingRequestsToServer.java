package project.view.pooyaviewpackage;

import javafx.scene.control.Alert;
import javafx.stage.Stage;
import project.controller.duel.PreliminaryPackage.GameManager;
import project.controller.duel.Utility.Utility;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.CardPosition;
import project.model.cardData.General.RowOfCardLocation;
import project.model.modelsforview.CardView;
import project.view.pooyaviewpackage.DuelView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SendingRequestsToServer {

    public void sendNormalSummoningRequestToServer(CardView cardView, CardLocation cardLocationSelecting, DuelView duelView) {
        System.out.println();
        System.out.println();
        System.out.println("select " + giveStringToGiveToServerByCardLocation(cardLocationSelecting));
        String output = GameManager.getDuelControllerByIndex(0).getInput("select " + giveStringToGiveToServerByCardLocation(cardLocationSelecting), true);
        //String output = GameManager.getDuelControllerByIndex(0).getInput("card show --selected", true);
        System.out.println("&" + output);
        DuelView.getControllerForView().getFinalCardLocationOfCurrentCardBeforeServer(cardView);
        output = GameManager.getDuelControllerByIndex(0).getInput("normal summon", true);
        System.out.println("***Normal Summoning Message from server received is : " + output);
        if (output.contains("successfully")) {


            DuelView.getAdvancedCardMovingController().advanceForwardBattleField();

            if (output.contains("do you")) {
                String doYouWantToActivate = "(o you want to activate your monster card's effect?)";
                Pattern doYouWantToActivatePattern = Pattern.compile(doYouWantToActivate);
                Matcher matcherForDoYouWantToActivate = doYouWantToActivatePattern.matcher(output);
                String nowItWillBeString = "now it will be (\\S+)'s turn";
                Pattern nowItWillBePattern = Pattern.compile(nowItWillBeString);
                Matcher match = nowItWillBePattern.matcher(output);
                String nowItWillBeTurn = "";
                System.out.println("WHOUUUUUUU");
                boolean isTrue = false;
                if (match.find()) {
                    nowItWillBeTurn = output.substring(match.start(), match.end());
                    DuelView.getShowOptionsToUser().doYouWantToAlert(nowItWillBeTurn + "\nDo you want to activate your trap or spell?");
                    isTrue = true;
                    // System.out.println("Found love at index "+ match.start() +" - "+ (match.end()-1));
                }
                if (!match.find() && isTrue) {
                    System.out.println("&*&*&*&*&* YOU MUST SCATTER IF YOU SEE THIS MESSAGE");
                }
                if (matcherForDoYouWantToActivate.find() && !match.find()) {
                    String stringShownToUser = matcherForDoYouWantToActivate.group(1);
                    DuelView.getShowOptionsToUser().doYouWantToAlert("D" + stringShownToUser);

                }
                System.out.println(nowItWillBeTurn + "p");
            }
            //System.out.println("Am i really summoning");
            //    DuelView.getTransition().applyTransitionForSummoningMonsterCard(cardView).play();
            //alone


        } else {
            DuelView.setCardLocationToSendCardTo(null);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Normal Summoning Message");
            alert.setContentText(output);
            alert.showAndWait();

        }
    }

    public void sendTributeSummoningRequestToServer(CardView cardView, CardLocation cardLocationSelecting, DuelView duelView) {
        String output = GameManager.getDuelControllerByIndex(0).getInput("select " + giveStringToGiveToServerByCardLocation(cardLocationSelecting), true);
        //String output = GameManager.getDuelControllerByIndex(0).getInput("card show --selected", true);
        System.out.println("&" + output);
        DuelView.getControllerForView().getFinalCardLocationOfCurrentCardBeforeServer(cardView);
        output = GameManager.getDuelControllerByIndex(0).getInput("tribute summon", true);
        if (output.contains("successfully")) {


            DuelView.getAdvancedCardMovingController().advanceForwardBattleField();


            //        DuelView.getTransition().applyTransitionForSummoningMonsterCard(cardView).play();
            //alone


        } else {
            DuelView.setCardLocationToSendCardTo(null);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Tribute Summoning Message");
            alert.setContentText(output);
            alert.showAndWait();
        }
    }

    public void sendSpecialSummoningRequestToServer(CardView cardView, CardLocation cardLocationSelecting, DuelView duelView) {
        String output = GameManager.getDuelControllerByIndex(0).getInput("select " + giveStringToGiveToServerByCardLocation(cardLocationSelecting), true);
        //String output = GameManager.getDuelControllerByIndex(0).getInput("card show --selected", true);
        System.out.println("&" + output);
        DuelView.getControllerForView().getFinalCardLocationOfCurrentCardBeforeServer(cardView);
        output = GameManager.getDuelControllerByIndex(0).getInput("special summon", true);
        System.out.println("*" + output);
        if (output.contains("successfully")) {


            DuelView.getAdvancedCardMovingController().advanceForwardBattleField();


            //      DuelView.getTransition().applyTransitionForSummoningMonsterCard(cardView).play();
            //alone


        } else {
            DuelView.setCardLocationToSendCardTo(null);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Special Summoning Message");
            alert.setContentText(output);
            alert.showAndWait();
        }
    }

    public void sendShowGraveyardRequestToServer(CardView cardView, CardLocation cardLocationSelecting, DuelView duelView) {
        String output = GameManager.getDuelControllerByIndex(0).getInput("show graveyard", true);
        //String output = GameManager.getDuelControllerByIndex(0).getInput("card show --selected", true);
        System.out.println("&" + output);
        CardLocation cardLocation = DuelView.getControllerForView().giveCardLocationByCoordinateInView(null, cardView);
        DuelView.setIsAllySeeingGraveyard(cardLocation.getRowOfCardLocation().toString().startsWith("ALLY"));
        //DuelView.getControllerForView().getFinalCardLocationOfCurrentCardBeforeServer(cardView);
        //output = GameManager.getDuelControllerByIndex(0).getInput("ritual summon", true);

        //System.out.println("*\n" + );
        DuelView.setGraveyardString(output);
        new GraveyardScene().start(new Stage());
//        if (output.contains("successfully")) {
//
//
//            //   DuelView.getTransition().applyTransitionForSummoningMonsterCard(cardView).play();
//            //alone
//
//
//        } else {
//            DuelView.setCardLocationToSendCardTo(null);
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Information Dialog");
//            alert.setHeaderText("Ritual Summoning Message");
//            alert.setContentText(output);
//            alert.showAndWait();
//        }
    }

    public void sendSettingRequestToServer(CardView cardView, CardLocation cardLocationSelecting) {
        String output = GameManager.getDuelControllerByIndex(0).getInput("select " + giveStringToGiveToServerByCardLocation(cardLocationSelecting), true);
        //String output = GameManager.getDuelControllerByIndex(0).getInput("card show --selected", true);
        System.out.println("&&&&" + output);
        DuelView.getControllerForView().getFinalCardLocationOfCurrentCardBeforeServer(cardView);
        output = GameManager.getDuelControllerByIndex(0).getInput("set", true);
        System.out.println("*" + output);
        if (output.contains("successfully")) {


            DuelView.getAdvancedCardMovingController().advanceForwardBattleField();


            //  DuelView.getTransition().applyTransitionForSettingCard(cardView);


        } else {
            DuelView.setCardLocationToSendCardTo(null);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Setting Card Message");
            alert.setContentText(output);
            alert.showAndWait();
        }
    }

    public void sendActivateEffectRequestToServer(CardView cardView, CardLocation cardLocationSelecting) {
        String output = GameManager.getDuelControllerByIndex(0).getInput("select " + giveStringToGiveToServerByCardLocation(cardLocationSelecting), true);
        //String output = GameManager.getDuelControllerByIndex(0).getInput("card show --selected", true);
        System.out.println("&" + output);
        boolean isMonsterActivating = false;
        boolean isSpellTrapActivatingFromSpellZone = false;
        boolean isSpellActivatingFromHandZone = false;
        if (cardLocationSelecting.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE) || cardLocationSelecting.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) {
            isMonsterActivating = true;
        }
        if (cardLocationSelecting.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_SPELL_ZONE) || cardLocationSelecting.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_SPELL_ZONE)
            || cardLocationSelecting.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE) || cardLocationSelecting.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE)) {
            isSpellTrapActivatingFromSpellZone = true;
        }
        if (cardLocationSelecting.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_HAND_ZONE) || cardLocationSelecting.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_HAND_ZONE)) {
            isSpellActivatingFromHandZone = true;
        }
        if (isSpellActivatingFromHandZone) {
            DuelView.getControllerForView().getFinalCardLocationOfCurrentCardBeforeServer(cardView);
        }
        //DuelView.getAdvancedCardMovingController().getWholeBoardInformationFromServer();
        //DuelView.getAdvancedCardMovingController().putValuesInCardAndCardViewHashMap();
        output = GameManager.getDuelControllerByIndex(0).getInput("activate effect", true);
        System.out.println("*" + output);
        if (output.contains("activated")) {


            DuelView.getAdvancedCardMovingController().advanceForwardBattleField();


            //DuelView.getAdvancedCardMovingController().getWholeBoardInformationFromServer();
            //DuelView.getAdvancedCardMovingController().prepareDeletionsAndAdditions();
            //DuelView.getAdvancedCardMovingController().refreshWholeBattleField();

//
//            if (isSpellActivatingFromHandZone) {
//
//
//                DuelView.getTransition().applyTransitionForActivatingSpellTrapSuper(cardView);
//                //alone
//
//
//            } else if (isSpellTrapActivatingFromSpellZone) {
//                DuelView.getTransition().applyTransitionForActivatingSpellTrapInSpellZoneSuper(cardView);
//            }
//


        } else {
            //  DuelView.getAdvancedCardMovingController().getWholeBoardInformationFromServer();
            DuelView.setCardLocationToSendCardTo(null);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Activate Effect Message");
            alert.setContentText(output);
            alert.showAndWait();
        }
    }

    public void sendAttackMonsterToMonsterRequestToServer(CardLocation cardLocationSelecting, CardLocation finalCardLocation, int yugiohIndex) {
        System.out.println("ZENOS IS ATTACKING MONSTER TO MONSTER :select " + giveStringToGiveToServerByCardLocation(cardLocationSelecting));
        String output = GameManager.getDuelControllerByIndex(0).getInput("select " + giveStringToGiveToServerByCardLocation(cardLocationSelecting), true);
        //String output = GameManager.getDuelControllerByIndex(0).getInput("card show --selected", true);
        System.out.println("&&&&" + output);
        //controllerForView.getFinalCardLocationOfCurrentCardBeforeServer(cardView);
        int yugiohOpponentMonsterIndex;
        if (finalCardLocation == null) {
            if (cardLocationSelecting.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) {
                finalCardLocation = new CardLocation(RowOfCardLocation.OPPONENT_MONSTER_ZONE, Utility.changeYuGiOhIndexToArrayIndex(yugiohIndex, RowOfCardLocation.OPPONENT_MONSTER_ZONE));
            } else if (cardLocationSelecting.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) {
                finalCardLocation = new CardLocation(RowOfCardLocation.ALLY_MONSTER_ZONE, Utility.changeYuGiOhIndexToArrayIndex(yugiohIndex, RowOfCardLocation.ALLY_MONSTER_ZONE));
            }
            yugiohOpponentMonsterIndex = yugiohIndex;
        } else {
            yugiohOpponentMonsterIndex = changeArrayIndexFromOneToFiveToYuGiOhIndex(finalCardLocation.getIndex(), finalCardLocation.getRowOfCardLocation());
        }
        System.out.println("ZENOS IS HUNTING :select " + giveStringToGiveToServerByCardLocation(finalCardLocation));
        System.out.println("yugioh index is " + yugiohOpponentMonsterIndex);
        int turn = GameManager.getDuelControllerByIndex(0).getTurn();
        CardView beingAttackedCardView = DuelView.getControllerForView().getCardViewByCardLocation(finalCardLocation);
        CardView attackerCardView = DuelView.getControllerForView().getCardViewByCardLocation(cardLocationSelecting);
        output = GameManager.getDuelControllerByIndex(0).getInput("attack " + yugiohOpponentMonsterIndex, true);
        System.out.println("*" + output);
        if (output.contains("now it will") && output.contains("do you")) {
            String wordToFind = "now it will be (\\S+)'s turn";
            Pattern word = Pattern.compile(wordToFind);
            Matcher match = word.matcher(output);
            String nowItWillBeTurn = "";
            System.out.println("WHOUUUUUUU");
            if (match.find()) {
                nowItWillBeTurn = output.substring(match.start(), match.end());
                // System.out.println("Found love at index "+ match.start() +" - "+ (match.end()-1));
            }
            System.out.println(nowItWillBeTurn + "p");
            DuelView.getShowOptionsToUser().doYouWantToAlert(nowItWillBeTurn + "\nDo you want to activate your trap or spell?");
        } else {

            DuelView.getAdvancedCardMovingController().advanceForwardBattleField();
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Information Dialog");
//            alert.setHeaderText("Result Message");
//            alert.setContentText(output);
//            alert.showAndWait();
        }


//
//        DuelView.setCardLocationToSendCardTo(null);
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Information Dialog");
//        alert.setHeaderText("Attack Monster To Monster Message");
//        alert.setContentText(output);
//        alert.showAndWait();
//        boolean opponentMonsterDestroyed = output.contains("your opponent's monster is destroyed") || output.contains("both you and your opponent monster cards are destroyed");
//        boolean opponentMonsterDestroyedInDefensePosition = output.contains("the defense position monster is destroyed") || output.contains("both you and your opponent monster cards are destroyed");

//        if (opponentMonsterDestroyed || opponentMonsterDestroyedInDefensePosition) {
//            if (!beingAttackedCardView.isCanBeSeen()) {
//                DuelView.getTransition().flipCardBackAndForthConsideringCardImage(beingAttackedCardView, true, 100);
//            }
//            DestroyCardTransition destroyCardTransition;
//            if (opponentMonsterDestroyed) {
//                destroyCardTransition = new DestroyCardTransition(beingAttackedCardView, false);
//            } else {
//                destroyCardTransition = new DestroyCardTransition(beingAttackedCardView, true);
//            }
//            destroyCardTransition.setOnFinished(new EventHandler<ActionEvent>() {
//                @Override
//                public void handle(ActionEvent actionEvent) {
//                    DuelView.getControllerForView().changeLabelOfCardForSendingCardToGraveyardZone(destroyCardTransition.getCardView());
//                    if (turn == 2) {
//                        destroyCardTransition.getCardView().setTranslateX(-1);
//                        destroyCardTransition.getCardView().setTranslateY(100);
//                        destroyCardTransition.getCardView().setTranslateY(-130);
//                        //destroyCardTransition.getCardView().setX(DuelView.getBattleFieldView().getUpperLeftX() + DuelView.getBattleFieldView().getWidth() - CardView.getCardWidth() + 3 * (CardView.getCardWidth() + 20.5) - 5);
//                        //destroyCardTransition.getCardView().setY(510 + 123 - 5 - 3);
//                    } else {
//                        destroyCardTransition.getCardView().setTranslateX(1);
//                        destroyCardTransition.getCardView().setTranslateY(-100);
//                        destroyCardTransition.getCardView().setTranslateY(130);
//                        //destroyCardTransition.getCardView().setX(DuelView.getBattleFieldView().getUpperLeftX() + 100 - CardView.getCardWidth());
//                        //destroyCardTransition.getCardView().setY(410 - 40 - CardView.getCardHeight());
//                    }
//                    if (opponentMonsterDestroyedInDefensePosition) {
//                        RotateTransition rotateTransition = DuelView.getTransition().rotateCardMinusNintyDegreesVeryQuickly(destroyCardTransition.getCardView());
//                        rotateTransition.play();
//                        rotateTransition.setOnFinished(new EventHandler<ActionEvent>() {
//                            @Override
//                            public void handle(ActionEvent actionEvent) {
//                                destroyCardTransition.getCardView().setShouldBeInvisible(false);
//                            }
//                        });
//                    } else {
//                        destroyCardTransition.getCardView().setShouldBeInvisible(false);
//                    }
//                    //controllerForView.sendCardToGraveyardZone(destroyCardTransition.getCardView()).play();
//                    Bounds bounds = destroyCardTransition.getCardView().localToScene(destroyCardTransition.getCardView().getBoundsInLocal());
//                    System.out.println("whrere is our card x = " + bounds.getMinX() + " y = " + bounds.getMinY());
//
//                }
//            });
//            destroyCardTransition.play();
//        }
//        if (output.contains("your monster card is destroyed") || output.contains("both you and your opponent monster cards are destroyed")) {
//            DestroyCardTransition destroyCardTransition = new DestroyCardTransition(attackerCardView, false);
//            destroyCardTransition.setOnFinished(new EventHandler<ActionEvent>() {
//                @Override
//                public void handle(ActionEvent actionEvent) {
//                    DuelView.getControllerForView().changeLabelOfCardForSendingCardToGraveyardZone(destroyCardTransition.getCardView());
//                    if (turn == 1) {
//                        //TranslateTransition translateTransition = DuelView.getControllerForView().sendCardToGraveyardZone(destroyCardTransition.getCardView());
//                        //translateTransition.play();
//                        //DuelView.getControllerForView().sendCardToGraveyardZone(destroyCardTransition.getCardView()).play();
//                        destroyCardTransition.getCardView().setTranslateX(-1);
//                        destroyCardTransition.getCardView().setTranslateY(100);
//                        destroyCardTransition.getCardView().setTranslateY(-130);
//                        //destroyCardTransition.getCardView().setMouseTransparent(false);
//                    } else {
//                        destroyCardTransition.getCardView().setTranslateX(1);
//                        destroyCardTransition.getCardView().setTranslateY(-100);
//                        destroyCardTransition.getCardView().setTranslateY(130);
//                    }
//
//                    //controllerForView.sendCardToGraveyardZone(destroyCardTransition.getCardView()).play();
//                    Bounds bounds = destroyCardTransition.getCardView().localToScene(destroyCardTransition.getCardView().getBoundsInLocal());
//                    System.out.println("whrere is our card x = " + bounds.getMinX() + " y = " + bounds.getMinY());
//
//                    destroyCardTransition.getCardView().setShouldBeInvisible(false);
//                }
//            });
//            destroyCardTransition.play();
//        }
    }

    public void sendAttackDirectRequestToServer(CardLocation cardLocationSelecting) {
        String output = GameManager.getDuelControllerByIndex(0).getInput("select " + giveStringToGiveToServerByCardLocation(cardLocationSelecting), true);
        //String output = GameManager.getDuelControllerByIndex(0).getInput("card show --selected", true);
        System.out.println("&&&&" + output);
        //controllerForView.getFinalCardLocationOfCurrentCardBeforeServer(cardView);
        output = GameManager.getDuelControllerByIndex(0).getInput("attack direct", true);
        System.out.println("*" + output);
        if (output.contains("now it will") && output.contains("do you")) {
            String wordToFind = "now it will be (\\S+)'s turn";
            Pattern word = Pattern.compile(wordToFind);
            Matcher match = word.matcher(output);
            String nowItWillBeTurn = "";
            System.out.println("WHOUUUUUUU");
            if (match.find()) {
                nowItWillBeTurn = output.substring(match.start(), match.end());
                // System.out.println("Found love at index "+ match.start() +" - "+ (match.end()-1));
            }
            System.out.println(nowItWillBeTurn + "p");
            DuelView.getShowOptionsToUser().doYouWantToAlert(nowItWillBeTurn + "\nDo you want to activate your trap or spell?");
        } else {

            DuelView.getAdvancedCardMovingController().advanceForwardBattleField();

        }
//
//        if (output.contains("successfully")) {
//
//            DuelView.getAdvancedCardMovingController().advanceForwardBattleField();
//
//
//            //transition.applyTransitionForSettingCard(cardView);
//        } else {
//            DuelView.setCardLocationToSendCardTo(null);
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Information Dialog");
//            alert.setHeaderText("Direct Attack Message");
//            alert.setContentText(output);
//            alert.showAndWait();
//        }
    }

    public void sendFlipSummoningRequestToServer(CardView cardView, CardLocation cardLocationSelecting) {
        System.out.println("mmm " + giveStringToGiveToServerByCardLocation(cardLocationSelecting));
        String output = GameManager.getDuelControllerByIndex(0).getInput("select " + giveStringToGiveToServerByCardLocation(cardLocationSelecting), true);
        System.out.println("&&&&" + output);
        output = GameManager.getDuelControllerByIndex(0).getInput("flip-summon", true);
        System.out.println("*" + output);
        if (!output.equals("")) {
            if (output.contains("successfully")) {
                DuelView.getAdvancedCardMovingController().advanceForwardBattleField();
                //         DuelView.getTransition().applyTransitionForFlipSummoning(cardView);
            } else {
                DuelView.setCardLocationToSendCardTo(null);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Flip Summoning Message");
                alert.setContentText(output);
                alert.showAndWait();
            }
        }
    }

    public void sendChangingCardPositionRequestToServer(CardView cardView, CardLocation cardLocationSelecting) {
        String output = GameManager.getDuelControllerByIndex(0).getInput("select " + giveStringToGiveToServerByCardLocation(cardLocationSelecting), true);
        System.out.println("&&&&" + output);
        CardPosition cardPosition = cardView.getCard().getCardPosition();
        output = "";
        boolean finallyAttackPosition = false;
        if (cardPosition.equals(CardPosition.FACE_UP_ATTACK_POSITION)) {
            output = GameManager.getDuelControllerByIndex(0).getInput("set --position defense", true);
        } else if (cardPosition.equals(CardPosition.FACE_UP_DEFENSE_POSITION)) {
            finallyAttackPosition = true;
            output = GameManager.getDuelControllerByIndex(0).getInput("set --position attack", true);
        }
        System.out.println("*" + output);
        if (!output.equals("")) {
            if (output.contains("successfully")) {
                DuelView.getAdvancedCardMovingController().advanceForwardBattleField();
            } else {
                DuelView.setCardLocationToSendCardTo(null);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Change Card Position Message");
                alert.setContentText(output);
                alert.showAndWait();
            }
        }
    }

    public static int changeArrayIndexFromOneToFiveToYuGiOhIndex(int cardIndex, RowOfCardLocation rowOfCardLocation) {
        boolean seeminglyChoosingSelf = rowOfCardLocation.equals(RowOfCardLocation.ALLY_MONSTER_ZONE) || rowOfCardLocation.equals(RowOfCardLocation.ALLY_SPELL_ZONE);
        boolean seeminglyChoosingOther = rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE) || rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_SPELL_ZONE);
        if (seeminglyChoosingSelf) {
            if (cardIndex == 1) {
                return 5;
            } else if (cardIndex == 2) {
                return 3;
            } else if (cardIndex == 3) {
                return 1;
            } else if (cardIndex == 4) {
                return 2;
            }
            return 4;
        } else if (seeminglyChoosingOther) {
            if (cardIndex == 1) {
                return 4;
            } else if (cardIndex == 2) {
                return 2;
            } else if (cardIndex == 3) {
                return 1;
            } else if (cardIndex == 4) {
                return 3;
            }
            return 5;
        }
        return cardIndex;
    }

    public static String giveStringToGiveToServerByCardLocation(CardLocation cardLocation) {
        int turn = GameManager.getDuelControllerByIndex(0).getFakeTurn();
        if (cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_SPELL_ZONE)) {
            if (turn == 1) {
                return "--spell " + changeArrayIndexFromOneToFiveToYuGiOhIndex(cardLocation.getIndex(), cardLocation.getRowOfCardLocation());
            } else {
                return "--opponent --spell " + changeArrayIndexFromOneToFiveToYuGiOhIndex(cardLocation.getIndex(), cardLocation.getRowOfCardLocation());
            }
        } else if (cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) {
            if (turn == 1) {
                return "--monster " + changeArrayIndexFromOneToFiveToYuGiOhIndex(cardLocation.getIndex(), cardLocation.getRowOfCardLocation());
            } else {
                return "--opponent --monster " + changeArrayIndexFromOneToFiveToYuGiOhIndex(cardLocation.getIndex(), cardLocation.getRowOfCardLocation());
            }
        } else if (cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE)) {
            if (turn == 1) {
                return "--field ";
            } else {
                return "--opponent --field ";
            }
        } else if (cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_GRAVEYARD_ZONE)) {
            if (turn == 1) {
                return "--graveyard " + cardLocation.getIndex();
            } else {
                return "--opponent --graveyard " + cardLocation.getIndex();
            }
        } else if (cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_HAND_ZONE)) {
            if (turn == 1) {
                return "--hand " + cardLocation.getIndex();
            } else {
                return "--opponent --hand " + cardLocation.getIndex();
            }
        } else if (cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_DECK_ZONE)) {
            if (turn == 1) {
                return "--deck " + cardLocation.getIndex();
            } else {
                return "--opponent --deck " + cardLocation.getIndex();
            }
        } else if (cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_SPELL_ZONE)) {
            if (turn == 1) {
                return "--opponent --spell " + changeArrayIndexFromOneToFiveToYuGiOhIndex(cardLocation.getIndex(), cardLocation.getRowOfCardLocation());
            } else {
                return "--spell " + changeArrayIndexFromOneToFiveToYuGiOhIndex(cardLocation.getIndex(), cardLocation.getRowOfCardLocation());
            }
        } else if (cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) {
            if (turn == 1) {
                return "--opponent --monster " + changeArrayIndexFromOneToFiveToYuGiOhIndex(cardLocation.getIndex(), cardLocation.getRowOfCardLocation());
            } else {
                return "--monster " + changeArrayIndexFromOneToFiveToYuGiOhIndex(cardLocation.getIndex(), cardLocation.getRowOfCardLocation());
            }
        } else if (cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE)) {
            if (turn == 1) {
                return "--opponent --field";
            } else {
                return "--field";
            }
        } else if (cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE)) {
            if (turn == 1) {
                return "--opponent --graveyard " + cardLocation.getIndex();
            } else {
                return "--graveyard " + cardLocation.getIndex();
            }
        } else if (cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_HAND_ZONE)) {
            if (turn == 1) {
                return "--opponent --hand " + cardLocation.getIndex();
            } else {
                return "--hand " + cardLocation.getIndex();
            }
        } else if (cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_DECK_ZONE)) {
            if (turn == 1) {
                return "--opponent --deck " + cardLocation.getIndex();
            } else {
                return "--deck " + cardLocation.getIndex();
            }
        } else {
            return "";
        }
    }


}
