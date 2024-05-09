package ent.gunpickups;

import ent.*;
import trident.TridEntity;
import blib.util.*;
import custom.guns.*;
import java.awt.*;

public class RiflePickup extends GunPickup{
    
    public RiflePickup(Position pos){
        super(pos);
        gun = new Rifle();
    }
    public RiflePickup(){
        super("riflepickup");
    }

    public TridEntity construct(Position pos, Dimension collision, int[] data){
        return new RiflePickup(pos);
    }
}
