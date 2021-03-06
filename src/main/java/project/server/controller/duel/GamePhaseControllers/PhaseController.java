package project.server.controller.duel.GamePhaseControllers;

import java.util.ArrayList;
import java.util.regex.Matcher;

import project.model.MonsterEffectEnums.*;
import project.model.SpellEffectEnums.ContinuousSpellCardEffect;
import project.server.controller.duel.GamePackage.ActionConductors.SendCardToGraveyardConductor;
import project.server.controller.duel.GamePackage.DuelBoard;
import project.server.controller.duel.GamePackage.DuelController;
import project.model.PhaseInGame;
import project.server.controller.duel.PreliminaryPackage.GameManager;
import project.model.Utility.Utility;
import project.server.controller.non_duel.storage.Storage;
import project.model.User;
import project.model.cardData.General.*;
import project.model.cardData.MonsterCardData.MonsterCard;
import project.model.cardData.MonsterCardData.MonsterCardFamily;
import project.model.cardData.MonsterCardData.MonsterCardValue;
import project.model.cardData.SpellCardData.SpellCard;
import project.model.cardData.TrapCardData.TrapCard;

public class PhaseController {
    private PhaseInGame phaseInGame;
    private ArrayList<Boolean> playersProhibitedToDrawCardNextTurn;
    private int numberOfSpellCardsPayedFor;
    private int numberOfSpellCardsToPayFor;
    private boolean isClassWaitingForPayingLifePointsOrDestroyingCard;
    private boolean gameIsOver;

    public PhaseController() {
        phaseInGame = PhaseInGame.ALLY_MAIN_PHASE_1;
        playersProhibitedToDrawCardNextTurn = new ArrayList<>();
        playersProhibitedToDrawCardNextTurn.add(false);
        playersProhibitedToDrawCardNextTurn.add(false);
        numberOfSpellCardsPayedFor = 0;
        numberOfSpellCardsToPayFor = 0;
        isClassWaitingForPayingLifePointsOrDestroyingCard = false;
        gameIsOver = false;
    }

    public String phaseControllerInputAnalysis(String string, String token) {
        String inputRegex = "(?<=\\n|^)next[\\s]+phase(?=\\n|$)";
        Matcher matcher = Utility.getCommandMatcher(string, inputRegex);
        if (Utility.isMatcherCorrectWithoutErrorPrinting(matcher)) {
            if (phaseInGame.equals(PhaseInGame.ALLY_MAIN_PHASE_1)) {
                phaseInGame = PhaseInGame.ALLY_BATTLE_PHASE;
                return "phase: battle phase\n";
            } else if (phaseInGame.equals(PhaseInGame.ALLY_BATTLE_PHASE)) {
                phaseInGame = PhaseInGame.ALLY_MAIN_PHASE_2;
                return "phase: main phase 2\n";
            } else if (phaseInGame.equals(PhaseInGame.ALLY_MAIN_PHASE_2)) {
                phaseInGame = PhaseInGame.ALLY_END_PHASE;
                String output = "phase: end phase\n" + resetAllVariables(token) + "phase: draw phase\n" + addCardToHand(token);
                if (gameIsOver) {
                    return output;
                }
                output += conductStandByPhase(token);
                return output;
            } else if (phaseInGame.equals(PhaseInGame.OPPONENT_MAIN_PHASE_1)) {
                phaseInGame = PhaseInGame.OPPONENT_BATTLE_PHASE;
                return "phase: battle phase\n";
            } else if (phaseInGame.equals(PhaseInGame.OPPONENT_BATTLE_PHASE)) {
                phaseInGame = PhaseInGame.OPPONENT_MAIN_PHASE_2;
                return "phase: main phase 2\n";
            } else if (phaseInGame.equals(PhaseInGame.OPPONENT_MAIN_PHASE_2)) {
                phaseInGame = PhaseInGame.OPPONENT_END_PHASE;
                String output = "phase: end phase\n" + resetAllVariables(token) + "phase: draw phase\n" + addCardToHand(token);
                if (gameIsOver) {
                    return output;
                }
                output += conductStandByPhase(token);
                return output;
            }
        }
        return "invalid command\n";
    }

    public PhaseInGame getPhaseInGame() {
        return phaseInGame;
    }

    public boolean isClassWaitingForPayingLifePointsOrDestroyingCard() {
        return isClassWaitingForPayingLifePointsOrDestroyingCard;
    }

    public void setPhaseInGame(PhaseInGame phaseInGame) {
        this.phaseInGame = phaseInGame;
    }

