package project.controller.duel.GamePackage.ai;

import project.controller.duel.CardEffects.MonsterEffectEnums.BeingAttackedEffect;
import project.controller.duel.CardEffects.MonsterEffectEnums.FlipEffect;
import project.controller.duel.CardEffects.TrapEffectEnums.MonsterAttackingTrapCardEffect;
import project.controller.duel.GamePackage.Action;
import project.controller.duel.GamePackage.ActionType;
import project.controller.duel.GamePackage.DuelBoard;
import project.controller.duel.PreliminaryPackage.GameManager;
import project.controller.duel.Utility.Utility;
import project.model.aidata.AIFurtherActivationInput;
import project.model.aidata.AISpellTrapSelections;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.CardPosition;
import project.model.cardData.MonsterCardData.MonsterCard;
import project.model.cardData.TrapCardData.TrapCard;

import java.util.ArrayList;

public class AIBattlePhaseMind {
    public static int numberOfActions = 0;
    private AICardFinder aiCardFinder;
    private AIKeyVariablesUpdater aiKeyVariablesUpdater;

    public AIBattlePhaseMind() {
        aiCardFinder = new AICardFinder();
        aiKeyVariablesUpdater = new AIKeyVariablesUpdater();
    }

    //AI BATTLE PHASE MIND
    protected String getCommandWhenAIIsInBattlePhaseWithNoNecessaryQuery(AI ai) {
        //ArrayList<CardLocation> allySpellCardLocationsNoNull = ai.createCardLocationArrayFromThisArray(false, false);
        //ArrayList<CardLocation> allyMonsterCardLocationsNoNull = ai.createCardLocationArrayFromThisArray(false, true);
        //ArrayList<CardLocation> opponentSpellCardLocationsNoNull = ai.createCardLocationArrayFromThisArray(true, false);
        //ArrayList<CardLocation> opponentMonsterCardLocationsNoNull = ai.createCardLocationArrayFromThisArray(true, true);
        //boolean doOpponentMonstersDominateAIWithBuffEffects = doMonstersInThisSideDominateTheOtherSideExtendable(opponentMonsterCardLocationsNoNull, allyMonsterCardLocationsNoNull, 0, true);
        //boolean doOpponentMonstersDominateAIAlone = doMonstersInThisSideDominateTheOtherSideExtendable(opponentMonsterCardLocationsNoNull, allyMonsterCardLocationsNoNull, 0, false);
        //boolean doAIMonstersDominateOpponentWithBuffEffects = doMonstersInThisSideDominateTheOtherSideExtendable(allyMonsterCardLocationsNoNull, opponentMonsterCardLocationsNoNull, 0, true);
        if ((!aiKeyVariablesUpdater.isDoesOpponentHaveMonsterDestroyingTrapCardsInAttacking() ||
            aiKeyVariablesUpdater.isDoesOpponentHaveDefensiveTrapCardsInBoard() &&
                aiKeyVariablesUpdater.isDoesAIHaveSpellDestroyingQuickSpellCardsInBoard() ||
            aiKeyVariablesUpdater.isDoesAIHaveMonstersWithStoppingTrapCardActivationEffectInBoard()) &&
            !aiKeyVariablesUpdater.isDoesOpponentHaveStoppingMonstersOfAttackingSpellCardsCurrentlyActiveInBoard()) {
            String output = aiCardFinder.findBestMonsterInBoardThatDominatesOpponentMonsterToAttack(ai);
            if (output.equals("nothing found")) {
                return "next phase";
            } else {
                return output;
            }
        }
        return "next phase";
    }


