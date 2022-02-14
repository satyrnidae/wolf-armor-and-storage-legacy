# Wolf Armor and Storage

![Java CI with Gradle](https://github.com/satyrnidae/wolfarmor/workflows/Java%20CI%20with%20Gradle/badge.svg) [![CurseForge](http://cf.way2muchnoise.eu/full_253689_downloads.svg)](https://minecraft.curseforge.com/projects/wolf-armor-and-storage?gameCategorySlug=mc-mods&projectID=253689) [![Versions](http://cf.way2muchnoise.eu/versions/253689.svg)](https://minecraft.curseforge.com/projects/wolf-armor-and-storage/files)

**Wolf Armor and Storage** is a simple Minecraft mod that adds armor, storage, and other minor tweaks to wolves.

I've long been irked by how fragile the vanilla wolves are.  If you do anything more than leave them sitting in your living room, you are very likely to quickly become wolf-less.  Now, equipped with the proper adornments, your wolves can become the veritable powerhouses that they were always meant to be, and you can gain a helpful adventuring companion to boot!

## FAQ

- **Could you update to X.X.X?**: Updating to a new Minecraft version will take time as the Minecraft codebase has changed drastically since I wrote this mod way back in 1.7.10. Rest assured that I am working on updating the mod currently, and you'll have your armored wolves in 1.16.5-1.18.1 as soon as the migration is complete. Bear with me a little longer!
- **Can you port to X modloader?**: Porting to other modloaders also takes time; however, I plan to support both Fabric and Forge modloaders in version 1.16.5+ with the help of the Architectury framework by shedaniel.
- **Can I use this mod in my modpack?**: Absolutely! I'm hopeful that one day everyone will have wolves that they can depend on as both fluffy friends and comrades in arms, but until that day I'm content with just getting this mod in the hands of as many people as possible. A word of warning though. The only place that I have personally uploaded and distributed this mod are the releases on this CurseForge page and on my GitHub. Be very careful downloading this mod from other vendors. If you do so, make sure that the **MD5 Checksum** of the file matches the listed checksum on the file downloads page: ![MD5 Checksum](https://i.imgur.com/fGPVF1S.png)

But enough of that, let's get on with the details!

## Armor Up Your Wolves

![An armored wolf in action](https://imgur.com/NSu4Y4o.gif)

*Fig. 1: An armored wolf in action*

This mod adds five new armors for your wolves, each corresponding to one of the five player armor types in vanilla Minecraft.  Armor can be found in generated loot chests, or crafted in a workbench.

### Crafting Armor for your Wolf

To craft a piece of wolf armor, you must obtain three armor pieces (two sets of boots and a helmet) and some extra material (leather, iron nuggets, iron ingots, gold ingots, or diamonds), and arrange them as below:

![Leather armor; helmet in center above both boots, with two leathers surrounding the boots in the middle row](https://imgur.com/u5y4xgq.gif)

*Fig 2a: The crafting recipe for leather armor*

![Chainmail armor; helmet in center above both boots, with two iron nuggets surrounding the boots in the middle row](https://imgur.com/gbcymM6.gif)

*Fig 2b: The crafting recipe for chainmail armor*

![Iron armor; helmet in center above both boots, with two iron ingots surrounding the boots in the middle row](https://imgur.com/j3icV2W.gif)

*Fig 2c: The crafting recipe for iron armor*

![Gold armor; helmet in center above both boots, with two gold ingots surrounding the boots in the middle row](https://imgur.com/VacjTu8.gif)

*Fig 2d: The crafting recipe for gold armor*

![Diamond armor; helmet in center above both boots, with two diamonds surrounding the boots in the middle row](https://imgur.com/mQxMCRl.gif)

*Fig 2e: The crafting recipe for diamond armor*

### Interactions and Equipping Armor

When armor is equipped on a wolf, they gain the same amount of protection as a player would with a full set of armor made from the corresponding material!  To apply armor to a wolf, either simply right-click them while holding the armor you wish to apply or sneak and right click them with any other item.  This opens the wolf's GUI and allows you to equip the armor of your choice.

![Cedar the wolf's GUI, showing his diamond wolf armor equipped](https://imgur.com/KJFCqZy.png)

*Fig 3a: The wolf GUI*

Armor can also be equipped indirectly via dispenser! The wolf does have to be tamed beforehand, however.

![Applying gold armor indirectly via a dispenser](https://imgur.com/3FXWsm9.gif)

*Fig 3b: Applying armor via dispenser*

### Dyeing leather armor

Like leather armor, leather wolf armor may be dyed any color:

![Lobo the wolf wearing purple leather armor](https://imgur.com/Q8Sv8wA.png)

*Fig 3a: A wolf wearing purple-dyed leather armor...*

![Leather armor in the wolf inventory](https://imgur.com/GYuvOTj.png)

*Fig 3b: ...and the purple armor in the wolf's inventory*

![Leather wolf armor dyed a questionable color](https://imgur.com/jaP5N55.png)

*Fig 3c: Wolf armor dyed a questionable color*

## Wolf Pack? Pack Wolf!

The second major addition of this mod is the ability to apply a chest to a tamed wolf, thus giving them some useful inventory space!

![Henrietta the pack wolf, with her six inventory spaces](https://imgur.com/V5EIp4b.png)

*Fig 4: A wolf with a chest equipped*

As an added bonus, not only is this inventory space helpful as storage, but if you fill it up with your wolf's favorite foods, they can heal on the fly from their inventory!

Simply right-click a tamed wolf that you own with any wooden chest (provided it is properly registered in the Ore Dictionary as such) and the inventory space will be added.  To remove an equipped chest, just click the "Remove Chest" button below the armor slot in the wolf's inventory GUI.

Chest size is configurable from one to fifteen slots, and chests can be disabled entirely if so desired.  Wolf auto-healing can also be disabled.

## Show Me Those Stats!

Since 3.6.4, there is now an option to show wolves' stats above their heads, just under their nameplates!  It even works for non-named wolves!

![Fenrir and his friend sporting some snazzy stat bars](https://imgur.com/EJwK8ps.gif)

*Fig 5: Tamed wolves will display stats above their heads (optional)*

This option is disabled by default; to enable it, open the config file and set ``B:stats_in_nameplate`` to ``true``.

## Hungry Hungry Hippos— er, Wolves

As a balancing option, you can enable a version of hunger for wolves!  The current hunger level can be displayed in the GUI and above the wolf's head (if either of those readouts are currently on).

![Ashleigh has lots of mutton, but she's not hungry yet...](https://imgur.com/X0xheVy.png)

*Fig 6: A wolf's hunger bar displayed over their head*

Hunger and Saturation effects will now also affect wolves with hunger, so watch out when you're fighting those husks!

![Lillian gets a bit hungry fighting a husk](https://imgur.com/YQgor4F.gif)

*Fig 7: Hunger potions affect wolves if hunger is enabled*

If hunger is set to "full", a wolf can get damaged by starvation until it reaches 4 hearts (the same health level as a wild wolf).  By setting "starvation" to "true", you can also make them starve to death.

![Lillian starved to death](https://imgur.com/iuQRdPJ.png)

*Fig 8: I feel bad for enabling this, and so should you.  Why would anyone do this!?*

## Miscellaneous Additions

I've also taken the liberty of adding a couple other new features.

First, Advancements! There are two new "Husbandry" advancements you can earn: "Armor Up!", by equipping armor to your wolves, and "Wolf Pack", by giving your wolf some inventory space.

![Armor Up and Wolf Pack, visible in the Husbandry section](https://imgur.com/zfL35Vl.png)

*Fig 9: The new advancements*

Wild wolves can also be set to howl at the full moon.  This can make it easier to find them when there's a full moon out.  You can enable or disable this feature per your preference, but it is disabled by default.

## Mod Configuration

There are several configurable options for this mod. Currently, they can only be configured via the configuration file, located in ``config\satyrn\wolfarmor.cfg``.  What follows is a list of all of the options and their default settings.

``general`` category:
* Enable wolf backpacks: ``B:backpack=true``
* Backpack size (width by height): ``S:backpack_size=3x2``

``behavior`` category:
* Allow wolves to heal themselves from their backpacks: ``B:auto_eat=true``
* Allow wild wolves to howl at the moon: ``B:howl_at_moon=false``
* Set tamed wolf hunger option (disabled/heal/full): ``S:hunger=disabled``
* Set whether hungry wolves can starve to death when hunger is set to full: ``B:starvation=false``

``client`` category (these can only be set client-side):
* Show the wolf armor model: ``B:armor_model=true``
* Show the wolf backpack model: ``B:backpack_model=true``
* Show tamed wolves' stats (health, armor, and hunger) in the wolf's inventory: ``B:stats_in_gui=true``
* Show tamed wolves' stats (health, armor, and hunger) above the wolf's head: ``B:stats_in_nameplate=false``


## Compatibility


This mod utilizes a shadowed copy of SpongePowered Mixin 0.7.11.  Mods which are incompatible with Sponge, or which implement from an earlier version of the Mixin library, or which also use Mixin to alter the wolf entity may be incompatible with this mod.

Wolf Armor is generally compatible with any mod that does not have its own mixins on EntityWolf, and potentially some that do.  If you encounter an incompatibility with a specific mod, please report it on the [GitHub Issues Page](https://github.com/satyrnidae/wolfarmor/issues).

## Supported Mods

These mods are explicitly supported. Any issues with these mods should be reported on the [GitHub Issues Page](https://github.com/satyrnidae/wolfarmor/issues).

### Minecraft 1.12.2

* [Phosphor](https://www.curseforge.com/minecraft/mc-mods/phosphor) by jellysquid_
* [Sophisticated Wolves](https://www.curseforge.com/minecraft/mc-mods/sophisticated-wolves) by NightKosh
  * Was broken for a while due to a deleted API class; fixed in v3.8.0+
* [Carry On](https://www.curseforge.com/minecraft/mc-mods/carry-on) by Tschipp and Purplicious_Cow_
  * **Known Issues**
    * Interaction is slightly buggy due to the pick up functionality; must use a non-empty hand
* [Quark](https://www.curseforge.com/minecraft/mc-mods/quark) by Vazkii, mcvinnyq, and wiresegal
  * **Known Issues**
    * Interaction is slightly buggy due to petting, etc.
    * Render layers on foxhounds are specifically disabled
* [Let Sleeping Dogs Lie](https://www.curseforge.com/minecraft/mc-mods/let-sleeping-dogs-lie) by iChun
  * Since v3.7.0

![Newly compatible!](https://imgur.com/4ILIvvv.png)

*Fig 10: Lillian's enjoying this one!*

* [Electroblob's Wizardry](https://www.curseforge.com/minecraft/mc-mods/electroblobs-wizardry)
  * **Known Issues**
    * A GL issue is causing the wolf armor layers to glow on vanilla and spirit wolves.

![Spirit wolf time!](https://i.imgur.com/6p4t0K4.png?1)

*Fig 11: Spooky, yet also, snazzy!*

* [Mo' Bends](https://www.curseforge.com/minecraft/mc-mods/mo-bends)
  * Since 3.8.0

![Panting Armored Wolf](https://imgur.com/HDwSUnj.png)

*Fig 12: Adds some extra life to your wolves!*

## Compatible mods

These mods are compatible (i.e. won't crash horribly), but are not explicitly supported.

### Minecraft 1.12.2

* [Zoology](https://www.curseforge.com/minecraft/mc-mods/zoology) by RWTema
* [Atum 2: Return to the Sands](https://www.curseforge.com/minecraft/mc-mods/atum#c328) by Shadowclaimer and Girafi
  * Desert wolves remain unchanged.
* [Better Animations Collection 2](https://www.curseforge.com/minecraft/mc-mods/better-animations-collection-2) by Fusz_
  * Wolf armor uses the vanilla model, and does not match up with the wolf when sitting or when the tail is waving.
  * Framework is in place to add compatibiltiy in the future.
* [Better Animal Models](https://www.curseforge.com/minecraft/mc-mods/better-animal-models) by cybercat5555, ist_meow, and Ozelot5836 and [Better Animals Plus](https://www.curseforge.com/minecraft/mc-mods/betteranimalsplus) by cybercat5555 and ist_meow
  * Uses the vanilla wolf render model, which does not match up with the new wolf model.
  * Disable the chest and armor render in the configuration options to remedy this.
  * Framework is in place to add compatibility in the future.

## Incompatible Mods

The following mods are not compatible, and there are no plans to ensure compatibility at this time.

### Minecraft 1.12.2

* [Stacy's Wolves](https://www.curseforge.com/minecraft/mc-mods/stacys-wolves/files) by stacyplays, nathan_oneday, Lyrael_Rayne, maddielines, SnowShock35, and AKTheKnight
* [Doggy Talents](https://www.curseforge.com/minecraft/mc-mods/doggy-talents) by percivalalb, SimonMeskens, and XNovaViperX

## Contact Me

The best way to reach me is through the CurseForge mod page and on GitHub!

## Special Thanks

Thanks to everyone who has contributed to the continued development of Wolf Armor and Storage!

### Wolf Armor and Storage Legacy

* [CovertJaguar](https://github.com/CovertJaguar)
  * Suggested fix for broken build
* [gsbaoge](https://github.com/gsbaoge)
  * Pre-mixin capability subsystem
  * Created Simplified Chinese translation file
* [NightKosh](https://github.com/nightkosh)
  * Updated the Russian translation file
  * Helped with Sophisticated Wolves compatibility
* [papadave](https://github.com/papadave66)
  * Contributed to the Simplified Chinese translation file
  * Created the Traditional Chinese translation file
* [Romz24](https://github.com/Romz24)
  * Updated the Russian translation file
* [Samlegamer](https://github.com/Samlegamer)
  * Created the French translation file
* [SanAndreasP](https://github.com/SanAndreasP)
  * Helped with Phosphor compatibility
*  And, of course, anyone who's submitted an [issue](https://github.com/satyrnidae/wolfarmor/issues) on GitHub!

Thanks for supporting Wolf Armor and Storage!
