package update;

import trident.*;
import ent.*;
import blib.util.BTools;
import custom.*;
import ent.gunpickups.*;
import custom.guns.*;
import java.util.ArrayList;
public class Update {

    public static void setup(){
        // Add custom entities to the registry here. Required in order to load them properly
        Trident.addCustomEntity(new ExampleEntity()); // Use the empty constructor
        Trident.addCustomEntity(new GunPickup());
        Trident.addCustomEntity(new HUD());
        Trident.addCustomEntity(new Crate());
        Trident.addCustomEntity(new PistolPickup());
        Trident.addCustomEntity(new RevolverPickup());
        Trident.addCustomEntity(new RiflePickup());
        Trident.addCustomEntity(new ShotgunPickup());

        // Set settings
        Trident.setPlrSpeed(0.2);
        Trident.setShortCollision(true);

        // Start player off with empty hands
        PlayerInfo.giveGun(new Hands());

        // Post Processing
        Trident.setBloom(0.2);
        Trident.setExposure(1);
        Trident.enableBloom = false;
        Trident.enableExposure = false;
        Trident.setLightBlur(1);
    }

    public static void sceneStart(String scene){

    }
    
    public static void update(long elapsedTime){
        // Update timers
        if(PlayerInfo.reloadTime > 0){
            PlayerInfo.reloadTime -= elapsedTime;
        }
        if(PlayerInfo.shootTime > 0){
            PlayerInfo.shootTime -= elapsedTime;
        }
        if(PlayerInfo.newSelTime > 0){
            PlayerInfo.newSelTime -= elapsedTime;
        }
        
        // Automatic gun firing
        if(PlayerInfo.hasGun && PlayerInfo.gun.auto && Trident.getMouseDown(1) && PlayerInfo.reloadTime <= 0 && PlayerInfo.shootTime <= 0 && PlayerInfo.newSelTime <= 0){
            double dir = BTools.getAngle(Trident.getPlrPos(), Trident.mouseWorldPos);
            PlayerInfo.gun.shoot(dir);
            PlayerInfo.shootTime = PlayerInfo.gun.shootTime;
        }
    }

    public static void trigger(int id){
        
    }

    public static void tridentEvent(int id){
        if(id == Trident.EVENT_SCREENSHOT){
            System.out.println("Took a screenshot!");
        }
    }

    public static int command(ArrayList<String> cmdParts){ // cmdParts.get(0) is the command, while the rest are arguments for the command.
        switch(cmdParts.get(0)){
        case "helloWorld":
            System.out.println("Hello, World!");
            return 0;
        case "ping":
            System.out.println("pong");
            return 0;
        }
        return 1; // return 1 if command is not recognized
    }
}
