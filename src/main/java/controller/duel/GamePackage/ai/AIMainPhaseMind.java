package controller.duel.GamePackage.ai;

import controller.duel.CardEffects.MonsterEffectEnums.BeingAttackedEffect;
import controller.duel.CardEffects.SpellEffectEnums.ContinuousSpellCardEffect;
import controller.duel.CardEffects.SpellEffectEnums.NormalSpellCardEffect;
import controller.duel.CardEffects.TrapEffectEnums.NormalSummonTrapCardEffect;
import controller.duel.CardEffects.TrapEffectEnums.NormalTrapCardEffect;
import controller.duel.GamePackage.*;
import controller.duel.PreliminaryPackage.GameManager;
import controller.duel.Utility.Utility;
import model.AIFurtherActivationInput;
import model.cardData.General.Card;
import model.cardData.General.CardLocation;
import model.cardData.General.RowOfCardLocation;
import model.cardData.MonsterCardData.MonsterCard;
import model.cardData.SpellCardData.SpellCard;
import model.cardData.TrapCardData.TrapCard;

import java.util.ArrayList;

public class AIMainPhaseMind {
    //private static int maximumNumberOfNormalSummonOrSet = 0;
    private AICardFinder aiCardFinder;
    private AIKeyVariablesUpdater aiKeyVariablesUpdater;
    private int numberOfTributesNeeded;
    private int numberOfTributesDone;
    private int indexOfPreviouslyChosenSpellTrapForKilling;

    /*
    protected AIBoardAnalysis opponentMonsterSpellBoardAnalysis;
    protected AIBoardAnalysis opponentHandBoardAnalysis;
    protected AIBoardAnalysis aiMonsterSpellBoardAnalysis;
    protected AIBoardAnalysis aiHandBoardAnalysis;
    protected ArrayList<Card> opponentMonsterCards;
    protected ArrayList<Card> opponentCardsInHand;
    protected ArrayList<Card> opponentSpellTrapCards;
    protected ArrayList<Card> opponentSpellFieldCard;
    RowOfCardLocation opponentMonsterCardsRowOfCardLocation = null;
    RowOfCardLocation opponentSpellCardsRowOfCardLocation = null;
    RowOfCardLocation opponentSpellFieldCardRowOfCardLocation = null;
    RowOfCardLocation opponentGraveyardCardsRowOfCardLocation = null;
    RowOfCardLocation opponentHandCardsRowOfCardLocation = null;
    protected ArrayList<Card> aiMonsterCards;
    protected ArrayList<Card> aiCardsInHand;
    protected ArrayList<Card> aiSpellTrapCards;
    protected ArrayList<Card> aiSpellFieldCard;
    RowOfCardLocation aiMonsterCardsRowOfCardLocation = null;
    RowOfCardLocation aiSpellCardsRowOfCardLocation = null;
    RowOfCardLocation aiSpellFieldCardRowOfCardLocation = null;
    RowOfCardLocation aiGraveyardCardsRowOfCardLocation = null;
    RowOfCardLocation aiHandCardsRowOfCardLocation = null;


     */
    public AIMainPhaseMind() {
        aiCardFinder = new AICardFinder();
        aiKeyVariablesUpdater = new AIKeyVariablesUpdater();
    }

    //AI MAIN PHASE 1 OR 2 MIND
    protected String getCommandForChoosingMonstersForTributesInNormalSummoning(AI ai) {
        ArrayList<Integer> indexesOfMonstersWorthyOfBeingTributes = ai.getAiBoardUnderstander().getAIMonsterSpellBoardAnalysis().getIndexesOfMonstersWorthyOfBeingTributes();
        numberOfTributesNeeded -= 1;
        //not a pretty elegent method for choosing tributes
        int index = indexesOfMonstersWorthyOfBeingTributes.get(indexesOfMonstersWorthyOfBeingTributes.size() - 1);
        return "select --monster " + Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(index + 1, true);
    }

    protected String getCommandForDiscardingCardsInSpecialSummoning(AI ai) {
        return "select --hand 1";
    }

    protected String getCommandForChoosingMonstersForTributesInSpecialSummoning(AI ai) {
        return getCommandForChoosingMonstersForTributesInNormalSummoning(ai);
    }

    protected String getCommandForChoosingMonstersForTributesInTributeSummoning(AI ai) {
        return getCommandForChoosingMonstersForTributesInNormalSummoning(ai);
    }