    //AI BATTLE PHASE CHAIN MIND
    protected String getCommandForChoosingToActivateTrapInChainOrNotInBattlePhase(AI ai) {
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(0);
        Action attackAction = uninterruptedActions.get(uninterruptedActions.size() - 1);
        CardLocation attackingMonsterLocation = attackAction.getFinalMainCardLocation();
        if (attackAction.getActionType().equals(ActionType.ALLY_MONSTER_ATTACKING_OPPONENT_MONSTER) || attackAction.getActionType().equals(ActionType.OPPONENT_MONSTER_ATTACKING_ALLY_MONSTER)) {
            CardLocation beingAttackedLocation = attackAction.getTargetingCards().get(attackAction.getTargetingCards().size() - 1);
            if (doesMonsterDominateOtherMonsterConsideringEquipAndFieldSpellCards(beingAttackedLocation, attackingMonsterLocation, 0)) {
                return "no";
            }
        }
        int aiTurn = GameManager.getDuelControllerByIndex(0).getAiTurn();
        int enemyLifePoints = GameManager.getDuelControllerByIndex(0).getLifePoints().get(2 - aiTurn);
        if ((enemyLifePoints < MonsterCard.giveATKDEFConsideringEffects("attack", attackingMonsterLocation, 0)
            || 1600 <= MonsterCard.giveATKDEFConsideringEffects("attack", attackingMonsterLocation, 0)) &&
            aiKeyVariablesUpdater.isDoesAIHaveDamageInflictingTrapCardsInBattlePhaseInBoard()) {
            ai.setAiSpellTrapSelections(AISpellTrapSelections.SELECT_A_DAMAGE_INFLICTING_TRAP_CARD_IN_BOARD);
            return "yes";
        }
        if (ai.getAiBoardUnderstander().getOpponentMonsterSpellBoardAnalysis().getIndexesOfMonstersWorthyOfKillingAndFaceUpAttackPosition().size() +
            ai.getAiBoardUnderstander().getOpponentMonsterSpellBoardAnalysis().getIndexesOfMonstersGoodToBeKilledAndFaceUpAttackPosition().size() >= 1 &&
            aiKeyVariablesUpdater.isDoesAIHaveMonsterDestroyingTrapCardsInBattlePhaseInBoard()) {
            ai.setAiSpellTrapSelections(AISpellTrapSelections.SELECT_A_MONSTER_DESTROYING_TRAP_CARD_IN_BOARD);
            return "yes";
        }
        if (aiKeyVariablesUpdater.isDoesAIHaveMonstersWithEffectSettingToZeroInAIGraveyard() && aiKeyVariablesUpdater.isDoesAIHaveSpecialSummoningTrapCardsInBoard()) {
            ai.setAiFurtherActivationInput(AIFurtherActivationInput.SELECT_A_MONSTER_SETTING_ATTACK_TO_0_EFFECT_FROM_OWN_GRAVEYARD);
            ai.setAiSpellTrapSelections(AISpellTrapSelections.SELECT_A_SPECIAL_SUMMONING_TRAP_CARD_IN_BOARD);
            return "yes";
        }
        if (aiKeyVariablesUpdater.isDoesAIHaveSelfDestructionMonstersInAIGraveyard() && aiKeyVariablesUpdater.isDoesAIHaveSpecialSummoningTrapCardsInBoard()) {
            ai.setAiFurtherActivationInput(AIFurtherActivationInput.SELECT_A_SELF_DESTRUCTION_MONSTER_FROM_OWN_GRAVEYARD);
            ai.setAiSpellTrapSelections(AISpellTrapSelections.SELECT_A_SPECIAL_SUMMONING_TRAP_CARD_IN_BOARD);
            return "yes";
        }
        if (aiKeyVariablesUpdater.isDoesAIHaveMonstersWithHighAttackInAIGraveyard() && aiKeyVariablesUpdater.isDoesAIHaveSpecialSummoningTrapCardsInBoard()) {
            ai.setAiFurtherActivationInput(AIFurtherActivationInput.SELECT_A_MONSTER_WITH_HIGH_ATTACK_FROM_OWN_GRAVEYARD);
            ai.setAiSpellTrapSelections(AISpellTrapSelections.SELECT_A_SPECIAL_SUMMONING_TRAP_CARD_IN_BOARD);
            return "yes";
        }
        if (aiKeyVariablesUpdater.isDoesAIHaveMonsterAttackNegatingTrapCardsInBattlePhaseInBoard()) {
            ai.setAiSpellTrapSelections(AISpellTrapSelections.SELECT_A_MONSTER_ATTACK_NEGATING_TRAP_CARD_IN_BOARD);
            return "yes";
        }
        return "no";
    }

