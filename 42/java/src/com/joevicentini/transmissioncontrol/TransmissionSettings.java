package com.joevicentini.transmissioncontrol;

import zombie.SandboxOptions;
import zombie.characters.BodyDamage.BodyPartType;

public final class TransmissionSettings {
    private static final String PREFIX = "TransmissionControl.";

    private TransmissionSettings() {
    }

    public enum InjuryType {
        SCRATCH("Scratch", 7),
        LACERATION("Laceration", 25),
        BITE("Bite", 100);

        private final String optionName;
        private final int fallbackChance;

        InjuryType(String optionName, int fallbackChance) {
            this.optionName = optionName;
            this.fallbackChance = fallbackChance;
        }
    }

    public static boolean respectVanillaTransmission() {
        return booleanOption(PREFIX + "RespectVanillaTransmission", true);
    }

    public static int transmissionMode() {
        return SandboxOptions.instance.lore.transmission.getValue();
    }

    public static int mortalityMode() {
        return SandboxOptions.instance.lore.mortality.getValue();
    }

    public static int chance(InjuryType injuryType, BodyPartType bodyPartType) {
        int generalChance = integerOption(PREFIX + injuryType.optionName + "Chance", injuryType.fallbackChance);
        if (!booleanOption(PREFIX + "Advanced" + injuryType.optionName, false)) {
            return clampChance(generalChance);
        }

        String bodyPart = bodyPartOptionName(bodyPartType);
        if (bodyPart == null) {
            return clampChance(generalChance);
        }

        return clampChance(integerOption(
            PREFIX + injuryType.optionName + bodyPart + "Chance",
            generalChance
        ));
    }

    private static int integerOption(String name, int fallback) {
        SandboxOptions.SandboxOption option = SandboxOptions.instance.getOptionByName(name);
        if (option instanceof SandboxOptions.IntegerSandboxOption integerOption) {
            return integerOption.getValue();
        }
        return fallback;
    }

    private static boolean booleanOption(String name, boolean fallback) {
        SandboxOptions.SandboxOption option = SandboxOptions.instance.getOptionByName(name);
        if (option instanceof SandboxOptions.BooleanSandboxOption booleanOption) {
            return booleanOption.getValue();
        }
        return fallback;
    }

    private static int clampChance(int chance) {
        return Math.max(0, Math.min(100, chance));
    }

    private static String bodyPartOptionName(BodyPartType bodyPartType) {
        if (bodyPartType == null) {
            return null;
        }

        return switch (bodyPartType) {
            case Head -> "Head";
            case Neck -> "Neck";
            case Torso_Upper -> "UpperTorso";
            case Torso_Lower -> "LowerTorso";
            case Groin -> "Groin";
            case UpperArm_L, UpperArm_R -> "UpperArm";
            case ForeArm_L, ForeArm_R -> "Forearm";
            case Hand_L, Hand_R -> "Hand";
            case UpperLeg_L, UpperLeg_R -> "Thigh";
            case LowerLeg_L, LowerLeg_R -> "Shin";
            case Foot_L, Foot_R -> "Foot";
            default -> null;
        };
    }
}
