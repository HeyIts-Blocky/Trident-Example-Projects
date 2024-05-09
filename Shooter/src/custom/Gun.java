package custom;

import blib.util.*;
import ent.Bullet;
import trident.Trident;
import java.util.Random;

public class Gun {

    public String name;
    public int maxAmmo, damage;
    public long reloadTime;
    public long shootTime;
    public boolean auto;
    public int inaccuracy;
    public int ammoStorage;
    public int ammo;
    
    public Gun(String n, int a, int d, long rt, long st, boolean au, int inacc, double stor){
        name = n;
        maxAmmo = a;
        damage = d;
        reloadTime = rt;
        shootTime = st;
        auto = au;
        inaccuracy = inacc;
        ammoStorage = (int)(maxAmmo * stor);
        ammo = maxAmmo;
    }

    public void shoot(double dir){
        if(ammo > 0){
            Random rand = new Random();
            double inacc = (rand.nextInt(inaccuracy * 2) - inaccuracy) / 100.0;
            Trident.getEntities().add(new Bullet(Trident.getPlrPos().copy(), Math.toDegrees(dir + inacc), damage));
            ammo--;
            shootSounds();
        }else{
            reload();
        }
    }

    public void reloadSounds(){
        Audio.play("data/sounds/gun sounds/testReload.wav");
    }

    public void shootSounds(){
        Audio.play("data/sounds/gun sounds/testShoot.wav");
    }

    public void reload(){
        if(ammoStorage <= 0) return;
        PlayerInfo.reloadTime = reloadTime;
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