    protected String getCommandForChoosingSpellTrapToActivateInChainInBattlePhase(AI ai) {
        if (ai.getAiSpellTrapSelections().equals(AISpellTrapSelections.SELECT_A_DAMAGE_INFLICTING_TRAP_CARD_IN_BOARD)) {
            ai.setAiSpellTrapSelections(AISpellTrapSelections.NOTHING);
            return aiCardFinder.findTrapCardInBoardToActivateWithThisMonsterAttackingTrapCardEffect(ai, MonsterAttackingTrapCardEffect.INFLICT_DAMAGE_TO_OPPONENT_EQUAL_TO_MONSTERS_ATK);
        } else if (ai.getAiSpellTrapSelections().equals(AISpellTrapSelections.SELECT_A_MONSTER_DESTROYING_TRAP_CARD_IN_BOARD)) {
            ai.setAiSpellTrapSelections(AISpellTrapSelections.NOTHING);
            return aiCardFinder.findTrapCardInBoardToActivateWithThisMonsterAttackingTrapCardEffect(ai, MonsterAttackingTrapCardEffect.DESTROY_ALL_OPPONENT_ATTACK_POSITION_MONSTERS);
        } else if (ai.getAiSpellTrapSelections().equals(AISpellTrapSelections.SELECT_A_SPECIAL_SUMMONING_TRAP_CARD_IN_BOARD)) {
            ai.setAiSpellTrapSelections(AISpellTrapSelections.NOTHING);
            return aiCardFinder.findTrapCardInBoardToActivateWithThisMonsterAttackingTrapCardEffect(ai, MonsterAttackingTrapCardEffect.SPECIAL_SUMMON_ONE_MONSTER_IN_YOUR_GRAVEYARD_IN_FACE_UP_ATTACK_POSITION);
        } else if (ai.getAiSpellTrapSelections().equals(AISpellTrapSelections.SELECT_A_MONSTER_ATTACK_NEGATING_TRAP_CARD_IN_BOARD)) {
            ai.setAiSpellTrapSelections(AISpellTrapSelections.NOTHING);
            return aiCardFinder.findTrapCardInBoardToActivateWithThisMonsterAttackingTrapCardEffect(ai, MonsterAttackingTrapCardEffect.NEGATE_OPPONENT_ATTACK);
        }
        return "next phase";
    }

    protected String getCommandForGivingFurtherInputToActivateSpellTrapInChainInBattlePhase(AI ai) {
        if (ai.getAiFurtherActivationInput().equals(AIFurtherActivationInput.SELECT_A_MONSTER_SETTING_ATTACK_TO_0_EFFECT_FROM_OWN_GRAVEYARD)) {
            ai.setAiFurtherActivationInput(AIFurtherActivationInput.NOTHING);
            return aiCardFinder.findMonsterInOwnGraveyardToSpecialSummonWithThisBeingAttackedEffect(ai, BeingAttackedEffect.SET_ATTACKING_MONSTER_ATK_TO_0_ONCE_PER_TURN);
        } else if (ai.getAiFurtherActivationInput().equals(AIFurtherActivationInput.SELECT_A_SELF_DESTRUCTION_MONSTER_FROM_OWN_GRAVEYARD)) {
            ai.setAiFurtherActivationInput(AIFurtherActivationInput.NOTHING);
            return aiCardFinder.findMonsterInOwnGraveyardToSpecialSummonWithThisBeingAttackedEffect(ai, BeingAttackedEffect.IF_DESTROYED_AND_SENT_TO_GRAVEYARD_SEND_ATTACKING_MONSTER_TO_GRAVEYARD);
        } else if (ai.getAiFurtherActivationInput().equals(AIFurtherActivationInput.SELECT_A_MONSTER_WITH_HIGH_ATTACK_FROM_OWN_GRAVEYARD)) {
            ai.setAiFurtherActivationInput(AIFurtherActivationInput.NOTHING);
            return aiCardFinder.findMonsterWithHighestAttackInOwnGraveyardToSpecialSummon(ai);
        } else if (ai.getAiFurtherActivationInput().equals(AIFurtherActivationInput.NOTHING)) {
            return ai.getAiMainPhaseMind().getCommandForGivingFurtherInputForActivatingSpellOrTrap(ai);
        }
        return "next phase";
    }

