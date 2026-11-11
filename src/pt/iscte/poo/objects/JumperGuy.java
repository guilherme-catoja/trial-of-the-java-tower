package pt.iscte.poo.objects;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.*;

import java.util.ArrayList;
import java.util.List;

public class JumperGuy extends Characters {
    // Movimento
    private Point2D position;
    private Direction facingDirection = Direction.RIGHT; // Atualizado para Direction
    private Vector2D velocity;
    private final int jump_force = 1;
    private boolean momentum = false; // If false, player is falling straight down; if true, the player is falling diagonally and can skip 1 tile jumps
    private boolean down = false;

    //Tiles no nível
    private List<ImageTile> walls = new ArrayList<>();
    private List<Enemy> enemies = new ArrayList<>();
    private List<Projectile> projectiles = new ArrayList<>();
    private List<ImageTile> items = new ArrayList<>();

    private int score = 0;
    private boolean win = false;

    public JumperGuy(Point2D position) {
        super(position, 10, 1);
        this.velocity = Direction.MIDDLE.asVector();
    }

    @Override
    public String getName() {
        if (facingDirection == Direction.RIGHT && velocity.getX() == 0 && velocity.getY() == 0) {
            return "JumperGuyRight";
        } else if (facingDirection == Direction.LEFT && velocity.getX() == 0 && velocity.getY() == 0) {
            return "JumperGuyLeft";
        } else if (facingDirection == Direction.RIGHT) {
            return "JumperGuyWalkRight";
        }
        return "JumperGuyWalkLeft";

    }

    @Override
    public int getLayer() {
        return 2;
    }

    @Override
    public Point2D getPosition() {
        return position;
    }

    public ImageTile checkSides(Vector2D direction) {
        Point2D positionSide = position.plus(direction);

        for (ImageTile tile : walls) {
            if (tile.getPosition().equals(positionSide) && (tile.getLayer() == 1 || tile.getLayer() == 2)) {
                return tile;
            }
        }
        for (ImageTile tile : walls) {
            if (tile.getPosition().equals(positionSide) && (tile.getLayer() == 0)) {
                return tile;
            }
        }
        return null;
    }

    public void setTiles(List<ImageTile> walls) {
        this.walls = walls;
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }

    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    public void setProjectiles(List<Projectile> projectiles) {
        this.projectiles = projectiles;
    }

