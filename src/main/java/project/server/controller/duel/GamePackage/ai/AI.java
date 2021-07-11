package project.server.controller.duel.GamePackage.ai;

import project.server.controller.duel.PreliminaryPackage.GameManager;
import project.model.aidata.AIActionType;
import project.model.aidata.AIFurtherActivationInput;
import project.model.aidata.AISpellTrapSelections;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardLocation;

import java.util.ArrayList;

public class AI {
    private AIBoardUnderstander aiBoardUnderstander;
    private AIQueryUnderstander aiQueryUnderstander;
    private AIMainPhaseMind aiMainPhaseMind;
    private AIBattlePhaseMind aiBattlePhaseMind;
    private boolean shouldRedirectAIMind;
    private AIActionType aiActionType;
    private AIFurtherActivationInput aiFurtherActivationInput;
    private AISpellTrapSelections aiSpellTrapSelections;
    private int aiTurn;

    public AI() {
        aiBoardUnderstander = new AIBoardUnderstander();
        aiQueryUnderstander = new AIQueryUnderstander();
        aiMainPhaseMind = new AIMainPhaseMind();
        aiBattlePhaseMind = new AIBattlePhaseMind();
        shouldRedirectAIMind = false;
        aiActionType = AIActionType.NOTHING;
        aiTurn = GameManager.getDuelControllerByIndex(0).getAiTurn();
    }

    public AIBoardUnderstander getAiBoardUnderstander() {
        return aiBoardUnderstander;
    }

    public AIQueryUnderstander getAiQueryUnderstander() {
        return aiQueryUnderstander;
    }

    public AIMainPhaseMind getAiMainPhaseMind() {
        return aiMainPhaseMind;
    }

    public AIBattlePhaseMind getAiBattlePhaseMind() {
        return aiBattlePhaseMind;
    }

    public boolean isShouldRedirectAIMind() {
        return shouldRedirectAIMind;
    }

    public AIActionType getAiActionType() {
        return aiActionType;
    }

    public AIFurtherActivationInput getAiFurtherActivationInput() {
        return aiFurtherActivationInput;
    }

    public AISpellTrapSelections getAiSpellTrapSelections() {
        return aiSpellTrapSelections;
    }

    public void setShouldRedirectAIMind(boolean shouldRedirectAIMind) {
        this.shouldRedirectAIMind = shouldRedirectAIMind;
    }

    public void setAiActionType(AIActionType aiActionType) {
        this.aiActionType = aiActionType;
    }

    public void setAiFurtherActivationInput(AIFurtherActivationInput aiFurtherActivationInput) {
        this.aiFurtherActivationInput = aiFurtherActivationInput;
    }

    public void setAiSpellTrapSelections(AISpellTrapSelections aiSpellTrapSelections) {
        this.aiSpellTrapSelections = aiSpellTrapSelections;
    }

    public String getCommand() {
        updateAIInformationAccordingToBoard();
        if (shouldRedirectAIMind) {
            return redirectInput() + "\n";
        }
        return aiQueryUnderstander.understandQuery(this);
    }


    protected void updateAIInformationAccordingToBoard() {
        aiMainPhaseMind.getAiKeyVariablesUpdater().clearVariables();
        aiBattlePhaseMind.getAiKeyVariablesUpdater().clearVariables();
        aiBoardUnderstander.updateVariablesForBoard();
        aiBoardUnderstander.updateInformationFromOpponentMonsterZone();
        aiBoardUnderstander.updateInformationFromOpponentSpellTrapZone();
        aiBoardUnderstander.updateInformationFromOpponentHandZone();
        aiBoardUnderstander.updateInformationFromOpponentGraveyardZone();
        aiBoardUnderstander.updateInformationFromAIMonsterZone();
        aiBoardUnderstander.updateInformationFromAISpellTrapZone();
        aiBoardUnderstander.updateInformationFromAIHandZone();
        aiBoardUnderstander.updateInformationFromAIGraveyardZone();
    }

