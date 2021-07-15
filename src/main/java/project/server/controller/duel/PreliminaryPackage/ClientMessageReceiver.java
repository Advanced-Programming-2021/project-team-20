package project.server.controller.duel.PreliminaryPackage;

import com.google.gson.JsonObject;
import project.client.view.pooyaviewpackage.DuelView;
import project.client.view.pooyaviewpackage.SendingRequestsToServer;
import project.model.cardData.General.Card;
import project.server.controller.duel.GamePackage.DuelController;
import project.server.controller.non_duel.storage.Storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientMessageReceiver {
    private static HashMap<DoubleToken, String> indirectMessages = new HashMap<>();

    public static void addDoubleTokenToIndirectMessages(DoubleToken doubleToken) {
        indirectMessages.put(doubleToken, "");
    }

    public synchronized static String findCommands(JsonObject details) {
        String token = details.get("token").getAsString();
        String request = details.get("request").getAsString();
        String firstAdditionalString = details.get("firstAdditionalString").getAsString();
        String integerString = details.get("integerString").getAsString();
        String outputFromServer = "";
        if (token.startsWith("nothin")) {
            return "you haven't logged in";
        }
        if (request.startsWith("Is it my turn")) {
            DoubleToken doubleToken = DoubleToken.getDoubleTokenByOneToken(token);
            if (doubleToken == null) {
                return "you haven't started battle";
            }
            DuelController duelController = GameManager.getDuelControllerByIndex(token);
            int fakeTurn = duelController.getFakeTurn();
            int turnForPlayer = duelController.getTurnByToken(token);
            if (fakeTurn == turnForPlayer) {
                return "true";
            }
            return "false";
        }
        if (request.startsWith("it's not my turn")) {
            outputFromServer = indirectMessages.get(DoubleToken.getDoubleTokenByOneToken(token));
            indirectMessages.replace(DoubleToken.getDoubleTokenByOneToken(token), "");
            return outputFromServer;
        } else if (request.startsWith("DuelStarter.getGameManager().getWholeReportToClient()")) {
            outputFromServer = DuelStarter.getGameManager().getWholeReportToClient(token);
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getNumberOfRounds()")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getNumberOfRounds() + "";
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getCurrentRound()")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getCurrentRound() + "";
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getTurn()")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getTurn() + "";
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"show deck\", true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("show deck", true, token);
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"show graveyard\", true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("show graveyard", true, token);
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).isAIPlaying()")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).isAIPlaying() + "";
        } else if (request.startsWith("give cards in")) {
            ArrayList<Card> cards = new ArrayList<>();
            int turn = GameManager.getDuelControllerByIndex(token).getTurnByToken(token);
            if (request.startsWith("give cards in my hand at the beginning of game")) {
                if (turn == 1) {
                    cards = GameManager.getDuelBoardByIndex(token).getAllyCardsInHand();
                } else {
                    cards = GameManager.getDuelBoardByIndex(token).getOpponentCardsInHand();
                }
            } else if (request.startsWith("give cards in my opponent hand at the beginning of game")) {
                if (turn == 1) {
                    cards = GameManager.getDuelBoardByIndex(token).getOpponentCardsInHand();
                } else {
                    cards = GameManager.getDuelBoardByIndex(token).getAllyCardsInHand();
                }
            } else if (request.startsWith("give cards in my deck at the beginning of game")) {
                if (turn == 1) {
                    cards = GameManager.getDuelBoardByIndex(token).getAllyCardsInDeck();
                } else {
                    cards = GameManager.getDuelBoardByIndex(token).getOpponentCardsInDeck();
                }
            } else if (request.startsWith("give cards in my opponent deck at the beginning of game")) {
                if (turn == 1) {
                    cards = GameManager.getDuelBoardByIndex(token).getAllyCardsInDeck();
                } else {
                    cards = GameManager.getDuelBoardByIndex(token).getOpponentCardsInDeck();
                }
            }
            String string = "";
            for (int i = 0; i < cards.size(); i++) {
                string += "*";
                string += cards.get(i).getCardName();
                string += "*!";
            }
            return string;
        } else if (request.startsWith("give my opponent username")) {
            int turn = GameManager.getDuelControllerByIndex(token).getTurnByToken(token);
            return GameManager.getDuelControllerByIndex(token).getPlayingUsernameByTurn(3 - turn);
        } else if (request.startsWith("give my opponent nickname")) {
            int turn = GameManager.getDuelControllerByIndex(token).getTurnByToken(token);
            String oppUsername = GameManager.getDuelControllerByIndex(token).getPlayingUsernameByTurn(3 - turn);
            return Storage.getUserByName(oppUsername).getNickname();
        } else {
            outputFromServer = findCommandsSynchronized(details);
        }
        return outputFromServer;
    }

    private static synchronized String findCommandsSynchronized(JsonObject details) {
        String token = details.get("token").getAsString();
        String request = details.get("request").getAsString();
        String firstAdditionalString = details.get("firstAdditionalString").getAsString();
        String integerString = details.get("integerString").getAsString();
        String outputFromServer = "";
        if (request.startsWith("DuelStarter.getGameManager().clearWholeReportToClient")) {
            DuelStarter.getGameManager().clearWholeReportToClient(token);
            return "";
            //????
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"select \" + SendingRequestsToServer.giveStringToGiveToServerByCardLocation(cardLocationSelecting), true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("select " + firstAdditionalString, true, token);
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"finish selecting\", true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("finish selecting", true, token);
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"attacking\", true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("attacking", true, token);
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"defensive\", true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("defensive", true, token);
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"pay\", true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("pay", true, token);
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"destroy\", true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("destroy", true, token);
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"surrender\", true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("surrender", true, token);
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"next phase\", true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("next phase", true, token);
        } else if (request.startsWith("GameManager.getPhaseControllerByIndex(token).getPhaseInGame()")) {
            outputFromServer = GameManager.getPhaseControllerByIndex(token).getPhaseInGame().toString();
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"card show --selected\", true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("card show --selected", true, token);
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getFakeTurn()")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getFakeTurn() + "";
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"select \" + miniString + \"--deck \" + (indexOfChosenCardInDeck+1), true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("select " + firstAdditionalString + "--deck " + integerString, true, token);
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"select \" + miniString + \"--graveyard \" + (indexOfChosenCardInGraveyard + 1), true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("select " + firstAdditionalString + "--graveyard " + integerString, true, token);
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"select \" + giveStringToGiveToServerByCardLocation(cardLocationSelecting), true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("select " + firstAdditionalString, true, token);
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"normal summon\", true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("normal summon", true, token);
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"tribute summon\", true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("tribute summon", true, token);
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"special summon\", true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("special summon", true, token);
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"show graveyard\", true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("show graveyard", true, token);
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"set\", true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("set", true, token);
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"activate effect\", true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("activate effect", true, token);
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"attack \" + yugiohOpponentMonsterIndex, true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("attack " + integerString, true, token);
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"attack direct\", true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("attack direct", true, token);
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"flip-summon\", true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("flip-summon", true, token);
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"set --position defense\", true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("set --position defense", true, token);
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"set --position attack\", true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("set --position attack", true, token);
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getAvailableCardLocationForUseForClient(token)")) {
            return GameManager.getDuelControllerByIndex(token).getAvailableCardLocationForUseForClient(token);
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).clearAvailableCardLocationForUseForClient(token)")) {
            GameManager.getDuelControllerByIndex(token).clearAvailableCardLocationForUseForClient(token);
            return "";
        }

        outputFromServer = takeCareOfOutput(outputFromServer, token);
        return outputFromServer;
    }


    private static String takeCareOfOutput(String outputFromServer, String token) {
        String nowItWillBeString = "now it will be (\\S+)'s turn";
        Pattern nowItWillBePattern = Pattern.compile(nowItWillBeString);
        Matcher match = nowItWillBePattern.matcher(outputFromServer);
        String nowItWillBeTurn = "";
        System.out.println("WHOUUUUUUU");
        boolean isTrue = false;
        if (match.find()) {
            nowItWillBeTurn = outputFromServer.substring(match.start(), match.end());
            indirectMessages.replace(DoubleToken.getDoubleTokenByOneToken(token), nowItWillBeTurn + "\nDo you want to activate your trap or spell?");
            isTrue = true;
            return "please wait until your input is being processed.";
            // System.out.println("Found love at index "+ match.start() +" - "+ (match.end()-1));
        } else {
            indirectMessages.replace(DoubleToken.getDoubleTokenByOneToken(token), "call your advance");
            return outputFromServer;
        }
    }
}
