package project.controller.duel.CardEffects.EffectImplementations;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EffectTest {

    @Test
    void doesExistSubsetOfNormalMonsterLevelsWithSumEqualToRitualMonsterLevel() {
        //Effect effect = new Effect();
        ArrayList<Integer> monsterCardLevels = new ArrayList<>();
        monsterCardLevels.add(5);
        boolean bool1 = Effect.doesExistSubsetOfNormalMonsterLevelsWithSumEqualToRitualMonsterLevel(monsterCardLevels, 5, 1);
        assertTrue(bool1);
        monsterCardLevels.add(3);
        boolean bool2 = Effect.doesExistSubsetOfNormalMonsterLevelsWithSumEqualToRitualMonsterLevel(monsterCardLevels, 8, 2);
        assertTrue(bool2);
        monsterCardLevels.add(2);
        boolean bool3 = Effect.doesExistSubsetOfNormalMonsterLevelsWithSumEqualToRitualMonsterLevel(monsterCardLevels, 7, 3);
        assertTrue(bool3);
        boolean bool4 = Effect.doesExistSubsetOfNormalMonsterLevelsWithSumEqualToRitualMonsterLevel(monsterCardLevels, 11, 3);
        assertFalse(bool4);
        boolean bool5 = Effect.doesExistSubsetOfNormalMonsterLevelsWithSumEqualToRitualMonsterLevel(monsterCardLevels, 4, 3);
        assertFalse(bool5);
    }
}
