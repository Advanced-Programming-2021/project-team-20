package project.client.view.pooyaviewpackage;

import javafx.scene.control.Alert;
import javafx.stage.Stage;
import project.model.Utility.Utility;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.CardPosition;
import project.model.cardData.General.RowOfCardLocation;
import project.client.modelsforview.CardView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SendingRequestsToServer {

    public void sendNormalSummoningRequestToServer(CardView cardView, CardLocation cardLocationSelecting, DuelView duelView) {
        System.out.println();
        System.out.println();
        System.out.println("select " + giveStringToGiveToServerByCardLocation(cardLocationSelecting));
        String token = DuelView.getToken();
        JsonCreator.setFirstAdditionalString(giveStringToGiveToServerByCardLocation(cardLocationSelecting));
        String output = JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getInput(\"select \" + giveStringToGiveToServerByCardLocation(cardLocationSelecting), true, token)");
        //String output = GameManager.getDuelControllerByIndex(0).getInput("card show --selected", true);
        System.out.println("&" + output);
        //  DuelView.getControllerForView().getFinalCardLocationOfCurrentCardBeforeServer(cardView);
        output = JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getInput(\"normal summon\", true, token)");
        System.out.println("***Normal Summoning Message from server received is : " + output);
        conductSwitchingTurnsForSummoningBeingCarefulForAI(output, "Normal Summoning");
    }

    public void conductSwitchingTurnsForSummoningBeingCarefulForAI(String output, String typeOfErrorMessage) {
        if (output.contains("won the")){
            DuelView.getAdvancedCardMovingController().advanceForwardBattleField();
        }
        else if (output.startsWith("please wait until")) {
            Boolean bool = Boolean.parseBoolean(JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).isAIPlaying()"));
            if (bool) {
                DuelView.getAdvancedCardMovingController().advanceForwardBattleField();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Processing Message");
                alert.setContentText(output);
                alert.showAndWait();
            }
        } else {
            if (output.contains("successfully") || output.contains("won the")) {
                DuelView.getAdvancedCardMovingController().advanceForwardBattleField();
            } else if (!output.contains("successfully") && typeOfErrorMessage.contains("ummon") ||
                typeOfErrorMessage.contains("irect") && (output.contains("this card already attack") || output.contains("you can't")) ||
                typeOfErrorMessage.contains("ttack") && typeOfErrorMessage.contains("onster") && (output.contains("this card already attack") || output.contains("there is no card") || output.contains("you can't"))) {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(typeOfErrorMessage + " Message");
                alert.setContentText(output);
                alert.showAndWait();
            } else {
                DuelView.getAdvancedCardMovingController().advanceForwardBattleField();
            }
        }
    }

    public void sendTributeSummoningRequestToServer(CardView cardView, CardLocation cardLocationSelecting, DuelView duelView) {
        String token = DuelView.getToken();
        System.out.println("select " + giveStringToGiveToServerByCardLocation(cardLocationSelecting));
        JsonCreator.setFirstAdditionalString(giveStringToGiveToServerByCardLocation(cardLocationSelecting));
        String output = JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getInput(\"select \" + giveStringToGiveToServerByCardLocation(cardLocationSelecting), true, token)");

        //String output = GameManager.getDuelControllerByIndex(0).getInput("card show --selected", true);
        System.out.println("&" + output);
        //    DuelView.getControllerForView().getFinalCardLocationOfCurrentCardBeforeServer(cardView);
        output = JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getInput(\"tribute summon\", true, token)");
        conductSwitchingTurnsForSummoningBeingCarefulForAI(output, "Tribute Summoning");
    }

    public void sendSpecialSummoningRequestToServer(CardView cardView, CardLocation cardLocationSelecting, DuelView duelView) {
        String token = DuelView.getToken();
        System.out.println("select " + giveStringToGiveToServerByCardLocation(cardLocationSelecting));
        JsonCreator.setFirstAdditionalString(giveStringToGiveToServerByCardLocation(cardLocationSelecting));
        String output = JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getInput(\"select \" + giveStringToGiveToServerByCardLocation(cardLocationSelecting), true, token)");

        System.out.println("&" + output);
        output = JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getInput(\"special summon\", true, token)");
        System.out.println("*" + output);
        conductSwitchingTurnsForSummoningBeingCarefulForAI(output, "Special Summoning");
    }

    public void sendShowGraveyardRequestToServer(CardView cardView) {
        String token = DuelView.getToken();
        String output = JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getInput(\"show graveyard\", true, token)");
        System.out.println("&" + output);
        CardLocation cardLocation = DuelView.getControllerForView().giveCardLocationByCoordinateInView(null, cardView);
        DuelView.setIsAllySeeingGraveyard(cardLocation.getRowOfCardLocation().toString().startsWith("ALLY"));
        DuelView.setGraveyardString(output);
        DuelView.getGraveyardScene().start(new Stage());
    }

    public void sendSettingRequestToServer(CardView cardView, CardLocation cardLocationSelecting) {
        String token = DuelView.getToken();
        System.out.println("select " + giveStringToGiveToServerByCardLocation(cardLocationSelecting));
        JsonCreator.setFirstAdditionalString(giveStringToGiveToServerByCardLocation(cardLocationSelecting));
        String output = JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getInput(\"select \" + giveStringToGiveToServerByCardLocation(cardLocationSelecting), true, token)");
        System.out.println("&&&&" + output);
        output = JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getInput(\"set\", true, token)");
        System.out.println("*" + output);
        if (output.contains("successfully")) {
            DuelView.getAdvancedCardMovingController().advanceForwardBattleField();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Setting Card Message");
            alert.setContentText(output);
            alert.showAndWait();
        }
    }

    public void sendActivateEffectRequestToServer(CardView cardView, CardLocation cardLocationSelecting) {
        String token = DuelView.getToken();
        System.out.println("select " + giveStringToGiveToServerByCardLocation(cardLocationSelecting));
        JsonCreator.setFirstAdditionalString(giveStringToGiveToServerByCardLocation(cardLocationSelecting));
        String output = JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getInput(\"select \" + giveStringToGiveToServerByCardLocation(cardLocationSelecting), true, token)");

        System.out.println("&" + output);
        output = JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getInput(\"activate effect\", true, token)");
        System.out.println("*" + output);
        if (output.contains("activated") || output.contains("please wait")) {
            DuelView.getAdvancedCardMovingController().advanceForwardBattleField();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Activate Effect Message");
            output = output.replaceAll("Simply enter select command", "");
            output = output.replaceAll("simply enter select command", "");
            output = output.replaceAll("Simple enter select command", "");
            boolean showDeckAndGraveyardHand = false;
            if (output.contains("show hand, graveyard, deck.\nselect one cyberse normal monster to special summon")) {
                showDeckAndGraveyardHand = true;
                output = "select one cyberse normal monster to special summon either from your deck, graveyard, hand.";
            }
            boolean showOnlyDeck = false;
            if (output.contains("show deck")) {
                showOnlyDeck = true;
                output = output.replaceAll("show deck", "");
            }
            boolean shouldGraveyardBeReady = false;
            if (output.contains("show graveyard")) {
                shouldGraveyardBeReady = true;
                output = output.replaceAll("show graveyard", "");
            }
            alert.setContentText(output);
            alert.showAndWait();
            if (shouldGraveyardBeReady) {
                DuelView.getGraveyardScene().setClassWaitingForUserToChooseCardFromGraveyard(true);
            }
            if (showOnlyDeck) {
                DuelView.getDeckScene().setClassWaitingForUserToChooseCardFromDeck(true);
                DuelView.getGraveyardScene().setClassWaitingForUserToChooseCardFromGraveyard(false);
                DuelView.getDeckScene().start(new Stage());
            }
            if (showDeckAndGraveyardHand) {
                DuelView.getDeckScene().setClassWaitingForUserToChooseCardFromDeck(true);
                DuelView.getGraveyardScene().setClassWaitingForUserToChooseCardFromGraveyard(true);
                DuelView.getDeckScene().start(new Stage());
            }
        }
    }

    public void conductSwitchingInAttackingMonster(String output) {
        if (output.contains("now it will") && output.contains("do you")) {
            if (DuelView.isAreWePlayingWithAI()) {
                DuelView.getAdvancedCardMovingController().advanceForwardBattleField();
            } else {
                String wordToFind = "now it will be (\\S+)'s turn";
                Pattern word = Pattern.compile(wordToFind);
                Matcher match = word.matcher(output);
                String nowItWillBeTurn = "";
                String anotherWordToFind = "o you want to (.+)";
                Pattern anotherWord = Pattern.compile(anotherWordToFind);
                Matcher anotherMatcher = anotherWord.matcher(output);
                System.out.println("WHOUUUUUUU");
                if (match.find()) {
                    nowItWillBeTurn = match.group(0);
                    if (anotherMatcher.find()) {
                        nowItWillBeTurn += "\nD";
                        nowItWillBeTurn += anotherMatcher.group(0);
                    }
                    // System.out.println("Found love at index "+ match.start() +" - "+ (match.end()-1));
                }
                System.out.println(nowItWillBeTurn + "p");
                DuelView.getShowOptionsToUser().doYouWantToAlert(nowItWillBeTurn);
            }
        } else {
            if (output.contains("this card already attack") || output.contains("there is no card") || output.contains("you can't")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Attack Monster Message");
                alert.setContentText(output);
                alert.showAndWait();
            } else {
                DuelView.getAdvancedCardMovingController().advanceForwardBattleField();
            }
        }
    }


    public void sendAttackMonsterToMonsterRequestToServer(CardLocation cardLocationSelecting, CardLocation finalCardLocation, int yugiohIndex) {
        System.out.println("ZENOS IS ATTACKING MONSTER TO MONSTER :select " + giveStringToGiveToServerByCardLocation(cardLocationSelecting));
        String token = DuelView.getToken();
        System.out.println("select " + giveStringToGiveToServerByCardLocation(cardLocationSelecting));
        JsonCreator.setFirstAdditionalString(giveStringToGiveToServerByCardLocation(cardLocationSelecting));
        String output = JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getInput(\"select \" + giveStringToGiveToServerByCardLocation(cardLocationSelecting), true, token)");

        System.out.println("&&&&" + output);
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
        int turn = Integer.parseInt(JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getTurn()"));
        CardView beingAttackedCardView = DuelView.getControllerForView().getCardViewByCardLocation(finalCardLocation);
        CardView attackerCardView = DuelView.getControllerForView().getCardViewByCardLocation(cardLocationSelecting);
        JsonCreator.setIntegerString(yugiohOpponentMonsterIndex + "");
        output = JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getInput(\"attack \" + yugiohOpponentMonsterIndex, true, token)");
        System.out.println("*" + output);
        conductSwitchingTurnsForSummoningBeingCarefulForAI(output, "Attack Monster");
        //   conductSwitchingInAttackingMonster(output);
    }


    public void conductSwitchingTurnsInDirectAttacking(String output) {
        if (output.contains("now it will") && output.contains("do you")) {
            if (DuelView.isAreWePlayingWithAI()) {
                DuelView.getAdvancedCardMovingController().advanceForwardBattleField();
            } else {
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

            }
        } else {
            if (output.contains("you can't attack")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Direct Attack Message");
                alert.setContentText(output);
                alert.showAndWait();
            } else {
                DuelView.getAdvancedCardMovingController().advanceForwardBattleField();
            }
        }
    }

    public void sendAttackDirectRequestToServer(CardLocation cardLocationSelecting) {
        String token = DuelView.getToken();
        JsonCreator.setFirstAdditionalString(giveStringToGiveToServerByCardLocation(cardLocationSelecting));
        String output = JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getInput(\"select \" + giveStringToGiveToServerByCardLocation(cardLocationSelecting), true, token)");

        //String output = GameManager.getDuelControllerByIndex(0).getInput("card show --selected", true);
        System.out.println("&&&&" + output);
        //controllerForView.getFinalCardLocationOfCurrentCardBeforeServer(cardView);
        output = JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getInput(\"attack direct\", true, token)");
        System.out.println("*" + output);
        conductSwitchingTurnsForSummoningBeingCarefulForAI(output, "Direct Attack");
        //conductSwitchingTurnsInDirectAttacking(output);
    }

    public void sendFlipSummoningRequestToServer(CardView cardView, CardLocation cardLocationSelecting) {
        System.out.println("mmm " + giveStringToGiveToServerByCardLocation(cardLocationSelecting));
        String token = DuelView.getToken();
        JsonCreator.setFirstAdditionalString(giveStringToGiveToServerByCardLocation(cardLocationSelecting));
        String output = JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getInput(\"select \" + giveStringToGiveToServerByCardLocation(cardLocationSelecting), true, token)");

        System.out.println("&&&&" + output);
        output = JsonCreator.getResult("GameManager.getDuelControllerByIndex(DuelView.getToken()).getInput(\"flip-summon\", true, token)");
        System.out.println("*" + output);
        conductSwitchingTurnsForSummoningBeingCarefulForAI(output, "Flip Summoning");
//        if (!output.equals("")) {
//            if (output.contains("successfully")) {
//                DuelView.getAdvancedCardMovingController().advanceForwardBattleField();
//
//            } else {
//                //  DuelView.setCardLocationToSendCardTo(null);
//                Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                alert.setTitle("Information Dialog");
//                alert.setHeaderText("Flip Summoning Message");
//                alert.setContentText(output);
//                alert.showAndWait();
//            }
//        }
    }

    public void sendChangingCardPositionRequestToServer(CardView cardView, CardLocation cardLocationSelecting) {
        String token = DuelView.getToken();
        JsonCreator.setFirstAdditionalString(giveStringToGiveToServerByCardLocation(cardLocationSelecting));
        String output = JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getInput(\"select \" + giveStringToGiveToServerByCardLocation(cardLocationSelecting), true, token)");

        System.out.println("&&&&" + output);
        CardPosition cardPosition = cardView.getCard().getCardPosition();
        output = "";
        boolean finallyAttackPosition = false;
        if (cardPosition.equals(CardPosition.FACE_UP_ATTACK_POSITION)) {
            output = JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getInput(\"set --position defense\", true, token)");
        } else if (cardPosition.equals(CardPosition.FACE_UP_DEFENSE_POSITION)) {
            finallyAttackPosition = true;
            output = JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getInput(\"set --position attack\", true, token)");
        }
        System.out.println("*" + output);
        if (!output.equals("")) {
            if (output.contains("successfully")) {
                DuelView.getAdvancedCardMovingController().advanceForwardBattleField();
            } else {
                //DuelView.setCardLocationToSendCardTo(null);
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
        int fakeTurn = Integer.parseInt(JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getFakeTurn()"));
        int belongingTurn = Integer.parseInt(JsonCreator.getResult("give my actual turn"));
        int turn = (fakeTurn == belongingTurn ? 1 : 2);
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
                return "--field";
            } else {
                return "--opponent --field";
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
