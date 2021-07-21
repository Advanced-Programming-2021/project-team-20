package project.model.TrapEffectEnums;

public enum LogicalActivationRequirement {
    OWNER_MUST_HAVE_AT_LEAST_ONE_CARD_IN_HAND,
    OPPONENT_MUST_HAVE_AT_LEAST_ONE_CARD_IN_HAND,
    OWNER_MUST_HAVE_AT_LEAST_ONE_MONSTER_IN_THEIR_GY,
    NORMAL_SUMMONED_MONSTER_MUST_HAVE_AT_LEAST_1000_ATK,
    FLIP_SUMMONED_MONSTER_MUST_HAVE_AT_LEAST_1000_ATK,
    MONSTER_IS_SUMMONED_OR_SPELL_TRAP_MONSTER_EFFECT_INCLUDING_SPECIAL_SUMMONING,
    ALL_CARDS_IN_SAME_COLUMN_AS_THIS_CARD_MUST_BE_OCCUPIED,
}
