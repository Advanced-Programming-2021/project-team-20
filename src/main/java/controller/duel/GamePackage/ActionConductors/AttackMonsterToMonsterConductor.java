package controller.duel.GamePackage.ActionConductors;

import java.util.ArrayList;
import java.util.regex.Matcher;

import controller.duel.CardEffects.MonsterEffectEnums.BeingAttackedEffect;
import controller.duel.GamePackage.Action;
import controller.duel.GamePackage.DuelBoard;
import controller.duel.GamePackage.DuelController;
import controller.duel.GamePhaseControllers.SelectCardController;
import controller.duel.PreliminaryPackage.GameManager;
import controller.duel.Utility.Utility;
import model.cardData.General.Card;
import model.cardData.General.CardLocation;
import model.cardData.General.CardPosition;
import model.cardData.General.RowOfCardLocation;
import model.cardData.MonsterCardData.MonsterCard;
import model.cardData.MonsterCardData.MonsterCardFamily;
import model.cardData.MonsterCardData.MonsterCardValue;

public class AttackMonsterToMonsterConductor {
    private static boolean isAttackCanceled;
    private static int attackingMonsterATK;
    private static int attackingMonsterDEF;
    private static int defendingMonsterATK;
    private static int defendingMonsterDEF;
    private static CardLocation attackingMonsterCardLocation;
    private static CardLocation defendingMonsterCardLocation;
    private static MonsterCard attackingMonsterCard;
    private static MonsterCard defendingMonsterCard;
    private static ArrayList<BeingAttackedEffect> beingAttackedEffectsForAttackingMonster = new ArrayList<>();
    private static ArrayList<BeingAttackedEffect> beingAttackedEffectsForDefendingMonster = new ArrayList<>();
    private static boolean promptingUserToActivateMonsterEffect;
    private static boolean isBeingAttackedMonsterFlipped = false;
    private static boolean isClassWaitingForFurtherChainInput = false;
    private static boolean doesDefendingMonsterEffectActivate = false;
    private static int actionTurn;
    private static boolean doesAttackingMonsterGoToGraveyard;
    private static boolean doesDefendingMonsterGoToGraveyard;
    private static boolean doesDefendingMonsterEventuallyGoToGraveyard;
    private static boolean doesAttackingMonsterEventuallyGoToGraveyard;
    private static ArrayList<Integer> playersLifePointsChange = new ArrayList<>();
    private static boolean didAttackingUserReceiveDamage = false;

    public static boolean isPromptingUserToActivateMonsterEffect() {
        return promptingUserToActivateMonsterEffect;
    }

    public static boolean isClassWaitingForFurtherChainInput() {
        return isClassWaitingForFurtherChainInput;
    }

    public static String AttackConductor(int index, int numberInListOfActions) {
        ArrayList<Action> actions = GameManager.getActionsByIndex(index);
        Action action = actions.get(numberInListOfActions);
        isAttackCanceled = action.isActionCanceled();
        if (!isAttackCanceled) {
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
            actionTurn = action.getActionTurn();
            playersLifePointsChange.clear();
            playersLifePointsChange.add(0);
            playersLifePointsChange.add(0);
            attackingMonsterCardLocation = action.getMainCardLocation();
            Card attackingCard = duelBoard.getCardByCardLocation(attackingMonsterCardLocation);
            attackingMonsterCard = (MonsterCard) attackingCard;
            defendingMonsterCardLocation = action.getTargetingCards().get(action.getTargetingCards().size() - 1);
            Card defendingCard = duelBoard.getCardByCardLocation(defendingMonsterCardLocation);
            defendingMonsterCard = (MonsterCard) defendingCard;
            if (defendingMonsterCard.getCardPosition().equals(CardPosition.FACE_DOWN_MONSTER_SET_POSITION)) {
                defendingMonsterCard.setCardPosition(CardPosition.FACE_UP_DEFENSE_POSITION);
                isBeingAttackedMonsterFlipped = true;
                //must change turns and prompt user to select for flip effect
            }
            //also put if flip effect
            if (doesDefendingMonsterHaveEffect() && !defendingMonsterCard.isOncePerTurnCardEffectUsed()) {
                promptingUserToActivateMonsterEffect = true;
                return "now it will be another player's turn\nshow board\ndo you want to activate your monster card's effect?";
            }
            return conductBattle();
        }
        return "action was canceled.";
    }

