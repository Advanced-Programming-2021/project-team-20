package project.server.controller.duel.GamePackage.ai;

import com.google.gson.annotations.Until;
import project.model.MonsterEffectEnums.*;
import project.model.SpellEffectEnums.*;
import project.model.TrapEffectEnums.*;
import project.server.controller.duel.GamePackage.Action;
import project.server.controller.duel.PreliminaryPackage.GameManager;
import project.server.controller.duel.Utility.Utility;
import project.model.aidata.AIActionType;
import project.model.aidata.AIFurtherActivationInput;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.CardPosition;
import project.model.cardData.General.RowOfCardLocation;
import project.model.cardData.MonsterCardData.MonsterCard;
import project.model.cardData.MonsterCardData.MonsterCardFamily;
import project.model.cardData.MonsterCardData.MonsterCardValue;
import project.model.cardData.SpellCardData.SpellCard;
import project.model.cardData.TrapCardData.TrapCard;

import java.util.ArrayList;

public class AICardFinder {
    protected String findAllMonsterDestroyingSpellCardFromHandOrBoard(AI ai) {
        ArrayList<Card> aiSpellTrapCards = ai.getAiBoardUnderstander().getAiSpellTrapCards();
        ArrayList<Card> aiCardsInHand = ai.getAiBoardUnderstander().getAiCardsInHand();
        for (int i = 0; i < 5; i++) {
            if (Card.isCardASpell(aiSpellTrapCards.get(i))) {
                SpellCard spellCard = (SpellCard) aiSpellTrapCards.get(i);
                if (spellCard != null) {
                    ArrayList<NormalSpellCardEffect> normalSpellCardEffects = spellCard.getNormalSpellCardEffects();
                    if (normalSpellCardEffects.contains(NormalSpellCardEffect.DESTROY_ALL_MONSTERS_ON_THE_FIELD)) {
                        ai.setShouldRedirectAIMind(true);
                        ai.setAiActionType(AIActionType.ACTIVATE_EFFECT);
                        return "select --spell " + Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(i + 1, true);
                    }
                }
            }
        }
        for (int i = 0; i < aiCardsInHand.size(); i++) {
            if (Card.isCardASpell(aiCardsInHand.get(i))) {
                SpellCard spellCard = (SpellCard) aiCardsInHand.get(i);
                ArrayList<NormalSpellCardEffect> normalSpellCardEffects = spellCard.getNormalSpellCardEffects();
                if (normalSpellCardEffects.contains(NormalSpellCardEffect.DESTROY_ALL_MONSTERS_ON_THE_FIELD)) {
                    ai.setShouldRedirectAIMind(true);
                    ai.setAiActionType(AIActionType.ACTIVATE_EFFECT);
                    return "select --hand " + (i + 1);
                }
            }
        }
        for (int i = 0; i < 5; i++) {
            if (Card.isCardASpell(aiSpellTrapCards.get(i))) {
                SpellCard spellCard = (SpellCard) aiSpellTrapCards.get(i);
                if (spellCard != null) {
                    ArrayList<NormalSpellCardEffect> normalSpellCardEffects = spellCard.getNormalSpellCardEffects();
                    if (normalSpellCardEffects.contains(NormalSpellCardEffect.DESTROY_ALL_OF_OPPONENTS_MONSTERS)) {
                        ai.setShouldRedirectAIMind(true);
                        ai.setAiActionType(AIActionType.ACTIVATE_EFFECT);
                        return "select --spell " + Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(i + 1, true);
                    }
                }
            }
        }
        for (int i = 0; i < aiCardsInHand.size(); i++) {
            if (Card.isCardASpell(aiCardsInHand.get(i))) {
                SpellCard spellCard = (SpellCard) aiCardsInHand.get(i);
                ArrayList<NormalSpellCardEffect> normalSpellCardEffects = spellCard.getNormalSpellCardEffects();
                if (normalSpellCardEffects.contains(NormalSpellCardEffect.DESTROY_ALL_OF_OPPONENTS_MONSTERS)) {
                    ai.setShouldRedirectAIMind(true);
                    ai.setAiActionType(AIActionType.ACTIVATE_EFFECT);
                    return "select --hand " + (i + 1);
                }
            }
        }
        return "next phase";
    }

    protected String findUndieableMonsterCardInHandToSet(AI ai) {
        ArrayList<Card> aiCardsInHand = ai.getAiBoardUnderstander().getAiCardsInHand();
        for (int i = 0; i < aiCardsInHand.size(); i++) {
            if (Card.isCardAMonster(aiCardsInHand.get(i))) {
                MonsterCard monsterCard = (MonsterCard) aiCardsInHand.get(i);
                ArrayList<BeingAttackedEffect> beingAttackedEffects = monsterCard.getBeingAttackedEffects();
                if (beingAttackedEffects.contains(BeingAttackedEffect.CANNOT_BE_DESTROYED_BY_BATTLE)) {
                    ai.setShouldRedirectAIMind(true);
                    ai.setAiActionType(AIActionType.SET);
                    return "select --hand " + (i + 1);
                }
            }
        }
        return "next phase";
    }

