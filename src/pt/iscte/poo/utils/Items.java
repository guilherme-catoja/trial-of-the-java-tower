package pt.iscte.poo.utils;

import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.objects.JumperGuy;

public abstract class Items implements ImageTile, Interactible {
    private Point2D position;
    private int hp = 1;

    public Items(Point2D position) {
        this.position = position;
    }

    @Override
    public String getName() {
        return "Item";
    }

    @Override
    public Point2D getPosition() {
        return position;
    }

    @Override
    public int getLayer() {
        return 2;
    }

    @Override
    public void interact(Characters character) {
        if (character.getClass().getSimpleName().equals("JumperGuy")){
            JumperGuy jumperGuy = (JumperGuy) character;

            jumperGuy.getSoundEffect().play();

            ImageGUI.getInstance().removeImage(this);
            hp = 0;
        }
    }

    @Override
    public int getHp() {
        return hp;
    }
}
