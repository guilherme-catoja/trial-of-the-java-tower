package pt.iscte.poo.objects;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Characters;
import pt.iscte.poo.utils.Interactible;
import pt.iscte.poo.utils.Point2D;

public class Trap implements ImageTile, Interactible {
    private Point2D position;
    private final int damage = 2;

    public Trap(Point2D position) {
        this.position = position;
    }

    @Override
    public String getName() {
        return "Trap";
    }

    @Override
    public int getLayer() {
        return 1;
    }

    @Override
    public Point2D getPosition() {
        return position;
    }

    @Override
    public void interact(Characters character) {
        character.takeTrueDamage(damage);
    }

    @Override
    public int getHp() {
        return 1;
    }
}