    protected String findDefensiveMonsterCardsInHandToSet(AI ai) {
        ArrayList<Card> aiCardsInHand = ai.getAiBoardUnderstander().getAiCardsInHand();
        for (int i = 0; i < aiCardsInHand.size(); i++) {
            if (Card.isCardAMonster(aiCardsInHand.get(i))) {
                MonsterCard monsterCard = (MonsterCard) aiCardsInHand.get(i);
                ArrayList<FlipEffect> flipEffects = monsterCard.getFlipEffects();
                if (flipEffects.contains(FlipEffect.DESTROY_1_MONSTER_ON_THE_FIELD)) {
                    ai.setShouldRedirectAIMind(true);
                    ai.setAiActionType(AIActionType.SET);
                    return "select --hand " + (i + 1);
                }
            }
        }
        for (int i = 0; i < aiCardsInHand.size(); i++) {
            if (Card.isCardAMonster(aiCardsInHand.get(i))) {
                MonsterCard monsterCard = (MonsterCard) aiCardsInHand.get(i);
                ArrayList<BeingAttackedEffect> beingAttackedEffects = monsterCard.getBeingAttackedEffects();
                if (beingAttackedEffects.contains(BeingAttackedEffect.NEGATE_ATTACK_ONCE_PER_TURN)) {
                    ai.setShouldRedirectAIMind(true);
                    ai.setAiActionType(AIActionType.SET);
                    return "select --hand " + (i + 1);
                }
            }
        }
        for (int i = 0; i < aiCardsInHand.size(); i++) {
            if (Card.isCardAMonster(aiCardsInHand.get(i))) {
                MonsterCard monsterCard = (MonsterCard) aiCardsInHand.get(i);
                ArrayList<BeingAttackedEffect> beingAttackedEffects = monsterCard.getBeingAttackedEffects();
                if (beingAttackedEffects.contains(BeingAttackedEffect.IF_DESTROYED_AND_SENT_TO_GRAVEYARD_SEND_ATTACKING_MONSTER_TO_GRAVEYARD)) {
                    ai.setShouldRedirectAIMind(true);
                    ai.setAiActionType(AIActionType.SET);
                    return "select --hand " + (i + 1);
                }
            }
        }
        return "next phase";
    }

    protected String findSpellCardToDestroyAllOfOpponentsSpellCard(AI ai) {
        ArrayList<Card> aiCardsInHand = ai.getAiBoardUnderstander().getAiCardsInHand();
        ArrayList<Card> aiSpellTrapCards = ai.getAiBoardUnderstander().getAiSpellTrapCards();
        for (int i = 0; i < 5; i++) {
            if (Card.isCardASpell(aiSpellTrapCards.get(i))) {
                SpellCard spellCard = (SpellCard) aiSpellTrapCards.get(i);
                if (spellCard != null) {
                    ArrayList<NormalSpellCardEffect> normalSpellCardEffects = spellCard.getNormalSpellCardEffects();
                    if (normalSpellCardEffects.contains(NormalSpellCardEffect.DESTROY_ALL_OF_OPPONENTS_SPELL_AND_TRAP_CARDS)) {
                        ai.setShouldRedirectAIMind(true);
                        ai.setAiActionType(AIActionType.ACTIVATE_EFFECT);
                        return "select --spell " + Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(i + 1, true);
                    }
                }
            }
        }
        for (int i = 0; i < aiCardsInHand.size(); i++) {
            if (Card.isCardASpell(aiCardsInHand.get(i))) {
                SpellCard spellCard = (SpellCard) aiCardsInHand.get(i);
                if (spellCard != null) {
                    ArrayList<NormalSpellCardEffect> normalSpellCardEffects = spellCard.getNormalSpellCardEffects();
                    if (normalSpellCardEffects.contains(NormalSpellCardEffect.DESTROY_ALL_OF_OPPONENTS_SPELL_AND_TRAP_CARDS)) {
                        ai.setShouldRedirectAIMind(true);
                        ai.setAiActionType(AIActionType.ACTIVATE_EFFECT);
                        return "select --hand " + (i + 1);
                    }
                }
            }
        }
        return "next phase";
    }

