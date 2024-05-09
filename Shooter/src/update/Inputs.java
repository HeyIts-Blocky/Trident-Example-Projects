package update;

import java.awt.*;
import java.awt.event.*;
import blib.util.*;
import custom.*;
import trident.*;
public class Inputs {
    
    public static void keyPressed(int key){
        // reload gun
        if(PlayerInfo.hasGun && key == KeyEvent.VK_R && PlayerInfo.gun.ammo < PlayerInfo.gun.maxAmmo){
            PlayerInfo.gun.reload();
        }
    }

    public static void mousePressed(int mb, Point mousePos, Position worldPos){
        if(mb == 1){ // mb1 = left click
            if(PlayerInfo.newSelTime > 0){
                // select new gun
                PlayerInfo.gun = PlayerInfo.guns.get(PlayerInfo.newSelGun);
                PlayerInfo.newSelTime = 0;
                PlayerInfo.shootTime = 1000;
                PlayerInfo.reloadTime = 0;
            }else if(PlayerInfo.hasGun && PlayerInfo.reloadTime <= 0 && PlayerInfo.shootTime <= 0){
                // shoot gun
                double dir = BTools.getAngle(Trident.getPlrPos(), worldPos);
                PlayerInfo.gun.shoot(dir);
                PlayerInfo.shootTime = PlayerInfo.gun.shootTime;
            }
        }
        if(mb == 3){ // mb3 = right click
            // stop selecting new gun
            PlayerInfo.newSelTime = 0;
        }
    }

    public static void onScroll(int scroll){
        // start selecting new gun
        PlayerInfo.newSelGun += scroll;
        if(PlayerInfo.newSelGun < 0) PlayerInfo.newSelGun = PlayerInfo.guns.size() - 1;
        if(PlayerInfo.newSelGun > PlayerInfo.guns.size() - 1) PlayerInfo.newSelGun = 0;
        PlayerInfo.newSelTime = 10000;
    }
}
