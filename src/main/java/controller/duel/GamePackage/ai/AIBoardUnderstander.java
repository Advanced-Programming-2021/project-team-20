package controller.duel.GamePackage.ai;

import controller.duel.CardEffects.MonsterEffectEnums.BeingAttackedEffect;
import controller.duel.CardEffects.MonsterEffectEnums.ContinuousMonsterEffect;
import controller.duel.CardEffects.MonsterEffectEnums.FlipEffect;
import controller.duel.CardEffects.MonsterEffectEnums.SummoningRequirement;
import controller.duel.CardEffects.SpellEffectEnums.ContinuousSpellCardEffect;
import controller.duel.CardEffects.SpellEffectEnums.NormalSpellCardEffect;
import controller.duel.CardEffects.SpellEffectEnums.QuickSpellEffect;
import controller.duel.CardEffects.TrapEffectEnums.*;
import controller.duel.GamePackage.DuelBoard;
import controller.duel.GamePackage.DuelController;
import controller.duel.PreliminaryPackage.GameManager;
import model.aidata.AIBoardAnalysis;
import model.cardData.General.Card;
import model.cardData.General.CardLocation;
import model.cardData.General.CardPosition;
import model.cardData.General.RowOfCardLocation;
import model.cardData.MonsterCardData.MonsterCard;
import model.cardData.SpellCardData.SpellCard;
import model.cardData.SpellCardData.SpellCardValue;
import model.cardData.TrapCardData.TrapCard;

import java.util.ArrayList;

public class AIBoardUnderstander {
    protected AIBoardAnalysis opponentMonsterSpellBoardAnalysis;
    protected AIBoardAnalysis opponentHandBoardAnalysis;
    protected AIBoardAnalysis aiMonsterSpellBoardAnalysis;
    protected AIBoardAnalysis aiHandBoardAnalysis;
    protected AIBoardAnalysis aiGraveyardBoardAnalysis;
    protected AIBoardAnalysis opponentGraveyardBoardAnalysis;
    protected ArrayList<Card> opponentMonsterCards;
    protected ArrayList<Card> opponentCardsInHand;
    protected ArrayList<Card> opponentSpellTrapCards;
    protected ArrayList<Card> opponentSpellFieldCard;
    protected ArrayList<Card> opponentCardsInGraveyard;
    protected ArrayList<Card> opponentCardsInDeck;
    RowOfCardLocation opponentMonsterCardsRowOfCardLocation = null;
    RowOfCardLocation opponentSpellCardsRowOfCardLocation = null;
    RowOfCardLocation opponentSpellFieldCardRowOfCardLocation = null;
    RowOfCardLocation opponentGraveyardCardsRowOfCardLocation = null;
    RowOfCardLocation opponentHandCardsRowOfCardLocation = null;
    protected ArrayList<Card> aiMonsterCards;
    protected ArrayList<Card> aiCardsInHand;
    protected ArrayList<Card> aiSpellTrapCards;
    protected ArrayList<Card> aiSpellFieldCard;
    protected ArrayList<Card> aiCardsInGraveyard;
    protected ArrayList<Card> aiCardsInDeck;
    RowOfCardLocation aiMonsterCardsRowOfCardLocation = null;
    RowOfCardLocation aiSpellCardsRowOfCardLocation = null;
    RowOfCardLocation aiSpellFieldCardRowOfCardLocation = null;
    RowOfCardLocation aiGraveyardCardsRowOfCardLocation = null;
    RowOfCardLocation aiHandCardsRowOfCardLocation = null;

    public AIBoardUnderstander() {
        opponentMonsterCards = new ArrayList<>();
        opponentCardsInHand = new ArrayList<>();
        opponentSpellTrapCards = new ArrayList<>();
        opponentSpellFieldCard = new ArrayList<>();
        aiMonsterCards = new ArrayList<>();
        aiCardsInHand = new ArrayList<>();
        aiSpellTrapCards = new ArrayList<>();
        aiSpellFieldCard = new ArrayList<>();
        aiCardsInGraveyard = new ArrayList<>();
        opponentCardsInGraveyard = new ArrayList<>();
        opponentMonsterSpellBoardAnalysis = new AIBoardAnalysis();
        opponentHandBoardAnalysis = new AIBoardAnalysis();
        aiMonsterSpellBoardAnalysis = new AIBoardAnalysis();
        aiHandBoardAnalysis = new AIBoardAnalysis();
        aiGraveyardBoardAnalysis = new AIBoardAnalysis();
        opponentGraveyardBoardAnalysis = new AIBoardAnalysis();
        aiCardsInDeck = new ArrayList<>();
        opponentCardsInDeck = new ArrayList<>();
    }

