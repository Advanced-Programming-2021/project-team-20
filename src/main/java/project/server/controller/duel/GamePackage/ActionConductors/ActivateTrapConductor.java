package project.server.controller.duel.GamePackage.ActionConductors;

import java.util.ArrayList;
import java.util.Random;

import project.model.MonsterEffectEnums.BeingAttackedEffect;
import project.model.MonsterEffectEnums.UponSummoningEffect;
import project.model.SpellEffectEnums.NormalSpellCardEffect;
import project.model.TrapEffectEnums.*;
import project.server.controller.duel.GamePackage.Action;
import project.model.ActionType;
import project.server.controller.duel.GamePackage.DuelBoard;
import project.server.controller.duel.GamePackage.DuelController;
import project.model.PhaseInGame;
import project.server.controller.duel.GamePhaseControllers.PhaseController;
import project.server.controller.duel.PreliminaryPackage.GameManager;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.CardPosition;
import project.model.cardData.General.RowOfCardLocation;
import project.model.cardData.MonsterCardData.MonsterCard;
import project.model.cardData.SpellCardData.SpellCard;
import project.model.cardData.TrapCardData.TrapCard;

public class ActivateTrapConductor {

    public static String conductActivatingTrapUninterruptedAction(String token, int numberInListOfActions) {
        Action uninterruptedAction = GameManager.getUninterruptedActionsByIndex(token).get(numberInListOfActions);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        CardLocation mainTrapCardLocation = uninterruptedAction.getMainCardLocation();
        Card mainTrapCard = duelBoard.getCardByCardLocation(mainTrapCardLocation);
        RowOfCardLocation rowOfMainTrapCardLocation = mainTrapCardLocation.getRowOfCardLocation();
        if (rowOfMainTrapCardLocation.equals(RowOfCardLocation.ALLY_HAND_ZONE) || rowOfMainTrapCardLocation.equals(RowOfCardLocation.OPPONENT_HAND_ZONE)) {
            duelBoard.removeCardByCardLocation(mainTrapCardLocation);
            int actionTurn = uninterruptedAction.getActionTurn();
            duelBoard.addCardToSpellZone(mainTrapCard, actionTurn, token);
            GameManager.getDuelControllerByIndex(token).addStringToSuperAlmightyString("mainCardLocation " + mainTrapCardLocation.getRowOfCardLocation()
                + " " + mainTrapCardLocation.getIndex() + " is being added to spell zone " + actionTurn + " and should finally be FACE_UP_ACTIVATED_POSITION", token);
        } else {
            GameManager.getDuelControllerByIndex(token).addStringToSuperAlmightyString("mainCardLocation " + mainTrapCardLocation.getRowOfCardLocation()
                + " " + mainTrapCardLocation.getIndex() + " is being stayed at spell zone " + uninterruptedAction.getActionTurn() + " and should finally be FACE_UP_ACTIVATED_POSITION", token);
        }
        mainTrapCard.setCardPosition(CardPosition.FACE_UP_ACTIVATED_POSITION);
        return "trap card ready for activation";
    }

    public static String conductActivatingTrap(String token, int numberInListOfActions) {
        Action action = GameManager.getActionsByIndex(token).get(numberInListOfActions);
        Action previousAction = null;
        if (numberInListOfActions > 0) {
            previousAction = GameManager.getActionsByIndex(token).get(numberInListOfActions - 1);
        }
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        CardLocation mainCardLocation = action.getFinalMainCardLocation();
        TrapCard trapCard = (TrapCard) duelBoard.getCardByCardLocation(mainCardLocation);
        if (action.isActionCanceled()) {
            return "\nactivation of trap was canceled";
        }
        return switchCaseAmongAllTrapEffects(token, trapCard, numberInListOfActions);
    }

