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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

//*******************************************************************************

public class BasicGameApp implements Runnable, KeyListener, MouseListener {

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
    Ball[] ballArray;
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

    BasketBallJesus basketBallJesus;
    Image BasketBallImage;

    Image forest = Toolkit.getDefaultToolkit().getImage("Forest.jpg"); // background

    //To make check crash methods work
    public boolean firstBallStick = true;
    public boolean firstBallYellowCrash = true;
    public boolean firstBallRedCrash = true;
    public boolean firstBallGreenCrash = true;
    public boolean firstBallBlueCrash = true;

    public int ballCount = 1;

    public int totalsum;

    public boolean gameFinish = false;


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

        ballArray = new Ball[100];
        ballImage = Toolkit.getDefaultToolkit().getImage("Wesley.png");

        basketBallJesus = new BasketBallJesus("Jesus", 500, 500);
        BasketBallImage = Toolkit.getDefaultToolkit().getImage("BasketBallJesus.jpg");

        // creates block rows
        for (int x = 0; x < BlueBlockArray.length; x = x + 1) {
            int randNum = (int) (Math.random()*3);
            BlueBlockArray[x] = new BlueBlock("BlueBlock" + 1, x * 100, 0);
            BlueBlockArray[x].health = BlueBlockArray[x].health + randNum;
        }
        for (int x = 0; x < RedBlockArray.length; x = x + 1) {
            int randNum = (int) (Math.random()*3);
            RedBlockArray[x] = new RedBlock("RedBlock" + 1, x * 100, 34);
            RedBlockArray[x].health = RedBlockArray[x].health + randNum;
        }
        for (int x = 0; x < ballArray.length; x = x + 1) {
            int randX = (int) ((Math.random()*400)+100);
            int randY = (int) ((Math.random()*350)+100);
            ballArray[x] = new Ball("wesley" + 1, randX, randY);
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
        for(int i = 0; i < ballCount; i++){
            ballArray[i].bounce();
        }
        basketBallJesus.move();

        ballStickCrash();
//        ballYellowCrash();
        ballBlueCrash();
//        ballGreenCrash();
        ballRedCrash();
        aveHealth();
    }

    public void ballStickCrash() {
        for(int i = 0; i<ballArray.length; i++) {
            if (ballArray[i].rect.intersects(stick.rect) && firstBallStick == true) { // bounce ball off stick with twist
                firstBallStick = false;
                int randNum = (int) (Math.random() * 25);

                if (randNum < 10) {             //Makes ball just bounce
                    ballArray[i].dy = -ballArray[i].dy;
                } else if (randNum < 20) {      //Makes ball just bounce and makes stick teleport
                    ballArray[i].dy = -ballArray[i].dy;
                    stick.xpos = 0;
                } else if (randNum < 30) {      //Makes ball just bounce and makes stick teleport
                    ballArray[i].dy = -ballArray[i].dy;
                    stick.xpos = 700;
                } else if (randNum < 40) {       //Makes ball bounce but at default speed(counteracts sped up one(below))
                    ballArray[i].dy = -10;
                    ballArray[i].dx = 10;
                } else if (randNum <= 50) {      //speeds ball up(makes harder)
                    ballArray[i].dy = -randNum + 7;
                    ballArray[i].dx = randNum - 7;
                }

            }
            if (!ballArray[i].rect.intersects(stick.rect)) {
                firstBallStick = true;
            }
        }
    }

//    public void ballYellowCrash() {
//        for (int x = 0; x < YellowBlockArray.length; x = x + 1) {
//            if (ball.rect.intersects(YellowBlockArray[x].rect) && YellowBlockArray[x].health > 0 && firstBallYellowCrash == true) {
//                firstBallYellowCrash = false;
//                System.out.println(YellowBlockArray[x].health);
//                ball.dy = -ball.dy;
//                YellowBlockArray[x].health = YellowBlockArray[x].health - 1;
//            }
//            if (!ball.rect.intersects(YellowBlockArray[x].rect)) {
//                firstBallYellowCrash = true;
//            }
//        }
//    }

    public void ballBlueCrash() {
        for(int i = 0; i<ballArray.length; i++) {
            for (int x = 0; x < BlueBlockArray.length; x = x + 1) {
                if (ballArray[i].rect.intersects(BlueBlockArray[x].rect) && BlueBlockArray[x].health > 0 && firstBallBlueCrash == true) {
                    firstBallBlueCrash = false;
                    ballArray[i].dy = -ballArray[i].dy;
                    BlueBlockArray[x].health = BlueBlockArray[x].health - 1;
                }
                if (!ballArray[i].rect.intersects(BlueBlockArray[x].rect)) {
                    firstBallBlueCrash = true;
                }
            }
        }
    }

