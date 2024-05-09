package custom;

import java.util.ArrayList;

public class PlayerInfo { // Holds data about what gun(s) the player has
    
    public static boolean hasGun = false;
    public static Gun gun = null;
    public static long reloadTime = 0;
    public static long shootTime = 0;
    public static ArrayList<Gun> guns = new ArrayList<Gun>();
    public static int newSelGun = 0;
    public static long newSelTime = 0;


    public static void giveGun(Gun g){
        for(Gun gun: guns){
            if(g.name.equals(gun.name)){
                // Already has the gun, give ammo
                gun.ammoStorage += g.ammoStorage;
                return;
            }
        }
        hasGun = true;
        gun = g;
        guns.add(g);
        newSelGun = guns.size() - 1;
        newSelTime = 0;
        reloadTime = 0;
        shootTime = 1000;
    }

}
