import java.awt.*;

/**
 * Created by chales on 11/6/2017.
 * Edits by mblair on 10/27/2025
 */
public class Ball {

    //VARIABLE DECLARATION SECTION
    //Here's where you state which variables you are going to use.
    public String name;               //name of the hero
    public int xpos;                  //the x position
    public int ypos;                  //the y position
    public int dx;                    //the speed of the hero in the x direction
    public int dy;                    //the speed of the hero in the y direction
    public int width;                 //the width of the hero image
    public int height;                //the height of the hero image
    public boolean isAlive;
    public Rectangle rect;


    //This is a constructor that takes 3 parameters.
    // This allows us to specify the hero's name and position when we build it.
    public Ball(String pName, int pXpos, int pYpos) {
        name = pName;
        xpos = pXpos;
        ypos = pYpos;
        dx = 10;
        dy = 10;
        width = 50;
        height = 50;
        isAlive = true;
        rect = new Rectangle(xpos, ypos, width, height);
    }

    public void move() { // move
        xpos = xpos + dx;
        ypos = ypos + dy;
    }

    public void bounce() {

    }

    public void wrap() {

    }
}






