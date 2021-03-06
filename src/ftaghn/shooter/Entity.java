package ftaghn.shooter;


//import game.test.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.MediaPlayer;

public abstract class Entity {

  public int x;
  public int y;
  public String type;
  public Context mContext;
  public Rect bounds, detectArea;
  public static boolean debugMode=false;
  public static boolean silentMode=false;
  public int shields;
  public static int MAX_SHIELDS;
  public MediaPlayer mp;
  boolean detected;
  public boolean 
  moveLeft,
  moveRight,
  moveUp,
  moveDown,
  action;

  public Entity( int x, int y, String type, int width, int height, Context mContext) 
  {    
    this.x = x;
    this.y = y;
    this.type = type;
    this.mContext = mContext;
    bounds = new Rect(x, y, width, height);
    if (type=="zerg") { detectArea = new Rect(x,y,width,300); }

    if (this instanceof Ship)
    {
      mp = MediaPlayer.create(mContext, R.raw.baseshot);
    }
    if (this instanceof Enemy)
    {
      mp = MediaPlayer.create(mContext, R.raw.lazer);
    }
    if (this instanceof Boss)
    {
      mp = MediaPlayer.create(mContext, R.raw.sb_blast);
    }
    if (this instanceof Backgnd)
    {
      mp = MediaPlayer.create(mContext, R.raw.paranoid);
    }
  }

  public void stop()
  {
    if (mp!=null)
    {
    mp.stop();
    mp.release();
    }
  }
  
  public int getShields() 
  {
    return shields;
  }
  public void setShields(int i) 
  {
    shields = i;
  }
  public void addShields(int i) 
  {
    shields += i;
    if (shields > MAX_SHIELDS)
      shields = MAX_SHIELDS;
  }


  public void update() {  }
  public void paint(Canvas g) {  }  

  public boolean isCollisionWith(Entity other)  
  {
    return  Rect.intersects(bounds, other.bounds);
  }

  public boolean isDetected(Entity other)
  {
    if (detectArea!=null)
      if (Rect.intersects(detectArea, other.bounds))
        return true;
    return false;
  }

  public boolean collidedWith(Entity entity) 
  {
    return false;
  }
}
