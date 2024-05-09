package custom.guns;

import java.util.Random;

import blib.util.*;
import custom.*;
import ent.Bullet;
import trident.Trident;

public class Shotgun extends Gun{
    
    public Shotgun(){
        super("Shotgun", 6, 5, 750, 1000, false, 20, 5);
    }

    public void shoot(double dir){
        if(ammo > 0){
            int numBullets = BTools.randInt(5, 8);
            for(int i = 0; i < numBullets; i++){
                Random rand = new Random();
                double inacc = (rand.nextInt(inaccuracy * 2) - inaccuracy) / 100.0;
                Trident.getEntities().add(new Bullet(Trident.getPlrPos().copy(), Math.toDegrees(dir + inacc), damage));
            }
            ammo--;
            shootSounds();
        }else{
            reload();
        }
    }

    public void reload(){
        if(ammoStorage <= 0) return;
        int numShells = maxAmmo - ammo;
        PlayerInfo.reloadTime = reloadTime * numShells;
        if(ammo > 0){
            ammoStorage += ammo;
            ammo = 0;
        }
        if(ammoStorage < maxAmmo){
            ammo += ammoStorage;
            ammoStorage = 0;
        }else{
            ammoStorage -= maxAmmo;
            ammo = maxAmmo;
        }
        reloadSounds();
    }
}
