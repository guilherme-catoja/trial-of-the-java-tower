package pt.iscte.poo.objects;

import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Characters;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

import java.util.List;
import java.util.Random;

public class Enemy extends Characters implements ImageTile {
    private boolean damaged = false;

    public Enemy(Point2D position) {
        super(position, 10, 3);
    }

    @Override
    public String getName() {
        if (damaged){
            return "EnemyDamaged";
        }
        return "Enemy";
    }

    @Override
    public int getLayer() {
        return 2;
    }

    @Override
    public void takeDamage(int damage){
        if (!damaged){
            damaged = true;
            setHp(getHp()-damage);
            try{
                getSoundEffect().load("sound/EnemyHit.wav");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            getSoundEffect().play();
        }
        else{
            damaged = false;
        }
    }

    @Override
    public void takeTrueDamage(int damage){
        //Não usado para o Enemy
    }

    public boolean hasHp(){
        return getHp() > 0;
    }

    public void resetSprite(){
        damaged = false;
    }

    public void move(int level_number, Point2D jumperguy_position, List<ImageTile> current_level, List<Projectile> projectiles, List<Enemy> enemies){
        Random random = new Random();
        int direction = random.nextInt(4); // direction == 0; 1; 2 ou 3
        if (fall(current_level, jumperguy_position, enemies)){
            // return;
        }
        //Levels 0 and 1
        else if (level_number < 2){
            if (direction == 0 && getPosition().getX() > 0) {
                moveLeft(current_level, jumperguy_position, enemies);
            }
            else if (direction == 1) {
                moveRight(current_level, jumperguy_position, enemies);
            }
            else if (direction == 2) {
                spawnProjectile(projectiles);
            }
            //if direction == 3 it does nothing
        }

        //Levels 2 and 3
        else if (level_number >= 2 && level_number < 4){
            if (direction == 0 && getPosition().getX() > 0) {
                moveLeft(current_level, jumperguy_position, enemies);
            }
            else if (direction == 1 && getPosition().getX() < 9) {
                moveRight(current_level, jumperguy_position, enemies);
            }
            else {
                spawnProjectile(projectiles);
            }
        }

        //Levels 4, 5 and 6
        else{
            if (direction < 2) {
                if (getPosition().getX() > jumperguy_position.getX()) {
                    moveLeft(current_level, jumperguy_position, enemies);
                }
                else if (getPosition().getX() < jumperguy_position.getX()) {
                    moveRight(current_level, jumperguy_position, enemies);
                }
            }
            else {
                spawnProjectile(projectiles);
            }
        }
    }

    //Handle left movement

    public void moveLeft(List<ImageTile> current_level, Point2D jumperguy_position, List<Enemy> enemies){
        if (getPosition().getX() > 0 && !isSolidOnLeft(current_level, jumperguy_position, enemies)) {
            setPosition(new Point2D(getPosition().getX()-1, getPosition().getY()));
        }
    }

    private boolean isSolidOnLeft(List<ImageTile> current_level, Point2D jumperguy_position, List<Enemy> enemies) {
        if (checkSides(Direction.LEFT, current_level, jumperguy_position, enemies)){
            return true;
        }
        return false;
    }

    //Handle right movement

    public void moveRight(List<ImageTile> current_level, Point2D jumperguy_position, List<Enemy> enemies){
        if (getPosition().getX() < 9 && !isSolidOnRight(current_level, jumperguy_position, enemies)) {
            setPosition(new Point2D(getPosition().getX()+1, getPosition().getY()));
        }
    }

    private boolean isSolidOnRight(List<ImageTile> current_level, Point2D jumperguy_position, List<Enemy> enemies) {
        if (checkSides(Direction.RIGHT, current_level, jumperguy_position, enemies)){
            return true;
        }
        return false;
    }


// Verificar o tile necessário
    public boolean checkSides(Direction direction, List<ImageTile> current_level, Point2D jumperguy_position, List<Enemy> enemies) {
        Point2D positionSide = getPosition().plus(direction.asVector());

        for (ImageTile tile : current_level) {
            if (tile.getPosition().equals(positionSide) && (tile.getName().equals("Wall") || tile.getName().equals("Trap"))) {
                return true;
            }
        }

        for (Enemy e : enemies) {
            if (e.getPosition().equals(positionSide)) {
                return true;
            }
        }

        if(positionSide.equals(jumperguy_position)){
            return true;
        }

        return false;
    }

    public boolean fall(List<ImageTile> current_level, Point2D jumperguy_position, List<Enemy> enemies) {
        if (isSurfaceBelow(current_level, jumperguy_position, enemies)) {
            return false;
        }
        setPosition(new Point2D(getPosition().getX(), getPosition().getY() + 1));
        return true;
    }

    private boolean isSurfaceBelow(List<ImageTile> current_level, Point2D jumperguy_position, List<Enemy> enemies) {
        return checkSides(Direction.DOWN, current_level, jumperguy_position, enemies);
    }

    public void spawnProjectile(List<Projectile> projectiles) {
        Projectile newProjectile = new Projectile(this.getPosition());
        projectiles.add(newProjectile);
        ImageGUI.getInstance().addImage(newProjectile); // Add the projectile to the GUI
    }
}
