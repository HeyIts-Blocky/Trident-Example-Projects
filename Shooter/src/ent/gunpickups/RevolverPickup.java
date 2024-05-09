package ent.gunpickups;

import ent.*;
import trident.TridEntity;
import blib.util.*;
import custom.guns.*;
import java.awt.*;

public class RevolverPickup extends GunPickup{
    
    public RevolverPickup(Position pos){
        super(pos);
        gun = new Revolver();
    }
    public RevolverPickup(){
        super("revolverpickup");
    }

    public TridEntity construct(Position pos, Dimension collision, int[] data){
        return new RevolverPickup(pos);
    }
}