    protected void updateVariablesForBoard() {
        opponentMonsterSpellBoardAnalysis.clearVariables();
        opponentHandBoardAnalysis.clearVariables();
        aiMonsterSpellBoardAnalysis.clearVariables();
        aiHandBoardAnalysis.clearVariables();
        aiGraveyardBoardAnalysis.clearVariables();
        opponentGraveyardBoardAnalysis.clearVariables();
        DuelController duelController = GameManager.getDuelControllerByIndex(0);
        int aiTurn = duelController.getAiTurn();
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(0);
        if (aiTurn == 1) {
            opponentMonsterCards = duelBoard.getOpponentMonsterCards();
            opponentCardsInHand = duelBoard.getOpponentCardsInHand();
            opponentSpellTrapCards = duelBoard.getOpponentSpellCards();
            opponentSpellFieldCard = duelBoard.getOpponentSpellFieldCard();
            opponentCardsInDeck = duelBoard.getOpponentCardsInDeck();
            opponentCardsInGraveyard = duelBoard.getOpponentCardsInGraveyard();
            opponentMonsterCardsRowOfCardLocation = RowOfCardLocation.OPPONENT_MONSTER_ZONE;
            opponentSpellCardsRowOfCardLocation = RowOfCardLocation.OPPONENT_SPELL_ZONE;
            opponentSpellFieldCardRowOfCardLocation = RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE;
            opponentGraveyardCardsRowOfCardLocation = RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE;
            opponentHandCardsRowOfCardLocation = RowOfCardLocation.OPPONENT_HAND_ZONE;
            aiMonsterCards = duelBoard.getAllyMonsterCards();
            aiCardsInHand = duelBoard.getAllyCardsInHand();
            aiSpellTrapCards = duelBoard.getAllySpellCards();
            aiSpellFieldCard = duelBoard.getAllySpellFieldCard();
            aiCardsInGraveyard = duelBoard.getAllyCardsInGraveyard();
            aiCardsInDeck = duelBoard.getAllyCardsInDeck();
            aiMonsterCardsRowOfCardLocation = RowOfCardLocation.ALLY_MONSTER_ZONE;
            aiSpellCardsRowOfCardLocation = RowOfCardLocation.ALLY_SPELL_ZONE;
            aiSpellFieldCardRowOfCardLocation = RowOfCardLocation.ALLY_SPELL_FIELD_ZONE;
            aiGraveyardCardsRowOfCardLocation = RowOfCardLocation.ALLY_GRAVEYARD_ZONE;
            aiHandCardsRowOfCardLocation = RowOfCardLocation.ALLY_HAND_ZONE;
        } else if (aiTurn == 2) {
            opponentMonsterCards = duelBoard.getAllyMonsterCards();
            opponentCardsInHand = duelBoard.getAllyCardsInHand();
            opponentSpellTrapCards = duelBoard.getAllySpellCards();
            opponentSpellFieldCard = duelBoard.getAllySpellFieldCard();
            opponentCardsInDeck = duelBoard.getAllyCardsInDeck();
            opponentCardsInGraveyard = duelBoard.getAllyCardsInGraveyard();
            opponentMonsterCardsRowOfCardLocation = RowOfCardLocation.ALLY_MONSTER_ZONE;
            opponentSpellCardsRowOfCardLocation = RowOfCardLocation.ALLY_SPELL_ZONE;
            opponentSpellFieldCardRowOfCardLocation = RowOfCardLocation.ALLY_SPELL_FIELD_ZONE;
            opponentGraveyardCardsRowOfCardLocation = RowOfCardLocation.ALLY_GRAVEYARD_ZONE;
            opponentHandCardsRowOfCardLocation = RowOfCardLocation.ALLY_HAND_ZONE;
            aiMonsterCards = duelBoard.getOpponentMonsterCards();
            aiCardsInHand = duelBoard.getOpponentCardsInHand();
            aiSpellTrapCards = duelBoard.getOpponentSpellCards();
            aiSpellFieldCard = duelBoard.getOpponentSpellFieldCard();
            aiCardsInDeck = duelBoard.getOpponentCardsInDeck();
            aiCardsInGraveyard = duelBoard.getOpponentCardsInGraveyard();
            aiHandCardsRowOfCardLocation = RowOfCardLocation.OPPONENT_HAND_ZONE;
            aiMonsterCardsRowOfCardLocation = RowOfCardLocation.OPPONENT_MONSTER_ZONE;
            aiSpellCardsRowOfCardLocation = RowOfCardLocation.OPPONENT_SPELL_ZONE;
            aiSpellFieldCardRowOfCardLocation = RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE;
            aiGraveyardCardsRowOfCardLocation = RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE;
        }

    }

    protected void updateInformationFromOpponentMonsterZone() {
        for (int i = 0; i < opponentMonsterCards.size(); i++) {
            if (opponentMonsterCards.get(i) != null) {
                if (Card.isCardAMonster(opponentMonsterCards.get(i))){
                    MonsterCard monsterCard = (MonsterCard) opponentMonsterCards.get(i);
                    updateInformationAboutMonsterCardsInThisArrayList(i, monsterCard, opponentMonsterSpellBoardAnalysis);
                }
            }
        }
    }

