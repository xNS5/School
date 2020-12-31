# Game Programming

This was a group programming assignment done in Unity. I developed a bit of a knack for creating AI, so I volunteered to
develop the AI for our project.


I created 5 AI scripts:

1. Goblin
2. Skeleton
3. Golem
4. Hellhound
5. Boss: Minotaur

Each monster had a different attack style, applying different amounts of damage based on the monster's stats.

Each monster has a weapon, with the Hellhound being the exception. The Weapon Controller detects a collision with the player's
game object and subtracts health from the player's health script.

# Labyrinthia

Designed by:
Nathan Becker, Michael Kennedy, Aedan Smith
_“Escape and become a hero, or die and be forgotten.”_


### Introduction

Labyrinthia is a third-person action game centered around the titular Greek Labyrinth.
Having angered the gods, the player, a Greek demigod, is thrown deep into the
labyrinth to prove their worth. Their only chance at survival is to battle their way through
a maze that changes every time they enter and defeat the Minotaur at the end. Their
way will be blocked by classic Greek monsters such as cyclops, dracanae, harpies, and
more. As they progress through the maze, players will be able to find powerups in the
form of godly blessings--proof that they have earned the approval of one of the gods. Be
careful though, the Greek gods don’t always get along, and choosing one’s blessing
may cost you another’s. Good reactions and smart use of customization will be the key
to surviving the hell known as the labyrinth!

### Game Description

Genre​:
● Action
● Adventure
● Strategy
Game Elements:
● Sword/Shield, Axe, spear, and Crossbow combat
● Dodging
● Exploring
Game Content:
● Thriller
● Action
Theme:
● Myth
● Medieval
● Fantasy
Game Sequence:
● Linear Central Story
● Side Objectives
Players:
● Single-player
Player Immersion:
● Customization
● Exploration
● Challenging Combat


### Game Play

When the player first opens the game, they will see giant stone gates slowly creaking
open, leading them to the maze. Distant echoes will sound--a dripping from stalagmites,
an unknown wind blowing through stone corridors--creating the sense of a winding cave
with no end in sight. The menu will appear as the background appears to keep moving,
always walking down a corridor but never reaching the end. The home screen features
a view of the character, which can be cycled through to see the other options. Difficulty,
sound, and video options are arranged on one side of the screen, and a weathered
notebook at the bottom represents the index, where players can read about the different
character options as well as the weapons and powerups available in the game. A
“Tutorial” box can be checked or unchecked which will allow the player to learn the
controls in a short beginner level before entering the full version of the labyrinth.
Once a difficulty and character have been selected, the game plays a short intro to the
story in the form of text on a black screen. This will vary slightly depending on the
character selected, but will explain that the player has offended the gods and has been
set a test for redemption: survive the labyrinth and defeat the minotaur. With this intro
out of the way, the scene will open on the player in a cavernous corridor. Braziers
provide the light every so often, though they are spread enough to leave shadows.
If the tutorial has been selected, UI text will explain the basic controls before a bronze
version of the player appears. Text explains that this is an automaton--one of the
several possible monsters that reside in the labyrinth. It attacks and hits the player.
More text explains that while your stamina, used to run and dodge, will regenerate, your
health only refills at shrines to the gods The player is then told to defeat the automaton,
which will be a simple task, entailing an attack or two along with a scripted dodge (which
uses the space bar). Movement is done with WASD, sprint can be done while holding
shift + movement. and attacks are done with the RMB. It will drop a drachma which the
player is directed to use at the shrine on the other side of the room (using F to interact).
This heals them before they use the stairs to the first level. The player comes upon a
clue, an inscription on the wall. They inspect it (using F to interact) and they are
introduced to their journal which saves the inscription for later.
Now in a dark corridor, the player begins their exploration of the labyrinth. As they follow
the twists and turns, they encounter different monsters such as the cyclopes, who have
incredible strength and health, but are relatively slow. If the player crouches, they can
sneak past some enemies, or attack from the shadows for bonus damage. Defeating
monsters will drop various items such as drachma used as offerings in order to heal or
receive blessings or potions that temporarily boost the player’s stats.
Each level will feature four shrines the player can visit before progressing to the next
level--though they can also just go to the next level directly if they find the exit before


