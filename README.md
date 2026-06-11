# 🧩 kondratjev Morphe Patches

Patches for use with [Morphe](https://morphe.software).

## ❓ About

A collection of bytecode patches for Android apps, built for the Morphe patcher.

| App | Package | Patches |
|---|---|---|
| Lyfta | `com.lyfta` | Enable Premium |
| Pillo | `xyz.rtrvr.pillo` | Unlock Premium |
| Medisafe | `com.medisafe.android.client` | Unlock Premium |
| RuStore | `ru.vk.store` | Enable debug menu, Disable ads, Bypass authorization |
| *Universal* | — | Change version code, Disable Pairip license check |

## 🩹 Patches list

<!-- PATCHES_START EXPANDED -->
> **[v1.4.0-dev.1](https://github.com/kondratjev/morphe-patches/releases/tag/v1.4.0-dev.1)**  •  `dev`  •  7 patches total
<details open>
<summary>📦 RuStore  •  2 patches</summary>
<br>

**🎯 Supported versions:**

| 1.103.0.3 |
| :---: |

| 💊 Patch | 📜 Description | ⚙️ Options |
|----------|----------------|-----------|
| [Disable ads](#disable-ads) | Disables all advertisements by forcing remote feature toggle reads to return false for all features. Note: this may affect other remote features as well. |  |
| [Enable debug menu](#enable-debug-menu) | Enables the hidden in-app debug screen and other developer features by forcing local feature toggles to true. |  |

</details>

<details open>
<summary>📦 Lyfta  •  1 patch</summary>
<br>

**🎯 Supported versions:**

| 1.572 |
| :---: |

| 💊 Patch | 📜 Description | ⚙️ Options |
|----------|----------------|-----------|
| [Enable Premium](#enable-premium) | Enables app features locked behind the subscription paywall. |  |

</details>

<details open>
<summary>📦 Medisafe  •  1 patch</summary>
<br>

**🎯 Supported versions:**

| 9.50.3 |
| :---: |

| 💊 Patch | 📜 Description | ⚙️ Options |
|----------|----------------|-----------|
| [Unlock Premium](#unlock-premium) | Unlocks all premium features including unlimited dependents, medfriends, custom ringtones and theme colors. |  |

</details>

<details open>
<summary>📦 Pillo  •  1 patch</summary>
<br>

**🎯 Supported versions:**

| 0.6.10 |
| :---: |

| 💊 Patch | 📜 Description | ⚙️ Options |
|----------|----------------|-----------|
| [Unlock Premium](#unlock-premium) | Unlocks premium features and removes ads by forcing subscription state to always be active. |  |

</details>

<details open>
<summary>🌐 Universal  •  2 patches</summary>
<br>

| 💊 Patch | 📜 Description | ⚙️ Options |
|----------|----------------|-----------|
| [Change version code](#change-version-code) | Changes the version code of the app to the value specified in patch options. Except when mounting, this can prevent app stores from updating the app and allow the app to be installed over an existing installation that has a higher version code. By default, the highest version code is set. | • Version code |
| [Disable Pairip license check](#disable-pairip-license-check) | Disables Play Integrity API (pairip) client-side license check. This patch does not bypass Play Integrity attestation or pairipcore virtualization. |  |

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

kondratjev Morphe Patches are licensed under the [GNU General Public License v3.0](LICENSE)
