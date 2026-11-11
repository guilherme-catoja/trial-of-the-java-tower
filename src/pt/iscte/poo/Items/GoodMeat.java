package pt.iscte.poo.Items;

import pt.iscte.poo.objects.JumperGuy;
import pt.iscte.poo.utils.Characters;
import pt.iscte.poo.utils.Items;
import pt.iscte.poo.utils.Point2D;

public class GoodMeat extends Items {
    private final int power_up = 3;
    private final int score_up = 20;

    public GoodMeat(Point2D position) {
        super(position);
    }

    @Override
    public String getName() {
        return "GoodMeat";
    }

    @Override
    public void interact(Characters character) {
        character.addHp(power_up);
        JumperGuy jumperGuy = (JumperGuy) character;
        jumperGuy.addScore(score_up);

        try{
            jumperGuy.getSoundEffect().load("sound/PickUp.wav");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        super.interact(character);
    }
}