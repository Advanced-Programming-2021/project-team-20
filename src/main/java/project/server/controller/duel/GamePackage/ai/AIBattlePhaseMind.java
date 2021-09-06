package project.server.controller.duel.GamePackage.ai;

import project.model.MonsterEffectEnums.BeingAttackedEffect;
import project.model.MonsterEffectEnums.FlipEffect;
import project.model.TrapEffectEnums.MonsterAttackingTrapCardEffect;
import project.server.controller.duel.GamePackage.Action;
import project.model.ActionType;
import project.server.controller.duel.GamePackage.DuelBoard;
import project.server.controller.duel.PreliminaryPackage.GameManager;
import project.model.Utility.Utility;
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
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(ai.getToken());
        Action attackAction = uninterruptedActions.get(uninterruptedActions.size() - 1);
        CardLocation attackingMonsterLocation = attackAction.getFinalMainCardLocation();
        if (attackAction.getActionType().equals(ActionType.ALLY_MONSTER_ATTACKING_OPPONENT_MONSTER) || attackAction.getActionType().equals(ActionType.OPPONENT_MONSTER_ATTACKING_ALLY_MONSTER)) {
            CardLocation beingAttackedLocation = attackAction.getTargetingCards().get(attackAction.getTargetingCards().size() - 1);
            if (doesMonsterDominateOtherMonsterConsideringEquipAndFieldSpellCards(beingAttackedLocation, attackingMonsterLocation, ai.getToken())) {
                return "no";
            }
        }
        int aiTurn = GameManager.getDuelControllerByIndex(ai.getToken()).getAiTurn();
        int enemyLifePoints = GameManager.getDuelControllerByIndex(ai.getToken()).getLifePoints().get(2 - aiTurn);
        if ((enemyLifePoints < MonsterCard.giveATKDEFConsideringEffects("attack", attackingMonsterLocation, ai.getToken())
            || 1600 <= MonsterCard.giveATKDEFConsideringEffects("attack", attackingMonsterLocation, ai.getToken())) &&
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
                if (!monsterCard.isHasCardAlreadyAttacked() && MonsterCard.giveATKDEFConsideringEffects("attack", new CardLocation(ai.getAiBoardUnderstander().opponentMonsterCardsRowOfCardLocation, i + 1), ai.getToken()) >= 2400) {
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
                    MonsterCard.giveATKDEFConsideringEffects("attack", new CardLocation(ai.getAiBoardUnderstander().opponentMonsterCardsRowOfCardLocation, i + 1), ai.getToken()) >= 2400) {
                    return "select --opponent --monster " + Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(i + 1, false, ai.getToken());
                }
            }
        }
        for (int i = 0; i < opponentMonsterCards.size(); i++) {
            if (Card.isCardAMonster(opponentMonsterCards.get(i))) {
                MonsterCard monsterCard = (MonsterCard) opponentMonsterCards.get(i);
                ArrayList<BeingAttackedEffect> beingAttackedEffects = monsterCard.getBeingAttackedEffects();
                if (beingAttackedEffects.contains(BeingAttackedEffect.CANNOT_BE_DESTROYED_BY_BATTLE)) {
                    return "select --opponent --monster " + Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(i + 1, false, ai.getToken());
                }
            }
        }
        for (int i = 0; i < opponentMonsterCards.size(); i++) {
            if (Card.isCardAMonster(opponentMonsterCards.get(i))) {
                MonsterCard monsterCard = (MonsterCard) opponentMonsterCards.get(i);
                ArrayList<BeingAttackedEffect> beingAttackedEffects = monsterCard.getBeingAttackedEffects();
                if (beingAttackedEffects.contains(BeingAttackedEffect.IF_DESTROYED_AND_SENT_TO_GRAVEYARD_SEND_ATTACKING_MONSTER_TO_GRAVEYARD)) {
                    return "select --opponent --monster " + Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(i + 1, false, ai.getToken());
                }
            }
        }
        ArrayList<Action> uninterruptedAction = GameManager.getUninterruptedActionsByIndex(ai.getToken());
        Action attackingAction = uninterruptedAction.get(uninterruptedAction.size() - 1);
        CardLocation attackingMonsterCardLocation = attackingAction.getFinalMainCardLocation();
        return "select --opponent --monster " + Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(attackingMonsterCardLocation.getIndex(), false, ai.getToken());
    }


    protected boolean doMonstersInThisSideDominateTheOtherSideExtendable(ArrayList<CardLocation> monsterCardsFirstSide
        , ArrayList<CardLocation> monsterCardsSecondSide, String token, boolean considerEquipSpellField) {
        //return false;

        //System.out.println("MNMNMNM");
        if (monsterCardsFirstSide.size() == 0) {
            return false;
        }
        for (int i = 0; i < monsterCardsSecondSide.size(); i++) {
            boolean answer;
            if (considerEquipSpellField) {
                answer = doesMonsterDominateOtherMonsterConsideringEquipAndFieldSpellCards(monsterCardsFirstSide.get(0), monsterCardsSecondSide.get(i), token);
            } else {
                answer = doesMonsterDominateOtherMonsterAlone(monsterCardsFirstSide.get(0), monsterCardsSecondSide.get(i), token);
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
                    return doMonstersInThisSideDominateTheOtherSideExtendable(newMonsterCardsFirstSide, newMonsterCardsForSecondSide, token, true);
                }
                return doMonstersInThisSideDominateTheOtherSideExtendable(newMonsterCardsFirstSide, newMonsterCardsForSecondSide, token, false);
            }
        }
        if (monsterCardsSecondSide.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    protected boolean doesMonsterDominateOtherMonsterConsideringEquipAndFieldSpellCards(CardLocation firstMonsterLocation, CardLocation secondMonsterLocation, String token) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        MonsterCard secondMonster = (MonsterCard) duelBoard.getCardByCardLocation(secondMonsterLocation);
        ArrayList<BeingAttackedEffect> beingAttackedEffects = secondMonster.getBeingAttackedEffects();
        ArrayList<FlipEffect> flipEffects = secondMonster.getFlipEffects();
        if (beingAttackedEffects.contains(BeingAttackedEffect.CANNOT_BE_DESTROYED_BY_BATTLE)) {
            return false;
        }
        if ((beingAttackedEffects.contains(BeingAttackedEffect.IF_DESTROYED_AND_SENT_TO_GRAVEYARD_SEND_ATTACKING_MONSTER_TO_GRAVEYARD)
            || flipEffects.contains(FlipEffect.DESTROY_1_MONSTER_ON_THE_FIELD) && secondMonster.getCardPosition().equals(CardPosition.FACE_DOWN_MONSTER_SET_POSITION)) &&
            MonsterCard.giveATKDEFConsideringEffects("attack", firstMonsterLocation, token) > MonsterCard.giveATKDEFConsideringEffects("attack", secondMonsterLocation, token)
            && MonsterCard.giveATKDEFConsideringEffects("attack", firstMonsterLocation, token) <= 2000) {
            return true;
        }
        if ((beingAttackedEffects.contains(BeingAttackedEffect.IF_DESTROYED_AND_SENT_TO_GRAVEYARD_SEND_ATTACKING_MONSTER_TO_GRAVEYARD)
            || flipEffects.contains(FlipEffect.DESTROY_1_MONSTER_ON_THE_FIELD) && secondMonster.getCardPosition().equals(CardPosition.FACE_DOWN_MONSTER_SET_POSITION)) &&
            MonsterCard.giveATKDEFConsideringEffects("attack", firstMonsterLocation, token) > MonsterCard.giveATKDEFConsideringEffects("defense", secondMonsterLocation, token)
            && MonsterCard.giveATKDEFConsideringEffects("attack", firstMonsterLocation, token) > 2000) {
            return false;
        }
        if (secondMonster.getCardPosition().equals(CardPosition.FACE_UP_ATTACK_POSITION)
            && MonsterCard.giveATKDEFConsideringEffects("attack", firstMonsterLocation, token) >
            MonsterCard.giveATKDEFConsideringEffects("attack", secondMonsterLocation, token)) {
            return true;
        }
        if ((secondMonster.getCardPosition().equals(CardPosition.FACE_UP_DEFENSE_POSITION) || secondMonster.getCardPosition().equals(CardPosition.FACE_DOWN_MONSTER_SET_POSITION))
            && MonsterCard.giveATKDEFConsideringEffects("attack", firstMonsterLocation, token) >
            MonsterCard.giveATKDEFConsideringEffects("defense", secondMonsterLocation, token)) {
            return true;
        }
        return false;
    }

    protected boolean doesMonsterDominateOtherMonsterAlone(CardLocation firstMonsterLocation, CardLocation secondMonsterLocation, String token) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
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
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(ai.getToken());
        Action aiSummoningAction = uninterruptedActions.get(uninterruptedActions.size() - 2);
        CardLocation mainCardLocation = aiSummoningAction.getFinalMainCardLocation();
        MonsterCard attackingMonsterCard = (MonsterCard) GameManager.getDuelBoardByIndex(ai.getToken()).getCardByCardLocation(mainCardLocation);
        Action opponentTrapActivatingAction = uninterruptedActions.get(uninterruptedActions.size() - 1);
        CardLocation opponentTrapLocation = opponentTrapActivatingAction.getFinalMainCardLocation();
        TrapCard trapCard = (TrapCard) GameManager.getDuelBoardByIndex(ai.getToken()).getCardByCardLocation(opponentTrapLocation);
        ArrayList<MonsterAttackingTrapCardEffect> monsterAttackingTrapCardEffects = trapCard.getMonsterAttackingTrapCardEffects();
        if (monsterAttackingTrapCardEffects.contains(MonsterAttackingTrapCardEffect.INFLICT_DAMAGE_TO_OPPONENT_EQUAL_TO_MONSTERS_ATK)) {
            if (MonsterCard.giveATKDEFConsideringEffects("attack", mainCardLocation, ai.getToken()) >= 2200) {
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
