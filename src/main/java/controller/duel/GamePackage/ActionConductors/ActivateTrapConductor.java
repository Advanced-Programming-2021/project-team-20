package controller.duel.GamePackage.ActionConductors;

import java.util.ArrayList;
import java.util.Random;

import controller.duel.CardEffects.MonsterEffectEnums.BeingAttackedEffect;
import controller.duel.CardEffects.MonsterEffectEnums.UponSummoningEffect;
import controller.duel.CardEffects.SpellEffectEnums.NormalSpellCardEffect;
import controller.duel.CardEffects.TrapEffectEnums.*;
import controller.duel.GamePackage.Action;
import controller.duel.GamePackage.ActionType;
import controller.duel.GamePackage.DuelBoard;
import controller.duel.GamePackage.DuelController;
import controller.duel.GamePackage.PhaseInGame;
import controller.duel.GamePhaseControllers.PhaseController;
import controller.duel.GamePhaseControllers.TributeSummonController;
import controller.duel.PreliminaryPackage.GameManager;
import model.cardData.General.Card;
import model.cardData.General.CardLocation;
import model.cardData.General.CardPosition;
import model.cardData.General.RowOfCardLocation;
import model.cardData.MonsterCardData.MonsterCard;
import model.cardData.SpellCardData.SpellCard;
import model.cardData.TrapCardData.TrapCard;

public class ActivateTrapConductor {

    public static String conductActivatingTrapUninterruptedAction(int index, int numberInListOfActions) {
        Action uninterruptedAction = GameManager.getUninterruptedActionsByIndex(index).get(numberInListOfActions);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        CardLocation mainTrapCardLocation = uninterruptedAction.getMainCardLocation();
        Card mainTrapCard = duelBoard.getCardByCardLocation(mainTrapCardLocation);
        RowOfCardLocation rowOfMainTrapCardLocation = mainTrapCardLocation.getRowOfCardLocation();
        if (rowOfMainTrapCardLocation.equals(RowOfCardLocation.ALLY_HAND_ZONE) || rowOfMainTrapCardLocation.equals(RowOfCardLocation.OPPONENT_HAND_ZONE)) {
            duelBoard.removeCardByCardLocation(mainTrapCardLocation);
            int actionTurn = uninterruptedAction.getActionTurn();
            duelBoard.addCardToSpellZone(mainTrapCard, actionTurn);
        }
        mainTrapCard.setCardPosition(CardPosition.FACE_UP_ACTIVATED_POSITION);
        return "trap card ready for activation";
    }

    public static String conductActivatingTrap(int index, int numberInListOfActions) {
        Action action = GameManager.getActionsByIndex(index).get(numberInListOfActions);
        Action previousAction = null;
        if (numberInListOfActions > 0) {
            previousAction = GameManager.getActionsByIndex(index).get(numberInListOfActions - 1);
        }
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        CardLocation mainCardLocation = action.getFinalMainCardLocation();
        TrapCard trapCard = (TrapCard) duelBoard.getCardByCardLocation(mainCardLocation);
        if (action.isActionCanceled()) {
            return "\nactivation of trap was canceled";
        }
        return switchCaseAmongAllTrapEffects(index, trapCard, numberInListOfActions);
    }