the shrines. The shrines can be used to heal if the player has a drachma, or an offering
can be made to secure the blessing of the particular god that shrine. If the player’s
godly parent owns that shrine, they are offered a free heal instead. Some shrines can
only be used if the player has not already activated others. For example, Zeus,
Poseidon, and Hades are very competitive, so you can only choose to appeal to one of
them.
As the player explores the maze, they’ll have to find clues relating to Greek mythology
in order to pass to the next level. Upon finding a clue, a new entry in the player’s journal
will be added with the words of that clue. Once the player finds the way to the next level
they have to answer a riddle associated with the clues they found in the part of the
maze. Upon successfully answering the riddle, they can progress onward and healing
automatically as they do. There are three different mazes, and the final one leads to the
boss room, which is a large open area in which the Minotaur resides. Defeating him will
allow the player to take the final stairs out of the labyrinth, beating the game. If they die
anywhere along the way, the game will display a game over message, indicating that
the player failed to redeem themselves, and has died unknown and unremembered. If
they win, they will instead receive a blurb about their stats as well as a story of their
character’s success. The game’s challenge is its main draw, along with the potential to
try out the different powers that each demigod brings.

### Key Features

```
● Single player.
● 1-2 hours per character run.
● 3D graphics.
● Three levels plus final boss room.
● Twelve possible characters with innate blessings.
● Twelve shrines dedicated to specific gods or goddesses.
● Six types of enemies: Cyclops, Dracanae, Skeleton Warriors, Harpies,
Hellhounds, and the Minotaur.
● Audio:
○ Background noises will consist of wind, water droplets, maybe an
occasional “mysterious sound”.
○ Player noises:
■ Breathing.
■ Echoing footsteps as the player walks.
■ Faster echoing footsteps as the player runs.
■ Misc other player sounds: using special attacks, regaining health,
stamina, picking up items, taking damage.
○ Monster Noises:
■ Idle sounds
■ Taking damage
■ Attacking
```

## Design Document

### Design Guidelines

This game is a combination of combat and exploration. The key to succeeding at
combat is learning each monster’s attack patterns and working around them. The player
will need to understand their enemies, their weapon, and their passive powerups to
succeed. This is driven by enemies that carry different weapons or have movement
types such as the slithering dracanae or the flying harpies. The player can find the
different weapons and shrines to acquire new passive skills throughout the maze. Dying
even once sends the player into the depths of tartarus with an ignoble death. Reaching
the end of the labyrinth and slaying the Minotaur means victory and becoming
immortalized as a true hero.

### Game Design Definitions

```
● Menu: ​The player will have access to a menu where they can adjust aspects of
the game such as brightness and sound level, save their current progress, and
exit the game.
● Inventory: ​The player’s inventory will contain consumables in the form of
stackable potions and drachmas. It also holds the different weapons. Once the
player picks up an item, they’ll be allowed to activate/switch to it using keys 1-5,
or quick switch with Q/E.
● Player Controls: ​The player will move with WASD and can act on deity shrines
with F. Q/E switch between weapons and the number keys will use any special
abilities or potions the player has assigned to that slot. Shift will make the player
sprint while C will crouch. Space will make the player dodge or jump. Players can
open the menu with M and their inventory with TAB. RMB causes the player to
attack while LMB will make the player block.
● Game Play: ​ Central play will consist of exploring the labyrinth and defeating the
enemies that the player comes across.
● Progressing: ​ The player can move to the next level when they gain access to
the staircase leading there, which is hidden somewhere in the level.
● Game Over (win/lose) ​: In order to win, the player has to pass all 3 levels, defeat
enemies and the Minotaur, all without dying. If the player dies, game over.
```

### Player Elements

```
● Model ​:
○ Idle free-form camera
○ Running (sprinting looks similar but the animation plays faster)
○ Jumping
```

**○** Sneaking
**○** Dodging/rolling
**● Weapons** ​:
○ Sword+Shield
○ Crossbow + arrows
○ Axe
○ Spear
**● Gold Drachmas
● Potions
● Clues
● Journal**


### Player Definition

```
● Default (Status) ​: The player by default starts out with stats associated with their
godly parent.
● Actions ​: The player can by default walk, run, fight, and use special abilities
relating to their godly parent.
● Information (Status) ​: The player can see the walls of the labyrinth, clues, and
monsters as they approach. They will carry a map that fills in as they explore.
● Default Properties ​: The player begins the game at the entrance to the labyrinth,
with stats unaugmented/augmented depending on their godly parent of choice.
● Winning ​: The player wins by defeating the Minotaur.
● Losing ​: The only way the player can lose is to die before escaping.
```
### Player Properties & Rewards

