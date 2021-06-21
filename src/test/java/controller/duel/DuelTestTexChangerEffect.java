package controller.duel;

import controller.duel.GamePackage.DuelController;
import controller.duel.GamePackage.PhaseInGame;
import controller.duel.PreliminaryPackage.GameManager;
import model.cardData.General.Card;
import model.cardData.MonsterCardData.MonsterCard;
import model.cardData.MonsterCardData.MonsterCardAttribute;
import model.cardData.MonsterCardData.MonsterCardFamily;
import model.cardData.MonsterCardData.MonsterCardValue;
import model.cardData.SpellCardData.SpellCard;
import model.cardData.SpellCardData.SpellCardValue;
import model.cardData.TrapCardData.TrapCard;
import model.cardData.TrapCardData.TrapCardValue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DuelTestTexChangerEffect {
    public static void addCardsToFirstPlayer(ArrayList<Card> firstPlayerDeck) {
        //firstPlayerDeck.add(new MonsterCard(1000, 1000, 4, MonsterCardAttribute.DARK, MonsterCardFamily.AQUA, MonsterCardValue.NORMAL, "normalAQUA", "", null, 3, 0, null));
        firstPlayerDeck.add(new MonsterCard(2000, 0, 4, MonsterCardAttribute.EARTH, MonsterCardFamily.CYBERSE, MonsterCardValue.NORMAL, "leotron", "", null, 3, 0, null));
        firstPlayerDeck.add(new MonsterCard(100, 100, 1, MonsterCardAttribute.DARK, MonsterCardFamily.CYBERSE, MonsterCardValue.EFFECT, "texchanger", "", null, 3, 0, null));

        //firstPlayerDeck.add(new SpellCard("dark hole", "", SpellCardValue.NORMAL, null, 3, 60, 0, null));
        firstPlayerDeck.add(new TrapCard("call of the hunted", "", TrapCardValue.NORMAL, null, 3, 60, 0, null));
        //firstPlayerDeck.add(new SpellCard("terraforming", "", SpellCardValue.NORMAL, null, 3, 60, 0, null));
        firstPlayerDeck.add(new TrapCard("torrential tribute", "", null, null, 3, 60, 0, null));
        //firstPlayerDeck.add(new SpellCard("messenger of peace", "", SpellCardValue.CONTINUOUS, null, 3, 6, 0, null));

        firstPlayerDeck.add(new TrapCard("mirror force", "", TrapCardValue.NORMAL, null, 3, 60, 0, null));
        firstPlayerDeck.add(new MonsterCard(2000, 0, 4, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "alexandrite dragon", "", null, 3, 0, null));

        firstPlayerDeck.add(new MonsterCard(2500, 2400, 7, MonsterCardAttribute.DARK, MonsterCardFamily.FIEND, MonsterCardValue.NORMAL, "suijin", "", null, 3, 0, null));
        firstPlayerDeck.add(new SpellCard("umiruka", "", SpellCardValue.FIELD, null, 3, 60, 0, null));

        firstPlayerDeck.add(new MonsterCard(2000, 0, 4, MonsterCardAttribute.EARTH, MonsterCardFamily.CYBERSE, MonsterCardValue.NORMAL, "leotron", "", null, 3, 0, null));
        firstPlayerDeck.add(new MonsterCard(1300, 1500, 4, MonsterCardAttribute.DARK, MonsterCardFamily.FIEND, MonsterCardValue.NORMAL, "zenosfive", "", null, 3, 0, null));
        firstPlayerDeck.add(new MonsterCard(2000, 1500, 4, MonsterCardAttribute.DARK, MonsterCardFamily.FIEND, MonsterCardValue.NORMAL, "zenossix", "", null, 3, 0, null));
        firstPlayerDeck.add(new MonsterCard(10, 1500, 4, MonsterCardAttribute.DARK, MonsterCardFamily.FIEND, MonsterCardValue.NORMAL, "zenosseven", "", null, 3, 0, null));
        firstPlayerDeck.add(new MonsterCard(100, 100, 1, MonsterCardAttribute.DARK, MonsterCardFamily.CYBERSE, MonsterCardValue.EFFECT, "texchanger", "", null, 3, 0, null));

        firstPlayerDeck.add(new MonsterCard(2000, 1500, 4, MonsterCardAttribute.DARK, MonsterCardFamily.FIEND, MonsterCardValue.NORMAL, "zenoseight", "", null, 3, 0, null));
        firstPlayerDeck.add(new MonsterCard(2000, 1500, 3, MonsterCardAttribute.DARK, MonsterCardFamily.FIEND, MonsterCardValue.NORMAL, "zenosnine", "", null, 3, 0, null));
        firstPlayerDeck.add(new MonsterCard(2000, 1500, 2, MonsterCardAttribute.DARK, MonsterCardFamily.FIEND, MonsterCardValue.NORMAL, "zenosten", "", null, 3, 0, null));
        firstPlayerDeck.add(new MonsterCard(2000, 1500, 1, MonsterCardAttribute.DARK, MonsterCardFamily.FIEND, MonsterCardValue.NORMAL, "zenoseleven", "", null, 3, 0, null));
        firstPlayerDeck.add(new MonsterCard(2000, 1500, 2, MonsterCardAttribute.DARK, MonsterCardFamily.FIEND, MonsterCardValue.NORMAL, "zenostwelve", "", null, 3, 0, null));
        firstPlayerDeck.add(new MonsterCard(2000, 1500, 2, MonsterCardAttribute.DARK, MonsterCardFamily.FIEND, MonsterCardValue.NORMAL, "zenosthirteen", "", null, 3, 0, null));
        firstPlayerDeck.add(new MonsterCard(2000, 1500, 2, MonsterCardAttribute.DARK, MonsterCardFamily.FIEND, MonsterCardValue.NORMAL, "zenos14", "", null, 3, 0, null));
        firstPlayerDeck.add(new MonsterCard(2000, 1500, 1, MonsterCardAttribute.DARK, MonsterCardFamily.FIEND, MonsterCardValue.NORMAL, "zenos15", "", null, 3, 0, null));
        firstPlayerDeck.add(new MonsterCard(2000, 1500, 2, MonsterCardAttribute.DARK, MonsterCardFamily.FIEND, MonsterCardValue.NORMAL, "zenos16", "", null, 3, 0, null));
        firstPlayerDeck.add(new MonsterCard(2000, 1500, 2, MonsterCardAttribute.DARK, MonsterCardFamily.CYBERSE, MonsterCardValue.NORMAL, "zenos17", "", null, 3, 0, null));

    }

    public static void addCardsToSecondPlayer(ArrayList<Card> secondPlayerDeck) {
        //secondPlayerDeck.add(new TrapCard("torrential tribute", "", null, null, 3, 60, 0, null));
        secondPlayerDeck.add(new SpellCard("twin twisters", "", SpellCardValue.QUICK_PLAY, null, 3, 6, 0, null));

        secondPlayerDeck.add(new MonsterCard(1800, 1200, 4, MonsterCardAttribute.EARTH, MonsterCardFamily.WARRIOR, MonsterCardValue.EFFECT, "terratiger, the empowered warrior", "", null, 3, 0, null));

        secondPlayerDeck.add(new TrapCard("mirror force", "", null, null, 3, 60, 0, null));
        secondPlayerDeck.add(new MonsterCard(200, 2000, 2, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "bitron", "", null, 3, 0, null));

        secondPlayerDeck.add(new MonsterCard(300, 500, 3, MonsterCardAttribute.LIGHT, MonsterCardFamily.FAIRY, MonsterCardValue.EFFECT, "marshmallon", "", null, 3, 0, null));
        secondPlayerDeck.add(new MonsterCard(2000, 0, 4, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "alexandrite dragon", "", null, 3, 0, null));
        secondPlayerDeck.add(new MonsterCard(300, 1900, 4, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "exdeathfour", "", null, 3, 0, null));
        secondPlayerDeck.add(new MonsterCard(600, 1900, 4, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "exdeathfive", "", null, 3, 0, null));
        secondPlayerDeck.add(new MonsterCard(2050, 1900, 4, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "exdeathsix", "", null, 3, 0, null));
        secondPlayerDeck.add(new MonsterCard(1800, 1900, 4, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "exdeathseven", "", null, 3, 0, null));
        secondPlayerDeck.add(new MonsterCard(1800, 1900, 4, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "exdeatheight", "", null, 3, 0, null));
        secondPlayerDeck.add(new MonsterCard(1800, 1900, 4, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "exdeathnine", "", null, 3, 0, null));
        secondPlayerDeck.add(new MonsterCard(1800, 1900, 4, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "exdeathten", "", null, 3, 0, null));
        secondPlayerDeck.add(new MonsterCard(1800, 1900, 4, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "exdeatheleven", "", null, 3, 0, null));
        secondPlayerDeck.add(new MonsterCard(1800, 1900, 4, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "exdeathtwelve", "", null, 3, 0, null));

    }

    // @BeforeAll
    // static void startGame() {
    //     GameManager gameManager = new GameManager();
    //     ArrayList<Card> firstPlayerDeck = new ArrayList<>();
    //     ArrayList<Card> secondPlayerDeck = new ArrayList<>();
    //     addCardsToFirstPlayer(firstPlayerDeck);
    //     addCardsToSecondPlayer(secondPlayerDeck);
    //     gameManager.addANewGame(firstPlayerDeck, null, secondPlayerDeck, null, "zenos", "exdeath", 1);
    //     DuelController duelController = GameManager.getDuelControllerByIndex(0);
    //     duelController.getLifePoints().set(0, 8000);
    //     duelController.getLifePoints().set(1, 8000);
    //     duelController.setTurn(1);
    //     duelController.setFakeTurn(1);
    //     GameManager.getPhaseControllerByIndex(0).setPhaseInGame(PhaseInGame.ALLY_MAIN_PHASE_1);
    //     //duelController.startDuel(0);
    //     //System.out.println(duelController.startDuel(0));i
    //     //DuelBoard duelBoard = GameManager.getDuelBoardByIndex(0);
    //     //System.out.println(duelBoard.showDuelBoard(0));
    //     //boolean continueInput = true;
    //     //String input;
    // }

    @Test
    void battle() {
        //Effect effect = new Effect();
        ArrayList<Integer> monsterCardLevels = new ArrayList<>();
        String output = "";
        DuelController duelController = GameManager.getDuelControllerByIndex(0);
        output = duelController.getInput("select --hand 2", true);
        assertEquals("card selected", output);
        output = duelController.getInput("set", true);
        assertEquals("set successfully\n", output);
        output = duelController.getInput("select --hand 2", true);
        output = duelController.getInput("set", true);
        assertEquals("set successfully\n", output);
        duelController.getInput("next phase", true);
        duelController.getInput("next phase", true);
        duelController.getInput("next phase", true);
        duelController.getInput("select --hand 1", true);
        duelController.getInput("set", true);
        duelController.getInput("select --hand 1", true);
        output = duelController.getInput("normal summon", true);
        assertEquals("summoned successfullydo you want to activate your monster card's effect?\n", output);
        duelController.getInput("yes", true);
        assertEquals("monster special summoned successfully\n\n", duelController.getInput("select --hand 2", true));
        duelController.getInput("next phase", true);
        duelController.getInput("select --monster 1", true);
        output = duelController.getInput("attack 1", true);
        output = duelController.getInput("yes", true);
        output = duelController.getInput("select --hand 2", true);
        assertEquals("this card cannot be chosen for special summon.", output);
        output = duelController.getInput("select --hand 3", true);
        assertEquals("this card cannot be chosen for special summon.", output);
        //output = duelController.getInput("select --deck 4", true);
        //assertEquals("this monster is not from cyberse family.\nselect another monster.", output);
        output = duelController.getInput("select --deck 8", true);
        assertEquals("this is not a normal monster.\nselect another monster.", output);
        duelController.getInput("select --hand 1", true);
        duelController.getInput("attacking", true);
        duelController.getInput("select --monster 1", true);
        output = duelController.getInput("attack 1", true);
        assertEquals("this card already attacked", output);
        duelController.getInput("next phase", true);
        duelController.getInput("select --monster 2", true);
        //output = duelController.getInput("set --position attack", true);
        //assertEquals("wvh", output);
        duelController.getInput("next phase", true);
        duelController.getInput("select --monster 1", true);
        output = duelController.getInput("set --position attack", true);
        //assertEquals("j", output);
        duelController.getInput("next phase", true);
        duelController.getInput("select --monster 2", true);
        output = duelController.getInput("attack 2", true);
        //ATTACKING TO BITRON
        //assertEquals("j", output);
        duelController.getInput("select --monster 1", true);
        output = duelController.getInput("attack 2", true);
        //TEX ATTACKS BITRON
        //assertEquals("j", output);
        //OPPONENT TURN
        duelController.getInput("next phase", true);
        duelController.getInput("next phase", true);
        duelController.getInput("next phase", true);
        output = duelController.getInput("select --monster 1", true);
        output = duelController.getInput("attack 1", true);
        output = duelController.getInput("yes", true);
        output = duelController.getInput("select --deck 3", true);
        output = duelController.getInput("defendsfdsffdssive", true);
        assertEquals("invalid command!", output);
        output = duelController.getInput("defensive", true);
        //assertEquals("dsf", output);
    }
}
