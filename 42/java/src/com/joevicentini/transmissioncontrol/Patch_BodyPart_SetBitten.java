package com.joevicentini.transmissioncontrol;

import java.util.ArrayDeque;
import java.util.Deque;

import com.joevicentini.transmissioncontrol.TransmissionSettings.InjuryType;
import me.zed_0xff.zombie_buddy.Patch;
import zombie.characters.BodyDamage.BodyPart;
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
        @Patch.This BodyPart bodyPart,
        @Patch.AllArguments Object[] arguments
    ) {
        boolean oneArgumentBite = arguments.length == 1
            && arguments[0] instanceof Boolean bitten
            && bitten;

        STATES.get().push(new BiteState(
            oneArgumentBite && ZombieDamageContext.isActive(),
            bodyPart.IsInfected(),
            bodyPart.IsFakeInfected()
        ));
    }

    @Patch.OnExit(onThrowable = Throwable.class)
    public static void exit(
        @Patch.This BodyPart bodyPart,
        @Patch.Thrown Throwable thrown
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

        int configuredChance = TransmissionSettings.chance(InjuryType.BITE, bodyPart.getType());
        if (Rand.Next(100) < configuredChance) {
            applySuccessfulKnoxTransmission(bodyPart);
        } else {
            bodyPart.SetInfected(state.wasInfected);
            bodyPart.SetFakeInfected(state.wasFakeInfected);
        }
    }

    private static void applySuccessfulKnoxTransmission(BodyPart bodyPart) {
        if (TransmissionSettings.mortalityMode() == 7) {
            bodyPart.SetInfected(false);
            bodyPart.SetFakeInfected(true);
        } else {
            bodyPart.SetInfected(true);
            bodyPart.SetFakeInfected(false);
        }
    }

    private record BiteState(boolean active, boolean wasInfected, boolean wasFakeInfected) {
    }
}
