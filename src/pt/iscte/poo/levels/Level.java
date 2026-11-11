package pt.iscte.poo.levels;

import pt.iscte.poo.Items.*;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.objects.*;
import pt.iscte.poo.utils.Point2D;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Level {
    private int level_number;
    private List<ImageTile> level_tiles = new ArrayList<>();
    private Point2D jumperGuyPosition = new Point2D(0, 0);

    private List<Enemy> enemies = new ArrayList<>();
    private List<ImageTile> items = new ArrayList<>();

    public Level(int level_number) {
        this.level_number = level_number;
        read_level();
    }

    public List<ImageTile> getLevel_tiles() {
        return level_tiles;
    }

    public void setLevel_tiles(List<ImageTile> level_tiles) {
        this.level_tiles = level_tiles;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    public List<ImageTile> getItems() {
        return items;
    }

    public void setItems(List<ImageTile> items) {
        this.items = items;
    }

    public Point2D getJumperGuyPosition() {
        return jumperGuyPosition;
    }

    public void setJumperGuyPosition(Point2D jumperGuyPosition) {
        this.jumperGuyPosition = jumperGuyPosition;
    }

    public void read_level (){
        try {
            Scanner fileScanner = new Scanner(new File("rooms/room" + level_number + ".txt"));

            String line = fileScanner.nextLine(); // Salta a primeira linha
            int y = 0;

            while ((fileScanner.hasNextLine())) {
                line = fileScanner.nextLine();
                for (int x = 0; x < 10; x++) {
                    Point2D position = new Point2D(x, y);
                    char tileChar = line.charAt(x);

                    if (tileChar == 'W') {
                        level_tiles.add(new Wall(position));
                    } else if (tileChar == 'S') {
                        level_tiles.add(new Stairs(position));
                    } else if (tileChar == 'J' || tileChar == 'H') {
                        jumperGuyPosition = position;
                        level_tiles.add(new Door(position, true));
                    } else if (tileChar == 'P') {
                        items.add(new Princess(position));
                        level_tiles.add(new Background(position));
                    } else if (tileChar == 't') {
                        level_tiles.add(new Trap(position));
                    } else if (tileChar == 's') {
                        items.add(new Sword(position));
                        level_tiles.add(new Background(position));
                    } else if (tileChar == 'g') {
                        items.add(new GoodMeat(position));
                        level_tiles.add(new Background(position));
                    }else if (tileChar == 'm') {
                        items.add(new MedKit(position));
                        level_tiles.add(new Background(position));
                    } else if (tileChar == 'c') {
                        items.add(new Coin(position));
                        level_tiles.add(new Background(position));
                    } else if (tileChar == 'd') {
                        items.add(new Shield(position));
                        level_tiles.add(new Background(position));
                    } else if (tileChar == '0') {
                        level_tiles.add(new Door(position, false));
                    } else if (tileChar == 'G') {
                        enemies.add(new Enemy(position));
                        level_tiles.add(new Background(position));
                    } else {
                        level_tiles.add(new Background(position));
                    }
                }
                y++;
            }
            fileScanner.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Ficheiro de nível não encontrado");
        }
    }
}
