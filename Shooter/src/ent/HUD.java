package ent;

import blib.game.Entity;
import blib.util.*;
import trident.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import custom.*;
import custom.guns.Hands;
public class HUD extends TridEntity { // Controls the Heads-Up Display

    // Constructor, runs when the entity is created
    public HUD(Position pos){
        super(pos);
        renderType = Entity.TOPPRIORITY;
    }
    // Registry constructor, used only for adding to the registry
    public HUD(){
        super("hud", false, 0);
    }
    // Custom constructor, used by the engine when building a scene
    public TridEntity construct(Position pos, Dimension collision, int[] data){
        return new HUD(pos);
    }

    // Render while in game
    public void render(Graphics g, JPanel panel, int x, int y){

        // Make an image to draw UI on
        //  > Making an image will help to make the UI "lag" behind the camera a little
        BufferedImage ui = new BufferedImage(684, 462, BufferedImage.TYPE_INT_ARGB);
        Graphics uig = ui.getGraphics();

        // Width and Height of the frame. You can also use
        // Trident.getFrameWidth() and Trident.getFrameHeight()
        int WIDTH = 684, HEIGHT = 462;

        // Make sure player has a gun and it isn't empty hands (since hands works somewhat like a gun)
        if(PlayerInfo.hasGun && !(PlayerInfo.gun instanceof Hands)){
            drawText(PlayerInfo.gun.name, uig, 2, HEIGHT - 60, TextBox.LEFT, 30); // gun name

            if(PlayerInfo.reloadTime <= 0) drawText(PlayerInfo.gun.ammo + "/" + PlayerInfo.gun.ammoStorage, uig, 2, HEIGHT - 30, TextBox.LEFT, 30); // ammo count
            else drawText("RELOADING", uig, 2, HEIGHT - 30, TextBox.LEFT, 30);
        }

        // Selection text at bottom right
        if(PlayerInfo.newSelTime > 0){

            // Collect gun names
            int prev = PlayerInfo.newSelGun - 1;
            if(prev < 0) prev = PlayerInfo.guns.size() - 1;
            int next = PlayerInfo.newSelGun + 1;
            if(next > PlayerInfo.guns.size() - 1) next = 0;

            String[] str = {
                PlayerInfo.guns.get(prev).name.toUpperCase(),
                PlayerInfo.guns.get(PlayerInfo.newSelGun).name.toUpperCase(),
                PlayerInfo.guns.get(next).name.toUpperCase(),
            };

            // Write all the gun names
            drawText(str[0], uig, WIDTH - 10, HEIGHT - 60, TextBox.RIGHT, 15);
            drawText(str[1], uig, WIDTH - 10, HEIGHT - 40, TextBox.RIGHT, 25);
            drawText(str[2], uig, WIDTH - 10, HEIGHT - 20, TextBox.RIGHT, 15);
        }   
        
        // (x, y) is the actual (screen) position, basically player
        // we use (x, y) so that when the camera "lags" behind the player, the hud is also affected
        // divide it by -2 so it lags in the right direction and doesn't do so too much, so it's still readable
        g.drawImage(ui, (x - 684 / 2) / -2, (y - 462 / 2) / -2, panel);

    }
    public void drawText(String text, Graphics g, int x, int y, int alignment, int fontSize){ // draws text with two black copies behind it so it can stand out on bright and dark maps
        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.PLAIN, fontSize));
        TextBox.draw(text, g, x - 2, y, alignment);
        TextBox.draw(text, g, x + 2, y, alignment);
        g.setColor(Color.white);
        TextBox.draw(text, g, x, y, alignment);
    }

    // Runs every tick while the game is running
    public void update(long elapsedTime){
        // go to the player's position
        position = Trident.getPlrPos();
    }
}