    protected void updateInformationFromOpponentSpellTrapZone() {
        updateInformationFromSpellTrapZone(opponentSpellTrapCards, opponentMonsterSpellBoardAnalysis);
    }

    protected void updateInformationFromOpponentHandZone() {
        updateInformationFromHand(opponentHandBoardAnalysis, opponentCardsInHand);
    }

    protected void updateInformationFromAIMonsterZone() {
        for (int i = 0; i < aiMonsterCards.size(); i++) {
            if (aiMonsterCards.get(i) != null) {
                if (Card.isCardAMonster(aiMonsterCards.get(i))){
                    MonsterCard monsterCard = (MonsterCard) aiMonsterCards.get(i);
                    updateInformationAboutMonsterCardsInThisArrayList(i, monsterCard, aiMonsterSpellBoardAnalysis);
                }
            }
        }
    }

    protected void updateInformationFromAISpellTrapZone() {
        updateInformationFromSpellTrapZone(aiSpellTrapCards, aiMonsterSpellBoardAnalysis);
    }

    protected void updateInformationFromAIGraveyardZone() {
        updateInformationFromHand(aiGraveyardBoardAnalysis, aiCardsInGraveyard);
    }

    protected void updateInformationFromOpponentGraveyardZone() {
        updateInformationFromHand(opponentGraveyardBoardAnalysis, opponentCardsInGraveyard);
    }

    private void updateInformationFromSpellTrapZone(ArrayList<Card> aiSpellTrapCards, AIBoardAnalysis aiBoardAnalysis) {
        for (int i = 0; i < aiSpellTrapCards.size(); i++) {
            if (aiSpellTrapCards.get(i) != null) {
                if (Card.isCardATrap(aiSpellTrapCards.get(i))) {
                    TrapCard trapCard = (TrapCard) aiSpellTrapCards.get(i);
                    updateInformationAboutTrapCardsInTheseArrayLists(i, trapCard, aiBoardAnalysis);
                } else if (Card.isCardASpell(aiSpellTrapCards.get(i))) {
                    SpellCard spellCard = (SpellCard) aiSpellTrapCards.get(i);
                    updateInformationAboutSpellCardsInTheseArrayLists(i, spellCard, aiBoardAnalysis);
                }
            }
        }
    }

    protected void updateInformationFromAIHandZone() {
        updateInformationFromHand(aiHandBoardAnalysis, aiCardsInHand);
    }

    private void updateInformationFromHand(AIBoardAnalysis handOrGraveyardBoardAnalysis, ArrayList<Card> cardsInHandOrGraveyard) {
        for (int i = 0; i < cardsInHandOrGraveyard.size(); i++) {
            if (cardsInHandOrGraveyard.get(i) != null) {
                if (Card.isCardAMonster(cardsInHandOrGraveyard.get(i))) {
                    MonsterCard monsterCard = (MonsterCard) cardsInHandOrGraveyard.get(i);
                    updateInformationAboutMonsterCardsInThisArrayList(i, monsterCard, handOrGraveyardBoardAnalysis);
                } else if (Card.isCardASpell(cardsInHandOrGraveyard.get(i))) {
                    SpellCard spellCard = (SpellCard) cardsInHandOrGraveyard.get(i);
                    updateInformationAboutSpellCardsInTheseArrayLists(i, spellCard, handOrGraveyardBoardAnalysis);
                } else if (Card.isCardATrap(cardsInHandOrGraveyard.get(i))) {
                    TrapCard trapCard = (TrapCard) cardsInHandOrGraveyard.get(i);
                    updateInformationAboutTrapCardsInTheseArrayLists(i, trapCard, handOrGraveyardBoardAnalysis);
                }
            }
        }
    }

