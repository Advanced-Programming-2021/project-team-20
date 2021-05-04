package CardEffects.EffectImplementations;

import CardData.General.Card;
import CardData.General.CardLocation;
import CardData.General.RowOfCardLocation;
import CardData.MonsterCardData.MonsterCard;
import CardData.MonsterCardData.MonsterCardAttribute;
import CardData.MonsterCardData.MonsterCardFamily;
import CardData.MonsterCardData.MonsterCardValue;
import CardData.SpellCardData.SpellCard;
import CardData.SpellCardData.SpellCardValue;
import CardData.TrapCardData.TrapCard;
import CardEffects.MonsterEffectEnums.SummoningRequirement;
import CardEffects.SpellEffectEnums.LogicalActivationRequirement;
import CardEffects.SpellEffectEnums.QuickSpellEffect;
import CardEffects.SpellEffectEnums.UserReplyForActivation;
import CardEffects.TrapEffectEnums.*;
import GamePackage.Action;
import GamePackage.ActionType;
import GamePackage.DuelBoard;
import GamePackage.DuelController;
import PreliminaryPackage.GameManager;

import java.util.ArrayList;

public class Effect {

    public static MessagesFromEffectToControllers canMonsterBeNormalSummonedOrSet(Card card, DuelBoard duelBoard, int turn, String string) {
        if (!Card.isCardAMonster(card)) {
            return MessagesFromEffectToControllers.IT_IS_NOT_A_MONSTER_CARD;
        }
        MonsterCard monsterCard = (MonsterCard) card;
        ArrayList<SummoningRequirement> cardSummoningRequirements = monsterCard.getSummoningRequirements();
        if (!cardSummoningRequirements.contains(SummoningRequirement.CAN_BE_NORMAL_SUMMONED)) {
            if (string.equals("normal summon")) {
                return MessagesFromEffectToControllers.CANT_BE_NORMAL_SUMMONED;
            } else if (string.equals("set")) {
                return MessagesFromEffectToControllers.CANT_BE_SET;
            }
        } else {
            ArrayList<Card> cardsInMonsterZone = null;
            if (turn == 1) {
                cardsInMonsterZone = duelBoard.getAllyMonsterCards();
            } else if (turn == 2) {
                cardsInMonsterZone = duelBoard.getOpponentMonsterCards();
            }
            int monstersForTribute = 0;
            for (int i = 0; i < 5; i++) {
                if (cardsInMonsterZone.get(i) != null) {
                    monstersForTribute++;
                }
            }
            return checkNumberOfMonstersToTribute(cardSummoningRequirements, monstersForTribute);
        }
        return null;
    }

    public static MessagesFromEffectToControllers checkNumberOfMonstersToTribute(ArrayList<SummoningRequirement> summoningRequirements, int monstersForTribute) {
        if (summoningRequirements.contains(SummoningRequirement.TRIBUTE_1_MONSTER) && monstersForTribute >= 1) {
            return MessagesFromEffectToControllers.PLEASE_CHOOSE_1_MONSTER_TO_TRIBUTE;
        } else if (summoningRequirements.contains(SummoningRequirement.TRIBUTE_1_MONSTER) && monstersForTribute == 0) {
            return MessagesFromEffectToControllers.THERE_ARE_NOT_ENOUGH_CARDS_FOR_TRIBUTE;
        } else if (summoningRequirements.contains(SummoningRequirement.TRIBUTE_2_MONSTERS) && monstersForTribute >= 2) {
            return MessagesFromEffectToControllers.PLEASE_CHOOSE_2_MONSTERS_TO_TRIBUTE;
        } else if (summoningRequirements.contains(SummoningRequirement.TRIBUTE_2_MONSTERS) && monstersForTribute <= 1) {
            return MessagesFromEffectToControllers.THERE_ARE_NOT_ENOUGH_CARDS_FOR_TRIBUTE;
        } else if (summoningRequirements.contains(SummoningRequirement.TRIBUTE_3_MONSTERS) && monstersForTribute >= 3) {
            return MessagesFromEffectToControllers.PLEASE_CHOOSE_3_MONSTERS_TO_TRIBUTE;
        } else if (summoningRequirements.contains(SummoningRequirement.TRIBUTE_3_MONSTERS) && monstersForTribute <= 2) {
            return MessagesFromEffectToControllers.THERE_ARE_NOT_ENOUGH_CARDS_FOR_TRIBUTE;
        }
        return MessagesFromEffectToControllers.YOU_CAN_NORMAL_SUMMON_THIS_MONSTER;
    }