    public static String switchCaseAmongAllTrapEffects(String token, TrapCard trapCard, int numberInListOfActions) {
        if (trapCard == null) {
            return "\nactivation of trap was canceled";
        }
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(token);
        ArrayList<Action> actions = GameManager.getActionsByIndex(token);
        ArrayList<ContinuousTrapCardEffect> continuousTrapCardEffects = trapCard.getContinuousTrapCardEffects();
        ArrayList<NormalTrapCardEffect> normalTrapCardEffects = trapCard.getNormalTrapCardEffects();

        Action thisUninterruptedAction = uninterruptedActions.get(numberInListOfActions);


        boolean trapCardIsNormal = false;
        if (normalTrapCardEffects.contains(NormalTrapCardEffect.SKIP_OPPONENT_DRAW_PHASE_NEXT_TURN)) {
            skipOpponentDrawCardNextPhase(actions.get(numberInListOfActions), token);
            trapCardIsNormal = true;
        }
        if (normalTrapCardEffects.contains(NormalTrapCardEffect.SPECIAL_SUMMON_ONE_MONSTER_IN_YOUR_GRAVEYARD_IN_FACE_UP_ATTACK_POSITION) && numberInListOfActions == 0) {
            specialSummonMonsterFromGraveyard(actions.get(numberInListOfActions), token, CardPosition.FACE_UP_ATTACK_POSITION);
            trapCardIsNormal = true;
        }
        if (normalTrapCardEffects.contains(NormalTrapCardEffect.OPPONENT_DISCARDS_ALL_CARDS_WITH_A_GIVEN_NAME_OTHERWISE_OWNER_LOSES_ONE_RANDOM_CARD)) {
            opponentDiscardsAllCardsOfAGivenNameOtherwiseOwnerLosesARandomCard(actions.get(numberInListOfActions), token);
            trapCardIsNormal = true;
        }
        if (normalTrapCardEffects.contains(NormalTrapCardEffect.DESTROY_ALL_CARDS_IN_THE_SAME_COLUMN_AS_THIS_CARD)) {
            destroyAllCardsInTheSameColumnAsThisCard(thisUninterruptedAction, token);
            trapCardIsNormal = true;
        }
        if (normalTrapCardEffects.contains(NormalTrapCardEffect.DEAL_300_MULTIPLIED_BY_NUMBER_OF_CARDS_IN_GRAVEYARD_TO_OPPONENT)) {
            deal300MultipliedByNumberOfCardsInOpponentGraveyardToGraveyard(actions.get(numberInListOfActions), token);
            trapCardIsNormal = true;
        }
        if (trapCardIsNormal) {
            if (continuousTrapCardEffects.size() == 0) {
                Action thisAction = actions.get(numberInListOfActions);
                CardLocation mainCardLocationInThisAction = thisAction.getMainCardLocation();
                int thisActionTurn = thisAction.getActionTurn();
                SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(mainCardLocationInThisAction, token);
                //sendCardToGraveyardAfterRemoving(mainCardLocationInThisAction, index, thisActionTurn);
            }
            return "trap activated";
        }
        Action uninterruptedAction = uninterruptedActions.get(numberInListOfActions - 1);
        ActionType actionType = uninterruptedAction.getActionType();
        int actionTurn = uninterruptedAction.getActionTurn();
        Action action = actions.get(numberInListOfActions - 1);

        //note that normalTrapCardEffects don't need to counter previous action so 5 lines above gives null pointer exception
        ArrayList<NormalSummonTrapCardEffect> normalSummonTrapCardEffects = trapCard.getNormalSummonTrapCardEffects();
        ArrayList<TributeSummonTrapCardEffect> tributeSummonTrapCardEffects = trapCard.getTributeSummonTrapCardEffects();
        ArrayList<FlipSummonTrapCardEffect> flipSummonTrapCardEffects = trapCard.getFlipSummonTrapCardEffects();
        ArrayList<RitualSummonTrapCardEffect> ritualSummonTrapCardEffects = trapCard.getRitualSummonTrapCardEffects();
        ArrayList<SpecialSummonTrapCardEffect> specialSummonTrapCardEffects = trapCard.getSpecialSummonTrapCardEffects();

        ArrayList<MonsterAttackingTrapCardEffect> monsterAttackingTrapCardEffects = trapCard.getMonsterAttackingTrapCardEffects();
        ArrayList<SpellCardActivationTrapCardEffect> spellCardActivationTrapCardEffects = trapCard.getSpellCardActivationTrapCardEffects();
        ArrayList<TrapCardActivationTrapCardEffect> trapCardActivationTrapCardEffects = trapCard.getTrapCardActivationTrapCardEffects();
        boolean isPreviousUninterruptedActionNormalSummoning = ((actionType.equals(ActionType.ALLY_NORMAL_SUMMONING_MONSTER) && actionTurn == 1) || (actionType.equals(ActionType.OPPONENT_NORMAL_SUMMONING_MONSTER)) && actionTurn == 2);
        boolean isPreviousUninterruptedActionTributeSummoning = ((actionType.equals(ActionType.ALLY_TRIBUTE_SUMMONING_MONSTER) && actionTurn == 1) || (actionType.equals(ActionType.OPPONENT_TRIBUTE_SUMMONING_MONSTER)) && actionTurn == 2);
        boolean isPreviousUninterruptedActionFlipSummoning = ((actionType.equals(ActionType.ALLY_FLIP_SUMMONING_MONSTER) && actionTurn == 1) || (actionType.equals(ActionType.OPPONENT_FLIP_SUMMONING_MONSTER)) && actionTurn == 2);
        boolean isPreviousUninterruptedActionSpecialSummoning = ((actionType.equals(ActionType.ALLY_SPECIAL_SUMMONING_MONSTER) && actionTurn == 1) || (actionType.equals(ActionType.OPPONENT_SPECIAL_SUMMONING_MONSTER)) && actionTurn == 2);
        boolean isPreviousUninterruptedActionRitualSummoning = ((actionType.equals(ActionType.ALLY_RITUAL_SUMMONING_MONSTER) && actionTurn == 1) || (actionType.equals(ActionType.OPPONENT_RITUAL_SUMMONING_MONSTER)) && actionTurn == 2);
        boolean isPreviousUninterruptedActionMonsterAttacking = ((actionType.equals(ActionType.ALLY_MONSTER_ATTACKING_OPPONENT_MONSTER) && actionTurn == 1) || (actionType.equals(ActionType.OPPONENT_MONSTER_ATTACKING_ALLY_MONSTER)) && actionTurn == 2) || (actionType.equals(ActionType.ALLY_DIRECT_ATTACKING) && actionTurn == 1) || (actionType.equals(ActionType.OPPONENT_DIRECT_ATTACKING) && actionTurn == 2);
        boolean isPreviousUninterruptedActionSpellActivating = ((actionType.equals(ActionType.ALLY_ACTIVATING_SPELL) && actionTurn == 1) || (actionType.equals(ActionType.OPPONENT_ACTIVATING_SPELL)) && actionTurn == 2);
        boolean isPreviousUninterruptedActionTrapActivating = ((actionType.equals(ActionType.ALLY_ACTIVATING_TRAP) && actionTurn == 1) || (actionType.equals(ActionType.OPPONENT_ACTIVATING_TRAP)) && actionTurn == 2);
        if (normalSummonTrapCardEffects.contains(NormalSummonTrapCardEffect.IF_ATK_IS_AT_LEAST_1000_ATK_DESTROY_IT) && isPreviousUninterruptedActionNormalSummoning) {
            destroyMainMonsterCardInPreviousAction(uninterruptedAction, token);
            cancelPreviousAction(action);
        }
        if (normalSummonTrapCardEffects.contains(NormalSummonTrapCardEffect.DESTROY_ALL_MONSTERS_ON_FIELD) && isPreviousUninterruptedActionNormalSummoning) {
            destroyAllMonsterCardsInTheField(token);
            cancelPreviousAction(action);
        }
        if (normalSummonTrapCardEffects.contains(NormalSummonTrapCardEffect.PAY_2000_LP) && isPreviousUninterruptedActionNormalSummoning) {
            pay2000HP(thisUninterruptedAction, token);
        }
        if (normalSummonTrapCardEffects.contains(NormalSummonTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD) && isPreviousUninterruptedActionNormalSummoning) {
            destroyMainMonsterCardInPreviousAction(uninterruptedAction, token);
            cancelPreviousAction(action);
        }
        if (tributeSummonTrapCardEffects.contains(TributeSummonTrapCardEffect.IF_ATK_IS_AT_LEAST_1000_ATK_DESTROY_IT) && isPreviousUninterruptedActionTributeSummoning) {
            destroyMainMonsterCardInPreviousAction(uninterruptedAction, token);
            cancelPreviousAction(action);
        }
        if (tributeSummonTrapCardEffects.contains(TributeSummonTrapCardEffect.DESTROY_ALL_MONSTERS_ON_FIELD) && isPreviousUninterruptedActionTributeSummoning) {
            destroyAllMonsterCardsInTheField(token);
            cancelPreviousAction(action);
        }
        if (tributeSummonTrapCardEffects.contains(TributeSummonTrapCardEffect.PAY_2000_LP) && isPreviousUninterruptedActionTributeSummoning) {
            pay2000HP(thisUninterruptedAction, token);
        }
        if (tributeSummonTrapCardEffects.contains(TributeSummonTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD) && isPreviousUninterruptedActionTributeSummoning) {
            destroyMainMonsterCardInPreviousAction(uninterruptedAction, token);
            cancelPreviousAction(action);
        }
        if (flipSummonTrapCardEffects.contains(FlipSummonTrapCardEffect.IF_ATK_IS_AT_LEAST_1000_ATK_DESTROY_IT) && isPreviousUninterruptedActionFlipSummoning) {
            destroyMainMonsterCardInPreviousAction(uninterruptedAction, token);
            cancelPreviousAction(action);
        }
        if (flipSummonTrapCardEffects.contains(FlipSummonTrapCardEffect.DESTROY_ALL_MONSTERS_ON_FIELD) && isPreviousUninterruptedActionFlipSummoning) {
            destroyAllMonsterCardsInTheField(token);
            cancelPreviousAction(action);
        }
        if (flipSummonTrapCardEffects.contains(FlipSummonTrapCardEffect.PAY_2000_LP) && isPreviousUninterruptedActionFlipSummoning) {
            pay2000HP(thisUninterruptedAction, token);
        }
        if (flipSummonTrapCardEffects.contains(FlipSummonTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD) && isPreviousUninterruptedActionFlipSummoning) {
            destroyMainMonsterCardInPreviousAction(uninterruptedAction, token);
            cancelPreviousAction(action);
        }
        if (specialSummonTrapCardEffects.contains(SpecialSummonTrapCardEffect.DESTROY_ALL_MONSTERS_ON_FIELD) && isPreviousUninterruptedActionSpecialSummoning) {
            destroyAllMonsterCardsInTheField(token);
            cancelPreviousAction(action);
        }
        if (specialSummonTrapCardEffects.contains(SpecialSummonTrapCardEffect.PAY_2000_LP) && isPreviousUninterruptedActionSpecialSummoning) {
            pay2000HP(thisUninterruptedAction, token);
        }
        if (specialSummonTrapCardEffects.contains(SpecialSummonTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD) && isPreviousUninterruptedActionSpecialSummoning) {
            destroyMainMonsterCardInPreviousAction(uninterruptedAction, token);
            cancelPreviousAction(action);
        }
        if (ritualSummonTrapCardEffects.contains(RitualSummonTrapCardEffect.DESTROY_ALL_MONSTERS_ON_FIELD) && isPreviousUninterruptedActionRitualSummoning) {
            destroyAllMonsterCardsInTheField(token);
            cancelPreviousAction(action);
        }
        if (ritualSummonTrapCardEffects.contains(RitualSummonTrapCardEffect.PAY_2000_LP) && isPreviousUninterruptedActionRitualSummoning) {
            pay2000HP(thisUninterruptedAction, token);
        }
        if (ritualSummonTrapCardEffects.contains(RitualSummonTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD) && isPreviousUninterruptedActionRitualSummoning) {
            destroyMainMonsterCardInPreviousAction(uninterruptedAction, token);
            cancelPreviousAction(action);
        }
        if (monsterAttackingTrapCardEffects.contains(MonsterAttackingTrapCardEffect.NEGATE_OPPONENT_ATTACK) && isPreviousUninterruptedActionMonsterAttacking) {
            cancelPreviousAction(action);
        }
        if (monsterAttackingTrapCardEffects.contains(MonsterAttackingTrapCardEffect.INFLICT_DAMAGE_TO_OPPONENT_EQUAL_TO_MONSTERS_ATK) && isPreviousUninterruptedActionMonsterAttacking) {
            if (thisUninterruptedAction.isDamageInflictionIsPossible()) {
                dealDamageToOpponentEqualToMonstersATK(action, token);
            }
        }
        if (monsterAttackingTrapCardEffects.contains(MonsterAttackingTrapCardEffect.DESTROY_ALL_OPPONENT_ATTACK_POSITION_MONSTERS) && isPreviousUninterruptedActionMonsterAttacking) {
            destroyAllFaceUpOpponentMonsterCards(uninterruptedAction, token);
            cancelPreviousAction(action);
        }
        if (monsterAttackingTrapCardEffects.contains(MonsterAttackingTrapCardEffect.END_BATTLE_PHASE) && isPreviousUninterruptedActionMonsterAttacking) {
            endBattlePhase(token, thisUninterruptedAction);
        }
        if (monsterAttackingTrapCardEffects.contains(MonsterAttackingTrapCardEffect.GAIN_HP_EQUAL_TO_MONSTERS_ATK) && isPreviousUninterruptedActionMonsterAttacking) {
            gainHPEqualToMonsterAttack(token, uninterruptedAction, thisUninterruptedAction);
        }
        if (spellCardActivationTrapCardEffects.contains(SpellCardActivationTrapCardEffect.DISCARD_1_CARD_NEGATE_DESTROY_CARD) && isPreviousUninterruptedActionSpellActivating) {
            discardCard(token, numberInListOfActions);
            destroyMainMonsterCardInPreviousAction(uninterruptedAction, token);
            cancelPreviousAction(action);
        }
        if (spellCardActivationTrapCardEffects.contains(SpellCardActivationTrapCardEffect.PAY_2000_LP) && doesPreviousUninterruptedActionContainACardEffectThatSpecialSummonsAMonster(uninterruptedAction, token) && isPreviousUninterruptedActionSpellActivating) {
            pay2000HP(thisUninterruptedAction, token);
        }
        if (spellCardActivationTrapCardEffects.contains(SpellCardActivationTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD) && doesPreviousUninterruptedActionContainACardEffectThatSpecialSummonsAMonster(uninterruptedAction, token) && isPreviousUninterruptedActionSpellActivating) {
            destroyMainMonsterCardInPreviousAction(uninterruptedAction, token);
            cancelPreviousAction(action);
        }
        if (trapCardActivationTrapCardEffects.contains(TrapCardActivationTrapCardEffect.PAY_2000_LP) && doesPreviousUninterruptedActionContainACardEffectThatSpecialSummonsAMonster(uninterruptedAction, token) && isPreviousUninterruptedActionTrapActivating) {
            pay2000HP(thisUninterruptedAction, token);
        }
        if (trapCardActivationTrapCardEffects.contains(TrapCardActivationTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD) && doesPreviousUninterruptedActionContainACardEffectThatSpecialSummonsAMonster(uninterruptedAction, token) && isPreviousUninterruptedActionTrapActivating) {
            destroyMainMonsterCardInPreviousAction(uninterruptedAction, token);
            cancelPreviousAction(action);
        }
        if (monsterAttackingTrapCardEffects.contains(MonsterAttackingTrapCardEffect.PAY_2000_LP) && doesPreviousUninterruptedActionContainACardEffectThatSpecialSummonsAMonster(uninterruptedAction, token) && isPreviousUninterruptedActionMonsterAttacking) {
            pay2000HP(thisUninterruptedAction, token);
        }
        if (monsterAttackingTrapCardEffects.contains(MonsterAttackingTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD) && doesPreviousUninterruptedActionContainACardEffectThatSpecialSummonsAMonster(uninterruptedAction, token) && isPreviousUninterruptedActionMonsterAttacking) {
            destroyDefendingMonsterCardInPreviousAction(uninterruptedAction, token);
            cancelPreviousAction(action);
        }
        if (monsterAttackingTrapCardEffects.contains(MonsterAttackingTrapCardEffect.SPECIAL_SUMMON_ONE_MONSTER_IN_YOUR_GRAVEYARD_IN_FACE_UP_ATTACK_POSITION)) {
            specialSummonMonsterFromGraveyard(actions.get(numberInListOfActions), token, CardPosition.FACE_UP_ATTACK_POSITION);
            cancelPreviousAction(action);
        }
        if (continuousTrapCardEffects.size() == 0) {
            Action thisAction = actions.get(numberInListOfActions);
            CardLocation mainCardLocationInThisAction = thisAction.getMainCardLocation();
            int thisActionTurn = thisAction.getActionTurn();
            SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(mainCardLocationInThisAction, token);
        }
        return "trap activated";
    }

