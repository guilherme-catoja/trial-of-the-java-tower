package pt.iscte.poo.levels;

import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.objects.Projectile;
import pt.iscte.poo.objects.Enemy;
import pt.iscte.poo.objects.JumperGuy;
import pt.iscte.poo.utils.Interactible;
import pt.iscte.poo.utils.Items;

import java.util.ArrayList;
import java.util.List;

public class LevelManager {
    private int level_number = 0;
    private List<Level> levels;

    private List<ImageTile> current_level_layout = new ArrayList<>();

    private List<Enemy> enemies = new ArrayList<>();
    private List<Projectile> projectiles = new ArrayList<>();
    private List<ImageTile> items = new ArrayList<>();

    public LevelManager() {
        //Saves all level objects in a list
        levels = readLevels();
    }

    public List<Level> readLevels(){
        List<Level> levels = new ArrayList<>();
        // i is the number of levels
        for (int i = 0; i < 7; i++) {
            levels.add(new Level(i));
        }
        return levels;
    }

    public void loadLevel(JumperGuy jumperGuy) {
        List<ImageTile> tiles = levels.get(level_number).getLevel_tiles();

        //Add jumperguy
        jumperGuy.setPosition(levels.get(level_number).getJumperGuyPosition());
        tiles.add(jumperGuy);

        //Add Enemies
        enemies = levels.get(level_number).getEnemies();

        //Clear Projectiles
        projectiles.clear();

        //Add items
        items = levels.get(level_number).getItems();

        for(Enemy e : enemies){
            if (!tiles.contains(e)){
                tiles.add(e);
            }
        }
        for(ImageTile item : items){
            if (!tiles.contains(item)){
                tiles.add(item);
            }
        }

        ImageGUI gui = ImageGUI.getInstance();
        gui.clearImages(); // Clear existing images
        gui.addImages(tiles);// Add new images

        tiles.remove(jumperGuy); // Remove the player from the tiles list
        jumperGuy.setTiles(tiles); // Update the player's tiles for the new level

        current_level_layout = tiles;
    }

    public void loadNextLevel(JumperGuy jumperGuy) {
        //Save current level layout
        levels.get(level_number).setLevel_tiles(current_level_layout);
        levels.get(level_number).setEnemies(enemies);
        levels.get(level_number).setItems(items);
        levels.get(level_number).setLevel_tiles(current_level_layout);

        // Increment the level number
        level_number++;

        loadLevel(jumperGuy);
    }

    public void loadPreviousLevel(JumperGuy jumperGuy) {
        //Save current level layout
        levels.get(level_number).setLevel_tiles(current_level_layout);
        levels.get(level_number).setEnemies(enemies);
        levels.get(level_number).setItems(items);

        // Decreases the level number
        level_number--;

        loadLevel(jumperGuy);
    }

    public int getLevel_number() {
        return level_number;
    }

    public List<Level> getLevels() {
        return levels;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    public List<ImageTile> getItems() {
        return items;
    }

    public void setCurrentLevelLayout(List<ImageTile> current_level_layout) {
        this.current_level_layout = current_level_layout;
    }

    public void moveEnemies(JumperGuy jumperGuy, List<ImageTile> current_level_layout) {
        List<Enemy> toRemove = new ArrayList<>();
        for (Enemy e : enemies) {
            //Queues the Enemy to be removed
            if (!e.hasHp()) {
                jumperGuy.addScore(50);
                toRemove.add(e);
                ImageGUI.getInstance().removeImage(e);
            }
            //Moves the Enemy
            else {
                e.move(level_number, jumperGuy.getPosition(), current_level_layout, projectiles, enemies);
            }
        }
        //Removes all queued Enemy
        enemies.removeAll(toRemove);
        current_level_layout.removeAll(toRemove);
    }

    public void moveProjectiles(){
        List<Projectile> toRemove = new ArrayList<>();
        for (Projectile projectile : getProjectiles()) {
            projectile.updatePosition();
            if (projectile.getPosition().getY() > 9) { // Check if projectile is out of bounds
                toRemove.add(projectile);
            }
            if (projectile.getHp() <= 0){
                toRemove.add(projectile);
                ImageGUI.getInstance().removeImage(projectile);
            }
        }
        projectiles.removeAll(toRemove);
    }

    public void removeItems(){
        List<ImageTile> toRemove = new ArrayList<>();
        for (ImageTile item : getItems()) {
            if (item instanceof Items itemsItem && itemsItem.getHp() == 0){
                toRemove.add(item);
            }
        }
        items.removeAll(toRemove);
        current_level_layout.removeAll(toRemove);
    }

    public List<ImageTile> getLevelLayout() {
        return current_level_layout;
    }
}
