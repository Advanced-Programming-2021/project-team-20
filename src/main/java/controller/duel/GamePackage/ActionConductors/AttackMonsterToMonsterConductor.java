package controller.duel.GamePackage.ActionConductors;

import java.util.ArrayList;
import java.util.regex.Matcher;

import controller.duel.CardEffects.MonsterEffectEnums.BeingAttackedEffect;
import controller.duel.CardEffects.MonsterEffectEnums.FlipEffect;
import controller.duel.GamePackage.Action;
import controller.duel.GamePackage.ActionType;
import controller.duel.GamePackage.DuelBoard;
import controller.duel.GamePackage.DuelController;
import controller.duel.GamePhaseControllers.ChainController;
import controller.duel.GamePhaseControllers.SelectCardController;
import controller.duel.GamePhaseControllers.SpecialSummonController;
import controller.duel.PreliminaryPackage.GameManager;
import controller.duel.Utility.Utility;
import model.cardData.General.Card;
import model.cardData.General.CardLocation;
import model.cardData.General.CardPosition;
import model.cardData.General.RowOfCardLocation;
import model.cardData.MonsterCardData.MonsterCard;
import model.cardData.MonsterCardData.MonsterCardFamily;
import model.cardData.MonsterCardData.MonsterCardValue;

public class AttackMonsterToMonsterConductor extends ChainController {
    private int indexOfClass;
    private int numberInListOfActionsOfClass;
    private boolean isAttackCanceled;
    private int attackingMonsterATK;
    private int attackingMonsterDEF;
    private int defendingMonsterATK;
    private int defendingMonsterDEF;
    private CardLocation attackingMonsterCardLocation;
    private CardLocation defendingMonsterCardLocation;
    private MonsterCard attackingMonsterCard;
    private MonsterCard defendingMonsterCard;
    private ArrayList<BeingAttackedEffect> beingAttackedEffectsForAttackingMonster;
    private ArrayList<BeingAttackedEffect> beingAttackedEffectsForDefendingMonster;
    private boolean promptingUserToActivateMonsterEffect;
    private boolean isBeingAttackedMonsterFlipped;
    private boolean isClassWaitingForPlayerToPickMonsterToSpecialSummon;
    private boolean isClassWaitingForUserToChooseAttackPositionOrDefensePosition;
    private boolean isClassWaitingForPlayerToPickMonsterToDestroy;
    private boolean doesDefendingMonsterEffectActivate;
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
        isClassWaitingForPlayerToPickMonsterToSpecialSummon = false;
        isClassWaitingForPlayerToPickMonsterToDestroy = false;
        isClassWaitingForUserToChooseAttackPositionOrDefensePosition = false;
        doesDefendingMonsterEffectActivate = false;
        didAttackingUserReceiveDamage = false;
        shouldRedirectAttackConductorFunctionToAvoidRepetition = false;
        isConductBattleNegatedBecauseOfMonsterEffect = false;
        playersLifePointsChange = new ArrayList<>();
        beingAttackedEffectsForAttackingMonster = new ArrayList<>();
        beingAttackedEffectsForDefendingMonster = new ArrayList<>();
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