    protected String findQuickSpellCardsToDestroyOpponentSpellTrapCards(AI ai) {
        ArrayList<Card> aiSpellTrapCards = ai.getAiBoardUnderstander().getAiSpellTrapCards();
        ArrayList<Card> aiCardsInHand = ai.getAiBoardUnderstander().getAiCardsInHand();
        for (int i = 0; i < 5; i++) {
            if (Card.isCardASpell(aiSpellTrapCards.get(i))) {
                SpellCard spellCard = (SpellCard) aiSpellTrapCards.get(i);
                ArrayList<QuickSpellEffect> quickSpellEffects = spellCard.getQuickSpellEffects();
                if (quickSpellEffects.contains(QuickSpellEffect.DISCARD_1_CARD_THEN_TARGET_UP_TO_2_SPELL_CARDS_AND_DESTROY)
                    && aiCardsInHand.size() >= 1 && ai.getAiBoardUnderstander().getOpponentMonsterSpellBoardAnalysis().getIndexesOfSpellTrapsWorthyOfKilling().size() >= 2) {
                    ai.setShouldRedirectAIMind(true);
                    ai.setAiActionType(AIActionType.ACTIVATE_EFFECT);
                    ai.setAiFurtherActivationInput(AIFurtherActivationInput.SELECT_A_CARD_FROM_YOUR_HAND_TO_DISCARD);
                    return "select --spell " + Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(i + 1, true);
                }
            }
        }
        for (int i = 0; i < aiCardsInHand.size(); i++) {
            if (Card.isCardASpell(aiCardsInHand.get(i))) {
                SpellCard spellCard = (SpellCard) aiCardsInHand.get(i);
                ArrayList<QuickSpellEffect> quickSpellEffects = spellCard.getQuickSpellEffects();
                if (quickSpellEffects.contains(QuickSpellEffect.DISCARD_1_CARD_THEN_TARGET_UP_TO_2_SPELL_CARDS_AND_DESTROY)
                    && aiCardsInHand.size() >= 2 && ai.getAiBoardUnderstander().getOpponentMonsterSpellBoardAnalysis().getIndexesOfSpellTrapsWorthyOfKilling().size() >= 2) {
                    ai.setShouldRedirectAIMind(true);
                    ai.setAiActionType(AIActionType.ACTIVATE_EFFECT);
                    ai.setAiFurtherActivationInput(AIFurtherActivationInput.SELECT_A_CARD_FROM_YOUR_HAND_TO_DISCARD);
                    return "select --hand " + (i + 1);
                }
            }
        }
        for (int i = 0; i < 5; i++) {
            if (Card.isCardASpell(aiSpellTrapCards.get(i))) {
                SpellCard spellCard = (SpellCard) aiSpellTrapCards.get(i);
                ArrayList<QuickSpellEffect> quickSpellEffects = spellCard.getQuickSpellEffects();
                if (quickSpellEffects.contains(QuickSpellEffect.TARGET_1_SPELL_TRAP_CARD_AND_DESTROY)
                    && ai.getAiBoardUnderstander().getOpponentMonsterSpellBoardAnalysis().getIndexesOfSpellTrapsWorthyOfKilling().size() == 1) {
                    ai.setShouldRedirectAIMind(true);
                    ai.setAiActionType(AIActionType.ACTIVATE_EFFECT);
                    ai.setAiFurtherActivationInput(AIFurtherActivationInput.SELECT_OPPONENT_SPELL_TRAP_CARDS_WORTHY_OF_KILLING);
                    return "select --spell " + Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(i + 1, true);
                }
            }
        }
        for (int i = 0; i < aiCardsInHand.size(); i++) {
            if (Card.isCardASpell(aiCardsInHand.get(i))) {
                SpellCard spellCard = (SpellCard) aiCardsInHand.get(i);
                ArrayList<QuickSpellEffect> quickSpellEffects = spellCard.getQuickSpellEffects();
                if (quickSpellEffects.contains(QuickSpellEffect.TARGET_1_SPELL_TRAP_CARD_AND_DESTROY)
                    && ai.getAiBoardUnderstander().getOpponentMonsterSpellBoardAnalysis().getIndexesOfSpellTrapsWorthyOfKilling().size() == 1) {
                    ai.setShouldRedirectAIMind(true);
                    ai.setAiActionType(AIActionType.ACTIVATE_EFFECT);
                    ai.setAiFurtherActivationInput(AIFurtherActivationInput.SELECT_OPPONENT_SPELL_TRAP_CARDS_WORTHY_OF_KILLING);
                    return "select --hand " + (i + 1);
                }
            }
        }
        return "next phase";
    }

    protected String findStoppingMonstersFromAttackingSpellCardsFromHandToActivate(AI ai) {
        ArrayList<Card> aiCardsInHand = ai.getAiBoardUnderstander().getAiCardsInHand();
        for (int i = 0; i < aiCardsInHand.size(); i++) {
            if (Card.isCardASpell(aiCardsInHand.get(i))) {
                SpellCard spellCard = (SpellCard) aiCardsInHand.get(i);
                ArrayList<ContinuousSpellCardEffect> continuousSpellCardEffects = spellCard.getContinuousSpellCardEffects();
                if (continuousSpellCardEffects.contains(ContinuousSpellCardEffect.UNTIL_THIS_CARD_IS_FACE_UP_ON_FIELD_OPPONENT_MONSTERS_CANT_ATTACK)) {
                    ai.setShouldRedirectAIMind(true);
                    ai.setAiActionType(AIActionType.ACTIVATE_EFFECT);
                    return "select --hand " + (i + 1);
                }
            }
        }
        return "next phase";
    }

    protected String findMonsterWithGoodAttackFromHandToNormalSummon(AI ai) {
        ArrayList<Card> aiCardsInHand = ai.getAiBoardUnderstander().getAiCardsInHand();
        for (int i = 0; i < aiCardsInHand.size(); i++) {
            if (Card.isCardAMonster(aiCardsInHand.get(i))) {
                MonsterCard monsterCard = (MonsterCard) aiCardsInHand.get(i);
                if (monsterCard.getAttackPower() >= 1800 && monsterCard.getLevel() <= 4) {
                    ai.setShouldRedirectAIMind(true);
                    ai.setAiActionType(AIActionType.NORMAL_SUMMON);
                    return "select --hand " + (i + 1);
                }
            }
        }
        return "next phase";
    }