    protected String getCommandForChoosingToActivateMonsterEffectInChainInBattlePhase(AI ai) {
        return "yes";
    }

    protected String getCommandForChoosingMonsterToSpecialSummonInChainInBattlePhase(AI ai) {
        ArrayList<Card> opponentMonsterCards = ai.getAiBoardUnderstander().getOpponentMonsterCards();
        boolean doesOpponentHaveHighAttackingMonsters = false;
        for (int i = 0; i < opponentMonsterCards.size(); i++) {
            if (Card.isCardAMonster(opponentMonsterCards.get(i))) {
                MonsterCard monsterCard = (MonsterCard) opponentMonsterCards.get(i);
                if (!monsterCard.isHasCardAlreadyAttacked() && MonsterCard.giveATKDEFConsideringEffects("attack", new CardLocation(ai.getAiBoardUnderstander().opponentMonsterCardsRowOfCardLocation, i + 1), 0) >= 2400) {
                    doesOpponentHaveHighAttackingMonsters = true;
                }
            }
        }
        if (doesOpponentHaveHighAttackingMonsters) {
            return aiCardFinder.findNormalMonsterCyberseCardInDeckGraveyardOrHandTo(ai, false);
        } else {
            return aiCardFinder.findNormalMonsterCyberseCardInDeckGraveyardOrHandTo(ai, true);
        }
    }

    protected String getCommandForChoosingAttackPositionOrDefensePositionInChainInBattlePhase(AI ai) {
        if (ai.getAiFurtherActivationInput().equals(AIFurtherActivationInput.ATTACKING)) {
            ai.setAiFurtherActivationInput(AIFurtherActivationInput.NOTHING);
            return "attacking";
        } else if (ai.getAiFurtherActivationInput().equals(AIFurtherActivationInput.DEFENSIVE)) {
            ai.setAiFurtherActivationInput(AIFurtherActivationInput.NOTHING);
            return "defensive";
        }
        return "next phase";
    }

    protected String getCommandForChoosingMonsterToDestroyInChainInBattlePhase(AI ai) {
        ArrayList<Card> opponentMonsterCards = ai.getAiBoardUnderstander().getOpponentMonsterCards();
        boolean doesOpponentHaveHighAttackingMonsters = false;
        for (int i = 0; i < opponentMonsterCards.size(); i++) {
            if (Card.isCardAMonster(opponentMonsterCards.get(i))) {
                MonsterCard monsterCard = (MonsterCard) opponentMonsterCards.get(i);
                if (!monsterCard.isHasCardAlreadyAttacked() &&
                    MonsterCard.giveATKDEFConsideringEffects("attack", new CardLocation(ai.getAiBoardUnderstander().opponentMonsterCardsRowOfCardLocation, i + 1), 0) >= 2400) {
                    return "select --opponent --monster " + Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(i + 1, false);
                }
            }
        }
        for (int i = 0; i < opponentMonsterCards.size(); i++) {
            if (Card.isCardAMonster(opponentMonsterCards.get(i))) {
                MonsterCard monsterCard = (MonsterCard) opponentMonsterCards.get(i);
                ArrayList<BeingAttackedEffect> beingAttackedEffects = monsterCard.getBeingAttackedEffects();
                if (beingAttackedEffects.contains(BeingAttackedEffect.CANNOT_BE_DESTROYED_BY_BATTLE)) {
                    return "select --opponent --monster " + Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(i + 1, false);
                }
            }
        }
        for (int i = 0; i < opponentMonsterCards.size(); i++) {
            if (Card.isCardAMonster(opponentMonsterCards.get(i))) {
                MonsterCard monsterCard = (MonsterCard) opponentMonsterCards.get(i);
                ArrayList<BeingAttackedEffect> beingAttackedEffects = monsterCard.getBeingAttackedEffects();
                if (beingAttackedEffects.contains(BeingAttackedEffect.IF_DESTROYED_AND_SENT_TO_GRAVEYARD_SEND_ATTACKING_MONSTER_TO_GRAVEYARD)) {
                    return "select --opponent --monster " + Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(i + 1, false);
                }
            }
        }
        ArrayList<Action> uninterruptedAction = GameManager.getUninterruptedActionsByIndex(0);
        Action attackingAction = uninterruptedAction.get(uninterruptedAction.size() - 1);
        CardLocation attackingMonsterCardLocation = attackingAction.getFinalMainCardLocation();
        return "select --opponent --monster " + Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(attackingMonsterCardLocation.getIndex(), false);
    }