    public void setPlayersProhibitedToDrawCardNextTurn(int turn, boolean bool) {
        playersProhibitedToDrawCardNextTurn.set(turn - 1, bool);
    }

    public boolean isPhaseCurrentlyMainPhase(int turn) {
        if (turn == 1 && (phaseInGame.equals(PhaseInGame.ALLY_MAIN_PHASE_1)
            || phaseInGame.equals(PhaseInGame.ALLY_MAIN_PHASE_2))) {
            return true;
        } else if (turn == 1) {
            return false;
        } else if (phaseInGame.equals(PhaseInGame.OPPONENT_MAIN_PHASE_1)
            || phaseInGame.equals(PhaseInGame.OPPONENT_MAIN_PHASE_2)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isPhaseCurrentlyBattlePhase(int turn) {
        if (turn == 1 && (phaseInGame.equals(PhaseInGame.ALLY_BATTLE_PHASE))) {
            return true;
        } else if (turn == 1) {
            return false;
        } else if (phaseInGame.equals(PhaseInGame.OPPONENT_BATTLE_PHASE)) {
            return true;
        } else {
            return false;
        }
    }

    public String redirectInputForStandByPhaseSpellCheck(String string, String token) {
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        int turn = duelController.getTurn();
        if (string.equals("pay")) {
            duelController.increaseLifePoints(-100, turn, token);
            numberOfSpellCardsToPayFor -= 1;
            numberOfSpellCardsPayedFor += 1;
            if (numberOfSpellCardsToPayFor == 0) {
                isClassWaitingForPayingLifePointsOrDestroyingCard = false;
                numberOfSpellCardsPayedFor = 0;
                if (turn == 1) {
                    phaseInGame = PhaseInGame.ALLY_MAIN_PHASE_1;
                } else {
                    phaseInGame = PhaseInGame.OPPONENT_MAIN_PHASE_1;
                }
                return "phase: main phase 1\n";
            } else {
                return "decide for your other spell cards\nenter pay or destroy\n";
            }
        } else if (string.equals("destroy")) {
            numberOfSpellCardsToPayFor -= 1;
            //isClassWaitingForPayingLifePointsOrDestroyingCard = false;
            ArrayList<Card> spellCards;
            if (turn == 1) {
                spellCards = duelBoard.getAllySpellCards();
            } else {
                spellCards = duelBoard.getOpponentSpellCards();
            }
            int numberOfSpellsFound = 0;
            for (int i = 0; i < spellCards.size(); i++) {
                SpellCard spellCard = (SpellCard) spellCards.get(i);
                if (spellCard != null) {
                    ArrayList<ContinuousSpellCardEffect> continuousSpellCardEffects = spellCard.getContinuousSpellCardEffects();
                    if (continuousSpellCardEffects.contains(ContinuousSpellCardEffect.STANDBY_PHASE_PAY_100_LP_OR_DESTROY_CARD)) {
                        if (spellCard.getCardPosition().equals(CardPosition.FACE_UP_ACTIVATED_POSITION)) {
                            numberOfSpellsFound += 1;
                            if (numberOfSpellsFound == numberOfSpellCardsPayedFor + 1) {
                                if (turn == 1) {
                                    SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(
                                        new CardLocation(RowOfCardLocation.ALLY_SPELL_ZONE, i + 1), token);
                                    if (numberOfSpellCardsToPayFor == 0) {
                                        isClassWaitingForPayingLifePointsOrDestroyingCard = false;
                                        phaseInGame = PhaseInGame.ALLY_MAIN_PHASE_1;
                                        return "phase: main phase 1\n";
                                    } else {
                                        return "decide for your other spell cards\nenter pay or destroy\n";
                                    }
                                } else {
                                    SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(
                                        new CardLocation(RowOfCardLocation.OPPONENT_SPELL_ZONE, i + 1), token);
                                    if (numberOfSpellCardsToPayFor == 0) {
                                        isClassWaitingForPayingLifePointsOrDestroyingCard = false;
                                        phaseInGame = PhaseInGame.OPPONENT_MAIN_PHASE_1;
                                        return "phase: main phase 1\n";
                                    } else {
                                        return "decide for your other spell cards\nenter pay or destroy\n";
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return "invalid command.\nenter pay or destroy\n";
    }

    public String conductStandByPhase(String token) {
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        int turn = duelController.getTurn();
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        String output = "phase: standby phase\n";
        ArrayList<Card> spellCards;
        if (turn == 1) {
            spellCards = duelBoard.getAllySpellCards();
            phaseInGame = PhaseInGame.ALLY_STANDBY_PHASE;
        } else {
            spellCards = duelBoard.getOpponentSpellCards();
            phaseInGame = PhaseInGame.OPPONENT_STANDBY_PHASE;
        }
        output += doesSpellExistThatNeedsToPay100LPOrGetDestroyed(spellCards);
        if (output.equals("phase: standby phase\n")) {
            if (turn == 1) {
                phaseInGame = PhaseInGame.ALLY_MAIN_PHASE_1;
            } else {
                phaseInGame = PhaseInGame.OPPONENT_MAIN_PHASE_1;
            }
            return output + "phase: main phase 1\n";
        }
        return output;
    }

    public String doesSpellExistThatNeedsToPay100LPOrGetDestroyed(ArrayList<Card> spellCards) {
        numberOfSpellCardsToPayFor = 0;
        numberOfSpellCardsPayedFor = 0;
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < spellCards.size(); i++) {
            if (spellCards.get(i) != null && Card.isCardASpell(spellCards.get(i))) {
                SpellCard spellCard = (SpellCard) spellCards.get(i);
                ArrayList<ContinuousSpellCardEffect> continuousSpellCardEffects = spellCard
                    .getContinuousSpellCardEffects();
                if (continuousSpellCardEffects
                    .contains(ContinuousSpellCardEffect.STANDBY_PHASE_PAY_100_LP_OR_DESTROY_CARD)) {
                    if (spellCard.getCardPosition().equals(CardPosition.FACE_UP_ACTIVATED_POSITION)) {
                        isClassWaitingForPayingLifePointsOrDestroyingCard = true;
                        numberOfSpellCardsToPayFor += 1;
                        output.append("do you want to pay 100 lifepoints or do you want to destroy your spell card?\nsimply enter pay or destroy\n");
                    }
                }
            }
        }
        return output.toString();
    }

    public String ifPossibleDrawACard(String token, int turn) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        CardLocation cardLocation;
        if (turn == 1) {
            cardLocation = new CardLocation(RowOfCardLocation.ALLY_DECK_ZONE, 1);
        } else {
            cardLocation = new CardLocation(RowOfCardLocation.OPPONENT_DECK_ZONE, 1);
        }
        Card card = duelBoard.getCardByCardLocation(cardLocation);
        if (card == null) {
            return "";
        }
        duelBoard.removeCardByCardLocation(cardLocation);
        duelBoard.addCardToHand(card, turn);
        GameManager.getDuelControllerByIndex(token).addStringToSuperAlmightyString("mainCardLocation " + cardLocation.getRowOfCardLocation()
            + " " + cardLocation.getIndex() + " is being added to hand zone " + turn + " and should finally be NO_CHANGE", token);

        return "card with name " + card.getCardName() + " is added to hand\n";
    }

    public String addCardToHand(String token) {
        // must check time seal card in the future here
        // must add standby phase effects too
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        int turn = duelController.getTurn();
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        CardLocation cardLocation = null;
        boolean isGameFinished = false;
        if (turn == 1) {
            cardLocation = new CardLocation(RowOfCardLocation.OPPONENT_DECK_ZONE, 1);
            ArrayList<Card> opponentDeck = duelBoard.getOpponentCardsInDeck();
            if (opponentDeck.size() == 0 && !playersProhibitedToDrawCardNextTurn.get(1)) {
                isGameFinished = true;
            }
        } else if (turn == 2) {
            cardLocation = new CardLocation(RowOfCardLocation.ALLY_DECK_ZONE, 1);
            ArrayList<Card> allyDeck = duelBoard.getAllyCardsInDeck();
            if (allyDeck.size() == 0 && !playersProhibitedToDrawCardNextTurn.get(0)) {
                isGameFinished = true;
            }
        }
        if (isGameFinished) {
            if (turn == 1) {
                int difference = duelController.getLifePoints().get(0) - duelController.getLifePoints().get(1);
                rewardWinner(turn, token, duelController.getLifePoints().get(0), 1);
                gameIsOver = true;
                return duelController.getPlayingUsers().get(0) + " won the game and the score is: " + difference + "\n";
            } else {
                int difference = duelController.getLifePoints().get(1) - duelController.getLifePoints().get(0);
                rewardWinner(turn, token, duelController.getLifePoints().get(0), 1);
                gameIsOver = true;
                return duelController.getPlayingUsers().get(1) + " won the game and the score is: " + difference + "\n";
            }
        }
        boolean drawingCardSuccessful = false;
        Card card = null;
        if (!playersProhibitedToDrawCardNextTurn.get(2 - turn)) {
            card = duelBoard.getCardByCardLocation(cardLocation);
            duelBoard.removeCardByCardLocation(cardLocation);
            duelBoard.addCardToHand(card, 3 - turn);
            GameManager.getDuelControllerByIndex(token).addStringToSuperAlmightyString("mainCardLocation " + cardLocation.getRowOfCardLocation()
                + " " + cardLocation.getIndex() + " is being added to hand zone " + (3 - turn) + " and should finally be NO_CHANGE", token);

            drawingCardSuccessful = true;
        } else {
            playersProhibitedToDrawCardNextTurn.set(2 - turn, false);
        }
        duelController.setTurn(3 - turn);
        duelController.setFakeTurn(3 - turn);
        if (turn == 1) {
            phaseInGame = PhaseInGame.OPPONENT_MAIN_PHASE_1;
        } else {
            phaseInGame = PhaseInGame.ALLY_MAIN_PHASE_1;
        }
        if (drawingCardSuccessful) {
            return "new card added to hand: " + card.getCardName() + "\n";
        } else {
            return "player was prohibited to draw a card this turn\n";
        }
    }

    private void rewardWinner(int turn, String token, int maxLifePointOffWinner, int rounds) {

        User winnerUser = Storage
            .getUserByName(GameManager.getDuelControllerByIndex(token).getPlayingUsernameByTurn(turn - 1));
        User loserUser = Storage
            .getUserByName(GameManager.getDuelControllerByIndex(token).getPlayingUsernameByTurn(-turn + 2));
        winnerUser.setMoney(rounds * (1000 + maxLifePointOffWinner));
        winnerUser.setScore(rounds * 1000);
        loserUser.setMoney(rounds * 100);

    }

    private String resetAllVariables(String token) {
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        duelController.setTotalTurnsUntilNow(duelController.getTotalTurnsUntilNow() + 1);
        duelController.setCanUserSummonOrSetMonsters(1, true);
        duelController.setCanUserSummonOrSetMonsters(2, true);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        ArrayList<CardLocation> cardLocationsToBeTakenBackInEndPhase = duelBoard.getCardLocationsToBeTakenBackInEndPhase();
        for (int i = 0; i < cardLocationsToBeTakenBackInEndPhase.size(); i++) {
            CardLocation cardLocation = cardLocationsToBeTakenBackInEndPhase.get(i);
            Card card = duelBoard.getCardByCardLocation(cardLocation);
            if (card != null) {
                SendCardToGraveyardConductor.removeCardAndGetRemovedCard(cardLocation, token);
                if (cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) {
                    duelBoard.addCardToMonsterZone(card, 1, token);

                    GameManager.getDuelControllerByIndex(token).addStringToSuperAlmightyString("mainCardLocation " + cardLocation.getRowOfCardLocation()
                        + " " + cardLocation.getIndex() + " is being added to monster zone " + 1 + " and should finally be NO_CHANGE", token);

                } else if (cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) {
                    duelBoard.addCardToMonsterZone(card, 2, token);

                    GameManager.getDuelControllerByIndex(token).addStringToSuperAlmightyString("mainCardLocation " + cardLocation.getRowOfCardLocation()
                        + " " + cardLocation.getIndex() + " is being added to monster zone " + 2 + " and should finally be NO_CHANGE", token);

                }
            }
        }
        duelBoard.clearCardLocationsToBeTakenBackInEndPhase();
        ArrayList<Card> allySpellCards = duelBoard.getAllySpellCards();
        ArrayList<Card> allyMonsterCards = duelBoard.getAllyMonsterCards();
        ArrayList<Card> allySpellFieldCard = duelBoard.getAllySpellFieldCard();
        ArrayList<Card> opponentSpellCards = duelBoard.getOpponentSpellCards();
        ArrayList<Card> opponentMonsterCards = duelBoard.getOpponentMonsterCards();
        ArrayList<Card> opponentSpellFieldCard = duelBoard.getOpponentSpellFieldCard();
        resetSetsOfArraylistsWhenTurnsChange(allyMonsterCards, allySpellCards, allySpellFieldCard);
        resetSetsOfArraylistsWhenTurnsChange(opponentMonsterCards, opponentSpellCards, opponentSpellFieldCard);
        for (int i = 0; i < 5; i++) {
            if (Card.isCardASpell(allySpellCards.get(i)) && ((SpellCard) allySpellCards.get(i)).getNumberOfTurnsForActivation() == 0) {
                SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(new CardLocation(RowOfCardLocation.ALLY_SPELL_ZONE, i + 1), token);
            }
            if (Card.isCardASpell(opponentSpellCards.get(i)) && ((SpellCard) opponentSpellCards.get(i)).getNumberOfTurnsForActivation() == 0) {
                SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(new CardLocation(RowOfCardLocation.OPPONENT_SPELL_ZONE, i + 1), token);
            }
        }
        return "";

    }

    private void resetSetsOfArraylistsWhenTurnsChange(ArrayList<Card> monsterCards, ArrayList<Card> spellCards,
                                                      ArrayList<Card> spellFieldCard) {
        for (int i = 0; i < monsterCards.size(); i++) {
            resetCharacteristicsOfOneCardWhenTurnChanges(monsterCards.get(i));
        }
        for (int i = 0; i < spellCards.size(); i++) {
            resetCharacteristicsOfOneCardWhenTurnChanges(spellCards.get(i));
        }
        for (int i = 0; i < spellFieldCard.size(); i++) {
            resetCharacteristicsOfOneCardWhenTurnChanges(spellFieldCard.get(i));
        }
    }

    private void resetCharacteristicsOfOneCardWhenTurnChanges(Card card) {
        if (Card.isCardAMonster(card)) {
            MonsterCard monsterCard = (MonsterCard) card;
            if (monsterCard.getRealName().equals("Scanner")) {
                resetScannerCard(monsterCard);
            }
            monsterCard.setOncePerTurnCardEffectUsed(false);
            monsterCard.setCardPositionChanged(false);
            monsterCard.setHasCardAlreadyAttacked(false);
        } else if (Card.isCardASpell(card)) {
            SpellCard spellCard = (SpellCard) card;
            spellCard.setOncePerTurnCardEffectUsed(false);
            spellCard.setNumberOfTurnsForActivation(spellCard.getNumberOfTurnsForActivation() - 1);
        } else if (Card.isCardATrap(card)) {
            TrapCard trapCard = (TrapCard) card;
            trapCard.setNumberOfTurnsForActivation(trapCard.getNumberOfTurnsForActivation() - 1);
        }
    }

    private void resetScannerCard(MonsterCard monsterCard) {
        monsterCard.setAttackPower(0);
        monsterCard.setDefensePower(0);
        monsterCard.setLevel(1);
        monsterCard.setMonsterCardFamily(MonsterCardFamily.MACHINE);
        monsterCard.setMonsterCardValue(MonsterCardValue.EFFECT);
        ArrayList<SummoningRequirement> summoningRequirements = new ArrayList<>();
        summoningRequirements.add(SummoningRequirement.CAN_BE_NORMAL_SUMMONED);
        monsterCard.setSummoningRequirements(summoningRequirements);
        ArrayList<UponSummoningEffect> uponSummoningEffects = new ArrayList<>();
        monsterCard.setUponSummoningEffects(uponSummoningEffects);
        ArrayList<BeingAttackedEffect> beingAttackedEffects = new ArrayList<>();
        monsterCard.setBeingAttackedEffects(beingAttackedEffects);
        ArrayList<ContinuousMonsterEffect> continuousMonsterEffects = new ArrayList<>();
        monsterCard.setContinuousMonsterEffects(continuousMonsterEffects);
        ArrayList<FlipEffect> flipEffects = new ArrayList<>();
        monsterCard.setFlipEffects(flipEffects);
        ArrayList<OptionalMonsterEffect> optionalMonsterEffects = new ArrayList<>();
        optionalMonsterEffects.add(OptionalMonsterEffect.ONCE_PER_TURN_CHOOSE_A_MONSTER_IN_YOUR_OPPONENTS_GRAVEYARD_AND_COPY_ALL_CHARACTERISTICS_UNTIL_THE_END_OF_THAT_TURN);
        monsterCard.setOptionalMonsterEffects(optionalMonsterEffects);
        ArrayList<SentToGraveyardEffect> sentToGraveyardEffects = new ArrayList<>();
        monsterCard.setSentToGraveyardEffects(sentToGraveyardEffects);
        ArrayList<AttackerEffect> attackerEffects = new ArrayList<>();
        monsterCard.setAttackerEffects(attackerEffects);
        monsterCard.setCardName("Scanner");
        monsterCard.setCardType(CardType.MONSTER);
        monsterCard.setCardDescription("");
    }

    public void clearAllVariablesOfThisClass() {
        phaseInGame = null;
        playersProhibitedToDrawCardNextTurn.clear();
        playersProhibitedToDrawCardNextTurn.add(false);
        playersProhibitedToDrawCardNextTurn.add(false);
        numberOfSpellCardsPayedFor = 0;
        numberOfSpellCardsToPayFor = 0;
        isClassWaitingForPayingLifePointsOrDestroyingCard = false;
        gameIsOver = false;
    }
}