    protected String findQuickSpellCardOnAISideWithSpellTrapDestroyingEffectInChain(AI ai) {
        ArrayList<Card> aiSpellTrapCards = ai.getAiBoardUnderstander().getAiSpellTrapCards();
        for (int i = 0; i < 5; i++) {
            if (Card.isCardASpell(aiSpellTrapCards.get(i))) {
                SpellCard spellCard = (SpellCard) aiSpellTrapCards.get(i);
                ArrayList<QuickSpellEffect> quickSpellEffects = spellCard.getQuickSpellEffects();
                if (quickSpellEffects.contains(QuickSpellEffect.TARGET_1_SPELL_TRAP_CARD_AND_DESTROY)) {
                    ai.setAiFurtherActivationInput(AIFurtherActivationInput.SELECT_OPPONENT_SPELL_TRAP_CARDS_WORTHY_OF_KILLING_IN_CHAIN);
                    return "select --spell " + Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(i + 1, true);
                }
            }
        }
        for (int i = 0; i < 5; i++) {
            if (Card.isCardASpell(aiSpellTrapCards.get(i))) {
                SpellCard spellCard = (SpellCard) aiSpellTrapCards.get(i);
                ArrayList<QuickSpellEffect> quickSpellEffects = spellCard.getQuickSpellEffects();
                if (quickSpellEffects.contains(QuickSpellEffect.DISCARD_1_CARD_THEN_TARGET_UP_TO_2_SPELL_CARDS_AND_DESTROY)) {
                    ai.setAiFurtherActivationInput(AIFurtherActivationInput.SELECT_A_CARD_FROM_YOUR_HAND_TO_DISCARD_IN_CHAIN);
                    return "select --spell " + Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(i + 1, true);
                }
            }
        }
        return "select --spell 1";
    }

    protected String findDefensiveTrapCardsInHandForBattlePhaseToSet(AI ai) {
        ArrayList<Card> aiCardsInHand = ai.getAiBoardUnderstander().getAiCardsInHand();
        for (int i = 0; i < aiCardsInHand.size(); i++) {
            if (Card.isCardATrap(aiCardsInHand.get(i))) {
                TrapCard trapCard = (TrapCard) aiCardsInHand.get(i);
                ArrayList<MonsterAttackingTrapCardEffect> monsterAttackingTrapCardEffects = trapCard.getMonsterAttackingTrapCardEffects();
                if (monsterAttackingTrapCardEffects.contains(MonsterAttackingTrapCardEffect.DESTROY_ALL_OPPONENT_ATTACK_POSITION_MONSTERS)
                    || monsterAttackingTrapCardEffects.contains(MonsterAttackingTrapCardEffect.NEGATE_OPPONENT_ATTACK)
                    || monsterAttackingTrapCardEffects.contains(MonsterAttackingTrapCardEffect.INFLICT_DAMAGE_TO_OPPONENT_EQUAL_TO_MONSTERS_ATK)) {
                    ai.setShouldRedirectAIMind(true);
                    ai.setAiActionType(AIActionType.SET);
                    return "select --hand " + (i + 1);
                }
            }
        }
        return "select --hand 1";
    }

    protected String findMonsterInBoardWithDecentAttackToChangeCardPositionToFaceUpAttackPosition(AI ai) {
        ArrayList<Card> aiMonsterCardsInBoard = ai.getAiBoardUnderstander().getAiMonsterCards();
        for (int i = 0; i < aiMonsterCardsInBoard.size(); i++) {
            if (Card.isCardAMonster(aiMonsterCardsInBoard.get(i)) && ((MonsterCard) aiMonsterCardsInBoard.get(i)).getAttackPower() >= 1800 &&
                (aiMonsterCardsInBoard.get(i)).getCardPosition().equals(CardPosition.FACE_UP_DEFENSE_POSITION)) {
                ai.setShouldRedirectAIMind(true);
                ai.setAiActionType(AIActionType.CHANGE_CARD_POSITION_TO_ATTACK);
                return "select --monster " + Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(i + 1, true);
            }
        }
        return "select --monster 1";
    }

