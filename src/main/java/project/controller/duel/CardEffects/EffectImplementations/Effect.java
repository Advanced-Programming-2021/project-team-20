package project.controller.duel.CardEffects.EffectImplementations;

import java.util.ArrayList;

import project.controller.duel.CardEffects.MonsterEffectEnums.BeingAttackedEffect;
import project.controller.duel.CardEffects.MonsterEffectEnums.SummoningRequirement;
import project.controller.duel.CardEffects.SpellEffectEnums.*;
import project.controller.duel.CardEffects.SpellEffectEnums.LogicalActivationRequirement;
import project.controller.duel.CardEffects.SpellEffectEnums.UserReplyForActivation;
import project.controller.duel.CardEffects.TrapEffectEnums.*;
import project.controller.duel.CardEffects.TrapEffectEnums.UserReplyForActivation.*;
import project.controller.duel.GamePackage.Action;
import project.controller.duel.GamePackage.ActionConductors.ContinuousMonsterEffectController;
import project.controller.duel.GamePackage.ActionType;
import project.controller.duel.GamePackage.DuelBoard;
import project.controller.duel.GamePackage.DuelController;
import project.controller.duel.PreliminaryPackage.GameManager;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.RowOfCardLocation;
import project.model.cardData.MonsterCardData.MonsterCard;
import project.model.cardData.MonsterCardData.MonsterCardAttribute;
import project.model.cardData.MonsterCardData.MonsterCardFamily;
import project.model.cardData.MonsterCardData.MonsterCardValue;
import project.model.cardData.SpellCardData.SpellCard;
import project.model.cardData.SpellCardData.SpellCardValue;
import project.model.cardData.TrapCardData.TrapCard;

public class Effect {
    public static MessagesFromEffectToControllers canMonsterBeSpecialSummoned(Card card, DuelBoard duelBoard, int turn, String string) {
        if (!Card.isCardAMonster(card)) {
            return MessagesFromEffectToControllers.IT_IS_NOT_A_MONSTER_CARD;
        }
        MonsterCard monsterCard = (MonsterCard) card;
        ArrayList<SummoningRequirement> cardSummoningRequirements = monsterCard.getSummoningRequirements();
        if (!cardSummoningRequirements.contains(SummoningRequirement.CAN_BE_SPECIAL_SUMMONED)) {
            return MessagesFromEffectToControllers.CANT_BE_SPECIAL_SUMMONED;
        } else {
            if (cardSummoningRequirements.contains(SummoningRequirement.DISCARD_1_CARD)) {
                ArrayList<Card> cardsInHand;
                if (turn == 1) {
                    cardsInHand = duelBoard.getAllyCardsInHand();
                } else {
                    cardsInHand = duelBoard.getOpponentCardsInHand();
                }
                if (cardsInHand.size() <= 1) {
                    return MessagesFromEffectToControllers.THERE_IS_NO_CARD_IN_HAND_TO_DISCARD;
                } else {
                    return MessagesFromEffectToControllers.PLEASE_CHOOSE_ONE_CARD_FROM_YOUR_HAND_TO_DISCARD;
                }
            }
            if (cardSummoningRequirements.contains(SummoningRequirement.TRIBUTE_3_MONSTERS)) {
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
                return checkNumberOfMonstersToTribute(cardSummoningRequirements, monstersForTribute, string);
            }
        }
        return null;
    }

    public static MessagesFromEffectToControllers canMonsterBeNormalSummonedOrSet(Card card, DuelBoard duelBoard, int turn, String string) {
        if (!Card.isCardAMonster(card)) {
            return MessagesFromEffectToControllers.IT_IS_NOT_A_MONSTER_CARD;
        }
        MonsterCard monsterCard = (MonsterCard) card;
        ArrayList<SummoningRequirement> cardSummoningRequirements = monsterCard.getSummoningRequirements();
        if (string.equals("normal summon") && !cardSummoningRequirements.contains(SummoningRequirement.CAN_BE_NORMAL_SUMMONED)) {
            return MessagesFromEffectToControllers.CANT_BE_NORMAL_SUMMONED;
        } else if (string.equals("tribute summon") && !cardSummoningRequirements.contains(SummoningRequirement.CAN_BE_TRIBUTE_SUMMONED)) {
            return MessagesFromEffectToControllers.CANT_BE_TRIBUTE_SUMMONED;
        } else if (string.equals("special summon") && !cardSummoningRequirements.contains(SummoningRequirement.CAN_BE_SPECIAL_SUMMONED)) {
            return MessagesFromEffectToControllers.CANT_BE_SPECIAL_SUMMONED;
        } else if (string.equals("ritual summon") && !cardSummoningRequirements.contains(SummoningRequirement.CAN_BE_RITUAL_SUMMONED)) {
            return MessagesFromEffectToControllers.CANT_BE_RITUAL_SUMMONED;
        } else {
            if (!cardSummoningRequirements.contains(SummoningRequirement.IN_CASE_OF_NORMAL_SUMMON_THERE_IS_NO_NEED_TO_COUNT_NUMBER_OF_TRIBUTES_NEEDED)
                && string.equals("normal summon") || !cardSummoningRequirements.contains(SummoningRequirement.IN_CASE_OF_SET_THERE_IS_NO_NEED_TO_COUNT_NUMBER_OF_TRIBUTES_NEEDED)
                && string.equals("set") || string.equals("tribute summon")) {
                //this checks tributes for all monsters
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
                return checkNumberOfMonstersToTribute(cardSummoningRequirements, monstersForTribute, string);
            } else {
                //coming inside here means we are checking beast king barbaros
                if (string.equals("normal summon")) {
                    return MessagesFromEffectToControllers.YOU_CAN_NORMAL_SUMMON_THIS_MONSTER;
                } else {
                    return MessagesFromEffectToControllers.YOU_CAN_SET_THIS_MONSTER;
                }
            }
        }
    }

