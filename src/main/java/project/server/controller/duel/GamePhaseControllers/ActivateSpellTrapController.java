package project.server.controller.duel.GamePhaseControllers;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import project.server.controller.duel.CardEffects.Effect;
import project.server.controller.duel.CardEffects.MessagesFromEffectToControllers;
import project.server.controller.duel.GamePackage.Action;
import project.model.ActionType;
import project.server.controller.duel.GamePackage.DuelBoard;
import project.server.controller.duel.GamePackage.DuelController;
import project.server.controller.duel.PreliminaryPackage.GameManager;
import project.model.Utility.Utility;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.CardPosition;
import project.model.cardData.General.RowOfCardLocation;
import project.model.cardData.MonsterCardData.MonsterCard;
import project.model.cardData.MonsterCardData.MonsterCardValue;
import project.model.cardData.SpellCardData.SpellCard;
import project.model.cardData.SpellCardData.SpellCardValue;
import project.model.cardData.TrapCardData.TrapCard;

public class ActivateSpellTrapController extends ChainController {
    private boolean areWeLookingForFurtherInputToActivateSpellTrap;
    private boolean areWeLookingForACardNameToBeInserted;
    private String messageSentToUser;
    private ArrayList<String> messagesSentToUser;
    private CardLocation mainCardLocation;
    private CardPosition mainCardPosition;
    private ArrayList<CardLocation> cardsToBeTargeted;
    private ArrayList<CardLocation> cardsToBeDiscarded;
    private ArrayList<CardLocation> cardsToBeChosenToApplyEquipSpellTo;
    private ArrayList<CardLocation> cardsToBeSpecialSummoned;
    private ArrayList<CardLocation> cardsToBeRitualSummoned;
    private ArrayList<CardLocation> cardsToBeChosenFromDeckAndAddedToHand;
    private ArrayList<CardLocation> cardsToBeDestroyed;
    private ArrayList<CardLocation> cardsToTakeControlOf;
    private ArrayList<CardLocation> cardsToBeChosenFromDeckAndSentToGraveyard;
    private ArrayList<CardPosition> cardsToBeSpecialSummonedInFaceUpAttackPositionOrDefensePosition;
    private ArrayList<Integer> sumOfLevelsOfChosenMonsters;
    private String optionalCardNameInput;

    public ActivateSpellTrapController() {
        areWeLookingForFurtherInputToActivateSpellTrap = false;
        isClassWaitingForChainCardToBeSelected = false;
        isGoingToChangeTurnsForChaining = false;
        areWeLookingForACardNameToBeInserted = false;
        optionalCardNameInput = "";
        mainCardPosition = CardPosition.FACE_UP_ACTIVATED_POSITION;
        messagesSentToUser = new ArrayList<>();
        cardsToBeTargeted = new ArrayList<>();
        cardsToBeDiscarded = new ArrayList<>();
        cardsToBeChosenToApplyEquipSpellTo = new ArrayList<>();
        cardsToBeSpecialSummoned = new ArrayList<>();
        cardsToBeRitualSummoned = new ArrayList<>();
        cardsToBeChosenFromDeckAndAddedToHand = new ArrayList<>();
        cardsToBeDestroyed = new ArrayList<>();
        cardsToTakeControlOf = new ArrayList<>();
        cardsToBeChosenFromDeckAndSentToGraveyard = new ArrayList<>();
        sumOfLevelsOfChosenMonsters = new ArrayList<>();
        cardsToBeSpecialSummonedInFaceUpAttackPositionOrDefensePosition = new ArrayList<>();
    }

    public boolean isAreWeLookingForFurtherInputToActivateSpellTrap() {
        return areWeLookingForFurtherInputToActivateSpellTrap;
    }

    public boolean isAreWeLookingForACardNameToBeInserted() {
        return areWeLookingForACardNameToBeInserted;
    }

    public void setGoingToChangeTurnsForChaining(boolean goingToChangeTurnsForChaining) {
        isGoingToChangeTurnsForChaining = goingToChangeTurnsForChaining;
    }

    public void setMainCardLocation(CardLocation mainCardLocation) {
        this.mainCardLocation = mainCardLocation;
    }

    public void setMessagesSentToUser(ArrayList<String> messagesSentToUser) {
        this.messagesSentToUser = messagesSentToUser;
    }

