//package project;
//
//public class ClassOfComments {
//    if (cardName.equals("torrential tribute")) {
//        normalSummonTrapCardEffects.add(NormalSummonTrapCardEffect.DESTROY_ALL_MONSTERS_ON_FIELD);
//    } else if (cardName.equals("mirror force")) {
//        monsterAttackingTrapCardEffects
//            .add(MonsterAttackingTrapCardEffect.DESTROY_ALL_OPPONENT_ATTACK_POSITION_MONSTERS);
//    } else if (cardName.equals("magic cylinder")) {
//        monsterAttackingTrapCardEffects.add(MonsterAttackingTrapCardEffect.NEGATE_OPPONENT_ATTACK);
//        monsterAttackingTrapCardEffects
//            .add(MonsterAttackingTrapCardEffect.INFLICT_DAMAGE_TO_OPPONENT_EQUAL_TO_MONSTERS_ATK);
//    } else if (cardName.equals("negate attack")) {
//        monsterAttackingTrapCardEffects.add(MonsterAttackingTrapCardEffect.NEGATE_OPPONENT_ATTACK);
//        monsterAttackingTrapCardEffects.add(MonsterAttackingTrapCardEffect.END_BATTLE_PHASE);
//    } else if (cardName.equals("trap hole")) {
//        normalSummonTrapCardEffects.add(NormalSummonTrapCardEffect.IF_ATK_IS_AT_LEAST_1000_ATK_DESTROY_IT);
//        flipSummonTrapCardEffects.add(FlipSummonTrapCardEffect.IF_ATK_IS_AT_LEAST_1000_ATK_DESTROY_IT);
//        logicalActivationRequirements
//            .add(LogicalActivationRequirement.NORMAL_SUMMONED_MONSTER_MUST_HAVE_AT_LEAST_1000_ATK);
//        logicalActivationRequirements
//            .add(LogicalActivationRequirement.FLIP_SUMMONED_MONSTER_MUST_HAVE_AT_LEAST_1000_ATK);
//    } else if (cardName.equals("time seal")) {
//        normalTrapCardEffects.add(NormalTrapCardEffect.SKIP_OPPONENT_DRAW_PHASE_NEXT_TURN);
//    } else if (cardName.equals("solemn warning")) {
//        normalSummonTrapCardEffects.add(NormalSummonTrapCardEffect.PAY_2000_LP);
//        normalSummonTrapCardEffects.add(NormalSummonTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD);
//        flipSummonTrapCardEffects.add(FlipSummonTrapCardEffect.PAY_2000_LP);
//        flipSummonTrapCardEffects.add(FlipSummonTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD);
//        specialSummonTrapCardEffects.add(SpecialSummonTrapCardEffect.PAY_2000_LP);
//        specialSummonTrapCardEffects
//            .add(SpecialSummonTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD);
//        ritualSummonTrapCardEffects.add(RitualSummonTrapCardEffect.PAY_2000_LP);
//        ritualSummonTrapCardEffects.add(RitualSummonTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD);
//        spellCardActivationTrapCardEffects.add(SpellCardActivationTrapCardEffect.PAY_2000_LP);
//        spellCardActivationTrapCardEffects
//            .add(SpellCardActivationTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD);
//        monsterEffectActivationTrapCardEffect.add(MonsterEffectActivationTrapCardEffect.PAY_2000_LP);
//        monsterEffectActivationTrapCardEffect
//            .add(MonsterEffectActivationTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD);
//        monsterAttackingTrapCardEffects.add(MonsterAttackingTrapCardEffect.PAY_2000_LP);
//        monsterAttackingTrapCardEffects
//            .add(MonsterAttackingTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD);
//        trapCardActivationTrapCardEffects.add(TrapCardActivationTrapCardEffect.PAY_2000_LP);
//        trapCardActivationTrapCardEffects
//            .add(TrapCardActivationTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD);
//        logicalActivationRequirements.add(
//            LogicalActivationRequirement.MONSTER_IS_SUMMONED_OR_SPELL_TRAP_MONSTER_EFFECT_INCLUDING_SPECIAL_SUMMONING);
//    } else if (cardName.equals("magic jammer")) {
//        spellCardActivationTrapCardEffects
//            .add(SpellCardActivationTrapCardEffect.DISCARD_1_CARD_NEGATE_DESTROY_CARD);
//        logicalActivationRequirements.add(LogicalActivationRequirement.OWNER_MUST_HAVE_AT_LEAST_ONE_CARD_IN_HAND);
//        userReplyForActivations.add(UserReplyForActivation.DISCARD_1_CARD);
//    } else if (cardName.equals("call of the hunted")) {
//        monsterAttackingTrapCardEffects.add(
//            MonsterAttackingTrapCardEffect.SPECIAL_SUMMON_ONE_MONSTER_IN_YOUR_GRAVEYARD_IN_FACE_UP_ATTACK_POSITION);
//        normalTrapCardEffects
//            .add(NormalTrapCardEffect.SPECIAL_SUMMON_ONE_MONSTER_IN_YOUR_GRAVEYARD_IN_FACE_UP_ATTACK_POSITION);
//        logicalActivationRequirements
//            .add(LogicalActivationRequirement.OWNER_MUST_HAVE_AT_LEAST_ONE_MONSTER_IN_THEIR_GY);
//        userReplyForActivations
//            .add(UserReplyForActivation.CHOOSE_ONE_MONSTER_FROM_OWNERS_GRAVEYARD_TO_SPECIAL_SUMMON);
//    } else if (cardName.equals("mind crush")) {
//        normalTrapCardEffects.add(
//            NormalTrapCardEffect.OPPONENT_DISCARDS_ALL_CARDS_WITH_A_GIVEN_NAME_OTHERWISE_OWNER_LOSES_ONE_RANDOM_CARD);
//        logicalActivationRequirements.add(LogicalActivationRequirement.OWNER_MUST_HAVE_AT_LEAST_ONE_CARD_IN_HAND);
//        logicalActivationRequirements
//            .add(LogicalActivationRequirement.OPPONENT_MUST_HAVE_AT_LEAST_ONE_CARD_IN_HAND);
//        userReplyForActivations.add(UserReplyForActivation.ENTER_NAME_OF_A_CARD);
//    }
//
//
//    if (cardName.equals("dark hole")) {
//        normalSpellCardEffects.add(NormalSpellCardEffect.DESTROY_ALL_MONSTERS_ON_THE_FIELD);
//    } else if (cardName.equals("harpie's feather duster")) {
//        normalSpellCardEffects.add(NormalSpellCardEffect.DESTROY_ALL_OF_OPPONENTS_SPELL_AND_TRAP_CARDS);
//    } else if (cardName.equals("raigeki")) {
//        normalSpellCardEffects.add(NormalSpellCardEffect.DESTROY_ALL_OF_OPPONENTS_MONSTERS);
//    } else if (cardName.equals("sword of dark destruction")) {
//        equipSpellEffects.add(EquipSpellEffect.FIEND_OR_SPELLCASTER_EQUIPPED_MONSTER_GAINS_400_ATK_LOSE_200_DEF);
//        logicalActivationRequirements
//            .add(LogicalActivationRequirement.OWNER_MUST_CONTROL_FIEND_OR_SPELLCASTER_MONSTER);
//        userReplyForActivations.add(UserReplyForActivation.CHOOSE_ONE_FIEND_OR_SPELLCASTER_MONSTER_OWNER_CONTROLS);
//    } else if (cardName.equals("black pendant")) {
//        equipSpellEffects.add(EquipSpellEffect.EQUIPPED_MONSTER_GAIN_500_ATK);
//        logicalActivationRequirements.add(LogicalActivationRequirement.OWNER_MUST_CONTROL_AT_LEAST_1_MONSTER);
//        userReplyForActivations.add(UserReplyForActivation.CHOOSE_ONE_MONSTER_OWNER_CONTROLS);
//    } else if (cardName.equals("united we stand")) {
//        equipSpellEffects
//            .add(EquipSpellEffect.EQUIPPED_MONSTER_GAIN_800_ATK_DEF_FOR_EACH_FACE_UP_MONSTER_OWNER_CONTROLS);
//        logicalActivationRequirements.add(LogicalActivationRequirement.OWNER_MUST_CONTROL_AT_LEAST_1_MONSTER);
//        userReplyForActivations.add(UserReplyForActivation.CHOOSE_ONE_MONSTER_OWNER_CONTROLS);
//    } else if (cardName.equals("magnum shield")) {
//        equipSpellEffects.add(EquipSpellEffect.WARRIOR_EQUIPPED_MONSTER_IF_DEFENSE_POSITION_GAIN_DEF_EQUAL_TO_ATK);
//        equipSpellEffects.add(EquipSpellEffect.WARRIOR_EQUIPPED_MONSTER_IF_ATTACK_POSITION_GAIN_ATK_EQUAL_TO_DEF);
//        logicalActivationRequirements.add(LogicalActivationRequirement.OWNER_MUST_CONTROL_WARRIOR_MONSTER);
//        userReplyForActivations.add(UserReplyForActivation.CHOOSE_ONE_WARRIOR_MONSTER_OWNER_CONTROLS);
//    } else if (cardName.equals("umiruka")) {
//        fieldSpellEffects.add(FieldSpellEffect.AQUA_GAINS_500_ATK);
//        fieldSpellEffects.add(FieldSpellEffect.AQUA_LOSES_400_DEF);
//    } else if (cardName.equals("closed forest")) {
//        fieldSpellEffects.add(FieldSpellEffect.BEAST_MONSTERS_OWNER_CONTROLS_GAIN_100_ATK_FOR_EACH_MONSTER_IN_GY);
//    } else if (cardName.equals("forest")) {
//        fieldSpellEffects.add(FieldSpellEffect.INSECT_GAIN_200_ATK_DEF);
//        fieldSpellEffects.add(FieldSpellEffect.BEAST_GAIN_200_ATK_DEF);
//        fieldSpellEffects.add(FieldSpellEffect.BEASTWARRIOR_GAIN_200_ATK_DEF);
//    } else if (cardName.equals("yami")) {
//        fieldSpellEffects.add(FieldSpellEffect.FIEND_GAIN_200_ATK_DEF);
//        fieldSpellEffects.add(FieldSpellEffect.SPELLCASTER_GAIN_200_ATK_DEF);
//        fieldSpellEffects.add(FieldSpellEffect.FAIRY_LOSE_200_ATK_DEF);
//    } else if (cardName.equals("ring of defense")) {
//        quickSpellEffects.add(QuickSpellEffect.TRAP_CARD_INFLICTING_DAMAGE_IS_ACTIVATED_SET_DAMAGE_OF_TRAP_CARD_TO_0);
//        logicalActivationRequirements.add(LogicalActivationRequirement.TRAP_CARD_INFLICTING_DAMAGE_MUST_BE_ACTIVATED);
//    } else if (cardName.equals("mystical space typhoon")) {
//        quickSpellEffects.add(QuickSpellEffect.TARGET_1_SPELL_TRAP_CARD_AND_DESTROY);
//        logicalActivationRequirements.add(LogicalActivationRequirement.MUST_EXIST_AT_LEAST_ONE_SPELL_CARD_IN_FIELD);
//        userReplyForActivations.add(UserReplyForActivation.CHOOSE_ONE_SPELL_TRAP_CARD_IN_FIELD);
//    } else if (cardName.equals("twin twisters")) {
//        quickSpellEffects.add(QuickSpellEffect.DISCARD_1_CARD_THEN_TARGET_UP_TO_2_SPELL_CARDS_AND_DESTROY);
//        logicalActivationRequirements.add(LogicalActivationRequirement.MUST_EXIST_AT_LEAST_ONE_SPELL_CARD_IN_FIELD);
//        userReplyForActivations.add(UserReplyForActivation.DISCARD_1_CARD);
//        userReplyForActivations.add(UserReplyForActivation.CHOOSE_UP_TO_TWO_SPELL_TRAP_CARDS_IN_FIELD);
//    } else if (cardName.equals("messenger of peace")) {
//        continuousSpellCardEffects.add(ContinuousSpellCardEffect.MONSTERS_WITH_1500_OR_MORE_ATK_CANT_ATTACK);
//        continuousSpellCardEffects.add(ContinuousSpellCardEffect.STANDBY_PHASE_PAY_100_LP_OR_DESTROY_CARD);
//    } else if (cardName.equals("spell absorption")) {
//        continuousSpellCardEffects
//            .add(ContinuousSpellCardEffect.IF_A_SPELL_IS_ACTIVATED_OWNER_GAINS_500_LIFE_POINTS);
//    } else if (cardName.equals("supply squad")) {
//        continuousSpellCardEffects
//            .add(ContinuousSpellCardEffect.IF_A_MONSTER_OWNER_CONTROLS_IS_DESTROYED_DRAW_1_CARD);
//    } else if (cardName.equals("pot of greed")) {
//        normalSpellCardEffects.add(NormalSpellCardEffect.DRAW_2_CARDS);
//    } else if (cardName.equals("change of heart")) {
//        normalSpellCardEffects.add(NormalSpellCardEffect.CONTROL_ONE_OF_OPPONENTS_MONSTERS_UNTIL_END_PHASE);
//        logicalActivationRequirements.add(LogicalActivationRequirement.OWNER_MUST_HAVE_AT_LEAST_ONE_MONSTER_ZONE_SLOT_EMPTY);
//        logicalActivationRequirements.add(LogicalActivationRequirement.OPPONENT_MUST_CONTROL_AT_LEAST_1_MONSTER);
//        userReplyForActivations.add(UserReplyForActivation.CHOOSE_ONE_OF_OPPONENTS_MONSTERS);
//    } else if (cardName.equals("swords of revealing light")) {
//        normalSpellCardEffects.add(NormalSpellCardEffect.FLIP_OPPONENT_MONSTER_CARDS);
//        continuousSpellCardEffects.add(ContinuousSpellCardEffect.UNTIL_THIS_CARD_IS_FACE_UP_ON_FIELD_OPPONENT_MONSTERS_CANT_ATTACK);
//        continuousSpellCardEffects.add(ContinuousSpellCardEffect.NUMBER_OF_TURNS_OF_ACTIVATION_REDUCES_BY_1_IN_EACH_TURN);
//    } else if (cardName.equals("terraforming")) {
//        normalSpellCardEffects.add(NormalSpellCardEffect.ADD_SPELL_FIELD_CARD_FROM_DECK_TO_HAND);
//        logicalActivationRequirements.add(LogicalActivationRequirement.OWNER_MUST_HAVE_ONE_SPELL_FIELD_CARD_IN_DECK);
//        userReplyForActivations.add(UserReplyForActivation.CHOOSE_ONE_SPELL_FIELD_CARD_FROM_OWNER_DECK);
//    } else if (cardName.equals("monster reborn")) {
//        normalSpellCardEffects.add(NormalSpellCardEffect.SPECIAL_SUMMON_MONSTER_FROM_EITHER_GY);
//        logicalActivationRequirements.add(LogicalActivationRequirement.MUST_EXIST_AT_LEAST_1_MONSTER_IN_EITHER_GY);
//        userReplyForActivations.add(UserReplyForActivation.CHOOSE_ONE_MONSTER_FROM_EITHER_GY);
//        userReplyForActivations.add(UserReplyForActivation.CHOOSE_FACE_UP_ATTACK_POSITION_OR_DEFENSE_POSITION_OF_YOUR_MONSTER);
//    } else if (cardName.equals("advanced ritual art")) {
//        logicalActivationRequirements.add(LogicalActivationRequirement.OWNER_MUST_HAVE_RITUAL_MONSTER_IN_HAND);
//        logicalActivationRequirements.add(
//            LogicalActivationRequirement.OWNER_MUST_HAVE_CARDS_WITH_SUM_OF_LEVELS_AT_LEAST_RITUAL_MONSTERS_LEVEL_IN_DECK);
//        ritualSpellEffects.add(
//            RitualSpellEffect.SEND_NORMAL_MONSTERS_WITH_SUM_OF_LEVELS_EQUAL_TO_MONSTERS_LEVEL_FROM_DECK_TO_GRAVEYARD);
//        ritualSpellEffects.add(RitualSpellEffect.RITUAL_SUMMON_CHOSEN_MONSTER_FROM_HAND);
//        userReplyForActivations.add(
//            UserReplyForActivation.CHOOSE_NORMAL_MONSTERS_FROM_YOUR_DECK_WITH_SUM_OF_LEVELS_EQUAL_TO_A_RITUAL_MONSTER_LEVEL);
//        userReplyForActivations.add(
//            UserReplyForActivation.CHOOSE_ONE_RITUAL_MONSTER_FROM_YOUR_HAND_WITH_LEVEL_EQUAL_TO_SUM_OF_LEVELS_YOU_CHOSE);
//        userReplyForActivations
//            .add(UserReplyForActivation.CHOOSE_FACE_UP_ATTACK_POSITION_OR_DEFENSE_POSITION_OF_YOUR_MONSTER);
//    }
//
//    if (cardDescription.equals("")) {
//        summoningRequirements.add(SummoningRequirement.CAN_BE_NORMAL_SUMMONED);
//        if (level == 5 || level == 6) {
//            summoningRequirements.add(SummoningRequirement.CAN_BE_TRIBUTE_SUMMONED);
//            summoningRequirements.add(SummoningRequirement.TRIBUTE_1_MONSTER);
//        } else if (level == 7 || level == 8) {
//            summoningRequirements.add(SummoningRequirement.CAN_BE_TRIBUTE_SUMMONED);
//            summoningRequirements.add(SummoningRequirement.TRIBUTE_2_MONSTERS);
//        } else if (level >= 9) {
//            summoningRequirements.add(SummoningRequirement.CAN_BE_TRIBUTE_SUMMONED);
//            summoningRequirements.add(SummoningRequirement.TRIBUTE_3_MONSTERS);
//        }
//    } else if (cardDescription.equals("b")) {
//        summoningRequirements.add(SummoningRequirement.CAN_BE_NORMAL_SUMMONED);
//        summoningRequirements.add(SummoningRequirement.CAN_BE_TRIBUTE_SUMMONED);
//        summoningRequirements.add(SummoningRequirement.TRIBUTE_3_MONSTERS);
//        summoningRequirements.add(
//            SummoningRequirement.IN_CASE_OF_NORMAL_SUMMON_THERE_IS_NO_NEED_TO_COUNT_NUMBER_OF_TRIBUTES_NEEDED);
//        summoningRequirements
//            .add(SummoningRequirement.IN_CASE_OF_SET_THERE_IS_NO_NEED_TO_COUNT_NUMBER_OF_TRIBUTES_NEEDED);
//        uponSummoningEffects.add(UponSummoningEffect.SET_ATK_1900_IF_NORMAL_SUMMONED);
//        uponSummoningEffects.add(UponSummoningEffect.SET_ATK_1900_IF_SET);
//        uponSummoningEffects.add(UponSummoningEffect.DESTROY_ALL_OF_YOUR_OPPONENTS_CARDS);
//    } else if (cardDescription.equals("c")) {
//
//    } else if (cardDescription.equals("r")) {
//        summoningRequirements.add(SummoningRequirement.CAN_BE_RITUAL_SUMMONED);
//    }
//        if (cardName.equals("beast king barbaros")) {
//        summoningRequirements.add(
//            SummoningRequirement.IN_CASE_OF_NORMAL_SUMMON_THERE_IS_NO_NEED_TO_COUNT_NUMBER_OF_TRIBUTES_NEEDED);
//        summoningRequirements
//            .add(SummoningRequirement.IN_CASE_OF_SET_THERE_IS_NO_NEED_TO_COUNT_NUMBER_OF_TRIBUTES_NEEDED);
//        uponSummoningEffects.add(UponSummoningEffect.SET_ATK_1900_IF_NORMAL_SUMMONED);
//        uponSummoningEffects.add(UponSummoningEffect.SET_ATK_1900_IF_SET);
//        uponSummoningEffects.add(UponSummoningEffect.DESTROY_ALL_OF_YOUR_OPPONENTS_CARDS);
//    }
//        if (cardName.equals("terratiger, the empowered warrior")) {
//        uponSummoningEffects.add(UponSummoningEffect.IF_NORMAL_SUMMONED_SPECIAL_SUMMON_1_LEVEL_4_OR_LESS_NORMAL_MONSTER_FROM_HAND_IN_DEFENSE_POSITION);
//    }
//        if (cardName.equals("the tricky")) {
//        summoningRequirements.add(SummoningRequirement.CAN_BE_SPECIAL_SUMMONED);
//        // summoningRequirements.add(SummoningRequirement.CAN_BE_NORMAL_SUMMONED);
//        summoningRequirements.add(SummoningRequirement.CAN_BE_TRIBUTE_SUMMONED);
//        summoningRequirements.add(SummoningRequirement.TRIBUTE_1_MONSTER);
//        summoningRequirements.add(SummoningRequirement.DISCARD_1_CARD);
//    } else if (cardName.equals("gate guardian")) {
//        summoningRequirements.add(SummoningRequirement.CAN_BE_SPECIAL_SUMMONED);
//        summoningRequirements.add(SummoningRequirement.TRIBUTE_3_MONSTERS);
//    } else if (cardName.equals("exploder dragon")) {
//        beingAttackedEffects.add(BeingAttackedEffect.NEITHER_PLAYER_RECEIVES_BATTLE_DAMAGE_IF_MONSTER_DIES);
//        beingAttackedEffects
//            .add(BeingAttackedEffect.IF_DESTROYED_AND_SENT_TO_GRAVEYARD_SEND_ATTACKING_MONSTER_TO_GRAVEYARD);
//    } else if (cardName.equals("yomi ship")) {
//        beingAttackedEffects
//            .add(BeingAttackedEffect.IF_DESTROYED_AND_SENT_TO_GRAVEYARD_SEND_ATTACKING_MONSTER_TO_GRAVEYARD);
//    } else if (cardName.equals("marshmallon")) {
//        beingAttackedEffects.add(BeingAttackedEffect.CANNOT_BE_DESTROYED_BY_BATTLE);
//        beingAttackedEffects
//            .add(BeingAttackedEffect.IF_FACE_DOWN_AT_THE_BEGINNING_THEN_OPPONENT_RECEIVES_1000_DAMAGE);
//    } else if (cardName.equals("texchanger")) {
//        beingAttackedEffects.add(BeingAttackedEffect.NEGATE_ATTACK_ONCE_PER_TURN);
//        beingAttackedEffects
//            .add(BeingAttackedEffect.SPECIAL_SUMMON_CYBERSE_NORMAL_MONSTER_FROM_HAND_GV_DECK_ONCE_PER_TURN);
//    } else if (cardName.equals("man-eater bug")) {
//        flipEffects.add(FlipEffect.DESTROY_1_MONSTER_ON_THE_FIELD);
//    } else if (cardName.equals("suijin")) {
//        beingAttackedEffects.add(BeingAttackedEffect.SET_ATTACKING_MONSTER_ATK_TO_0_ONCE_PER_TURN);
//    } else if (cardName.equals("herald of creation")) {
//        optionalMonsterEffects.add(
//            OptionalMonsterEffect.ONCE_PER_TURN_DISCARD_1_CARD_SEND_LEVEL_7_OR_MORE_MONSTER_FROM_GY_TO_HAND);
//    } else if (cardName.equals("scanner")) {
//        optionalMonsterEffects.add(
//            OptionalMonsterEffect.ONCE_PER_TURN_CHOOSE_A_MONSTER_IN_YOUR_OPPONENTS_GRAVEYARD_AND_COPY_ALL_CHARACTERISTICS_UNTIL_THE_END_OF_THAT_TURN);
//    } else if (cardName.equals("the calculator")) {
//        continuousMonsterEffects.add(
//            ContinuousMonsterEffect.ATK_IS_SET_300_MULTIPLIED_BY_TOTAL_OF_FACE_UP_MONSTER_LEVELS_YOU_CONTROL);
//    } else if (cardName.equals("mirage dragon")) {
//        continuousMonsterEffects
//            .add(ContinuousMonsterEffect.OPPONENT_CANNOT_ACTIVATE_TRAP_CARDS_WHILE_THIS_CARD_IS_FACE_UP);
//    } else if (cardName.equals("command knight")) {
//        continuousMonsterEffects.add(ContinuousMonsterEffect.ALL_MONSTERS_OWNER_CONTROLS_GAIN_400_ATK);
//        continuousMonsterEffects.add(ContinuousMonsterEffect.CANNOT_BE_ATTACKED_IF_YOU_CONTROL_ANOTHER_MONSTER);
//    }
//}