    public static MessagesFromEffectToControllers checkNumberOfMonstersToTribute(ArrayList<SummoningRequirement> summoningRequirements, int monstersForTribute, String string) {
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
        if (string.equals("normal summon")) {
            return MessagesFromEffectToControllers.YOU_CAN_NORMAL_SUMMON_THIS_MONSTER;
        } else if (string.equals("tribute summon")) {
            return MessagesFromEffectToControllers.YOU_CAN_TRIBUTE_SUMMON_THIS_MONSTER;
        } else if (string.equals("special summon")) {
            return MessagesFromEffectToControllers.YOU_CAN_SPECIAL_SUMMON_THIS_MONSTER;
        } else if (string.equals("set")) {
            return MessagesFromEffectToControllers.YOU_CAN_SET_THIS_MONSTER;
        }
        return null;
    }

    public static MessagesFromEffectToControllers canSpellTrapCardBeActivatedInChain(ActionType actionType, int actionTurn, int previousActionCardSpeed) {
        // This function also checks if preparations are complete and says yes if everything was ok
        MessagesFromEffectToControllers messagesFromEffectToControllers = null;
        return iterateThroughAllyOrOpponentSpellTrapCardsForCanSpellTrapCardBeActivatedInChain(actionTurn, actionType, previousActionCardSpeed);
    }


    private static MessagesFromEffectToControllers iterateThroughAllyOrOpponentSpellTrapCardsForCanSpellTrapCardBeActivatedInChain(int actionTurn, ActionType actionType, int previousActionCardSpeed) {
        //This function also calls are preparations complete if the card was available and returns the final answer
        //Here, actionTurn is the turn we want to make a move and we are going to check if our opponent can stop us
        MessagesFromEffectToControllers messagesFromEffectToControllers;
        ArrayList<Card> allyOrOpponentSpellTrapCards;
        if (actionTurn == 1) {
            allyOrOpponentSpellTrapCards = GameManager.getDuelBoardByIndex(0).getOpponentSpellCards();
        } else {
            allyOrOpponentSpellTrapCards = GameManager.getDuelBoardByIndex(0).getAllySpellCards();
        }
        for (int i = 0; i < allyOrOpponentSpellTrapCards.size(); i++) {
            CardLocation cardLocation = null;
            if (actionTurn == 2) {
                cardLocation = new CardLocation(RowOfCardLocation.ALLY_SPELL_ZONE, i + 1);
            } else {
                cardLocation = new CardLocation(RowOfCardLocation.OPPONENT_SPELL_ZONE, i + 1);
            }
            Card card = GameManager.getDuelBoardByIndex(0).getCardByCardLocation(cardLocation);
            if (card != null) {
                if (isSelectedSpellTrapCorrectAccordingToPreviousActionAndArePreparationsComplete(cardLocation, actionType, 0)) {
                    if (card.getSpeedOfCard() >= previousActionCardSpeed) {
                        return MessagesFromEffectToControllers.SPELL_TRAP_CARD_CAN_BE_ACTIVATED_IN_CHAIN;
                    }
                }
            }
        }
        return MessagesFromEffectToControllers.SPELL_TRAP_CARD_CANT_BE_ACTIVATED_IN_CHAIN;
    }

