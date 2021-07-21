package project.server.controller.duel.GamePhaseControllers;

import project.model.MonsterEffectEnums.ContinuousMonsterEffect;
import project.model.SpellEffectEnums.ContinuousSpellCardEffect;
import project.model.TrapEffectEnums.ContinuousTrapCardEffect;
import project.model.cardData.TrapCardData.TrapCard;
import project.server.controller.duel.GamePackage.DuelBoard;
import project.server.controller.duel.GamePackage.DuelController;
import project.server.controller.duel.PreliminaryPackage.GameManager;
import project.model.Utility.Utility;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.CardPosition;
import project.model.cardData.MonsterCardData.MonsterCard;
import project.model.cardData.SpellCardData.SpellCard;

import java.util.ArrayList;

public class BattlePhaseController extends ChainController {

    protected String checkAttackWithCard(Card card, String token) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        int turn = duelController.getTurn();
        if (!duelBoard.isCardInMonsterZone((MonsterCard) card, turn)) {
            return "you can't attack with this card";
        } else {
            return checkPhase(card, token);
        }
    }

    protected String checkPhase(Card card, String token) {
        String resultOfChecking = Utility.areWeInBattlePhase(token);
        if (!resultOfChecking.equals("")) {
            return resultOfChecking;
        } else {
            return checkPositionForAttack(card, token);
        }
    }

    protected String checkPositionForAttack(Card card, String token) {
        if (!(card.getCardPosition().equals(CardPosition.FACE_UP_ATTACK_POSITION))) {
            return "you can't attack with this card";
        } else {
            return checkAlreadyAttacked(card, token);
        }
    }

    protected String checkAlreadyAttacked(Card card, String token) {
        MonsterCard monsterCard = (MonsterCard) card;
        if (monsterCard.isHasCardAlreadyAttacked()) {
            return "this card already attacked";
        } else {
            return "";
        }
    }

    protected String areContinuousSpellTrapOrMonsterEffectsPreventingMonsterFromAttacking(CardLocation allyCardLocation, CardLocation opponentCardLocation, String token) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        ArrayList<Card> allySpellCards = duelBoard.getAllySpellCards();
        ArrayList<Card> opponentSpellCards = duelBoard.getOpponentSpellCards();
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        int turn = duelController.getTurn();
        String output = "";
        output = areMonsterWithATK1500OrMoreEligibleToAttack(allySpellCards, allyCardLocation, token);
        if (!output.equals("")) {
            return output;
        }
        output = areMonsterWithLevel4OrMoreEligibleToAttack(allySpellCards, opponentSpellCards, allyCardLocation, token);
        output = areMonsterWithATK1500OrMoreEligibleToAttack(opponentSpellCards, allyCardLocation, token);
        if (!output.equals("")) {
            return output;
        }
        if (turn == 1) {
            output = areOpponentMonstersAllowedToAttack(opponentSpellCards, token);
        } else {
            output = areOpponentMonstersAllowedToAttack(allySpellCards, token);
        }
        if (!output.equals("")) {
            return output;
        }
        if (turn == 1) {
            output = canMonsterCardBeTargetedCheckingOtherMonsters(duelBoard.getOpponentMonsterCards(), opponentCardLocation, token);
        } else {
            output = canMonsterCardBeTargetedCheckingOtherMonsters(duelBoard.getAllyMonsterCards(), opponentCardLocation, token);
        }
        if (!output.equals("")) {
            return output;
        }
        return "";
    }

    private String areMonsterWithLevel4OrMoreEligibleToAttack(ArrayList<Card> spellCards, ArrayList<Card> opponentSpellCards, CardLocation allyCardLocation, String token) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        Card card = duelBoard.getCardByCardLocation(allyCardLocation);
        if (Card.isCardAMonster(card) && ((MonsterCard) card).getLevel() >= 4) {
            for (int i = 0; i < spellCards.size(); i++) {
                if (spellCards.get(i) != null && Card.isCardATrap(spellCards.get(i))) {
                    TrapCard trapCard = (TrapCard) spellCards.get(i);
                    ArrayList<ContinuousTrapCardEffect> continuousTrapCardEffects = trapCard.getContinuousTrapCardEffects();
                    if (continuousTrapCardEffects.contains(ContinuousTrapCardEffect.ALL_LEVEL_4_OR_HIGHER_MONSTERS_CANT_ATTACK) && trapCard.getCardPosition().equals(CardPosition.FACE_UP_ACTIVATED_POSITION)) {
                        return "this monster card has level at least 4, so it can't attack.";
                    }
                }
            }
            for (int i = 0; i < opponentSpellCards.size(); i++) {
                if (opponentSpellCards.get(i) != null && Card.isCardATrap(opponentSpellCards.get(i))) {
                    TrapCard trapCard = (TrapCard) opponentSpellCards.get(i);
                    ArrayList<ContinuousTrapCardEffect> continuousTrapCardEffects = trapCard.getContinuousTrapCardEffects();
                    if (continuousTrapCardEffects.contains(ContinuousTrapCardEffect.ALL_LEVEL_4_OR_HIGHER_MONSTERS_CANT_ATTACK) && trapCard.getCardPosition().equals(CardPosition.FACE_UP_ACTIVATED_POSITION)) {
                        return "this monster card has level at least 4, so it can't attack.";
                    }
                }
            }
        }
        return "";
    }

    private String areMonsterWithATK1500OrMoreEligibleToAttack(ArrayList<Card> spellCards, CardLocation allyCardLocation, String token) {
        if (MonsterCard.giveATKDEFConsideringEffects("attack", allyCardLocation, token) >= 1500) {
            for (int i = 0; i < spellCards.size(); i++) {
                if (spellCards.get(i) != null && Card.isCardASpell(spellCards.get(i))) {
                    SpellCard spellCard = (SpellCard) spellCards.get(i);
                    ArrayList<ContinuousSpellCardEffect> continuousSpellCardEffects = spellCard.getContinuousSpellCardEffects();
                    if (continuousSpellCardEffects.contains(ContinuousSpellCardEffect.MONSTERS_WITH_1500_OR_MORE_ATK_CANT_ATTACK)) {
                        if (spellCard.getCardPosition().equals(CardPosition.FACE_UP_ACTIVATED_POSITION)) {
                            return "this monster card has at least 1500 ATK, so it can't attack.";
                        }
                    }
                }
            }
        }
        return "";
    }

    private String areOpponentMonstersAllowedToAttack(ArrayList<Card> spellCards, String token) {
        for (int i = 0; i < spellCards.size(); i++) {
            if (spellCards.get(i) != null && (Card.isCardASpell(spellCards.get(i)))) {
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
        }
        return "";
    }

    private String canMonsterCardBeTargetedCheckingOtherMonsters(ArrayList<Card> opponentMonsterCards, CardLocation opponentMonsterCardLocation, String token) {
        if (opponentMonsterCardLocation == null) {
            return "";
        }
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        MonsterCard opponentMonsterCard = (MonsterCard) duelBoard.getCardByCardLocation(opponentMonsterCardLocation);
        ArrayList<ContinuousMonsterEffect> continuousMonsterEffects = opponentMonsterCard.getContinuousMonsterEffects();
        if (continuousMonsterEffects.contains(ContinuousMonsterEffect.CANNOT_BE_ATTACKED_IF_YOU_CONTROL_ANOTHER_MONSTER) &&
            (opponentMonsterCard.getCardPosition().equals(CardPosition.FACE_UP_ATTACK_POSITION) || opponentMonsterCard.getCardPosition().equals(CardPosition.FACE_UP_DEFENSE_POSITION))) {
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
