package controller.duel.GamePhaseControllers;

import java.util.ArrayList;
import java.util.regex.Matcher;

import controller.duel.CardEffects.EffectImplementations.*;
import controller.duel.GamePackage.*;
import controller.duel.PreliminaryPackage.GameManager;
import controller.duel.Utility.Utility;
import model.cardData.General.*;
import model.cardData.SpellCardData.SpellCard;
import model.cardData.TrapCardData.TrapCard;

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

    public String userReplyYesNoForChain(String string) {
        ArrayList<Action> actions = GameManager.getActionsByIndex(0);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(0);
        String inputRegex = "(?<=\\n|^)no(?=\\n|$)";
        Matcher matcher = Utility.getCommandMatcher(string, inputRegex);
        DuelController duelController = GameManager.getDuelControllerByIndex(0);
        int turn = duelController.getTurn();
        if (Utility.isMatcherCorrectWithoutErrorPrinting(matcher)) {
            duelController.changeFakeTurn();
            int fakeTurn = duelController.getFakeTurn();
            isGoingToChangeTurnsForChaining = false;
            String usernameInChain = duelController.getPlayingUsernameByTurn(fakeTurn);
            return "now it will be " + usernameInChain + "'s turn\n" + duelBoard.showDuelBoard(0) + "\n" + Action.conductAllActions(0);
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

    public String canChainingOccur(int index, int turn, ActionType allyActionType, ActionType opponentActionType) {
        MessagesFromEffectToControllers messagesFromEffectToControllers = null;
        if (turn == 1) {
            messagesFromEffectToControllers = Effect.canSpellTrapCardBeActivatedInChain(allyActionType,  1);
        } else if (turn == 2) {
            messagesFromEffectToControllers = Effect.canSpellTrapCardBeActivatedInChain(opponentActionType, 2);
        }
        return applyEffectsIfChainingWasPossible(messagesFromEffectToControllers, index);
    }


    public String applyEffectsIfChainingWasPossible(MessagesFromEffectToControllers messagesFromEffectToControllers, int index) {
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        if (messagesFromEffectToControllers.equals(MessagesFromEffectToControllers.SPELL_TRAP_CARD_CAN_BE_ACTIVATED_IN_CHAIN)) {
            isGoingToChangeTurnsForChaining = true;
            duelController.changeFakeTurn();
            int fakeTurn = duelController.getFakeTurn();
            String usernameInChain = duelController.getPlayingUsernameByTurn(fakeTurn);
            return "now it will be " + usernameInChain + "'s turn\n" + duelBoard.showDuelBoard(index) + "\ndo you want to activate your trap or spell?";
        }
        return "";
    }

    public String isSelectedCardCorrectForChainActivation(String string, int index) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        ActivateSpellTrapController activateSpellTrapController = GameManager.getActivateSpellTrapControllerByIndex(index);
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(index);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        CardLocation cardLocation = selectedCardLocations.get(selectedCardLocations.size() - 1);
        Card card = duelBoard.getCardByCardLocation(cardLocation);
        boolean isAlreadyActivated = false;
        if (Card.isCardASpell(card)) {
            SpellCard spellCard = (SpellCard) card;
            isAlreadyActivated = spellCard.isAlreadyActivated();
        } else if (Card.isCardATrap(card)) {
            TrapCard trapCard = (TrapCard) card;
            isAlreadyActivated = trapCard.isAlreadyActivated();
        } else {
            return "you can't choose this card for chain activation.\nselect another card";
        }
        if (isAlreadyActivated) {
            return "you have already activated this card\nselect another card";
        } else {
            //should use advanced function
            ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(index);
            ActionType actionType = uninterruptedActions.get(uninterruptedActions.size()-1).getActionType();
            boolean chainSpellTrapIsCorrect = Effect.isSelectedSpellTrapCorrectAccordingToPreviousActionAndArePreparationsComplete(cardLocation, actionType,index);
            if (!chainSpellTrapIsCorrect) {
                return "you can't activate this card in chain\nselect another card";
            } else{
                String inputsNeeded = Effect.inputsNeededForActivatingSpellTrapCard(cardLocation, index);
                if (inputsNeeded.startsWith("nothing")){
                    activateSpellTrapController.setMainCardLocation(cardLocation);
                    isClassWaitingForChainCardToBeSelected = false;
                    activateSpellTrapController.createActionForActivatingSpellTrap(index);
                    selectCardController.resetSelectedCardLocationList();
                    String output = Action.conductUninterruptedAction(index);
                    String canChainingOccur = activateSpellTrapController.canChainingOccur(index);
                    //duelController.changeFakeTurn();
                    //used to give fakeTurn as input
                    if (canChainingOccur.equals("")) {
                        return output+"\n" + Action.conductAllActions(index);
                    }
                    //activateSpellTrapController.setGoingToChangeTurnsForChaining(true);
                    return output + "\n" + canChainingOccur;
                } else {
                    activateSpellTrapController.setAreWeLookingForFurtherInputToActivateSpellTrap(true);
                    activateSpellTrapController.setMainCardLocation(cardLocation);
                    //activateSpellTrapController.setRedirectInputBeingProcessesInChain(true);
                    selectCardController.resetSelectedCardLocationList();
                    isClassWaitingForChainCardToBeSelected = false;
                    return inputsNeeded;
                }
            }

        }
    }

}
