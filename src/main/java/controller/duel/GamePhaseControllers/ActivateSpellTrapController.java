package controller.duel.GamePhaseControllers;

import java.net.NoRouteToHostException;
import java.util.ArrayList;
import java.util.regex.Matcher;

import com.sun.source.tree.ArrayAccessTree;
import controller.duel.CardEffects.EffectImplementations.Effect;
import controller.duel.CardEffects.EffectImplementations.MessagesFromEffectToControllers;
import controller.duel.GamePackage.Action;
import controller.duel.GamePackage.ActionType;
import controller.duel.GamePackage.DuelBoard;
import controller.duel.GamePackage.DuelController;
import controller.duel.PreliminaryPackage.GameManager;
import controller.duel.Utility.Utility;
import model.cardData.General.Card;
import model.cardData.General.CardLocation;
import model.cardData.General.CardPosition;
import model.cardData.General.RowOfCardLocation;
import model.cardData.MonsterCardData.MonsterCard;
import model.cardData.MonsterCardData.MonsterCardAttribute;
import model.cardData.MonsterCardData.MonsterCardFamily;
import model.cardData.MonsterCardData.MonsterCardValue;
import model.cardData.SpellCardData.SpellCard;
import model.cardData.SpellCardData.SpellCardValue;
import model.cardData.TrapCardData.TrapCard;

public class ActivateSpellTrapController extends ChainController {
    private boolean areWeLookingForFurtherInputToActivateSpellTrap;
    private String messageSentToUser;
    private ArrayList<String> messagesSentToUser;
    private CardLocation mainCardLocation;
    private CardPosition mainCardPosition;
    private ArrayList<CardLocation> cardsToBeDiscarded;
    private ArrayList<CardLocation> cardsToBeChosenToApplyEquipSpellTo;
    private ArrayList<CardLocation> cardsToBeSpecialSummoned;
    private ArrayList<CardLocation> cardsToBeRitualSummoned;
    private ArrayList<CardLocation> cardsToBeChosenFromDeckAndAddedToHand;
    private ArrayList<CardLocation> cardsToBeDestroyed;
    private ArrayList<CardLocation> cardsToTakeControlOf;
    private ArrayList<CardLocation> cardsToBeChosenFromDeckAndSentToGraveyard;
    private ArrayList<Integer> sumOfLevelsOfChosenMonsters;

    public ActivateSpellTrapController() {
        areWeLookingForFurtherInputToActivateSpellTrap = false;
        isClassWaitingForChainCardToBeSelected = false;
        isGoingToChangeTurnsForChaining = false;
        mainCardPosition = CardPosition.FACE_UP_ACTIVATED_POSITION;
        messagesSentToUser = new ArrayList<>();
        cardsToBeDiscarded = new ArrayList<>();
        cardsToBeChosenToApplyEquipSpellTo = new ArrayList<>();
        cardsToBeSpecialSummoned = new ArrayList<>();
        cardsToBeRitualSummoned = new ArrayList<>();
        cardsToBeChosenFromDeckAndAddedToHand = new ArrayList<>();
        cardsToBeDestroyed = new ArrayList<>();
        cardsToTakeControlOf = new ArrayList<>();
        cardsToBeChosenFromDeckAndSentToGraveyard = new ArrayList<>();
        sumOfLevelsOfChosenMonsters = new ArrayList<>();
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
        ActivateMonsterController activateMonsterController = GameManager.getActivateMonsterControllerByIndex(index);
        if (Card.isCardAMonster(card)) {
            return activateMonsterController.activateMonsterEffectInputAnalysis("activate effect");
            //"activate effect is only for spell and trap cards.";
        } else {
            return checkCorrectPhase(index);
        }
    }

