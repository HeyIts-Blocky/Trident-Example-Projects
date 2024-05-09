package ent;

import blib.util.*;
import trident.*;
import java.awt.*;
public class CanDamage extends TridEntity { // Any entity that should take damage has to extend this class

    public int hp = 100;
    public Dimension box = new Dimension(32, 32);

    // Constructor, runs when the entity is created
    public CanDamage(Position pos, int hp){
        super(pos);
        this.hp = hp;
    }
    // Registry constructor, used only for adding to the registry
    public CanDamage(){
        super("candamage", false, 1);
    }
    public CanDamage(String n, int numData){
        super(n, false, numData);
    }
    // Custom constructor, used by the engine when building a scene
    public TridEntity construct(Position pos, Dimension collision, int[] data){
        return new CanDamage(pos, data[0]);
    }

    public final void damage(int amount){
        hp -= amount;
        if(hp <= 0){
            Trident.destroy(this);
        }
    }

    public final Rectangle getHitbox(){
        return new Rectangle((int)(position.x - box.width / 2), (int)(position.y - box.height), box.width, box.height);
    }
}
