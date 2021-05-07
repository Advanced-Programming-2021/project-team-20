package controller.duel.GamePhaseControllers;

import java.util.ArrayList;
import java.util.regex.Matcher;

import controller.duel.CardEffects.MonsterEffectEnums.ContinuousMonsterEffect;
import controller.duel.CardEffects.SpellEffectEnums.ContinuousSpellCardEffect;
import controller.duel.GamePackage.ActionConductors.SendCardToGraveyardConductor;
import controller.duel.GamePackage.DuelBoard;
import controller.duel.GamePackage.DuelController;
import controller.duel.GamePackage.PhaseInGame;
import controller.duel.PreliminaryPackage.GameManager;
import controller.duel.Utility.Utility;
import model.cardData.General.Card;
import model.cardData.General.CardLocation;
import model.cardData.General.CardPosition;
import model.cardData.General.RowOfCardLocation;
import model.cardData.MonsterCardData.MonsterCard;
import model.cardData.SpellCardData.SpellCard;
import model.cardData.TrapCardData.TrapCard;

public class PhaseController {
    private PhaseInGame phaseInGame;
    private ArrayList<Boolean> playersProhibitedToDrawCardNextTurn;
    private boolean isClassWaitingForPayingLifePointsOrDestroyingCard;
    private boolean gameIsOver;

    public PhaseController() {
        phaseInGame = null;
        playersProhibitedToDrawCardNextTurn = new ArrayList<>();
        playersProhibitedToDrawCardNextTurn.add(false);
        playersProhibitedToDrawCardNextTurn.add(false);
        isClassWaitingForPayingLifePointsOrDestroyingCard = false;
        gameIsOver = false;
    }