    protected boolean doMonstersInThisSideDominateTheOtherSideExtendable(ArrayList<CardLocation> monsterCardsFirstSide
        , ArrayList<CardLocation> monsterCardsSecondSide, int index, boolean considerEquipSpellField) {
        //return false;

        //System.out.println("MNMNMNM");
        if (monsterCardsFirstSide.size() == 0) {
            return false;
        }
        for (int i = 0; i < monsterCardsSecondSide.size(); i++) {
            boolean answer;
            if (considerEquipSpellField) {
                answer = doesMonsterDominateOtherMonsterConsideringEquipAndFieldSpellCards(monsterCardsFirstSide.get(0), monsterCardsSecondSide.get(i), index);
            } else {
                answer = doesMonsterDominateOtherMonsterAlone(monsterCardsFirstSide.get(0), monsterCardsSecondSide.get(i), index);
            }
            if (answer) {
                ArrayList<CardLocation> newMonsterCardsFirstSide = new ArrayList<>();
                for (int j = 1; j < monsterCardsFirstSide.size(); j++) {
                    newMonsterCardsFirstSide.add(monsterCardsFirstSide.get(j));
                }
                ArrayList<CardLocation> newMonsterCardsForSecondSide = new ArrayList<>();
                for (int j = 0; j < monsterCardsSecondSide.size(); j++) {
                    if (j != i) {
                        newMonsterCardsForSecondSide.add(monsterCardsSecondSide.get(j));
                    }
                }
                if (considerEquipSpellField) {
                    return doMonstersInThisSideDominateTheOtherSideExtendable(newMonsterCardsFirstSide, newMonsterCardsForSecondSide, index, true);
                }
                return doMonstersInThisSideDominateTheOtherSideExtendable(newMonsterCardsFirstSide, newMonsterCardsForSecondSide, index, false);
            }
        }
        if (monsterCardsSecondSide.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    protected boolean doesMonsterDominateOtherMonsterConsideringEquipAndFieldSpellCards(CardLocation firstMonsterLocation, CardLocation secondMonsterLocation, int index) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        MonsterCard secondMonster = (MonsterCard) duelBoard.getCardByCardLocation(secondMonsterLocation);
        ArrayList<BeingAttackedEffect> beingAttackedEffects = secondMonster.getBeingAttackedEffects();
        ArrayList<FlipEffect> flipEffects = secondMonster.getFlipEffects();
        if (beingAttackedEffects.contains(BeingAttackedEffect.CANNOT_BE_DESTROYED_BY_BATTLE)) {
            return false;
        }
        if ((beingAttackedEffects.contains(BeingAttackedEffect.IF_DESTROYED_AND_SENT_TO_GRAVEYARD_SEND_ATTACKING_MONSTER_TO_GRAVEYARD)
            || flipEffects.contains(FlipEffect.DESTROY_1_MONSTER_ON_THE_FIELD) && secondMonster.getCardPosition().equals(CardPosition.FACE_DOWN_MONSTER_SET_POSITION)) &&
            MonsterCard.giveATKDEFConsideringEffects("attack", firstMonsterLocation, index) > MonsterCard.giveATKDEFConsideringEffects("attack", secondMonsterLocation, index)
            && MonsterCard.giveATKDEFConsideringEffects("attack", firstMonsterLocation, index) <= 2000) {
            return true;
        }
        if ((beingAttackedEffects.contains(BeingAttackedEffect.IF_DESTROYED_AND_SENT_TO_GRAVEYARD_SEND_ATTACKING_MONSTER_TO_GRAVEYARD)
            || flipEffects.contains(FlipEffect.DESTROY_1_MONSTER_ON_THE_FIELD) && secondMonster.getCardPosition().equals(CardPosition.FACE_DOWN_MONSTER_SET_POSITION)) &&
            MonsterCard.giveATKDEFConsideringEffects("attack", firstMonsterLocation, index) > MonsterCard.giveATKDEFConsideringEffects("defense", secondMonsterLocation, index)
            && MonsterCard.giveATKDEFConsideringEffects("attack", firstMonsterLocation, index) > 2000) {
            return false;
        }
        if (secondMonster.getCardPosition().equals(CardPosition.FACE_UP_ATTACK_POSITION)
            && MonsterCard.giveATKDEFConsideringEffects("attack", firstMonsterLocation, index) >
            MonsterCard.giveATKDEFConsideringEffects("attack", secondMonsterLocation, index)) {
            return true;
        }
        if ((secondMonster.getCardPosition().equals(CardPosition.FACE_UP_DEFENSE_POSITION) || secondMonster.getCardPosition().equals(CardPosition.FACE_DOWN_MONSTER_SET_POSITION))
            && MonsterCard.giveATKDEFConsideringEffects("attack", firstMonsterLocation, index) >
            MonsterCard.giveATKDEFConsideringEffects("defense", secondMonsterLocation, index)) {
            return true;
        }
        return false;
    }

    protected boolean doesMonsterDominateOtherMonsterAlone(CardLocation firstMonsterLocation, CardLocation secondMonsterLocation, int index) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        MonsterCard secondMonster = (MonsterCard) duelBoard.getCardByCardLocation(secondMonsterLocation);
        ArrayList<BeingAttackedEffect> beingAttackedEffects = secondMonster.getBeingAttackedEffects();
        ArrayList<FlipEffect> flipEffects = secondMonster.getFlipEffects();
        MonsterCard firstMonster = (MonsterCard) duelBoard.getCardByCardLocation(firstMonsterLocation);
        return doesMonsterDominateOtherMonsterAloneOnlyMonsterCardInput(firstMonster, secondMonster);
    }