    private void updateInformationAboutMonsterCardsInThisArrayList(int i, MonsterCard monsterCard, AIBoardAnalysis aiBoardAnalysis) {
        if (monsterCard != null){
            if (monsterCard.getCardPosition().equals(CardPosition.FACE_UP_ATTACK_POSITION)
                || monsterCard.getCardPosition().equals(CardPosition.FACE_UP_DEFENSE_POSITION)
                && MonsterCard.giveATKDEFConsideringEffects("attack", new CardLocation(opponentMonsterCardsRowOfCardLocation, i + 1), 0) >= 2100) {
                if (monsterCard.getEquipSpellEffects().size() > 0) {
                    ArrayList<Integer> indexesOfMonstersWithHighAttackPowerBecauseOfEquipOrFieldSpells = aiBoardAnalysis.getIndexesOfMonstersWithHighAttackPowerBecauseOfEquipOrFieldSpells();
                    indexesOfMonstersWithHighAttackPowerBecauseOfEquipOrFieldSpells.add(i);
                }
                //indexesOfMonstersWithHighAttackPower.add(i);
            }
            if (monsterCard.getCardPosition().equals(CardPosition.FACE_DOWN_MONSTER_SET_POSITION)
                || monsterCard.getCardPosition().equals(CardPosition.FACE_UP_DEFENSE_POSITION)
                && MonsterCard.giveATKDEFConsideringEffects("defense", new CardLocation(opponentMonsterCardsRowOfCardLocation, i + 1), 0) >= 2000) {
                if (monsterCard.getEquipSpellEffects().size() > 0) {
                    ArrayList<Integer> indexesOfMonstersWithHighDefensePowerBecauseOfEquipOrFieldSpells = aiBoardAnalysis.getIndexesOfMonstersWithHighDefensePowerBecauseOfEquipOrFieldSpells();
                    indexesOfMonstersWithHighDefensePowerBecauseOfEquipOrFieldSpells.add(i);
                }
                //indexesOfMonstersWithHighDefense.add(i);
            }
            ArrayList<BeingAttackedEffect> beingAttackedEffects = monsterCard.getBeingAttackedEffects();
            ArrayList<Integer> indexesOfUndieableMonsters = aiBoardAnalysis.getIndexesOfUndieableMonsters();
            if (beingAttackedEffects.contains(BeingAttackedEffect.CANNOT_BE_DESTROYED_BY_BATTLE)) {
                indexesOfUndieableMonsters.add(i);
            }
            ArrayList<Integer> indexesOfSelfDestructionMonstersWithNoDamageCalculation = aiBoardAnalysis.getIndexesOfSelfDestructionMonstersWithNoDamageCalculations();
            ArrayList<Integer> indexesOfSelfDestructionMonsters = aiBoardAnalysis.getIndexesOfSelfDestructionMonsters();
            if (beingAttackedEffects.contains(BeingAttackedEffect.IF_DESTROYED_AND_SENT_TO_GRAVEYARD_SEND_ATTACKING_MONSTER_TO_GRAVEYARD)) {
                indexesOfSelfDestructionMonsters.add(i);
                if (beingAttackedEffects.contains(BeingAttackedEffect.NEITHER_PLAYER_RECEIVES_BATTLE_DAMAGE_IF_MONSTER_DIES)) {
                    indexesOfSelfDestructionMonstersWithNoDamageCalculation.add(i);
                }
            }
            ArrayList<Integer> indexesOfMonstersWithEffectSettingToZero = aiBoardAnalysis.getIndexesOfMonstersWithEffectSettingToZero();
            if (beingAttackedEffects.contains(BeingAttackedEffect.SET_ATTACKING_MONSTER_ATK_TO_0_ONCE_PER_TURN)) {
                indexesOfMonstersWithEffectSettingToZero.add(i);
            }
            ArrayList<Integer> indexesOfMonstersWithNegatingEffects = aiBoardAnalysis.getIndexesOfMonstersWithNegatingEffects();
            if (beingAttackedEffects.contains(BeingAttackedEffect.NEGATE_ATTACK_ONCE_PER_TURN)) {
                indexesOfMonstersWithNegatingEffects.add(i);
            }
            ArrayList<SummoningRequirement> summoningRequirements = monsterCard.getSummoningRequirements();
            ArrayList<Integer> indexesOfMonstersNeeding2Tributes = aiBoardAnalysis.getIndexesOfMonstersNeeding2Tributes();
            ArrayList<Integer> indexesOfMonstersNeeding3Tributes = aiBoardAnalysis.getIndexesOfMonstersNeeding3Tributes();
            if (summoningRequirements.contains(SummoningRequirement.TRIBUTE_2_MONSTERS)) {
                indexesOfMonstersNeeding2Tributes.add(i);
            } else if (summoningRequirements.contains(SummoningRequirement.TRIBUTE_3_MONSTERS)) {
                indexesOfMonstersNeeding3Tributes.add(i);
            }
            if (monsterCard.getAttackPower() >= 2200) {
                ArrayList<Integer> indexesOfMonstersWithHighAttackPower = aiBoardAnalysis.getIndexesOfMonstersWithHighAttack();
                indexesOfMonstersWithHighAttackPower.add(i);
            } else if (monsterCard.getAttackPower() >= 1800) {
                ArrayList<Integer> indexesOfMonstersWithGoodAttackPower = aiBoardAnalysis.getIndexesOfMonstersWithGoodAttack();
                indexesOfMonstersWithGoodAttackPower.add(i);
            }
            if (monsterCard.getDefensePower() >= 2000) {
                ArrayList<Integer> indexesOfMonstersWithHighDefense = aiBoardAnalysis.getIndexesOfMonstersWithHighDefense();
                indexesOfMonstersWithHighDefense.add(i);
            }
            ArrayList<FlipEffect> flipEffects = monsterCard.getFlipEffects();
            ArrayList<Integer> indexesOfMonstersWithFlipEffectsIncludingDestroyingMonsters = aiBoardAnalysis.getIndexesOfMonstersWithFlipEffectsIncludingDestroyingMonsters();
            if (flipEffects.contains(FlipEffect.DESTROY_1_MONSTER_ON_THE_FIELD) && monsterCard.getCardPosition().equals(CardPosition.FACE_DOWN_MONSTER_SET_POSITION)) {
                indexesOfMonstersWithFlipEffectsIncludingDestroyingMonsters.add(i);
            }
            if (monsterCard.getAttackPower() >= 1800 && monsterCard.getCardPosition().equals(CardPosition.FACE_UP_DEFENSE_POSITION)) {
                ArrayList<Integer> indexesOfMonstersWithDecentAttackButDefensePosition = aiBoardAnalysis.getIndexesOfMonstersWithDecentAttackButDefensePosition();
                indexesOfMonstersWithDecentAttackButDefensePosition.add(i);
            }
            ArrayList<Integer> indexesOfMonstersWorthyOfKilling = aiBoardAnalysis.getIndexesOfMonstersWorthyOfKilling();
            ArrayList<Integer> indexesOfMonstersWorthyOfKillingAndFaceUpAttackPosition = aiBoardAnalysis.getIndexesOfMonstersWorthyOfKillingAndFaceUpAttackPosition();
            ArrayList<Integer> indexesOfMonstersGoodToBeKilledAndFaceUpAttackPosition = aiBoardAnalysis.getIndexesOfMonstersGoodToBeKilledAndFaceUpAttackPosition();
            ArrayList<Integer> indexesOfMonstersWorthyOfBeingTributes = aiBoardAnalysis.getIndexesOfMonstersWorthyOfBeingTributes();
            if (monsterCard.getAttackPower() >= 2200 || beingAttackedEffects.contains(BeingAttackedEffect.CANNOT_BE_DESTROYED_BY_BATTLE)
                || beingAttackedEffects.contains(BeingAttackedEffect.IF_DESTROYED_AND_SENT_TO_GRAVEYARD_SEND_ATTACKING_MONSTER_TO_GRAVEYARD)
                || beingAttackedEffects.contains(BeingAttackedEffect.SET_ATTACKING_MONSTER_ATK_TO_0_ONCE_PER_TURN)
                || beingAttackedEffects.contains(BeingAttackedEffect.NEGATE_ATTACK_ONCE_PER_TURN)
                || flipEffects.contains(FlipEffect.DESTROY_1_MONSTER_ON_THE_FIELD) && monsterCard.getCardPosition().equals(CardPosition.FACE_DOWN_MONSTER_SET_POSITION)) {
                indexesOfMonstersWorthyOfKilling.add(i);
                if (monsterCard.getCardPosition().equals(CardPosition.FACE_UP_ATTACK_POSITION)) {
                    indexesOfMonstersWorthyOfKillingAndFaceUpAttackPosition.add(i);
                }
            } else {
                indexesOfMonstersWorthyOfBeingTributes.add(i);
                if (monsterCard.getCardPosition().equals(CardPosition.FACE_UP_ATTACK_POSITION)) {
                    indexesOfMonstersGoodToBeKilledAndFaceUpAttackPosition.add(i);
                }
            }
            ArrayList<Integer> indexesOfMonstersWithStoppingTrapCardActivationEffect = aiBoardAnalysis.getIndexesOfMonstersWithStoppingTrapCardActivationEffect();
            ArrayList<ContinuousMonsterEffect> continuousMonsterEffects = monsterCard.getContinuousMonsterEffects();
            if (continuousMonsterEffects.contains(ContinuousMonsterEffect.OPPONENT_CANNOT_ACTIVATE_TRAP_CARDS_WHILE_THIS_CARD_IS_FACE_UP)) {
                indexesOfMonstersWithStoppingTrapCardActivationEffect.add(i);
            }
        }
    }