```
● Health ​: decreases as the player takes damage and can refill at shrines.
● Stamina ​: decreases as the player runs or dodges. Refills over time.
● Drachma ​: Found at key points of the labyrinth. These are used as offerings to
buy blessings or heal at the gods’ shrines.
● Potions ​: Item drops from monsters that have a variety of effects.
○ Ambrosia: Heals the player some.
○ Nectar: Refills the player’s stamina bar.
○ Potion of Strength: Makes the player deal more damage for a time.
○ Potion of Stealth: Makes the player harder to detect temporarily.
● Godly Blessings ​: blessings from the gods. Acquired from shrines via drachmas
or from choosing a specific godly parent at the beginning of the game. Some give
passives (P) while some give Special Attacks (SA). Some gods dislike each
other, making it so that those blessings cannot be equipped at the same time.
○ Zeus (Masterbolt - SA): The player’s next attack will fire a lightning bolt.
○ Poseidon (Earthquake - SA): Stagger nearby enemies.
○ Hades (Undead - P): Can revive at the nearest shrine once.
○ Ares (Fervor - SA): Gain increased attack speed for a short period.
○ Apollo (Master Archer - P): Archery based weapons do more damage.
○ Artemis (Huntress - P): Sneak attacks do more damage and the player is
stealthier.
○ Athena (Aegis - SA): Blocks the next incoming attack and stuns the
attacker.
```

```
○ Hera (Family - P): Slightly reduces incoming damage and allows the
player to equip blessings regardless of conflicts.
○ Aphrodite (Charm - P): Monsters will not attack the player until attacked.
○ Hermes (Traveler - P): Increased player movement speed.
○ Demeter (Fertile - P): Player regenerates health slowly over time.
○ Hephaestus (Godly Forging - SA): Increases weapon damage for a short
time.
```
### User Interface (UI)

Below: A cut of the help menu from in-game. It offers a few tips about the game on the
left and shows the controls on the right.

### Heads Up Display (HUD)

Main Menu Layout:


In-game HUD: (Of note: other information may be available, such as enemy health,
depending on current position and state)
In-game Menu Layout:

### Player View

The player can control the camera with the mouse movement. Moving the mouse in a
certain direction will cause the player to look in that direction. Player movement will
synchronize with the camera direction so that W always moves in the direction players


are looking. The player has a light above them that is their only vision in the dark of the
labyrinth. On a larger resolution screen, the bars will obviously not overlap.

### Antagonistic Elements

```
● Enemies
○ Goblin ​: The simplest enemy with no special attacks or abilities. Low
health and low damage.
```

○ **Skeleton** ​: A simple enemy that has the ability to reform itself once. Low
health, but it must be defeated twice. Medium damage.
○ **Hellhound** ​: A fast, difficult to hit enemy that dashes past the player. High
damage, but relatively low health.


○ **Golem** ​: A big, slow enemy that deals immense damage if it makes contact
with the player. Has high health.
○ **Minotaur** ​: The boss character, which deals heavy damage. After a 25%
decrease in health, the Minotaur’s attack style switches from
hand-to-hand, to a sword and axe combo.
**● Traps** ​(These are currently unimplemented, so we have no pictures yet)
○ **Pit Fall:** ​ A trap where the player falls into a pit and takes damage.
○ **Hidden Spears** ​: A trap where spears fall from the ceiling after the player
walks on a pressure plate.


### Antagonistic Definitions

Arrow Trap​: Deals damage to the player if they are standing in a certain area after
walking on a pressure plate. A blurred animation will play from the trap wall to the next
wall hit.
Pit Trap​: This will consist of a short fall that will damage players. Current vision is a
spiked floor, but due to game design, some sort of lava floor type implementation will
more likely be used.
Monster​: These are the enemies that will patrol the labyrinth. They will vary in
appearance, difficulty, and attack pattern, but will generally move between four
waypoints and attack the player if approached. The player can attempt to sneak past
them (or attack from stealth for bonus damage) using the shadows nearby.

### Antagonistic Properties