    public boolean doesMonsterDominateOtherMonsterAloneOnlyMonsterCardInput(MonsterCard firstMonster, MonsterCard secondMonster) {
        ArrayList<BeingAttackedEffect> beingAttackedEffects = secondMonster.getBeingAttackedEffects();
        if (beingAttackedEffects.contains(BeingAttackedEffect.CANNOT_BE_DESTROYED_BY_BATTLE)) {
            return false;
        }
        ArrayList<FlipEffect> flipEffects = secondMonster.getFlipEffects();
        if ((beingAttackedEffects.contains(BeingAttackedEffect.IF_DESTROYED_AND_SENT_TO_GRAVEYARD_SEND_ATTACKING_MONSTER_TO_GRAVEYARD)
            || flipEffects.contains(FlipEffect.DESTROY_1_MONSTER_ON_THE_FIELD) && secondMonster.getCardPosition().equals(CardPosition.FACE_DOWN_MONSTER_SET_POSITION)) &&
            firstMonster.getAttackPower() > secondMonster.getAttackPower() && firstMonster.getAttackPower() <= 2000) {
            return true;
        }
        if ((beingAttackedEffects.contains(BeingAttackedEffect.IF_DESTROYED_AND_SENT_TO_GRAVEYARD_SEND_ATTACKING_MONSTER_TO_GRAVEYARD)
            || flipEffects.contains(FlipEffect.DESTROY_1_MONSTER_ON_THE_FIELD) && secondMonster.getCardPosition().equals(CardPosition.FACE_DOWN_MONSTER_SET_POSITION)) &&
            firstMonster.getAttackPower() > secondMonster.getAttackPower() && firstMonster.getAttackPower() > 2000) {
            return false;
        }
        if (secondMonster.getCardPosition().equals(CardPosition.FACE_UP_ATTACK_POSITION) && firstMonster.getAttackPower() > secondMonster.getAttackPower()) {
            return true;
        }
        if ((secondMonster.getCardPosition().equals(CardPosition.FACE_UP_DEFENSE_POSITION) || secondMonster.getCardPosition().equals(CardPosition.FACE_DOWN_MONSTER_SET_POSITION))
            && firstMonster.getAttackPower() > secondMonster.getDefensePower()) {
            return true;
        }
        return false;
    }