    public static String defendingMonsterEffectAnalysis(String string) {
        String inputRegex = "(?<=\\n|^)no(?=\\n|$)";
        Matcher matcher = Utility.getCommandMatcher(string, inputRegex);
        DuelController duelController = GameManager.getDuelControllerByIndex(0);
        int turn = duelController.getTurn();
        if (Utility.isMatcherCorrectWithoutErrorPrinting(matcher)) {
            promptingUserToActivateMonsterEffect = false;
        } else {
            String anotherRegex = "(?<=\\n|^)yes(?=\\n|$)";
            Matcher newMatcher = Utility.getCommandMatcher(string, anotherRegex);
            if (Utility.isMatcherCorrectWithoutErrorPrinting(newMatcher)) {
                promptingUserToActivateMonsterEffect = false;
                isClassWaitingForFurtherChainInput = true;
                doesDefendingMonsterEffectActivate = true;
                ArrayList<BeingAttackedEffect> beingAttackedEffects = defendingMonsterCard.getBeingAttackedEffects();
                if (beingAttackedEffects.contains(BeingAttackedEffect.NEGATE_ATTACK_ONCE_PER_TURN) && beingAttackedEffects.contains(BeingAttackedEffect.SPECIAL_SUMMON_CYBERSE_NORMAL_MONSTER_FROM_HAND_GV_DECK_ONCE_PER_TURN)) {
                    return "show hand, graveyard, deck.\nselect one cyberse normal monster to special summon";
                }
            }
        }
        return conductBattle();
    }