    public void setItems(List<ImageTile> items) {
        this.items = items;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public int getScore() {
        return score;
    }

    public void setWin(){
        win = true;
        velocity = Direction.MIDDLE.asVector();
    }

    public boolean getWin(){
        return win;
    }

    public void takeDamage(int damage) {
        setHp(getHp() - damage + getDefense());
        if (this.getName().equals("JumperGuyLeft") || this.getName().equals("JumperGuyRight")) {
            try{
                getSoundEffect().load("sound/JumperGuyHit.wav");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            getSoundEffect().play();
        }
    }

    public void takeTrueDamage(int damage) {
        setHp(getHp() - damage);
        if (this.getName().equals("JumperGuyLeft") || this.getName().equals("JumperGuyRight")) {
            try{
                getSoundEffect().load("sound/JumperGuyHit.wav");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            getSoundEffect().play();
        }
    }

    // Atualização da posição baseada em velocidade e colisões
    public void move() {
        fall();

        damageEnemies();

        //If the player tries to leave the map there is no tiles, so when checking it throws a NullPointerException
        try {
            handleCollisions();
        }
        catch (NullPointerException e) {
            velocity = new Vector2D(0, velocity.getY());
        }

        interact();

        if (velocity.getY() < 0 && !checkSides(Direction.MIDDLE.asVector()).getName().equals("Stairs")) {
            try{
                getSoundEffect().load("sound/Jump.wav");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            getSoundEffect().play();
        }

        position = position.plus(velocity);
        resetVelocityIfOnSurface();

        checkProjectileHit();

        down = false;
    }

    private void damageEnemies(){
        for(Enemy e : enemies){
            if (e.getPosition().equals(position.plus(velocity))) {
                e.takeDamage(getStrength());
                velocity = Direction.MIDDLE.asVector();
                momentum = false;
            }
            else {
                e.resetSprite();
            }
        }
    }

    private void checkProjectileHit(){
        for(Projectile projectile : projectiles){
            if (projectile.getPosition().equals(position)) {
                projectile.takeDamage();
                projectile.interact(this);
            }
        }
    }

    private void interact(){
        for (ImageTile tile : items) {
            if (tile.getPosition().equals(position.plus(velocity))) {
                ((Interactible) tile).interact(this);
            }
        }
    }

    private void handleCollisions() {
        if (isSurfaceBelow()) {
            handleSurfaceCollisions();
        } else {
            handleAirborneMovement();
        }
    }

    private void handleSurfaceCollisions() {
        if ((velocity.equals(Direction.LEFT.asVector()) && isWallOnLeft()) ||
                (velocity.equals(Direction.LEFT_UP.asVector()) && isWallAboveLeft()) ||
                (velocity.equals(Direction.RIGHT.asVector()) && isWallOnRight()) ||
                (velocity.equals(Direction.RIGHT_UP.asVector()) && isWallAboveRight())) {
            velocity = new Vector2D(0, velocity.getY());
        }
    }

    private void handleAirborneMovement() {
        if (!momentum) {
            velocity = Direction.DOWN.asVector();
        } else {
            if ((isWallBelowLeftAndMovingLeft() || isWallBelowRightAndMovingRight())) {
                velocity = new Vector2D(velocity.getX(), 0);
                momentum = false;
            } else if (velocity.equals(Direction.LEFT_DOWN.asVector()) && isWallOnLeft()) {
                velocity = new Vector2D(0, velocity.getY());
            } else if (velocity.equals(Direction.RIGHT_DOWN.asVector()) && isWallOnRight()) {
                velocity = new Vector2D(0, velocity.getY());
            }
        }
    }

    private void resetVelocityIfOnSurface() {
        if (isSurfaceBelow()) {
            velocity = Direction.MIDDLE.asVector();
            momentum = false;
        }
    }

    private boolean isSurfaceBelow() {
        return checkSides(Direction.DOWN.asVector()).getName().equals("Wall") || checkSides(Direction.DOWN.asVector()).getName().equals("Stairs") || checkSides(Direction.DOWN.asVector()).getName().equals("Trap");
    }

    private boolean isWallOnLeft() {
        return checkSides(Direction.LEFT.asVector()).getName().equals("Wall") || checkSides(Direction.LEFT.asVector()).getName().equals("Trap") ;
    }

    private boolean isWallAboveLeft() {
        return checkSides(Direction.LEFT_UP.asVector()).getName().equals("Wall") || checkSides(Direction.LEFT_UP.asVector()).getName().equals("Trap");
    }

    private boolean isWallOnRight() {
        return checkSides(Direction.RIGHT.asVector()).getName().equals("Wall") || checkSides(Direction.RIGHT.asVector()).getName().equals("Trap");
    }

    private boolean isWallAboveRight() {
        return checkSides(Direction.RIGHT_UP.asVector()).getName().equals("Wall") || checkSides(Direction.RIGHT_UP.asVector()).getName().equals("Trap");
    }

    private boolean isWallAbove() {
        return checkSides(Direction.UP.asVector()).getName().equals("Wall") || checkSides(Direction.UP.asVector()).getName().equals("Trap");
    }

    private boolean isWallBelowLeftAndMovingLeft() {
        return (checkSides(Direction.LEFT_DOWN.asVector()).getName().equals("Wall") || checkSides(Direction.LEFT_DOWN.asVector()).getName().equals("Trap")) && velocity.equals(Direction.LEFT_DOWN.asVector()) ;
    }

    private boolean isWallBelowRightAndMovingRight() {
        return (checkSides(Direction.RIGHT_DOWN.asVector()).getName().equals("Wall") || checkSides(Direction.RIGHT_DOWN.asVector()).getName().equals("Trap")) && velocity.equals(Direction.RIGHT_DOWN.asVector());
    }

    public void jump() {
        if (canJump()) {
            velocity = new Vector2D(velocity.getX(), -jump_force);
            momentum = true;
            for(Projectile projectile : projectiles){
                if (projectile.getPosition().equals(position.plus(Direction.UP.asVector()))) {
                    projectile.takeDamage();
                    projectile.interact(this);
                }
            }
        }
    }

    private boolean canJump() {
        if (position.getY() > 0){
            return (isSurfaceBelow() && !isWallAbove() && position.getY() > 0);
        }
        return false;
    }

    public void moveLeft() {
        if (isSurfaceBelow() && !down) {
            velocity = new Vector2D(-1, velocity.getY());
            facingDirection = Direction.LEFT;
        }
    }

    public void moveRight() {
        if (isSurfaceBelow() && !down) {
            velocity = new Vector2D(1, velocity.getY());
            facingDirection = Direction.RIGHT;
        }
    }

    public void moveDown() {
        if (checkSides(Direction.DOWN.asVector()).getName().equals("Stairs")) {
            velocity = Direction.DOWN.asVector();
            down = true;
        }
    }

    public void fall() {
        ImageTile positionBelow = checkSides(Direction.DOWN.asVector());
        if (positionBelow.getName().equals("Trap")){
            if (positionBelow instanceof Interactible) {
                Interactible interactible = (Interactible) positionBelow;
                interactible.interact(this);
            }
        }
        if (positionBelow.getName().equals("Stairs") && down) {
            down = false;
            return;
        }
        //Sets Y velocity to 0 only if he's falling, otherwise he wouldn't jump
        else if (isSurfaceBelow() && velocity.getY() > 0) {
            velocity = new Vector2D(velocity.getX(), 0);
            return;
        } else if (isSurfaceBelow()) {
            return;
        }
        velocity = new Vector2D(velocity.getX(), 1);
    }

    public boolean checkClosedDoor() {
        ImageTile current_tile = checkSides(Direction.MIDDLE.asVector());
        if (current_tile.getPosition().equals(position) && current_tile.getName().equals("DoorClosed")) {
            return true;
        }
        return false;
    }

    public boolean checkOpenDoor() {
        ImageTile current_tile = checkSides(Direction.MIDDLE.asVector());
        if (current_tile.getPosition().equals(position) && current_tile.getName().equals("DoorOpen")) {
            return true;
        }
        return false;
    }
}