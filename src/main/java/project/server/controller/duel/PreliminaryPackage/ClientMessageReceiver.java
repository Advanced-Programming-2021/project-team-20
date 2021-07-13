package project.server.controller.duel.PreliminaryPackage;

import com.google.gson.JsonObject;
import project.client.view.pooyaviewpackage.DuelView;
import project.client.view.pooyaviewpackage.SendingRequestsToServer;

public class ClientMessageReceiver {
    public static String findCommands(JsonObject details) {
        String token = details.get("token").getAsString();
        String request = details.get("request").getAsString();
        String firstAdditionalString = details.get("firstAdditionalString").getAsString();
        String integerString = details.get("integerString").getAsString();
        String outputFromServer;
        if (request.startsWith("DuelStarter.getGameManager().getWholeReportToClient()")) {
            outputFromServer =  DuelStarter.getGameManager().getWholeReportToClient(token);
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getNumberOfRounds()")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getNumberOfRounds() + "";
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getCurrentRound()")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getCurrentRound() + "";
        } else if (request.startsWith("DuelStarter.getGameManager().clearWholeReportToClient")) {
            DuelStarter.getGameManager().clearWholeReportToClient(token);
            return "";
            //????
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getTurn()")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getTurn() + "";
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"show deck\", true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("show deck", true, token);
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"show graveyard\", true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("show graveyard", true, token);
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).isAIPlaying()")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).isAIPlaying() + "";
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
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"attacking\", true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("attacking", true, token);
        } else if (request.startsWith("GameManager.getDuelControllerByIndex(token).getInput(\"defensive\", true, token)")) {
            outputFromServer = GameManager.getDuelControllerByIndex(token).getInput("defensive", true, token);
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
        }

        return "";
    }
}