    public String checkCorrectPhase(int index) {
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        int turn = duelController.getTurn();
        int fakeTurn = duelController.getFakeTurn();
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
            isAlreadyActivated = spellCard.isCardAlreadyActivated();

        } else if (Card.isCardATrap(card)) {
            TrapCard trapCard = (TrapCard) card;
            isAlreadyActivated = trapCard.isCardAlreadyActivated();
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
        if (card == null) {
            System.out.println("WHOOPS!");
        } else {
            System.out.println("CARD NAME IS " + card.getCardName());
        }
        int fakeTurn = duelController.getFakeTurn();
        RowOfCardLocation rowOfCardLocation = RowOfCardLocation.OPPONENT_SPELL_ZONE;
        if (fakeTurn == 1) {
            if (Card.isCardASpell(card) && ((SpellCard) card).getSpellCardValue().equals(SpellCardValue.FIELD)) {
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
        if (duelBoard.isZoneFull(rowOfCardLocation) && (rowOfCardLocation.equals(RowOfCardLocation.ALLY_SPELL_ZONE) || rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_SPELL_ZONE))) {
            return "spell zone is full";
        } else if (duelBoard.isZoneFull(rowOfCardLocation) && (rowOfCardLocation.equals(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE) || rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE))) {
            return "spell field zone is full";
        } else {
            String output = arePreparationsCompleteForSpellTrapActivation(index);
            System.out.println("SUCH A THING THAT IS SUPPOSED TO GIVE THINGS NEEDED OR SAY PREPARATIONS ARE NOT OK IS SAYING\n" + output);
            if (output.startsWith("pre")) {
                return output;
            } else if (output.equals("nothing needed")) {
                createActionForActivatingSpellTrap(index);
                selectCardController.resetSelectedCardLocationList();
                String finalOutput = Action.conductUninterruptedAction(index);
                String canChainingOccur = canChainingOccur(index);
                //duelController.changeFakeTurn();
                //used to give fakeTurn as input
                if (canChainingOccur.equals("")) {
                    return finalOutput + "\n" + Action.conductAllActions(index);
                }
                //activateSpellTrapController.setGoingToChangeTurnsForChaining(true);
                return finalOutput + "\n" + canChainingOccur;
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
        ArrayList<String> resultOfChecking = Effect.inputsNeededForActivatingSpellTrapCard(spellTrapCardActivating, index);
        //System.out.println("WHY IS RESULTOFCHECKING NULL"+resultOfChecking);
        messagesSentToUser = resultOfChecking;
        mainCardLocation = spellTrapCardActivating;
        return resultOfChecking.get(resultOfChecking.size() - 1);
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
        String message = messagesSentToUser.get(messagesSentToUser.size() - 1);
        if (message.startsWith("please choose one dark") || message.startsWith("please choose one monster") || message.startsWith("please choose one warrior")) {
            if (fakeTurn == 1 && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) {
                return "invalid selection\nplease try again";
            } else if (fakeTurn == 2 && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) {
                return "invalid selection\nplease try again";
            } else if (!Card.isCardAMonster(card)) {
                return "invalid selection\nplease try again";
            } else {
                if ((message.startsWith("please choose one dark") && !((MonsterCard) card).getMonsterCardAttribute().equals(MonsterCardAttribute.DARK))) {
                    return "invalid selection\nplease try again";
                } else if (message.startsWith("please choose one warrior") && !((MonsterCard) card).getMonsterCardFamily().equals(MonsterCardFamily.WARRIOR)) {
                    return "invalid selection\nplease try again";
                } else {
                    cardsToBeChosenToApplyEquipSpellTo.add(cardLocation);
                    boolean isMoreInputNeeded = isMoreInputNeededWhenOneInputIsGivenCorrectly(index);
                    if (isMoreInputNeeded) {
                        return messagesSentToUser.get(messagesSentToUser.size() - 1);
                    } else {
                        output = Action.conductUninterruptedAction(0);
                        canChainingOccur = canChainingOccur(index);
                    }
                    //used to give fakeTurn as input
                }
            }
        } else if (message.startsWith("show graveyard")) {
            if (fakeTurn == 1 && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_GRAVEYARD_ZONE)) {
                return "invalid selection\nplease try again";
            } else if (fakeTurn == 2 && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE)) {
                return "invalid selection\nplease try again";
            } else if (!Card.isCardAMonster(card)) {
                return "invalid selection\nplease try again";
            } else {
                cardsToBeSpecialSummoned.add(cardLocation);
                boolean isMoreInputNeeded = isMoreInputNeededWhenOneInputIsGivenCorrectly(index);
                if (isMoreInputNeeded) {
                    return messagesSentToUser.get(messagesSentToUser.size() - 1);
                } else {
                    output = Action.conductUninterruptedAction(0);
                    canChainingOccur = canChainingOccur(index);
                }
            }
        } else if (message.startsWith("please choose one of your opponent's monsters")) {
            if (fakeTurn == 1 && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) {
                return "invalid selection\nplease try again";
            } else if (fakeTurn == 2 && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) {
                return "invalid selection\nplease try again";
            } else if (!Card.isCardAMonster(card)) {
                return "invalid selection\nplease try again";
            } else {
                cardsToTakeControlOf.add(cardLocation);
                boolean isMoreInputNeeded = isMoreInputNeededWhenOneInputIsGivenCorrectly(index);
                if (isMoreInputNeeded) {
                    return messagesSentToUser.get(messagesSentToUser.size() - 1);
                } else {
                    output = Action.conductUninterruptedAction(0);
                    canChainingOccur = canChainingOccur(index);
                }
            }
        } else if (message.startsWith("show deck")) {
            if (fakeTurn == 1 && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_DECK_ZONE)) {
                return "invalid selection\nplease try again";
            } else if (fakeTurn == 2 && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_DECK_ZONE)) {
                return "invalid selection\nplease try again";
            } else if (!Card.isCardASpell(card)) {
                return "invalid selection\nplease try again";
            } else if (!((SpellCard) card).getSpellCardValue().equals(SpellCardValue.FIELD)) {
                return "invalid selection\nplease try again";
            } else {
                cardsToBeChosenFromDeckAndAddedToHand.add(cardLocation);
                boolean isMoreInputNeeded = isMoreInputNeededWhenOneInputIsGivenCorrectly(index);
                if (isMoreInputNeeded) {
                    return messagesSentToUser.get(messagesSentToUser.size() - 1);
                } else {
                    output = Action.conductUninterruptedAction(0);
                    canChainingOccur = canChainingOccur(index);
                }
                //used to give fakeTurn as input
            }
        }//discard card from hand and choose up to two spell cards not written
        else if (message.startsWith("please choose one card from your hand to discard")) {
            if (fakeTurn == 1 && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_HAND_ZONE)) {
                return "invalid selection\nplease try again";
            } else if (fakeTurn == 2 && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_HAND_ZONE)) {
                return "invalid selection\nplease try again";
            } else {
                cardsToBeDiscarded.add(cardLocation);
                boolean isMoreInputNeeded = isMoreInputNeededWhenOneInputIsGivenCorrectly(index);
                if (isMoreInputNeeded) {
                    return messagesSentToUser.get(messagesSentToUser.size() - 1);
                } else {
                    output = Action.conductUninterruptedAction(0);
                    canChainingOccur = canChainingOccur(index);
                }
                //used to give fakeTurn as input
            }
        } else if (message.startsWith("please choose normal monsters")) {
            if (fakeTurn == 1 && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_DECK_ZONE)) {
                return "invalid selection\nplease try again";
            } else if (fakeTurn == 2 && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_DECK_ZONE)) {
                return "invalid selection\nplease try again";
            } else if (!Card.isCardAMonster(card)) {
                return "chosen card is not a monster\nplease try again";
            } else if (!((MonsterCard) card).getMonsterCardValue().equals(MonsterCardValue.NORMAL)) {
                return "chosen monster card is not normal\nplease try again";
            } else {
                cardsToBeChosenFromDeckAndSentToGraveyard.add(cardLocation);
                sumOfLevelsOfChosenMonsters.add(((MonsterCard) card).getLevel());
                if (areSumOfNormalMonsterLevelsEqualToARitualMonsterLevel(fakeTurn, index)) {
                    messagesSentToUser.remove(messagesSentToUser.size() - 1);
                    if (messagesSentToUser.size() == 0) {
                        areWeLookingForFurtherInputToActivateSpellTrap = false;
                        selectCardController.resetSelectedCardLocationList();
                        createActionForActivatingSpellTrap(index);
                        output = Action.conductUninterruptedAction(0);
                        canChainingOccur = canChainingOccur(index);
                    } else {
                        return messagesSentToUser.get(messagesSentToUser.size() - 1);
                    }
                } else {
                    return "select another normal monster from your deck";
                }
                //used to give fakeTurn as input
            }
        } else if (message.startsWith("now select one ritual")) {
            if (fakeTurn == 1 && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_HAND_ZONE)) {
                return "invalid selection\nplease try again";
            } else if (fakeTurn == 2 && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_HAND_ZONE)) {
                return "invalid selection\nplease try again";
            } else if (!Card.isCardAMonster(card)) {
                return "chosen card is not a monster\nplease try again";
            } else if (!((MonsterCard) card).getMonsterCardValue().equals(MonsterCardValue.RITUAL)) {
                return "chosen monster card is not ritual\nplease try again";
            } else if (((MonsterCard) card).getLevel() != sumOfLevelsOfChosenMonsters()) {
                return "the level of this ritual monster is not equal to the sum of levels of previously chosen cards\nplease try again";
            } else {
                cardsToBeRitualSummoned.add(cardLocation);
                boolean isMoreInputNeeded = isMoreInputNeededWhenOneInputIsGivenCorrectly(index);
                if (isMoreInputNeeded) {
                    return messagesSentToUser.get(messagesSentToUser.size() - 1);
                } else {
                    output = Action.conductUninterruptedAction(0);
                    canChainingOccur = canChainingOccur(index);
                }
                //used to give fakeTurn as input
            }
        } else if (message.startsWith("please choose if you want")) {
            String input = duelController.getLatestInput();
            if (input.equals("attack")) {
                mainCardPosition = CardPosition.FACE_UP_ATTACK_POSITION;
                boolean isMoreInputNeeded = isMoreInputNeededWhenOneInputIsGivenCorrectly(index);
                if (isMoreInputNeeded) {
                    return messagesSentToUser.get(messagesSentToUser.size() - 1);
                } else {
                    output = Action.conductUninterruptedAction(0);
                    canChainingOccur = canChainingOccur(index);
                }
            } else if (input.equals("defense")) {
                mainCardPosition = CardPosition.FACE_UP_DEFENSE_POSITION;
                boolean isMoreInputNeeded = isMoreInputNeededWhenOneInputIsGivenCorrectly(index);
                if (isMoreInputNeeded) {
                    return messagesSentToUser.get(messagesSentToUser.size() - 1);
                } else {
                    output = Action.conductUninterruptedAction(0);
                    canChainingOccur = canChainingOccur(index);
                }
            } else {
                return "invalid input\nplease enter attack or defense";
            }
        }
        if (!canChainingOccur.equals("")) {
            output += canChainingOccur;
            return output;
        }
        return output + Action.conductAllActions(0);
    }


    private boolean areSumOfNormalMonsterLevelsEqualToARitualMonsterLevel(int turn, int index) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        ArrayList<Card> cardsInHand;
        if (turn == 1) {
            cardsInHand = duelBoard.getAllyCardsInHand();
        } else {
            cardsInHand = duelBoard.getOpponentCardsInHand();
        }
        for (int i = 0; i < cardsInHand.size(); i++) {
            if (Card.isCardAMonster(cardsInHand.get(i)) && ((MonsterCard) cardsInHand.get(i)).getMonsterCardValue().equals(MonsterCardValue.RITUAL)) {
                int level = ((MonsterCard) cardsInHand.get(i)).getLevel();
                if (sumOfLevelsOfChosenMonsters() == level) {
                    return true;
                }
            }
        }
        return false;
    }

    private int sumOfLevelsOfChosenMonsters() {
        int sumOfLevels = 0;
        for (int j = 0; j < sumOfLevelsOfChosenMonsters.size(); j++) {
            sumOfLevels += sumOfLevelsOfChosenMonsters.get(j);
        }
        return sumOfLevels;
    }

    private boolean isMoreInputNeededWhenOneInputIsGivenCorrectly(int index) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(index);
        messagesSentToUser.remove(messagesSentToUser.size() - 1);
        if (messagesSentToUser.size() > 0) {
            return true;
        } else {
            areWeLookingForFurtherInputToActivateSpellTrap = false;
            selectCardController.resetSelectedCardLocationList();
            createActionForActivatingSpellTrap(index);
            return false;
        }
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
                uninterruptedActions.add(new Action(ActionType.ALLY_ACTIVATING_SPELL, 1, mainCardLocation, null, null, cardsToBeDiscarded, cardsToBeChosenToApplyEquipSpellTo, cardsToBeSpecialSummoned, cardsToBeChosenFromDeckAndAddedToHand, cardsToBeDestroyed, cardsToTakeControlOf, cardsToBeChosenFromDeckAndSentToGraveyard, cardsToBeRitualSummoned, CardPosition.FACE_UP_ACTIVATED_POSITION));
                actions.add(new Action(ActionType.ALLY_ACTIVATING_SPELL, 1, mainCardLocation, null, null, cardsToBeDiscarded, cardsToBeChosenToApplyEquipSpellTo, cardsToBeSpecialSummoned, cardsToBeChosenFromDeckAndAddedToHand, cardsToBeDestroyed, cardsToTakeControlOf, cardsToBeChosenFromDeckAndSentToGraveyard, cardsToBeRitualSummoned, CardPosition.FACE_UP_ACTIVATED_POSITION));

            } else if (Card.isCardATrap(card)) {
                uninterruptedActions.add(new Action(ActionType.ALLY_ACTIVATING_TRAP, 1, mainCardLocation, null, null, cardsToBeDiscarded, cardsToBeChosenToApplyEquipSpellTo, cardsToBeSpecialSummoned, cardsToBeChosenFromDeckAndAddedToHand, cardsToBeDestroyed, cardsToTakeControlOf, cardsToBeChosenFromDeckAndSentToGraveyard, cardsToBeRitualSummoned, CardPosition.FACE_UP_ACTIVATED_POSITION));
                actions.add(new Action(ActionType.ALLY_ACTIVATING_TRAP, 1, mainCardLocation, null, null, cardsToBeDiscarded, cardsToBeChosenToApplyEquipSpellTo, cardsToBeSpecialSummoned, cardsToBeChosenFromDeckAndAddedToHand, cardsToBeDestroyed, cardsToTakeControlOf, cardsToBeChosenFromDeckAndSentToGraveyard, cardsToBeRitualSummoned, CardPosition.FACE_UP_ACTIVATED_POSITION));
            }
            //add action that conducts effects of the card
        } else if (fakeTurn == 2) {
            if (Card.isCardASpell(card)) {
                uninterruptedActions.add(new Action(ActionType.OPPONENT_ACTIVATING_SPELL, 2, mainCardLocation, null, null, cardsToBeDiscarded, cardsToBeChosenToApplyEquipSpellTo, cardsToBeSpecialSummoned, cardsToBeChosenFromDeckAndAddedToHand, cardsToBeDestroyed, cardsToTakeControlOf, cardsToBeChosenFromDeckAndSentToGraveyard, cardsToBeRitualSummoned, CardPosition.FACE_UP_ACTIVATED_POSITION));
                actions.add(new Action(ActionType.OPPONENT_ACTIVATING_SPELL, 2, mainCardLocation, null, null, cardsToBeDiscarded, cardsToBeChosenToApplyEquipSpellTo, cardsToBeSpecialSummoned, cardsToBeChosenFromDeckAndAddedToHand, cardsToBeDestroyed, cardsToTakeControlOf, cardsToBeChosenFromDeckAndSentToGraveyard, cardsToBeRitualSummoned, CardPosition.FACE_UP_ACTIVATED_POSITION));
            } else if (Card.isCardATrap(card)) {
                uninterruptedActions.add(new Action(ActionType.OPPONENT_ACTIVATING_TRAP, 2, mainCardLocation, null, null, cardsToBeDiscarded, cardsToBeChosenToApplyEquipSpellTo, cardsToBeSpecialSummoned, cardsToBeChosenFromDeckAndAddedToHand, cardsToBeDestroyed, cardsToTakeControlOf, cardsToBeChosenFromDeckAndSentToGraveyard, cardsToBeRitualSummoned, CardPosition.FACE_UP_ACTIVATED_POSITION));
                actions.add(new Action(ActionType.OPPONENT_ACTIVATING_TRAP, 2, mainCardLocation, null, null, cardsToBeDiscarded, cardsToBeChosenToApplyEquipSpellTo, cardsToBeSpecialSummoned, cardsToBeChosenFromDeckAndAddedToHand, cardsToBeDestroyed, cardsToTakeControlOf, cardsToBeChosenFromDeckAndSentToGraveyard, cardsToBeRitualSummoned, CardPosition.FACE_UP_ACTIVATED_POSITION));
            }//add action that conducts effects of the card
        }
        cardsToBeDiscarded.clear();
        cardsToBeChosenToApplyEquipSpellTo.clear();
        cardsToBeSpecialSummoned.clear();
        cardsToBeChosenFromDeckAndAddedToHand.clear();
        cardsToBeDestroyed.clear();
        cardsToTakeControlOf.clear();
    }

    public String canChainingOccur(int index) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(index);
        Action uninterruptedAction = uninterruptedActions.get(uninterruptedActions.size() - 1);
        MessagesFromEffectToControllers messagesFromEffectToControllers = null;
        int actionTurn = uninterruptedAction.getActionTurn();
        if (uninterruptedAction.getActionType().equals(ActionType.ALLY_ACTIVATING_SPELL)) {
            messagesFromEffectToControllers = Effect.canSpellTrapCardBeActivatedInChain(ActionType.ALLY_ACTIVATING_SPELL, actionTurn);
        } else if (uninterruptedAction.getActionType().equals(ActionType.ALLY_ACTIVATING_TRAP)) {
            messagesFromEffectToControllers = Effect.canSpellTrapCardBeActivatedInChain(ActionType.ALLY_ACTIVATING_TRAP, actionTurn);
        } else if (uninterruptedAction.getActionType().equals(ActionType.OPPONENT_ACTIVATING_SPELL)) {
            messagesFromEffectToControllers = Effect.canSpellTrapCardBeActivatedInChain(ActionType.OPPONENT_ACTIVATING_SPELL, actionTurn);
        } else if (uninterruptedAction.getActionType().equals(ActionType.OPPONENT_ACTIVATING_TRAP)) {
            messagesFromEffectToControllers = Effect.canSpellTrapCardBeActivatedInChain(ActionType.OPPONENT_ACTIVATING_TRAP, actionTurn);
        }
        return applyEffectsIfChainingWasPossible(messagesFromEffectToControllers, index);
    }


}