    protected String getCommandForGivingFurtherInputForActivatingSpellOrTrap(AI ai) {
        if (ai.getAiFurtherActivationInput().equals(AIFurtherActivationInput.SELECT_A_CARD_FROM_YOUR_HAND_TO_DISCARD) ||
            ai.getAiFurtherActivationInput().equals(AIFurtherActivationInput.SELECT_A_CARD_FROM_YOUR_HAND_TO_DISCARD_IN_CHAIN)) {
            ArrayList<Card> cardsInAIHand = ai.getAiBoardUnderstander().getAiCardsInHand();
            String output = "select --hand 1";
            /*
            for (int i = 0; i < cardsInAIHand.size(); i++) {
                if (Card.isCardAMonster(cardsInAIHand.get(i))) {
                    if (((MonsterCard) cardsInAIHand.get(i)).getAttackPower() <= 2000) {
                        output = "select --hand " + (i + 1);
                    }
                }
            }

             */
            if (ai.getAiFurtherActivationInput().equals(AIFurtherActivationInput.SELECT_A_CARD_FROM_YOUR_HAND_TO_DISCARD)) {
                ai.setAiFurtherActivationInput(AIFurtherActivationInput.SELECT_2_OF_OPPONENT_SPELL_TRAP_CARDS_WORTHY_OF_KILLING);
            } else {
                ai.setAiFurtherActivationInput(AIFurtherActivationInput.SELECT_2_OF_OPPONENT_SPELL_TRAP_CARDS_WORTHY_OF_KILLING_IN_CHAIN);
            }
            return output;
        } else if (ai.getAiFurtherActivationInput().equals(AIFurtherActivationInput.SELECT_2_OF_OPPONENT_SPELL_TRAP_CARDS_WORTHY_OF_KILLING)) {
            ArrayList<Integer> indexesOfSpellTrapsWorthyOfKilling = ai.getAiBoardUnderstander().getOpponentMonsterSpellBoardAnalysis().getIndexesOfSpellTrapsWorthyOfKilling();
            int index = indexesOfSpellTrapsWorthyOfKilling.get(indexesOfSpellTrapsWorthyOfKilling.size() - 1);
            //indexesOfSpellTrapsWorthyOfKilling.remove(indexesOfSpellTrapsWorthyOfKilling.size() - 1);
            indexOfPreviouslyChosenSpellTrapForKilling = index;
            ai.setAiFurtherActivationInput(AIFurtherActivationInput.SELECT_OPPONENT_SPELL_TRAP_CARDS_WORTHY_OF_KILLING);
            return "select --opponent --spell " + Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(index + 1, false);
        } else if (ai.getAiFurtherActivationInput().equals(AIFurtherActivationInput.SELECT_OPPONENT_SPELL_TRAP_CARDS_WORTHY_OF_KILLING)) {
            ArrayList<Integer> indexesOfSpellTrapsWorthyOfKilling =
                ai.getAiBoardUnderstander().getOpponentMonsterSpellBoardAnalysis().getIndexesOfSpellTrapsWorthyOfKilling();
            ArrayList<Integer> indexesOfSpellTrapsGoodToBeKilled =
                ai.getAiBoardUnderstander().getOpponentMonsterSpellBoardAnalysis().getIndexesOfSpellTrapsGoodToBeKilled();
            int index;
            if (indexesOfSpellTrapsWorthyOfKilling.size() > 0) {
                for (Integer integer : indexesOfSpellTrapsWorthyOfKilling) {
                    index = integer;
                    if (index != indexOfPreviouslyChosenSpellTrapForKilling) {
                        ai.setAiFurtherActivationInput(AIFurtherActivationInput.NOTHING);
                        indexOfPreviouslyChosenSpellTrapForKilling = -1;
                        return "select --opponent --spell " + Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(index + 1, false);
                    }
                }
            } else {
                //index = indexesOfSpellTrapsGoodToBeKilled.get(indexesOfSpellTrapsGoodToBeKilled.size() - 1);
                for (Integer integer : indexesOfSpellTrapsGoodToBeKilled) {
                    index = integer;
                    if (index != indexOfPreviouslyChosenSpellTrapForKilling) {
                        ai.setAiFurtherActivationInput(AIFurtherActivationInput.NOTHING);
                        indexOfPreviouslyChosenSpellTrapForKilling = -1;
                        return "select --opponent --spell " + Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(index + 1, false);
                    }
                }
            }
            for (int i = 0; i < 5; i++) {
                if (i != indexOfPreviouslyChosenSpellTrapForKilling) {
                    ai.setAiFurtherActivationInput(AIFurtherActivationInput.NOTHING);
                    indexOfPreviouslyChosenSpellTrapForKilling = -1;
                    return "select --opponent --spell " + Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(i + 1, false);
                }
            }
        } else if (ai.getAiFurtherActivationInput().equals(AIFurtherActivationInput.SELECT_2_OF_OPPONENT_SPELL_TRAP_CARDS_WORTHY_OF_KILLING_IN_CHAIN)) {
            String output = aiCardFinder.findActiveMainCardInPreviousUninterruptedAction(ai);
            indexOfPreviouslyChosenSpellTrapForKilling = output.charAt(output.length() - 1);
            ai.setAiFurtherActivationInput(AIFurtherActivationInput.SELECT_OPPONENT_SPELL_TRAP_CARDS_WORTHY_OF_KILLING);
            return output;
        } else if (ai.getAiFurtherActivationInput().equals(AIFurtherActivationInput.SELECT_OPPONENT_SPELL_TRAP_CARDS_WORTHY_OF_KILLING_IN_CHAIN)) {
            String output = aiCardFinder.findActiveMainCardInPreviousUninterruptedAction(ai);
            int index = output.charAt(output.length() - 1);
            if (index != indexOfPreviouslyChosenSpellTrapForKilling) {
                ai.setAiFurtherActivationInput(AIFurtherActivationInput.NOTHING);
                return output;
            } else {
                ai.setAiFurtherActivationInput(AIFurtherActivationInput.SELECT_OPPONENT_SPELL_TRAP_CARDS_WORTHY_OF_KILLING);
                return getCommandForGivingFurtherInputForActivatingSpellOrTrap(ai);
            }
        }
        return "next phase";
    }