    private void updateInformationAboutSpellCardsInTheseArrayLists(int i, SpellCard spellCard, AIBoardAnalysis boardAnalysis) {
        if (spellCard != null){
            ArrayList<NormalSpellCardEffect> normalSpellCardEffect = spellCard.getNormalSpellCardEffects();
            ArrayList<Integer> indexesOfMonsterDestroyingSpellCards = boardAnalysis.getIndexesOfMonsterDestroyingSpellCards();
            if (normalSpellCardEffect.contains(NormalSpellCardEffect.DESTROY_ALL_OF_OPPONENTS_MONSTERS)) {
                indexesOfMonsterDestroyingSpellCards.add(i);
            }
            ArrayList<Integer> indexesOfAllMonstersDestroyingSpellCards = boardAnalysis.getIndexesOfAllMonstersDestroyingSpellCards();
            if (normalSpellCardEffect.contains(NormalSpellCardEffect.DESTROY_ALL_MONSTERS_ON_THE_FIELD)) {
                indexesOfAllMonstersDestroyingSpellCards.add(i);
            }
            ArrayList<Integer> indexesOfSpellsDestroyingAllSpellCards = boardAnalysis.getIndexesOfSpellsDestroyingAllSpellCards();
            if (normalSpellCardEffect.contains(NormalSpellCardEffect.DESTROY_ALL_OF_OPPONENTS_SPELL_AND_TRAP_CARDS)) {
                indexesOfSpellsDestroyingAllSpellCards.add(i);
            }
            ArrayList<Integer> indexesOfSpecialSummoningSpellCards = boardAnalysis.getIndexesOfSpecialSummoningSpellCardsToBeActivated();
            if (normalSpellCardEffect.contains(NormalSpellCardEffect.SPECIAL_SUMMON_MONSTER_FROM_EITHER_GY)) {
                indexesOfSpecialSummoningSpellCards.add(i);
            }
            ArrayList<Integer> indexesOfCardDrawingSpellCards = boardAnalysis.getIndexesOfCardDrawingSpellCards();
            if (normalSpellCardEffect.contains(NormalSpellCardEffect.DRAW_2_CARDS)) {
                indexesOfCardDrawingSpellCards.add(i);
            }
            ArrayList<Integer> indexesOfMonsterSwappingSpellCards = boardAnalysis.getIndexesOfMonsterSwappingSpellCards();
            if (normalSpellCardEffect.contains(NormalSpellCardEffect.CONTROL_ONE_OF_OPPONENTS_MONSTERS_UNTIL_END_PHASE)) {
                indexesOfMonsterSwappingSpellCards.add(i);
            }
            ArrayList<QuickSpellEffect> quickSpellEffect = spellCard.getQuickSpellEffects();
            ArrayList<Integer> indexesOfQuickSpellTrapDestroyingSpellCards = boardAnalysis.getIndexesOfQuickSpellTrapDestroyingSpellCards();
            if (quickSpellEffect.contains(QuickSpellEffect.DISCARD_1_CARD_THEN_TARGET_UP_TO_2_SPELL_CARDS_AND_DESTROY)) {
                indexesOfQuickSpellTrapDestroyingSpellCards.add(i);
            }
            if (quickSpellEffect.contains(QuickSpellEffect.TARGET_1_SPELL_TRAP_CARD_AND_DESTROY)) {
                indexesOfQuickSpellTrapDestroyingSpellCards.add(i);
            }
            ArrayList<Integer> indexesOfStoppingMonstersFromAttackingSpellCards = boardAnalysis.getIndexesOfStoppingMonstersFromAttackingSpellCards();
            ArrayList<Integer> indexesOfStoppingMonstersFromAttackingSpellCardsCurrentlyActive = boardAnalysis.getIndexesOfStoppingMonstersFromAttackingSpellCardsCurrentlyActive();
            ArrayList<ContinuousSpellCardEffect> continuousSpellCardEffects = spellCard.getContinuousSpellCardEffects();
            if (continuousSpellCardEffects.contains(ContinuousSpellCardEffect.MONSTERS_WITH_1500_OR_MORE_ATK_CANT_ATTACK)) {
                indexesOfStoppingMonstersFromAttackingSpellCards.add(i);
                if (spellCard.getCardPosition().equals(CardPosition.FACE_UP_ACTIVATED_POSITION)){
                    indexesOfStoppingMonstersFromAttackingSpellCardsCurrentlyActive.add(i);
                }
            }
            if (continuousSpellCardEffects.contains(ContinuousSpellCardEffect.UNTIL_THIS_CARD_IS_FACE_UP_ON_FIELD_OPPONENT_MONSTERS_CANT_ATTACK)) {
                indexesOfStoppingMonstersFromAttackingSpellCards.add(i);
                if (spellCard.getCardPosition().equals(CardPosition.FACE_UP_ACTIVATED_POSITION)){
                    indexesOfStoppingMonstersFromAttackingSpellCardsCurrentlyActive.add(i);
                }
            }
            ArrayList<Integer> indexesOfEquipSpells = boardAnalysis.getIndexesOfEquipSpells();
            if (spellCard.getSpellCardValue().equals(SpellCardValue.EQUIP) && spellCard.isCardAlreadyActivated()) {
                indexesOfEquipSpells.add(i);
            }
            ArrayList<Integer> indexesOfSpellFieldCards = boardAnalysis.getIndexesOfSpellFieldCards();
            if (spellCard.getSpellCardValue().equals(SpellCardValue.FIELD) && spellCard.isCardAlreadyActivated()) {
                indexesOfSpellFieldCards.add(i);
            }
            ArrayList<Integer> indexesOfSpellTrapCardsWorthyOfKilling = boardAnalysis.getIndexesOfSpellTrapsWorthyOfKilling();
            if (continuousSpellCardEffects.contains(ContinuousSpellCardEffect.UNTIL_THIS_CARD_IS_FACE_UP_ON_FIELD_OPPONENT_MONSTERS_CANT_ATTACK)
                || continuousSpellCardEffects.contains(ContinuousSpellCardEffect.MONSTERS_WITH_1500_OR_MORE_ATK_CANT_ATTACK)) {
                indexesOfSpellTrapCardsWorthyOfKilling.add(i);
            }
            ArrayList<Integer> indexesOfSpellTrapCardsGoodToBeKilled = boardAnalysis.getIndexesOfSpellTrapsGoodToBeKilled();
            if (spellCard.getSpellCardValue().equals(SpellCardValue.EQUIP) || spellCard.getSpellCardValue().equals(SpellCardValue.FIELD)) {
                indexesOfSpellTrapCardsGoodToBeKilled.add(i);
            }
        }
    }