    public static String switchCaseAmongAllTrapEffects(int index, TrapCard trapCard, int numberInListOfActions) {
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(index);
        ArrayList<Action> actions = GameManager.getActionsByIndex(index);
        ArrayList<ContinuousTrapCardEffect> continuousTrapCardEffects = trapCard.getContinuousTrapCardEffects();
        ArrayList<NormalTrapCardEffect> normalTrapCardEffects = trapCard.getNormalTrapCardEffects();
        boolean trapCardIsNormal = false;
        if (normalTrapCardEffects.contains(NormalTrapCardEffect.SKIP_OPPONENT_DRAW_PHASE_NEXT_TURN)) {
            skipOpponentDrawCardNextPhase(actions.get(numberInListOfActions), index);
            trapCardIsNormal = true;
        }
        if (normalTrapCardEffects.contains(NormalTrapCardEffect.SPECIAL_SUMMON_ONE_MONSTER_IN_YOUR_GRAVEYARD_IN_FACE_UP_ATTACK_POSITION) && numberInListOfActions == 0) {
            specialSummonMonsterFromGraveyard(actions.get(numberInListOfActions), index, CardPosition.FACE_UP_ATTACK_POSITION);
            trapCardIsNormal = true;
        }
        if (normalTrapCardEffects.contains(NormalTrapCardEffect.OPPONENT_DISCARDS_ALL_CARDS_WITH_A_GIVEN_NAME_OTHERWISE_OWNER_LOSES_ONE_RANDOM_CARD)) {
            opponentDiscardsAllCardsOfAGivenNameOtherwiseOwnerLosesARandomCard(actions.get(numberInListOfActions), index);
            trapCardIsNormal = true;
        }
        if (trapCardIsNormal) {
            if (continuousTrapCardEffects.size() == 0) {
                Action thisAction = actions.get(numberInListOfActions);
                CardLocation mainCardLocationInThisAction = thisAction.getMainCardLocation();
                int thisActionTurn = thisAction.getActionTurn();
                SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(mainCardLocationInThisAction, index);
                //sendCardToGraveyardAfterRemoving(mainCardLocationInThisAction, index, thisActionTurn);
            }
            return "trap activated";
        }
        Action uninterruptedAction = uninterruptedActions.get(numberInListOfActions - 1);
        ActionType actionType = uninterruptedAction.getActionType();
        int actionTurn = uninterruptedAction.getActionTurn();
        Action action = actions.get(numberInListOfActions - 1);
        Action thisUninterruptedAction = uninterruptedActions.get(numberInListOfActions);
        //ArrayList<NormalTrapCardEffect> normalTrapCardEffects = trapCard.getNormalTrapCardEffects();
        //note that normalTrapCardEffects don't need to counter previous action so 5 lines above gives null pointer exception
        ArrayList<NormalSummonTrapCardEffect> normalSummonTrapCardEffects = trapCard.getNormalSummonTrapCardEffects();
        ArrayList<TributeSummonTrapCardEffect> tributeSummonTrapCardEffects = trapCard.getTributeSummonTrapCardEffects();
        ArrayList<FlipSummonTrapCardEffect> flipSummonTrapCardEffects = trapCard.getFlipSummonTrapCardEffects();
        ArrayList<RitualSummonTrapCardEffect> ritualSummonTrapCardEffects = trapCard.getRitualSummonTrapCardEffects();
        ArrayList<SpecialSummonTrapCardEffect> specialSummonTrapCardEffects = trapCard.getSpecialSummonTrapCardEffects();

        ArrayList<MonsterAttackingTrapCardEffect> monsterAttackingTrapCardEffects = trapCard.getMonsterAttackingTrapCardEffects();
        ArrayList<SpellCardActivationTrapCardEffect> spellCardActivationTrapCardEffects = trapCard.getSpellCardActivationTrapCardEffects();
        ArrayList<TrapCardActivationTrapCardEffect> trapCardActivationTrapCardEffects = trapCard.getTrapCardActivationTrapCardEffects();
        //ArrayList<MonsterEffectActivationTrapCardEffect> monsterEffectActivationTrapCardEffects = trapCard.getMonsterEffectActivationTrapCardEffect();
        boolean isPreviousUninterruptedActionNormalSummoning = ((actionType.equals(ActionType.ALLY_NORMAL_SUMMONING_MONSTER) && actionTurn == 1) || (actionType.equals(ActionType.OPPONENT_NORMAL_SUMMONING_MONSTER)) && actionTurn == 2);
        boolean isPreviousUninterruptedActionTributeSummoning = ((actionType.equals(ActionType.ALLY_TRIBUTE_SUMMONING_MONSTER) && actionTurn == 1) || (actionType.equals(ActionType.OPPONENT_TRIBUTE_SUMMONING_MONSTER)) && actionTurn == 2);
        boolean isPreviousUninterruptedActionFlipSummoning = ((actionType.equals(ActionType.ALLY_FLIP_SUMMONING_MONSTER) && actionTurn == 1) || (actionType.equals(ActionType.OPPONENT_FLIP_SUMMONING_MONSTER)) && actionTurn == 2);
        boolean isPreviousUninterruptedActionSpecialSummoning = ((actionType.equals(ActionType.ALLY_SPECIAL_SUMMONING_MONSTER) && actionTurn == 1) || (actionType.equals(ActionType.OPPONENT_SPECIAL_SUMMONING_MONSTER)) && actionTurn == 2);
        boolean isPreviousUninterruptedActionRitualSummoning = ((actionType.equals(ActionType.ALLY_RITUAL_SUMMONING_MONSTER) && actionTurn == 1) || (actionType.equals(ActionType.OPPONENT_RITUAL_SUMMONING_MONSTER)) && actionTurn == 2);
        boolean isPreviousUninterruptedActionMonsterAttacking = ((actionType.equals(ActionType.ALLY_MONSTER_ATTACKING_OPPONENT_MONSTER) && actionTurn == 1) || (actionType.equals(ActionType.OPPONENT_MONSTER_ATTACKING_ALLY_MONSTER)) && actionTurn == 2) || (actionType.equals(ActionType.ALLY_DIRECT_ATTACKING) && actionTurn == 1) || (actionType.equals(ActionType.OPPONENT_DIRECT_ATTACKING) && actionTurn == 2);
        boolean isPreviousUninterruptedActionSpellActivating = ((actionType.equals(ActionType.ALLY_ACTIVATING_SPELL) && actionTurn == 1) || (actionType.equals(ActionType.OPPONENT_ACTIVATING_SPELL)) && actionTurn == 2);
        boolean isPreviousUninterruptedActionTrapActivating = ((actionType.equals(ActionType.ALLY_ACTIVATING_TRAP) && actionTurn == 1) || (actionType.equals(ActionType.OPPONENT_ACTIVATING_TRAP)) && actionTurn == 2);
        //boolean isPreviousUninterruptedActionMonsterEffectActivating = ((actionType.equals(ActionType.ALLY_ACTIVATING_MONSTER_EFFECT) && actionTurn == 1) || (actionType.equals(ActionType.OPPONENT_ACTIVATING_MONSTER_EFFECT)) && actionTurn == 2);
        if (normalSummonTrapCardEffects.contains(NormalSummonTrapCardEffect.IF_ATK_IS_AT_LEAST_1000_ATK_DESTROY_IT) && isPreviousUninterruptedActionNormalSummoning) {
            destroyMainMonsterCardInPreviousAction(uninterruptedAction, index);
            cancelPreviousAction(action);
        }
        if (normalSummonTrapCardEffects.contains(NormalSummonTrapCardEffect.DESTROY_ALL_MONSTERS_ON_FIELD) && isPreviousUninterruptedActionNormalSummoning) {
            destroyAllMonsterCardsInTheField(index);
            cancelPreviousAction(action);
        }
        if (normalSummonTrapCardEffects.contains(NormalSummonTrapCardEffect.PAY_2000_LP) && isPreviousUninterruptedActionNormalSummoning) {
            pay2000HP(thisUninterruptedAction, index);
        }
        if (normalSummonTrapCardEffects.contains(NormalSummonTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD) && isPreviousUninterruptedActionNormalSummoning) {
            destroyMainMonsterCardInPreviousAction(uninterruptedAction, index);
            cancelPreviousAction(action);
        }
        if (tributeSummonTrapCardEffects.contains(TributeSummonTrapCardEffect.IF_ATK_IS_AT_LEAST_1000_ATK_DESTROY_IT) && isPreviousUninterruptedActionTributeSummoning) {
            destroyMainMonsterCardInPreviousAction(uninterruptedAction, index);
            cancelPreviousAction(action);
        }
        if (tributeSummonTrapCardEffects.contains(TributeSummonTrapCardEffect.DESTROY_ALL_MONSTERS_ON_FIELD) && isPreviousUninterruptedActionTributeSummoning) {
            destroyAllMonsterCardsInTheField(index);
            cancelPreviousAction(action);
        }
        if (tributeSummonTrapCardEffects.contains(TributeSummonTrapCardEffect.PAY_2000_LP) && isPreviousUninterruptedActionTributeSummoning) {
            pay2000HP(thisUninterruptedAction, index);
        }
        if (tributeSummonTrapCardEffects.contains(TributeSummonTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD) && isPreviousUninterruptedActionTributeSummoning) {
            destroyMainMonsterCardInPreviousAction(uninterruptedAction, index);
            cancelPreviousAction(action);
        }
        if (flipSummonTrapCardEffects.contains(FlipSummonTrapCardEffect.IF_ATK_IS_AT_LEAST_1000_ATK_DESTROY_IT) && isPreviousUninterruptedActionFlipSummoning) {
            destroyMainMonsterCardInPreviousAction(uninterruptedAction, index);
            cancelPreviousAction(action);
        }
        if (flipSummonTrapCardEffects.contains(FlipSummonTrapCardEffect.DESTROY_ALL_MONSTERS_ON_FIELD) && isPreviousUninterruptedActionFlipSummoning) {
            destroyAllMonsterCardsInTheField(index);
            cancelPreviousAction(action);
        }
        if (flipSummonTrapCardEffects.contains(FlipSummonTrapCardEffect.PAY_2000_LP) && isPreviousUninterruptedActionFlipSummoning) {
            pay2000HP(thisUninterruptedAction, index);
        }
        if (flipSummonTrapCardEffects.contains(FlipSummonTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD) && isPreviousUninterruptedActionFlipSummoning) {
            destroyMainMonsterCardInPreviousAction(uninterruptedAction, index);
            cancelPreviousAction(action);
        }
        if (specialSummonTrapCardEffects.contains(SpecialSummonTrapCardEffect.DESTROY_ALL_MONSTERS_ON_FIELD) && isPreviousUninterruptedActionSpecialSummoning) {
            destroyAllMonsterCardsInTheField(index);
            cancelPreviousAction(action);
        }
        if (specialSummonTrapCardEffects.contains(SpecialSummonTrapCardEffect.PAY_2000_LP) && isPreviousUninterruptedActionSpecialSummoning) {
            pay2000HP(thisUninterruptedAction, index);
        }
        if (specialSummonTrapCardEffects.contains(SpecialSummonTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD) && isPreviousUninterruptedActionSpecialSummoning) {
            destroyMainMonsterCardInPreviousAction(uninterruptedAction, index);
            cancelPreviousAction(action);
        }
        if (ritualSummonTrapCardEffects.contains(RitualSummonTrapCardEffect.DESTROY_ALL_MONSTERS_ON_FIELD) && isPreviousUninterruptedActionRitualSummoning) {
            destroyAllMonsterCardsInTheField(index);
            cancelPreviousAction(action);
        }
        if (ritualSummonTrapCardEffects.contains(RitualSummonTrapCardEffect.PAY_2000_LP) && isPreviousUninterruptedActionRitualSummoning) {
            pay2000HP(thisUninterruptedAction, index);
        }
        if (ritualSummonTrapCardEffects.contains(RitualSummonTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD) && isPreviousUninterruptedActionRitualSummoning) {
            destroyMainMonsterCardInPreviousAction(uninterruptedAction, index);
            cancelPreviousAction(action);
        }
        if (monsterAttackingTrapCardEffects.contains(MonsterAttackingTrapCardEffect.NEGATE_OPPONENT_ATTACK) && isPreviousUninterruptedActionMonsterAttacking) {
            cancelPreviousAction(action);
        }
        if (monsterAttackingTrapCardEffects.contains(MonsterAttackingTrapCardEffect.INFLICT_DAMAGE_TO_OPPONENT_EQUAL_TO_MONSTERS_ATK) && isPreviousUninterruptedActionMonsterAttacking) {
            dealDamageToOpponentEqualToMonstersATK(action, index);
        }
        if (monsterAttackingTrapCardEffects.contains(MonsterAttackingTrapCardEffect.DESTROY_ALL_OPPONENT_ATTACK_POSITION_MONSTERS) && isPreviousUninterruptedActionMonsterAttacking) {
            destroyAllFaceUpOpponentMonsterCards(uninterruptedAction, index);
            cancelPreviousAction(action);
        }
        if (monsterAttackingTrapCardEffects.contains(MonsterAttackingTrapCardEffect.END_BATTLE_PHASE) && isPreviousUninterruptedActionMonsterAttacking) {
            endBattlePhase(index);
        }
        if (spellCardActivationTrapCardEffects.contains(SpellCardActivationTrapCardEffect.DISCARD_1_CARD_NEGATE_DESTROY_CARD) && isPreviousUninterruptedActionSpellActivating) {
            System.out.println("extraordinary!");
            discardCard(index, numberInListOfActions);
            destroyMainMonsterCardInPreviousAction(uninterruptedAction, index);
            cancelPreviousAction(action);
        }
        if (spellCardActivationTrapCardEffects.contains(SpellCardActivationTrapCardEffect.PAY_2000_LP) && doesPreviousUninterruptedActionContainACardEffectThatSpecialSummonsAMonster(uninterruptedAction, index) && isPreviousUninterruptedActionSpellActivating) {
            pay2000HP(thisUninterruptedAction, index);
        }
        if (spellCardActivationTrapCardEffects.contains(SpellCardActivationTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD) && doesPreviousUninterruptedActionContainACardEffectThatSpecialSummonsAMonster(uninterruptedAction, index) && isPreviousUninterruptedActionSpellActivating) {
            destroyMainMonsterCardInPreviousAction(uninterruptedAction, index);
            cancelPreviousAction(action);
        }
        if (trapCardActivationTrapCardEffects.contains(TrapCardActivationTrapCardEffect.PAY_2000_LP) && doesPreviousUninterruptedActionContainACardEffectThatSpecialSummonsAMonster(uninterruptedAction, index) && isPreviousUninterruptedActionTrapActivating) {
            pay2000HP(thisUninterruptedAction, index);
        }
        if (trapCardActivationTrapCardEffects.contains(TrapCardActivationTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD) && doesPreviousUninterruptedActionContainACardEffectThatSpecialSummonsAMonster(uninterruptedAction, index) && isPreviousUninterruptedActionTrapActivating) {
            destroyMainMonsterCardInPreviousAction(uninterruptedAction, index);
            cancelPreviousAction(action);
        }
        if (monsterAttackingTrapCardEffects.contains(MonsterAttackingTrapCardEffect.PAY_2000_LP) && doesPreviousUninterruptedActionContainACardEffectThatSpecialSummonsAMonster(uninterruptedAction, index) && isPreviousUninterruptedActionMonsterAttacking) {
            pay2000HP(thisUninterruptedAction, index);
        }
        if (monsterAttackingTrapCardEffects.contains(MonsterAttackingTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD) && doesPreviousUninterruptedActionContainACardEffectThatSpecialSummonsAMonster(uninterruptedAction, index) && isPreviousUninterruptedActionMonsterAttacking) {
            destroyDefendingMonsterCardInPreviousAction(uninterruptedAction, index);
            cancelPreviousAction(action);
        }
        if (monsterAttackingTrapCardEffects.contains(MonsterAttackingTrapCardEffect.SPECIAL_SUMMON_ONE_MONSTER_IN_YOUR_GRAVEYARD_IN_FACE_UP_ATTACK_POSITION)) {
            specialSummonMonsterFromGraveyard(actions.get(numberInListOfActions), index, CardPosition.FACE_UP_ATTACK_POSITION);
            cancelPreviousAction(action);
        }
        //System.out.println("main trap card continuous is " + continuousTrapCardEffects);
        if (continuousTrapCardEffects.size() == 0) {
            Action thisAction = actions.get(numberInListOfActions);
            CardLocation mainCardLocationInThisAction = thisAction.getMainCardLocation();
            int thisActionTurn = thisAction.getActionTurn();
            SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(mainCardLocationInThisAction, index);
            //sendCardToGraveyardAfterRemoving(mainCardLocationInThisAction, index, thisActionTurn);
        }
        return "trap activated";
    }

