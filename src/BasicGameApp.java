//Basic Game Application
// Basic Object, Image, Movement
// Threaded

//*******************************************************************************
//Import Section
//Add Java libraries needed for the game
//import java.awt.Canvas;

//Graphics Libraries
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

//*******************************************************************************

public class BasicGameApp implements Runnable, KeyListener {

    //Variable Definition Section
    //Declare the variables used in the program
    //You can set their initial values too

    //Sets the width and height of the program window
    final int WIDTH = 1000;
    final int HEIGHT = 700;

    //Declare the variables needed for the graphics
    public JFrame frame;
    public Canvas canvas;
    public JPanel panel;

    public BufferStrategy bufferStrategy;

    Stick stick;
    Image stickImage;

    Ball ball;
    Image ballImage;

    BlueBlock BlueBlock;
    Image BlueBlockImage;
    BlueBlock[] BlueBlockArray;

    GreenBlock GreenBlock;
    Image GreenBlockImage;
    GreenBlock[] GreenBlockArray;

    RedBlock RedBlock;
    Image RedBlockImage;
    RedBlock[] RedBlockArray;

    YellowBlock YellowBlock;
    Image YellowBlockImage;
    YellowBlock[] YellowBlockArray;

    public boolean firstBallStick = true;
    public boolean firstBallYellowCrash = true;



    // Main method definition
    // This is the code that runs first and automatically
    public static void main(String[] args) {
        BasicGameApp ex = new BasicGameApp();   //creates a new instance of the game
        new Thread(ex).start();                 //creates a threads & starts up the code in the run( ) method
    }


    // This section is the setup portion of the program
    // Initialize your variables and construct your program objects here.
    public BasicGameApp() { // BasicGameApp constructor
        setUpGraphics();

        BlueBlockArray = new BlueBlock[10];
        BlueBlockImage = Toolkit.getDefaultToolkit().getImage("BlueBlock.png");

        GreenBlockArray = new GreenBlock[10];
        GreenBlockImage = Toolkit.getDefaultToolkit().getImage("GreenBlock.png");

        RedBlockArray = new RedBlock[10];
        RedBlockImage = Toolkit.getDefaultToolkit().getImage("RedBlock.png");

        YellowBlockArray = new YellowBlock[10];
        YellowBlockImage = Toolkit.getDefaultToolkit().getImage("YellowBlock.png");

        stick = new Stick("stick", 450, 600);
        stickImage = Toolkit.getDefaultToolkit().getImage("Stick.png");

        ball = new Ball("wesley", 500,500);
        ballImage = Toolkit.getDefaultToolkit().getImage("Wesley.png");

        for (int x = 0; x < BlueBlockArray.length; x=x+1) {
                BlueBlockArray[x] = new BlueBlock("BlueBlock" + 1, x*100, 0);
        }
        for (int x = 0; x < GreenBlockArray.length; x=x+1) {
            GreenBlockArray[x] = new GreenBlock("GreenBlock" + 1, x*100, 34);
        }
        for (int x = 0; x < RedBlockArray.length; x=x+1) {
            RedBlockArray[x] = new RedBlock("RedBlock" + 1, x*100, 68);
        }
        for (int x = 0; x < YellowBlockArray.length; x=x+1) {
            YellowBlockArray[x] = new YellowBlock("YellowBlock" + 1, x*100, 102);
        }

        run();

    } // end BasicGameApp constructor


//*******************************************************************************
//User Method Section
// put your code to do things here.

    // main thread
    // this is the code that plays the game after you set things up
    public void run() {
        //for the moment we will loop things forever.
        while (true) {
            moveThings();  //move all the game objects
            render();  // paint the graphics
            pause(30); // sleep for 10 ms
        }
    }

    public void moveThings() {
        stick.move();
        ball.bounce();

        ballStickCrash();
        ballFloorCrash();
        ballYellowCrash();
    }

