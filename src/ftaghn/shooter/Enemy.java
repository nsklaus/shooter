package ftaghn.shooter;

//import ftaghn.shooter.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


public class Enemy extends Entity{

  private Bitmap[] shipSprite;
  private Bitmap[] explodeSprite;
  private Paint mBitmapPaint = new Paint();
  int sizeX=21;
  int sizeY=27;
  int speed=2;
  Weapon shoot;
  int shotTimer;
  boolean alive=true;
  int fc;

  public Enemy(int x, int y, String type, Context mContext)
  {
    super(x, y, type, x+21, y+27, mContext);
    shipSprite = new Bitmap[]{
        BitmapFactory.decodeResource(mContext.getResources(),R.drawable.enemy3),
        BitmapFactory.decodeResource(mContext.getResources(),R.drawable.sb_shipleft),
        BitmapFactory.decodeResource(mContext.getResources(),R.drawable.sb_shipright)
    };
    explodeSprite = new Bitmap[]{
        BitmapFactory.decodeResource(mContext.getResources(),R.drawable.boom1),
        BitmapFactory.decodeResource(mContext.getResources(),R.drawable.boom2),
        BitmapFactory.decodeResource(mContext.getResources(),R.drawable.boom3),
        BitmapFactory.decodeResource(mContext.getResources(),R.drawable.boom4),
        BitmapFactory.decodeResource(mContext.getResources(),R.drawable.boom5),
        BitmapFactory.decodeResource(mContext.getResources(),R.drawable.boom6),
        BitmapFactory.decodeResource(mContext.getResources(),R.drawable.boom7),
        BitmapFactory.decodeResource(mContext.getResources(),R.drawable.boom8),
        BitmapFactory.decodeResource(mContext.getResources(),R.drawable.boom9),
        BitmapFactory.decodeResource(mContext.getResources(),R.drawable.boom10),
        BitmapFactory.decodeResource(mContext.getResources(),R.drawable.boom11),
        BitmapFactory.decodeResource(mContext.getResources(),R.drawable.boom12),
        BitmapFactory.decodeResource(mContext.getResources(),R.drawable.boom13),
    };
  }
  public void update()
  {

    y+=speed;
    bounds.left=x;
    bounds.right=x+sizeX;
    bounds.top=y;
    bounds.bottom=y+sizeY;

    detectArea.left=x;
    detectArea.right=x+sizeX;
    detectArea.top=y;
    detectArea.bottom=y+300;
    shotTimer++;
    //myGame.processCollisions(this);
    if (y>420)
    {
      myGame.entities.remove(this);
      myGame.enemyCount-=1;
    }
    if (detected)
    {
      if (shotTimer>25)
      {
        //if (!silentMode)mp.start();
        myGame.entities.add(shoot = new Weapon(x-7, y+30, "enemyshoot", this, mContext));
        shotTimer=0;
      }
    }
  }

  public boolean collidedWith(Entity entity) {
    //setShields(shields-=10);
    if (entity instanceof Weapon && entity.type=="enemyshoot")
      return false;
    if (!(entity instanceof Ship) && !(entity instanceof Boss))
    {
      myGame.entities.remove(entity);
      alive=false;
      //myGame.entities.remove(this);    
      myGame.enemyCount-=1;
      myGame.progress+=1;
    }
    return true;
  }


  public void paint(Canvas g)
  {
    if (alive)
      g.drawBitmap(shipSprite[0], x, y, mBitmapPaint);
    if (!alive)
    {
      if (fc<12)
      {
        fc++;
        g.drawBitmap(explodeSprite[fc], x, y, mBitmapPaint);
      }
      if (fc==12)
      myGame.entities.remove(this); 
    }
    if (debugMode)
    {
      mBitmapPaint.setStyle(Paint.Style.STROKE);
      mBitmapPaint.setColor(Color.WHITE);
      g.drawRect(bounds, mBitmapPaint);
      g.drawRect(detectArea, mBitmapPaint);
    }
  }   
}