    private static void destroyAllCardsInTheSameColumnAsThisCard(Action thisUninterruptedAction, String token) {
        CardLocation cardLocation = thisUninterruptedAction.getFinalMainCardLocation();
        int index = cardLocation.getIndex();
        CardLocation allySpell = new CardLocation(RowOfCardLocation.ALLY_SPELL_ZONE, index);
        CardLocation allyMonster = new CardLocation(RowOfCardLocation.ALLY_MONSTER_ZONE, index);
        CardLocation opponentSpell = new CardLocation(RowOfCardLocation.OPPONENT_SPELL_ZONE, index);
        CardLocation opponentMonster = new CardLocation(RowOfCardLocation.OPPONENT_MONSTER_ZONE, index);
        SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(allyMonster, token);
        SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(opponentMonster, token);
        if (cardLocation.getRowOfCardLocation() != RowOfCardLocation.ALLY_SPELL_ZONE) {
            SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(allySpell, token);
        }
        if (cardLocation.getRowOfCardLocation() != RowOfCardLocation.OPPONENT_SPELL_ZONE) {
            SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(opponentSpell, token);
        }
    }

    private static void deal300MultipliedByNumberOfCardsInOpponentGraveyardToGraveyard(Action action, String token) {
        int turn = action.getActionTurn();
        int numberOfCardsInGraveyard = 0;
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        if (turn == 1) {
            numberOfCardsInGraveyard = duelBoard.getOpponentCardsInGraveyard().size();
        } else {
            numberOfCardsInGraveyard = duelBoard.getAllyCardsInGraveyard().size();
        }
        GameManager.getDuelControllerByIndex(token).increaseLifePoints(300*numberOfCardsInGraveyard, 3-turn, token);
    }

