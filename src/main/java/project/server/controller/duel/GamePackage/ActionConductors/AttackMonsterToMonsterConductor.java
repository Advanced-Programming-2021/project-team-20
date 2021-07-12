package project.server.controller.duel.GamePackage.ActionConductors;

import java.util.ArrayList;
import java.util.regex.Matcher;

import project.model.MonsterEffectEnums.BeingAttackedEffect;
import project.model.MonsterEffectEnums.FlipEffect;
import project.server.controller.duel.GamePackage.Action;
import project.model.ActionType;
import project.server.controller.duel.GamePackage.DuelBoard;
import project.server.controller.duel.GamePackage.DuelController;
import project.server.controller.duel.GamePhaseControllers.ChainController;
import project.server.controller.duel.GamePhaseControllers.SelectCardController;
import project.server.controller.duel.GamePhaseControllers.SpecialSummonController;
import project.server.controller.duel.PreliminaryPackage.GameManager;
import project.server.controller.duel.Utility.Utility;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.CardPosition;
import project.model.cardData.General.RowOfCardLocation;
import project.model.cardData.MonsterCardData.MonsterCard;
import project.model.cardData.MonsterCardData.MonsterCardFamily;
import project.model.cardData.MonsterCardData.MonsterCardValue;

public class AttackMonsterToMonsterConductor extends ChainController {
    //private int indexOfClass;
    private int numberInListOfActionsOfClass;
    //private boolean isAttackCanceled;
    private int attackingMonsterATK;
    private int attackingMonsterDEF;
    private int defendingMonsterATK;
    private int defendingMonsterDEF;
    private CardLocation attackingMonsterCardLocation;
    private CardLocation defendingMonsterCardLocation;
    private MonsterCard attackingMonsterCard;
    private MonsterCard defendingMonsterCard;
    //private ArrayList<BeingAttackedEffect> beingAttackedEffectsForAttackingMonster;
    //private ArrayList<BeingAttackedEffect> beingAttackedEffectsForDefendingMonster;
    private boolean promptingUserToActivateMonsterEffect;
    private boolean isBeingAttackedMonsterFlipped;
    private boolean isClassWaitingForPlayerToPickMonsterToSpecialSummon;
    private boolean isClassWaitingForUserToChooseAttackPositionOrDefensePosition;
    private boolean isClassWaitingForPlayerToPickMonsterToDestroy;
    private boolean doesDefendingMonsterEffectActivate;
    private boolean playerIsSupposedToPickMonsterToSpecialSummon;
    private int actionTurn;
    private boolean doesAttackingMonsterGoToGraveyard;
    private boolean doesDefendingMonsterGoToGraveyard;
    private boolean doesDefendingMonsterEventuallyGoToGraveyard;
    private boolean doesAttackingMonsterEventuallyGoToGraveyard;
    private ArrayList<Integer> playersLifePointsChange;
    private boolean didAttackingUserReceiveDamage;
    private boolean isConductBattleNegatedBecauseOfMonsterEffect;
    private boolean shouldRedirectAttackConductorFunctionToAvoidRepetition;
    private CardLocation cardToBeSpecialSummoned;
    private CardPosition cardPositionToBeSpecialSummoned;

    public AttackMonsterToMonsterConductor() {
        isBeingAttackedMonsterFlipped = false;
        playerIsSupposedToPickMonsterToSpecialSummon = false;
        isClassWaitingForPlayerToPickMonsterToSpecialSummon = false;
        isClassWaitingForPlayerToPickMonsterToDestroy = false;
        isClassWaitingForUserToChooseAttackPositionOrDefensePosition = false;
        doesDefendingMonsterEffectActivate = false;
        didAttackingUserReceiveDamage = false;
        shouldRedirectAttackConductorFunctionToAvoidRepetition = false;
        isConductBattleNegatedBecauseOfMonsterEffect = false;
        playersLifePointsChange = new ArrayList<>();
    }

    public boolean isPromptingUserToActivateMonsterEffect() {
        return promptingUserToActivateMonsterEffect;
    }