    protected String getCommandForGivingACardName(AI ai) {
        ArrayList<Card> opponentCardsInHand = ai.getAiBoardUnderstander().opponentCardsInHand;
        int maximumAttack = 0;
        if (ai.getAiBoardUnderstander().getOpponentHandBoardAnalysis().getIndexesOfMonstersWorthyOfKilling().size() >= 1) {
            for (int i = 0; i < opponentCardsInHand.size(); i++) {
                if (Card.isCardAMonster(opponentCardsInHand.get(i))) {
                    if (((MonsterCard) opponentCardsInHand.get(i)).getAttackPower() > maximumAttack) {
                        maximumAttack = ((MonsterCard) opponentCardsInHand.get(i)).getAttackPower();
                    }
                }
            }
            for (int i = 0; i < opponentCardsInHand.size(); i++) {
                if (Card.isCardAMonster(opponentCardsInHand.get(i))) {
                    if (((MonsterCard) opponentCardsInHand.get(i)).getAttackPower() == maximumAttack) {
                        return opponentCardsInHand.get(i).getCardName();
                    }
                }
            }
        } else if (ai.getAiBoardUnderstander().getOpponentHandBoardAnalysis().getIndexesOfSpellTrapsWorthyOfKilling().size() >= 1) {
            ArrayList<Integer> indexesOfTargetingCards = ai.getAiBoardUnderstander().getOpponentHandBoardAnalysis().getIndexesOfSpellTrapsWorthyOfKilling();
            return opponentCardsInHand.get(indexesOfTargetingCards.get(0)).getCardName();
        }
        for (int i = 0; i < opponentCardsInHand.size(); i++) {
            if (Card.isCardASpell(opponentCardsInHand.get(i))) {
                SpellCard spellCard = (SpellCard) opponentCardsInHand.get(i);
                ArrayList<NormalSpellCardEffect> normalSpellCardEffects = spellCard.getNormalSpellCardEffects();
                if (normalSpellCardEffects.contains(NormalSpellCardEffect.DESTROY_ALL_OF_OPPONENTS_MONSTERS) ||
                    normalSpellCardEffects.contains(NormalSpellCardEffect.DESTROY_ALL_OF_OPPONENTS_SPELL_AND_TRAP_CARDS) ||
                    normalSpellCardEffects.contains(NormalSpellCardEffect.DESTROY_ALL_MONSTERS_ON_THE_FIELD)) {
                    return opponentCardsInHand.get(i).getCardName();
                }
            }
        }
        return opponentCardsInHand.get(0).getCardName();
    }

    protected String getCommandForChoosingToActivateMonsterFlipEffectInMainPhase(AI ai) {
        ArrayList<Card> opponentMonsterCards = ai.getAiBoardUnderstander().opponentMonsterCards;
        for (int i = 0; i < opponentMonsterCards.size(); i++) {
            if (opponentMonsterCards.get(i) != null) {
                return "yes";
            }
        }
        return "no";
    }

    protected String getCommandForChoosingMonsterToDestroy(AI ai) {
        ArrayList<Integer> monstersWorthyOfKilling = ai.getAiBoardUnderstander().getOpponentMonsterSpellBoardAnalysis().getIndexesOfMonstersWorthyOfKilling();
        if (monstersWorthyOfKilling.size() == 0) {
            for (int i = 0; i < ai.getAiBoardUnderstander().getOpponentMonsterCards().size(); i++) {
                if (ai.getAiBoardUnderstander().getOpponentMonsterCards().get(i) != null) {
                    return "select --opponent --monster " + Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(i + 1, false);
                }
            }
        }
        int maximumAttack = 0;
        int desiredIndex = monstersWorthyOfKilling.get(0);
        for (int i = 0; i < monstersWorthyOfKilling.size(); i++) {
            if (GameManager.getDuelControllerByIndex(0).getAiTurn() == 1) {
                int monsterAttack = MonsterCard.giveATKDEFConsideringEffects("attack", new CardLocation(RowOfCardLocation.OPPONENT_MONSTER_ZONE, i + 1), 0);
                if (monsterAttack > maximumAttack) {
                    maximumAttack = monsterAttack;
                    desiredIndex = i;
                }
            } else {
                int monsterAttack = MonsterCard.giveATKDEFConsideringEffects("attack", new CardLocation(RowOfCardLocation.ALLY_MONSTER_ZONE, i + 1), 0);
                if (monsterAttack > maximumAttack) {
                    maximumAttack = monsterAttack;
                    desiredIndex = i;
                }
            }
        }
        if (maximumAttack >= 2200) {
            return "select --opponent --monster " + Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(desiredIndex + 1, false);
        }
        for (int i = 0; i < monstersWorthyOfKilling.size(); i++) {
            CardLocation cardLocation = null;
            if (GameManager.getDuelControllerByIndex(0).getAiTurn() == 1) {
                cardLocation = new CardLocation(RowOfCardLocation.OPPONENT_MONSTER_ZONE, i + 1);
            } else {
                cardLocation = new CardLocation(RowOfCardLocation.ALLY_MONSTER_ZONE, i + 1);
            }
            MonsterCard monsterCard = (MonsterCard) GameManager.getDuelBoardByIndex(0).getCardByCardLocation(cardLocation);
            ArrayList<BeingAttackedEffect> beingAttackedEffects = monsterCard.getBeingAttackedEffects();
            if (beingAttackedEffects.contains(BeingAttackedEffect.CANNOT_BE_DESTROYED_BY_BATTLE)) {
                return "select --opponent --monster " + Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(i + 1, false);
            }
        }
        return "select --opponent --monster " + Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(monstersWorthyOfKilling.get(0) + 1, false);
    }