    public String AttackConductor(int index, int numberInListOfActions) {
        indexOfClass = index;
        numberInListOfActionsOfClass = numberInListOfActions;
        ArrayList<Action> actions = GameManager.getActionsByIndex(index);
        Action action = actions.get(numberInListOfActions);
        if (shouldRedirectAttackConductorFunctionToAvoidRepetition) {
            shouldRedirectAttackConductorFunctionToAvoidRepetition = false;
            if (isConductBattleNegatedBecauseOfMonsterEffect) {
                isConductBattleNegatedBecauseOfMonsterEffect = false;
                return "action was canceled because of monster card's effect";
            }
            if (isAttackCanceled){
                return "action was canceled";
            }
            return conductBattle();
        }
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
                ArrayList<FlipEffect> defendingMonsterFlipEffect = defendingMonsterCard.getFlipEffects();
                if (defendingMonsterFlipEffect.contains(FlipEffect.DESTROY_1_MONSTER_ON_THE_FIELD)) {
                    promptingUserToActivateMonsterEffect = true;
                    shouldRedirectAttackConductorFunctionToAvoidRepetition = true;
                    return "now it will be another player's turn\nshow board\ndo you want to activate your monster card's effect?";
                }
                //must change turns and prompt user to select for flip effect
            }
            //also put if flip effect
            if (doesDefendingMonsterHaveEffect() && !defendingMonsterCard.isOncePerTurnCardEffectUsed()) {
                promptingUserToActivateMonsterEffect = true;
                shouldRedirectAttackConductorFunctionToAvoidRepetition = true;
                return "now it will be another player's turn\nshow board\ndo you want to activate your monster card's effect?";
            }
            return conductBattle();
        }
        return "action was canceled.";
    }

    public String defendingMonsterEffectAnalysis(String string) {
        String inputRegex = "(?<=\\n|^)no(?=\\n|$)";
        Matcher matcher = Utility.getCommandMatcher(string, inputRegex);
        ArrayList<Action> actions = GameManager.getActionsByIndex(0);
        Action action = actions.get(actions.size() - 1);
        int actionTurn = action.getActionTurn();
        if (Utility.isMatcherCorrectWithoutErrorPrinting(matcher)) {
            promptingUserToActivateMonsterEffect = false;
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
                    if (canPlayerSpecialSummonCyberseMonsterFromHandDeckOrGraveyard()) {
                        String canChainingOccur = canChainingOccur(0, actionTurn, ActionType.ALLY_MONSTER_ATTACKING_OPPONENT_MONSTER, ActionType.OPPONENT_MONSTER_ATTACKING_ALLY_MONSTER);
                        if (!canChainingOccur.equals("")) {
                            return canChainingOccur;
                        }
                        isClassWaitingForPlayerToPickMonsterToSpecialSummon = true;
                        return "show hand, graveyard, deck.\nselect one cyberse normal monster to special summon";
                    }
                }
                ArrayList<FlipEffect> flipEffects = defendingMonsterCard.getFlipEffects();
                if (isBeingAttackedMonsterFlipped && flipEffects.contains(FlipEffect.DESTROY_1_MONSTER_ON_THE_FIELD)){
                    isClassWaitingForPlayerToPickMonsterToDestroy = true;
                    return "select one monster on the field to destroy";
                }
            }
        }
        return Action.conductAllActions(indexOfClass);
    }

    public boolean canPlayerSpecialSummonCyberseMonsterFromHandDeckOrGraveyard() {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(0);
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

    public String checkGivenInputForMonsterToDestroy(int index){
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(index);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        CardLocation cardLocation = selectedCardLocations.get(selectedCardLocations.size() - 1);
        Card card = duelBoard.getCardByCardLocation(cardLocation);
        if (Card.isCardATrap(card) || Card.isCardASpell(card)) {
            return "this card cannot be chosen for destroying.\nselect another card";
        } else if (Card.isCardAMonster(card) &&
            !(cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE) || cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE))) {
            return "you can't select this monster for destroying.\nselect another card";
        } else {
            SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(cardLocation, index);
            Card attackingMonsterCardChecking = duelBoard.getCardByCardLocation(attackingMonsterCardLocation);
            Card defendingMonsterCardChecking = duelBoard.getCardByCardLocation(defendingMonsterCardLocation);
            if (attackingMonsterCardChecking == null || defendingMonsterCardChecking == null){
                isAttackCanceled = true;
            }
            isClassWaitingForPlayerToPickMonsterToDestroy = false;
            return "monster destroyed successfully\n"+Action.conductAllActions(index);
        }
    }


    public String checkGivenInputForMonsterEffectActivation(int index) {
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

    public String checkGivenInputForFaceUpAttackPositionOrFaceUpDefensePosition(String string) {
        String output = "";
        String canChainingOccur = "";
        SpecialSummonController specialSummonController = GameManager.getSpecialSummonControllerByIndex(0);
        ArrayList<Action> actions = GameManager.getActionsByIndex(0);
        Action action = actions.get(actions.size() - 1);
        int actionTurn = action.getActionTurn();
        if (string.equals("attacking")) {
            isClassWaitingForUserToChooseAttackPositionOrDefensePosition = false;
            //cardPositionOfMainCard = CardPosition.FACE_UP_ATTACK_POSITION;
            cardPositionToBeSpecialSummoned = CardPosition.FACE_UP_ATTACK_POSITION;
            createActionForSpecialSummoningMonster(0);
            output = Action.conductUninterruptedAction(0);
            canChainingOccur = specialSummonController.canChainingOccur(0, actionTurn, ActionType.ALLY_SPECIAL_SUMMONING_MONSTER, ActionType.OPPONENT_SPECIAL_SUMMONING_MONSTER);
        } else if (string.equals("defensive")) {
            isClassWaitingForUserToChooseAttackPositionOrDefensePosition = false;
            cardPositionToBeSpecialSummoned = CardPosition.FACE_UP_DEFENSE_POSITION;
            //cardPositionOfMainCard = CardPosition.FACE_UP_DEFENSE_POSITION;
            createActionForSpecialSummoningMonster(0);
            output = Action.conductUninterruptedAction(0);
            canChainingOccur = specialSummonController.canChainingOccur(0, actionTurn, ActionType.ALLY_SPECIAL_SUMMONING_MONSTER, ActionType.OPPONENT_SPECIAL_SUMMONING_MONSTER);
        }
        if (!isClassWaitingForUserToChooseAttackPositionOrDefensePosition) {
            if (!canChainingOccur.equals("")) {
                output += canChainingOccur;
                return output;
            }
            return output + Action.conductAllActions(0);
        }
        return "invalid input\nplease enter attacking or defensive";
    }

    public void createActionForSpecialSummoningMonster(int index) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(index);
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        int turn = duelController.getTurn();
        selectCardController.resetSelectedCardLocationList();
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(index);
        ArrayList<Action> actions = GameManager.getActionsByIndex(index);
        if (turn == 1) {
            uninterruptedActions.add(new Action(ActionType.ALLY_SPECIAL_SUMMONING_MONSTER, 1, cardToBeSpecialSummoned, null, null, null, null, null, null, null, null, null, null, cardPositionToBeSpecialSummoned));
            actions.add(new Action(ActionType.ALLY_SPECIAL_SUMMONING_MONSTER, 1, cardToBeSpecialSummoned, null, null, null, null, null, null, null, null, null, null, cardPositionToBeSpecialSummoned));
            //add action that conducts effects of the card
        } else if (turn == 2) {
            uninterruptedActions.add(new Action(ActionType.OPPONENT_SPECIAL_SUMMONING_MONSTER, 2, cardToBeSpecialSummoned, null, null, null, null, null, null, null, null, null, null, cardPositionToBeSpecialSummoned));
            actions.add(new Action(ActionType.OPPONENT_SPECIAL_SUMMONING_MONSTER, 2, cardToBeSpecialSummoned, null, null, null, null, null, null, null, null, null, null, cardPositionToBeSpecialSummoned));
            //add action that conducts effects of the card
        }
        cardToBeSpecialSummoned = null;
        cardPositionToBeSpecialSummoned = null;
    }

    public boolean doesDefendingMonsterHaveEffect() {
        ArrayList<BeingAttackedEffect> beingAttackedEffects = defendingMonsterCard.getBeingAttackedEffects();
        if (beingAttackedEffects.contains(BeingAttackedEffect.NEGATE_ATTACK_ONCE_PER_TURN) && beingAttackedEffects.contains(BeingAttackedEffect.SPECIAL_SUMMON_CYBERSE_NORMAL_MONSTER_FROM_HAND_GV_DECK_ONCE_PER_TURN)) {
            return true;
        }
        if (beingAttackedEffects.contains(BeingAttackedEffect.SET_ATTACKING_MONSTER_ATK_TO_0_ONCE_PER_TURN)) {
            return true;
        }
        return false;
    }

    public String conductBattle() {
        if (!isConductBattleNegatedBecauseOfMonsterEffect && !isAttackCanceled) {
            if (defendingMonsterCard.getCardPosition().equals(CardPosition.FACE_UP_ATTACK_POSITION)) {
                String output = faceUpAttackPositionMonsterToFaceUpAttackPositionMonster();
                resetVariables();
                return output;
            }
            resetVariables();
            String output = faceUpAttackPositionMonsterToFaceUpDefensePositionMonster();
            return output;
        }
        resetVariables();
        return "attack was negated because of monster card effect";
    }

    private void resetVariables() {
        isBeingAttackedMonsterFlipped = false;
        isClassWaitingForPlayerToPickMonsterToSpecialSummon = false;
        isClassWaitingForUserToChooseAttackPositionOrDefensePosition = false;
        isClassWaitingForPlayerToPickMonsterToDestroy = false;
        doesDefendingMonsterEffectActivate = false;
        didAttackingUserReceiveDamage = false;
        shouldRedirectAttackConductorFunctionToAvoidRepetition = false;
        isConductBattleNegatedBecauseOfMonsterEffect = false;
        playersLifePointsChange = new ArrayList<>();
        beingAttackedEffectsForAttackingMonster.clear();
        beingAttackedEffectsForDefendingMonster.clear();
    }

    public String faceUpAttackPositionMonsterToFaceUpAttackPositionMonster() {
        attackingMonsterATK = MonsterCard.giveATKDEFConsideringEffects("attack", attackingMonsterCardLocation, 0);
        defendingMonsterATK = MonsterCard.giveATKDEFConsideringEffects("attack", defendingMonsterCardLocation, 0);
        System.out.println("attackingMonsterATK is " + attackingMonsterATK);
        System.out.println("defendingMonsterATK is " + defendingMonsterATK);
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
        //reconsiderEffectsOfMonsterInBattle();
        return finishAttackConduction();
    }

    public String faceUpAttackPositionMonsterToFaceUpDefensePositionMonster() {
        System.out.println("defending monster card name os " + defendingMonsterCard.getCardName() + "\niiiiiiiiiiiiiiiiiiiiiii\n");
        System.out.println("attacking monster card name os " + attackingMonsterCard.getCardName() + "\niiiiiiiiiiiiiiiiiiiiiii\n");
        attackingMonsterATK = MonsterCard.giveATKDEFConsideringEffects("attack", attackingMonsterCardLocation, 0);
        defendingMonsterDEF = MonsterCard.giveATKDEFConsideringEffects("defense", defendingMonsterCardLocation, 0);
        ArrayList<BeingAttackedEffect> beingAttackedEffects = defendingMonsterCard.getBeingAttackedEffects();
        for (int i = 0; i < beingAttackedEffects.size(); i++) {
            System.out.println("being attacked effect are " + beingAttackedEffects.get(i));
        }
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
    public void tendToNeitherPlayerReceivesBattleDamageIfMonsterDies() {
        if (beingAttackedEffectsForDefendingMonster.contains(BeingAttackedEffect.NEITHER_PLAYER_RECEIVES_BATTLE_DAMAGE_IF_MONSTER_DIES) && doesDefendingMonsterGoToGraveyard) {
            playersLifePointsChange.set(0, 0);
            playersLifePointsChange.set(1, 0);
        }
        if (beingAttackedEffectsForAttackingMonster.contains(BeingAttackedEffect.NEITHER_PLAYER_RECEIVES_BATTLE_DAMAGE_IF_MONSTER_DIES) && doesAttackingMonsterGoToGraveyard) {
            playersLifePointsChange.set(0, 0);
            playersLifePointsChange.set(1, 0);
        }
    }

    public void tendToMonsterSendingOtherMonsterToGraveyardIfItselfIsDestroyed() {
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

    public String finishAttackConduction() {
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

    public String tendToFaceUpDefensePositionMonsterLogicallyWinning() {
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

    public String tendToFaceUpDefensePositionMonsterLogicallyEqual() {
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

    public String tendToFaceUpDefensePositionMonsterLogicallyLost() {
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

    public String tendToFaceUpAttackPositionMonsterLogicallyWinning() {
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

    public String tendToFaceUpAttackPositionMonsterLogicallyEqual() {
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

    public String tendToFaceUpAttackPositionMonsterLogicallyLost() {
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
