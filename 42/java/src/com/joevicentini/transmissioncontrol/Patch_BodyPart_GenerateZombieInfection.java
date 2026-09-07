package com.joevicentini.transmissioncontrol;

import com.joevicentini.transmissioncontrol.TransmissionSettings.InjuryType;
import me.zed_0xff.zombie_buddy.Patch;
import zombie.characters.BodyDamage.BodyPart;
import zombie.core.random.Rand;

@Patch(
    className = "zombie.characters.BodyDamage.BodyPart",
    methodName = "generateZombieInfection"
)
public final class Patch_BodyPart_GenerateZombieInfection {
    private Patch_BodyPart_GenerateZombieInfection() {
    }

    @Patch.OnEnter(skipOn = true)
    public static boolean enter(
        @Patch.This BodyPart bodyPart,
        @Patch.Argument(value = 0, readOnly = false) int baseChance
    ) {
        int configuredChance = configuredChance(bodyPart, baseChance);
        if (configuredChance < 0) {
            return false;
        }

        baseChance = configuredChance;
        if (TransmissionSettings.respectVanillaTransmission()) {
            return false;
        }

        rollConfiguredTransmission(bodyPart, configuredChance);
        return true;
    }

    public static int configuredChance(BodyPart bodyPart, int baseChance) {
        if (!ZombieDamageContext.isActive()) {
            return -1;
        }

        InjuryType injuryType = injuryTypeFromVanillaChance(baseChance);
        if (injuryType == null) {
            return -1;
        }

        return TransmissionSettings.chance(injuryType, bodyPart.getType());
    }

    public static void rollConfiguredTransmission(BodyPart bodyPart, int configuredChance) {
        if (Rand.Next(100) < configuredChance) {
            applySuccessfulKnoxTransmission(bodyPart);
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

    private static InjuryType injuryTypeFromVanillaChance(int baseChance) {
        return switch (baseChance) {
            case 7 -> InjuryType.SCRATCH;
            case 25 -> InjuryType.LACERATION;
            default -> null;
        };
    }
}