    public void ballRedCrash() {
        for(int i = 0; i<ballArray.length; i++) {
            for (int x = 0; x < RedBlockArray.length; x = x + 1) {
                if (ballArray[i].rect.intersects(RedBlockArray[x].rect) && RedBlockArray[x].health > 0 && firstBallRedCrash == true) {
                    firstBallRedCrash = false;
                    System.out.println(RedBlockArray[x].health);
                    ballArray[i].dy = -ballArray[i].dy;
                    RedBlockArray[x].health = RedBlockArray[x].health - 1;
                }
                if (!ballArray[i].rect.intersects(RedBlockArray[x].rect)) {
                    firstBallRedCrash = true;
                }
            }
        }
    }
    public void aveHealth(){
        totalsum = 0;
        for(int i=0;i<BlueBlockArray.length;i++){
            totalsum = totalsum + BlueBlockArray[i].health + RedBlockArray[i].health;

        }
    }

//    public void ballGreenCrash() {
//        for (int x = 0; x < GreenBlockArray.length; x = x + 1) {
//            if (ball.rect.intersects(GreenBlockArray[x].rect) && GreenBlockArray[x].health > 0 && firstBallGreenCrash == true) {
//                firstBallGreenCrash = false;
//                System.out.println(GreenBlockArray[x].health);
//                ball.dy = -ball.dy;
//                GreenBlockArray[x].health = GreenBlockArray[x].health - 1;
//            }
//            if (!ball.rect.intersects(GreenBlockArray[x].rect)) {
//                firstBallGreenCrash = true;
//            }
//        }
//    }

    //Paints things on the screen using bufferStrategy
    private void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);
        //draw the image
        g.drawImage(forest, 0, 0, WIDTH, HEIGHT, null);

        g.drawImage(stickImage, stick.xpos, stick.ypos, stick.width, stick.height, null);
        g.drawImage(BasketBallImage, basketBallJesus.xpos, basketBallJesus.ypos, basketBallJesus.width, basketBallJesus.height, null);

        for(int i = 0;i<ballCount;i++){
            if(ballArray[i].ypos < 700 - ballArray[i].height){
                g.drawImage(ballImage, ballArray[i].xpos, ballArray[i].ypos, ballArray[i].width, ballArray[i].height, null);
            }
        }

        for (int x = 0; x < BlueBlockArray.length; x++) {
            if (BlueBlockArray[x].health > 0) {
                g.drawImage(BlueBlockImage, BlueBlockArray[x].xpos, BlueBlockArray[x].ypos, BlueBlockArray[x].width, BlueBlockArray[x].height, null);
            }
        }

        for (int x = 0; x < BlueBlockArray.length; x++) {
            if (RedBlockArray[x].health > 0) {
                g.drawImage(RedBlockImage, RedBlockArray[x].xpos, RedBlockArray[x].ypos, RedBlockArray[x].width, RedBlockArray[x].height, null);
            }
        }
        //Disappearing blocks
//        for(int x = 0;x<=YellowBlockArray.length;x=x+1) {
//            if (YellowBlockArray[x].health < 0) {
//
//            }
//        }

        //write text --> make say you lost if it hits the bottom
        boolean ballinPlay = false;
        for(int i=0;i<ballCount; i++){
            if (ballArray[i].ypos < 700) {
                ballinPlay = true;
            }
        }
        if (!ballinPlay){
            g.setFont(new Font("Arial", Font.BOLD, 37));
            g.setColor(new Color(255, 0, 0));
            g.drawString("You Lost :(", 450, 400);
        }
        //says you won when hitting top(get through all blocks first)
        if (totalsum == 0){
            g.setFont(new Font("Arial", Font.BOLD, 37));
            g.setColor(new Color(255, 0, 0));
            g.drawString("You Won :D", 450, 400);
            for(int x=0; x<ballArray.length;x++){
                ballArray[x].dx = 0;
                ballArray[x].dy = 0;
            }
        }

        for (int x = 0; x < BlueBlockArray.length; x = x + 1) {
            if (BlueBlockArray[x].health > 0) {
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.setColor(new Color(0, 0, 0));
                g.drawString("" + BlueBlockArray[x].health, x * 100 + 20, 20);
            } else {
                g.setFont(new Font("Arial", Font.BOLD, 0));
                g.setColor(new Color(0, 0, 0));
                g.drawString("" + BlueBlockArray[x].health, x * 100 + 20, 102 + 20);
            }
        }
