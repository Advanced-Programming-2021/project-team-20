package project.model;

public enum ActionType {
    ALLY_MONSTER_ATTACKING_OPPONENT_MONSTER,
    ALLY_DRAWING_CARD,
    ALLY_FLIP_SUMMONING_MONSTER,
    ALLY_NORMAL_SUMMONING_MONSTER,
    ALLY_TRIBUTE_SUMMONING_MONSTER,
    ALLY_SPECIAL_SUMMONING_MONSTER,
    ALLY_RITUAL_SUMMONING_MONSTER,
    ALLY_SETTING_MONSTER,
    ALLY_CHANGING_MONSTER_CARD_POSITION,
    ALLY_SETTING_SPELL_OR_TRAP_CARD,
    ALLY_ACTIVATING_SPELL,
    ALLY_ACTIVATING_TRAP,
    ALLY_ACTIVATING_MONSTER_EFFECT,
    ALLY_DIRECT_ATTACKING,
    OPPONENT_MONSTER_ATTACKING_ALLY_MONSTER,
    OPPONENT_DRAWING_CARD,
    OPPONENT_FLIP_SUMMONING_MONSTER,
    OPPONENT_NORMAL_SUMMONING_MONSTER,
    OPPONENT_TRIBUTE_SUMMONING_MONSTER,
    OPPONENT_SPECIAL_SUMMONING_MONSTER,
    OPPONENT_RITUAL_SUMMONING_MONSTER,
    OPPONENT_CHANGING_MONSTER_CARD_POSITION,
    OPPONENT_SETTING_MONSTER,
    OPPONENT_SETTING_SPELL_OR_TRAP_CARD,
    OPPONENT_ACTIVATING_SPELL,
    OPPONENT_ACTIVATING_TRAP,
    OPPONENT_ACTIVATING_MONSTER_EFFECT,
    OPPONENT_DIRECT_ATTACKING
}