package pt.iscte.poo.utils;

import pt.iscte.poo.AudioPlayer.AudioPlayer;
import pt.iscte.poo.gui.ImageTile;

// Abstract class for the JumperGuy and the Enemy
public abstract class Characters implements ImageTile{
    private Point2D position;

    // Stats
    private int hp;
    private int max_hp;
    private int strength;
    private int defense = 0;

    private AudioPlayer soundEffect = new AudioPlayer();

    public Characters(Point2D position,int max_hp, int strength) {
        this.position = position;
        this.max_hp = max_hp;
        this.hp = max_hp;
        this.strength = strength;
    }

    @Override
    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
        if (this.hp > max_hp) {
            this.hp = max_hp;
        }
    }

    public int getStrength() {
        return strength;
    }

    public void addStrength(int strength) {
        this.strength += strength;
    }

    public void removeStrength(int strength) {
        this.strength = strength;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getDefense() {
        return defense;
    }

    public int getMax_hp() {
        return max_hp;
    }

    public void setMax_hp(int max_hp) {
        this.max_hp = max_hp;
    }

    public void addHp(int hp) {
        this.hp += hp;
        if (this.hp > max_hp) {
            this.hp = max_hp;
        }
    }

    public AudioPlayer getSoundEffect() {
        return soundEffect;
    }

    public abstract void takeDamage(int damage);

    //Dano que ignora a defesa
    public abstract void takeTrueDamage(int damage);
}