# TransmissionControl

**TransmissionControl** is a Project Zomboid Build 42 mod that lets you configure the chance of contracting the **Knox Virus** from zombie-inflicted scratches, lacerations, and bites.

Instead of being limited to Project Zomboid's default infection probabilities (7% for scratches, 25% for lacerations, and 100% for bites), you can set each transmission chance anywhere from **0% to 100%**, including **separate probabilities for different parts of the body**.

## Features

- Configure Knox infection chance from:
  - Scratches
  - Lacerations
  - Bites
- Set each chance from **0% to 100%**.
- Optional advanced settings for different body parts.
- Optionally respect Project Zomboid's vanilla Knox Transmission setting.
- Default values match vanilla behavior:
  - **Scratch:** 7%
  - **Laceration:** 25%
  - **Bite:** 100%
- Only affects **Knox Virus transmission** from zombie-inflicted wounds.
- Does not intentionally alter wound damage, bleeding, pain, healing time, clothing damage, or ordinary wound infection.
- Available in multiple Project Zomboid languages.

## Requirements

- **Project Zomboid Build 42**
- **ZombieBuddy 2.3.2 or 2.3.3**

TransmissionControl uses ZombieBuddy to modify the game's Knox infection roll directly.

ZombieBuddy must be installed and enabled for TransmissionControl to work.

## Sandbox Settings

TransmissionControl adds its own page to the Sandbox Options menu.

### General Chances

| Setting                        |  Range | Default |
| ------------------------------ | -----: | ------: |
| Scratch Transmission Chance    | 0–100% |      7% |
| Laceration Transmission Chance | 0–100% |     25% |
| Bite Transmission Chance       | 0–100% |    100% |

These values determine the probability that a zombie-inflicted wound of that type transmits the Knox Virus.

### Respect Vanilla Transmission Settings

Enabled by default.

When enabled, TransmissionControl only applies its configured infection chances to transmission routes permitted by Project Zomboid's existing **Knox Transmission** setting.

For example:

| Vanilla Knox Transmission | Bite    | Scratch / Laceration |
| ------------------------- | ------- | -------------------- |
| Blood + Saliva            | Allowed | Allowed              |
| Saliva Only               | Allowed | Blocked              |
| None                      | Blocked | Blocked              |

TransmissionControl still uses your configured percentages for routes that are allowed.

When **Respect Vanilla Transmission Settings** is disabled, TransmissionControl ignores those vanilla route restrictions and evaluates the configured infection chance directly.

## Advanced Body-Part Chances

Scratch, laceration, and bite transmission can each be configured separately for:

- Head
- Neck
- Upper Torso
- Lower Torso
- Groin
- Upper Arm
- Forearm
- Hand
- Thigh
- Shin
- Foot

Each injury type has its own **Advanced** toggle.

When an advanced section is disabled, the general chance for that injury type is used for every body part.

## What TransmissionControl Doesn't Change

TransmissionControl is designed to modify the **Knox infection probability**, not the wound itself.

A zombie attack still uses Project Zomboid's normal injury system. The mod does not intentionally change:

- Whether the zombie causes a scratch, laceration, or bite
- Wound damage
- Bleeding
- Pain
- Healing time
- Clothing damage
- Ordinary wound infection

Knox infection and normal wound infection remain separate systems.

## How It Works

Project Zomboid normally performs the Knox transmission roll as part of its zombie injury handling.

TransmissionControl uses **ZombieBuddy Java hooks** to intercept those infection-specific code paths and substitute the configured probability while preserving the rest of the vanilla injury behavior.

The mod also tracks the zombie-damage context so that ordinary scratches or cuts from non-zombie sources are not treated as zombie transmission events.

## Multiplayer

The current implementation is structured around the same zombie-damage path used by the multiplayer server. It probably works.

However, multiplayer support has **not yet been formally tested and verified** for TransmissionControl.

## Build 42

The current version of TransmissionControl is developed for **Project Zomboid Build 42**.

Compatibility with Build 41 is not provided.

## Translations

TransmissionControl includes localized sandbox-option text for Project Zomboid's supported Build 42 language set.

Contributions and corrections to translations are welcome.

## Source Code

The source code for the ZombieBuddy Java hooks is included in this repository under:

```text
42/java/src/com/joevicentini/transmissioncontrol/
```

The compiled mod JAR is located at:

```text
42/media/java/TransmissionControl.jar
```

## License

TransmissionControl is distributed under the license included in the [`LICENSE`](LICENSE) file.

## Disclaimer

TransmissionControl is an unofficial Project Zomboid mod and is not affiliated with or endorsed by The Indie Stone.