    protected String findBestMonsterInBoardThatDominatesOpponentMonsterToAttack(AI ai) {
        ArrayList<Card> aiMonsterCardsInBoard = ai.getAiBoardUnderstander().getAiMonsterCards();
        ArrayList<Card> opponentMonsterCardsInBoard = ai.getAiBoardUnderstander().getOpponentMonsterCards();
        boolean doesOpponentHaveAMonster = false;
        for (int j = 0; j < opponentMonsterCardsInBoard.size(); j++) {
            if (Card.isCardAMonster(opponentMonsterCardsInBoard.get(j))) {
                doesOpponentHaveAMonster = true;
            }
        }
        if (!doesOpponentHaveAMonster) {
            for (int i = 0; i < aiMonsterCardsInBoard.size(); i++) {
                if (Card.isCardAMonster(aiMonsterCardsInBoard.get(i)) && !((MonsterCard) aiMonsterCardsInBoard.get(i)).isHasCardAlreadyAttacked() &&
                    (aiMonsterCardsInBoard.get(i)).getCardPosition().equals(CardPosition.FACE_UP_ATTACK_POSITION)) {
                    ai.setShouldRedirectAIMind(true);
                    ai.setAiActionType(AIActionType.ATTACK_DIRECTLY);
                    return "select --monster " + Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(i + 1, true);
                }
            }
        }
        int bestI = 0;
        int bestJ = 0;
        int minimumAttack = 10000;
        int maximumAttack = -10000;
        int foundAttack;
        for (int j = 0; j < opponentMonsterCardsInBoard.size(); j++) {
            bestI = 0;
            bestJ = 0;
            minimumAttack = 10000;
            maximumAttack = -10000;
            for (int i = 0; i < aiMonsterCardsInBoard.size(); i++) {
                if (Card.isCardAMonster(opponentMonsterCardsInBoard.get(j)) && Card.isCardAMonster(aiMonsterCardsInBoard.get(i))
                    && !((MonsterCard) aiMonsterCardsInBoard.get(i)).isHasCardAlreadyAttacked() &&
                    (aiMonsterCardsInBoard.get(i)).getCardPosition().equals(CardPosition.FACE_UP_ATTACK_POSITION) &&
                    ai.getAiBattlePhaseMind().doesMonsterDominateOtherMonsterConsideringEquipAndFieldSpellCards
                        (new CardLocation(ai.getAiBoardUnderstander().aiMonsterCardsRowOfCardLocation, i + 1), new CardLocation(ai.getAiBoardUnderstander().opponentMonsterCardsRowOfCardLocation, j + 1), 0)) {
                    foundAttack = MonsterCard.giveATKDEFConsideringEffects("attack", new CardLocation(ai.getAiBoardUnderstander().aiMonsterCardsRowOfCardLocation, i + 1), 0);

                    if (opponentMonsterCardsInBoard.get(j).getCardPosition().equals(CardPosition.FACE_UP_ATTACK_POSITION)) {
                        if (foundAttack > maximumAttack) {
                            maximumAttack = foundAttack;
                            bestI = i;
                            bestJ = j;
                        }
                    } else {
                        if (foundAttack < minimumAttack) {
                            minimumAttack = foundAttack;
                            bestI = i;
                            bestJ = j;
                        }
                    }
                }
            }
            if (maximumAttack != -10000 || minimumAttack != 10000) {
                ai.setShouldRedirectAIMind(true);
                setAIActionTypeForAttackingOpponentMonster(ai, Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(bestJ + 1, false));
                return "select --monster " + Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(bestI + 1, true);
            }
        }
        return "nothing found";
    }

    protected void setAIActionTypeForAttackingOpponentMonster(AI ai, int j) {
        if (j == 1) {
            ai.setAiActionType(AIActionType.ATTACK_1);
        } else if (j == 2) {
            ai.setAiActionType(AIActionType.ATTACK_2);
        } else if (j == 3) {
            ai.setAiActionType(AIActionType.ATTACK_3);
        } else if (j == 4) {
            ai.setAiActionType(AIActionType.ATTACK_4);
        } else {
            ai.setAiActionType(AIActionType.ATTACK_5);
        }
    }

    protected String findTrapCardOnAISideWithMonsterDestroyingInSummoningEffect(AI ai) {
        System.out.println("AI ATTENTION PLEASE 11");
        ArrayList<Card> aiSpellTrapCards = ai.getAiBoardUnderstander().getAiSpellTrapCards();
        for (int i = 0; i < 5; i++) {
            if (Card.isCardATrap(aiSpellTrapCards.get(i))) {
                TrapCard trapCard = (TrapCard) aiSpellTrapCards.get(i);
                ArrayList<NormalSummonTrapCardEffect> normalSummonTrapCardEffects = trapCard.getNormalSummonTrapCardEffects();
                ArrayList<FlipSummonTrapCardEffect> flipSummonTrapCardEffects = trapCard.getFlipSummonTrapCardEffects();
                ArrayList<TributeSummonTrapCardEffect> tributeSummonTrapCardEffects = trapCard.getTributeSummonTrapCardEffects();
                ArrayList<SpecialSummonTrapCardEffect> specialSummonTrapCardEffects = trapCard.getSpecialSummonTrapCardEffects();
                ArrayList<RitualSummonTrapCardEffect> ritualSummonTrapCardEffects = trapCard.getRitualSummonTrapCardEffects();
                if (normalSummonTrapCardEffects.size() > 0 || flipSummonTrapCardEffects.size() > 0 || tributeSummonTrapCardEffects.size() > 0
                    || specialSummonTrapCardEffects.size() > 0 || ritualSummonTrapCardEffects.size() > 0) {
                    return "select --spell " + Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(i + 1, true);
                }
            }
        }
        return "select --spell 1";
    }

