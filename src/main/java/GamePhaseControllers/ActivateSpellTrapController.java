package GamePhaseControllers;

import CardData.General.Card;
import CardData.General.CardLocation;
import CardData.General.RowOfCardLocation;
import CardData.MonsterCardData.MonsterCard;
import CardData.MonsterCardData.MonsterCardAttribute;
import CardData.SpellCardData.SpellCard;
import CardData.SpellCardData.SpellCardValue;
import CardData.TrapCardData.TrapCard;
import CardEffects.EffectImplementations.Effect;
import CardEffects.EffectImplementations.MessagesFromEffectToControllers;
import GamePackage.Action;
import GamePackage.ActionType;
import GamePackage.DuelBoard;
import GamePackage.DuelController;
import PreliminaryPackage.GameManager;
import Utility.Utility;

import java.util.ArrayList;
import java.util.regex.Matcher;

import static CardData.MonsterCardData.MonsterCardFamily.WARRIOR;

public class ActivateSpellTrapController extends ChainController {
    private boolean areWeLookingForFurtherInputToActivateSpellTrap;
    private String messageSentToUser;
    private CardLocation mainCardLocation;
    private ArrayList<CardLocation> cardsToBeDiscarded;
    private ArrayList<CardLocation> cardsToBeChosenToApplyEquipSpellTo;
    private ArrayList<CardLocation> cardsToBeSpecialSummoned;
    private ArrayList<CardLocation> cardsToBeChosenFromDeckAndAddedToHand;
    private ArrayList<CardLocation> cardsToBeDestroyed;
    private ArrayList<CardLocation> cardsToTakeControlOf;

    public ActivateSpellTrapController() {
        areWeLookingForFurtherInputToActivateSpellTrap = false;
        isClassWaitingForChainCardToBeSelected = false;
        isGoingToChangeTurnsForChaining = false;
        cardsToBeDiscarded = new ArrayList<>();
        cardsToBeChosenToApplyEquipSpellTo = new ArrayList<>();
        cardsToBeSpecialSummoned = new ArrayList<>();
        cardsToBeChosenFromDeckAndAddedToHand = new ArrayList<>();
        cardsToBeDestroyed = new ArrayList<>();
        cardsToTakeControlOf = new ArrayList<>();
    }

    public boolean isAreWeLookingForFurtherInputToActivateSpellTrap() {
        return areWeLookingForFurtherInputToActivateSpellTrap;
    }

    public void setGoingToChangeTurnsForChaining(boolean goingToChangeTurnsForChaining) {
        isGoingToChangeTurnsForChaining = goingToChangeTurnsForChaining;
    }

    public void setMainCardLocation(CardLocation mainCardLocation) {
        this.mainCardLocation = mainCardLocation;
    }

    public void setAreWeLookingForFurtherInputToActivateSpellTrap(boolean areWeLookingForFurtherInputToActivateSpellTrap) {
        this.areWeLookingForFurtherInputToActivateSpellTrap = areWeLookingForFurtherInputToActivateSpellTrap;
    }

    public String activateSpellTrapEffectInputAnalysis(String string) {
        String inputRegex = "(?<=\\n|^)activate[\\s]+effect(?=\\n|$)";
        Matcher matcher = Utility.getCommandMatcher(string, inputRegex);
        if (Utility.isMatcherCorrectWithoutErrorPrinting(matcher)) {
            return startChecking(0);
        }
        return null;
    }

    public String startChecking(int index) {
        String resultOfChecking = Utility.isACardSelected(index, "", false);
        if (!resultOfChecking.equals("")) {
            return resultOfChecking;
        } else {
            return checkTypeOfCardForActivation(index);
        }
    }

