package com.joevicentini.transmissioncontrol.patches;

import com.joevicentini.transmissioncontrol.TransmissionSettings;
import com.joevicentini.transmissioncontrol.TransmissionSettings.InjuryType;
import com.joevicentini.transmissioncontrol.ZombieDamageContext;
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
        if (!ZombieDamageContext.isActive()) {
            return false;
        }

        InjuryType injuryType = injuryTypeFromVanillaChance(baseChance);
        if (injuryType == null) {
            return false;
        }

        int configuredChance = TransmissionSettings.chance(injuryType, bodyPart.getType());
        baseChance = configuredChance;

        if (TransmissionSettings.respectVanillaTransmission()) {
            return false;
        }

        if (Rand.Next(100) < configuredChance) {
            applySuccessfulKnoxTransmission(bodyPart);
        }

        return true;
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