    private void updateInformationAboutTrapCardsInTheseArrayLists(int i, TrapCard trapCard, AIBoardAnalysis aiBoardAnalysis) {
        if (trapCard != null){
            ArrayList<Integer> indexesOfCardDiscardingTrapCards = aiBoardAnalysis.getIndexesOfCardDiscardingTrapCards();
            ArrayList<NormalTrapCardEffect> normalTrapCardEffects = trapCard.getNormalTrapCardEffects();
            if (normalTrapCardEffects.contains(NormalTrapCardEffect.OPPONENT_DISCARDS_ALL_CARDS_WITH_A_GIVEN_NAME_OTHERWISE_OWNER_LOSES_ONE_RANDOM_CARD)) {
                indexesOfCardDiscardingTrapCards.add(i);
            }
            ArrayList<NormalSummonTrapCardEffect> normalSummonTrapCardEffects = trapCard.getNormalSummonTrapCardEffects();
            ArrayList<TributeSummonTrapCardEffect> tributeSummonTrapCardEffects = trapCard.getTributeSummonTrapCardEffects();
            ArrayList<SpecialSummonTrapCardEffect> specialSummonTrapCardEffects = trapCard.getSpecialSummonTrapCardEffects();
            ArrayList<Integer> indexesOfMonsterDestroyingTrapCardsInSummoning = aiBoardAnalysis.getIndexesOfMonsterDestroyingTrapCardsInSummoning();
            if (normalSummonTrapCardEffects.size() > 0 || tributeSummonTrapCardEffects.size() > 0 || specialSummonTrapCardEffects.size() > 0) {
                indexesOfMonsterDestroyingTrapCardsInSummoning.add(i);
            }
            ArrayList<MonsterAttackingTrapCardEffect> monsterAttackingTrapCardEffects = trapCard.getMonsterAttackingTrapCardEffects();
            ArrayList<Integer> indexesOfMonsterDestroyingTrapCardsInAttacking = aiBoardAnalysis.getIndexesOfMonsterDestroyingTrapCardsInBattlePhase();
            if (monsterAttackingTrapCardEffects.contains(MonsterAttackingTrapCardEffect.DESTROY_ALL_OPPONENT_ATTACK_POSITION_MONSTERS)) {
                indexesOfMonsterDestroyingTrapCardsInAttacking.add(i);
            }
            ArrayList<Integer> indexesOfDamageDealingTrapCards = aiBoardAnalysis.getIndexesOfDamageDealingTrapCardsInBattlePhase();
            if (monsterAttackingTrapCardEffects.contains(MonsterAttackingTrapCardEffect.INFLICT_DAMAGE_TO_OPPONENT_EQUAL_TO_MONSTERS_ATK)) {
                indexesOfDamageDealingTrapCards.add(i);
            }
            ArrayList<Integer> indexesOfSpecialSummoningTrapCards = aiBoardAnalysis.getIndexesOfSpecialSummoningTrapCardsToBeActivated();
            if (monsterAttackingTrapCardEffects.contains(MonsterAttackingTrapCardEffect.SPECIAL_SUMMON_ONE_MONSTER_IN_YOUR_GRAVEYARD_IN_FACE_UP_ATTACK_POSITION)) {
                indexesOfSpecialSummoningTrapCards.add(i);
            }
            ArrayList<SpellCardActivationTrapCardEffect> spellCardActivationTrapCardEffects = trapCard.getSpellCardActivationTrapCardEffects();
            ArrayList<Integer> indexesOfQuickSpellTrapDestroyingSpellCards = aiBoardAnalysis.getIndexesOfQuickSpellTrapDestroyingSpellCards();
            if (spellCardActivationTrapCardEffects.contains(SpellCardActivationTrapCardEffect.DISCARD_1_CARD_NEGATE_DESTROY_CARD)) {
                indexesOfQuickSpellTrapDestroyingSpellCards.add(i);
            }
            ArrayList<Integer> indexesOfAttackNegatingTrapCards = aiBoardAnalysis.getIndexesOfAttackNegatingTrapCardsInBattlePhase();
            if (monsterAttackingTrapCardEffects.contains(MonsterAttackingTrapCardEffect.NEGATE_OPPONENT_ATTACK)
                && monsterAttackingTrapCardEffects.contains(MonsterAttackingTrapCardEffect.END_BATTLE_PHASE)) {
                indexesOfAttackNegatingTrapCards.add(i);
            }
            ArrayList<Integer> indexesOfSpellTrapCardsWorthyOfKilling = aiBoardAnalysis.getIndexesOfSpellTrapsWorthyOfKilling();
            if (monsterAttackingTrapCardEffects.contains(MonsterAttackingTrapCardEffect.DESTROY_ALL_OPPONENT_ATTACK_POSITION_MONSTERS)
                || monsterAttackingTrapCardEffects.contains(MonsterAttackingTrapCardEffect.INFLICT_DAMAGE_TO_OPPONENT_EQUAL_TO_MONSTERS_ATK)
                || monsterAttackingTrapCardEffects.contains(MonsterAttackingTrapCardEffect.SPECIAL_SUMMON_ONE_MONSTER_IN_YOUR_GRAVEYARD_IN_FACE_UP_ATTACK_POSITION)
                || indexesOfMonsterDestroyingTrapCardsInSummoning.contains(i)) {
                indexesOfSpellTrapCardsWorthyOfKilling.add(i);
            }
        }
    }

