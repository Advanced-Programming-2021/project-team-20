package controller.duel.GamePackage.ActionConductors;

import java.util.ArrayList;

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
        CardLocation mainCardLocation = action.getMainCardLocation();
        TrapCard trapCard = (TrapCard) duelBoard.getCardByCardLocation(mainCardLocation);
        return switchCaseAmongAllTrapEffects(index, trapCard, numberInListOfActions);
    }

    public static String switchCaseAmongAllTrapEffects(int index, TrapCard trapCard, int numberInListOfActions) {
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(index);
        ArrayList<Action> actions = GameManager.getActionsByIndex(index);
        Action uninterruptedAction = uninterruptedActions.get(numberInListOfActions - 1);
        ActionType actionType = uninterruptedAction.getActionType();
        int actionTurn = uninterruptedAction.getActionTurn();
        Action action = actions.get(numberInListOfActions - 1);
        ArrayList<NormalTrapCardEffect> normalTrapCardEffects = trapCard.getNormalTrapCardEffects();
        //note that normalTrapCardEffects don't need to counter previous action so 5 lines above gives null pointer exception
        ArrayList<NormalSummonTrapCardEffect> normalSummonTrapCardEffects = trapCard.getNormalSummonTrapCardEffects();
        ArrayList<TributeSummonTrapCardEffect> tributeSummonTrapCardEffects = trapCard.getTributeSummonTrapCardEffects();
        ArrayList<FlipSummonTrapCardEffect> flipSummonTrapCardEffects = trapCard.getFlipSummonTrapCardEffects();
        ArrayList<RitualSummonTrapCardEffect> ritualSummonTrapCardEffects = trapCard.getRitualSummonTrapCardEffects();
        ArrayList<SpecialSummonTrapCardEffect> specialSummonTrapCardEffects = trapCard.getSpecialSummonTrapCardEffects();
        ArrayList<ContinuousTrapCardEffect> continuousTrapCardEffects = trapCard.getContinuousTrapCardEffects();
        ArrayList<MonsterAttackingTrapCardEffect> monsterAttackingTrapCardEffects = trapCard.getMonsterAttackingTrapCardEffects();
        ArrayList<SpellCardActivationTrapCardEffect> spellCardActivationTrapCardEffects = trapCard.getSpellCardActivationTrapCardEffects();
        ArrayList<TrapCardActivationTrapCardEffect> trapCardActivationTrapCardEffects = trapCard.getTrapCardActivationTrapCardEffects();
        ArrayList<MonsterEffectActivationTrapCardEffect> monsterEffectActivationTrapCardEffects = trapCard.getMonsterEffectActivationTrapCardEffect();
        boolean isPreviousUninterruptedActionNormalSummoning = ((actionType.equals(ActionType.ALLY_NORMAL_SUMMONING_MONSTER) && actionTurn == 1) || (actionType.equals(ActionType.OPPONENT_NORMAL_SUMMONING_MONSTER)) && actionTurn == 2);
        boolean isPreviousUninterruptedActionTributeSummoning = ((actionType.equals(ActionType.ALLY_TRIBUTE_SUMMONING_MONSTER) && actionTurn == 1) || (actionType.equals(ActionType.OPPONENT_TRIBUTE_SUMMONING_MONSTER)) && actionTurn == 2);
        boolean isPreviousUninterruptedActionFlipSummoning = ((actionType.equals(ActionType.ALLY_FLIP_SUMMONING_MONSTER) && actionTurn == 1) || (actionType.equals(ActionType.OPPONENT_FLIP_SUMMONING_MONSTER)) && actionTurn == 2);
        boolean isPreviousUninterruptedActionSpecialSummoning = ((actionType.equals(ActionType.ALLY_SPECIAL_SUMMONING_MONSTER) && actionTurn == 1) || (actionType.equals(ActionType.OPPONENT_SPECIAL_SUMMONING_MONSTER)) && actionTurn == 2);
        boolean isPreviousUninterruptedActionRitualSummoning = ((actionType.equals(ActionType.ALLY_RITUAL_SUMMONING_MONSTER) && actionTurn == 1) || (actionType.equals(ActionType.OPPONENT_RITUAL_SUMMONING_MONSTER)) && actionTurn == 2);
        boolean isPreviousUninterruptedActionMonsterAttacking = ((actionType.equals(ActionType.ALLY_MONSTER_ATTACKING_OPPONENT_MONSTER) && actionTurn == 1) || (actionType.equals(ActionType.OPPONENT_MONSTER_ATTACKING_ALLY_MONSTER)) && actionTurn == 2) || (actionType.equals(ActionType.ALLY_DIRECT_ATTACKING) && actionTurn == 1) || (actionType.equals(ActionType.OPPONENT_DIRECT_ATTACKING) && actionTurn == 2);
        boolean isPreviousUninterruptedActionSpellActivating = ((actionType.equals(ActionType.ALLY_ACTIVATING_SPELL) && actionTurn == 1) || (actionType.equals(ActionType.OPPONENT_ACTIVATING_SPELL)) && actionTurn == 2);
        boolean isPreviousUninterruptedActionTrapActivating = ((actionType.equals(ActionType.ALLY_ACTIVATING_TRAP) && actionTurn == 1) || (actionType.equals(ActionType.OPPONENT_ACTIVATING_TRAP)) && actionTurn == 2);
        boolean isPreviousUninterruptedActionMonsterEffectActivating = ((actionType.equals(ActionType.ALLY_ACTIVATING_MONSTER_EFFECT) && actionTurn == 1) || (actionType.equals(ActionType.OPPONENT_ACTIVATING_MONSTER_EFFECT)) && actionTurn == 2);
        if (normalSummonTrapCardEffects.contains(NormalSummonTrapCardEffect.IF_ATK_IS_AT_LEAST_1000_ATK_DESTROY_IT) && isPreviousUninterruptedActionNormalSummoning) {
            destroyMainMonsterCardInPreviousAction(uninterruptedAction, index);
            cancelPreviousAction(action);
        }
        if (normalSummonTrapCardEffects.contains(NormalSummonTrapCardEffect.DESTROY_ALL_MONSTERS_ON_FIELD) && isPreviousUninterruptedActionNormalSummoning) {
            destroyAllMonsterCardsInTheField(index);
            cancelPreviousAction(action);
        }
        if (normalSummonTrapCardEffects.contains(NormalSummonTrapCardEffect.PAY_2000_LP) && isPreviousUninterruptedActionNormalSummoning) {
            pay2000HP(uninterruptedAction, index);
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
            pay2000HP(uninterruptedAction, index);
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
            pay2000HP(uninterruptedAction, index);
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
            pay2000HP(uninterruptedAction, index);
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
            pay2000HP(uninterruptedAction, index);
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
            discardCard(index, numberInListOfActions);
            destroyMainMonsterCardInPreviousAction(uninterruptedAction, index);
            cancelPreviousAction(action);
        }
        if (spellCardActivationTrapCardEffects.contains(SpellCardActivationTrapCardEffect.PAY_2000_LP) && doesPreviousUninterruptedActionContainACardEffectThatSpecialSummonsAMonster(uninterruptedAction, index) && isPreviousUninterruptedActionSpellActivating) {
            pay2000HP(uninterruptedAction, index);
        }
        if (spellCardActivationTrapCardEffects.contains(SpellCardActivationTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD) && doesPreviousUninterruptedActionContainACardEffectThatSpecialSummonsAMonster(uninterruptedAction, index) && isPreviousUninterruptedActionSpellActivating) {
            destroyMainMonsterCardInPreviousAction(uninterruptedAction, index);
            cancelPreviousAction(action);
        }
        if (trapCardActivationTrapCardEffects.contains(TrapCardActivationTrapCardEffect.PAY_2000_LP) && doesPreviousUninterruptedActionContainACardEffectThatSpecialSummonsAMonster(uninterruptedAction, index) && isPreviousUninterruptedActionTrapActivating) {
            pay2000HP(uninterruptedAction, index);
        }
        if (trapCardActivationTrapCardEffects.contains(TrapCardActivationTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD) && doesPreviousUninterruptedActionContainACardEffectThatSpecialSummonsAMonster(uninterruptedAction, index) && isPreviousUninterruptedActionTrapActivating) {
            destroyMainMonsterCardInPreviousAction(uninterruptedAction, index);
            cancelPreviousAction(action);
        }
        if (monsterEffectActivationTrapCardEffects.contains(MonsterEffectActivationTrapCardEffect.PAY_2000_LP) && doesPreviousUninterruptedActionContainACardEffectThatSpecialSummonsAMonster(uninterruptedAction, index) && isPreviousUninterruptedActionMonsterEffectActivating) {
            pay2000HP(uninterruptedAction, index);
        }
        if (monsterEffectActivationTrapCardEffects.contains(MonsterEffectActivationTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD) && doesPreviousUninterruptedActionContainACardEffectThatSpecialSummonsAMonster(uninterruptedAction, index) && isPreviousUninterruptedActionMonsterEffectActivating) {
            destroyMainMonsterCardInPreviousAction(uninterruptedAction, index);
            cancelPreviousAction(action);
        }
        //System.out.println("main trap card continuous is " + continuousTrapCardEffects);
        if (continuousTrapCardEffects.size() == 0) {
            Action thisAction = actions.get(numberInListOfActions);
            CardLocation mainCardLocationInThisAction = thisAction.getMainCardLocation();
            int thisActionTurn = thisAction.getActionTurn();
            sendCardToGraveyardAfterRemoving(mainCardLocationInThisAction, index);
            //sendCardToGraveyardAfterRemoving(mainCardLocationInThisAction, index, thisActionTurn);
        }
        return "trap activated";
    }

    public static void cancelPreviousAction(Action uninterruptedAction) {
        uninterruptedAction.setActionCanceled(true);
    }

    public static void destroyMainMonsterCardInPreviousAction(Action uninterruptedAction, int index) {
        CardLocation finalMainCardLocation = uninterruptedAction.getFinalMainCardLocation();
        sendCardToGraveyardAfterRemoving(finalMainCardLocation, index);
        //sendCardToGraveyardAfterRemoving(finalMainCardLocation, index, uninterruptedAction.getActionTurn());
    }

    public static Card removeCardAndGetRemovedCard(CardLocation cardToBeRemoved, int index) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        Card card = duelBoard.getCardByCardLocation(cardToBeRemoved);
        duelBoard.removeCardByCardLocation(cardToBeRemoved);
        return card;
    }

    public static void sendCardToGraveyardAfterRemoving(CardLocation targetingCardLocation, int index) {
        //System.out.println("sendCardToGraveyardAfterRemoving rowOfCardLocation" + targetingCardLocation.getRowOfCardLocation());
        //System.out.println("sendCardToGraveyardAfterRemoving card index" + targetingCardLocation.getIndex());
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        Card cardGoingToBeSentToGraveyard = duelBoard.getCardByCardLocation(targetingCardLocation);
        if (cardGoingToBeSentToGraveyard != null) {
            int graveyardToSendCardTo;
            RowOfCardLocation rowOfCardLocationOfThrownCard = targetingCardLocation.getRowOfCardLocation();
            if (rowOfCardLocationOfThrownCard.equals(RowOfCardLocation.ALLY_HAND_ZONE) || rowOfCardLocationOfThrownCard.equals(RowOfCardLocation.ALLY_MONSTER_ZONE)
                || rowOfCardLocationOfThrownCard.equals(RowOfCardLocation.ALLY_SPELL_ZONE) || rowOfCardLocationOfThrownCard.equals(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE)) {
                graveyardToSendCardTo = 1;
            } else {
                graveyardToSendCardTo = 2;
            }
            Card card = removeCardAndGetRemovedCard(targetingCardLocation, index);
            if (card == null) {

            }
            System.out.println("CARD WITH NAME" + card.getCardName() + "IS BEING SENT TO GRAVEYARD " + graveyardToSendCardTo);
            duelBoard.addCardToGraveyard(card, graveyardToSendCardTo);
        }
    }

    public static void sendAllCardsInThisArrayListToGraveyard(ArrayList<CardLocation> targetingCards, int index, int graveyardToSendCardsTo) {
        //System.out.println("sendAllCardsInThisArrayListToGraveyard index is " + index);
        for (int i = 0; i < targetingCards.size(); i++) {
            sendCardToGraveyardAfterRemoving(targetingCards.get(i), index);
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
                    sendCardToGraveyardAfterRemoving(targetingCards.get(i), index);
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
        sendCardToGraveyardAfterRemoving(cardsToBeDiscarded.get(cardsToBeDiscarded.size() - 1), index);
        //sendCardToGraveyardAfterRemoving(cardsToBeDiscarded.get(cardsToBeDiscarded.size() - 1), index, currentAction.getActionTurn());
    }

    public static void pay2000HP(Action action, int index) {
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        int actionTurn = action.getActionTurn();
        duelController.increaseLifePoints(-2000, actionTurn);
    }

    public static boolean doesPreviousUninterruptedActionContainACardEffectThatSpecialSummonsAMonster(Action uninterruptedAction, int index) {
        CardLocation mainCardLocation = uninterruptedAction.getMainCardLocation();
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        Card card = duelBoard.getCardByCardLocation(mainCardLocation);
        if (Card.isCardAMonster(card)) {
            MonsterCard monsterCard = (MonsterCard) card;
            ArrayList<UponSummoningEffect> uponSummoningEffects = monsterCard.getUponSummoningEffects();
            if (uponSummoningEffects.contains(UponSummoningEffect.IF_NORMAL_SUMMONED_SPECIAL_SUMMON_1_LEVEL_4_OR_LESS_MONSTER_FROM_HAND_IN_DEFENSE_POSITION)) {
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
            ArrayList<ContinuousTrapCardEffect> continuousTrapCardEffects = trapCard.getContinuousTrapCardEffects();
            if (continuousTrapCardEffects.contains(ContinuousTrapCardEffect.THIS_CARD_IS_TIED_TO_THE_SPECIAL_SUMMONED_MONSTER)) {
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
