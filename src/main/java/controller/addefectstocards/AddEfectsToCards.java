package controller.addefectstocards;

import java.util.HashMap;
import java.util.Map;

import model.cardData.MonsterCardData.MonsterCardAttribute;

public class AddEfectsToCards {

    public Map<String, Enum<?>> getMonsterEffects(String cardname) {
        Map<String, Enum<?>> monsterCardEffects = new HashMap<>();
        if (cardname.equals("Command Knight")) {
            //monsterCardEffects.put("1", );
           // MonsterCardAttribute.
            monsterCardEffects.put("2", MonsterCardAttribute.DIVINE);
            return monsterCardEffects;
        }

        return null;
    }
}
