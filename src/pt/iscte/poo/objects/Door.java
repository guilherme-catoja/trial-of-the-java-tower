package pt.iscte.poo.objects;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public class Door implements ImageTile {
    Point2D position;
    private boolean open;

    public Door(Point2D position, boolean isOpen) {
        this.position = position;
        this.open = isOpen;
    }

    @Override
    public String getName() {
        if (open) {
            return "DoorOpen";
        }
        return "DoorClosed";
    }

    @Override
    public int getLayer() {
        return 1;
    }

    @Override
    public Point2D getPosition() {
        return position;
    }
}
