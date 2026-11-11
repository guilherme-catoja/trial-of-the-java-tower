package pt.iscte.poo.objects;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Characters;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Interactible;
import pt.iscte.poo.utils.Point2D;

public class Projectile implements ImageTile, Interactible {
    private Point2D position;
    private int hp = 1;
    private final int damage = 3;

    public Projectile(Point2D position) {
        this.position = position;
    }

    @Override
    public String getName() {
        return "Projectile";
    }

    @Override
    public Point2D getPosition() {
        return position;
    }

    @Override
    public int getLayer() {
        return 3;
    }

    public void updatePosition() {
        position = position.plus(Direction.DOWN.asVector());
    }

    @Override
    public void interact(Characters character) {
        character.takeDamage(damage);
    }

    public int getHp() {
        return hp;
    }

    public void takeDamage() {
        hp = 0;
    }
}