    public AIBoardAnalysis getOpponentMonsterSpellBoardAnalysis() {
        return opponentMonsterSpellBoardAnalysis;
    }

    public AIBoardAnalysis getOpponentHandBoardAnalysis() {
        return opponentHandBoardAnalysis;
    }

    public AIBoardAnalysis getAiMonsterSpellBoardAnalysis() {
        return aiMonsterSpellBoardAnalysis;
    }

    public AIBoardAnalysis getAiHandBoardAnalysis() {
        return aiHandBoardAnalysis;
    }

    public AIBoardAnalysis getAiGraveyardBoardAnalysis() {
        return aiGraveyardBoardAnalysis;
    }

    public AIBoardAnalysis getOpponentGraveyardBoardAnalysis() {
        return opponentGraveyardBoardAnalysis;
    }

    public ArrayList<Card> getOpponentMonsterCards() {
        return opponentMonsterCards;
    }

    public ArrayList<Card> getOpponentCardsInHand() {
        return opponentCardsInHand;
    }

    public ArrayList<Card> getOpponentSpellTrapCards() {
        return opponentSpellTrapCards;
    }

    public ArrayList<Card> getOpponentSpellFieldCard() {
        return opponentSpellFieldCard;
    }

    public ArrayList<Card> getOpponentCardsInGraveyard() {
        return opponentCardsInGraveyard;
    }