    public static MessagesFromEffectToControllers canSpellTrapCardBeActivatedInChain(ActionType actionType, DuelBoard duelBoard, int actionTurn) {
        // This function also checks if preparations are complete and says yes if everything was ok
        MessagesFromEffectToControllers messagesFromEffectToControllers = null;
        if (actionType.equals(ActionType.ALLY_MONSTER_ATTACKING_OPPONENT_MONSTER) && actionTurn == 1) {
            System.out.println("C1");
            ArrayList<Card> opponentSpellTrapCards = duelBoard.getOpponentSpellCards();
            messagesFromEffectToControllers = areCardsAvailableForActivatingSpellTrapCardInMonsterAttackingInChain(opponentSpellTrapCards);
            return iterateThroughAllyOrOpponentSpellTrapCardsForCanSpellTrapCardBeActivatedInChain(messagesFromEffectToControllers, opponentSpellTrapCards, "opponent");
        } else if (actionType.equals(ActionType.OPPONENT_MONSTER_ATTACKING_ALLY_MONSTER) && actionTurn == 2) {
            System.out.println("C2");
            ArrayList<Card> allySpellTrapCards = duelBoard.getAllySpellCards();
            /*
            for (int i = 0; i < allySpellTrapCards.size(); i++) {
                if (allySpellTrapCards.get(i) != null) {
                    System.out.println(allySpellTrapCards.get(i).getCardName());
                } else {
                    System.out.println("null spell card");
                }
            }

             */
            messagesFromEffectToControllers = areCardsAvailableForActivatingSpellTrapCardInMonsterAttackingInChain(allySpellTrapCards);
            return iterateThroughAllyOrOpponentSpellTrapCardsForCanSpellTrapCardBeActivatedInChain(messagesFromEffectToControllers, allySpellTrapCards, "ally");
        } else if (actionType.equals(ActionType.ALLY_DIRECT_ATTACKING) && actionTurn == 1) {
            System.out.println("C3");
            ArrayList<Card> opponentSpellTrapCards = duelBoard.getOpponentSpellCards();
            messagesFromEffectToControllers = areCardsAvailableForActivatingSpellTrapCardInMonsterAttackingInChain(opponentSpellTrapCards);
            return iterateThroughAllyOrOpponentSpellTrapCardsForCanSpellTrapCardBeActivatedInChain(messagesFromEffectToControllers, opponentSpellTrapCards, "opponent");
        } else if (actionType.equals(ActionType.OPPONENT_DIRECT_ATTACKING) && actionTurn == 2) {
            System.out.println("C4");
            ArrayList<Card> allySpellTrapCards = duelBoard.getAllySpellCards();
            messagesFromEffectToControllers = areCardsAvailableForActivatingSpellTrapCardInMonsterAttackingInChain(allySpellTrapCards);
            return iterateThroughAllyOrOpponentSpellTrapCardsForCanSpellTrapCardBeActivatedInChain(messagesFromEffectToControllers, allySpellTrapCards, "ally");
        }else if ((actionType.equals(ActionType.ALLY_ACTIVATING_SPELL) || actionType.equals(ActionType.ALLY_ACTIVATING_TRAP)) && actionTurn == 1) {
            System.out.println("C5");
            ArrayList<Card> opponentSpellTrapCards = duelBoard.getOpponentSpellCards();
            messagesFromEffectToControllers = areCardsAvailableForActivatingSpellTrapCardInSpellTrapActivatingInChain(opponentSpellTrapCards, actionType);
            return iterateThroughAllyOrOpponentSpellTrapCardsForCanSpellTrapCardBeActivatedInChain(messagesFromEffectToControllers, opponentSpellTrapCards, "ally");
        } else if ((actionType.equals(ActionType.OPPONENT_ACTIVATING_SPELL) || actionType.equals(ActionType.OPPONENT_ACTIVATING_TRAP)) && actionTurn == 2) {
            System.out.println("C6");
            ArrayList<Card> allySpellTrapCards = duelBoard.getAllySpellCards();
            messagesFromEffectToControllers = areCardsAvailableForActivatingSpellTrapCardInSpellTrapActivatingInChain(allySpellTrapCards, actionType);
            return iterateThroughAllyOrOpponentSpellTrapCardsForCanSpellTrapCardBeActivatedInChain(messagesFromEffectToControllers, allySpellTrapCards, "ally");
        } else if (actionType.equals(ActionType.ALLY_NORMAL_SUMMONING_MONSTER) && actionTurn == 1) {
            System.out.println("C7");
            ArrayList<Card> opponentSpellTrapCards = duelBoard.getOpponentSpellCards();
            /*
            for (int i = 0; i < opponentSpellTrapCards.size(); i++) {
                if (opponentSpellTrapCards.get(i) != null) {
                    System.out.println(opponentSpellTrapCards.get(i).getCardName());
                } else {
                    System.out.println("null spell card");
                }
            }

             */
            messagesFromEffectToControllers = areCardsAvailableForActivatingSpellTrapCardInMonsterNormalSummoningInChain(opponentSpellTrapCards, actionType);
            return iterateThroughAllyOrOpponentSpellTrapCardsForCanSpellTrapCardBeActivatedInChain(messagesFromEffectToControllers, opponentSpellTrapCards, "opponent");
        } else if (actionType.equals(ActionType.ALLY_FLIP_SUMMONING_MONSTER) && actionTurn == 1) {
            System.out.println("C8");
            ArrayList<Card> opponentSpellTrapCards = duelBoard.getOpponentSpellCards();
            messagesFromEffectToControllers = areCardsAvailableForActivatingSpellTrapCardInMonsterFlipSummoningInChain(opponentSpellTrapCards, actionType);
            return iterateThroughAllyOrOpponentSpellTrapCardsForCanSpellTrapCardBeActivatedInChain(messagesFromEffectToControllers, opponentSpellTrapCards, "opponent");
        } else if (actionType.equals(ActionType.ALLY_SPECIAL_SUMMONING_MONSTER) && actionTurn == 1) {
            System.out.println("C9");
            ArrayList<Card> opponentSpellTrapCards = duelBoard.getOpponentSpellCards();
            messagesFromEffectToControllers = areCardsAvailableForActivatingSpellTrapCardInMonsterSpecialSummoningInChain(opponentSpellTrapCards, actionType);
            return iterateThroughAllyOrOpponentSpellTrapCardsForCanSpellTrapCardBeActivatedInChain(messagesFromEffectToControllers, opponentSpellTrapCards, "opponent");
        } else if (actionType.equals(ActionType.ALLY_RITUAL_SUMMONING_MONSTER) && actionTurn == 1) {
            System.out.println("C10");
            ArrayList<Card> opponentSpellTrapCards = duelBoard.getOpponentSpellCards();
            messagesFromEffectToControllers = areCardsAvailableForActivatingSpellTrapCardInMonsterRitualSummoningInChain(opponentSpellTrapCards, actionType);
            return iterateThroughAllyOrOpponentSpellTrapCardsForCanSpellTrapCardBeActivatedInChain(messagesFromEffectToControllers, opponentSpellTrapCards, "opponent");
        } else if (actionType.equals(ActionType.OPPONENT_NORMAL_SUMMONING_MONSTER) && actionTurn == 2) {
            System.out.println("C11");
            ArrayList<Card> allySpellTrapCards = duelBoard.getAllySpellCards();
            messagesFromEffectToControllers = areCardsAvailableForActivatingSpellTrapCardInMonsterNormalSummoningInChain(allySpellTrapCards, actionType);
            return iterateThroughAllyOrOpponentSpellTrapCardsForCanSpellTrapCardBeActivatedInChain(messagesFromEffectToControllers, allySpellTrapCards, "ally");
        } else if (actionType.equals(ActionType.OPPONENT_FLIP_SUMMONING_MONSTER) && actionTurn == 2) {
            System.out.println("C12");
            ArrayList<Card> allySpellTrapCards = duelBoard.getAllySpellCards();
            messagesFromEffectToControllers = areCardsAvailableForActivatingSpellTrapCardInMonsterFlipSummoningInChain(allySpellTrapCards, actionType);
            return iterateThroughAllyOrOpponentSpellTrapCardsForCanSpellTrapCardBeActivatedInChain(messagesFromEffectToControllers, allySpellTrapCards, "ally");
        } else if (actionType.equals(ActionType.OPPONENT_SPECIAL_SUMMONING_MONSTER) && actionTurn == 2) {
            System.out.println("C13");
            ArrayList<Card> allySpellTrapCards = duelBoard.getAllySpellCards();
            messagesFromEffectToControllers = areCardsAvailableForActivatingSpellTrapCardInMonsterSpecialSummoningInChain(allySpellTrapCards, actionType);
            return iterateThroughAllyOrOpponentSpellTrapCardsForCanSpellTrapCardBeActivatedInChain(messagesFromEffectToControllers, allySpellTrapCards, "ally");
        } else if (actionType.equals(ActionType.OPPONENT_RITUAL_SUMMONING_MONSTER) && actionTurn == 2) {
            System.out.println("C14");
            ArrayList<Card> allySpellTrapCards = duelBoard.getAllySpellCards();
            messagesFromEffectToControllers = areCardsAvailableForActivatingSpellTrapCardInMonsterRitualSummoningInChain(allySpellTrapCards, actionType);
            return iterateThroughAllyOrOpponentSpellTrapCardsForCanSpellTrapCardBeActivatedInChain(messagesFromEffectToControllers, allySpellTrapCards, "ally");
        }
        System.out.println("C15");
        return MessagesFromEffectToControllers.SPELL_TRAP_CARD_CANT_BE_ACTIVATED_IN_CHAIN;
    }