    protected String getCommandWhenAIIsInMainPhaseWithNoNecessaryQuery(AI ai) {
        //for (int i = 0;i<500; i++){
        System.out.println("ai is free to think in main phase");
        //}

        //ai.getAiInformationUpdater().updateVariablesForBoard();
        ai.updateAIInformationAccordingToBoard();
        aiKeyVariablesUpdater.updateVariablesOfThisClassAccordingToSituation(ai);
        DuelController duelController = GameManager.getDuelControllerByIndex(0);
        //ArrayList<CardLocation> allySpellCardLocationsNoNull = ai.createCardLocationArrayFromThisArray(false, false);
        ArrayList<CardLocation> allyMonsterCardLocationsNoNull = ai.createCardLocationArrayFromThisArray(false, true);
        //ArrayList<CardLocation> opponentSpellCardLocationsNoNull = ai.createCardLocationArrayFromThisArray(true, false);
        ArrayList<CardLocation> opponentMonsterCardLocationsNoNull = ai.createCardLocationArrayFromThisArray(true, true);
        boolean doOpponentMonstersDominateAIWithBuffEffects = ai.getAiBattlePhaseMind().doMonstersInThisSideDominateTheOtherSideExtendable(opponentMonsterCardLocationsNoNull, allyMonsterCardLocationsNoNull, 0, true);
        boolean doOpponentMonstersDominateAIAlone = ai.getAiBattlePhaseMind().doMonstersInThisSideDominateTheOtherSideExtendable(opponentMonsterCardLocationsNoNull, allyMonsterCardLocationsNoNull, 0, false);
        boolean doAIMonstersDominateOpponentWithBuffEffects = ai.getAiBattlePhaseMind().doMonstersInThisSideDominateTheOtherSideExtendable(allyMonsterCardLocationsNoNull, opponentMonsterCardLocationsNoNull, 0, true);
        //boolean doAIMonstersDominateOpponentAlone = ai.getAiBattlePhaseMind().doMonstersInThisSideDominateTheOtherSideExtendable(allyMonsterCardLocationsNoNull, opponentMonsterCardLocationsNoNull, 0, true);
        //set mystical space typhoon or twin twisters
        System.out.println("IMPORTANT MESSAGE ");
        System.out.println("doOpponentMonstersDominateAIWithBuffEffects " + doOpponentMonstersDominateAIWithBuffEffects);
        System.out.println("doOpponentMonstersDominateAIAlone " + doOpponentMonstersDominateAIAlone);
        System.out.println("doAIMonstersDominateOpponentWithBuffEffects " + doAIMonstersDominateOpponentWithBuffEffects);
        if (aiKeyVariablesUpdater.isDoesAIHaveCardDrawingSpellCardsInHand()) {
            return aiCardFinder.findCardDrawingSpellCardInHandToActivate(ai);
        }
        if (!aiKeyVariablesUpdater.isCanOpponentInterruptAISpellWithQuickSpells() &&
            aiKeyVariablesUpdater.isDoesAIHaveSpellDestroyingAllSpellCardsInHand() &&
            (ai.getAiBoardUnderstander().getOpponentMonsterSpellBoardAnalysis().getIndexesOfSpellTrapsWorthyOfKilling().size() >= 2 &&
                ai.getAiBoardUnderstander().getOpponentMonsterSpellBoardAnalysis().getIndexesOfSpellTrapsGoodToBeKilled().size() >= 1 ||
                ai.getAiBoardUnderstander().getOpponentMonsterSpellBoardAnalysis().getIndexesOfSpellTrapsWorthyOfKilling().size() >= 3)) {
            return aiCardFinder.findSpellCardToDestroyAllOfOpponentsSpellCard(ai);
        }
        if (!aiKeyVariablesUpdater.isCanOpponentInterruptAISpellWithQuickSpells() &&
            (aiKeyVariablesUpdater.isDoesAIHaveSpellDestroyingQuickSpellCardsInHand() || aiKeyVariablesUpdater.isDoesAIHaveSpellDestroyingQuickSpellCardsInBoard()) &&
            (ai.getAiBoardUnderstander().getOpponentMonsterSpellBoardAnalysis().getIndexesOfSpellTrapsWorthyOfKilling().size() >= 2 ||
                ai.getAiBoardUnderstander().getOpponentMonsterSpellBoardAnalysis().getIndexesOfSpellTrapsGoodToBeKilled().size() >= 1 &&
                    ai.getAiBoardUnderstander().getOpponentMonsterSpellBoardAnalysis().getIndexesOfSpellTrapsWorthyOfKilling().size() == 1)) {
            return aiCardFinder.findQuickSpellCardsToDestroyOpponentSpellTrapCards(ai);
        }
        if (aiKeyVariablesUpdater.isDoesAIHaveSpellDestroyingQuickSpellCardsInHand() &&
            !aiKeyVariablesUpdater.isDoesAIHaveSpellDestroyingQuickSpellCardsInBoard()) {
            return aiCardFinder.findQuickSpellCardInHandToSet(ai);
        }
        if (aiKeyVariablesUpdater.isDoesAIHaveCardDiscardingTrapCardsInBoard() &&
            (ai.getAiBoardUnderstander().getOpponentHandBoardAnalysis().getIndexesOfSpellTrapsWorthyOfKilling().size() >= 1 ||
                ai.getAiBoardUnderstander().getOpponentHandBoardAnalysis().getIndexesOfMonstersWorthyOfKilling().size() >= 1 &&
                    !aiKeyVariablesUpdater.isDoesOpponentHaveSpecialSummoningSpellTrapCards())) {
            return aiCardFinder.findCardDiscardingTrapCardInBoardToActivate(ai);
        }
        // check if man eater bug is from change of heart or not
        if (aiKeyVariablesUpdater.isDoesAIHaveMonstersWithFlipEffectsIncludingDestroyingMonsters() &&
            aiKeyVariablesUpdater.isDoesOpponentHaveStoppingMonstersOfAttackingSpellCardsInHand() &&
            ai.getAiBoardUnderstander().getOpponentMonsterSpellBoardAnalysis().getIndexesOfMonstersWorthyOfKilling().size() >= 1) {
            return aiCardFinder.findMonsterWithFlipEffectToFlipSummon(ai);
        }
        if (doOpponentMonstersDominateAIAlone || doOpponentMonstersDominateAIWithBuffEffects) {
            if (aiKeyVariablesUpdater.isDoesAIHaveStoppingMonstersOfAttackingSpellCardsInHand() &&
                !aiKeyVariablesUpdater.isDoesAIHaveStoppingMonstersOfAttackingSpellCardsInBoard() &&
                !aiKeyVariablesUpdater.isCanOpponentInterruptAISpellWithQuickSpells()) {
                return aiCardFinder.findStoppingMonstersFromAttackingSpellCardsFromHandToActivate(ai);
            }
            if (!aiKeyVariablesUpdater.isCanOpponentInterruptAISpellWithQuickSpells() &&
                aiKeyVariablesUpdater.isDoesAIHaveMonsterDestroyingSpellCardsEitherInBoardOrInHand() &&
                ai.getAiBoardUnderstander().getOpponentMonsterSpellBoardAnalysis().getIndexesOfMonstersWorthyOfKilling().size() >= 2) {
                return aiCardFinder.findAllMonsterDestroyingSpellCardFromHandOrBoard(ai);
            }
            if (aiKeyVariablesUpdater.isDoesOpponentHaveMonsterWithSelfDestructionEffectAndNoDamageCalculation() &&
                aiKeyVariablesUpdater.isDoesOpponentHaveMonsterWithHighAttackPowerInBoard() &&
                (aiKeyVariablesUpdater.isDoesAIHaveMonsterSwappingSpellCardsInBoard() || aiKeyVariablesUpdater.isDoesAIHaveMonsterSwappingSpellCardsInHand())) {
                return aiCardFinder.findMonsterSwappingSpellCardsInHandOrBoardToActivate(ai);
            }
            if (aiKeyVariablesUpdater.isDoesAIHaveUndieableMonstersInHand() &&
                duelController.canUserSummonOrSetMonsters(duelController.getAiTurn())) {
                return aiCardFinder.findUndieableMonsterCardInHandToSet(ai);
            }
            if (aiKeyVariablesUpdater.isDoesAIHaveDefensiveMonstersInHand() &&
                duelController.canUserSummonOrSetMonsters(duelController.getAiTurn())) {
                return aiCardFinder.findDefensiveMonsterCardsInHandToSet(ai);
            }
        } else if (doAIMonstersDominateOpponentWithBuffEffects) {
            if (aiKeyVariablesUpdater.isDoesOpponentHaveDefensiveTrapCardsInBoard() &&
                aiKeyVariablesUpdater.isDoesAIHaveMonstersWithStoppingTrapCardActivationEffectInHand() &&
                duelController.canUserSummonOrSetMonsters(duelController.getAiTurn())) {
                return aiCardFinder.findMonsterWithStoppingTrapCardActivationFromHandToNormalSummon(ai);
            }
            if (!aiKeyVariablesUpdater.isDoesOpponentHaveMonsterDestroyingTrapCardsInSummoning() &&
                aiKeyVariablesUpdater.isDoesAIHaveMonstersWithGoodAttackInHand() &&
                duelController.canUserSummonOrSetMonsters(duelController.getAiTurn())) {
                return aiCardFinder.findMonsterWithGoodAttackFromHandToNormalSummon(ai);
            }
            if (!aiKeyVariablesUpdater.isCanOpponentInterruptAISpellWithQuickSpells() && aiKeyVariablesUpdater.isDoesOpponentHaveMonsterDestroyingTrapCardsInAttacking()
                && aiKeyVariablesUpdater.isDoesAIHaveSpellDestroyingQuickSpellCardsInHand() &&
                ai.getAiBoardUnderstander().getOpponentMonsterSpellBoardAnalysis().getIndexesOfSpellTrapsWorthyOfKilling().size() >= 2) {
                return aiCardFinder.findSpellCardToDestroyAllOfOpponentsSpellCard(ai);
            }
            if (!aiKeyVariablesUpdater.isCanOpponentInterruptAISpellWithQuickSpells() && aiKeyVariablesUpdater.isDoesAIHaveSpellDestroyingQuickSpellCardsInHand()) {
                return aiCardFinder.findQuickSpellCardsToDestroyOpponentSpellTrapCards(ai);
            }
            if (aiKeyVariablesUpdater.isDoesAIHaveMonstersWithHighAttackInHand()) {
                return aiCardFinder.findMonsterInBoardWithDecentAttackToChangeCardPositionToFaceUpAttackPosition(ai);
            }
        } else {
            //System.out.println("nobody's monsters dominated another and i am here thinking");
            //System.out.println(duelController.getHaveUsersAlreadySummonedOrSetOnce(duelController.getAiTurn()));
            if (aiKeyVariablesUpdater.isDoesAIHaveMonstersWithGoodAttackInHand() && duelController.canUserSummonOrSetMonsters(duelController.getAiTurn())) {
                return aiCardFinder.findMonsterWithGoodAttackFromHandToNormalSummon(ai);
            }
            if (aiKeyVariablesUpdater.isDoesAIHaveDefensiveMonstersInHand() && duelController.canUserSummonOrSetMonsters(duelController.getAiTurn())) {
                return aiCardFinder.findDefensiveMonsterCardsInHandToSet(ai);
            }
            if (aiKeyVariablesUpdater.isDoesAIHaveMonstersNeeding2TributesInHand() &&
                duelController.canUserSummonOrSetMonsters(duelController.getAiTurn()) &&
                ai.getAiBoardUnderstander().getAiMonsterSpellBoardAnalysis().getIndexesOfMonstersWorthyOfBeingTributes().size() >= 2) {
                numberOfTributesNeeded = 2;
                return aiCardFinder.findMonsterNeedingTributeInHandToNormalSummon(ai);
            }
            if (aiKeyVariablesUpdater.isDoesAIHaveMonstersNeeding3TributesInHand() &&
                ai.getAiBoardUnderstander().getAiMonsterSpellBoardAnalysis().getIndexesOfMonstersWorthyOfBeingTributes().size() >= 3) {
                numberOfTributesNeeded = 3;
                return aiCardFinder.findMonsterNeedingTributeInHandToSpecialSummon(ai);
            }
        }
        System.out.println("THIS MESSAGE IS BECAUSE I WANT  TO SET A TRAP CARD");
        System.out.println(GameManager.getPhaseControllerByIndex(0).getPhaseInGame());
        System.out.println(GameManager.getDuelControllerByIndex(0).getAiTurn());
        if (GameManager.getPhaseControllerByIndex(0).getPhaseInGame().equals(PhaseInGame.OPPONENT_MAIN_PHASE_2) &&
            GameManager.getDuelControllerByIndex(0).getAiTurn() == 2 ||
            GameManager.getPhaseControllerByIndex(0).getPhaseInGame().equals(PhaseInGame.ALLY_MAIN_PHASE_2) &&
                GameManager.getDuelControllerByIndex(0).getAiTurn() == 1) {
            if (aiKeyVariablesUpdater.isDoesAIHaveDefensiveTrapCardsInHand() && !aiKeyVariablesUpdater.isDoesAIHaveDefensiveTrapCardsInBoard()) {
                return aiCardFinder.findDefensiveTrapCardsInHandForBattlePhaseToSet(ai);
            }
        }
        return "next phase\n";
    }


