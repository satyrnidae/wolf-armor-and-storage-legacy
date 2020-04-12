# Wolf Armor and Storage [![Curseforge](http://cf.way2muchnoise.eu/wolf-armor-and-storage.svg)](https://minecraft.curseforge.com/projects/wolf-armor-and-storage?gameCategorySlug=mc-mods&projectID=253689) [![Versions](http://cf.way2muchnoise.eu/versions/wolf-armor-and-storage.svg)](https://minecraft.curseforge.com/projects/wolf-armor-and-storage/files)

**_Note: the most recent supported version of this mod is <span style='color: red'>3.4.1.1-universal</span>, and this description is up-to-date with that version as of 2020-04-11._**

**_Note 2: Since it gets asked a lot, yes, you may include this mod in your modpack._**

**Wolf Armor and Storage** is a simple Minecraft mod that adds armor, storage, and other minor tweaks to wolves.

I've long been irked by how fragile the vanilla wolves are.  If you do anything more than leave them sitting in your living room, you are very likely to quickly become wolf-less.  Now, equipped with the proper adornments, your wolves can become the veritable powerhouses that they were always meant to be, and you can gain a helpful adventuring companion to boot!

![An armored wolf in action](https://imgur.com/NSu4Y4o.gif)<br>*Fig. 1: An armored wolf in action*

## Armor Up Your Wolves

This mod adds five new armors for your wolves, each corresponding to one of the five armor types in vanilla Minecraft.  Armor can be found in generated loot chests, or crafted in a workbench.

### Crafting Armor for your Wolf

To craft a piece of wolf armor, you must obtain three armor pieces (two sets of boots and a helmet) and some extra material (leather, iron nuggets, iron ingots, gold ingots, or diamonds), and arrange them as below:

![Leather armor; helmet in center above both boots, with two leathers surrounding the boots in the middle row](https://imgur.com/u5y4xgq.gif)<br>*Fig 2a: The crafting recipe for leather armor*

![Chainmail armor; helmet in center above both boots, with two iron nuggets surrounding the boots in the middle row](https://imgur.com/gbcymM6.gif)<br>*Fig 2b: The crafting recipe for chainmail armor*

![Iron armor; helmet in center above both boots, with two iron ingots surrounding the boots in the middle row](https://imgur.com/j3icV2W.gif)<br>*Fig 2c: The crafting recipe for iron armor*

![Gold armor; helmet in center above both boots, with two gold ingots surrounding the boots in the middle row](https://imgur.com/VacjTu8.gif)<br>*Fig 2c: The crafting recipe for iron armor*

![Diamond armor; helmet in center above both boots, with two diamonds surrounding the boots in the middle row](https://imgur.com/mQxMCRl.gif)<br>*Fig 2c: The crafting recipe for iron armor*

### Interactions and Equipping Armor

When armor is equipped on a wolf, they gain the same amount of protection as a player would with a full set of armor made from the corresponding material!  To apply armor to a wolf, either simply right-click them while holding the armor you wish to apply or sneak and right click them with any other item.  This opens the wolf's GUI and allows you to equip the armor of your choice.

![Cedar the wolf's GUI, showing his diamond wolf armor equipped](https://imgur.com/KJFCqZy.png)<br>*Fig 3a: The wolf GUI*

Armor can also be equipped indirectly via dispenser! The wolf does have to be tamed beforehand, however.

![Applying gold armor indirectly via a dispenser](https://imgur.com/3FXWsm9.gif)<br>*Fig 3b: Applying armor via dispenser*

### Dyeing leather armor

Like leather armor, leather wolf armor may be dyed any color:

![Lobo the wolf wearing purple leather armor](https://imgur.com/Q8Sv8wA.png)<br>*Fig 3a: A wolf wearing purple-dyed leather armor...*

![Leather armor in the wolf inventory](https://imgur.com/GYuvOTj.png)<br>*Fig 3b: ...and the purple armor in the wolf's inventory*

![Leather wolf armor dyed a questionable color](https://imgur.com/jaP5N55.png)<br>*Fig 3c: Wolf armor dyed a questionable color*

## Wolf Pack? Pack Wolf!

The second major addition of this mod is the ability to apply a chest to a tamed wolf, thus giving them some useful inventory space!

![Henrietta the pack wolf, with her six inventory spaces](https://imgur.com/V5EIp4b.png)<br>*Fig 4: A wolf with a chest equipped*

As an added bonus, not only is this inventory space helpful as storage, but if you fill it up with your wolf's favorite foods, they can heal on the fly from their inventory!

Simply right-click a tamed wolf that you own with any wooden chest (provided it is properly registered in the Ore Dictionary as such) and the inventory space will be added.  To remove an equipped chest, just click the "Remove Chest" button below the armor slot in the wolf's inventory GUI.

Chest size is configurable from one to fifteen slots, and chests can be disabled entirely if so desired.  Wolf auto-healing can also be disabled.

## Miscellaneous Additions

I've also taken the liberty of adding a couple other new features:
* Advancements! There are two you can earn: "Armor Up!", by equipping armor to your wolves, and "Wolf Pack", by giving your wolf some inventory space.
* Wolves can now howl on the full moon.  This can make it easier to find them when there's a full moon out.  You can enable or disable this feature per your preference.

## Mod Configuration

There are several configurable options for this mod.  You can either access these through the mod configuration GUI, or by editing the configuration file under `config/attributestudios/wolfarmor.cfg`.

Server owners or single-player users can configure the following:
* Enable or disable chests
* Set the maximum size of the wolf chests (from 1x1 up to 5x3)
* Enable or disable auto-healing wolves
* Enable or disable wild wolves howling on a full moon

Client-side users can also specify a couple visual options:
* Enable or disable wolf armor and health display in the GUI
* Enable or disable armor and wolf backpack rendering

## Compatibility

This mod utilizes a shadowed copy of SpongePowered Mixin 0.7.5.  Mods which are incompatible with Sponge, or which implement from a different version of the Mixin library, or which also use Mixin to alter the wolf entity may be incompatible with this mod.

Wolf Armor is generally compatible with any mod that does not alter the manner in which wolves function, and may be compatible with some that do.  If you encounter an incompatibility with a specific mod, please report it either here, or on the [github](https://github.com/satyrnidae/wolfarmor/issues) page for the mod.

### Supported Mods

These mods are explicitly supported, with known issues below.  If any further issues are found while running Wolf Armor and Storage alongside any of the following, please report it as an issue:
* [Phosphor](https://www.curseforge.com/minecraft/mc-mods/phosphor) by jellysquid_
* [Sophisticated Wolves](https://www.curseforge.com/minecraft/mc-mods/sophisticated-wolves) by NightKosh
* [Carry On](https://www.curseforge.com/minecraft/mc-mods/carry-on) by Tschipp and Purplicious_Cow_
    * Interaction is slightly buggy due to the pick up functionality; must use a non-empty hand
* [Quark](https://www.curseforge.com/minecraft/mc-mods/quark) by Vazkii, mcvinnyq, and wiresegal
    * Interaction is sligtly buggy due to petting, etc.
    * Render layers on foxhounds are specifically disabled

### Compatible mods

These mods are compatible (i.e. won't crash horribly), but are not explicitly supported.  I have marked known issues or incompatibilities below each:
* [Zoology](https://www.curseforge.com/minecraft/mc-mods/zoology) by RWTema
* [Atum 2: Return to the Sands](https://www.curseforge.com/minecraft/mc-mods/atum#c328) by Shadowclaimer and Girafi
    * Desert wolves remain unchanged
* [Better Animations Collection 2](https://www.curseforge.com/minecraft/mc-mods/better-animations-collection-2) by Fusz_
    * Wolf armor uses the vanilla model, and does not match up with the wolf when sitting or when the tail is waving.
* [Better Animal Models](https://www.curseforge.com/minecraft/mc-mods/better-animal-models) by cybercat5555, ist_meow, and Ozelot5836 and [Better Animals Plus](https://www.curseforge.com/minecraft/mc-mods/betteranimalsplus) by cybercat5555 and ist_meow
    * Uses the vanilla wolf render model, which does not match up with the new wolf model.  Disable the chest and armor render in the configuration options to remedy this.

### Incompatible Mods

The following mods are not compatible, and there are no plans to ensure compatibility at this time:
* [Stacy's Wolves](https://www.curseforge.com/minecraft/mc-mods/stacys-wolves/files) by stacyplays, nathan_oneday, Lyrael_Rayne, maddielines, SnowShock35, and AKTheKnight
* [Doggy Talents](https://www.curseforge.com/minecraft/mc-mods/doggy-talents) by percivalalb, SimonMeskens, and XNovaViperX

## Contact Me

I can be reached via the comments or through github / the email I have listed on my github profile!  I might also start a Discord for this mod if I can find some trustworthy moderators; keep an eye out if you're interested.

## Special Thanks

Thanks to everyone who has submitted an idea, issue, or a pull request!  This wouldn't be possible without your interest in the mod.

Additional thanks go to:
* [SanAndreasP](https://www.curseforge.com/members/sanandreasp) for fixing the incompatibility with Phosphor
* [NightKosh](https://www.curseforge.com/members/nightkosh) for their help with Sophisticated Wolves compatibility and the Russian translation files
* [Romz24](https://github.com/Romz24) for their help on the Russian translation files
* [gsbaoge](https://github.com/gsbaoge) for their help with the original Capability system