    protected String findActiveMainCardInPreviousUninterruptedAction(AI ai) {
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(0);
        Action uninterruptedAction = uninterruptedActions.get(uninterruptedActions.size() - 1);
        CardLocation mainCardLocation = uninterruptedAction.getFinalMainCardLocation();
        RowOfCardLocation mainCardRowOfCardLocation = mainCardLocation.getRowOfCardLocation();
        int mainCardIndex = mainCardLocation.getIndex();
        String finalDecisionRowOfCardLocation = "";
        int finalDecisionIndex = Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(mainCardIndex, false);
        if (mainCardRowOfCardLocation.equals(RowOfCardLocation.ALLY_SPELL_ZONE) || mainCardRowOfCardLocation.equals(RowOfCardLocation.OPPONENT_SPELL_ZONE)) {
            finalDecisionRowOfCardLocation = " --opponent --spell ";
        } else if (mainCardRowOfCardLocation.equals(RowOfCardLocation.ALLY_MONSTER_ZONE) || mainCardRowOfCardLocation.equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) {
            finalDecisionRowOfCardLocation = " --opponent --monster ";
        }
        return "select" + finalDecisionRowOfCardLocation + finalDecisionIndex;
    }

    protected String findQuickSpellCardInHandToSet(AI ai) {
        ArrayList<Card> aiCardsInHand = ai.getAiBoardUnderstander().getAiCardsInHand();
        for (int i = 0; i < aiCardsInHand.size(); i++) {
            if (Card.isCardASpell(aiCardsInHand.get(i))) {
                SpellCard spellCard = (SpellCard) aiCardsInHand.get(i);
                ArrayList<QuickSpellEffect> quickSpellEffects = spellCard.getQuickSpellEffects();
                if (quickSpellEffects.contains(QuickSpellEffect.TARGET_1_SPELL_TRAP_CARD_AND_DESTROY)
                    || quickSpellEffects.contains(QuickSpellEffect.DISCARD_1_CARD_THEN_TARGET_UP_TO_2_SPELL_CARDS_AND_DESTROY)) {
                    ai.setShouldRedirectAIMind(true);
                    ai.setAiActionType(AIActionType.SET);
                    return "select --hand " + (i + 1);
                }
            }
        }
        return "next phase";
    }

    protected String findCardDrawingSpellCardInHandToActivate(AI ai) {
        ArrayList<Card> aiCardsInHand = ai.getAiBoardUnderstander().getAiCardsInHand();
        for (int i = 0; i < aiCardsInHand.size(); i++) {
            if (Card.isCardASpell(aiCardsInHand.get(i))) {
                SpellCard spellCard = (SpellCard) aiCardsInHand.get(i);
                ArrayList<NormalSpellCardEffect> normalSpellCardEffects = spellCard.getNormalSpellCardEffects();
                if (normalSpellCardEffects.contains(NormalSpellCardEffect.DRAW_2_CARDS)) {
                    ai.setShouldRedirectAIMind(true);
                    ai.setAiActionType(AIActionType.ACTIVATE_EFFECT);
                    return "select --hand " + (i + 1);
                }
            }
        }
        return "next phase";
    }

    protected String findMonsterSwappingSpellCardsInHandOrBoardToActivate(AI ai) {
        ArrayList<Card> aiSpellTrapCards = ai.getAiBoardUnderstander().getAiSpellTrapCards();
        ArrayList<Card> aiCardsInHand = ai.getAiBoardUnderstander().getAiCardsInHand();
        for (int i = 0; i < aiSpellTrapCards.size(); i++) {
            if (Card.isCardASpell(aiSpellTrapCards.get(i))) {
                SpellCard spellCard = (SpellCard) aiSpellTrapCards.get(i);
                ArrayList<NormalSpellCardEffect> normalSpellCardEffects = spellCard.getNormalSpellCardEffects();
                if (normalSpellCardEffects.contains(NormalSpellCardEffect.CONTROL_ONE_OF_OPPONENTS_MONSTERS_UNTIL_END_PHASE)) {
                    ai.setShouldRedirectAIMind(true);
                    ai.setAiActionType(AIActionType.ACTIVATE_EFFECT);
                    return "select --spell " + Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(i + 1, true);
                }
            }
        }
        for (int i = 0; i < aiCardsInHand.size(); i++) {
            if (Card.isCardASpell(aiCardsInHand.get(i))) {
                SpellCard spellCard = (SpellCard) aiCardsInHand.get(i);
                ArrayList<NormalSpellCardEffect> normalSpellCardEffects = spellCard.getNormalSpellCardEffects();
                if (normalSpellCardEffects.contains(NormalSpellCardEffect.CONTROL_ONE_OF_OPPONENTS_MONSTERS_UNTIL_END_PHASE)) {
                    ai.setShouldRedirectAIMind(true);
                    ai.setAiActionType(AIActionType.ACTIVATE_EFFECT);
                    return "select --hand " + (i + 1);
                }
            }
        }
        return "next phase";
    }