```
● Enemies:
○ Health
○ Movement (patrol/chase)
○ Dodgeable attacks
○ Damage
● Traps:
○ Damage in a restricted area
○ Indicators
```
### Artificial Intelligence (AI)

Currently we have 4 out
of the 5 AI created and
working:
● Skeleton
● Goblin
● Hellhound
● Golem
● Minotaur
Originally, the monsters
included a Harpy,
Dracanae, and Cyclops
model however the only
available models of
these monsters required
payment. Instead, a


goblin and golem model were chosen in lieu of the harpy and cyclops. There wasn’t a
suitable model to replace the Dracanae, so 5 models should be fine for now. Each
monster’s attack sequence is different, however the general AI behavior is the same
across all monsters:
At the moment the AI continues attacking indefinitely. The attack sequences can include
additional animations and behaviors, at this point they just have basic patrolling and
attack sequences. More behaviors will be added once the combat system is
implemented. The AI script itself only requires minor tweaking, the more challenging
aspect is locating or creating suitable animations for the model.
The Minotaur’s script will have to be different because in the final boss level it will have
to constantly get the player’s location in order to charge them. The attack sequences
will depend on the Minotaur’s health at a given point. For every 25% decrease in health,
the minotaur’s attack pattern will change.

### The Story

The story will have specifics for each demigod in the form of a paragraph or two of
flavor text, but the general story will remain the same regardless of which character the
player chooses. The gods are displeased with the player for a transgression and have
cast them into the labyrinth to prove themselves as a hero or die. If the player dies and
loses, a short blurb will play about how the player died in an unknown location and has
been lost to history. If they win, they will instead receive a short passage about how
they redeemed themselves and will live on in immortality.

### Level Design

This game will feature three levels, each containing monsters of different types with
different attack styles. The architect Daedalus enchanted life into the maze, so should a
player enter more than once, they will experience a different maze and have monsters
generated in different areas. As he had pity on heroes in his lifetime, he allows the
player to change the difficulty of their journey through his maze. If the player decides to
do the tutorial, it is done in a simple room with a single bronze automaton enemy, a
shrine, and a staircase.
The first level will contain easy monsters like harpies and skeletons. The harpies and
skeletons will have pretty low stats, allowing the player to become accustomed to
playing for real. The harpies will have no special abilities, but the skeletons will have the
ability to reform once or twice, adding an extra level of difficulty to the fight.
The second level will contain a combination of the previous harpies and skeletons while
adding dracanae and cyclopes. The cyclopes on level two will be armed with large axes


and deal heavy blows, but will be slower than the other opponents -- allowing the player
to dodge and counterattack. The dracanae will have quick, slithery movement and two
swords, but are relatively weak and can be blocked.
The third level will introduce hellhounds. The hellhounds are player-sized dogs of
Hades, with very fast movement. They can charge at the player and knock them over
for a short period of time. Dodging their charge is key.
Level three grants access to the boss room with the final monster: the Minotaur. The
minotaur will have some special attacks, but will simply fight with his hands and horns.
Once the player defeats the Minotaur, they will be allowed to exit the Labyrinth.
Each level will have four shrines to various Greek gods. These grant the player stat
boosts, abilities, or allow them to heal back to full health. In certain cases (Easy difficulty
or with the Hades blessing) these will also serve as respawn points.
Example of Levels 1-3:
Color Coordinated to show notable features:


With Monsters spawned in Scene View:
Boss Room Layout:


**Key:**
● **Light blue** ​: the locations of the altars that exist in each level.
● **Yellow** ​: the entrance/spawn point.
● **Red** ​: the exit.
● **Purple** ​: example enemy locations
The altars are designed to be in the same place each time--making it so that the player
will have to explore each corner of the maze to acquire each blessing. The entrance can
be in four different locations (central corners) while the exit is placed in one of 8
locations along the edge based on the entrance location. This maze is randomly
generated each time with the above constraints, as well as the requirement that only so
many tiles can be left unfilled. Each of the three main levels will consist of these
randomly generated mazes.
The maze is composed of a maximum of 100 tiles. These have different amounts of
walls to allow the randomly generated level to form different routes each time. Breaking
it down further, each of these tiles will have variations of their own. For example, a
horizontal hallway might have a pit trap across it, an arrow trap if the player steps in
certain locations, a semi randomly selected monster patrolling it (four waypoints in a
square), or consist of nothing more than a simple hallway. The number of monsters and
their locations will vary, but there will be a guarantee that there will be no more or less
than a set amount of monsters.

