package ent;

import blib.util.*;
import trident.*;
import java.awt.*;

import javax.swing.JPanel;
public class Crate extends CanDamage { // Simple box that takes damage

    
    public Crate(Position pos){
        super(pos, 20);
    }
    
    public Crate(){
        super("crate", 0);
    }

    public void render(Graphics g, JPanel panel, int x, int y){
        g.setColor(Color.orange);
        g.fillRect(x - 16, y - 32, 32, 32);
    }
    
    public TridEntity construct(Position pos, Dimension collision, int[] data){
        return new Crate(pos);
    }
}
