package pt.iscte.poo.Items;

import pt.iscte.poo.objects.JumperGuy;
import pt.iscte.poo.utils.Characters;
import pt.iscte.poo.utils.Items;
import pt.iscte.poo.utils.Point2D;

public class Sword  extends Items {
    private final int power_up = 2;
    private final int score_up = 10;

    public Sword(Point2D position) {
        super(position);
    }

    @Override
    public String getName() {
        return "Sword";
    }

    @Override
    public void interact(Characters character) {
        character.addStrength(power_up);
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
