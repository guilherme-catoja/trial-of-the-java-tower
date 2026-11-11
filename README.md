# Trial of the Java Tower

A small Java platform game developed for the Object-Oriented Programming
course (`Programação Orientada a Objetos`). Guide Jumper Guy through several
rooms, defeat enemies, collect items, and save the princess while trying to
achieve a high score.

## Features

- Seven levels loaded from the `rooms` directory
- Platforming, doors, stairs, traps, enemies, and projectiles
- Collectible coins, food, medkits, shields, and swords
- Background music and sound effects
- A top-10 high-score leaderboard saved in `saves/HighScores.txt`

## Requirements

- Java Development Kit (JDK) 17 or newer
- A desktop environment capable of running Java Swing

## Running the game

From the project root, compile the source files:

```powershell
New-Item -ItemType Directory -Force out
javac -d out (Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName })
```

Then start the game:

```powershell
java -cp out pt.iscte.poo.game.Main
```

Run the commands from the project root so the game can find its `images`,
`music`, `sound`, `rooms`, and `saves` directories.

## Controls

| Key | Action |
| --- | --- |
| Left arrow | Move left |
| Right arrow | Move right |
| Up arrow | Jump or use a door |
| Down arrow | Go down stairs |

## Project structure

```text
src/       Java source code
images/    Game sprites and backgrounds
rooms/     Level layouts
music/     Background music
sound/     Sound effects
saves/     High-score data
```

## Legal Notice

This game is an independent project and is not affiliated with Oracle.
