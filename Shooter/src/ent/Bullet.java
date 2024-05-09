package ent;

import blib.util.*;
import trident.*;
import javax.swing.*;
import java.awt.*;
public class Bullet extends TridEntity {

    public static double bulletSpeed = 0.5;

    Position direction = new Position();

    int damage = 0;

    // Constructor, runs when the entity is created
    public Bullet(Position pos, double dir, int dmg){
        super(pos);
        double rad = Math.toRadians(dir);
        direction = BTools.angleToVector(rad);
        damage = dmg; 
    }
    // Registry constructor, used only for adding to the registry
    public Bullet(){
        super("bullet", false, 2);
    }
    // Custom constructor, used by the engine when building a scene
    public TridEntity construct(Position pos, Dimension collision, int[] data){
        return new Bullet(pos, (int)data[0], data[1]);
    }

    // Render while in game
    public void render(Graphics g, JPanel panel, int x, int y){
        g.setColor(Color.yellow);
        g.fillOval(x - 4, y - 4, 8, 8);
    }

    // Render while in engine. Tip: call the normal render first to still render it normally, then overlay something on top
    public void engineRender(Graphics g, JPanel panel, int x, int y){
        super.engineRender(g, panel, x, y);
    }

    // Runs every tick while the game is running
    public void update(long elapsedTime){
        // Move bullet
        position.x += direction.x * bulletSpeed * elapsedTime;
        position.y += direction.y * bulletSpeed * elapsedTime;

        // Check collision to delete bullet
        for(Rectangle r: Trident.getCollision()){
            if(r.contains(new Point((int)position.x, (int)position.y))){
                Trident.destroy(this);
                return;
            }
        }

        // Check for entities to damage
        for(int i = 0; i < Trident.getEntities().size(); i++){
            TridEntity e = Trident.getEntities().get(i);
            if(e instanceof CanDamage){
                CanDamage obj = (CanDamage)e;
                if(obj.getHitbox().contains(new Point((int)position.x, (int)position.y))){
                    obj.damage(damage);
                    Trident.destroy(this);
                    return;
                }
            }
        }
    }
}
