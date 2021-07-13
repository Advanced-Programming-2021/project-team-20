package project.server.controller.duel.GamePhaseControllers;

import java.util.ArrayList;
import java.util.regex.Matcher;

import project.model.ActionType;
import project.server.controller.duel.CardEffects.*;
import project.server.controller.duel.GamePackage.*;
import project.server.controller.duel.PreliminaryPackage.GameManager;
import project.model.Utility.Utility;
import project.model.cardData.General.*;
import project.model.cardData.SpellCardData.SpellCard;
import project.model.cardData.TrapCardData.TrapCard;

public class ChainController {

    protected boolean isClassWaitingForChainCardToBeSelected;
    protected boolean isGoingToChangeTurnsForChaining;

    public ChainController() {
        isClassWaitingForChainCardToBeSelected = false;
        isGoingToChangeTurnsForChaining = false;
    }

    public boolean isClassWaitingForChainCardToBeSelected() {
        return isClassWaitingForChainCardToBeSelected;
    }

    public boolean isGoingToChangeTurnsForChaining() {
        return isGoingToChangeTurnsForChaining;
    }

    public String userReplyYesNoForChain(String string, String token) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        String inputRegex = "(?<=\\n|^)no(?=\\n|$)";
        Matcher matcher = Utility.getCommandMatcher(string, inputRegex);
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        if (Utility.isMatcherCorrectWithoutErrorPrinting(matcher)) {
            duelController.changeFakeTurn();
            int fakeTurn = duelController.getFakeTurn();
            isGoingToChangeTurnsForChaining = false;
            String usernameInChain = duelController.getPlayingUsernameByTurn(fakeTurn);
            return "now it will be " + usernameInChain + "'s turn" + "\n" + Action.conductAllActions(token);
        } else {
            String anotherRegex = "(?<=\\n|^)yes(?=\\n|$)";
            Matcher newMatcher = Utility.getCommandMatcher(string, anotherRegex);
            if (Utility.isMatcherCorrectWithoutErrorPrinting(newMatcher)) {
                isGoingToChangeTurnsForChaining = false;
                isClassWaitingForChainCardToBeSelected = true;
                return "simply select the spell or trap card you want to activate.\nthere is no need to enter activate command after that.";
            }
            return "invalid command";
        }
    }

    public String canChainingOccur(String token, int turn, ActionType allyActionType, ActionType opponentActionType) {
        MessagesFromEffectToControllers messagesFromEffectToControllers = null;
        if (turn == 1) {
            messagesFromEffectToControllers = Effect.canSpellTrapCardBeActivatedInChain(allyActionType, 1, 0, token);
        } else if (turn == 2) {
            messagesFromEffectToControllers = Effect.canSpellTrapCardBeActivatedInChain(opponentActionType, 2, 0, token);
        }
        return applyEffectsIfChainingWasPossible(messagesFromEffectToControllers, token);
    }


    public String applyEffectsIfChainingWasPossible(MessagesFromEffectToControllers messagesFromEffectToControllers, String token) {
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        if (messagesFromEffectToControllers.equals(MessagesFromEffectToControllers.SPELL_TRAP_CARD_CAN_BE_ACTIVATED_IN_CHAIN)) {
            isGoingToChangeTurnsForChaining = true;
            duelController.changeFakeTurn();
            int fakeTurn = duelController.getFakeTurn();
            String usernameInChain = duelController.getPlayingUsernameByTurn(fakeTurn);
            return "now it will be " + usernameInChain + "'s turn" + "\ndo you want to activate your trap or spell?";
        }
        return "";
    }

    public String isSelectedCardCorrectForChainActivation(String string, String token) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        ActivateSpellTrapController activateSpellTrapController = GameManager.getActivateSpellTrapControllerByIndex(token);
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        CardLocation cardLocation = selectedCardLocations.get(selectedCardLocations.size() - 1);
        Card card = duelBoard.getCardByCardLocation(cardLocation);
        boolean isAlreadyActivated = false;
        if (Card.isCardASpell(card)) {
            SpellCard spellCard = (SpellCard) card;
            isAlreadyActivated = spellCard.isCardAlreadyActivated();
        } else if (Card.isCardATrap(card)) {
            TrapCard trapCard = (TrapCard) card;
            isAlreadyActivated = trapCard.isCardAlreadyActivated();
        } else {
            return "you can't choose this card for chain activation.\nselect another card";
        }
        if (isAlreadyActivated) {
            return "you have already activated this card\nselect another card";
        } else {
            //should use advanced function
            ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(token);
            ActionType actionType = uninterruptedActions.get(uninterruptedActions.size() - 1).getActionType();
            boolean chainSpellTrapIsCorrect = Effect.isSelectedSpellTrapCorrectAccordingToPreviousActionAndArePreparationsComplete(cardLocation, actionType, token);
            if (!chainSpellTrapIsCorrect) {
                return "you can't activate this card in chain\nselect another card";
            } else {
                ArrayList<String> inputsNeeded = Effect.inputsNeededForActivatingSpellTrapCard(cardLocation, token);
                if (inputsNeeded.get(inputsNeeded.size() - 1).startsWith("nothing")) {
                    activateSpellTrapController.setMainCardLocation(cardLocation);
                    isClassWaitingForChainCardToBeSelected = false;
                    activateSpellTrapController.createActionForActivatingSpellTrap(token);
                    selectCardController.resetSelectedCardLocationList();
                    String output = Action.conductUninterruptedAction(token);
                    String canChainingOccur = activateSpellTrapController.canChainingOccur(token);
                    //used to give fakeTurn as input
                    if (canChainingOccur.equals("")) {
                        return output + "\n" + Action.conductAllActions(token);
                    }
                    return output + "\n" + canChainingOccur;
                } else {
                    activateSpellTrapController.setAreWeLookingForFurtherInputToActivateSpellTrap(true);
                    activateSpellTrapController.setMainCardLocation(cardLocation);
                    activateSpellTrapController.setMessagesSentToUser(inputsNeeded);
                    selectCardController.resetSelectedCardLocationList();
                    isClassWaitingForChainCardToBeSelected = false;
                    return inputsNeeded.get(inputsNeeded.size() - 1);
                }
            }

        }
    }
    public void clearAllVariablesOfThisClass(){
        isClassWaitingForChainCardToBeSelected = false;
        isGoingToChangeTurnsForChaining = false;
    }
}