    private static MessagesFromEffectToControllers iterateThroughAllyOrOpponentSpellTrapCardsForCanSpellTrapCardBeActivatedInChain(MessagesFromEffectToControllers messagesFromEffectToControllers, ArrayList<Card> allyOrOpponentSpellTrapCards, String allyOrOpponent) {
        //This function also calls are preparations complete if the card was available and returns the final answer
        if (messagesFromEffectToControllers.equals(MessagesFromEffectToControllers.SPELL_TRAP_CARD_CANT_BE_ACTIVATED_IN_CHAIN)) {
            return messagesFromEffectToControllers;
        } else {
            for (int i = 0; i < allyOrOpponentSpellTrapCards.size(); i++) {
                CardLocation cardLocation = null;
                if (allyOrOpponent.equals("ally")) {
                    cardLocation = new CardLocation(RowOfCardLocation.ALLY_SPELL_ZONE, i + 1);
                } else {
                    cardLocation = new CardLocation(RowOfCardLocation.OPPONENT_SPELL_ZONE, i + 1);
                }
                messagesFromEffectToControllers = arePreparationsCompleteForActivatingSpellTrapCard(cardLocation, 0);
                if (messagesFromEffectToControllers != null){
                    if (messagesFromEffectToControllers.equals(MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_SPELL_ARE_COMPLETE) || messagesFromEffectToControllers.equals(MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_TRAP_ARE_COMPLETE)) {
                        return MessagesFromEffectToControllers.SPELL_TRAP_CARD_CAN_BE_ACTIVATED_IN_CHAIN;
                    }
                }
            }
            return MessagesFromEffectToControllers.SPELL_TRAP_CARD_CANT_BE_ACTIVATED_IN_CHAIN;
        }
    }

