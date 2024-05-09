package trident;

import javax.swing.*;
import java.awt.*;
import blib.game.*;
import blib.util.*;

import java.awt.event.*;
import java.io.File;
import java.io.PrintWriter;

import blib.input.*;
import java.util.ArrayList;
import java.util.Scanner;

import blib.anim.*;

import trident.ent.*;
import update.*;
public class MainPanel extends JPanel {

    protected FrameManager frameManager = new FrameManager();
    public static Server server;
    protected KeyManager km = new InputListener(this);
    private Animator introAnim;
    public static Position introPos = new Position();
    public static ImageIcon splash = new ImageIcon("data/images/trident/splash.png");
    public static boolean inIntro = false;
    private RenderingThread rendThread = new RenderingThread();
    
    public MainPanel(){

        System.setProperty("sun.java2d.opengl", "true"); // hardware acceleration?

        setBackground(Color.black);

        Trident.player = new Player(new Position(), km, 0.1, this, "data/images/player", 16, 16);
        Trident.player.camera.setDimension = new Dimension(frameManager.WIDTH, frameManager.HEIGHT);
        Trident.player.resizeImages(32, 32);
        Trident.currentScene = new Scene("Test Scene");
        Trident.camShake = new CamShake(Trident.player.camera);
        Trident.lightManager.blur.setRadius(100);

        Trident.addCustomEntity(new BoxColl());
        Trident.addCustomEntity(new BoxNoColl());
        Trident.addCustomEntity(new InvisColl());
        Trident.addCustomEntity(new PlrStart());
        Trident.addCustomEntity(new Trigger());
        Trident.addCustomEntity(new TridLight());

        setFocusTraversalKeysEnabled(false);

        Update.setup();

        Trident.setupScenes();

        Trident.loadScene(Trident.defaultScene);

        try{
            ArrayList<Animation> anims = new ArrayList<Animation>();
            anims.add(new Animation("data/animations/intro"));

            introAnim = new Animator(introPos, anims);
        }catch(Exception e){
            Trident.intro = false;
        }
        if(Trident.intro){
            introAnim.play("intro");
            inIntro = true;
        }
        if(Trident.splash != null && BTools.hasImage(Trident.splash)){
            BTools.resizeImgIcon(Trident.splash, 160, 160);
        }

        rendThread.start();

        server = new Server(new ServerListener(), this);
    }

    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        Graphics g = frameManager.newFrame();

        frameManager.bgColor = Trident.currentScene.bgColor;
        
        if(rendThread.lastFrame != null){
            g.drawImage(rendThread.lastFrame, 0, 0, null);
        }
        
