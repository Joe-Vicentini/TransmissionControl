package com.joevicentini.transmissioncontrol.patches;

import java.util.ArrayDeque;
import java.util.Deque;

import com.joevicentini.transmissioncontrol.TransmissionSettings;
import com.joevicentini.transmissioncontrol.TransmissionSettings.InjuryType;
import com.joevicentini.transmissioncontrol.ZombieDamageContext;
import me.zed_0xff.zombie_buddy.annotations.Patch;
import zombie.core.random.Rand;

@Patch(
    className = "zombie.characters.BodyDamage.BodyPart",
    methodName = "SetBitten"
)
public final class Patch_BodyPart_SetBitten {
    private static final ThreadLocal<Deque<BiteState>> STATES = ThreadLocal.withInitial(ArrayDeque::new);

    private Patch_BodyPart_SetBitten() {
    }

    @Patch.OnEnter
    public static void enter(
        @Patch.AllArguments Object[] arguments,
        @Patch.Field(value = "isInfected", readOnly = true) boolean isInfected,
        @Patch.Field(value = "isFakeInfected", readOnly = true) boolean isFakeInfected
    ) {
        boolean oneArgumentBite = arguments.length == 1
            && arguments[0] instanceof Boolean bitten
            && bitten;

        STATES.get().push(new BiteState(
            oneArgumentBite && ZombieDamageContext.isActive(),
            isInfected,
            isFakeInfected
        ));
    }

    @Patch.OnExit(onThrowable = Throwable.class)
    public static void exit(
        @Patch.Thrown Throwable thrown,
        @Patch.Field(value = "type", readOnly = true) Object bodyPartType,
        @Patch.Field(value = "isInfected") boolean isInfected,
        @Patch.Field(value = "isFakeInfected") boolean isFakeInfected
    ) {
        Deque<BiteState> states = STATES.get();
        if (states.isEmpty()) {
            return;
        }

        BiteState state = states.pop();
        if (states.isEmpty()) {
            STATES.remove();
        }

        if (thrown != null || !state.active) {
            return;
        }

        if (TransmissionSettings.respectVanillaTransmission()
            && TransmissionSettings.transmissionMode() == 4) {
            return;
        }

        int configuredChance = TransmissionSettings.chance(InjuryType.BITE, bodyPartType);
        if (Rand.Next(100) < configuredChance) {
            if (TransmissionSettings.mortalityMode() == 7) {
                isInfected = false;
                isFakeInfected = true;
            } else {
                isInfected = true;
                isFakeInfected = false;
            }
        } else {
            isInfected = state.wasInfected;
            isFakeInfected = state.wasFakeInfected;
        }
    }

    private record BiteState(boolean active, boolean wasInfected, boolean wasFakeInfected) {
    }
}
