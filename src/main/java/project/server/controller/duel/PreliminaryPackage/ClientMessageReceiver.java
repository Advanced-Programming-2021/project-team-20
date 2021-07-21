package project.server.controller.duel.PreliminaryPackage;

import com.google.gson.JsonObject;
import project.client.view.pooyaviewpackage.DuelView;
import project.client.view.pooyaviewpackage.SendingRequestsToServer;
import project.model.PhaseInGame;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.RowOfCardLocation;
import project.model.cardData.MonsterCardData.MonsterCard;
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

    public static String findCommands(JsonObject details) {
        String token = details.get("token").getAsString();
        String request = details.get("request").getAsString();
        String firstAdditionalString = details.get("firstAdditionalString").getAsString();
        String integerString = details.get("integerString").getAsString();
        String outputFromServer = "";
        System.out.println(DuelStarter.getGameManager().getWholeReportToClient(token));
        if (request.startsWith("game is over")) {
            indirectMessages.replace(DoubleToken.getDoubleTokenByOneToken(token), "call your advance");
            GameManager.clearNecessaryObjects(token);
            return "you haven't kjnjhbmnhb";
        }
        if (token.startsWith("nothin")) {
            return "you haven't logged in";
        }
        if (request.startsWith("give my actual turn")) {
            DoubleToken doubleToken = DoubleToken.getDoubleTokenByOneToken(token);
            if (doubleToken == null) {
                return "you haven't started battle";
            }
            DuelController duelController = GameManager.getDuelControllerByIndex(token);
            return duelController.getTurnByToken(token) + "";
        }
        if (request.startsWith("Is it my turn")) {
            DoubleToken doubleToken = DoubleToken.getDoubleTokenByOneToken(token);
            if (doubleToken == null) {
                return "you haven't started battle";
            }
            try {
                DuelController duelController = GameManager.getDuelControllerByIndex(token);
                int fakeTurn = duelController.getFakeTurn();
                int turnForPlayer = duelController.getTurnByToken(token);
                System.out.println("token is " + token + " and fakeTurn = " + fakeTurn + " and turnForPlayer = " + turnForPlayer);
                if (fakeTurn == turnForPlayer) {
                    return "true";
                }
                return "false";
            } catch (Exception e) {
                return "you haven't started battle";
            }
        }
        if (request.startsWith("it's not my turn")) {
            StringBuilder output = new StringBuilder();
            for (int i = 0; i < indirectMessages.get(DoubleToken.getDoubleTokenByOneToken(token)).length(); i++) {
                output.append(indirectMessages.get(DoubleToken.getDoubleTokenByOneToken(token)).charAt(i));
            }
            outputFromServer = output.toString();
            indirectMessages.replace(DoubleToken.getDoubleTokenByOneToken(token), "");
            System.out.println("going outside is \n\n\n\n\n\n\n\n\n\n\n\n/" + outputFromServer + "/\n\n\n\n");
            return outputFromServer;
        } else if (request.startsWith("MonsterCard.giveATKDEFConsideringEffects(\"defense\", new CardLocation(rowOfCardLocation, index), token)")) {
            outputFromServer = MonsterCard.giveATKDEFConsideringEffects("defense", new CardLocation(RowOfCardLocation.valueOf(firstAdditionalString),
                Integer.parseInt(integerString)), token) + "";
        } else if (request.startsWith("MonsterCard.giveATKDEFConsideringEffects(\"attack\", new CardLocation(rowOfCardLocation, index), token)")) {
            outputFromServer = MonsterCard.giveATKDEFConsideringEffects("attack", new CardLocation(RowOfCardLocation.valueOf(firstAdditionalString),
                Integer.parseInt(integerString)), token) + "";
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
                    cards = GameManager.getDuelBoardByIndex(token).getOpponentCardsInDeck();
                } else {
                    cards = GameManager.getDuelBoardByIndex(token).getAllyCardsInDeck();
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
        if (request.startsWith("DuelStarter.getGameManager().clearWholeReportToClient()")) {
            DuelStarter.getGameManager().clearWholeReportToClient(token, false);
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
            DuelController duelController = GameManager.getDuelControllerByIndex(token);
            duelController.addStringToWhatUsersSay("*user" + duelController.getTurnByToken(token) + ": next phase*", token);
            PhaseInGame phaseInGame = GameManager.getPhaseControllerByIndex(token).getPhaseInGame();
            System.out.println("I SEE NEXT PHASE HERE IS NOW " + phaseInGame);
            boolean endOfTurnConfirmed = false;
            if (phaseInGame.equals(PhaseInGame.ALLY_MAIN_PHASE_2) || phaseInGame.equals(PhaseInGame.OPPONENT_MAIN_PHASE_2)) {
                endOfTurnConfirmed = true;
                duelController.addStringToWhatUsersSay("*user" + duelController.getTurnByToken(token) + ": next phase*", token);
            }
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("next phase", true, token);
            phaseInGame = GameManager.getPhaseControllerByIndex(token).getPhaseInGame();
            System.out.println("I SEE NEXT PHASE HERE IS NOW " + phaseInGame);
            if ((phaseInGame.equals(PhaseInGame.ALLY_MAIN_PHASE_1) || phaseInGame.equals(PhaseInGame.OPPONENT_MAIN_PHASE_1)) && !duelController.isAIPlaying()) {
                duelController.addStringToWhatUsersSay("*user" + (3 - duelController.getTurnByToken(token)) + ": next phase*", token);
                duelController.addStringToWhatUsersSay("*user" + (3 - duelController.getTurnByToken(token)) + ": next phase*", token);
            }
        } else if (request.startsWith("GameManager.getPhaseControllerByIndex(token).getPhaseInGame()")) {
            outputFromServer = GameManager.getPhaseControllerByIndex(token).getPhaseInGame().toString();
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"card show --selected\", true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("card show --selected", true, token);
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getFakeTurn()")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getFakeTurn() + "";
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"select \" + miniString + \"--deck \" + (indexOfChosenCardInDeck+1), true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("select " + firstAdditionalString + "--deck " + integerString, true, token);
            DuelController duelController = GameManager.getDuelControllerByIndex(token);
            duelController.addStringToWhatUsersSay("*user" + duelController.getTurnByToken(token) + ": " + "select " + firstAdditionalString + "--deck " + integerString + "*", token);
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"select \" + miniString + \"--graveyard \" + (indexOfChosenCardInGraveyard + 1), true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("select " + firstAdditionalString + "--graveyard " + integerString, true, token);
            DuelController duelController = GameManager.getDuelControllerByIndex(token);
            duelController.addStringToWhatUsersSay("*user" + duelController.getTurnByToken(token) + ": " + "select " + firstAdditionalString + "--graveyard " + integerString + "*", token);
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"select \" + giveStringToGiveToServerByCardLocation(cardLocationSelecting), true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("select " + firstAdditionalString, true, token);
            if (outputFromServer.contains("card selected")) {
                DuelController duelController = GameManager.getDuelControllerByIndex(token);
                duelController.addStringToWhatUsersSay("*user" + duelController.getTurnByToken(token) + ": " + "select " + firstAdditionalString + "*", token);
            }
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"normal summon\", true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("normal summon", true, token);
            if (outputFromServer.contains("successfully")) {
                DuelController duelController = GameManager.getDuelControllerByIndex(token);
                duelController.addStringToWhatUsersSay("*user" + duelController.getTurnByToken(token) + ": normal summon*", token);
            }
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"tribute summon\", true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("tribute summon", true, token);
            if (outputFromServer.contains("successfully")) {
                DuelController duelController = GameManager.getDuelControllerByIndex(token);
                duelController.addStringToWhatUsersSay("*user" + duelController.getTurnByToken(token) + ": tribute summon*", token);
            }
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"special summon\", true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("special summon", true, token);
            if (outputFromServer.contains("successfully")) {
                DuelController duelController = GameManager.getDuelControllerByIndex(token);
                duelController.addStringToWhatUsersSay("*user" + duelController.getTurnByToken(token) + ": special summon*", token);
            }
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"show graveyard\", true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("show graveyard", true, token);
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"set\", true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("set", true, token);
            if (outputFromServer.contains("successfully")) {
                DuelController duelController = GameManager.getDuelControllerByIndex(token);
                duelController.addStringToWhatUsersSay("*user" + duelController.getTurnByToken(token) + ": set*", token);
            }
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"activate effect\", true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("activate effect", true, token);
            if (outputFromServer.contains("successfully") || outputFromServer.contains("do you want")) {
                DuelController duelController = GameManager.getDuelControllerByIndex(token);
                duelController.addStringToWhatUsersSay("*user" + duelController.getTurnByToken(token) + ": activate effect*", token);
            }
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"attack \" + yugiohOpponentMonsterIndex, true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("attack " + integerString, true, token);
            if (outputFromServer.contains("now it will") || outputFromServer.contains("destroyed")) {
                DuelController duelController = GameManager.getDuelControllerByIndex(token);
                duelController.addStringToWhatUsersSay("*user" + duelController.getTurnByToken(token) + ": " + "attack " + integerString + "*", token);
            }
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"attack direct\", true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("attack direct", true, token);
            if (outputFromServer.contains("now it will") || outputFromServer.contains("receives")) {
                DuelController duelController = GameManager.getDuelControllerByIndex(token);
                duelController.addStringToWhatUsersSay("*user" + duelController.getTurnByToken(token) + ": attack direct*", token);
            }
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"flip-summon\", true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("flip-summon", true, token);
            if (outputFromServer.contains("successfully")) {
                DuelController duelController = GameManager.getDuelControllerByIndex(token);
                duelController.addStringToWhatUsersSay("*user" + duelController.getTurnByToken(token) + ": flip-summon*", token);
            }
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"set --position defense\", true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("set --position defense", true, token);
            if (outputFromServer.contains("successfully")) {
                DuelController duelController = GameManager.getDuelControllerByIndex(token);
                duelController.addStringToWhatUsersSay("*user" + duelController.getTurnByToken(token) + ": set --position defense*", token);
            }
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"set --position attack\", true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("set --position attack", true, token);
            if (outputFromServer.contains("successfully")) {
                DuelController duelController = GameManager.getDuelControllerByIndex(token);
                duelController.addStringToWhatUsersSay("*user" + duelController.getTurnByToken(token) + ": set --position attack*", token);
            }
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"yes\", true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("yes", true, token);
            DuelController duelController = GameManager.getDuelControllerByIndex(token);
            duelController.addStringToWhatUsersSay("*user" + duelController.getTurnByToken(token) + ": yes*", token);
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"no\", true, token)")) {
            indirectMessages.replace(DoubleToken.getDoubleTokenByOneToken(token), "call your advance");
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("no", true, token);
            DuelController duelController = GameManager.getDuelControllerByIndex(token);
            duelController.addStringToWhatUsersSay("*user" + duelController.getTurnByToken(token) + ": no*", token);
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getAvailableCardLocationForUseForClient(token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getAvailableCardLocationForUseForClient(token);
            System.out.println("AVAILABLE CARD LOCATION FOR USE COMING FROM SERVER =*" + outputFromServer + "*");
            return outputFromServer;
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).clearAvailableCardLocationForUseForClient(token)")) {
            GameManager.getDuelControllerByIndex(token).clearAvailableCardLocationForUseForClient(token);
            return "";
        }

        outputFromServer = takeCareOfOutput(outputFromServer, token);
        return outputFromServer;
    }


    private static String takeCareOfOutput(String outputFromServer, String token) {
        if (GameManager.getDuelControllerByIndex(token).isAIPlaying()) {
            String nowItWillBeString = "now it will be AI's turn";
            Pattern nowItWillBePattern = Pattern.compile(nowItWillBeString);
            Matcher match = nowItWillBePattern.matcher(outputFromServer);
            String nowItWillBeTurn = "";
            System.out.println("WHOUUUUUUU");
            boolean isTrue = false;
            if (match.find()) {
                return "please wait until your input is being processed.";
                // System.out.println("Found love at index "+ match.start() +" - "+ (match.end()-1));
            } else {
//                nowItWillBeString = "now it will be (\\S+)'s turn";
//                nowItWillBePattern = Pattern.compile(nowItWillBeString);
//                match = nowItWillBePattern.matcher(outputFromServer);
//                nowItWillBeTurn = "";
//                System.out.println("WHOUUUUUUU");
//                if (match.find()){
//
//                }
//                indirectMessages.replace(DoubleToken.getDoubleTokenByOneToken(token), "call your advance");
                return outputFromServer;
            }
        } else {
            String nowItWillBeString = "now it will be (\\S+)'s turn";
            Pattern nowItWillBePattern = Pattern.compile(nowItWillBeString);
            Matcher match = nowItWillBePattern.matcher(outputFromServer);
            String nowItWillBeTurn = "";
            System.out.println("WHOUUUUUUU");
            boolean isTrue = false;
            if (match.find()) {
                nowItWillBeTurn = outputFromServer.substring(match.start(), match.end());
                nowItWillBeString = "do you want to (.+)";
                nowItWillBePattern = Pattern.compile(nowItWillBeString);
                Matcher newmatch = nowItWillBePattern.matcher(outputFromServer);
                if (newmatch.find()) {
                    indirectMessages.replace(DoubleToken.getDoubleTokenByOneToken(token), nowItWillBeTurn + "\n" + outputFromServer.substring(newmatch.start(), newmatch.end()));
                } else {
                    indirectMessages.replace(DoubleToken.getDoubleTokenByOneToken(token), nowItWillBeTurn + "\n");
                }
                isTrue = true;
                return "please wait until your input is being processed.";
                // System.out.println("Found love at index "+ match.start() +" - "+ (match.end()-1));
            } else {
                indirectMessages.replace(DoubleToken.getDoubleTokenByOneToken(token), "call your advance");
                return outputFromServer;
            }
        }
    }
}