    protected String findMonsterWithStoppingTrapCardActivationFromHandToNormalSummon(AI ai) {
        ArrayList<Card> aiCardsInHand = ai.getAiBoardUnderstander().getAiCardsInHand();
        for (int i = 0; i < aiCardsInHand.size(); i++) {
            if (Card.isCardAMonster(aiCardsInHand.get(i))) {
                MonsterCard monsterCard = (MonsterCard) aiCardsInHand.get(i);
                ArrayList<ContinuousMonsterEffect> continuousMonsterEffects = monsterCard.getContinuousMonsterEffects();
                if (continuousMonsterEffects.contains(ContinuousMonsterEffect.OPPONENT_CANNOT_ACTIVATE_TRAP_CARDS_WHILE_THIS_CARD_IS_FACE_UP)) {
                    ai.setShouldRedirectAIMind(true);
                    ai.setAiActionType(AIActionType.NORMAL_SUMMON);
                    return "select --hand " + (i + 1);
                }
            }
        }
        return "next phase";
    }

    protected String findMonsterWithFlipEffectToFlipSummon(AI ai) {
        ArrayList<Card> aiMonsterCards = ai.getAiBoardUnderstander().getAiMonsterCards();
        for (int i = 0; i < aiMonsterCards.size(); i++) {
            if (Card.isCardAMonster(aiMonsterCards.get(i))) {
                MonsterCard monsterCard = (MonsterCard) aiMonsterCards.get(i);
                ArrayList<FlipEffect> flipEffects = monsterCard.getFlipEffects();
                if (flipEffects.contains(FlipEffect.DESTROY_1_MONSTER_ON_THE_FIELD)) {
                    ai.setShouldRedirectAIMind(true);
                    ai.setAiActionType(AIActionType.FLIP_SUMMON);
                    return "select --monster " + Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(i + 1, true);
                }
            }
        }
        return "next phase";
    }

    protected String findCardDiscardingTrapCardInBoardToActivate(AI ai) {
        ArrayList<Card> aiSpellTrapCards = ai.getAiBoardUnderstander().getAiSpellTrapCards();
        for (int i = 0; i < aiSpellTrapCards.size(); i++) {
            if (Card.isCardATrap(aiSpellTrapCards.get(i))) {
                TrapCard trapCard = (TrapCard) aiSpellTrapCards.get(i);
                ArrayList<NormalTrapCardEffect> normalTrapCardEffects = trapCard.getNormalTrapCardEffects();
                if (normalTrapCardEffects.contains(NormalTrapCardEffect.OPPONENT_DISCARDS_ALL_CARDS_WITH_A_GIVEN_NAME_OTHERWISE_OWNER_LOSES_ONE_RANDOM_CARD)) {
                    ai.setShouldRedirectAIMind(true);
                    ai.setAiActionType(AIActionType.ACTIVATE_EFFECT);
                    return "select --spell " + Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(i + 1, true);
                }
            }
        }
        return "next phase";
    }

    protected String findMonsterNeedingTributeInHandToNormalSummon(AI ai) {
        ArrayList<Card> aiCardsInHand = ai.getAiBoardUnderstander().getAiCardsInHand();
        for (int i = 0; i < aiCardsInHand.size(); i++) {
            if (Card.isCardAMonster(aiCardsInHand.get(i))) {
                MonsterCard monsterCard = (MonsterCard) aiCardsInHand.get(i);
                ArrayList<SummoningRequirement> summoningRequirements = monsterCard.getSummoningRequirements();
                if (summoningRequirements.contains(SummoningRequirement.TRIBUTE_2_MONSTERS)) {
                    ai.setShouldRedirectAIMind(true);
                    ai.setAiActionType(AIActionType.NORMAL_SUMMON);
                    return "select --hand " + (i + 1);
                }
            }
        }
        return "next phase";
    }

    protected String findMonsterNeedingTributeInHandToSpecialSummon(AI ai) {
        ArrayList<Card> aiCardsInHand = ai.getAiBoardUnderstander().getAiCardsInHand();
        for (int i = 0; i < aiCardsInHand.size(); i++) {
            if (Card.isCardAMonster(aiCardsInHand.get(i))) {
                MonsterCard monsterCard = (MonsterCard) aiCardsInHand.get(i);
                ArrayList<SummoningRequirement> summoningRequirements = monsterCard.getSummoningRequirements();
                if (summoningRequirements.contains(SummoningRequirement.TRIBUTE_3_MONSTERS)) {
                    ai.setShouldRedirectAIMind(true);
                    ai.setAiActionType(AIActionType.SPECIAL_SUMMON);
                    return "select --hand " + (i + 1);
                }
            }
        }
        return "next phase";
    }

    protected String findMonsterInOwnGraveyardToSpecialSummonWithThisBeingAttackedEffect(AI ai, BeingAttackedEffect beingAttackedEffect) {
        ArrayList<Card> aiCardsInGraveyard = ai.getAiBoardUnderstander().getAiCardsInGraveyard();
        for (int i = 0; i < aiCardsInGraveyard.size(); i++) {
            if (Card.isCardAMonster(aiCardsInGraveyard.get(i))) {
                MonsterCard monsterCard = (MonsterCard) aiCardsInGraveyard.get(i);
                ArrayList<BeingAttackedEffect> beingAttackedEffectsOfMonster = monsterCard.getBeingAttackedEffects();
                if (beingAttackedEffectsOfMonster.contains(beingAttackedEffect)) {
                    ai.setShouldRedirectAIMind(true);
                    ai.setAiActionType(AIActionType.SPECIAL_SUMMON);
                    return "select --graveyard " + (i + 1);
                }
            }
        }
        return "next phase";
    }

