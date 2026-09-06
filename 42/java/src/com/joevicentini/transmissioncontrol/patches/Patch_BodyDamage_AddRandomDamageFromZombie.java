package com.joevicentini.transmissioncontrol.patches;

import com.joevicentini.transmissioncontrol.ZombieDamageContext;
import me.zed_0xff.zombie_buddy.annotations.Patch;

@Patch(
    className = "zombie.characters.BodyDamage.BodyDamage",
    methodName = "AddRandomDamageFromZombie"
)
public final class Patch_BodyDamage_AddRandomDamageFromZombie {
    private Patch_BodyDamage_AddRandomDamageFromZombie() {
    }

    @Patch.OnEnter
    public static void enter() {
        ZombieDamageContext.enter();
    }

    @Patch.OnExit(onThrowable = Throwable.class)
    public static void exit() {
        ZombieDamageContext.exit();
    }
}