    //AI MAIN PHASE 1 CHAIN MIND
    protected String getCommandForChoosingToActivateTrapInChainOrNotInMainPhase1(AI ai) {
        //ai.getAiInformationUpdater().updateVariablesForBoard();
        //ai turn chain 3rd time not handeled
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(0);
        Action uninterruptedAction = uninterruptedActions.get(uninterruptedActions.size() - 1);
        ArrayList<CardLocation> opponentMonsterCardLocationsNoNull = ai.createCardLocationArrayFromThisArray(true, true);
        ArrayList<CardLocation> allyMonsterCardLocationsNoNull = ai.createCardLocationArrayFromThisArray(false, true);
        boolean doOpponentMonstersDominateAIWithBuffEffects = ai.getAiBattlePhaseMind().doMonstersInThisSideDominateTheOtherSideExtendable(opponentMonsterCardLocationsNoNull, allyMonsterCardLocationsNoNull, 0, true);
        boolean doAIMonstersDominateOpponentWithBuffEffects = ai.getAiBattlePhaseMind().doMonstersInThisSideDominateTheOtherSideExtendable(allyMonsterCardLocationsNoNull, opponentMonsterCardLocationsNoNull, 0, true);
        if (uninterruptedAction.getActionType().equals(ActionType.ALLY_ACTIVATING_SPELL) || uninterruptedAction.getActionType().equals(ActionType.OPPONENT_ACTIVATING_SPELL)) {
            SpellCard finalCardActivated = (SpellCard) GameManager.getDuelBoardByIndex(0).getCardByCardLocation(uninterruptedAction.getFinalMainCardLocation());
            ArrayList<NormalSpellCardEffect> normalSpellCardEffects = finalCardActivated.getNormalSpellCardEffects();
            if (doAIMonstersDominateOpponentWithBuffEffects && normalSpellCardEffects.contains(NormalSpellCardEffect.DESTROY_ALL_MONSTERS_ON_THE_FIELD)) {
                return "yes";
            }
            if (normalSpellCardEffects.contains(NormalSpellCardEffect.DESTROY_ALL_OF_OPPONENTS_SPELL_AND_TRAP_CARDS)
                || normalSpellCardEffects.contains(NormalSpellCardEffect.DESTROY_ALL_OF_OPPONENTS_MONSTERS)) {
                return "yes";
            }
            ArrayList<ContinuousSpellCardEffect> continuousSpellCardEffects = finalCardActivated.getContinuousSpellCardEffects();
            if (continuousSpellCardEffects.contains(ContinuousSpellCardEffect.UNTIL_THIS_CARD_IS_FACE_UP_ON_FIELD_OPPONENT_MONSTERS_CANT_ATTACK)) {
                return "yes";
            }
            boolean canOpponentSpecialSummonMonstersWithHighAttack = false;
            ArrayList<Card> allyCardsInGraveyard = GameManager.getDuelBoardByIndex(0).getAllyCardsInGraveyard();
            ArrayList<Card> opponentCardsInGraveyard = GameManager.getDuelBoardByIndex(0).getOpponentCardsInGraveyard();
            for (int i = 0; i < opponentCardsInGraveyard.size(); i++) {
                if (Card.isCardAMonster(opponentCardsInGraveyard.get(i)) && ((MonsterCard) opponentCardsInGraveyard.get(i)).getAttackPower() >= 2200) {
                    canOpponentSpecialSummonMonstersWithHighAttack = true;
                }
            }
            for (int i = 0; i < allyCardsInGraveyard.size(); i++) {
                if (Card.isCardAMonster(allyCardsInGraveyard.get(i)) && ((MonsterCard) allyCardsInGraveyard.get(i)).getAttackPower() >= 2200) {
                    canOpponentSpecialSummonMonstersWithHighAttack = true;
                }
            }
            if (normalSpellCardEffects.contains(NormalSpellCardEffect.SPECIAL_SUMMON_MONSTER_FROM_EITHER_GY) && canOpponentSpecialSummonMonstersWithHighAttack) {
                return "yes";
            }
        } else if (uninterruptedAction.getActionType().equals(ActionType.ALLY_ACTIVATING_TRAP) || uninterruptedAction.getActionType().equals(ActionType.OPPONENT_ACTIVATING_TRAP)) {
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(0);
            TrapCard trapCard = (TrapCard) duelBoard.getCardByCardLocation(uninterruptedAction.getFinalMainCardLocation());
            ArrayList<NormalTrapCardEffect> normalTrapCardEffects = trapCard.getNormalTrapCardEffects();
            boolean canOpponentSpecialSummonMonstersWithHighAttack = false;
            int opponentTurn = 3 - GameManager.getDuelControllerByIndex(0).getAiTurn();
            ArrayList<Card> opponentGraveyardCards = null;
            if (opponentTurn == 1) {
                opponentGraveyardCards = duelBoard.getAllyCardsInGraveyard();
            } else {
                opponentGraveyardCards = duelBoard.getOpponentCardsInGraveyard();
            }
            for (int i = 0; i < opponentGraveyardCards.size(); i++) {
                if (Card.isCardAMonster(opponentGraveyardCards.get(i)) && ((MonsterCard) opponentGraveyardCards.get(i)).getAttackPower() >= 2200) {
                    canOpponentSpecialSummonMonstersWithHighAttack = true;
                }
            }
            if (normalTrapCardEffects.contains(NormalTrapCardEffect.SPECIAL_SUMMON_ONE_MONSTER_IN_YOUR_GRAVEYARD_IN_FACE_UP_ATTACK_POSITION) && canOpponentSpecialSummonMonstersWithHighAttack) {
                return "yes";
            } else if (normalTrapCardEffects.contains(NormalTrapCardEffect.SPECIAL_SUMMON_ONE_MONSTER_IN_YOUR_GRAVEYARD_IN_FACE_UP_ATTACK_POSITION)) {
                return "no";
            }
        } else if (doOpponentMonstersDominateAIWithBuffEffects && !aiKeyVariablesUpdater.isDoesOpponentHaveSpecialSummoningSpellTrapCards() &&
            ai.getAiBoardUnderstander().getOpponentMonsterSpellBoardAnalysis().getIndexesOfMonstersWorthyOfKilling().size() +
                ai.getAiBoardUnderstander().getOpponentMonsterSpellBoardAnalysis().getIndexesOfMonstersWithGoodAttack().size() >= 2) {
            return "yes";
        }
        return "no";
    }

