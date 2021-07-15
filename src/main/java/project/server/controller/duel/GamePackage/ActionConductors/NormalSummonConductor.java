package project.server.controller.duel.GamePackage.ActionConductors;

import java.util.ArrayList;
import java.util.regex.Matcher;

import project.model.MonsterEffectEnums.UponSummoningEffect;
import project.server.controller.duel.GamePackage.Action;
import project.model.ActionType;
import project.server.controller.duel.GamePackage.DuelBoard;
import project.server.controller.duel.GamePackage.DuelController;
import project.server.controller.duel.GamePhaseControllers.SelectCardController;
import project.server.controller.duel.PreliminaryPackage.GameManager;
import project.model.Utility.Utility;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.CardPosition;
import project.model.cardData.General.RowOfCardLocation;
import project.model.cardData.MonsterCardData.MonsterCard;
import project.model.cardData.MonsterCardData.MonsterCardValue;

public class NormalSummonConductor {
    private static boolean isActionCanceled = false;
    private static boolean isClassWaitingForPlayerToPickMonsterToSpecialSummon = false;
    private static boolean shouldRedirectConductorToAvoidRepetition = false;
    private static boolean promptingUserToActivateMonsterEffect = false;
    private static CardLocation monsterCardToBeNormalSummoned;
    private static int actionTurn;


    public static boolean isPromptingUserToActivateMonsterEffect() {
        return promptingUserToActivateMonsterEffect;
    }

    public static boolean isIsClassWaitingForPlayerToPickMonsterToSpecialSummon() {
        return isClassWaitingForPlayerToPickMonsterToSpecialSummon;
    }