    private static MessagesFromEffectToControllers areCardsAvailableForActivatingSpellTrapCardInSpellTrapActivatingInChain(ArrayList<Card> allySpellTrapCards, ActionType actionType) {
        //This function has a bit of errors because i haven't considered speed of cards correctly
        for (int i = 0; i < allySpellTrapCards.size(); i++) {
            if (Card.isCardASpell(allySpellTrapCards.get(i))) {
                ArrayList<QuickSpellEffect> quickSpellEffects = ((SpellCard) allySpellTrapCards.get(i)).getQuickSpellEffects();
                if (quickSpellEffects.contains(QuickSpellEffect.TARGET_1_SPELL_TRAP_CARD_AND_DESTROY)) {
                    return MessagesFromEffectToControllers.SPELL_TRAP_CARD_CAN_BE_ACTIVATED_IN_CHAIN;
                } else if (quickSpellEffects.contains(QuickSpellEffect.DISCARD_1_CARD_THEN_TARGET_UP_TO_2_SPELL_CARDS_AND_DESTROY)) {
                    return MessagesFromEffectToControllers.SPELL_TRAP_CARD_CAN_BE_ACTIVATED_IN_CHAIN;
                }
            } else if (Card.isCardATrap(allySpellTrapCards.get(i))) {
                ArrayList<SpellCardActivationTrapCardEffect> spellCardActivationTrapCardEffects = ((TrapCard) allySpellTrapCards.get(i)).getSpellCardActivationTrapCardEffects();
                if (spellCardActivationTrapCardEffects.size() > 0 && (actionType.equals(ActionType.ALLY_ACTIVATING_SPELL) || actionType.equals(ActionType.OPPONENT_ACTIVATING_SPELL))) {
                    return MessagesFromEffectToControllers.SPELL_TRAP_CARD_CAN_BE_ACTIVATED_IN_CHAIN;
                }
            }
        }
        return MessagesFromEffectToControllers.SPELL_TRAP_CARD_CANT_BE_ACTIVATED_IN_CHAIN;
    }

    private static MessagesFromEffectToControllers areCardsAvailableForActivatingSpellTrapCardInMonsterAttackingInChain(ArrayList<Card> allySpellTrapCards) {
        for (int i = 0; i < allySpellTrapCards.size(); i++) {
            if (Card.isCardASpell(allySpellTrapCards.get(i))) {
                ArrayList<QuickSpellEffect> quickSpellEffects = ((SpellCard) allySpellTrapCards.get(i)).getQuickSpellEffects();
                if (quickSpellEffects.contains(QuickSpellEffect.TARGET_1_SPELL_TRAP_CARD_AND_DESTROY)) {
                    return MessagesFromEffectToControllers.SPELL_TRAP_CARD_CAN_BE_ACTIVATED_IN_CHAIN;
                } else if (quickSpellEffects.contains(QuickSpellEffect.DISCARD_1_CARD_THEN_TARGET_UP_TO_2_SPELL_CARDS_AND_DESTROY)) {
                    return MessagesFromEffectToControllers.SPELL_TRAP_CARD_CAN_BE_ACTIVATED_IN_CHAIN;
                }
            } else if (Card.isCardATrap(allySpellTrapCards.get(i))) {
                ArrayList<MonsterAttackingTrapCardEffect> monsterAttackingTrapCardEffects = ((TrapCard) allySpellTrapCards.get(i)).getMonsterAttackingTrapCardEffects();
                if (monsterAttackingTrapCardEffects.size() > 0) {
                    return MessagesFromEffectToControllers.SPELL_TRAP_CARD_CAN_BE_ACTIVATED_IN_CHAIN;
                }
            }
        }
        return MessagesFromEffectToControllers.SPELL_TRAP_CARD_CANT_BE_ACTIVATED_IN_CHAIN;
    }

    private static MessagesFromEffectToControllers areCardsAvailableForActivatingSpellTrapCardInMonsterNormalSummoningInChain(ArrayList<Card> spellTrapCards, ActionType actionType) {
        for (int i = 0; i < spellTrapCards.size(); i++) {
            if (spellTrapCards.get(i) != null && Card.isCardATrap(spellTrapCards.get(i))) {
                ArrayList<NormalSummonTrapCardEffect> normalSummonTrapCardEffects = ((TrapCard) spellTrapCards.get(i)).getNormalSummonTrapCardEffects();
                if (normalSummonTrapCardEffects.size() > 0 && (actionType.equals(ActionType.ALLY_NORMAL_SUMMONING_MONSTER) || actionType.equals(ActionType.OPPONENT_NORMAL_SUMMONING_MONSTER))) {
                    return MessagesFromEffectToControllers.SPELL_TRAP_CARD_CAN_BE_ACTIVATED_IN_CHAIN;
                }
            }
        }
        return MessagesFromEffectToControllers.SPELL_TRAP_CARD_CANT_BE_ACTIVATED_IN_CHAIN;
    }

    private static MessagesFromEffectToControllers areCardsAvailableForActivatingSpellTrapCardInMonsterFlipSummoningInChain(ArrayList<Card> spellTrapCards, ActionType actionType) {
        for (int i = 0; i < spellTrapCards.size(); i++) {
            if (Card.isCardATrap(spellTrapCards.get(i))) {
                ArrayList<FlipSummonTrapCardEffect> flipSummonTrapCardEffects = ((TrapCard) spellTrapCards.get(i)).getFlipSummonTrapCardEffects();
                if (flipSummonTrapCardEffects.size() > 0 && (actionType.equals(ActionType.ALLY_FLIP_SUMMONING_MONSTER) || actionType.equals(ActionType.OPPONENT_FLIP_SUMMONING_MONSTER))) {
                    return MessagesFromEffectToControllers.SPELL_TRAP_CARD_CAN_BE_ACTIVATED_IN_CHAIN;
                }
            }
        }
        return MessagesFromEffectToControllers.SPELL_TRAP_CARD_CANT_BE_ACTIVATED_IN_CHAIN;
    }