### Audio & Sound F/X

```
● Death Sound Effects
○ Goblin
■ The goblin emits a low-pitched grunt sound upon death.
○ Skeleton
■ When the skeleton is “killed”, a “bones falling on the ground” sound
effect plays
○ Hellhound
■ Sound of a wolf howling
● Music
○ A dark sounding music file has been added to Audio Sources attached to
the Maze Outline that plays on wake, and loops.
```

### Game Architecture

### How To Play Copy

Upon starting your copy of Labyrinthia, you’ll see the main menu. It allows you to scroll
through the character options, change the difficulty, and toggle the tutorial. If this is your
first time playing, we recommend setting the difficulty to Easy and toggling the Tutorial
button. In Easy, you’ll have the ability to respawn, so you’ll have no worries about
learning the game!
Once you’re into the game, try fiddling around with the controls. W, A, S, D are your
basic movement commands and you can press Space to jump. You can left click your
mouse to attack, or right click to block. If you press C you will duck down and
sneak--doing this can help you get past monsters without a fight! If you do get into a
fight, make sure you press Z to dodge out of the way of enemy attacks!


Now that you’ve got yourself familiar with the basics, let’s get moving. Most of the
hallways will be open and easy to navigate. However, there are two types of traps
you’ve got to look out for. The first is the pit trap. Don’t fall in or else you’ll take
significant damage! The other is the cave in trap--if you see blocks starting to fall on
you, make sure you get out of the way.
If you see a monster, you’ve got a choice on your hands. You can try to sneak past it
and be on your way, or you can fight it and try to claim whatever loot it may have.
Monsters will drop potions or drachma, which are necessary to buy a god’s blessing. If
you decide to fight, be careful. Once your health runs out, that’s it--it’s back to the start
for you.
What if you take damage, but survive? One option is to use a potion, but if you don’t
have one of those, you’ll have to find a shrine. There are four shrines per level, each
dedicated to a different god or goddess. When you find one, you can pay a drachma for
the blessing of that god, or to heal. Be careful which you choose, because you’ll only
find so many drachma each game. Some blessings grant your character passive
abilities, such as the ability to sneak better, while others allow you to use a special
ability. If you’ve been blessed by Zeus, you can make use of his Masterbolt to blast an
enemy to ashes!
Healed up and blessed, it’s time to find your way to the next level. Just explore the
maze until you find the stairs up and climb them to continue. There are three levels to
climb, before you find the object of your quest, the mighty minotaur. Once you’ve
explored those levels, gained as many blessings and potions as you think you’ll need,
you’ll put your life on the line to fight the minotaur. Win, and you escape the labyrinth,
becoming a legend. Lose, and it’s back to the beginning.

## Good luck, hero. You’ll need it.


## Technical Document

**Code Structure
● Player Scripts**


**● Monster Scripts**


**● Level Generation**


**● UI Scripts**


### Concerns and Alternatives

```
● AI Scripts
○ The AI Scripts for the non-boss creatures, while they are the same conceptually
as the Minotaur, rely on the OnTriggerEnter(), OnTriggerExit(), and raycasting to
detect the player and attack them whereas the Minotaur on the other hand relies
on coroutines and a constant fix on the player’s location at any given time. The
other monsters are more prone to bugs and the states/animations not lining up.
The easiest fix for all of the monsters is to use coroutines as opposed to the
current implementation. Using coroutines for the various states/animations would
allow the monster to wait until after the animation is complete in order to
transition to the next state/animation.
○ The Minotaur, even though it’s more complex, still has a few bugs:
■ After attacking, the model floats backward a few spaces and continues to
act out the animation. This possibly is because one of the Coroutines is
switching off and on the NavMeshAgent’s .isStopped() method. Further
investigation and testing will be required to polish the combat animations.
● The Monster combat scripts rely on the OnTriggerEnter/Exit functions to damage the
player. The downside of doing it this way is that if the weapon sometimes pass back
through the player as the AI resets to its initial position, resulting in doing 2x the damage
per hit. A fix was attempted by turning off the collider OnTriggerExit(), but results have
been mixed.
```