        frameManager.renderFrame(this, graphics);
    }

    public MainPanel panel = this;
    private class InputListener extends InputAdapter {
        public InputListener(JPanel panel){
            super(panel);
        }

        public void onKeyPressed(int key){
            if(key == KeyEvent.VK_F12){
                frameManager.saveScreenshot("data/screenshots");
                Update.tridentEvent(Trident.EVENT_SCREENSHOT);
            }
            if(key == KeyEvent.VK_F11){
                Trident.fullscreen = !Trident.fullscreen;
                Main.window = BTools.getWindowFullscreen(Main.window, Trident.fullscreen, panel);
                return;
            }
            if(!inIntro){
                if(key == 192 && Trident.consoleEnabled){
                    // dev console
                    String command = JOptionPane.showInputDialog(null, "Enter Console Command", "Dev Console", JOptionPane.QUESTION_MESSAGE);
                    if(command != null && command.length() > 0){
                        ArrayList<String> cmdParts = new ArrayList<String>();
                        Scanner scanner = new Scanner(command);
                        while(scanner.hasNext()){
                            cmdParts.add(scanner.next());
                        }
                        scanner.close();

                        if(cmdParts.size() == 0) return;
                        try{
                        switch(cmdParts.get(0)){
                        case "drawCollision":
                            if(cmdParts.size() == 1){
                                JOptionPane.showMessageDialog(null, "drawCollision is " + Trident.drawCollision, "Dev Console", JOptionPane.INFORMATION_MESSAGE);
                                break;
                            }
                            if(cmdParts.get(1).equals("1") || cmdParts.get(1).equals("true")){
                                Trident.drawCollision = true;
                            }
                            if(cmdParts.get(1).equals("0") || cmdParts.get(1).equals("false")){
                                Trident.drawCollision = false;
                            }
                            break;
                        case "engineDraw":
                            if(cmdParts.size() == 1){
                                JOptionPane.showMessageDialog(null, "engineDraw is " + Trident.engineDraw, "Dev Console", JOptionPane.INFORMATION_MESSAGE);
                                break;
                            }
                            if(cmdParts.get(1).equals("1") || cmdParts.get(1).equals("true")){
                                Trident.engineDraw = true;
                            }
                            if(cmdParts.get(1).equals("0") || cmdParts.get(1).equals("false")){
                                Trident.engineDraw = false;
                            }
                            break;
                        case "drawPos":
                            if(cmdParts.size() == 1){
                                JOptionPane.showMessageDialog(null, "drawPos is " + Trident.drawPos, "Dev Console", JOptionPane.INFORMATION_MESSAGE);
                                break;
                            }
                            if(cmdParts.get(1).equals("1") || cmdParts.get(1).equals("true")){
                                Trident.drawPos = true;
                            }
                            if(cmdParts.get(1).equals("0") || cmdParts.get(1).equals("false")){
                                Trident.drawPos = false;
                            }
                            break;
                        case "noclip":
                            if(cmdParts.size() == 1){
                                JOptionPane.showMessageDialog(null, "noclip is " + Trident.noclip, "Dev Console", JOptionPane.INFORMATION_MESSAGE);
                                break;
                            }
                            if(cmdParts.get(1).equals("1") || cmdParts.get(1).equals("true")){
                                Trident.noclip = true;
                            }
                            if(cmdParts.get(1).equals("0") || cmdParts.get(1).equals("false")){
                                Trident.noclip = false;
                            }
                            break;
                        case "tp":
                            int x = Integer.parseInt(cmdParts.get(1));
                            int y = Integer.parseInt(cmdParts.get(2));
                            Trident.setPlrPos(new Position(x, y));
                            break;
                        case "loadMap":
                            String map = "";
                            for(int i = 1; i < cmdParts.size(); i++){
                                map += cmdParts.get(i);
                                if(i != cmdParts.size() - 1) map += " ";
                            }
                            Trident.loadScene(map);
                            break;
                        case "drawFrames":
                            if(cmdParts.size() == 1){
                                JOptionPane.showMessageDialog(null, "drawFrames is " + Trident.drawFrames, "Dev Console", JOptionPane.INFORMATION_MESSAGE);
                                break;
                            }
                            if(cmdParts.get(1).equals("1") || cmdParts.get(1).equals("true")){
                                Trident.drawFrames = true;
                            }
                            if(cmdParts.get(1).equals("0") || cmdParts.get(1).equals("false")){
                                Trident.drawFrames = false;
                            }
                            break;
                        case "debugColor":
                            int r, g, b;
                            float alpha = -1;
                            r = Integer.parseInt(cmdParts.get(1));
                            g = Integer.parseInt(cmdParts.get(2));
                            b = Integer.parseInt(cmdParts.get(3));
                            if(cmdParts.size() == 5){
                                alpha = Float.parseFloat(cmdParts.get(4));
                            }
                            if(alpha != -1){
                                Trident.debugColor = new Color(r / 255f, g / 255f, b / 255f, alpha);
                            }else{
                                Trident.debugColor = new Color(r, g, b);
                            }
                            break;
                        case "enableBloom":
                            if(cmdParts.size() == 1){
                                JOptionPane.showMessageDialog(null, "enableBloom is " + Trident.enableBloom, "Dev Console", JOptionPane.INFORMATION_MESSAGE);
                                break;
                            }
                            if(cmdParts.get(1).equals("1") || cmdParts.get(1).equals("true")){
                                Trident.enableBloom = true;
                            }
                            if(cmdParts.get(1).equals("0") || cmdParts.get(1).equals("false")){
                                Trident.enableBloom = false;
                            }
                            break;
                        case "enableExposure":
                            if(cmdParts.size() == 1){
                                JOptionPane.showMessageDialog(null, "enableExposure is " + Trident.enableExposure, "Dev Console", JOptionPane.INFORMATION_MESSAGE);
                                break;
                            }
                            if(cmdParts.get(1).equals("1") || cmdParts.get(1).equals("true")){
                                Trident.enableExposure = true;
                            }
                            if(cmdParts.get(1).equals("0") || cmdParts.get(1).equals("false")){
                                Trident.enableExposure = false;
                            }
                            break;
                        case "setBloom":
                            double amount = Double.parseDouble(cmdParts.get(1));
                            Trident.setBloom(amount);
                            break;
                        case "setExposure":
                            double expo = Double.parseDouble(cmdParts.get(1));
                            Trident.setExposure(expo);
                            break;
                        case "setLightBlur":
                            int blurLevel = Integer.parseInt(cmdParts.get(1)); 
                            Trident.setLightBlur(blurLevel);
                            break;
                        case "fullbright":
                            if(cmdParts.size() == 1){
                                JOptionPane.showMessageDialog(null, "fullbright is " + Trident.fullbright, "Dev Console", JOptionPane.INFORMATION_MESSAGE);
                                break;
                            }
                            if(cmdParts.get(1).equals("1") || cmdParts.get(1).equals("true")){
                                Trident.fullbright = true;
                            }
                            if(cmdParts.get(1).equals("0") || cmdParts.get(1).equals("false")){
                                Trident.fullbright = false;
                            }
                            break;
                        default:
                            int cmd = Update.command(cmdParts);
                            if(cmd != 0){
                                JOptionPane.showMessageDialog(null, "Unknown command: " + cmdParts.get(0), "Dev Console", JOptionPane.INFORMATION_MESSAGE);
                            }
                            break;
                        }
                    }catch(Exception e){
                        int sel = JOptionPane.showConfirmDialog(null, "Something went wrong while running your command. Save error log?", "Dev Console", JOptionPane.YES_NO_OPTION);
                        if(sel == 0){
                            try{
                                File file = new File("errorLog.txt");
                                file.createNewFile();
                                PrintWriter writer = new PrintWriter(file);
                                e.printStackTrace(writer);
                                writer.close();
                            }catch(Exception ex){
                                JOptionPane.showMessageDialog(null, "Somehow, something also went wrong while saving the error logs. Printing into the default console...", "Dev Console", JOptionPane.INFORMATION_MESSAGE);
                                System.out.println(" *********  What went wrong while printing:");
                                ex.printStackTrace();
                                System.out.println(" *********  What went wrong with the command:");
                                e.printStackTrace();
                            }
                        }
                    }
                    }
                }
                Inputs.keyPressed(key);
            }
        }

        public void onMousePressed(int mb, Point mousePos){
            mousePos = frameManager.getMousePos(panel, mousePos);
            Position worldPos = Trident.player.camera.mouseToPos(mousePos);
            if(!inIntro) Inputs.mousePressed(mb, mousePos, worldPos);
        }

        public void onScroll(int scroll){
            if(!inIntro) Inputs.onScroll(scroll);
        }
    }

    private class ServerListener implements ActionListener {
        public void actionPerformed(ActionEvent event){
            if(inIntro){
                introAnim.update(server.getElapsedTime());
                if(!introAnim.isPlaying()){
                    inIntro = false;
                }
                repaint();
                return;
            }
            Trident.mousePos = frameManager.getMousePos(panel, km.getMousePos());
            Trident.mouseDelta = km.getMouseDelta();
            Trident.mouseWorldPos = Trident.player.camera.mouseToPos(Trident.mousePos);

            Trident.camShake.update(server.getElapsedTime());

            if(!Trident.noclip) Trident.player.updateWithCollision(server.getElapsedTime(), Trident.currentScene.getCollision());
            else Trident.player.update(server.getElapsedTime());

            for(int i = 0; i < Trident.getEntities().size(); i++){
                TridEntity e = Trident.getEntities().get(i);
                e.update(server.getElapsedTime());
                if(e instanceof Trigger){
                    Trigger trig = (Trigger)e;
                    if(trig.containsPos(Trident.player.getPos())){
                        Update.trigger(trig.id);
                    }
                }
            }

            if(Trident.reset){
                km.reset();
                Trident.reset = false;
            }
            for(int i = 0; i < 255; i++){
                Trident.keys[i] = km.getKeyDown(i);
            }
            Trident.m1 = km.getMouseDown(1);
            Trident.m2 = km.getMouseDown(2);
            Trident.m3 = km.getMouseDown(3);
            Trident.m4 = km.getMouseDown(4);
            Trident.m5 = km.getMouseDown(5);

            if(Trident.newSprite != null){
                Trident.player = new Player(Trident.player.getPos(), km, 0.2, panel, Trident.newSprite, 16, 16);
                Trident.newSprite = null;
                Trident.player.resizeImages(32, 32);
                Trident.player.camera.setDimension = new Dimension(frameManager.WIDTH, frameManager.HEIGHT);
                Trident.camShake = new CamShake(Trident.player.camera);
                Trident.player.shortCollision = true;
            }

            Update.update(server.getElapsedTime());

            try {Trident.getEntities().sort((o1, o2) -> o2.compareSort(o1));} catch(Exception e){}
        }
    }
}
