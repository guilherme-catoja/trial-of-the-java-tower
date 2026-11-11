package pt.iscte.poo.Items;

import pt.iscte.poo.objects.JumperGuy;
import pt.iscte.poo.utils.Characters;
import pt.iscte.poo.utils.Items;
import pt.iscte.poo.utils.Point2D;

public class Coin extends Items {
    private final int score_up = 100;

    public Coin(Point2D position) { super(position); }

    @Override
    public String getName() {
        return "Coin";
    }

    @Override
    public void interact(Characters character) {
        if (character.getClass().getSimpleName().equals("JumperGuy")){
            JumperGuy jumperGuy = (JumperGuy) character;
            jumperGuy.addScore(score_up);

            try{
                jumperGuy.getSoundEffect().load("sound/Coin.wav");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            super.interact(character);
        }
    }
}