    protected String redirectInput() {
        shouldRedirectAIMind = false;
        if (aiActionType.equals(AIActionType.NORMAL_SUMMON)) {
            aiActionType = AIActionType.NOTHING;
            GameManager.getDuelControllerByIndex(0).addStringToWhatUsersSay("*user" + aiTurn + ": normal summon*");
            return "normal summon";
        } else if (aiActionType.equals(AIActionType.SPECIAL_SUMMON)) {
            aiActionType = AIActionType.NOTHING;
            GameManager.getDuelControllerByIndex(0).addStringToWhatUsersSay("*user" + aiTurn + ": special summon*");
            return "special summon";
        } else if (aiActionType.equals(AIActionType.SET)) {
            aiActionType = AIActionType.NOTHING;
            GameManager.getDuelControllerByIndex(0).addStringToWhatUsersSay("*user" + aiTurn + ": set*");
            return "set";
        } else if (aiActionType.equals(AIActionType.ACTIVATE_EFFECT)) {
            aiActionType = AIActionType.NOTHING;
            GameManager.getDuelControllerByIndex(0).addStringToWhatUsersSay("*user" + aiTurn + ": activate effect*");
            return "activate effect";
        } else if (aiActionType.equals(AIActionType.ATTACK_1)) {
            aiActionType = AIActionType.NOTHING;
            GameManager.getDuelControllerByIndex(0).addStringToWhatUsersSay("*user" + aiTurn + ": attack 1*");
            return "attack 1";
        } else if (aiActionType.equals(AIActionType.ATTACK_2)) {
            aiActionType = AIActionType.NOTHING;
            GameManager.getDuelControllerByIndex(0).addStringToWhatUsersSay("*user" + aiTurn + ": attack 2*");
            return "attack 2";
        } else if (aiActionType.equals(AIActionType.ATTACK_3)) {
            aiActionType = AIActionType.NOTHING;
            GameManager.getDuelControllerByIndex(0).addStringToWhatUsersSay("*user" + aiTurn + ": attack 3*");
            return "attack 3";
        } else if (aiActionType.equals(AIActionType.ATTACK_4)) {
            aiActionType = AIActionType.NOTHING;
            GameManager.getDuelControllerByIndex(0).addStringToWhatUsersSay("*user" + aiTurn + ": attack 4*");
            return "attack 4";
        } else if (aiActionType.equals(AIActionType.ATTACK_5)) {
            aiActionType = AIActionType.NOTHING;
            GameManager.getDuelControllerByIndex(0).addStringToWhatUsersSay("*user" + aiTurn + ": attack 5*");
            return "attack 5";
        } else if (aiActionType.equals(AIActionType.ATTACK_DIRECTLY)) {
            aiActionType = AIActionType.NOTHING;
            GameManager.getDuelControllerByIndex(0).addStringToWhatUsersSay("*user" + aiTurn + ": attack direct*");
            return "attack direct";
        } else if (aiActionType.equals(AIActionType.CHANGE_CARD_POSITION_TO_ATTACK)) {
            aiActionType = AIActionType.NOTHING;
            GameManager.getDuelControllerByIndex(0).addStringToWhatUsersSay("*user" + aiTurn + ": set --position attack*");
            return "set --position attack";
        } else if (aiActionType.equals(AIActionType.CHANGE_CARD_POSITION_TO_DEFENSE)) {
            aiActionType = AIActionType.NOTHING;
            GameManager.getDuelControllerByIndex(0).addStringToWhatUsersSay("*user" + aiTurn + ": set --position defense*");
            return "set --position defense";
        }
        aiActionType = AIActionType.NOTHING;
        GameManager.getDuelControllerByIndex(0).addStringToWhatUsersSay("*user" + aiTurn + ": next phase*");
        return "next phase";
    }

    protected ArrayList<CardLocation> createCardLocationArrayFromThisArray(boolean isZoneForOpponent, boolean isMonsterZoneOrSpell) {
        ArrayList<CardLocation> arrayList = new ArrayList<>();
        if (isZoneForOpponent) {
            if (isMonsterZoneOrSpell) {
                for (int i = 0; i < aiBoardUnderstander.opponentMonsterCards.size(); i++) {
                    if (Card.isCardAMonster(aiBoardUnderstander.opponentMonsterCards.get(i))) {
                        arrayList.add(new CardLocation(aiBoardUnderstander.opponentMonsterCardsRowOfCardLocation, i + 1));
                    }
                }
            } else {
                for (int i = 0; i < aiBoardUnderstander.opponentSpellTrapCards.size(); i++) {
                    if (Card.isCardASpell(aiBoardUnderstander.opponentSpellTrapCards.get(i)) || Card.isCardATrap(aiBoardUnderstander.opponentSpellTrapCards.get(i))) {
                        arrayList.add(new CardLocation(aiBoardUnderstander.opponentSpellCardsRowOfCardLocation, i + 1));
                    }
                }
            }
        } else {
            if (isMonsterZoneOrSpell) {
                for (int i = 0; i < aiBoardUnderstander.aiMonsterCards.size(); i++) {
                    if (Card.isCardAMonster(aiBoardUnderstander.aiMonsterCards.get(i))) {
                        arrayList.add(new CardLocation(aiBoardUnderstander.aiMonsterCardsRowOfCardLocation, i + 1));
                    }
                }
            } else {
                for (int i = 0; i < aiBoardUnderstander.aiSpellTrapCards.size(); i++) {
                    if (Card.isCardASpell(aiBoardUnderstander.aiSpellTrapCards.get(i)) || Card.isCardATrap(aiBoardUnderstander.aiSpellTrapCards.get(i))) {
                        arrayList.add(new CardLocation(aiBoardUnderstander.aiSpellCardsRowOfCardLocation, i + 1));
                    }
                }
            }
        }
        return arrayList;
    }
}
