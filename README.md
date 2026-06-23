# 🧩 Kondratjev Patches

Patches for use with [Morphe](https://morphe.software).

## ❓ About

A collection of bytecode patches for Android apps, built for the Morphe patcher.

## 🩹 Patches list

<!-- PATCHES_START EXPANDED -->
> **[v1.14.0](https://github.com/kondratjev/morphe-patches/releases/tag/v1.14.0)**  •  `main`  •  23 patches total
<details open>
<summary>📦 RuStore  •  6 patches</summary>
<br>

**🎯 Supported versions:**

| 1.103.0.3 |
| :---: |

| 💊 Patch | 📜 Description | ⚙️ Options |
|----------|----------------|-----------|
| [Disable Mine redesign](#disable-mine-redesign) | Reverts the Mine screen to the classic layout, disabling the redesigned V2/V3 interface. |  |
| [Disable ads](#disable-ads) | Removes all advertisements from the app. |  |
| [Disable analytics](#disable-analytics) | Disables VK-specific analytics. |  |
| [Disable background scan](#disable-background-scan) | Disables the periodic Kaspersky background device scan. |  |
| [Disable gaming profile](#disable-gaming-profile) | Removes the Game Profile section from the Mine screen, including the button and the usage statistics screen. |  |
| [Skip update auth](#skip-update-auth) | Removes the login requirement when updating apps. |  |

</details>

<details open>
<summary>📦 SoundCloud  •  3 patches</summary>
<br>

**🎯 Supported versions:**

| 2026.06.03-release |
| :---: |

| 💊 Patch | 📜 Description | ⚙️ Options |
|----------|----------------|-----------|
| [Disable OneTrust consent popup](#disable-onetrust-consent-popup) | Disables the OneTrust consent/cookies popup on first launch. |  |
| [Disable telemetry](#disable-telemetry) | Disables SoundCloud's telemetry system. |  |
| [Enable SoundCloud Go+](#enable-soundcloud-go) | Enables all premium features, hides upsell UI and ads. |  |

</details>

<details open>
<summary>📦 Pillo  •  2 patches</summary>
<br>

**🎯 Supported versions:**

| 0.6.11 |
| :---: |

| 💊 Patch | 📜 Description | ⚙️ Options |
|----------|----------------|-----------|
| [Disable Pulse tracking](#disable-pulse-tracking) | Disables PulseSDK analytics sending events to events.pillo.care. |  |
| [Unlock Premium](#unlock-premium) | Unlocks premium features and removes ads. |  |

</details>

<details open>
<summary>📦 FatSecret  •  2 patches</summary>
<br>

**🎯 Supported versions:**

| 11.5.0.4 |
| :---: |

| 💊 Patch | 📜 Description | ⚙️ Options |
|----------|----------------|-----------|
| [Disable community tab](#disable-community-tab) | Hides the News/Community tab from the bottom navigation. |  |
| [Unlock Premium](#unlock-premium) | Unlocks all FatSecret Gold features and hides the Premium tab. |  |

</details>

<details open>
<summary>📦 Lifesum  •  1 patch</summary>
<br>

**🎯 Supported versions:**

| 20.6.1 |
| :---: |

| 💊 Patch | 📜 Description | ⚙️ Options |
|----------|----------------|-----------|
| [Unlock Premium](#unlock-premium) | Unlocks all premium features. |  |

</details>

<details open>
<summary>📦 Lyfta  •  1 patch</summary>
<br>

**🎯 Supported versions:**

| 1.573 |
| :---: |

| 💊 Patch | 📜 Description | ⚙️ Options |
|----------|----------------|-----------|
| [Unlock Premium](#unlock-premium) | Unlocks all premium features. |  |

</details>

<details open>
<summary>📦 Medisafe  •  1 patch</summary>
<br>

**🎯 Supported versions:**

| 9.50.3 |
| :---: |

| 💊 Patch | 📜 Description | ⚙️ Options |
|----------|----------------|-----------|
| [Unlock Premium](#unlock-premium) | Unlocks all premium features. |  |

</details>

<details open>
<summary>📦 Ornament  •  1 patch</summary>
<br>

**🎯 Supported versions:**

| 4.15.1 |
| :---: |

| 💊 Patch | 📜 Description | ⚙️ Options |
|----------|----------------|-----------|
| [Unlock Premium](#unlock-premium) | Unlocks part of Ornament Pro features. |  |

</details>

<details open>
<summary>📦 Parcels  •  1 patch</summary>
<br>

**🎯 Supported versions:**

| 3.0.11 |
| :---: |

| 💊 Patch | 📜 Description | ⚙️ Options |
|----------|----------------|-----------|
| [Unlock Premium](#unlock-premium) | Unlocks premium features. |  |

</details>

<details open>
<summary>📦 WiseMeal  •  1 patch</summary>
<br>

**🎯 Supported versions:**

| 1.3.0 |
| :---: |

| 💊 Patch | 📜 Description | ⚙️ Options |
|----------|----------------|-----------|
| [Unlock Premium](#unlock-premium) | ROOT ONLY. Unlocks all premium features. |  |

</details>

<details open>
<summary>📦 Yazio  •  1 patch</summary>
<br>

**🎯 Supported versions:**

| 12.87.0 |
| :---: |

| 💊 Patch | 📜 Description | ⚙️ Options |
|----------|----------------|-----------|
| [Unlock Premium](#unlock-premium) | Unlocks all Yazio Pro features. |  |

</details>

<details open>
<summary>🌐 Universal  •  3 patches</summary>
<br>

| 💊 Patch | 📜 Description | ⚙️ Options |
|----------|----------------|-----------|
| [Change version code](#change-version-code) | Changes the app version code, allowing installation over existing versions. | • Version code |
| [Disable Pairip license check](#disable-pairip-license-check) | Disables the Pairip license verification check. |  |
| [Disable analytics](#disable-analytics) | Disables analytics and tracking from multiple SDKs, including AppMetrica, MyTracker, Firebase, Sentry, Google Analytics, Amplitude, Mixpanel, Adjust, AppsFlyer, Facebook, MoEngage, and comScore. |  |

</details>

<!-- PATCHES_END -->

## 📚 How to use

Click here to add these patches to Morphe:

> https://morphe.software/add-source?github=kondratjev/morphe-patches

Or manually add this repository URL in Morphe Manager → Sources:

> `https://github.com/kondratjev/morphe-patches`

## ⚖️ Disclaimer

This project is provided for **educational purposes only**. The patches are intended to help developers understand Android bytecode modification and the Morphe patching framework.

- **No affiliation** — This project is not affiliated with, endorsed by, or connected to any of the patched applications or their developers.
- **No warranty** — These patches are provided "as is" without warranty of any kind. Use at your own risk.
- **Terms of Service** — Using modified versions of applications may violate their Terms of Service. It is your responsibility to review and comply with applicable terms.
- **No redistribution** — The patched APK files should not be redistributed. These patches are meant to be applied by end users to their own legally obtained APKs.
- **Fair use** — These patches are developed through independent reverse engineering for interoperability and personal use, consistent with fair use principles.

The author assumes no liability for any consequences resulting from the use of these patches.

## 🛠️ Building

```bash
./gradlew patches:generatePatchesList
```

The built `.mpp` file will be at `patches/build/libs/`.

## 📜 License

Kondratjev Patches are licensed under the [GNU General Public License v3.0](LICENSE)