    public boolean isClassWaitingForPlayerToPickMonsterToSpecialSummon() {
        return isClassWaitingForPlayerToPickMonsterToSpecialSummon;
    }

    public boolean isClassWaitingForUserToChooseAttackPositionOrDefensePosition() {
        return isClassWaitingForUserToChooseAttackPositionOrDefensePosition;
    }

    public boolean isClassWaitingForPlayerToPickMonsterToDestroy() {
        return isClassWaitingForPlayerToPickMonsterToDestroy;
    }

    public String AttackConductor(String token, int numberInListOfActions) {
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        //indexOfClass = token;
        numberInListOfActionsOfClass = numberInListOfActions;
        ArrayList<Action> actions = GameManager.getActionsByIndex(token);
        Action action = actions.get(numberInListOfActions);
        if (shouldRedirectAttackConductorFunctionToAvoidRepetition) {
            shouldRedirectAttackConductorFunctionToAvoidRepetition = false;
            if (action.isActionCanceled()) {
                attackingMonsterCard.setHasCardAlreadyAttacked(true);
                return "action was canceled";
            }
            if (playerIsSupposedToPickMonsterToSpecialSummon) {
                playerIsSupposedToPickMonsterToSpecialSummon = false;
                isClassWaitingForPlayerToPickMonsterToSpecialSummon = true;
                return "show hand, graveyard, deck.\nselect one cyberse normal monster to special summon";
            }
            if (isConductBattleNegatedBecauseOfMonsterEffect) {
                attackingMonsterCard.setHasCardAlreadyAttacked(true);
                isConductBattleNegatedBecauseOfMonsterEffect = false;
                return "action was canceled because of monster card's effect";
            }
            return conductBattle(token);
        }
        if (!action.isActionCanceled()) {
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
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
                //System.out.println("FFFFFFF");
                GameManager.getDuelControllerByIndex(token).addStringToSuperAlmightyString("mainCardLocation " + defendingMonsterCardLocation.getRowOfCardLocation()
                    + " " + defendingMonsterCardLocation.getIndex() + " is being added to monster zone " + (3-actionTurn) + " and should finally be FACE_UP_DEFENSE_POSITION");

                defendingMonsterCard.setCardPosition(CardPosition.FACE_UP_DEFENSE_POSITION);
                isBeingAttackedMonsterFlipped = true;
                ArrayList<FlipEffect> defendingMonsterFlipEffect = defendingMonsterCard.getFlipEffects();
                if (defendingMonsterFlipEffect.contains(FlipEffect.DESTROY_1_MONSTER_ON_THE_FIELD)) {
                    promptingUserToActivateMonsterEffect = true;
                    shouldRedirectAttackConductorFunctionToAvoidRepetition = true;
                    duelController.changeFakeTurn();
                    int fakeTurn = duelController.getFakeTurn();
                    String usernameInChain = duelController.getPlayingUsernameByTurn(fakeTurn);
                    return "now it will be " + usernameInChain + "'s turn" + "\ndo you want to activate your monster card's effect?";
                }
            }
            if (doesDefendingMonsterHaveEffect(defendingMonsterCard) && !defendingMonsterCard.isOncePerTurnCardEffectUsed()) {
                promptingUserToActivateMonsterEffect = true;
                shouldRedirectAttackConductorFunctionToAvoidRepetition = true;
                duelController.changeFakeTurn();
                int fakeTurn = duelController.getFakeTurn();
                String usernameInChain = duelController.getPlayingUsernameByTurn(fakeTurn);
                return "now it will be " + usernameInChain + "'s turn" + "\ndo you want to activate your monster card's effect?";
            }
            return conductBattle(token);
        } else {
            if (attackingMonsterCard != null){
                attackingMonsterCard.setHasCardAlreadyAttacked(true);
            }
        }
        return "action was canceled.";
    }


    public String defendingMonsterEffectAnalysis(String string, String token) {
        String inputRegex = "(?<=\\n|^)no(?=\\n|$)";
        Matcher matcher = Utility.getCommandMatcher(string, inputRegex);
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        int fakeTurn = duelController.getFakeTurn();
        if (Utility.isMatcherCorrectWithoutErrorPrinting(matcher)) {
            promptingUserToActivateMonsterEffect = false;
            duelController.changeFakeTurn();
        } else {
            String anotherRegex = "(?<=\\n|^)yes(?=\\n|$)";
            Matcher newMatcher = Utility.getCommandMatcher(string, anotherRegex);
            if (Utility.isMatcherCorrectWithoutErrorPrinting(newMatcher)) {
                defendingMonsterCard.setOncePerTurnCardEffectUsed(true);
                promptingUserToActivateMonsterEffect = false;
                //isClassWaitingForFurtherChainInput = true;
                doesDefendingMonsterEffectActivate = true;
                ArrayList<BeingAttackedEffect> beingAttackedEffects = defendingMonsterCard.getBeingAttackedEffects();
                if (beingAttackedEffects.contains(BeingAttackedEffect.NEGATE_ATTACK_ONCE_PER_TURN) && beingAttackedEffects.contains(BeingAttackedEffect.SPECIAL_SUMMON_CYBERSE_NORMAL_MONSTER_FROM_HAND_GV_DECK_ONCE_PER_TURN)) {
                    isConductBattleNegatedBecauseOfMonsterEffect = true;
                    if (canPlayerSpecialSummonCyberseMonsterFromHandDeckOrGraveyard(token)) {
                        String canChainingOccur = canChainingOccur(token, fakeTurn, ActionType.ALLY_MONSTER_ATTACKING_OPPONENT_MONSTER, ActionType.OPPONENT_MONSTER_ATTACKING_ALLY_MONSTER);
                        if (!canChainingOccur.equals("")) {
                            playerIsSupposedToPickMonsterToSpecialSummon = true;
                            return canChainingOccur;
                        }
                        isClassWaitingForPlayerToPickMonsterToSpecialSummon = true;
                        return "show hand, graveyard, deck.\nselect one cyberse normal monster to special summon";
                    }
                }
                ArrayList<FlipEffect> flipEffects = defendingMonsterCard.getFlipEffects();
                if (isBeingAttackedMonsterFlipped && flipEffects.contains(FlipEffect.DESTROY_1_MONSTER_ON_THE_FIELD)) {
                    isClassWaitingForPlayerToPickMonsterToDestroy = true;
                    return "select one monster on the field to destroy";
                }
                if (beingAttackedEffects.contains(BeingAttackedEffect.SET_ATTACKING_MONSTER_ATK_TO_0_ONCE_PER_TURN)) {
                    //defendingMonsterCard.setOncePerTurnCardEffectUsed(true);
                    duelController.changeFakeTurn();
                }
            }
        }
        return Action.conductAllActions(token);
    }

    public boolean canPlayerSpecialSummonCyberseMonsterFromHandDeckOrGraveyard(String token) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        ArrayList<Card> deckCards;
        ArrayList<Card> handCards;
        ArrayList<Card> graveyardCards;
        if (defendingMonsterCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) {
            deckCards = duelBoard.getOpponentCardsInDeck();
            handCards = duelBoard.getOpponentCardsInHand();
            graveyardCards = duelBoard.getOpponentCardsInGraveyard();
        } else {
            deckCards = duelBoard.getAllyCardsInDeck();
            handCards = duelBoard.getAllyCardsInHand();
            graveyardCards = duelBoard.getAllyCardsInGraveyard();
        }
        return !(duelBoard.isZoneFull(RowOfCardLocation.ALLY_MONSTER_ZONE) && actionTurn == 1 || duelBoard.isZoneFull(RowOfCardLocation.OPPONENT_MONSTER_ZONE) && actionTurn == 2) &&
            (doesArrayListContainCyberseMonsterCard(handCards) || doesArrayListContainCyberseMonsterCard(deckCards) ||
                doesArrayListContainCyberseMonsterCard(graveyardCards));
    }

    public boolean doesArrayListContainCyberseMonsterCard(ArrayList<Card> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i) != null) {
                if (Card.isCardAMonster(arrayList.get(i)) && ((MonsterCard) arrayList.get(i)).getMonsterCardFamily().equals(MonsterCardFamily.CYBERSE)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String checkGivenInputForMonsterToDestroy(String token) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        CardLocation cardLocation = selectedCardLocations.get(selectedCardLocations.size() - 1);
        Card card = duelBoard.getCardByCardLocation(cardLocation);
        if (Card.isCardATrap(card) || Card.isCardASpell(card)) {
            return "this card cannot be chosen for destroying.\nselect another card";
        } else if (Card.isCardAMonster(card) &&
            !(cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE) || cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE))) {
            return "you can't select this monster for destroying.\nselect another card";
        } else {
            SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(cardLocation, token);
            Card attackingMonsterCardChecking = duelBoard.getCardByCardLocation(attackingMonsterCardLocation);
            Card defendingMonsterCardChecking = duelBoard.getCardByCardLocation(defendingMonsterCardLocation);
            if (attackingMonsterCardChecking == null || defendingMonsterCardChecking == null) {
                ArrayList<Action> actions = GameManager.getActionsByIndex(token);
                Action action = actions.get(numberInListOfActionsOfClass);
                action.setActionCanceled(true);
            }
            isClassWaitingForPlayerToPickMonsterToDestroy = false;
            DuelController duelController = GameManager.getDuelControllerByIndex(token);
            duelController.changeFakeTurn();
            return "monster destroyed successfully\n" + Action.conductAllActions(token);
        }
    }


    public String checkGivenInputForMonsterEffectActivation(String token) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        CardLocation cardLocation = selectedCardLocations.get(selectedCardLocations.size() - 1);
        Card card = duelBoard.getCardByCardLocation(cardLocation);
        if (Card.isCardATrap(card) || Card.isCardASpell(card)) {
            return "this card cannot be chosen for special summon.";
        } else if (Card.isCardAMonster(card) && !((MonsterCard) card).getMonsterCardValue().equals(MonsterCardValue.NORMAL)) {
            return "this is not a normal monster.\nselect another monster.";
        } else if (Card.isCardAMonster(card) && !((MonsterCard) card).getMonsterCardFamily().equals(MonsterCardFamily.CYBERSE)) {
            return "this monster is not from cyberse family.\nselect another monster.";
        } else {
            DuelController duelController = GameManager.getDuelControllerByIndex(token);
            int fakeTurn = duelController.getFakeTurn();
            RowOfCardLocation rowOfCardLocation = cardLocation.getRowOfCardLocation();
            if (fakeTurn == 1 && !rowOfCardLocation.equals(RowOfCardLocation.ALLY_HAND_ZONE) && !rowOfCardLocation.equals(RowOfCardLocation.ALLY_DECK_ZONE) && !rowOfCardLocation.equals(RowOfCardLocation.ALLY_GRAVEYARD_ZONE)) {
                return "selected card is neither in your hand, deck, or graveyard.\nselect another card";
            } else if (fakeTurn == 2 && !rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_HAND_ZONE) && !rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_DECK_ZONE) && !rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE)) {
                return "selected card is neither in your hand, deck, or graveyard.\nselect another card";
            } else {
                cardToBeSpecialSummoned = cardLocation;
                isClassWaitingForPlayerToPickMonsterToSpecialSummon = false;
                isClassWaitingForUserToChooseAttackPositionOrDefensePosition = true;
                return "choose if you want to special summon your monster in face up attack position or face up defense position\nenter attacking or defensive";
                /*
                duelBoard.removeCardByCardLocation(cardLocation);
                duelBoard.addCardToMonsterZone(card, actionTurn);
                defendingMonsterCard.setOncePerTurnCardEffectUsed(true);
                return Action.conductAllActions(index);

                 */
            }
        }
    }

    public String redirectInputForAnalyzingAttackPositionOrDefensePosition(String string, String token) {
        String output = "";
        String canChainingOccur = "";
        SpecialSummonController specialSummonController = GameManager.getSpecialSummonControllerByIndex(token);
        ArrayList<Action> actions = GameManager.getActionsByIndex(token);
        if (string.equals("attacking")) {
            isClassWaitingForUserToChooseAttackPositionOrDefensePosition = false;
            cardPositionToBeSpecialSummoned = CardPosition.FACE_UP_ATTACK_POSITION;
            createActionForSpecialSummoningMonster(token);
            output = Action.conductUninterruptedAction(token);
            Action action = actions.get(actions.size() - 1);
            int actionTurn = action.getActionTurn();
            canChainingOccur = specialSummonController.canChainingOccur(token, actionTurn, ActionType.ALLY_SPECIAL_SUMMONING_MONSTER, ActionType.OPPONENT_SPECIAL_SUMMONING_MONSTER);
        } else if (string.equals("defensive")) {
            isClassWaitingForUserToChooseAttackPositionOrDefensePosition = false;
            cardPositionToBeSpecialSummoned = CardPosition.FACE_UP_DEFENSE_POSITION;
            createActionForSpecialSummoningMonster(token);
            output = Action.conductUninterruptedAction(token);
            Action action = actions.get(actions.size() - 1);
            int actionTurn = action.getActionTurn();
            canChainingOccur = specialSummonController.canChainingOccur(token, actionTurn, ActionType.ALLY_SPECIAL_SUMMONING_MONSTER, ActionType.OPPONENT_SPECIAL_SUMMONING_MONSTER);
        }
        if (!isClassWaitingForUserToChooseAttackPositionOrDefensePosition) {
            if (!canChainingOccur.equals("")) {
                output += canChainingOccur;
                return output;
            }
            return output + Action.conductAllActions(token);
        }
        return "invalid input\nplease enter attacking or defensive";
    }

    public void createActionForSpecialSummoningMonster(String token) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        int fakeTurn = duelController.getFakeTurn();
        selectCardController.resetSelectedCardLocationList();
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(token);
        ArrayList<Action> actions = GameManager.getActionsByIndex(token);
        if (fakeTurn == 1) {
            uninterruptedActions.add(new Action(ActionType.ALLY_SPECIAL_SUMMONING_MONSTER, 1, cardToBeSpecialSummoned, null, null, null, null, null, null, null, null, null, null, cardPositionToBeSpecialSummoned, "", null));
            actions.add(new Action(ActionType.ALLY_SPECIAL_SUMMONING_MONSTER, 1, cardToBeSpecialSummoned, null, null, null, null, null, null, null, null, null, null, cardPositionToBeSpecialSummoned, "", null));
            //add action that conducts effects of the card
        } else if (fakeTurn == 2) {
            uninterruptedActions.add(new Action(ActionType.OPPONENT_SPECIAL_SUMMONING_MONSTER, 2, cardToBeSpecialSummoned, null, null, null, null, null, null, null, null, null, null, cardPositionToBeSpecialSummoned, "", null));
            actions.add(new Action(ActionType.OPPONENT_SPECIAL_SUMMONING_MONSTER, 2, cardToBeSpecialSummoned, null, null, null, null, null, null, null, null, null, null, cardPositionToBeSpecialSummoned, "", null));
            //add action that conducts effects of the card
        }
        cardToBeSpecialSummoned = null;
        cardPositionToBeSpecialSummoned = null;
    }

    public boolean doesDefendingMonsterHaveEffect(MonsterCard defendingMonsterCard) {
        ArrayList<BeingAttackedEffect> beingAttackedEffectsForDefendingMonster = defendingMonsterCard.getBeingAttackedEffects();
        if (beingAttackedEffectsForDefendingMonster.contains(BeingAttackedEffect.NEGATE_ATTACK_ONCE_PER_TURN) &&
            beingAttackedEffectsForDefendingMonster.contains(BeingAttackedEffect.SPECIAL_SUMMON_CYBERSE_NORMAL_MONSTER_FROM_HAND_GV_DECK_ONCE_PER_TURN)) {
            return true;
        }
        if (beingAttackedEffectsForDefendingMonster.contains(BeingAttackedEffect.SET_ATTACKING_MONSTER_ATK_TO_0_ONCE_PER_TURN)) {
            return true;
        }
        return false;
    }

    public String conductBattle(String token) {
        ArrayList<Action> actions = GameManager.getActionsByIndex(token);
        Action action = actions.get(numberInListOfActionsOfClass);
        if (!isConductBattleNegatedBecauseOfMonsterEffect && !action.isActionCanceled()) {
            if (defendingMonsterCard.getCardPosition().equals(CardPosition.FACE_UP_ATTACK_POSITION)) {
                String output = faceUpAttackPositionMonsterToFaceUpAttackPositionMonster(token);
                resetVariables();
                return output;
            }
            String output = faceUpAttackPositionMonsterToFaceUpDefensePositionMonster(token);
            resetVariables();
            return output;
        }
        resetVariables();
        return "attack was negated because of monster card effect";
    }

    private void resetVariables() {
        attackingMonsterATK = 0;
        attackingMonsterDEF = 0;
        defendingMonsterATK = 0;
        defendingMonsterDEF = 0;
        isBeingAttackedMonsterFlipped = false;
        playerIsSupposedToPickMonsterToSpecialSummon = false;
        isClassWaitingForPlayerToPickMonsterToSpecialSummon = false;
        isClassWaitingForUserToChooseAttackPositionOrDefensePosition = false;
        isClassWaitingForPlayerToPickMonsterToDestroy = false;
        doesDefendingMonsterEffectActivate = false;
        didAttackingUserReceiveDamage = false;
        shouldRedirectAttackConductorFunctionToAvoidRepetition = false;
        isConductBattleNegatedBecauseOfMonsterEffect = false;
        playersLifePointsChange.clear();
    }

    public String faceUpAttackPositionMonsterToFaceUpAttackPositionMonster(String token) {
        attackingMonsterATK = MonsterCard.giveATKDEFConsideringEffects("attack", attackingMonsterCardLocation, token);
        defendingMonsterATK = MonsterCard.giveATKDEFConsideringEffects("attack", defendingMonsterCardLocation, token);
        ArrayList<BeingAttackedEffect> beingAttackedEffects = defendingMonsterCard.getBeingAttackedEffects();
        if (beingAttackedEffects.contains(BeingAttackedEffect.SET_ATTACKING_MONSTER_ATK_TO_0_ONCE_PER_TURN) && doesDefendingMonsterEffectActivate) {
            doesDefendingMonsterEffectActivate = false;
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
        return finishAttackConduction(token);
    }

    public String faceUpAttackPositionMonsterToFaceUpDefensePositionMonster(String token) {
        attackingMonsterATK = MonsterCard.giveATKDEFConsideringEffects("attack", attackingMonsterCardLocation, token);
        defendingMonsterDEF = MonsterCard.giveATKDEFConsideringEffects("defense", defendingMonsterCardLocation, token);
        ArrayList<BeingAttackedEffect> beingAttackedEffects = defendingMonsterCard.getBeingAttackedEffects();
        if (beingAttackedEffects.contains(BeingAttackedEffect.SET_ATTACKING_MONSTER_ATK_TO_0_ONCE_PER_TURN) && doesDefendingMonsterEffectActivate) {
            doesDefendingMonsterEffectActivate = false;
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
        return finishAttackConduction(token);
    }

    public void tendToNeitherPlayerReceivesBattleDamageIfMonsterDies(ArrayList<BeingAttackedEffect> beingAttackedEffectsForAttackingMonster, ArrayList<BeingAttackedEffect> beingAttackedEffectsForDefendingMonster) {
        if (beingAttackedEffectsForDefendingMonster.contains(BeingAttackedEffect.NEITHER_PLAYER_RECEIVES_BATTLE_DAMAGE_IF_MONSTER_DIES) && doesDefendingMonsterGoToGraveyard) {
            playersLifePointsChange.set(0, 0);
            playersLifePointsChange.set(1, 0);
        }
        if (beingAttackedEffectsForAttackingMonster.contains(BeingAttackedEffect.NEITHER_PLAYER_RECEIVES_BATTLE_DAMAGE_IF_MONSTER_DIES) && doesAttackingMonsterGoToGraveyard) {
            playersLifePointsChange.set(0, 0);
            playersLifePointsChange.set(1, 0);
        }
    }

    public String finishAttackConduction(String token) {
        doesDefendingMonsterEventuallyGoToGraveyard = doesDefendingMonsterGoToGraveyard;
        doesAttackingMonsterEventuallyGoToGraveyard = doesAttackingMonsterGoToGraveyard;
        ArrayList<BeingAttackedEffect> beingAttackedEffectsForDefendingMonster = defendingMonsterCard.getBeingAttackedEffects();
        ArrayList<BeingAttackedEffect> beingAttackedEffectsForAttackingMonster = attackingMonsterCard.getBeingAttackedEffects();
        tendToNeitherPlayerReceivesBattleDamageIfMonsterDies(beingAttackedEffectsForAttackingMonster, beingAttackedEffectsForDefendingMonster);
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        if (beingAttackedEffectsForDefendingMonster.contains(BeingAttackedEffect.IF_FACE_DOWN_AT_THE_BEGINNING_THEN_OPPONENT_RECEIVES_1000_DAMAGE) && isBeingAttackedMonsterFlipped) {
            playersLifePointsChange.set(actionTurn - 1, playersLifePointsChange.get(actionTurn - 1) - 1000);
        }
        duelController.increaseLifePoints(playersLifePointsChange.get(0), 1);
        duelController.increaseLifePoints(playersLifePointsChange.get(1), 2);
        String output = "";
        output += tendToFaceUpAttackPositionMonsterLogicallyWinning(beingAttackedEffectsForAttackingMonster, beingAttackedEffectsForDefendingMonster);
        output += tendToFaceUpAttackPositionMonsterLogicallyEqual(beingAttackedEffectsForAttackingMonster, beingAttackedEffectsForDefendingMonster);
        output += tendToFaceUpAttackPositionMonsterLogicallyLost(beingAttackedEffectsForAttackingMonster, beingAttackedEffectsForDefendingMonster);
        output += tendToFaceUpDefensePositionMonsterLogicallyWinning(beingAttackedEffectsForAttackingMonster, beingAttackedEffectsForDefendingMonster);
        output += tendToFaceUpDefensePositionMonsterLogicallyEqual();
        output += tendToFaceUpDefensePositionMonsterLogicallyLost();

        if (doesAttackingMonsterEventuallyGoToGraveyard) {
            SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(attackingMonsterCardLocation, token);
        }
        if (doesDefendingMonsterEventuallyGoToGraveyard) {
            SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(defendingMonsterCardLocation, token);
        }
        attackingMonsterCard.setHasCardAlreadyAttacked(true);
        return output;
    }

    public String tendToFaceUpDefensePositionMonsterLogicallyWinning(ArrayList<BeingAttackedEffect> beingAttackedEffectsForAttackingMonster, ArrayList<BeingAttackedEffect> beingAttackedEffectsForDefendingMonster) {
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
                doesAttackingMonsterEventuallyGoToGraveyard = false;
                doesDefendingMonsterEventuallyGoToGraveyard = true;
            }
        }
        return output;
    }

    public String tendToFaceUpDefensePositionMonsterLogicallyEqual() {
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

    public String tendToFaceUpDefensePositionMonsterLogicallyLost() {
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

    public String tendToFaceUpAttackPositionMonsterLogicallyWinning(ArrayList<BeingAttackedEffect> beingAttackedEffectsForAttackingMonster, ArrayList<BeingAttackedEffect> beingAttackedEffectsForDefendingMonster) {
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

    public String tendToFaceUpAttackPositionMonsterLogicallyEqual(ArrayList<BeingAttackedEffect> beingAttackedEffectsForAttackingMonster, ArrayList<BeingAttackedEffect> beingAttackedEffectsForDefendingMonster) {
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

    public String tendToFaceUpAttackPositionMonsterLogicallyLost(ArrayList<BeingAttackedEffect> beingAttackedEffectsForAttackingMonster, ArrayList<BeingAttackedEffect> beingAttackedEffectsForDefendingMonster) {
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
