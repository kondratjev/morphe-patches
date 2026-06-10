# 🧩 kondratjev Morphe Patches

Patches for use with [Morphe](https://morphe.software).

## ❓ About

A collection of patches for various Android apps, built for the Morphe patcher.
Currently includes patches for **Lyfta** and **universal Pairip license check bypass**.

## 🩹 Patches list

<!-- PATCHES_START EXPANDED -->

<!-- Do not modify this section by hand. The patch list is generated when release.yml creates a new release.
     
     If you wish for the patches list to be collapsed, then remove the word 'EXPANDED' from the comment tag above.

     If you wish to manually keep this list updated then remove the PATCHES_START and PATCHES_END 
     comment blocks entirely. -->

#### A list of your patches will be automatically shown here after your first patches release is created.

&nbsp;

## 📚 How to use

Click here to add these patches to Morphe:  
https://morphe.software/add-source?github=kondratjev/morphe-patches

Or manually add this repository URL as a patch source in Morphe Manager:  
`https://github.com/kondratjev/morphe-patches`

<!-- The patches end tag is intentionally placed here so the first release will cleanup 
     this readme of all developer instructions above. -->
<!-- PATCHES_END -->

## 🛠️ Building

To build these patches, run:

```bash
./gradlew patches:generatePatchesList
```

The built `.mpp` file will be at `patches/build/libs/`.

## 📜 License

kondratjev Morphe Patches are licensed under the [GNU General Public License v3.0](LICENSE)