    public String phaseControllerInputAnalysis(String string) {
        String inputRegex = "(?<=\\n|^)next[\\s]+phase(?=\\n|$)";
        Matcher matcher = Utility.getCommandMatcher(string, inputRegex);
        if (Utility.isMatcherCorrectWithoutErrorPrinting(matcher)) {
            if (phaseInGame.equals(PhaseInGame.ALLY_MAIN_PHASE_1)) {
                phaseInGame = PhaseInGame.ALLY_BATTLE_PHASE;
                return "phase: battle phase";
            } else if (phaseInGame.equals(PhaseInGame.ALLY_BATTLE_PHASE)) {
                phaseInGame = PhaseInGame.ALLY_MAIN_PHASE_2;
                return "phase: main phase 2";
            } else if (phaseInGame.equals(PhaseInGame.ALLY_MAIN_PHASE_2)) {
                phaseInGame = PhaseInGame.ALLY_END_PHASE;
                String output = "phase: end phase\n" + resetAllVariables(0) + "phase: draw phase\n" + addCardToHand(0);
                if (gameIsOver) {
                    return output;
                }
                output += conductStandByPhase(0);
                return output;
            } else if (phaseInGame.equals(PhaseInGame.OPPONENT_MAIN_PHASE_1)) {
                phaseInGame = PhaseInGame.OPPONENT_BATTLE_PHASE;
                return "phase: battle phase";
            } else if (phaseInGame.equals(PhaseInGame.OPPONENT_BATTLE_PHASE)) {
                phaseInGame = PhaseInGame.OPPONENT_MAIN_PHASE_2;
                return "phase: main phase 2";
            } else if (phaseInGame.equals(PhaseInGame.OPPONENT_MAIN_PHASE_2)) {
                phaseInGame = PhaseInGame.OPPONENT_END_PHASE;
                String output = "phase: end phase\n" + resetAllVariables(0) + "phase: draw phase\n" + addCardToHand(0);
                if (gameIsOver) {
                    return output;
                }
                output += conductStandByPhase(0);
                return output;
            }
        }
        return "invalid command";
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

    public void setPlayersProhibitedToDrawCardNextTurn(int turn, boolean bool){
        playersProhibitedToDrawCardNextTurn.set(turn-1, bool);
    }

    public boolean isPhaseCurrentlyMainPhase(int turn) {
        if (turn == 1 && (phaseInGame.equals(PhaseInGame.ALLY_MAIN_PHASE_1) || phaseInGame.equals(PhaseInGame.ALLY_MAIN_PHASE_2))) {
            return true;
        } else if (turn == 1) {
            return false;
        } else if (phaseInGame.equals(PhaseInGame.OPPONENT_MAIN_PHASE_1) || phaseInGame.equals(PhaseInGame.OPPONENT_MAIN_PHASE_2)) {
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

    public String redirectInputForStandByPhaseSpellCheck(String string) {
        DuelController duelController = GameManager.getDuelControllerByIndex(0);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(0);
        int turn = duelController.getTurn();
        if (string.equals("pay")) {
            isClassWaitingForPayingLifePointsOrDestroyingCard = false;
            duelController.increaseLifePoints(-100, turn);
            if (turn == 1){
                phaseInGame = PhaseInGame.ALLY_MAIN_PHASE_1;
            } else {
                phaseInGame = PhaseInGame.OPPONENT_MAIN_PHASE_1;
            }
            return "phase: main phase 1\n";
        } else if (string.equals("destroy")) {
            isClassWaitingForPayingLifePointsOrDestroyingCard = false;
            ArrayList<Card> spellCards;
            if (turn == 1) {
                spellCards = duelBoard.getAllySpellCards();
            } else {
                spellCards = duelBoard.getOpponentSpellCards();
            }
            for (int i = 0; i < spellCards.size(); i++) {
                SpellCard spellCard = (SpellCard) spellCards.get(i);
                ArrayList<ContinuousSpellCardEffect> continuousSpellCardEffects = spellCard.getContinuousSpellCardEffects();
                if (continuousSpellCardEffects.contains(ContinuousSpellCardEffect.STANDBY_PHASE_PAY_100_LP_OR_DESTROY_CARD)) {
                    if (spellCard.getCardPosition().equals(CardPosition.FACE_UP_ACTIVATED_POSITION)) {
                        if (turn == 1){
                            SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(new CardLocation(RowOfCardLocation.ALLY_SPELL_ZONE, i+1), 1);
                            phaseInGame = PhaseInGame.ALLY_MAIN_PHASE_1;
                        } else {
                            SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(new CardLocation(RowOfCardLocation.OPPONENT_SPELL_ZONE, i+1), 2);
                            phaseInGame = PhaseInGame.OPPONENT_MAIN_PHASE_1;
                        }
                        return "phase: main phase 1\n";
                    }
                }
            }
        }
        return "invalid command.\nenter pay or destroy\n";
    }


    public String conductStandByPhase(int index) {
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        int turn = duelController.getTurn();
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        String output = "phase: standby phase\n";
        ArrayList<Card> spellCards;
        if (turn == 1) {
            spellCards = duelBoard.getAllySpellCards();
            phaseInGame = PhaseInGame.ALLY_BATTLE_PHASE;
        } else {
            spellCards = duelBoard.getOpponentSpellCards();
            phaseInGame = PhaseInGame.OPPONENT_STANDBY_PHASE;
        }
        output += doesSpellExistThatNeedsToPay100LPOrGetDestroyed(spellCards);
        if (output.equals("\"phase: standby phase\\n\"")) {
            if (turn == 1) {
                phaseInGame = PhaseInGame.ALLY_MAIN_PHASE_1;
            } else {
                phaseInGame = PhaseInGame.OPPONENT_MAIN_PHASE_1;
            }
            return output + "phase: main phase 1";
        }
        return output;
    }

    public String doesSpellExistThatNeedsToPay100LPOrGetDestroyed(ArrayList<Card> spellCards) {
        for (int i = 0; i < spellCards.size(); i++) {
            SpellCard spellCard = (SpellCard) spellCards.get(i);
            ArrayList<ContinuousSpellCardEffect> continuousSpellCardEffects = spellCard.getContinuousSpellCardEffects();
            if (continuousSpellCardEffects.contains(ContinuousSpellCardEffect.STANDBY_PHASE_PAY_100_LP_OR_DESTROY_CARD)) {
                if (spellCard.getCardPosition().equals(CardPosition.FACE_UP_ACTIVATED_POSITION)) {
                    isClassWaitingForPayingLifePointsOrDestroyingCard = true;
                    return "do you want to pay 100 lifepoints or do you want to destroy your spell card?\nsimply enter pay or destroy\n";
                }
            }
        }
        return "";
    }

    public String ifPossibleDrawACard(int index, int turn) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        CardLocation cardLocation;
        if (turn == 1) {
            cardLocation = new CardLocation(RowOfCardLocation.ALLY_DECK_ZONE, 1);
        } else {
            cardLocation = new CardLocation(RowOfCardLocation.OPPONENT_DECK_ZONE, 2);
        }
        Card card = duelBoard.getCardByCardLocation(cardLocation);
        if (card == null) {
            return "";
        }
        duelBoard.removeCardByCardLocation(cardLocation);
        duelBoard.addCardToHand(card, turn);
        return "card with name " + card.getCardName() + " is added to hand";
    }


    public String addCardToHand(int index) {
        // must check time seal card in the future here
        // must add standby phase effects too
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        int turn = duelController.getTurn();
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
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
                gameIsOver = true;
                return duelController.getPlayingUsers().get(0) + " won the game and the score is: " + difference + "\n";
            } else {
                int difference = duelController.getLifePoints().get(1) - duelController.getLifePoints().get(0);
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
            duelController.setTurn(3 - turn);
            duelController.setFakeTurn(3 - turn);
            drawingCardSuccessful = true;
        } else {
            playersProhibitedToDrawCardNextTurn.set(2 - turn, false);
        }
        if (turn == 1) {
            phaseInGame = PhaseInGame.OPPONENT_MAIN_PHASE_1;
        } else {
            phaseInGame = PhaseInGame.ALLY_MAIN_PHASE_1;
        }
        if (drawingCardSuccessful) {
            return "new card added to hand: " + card.getCardName() + "\nphase: standby phase\nphase: main phase 1\n";
        } else {
            return "player was prohibited to draw a card this turn\n";
        }
    }

    private String resetAllVariables(int index) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        ArrayList<Card> allySpellCards = duelBoard.getAllySpellCards();
        ArrayList<Card> allyMonsterCards = duelBoard.getAllyMonsterCards();
        ArrayList<Card> allySpellFieldCard = duelBoard.getAllySpellFieldCard();
        ArrayList<Card> opponentSpellCards = duelBoard.getOpponentSpellCards();
        ArrayList<Card> opponentMonsterCards = duelBoard.getOpponentMonsterCards();
        ArrayList<Card> opponentSpellFieldCard = duelBoard.getOpponentSpellFieldCard();
        resetSetsOfArraylistsWhenTurnsChange(allyMonsterCards, allySpellCards, allySpellFieldCard);
        resetSetsOfArraylistsWhenTurnsChange(opponentMonsterCards, opponentSpellCards, opponentSpellFieldCard);
        return "";
    }

    private void resetSetsOfArraylistsWhenTurnsChange(ArrayList<Card> monsterCards, ArrayList<Card> spellCards, ArrayList<Card> spellFieldCard) {
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
            if (monsterCard.getRealName().equals("scanner")) {
                resetScannerCard(monsterCard);
            }
            monsterCard.setOncePerTurnCardEffectUsed(false);
            monsterCard.setCardPositionChanged(false);
            monsterCard.setCardAttacked(false);
        } else if (Card.isCardASpell(card)) {
            SpellCard spellCard = (SpellCard) card;
            spellCard.setNumberOfTurnsForActivation(spellCard.getNumberOfTurnsForActivation() - 1);
        } else if (Card.isCardATrap(card)) {
            TrapCard trapCard = (TrapCard) card;
            trapCard.setNumberOfTurnsForActivation(trapCard.getNumberOfTurnsForActivation() - 1);
        }
    }

    private void resetScannerCard(MonsterCard monsterCard) {
        //reseting scanner
    }
}