    public void setAreWeLookingForFurtherInputToActivateSpellTrap(boolean areWeLookingForFurtherInputToActivateSpellTrap) {
        this.areWeLookingForFurtherInputToActivateSpellTrap = areWeLookingForFurtherInputToActivateSpellTrap;
    }

    public String activateSpellTrapEffectInputAnalysis(String string, String token) {
        String inputRegex = "(?<=\\n|^)activate[\\s]+effect(?=\\n|$)";
        Matcher matcher = Utility.getCommandMatcher(string, inputRegex);
        if (Utility.isMatcherCorrectWithoutErrorPrinting(matcher)) {
            return startChecking(token);
        }
        return null;
    }

    public String startChecking(String token) {
        String resultOfChecking = Utility.isACardSelected(token, "", false);
        if (!resultOfChecking.equals("")) {
            return resultOfChecking;
        } else {
            return checkTypeOfCardForActivation(token);
        }
    }

    public String checkTypeOfCardForActivation(String token) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        Card card = duelBoard.getCardByCardLocation(selectedCardLocations.get(selectedCardLocations.size() - 1));
        ActivateMonsterController activateMonsterController = GameManager.getActivateMonsterControllerByIndex(token);
        if (Card.isCardAMonster(card)) {
            return activateMonsterController.activateMonsterEffectInputAnalysis("activate effect", token);
        } else {
            return checkCorrectPhase(token);
        }
    }

    public String checkCorrectPhase(String token) {
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        int turn = duelController.getTurn();
        int fakeTurn = duelController.getFakeTurn();
        String resultOfChecking;
        //wrong if
        if (turn == fakeTurn) {
            resultOfChecking = Utility.areWeInMainPhase(token);
            if (!resultOfChecking.equals("")) {
                return resultOfChecking;
            } else {
                return isSpellTrapCardAlreadyActivated(token);
            }
        } else {
            return isSpellTrapCardAlreadyActivated(token);
        }
    }

    public String isSpellTrapCardAlreadyActivated(String token) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        CardLocation cardLocation = selectedCardLocations.get(selectedCardLocations.size() - 1);
        Card card = duelBoard.getCardByCardLocation(cardLocation);
        boolean isAlreadyActivated = false;
        if (Card.isCardASpell(card)) {
            SpellCard spellCard = (SpellCard) card;
            isAlreadyActivated = spellCard.isCardAlreadyActivated();
            if (!cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_SPELL_ZONE) &&
                !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_SPELL_ZONE) &&
                !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_HAND_ZONE) &&
                !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_HAND_ZONE) &&
                !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE) &&
                !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE)) {
                return "you can't activate spell card that is neither set on the field nor is in your hand";
            }
        } else if (Card.isCardATrap(card)) {
            TrapCard trapCard = (TrapCard) card;
            isAlreadyActivated = trapCard.isCardAlreadyActivated();
            if (!cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_SPELL_ZONE) && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_SPELL_ZONE)) {
                return "you can't activate trap card that is not set on the field";
            }
            if (trapCard.getTurnCardWasSet() >= GameManager.getDuelControllerByIndex(token).getTotalTurnsUntilNow()) {
                return "you can't activate trap card on the same turn you set it";
            }
        }
        if (isAlreadyActivated) {
            return "you have already activated this card";
        } else {
            return isSpellCardZoneFull(token);
        }
    }

    public String isSpellCardZoneFull(String token) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        CardLocation cardLocation = selectedCardLocations.get(selectedCardLocations.size() - 1);
        Card card = duelBoard.getCardByCardLocation(cardLocation);
        //probably should be turn
        int fakeTurn = duelController.getFakeTurn();
        RowOfCardLocation rowOfCardLocation = RowOfCardLocation.OPPONENT_SPELL_ZONE;
        if (fakeTurn == 1) {
            if (Card.isCardASpell(card) && ((SpellCard) card).getSpellCardValue().equals(SpellCardValue.FIELD)) {
                rowOfCardLocation = RowOfCardLocation.ALLY_SPELL_FIELD_ZONE;
            } else {
                rowOfCardLocation = RowOfCardLocation.ALLY_SPELL_ZONE;
            }
        } else if (fakeTurn == 2) {
            if (Card.isCardASpell(card) && ((SpellCard) card).getSpellCardValue().equals(SpellCardValue.FIELD)) {
                rowOfCardLocation = RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE;
            } else {
                rowOfCardLocation = RowOfCardLocation.OPPONENT_SPELL_ZONE;
            }
        }
        if (duelBoard.isZoneFull(rowOfCardLocation) && (rowOfCardLocation.equals(RowOfCardLocation.ALLY_SPELL_ZONE) || rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_SPELL_ZONE))
            && (cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_HAND_ZONE) || cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_HAND_ZONE))) {
            return "spell zone is full";
        } else {
            String output = arePreparationsCompleteForSpellTrapActivation(token);
            if (output.startsWith("pre")) {
                return output;
            } else if (output.equals("nothing needed")) {
                createActionForActivatingSpellTrap(token);
                selectCardController.resetSelectedCardLocationList();
                String finalOutput = Action.conductUninterruptedAction(token);
                String canChainingOccur = canChainingOccur(token);
                if (canChainingOccur.equals("")) {
                    return finalOutput + "\n" + Action.conductAllActions(token);
                }
                return finalOutput + "\n" + canChainingOccur;
            } else {
                if (card.getCardName().equals("mind crush")) {
                    areWeLookingForACardNameToBeInserted = true;
                } else {
                    areWeLookingForFurtherInputToActivateSpellTrap = true;
                }
                return output;
            }
        }
    }

    public String arePreparationsCompleteForSpellTrapActivation(String token) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        CardLocation selectedCardLocation = selectedCardLocations.get(selectedCardLocations.size() - 1);
        Card card = GameManager.getDuelBoardByIndex(token).getCardByCardLocation(selectedCardLocation);
        //if (Card.isCardATrap(card))
        if (Card.isCardASpell(card) || Card.isCardATrap(card) && ((TrapCard) card).getNormalTrapCardEffects().size() > 0) {
            MessagesFromEffectToControllers messagesFromEffectToControllers = Effect.arePreparationsCompleteForActivatingSpellTrapCard(selectedCardLocation, token);
            if (messagesFromEffectToControllers.equals(MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_SPELL_ARE_NOT_COMPLETE)) {
                return "preparations of this spell are not complete yet";
            } else if (messagesFromEffectToControllers.equals(MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_TRAP_ARE_NOT_COMPLETE)) {
                return "preparations of this trap are not complete yet";
            } else if (messagesFromEffectToControllers.equals(MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_SPELL_ARE_COMPLETE)) {
                return checkWhatIsNeededForSpellTrapActivation(token);
            } else if (messagesFromEffectToControllers.equals(MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_TRAP_ARE_COMPLETE)) {
                return checkWhatIsNeededForSpellTrapActivation(token);
            }
            return null;
        }
        return "preparations of this trap are not complete yet";
    }

    public String checkWhatIsNeededForSpellTrapActivation(String token) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        CardLocation spellTrapCardActivating = selectedCardLocations.get(selectedCardLocations.size() - 1);
        ArrayList<String> resultOfChecking = Effect.inputsNeededForActivatingSpellTrapCard(spellTrapCardActivating, token);
        messagesSentToUser = resultOfChecking;
        mainCardLocation = spellTrapCardActivating;
        return resultOfChecking.get(resultOfChecking.size() - 1);
    }

    public String redirectInput(String token) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        CardLocation cardLocation = selectedCardLocations.get(selectedCardLocations.size() - 1);
        Card card = duelBoard.getCardByCardLocation(cardLocation);
        int fakeTurn = duelController.getFakeTurn();
        String canChainingOccur = "";
        String output = "";
        String message = messagesSentToUser.get(messagesSentToUser.size() - 1);
        if (message.startsWith("please choose one ") && !message.startsWith("please choose one of your opponent's monsters")
            && !message.startsWith("please choose one spell") && !message.startsWith("please choose one card")) {
            if (fakeTurn == 1 && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) {
                return "invalid selection\nplease try again";
            } else if (fakeTurn == 2 && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) {
                return "invalid selection\nplease try again";
            } else if (!Card.isCardAMonster(card)) {
                return "invalid selection\nplease try again";
            } else {
                String regexString = "([a-z_]+)/";
                Pattern pattern = Pattern.compile(regexString);
                Matcher matcher = pattern.matcher(message);
                boolean ok = false;
                while (matcher.find()) {
                    if (matcher.group(1).equals(((MonsterCard) card).getMonsterCardFamily().toString().toLowerCase())) {
                        ok = true;
                    }
                }
                if (!ok) {
                    return "invalid selection\nplease try again";
                } else {
                    cardsToBeChosenToApplyEquipSpellTo.add(cardLocation);
                    boolean isMoreInputNeeded = isMoreInputNeededWhenOneInputIsGivenCorrectly(token);
                    if (isMoreInputNeeded) {
                        return messagesSentToUser.get(messagesSentToUser.size() - 1);
                    } else {
                        output = Action.conductUninterruptedAction(token);
                        canChainingOccur = canChainingOccur(token);
                    }
                    //used to give fakeTurn as input
                }
            }
        } else if (message.startsWith("show graveyard\nplease choose one monster from either graveyard")) {
            if (!cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_GRAVEYARD_ZONE)
                && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE)) {
                return "invalid selection\nplease try again";
            } else if (!Card.isCardAMonster(card)) {
                return "invalid selection\nplease try again";
            } else {
                cardsToBeSpecialSummoned.add(cardLocation);
                boolean isMoreInputNeeded = isMoreInputNeededWhenOneInputIsGivenCorrectly(token);
                if (isMoreInputNeeded) {
                    return messagesSentToUser.get(messagesSentToUser.size() - 1);
                } else {
                    output = Action.conductUninterruptedAction(token);
                    canChainingOccur = canChainingOccur(token);
                }
            }
        } else if (message.startsWith("show graveyard\nplease choose one monster from your own graveyard")) {
            if (fakeTurn == 1 && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_GRAVEYARD_ZONE)) {
                return "invalid selection\nplease try again";
            } else if (fakeTurn == 2 && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE)) {
                return "invalid selection\nplease try again";
            } else if (!Card.isCardAMonster(card)) {
                return "invalid selection\nplease try again";
            } else {
                cardsToBeSpecialSummoned.add(cardLocation);
                boolean isMoreInputNeeded = isMoreInputNeededWhenOneInputIsGivenCorrectly(token);
                if (isMoreInputNeeded) {
                    return messagesSentToUser.get(messagesSentToUser.size() - 1);
                } else {
                    output = Action.conductUninterruptedAction(token);
                    canChainingOccur = canChainingOccur(token);
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
                boolean isMoreInputNeeded = isMoreInputNeededWhenOneInputIsGivenCorrectly(token);
                if (isMoreInputNeeded) {
                    return messagesSentToUser.get(messagesSentToUser.size() - 1);
                } else {
                    output = Action.conductUninterruptedAction(token);
                    canChainingOccur = canChainingOccur(token);
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
                boolean isMoreInputNeeded = isMoreInputNeededWhenOneInputIsGivenCorrectly(token);
                if (isMoreInputNeeded) {
                    return messagesSentToUser.get(messagesSentToUser.size() - 1);
                } else {
                    output = Action.conductUninterruptedAction(token);
                    canChainingOccur = canChainingOccur(token);
                }
                //used to give fakeTurn as input
            }
        } else if (message.startsWith("please choose one card from your hand to discard")) {
            if (fakeTurn == 1 && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_HAND_ZONE)) {
                return "invalid selection\nplease try again";
            } else if (fakeTurn == 2 && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_HAND_ZONE)) {
                return "invalid selection\nplease try again";
            } else {
                cardsToBeDiscarded.add(cardLocation);
                boolean isMoreInputNeeded = isMoreInputNeededWhenOneInputIsGivenCorrectly(token);
                if (isMoreInputNeeded) {
                    return messagesSentToUser.get(messagesSentToUser.size() - 1);
                } else {
                    output = Action.conductUninterruptedAction(token);
                    canChainingOccur = canChainingOccur(token);
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
                for (int i = 0; i < cardsToBeChosenFromDeckAndSentToGraveyard.size(); i++) {
                    if (cardsToBeChosenFromDeckAndSentToGraveyard.get(i).getRowOfCardLocation().equals(cardLocation.getRowOfCardLocation()) && cardsToBeChosenFromDeckAndSentToGraveyard.get(i).getIndex() == cardLocation.getIndex()) {
                        return "you have already chosen this normal monster for sending to graveyard\nplease try again";
                    }
                }
                cardsToBeChosenFromDeckAndSentToGraveyard.add(cardLocation);
                sumOfLevelsOfChosenMonsters.add(((MonsterCard) card).getLevel());
                if (areSumOfNormalMonsterLevelsEqualToARitualMonsterLevel(fakeTurn, token)) {
                    messagesSentToUser.remove(messagesSentToUser.size() - 1);
                    if (messagesSentToUser.size() == 0) {
                        areWeLookingForFurtherInputToActivateSpellTrap = false;
                        selectCardController.resetSelectedCardLocationList();
                        createActionForActivatingSpellTrap(token);
                        output = Action.conductUninterruptedAction(token);
                        canChainingOccur = canChainingOccur(token);
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
                boolean isMoreInputNeeded = isMoreInputNeededWhenOneInputIsGivenCorrectly(token);
                if (isMoreInputNeeded) {
                    return messagesSentToUser.get(messagesSentToUser.size() - 1);
                } else {
                    output = Action.conductUninterruptedAction(token);
                    canChainingOccur = canChainingOccur(token);
                }
                //used to give fakeTurn as input
            }
        } else if (message.startsWith("please choose if you want")) {
            String input = duelController.getLatestInput();
            if (input.equals("attacking")) {
                cardsToBeSpecialSummonedInFaceUpAttackPositionOrDefensePosition.add(CardPosition.FACE_UP_ATTACK_POSITION);
                boolean isMoreInputNeeded = isMoreInputNeededWhenOneInputIsGivenCorrectly(token);
                if (isMoreInputNeeded) {
                    return messagesSentToUser.get(messagesSentToUser.size() - 1);
                } else {
                    output = Action.conductUninterruptedAction(token);
                    canChainingOccur = canChainingOccur(token);
                }
            } else if (input.equals("defensive")) {
                cardsToBeSpecialSummonedInFaceUpAttackPositionOrDefensePosition.add(CardPosition.FACE_UP_DEFENSE_POSITION);
                boolean isMoreInputNeeded = isMoreInputNeededWhenOneInputIsGivenCorrectly(token);
                if (isMoreInputNeeded) {
                    return messagesSentToUser.get(messagesSentToUser.size() - 1);
                } else {
                    output = Action.conductUninterruptedAction(token);
                    canChainingOccur = canChainingOccur(token);
                }
            } else {
                return "invalid input\nplease enter attack or defense";
            }
        } else if (message.startsWith("please enter")) {
            optionalCardNameInput = duelController.getLatestInput();
            boolean isMoreInputNeeded = isMoreInputNeededWhenOneInputIsGivenCorrectly(token);
            if (isMoreInputNeeded) {
                return messagesSentToUser.get(messagesSentToUser.size() - 1);
            } else {
                areWeLookingForACardNameToBeInserted = false;
                output = Action.conductUninterruptedAction(token);
                canChainingOccur = canChainingOccur(token);
            }
            //used to give fakeTurn as input
        } else if (message.startsWith("please choose one spell or trap")) {
            if (!cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_SPELL_ZONE)
                && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE)
                && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_SPELL_ZONE)
                && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE)) {
                return "invalid selection\nplease try again";
            } else if (card == null) {
                return "there is no card here\nplease try again";
            } else {
                cardsToBeTargeted.add(cardLocation);
                boolean isMoreInputNeeded = isMoreInputNeededWhenOneInputIsGivenCorrectly(token);
                if (isMoreInputNeeded) {
                    return messagesSentToUser.get(messagesSentToUser.size() - 1);
                } else {
                    output = Action.conductUninterruptedAction(token);
                    canChainingOccur = canChainingOccur(token);
                }
            }
        } else if (message.startsWith("please choose up to two spell")) {
            String currentInput = duelController.getLatestInput();
            if (currentInput.equals("finish selecting")) {
                areWeLookingForFurtherInputToActivateSpellTrap = false;
                selectCardController.resetSelectedCardLocationList();
                createActionForActivatingSpellTrap(token);
                output = Action.conductUninterruptedAction(token);
                canChainingOccur = canChainingOccur(token);
            } else {
                if (!cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_SPELL_ZONE)
                    && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE)
                    && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_SPELL_ZONE)
                    && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE)) {
                    return "invalid selection\nplease try again";
                } else if (card == null) {
                    return "there is no card here\nplease try again";
                } else {
                    cardsToBeTargeted.add(cardLocation);
                    boolean isMoreInputNeeded = isMoreInputNeededWhenOneInputIsGivenCorrectly(token);
                    if (isMoreInputNeeded) {
                        return "you can select another spell or trap card to destroy";
                    } else {
                        output = Action.conductUninterruptedAction(token);
                        canChainingOccur = canChainingOccur(token);
                    }
                }
            }
        } else if (message.startsWith("please choose up a face up monster")) {
            if (!cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE)
                && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) {
                return "this card is not in the monster zone\nplease try again";
            } else if (card == null) {
                return "there is no card here\nplease try again";
            } else if (!(card).getCardPosition().equals(CardPosition.FACE_UP_DEFENSE_POSITION) &&
                !(card).getCardPosition().equals(CardPosition.FACE_UP_ATTACK_POSITION)) {
                return "this card is not face up.\nplease try again";
            } else {
                cardsToBeTargeted.add(cardLocation);
                boolean isMoreInputNeeded = isMoreInputNeededWhenOneInputIsGivenCorrectly(token);
                if (isMoreInputNeeded) {
                    return messagesSentToUser.get(messagesSentToUser.size() - 1);
                } else {
                    output = Action.conductUninterruptedAction(token);
                    canChainingOccur = canChainingOccur(token);
                }
            }
        } else if (message.startsWith("please choose a level 5 or higher normal monster from")) {
            System.out.println("\n\n\ncard name chosen for special summoning 8is "+ card.getCardName());
            if (fakeTurn == 1 && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_HAND_ZONE)
                || fakeTurn == 2 && !cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_HAND_ZONE)) {
                return "invalid selection\nplease try again";
            } else if (Card.isCardAMonster(card)) {
                return "this is not a monster card.\nplease try again";
            } else if (!((MonsterCard) card).getMonsterCardValue().equals(MonsterCardValue.NORMAL)) {
                return "this is not a normal monster.\nplease try again.";
            } else if (((MonsterCard) card).getLevel() < 5) {
                return "level of this card is less than 5.\nplease try again";
            } else {
                cardsToBeSpecialSummoned.add(cardLocation);
                boolean isMoreInputNeeded = isMoreInputNeededWhenOneInputIsGivenCorrectly(token);
                if (isMoreInputNeeded) {
                    return messagesSentToUser.get(messagesSentToUser.size() - 1);
                } else {
                    output = Action.conductUninterruptedAction(token);
                    canChainingOccur = canChainingOccur(token);
                }
            }
        }
        if (!canChainingOccur.equals("")) {
            output += canChainingOccur;
            return output;
        }
        return output + Action.conductAllActions(token);
    }


    private boolean areSumOfNormalMonsterLevelsEqualToARitualMonsterLevel(int turn, String token) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
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

    private boolean isMoreInputNeededWhenOneInputIsGivenCorrectly(String token) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        messagesSentToUser.remove(messagesSentToUser.size() - 1);
        if (messagesSentToUser.size() > 0) {
            return true;
        } else {
            areWeLookingForFurtherInputToActivateSpellTrap = false;
            selectCardController.resetSelectedCardLocationList();
            createActionForActivatingSpellTrap(token);
            return false;
        }
    }

    public void createActionForActivatingSpellTrap(String token) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        int fakeTurn = duelController.getFakeTurn();
        Card card = duelBoard.getCardByCardLocation(mainCardLocation);
        selectCardController.resetSelectedCardLocationList();
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(token);
        ArrayList<Action> actions = GameManager.getActionsByIndex(token);
        if (fakeTurn == 1) {
            if (Card.isCardASpell(card)) {
                uninterruptedActions.add(new Action(ActionType.ALLY_ACTIVATING_SPELL, 1, mainCardLocation, cardsToBeTargeted, null, cardsToBeDiscarded, cardsToBeChosenToApplyEquipSpellTo, cardsToBeSpecialSummoned, cardsToBeChosenFromDeckAndAddedToHand, cardsToBeDestroyed, cardsToTakeControlOf, cardsToBeChosenFromDeckAndSentToGraveyard, cardsToBeRitualSummoned, CardPosition.FACE_UP_ACTIVATED_POSITION, optionalCardNameInput, cardsToBeSpecialSummonedInFaceUpAttackPositionOrDefensePosition));
                actions.add(new Action(ActionType.ALLY_ACTIVATING_SPELL, 1, mainCardLocation, cardsToBeTargeted, null, cardsToBeDiscarded, cardsToBeChosenToApplyEquipSpellTo, cardsToBeSpecialSummoned, cardsToBeChosenFromDeckAndAddedToHand, cardsToBeDestroyed, cardsToTakeControlOf, cardsToBeChosenFromDeckAndSentToGraveyard, cardsToBeRitualSummoned, CardPosition.FACE_UP_ACTIVATED_POSITION, optionalCardNameInput, cardsToBeSpecialSummonedInFaceUpAttackPositionOrDefensePosition));

            } else if (Card.isCardATrap(card)) {
                uninterruptedActions.add(new Action(ActionType.ALLY_ACTIVATING_TRAP, 1, mainCardLocation, cardsToBeTargeted, null, cardsToBeDiscarded, cardsToBeChosenToApplyEquipSpellTo, cardsToBeSpecialSummoned, cardsToBeChosenFromDeckAndAddedToHand, cardsToBeDestroyed, cardsToTakeControlOf, cardsToBeChosenFromDeckAndSentToGraveyard, cardsToBeRitualSummoned, CardPosition.FACE_UP_ACTIVATED_POSITION, optionalCardNameInput, cardsToBeSpecialSummonedInFaceUpAttackPositionOrDefensePosition));
                actions.add(new Action(ActionType.ALLY_ACTIVATING_TRAP, 1, mainCardLocation, cardsToBeTargeted, null, cardsToBeDiscarded, cardsToBeChosenToApplyEquipSpellTo, cardsToBeSpecialSummoned, cardsToBeChosenFromDeckAndAddedToHand, cardsToBeDestroyed, cardsToTakeControlOf, cardsToBeChosenFromDeckAndSentToGraveyard, cardsToBeRitualSummoned, CardPosition.FACE_UP_ACTIVATED_POSITION, optionalCardNameInput, cardsToBeSpecialSummonedInFaceUpAttackPositionOrDefensePosition));
            }
        } else if (fakeTurn == 2) {
            if (Card.isCardASpell(card)) {
                uninterruptedActions.add(new Action(ActionType.OPPONENT_ACTIVATING_SPELL, 2, mainCardLocation, cardsToBeTargeted, null, cardsToBeDiscarded, cardsToBeChosenToApplyEquipSpellTo, cardsToBeSpecialSummoned, cardsToBeChosenFromDeckAndAddedToHand, cardsToBeDestroyed, cardsToTakeControlOf, cardsToBeChosenFromDeckAndSentToGraveyard, cardsToBeRitualSummoned, CardPosition.FACE_UP_ACTIVATED_POSITION, optionalCardNameInput, cardsToBeSpecialSummonedInFaceUpAttackPositionOrDefensePosition));
                actions.add(new Action(ActionType.OPPONENT_ACTIVATING_SPELL, 2, mainCardLocation, cardsToBeTargeted, null, cardsToBeDiscarded, cardsToBeChosenToApplyEquipSpellTo, cardsToBeSpecialSummoned, cardsToBeChosenFromDeckAndAddedToHand, cardsToBeDestroyed, cardsToTakeControlOf, cardsToBeChosenFromDeckAndSentToGraveyard, cardsToBeRitualSummoned, CardPosition.FACE_UP_ACTIVATED_POSITION, optionalCardNameInput, cardsToBeSpecialSummonedInFaceUpAttackPositionOrDefensePosition));
            } else if (Card.isCardATrap(card)) {
                uninterruptedActions.add(new Action(ActionType.OPPONENT_ACTIVATING_TRAP, 2, mainCardLocation, cardsToBeTargeted, null, cardsToBeDiscarded, cardsToBeChosenToApplyEquipSpellTo, cardsToBeSpecialSummoned, cardsToBeChosenFromDeckAndAddedToHand, cardsToBeDestroyed, cardsToTakeControlOf, cardsToBeChosenFromDeckAndSentToGraveyard, cardsToBeRitualSummoned, CardPosition.FACE_UP_ACTIVATED_POSITION, optionalCardNameInput, cardsToBeSpecialSummonedInFaceUpAttackPositionOrDefensePosition));
                actions.add(new Action(ActionType.OPPONENT_ACTIVATING_TRAP, 2, mainCardLocation, cardsToBeTargeted, null, cardsToBeDiscarded, cardsToBeChosenToApplyEquipSpellTo, cardsToBeSpecialSummoned, cardsToBeChosenFromDeckAndAddedToHand, cardsToBeDestroyed, cardsToTakeControlOf, cardsToBeChosenFromDeckAndSentToGraveyard, cardsToBeRitualSummoned, CardPosition.FACE_UP_ACTIVATED_POSITION, optionalCardNameInput, cardsToBeSpecialSummonedInFaceUpAttackPositionOrDefensePosition));
            }
        }
        optionalCardNameInput = "";
        cardsToBeTargeted.clear();
        cardsToBeDiscarded.clear();
        cardsToBeChosenToApplyEquipSpellTo.clear();
        cardsToBeSpecialSummoned.clear();
        cardsToBeChosenFromDeckAndAddedToHand.clear();
        cardsToBeDestroyed.clear();
        cardsToTakeControlOf.clear();
        cardsToBeChosenFromDeckAndSentToGraveyard.clear();
        cardsToBeRitualSummoned.clear();
    }

    public String canChainingOccur(String token) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(token);
        Action uninterruptedAction = uninterruptedActions.get(uninterruptedActions.size() - 1);
        Card mainCard = duelBoard.getCardByCardLocation(uninterruptedAction.getFinalMainCardLocation());
        MessagesFromEffectToControllers messagesFromEffectToControllers = null;
        int actionTurn = uninterruptedAction.getActionTurn();
        if (uninterruptedAction.getActionType().equals(ActionType.ALLY_ACTIVATING_SPELL)) {
            messagesFromEffectToControllers = Effect.canSpellTrapCardBeActivatedInChain(ActionType.ALLY_ACTIVATING_SPELL, actionTurn, mainCard.getSpeedOfCard(), token);
        } else if (uninterruptedAction.getActionType().equals(ActionType.ALLY_ACTIVATING_TRAP)) {
            messagesFromEffectToControllers = Effect.canSpellTrapCardBeActivatedInChain(ActionType.ALLY_ACTIVATING_TRAP, actionTurn, mainCard.getSpeedOfCard(), token);
        } else if (uninterruptedAction.getActionType().equals(ActionType.OPPONENT_ACTIVATING_SPELL)) {
            messagesFromEffectToControllers = Effect.canSpellTrapCardBeActivatedInChain(ActionType.OPPONENT_ACTIVATING_SPELL, actionTurn, mainCard.getSpeedOfCard(), token);
        } else if (uninterruptedAction.getActionType().equals(ActionType.OPPONENT_ACTIVATING_TRAP)) {
            messagesFromEffectToControllers = Effect.canSpellTrapCardBeActivatedInChain(ActionType.OPPONENT_ACTIVATING_TRAP, actionTurn, mainCard.getSpeedOfCard(), token);
        }
        return applyEffectsIfChainingWasPossible(messagesFromEffectToControllers, token);
    }

    public void clearAllVariablesOfThisClass() {
        areWeLookingForFurtherInputToActivateSpellTrap = false;
        areWeLookingForACardNameToBeInserted = false;
        messageSentToUser = "";
        messagesSentToUser.clear();
        mainCardLocation = null;
        mainCardPosition = null;
        cardsToBeTargeted.clear();
        cardsToBeDiscarded.clear();
        cardsToBeChosenToApplyEquipSpellTo.clear();
        cardsToBeSpecialSummoned.clear();
        cardsToBeRitualSummoned.clear();
        cardsToBeChosenFromDeckAndAddedToHand.clear();
        cardsToBeDestroyed.clear();
        cardsToTakeControlOf.clear();
        cardsToBeChosenFromDeckAndSentToGraveyard.clear();
        cardsToBeSpecialSummonedInFaceUpAttackPositionOrDefensePosition.clear();
        sumOfLevelsOfChosenMonsters.clear();
        optionalCardNameInput = "";
    }
}
