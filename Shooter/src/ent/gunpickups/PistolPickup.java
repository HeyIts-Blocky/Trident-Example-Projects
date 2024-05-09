package ent.gunpickups;

import ent.*;
import trident.TridEntity;
import blib.util.*;
import custom.guns.*;
import java.awt.*;

public class PistolPickup extends GunPickup{ 
    
    public PistolPickup(Position pos){
        super(pos);
        gun = new Pistol();
    }
    public PistolPickup(){
        super("pistolpickup");
    }

    public TridEntity construct(Position pos, Dimension collision, int[] data){
        return new PistolPickup(pos);
    }
}