    public static String checkGivenInputForMonsterEffectActivation(int index) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(index);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        CardLocation cardLocation = selectedCardLocations.get(selectedCardLocations.size() - 1);
        Card card = duelBoard.getCardByCardLocation(cardLocation);
        if (Card.isCardATrap(card) || Card.isCardASpell(card)) {
            return "this card cannot be chosen for special summon.";
        } else if (Card.isCardAMonster(card) && !((MonsterCard) card).getMonsterCardValue().equals(MonsterCardValue.NORMAL)) {
            return "this is not a normal monster.\nselect another monster.";
        } else if (Card.isCardAMonster(card) && !((MonsterCard) card).getMonsterCardFamily().equals(MonsterCardFamily.CYBERSE)) {
            return "this monster is not from cyberse family.\nselect another monster.";
        } else {
            ArrayList<Action> actions = GameManager.getActionsByIndex(index);
            Action action = actions.get(actions.size() - Action.getCurrentActionConducting() - 1);
            int actionTurn = action.getActionTurn();
            RowOfCardLocation rowOfCardLocation = cardLocation.getRowOfCardLocation();
            if (actionTurn == 1 && !rowOfCardLocation.equals(RowOfCardLocation.ALLY_HAND_ZONE) && !rowOfCardLocation.equals(RowOfCardLocation.ALLY_DECK_ZONE) && !rowOfCardLocation.equals(RowOfCardLocation.ALLY_GRAVEYARD_ZONE)) {
                return "selected card is neither in your hand, deck, or graveyard.\nselect another card";
            } else if (actionTurn == 2 && !rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_HAND_ZONE) && !rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_DECK_ZONE) && !rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE)) {
                return "selected card is neither in your hand, deck, or graveyard.\nselect another card";
            } else {
                duelBoard.removeCardByCardLocation(cardLocation);
                duelBoard.addCardToMonsterZone(card, actionTurn);
                defendingMonsterCard.setOncePerTurnCardEffectUsed(true);
                return Action.conductAllActions(index);
            }
        }
    }

    public static boolean doesDefendingMonsterHaveEffect() {
        ArrayList<BeingAttackedEffect> beingAttackedEffects = defendingMonsterCard.getBeingAttackedEffects();
        if (beingAttackedEffects.contains(BeingAttackedEffect.NEGATE_ATTACK_ONCE_PER_TURN) && beingAttackedEffects.contains(BeingAttackedEffect.SPECIAL_SUMMON_CYBERSE_NORMAL_MONSTER_FROM_HAND_GV_DECK_ONCE_PER_TURN)) {
            return true;
        }
        if (beingAttackedEffects.contains(BeingAttackedEffect.SET_ATTACKING_MONSTER_ATK_TO_0_ONCE_PER_TURN)) {
            return true;
        }
        return false;
    }

    public static String conductBattle() {
        if (defendingMonsterCard.getCardPosition().equals(CardPosition.FACE_UP_ATTACK_POSITION)) {
            return faceUpAttackPositionMonsterToFaceUpAttackPositionMonster();
        }
        return faceUpAttackPositionMonsterToFaceUpDefensePositionMonster();
    }


    public static String faceUpAttackPositionMonsterToFaceUpAttackPositionMonster() {
        attackingMonsterATK = MonsterCard.giveATKDEFConsideringEffects("attack", attackingMonsterCardLocation, 0);
        defendingMonsterATK = MonsterCard.giveATKDEFConsideringEffects("attack", defendingMonsterCardLocation, 0);
        System.out.println("attackingMonsterATK is " + attackingMonsterATK);
        System.out.println("defendingMonsterATK is " + defendingMonsterATK);
        ArrayList<BeingAttackedEffect> beingAttackedEffects = defendingMonsterCard.getBeingAttackedEffects();
        if (beingAttackedEffects.contains(BeingAttackedEffect.SET_ATTACKING_MONSTER_ATK_TO_0_ONCE_PER_TURN) && doesDefendingMonsterEffectActivate) {
            attackingMonsterATK = 0;
        }
        if (attackingMonsterATK == defendingMonsterATK) {
            doesAttackingMonsterGoToGraveyard = true;
            doesDefendingMonsterGoToGraveyard = true;
        } else if (attackingMonsterATK > defendingMonsterATK) {
            doesAttackingMonsterGoToGraveyard = false;
            doesDefendingMonsterGoToGraveyard = true;
            playersLifePointsChange.set(2 - actionTurn, defendingMonsterATK - attackingMonsterATK);
        } else {
            doesAttackingMonsterGoToGraveyard = true;
            doesDefendingMonsterGoToGraveyard = false;
            playersLifePointsChange.set(actionTurn - 1, attackingMonsterATK - defendingMonsterATK);
            didAttackingUserReceiveDamage = true;
        }
        //reconsiderEffectsOfMonsterInBattle();
        return finishAttackConduction();
    }

    public static String faceUpAttackPositionMonsterToFaceUpDefensePositionMonster() {
        System.out.println("defending monster card name os " + defendingMonsterCard.getCardName() + "\niiiiiiiiiiiiiiiiiiiiiii\n");
        System.out.println("attacking monster card name os " + attackingMonsterCard.getCardName() + "\niiiiiiiiiiiiiiiiiiiiiii\n");
        attackingMonsterATK = MonsterCard.giveATKDEFConsideringEffects("attack", attackingMonsterCardLocation, 0);
        defendingMonsterDEF = MonsterCard.giveATKDEFConsideringEffects("defense", defendingMonsterCardLocation, 0);
        ArrayList<BeingAttackedEffect> beingAttackedEffects = defendingMonsterCard.getBeingAttackedEffects();
        for (int i = 0; i < beingAttackedEffects.size(); i++) {
            System.out.println("being attacked effect are " + beingAttackedEffects.get(i));
        }
        if (beingAttackedEffects.contains(BeingAttackedEffect.SET_ATTACKING_MONSTER_ATK_TO_0_ONCE_PER_TURN) && doesDefendingMonsterEffectActivate) {
            attackingMonsterATK = 0;
        }
        if (attackingMonsterATK == defendingMonsterDEF) {
            doesAttackingMonsterGoToGraveyard = false;
            doesDefendingMonsterGoToGraveyard = false;
        } else if (attackingMonsterATK > defendingMonsterDEF) {
            doesAttackingMonsterGoToGraveyard = false;
            doesDefendingMonsterGoToGraveyard = true;
        } else {
            doesAttackingMonsterGoToGraveyard = false;
            doesDefendingMonsterGoToGraveyard = false;
            playersLifePointsChange.set(actionTurn - 1, attackingMonsterATK - defendingMonsterDEF);
            didAttackingUserReceiveDamage = true;
        }
        //reconsiderEffectsOfMonsterInBattle();
        return finishAttackConduction();
    }

    /*
        public static void reconsiderEffectsOfMonsterInBattle() {
            ArrayList<BeingAttackedEffect> beingAttackedEffects = defendingMonsterCard.getBeingAttackedEffects();
            if (beingAttackedEffects.contains(BeingAttackedEffect.NEITHER_PLAYER_RECEIVES_BATTLE_DAMAGE_IF_MONSTER_DIES) && doesDefendingMonsterGoToGraveyard) {
                playersLifePointsChange.set(0, 0);
                playersLifePointsChange.set(1, 0);
            }
            if (beingAttackedEffects.contains(BeingAttackedEffect.IF_DESTROYED_AND_SENT_TO_GRAVEYARD_SEND_ATTACKING_MONSTER_TO_GRAVEYARD)) {
                if (doesDefendingMonsterGoToGraveyard) {
                    doesAttackingMonsterGoToGraveyard = true;
                }
            }
            if (beingAttackedEffects.contains(BeingAttackedEffect.CANNOT_BE_DESTROYED_BY_BATTLE)) {
                if (doesDefendingMonsterGoToGraveyard) {
                    doesDefendingMonsterGoToGraveyard = false;
                }
            }
            if (beingAttackedEffects.contains(BeingAttackedEffect.IF_FACE_DOWN_AT_THE_BEGINNING_THEN_OPPONENT_RECEIVES_1000_DAMAGE) && isBeingAttackedMonsterFlipped) {
                playersLifePointsChange.set(2 - actionTurn, -1000);
            }
        }


     */
    public static void tendToNeitherPlayerReceivesBattleDamageIfMonsterDies() {
        if (beingAttackedEffectsForDefendingMonster.contains(BeingAttackedEffect.NEITHER_PLAYER_RECEIVES_BATTLE_DAMAGE_IF_MONSTER_DIES) && doesDefendingMonsterGoToGraveyard) {
            playersLifePointsChange.set(0, 0);
            playersLifePointsChange.set(1, 0);
        }
        if (beingAttackedEffectsForAttackingMonster.contains(BeingAttackedEffect.NEITHER_PLAYER_RECEIVES_BATTLE_DAMAGE_IF_MONSTER_DIES) && doesAttackingMonsterGoToGraveyard) {
            playersLifePointsChange.set(0, 0);
            playersLifePointsChange.set(1, 0);
        }
    }

    public static void tendToMonsterSendingOtherMonsterToGraveyardIfItselfIsDestroyed() {
        if (!beingAttackedEffectsForAttackingMonster.contains(BeingAttackedEffect.CANNOT_BE_DESTROYED_BY_BATTLE) &&
            beingAttackedEffectsForDefendingMonster.contains(BeingAttackedEffect.IF_DESTROYED_AND_SENT_TO_GRAVEYARD_SEND_ATTACKING_MONSTER_TO_GRAVEYARD) && doesDefendingMonsterGoToGraveyard) {
            doesAttackingMonsterEventuallyGoToGraveyard = true;
            //output += "\nbecause of your opponent's monster's effect, your monster is destroyed too.";
        }
        if (!beingAttackedEffectsForDefendingMonster.contains(BeingAttackedEffect.CANNOT_BE_DESTROYED_BY_BATTLE) &&
            beingAttackedEffectsForAttackingMonster.contains(BeingAttackedEffect.IF_DESTROYED_AND_SENT_TO_GRAVEYARD_SEND_ATTACKING_MONSTER_TO_GRAVEYARD) && doesAttackingMonsterGoToGraveyard) {
            doesDefendingMonsterEventuallyGoToGraveyard = true;
            //output += "\nbecause of your opponent's monster's effect, your monster is destroyed too.";
        }
    }

    public static String finishAttackConduction() {
        doesDefendingMonsterEventuallyGoToGraveyard = doesDefendingMonsterGoToGraveyard;
        doesAttackingMonsterEventuallyGoToGraveyard = doesAttackingMonsterGoToGraveyard;
        beingAttackedEffectsForDefendingMonster = defendingMonsterCard.getBeingAttackedEffects();
        beingAttackedEffectsForAttackingMonster = attackingMonsterCard.getBeingAttackedEffects();
        tendToNeitherPlayerReceivesBattleDamageIfMonsterDies();
        DuelController duelController = GameManager.getDuelControllerByIndex(0);
        if (beingAttackedEffectsForDefendingMonster.contains(BeingAttackedEffect.IF_FACE_DOWN_AT_THE_BEGINNING_THEN_OPPONENT_RECEIVES_1000_DAMAGE) && isBeingAttackedMonsterFlipped) {
            playersLifePointsChange.set(actionTurn - 1, playersLifePointsChange.get(actionTurn - 1) - 1000);
        }
        duelController.increaseLifePoints(playersLifePointsChange.get(0), 1);
        duelController.increaseLifePoints(playersLifePointsChange.get(1), 2);
        String output = "";
        output += tendToFaceUpAttackPositionMonsterLogicallyWinning();
        output += tendToFaceUpAttackPositionMonsterLogicallyEqual();
        output += tendToFaceUpAttackPositionMonsterLogicallyLost();
        output += tendToFaceUpDefensePositionMonsterLogicallyWinning();
        output += tendToFaceUpDefensePositionMonsterLogicallyEqual();
        output += tendToFaceUpDefensePositionMonsterLogicallyLost();

        //tendToMonsterSendingOtherMonsterToGraveyardIfItselfIsDestroyed();
        if (doesAttackingMonsterEventuallyGoToGraveyard) {
            SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(attackingMonsterCardLocation, 0);
            //sendCardToGraveyardAfterRemoving(attackingMonsterCardLocation, 0, actionTurn);
        }
        if (doesDefendingMonsterEventuallyGoToGraveyard) {
            //if (!beingAttackedEffectsForDefendingMonster.contains(BeingAttackedEffect.CANNOT_BE_DESTROYED_BY_BATTLE)) {
            SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(defendingMonsterCardLocation, 0);
            //}
            //sendCardToGraveyardAfterRemoving(defendingMonsterCardLocation, 0, actionTurn);
        }
        return output;
    }

    public static String tendToFaceUpDefensePositionMonsterLogicallyWinning() {
        System.out.println("tendToFaceUpDefensePositionMonsterLogicallyWinning");
        String output = "";
        if (defendingMonsterCard.getCardPosition().equals(CardPosition.FACE_UP_DEFENSE_POSITION) && !doesAttackingMonsterGoToGraveyard && doesDefendingMonsterGoToGraveyard) {
            if (isBeingAttackedMonsterFlipped) {
                output += "opponent's monster card was " + defendingMonsterCard.getCardName() + " and ";
            }
            if (beingAttackedEffectsForDefendingMonster.contains(BeingAttackedEffect.CANNOT_BE_DESTROYED_BY_BATTLE)) {
                output += "the defense position monster is not destroyed because of its effect";
                doesAttackingMonsterEventuallyGoToGraveyard = false;
                doesDefendingMonsterEventuallyGoToGraveyard = false;
            } else if (!beingAttackedEffectsForAttackingMonster.contains(BeingAttackedEffect.CANNOT_BE_DESTROYED_BY_BATTLE) &&
                beingAttackedEffectsForDefendingMonster.contains(BeingAttackedEffect.IF_DESTROYED_AND_SENT_TO_GRAVEYARD_SEND_ATTACKING_MONSTER_TO_GRAVEYARD)) {
                output += "the defense position monster is destroyed and your monster is destroyed because of opponent monster card's effect";
                doesAttackingMonsterEventuallyGoToGraveyard = true;
                doesDefendingMonsterEventuallyGoToGraveyard = true;
            } else {
                output += "the defense position monster is destroyed";
                doesAttackingMonsterEventuallyGoToGraveyard = true;
                doesDefendingMonsterEventuallyGoToGraveyard = true;
            }
        }
        return output;
    }

    public static String tendToFaceUpDefensePositionMonsterLogicallyEqual() {
        System.out.println("tendToFaceUpDefensePositionMonsterLogicallyEqual");
        String output = "";
        if (defendingMonsterCard.getCardPosition().equals(CardPosition.FACE_UP_DEFENSE_POSITION) && !doesAttackingMonsterGoToGraveyard && !doesDefendingMonsterGoToGraveyard && !didAttackingUserReceiveDamage) {
            if (isBeingAttackedMonsterFlipped) {
                output += "opponent's monster card was " + defendingMonsterCard.getCardName() + " and ";
            }
            output += "no card is destroyed";
            doesAttackingMonsterEventuallyGoToGraveyard = false;
            doesDefendingMonsterEventuallyGoToGraveyard = false;
        }
        return output;
    }

    public static String tendToFaceUpDefensePositionMonsterLogicallyLost() {
        System.out.println("tendToFaceUpDefensePositionMonsterLogicallyLost");
        String output = "";
        if (defendingMonsterCard.getCardPosition().equals(CardPosition.FACE_UP_DEFENSE_POSITION) && !doesAttackingMonsterGoToGraveyard && !doesDefendingMonsterGoToGraveyard && didAttackingUserReceiveDamage) {
            if (isBeingAttackedMonsterFlipped) {
                output += "opponent's monster card was " + defendingMonsterCard.getCardName() + " and ";
            }
            output += "no card is destroyed and you received " + playersLifePointsChange.get(actionTurn - 1) * (-1) + " battle damage";
            doesAttackingMonsterEventuallyGoToGraveyard = false;
            doesDefendingMonsterEventuallyGoToGraveyard = false;
        }
        return output;
    }

    public static String tendToFaceUpAttackPositionMonsterLogicallyWinning() {
        System.out.println("tendToFaceUpAttackPositionMonsterLogicallyWinning");
        String output = "";
        if (defendingMonsterCard.getCardPosition().equals(CardPosition.FACE_UP_ATTACK_POSITION) && !doesAttackingMonsterGoToGraveyard && doesDefendingMonsterGoToGraveyard) {
            if (beingAttackedEffectsForDefendingMonster.contains(BeingAttackedEffect.CANNOT_BE_DESTROYED_BY_BATTLE)) {
                output += "your opponent's monster is not destroyed because of its effect and your opponent receives " + playersLifePointsChange.get(2 - actionTurn) * (-1) + " battle damage";
                doesAttackingMonsterEventuallyGoToGraveyard = false;
                doesDefendingMonsterEventuallyGoToGraveyard = false;
            } else if (beingAttackedEffectsForDefendingMonster.contains(BeingAttackedEffect.IF_DESTROYED_AND_SENT_TO_GRAVEYARD_SEND_ATTACKING_MONSTER_TO_GRAVEYARD)
                && !beingAttackedEffectsForAttackingMonster.contains(BeingAttackedEffect.CANNOT_BE_DESTROYED_BY_BATTLE)) {
                output += "your opponent's monster is destroyed and your monster is destroyed because of your opponent monster card's effect " +
                    "and your opponent receives " + playersLifePointsChange.get(2 - actionTurn) * (-1) + " battle damage";
                doesAttackingMonsterEventuallyGoToGraveyard = true;
                doesDefendingMonsterEventuallyGoToGraveyard = true;
            } else {
                output += "your opponent's monster is destroyed and your opponent receives " + playersLifePointsChange.get(2 - actionTurn) * (-1) + " battle damage";
                doesAttackingMonsterEventuallyGoToGraveyard = false;
                doesDefendingMonsterEventuallyGoToGraveyard = true;
            }
        }
        return output;
    }

    public static String tendToFaceUpAttackPositionMonsterLogicallyEqual() {
        System.out.println("tendToFaceUpAttackPositionMonsterLogicallyEqual");
        String output = "";
        if (defendingMonsterCard.getCardPosition().equals(CardPosition.FACE_UP_ATTACK_POSITION) && doesAttackingMonsterGoToGraveyard && doesDefendingMonsterGoToGraveyard) {
            if (beingAttackedEffectsForAttackingMonster.contains(BeingAttackedEffect.CANNOT_BE_DESTROYED_BY_BATTLE) && beingAttackedEffectsForDefendingMonster.contains(BeingAttackedEffect.CANNOT_BE_DESTROYED_BY_BATTLE)) {
                output += "no card is destroyed because of the card's effects and no one receives damage";
                doesAttackingMonsterEventuallyGoToGraveyard = false;
                doesDefendingMonsterEventuallyGoToGraveyard = false;
            } else if (beingAttackedEffectsForAttackingMonster.contains(BeingAttackedEffect.CANNOT_BE_DESTROYED_BY_BATTLE) && !beingAttackedEffectsForDefendingMonster.contains(BeingAttackedEffect.CANNOT_BE_DESTROYED_BY_BATTLE)) {
                output += "your opponent's monster is destroyed and your monster is not destroyed because of its effect and no one receives damage";
                doesAttackingMonsterEventuallyGoToGraveyard = false;
                doesDefendingMonsterEventuallyGoToGraveyard = true;
            } else if (!beingAttackedEffectsForAttackingMonster.contains(BeingAttackedEffect.CANNOT_BE_DESTROYED_BY_BATTLE) && beingAttackedEffectsForDefendingMonster.contains(BeingAttackedEffect.CANNOT_BE_DESTROYED_BY_BATTLE)) {
                output += "your opponent's monster is not destroyed because of its effect and your monster is destroyed and no one receives damage";
                doesAttackingMonsterEventuallyGoToGraveyard = true;
                doesDefendingMonsterEventuallyGoToGraveyard = false;
            } else if (!beingAttackedEffectsForAttackingMonster.contains(BeingAttackedEffect.CANNOT_BE_DESTROYED_BY_BATTLE) && !beingAttackedEffectsForDefendingMonster.contains(BeingAttackedEffect.CANNOT_BE_DESTROYED_BY_BATTLE)) {
                output += "both you and your opponent monster cards are destroyed and no one receives damage";
                doesAttackingMonsterEventuallyGoToGraveyard = true;
                doesDefendingMonsterEventuallyGoToGraveyard = true;
            }
        }
        return output;
    }

    public static String tendToFaceUpAttackPositionMonsterLogicallyLost() {
        System.out.println("tendToFaceUpAttackPositionMonsterLogicallyLost");
        String output = "";
        if (defendingMonsterCard.getCardPosition().equals(CardPosition.FACE_UP_ATTACK_POSITION) && doesAttackingMonsterGoToGraveyard && !doesDefendingMonsterGoToGraveyard) {
            if (beingAttackedEffectsForAttackingMonster.contains(BeingAttackedEffect.CANNOT_BE_DESTROYED_BY_BATTLE)) {
                output += "your monster card is not destroyed because of its effect and you received " + playersLifePointsChange.get(actionTurn - 1) * (-1) + " battle damage";
                doesAttackingMonsterEventuallyGoToGraveyard = false;
                doesDefendingMonsterEventuallyGoToGraveyard = false;
            } else if (beingAttackedEffectsForAttackingMonster.contains(BeingAttackedEffect.IF_DESTROYED_AND_SENT_TO_GRAVEYARD_SEND_ATTACKING_MONSTER_TO_GRAVEYARD) &&
                !beingAttackedEffectsForDefendingMonster.contains(BeingAttackedEffect.CANNOT_BE_DESTROYED_BY_BATTLE)) {
                output += "both you and your opponent monster cards are destroyed because of your monster card's effect " +
                    "and you received " + playersLifePointsChange.get(actionTurn - 1) * (-1) + " battle damage";
                doesAttackingMonsterEventuallyGoToGraveyard = true;
                doesDefendingMonsterEventuallyGoToGraveyard = true;
            } else {
                output += "your monster card is destroyed and you received " + playersLifePointsChange.get(actionTurn - 1) * (-1) + " battle damage";
                doesAttackingMonsterEventuallyGoToGraveyard = true;
                doesDefendingMonsterEventuallyGoToGraveyard = false;
            }
        }
        return output;
    }

}