    public RowOfCardLocation getOpponentMonsterCardsRowOfCardLocation() {
        return opponentMonsterCardsRowOfCardLocation;
    }

    public RowOfCardLocation getOpponentSpellCardsRowOfCardLocation() {
        return opponentSpellCardsRowOfCardLocation;
    }

    public RowOfCardLocation getOpponentSpellFieldCardRowOfCardLocation() {
        return opponentSpellFieldCardRowOfCardLocation;
    }

    public RowOfCardLocation getOpponentGraveyardCardsRowOfCardLocation() {
        return opponentGraveyardCardsRowOfCardLocation;
    }

    public RowOfCardLocation getOpponentHandCardsRowOfCardLocation() {
        return opponentHandCardsRowOfCardLocation;
    }

    public ArrayList<Card> getAiMonsterCards() {
        return aiMonsterCards;
    }

    public ArrayList<Card> getAiCardsInHand() {
        return aiCardsInHand;
    }

    public ArrayList<Card> getAiSpellTrapCards() {
        return aiSpellTrapCards;
    }

    public ArrayList<Card> getAiSpellFieldCard() {
        return aiSpellFieldCard;
    }

    public ArrayList<Card> getAiCardsInGraveyard() {
        return aiCardsInGraveyard;
    }

    public RowOfCardLocation getAiMonsterCardsRowOfCardLocation() {
        return aiMonsterCardsRowOfCardLocation;
    }

    public RowOfCardLocation getAiSpellCardsRowOfCardLocation() {
        return aiSpellCardsRowOfCardLocation;
    }

    public RowOfCardLocation getAiSpellFieldCardRowOfCardLocation() {
        return aiSpellFieldCardRowOfCardLocation;
    }

    public RowOfCardLocation getAiGraveyardCardsRowOfCardLocation() {
        return aiGraveyardCardsRowOfCardLocation;
    }

    public RowOfCardLocation getAiHandCardsRowOfCardLocation() {
        return aiHandCardsRowOfCardLocation;
    }

    public AIBoardAnalysis getAIMonsterSpellBoardAnalysis() {
        return aiMonsterSpellBoardAnalysis;
    }

    public ArrayList<Card> getAiCardsInDeck() {
        return aiCardsInDeck;
    }

    public ArrayList<Card> getOpponentCardsInDeck() {
        return opponentCardsInDeck;
    }
}

