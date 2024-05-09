package ent;

import blib.util.*;
import custom.*;
import trident.*;
import javax.swing.*;
import java.awt.*;
public class GunPickup extends TridEntity { // Empty gun pickup, not really supposed to be used, but instead extended off of

    public Gun gun = new Gun("Test Gun", 30, 5, 1000, 150, true, 5, 5);

    // Constructor, runs when the entity is created
    public GunPickup(Position pos){
        super(pos);
    }
    public GunPickup(Position pos, Gun g){
        super(pos);
        gun = g;
    }
    // Registry constructor, used only for adding to the registry
    public GunPickup(){
        super("gunpickup", false, 0);
    }
    public GunPickup(String n){
        super(n, false, 0);
    }
    // Custom constructor, used by the engine when building a scene
    public TridEntity construct(Position pos, Dimension collision, int[] data){
        return new GunPickup(pos);
    }

    // Render while in game
    public void render(Graphics g, JPanel panel, int x, int y){
        g.setColor(Color.gray);
        g.fillRect(x - 8, y - 8, 16, 16);
    }

    // Runs every tick while the game is running
    public final void update(long elapsedTime){
        if(BTools.getDistance(position, Trident.getPlrPos()) < 32){
            PlayerInfo.giveGun(gun);
            Trident.destroy(this);
        }
    }
}