    protected String getCommandForChoosingSpellTrapToActivateInChainInMainPhase1(AI ai) {
        //ai.getAiInformationUpdater().updateVariablesForBoard();
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(0);
        Action uninterruptedAction = uninterruptedActions.get(uninterruptedActions.size() - 1);
        if (uninterruptedAction.getActionType().equals(ActionType.ALLY_ACTIVATING_SPELL) || uninterruptedAction.getActionType().equals(ActionType.OPPONENT_ACTIVATING_SPELL)
            || uninterruptedAction.getActionType().equals(ActionType.ALLY_ACTIVATING_TRAP) || uninterruptedAction.getActionType().equals(ActionType.OPPONENT_ACTIVATING_TRAP)) {
            return aiCardFinder.findQuickSpellCardOnAISideWithSpellTrapDestroyingEffectInChain(ai);
        }
        return aiCardFinder.findTrapCardOnAISideWithMonsterDestroyingInSummoningEffect(ai);
    }

    protected String getCommandForGivingFurtherInputToActivateSpellTrapInChainInMainPhase1(AI ai) {
        //ai.getAiInformationUpdater().updateVariablesForBoard();
        return getCommandForGivingFurtherInputForActivatingSpellOrTrap(ai);
    }

    //AI MAIN PHASE 2 CHAIN MIND
    protected String getCommandForChoosingToActivateTrapInChainOrNotInMainPhase2(AI ai) {
        return getCommandForChoosingToActivateTrapInChainOrNotInMainPhase1(ai);
    }

