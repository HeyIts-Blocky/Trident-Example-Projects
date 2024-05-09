package trident;

import java.awt.image.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import blib.util.*;
import java.awt.*;
import blib.game.*;
public class RenderingThread extends Thread {

    public BufferedImage lastFrame = null;
    private long elapsedTime, previousStartTime = -1;
    
    public void run(){
        while(true){
            try{
                
                long now = System.currentTimeMillis();
                elapsedTime = previousStartTime != -1 ? now - previousStartTime : 0;
                previousStartTime = now;
                int WIDTH = Trident.getFrameWidth(), HEIGHT = Trident.getFrameHeight();
                int offX, offY;
                offX = Trident.camShake.offX;
                offY = Trident.camShake.offY;
                BufferedImage newFrame = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
                Graphics g = newFrame.getGraphics();

                if(Trident.engineDraw){
                    for(TridEntity e: Trident.currentScene.entities){
                        Point p = Trident.player.camera.worldToScreen(e.position);
                        e.engineRender(g, null, p.x, p.y);
                    }
                }

                ArrayList<ArrayList<Entity>> splitEnt = Trident.player.camera.splitEntities(Trident.tridArrToEntArr(Trident.currentScene.entities), 16);
                if(!Trident.engineDraw) Trident.player.camera.render(g, splitEnt.get(2), offX, offY);
                if(!Trident.engineDraw) Trident.player.camera.render(g, splitEnt.get(0), offX, offY);
                if(Trident.drawPlayer){
                    Trident.player.render(null, g, WIDTH / 2 - 16 - offX, HEIGHT / 2 - 16 - offY);
                }
                if(!Trident.engineDraw) Trident.player.camera.render(g, splitEnt.get(1), offX, offY);

                if(!Trident.fullbright) Trident.lightManager.render(Trident.player.camera, Trident.lights, g, offX, offY);

                if(!Trident.engineDraw) Trident.player.camera.render(g, splitEnt.get(3), offX, offY);


                

                if(Trident.drawCollision){
                    g.setColor(Color.red);
                    ArrayList<Rectangle> collision = Trident.currentScene.getCollision();
                    collision.add(Trident.player.getCollision());
                    for(Rectangle r: collision){
                        Point p = Trident.player.camera.worldToScreen(new Position(r.x, r.y));
                        g.drawRect(p.x, p.y, r.width, r.height);
                        g.drawLine(p.x, p.y, p.x + r.width, p.y + r.height);
                    }
                }

                if(Trident.drawPos){
                    g.setColor(Trident.debugColor);
                    g.setFont(new Font("Arial", Font.ITALIC, 10));
                    TextBox.draw(Trident.player.getPos().toStringSimple(), g, 10, 30);
                }
                if(Trident.drawFrames){
                    g.setColor(Trident.debugColor);
                    g.setFont(new Font("Arial", Font.ITALIC, 10));
                    TextBox.draw("TPS: " + (1000 / Math.max(MainPanel.server.getElapsedTime(), 1)) + " (" + MainPanel.server.getElapsedTime() + " ms)", g, 10, 30);
                    TextBox.draw("FPS: " + (1000 / Math.max(elapsedTime, 1)) + " (" + elapsedTime + " ms)", g, 10, 40); 
                }
                
                // Apply Post Processing
                if(Trident.enableExposure){
                    Trident.exposure.filter(newFrame, newFrame);
                }
                if(Trident.enableBloom){
                    Trident.bloom.filter(newFrame, newFrame);
                }
                
                if(MainPanel.inIntro){
                    g.setColor(Color.black);
                    g.fillRect(0, 0, 700, 500);

                    if(Trident.splash != null && BTools.hasImage(Trident.splash)){
                        // Trident splash + custom splash
                        MainPanel.splash.paintIcon(null, g, WIDTH / 2 - 80, 40);
                        Trident.splash.paintIcon(null, g, WIDTH / 2 - 80, HEIGHT - 200);
                    }else{
                        // Trident splash only
                        MainPanel.splash.paintIcon(null, g, WIDTH / 2 - 80, HEIGHT / 2 - 80);
                    }

                    float alpha = (float)MainPanel.introPos.x;
                    g.setColor(new Color(0f, 0f, 0f, alpha));
                    g.fillRect(0, 0, 700, 500);
                }

                

                lastFrame = newFrame;
            }catch(Exception e){
                if(!(e instanceof ConcurrentModificationException)){
                    e.printStackTrace();
                }
            }
            

        }
    }
}
