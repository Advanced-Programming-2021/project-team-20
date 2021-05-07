package controller.duel.GamePhaseControllers;

import com.sun.org.apache.xerces.internal.impl.Constants;
import com.sun.org.apache.xerces.internal.impl.xs.opti.SchemaDOMParser;
import controller.duel.CardEffects.MonsterEffectEnums.ContinuousMonsterEffect;
import controller.duel.CardEffects.SpellEffectEnums.ContinuousSpellCardEffect;
import controller.duel.GamePackage.DuelBoard;
import controller.duel.GamePackage.DuelController;
import controller.duel.PreliminaryPackage.GameManager;
import controller.duel.Utility.Utility;
import model.cardData.General.Card;
import model.cardData.General.CardLocation;
import model.cardData.General.CardPosition;
import model.cardData.MonsterCardData.MonsterCard;
import model.cardData.SpellCardData.SpellCard;

import java.util.ArrayList;

public class BattlePhaseController extends ChainController {

    protected String checkAttackWithCard(Card card, int index) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        int turn = duelController.getTurn();
        if (!duelBoard.isCardInMonsterZone((MonsterCard) card, turn)) {
            return "you can't attack with this card";
        } else {
            return checkPhase(card, index);
        }
    }

    protected String checkPhase(Card card, int index) {
        String resultOfChecking = Utility.areWeInBattlePhase(0);
        if (!resultOfChecking.equals("")) {
            return resultOfChecking;
        } else {
            return checkPositionForAttack(card, index);
        }
    }

    protected String checkPositionForAttack(Card card, int index) {
        if (!(card.getCardPosition().equals(CardPosition.FACE_UP_ATTACK_POSITION))) {
            return "you can't attack with this card";
        } else {
            return checkAlreadyAttacked(card, index);
        }
    }

    protected String checkAlreadyAttacked(Card card, int index) {
        MonsterCard monsterCard = (MonsterCard) card;
        if (monsterCard.isCardAttacked()) {
            return "this card already attacked";
        } else {
            return "";
            // return checkIndexOfAttackedMonster(card, index);
        }
    }

    protected String areContinuousSpellTrapOrMonsterEffectsPreventingMonsterFromAttacking(CardLocation allyCardLocation, CardLocation opponentCardLocation, int index) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        ArrayList<Card> allySpellCards = duelBoard.getAllySpellCards();
        ArrayList<Card> opponentSpellCards = duelBoard.getOpponentSpellCards();
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        int turn = duelController.getTurn();
        String output = "";
        output = areMonsterWithATK1500OrMoreEligibleToAttack(allySpellCards, allyCardLocation);
        if (!output.equals("")) {
            return output;
        }
        output = areMonsterWithATK1500OrMoreEligibleToAttack(opponentSpellCards, allyCardLocation);
        if (!output.equals("")) {
            return output;
        }
        if (turn == 1) {
            output = areOpponentMonstersAllowedToAttack(opponentSpellCards, index);
        } else {
            output = areOpponentMonstersAllowedToAttack(allySpellCards, index);
        }
        if (!output.equals("")) {
            return output;
        }
        if (turn == 1) {
            output = canMonsterCardBeTargetedCheckingOtherMonsters(duelBoard.getOpponentMonsterCards(), opponentCardLocation, index);
        } else {
            output = canMonsterCardBeTargetedCheckingOtherMonsters(duelBoard.getAllyMonsterCards(), opponentCardLocation, index);
        }
        if (!output.equals("")) {
            return output;
        }
        return "";
    }

    private String areMonsterWithATK1500OrMoreEligibleToAttack(ArrayList<Card> spellCards, CardLocation allyCardLocation) {
        if (MonsterCard.giveATKDEFConsideringEffects("attack", allyCardLocation, 0) >= 1500) {
            for (int i = 0; i < spellCards.size(); i++) {
                SpellCard spellCard = (SpellCard) spellCards.get(i);
                ArrayList<ContinuousSpellCardEffect> continuousSpellCardEffects = spellCard.getContinuousSpellCardEffects();
                if (continuousSpellCardEffects.contains(ContinuousSpellCardEffect.MONSTERS_WITH_1500_OR_MORE_ATK_CANT_ATTACK)) {
                    return "this monster card has at least 1500 ATK, so it can't attack.";
                }
            }
        }
        return "";
    }

    private String areOpponentMonstersAllowedToAttack(ArrayList<Card> spellCards, int index) {
        for (int i = 0; i < spellCards.size(); i++) {
            SpellCard spellCard = (SpellCard) spellCards.get(i);
            ArrayList<ContinuousSpellCardEffect> continuousSpellCardEffects = spellCard.getContinuousSpellCardEffects();
            if (continuousSpellCardEffects.contains(ContinuousSpellCardEffect.UNTIL_THIS_CARD_IS_FACE_UP_ON_FIELD_OPPONENT_MONSTERS_CANT_ATTACK)) {
                if (spellCard.getCardPosition().equals(CardPosition.FACE_UP_ACTIVATED_POSITION)) {
                    if (spellCard.getNumberOfTurnsForActivation() >= 1) {
                        return "your monster is not allowed to attack in this turn";
                    }
                }
            }
        }
        return "";
    }

    private String canMonsterCardBeTargetedCheckingOtherMonsters(ArrayList<Card> opponentMonsterCards, CardLocation opponentMonsterCardLocation, int index) {
        if (opponentMonsterCardLocation == null) {
            return "";
        }
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        MonsterCard opponentMonsterCard = (MonsterCard) duelBoard.getCardByCardLocation(opponentMonsterCardLocation);
        ArrayList<ContinuousMonsterEffect> continuousMonsterEffects = opponentMonsterCard.getContinuousMonsterEffects();
        if (continuousMonsterEffects.contains(ContinuousMonsterEffect.CANNOT_BE_ATTACKED_IF_YOU_CONTROL_ANOTHER_MONSTER)) {
            int numberOfMonsters = 0;
            for (int i = 0; i < opponentMonsterCards.size(); i++) {
                if (opponentMonsterCards.get(i) != null) {
                    numberOfMonsters += 1;
                }
            }
            if (numberOfMonsters > 1) {
                return "you can't target this monster for attacking";
            }
            return "";
        }
        return "";
    }
}