    public static String conductNormalSummoningActionUninterruptedAction(String token, int numberInListOfActions) {
        //if (!isActionCanceled){
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(token);
        Action uninterruptedAction = uninterruptedActions.get(numberInListOfActions);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        for (int i = 0; i < uninterruptedAction.getSpendingCards().size(); i++) {
            SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(uninterruptedAction.getSpendingCards().get(i), token);
        }
        int turn = 0;
        CardLocation superFinalCardLocation = null;
        if (uninterruptedAction.getActionType().equals(ActionType.ALLY_NORMAL_SUMMONING_MONSTER)) {
            turn = 1;
            superFinalCardLocation = new CardLocation(duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.ALLY_MONSTER_ZONE, true).getRowOfCardLocation(),
                duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.ALLY_MONSTER_ZONE, true).getIndex() + 1);
            uninterruptedAction.setFinalMainCardLocation(superFinalCardLocation);
        } else if (uninterruptedAction.getActionType().equals(ActionType.OPPONENT_NORMAL_SUMMONING_MONSTER)) {
            turn = 2;
            superFinalCardLocation = new CardLocation(duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.OPPONENT_MONSTER_ZONE, false).getRowOfCardLocation(),
                duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.OPPONENT_MONSTER_ZONE, false).getIndex() + 1);
            uninterruptedAction.setFinalMainCardLocation(superFinalCardLocation);
        }
        Card mainCard = duelBoard.getCardByCardLocation(uninterruptedAction.getMainCardLocation());
        duelBoard.removeCardByCardLocation(uninterruptedAction.getMainCardLocation());
        //GameManager.getDuelControllerByIndex(index).addStringToAvailableCardLocationForUseForClient(superFinalCardLocation);
        duelBoard.addCardToMonsterZone(mainCard, turn, token);
        GameManager.getDuelControllerByIndex(token).addStringToSuperAlmightyString("mainCardLocation " + uninterruptedAction.getMainCardLocation().getRowOfCardLocation()
            + " " + uninterruptedAction.getMainCardLocation().getIndex() + " is being added to monster zone " + turn + " and should finally be FACE_UP_ATTACK_POSITION", token);
        mainCard.setCardPosition(CardPosition.FACE_UP_ATTACK_POSITION);
        MonsterCard monsterCard = (MonsterCard) mainCard;
        monsterCard.setCardPositionChanged(true);
        GameManager.getDuelControllerByIndex(token).setCanUserSummonOrSetMonsters(uninterruptedAction.getActionTurn(), false);
        return "summoned successfully";
    }

    public static String conductNormalSummoningAction(String token, int numberInListOfActions) {
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(token);
        Action uninterruptedAction = uninterruptedActions.get(numberInListOfActions);
        ArrayList<Action> actions = GameManager.getActionsByIndex(token);
        Action action = actions.get(numberInListOfActions);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        if (!action.isActionCanceled()) {
            if (shouldRedirectConductorToAvoidRepetition) {
                shouldRedirectConductorToAvoidRepetition = false;
                return "";
            }
            CardLocation mainCardLocation = uninterruptedAction.getFinalMainCardLocation();
            Card mainCard = duelBoard.getCardByCardLocation(mainCardLocation);
            MonsterCard monsterCard = (MonsterCard) mainCard;
            ArrayList<UponSummoningEffect> uponSummoningEffects = monsterCard.getUponSummoningEffects();
            if (uponSummoningEffects.contains(UponSummoningEffect.SET_ATK_1900_IF_NORMAL_SUMMONED)) {
                monsterCard.setAttackPower(1900);
            }
            if (uponSummoningEffects.contains(UponSummoningEffect.IF_NORMAL_SUMMONED_SPECIAL_SUMMON_1_LEVEL_4_OR_LESS_NORMAL_MONSTER_FROM_HAND_IN_DEFENSE_POSITION)) {
                if (doesPlayerHaveLevel4OrLessNormalMonsterInHand(action.getActionTurn(), token)) {
                    shouldRedirectConductorToAvoidRepetition = true;
                    promptingUserToActivateMonsterEffect = true;
                    monsterCardToBeNormalSummoned = mainCardLocation;
                    actionTurn = action.getActionTurn();
                    return "do you want to activate your monster card's effect?";
                }
            }
        }
        return "";
    }

    public static boolean doesPlayerHaveLevel4OrLessNormalMonsterInHand(int actionTurn, String token) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        ArrayList<Card> cardsInHand = null;
        if (actionTurn == 1) {
            cardsInHand = duelBoard.getAllyCardsInHand();
        } else {
            cardsInHand = duelBoard.getOpponentCardsInHand();
        }
        for (int i = 0; i < cardsInHand.size(); i++) {
            if (Card.isCardAMonster(cardsInHand.get(i))) {
                MonsterCard monsterCard = (MonsterCard) cardsInHand.get(i);
                if (monsterCard.getMonsterCardValue().equals(MonsterCardValue.NORMAL) && monsterCard.getLevel() <= 4) {
                    if (!(duelBoard.isZoneFull(RowOfCardLocation.ALLY_MONSTER_ZONE) && actionTurn == 1 ||
                        duelBoard.isZoneFull(RowOfCardLocation.OPPONENT_MONSTER_ZONE) && actionTurn == 2)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String normalSummonedMonsterEffectAnalysis(String string,String token) {
        String inputRegex = "(?<=\\n|^)no(?=\\n|$)";
        Matcher matcher = Utility.getCommandMatcher(string, inputRegex);
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        if (Utility.isMatcherCorrectWithoutErrorPrinting(matcher)) {
            promptingUserToActivateMonsterEffect = false;
        } else {
            String anotherRegex = "(?<=\\n|^)yes(?=\\n|$)";
            Matcher newMatcher = Utility.getCommandMatcher(string, anotherRegex);
            if (Utility.isMatcherCorrectWithoutErrorPrinting(newMatcher)) {
                DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
                MonsterCard mainMonsterCard = (MonsterCard) duelBoard.getCardByCardLocation(monsterCardToBeNormalSummoned);
                mainMonsterCard.setOncePerTurnCardEffectUsed(true);
                promptingUserToActivateMonsterEffect = false;
                //isClassWaitingForFurtherChainInput = true;
                ArrayList<UponSummoningEffect> uponSummoningEffects = mainMonsterCard.getUponSummoningEffects();
                if (uponSummoningEffects.contains(UponSummoningEffect.IF_NORMAL_SUMMONED_SPECIAL_SUMMON_1_LEVEL_4_OR_LESS_NORMAL_MONSTER_FROM_HAND_IN_DEFENSE_POSITION)) {
                    isClassWaitingForPlayerToPickMonsterToSpecialSummon = true;
                    return "select one normal monster with level at most 4 from your hand to special summon in defense position\n";
                }
            }
        }
        return Action.conductAllActions(token);
    }

    public static String checkGivenInputForMonsterToSpecialSummon(String token) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        CardLocation cardLocation = selectedCardLocations.get(selectedCardLocations.size() - 1);
        Card cardToBeSpecialSummoned = duelBoard.getCardByCardLocation(cardLocation);
        if (Card.isCardATrap(cardToBeSpecialSummoned) || Card.isCardASpell(cardToBeSpecialSummoned)) {
            return "this card cannot be chosen for special summoning.\nselect another card\n";
        } else if (Card.isCardAMonster(cardToBeSpecialSummoned) && !((MonsterCard) cardToBeSpecialSummoned).getMonsterCardValue().equals(MonsterCardValue.NORMAL)) {
            return "this is not a normal monster.\nselect another card\n";
        } else if (Card.isCardAMonster(cardToBeSpecialSummoned) && !(((MonsterCard) cardToBeSpecialSummoned).getLevel() <= 4)) {
            return "this monster's level is more than 4.\nselect another card\n";
        } else if (Card.isCardAMonster(cardToBeSpecialSummoned) &&
            !(cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_HAND_ZONE) && actionTurn == 1 ||
                cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_HAND_ZONE) && actionTurn == 2)) {
            return "this card is not in your hand.\nselect another card";
        } else {
            isClassWaitingForPlayerToPickMonsterToSpecialSummon = false;
            SendCardToGraveyardConductor.removeCardAndGetRemovedCard(cardLocation, token);
            duelBoard.addCardToMonsterZone(cardToBeSpecialSummoned, actionTurn, token);

            GameManager.getDuelControllerByIndex(token).addStringToSuperAlmightyString("mainCardLocation " + cardLocation.getRowOfCardLocation()
                + " " + cardLocation.getIndex() + " is being added to monster zone " + actionTurn + " and should finally be FACE_UP_DEFENSE_POSITION", token);


            cardToBeSpecialSummoned.setCardPosition(CardPosition.FACE_UP_DEFENSE_POSITION);
            return "monster special summoned successfully\n" + Action.conductAllActions(token);
        }
    }
}