    protected String findMonsterWithHighestAttackInOwnGraveyardToSpecialSummon(AI ai) {
        ArrayList<Card> aiCardsInGraveyard = ai.getAiBoardUnderstander().getAiCardsInGraveyard();
        int highestAttack = 0;
        int index = 0;
        for (int i = 0; i < aiCardsInGraveyard.size(); i++) {
            if (Card.isCardAMonster(aiCardsInGraveyard.get(i))) {
                MonsterCard monsterCard = (MonsterCard) aiCardsInGraveyard.get(i);
                if (monsterCard.getAttackPower() > highestAttack) {
                    highestAttack = monsterCard.getAttackPower();
                    index = i;
                }
            }
        }
        ai.setShouldRedirectAIMind(true);
        ai.setAiActionType(AIActionType.SPECIAL_SUMMON);
        return "select --graveyard " + (index + 1);
    }

    protected String findTrapCardInBoardToActivateWithThisMonsterAttackingTrapCardEffect(AI ai, MonsterAttackingTrapCardEffect monsterAttackingTrapCardEffect) {
        ArrayList<Card> aiSpellTrapCards = ai.getAiBoardUnderstander().getAiSpellTrapCards();
        for (int i = 0; i < aiSpellTrapCards.size(); i++) {
            if (Card.isCardATrap(aiSpellTrapCards.get(i))) {
                TrapCard trapCard = (TrapCard) aiSpellTrapCards.get(i);
                ArrayList<MonsterAttackingTrapCardEffect> monsterAttackingTrapCardEffects = trapCard.getMonsterAttackingTrapCardEffects();
                if (monsterAttackingTrapCardEffects.contains(monsterAttackingTrapCardEffect)) {
                    ai.setShouldRedirectAIMind(true);
                    ai.setAiActionType(AIActionType.ACTIVATE_EFFECT);
                    return "select --spell " + Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(i + 1, true);
                }
            }
        }
        return "next phase";
    }

    protected String findNormalMonsterCyberseCardInDeckGraveyardOrHandTo(AI ai, boolean isAttackingOrDefensive) {
        ArrayList<Card> aiCardsInDeck = ai.getAiBoardUnderstander().getAiCardsInDeck();
        for (int i = 0; i < aiCardsInDeck.size(); i++) {
            if (Card.isCardAMonster(aiCardsInDeck.get(i))) {
                MonsterCard monsterCard = (MonsterCard) aiCardsInDeck.get(i);
                if (monsterCard.getMonsterCardFamily().equals(MonsterCardFamily.CYBERSE)
                    && monsterCard.getMonsterCardValue().equals(MonsterCardValue.NORMAL)) {
                    if (isAttackingOrDefensive) {
                        ai.setAiFurtherActivationInput(AIFurtherActivationInput.ATTACKING);
                    } else {
                        ai.setAiFurtherActivationInput(AIFurtherActivationInput.DEFENSIVE);
                    }
                    return "select --deck " + (i + 1);
                }
            }
        }
        ArrayList<Card> aiCardsInHand = ai.getAiBoardUnderstander().getAiCardsInHand();
        for (int i = 0; i < aiCardsInHand.size(); i++) {
            if (Card.isCardAMonster(aiCardsInHand.get(i))) {
                MonsterCard monsterCard = (MonsterCard) aiCardsInHand.get(i);
                if (monsterCard.getMonsterCardFamily().equals(MonsterCardFamily.CYBERSE)
                    && monsterCard.getMonsterCardValue().equals(MonsterCardValue.NORMAL)) {
                    if (isAttackingOrDefensive) {
                        ai.setAiFurtherActivationInput(AIFurtherActivationInput.ATTACKING);
                    } else {
                        ai.setAiFurtherActivationInput(AIFurtherActivationInput.DEFENSIVE);
                    }
                    return "select --hand " + (i + 1);
                }
            }
        }
        ArrayList<Card> aiCardsInGraveyard = ai.getAiBoardUnderstander().getAiCardsInGraveyard();
        for (int i = 0; i < aiCardsInGraveyard.size(); i++) {
            if (Card.isCardAMonster(aiCardsInGraveyard.get(i))) {
                MonsterCard monsterCard = (MonsterCard) aiCardsInGraveyard.get(i);
                if (monsterCard.getMonsterCardFamily().equals(MonsterCardFamily.CYBERSE)
                    && monsterCard.getMonsterCardValue().equals(MonsterCardValue.NORMAL)) {
                    if (isAttackingOrDefensive) {
                        ai.setAiFurtherActivationInput(AIFurtherActivationInput.ATTACKING);
                    } else {
                        ai.setAiFurtherActivationInput(AIFurtherActivationInput.DEFENSIVE);
                    }
                    return "select --graveyard " + (i + 1);
                }
            }
        }
        return "next phase";
    }
}