    public static boolean isSelectedSpellTrapCorrectAccordingToPreviousActionAndArePreparationsComplete(CardLocation cardLocation, ActionType actionType, int index) {
        MessagesFromEffectToControllers messagesFromEffectToControllers = arePreparationsCompleteForActivatingSpellTrapCard(cardLocation, index);
        if (messagesFromEffectToControllers != null) {
            if (messagesFromEffectToControllers.equals(MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_SPELL_ARE_COMPLETE) || messagesFromEffectToControllers.equals(MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_TRAP_ARE_COMPLETE)) {
                if (previousActionTypeIsInSyncWithActivationOfThisSpellTrapCardInChain(cardLocation, actionType)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean previousActionTypeIsInSyncWithActivationOfThisSpellTrapCardInChain(CardLocation spellTrapCardLocation, ActionType previousActionType) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(0);
        Card spellTrapCard = duelBoard.getCardByCardLocation(spellTrapCardLocation);
        if (previousActionType.equals(ActionType.ALLY_NORMAL_SUMMONING_MONSTER) || previousActionType.equals(ActionType.OPPONENT_NORMAL_SUMMONING_MONSTER)) {
            return isSelectedSpellTrapCardCorrectForActivatingInMonsterNormalSummoningInChain(spellTrapCard, previousActionType);
        } else if (previousActionType.equals(ActionType.ALLY_TRIBUTE_SUMMONING_MONSTER) || previousActionType.equals(ActionType.OPPONENT_TRIBUTE_SUMMONING_MONSTER)) {
            return isSelectedSpellTrapCardCorrectForActivatingInMonsterTributeSummoningInChain(spellTrapCard, previousActionType);
        } else if (previousActionType.equals(ActionType.ALLY_FLIP_SUMMONING_MONSTER) || previousActionType.equals(ActionType.OPPONENT_FLIP_SUMMONING_MONSTER)) {
            return isSelectedSpellTrapCardCorrectForActivatingInMonsterFlipSummoningInChain(spellTrapCard, previousActionType);
        } else if (previousActionType.equals(ActionType.ALLY_SPECIAL_SUMMONING_MONSTER) || previousActionType.equals(ActionType.OPPONENT_SPECIAL_SUMMONING_MONSTER)) {
            return isSelectedSpellTrapCardCorrectForActivatingInMonsterSpecialSummoningInChain(spellTrapCard, previousActionType);
        } else if (previousActionType.equals(ActionType.ALLY_RITUAL_SUMMONING_MONSTER) || previousActionType.equals(ActionType.OPPONENT_RITUAL_SUMMONING_MONSTER)) {
            return isSelectedSpellTrapCardCorrectForActivatingInMonsterRitualSummoningInChain(spellTrapCard, previousActionType);
        } else if (previousActionType.equals(ActionType.ALLY_MONSTER_ATTACKING_OPPONENT_MONSTER) || previousActionType.equals(ActionType.OPPONENT_MONSTER_ATTACKING_ALLY_MONSTER)) {
            return isSelectedSpellTrapCardCorrectForActivatingInMonsterAttackingInChain(spellTrapCard, previousActionType);
        } else if (previousActionType.equals(ActionType.ALLY_DIRECT_ATTACKING) || previousActionType.equals(ActionType.OPPONENT_DIRECT_ATTACKING)) {
            return isSelectedSpellTrapCardCorrectForActivatingInMonsterAttackingInChain(spellTrapCard, previousActionType);
        } else if (previousActionType.equals(ActionType.ALLY_ACTIVATING_SPELL) || previousActionType.equals(ActionType.OPPONENT_ACTIVATING_SPELL)) {
            return isSelectedSpellTrapCardCorrectForActivatingInSpellTrapActivatingInChain(spellTrapCard, previousActionType);
        } else if (previousActionType.equals(ActionType.ALLY_ACTIVATING_TRAP) || previousActionType.equals(ActionType.OPPONENT_ACTIVATING_TRAP)) {
            return isSelectedSpellTrapCardCorrectForActivatingInSpellTrapActivatingInChain(spellTrapCard, previousActionType);
        }
        return false;
    }

    private static boolean isSelectedSpellTrapCardCorrectForActivatingInSpellTrapActivatingInChain(Card spellTrapCard, ActionType actionType) {
        //This function does not consider speed of cards
        if (Card.isCardASpell(spellTrapCard)) {
            ArrayList<QuickSpellEffect> quickSpellEffects = ((SpellCard) spellTrapCard).getQuickSpellEffects();
            if (quickSpellEffects.contains(QuickSpellEffect.TARGET_1_SPELL_TRAP_CARD_AND_DESTROY)) {
                return true;
            } else if (quickSpellEffects.contains(QuickSpellEffect.DISCARD_1_CARD_THEN_TARGET_UP_TO_2_SPELL_CARDS_AND_DESTROY)) {
                return true;
            } else if (quickSpellEffects.contains(QuickSpellEffect.TRAP_CARD_INFLICTING_DAMAGE_IS_ACTIVATED_SET_DAMAGE_OF_TRAP_CARD_TO_0)) {
                Action uninterruptedAction = GameManager.getUninterruptedActionsByIndex(0).get(GameManager.getUninterruptedActionsByIndex(0).size() - 1);
                CardLocation finalCardLocation = uninterruptedAction.getFinalMainCardLocation();
                Card card = GameManager.getDuelBoardByIndex(0).getCardByCardLocation(finalCardLocation);
                if (Card.isCardATrap(card)) {
                    TrapCard trapCard = (TrapCard) card;
                    if (trapCard.getMonsterAttackingTrapCardEffects().contains(MonsterAttackingTrapCardEffect.INFLICT_DAMAGE_TO_OPPONENT_EQUAL_TO_MONSTERS_ATK)) {
                        return true;
                    }
                }
                return false;
            }
            //there is one quick effect i didn't take care of here
        } else if (Card.isCardATrap(spellTrapCard)) {
            ArrayList<SpellCardActivationTrapCardEffect> spellCardActivationTrapCardEffects = ((TrapCard) spellTrapCard).getSpellCardActivationTrapCardEffects();
            ArrayList<TrapCardActivationTrapCardEffect> trapCardActivationTrapCardEffects = ((TrapCard) spellTrapCard).getTrapCardActivationTrapCardEffects();
            if (spellCardActivationTrapCardEffects.size() > 0 && (actionType.equals(ActionType.ALLY_ACTIVATING_SPELL) || actionType.equals(ActionType.OPPONENT_ACTIVATING_SPELL)) ||
                trapCardActivationTrapCardEffects.size() > 0 && (actionType.equals(ActionType.ALLY_ACTIVATING_TRAP) || actionType.equals(ActionType.OPPONENT_ACTIVATING_TRAP))) {
                return true;
            }
        }
        return false;
    }

    private static boolean isSelectedSpellTrapCardCorrectForActivatingInMonsterAttackingInChain(Card spellTrapCard, ActionType actionType) {
        boolean actionTypeCorrect = actionType.equals(ActionType.ALLY_MONSTER_ATTACKING_OPPONENT_MONSTER) || actionType.equals(ActionType.OPPONENT_MONSTER_ATTACKING_ALLY_MONSTER)
            || actionType.equals(ActionType.ALLY_DIRECT_ATTACKING) || actionType.equals(ActionType.OPPONENT_DIRECT_ATTACKING);
        if (Card.isCardASpell(spellTrapCard)) {
            /*
            ArrayList<QuickSpellEffect> quickSpellEffects = ((SpellCard) spellTrapCard).getQuickSpellEffects();
            if (quickSpellEffects.contains(QuickSpellEffect.TARGET_1_SPELL_TRAP_CARD_AND_DESTROY) && actionTypeCorrect) {
                return true;
            } else if (quickSpellEffects.contains(QuickSpellEffect.DISCARD_1_CARD_THEN_TARGET_UP_TO_2_SPELL_CARDS_AND_DESTROY) && actionTypeCorrect) {
                return true;
            }

             */
        } else if (Card.isCardATrap(spellTrapCard)) {
            ArrayList<MonsterAttackingTrapCardEffect> monsterAttackingTrapCardEffects = ((TrapCard) spellTrapCard).getMonsterAttackingTrapCardEffects();
            if (monsterAttackingTrapCardEffects.size() > 0) {
                return true;
            }
        }
        return false;
    }

    private static boolean isSelectedSpellTrapCardCorrectForActivatingInMonsterNormalSummoningInChain(Card spellTrapCard, ActionType actionType) {
        if (Card.isCardATrap(spellTrapCard)) {
            ArrayList<NormalSummonTrapCardEffect> normalSummonTrapCardEffects = ((TrapCard) spellTrapCard).getNormalSummonTrapCardEffects();
            if (normalSummonTrapCardEffects.size() > 0 && (actionType.equals(ActionType.ALLY_NORMAL_SUMMONING_MONSTER) || actionType.equals(ActionType.OPPONENT_NORMAL_SUMMONING_MONSTER))) {
                return true;
            }
        }
        return false;
    }

    private static boolean isSelectedSpellTrapCardCorrectForActivatingInMonsterTributeSummoningInChain(Card spellTrapCard, ActionType actionType) {
        if (Card.isCardATrap(spellTrapCard)) {
            ArrayList<TributeSummonTrapCardEffect> tributeSummonTrapCardEffects = ((TrapCard) spellTrapCard).getTributeSummonTrapCardEffects();
            if (tributeSummonTrapCardEffects.size() > 0 && (actionType.equals(ActionType.ALLY_TRIBUTE_SUMMONING_MONSTER) || actionType.equals(ActionType.OPPONENT_TRIBUTE_SUMMONING_MONSTER))) {
                return true;
            }
        }
        return false;
    }

    private static boolean isSelectedSpellTrapCardCorrectForActivatingInMonsterFlipSummoningInChain(Card spellTrapCard, ActionType actionType) {
        if (Card.isCardATrap(spellTrapCard)) {
            ArrayList<FlipSummonTrapCardEffect> flipSummonTrapCardEffects = ((TrapCard) spellTrapCard).getFlipSummonTrapCardEffects();
            if (flipSummonTrapCardEffects.size() > 0 && (actionType.equals(ActionType.ALLY_FLIP_SUMMONING_MONSTER) || actionType.equals(ActionType.OPPONENT_FLIP_SUMMONING_MONSTER))) {
                return true;
            }
        }
        return false;
    }

    private static boolean isSelectedSpellTrapCardCorrectForActivatingInMonsterSpecialSummoningInChain(Card spellTrapCard, ActionType actionType) {
        if (Card.isCardATrap(spellTrapCard)) {
            ArrayList<SpecialSummonTrapCardEffect> specialSummonTrapCardEffects = ((TrapCard) spellTrapCard).getSpecialSummonTrapCardEffects();
            if (specialSummonTrapCardEffects.size() > 0 && (actionType.equals(ActionType.ALLY_SPECIAL_SUMMONING_MONSTER) || actionType.equals(ActionType.OPPONENT_SPECIAL_SUMMONING_MONSTER))) {
                return true;
            }
        }
        return false;
    }

    private static boolean isSelectedSpellTrapCardCorrectForActivatingInMonsterRitualSummoningInChain(Card spellTrapCard, ActionType actionType) {
        if (Card.isCardATrap(spellTrapCard)) {
            ArrayList<RitualSummonTrapCardEffect> ritualSummonTrapCardEffects = ((TrapCard) spellTrapCard).getRitualSummonTrapCardEffects();
            if (ritualSummonTrapCardEffects.size() > 0 && (actionType.equals(ActionType.ALLY_RITUAL_SUMMONING_MONSTER) || actionType.equals(ActionType.OPPONENT_RITUAL_SUMMONING_MONSTER))) {
                return true;
            }
        }
        return false;
    }

    public static MessagesFromEffectToControllers arePreparationsCompleteForActivatingSpellTrapCard(CardLocation cardLocation, int index) {
        //this function doesn't care if main card is given as response in chain or not
        ContinuousMonsterEffectController continuousMonsterEffectController = GameManager.getContinuousMonsterEffectControllersByIndex(index);
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        Card card = duelBoard.getCardByCardLocation(cardLocation);
        int turn = duelController.getTurn();
        int fakeTurn;
        if (cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_SPELL_ZONE) || cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE)
            || cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_HAND_ZONE)) {
            fakeTurn = 1;
        } else {
            fakeTurn = 2;
        }
        boolean preparationsAreComplete = true;
        if (Card.isCardASpell(card)) {
            SpellCard spellCard = (SpellCard) card;
            ArrayList<LogicalActivationRequirement> logicalActivationRequirements = spellCard.getLogicalActivationRequirements();
            if (logicalActivationRequirements.contains(LogicalActivationRequirement.MUST_EXIST_AT_LEAST_1_MONSTER_IN_EITHER_GY)) {
                if (!logicalActivationRequirementSpellMustExistAtLeastOneMonsterInEitherGraveyard(duelBoard, fakeTurn)) {
                    return MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_SPELL_ARE_NOT_COMPLETE;
                }
            }
            if (logicalActivationRequirements.contains(LogicalActivationRequirement.MUST_EXIST_AT_LEAST_ONE_SPELL_CARD_IN_FIELD)) {
                if (!logicalActivationRequirementSpellMustExistAtLeastOneSpellInField(duelBoard, fakeTurn)) {
                    return MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_SPELL_ARE_NOT_COMPLETE;
                }
            }
            if (logicalActivationRequirements.contains(LogicalActivationRequirement.OWNER_MUST_CONTROL_WARRIOR_MONSTER)) {
                if (!logicalActivationRequirementSpellOwnerMustControlSpecificMonsterFamily(duelBoard, fakeTurn, MonsterCardFamily.WARRIOR, null)) {
                    return MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_SPELL_ARE_NOT_COMPLETE;
                }
            }
            if (logicalActivationRequirements.contains(LogicalActivationRequirement.OWNER_MUST_CONTROL_FIEND_OR_SPELLCASTER_MONSTER)) {
                if (!logicalActivationRequirementSpellOwnerMustControlSpecificMonsterFamily(duelBoard, fakeTurn, MonsterCardFamily.FIEND, null)
                    && !logicalActivationRequirementSpellOwnerMustControlSpecificMonsterFamily(duelBoard, fakeTurn, MonsterCardFamily.SPELLCASTER, null)) {
                    return MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_SPELL_ARE_NOT_COMPLETE;
                }
            }
            if (logicalActivationRequirements.contains(LogicalActivationRequirement.OWNER_MUST_CONTROL_AT_LEAST_1_MONSTER)) {
                if (!logicalActivationRequirementSpellOwnerMustControlSpecificMonsterFamily(duelBoard, fakeTurn, null, null)) {
                    return MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_SPELL_ARE_NOT_COMPLETE;
                }
            }
            if (logicalActivationRequirements.contains(LogicalActivationRequirement.OWNER_MUST_HAVE_ONE_SPELL_FIELD_CARD_IN_DECK)) {
                if (!logicalActivationRequirementSpellOwnerMustHaveAtLeast1SpellFieldCardInDeck(duelBoard, fakeTurn)) {
                    return MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_SPELL_ARE_NOT_COMPLETE;
                }
            }
            if (logicalActivationRequirements.contains(LogicalActivationRequirement.OWNER_MUST_HAVE_AT_LEAST_ONE_MONSTER_ZONE_SLOT_EMPTY)) {
                if (!logicalActivationRequirementSpellOwnerMustHaveAtLeast1MonsterZoneSlotEmpty(duelBoard, fakeTurn)) {
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
            if (continuousMonsterEffectController.areContinuousMonsterCardEffectsPreventingUserFromActivatingTrap(cardLocation, index)) {
                return MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_TRAP_ARE_NOT_COMPLETE;
            }
            TrapCard trapCard = (TrapCard) card;
            ArrayList<project.controller.duel.CardEffects.TrapEffectEnums.LogicalActivationRequirement> logicalActivationRequirements = trapCard.getLogicalActivationRequirements();
            if (logicalActivationRequirements.contains(project.controller.duel.CardEffects.TrapEffectEnums.LogicalActivationRequirement.OWNER_MUST_HAVE_AT_LEAST_ONE_CARD_IN_HAND)) {
                if (!logicalActivationRequirementSpellOwnerMustHaveAtLeast1CardInHand(duelBoard, fakeTurn)) {
                    return MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_TRAP_ARE_NOT_COMPLETE;
                }
            }
            if (logicalActivationRequirements.contains(project.controller.duel.CardEffects.TrapEffectEnums.LogicalActivationRequirement.OPPONENT_MUST_HAVE_AT_LEAST_ONE_CARD_IN_HAND)) {
                if (!logicalActivationRequirementSpellOwnerMustHaveAtLeast1CardInHand(duelBoard, 3 - fakeTurn)) {
                    return MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_TRAP_ARE_NOT_COMPLETE;
                }
            }
            if (logicalActivationRequirements.contains(project.controller.duel.CardEffects.TrapEffectEnums.LogicalActivationRequirement.OWNER_MUST_HAVE_AT_LEAST_ONE_MONSTER_IN_THEIR_GY)) {
                if (!logicalActivationRequirementTrapOwnerMustHaveAtLeast1MonsterInTheirGraveyard(duelBoard, fakeTurn)) {
                    return MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_TRAP_ARE_NOT_COMPLETE;
                }
            }
            if (logicalActivationRequirements.contains(project.controller.duel.CardEffects.TrapEffectEnums.LogicalActivationRequirement.NORMAL_SUMMONED_MONSTER_MUST_HAVE_AT_LEAST_1000_ATK)) {
                if (!logicalActivationRequirementTrapSummonedMonsterMustHaveAtLeast1000ATK(index)) {
                    return MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_TRAP_ARE_NOT_COMPLETE;
                }
            }
            if (logicalActivationRequirements.contains(project.controller.duel.CardEffects.TrapEffectEnums.LogicalActivationRequirement.FLIP_SUMMONED_MONSTER_MUST_HAVE_AT_LEAST_1000_ATK)) {
                if (!logicalActivationRequirementTrapSummonedMonsterMustHaveAtLeast1000ATK(index)) {
                    return MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_TRAP_ARE_NOT_COMPLETE;
                }
            }
            if (logicalActivationRequirements.contains(project.controller.duel.CardEffects.TrapEffectEnums.LogicalActivationRequirement.MONSTER_IS_SUMMONED_OR_SPELL_TRAP_MONSTER_EFFECT_INCLUDING_SPECIAL_SUMMONING)) {
                if (!logicalActivationRequirementTrapMonsterIsSummonedOrSpellTrapMonsterEffectIncludingSpecialSummoningIsActivated(index)) {
                    return MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_TRAP_ARE_NOT_COMPLETE;
                }
            }
            return MessagesFromEffectToControllers.PREPARATIONS_FOR_ACTIVATION_OF_THIS_TRAP_ARE_COMPLETE;
        }
        return null;
    }


    public static boolean logicalActivationRequirementSpellMustExistAtLeastOneSpellInField(DuelBoard duelBoard, int fakeTurn) {
        ArrayList<Card> allySpellCards = duelBoard.getAllySpellCards();
        ArrayList<Card> opponentSpellCards = duelBoard.getOpponentSpellCards();
        Card allySpellFieldCard = duelBoard.getAllySpellFieldCard().get(0);
        Card opponentSpellFieldCard = duelBoard.getOpponentSpellFieldCard().get(0);
        for (int i = 0; i < 5; i++) {
            if (allySpellCards.get(i) != null) {
                return true;
            } else if (opponentSpellCards.get(i) != null) {
                return true;
            } else if (allySpellFieldCard != null) {
                return true;
            } else if (opponentSpellFieldCard != null) {
                return true;
            }
        }
        return false;
    }

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
                if (monsterCardAttribute == null && monsterCardFamily == null) {
                    return true;
                }
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

    public static boolean logicalActivationRequirementSpellOwnerMustHaveAtLeast1MonsterZoneSlotEmpty(DuelBoard duelBoard, int fakeTurn) {
        if (fakeTurn == 1) {
            return !duelBoard.isZoneFull(RowOfCardLocation.ALLY_MONSTER_ZONE);
        } else {
            return !duelBoard.isZoneFull(RowOfCardLocation.OPPONENT_MONSTER_ZONE);
        }
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
                //   System.out.println("monster card in hand has name " + monsterCard.getCardName());
                if (monsterCard.getMonsterCardValue().equals(MonsterCardValue.RITUAL)) {
                    //      System.out.println("i a, returning true");
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
        for (int i = 0; i < cardsInHand.size(); i++) {
            Card card = cardsInHand.get(i);
            if (Card.isCardAMonster(card) && ((MonsterCard) card).getMonsterCardValue().equals(MonsterCardValue.RITUAL)) {
                levelsOfRitualCardsInHand.add(((MonsterCard) card).getLevel());
            }
        }
        // Algorithmic Reasoning !!!
        boolean isRitualSummoningPossible;
        for (int i = 0; i < levelsOfRitualCardsInHand.size(); i++) {
            isRitualSummoningPossible = doesExistSubsetOfNormalMonsterLevelsWithSumEqualToRitualMonsterLevel(normalMonsterCardLevels,
                levelsOfRitualCardsInHand.get(i), normalMonsterCardLevels.size());
            if (isRitualSummoningPossible) {
                return true;
            }
        }
        return false;
    }

    public static boolean doesExistSubsetOfNormalMonsterLevelsWithSumEqualToRitualMonsterLevel(ArrayList<Integer> normalMonsterCardLevels, int levelOfRitualMonsterInHand, int size) {
        if (levelOfRitualMonsterInHand == 0) {
            return true;
        }
        if (size == 0) {
            return false;
        }
        if (normalMonsterCardLevels.get(size - 1) > levelOfRitualMonsterInHand) {
            return doesExistSubsetOfNormalMonsterLevelsWithSumEqualToRitualMonsterLevel(normalMonsterCardLevels, levelOfRitualMonsterInHand, size - 1);
        }
        return doesExistSubsetOfNormalMonsterLevelsWithSumEqualToRitualMonsterLevel(normalMonsterCardLevels, levelOfRitualMonsterInHand - normalMonsterCardLevels.get(size - 1), size - 1) ||
            doesExistSubsetOfNormalMonsterLevelsWithSumEqualToRitualMonsterLevel(normalMonsterCardLevels, levelOfRitualMonsterInHand, size - 1);
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
        CardLocation finalMainCardLocation = uninterruptedAction.getFinalMainCardLocation();
        MonsterCard monsterCard = (MonsterCard) duelBoard.getCardByCardLocation(finalMainCardLocation);
        return monsterCard.getAttackPower() >= 1000;
    }

    public static boolean logicalActivationRequirementTrapMonsterIsSummonedOrSpellTrapMonsterEffectIncludingSpecialSummoningIsActivated(int index) {
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(index);
        Action uninterruptedAction = uninterruptedActions.get(uninterruptedActions.size() - 1);
        ActionType uninterruptedActionType = uninterruptedAction.getActionType();
        if (uninterruptedActionType.equals(ActionType.ALLY_NORMAL_SUMMONING_MONSTER) || uninterruptedActionType.equals(ActionType.ALLY_TRIBUTE_SUMMONING_MONSTER)
            || uninterruptedActionType.equals(ActionType.ALLY_FLIP_SUMMONING_MONSTER) || uninterruptedActionType.equals(ActionType.ALLY_SPECIAL_SUMMONING_MONSTER)
            || uninterruptedActionType.equals(ActionType.ALLY_RITUAL_SUMMONING_MONSTER) || uninterruptedActionType.equals(ActionType.OPPONENT_NORMAL_SUMMONING_MONSTER)
            || uninterruptedActionType.equals(ActionType.OPPONENT_TRIBUTE_SUMMONING_MONSTER) || uninterruptedActionType.equals(ActionType.OPPONENT_FLIP_SUMMONING_MONSTER)
            || uninterruptedActionType.equals(ActionType.OPPONENT_SPECIAL_SUMMONING_MONSTER) || uninterruptedActionType.equals(ActionType.OPPONENT_RITUAL_SUMMONING_MONSTER)) {
            return true;
        } else {
            if (uninterruptedActionType.equals(ActionType.ALLY_ACTIVATING_SPELL) || uninterruptedActionType.equals(ActionType.OPPONENT_ACTIVATING_SPELL)) {
                CardLocation spellCardLocation = uninterruptedAction.getFinalMainCardLocation();
                Card card = GameManager.getDuelBoardByIndex(index).getCardByCardLocation(spellCardLocation);
                ArrayList<NormalSpellCardEffect> normalSpellCardEffects = ((SpellCard) card).getNormalSpellCardEffects();
                if (normalSpellCardEffects.contains(NormalSpellCardEffect.SPECIAL_SUMMON_MONSTER_FROM_EITHER_GY)) {
                    return true;
                }
            } else if (uninterruptedActionType.equals(ActionType.ALLY_ACTIVATING_TRAP) || uninterruptedActionType.equals(ActionType.OPPONENT_ACTIVATING_TRAP)) {
                CardLocation trapCardLocation = uninterruptedAction.getFinalMainCardLocation();
                Card card = GameManager.getDuelBoardByIndex(index).getCardByCardLocation(trapCardLocation);
                ArrayList<NormalTrapCardEffect> normalTrapCardEffects = ((TrapCard) card).getNormalTrapCardEffects();
                if (normalTrapCardEffects.contains(NormalTrapCardEffect.SPECIAL_SUMMON_ONE_MONSTER_IN_YOUR_GRAVEYARD_IN_FACE_UP_ATTACK_POSITION)) {
                    return true;
                }
            } else if (uninterruptedActionType.equals(ActionType.ALLY_MONSTER_ATTACKING_OPPONENT_MONSTER) || uninterruptedActionType.equals(ActionType.OPPONENT_MONSTER_ATTACKING_ALLY_MONSTER)) {
                CardLocation defendingMonsterCardLocation = uninterruptedAction.getTargetingCards().get(uninterruptedAction.getTargetingCards().size() - 1);
                Card card = GameManager.getDuelBoardByIndex(index).getCardByCardLocation(defendingMonsterCardLocation);
                ArrayList<BeingAttackedEffect> beingAttackedEffects = ((MonsterCard) card).getBeingAttackedEffects();
                if (beingAttackedEffects.contains(BeingAttackedEffect.SPECIAL_SUMMON_CYBERSE_NORMAL_MONSTER_FROM_HAND_GV_DECK_ONCE_PER_TURN)) {
                    return true;
                }
            }

        }
        return false;
    }

    public static ArrayList<String> inputsNeededForActivatingSpellTrapCard(CardLocation cardLocation, int index) {
        ArrayList<String> output = new ArrayList<>();
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        Card card = duelBoard.getCardByCardLocation(cardLocation);
        if (Card.isCardASpell(card)) {
            SpellCard spellCard = (SpellCard) card;
            ArrayList<UserReplyForActivation> userReplyForActivations = spellCard.getUserReplyForActivations();
            if (userReplyForActivations.contains(UserReplyForActivation.CHOOSE_ONE_FIEND_OR_SPELLCASTER_MONSTER_OWNER_CONTROLS)) {
                output.add("please choose one fiend or spellcaster monster you control for assigning your equip spell card to it.\nSimply enter select command");
            }
            if (userReplyForActivations.contains(UserReplyForActivation.CHOOSE_ONE_WARRIOR_MONSTER_OWNER_CONTROLS)) {
                output.add("please choose one warrior monster you control for assigning your equip spell card to it.\nSimply enter select command");
            }
            if (userReplyForActivations.contains(UserReplyForActivation.CHOOSE_ONE_MONSTER_OWNER_CONTROLS)) {
                output.add("please choose one monster you control for assigning your equip spell card to it.\nSimply enter select command");
            }
            if (userReplyForActivations.contains(UserReplyForActivation.CHOOSE_ONE_OF_OPPONENTS_MONSTERS)) {
                output.add("please choose one of your opponent's monsters\nSimply enter select command");
            }
            if (userReplyForActivations.contains(UserReplyForActivation.CHOOSE_ONE_SPELL_FIELD_CARD_FROM_OWNER_DECK)) {
                output.add("show deck\nplease choose one spell field card from your deck\nSimply enter select command");
            }
            if (userReplyForActivations.contains(UserReplyForActivation.CHOOSE_UP_TO_TWO_SPELL_TRAP_CARDS_IN_FIELD)) {
                output.add("please choose up to two spell or trap cards in the field.\nSimply enter select command\nAfter you are done selecting, enter finish selecting");
                output.add("please choose up to two spell or trap cards in the field.\nSimply enter select command\nAfter you are done selecting, enter finish selecting");
            }
            if (userReplyForActivations.contains(UserReplyForActivation.CHOOSE_ONE_SPELL_TRAP_CARD_IN_FIELD)) {
                output.add("please choose one spell or trap card in the field.\nSimply enter select command");
            }
            if (userReplyForActivations.contains(UserReplyForActivation.DISCARD_1_CARD)) {
                output.add("please choose one card from your hand to discard.\nSimple enter select command");
            }
            if (userReplyForActivations.contains(UserReplyForActivation.CHOOSE_FACE_UP_ATTACK_POSITION_OR_DEFENSE_POSITION_OF_YOUR_MONSTER)) {
                output.add("please choose if you want to summon your monster in face up attack position of face up defense position.\n" +
                    "simple enter attacking or defensive");
            }
            if (userReplyForActivations.contains(UserReplyForActivation.CHOOSE_ONE_RITUAL_MONSTER_FROM_YOUR_HAND_WITH_LEVEL_EQUAL_TO_SUM_OF_LEVELS_YOU_CHOSE)) {
                output.add("now select one ritual monster from your hand\nits level should be equal to the sum of levels of normal monsters you have already chosen");
            }
            if (userReplyForActivations.contains(UserReplyForActivation.CHOOSE_NORMAL_MONSTERS_FROM_YOUR_DECK_WITH_SUM_OF_LEVELS_EQUAL_TO_A_RITUAL_MONSTER_LEVEL)) {
                output.add("please choose normal monsters form your deck with sum of levels equal to level of your ritual monster" +
                    "\nsimply enter select command\nshow deck");
            }
            if (userReplyForActivations.contains(UserReplyForActivation.CHOOSE_ONE_MONSTER_FROM_EITHER_GY)) {
                output.add("show graveyard\nplease choose one monster from either graveyard\nSimply enter select command");
            }
            if (output.size() == 0) {
                output.add("nothing needed");
                return output;
            }
            return output;
        } else if (Card.isCardATrap(card)) {
            TrapCard trapCard = (TrapCard) card;
            ArrayList<project.controller.duel.CardEffects.TrapEffectEnums.UserReplyForActivation> userReplyForActivations = trapCard.getUserReplyForActivations();
            if (userReplyForActivations.contains(project.controller.duel.CardEffects.TrapEffectEnums.UserReplyForActivation.DISCARD_1_CARD)) {
                output.add("please choose one card from your hand to discard.\nSimple enter select command");
            }
            if (userReplyForActivations.contains(project.controller.duel.CardEffects.TrapEffectEnums.UserReplyForActivation.CHOOSE_ONE_MONSTER_FROM_OWNERS_GRAVEYARD_TO_SPECIAL_SUMMON)) {
                output.add("show graveyard\nplease choose one monster from your own graveyard\nSimply enter select command");
            }
            if (userReplyForActivations.contains(project.controller.duel.CardEffects.TrapEffectEnums.UserReplyForActivation.ENTER_NAME_OF_A_CARD)) {
                output.add("please enter the name of a card");
            }
            if (output.size() == 0) {
                output.add("nothing needed");
                return output;
            }
            return output;
        }
        return null;
    }

}