    public boolean doMonstersInThisSideDominateTheOtherSideAloneOnlyMonsterCardInput(ArrayList<MonsterCard> monsterCardsFirstSide
        , ArrayList<MonsterCard> monsterCardsSecondSide) {
        if (monsterCardsFirstSide.size() == 0) {
            return false;
        }
        for (int i = 0; i < monsterCardsSecondSide.size(); i++) {
            boolean answer = doesMonsterDominateOtherMonsterAloneOnlyMonsterCardInput(monsterCardsFirstSide.get(0), monsterCardsSecondSide.get(i));
            if (answer) {
                ArrayList<MonsterCard> newMonsterCardsFirstSide = new ArrayList<>();
                for (int j = 1; j < monsterCardsFirstSide.size(); j++) {
                    newMonsterCardsFirstSide.add(monsterCardsFirstSide.get(j));
                }
                ArrayList<MonsterCard> newMonsterCardsForSecondSide = new ArrayList<>();
                for (int j = 0; j < monsterCardsSecondSide.size(); j++) {
                    if (j != i) {
                        newMonsterCardsForSecondSide.add(monsterCardsSecondSide.get(j));
                    }
                }
                return doMonstersInThisSideDominateTheOtherSideAloneOnlyMonsterCardInput(newMonsterCardsFirstSide, newMonsterCardsForSecondSide);
            }
        }
        if (monsterCardsSecondSide.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public String getCommandForChoosingToActivateSpellTrapForThirdTimeInChain(AI ai) {
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(0);
        Action aiSummoningAction = uninterruptedActions.get(uninterruptedActions.size() - 2);
        CardLocation mainCardLocation = aiSummoningAction.getFinalMainCardLocation();
        MonsterCard attackingMonsterCard = (MonsterCard) GameManager.getDuelBoardByIndex(0).getCardByCardLocation(mainCardLocation);
        Action opponentTrapActivatingAction = uninterruptedActions.get(uninterruptedActions.size() - 1);
        CardLocation opponentTrapLocation = opponentTrapActivatingAction.getFinalMainCardLocation();
        TrapCard trapCard = (TrapCard) GameManager.getDuelBoardByIndex(0).getCardByCardLocation(opponentTrapLocation);
        ArrayList<MonsterAttackingTrapCardEffect> monsterAttackingTrapCardEffects = trapCard.getMonsterAttackingTrapCardEffects();
        if (monsterAttackingTrapCardEffects.contains(MonsterAttackingTrapCardEffect.INFLICT_DAMAGE_TO_OPPONENT_EQUAL_TO_MONSTERS_ATK)) {
            if (MonsterCard.giveATKDEFConsideringEffects("attack", mainCardLocation, 0) >= 2200) {
                return "yes";
            } else {
                return "no";
            }
        } else if (monsterAttackingTrapCardEffects.contains(MonsterAttackingTrapCardEffect.DESTROY_ALL_OPPONENT_ATTACK_POSITION_MONSTERS)) {
            if (ai.getAiBoardUnderstander().getAIMonsterSpellBoardAnalysis().getIndexesOfMonstersWorthyOfKillingAndFaceUpAttackPosition().size() +
                ai.getAiBoardUnderstander().getAiMonsterSpellBoardAnalysis().getIndexesOfMonstersGoodToBeKilledAndFaceUpAttackPosition().size() >= 2) {
                return "yes";
            }
        } else if (monsterAttackingTrapCardEffects.contains(MonsterAttackingTrapCardEffect.SPECIAL_SUMMON_ONE_MONSTER_IN_YOUR_GRAVEYARD_IN_FACE_UP_ATTACK_POSITION)) {
            if (aiKeyVariablesUpdater.isDoesOpponentHaveUndieableMonstersInOpponentGraveyard() || aiKeyVariablesUpdater.isDoesOpponentHaveMonstersWithHighAttackInOpponentGraveyard()
                || aiKeyVariablesUpdater.isDoesOpponentHaveMonstersWithSelfDestructionEffectInOpponentGraveyard() || aiKeyVariablesUpdater.isDoesOpponentHaveMonstersWithEffectSettingToZeroInOpponentGraveyard()) {
                return "yes";
            }
        }
        return "no";
    }

    public AIKeyVariablesUpdater getAiKeyVariablesUpdater() {
        return aiKeyVariablesUpdater;
    }
}
