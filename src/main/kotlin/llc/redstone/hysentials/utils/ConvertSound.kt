package llc.redstone.hysentials.utils

enum class Sound(val displayName: String, val path: String) {
    AMBIENCE_CAVE("Ambience Cave", "ambient.cave.cave"),
    AMBIENCE_RAIN("Ambience Rain", "ambient.weather.rain"),
    AMBIENCE_THUNDER("Ambience Thunder", "ambient.weather.thunder"),
    ANVIL_BREAK("Anvil Break", "random.anvil_break"),
    ANVIL_LAND("Anvil Land", "random.anvil_land"),
    ANVIL_USE("Anvil Use", "random.anvil_use"),
    ARROW_HIT("Arrow Hit", "random.bowhit"),
    BURP("Burp", "random.burp"),
    CHEST_CLOSE("Chest Close", "random.chestclosed"),
    CHEST_OPEN("Chest Open", "random.chestopen"),
    CLICK("Click", "random.click"),
    DOOR_CLOSE("Door Close", "random.door_close"),
    DOOR_OPEN("Door Open", "random.door_open"),
    DRINK("Drink", "random.drink"),
    EAT("Eat", "random.eat"),
    EXPLODE("Explode", "random.explode"),
    FALL_BIG("Fall Big", "game.player.hurt.fall.big"),
    FALL_SMALL("Fall Small", "game.player.hurt.fall.small"),
    FIZZ("Fizz", "random.fizz"),
    FUSE("Fuse", "game.tnt.primed"),
    GLASS("Glass", "dig.glass"),
    HURT_FLESH("Hurt Flesh", "game.player.hurt"),
    ITEM_BREAK("Item Break", "random.break"),
    ITEM_PICKUP("Item Pickup", "random.pop"),
    LAVA_POP("Lava Pop", "liquid.lavapop"),
    LEVEL_UP("Level Up", "random.levelup"),
    NOTE_BASS("Note Bass", "note.bass"),
    NOTE_PIANO("Note Piano", "note.harp"),
    NOTE_BASS_DRUM("Note Bass Drum", "note.bd"),
    NOTE_STICKS("Note Sticks", "note.hat"),
    NOTE_BASS_GUITAR("Note Bass Guitar", "note.bassattack"),
    NOTE_SNARE_DRUM("Note Snare Drum", "note.snare"),
    NOTE_PLING("Note Pling", "note.pling"),
    ORB_PICKUP("Orb Pickup", "random.orb"),
    SHOOT_ARROW("Shoot Arrow", "random.bow"),
    SPLASH("Splash", "game.player.swim.splash"),
    SWIM("Swim", "game.player.swim"),
    WOOD_CLICK("Wood Click", "random.wood_click"),
    BAT_DEATH("Bat Death", "mob.bat.death"),
    BAT_HURT("Bat Hurt", "mob.bat.hurt"),
    BAT_IDLE("Bat Idle", "mob.bat.idle"),
    BAT_LOOP("Bat Loop", "mob.bat.loop"),
    BAT_TAKEOFF("Bat Takeoff", "mob.bat.takeoff"),
    BLAZE_BREATH("Blaze Breath", "mob.blaze.breathe"),
    BLAZE_DEATH("Blaze Death", "mob.blaze.death"),
    BLAZE_HIT("Blaze Hit", "mob.blaze.hit"),
    CAT_HISS("Cat Hiss", "mob.cat.hiss"),
    CAT_HIT("Cat Hit", "mob.cat.hitt"),
    CAT_MEOW("Cat Meow", "mob.cat.meow"),
    CAT_PURR("Cat Purr", "mob.cat.purr"),
    CAT_PURREOW("Cat Purreow", "mob.cat.purreow"),
    CHICKEN_IDLE("Chicken Idle", "mob.chicken.say"),
    CHICKEN_HURT("Chicken Hurt", "mob.chicken.hurt"),
    CHICKEN_EGG_POP("Chicken Egg Pop", "mob.chicken.plop"),
    CHICKEN_WALK("Chicken Walk", "mob.chicken.step"),
    COW_IDLE("Cow Idle", "mob.cow.say"),
    COW_HURT("Cow Hurt", "mob.cow.hurt"),
    COW_WALK("Cow Walk", "mob.cow.step"),
    CREEPER_HISS("Creeper Hiss", "mob.creeper.say"),
    CREEPER_DEATH("Creeper Death", "mob.creeper.death"),
    ENDERDRAGON_DEATH("Enderdragon Death", "mob.enderdragon.end"),
    ENDERDRAGON_GROWL("Enderdragon Growl", "mob.enderdragon.growl"),
    ENDERDRAGON_HIT("Enderdragon Hit", "mob.enderdragon.hit"),
    ENDERDRAGON_WINGS("Enderdragon Wings", "mob.enderdragon.wings"),
    ENDERMAN_DEATH("Enderman Death", "mob.endermen.death"),
    ENDERMAN_HIT("Enderman Hit", "mob.endermen.hit"),
    ENDERMAN_IDLE("Enderman Idle", "mob.endermen.idle"),
    ENDERMAN_TELEPORT("Enderman Teleport", "mob.endermen.portal"),
    ENDERMAN_SCREAM("Enderman Scream", "mob.endermen.scream"),
    ENDERMAN_STARE("Enderman Stare", "mob.endermen.stare"),
    GHAST_SCREAM("Ghast Scream", "mob.ghast.scream"),
    GHAST_SCREAM2("Ghast Scream2", "mob.ghast.affectionate_scream"),
    GHAST_CHARGE("Ghast Charge", "mob.ghast.charge"),
    GHAST_DEATH("Ghast Death", "mob.ghast.death"),
    GHAST_FIREBALL("Ghast Fireball", "mob.ghast.fireball"),
    GHAST_MOAN("Ghast Moan", "mob.ghast.moan"),
    GUARDIAN_HIT("Guardian Hit", "mob.guardian.hit"),
    GUARDIAN_IDLE("Guardian Idle", "mob.guardian.idle"),
    GUARDIAN_DEATH("Guardian Death", "mob.guardian.death"),
    GUARDIAN_ELDER_HIT("Guardian Elder Hit", "mob.guardian.elder.hit"),
    GUARDIAN_ELDER_IDLE("Guardian Elder Idle", "mob.guardian.elder.idle"),
    GUARDIAN_ELDER_DEATH("Guardian Elder Death", "mob.guardian.elder.death"),
    GUARDIAN_LAND_HIT("Guardian Land Hit", "mob.guardian.land.hit"),
    GUARDIAN_LAND_IDLE("Guardian Land Idle", "mob.guardian.land.idle"),
    GUARDIAN_LAND_DEATH("Guardian Land Death", "mob.guardian.land.death"),
    GUARDIAN_CURSE("Guardian Curse", "mob.guardian.curse"),
    GUARDIAN_ATTACK("Guardian Attack", "mob.guardian.attack"),
    GUARDIAN_FLOP("Guardian Flop", "mob.guardian.flop"),
    IRON_GOLEM_DEATH("Irongolem Death", "mob.irongolem.death"),
    IRON_GOLEM_HIT("Irongolem Hit", "mob.irongolem.hit"),
    IRON_GOLEM_THROW("Irongolem Throw", "mob.irongolem.throw"),
    IRON_GOLEM_WALK("Irongolem Walk", "mob.irongolem.walk"),
    MAGMACUBE_WALK("Magmacube Walk", "mob.magmacube.small"),
    MAGMACUBE_WALK2("Magmacube Walk2", "mob.magmacube.big"),
    MAGMACUBE_JUMP("Magmacube Jump", "mob.magmacube.jump"),
    PIG_IDLE("Pig Idle", "mob.pig.say"),
    PIG_DEATH("Pig Death", "mob.pig.death"),
    PIG_WALK("Pig Walk", "mob.pig.step"),
    RABBIT_AMBIENT("Rabbit Ambient", "mob.rabbit.idle"),
    RABBIT_DEATH("Rabbit Death", "mob.rabbit.death"),
    RABBIT_HURT("Rabbit Hurt", "mob.rabbit.hurt"),
    RABBIT_JUMP("Rabbit Jump", "mob.rabbit.hop"),
    SHEEP_IDLE("Sheep Idle", "mob.sheep.say"),
    SHEEP_SHEAR("Sheep Shear", "mob.sheep.shear"),
    SHEEP_WALK("Sheep Walk", "mob.sheep.step"),
    SILVERFISH_HIT("Silverfish Hit", "mob.silverfish.hit"),
    SILVERFISH_KILL("Silverfish Kill", "mob.silverfish.kill"),
    SILVERFISH_IDLE("Silverfish Idle", "mob.silverfish.say"),
    SILVERFISH_WALK("Silverfish Walk", "mob.silverfish.step"),
    SKELETON_IDLE("Skeleton Idle", "mob.skeleton.say"),
    SKELETON_DEATH("Skeleton Death", "mob.skeleton.death"),
    SKELETON_HURT("Skeleton Hurt", "mob.skeleton.hurt"),
    SKELETON_WALK("Skeleton Walk", "mob.skeleton.step"),
    SLIME_ATTACK("Slime Attack", "mob.slime.attack"),
    SLIME_WALK("Slime Walk", "mob.slime.small"),
    SLIME_WALK2("Slime Walk2", "mob.slime.big"),
    SPIDER_IDLE("Spider Idle", "mob.spider.say"),
    SPIDER_DEATH("Spider Death", "mob.spider.death"),
    SPIDER_WALK("Spider Walk", "mob.spider.step"),
    WITHER_DEATH("Wither Death", "mob.wither.death"),
    WITHER_HURT("Wither Hurt", "mob.wither.hurt"),
    WITHER_IDLE("Wither Idle", "mob.wither.idle"),
    WITHER_SHOOT("Wither Shoot", "mob.wither.shoot"),
    WITHER_SPAWN("Wither Spawn", "mob.wither.spawn"),
    WOLF_BARK("Wolf Bark", "mob.wolf.bark"),
    WOLF_DEATH("Wolf Death", "mob.wolf.death"),
    WOLF_GROWL("Wolf Growl", "mob.wolf.growl"),
    WOLF_HOWL("Wolf Howl", "mob.wolf.howl"),
    WOLF_HURT("Wolf Hurt", "mob.wolf.hurt"),
    WOLF_PANT("Wolf Pant", "mob.wolf.panting"),
    WOLF_SHAKE("Wolf Shake", "mob.wolf.shake"),
    WOLF_WALK("Wolf Walk", "mob.wolf.step"),
    WOLF_WHINE("Wolf Whine", "mob.wolf.whine"),
    ZOMBIE_METAL("Zombie Metal", "mob.zombie.metal"),
    ZOMBIE_WOOD("Zombie Wood", "mob.zombie.wood"),
    ZOMBIE_WOODBREAK("Zombie Woodbreak", "mob.zombie.woodbreak"),
    ZOMBIE_IDLE("Zombie Idle", "mob.zombie.say"),
    ZOMBIE_DEATH("Zombie Death", "mob.zombie.death"),
    ZOMBIE_HURT("Zombie Hurt", "mob.zombie.hurt"),
    ZOMBIE_INFECT("Zombie Infect", "mob.zombie.infect"),
    ZOMBIE_UNFECT("Zombie Unfect", "mob.zombie.unfect"),
    ZOMBIE_REMEDY("Zombie Remedy", "mob.zombie.remedy"),
    ZOMBIE_WALK("Zombie Walk", "mob.zombie.step"),
    ZOMBIE_PIG_IDLE("Zombie Pig Idle", "mob.zombiepig.zpig"),
    ZOMBIE_PIG_ANGRY("Zombie Pig Angry", "mob.zombiepig.zpigangry"),
    ZOMBIE_PIG_DEATH("Zombie Pig Death", "mob.zombiepig.zpigdeath"),
    ZOMBIE_PIG_HURT("Zombie Pig Hurt", "mob.zombiepig.zpighurt"),
    FIREWORK_BLAST("Firework Blast", "fireworks.blast"),
    FIREWORK_BLAST2("Firework Blast2", "fireworks.blast_far"),
    FIREWORK_LARGE_BLAST("Firework Large Blast", "fireworks.largeBlast"),
    FIREWORK_LARGE_BLAST2("Firework Large Blast2", "fireworks.largeBlast_far"),
    FIREWORK_TWINKLE("Firework Twinkle", "fireworks.twinkle"),
    FIREWORK_TWINKLE2("Firework Twinkle2", "fireworks.twinkle_far"),
    FIREWORK_LAUNCH("Firework Launch", "fireworks.launch"),
    SUCCESSFUL_HIT("Successful Hit", "random.successful_hit"),
    HORSE_ANGRY("Horse Angry", "mob.horse.angry"),
    HORSE_ARMOR("Horse Armor", "mob.horse.armor"),
    HORSE_BREATHE("Horse Breathe", "mob.horse.breathe"),
    HORSE_DEATH("Horse Death", "mob.horse.death"),
    HORSE_GALLOP("Horse Gallop", "mob.horse.gallop"),
    HORSE_HIT("Horse Hit", "mob.horse.hit"),
    HORSE_IDLE("Horse Idle", "mob.horse.idle"),
    HORSE_JUMP("Horse Jump", "mob.horse.jump"),
    HORSE_LAND("Horse Land", "mob.horse.land"),
    HORSE_SADDLE("Horse Saddle", "mob.horse.leather"),
    HORSE_SOFT("Horse Soft", "mob.horse.soft"),
    HORSE_WOOD("Horse Wood", "mob.horse.wood"),
    DONKEY_ANGRY("Donkey Angry", "mob.horse.donkey.angry"),
    DONKEY_DEATH("Donkey Death", "mob.horse.donkey.death"),
    DONKEY_HIT("Donkey Hit", "mob.horse.donkey.hit"),
    DONKEY_IDLE("Donkey Idle", "mob.horse.donkey.idle"),
    HORSE_SKELETON_DEATH("Horse Skeleton Death", "mob.horse.skeleton.death"),
    HORSE_SKELETON_HIT("Horse Skeleton Hit", "mob.horse.skeleton.hit"),
    HORSE_SKELETON_IDLE("Horse Skeleton Idle", "mob.horse.skeleton.idle"),
    HORSE_ZOMBIE_DEATH("Horse Zombie Death", "mob.horse.zombie.death"),
    HORSE_ZOMBIE_HIT("Horse Zombie Hit", "mob.horse.zombie.hit"),
    HORSE_ZOMBIE_IDLE("Horse Zombie Idle", "mob.horse.zombie.idle"),
    VILLAGER_DEATH("Villager Death", "mob.villager.death"),
    VILLAGER_HAGGLE("Villager Haggle", "mob.villager.haggle"),
    VILLAGER_HIT("Villager Hit", "mob.villager.hit"),
    VILLAGER_IDLE("Villager Idle", "mob.villager.idle"),
    VILLAGER_NO("Villager No", "mob.villager.no"),
    VILLAGER_YES("Villager Yes", "mob.villager.yes");


    fun getSoundPath(): String {
        return path
    }

    companion object {
        fun fromName(name: String): Sound {
            return values().find { it.name == name } ?: AMBIENCE_CAVE
        }
    }
}