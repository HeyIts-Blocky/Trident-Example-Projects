package trident;

import blib.game.*;
import blib.util.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import update.*;
import java.io.*;
import trident.ent.*;
import com.jhlabs.image.GlowFilter;
import com.jhlabs.image.ExposureFilter;
public class Trident {

    // Debug Settings
    public static boolean drawPos = false;
    public static boolean drawCollision = false;
    public static boolean noclip = false;
    public static boolean engineDraw = false;
    public static Color debugColor = Color.red;
    public static boolean intro = true;
    public static ImageIcon splash = null;
    public static boolean drawFrames = false;
    public static boolean consoleEnabled = true;
    public static boolean fullbright = false;
    
    // Public Variables
    public static Point mousePos;
    public static Point mouseDelta;
    public static boolean drawPlayer = true;
    public static Position mouseWorldPos = new Position();
    public static boolean enableBloom = true, enableExposure = true;


    // Trident Variables
    protected static Player player;
    protected static Scene currentScene;
    protected static boolean fullscreen = false;
    protected static ArrayList<Scene> loadedScenes = new ArrayList<Scene>();
    protected static ArrayList<TridEntity> entRegistry = new ArrayList<TridEntity>();
    protected static String defaultScene = "default";
    protected static boolean m1 = false, m2 = false, m3 = false, m4 = false, m5 = false;
    protected static boolean[] keys = new boolean[255];
    protected static CamShake camShake;
    protected static ArrayList<Entity> lights = new ArrayList<Entity>();
    protected static LightManager lightManager = new LightManager(255);
    protected static GlowFilter bloom = new GlowFilter();
    protected static ExposureFilter exposure = new ExposureFilter();
    protected static boolean reset = false;
    protected static String newSprite = null;

    // Trident Events
    public static final int EVENT_SCREENSHOT = 0;


    // Setting methods
    public static void setPlrSpeed(double speed){
        player.speed = speed;
    }
    public static void setPlrPos(Position pos){
        player.goToPos(pos);
    }
    public static void setShortCollision(boolean b){
        player.shortCollision = b;
    }
    public static void setWindowTitle(String title){
        Main.window.setTitle(title);
    }
    public static void setupScenes(){
        try{
            loadedScenes = new ArrayList<Scene>();
            File dir = new File("data/scenes");
            File[] files = dir.listFiles();
            ArrayList<File> sceneFiles = new ArrayList<File>();
            for(File f: files){
                if(BTools.hasExtension(f, "bson")) sceneFiles.add(f);
            }

            for(File f: sceneFiles){
                loadedScenes.add(new Scene(f));
            }
        }catch(Exception e){
            System.out.println("Error setting up scenes.");
            e.printStackTrace();
        }
        
    }
    public static void loadScene(String name){
        lights = new ArrayList<Entity>();
        for(Scene s: loadedScenes){
            if(s.name.equals(name)){
                currentScene = s;
                player.goToPos(s.plrStart);
                player.setDirection(s.plrDir);
                for(TridEntity e: s.entities){
                    if(e instanceof TridLight){
                        TridLight asLight = (TridLight)e;
                        lights.add(new Light(asLight.position, asLight.radius));
                    }
                    e.sceneStart(s.name);
                }
                lightManager.defaultLight = s.defaultLight;
                Update.sceneStart(name);
                return;
            }
        }
        System.out.println("***********************************************************************************");
        System.out.println("Error loading scene: No scene with name '" + name + "' found.");
        System.out.println("***********************************************************************************");
    }
    public static void addCustomEntity(TridEntity e){ // Add a cutsom entity to the registry
        entRegistry.add(e);
    }
    public static void spawnEntity(TridEntity e){
        currentScene.entities.add(e);
    }
    public static void setDefaultScene(String s){
        defaultScene = s;
    }
    public static void destroy(TridEntity object){
        getEntities().remove(object);
    }
    public static void shakeCam(double intensity){
        camShake.addTrauma(intensity);
    }
    public static void removeShake(){
        camShake.trauma = 0;
    }
    public static void setShakeStrength(int str){
        camShake.strength = str;
    }
    public static void setShakeLoss(double loss){
        camShake.traumaLoss = loss;
    }
    public static void setBloom(double amount){
        bloom.setAmount((float)amount);
    }
    public static void setExposure(double exp){
        exposure.setExposure((float)exp);
    }
    public static void setLightBlur(int level){
        lightManager.blur.setIterations(level);
    }
    public static void addLight(Light l){
        lights.add(l);
    }
    public static void resetKeys(){
        reset = true;
    }
    public static void setPlrSprite(String path){
        newSprite = path;
    }
    public static void removeLight(Light l){
        lights.remove(l);
    }
    public static void setDefaultLight(int level){
        lightManager.defaultLight = level;
    }

    // Getting methods
    public static double getPlrSpeed(){
        return player.speed;
    }
    public static Position getPlrPos(){
        return player.getPos().copy();
    }
    public static Scene getCurrentScene(){
        return currentScene;
    }
    public static boolean getFullscreen(){
        return fullscreen; 
    }
    public static ArrayList<Entity> tridArrToEntArr(ArrayList<TridEntity> entities){
        ArrayList<Entity> newEntities = new ArrayList<Entity>();
        for(TridEntity e: entities){
            newEntities.add((Entity)e);
        }
        return newEntities;
    }
    public static ArrayList<TridEntity> entArrToTridArr(ArrayList<Entity> entities){
        ArrayList<TridEntity> newEntities = new ArrayList<TridEntity>();
        for(Entity e: entities){
            newEntities.add((TridEntity)e);
        }
        return newEntities;
    }
    public static ArrayList<TridEntity> getEntities(){
        return currentScene.entities;
    }
    public static ArrayList<Rectangle> getCollision(){
        return currentScene.getCollision();
    }
    public static boolean getMouseDown(int mb){
        if(mb == 1){
            return m1;
        }
        if(mb == 2){
            return m2;
        }
        if(mb == 3){
            return m3;
        }
        if(mb == 4){
            return m4;
        }
        if(mb == 5){
            return m5;
        }
        return false;
    }
    public static boolean getKeyDown(int key){
        return keys[key];
    }
    public static Player getPlr(){
        return player;
    }
    public static int getFrameWidth(){
        return 684;
    }
    public static int getFrameHeight(){
        return 462;
    }

}
