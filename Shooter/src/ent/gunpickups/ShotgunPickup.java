package ent.gunpickups;

import ent.*;
import trident.TridEntity;
import blib.util.*;
import custom.guns.*;
import java.awt.*;

public class ShotgunPickup extends GunPickup{
    
    public ShotgunPickup(Position pos){
        super(pos);
        gun = new Shotgun();
    }
    public ShotgunPickup(){
        super("shotgunpickup");
    }

    public TridEntity construct(Position pos, Dimension collision, int[] data){
        return new ShotgunPickup(pos);
    }
}