    public static void opponentDiscardsAllCardsOfAGivenNameOtherwiseOwnerLosesARandomCard(Action thisAction, int index) {
        int actionTurn = thisAction.getActionTurn();
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        if (actionTurn == 1) {
            ArrayList<Card> opponentCards = duelBoard.getOpponentCardsInHand();
            ArrayList<CardLocation> opponentHandCardLocations = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                opponentHandCardLocations.add(new CardLocation(RowOfCardLocation.OPPONENT_HAND_ZONE, i + 1));
            }
            if (doesExistACardWithGivenNameInGivenArrayList(opponentCards, thisAction.getOptionalCardNameInput())) {
                removeAllCardsWithNameInArrayList(opponentHandCardLocations, thisAction.getOptionalCardNameInput(), index);
            } else {
                ownerDiscardsARandomCard(thisAction, index);
            }
        } else {
            ArrayList<Card> allyCards = duelBoard.getAllyCardsInHand();
            ArrayList<CardLocation> allyHandCardLocations = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                allyHandCardLocations.add(new CardLocation(RowOfCardLocation.ALLY_HAND_ZONE, i + 1));
            }
            if (doesExistACardWithGivenNameInGivenArrayList(allyCards, thisAction.getOptionalCardNameInput())) {
                removeAllCardsWithNameInArrayList(allyHandCardLocations, thisAction.getOptionalCardNameInput(), index);
            } else {
                ownerDiscardsARandomCard(thisAction, index);
            }
        }
    }

    public static void ownerDiscardsARandomCard(Action thisAction, int index) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        int actionTurn = thisAction.getActionTurn();
        if (actionTurn == 1) {
            Random rand = new Random();
            int randomNumber = rand.nextInt(duelBoard.getAllyCardsInHand().size()) + 1;
            SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(new CardLocation(RowOfCardLocation.ALLY_HAND_ZONE, randomNumber), index);
        } else {
            Random rand = new Random();
            int randomNumber = rand.nextInt(duelBoard.getOpponentCardsInHand().size()) + 1;
            SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(new CardLocation(RowOfCardLocation.OPPONENT_HAND_ZONE, randomNumber), index);
        }
    }

    public static boolean doesExistACardWithGivenNameInGivenArrayList(ArrayList<Card> arrayList, String cardName) {
        System.out.println("POOR CARD NAME IS " + cardName);
        for (int i = 0; i < arrayList.size(); i++) {
            System.out.println("CANDIDATES " + arrayList.get(i).getCardName());
            if (arrayList.get(i).getCardName().equals(cardName)) {
                return true;
            }
        }
        return false;
    }

    public static void removeAllCardsWithNameInArrayList(ArrayList<CardLocation> arrayList, String cardName, int index) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        int carefulReductionInIndexes = 0;
        boolean shouldContinue = true;
        while (shouldContinue) {
            boolean firstOccurence = false;
            for (int i = 0; i < arrayList.size(); i++) {
                if (!firstOccurence){
                    String sampleCardName = duelBoard.getCardByCardLocation(arrayList.get(i)).getCardName();
                    if (sampleCardName.equals(cardName)) {
                        SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(new CardLocation(arrayList.get(i).getRowOfCardLocation(), arrayList.get(i).getIndex()), index);
                        //carefulReductionInIndexes += 1;
                        firstOccurence = true;
                    }
                }
            }
            if (arrayList.size() == 0){
                shouldContinue = false;
            } else {
                if (arrayList.get(0).getRowOfCardLocation().equals(RowOfCardLocation.ALLY_HAND_ZONE)){
                    ArrayList<Card> allyCardsInHand = duelBoard.getAllyCardsInHand();
                    if (!doesExistACardWithGivenNameInGivenArrayList(allyCardsInHand, cardName)){
                        shouldContinue = false;
                    }
                } else {
                    ArrayList<Card> opponentCardsInHand = duelBoard.getOpponentCardsInHand();
                    if (!doesExistACardWithGivenNameInGivenArrayList(opponentCardsInHand, cardName)){
                        shouldContinue = false;
                    }
                }
            }

        }
    }

    public static void specialSummonMonsterFromGraveyard(Action thisAction, int index, CardPosition cardPosition) {
        ArrayList<CardLocation> cardsToBeSpecialSummoned = thisAction.getCardsToBeSpecialSummoned();
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        int actionTurn = thisAction.getActionTurn();
        Card card = SendCardToGraveyardConductor.removeCardAndGetRemovedCard(cardsToBeSpecialSummoned.get(cardsToBeSpecialSummoned.size() - 1), index);
        duelBoard.addCardToMonsterZone(card, actionTurn);
        card.setCardPosition(cardPosition);
    }

    public static void skipOpponentDrawCardNextPhase(Action thisAction, int index) {
        int actionTurn = thisAction.getActionTurn();
        PhaseController phaseController = GameManager.getPhaseControllerByIndex(index);
        phaseController.setPlayersProhibitedToDrawCardNextTurn(3 - actionTurn, true);
    }

    public static void cancelPreviousAction(Action action) {
        System.out.println("CANCELLING :)))");
        action.setActionCanceled(true);
    }

    public static void destroyMainMonsterCardInPreviousAction(Action uninterruptedAction, int index) {
        CardLocation finalMainCardLocation = uninterruptedAction.getFinalMainCardLocation();
        SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(finalMainCardLocation, index);
        //sendCardToGraveyardAfterRemoving(finalMainCardLocation, index, uninterruptedAction.getActionTurn());
    }

    public static void destroyDefendingMonsterCardInPreviousAction(Action uninterruptedAction, int index) {
        CardLocation defendingCardLocation = uninterruptedAction.getTargetingCards().get(uninterruptedAction.getTargetingCards().size() - 1);
        SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(defendingCardLocation, index);
        //sendCardToGraveyardAfterRemoving(finalMainCardLocation, index, uninterruptedAction.getActionTurn());
    }

    public static void sendAllCardsInThisArrayListToGraveyard(ArrayList<CardLocation> targetingCards, int index, int graveyardToSendCardsTo) {
        //System.out.println("sendAllCardsInThisArrayListToGraveyard index is " + index);
        for (int i = 0; i < targetingCards.size(); i++) {
            SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(targetingCards.get(i), index);
            //sendCardToGraveyardAfterRemoving(targetingCards.get(i), index, graveyardToSendCardsTo);
        }
    }

    public static ArrayList<CardLocation> giveCardLocationArrayListByRowOfCardLocation(RowOfCardLocation rowOfCardLocation) {
        ArrayList<CardLocation> cardLocations = new ArrayList<>();
        if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) {
            for (int i = 0; i < 5; i++) {
                cardLocations.add(new CardLocation(RowOfCardLocation.ALLY_MONSTER_ZONE, i + 1));
            }
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) {
            for (int i = 0; i < 5; i++) {
                cardLocations.add(new CardLocation(RowOfCardLocation.OPPONENT_MONSTER_ZONE, i + 1));
            }
        } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_SPELL_ZONE)) {
            for (int i = 0; i < 5; i++) {
                cardLocations.add(new CardLocation(RowOfCardLocation.ALLY_SPELL_ZONE, i + 1));
            }
            cardLocations.add(new CardLocation(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE, 1));
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_SPELL_ZONE)) {
            for (int i = 0; i < 5; i++) {
                cardLocations.add(new CardLocation(RowOfCardLocation.OPPONENT_SPELL_ZONE, i + 1));
            }
            cardLocations.add(new CardLocation(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE, 1));
        }
        return cardLocations;
    }

    /*
    public static void sendCardToGraveyardAfterRemoving(CardLocation targetingCardLocation, int index, int graveyardToSendCardTo) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        Card card = removeCardAndGetRemovedCard(targetingCardLocation, index);
        duelBoard.addCardToGraveyard(card, graveyardToSendCardTo);
    }
     */

    public static void destroyAllMonsterCardsInTheField(int index) {
        ArrayList<CardLocation> targetingCards;
        targetingCards = giveCardLocationArrayListByRowOfCardLocation(RowOfCardLocation.OPPONENT_MONSTER_ZONE);
        sendAllCardsInThisArrayListToGraveyard(targetingCards, index, 2);
        targetingCards = giveCardLocationArrayListByRowOfCardLocation(RowOfCardLocation.ALLY_MONSTER_ZONE);
        sendAllCardsInThisArrayListToGraveyard(targetingCards, index, 1);
    }

    public static void destroyAllFaceUpOpponentMonsterCards(Action uninterruptedAction, int index) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        ArrayList<CardLocation> targetingCards;
        int actionTurn = uninterruptedAction.getActionTurn();
        int graveyardToSendCardsTo;
        if (actionTurn == 1) {
            targetingCards = giveCardLocationArrayListByRowOfCardLocation(RowOfCardLocation.ALLY_MONSTER_ZONE);
            graveyardToSendCardsTo = 1;
        } else {
            targetingCards = giveCardLocationArrayListByRowOfCardLocation(RowOfCardLocation.OPPONENT_MONSTER_ZONE);
            //sendAllCardsInThisArrayListToGraveyard(targetingCards, index, 2);
            graveyardToSendCardsTo = 2;
        }
        for (int i = 0; i < targetingCards.size(); i++) {
            MonsterCard monsterCard = (MonsterCard) duelBoard.getCardByCardLocation(targetingCards.get(i));
            if (monsterCard != null) {
                if (monsterCard.getCardPosition().equals(CardPosition.FACE_UP_ATTACK_POSITION)) {
                    SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(targetingCards.get(i), index);
                    //sendCardToGraveyardAfterRemoving(targetingCards.get(i), index, graveyardToSendCardsTo);
                }
            }
        }
    }

    public static void endBattlePhase(int index) {
        PhaseController phaseController = GameManager.getPhaseControllerByIndex(index);
        if (phaseController.getPhaseInGame().equals(PhaseInGame.ALLY_BATTLE_PHASE)) {
            phaseController.setPhaseInGame(PhaseInGame.ALLY_MAIN_PHASE_2);
        }
        if (phaseController.getPhaseInGame().equals(PhaseInGame.OPPONENT_BATTLE_PHASE)) {
            phaseController.setPhaseInGame(PhaseInGame.OPPONENT_MAIN_PHASE_2);
        }
    }

    public static void discardCard(int index, int numberInListOfActions) {
        Action currentAction = GameManager.getActionsByIndex(index).get(numberInListOfActions);
        ArrayList<CardLocation> cardsToBeDiscarded = currentAction.getCardsToBeDiscarded();
        SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(cardsToBeDiscarded.get(cardsToBeDiscarded.size() - 1), index);
        //sendCardToGraveyardAfterRemoving(cardsToBeDiscarded.get(cardsToBeDiscarded.size() - 1), index, currentAction.getActionTurn());
    }

    public static void pay2000HP(Action action, int index) {
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        int actionTurn = action.getActionTurn();
        duelController.increaseLifePoints(-2000, actionTurn);
    }

    public static boolean doesPreviousUninterruptedActionContainACardEffectThatSpecialSummonsAMonster(Action uninterruptedAction, int index) {
        CardLocation mainCardLocation = uninterruptedAction.getFinalMainCardLocation();
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        Card card = duelBoard.getCardByCardLocation(mainCardLocation);
        if (Card.isCardAMonster(card)) {
            MonsterCard monsterCard = (MonsterCard) card;
            ArrayList<UponSummoningEffect> uponSummoningEffects = monsterCard.getUponSummoningEffects();
            if (uponSummoningEffects.contains(UponSummoningEffect.IF_NORMAL_SUMMONED_SPECIAL_SUMMON_1_LEVEL_4_OR_LESS_MONSTER_FROM_HAND_IN_DEFENSE_POSITION)) {
                return true;
            }
            CardLocation defendingMonsterCardLocation = uninterruptedAction.getTargetingCards().get(uninterruptedAction.getTargetingCards().size() - 1);
            Card defendingCard = duelBoard.getCardByCardLocation(defendingMonsterCardLocation);
            MonsterCard defendingMonsterCard = (MonsterCard) defendingCard;
            ArrayList<BeingAttackedEffect> beingAttackedEffects = defendingMonsterCard.getBeingAttackedEffects();
            if (beingAttackedEffects.contains(BeingAttackedEffect.SPECIAL_SUMMON_CYBERSE_NORMAL_MONSTER_FROM_HAND_GV_DECK_ONCE_PER_TURN)) {
                return true;
            }
        }
        if (Card.isCardASpell(card)) {
            SpellCard spellCard = (SpellCard) card;
            ArrayList<NormalSpellCardEffect> normalSpellCardEffects = spellCard.getNormalSpellCardEffects();
            if (normalSpellCardEffects.contains(NormalSpellCardEffect.SPECIAL_SUMMON_MONSTER_FROM_EITHER_GY)) {
                return true;
            }
        }
        if (Card.isCardATrap(card)) {
            TrapCard trapCard = (TrapCard) card;
            ArrayList<NormalTrapCardEffect> normalTrapCardEffects = trapCard.getNormalTrapCardEffects();
            if (normalTrapCardEffects.contains(NormalTrapCardEffect.SPECIAL_SUMMON_ONE_MONSTER_IN_YOUR_GRAVEYARD_IN_FACE_UP_ATTACK_POSITION)) {
                return true;
            }
        }
        return false;
    }

    public static void dealDamageToOpponentEqualToMonstersATK(Action action, int index) {
        CardLocation mainCardLocation = action.getMainCardLocation();
        int actionTurn = action.getActionTurn();
        int attackOfMainCard = MonsterCard.giveATKDEFConsideringEffects("attack", mainCardLocation, index);
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        duelController.increaseLifePoints(attackOfMainCard * (-1), actionTurn);
    }
}