    public String checkTypeOfCardForActivation(int index) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(index);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        Card card = duelBoard.getCardByCardLocation(selectedCardLocations.get(selectedCardLocations.size() - 1));
        if (Card.isCardAMonster(card)) {
            return "activate effect is only for spell and trap cards.";
        } else {
            return checkCorrectPhase(index);
        }
    }

    public String checkCorrectPhase(int index) {
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        int turn = duelController.getTurn();
        int fakeTurn = duelController.getFakeTurn();
        ArrayList<Action> actions = GameManager.getActionsByIndex(index);
        Action action = actions.get(actions.size() - 1);
        String resultOfChecking;
        if (turn == fakeTurn) {
            resultOfChecking = Utility.areWeInMainPhase(index);
            if (!resultOfChecking.equals("")) {
                return resultOfChecking;
            } else {
                return isSpellTrapCardAlreadyActivated(index);
            }
        } else {
            return isSpellTrapCardAlreadyActivated(index);
        }
    }

    public String isSpellTrapCardAlreadyActivated(int index) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(index);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        Card card = duelBoard.getCardByCardLocation(selectedCardLocations.get(selectedCardLocations.size() - 1));
        boolean isAlreadyActivated = false;
        if (Card.isCardASpell(card)) {
            SpellCard spellCard = (SpellCard) card;
            isAlreadyActivated = spellCard.isAlreadyActivated();

        } else if (Card.isCardATrap(card)) {
            TrapCard trapCard = (TrapCard) card;
            isAlreadyActivated = trapCard.isAlreadyActivated();
        }
        if (isAlreadyActivated) {
            return "you have already activated this card";
        } else {
            return isSpellCardZoneFull(index);
        }
    }

    public String isSpellCardZoneFull(int index) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(index);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        Card card = duelBoard.getCardByCardLocation(selectedCardLocations.get(selectedCardLocations.size() - 1));
        int fakeTurn = duelController.getFakeTurn();
        RowOfCardLocation rowOfCardLocation = RowOfCardLocation.OPPONENT_SPELL_ZONE;
        if (fakeTurn == 1) {
            if (card instanceof SpellCard && ((SpellCard) card).getSpellCardValue().equals(SpellCardValue.FIELD)) {
                rowOfCardLocation = RowOfCardLocation.ALLY_SPELL_FIELD_ZONE;
            } else {
                rowOfCardLocation = RowOfCardLocation.ALLY_SPELL_ZONE;
            }
        } else if (fakeTurn == 2) {
            if (card instanceof SpellCard && ((SpellCard) card).getSpellCardValue().equals(SpellCardValue.FIELD)) {
                rowOfCardLocation = RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE;
            } else {
                rowOfCardLocation = RowOfCardLocation.OPPONENT_SPELL_ZONE;
            }
        }
        if (duelBoard.isZoneFull(rowOfCardLocation)) {
            return "spell field zone is full";
        } else {
            String output = arePreparationsCompleteForSpellTrapActivation(index);
            if (output.startsWith("pre")){
                return output;
            } else if (output.equals("nothing needed")){
                createActionForActivatingSpellTrap(index);
                selectCardController.resetSelectedCardLocationList();
                String canChainingOccur = canChainingOccur(index);
                //duelController.changeFakeTurn();
                //used to give fakeTurn as input
                if (canChainingOccur.equals("")) {
                    return Action.conductUninterruptedAction(index) + Action.conductAllActions(index);
                }
                //activateSpellTrapController.setGoingToChangeTurnsForChaining(true);
                return Action.conductUninterruptedAction(index) + "\n" + canChainingOccur;
            } else {
                areWeLookingForFurtherInputToActivateSpellTrap = true;
                return output;
            }
        }
    }

    public String arePreparationsCompleteForSpellTrapActivation(int index) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(index);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        MessagesFromEffectToControllers messagesFromEffectToControllers = Effect.arePreparationsCompleteForActivatingSpellTrapCard(selectedCardLocations.get(selectedCardLocations.size() - 1), index);
        if (messagesFromEffectToControllers.equals(MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_SPELL_ARE_NOT_COMPLETE)) {
            return "preparations of this spell are not complete yet";
        } else if (messagesFromEffectToControllers.equals(MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_TRAP_ARE_NOT_COMPLETE)) {
            return "preparations of this trap are not complete yet";
        } else if (messagesFromEffectToControllers.equals(MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_SPELL_ARE_COMPLETE)) {
            return checkWhatIsNeededForSpellTrapActivation(index);
        } else if (messagesFromEffectToControllers.equals(MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_TRAP_ARE_COMPLETE)) {
            return checkWhatIsNeededForSpellTrapActivation(index);
        }
        return null;
    }

    public String checkWhatIsNeededForSpellTrapActivation(int index) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(index);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        int fakeTurn = GameManager.getDuelControllerByIndex(index).getFakeTurn();
        CardLocation spellTrapCardActivating = selectedCardLocations.get(selectedCardLocations.size() - 1);
        String resultOfChecking = Effect.inputsNeededForActivatingSpellTrapCard(spellTrapCardActivating, index);
        //System.out.println("WHY IS RESULTOFCHECKING NULL"+resultOfChecking);
        messageSentToUser = resultOfChecking;
        mainCardLocation = spellTrapCardActivating;
        return resultOfChecking;
    }

    public String redirectInput(int index) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(0);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(0);
        DuelController duelController = GameManager.getDuelControllerByIndex(0);
        CardLocation cardLocation = selectedCardLocations.get(selectedCardLocations.size() - 1);
        Card card = duelBoard.getCardByCardLocation(cardLocation);
        int fakeTurn = duelController.getFakeTurn();
        String canChainingOccur = "";
        String output = "";
        if (messageSentToUser.startsWith("please choose one dark") || messageSentToUser.startsWith("please choose one monster") || messageSentToUser.startsWith("please choose one warrior")) {
            if (fakeTurn == 1 && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) {
                return "invalid selection\nplease try again";
            } else if (fakeTurn == 2 && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) {
                return "invalid selection\nplease try again";
            } else if (!Card.isCardAMonster(card)) {
                return "invalid selection\nplease try again";
            } else {
                if ((messageSentToUser.startsWith("please choose one dark") && !((MonsterCard) card).getMonsterCardAttribute().equals(MonsterCardAttribute.DARK))) {
                    return "invalid selection\nplease try again";
                } else if (messageSentToUser.startsWith("please choose one warrior") && !((MonsterCard) card).getMonsterCardFamily().equals(WARRIOR)) {
                    return "invalid selection\nplease try again";
                } else {
                    areWeLookingForFurtherInputToActivateSpellTrap = false;
                    cardsToBeChosenToApplyEquipSpellTo.add(cardLocation);
                    selectCardController.resetSelectedCardLocationList();
                    createActionForActivatingSpellTrap(index);
                    /*
                    if (isRedirectInputBeingProcessesInChain) {
                        duelController.changeFakeTurn();
                        isRedirectInputBeingProcessesInChain = false;
                    }

                     */
                    output = Action.conductUninterruptedAction(0);
                    canChainingOccur = canChainingOccur(index);
                    //used to give fakeTurn as input
                }
            }
        } else if (messageSentToUser.startsWith("show graveyard")) {
            if (fakeTurn == 1 && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_GRAVEYARD_ZONE)) {
                return "invalid selection\nplease try again";
            } else if (fakeTurn == 2 && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE)) {
                return "invalid selection\nplease try again";
            } else if (!Card.isCardAMonster(card)) {
                return "invalid selection\nplease try again";
            } else {
                areWeLookingForFurtherInputToActivateSpellTrap = false;
                cardsToBeSpecialSummoned.add(cardLocation);
                selectCardController.resetSelectedCardLocationList();
                createActionForActivatingSpellTrap(index);
                /*
                if (isRedirectInputBeingProcessesInChain) {
                    duelController.changeFakeTurn();
                    isRedirectInputBeingProcessesInChain = false;
                }

                 */
                output = Action.conductUninterruptedAction(0);
                canChainingOccur = canChainingOccur(index);
                //used to give fakeTurn as input
            }
        } else if (messageSentToUser.startsWith("please choose one of your opponent's monsters")) {
            if (fakeTurn == 1 && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) {
                return "invalid selection\nplease try again";
            } else if (fakeTurn == 2 && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) {
                return "invalid selection\nplease try again";
            } else if (!Card.isCardAMonster(card)) {
                return "invalid selection\nplease try again";
            } else {
                areWeLookingForFurtherInputToActivateSpellTrap = false;
                cardsToTakeControlOf.add(cardLocation);
                System.out.println("887");
                selectCardController.resetSelectedCardLocationList();
                createActionForActivatingSpellTrap(index);
                /*
                if (isRedirectInputBeingProcessesInChain) {
                    duelController.changeFakeTurn();
                    isRedirectInputBeingProcessesInChain = false;
                }

                 */
                output = Action.conductUninterruptedAction(0);
                canChainingOccur = canChainingOccur(index);
                //used to give fakeTurn as input
            }
        } else if (messageSentToUser.startsWith("show deck")) {
            if (fakeTurn == 1 && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_DECK_ZONE)) {
                return "invalid selection\nplease try again";
            } else if (fakeTurn == 2 && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_DECK_ZONE)) {
                return "invalid selection\nplease try again";
            } else if (!Card.isCardASpell(card)) {
                return "invalid selection\nplease try again";
            } else if (!((SpellCard) card).getSpellCardValue().equals(SpellCardValue.FIELD)) {
                return "invalid selection\nplease try again";
            } else {
                areWeLookingForFurtherInputToActivateSpellTrap = false;
                cardsToBeChosenFromDeckAndAddedToHand.add(cardLocation);
                selectCardController.resetSelectedCardLocationList();
                createActionForActivatingSpellTrap(index);
                /*
                if (isRedirectInputBeingProcessesInChain) {
                    duelController.changeFakeTurn();
                    isRedirectInputBeingProcessesInChain = false;
                }

                 */
                output = Action.conductUninterruptedAction(0);
                canChainingOccur = canChainingOccur(index);
                //used to give fakeTurn as input
            }
        }//discard card from hand and choose up to two spell cards not written
        else if (messageSentToUser.startsWith("please choose one card from your hand to discard")) {
            if (fakeTurn == 1 && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_HAND_ZONE)) {
                return "invalid selection\nplease try again";
            } else if (fakeTurn == 2 && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_HAND_ZONE)) {
                return "invalid selection\nplease try again";
            } else {
                areWeLookingForFurtherInputToActivateSpellTrap = false;
                cardsToBeDiscarded.add(cardLocation);
                selectCardController.resetSelectedCardLocationList();
                createActionForActivatingSpellTrap(index);
                /*
                if (isRedirectInputBeingProcessesInChain) {
                    duelController.changeFakeTurn();
                    isRedirectInputBeingProcessesInChain = false;
                }

                 */
                output = Action.conductUninterruptedAction(0);
                canChainingOccur = canChainingOccur(index);
                //used to give fakeTurn as input
            }
        }
        if (!canChainingOccur.equals("")) {
            output += canChainingOccur;
            return output;
        }
        return output + Action.conductAllActions(0);
    }


    public void createActionForActivatingSpellTrap(int index) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(index);
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        int fakeTurn = duelController.getFakeTurn();
        Card card = duelBoard.getCardByCardLocation(mainCardLocation);
        //System.out.println("mainCard\n" + mainCard.getRowOfCardLocation().toString() + mainCard.getIndex());
        selectCardController.resetSelectedCardLocationList();
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(index);
        ArrayList<Action> actions = GameManager.getActionsByIndex(index);
        if (fakeTurn == 1) {
            if (Card.isCardASpell(card)) {
                uninterruptedActions.add(new Action(ActionType.ALLY_ACTIVATING_SPELL, 1, mainCardLocation, null, null, cardsToBeDiscarded, cardsToBeChosenToApplyEquipSpellTo, cardsToBeSpecialSummoned, cardsToBeChosenFromDeckAndAddedToHand, cardsToBeDestroyed, cardsToTakeControlOf));
                actions.add(new Action(ActionType.ALLY_ACTIVATING_SPELL, 1, mainCardLocation, null, null, cardsToBeDiscarded, cardsToBeChosenToApplyEquipSpellTo, cardsToBeSpecialSummoned, cardsToBeChosenFromDeckAndAddedToHand, cardsToBeDestroyed, cardsToTakeControlOf));

            } else if (Card.isCardATrap(card)) {
                uninterruptedActions.add(new Action(ActionType.ALLY_ACTIVATING_TRAP, 1, mainCardLocation, null, null, cardsToBeDiscarded, cardsToBeChosenToApplyEquipSpellTo, cardsToBeSpecialSummoned, cardsToBeChosenFromDeckAndAddedToHand, cardsToBeDestroyed, cardsToTakeControlOf));
                actions.add(new Action(ActionType.ALLY_ACTIVATING_TRAP, 1, mainCardLocation, null, null, cardsToBeDiscarded, cardsToBeChosenToApplyEquipSpellTo, cardsToBeSpecialSummoned, cardsToBeChosenFromDeckAndAddedToHand, cardsToBeDestroyed, cardsToTakeControlOf));
            }
            //add action that conducts effects of the card
        } else if (fakeTurn == 2) {
            if (Card.isCardASpell(card)) {
                uninterruptedActions.add(new Action(ActionType.OPPONENT_ACTIVATING_SPELL, 2, mainCardLocation, null, null, cardsToBeDiscarded, cardsToBeChosenToApplyEquipSpellTo, cardsToBeSpecialSummoned, cardsToBeChosenFromDeckAndAddedToHand, cardsToBeDestroyed, cardsToTakeControlOf));
                actions.add(new Action(ActionType.OPPONENT_ACTIVATING_SPELL, 2, mainCardLocation, null, null, cardsToBeDiscarded, cardsToBeChosenToApplyEquipSpellTo, cardsToBeSpecialSummoned, cardsToBeChosenFromDeckAndAddedToHand, cardsToBeDestroyed, cardsToTakeControlOf));
            } else if (Card.isCardATrap(card)) {
                uninterruptedActions.add(new Action(ActionType.OPPONENT_ACTIVATING_TRAP, 2, mainCardLocation, null, null, cardsToBeDiscarded, cardsToBeChosenToApplyEquipSpellTo, cardsToBeSpecialSummoned, cardsToBeChosenFromDeckAndAddedToHand, cardsToBeDestroyed, cardsToTakeControlOf));
                actions.add(new Action(ActionType.OPPONENT_ACTIVATING_TRAP, 2, mainCardLocation, null, null, cardsToBeDiscarded, cardsToBeChosenToApplyEquipSpellTo, cardsToBeSpecialSummoned, cardsToBeChosenFromDeckAndAddedToHand, cardsToBeDestroyed, cardsToTakeControlOf));
            }//add action that conducts effects of the card
        }

    }

    public String canChainingOccur(int index) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(index);
        Action uninterruptedAction = uninterruptedActions.get(uninterruptedActions.size() - 1);
        MessagesFromEffectToControllers messagesFromEffectToControllers = null;
        int actionTurn = uninterruptedAction.getActionTurn();
        if (uninterruptedAction.getActionType().equals(ActionType.ALLY_ACTIVATING_SPELL)) {
            messagesFromEffectToControllers = Effect.canSpellTrapCardBeActivatedInChain(ActionType.ALLY_ACTIVATING_SPELL, duelBoard, actionTurn);
        } else if (uninterruptedAction.getActionType().equals(ActionType.ALLY_ACTIVATING_TRAP)) {
            messagesFromEffectToControllers = Effect.canSpellTrapCardBeActivatedInChain(ActionType.ALLY_ACTIVATING_TRAP, duelBoard, actionTurn);
        } else if (uninterruptedAction.getActionType().equals(ActionType.OPPONENT_ACTIVATING_SPELL)) {
            messagesFromEffectToControllers = Effect.canSpellTrapCardBeActivatedInChain(ActionType.OPPONENT_ACTIVATING_SPELL, duelBoard, actionTurn);
        } else if (uninterruptedAction.getActionType().equals(ActionType.OPPONENT_ACTIVATING_TRAP)) {
            messagesFromEffectToControllers = Effect.canSpellTrapCardBeActivatedInChain(ActionType.OPPONENT_ACTIVATING_TRAP, duelBoard, actionTurn);
        }
        return applyEffectsIfChainingWasPossible(messagesFromEffectToControllers, index);
    }


}
