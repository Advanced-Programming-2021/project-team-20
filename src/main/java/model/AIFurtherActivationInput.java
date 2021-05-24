package model;

public enum AIFurtherActivationInput {
    SELECT_A_CARD_FROM_YOUR_HAND_TO_DISCARD,
    SELECT_OPPONENT_SPELL_TRAP_CARDS_WORTHY_OF_KILLING,
    SELECT_2_OF_OPPONENT_SPELL_TRAP_CARDS_WORTHY_OF_KILLING,
    SELECT_A_CARD_FROM_YOUR_HAND_TO_DISCARD_IN_CHAIN,
    SELECT_OPPONENT_SPELL_TRAP_CARDS_WORTHY_OF_KILLING_IN_CHAIN,
    SELECT_2_OF_OPPONENT_SPELL_TRAP_CARDS_WORTHY_OF_KILLING_IN_CHAIN,
    SELECT_A_MONSTER_SETTING_ATTACK_TO_0_EFFECT_FROM_OWN_GRAVEYARD,
    SELECT_A_SELF_DESTRUCTION_MONSTER_FROM_OWN_GRAVEYARD,
    SELECT_A_MONSTER_WITH_HIGH_ATTACK_FROM_OWN_GRAVEYARD,
    ATTACKING,
    DEFENSIVE,
    NOTHING,
}