    private static MessagesFromEffectToControllers areCardsAvailableForActivatingSpellTrapCardInMonsterSpecialSummoningInChain(ArrayList<Card> spellTrapCards, ActionType actionType) {
        for (int i = 0; i < spellTrapCards.size(); i++) {
            if (Card.isCardATrap(spellTrapCards.get(i))) {
                ArrayList<SpecialSummonTrapCardEffect> specialSummonTrapCardEffects = ((TrapCard) spellTrapCards.get(i)).getSpecialSummonTrapCardEffects();
                if (specialSummonTrapCardEffects.size() > 0 && (actionType.equals(ActionType.ALLY_SPECIAL_SUMMONING_MONSTER) || actionType.equals(ActionType.OPPONENT_SPECIAL_SUMMONING_MONSTER))) {
                    return MessagesFromEffectToControllers.SPELL_TRAP_CARD_CAN_BE_ACTIVATED_IN_CHAIN;
                }
            }
        }
        return MessagesFromEffectToControllers.SPELL_TRAP_CARD_CANT_BE_ACTIVATED_IN_CHAIN;
    }

    private static MessagesFromEffectToControllers areCardsAvailableForActivatingSpellTrapCardInMonsterRitualSummoningInChain(ArrayList<Card> spellTrapCards, ActionType actionType) {
        for (int i = 0; i < spellTrapCards.size(); i++) {
            if (Card.isCardATrap(spellTrapCards.get(i))) {
                ArrayList<RitualSummonTrapCardEffect> ritualSummonTrapCardEffects = ((TrapCard) spellTrapCards.get(i)).getRitualSummonTrapCardEffects();
                if (ritualSummonTrapCardEffects.size() > 0 && (actionType.equals(ActionType.ALLY_RITUAL_SUMMONING_MONSTER) || actionType.equals(ActionType.OPPONENT_RITUAL_SUMMONING_MONSTER))) {
                    return MessagesFromEffectToControllers.SPELL_TRAP_CARD_CAN_BE_ACTIVATED_IN_CHAIN;
                }
            }
        }
        return MessagesFromEffectToControllers.SPELL_TRAP_CARD_CANT_BE_ACTIVATED_IN_CHAIN;
    }

