package pt.iscte.poo.objects;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public class Background implements ImageTile {
    Point2D position;

    public Background(Point2D position) {
        this.position = position;
    }

    @Override
    public String getName() {
        return "Background";
    }

    @Override
    public int getLayer() {
        return 0;
    }

    @Override
    public Point2D getPosition() {
        return position;
    }
}