    protected String getCommandForChoosingSpellTrapToActivateInChainInMainPhase2(AI ai) {
        return getCommandForChoosingSpellTrapToActivateInChainInMainPhase1(ai);
    }

    protected String getCommandForGivingFurtherInputToActivateSpellTrapInChainInMainPhase2(AI ai) {
        return getCommandForGivingFurtherInputToActivateSpellTrapInChainInMainPhase1(ai);
    }

    protected String getCommandForChoosingToActivateSpellTrapForThirdTimeInChain(AI ai) {
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(0);
        Action aiSummoningAction = uninterruptedActions.get(uninterruptedActions.size() - 2);
        CardLocation mainCardLocation = aiSummoningAction.getFinalMainCardLocation();
        MonsterCard summonedMonsterCard = (MonsterCard) GameManager.getDuelBoardByIndex(0).getCardByCardLocation(mainCardLocation);
        Action opponentTrapActivatingAction = uninterruptedActions.get(uninterruptedActions.size() - 1);
        CardLocation opponentTrapLocation = opponentTrapActivatingAction.getFinalMainCardLocation();
        TrapCard trapCard = (TrapCard) GameManager.getDuelBoardByIndex(0).getCardByCardLocation(opponentTrapLocation);
        ArrayList<NormalSummonTrapCardEffect> normalSummonTrapCardEffects = trapCard.getNormalSummonTrapCardEffects();
        if (normalSummonTrapCardEffects.contains(NormalSummonTrapCardEffect.IF_ATK_IS_AT_LEAST_1000_ATK_DESTROY_IT)) {
            if (summonedMonsterCard.getAttackPower() >= 2200) {
                return "yes";
            } else {
                return "no";
            }
        } else if (normalSummonTrapCardEffects.contains(NormalSummonTrapCardEffect.DESTROY_ALL_MONSTERS_ON_FIELD)) {
            ArrayList<CardLocation> opponentMonsterCardLocationsNoNull = ai.createCardLocationArrayFromThisArray(true, true);
            ArrayList<CardLocation> allyMonsterCardLocationsNoNull = ai.createCardLocationArrayFromThisArray(false, true);
            boolean doOpponentMonstersDominateAIWithBuffEffects = ai.getAiBattlePhaseMind().doMonstersInThisSideDominateTheOtherSideExtendable(opponentMonsterCardLocationsNoNull, allyMonsterCardLocationsNoNull, 0, true);
            boolean doAIMonstersDominateOpponentWithBuffEffects = ai.getAiBattlePhaseMind().doMonstersInThisSideDominateTheOtherSideExtendable(allyMonsterCardLocationsNoNull, opponentMonsterCardLocationsNoNull, 0, true);
            if (doOpponentMonstersDominateAIWithBuffEffects) {
                return "no";
            } else if (doAIMonstersDominateOpponentWithBuffEffects) {
                if (ai.getAiBoardUnderstander().getAiMonsterSpellBoardAnalysis().getIndexesOfMonstersWorthyOfKilling().size() >= 2 ||
                    ai.getAiBoardUnderstander().getAiMonsterSpellBoardAnalysis().getIndexesOfMonstersWorthyOfKilling().size() +
                        ai.getAiBoardUnderstander().getAiMonsterSpellBoardAnalysis().getIndexesOfMonstersWorthyOfBeingTributes().size() >= 2) {
                    return "yes";
                } else {
                    return "no";
                }
            }
        }
        return "no";
    }

    public AIKeyVariablesUpdater getAiKeyVariablesUpdater() {
        return aiKeyVariablesUpdater;
    }
}