    public static MessagesFromEffectToControllers arePreparationsCompleteForActivatingSpellTrapCard(CardLocation cardLocation, int index) {
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        Card card = duelBoard.getCardByCardLocation(cardLocation);
        if (card == null) {
            System.out.println("the card we are checking its preparations is null");
        } else {
            System.out.println("the card we are checking its preparations has name " + card.getCardName());
        }
        int turn = duelController.getTurn();
        int fakeTurn = duelController.getFakeTurn();
        boolean preparationsAreComplete = true;
        if (Card.isCardASpell(card)) {
            SpellCard spellCard = (SpellCard) card;
            ArrayList<LogicalActivationRequirement> logicalActivationRequirements = spellCard.getLogicalActivationRequirements();
            if (logicalActivationRequirements.contains(LogicalActivationRequirement.MUST_EXIST_AT_LEAST_1_MONSTER_IN_EITHER_GY)) {
                if (!logicalActivationRequirementSpellMustExistAtLeastOneMonsterInEitherGraveyard(duelBoard, fakeTurn)) {
                    return MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_SPELL_ARE_NOT_COMPLETE;
                }
            }
            if (logicalActivationRequirements.contains(LogicalActivationRequirement.OWNER_MUST_CONTROL_WARRIOR_MONSTER)) {
                if (!logicalActivationRequirementSpellOwnerMustControlSpecificMonsterFamily(duelBoard, fakeTurn, MonsterCardFamily.WARRIOR, null)) {
                    return MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_SPELL_ARE_NOT_COMPLETE;
                }
            }
            if (logicalActivationRequirements.contains(LogicalActivationRequirement.OWNER_MUST_CONTROL_DARK_MONSTER)) {
                if (!logicalActivationRequirementSpellOwnerMustControlSpecificMonsterFamily(duelBoard, fakeTurn, null, MonsterCardAttribute.DARK)) {
                    return MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_SPELL_ARE_NOT_COMPLETE;
                }
            }
            if (logicalActivationRequirements.contains(LogicalActivationRequirement.OWNER_MUST_HAVE_ONE_SPELL_FIELD_CARD_IN_DECK)) {
                if (!logicalActivationRequirementSpellOwnerMustHaveAtLeast1SpellFieldCardInDeck(duelBoard, fakeTurn)) {
                    return MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_SPELL_ARE_NOT_COMPLETE;
                }
            }
            if (logicalActivationRequirements.contains(LogicalActivationRequirement.OPPONENT_MUST_CONTROL_AT_LEAST_1_MONSTER)) {
                if (!logicalActivationRequirementSpellOpponentMustControlAtLeast1Monster(duelBoard, fakeTurn)) {
                    return MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_SPELL_ARE_NOT_COMPLETE;
                }
            }
            if (logicalActivationRequirements.contains(LogicalActivationRequirement.OWNER_MUST_HAVE_AT_LEAST_ONE_CARD_IN_HAND)) {
                if (!logicalActivationRequirementSpellOwnerMustHaveAtLeast1CardInHand(duelBoard, fakeTurn)) {
                    return MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_SPELL_ARE_NOT_COMPLETE;
                }
            }
            if (logicalActivationRequirements.contains(LogicalActivationRequirement.TRAP_CARD_INFLICTING_DAMAGE_MUST_BE_ACTIVATED)) {
                if (!logicalActivationRequirementSpellTrapCardInflictingDamageIsActivated(index, fakeTurn)) {
                    return MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_SPELL_ARE_NOT_COMPLETE;
                }
            }
            if (logicalActivationRequirements.contains(LogicalActivationRequirement.OWNER_MUST_HAVE_RITUAL_MONSTER_IN_HAND)) {
                if (!logicalActivationRequirementSpellOwnerMustHaveAtLeast1RitualMonsterInHand(duelBoard, fakeTurn)) {
                    return MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_SPELL_ARE_NOT_COMPLETE;
                }
            }
            if (logicalActivationRequirements.contains(LogicalActivationRequirement.OWNER_MUST_HAVE_CARDS_WITH_SUM_OF_LEVELS_AT_LEAST_RITUAL_MONSTERS_LEVEL_IN_DECK)) {
                if (!logicalActivationRequirementSpellOwnerMustHaveNormalMonstersWithSumOfLevelsEqualToLevelOfRitualMonster(index, fakeTurn)) {
                    return MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_SPELL_ARE_NOT_COMPLETE;
                }
            }
            return MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_SPELL_ARE_COMPLETE;
        } else if (Card.isCardATrap(card)) {
            TrapCard trapCard = (TrapCard) card;
            ArrayList<CardEffects.TrapEffectEnums.LogicalActivationRequirement> logicalActivationRequirements = trapCard.getLogicalActivationRequirements();
            if (logicalActivationRequirements.contains(CardEffects.TrapEffectEnums.LogicalActivationRequirement.OWNER_MUST_HAVE_AT_LEAST_ONE_CARD_IN_HAND)) {
                if (!logicalActivationRequirementSpellOwnerMustHaveAtLeast1CardInHand(duelBoard, fakeTurn)) {
                    return MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_TRAP_ARE_NOT_COMPLETE;
                }
            }
            if (logicalActivationRequirements.contains(CardEffects.TrapEffectEnums.LogicalActivationRequirement.OWNER_MUST_HAVE_AT_LEAST_ONE_MONSTER_IN_THEIR_GY)) {
                if (!logicalActivationRequirementTrapOwnerMustHaveAtLeast1MonsterInTheirGraveyard(duelBoard, fakeTurn)) {
                    return MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_TRAP_ARE_NOT_COMPLETE;
                }
            }
            if (logicalActivationRequirements.contains(CardEffects.TrapEffectEnums.LogicalActivationRequirement.NORMAL_SUMMONED_MONSTER_MUST_HAVE_AT_LEAST_1000_ATK)) {
                if (!logicalActivationRequirementTrapSummonedMonsterMustHaveAtLeast1000ATK(index)) {
                    return MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_TRAP_ARE_NOT_COMPLETE;
                }
            }
            if (logicalActivationRequirements.contains(CardEffects.TrapEffectEnums.LogicalActivationRequirement.FLIP_SUMMONED_MONSTER_MUST_HAVE_AT_LEAST_1000_ATK)) {
                if (!logicalActivationRequirementTrapSummonedMonsterMustHaveAtLeast1000ATK(index)) {
                    return MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_TRAP_ARE_NOT_COMPLETE;
                }
            }
            if (logicalActivationRequirements.contains(CardEffects.TrapEffectEnums.LogicalActivationRequirement.MONSTER_IS_SUMMONED_OR_SPELL_TRAP_MONSTER_EFFECT_INCLUDING_SPECIAL_SUMMONING)) {
                if (!logicalActivationRequirementTrapMonsterIsSummonedOrSpellTrapMonsterEffectIncludingSpecialSummoningIsActivated(index)) {
                    return MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_TRAP_ARE_NOT_COMPLETE;
                }
            }
            return MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_TRAP_ARE_COMPLETE;
        }
        System.out.println("this card is not a spell or trap and i am returning null");
        return null;
    }

    //public static MessagesFromEffectToControllers arePreparationsCompleteForActivatingMonsterCardEffect()

    public static boolean logicalActivationRequirementSpellMustExistAtLeastOneMonsterInEitherGraveyard(DuelBoard duelBoard, int fakeTurn) {
        ArrayList<Card> cardsInAllyGraveyard = duelBoard.getAllyCardsInGraveyard();
        ArrayList<Card> cardsInOpponentGraveyard = duelBoard.getOpponentCardsInGraveyard();
        if (cardsInAllyGraveyard.size() + cardsInOpponentGraveyard.size() == 0) {
            return false;
        }
        return true;
    }