//        for (int x = 0; x < GreenBlockArray.length; x = x + 1) {
//            if (GreenBlockArray[x].health > 0) {
//                g.setFont(new Font("Arial", Font.BOLD, 20));
//                g.setColor(new Color(0, 0, 0));
//                g.drawString("" + GreenBlockArray[x].health, x * 100 + 20, 34 + 20);
//            } else {
//                g.setFont(new Font("Arial", Font.BOLD, 0));
//                g.setColor(new Color(0, 0, 0));
//                g.drawString("" + GreenBlockArray[x].health, x * 100 + 20, 102 + 20);
//            }
//        }
        for (int x = 0; x < RedBlockArray.length; x = x + 1) {
            if (RedBlockArray[x].health > 0) {
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.setColor(new Color(0, 0, 0));
                g.drawString("" + RedBlockArray[x].health, x * 100 + 20, 34 + 20);
            } else {
                g.setFont(new Font("Arial", Font.BOLD, 0));
                g.setColor(new Color(0, 0, 0));
                g.drawString("" + RedBlockArray[x].health, x * 100 + 20, 102 + 20);
            }
        }
//        for (int x = 0; x < YellowBlockArray.length; x = x + 1) {
//            if (YellowBlockArray[x].health > 0) {
//                g.setFont(new Font("Arial", Font.BOLD, 20));
//                g.setColor(new Color(0, 0, 0));
//                g.drawString("" + YellowBlockArray[x].health, x * 100 + 20, 102 + 20);
//            } else {
//                g.setFont(new Font("Arial", Font.BOLD, 0));
//                g.setColor(new Color(0, 0, 0));
//                g.drawString("" + YellowBlockArray[x].health, x * 100 + 20, 102 + 20);
//            }
//        }


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
    public void restart(){
        render();
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
        canvas.addMouseListener(this);
        System.out.println("DONE graphic setup");
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        //stick movement(only lateral because I don't want it moving up or down)
        System.out.println(e.getKeyCode());
        if(e.getKeyCode() == 37){//left
            stick.dx = -25;
        }
        if(e.getKeyCode() == 39){//right
            stick.dx = 25;
        }

        //BasketBallJesus Movement
        if(e.getKeyCode() == 87){ //up
            basketBallJesus.dy = -20;
        }
        if(e.getKeyCode() == 83){//down
            basketBallJesus.dy = 20;
        }
        if(e.getKeyCode() == 65){//left
            basketBallJesus.dx = -20;
        }
        if(e.getKeyCode() == 68){//right
            basketBallJesus.dx = 20;
        }

        //Makes BasketBall Jesus get bigger
        if(e.getKeyCode() == 69){ //bigger
            basketBallJesus.height = basketBallJesus.height + 5;
            basketBallJesus.width = basketBallJesus.width + 5;
        }
        if(e.getKeyCode() == 81){ //bigger
            basketBallJesus.height = basketBallJesus.height - 5;
            basketBallJesus.width = basketBallJesus.width - 5;
        }

//        if(ball.dx == 0 && ball.dy ==0 && e.getKeyCode() == 82){ //restart
//            setUpGraphics();
//
//        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

        //Stick stops movement
        if(e.getKeyCode() == 37){
            stick.dx = 0;
        }
        if(e.getKeyCode() == 39){
            stick.dx = 0;
        }

        //BasketBallJesus stops movement
        if(e.getKeyCode() == 87){ //up
            basketBallJesus.dy = 0;
        }
        if(e.getKeyCode() == 83){//down
            basketBallJesus.dy = 0;
        }
        if(e.getKeyCode() == 65){//left
            basketBallJesus.dx = 0;
        }
        if(e.getKeyCode() == 68){//right
            basketBallJesus.dx = 0;
        }

        if(e.getKeyCode() == 82){ //restart

            render();
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(basketBallJesus.rect.contains(e.getPoint())){
            int randX = (int)((Math.random()*400)+150);
            int randY = (int)((Math.random()*600)+100);

            ballCount++;
//            ballImage = Toolkit.getDefaultToolkit().getImage("Wesley.png");

        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