    private static void gainHPEqualToMonsterAttack(String token, Action uninterruptedAction, Action thisUninterruptedAction) {
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        duelController.increaseLifePoints(MonsterCard.giveATKDEFConsideringEffects("attack", uninterruptedAction.getFinalMainCardLocation(), token),
            thisUninterruptedAction.getActionTurn(), token);
    }

    public static void opponentDiscardsAllCardsOfAGivenNameOtherwiseOwnerLosesARandomCard(Action thisAction, String token) {
        int actionTurn = thisAction.getActionTurn();
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        if (actionTurn == 1) {
            ArrayList<Card> opponentCards = duelBoard.getOpponentCardsInHand();
            ArrayList<CardLocation> opponentHandCardLocations = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                opponentHandCardLocations.add(new CardLocation(RowOfCardLocation.OPPONENT_HAND_ZONE, i + 1));
            }
            if (doesExistACardWithGivenNameInGivenArrayList(opponentCards, thisAction.getOptionalCardNameInput())) {
                removeAllCardsWithNameInArrayList(opponentHandCardLocations, thisAction.getOptionalCardNameInput(), token);
            } else {
                ownerDiscardsARandomCard(thisAction, token);
            }
        } else {
            ArrayList<Card> allyCards = duelBoard.getAllyCardsInHand();
            ArrayList<CardLocation> allyHandCardLocations = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                allyHandCardLocations.add(new CardLocation(RowOfCardLocation.ALLY_HAND_ZONE, i + 1));
            }
            if (doesExistACardWithGivenNameInGivenArrayList(allyCards, thisAction.getOptionalCardNameInput())) {
                removeAllCardsWithNameInArrayList(allyHandCardLocations, thisAction.getOptionalCardNameInput(), token);
            } else {
                ownerDiscardsARandomCard(thisAction, token);
            }
        }
    }

    public static void ownerDiscardsARandomCard(Action thisAction, String token) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        int actionTurn = thisAction.getActionTurn();
        if (actionTurn == 1) {
            Random rand = new Random();
            int randomNumber = rand.nextInt(duelBoard.getAllyCardsInHand().size()) + 1;
            SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(new CardLocation(RowOfCardLocation.ALLY_HAND_ZONE, randomNumber), token);
        } else {
            Random rand = new Random();
            int randomNumber = rand.nextInt(duelBoard.getOpponentCardsInHand().size()) + 1;
            SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(new CardLocation(RowOfCardLocation.OPPONENT_HAND_ZONE, randomNumber), token);
        }
    }

    public static boolean doesExistACardWithGivenNameInGivenArrayList(ArrayList<Card> arrayList, String cardName) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).getCardName().equals(cardName)) {
                return true;
            }
        }
        return false;
    }

    public static void removeAllCardsWithNameInArrayList(ArrayList<CardLocation> arrayList, String cardName, String token) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        int carefulReductionInIndexes = 0;
        boolean shouldContinue = true;
        while (shouldContinue) {
            boolean firstOccurence = false;
            for (int i = 0; i < arrayList.size(); i++) {
                if (!firstOccurence) {
                    String sampleCardName = duelBoard.getCardByCardLocation(arrayList.get(i)).getCardName();
                    if (sampleCardName.equals(cardName)) {
                        SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(new CardLocation(arrayList.get(i).getRowOfCardLocation(), arrayList.get(i).getIndex()), token);
                        //carefulReductionInIndexes += 1;
                        firstOccurence = true;
                    }
                }
            }
            if (arrayList.size() == 0) {
                shouldContinue = false;
            } else {
                if (arrayList.get(0).getRowOfCardLocation().equals(RowOfCardLocation.ALLY_HAND_ZONE)) {
                    ArrayList<Card> allyCardsInHand = duelBoard.getAllyCardsInHand();
                    if (!doesExistACardWithGivenNameInGivenArrayList(allyCardsInHand, cardName)) {
                        shouldContinue = false;
                    }
                } else {
                    ArrayList<Card> opponentCardsInHand = duelBoard.getOpponentCardsInHand();
                    if (!doesExistACardWithGivenNameInGivenArrayList(opponentCardsInHand, cardName)) {
                        shouldContinue = false;
                    }
                }
            }

        }
    }

    public static void specialSummonMonsterFromGraveyard(Action thisAction, String token, CardPosition cardPosition) {
        ArrayList<CardLocation> cardsToBeSpecialSummoned = thisAction.getCardsToBeSpecialSummoned();
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        int actionTurn = thisAction.getActionTurn();
        Card card = SendCardToGraveyardConductor.removeCardAndGetRemovedCard(cardsToBeSpecialSummoned.get(cardsToBeSpecialSummoned.size() - 1), token);
        duelBoard.addCardToMonsterZone(card, actionTurn, token);
        GameManager.getDuelControllerByIndex(token).addStringToSuperAlmightyString("mainCardLocation " + cardsToBeSpecialSummoned.get(cardsToBeSpecialSummoned.size() - 1).getRowOfCardLocation()
            + " " + cardsToBeSpecialSummoned.get(cardsToBeSpecialSummoned.size() - 1).getIndex() + " is being added to monster zone " + actionTurn + " and should finally be "
            + cardPosition, token);
        card.setCardPosition(cardPosition);
    }

    public static void skipOpponentDrawCardNextPhase(Action thisAction, String token) {
        int actionTurn = thisAction.getActionTurn();
        PhaseController phaseController = GameManager.getPhaseControllerByIndex(token);
        phaseController.setPlayersProhibitedToDrawCardNextTurn(3 - actionTurn, true);
    }

    public static void cancelPreviousAction(Action action) {
        //System.out.println("CANCELLING :)))");
        action.setActionCanceled(true);
    }

    public static void destroyMainMonsterCardInPreviousAction(Action uninterruptedAction, String token) {
        CardLocation finalMainCardLocation = uninterruptedAction.getFinalMainCardLocation();
        SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(finalMainCardLocation, token);
    }

    public static void destroyDefendingMonsterCardInPreviousAction(Action uninterruptedAction, String token) {
        CardLocation defendingCardLocation = uninterruptedAction.getTargetingCards().get(uninterruptedAction.getTargetingCards().size() - 1);
        SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(defendingCardLocation, token);
    }

    public static void sendAllCardsInThisArrayListToGraveyard(ArrayList<CardLocation> targetingCards, String token, int graveyardToSendCardsTo) {
        for (int i = 0; i < targetingCards.size(); i++) {
            SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(targetingCards.get(i), token);
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


    public static void destroyAllMonsterCardsInTheField(String token) {
        ArrayList<CardLocation> targetingCards;
        targetingCards = giveCardLocationArrayListByRowOfCardLocation(RowOfCardLocation.OPPONENT_MONSTER_ZONE);
        sendAllCardsInThisArrayListToGraveyard(targetingCards, token, 2);
        targetingCards = giveCardLocationArrayListByRowOfCardLocation(RowOfCardLocation.ALLY_MONSTER_ZONE);
        sendAllCardsInThisArrayListToGraveyard(targetingCards, token, 1);
    }

    public static void destroyAllFaceUpOpponentMonsterCards(Action uninterruptedAction, String token) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        ArrayList<CardLocation> targetingCards;
        int actionTurn = uninterruptedAction.getActionTurn();
        if (actionTurn == 1) {
            targetingCards = giveCardLocationArrayListByRowOfCardLocation(RowOfCardLocation.ALLY_MONSTER_ZONE);
        } else {
            targetingCards = giveCardLocationArrayListByRowOfCardLocation(RowOfCardLocation.OPPONENT_MONSTER_ZONE);
        }
        for (int i = 0; i < targetingCards.size(); i++) {
            MonsterCard monsterCard = (MonsterCard) duelBoard.getCardByCardLocation(targetingCards.get(i));
            if (monsterCard != null) {
                if (monsterCard.getCardPosition().equals(CardPosition.FACE_UP_ATTACK_POSITION)) {
                    SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(targetingCards.get(i), token);
                }
            }
        }
    }

    public static void endBattlePhase(String token, Action thisUninterruptedAction) {
        PhaseController phaseController = GameManager.getPhaseControllerByIndex(token);
        if (phaseController.getPhaseInGame().equals(PhaseInGame.ALLY_BATTLE_PHASE)) {
            phaseController.setPhaseInGame(PhaseInGame.ALLY_MAIN_PHASE_2);
        }
        if (phaseController.getPhaseInGame().equals(PhaseInGame.OPPONENT_BATTLE_PHASE)) {
            phaseController.setPhaseInGame(PhaseInGame.OPPONENT_MAIN_PHASE_2);
        }
        int actionTurn = thisUninterruptedAction.getActionTurn();
        GameManager.getDuelControllerByIndex(token).addStringToWhatUsersSay("*user" + (3-actionTurn) + ": next phase*", token);
    }

    public static void discardCard(String token, int numberInListOfActions) {
        Action currentAction = GameManager.getActionsByIndex(token).get(numberInListOfActions);
        ArrayList<CardLocation> cardsToBeDiscarded = currentAction.getCardsToBeDiscarded();
        SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(cardsToBeDiscarded.get(cardsToBeDiscarded.size() - 1), token);
    }

    public static void pay2000HP(Action action, String token) {
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        int actionTurn = action.getActionTurn();
        duelController.increaseLifePoints(-2000, actionTurn, token);
    }

    public static boolean doesPreviousUninterruptedActionContainACardEffectThatSpecialSummonsAMonster(Action uninterruptedAction, String token) {
        CardLocation mainCardLocation = uninterruptedAction.getFinalMainCardLocation();
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        Card card = duelBoard.getCardByCardLocation(mainCardLocation);
        if (Card.isCardAMonster(card)) {
            MonsterCard monsterCard = (MonsterCard) card;
            ArrayList<UponSummoningEffect> uponSummoningEffects = monsterCard.getUponSummoningEffects();
            if (uponSummoningEffects.contains(UponSummoningEffect.IF_NORMAL_SUMMONED_SPECIAL_SUMMON_1_LEVEL_4_OR_LESS_NORMAL_MONSTER_FROM_HAND_IN_DEFENSE_POSITION)) {
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

    public static void dealDamageToOpponentEqualToMonstersATK(Action action, String token) {
        CardLocation mainCardLocation = action.getMainCardLocation();
        int actionTurn = action.getActionTurn();
        int attackOfMainCard = MonsterCard.giveATKDEFConsideringEffects("attack", mainCardLocation, token);
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        duelController.increaseLifePoints(attackOfMainCard * (-1), actionTurn, token);
    }
}