    public static boolean logicalActivationRequirementSpellOwnerMustControlSpecificMonsterFamily(DuelBoard duelBoard, int fakeTurn, MonsterCardFamily monsterCardFamily, MonsterCardAttribute monsterCardAttribute) {
        ArrayList<Card> cardsInOwnerMonsterField = null;
        if (fakeTurn == 1) {
            cardsInOwnerMonsterField = duelBoard.getAllyMonsterCards();
        } else if (fakeTurn == 2) {
            cardsInOwnerMonsterField = duelBoard.getOpponentMonsterCards();
        }
        for (int i = 0; i < cardsInOwnerMonsterField.size(); i++) {
            if (cardsInOwnerMonsterField.get(i) != null) {
                MonsterCard monsterCard = (MonsterCard) cardsInOwnerMonsterField.get(i);
                if (monsterCardAttribute == null && monsterCard.getMonsterCardFamily().equals(monsterCardFamily)) {
                    return true;
                }
                if (monsterCardFamily == null && monsterCard.getMonsterCardAttribute().equals(monsterCardAttribute)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean logicalActivationRequirementSpellOwnerMustHaveAtLeast1SpellFieldCardInDeck(DuelBoard duelBoard, int fakeTurn) {
        ArrayList<Card> cardsInOwnerDeck = null;
        if (fakeTurn == 1) {
            cardsInOwnerDeck = duelBoard.getAllyCardsInDeck();
        } else if (fakeTurn == 2) {
            cardsInOwnerDeck = duelBoard.getOpponentCardsInDeck();
        }
        for (int i = 0; i < cardsInOwnerDeck.size(); i++) {
            Card card = cardsInOwnerDeck.get(i);
            if (Card.isCardASpell(card)) {
                SpellCard spellCard = (SpellCard) card;
                if (spellCard.getSpellCardValue().equals(SpellCardValue.FIELD)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean logicalActivationRequirementSpellOpponentMustControlAtLeast1Monster(DuelBoard duelBoard, int fakeTurn) {
        ArrayList<Card> cardsInOwnerMonsterField = null;
        if (fakeTurn == 1) {
            cardsInOwnerMonsterField = duelBoard.getOpponentMonsterCards();
        } else if (fakeTurn == 2) {
            cardsInOwnerMonsterField = duelBoard.getAllyMonsterCards();
        }
        return cardsInOwnerMonsterField.size() > 0;
    }

    public static boolean logicalActivationRequirementSpellOwnerMustHaveAtLeast1CardInHand(DuelBoard duelBoard, int fakeTurn) {
        ArrayList<Card> cardsInOwnerHand = null;
        if (fakeTurn == 1) {
            cardsInOwnerHand = duelBoard.getAllyCardsInHand();
        } else if (fakeTurn == 2) {
            cardsInOwnerHand = duelBoard.getOpponentCardsInHand();
        }
        return cardsInOwnerHand.size() > 0;
    }

    public static boolean logicalActivationRequirementSpellTrapCardInflictingDamageIsActivated(int index, int fakeTurn) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        ArrayList<Action> actions = GameManager.getActionsByIndex(index);
        if (actions.size() == 0) {
            return false;
        }
        Action action = actions.get(actions.size() - 1);
        Card card = duelBoard.getCardByCardLocation(action.getMainCardLocation());
        if (Card.isCardATrap(card)) {
            TrapCard trapCard = (TrapCard) card;
            ArrayList<MonsterAttackingTrapCardEffect> monsterAttackingTrapCardEffects = trapCard.getMonsterAttackingTrapCardEffects();
            if (monsterAttackingTrapCardEffects.contains(MonsterAttackingTrapCardEffect.INFLICT_DAMAGE_TO_OPPONENT_EQUAL_TO_MONSTERS_ATK)) {
                return true;
            }
        }
        return false;
    }

    public static boolean logicalActivationRequirementSpellOwnerMustHaveAtLeast1RitualMonsterInHand(DuelBoard duelBoard, int fakeTurn) {
        ArrayList<Card> cardsInOwnerHand = null;
        if (fakeTurn == 1) {
            cardsInOwnerHand = duelBoard.getAllyCardsInHand();
        } else if (fakeTurn == 2) {
            cardsInOwnerHand = duelBoard.getOpponentCardsInHand();
        }
        for (int i = 0; i < cardsInOwnerHand.size(); i++) {
            Card card = cardsInOwnerHand.get(i);
            if (Card.isCardAMonster(card)) {
                MonsterCard monsterCard = (MonsterCard) card;
                if (monsterCard.getMonsterCardValue().equals(MonsterCardValue.RITUAL)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean logicalActivationRequirementSpellOwnerMustHaveNormalMonstersWithSumOfLevelsEqualToLevelOfRitualMonster(int index, int fakeTurn) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        ArrayList<Card> cardsInDeck = null;
        ArrayList<Card> cardsInHand = null;
        if (fakeTurn == 1) {
            cardsInDeck = duelBoard.getAllyCardsInDeck();
            cardsInHand = duelBoard.getAllyCardsInHand();
        } else if (fakeTurn == 2) {
            cardsInDeck = duelBoard.getOpponentCardsInDeck();
            cardsInHand = duelBoard.getOpponentCardsInHand();
        }
        ArrayList<Integer> normalMonsterCardLevels = new ArrayList<>();
        for (int i = 0; i < cardsInDeck.size(); i++) {
            Card card = cardsInDeck.get(i);
            if (Card.isCardAMonster(card) && ((MonsterCard) card).getMonsterCardValue().equals(MonsterCardValue.NORMAL)) {
                normalMonsterCardLevels.add(((MonsterCard) card).getLevel());
            }
        }
        ArrayList<Integer> levelsOfRitualCardsInHand = new ArrayList<>();
        if (!logicalActivationRequirementSpellOwnerMustHaveAtLeast1RitualMonsterInHand(duelBoard, fakeTurn)) {
            return false;
        } else {
            for (int i = 0; i < cardsInHand.size(); i++) {
                Card card = cardsInHand.get(i);
                if (Card.isCardAMonster(card) && ((MonsterCard) card).getMonsterCardValue().equals(MonsterCardValue.RITUAL)) {
                    levelsOfRitualCardsInHand.add(((MonsterCard) card).getLevel());
                }
            }
        }
        // Algorithmic Reasoning !!!
        return false;
    }

    public static boolean logicalActivationRequirementTrapOwnerMustHaveAtLeast1MonsterInTheirGraveyard(DuelBoard duelBoard, int fakeTurn) {
        ArrayList<Card> cardsInGraveyard = null;
        if (fakeTurn == 1) {
            cardsInGraveyard = duelBoard.getAllyCardsInGraveyard();
        } else if (fakeTurn == 2) {
            cardsInGraveyard = duelBoard.getOpponentCardsInGraveyard();
        }
        if (cardsInGraveyard == null) {
            return false;
        }
        for (int i = 0; i < cardsInGraveyard.size(); i++) {
            Card card = cardsInGraveyard.get(i);
            if (Card.isCardAMonster(card)) {
                return true;
            }
        }
        return false;
    }

    public static boolean logicalActivationRequirementTrapSummonedMonsterMustHaveAtLeast1000ATK(int index) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(index);
        Action uninterruptedAction = uninterruptedActions.get(uninterruptedActions.size() - 1);
        CardLocation mainCardLocation = uninterruptedAction.getMainCardLocation();
        System.out.println("this mainCardLocation we are analyzing has location "+mainCardLocation.getRowOfCardLocation()+" "+mainCardLocation.getIndex());
        MonsterCard monsterCard = (MonsterCard) duelBoard.getCardByCardLocation(mainCardLocation);
        System.out.println("monster card name is "+monsterCard.getCardName());
        return monsterCard.getAttackPower() >= 1000;
    }

    public static boolean logicalActivationRequirementTrapMonsterIsSummonedOrSpellTrapMonsterEffectIncludingSpecialSummoningIsActivated(int index) {
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(index);
        Action uninterruptedAction = uninterruptedActions.get(uninterruptedActions.size() - 1);
        ActionType uninterruptedActionType = uninterruptedAction.getActionType();
        if (uninterruptedActionType.equals(ActionType.ALLY_NORMAL_SUMMONING_MONSTER) || uninterruptedActionType.equals(ActionType.ALLY_FLIP_SUMMONING_MONSTER)
                || uninterruptedActionType.equals(ActionType.ALLY_SPECIAL_SUMMONING_MONSTER) || uninterruptedActionType.equals(ActionType.ALLY_RITUAL_SUMMONING_MONSTER)
                || uninterruptedActionType.equals(ActionType.OPPONENT_NORMAL_SUMMONING_MONSTER) || uninterruptedActionType.equals(ActionType.OPPONENT_FLIP_SUMMONING_MONSTER)
                || uninterruptedActionType.equals(ActionType.OPPONENT_SPECIAL_SUMMONING_MONSTER) || uninterruptedActionType.equals(ActionType.OPPONENT_RITUAL_SUMMONING_MONSTER)) {
            return true;
        }
        // have to check more ifs
        return false;
    }

    public static String inputsNeededForActivatingSpellTrapCard(CardLocation cardLocation, int index) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        Card card = duelBoard.getCardByCardLocation(cardLocation);
        if (Card.isCardASpell(card)) {
            SpellCard spellCard = (SpellCard) card;
            ArrayList<UserReplyForActivation> userReplyForActivations = spellCard.getUserReplyForActivations();
            if (userReplyForActivations.contains(UserReplyForActivation.CHOOSE_ONE_DARK_MONSTER_OWNER_CONTROLS)) {
                return "please choose one dark monster you control for assigning your equip spell card to it.\nSimply enter select command";
            }
            if (userReplyForActivations.contains(UserReplyForActivation.CHOOSE_ONE_WARRIOR_MONSTER_OWNER_CONTROLS)) {
                return "please choose one warrior monster you control for assigning your equip spell card to it.\nSimply enter select command";
            }
            if (userReplyForActivations.contains(UserReplyForActivation.CHOOSE_ONE_MONSTER_OWNER_CONTROLS)) {
                return "please choose one monster you control for assigning your equip spell card to it.\nSimply enter select command";
            }
            if (userReplyForActivations.contains(UserReplyForActivation.CHOOSE_ONE_MONSTER_FROM_EITHER_GY)) {
                return "show graveyard\nplease choose one monster from either graveyard\nSimply enter select command";
            }
            if (userReplyForActivations.contains(UserReplyForActivation.CHOOSE_ONE_OF_OPPONENTS_MONSTERS)) {
                return "please choose one of your opponent's monsters\nSimply enter select command";
            }
            if (userReplyForActivations.contains(UserReplyForActivation.CHOOSE_ONE_SPELL_FIELD_CARD_FROM_OWNER_DECK)) {
                return "show deck\nplease choose one spell field card from your deck\nSimply enter select command";
            }
            if (userReplyForActivations.contains(UserReplyForActivation.CHOOSE_UP_TO_TWO_SPELL_TRAP_CARDS_IN_FIELD)) {
                return "please choose up to two spell or trap cards in the field.\nSimply enter select command\nAfter you are done selecting, enter finish";
            }
            if (userReplyForActivations.contains(UserReplyForActivation.DISCARD_1_CARD)) {
                return "please choose one card from your hand to discard.\nSimple enter select command";
            }
            return "nothing needed";
        } else if (Card.isCardATrap(card)) {
            TrapCard trapCard = (TrapCard) card;
            ArrayList<CardEffects.TrapEffectEnums.UserReplyForActivation> userReplyForActivations = trapCard.getUserReplyForActivations();
            if (userReplyForActivations.contains(CardEffects.TrapEffectEnums.UserReplyForActivation.DISCARD_1_CARD)) {
                return "please choose one card from your hand to discard.\nSimple enter select command";
            }
            return "nothing needed";
        }
        return null;
    }

}