    public void ballStickCrash(){
        if(ball.rect.intersects(stick.rect) && firstBallStick == true){
            firstBallStick = false;
            ball.dy = -ball.dy;
        }
        if(ball.rect.intersects(stick.rect)){
            firstBallStick = true;
        }
    }
    public void ballFloorCrash(){
        if(ball.ypos>=700-ball.height){
            ball.dy = 0;
            ball.dx = 0;
        }
    }
    public void ballYellowCrash(){
        for(int x = 0;x<YellowBlockArray.length;x=x+1) {
            if (ball.rect.intersects(YellowBlockArray[x].rect) && YellowBlockArray[x].health > 0 && firstBallYellowCrash) {
                System.out.println(YellowBlockArray[x].health);
                ball.dy = -ball.dy;
                YellowBlockArray[x].health = YellowBlockArray[x].health - 1;
            }
            if (!ball.rect.intersects(YellowBlockArray[x].rect)){
                firstBallYellowCrash = false;
            }
        }
    }

    //Paints things on the screen using bufferStrategy
    private void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);
        //draw the image

        g.drawImage(stickImage, stick.xpos, stick.ypos, stick.width, stick.height, null);
        g.drawImage(ballImage, ball.xpos, ball.ypos, ball.width, ball.height, null);

        for (int x = 0; x < BlueBlockArray.length; x++) {
            g.drawImage(BlueBlockImage, BlueBlockArray[x].xpos, BlueBlockArray[x].ypos, BlueBlockArray[x].width, BlueBlockArray[x].height, null);
            g.drawImage(GreenBlockImage, GreenBlockArray[x].xpos, GreenBlockArray[x].ypos, GreenBlockArray[x].width, GreenBlockArray[x].height, null);
            g.drawImage(RedBlockImage, RedBlockArray[x].xpos, RedBlockArray[x].ypos, RedBlockArray[x].width, RedBlockArray[x].height, null);
            g.drawImage(YellowBlockImage, YellowBlockArray[x].xpos, YellowBlockArray[x].ypos, YellowBlockArray[x].width, YellowBlockArray[x].height, null);
        }
        //Disappearing blocks
//        for(int x = 0;x<=YellowBlockArray.length;x=x+1) {
//            if (YellowBlockArray[x].health < 0) {
//
//            }
//        }

        //write text --> make say you lost if it hits the bottom
        if(ball.ypos>=700-ball.height){
            g.setFont(new Font("Arial", Font.BOLD,37));
            g.setColor(new Color(0,0,0));
            g.drawString("You Lost :(", 450,400);
        }

        g.dispose();
        bufferStrategy.show();
    }

    //Pauses or sleeps the computer for the amount specified in milliseconds
    public void pause(int time ) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }

    //Graphics setup method
    private void setUpGraphics() {
        frame = new JFrame("Application Template");   //Create the program window or frame.  Names it.

        panel = (JPanel) frame.getContentPane();  //sets up a JPanel which is what goes in the frame
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));  //sizes the JPanel
        panel.setLayout(null);   //set the layout

        // creates a canvas which is a blank rectangular area of the screen onto which the application can draw
        // and trap input events (Mouse and Keyboard events)
        canvas = new Canvas();
        canvas.setBounds(0, 0, WIDTH, HEIGHT);
        canvas.setIgnoreRepaint(true);

        panel.add(canvas);  // adds the canvas to the panel.

        // frame operations
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //makes the frame close and exit nicely
        frame.pack();  //adjusts the frame and its contents so the sizes are at their default or larger
        frame.setResizable(false);   //makes it so the frame cannot be resized
        frame.setVisible(true);      //IMPORTANT!!!  if the frame is not set to visible it will not appear on the screen!

        // sets up things so the screen displays images nicely.
        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        canvas.requestFocus();
        canvas.addKeyListener(this);
        System.out.println("DONE graphic setup");
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(e.getKeyCode());
        if(e.getKeyCode() == 37){
            stick.dx = -20;
        }
        if(e.getKeyCode() == 39){
            stick.dx = 20;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == 37){
            stick.dx = 0;
        }
        if(e.getKeyCode() == 39){
            stick.dx = 0;
        }

    }
}
