package ftaghn.shooter;

import java.util.ArrayList;
//import game.test.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class myGame extends ArcadeGame 
{
  public static final String NAME = "stuff";	
  private static final long UPDATE_DELAY = 40;
  private Context mContext;
  public static ArrayList<Entity> entities;
  private Paint mTextPaint = new Paint();
  private Paint mBitmapPaint = new Paint();
  private Paint shieldBarPaint = new Paint();
  private Paint laserBarPaint = new Paint();
  private Paint progressBarPaint = new Paint();
  //public static boolean left,right,up,down,action;
  public boolean bossArrived;
  public static int width, height;
  public static int progress; // 234->314
  public static int weaponHeat=125; // 125->205
  public static int shipX, shipY;
  public static boolean ingame, title, win, gameover;
  private Rect  inputUp,inputDown,inputLeft,inputRight,inputButton1,inputButton2;
  private Bitmap padbg,item1,item2;
  public int screenWidth, screenHeight;
  public static boolean up, down, left, right, action1,action2;
  public static int eX, eY;
  private Paint textPaint = new Paint();
  public Ship xyz;
  //public Backgnd rrr;
  public static int enemyCount;
  Bitmap dash;
  Bitmap lost;
  Bitmap logo;

  public myGame(Context context)
  {
    super(context);
    mContext = context;
    super.setUpdatePeriod(UPDATE_DELAY);
  }

  public myGame(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    mContext = context;
    super.setUpdatePeriod(UPDATE_DELAY);
  }
/*
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event)
  {
    if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT)  { left   = true; } 
    if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) { right  = true; } 
    if (keyCode == KeyEvent.KEYCODE_DPAD_UP)    { up     = true; } 
    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN)  { down   = true; } 
    if (keyCode == KeyEvent.KEYCODE_SPACE)      { action = true; }
    if (keyCode == KeyEvent.KEYCODE_U) 
    { 
      System.out.printf("enemyCount=[%d]\n",enemyCount); 
      System.out.printf("progress=[%d]\n",progress);
      System.out.printf("width=[%d]\n height=[%d]\n",width,height);
      Entity.debugMode=!Entity.debugMode;
    } 
    if (keyCode == KeyEvent.KEYCODE_R)  { GameOver(); }
    if (keyCode == KeyEvent.KEYCODE_S)  { if (!ingame)newGame(); }
    if (keyCode == KeyEvent.KEYCODE_M)  { Entity.silentMode=!Entity.silentMode; }
    if (keyCode == KeyEvent.KEYCODE_Q)  { System.exit(0); }
    return true;
  }

  @Override
  public boolean onKeyUp(int keyCode, KeyEvent event)
  {
    if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT)  { left   = false;  }
    if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) { right  = false;  }
    if (keyCode == KeyEvent.KEYCODE_DPAD_UP)    { up     = false;  }
    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN)  { down   = false;  }
    if (keyCode == KeyEvent.KEYCODE_SPACE)      { action = false;  }
    return true;
  }
  */
  @Override
  protected void onDraw(Canvas canvas) 
  {
    super.dispatchDraw(canvas);
    paint(canvas);
  }	

  public void initialize()
  {
    width   = getWidth();
    height  = getHeight();
    dash    = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.controls);
    lost    = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.gameover);
    logo    = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.title);    
    title   = true;
    // virtual controller
    screenWidth=350;
    screenHeight=490;
    inputUp       = new Rect(80,screenHeight-65,130,screenHeight-35);
    inputDown     = new Rect(80,screenHeight-25,130,screenHeight);
    inputLeft     = new Rect(35, screenHeight-45,70,screenHeight-15);
    inputRight    = new Rect(140,screenHeight-45,175,screenHeight-15);
    inputButton1  = new Rect(screenWidth-130,screenHeight-45,screenWidth-90,screenHeight-10);
    inputButton2  = new Rect(screenWidth-80,screenHeight-45,screenWidth-40,screenHeight-10);
  }
  
  @Override
  public boolean onTouchEvent(MotionEvent event) 
  {
    int action = event.getAction();
    eX = (int) event.getX();
    eY = (int) event.getY();

    switch (action) {
    case MotionEvent.ACTION_UP:
      up     = false;
      down   = false;
      left   = false;
      right  = false;
      action1= false;
      action2= false;
      break;
    case MotionEvent.ACTION_DOWN:
      eX = (int) event.getX();
      eY = (int) event.getY();
      if (inputUp.contains(eX, eY)){up     = true; }
      if (inputDown.contains(eX, eY)){down   = true; }
      if (inputLeft.contains(eX, eY)){left   = true; }
      if (inputRight.contains(eX, eY)){right  = true; }
      if (!title && inputButton1.contains(eX,eY)){action1=true;}
      if (inputButton2.contains(eX,eY)){Entity.debugMode=!Entity.debugMode;}
      if (title && inputButton1.contains(eX,eY)){newGame();}
      if (gameover && inputButton1.contains(eX,eY)){newGame();}
      break;
    }
    return true;
  }

  public void newGame()
  {    
    // health Bar Energy
    shieldBarPaint.setARGB(255, 0, 255, 255);
    shieldBarPaint.setStyle(Paint.Style.FILL);
    // Laser Bar Energy
    laserBarPaint.setARGB(255, 0, 255, 96);
    laserBarPaint.setStyle(Paint.Style.FILL);
    // progress Bar Energy
    progressBarPaint.setARGB(255, 0, 255, 96);
    progressBarPaint.setStyle(Paint.Style.FILL);
    enemyCount=0;       // reset count
    progress=234;       // reset progress
    bossArrived=false;  // reset boss coming
    entities = new ArrayList<Entity>();
    entities.add(new Backgnd(0, 0, "bg", mContext));
    genEnemies();
    entities.add(xyz = new Ship(130, 300, "hero",  mContext));
    title = false;
    gameover=false;
    ingame=true;
  }

  public int rndX()
  {
    int test = ((int)Math.round(Math.random()*width-30));
    return test;
  }
  public int rndY()
  {
    int test = ((int)Math.round(Math.random()*height-430));
    //test-=430;
    return test;
  }

  public void genEnemies()
  {
    enemyCount+=1;
    entities.add(new Enemy(rndX() ,  rndY(), "zerg",  mContext));
  }

  public void genBoss()
  {
    //bossArrived=true;
    entities.add(new Boss(80 , -120, "zerg",  mContext));
    progressBarPaint.setARGB(255, 255, 0, 0);
    progressBarPaint.setStyle(Paint.Style.FILL);
    progress=234;
    bossArrived=true;
  }

  public void paint(Canvas g)
  { 
    if (title)
    {
      mTextPaint.setARGB(255, 255, 255, 255);
      String s1 = "（S）キーでスタート";
      String s2 = "（Q）キーで止める";
      g.drawBitmap(logo, 20, 100, mBitmapPaint);
      g.drawText(s1, 120, 200, mTextPaint);
      g.drawText(s2, 120, 220, mTextPaint);
      //title=false;
      //ingame=true;
      //newGame();
    }
    if (ingame)
    {
     // if (!mPlayer.isPlaying())
      //  mPlayer.start();
      for (int i = 0; i < entities.size(); i++) {
        Entity eTemp = (Entity) entities.get(i);
        if (!(eTemp instanceof Backgnd))
          processCollisions(eTemp);
        eTemp.update();
        eTemp.paint(g);
      }
      
      if (enemyCount<10 && progress<265)//312)
        genEnemies();
      if (progress>=265&&!bossArrived)
        genBoss();

      if (weaponHeat>125)
        weaponHeat--;

      if (xyz.getShields()<=18)
        GameOver();

      g.drawRect(18,419,xyz.getShields(),428,shieldBarPaint);
      g.drawRect(125,419,weaponHeat,428,laserBarPaint);
      g.drawRect(234,419,progress,428,progressBarPaint);
      g.drawBitmap(dash, -4, 417, mBitmapPaint);
    }
    if (gameover) 
    {
      //mPlayer.release();
      mTextPaint.setARGB(255, 255, 255, 255);
      String s1 = "（S）キーでリスタート";
      String s2 = "（Q）キーで止める";
      g.drawBitmap(lost, 35, 100, mBitmapPaint);
      g.drawText(s1, 120, 170, mTextPaint);
      g.drawText(s2, 120, 190, mTextPaint);
    }
    
    textPaint.setARGB(255, 255, 0, 255);
    g.drawRect(inputUp,textPaint );
    g.drawRect(inputDown,textPaint );
    g.drawRect(inputLeft, textPaint);
    g.drawRect(inputRight, textPaint);
    g.drawRect(inputButton1, textPaint);
    g.drawRect(inputButton2, textPaint);
    
  }

  public static boolean processCollisions(Entity e1) { 

    for (int i = 0; i < entities.size(); i++) {
      Entity eTemp = (Entity) entities.get(i);
      if (e1 != eTemp && !(eTemp instanceof Backgnd)) {
        if (eTemp.isCollisionWith(e1))
          return e1.collidedWith(eTemp);

        if (((e1 instanceof Enemy || e1 instanceof Boss) && eTemp instanceof Ship))
        {
          if (e1.isDetected(eTemp)){
            e1.detected=true;            
          }
          else
            e1.detected=false;
        }
      }        
    }
    return false;
  }

  public void GameOver() 
  { 
    ingame   = false;
    gameover = true;
    for (int i = 0; i < entities.size(); i++) {
      Entity eTemp = (Entity) entities.get(i);
      eTemp.stop();
      entities.remove(eTemp);
      eTemp=null;
    } 
    entities=null;
  }

  @Override
  protected void updatePhysics() {	}

  @Override
  protected boolean gameOver() { return ingame; }

  @Override
  protected long getScore() { return 0; }
}
