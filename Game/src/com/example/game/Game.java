package com.example.game;

import java.util.Random;

import com.example.ball.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

public class Game extends ImageView {

	private Context mContext;
	int x = -1;
	int y = -1;
	private Handler h;
	private final int FRAME_RATE = 30;
	private float currX, currY;
	private Rect left, right;
	private Paint paint;
	private ShapeDrawable rectangle;
	private BitmapDrawable player, projectile, background, retry, button;
	private int buttonWidth = 100;
	private int height = 0;
	private int width = 0;
	private int projectileSize = 0;
	private int playerHeight = 0;
	private int playerWidth = 0;
	private int delayCoefficient = 10;
	private int speedCoefficient = 5;
	private Random rGen = new Random();
	private int projectileNum = 20;
	private boolean alive = true;
	private Projectile[] projectileArray = new Projectile[projectileNum + 1];
	private long startTime, endTime, resetTime;
	private String lifetime;
	
	public Game(Context context, AttributeSet attrs)  {

		super(context, attrs);
		mContext = context;
		h = new Handler();

		currX = 1;
		currY = 1;

		paint = new Paint();
		left = new Rect(0,0,0,0);
		right = new Rect(0,0,0,0);
		rectangle = new ShapeDrawable(new RectShape());
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		currX = event.getX();
		currY = event.getY();

		if (alive == true && currX > buttonWidth && currX < (this.getWidth() - (player.getBitmap().getWidth() + buttonWidth)))
		{
			if(currX > x)
			{
				player = (BitmapDrawable) mContext.getResources().getDrawable(R.drawable.right);
			}
			else
			{
				player = (BitmapDrawable) mContext.getResources().getDrawable(R.drawable.left);
			}
			x = (int)currX;
		}
		else if (alive == false && (Float.parseFloat(time(resetTime, endTime)) > 0.5) && currX > (25 + (this.getWidth()/2 - 500/2)) && currX < 500 + (25 + (this.getWidth()/2 - 500/2)) && currY > (this.getHeight()/2 - 200/2) && currY < 200 + (this.getHeight()/2 - 200/2))
		{
			for(int i = 0; i <= projectileNum; i++)
			{
				projectileArray[i].reset();
			}
			alive = true;
			background = (BitmapDrawable) mContext.getResources().getDrawable(R.drawable.background);
			startTime = System.currentTimeMillis();
		}
		/*
		    if (currX < buttonWidth) 
		    {
		    	if(x > buttonWidth)
		    	{
		    		if(x < buttonWidth + jump)
		    		{
		    			x = buttonWidth;
		    		}
		    		else 
		    		{	
				    	x = x - jump;
				    	player = (BitmapDrawable) mContext.getResources().getDrawable(R.drawable.left);
		    		}
		    	}
		    }
		    if (currX > this.getWidth() - buttonWidth)
		    {
		    	if(x < this.getWidth() - (buttonWidth + player.getBitmap().getWidth()))
		    	{
		    		if(x > this.getWidth() - (buttonWidth + player.getBitmap().getWidth() + jump))
		    		{
		    			x = this.getWidth() - (buttonWidth + player.getBitmap().getWidth());
		    		}
		    		else
		    		{
			    		x = x + jump;
			    		player = (BitmapDrawable) mContext.getResources().getDrawable(R.drawable.right);
		    		}
		    	}
		    }
		 */
		//invalidate();
		//return super.onTouchEvent(event);
		return true;
	}


	private Runnable r = new Runnable() {
		@Override
		public void run() {
			invalidate();
		}
	};

	protected void onDraw(Canvas c) {
		if (x < 0 && y < 0)
		{
			player = (BitmapDrawable) mContext.getResources().getDrawable(R.drawable.left);
			projectile = (BitmapDrawable) mContext.getResources().getDrawable(R.drawable.projectile);
			background = (BitmapDrawable) mContext.getResources().getDrawable(R.drawable.background);
			retry = (BitmapDrawable) mContext.getResources().getDrawable(R.drawable.retry);
			button = (BitmapDrawable) mContext.getResources().getDrawable(R.drawable.button);
			x = this.getHeight()/2;
			y = this.getHeight() - player.getBitmap().getHeight();
			height = this.getHeight();
			width = this.getWidth();
			projectileSize = projectile.getBitmap().getHeight();
			playerHeight = player.getBitmap().getHeight();
			playerWidth = player.getBitmap().getWidth();
			startTime = System.currentTimeMillis();
			
			for(int i = 0; i <= projectileNum; i++)
			{
				projectileArray[i] = new Projectile((buttonWidth + 25 + rGen.nextInt(width - (buttonWidth*2 + 50))), ((rGen.nextInt(delayCoefficient)*-100) - projectileSize), rGen.nextInt(speedCoefficient + 10));
			}
			
		}
		c.drawBitmap(background.getBitmap(), buttonWidth, 0, null);
		c.drawBitmap(player.getBitmap(), x, y, null);

		//y = this.getHeight() - player.getBitmap().getHeight();
		
		for(int i = 0; i <= projectileNum; i++)
		{
			if(alive == true)
			{
				projectileArray[i].move();
				c.drawBitmap(projectile.getBitmap(),projectileArray[i].getX(), projectileArray[i].getY(), null);
			}
			else
			{
				c.drawBitmap(projectile.getBitmap(),projectileArray[i].getX(), projectileArray[i].getY(), null);
				c.drawBitmap(retry.getBitmap(), (25 + (this.getWidth()/2 - 500/2)), (this.getHeight()/2 - 200/2), null);
				c.drawBitmap(button.getBitmap(), (25 + (this.getWidth()/2 - 500/2)), (this.getHeight()/2 - 300), null);
				paint.setColor(Color.RED); 
				paint.setTextSize(70); 
				//lifetime = "999.999";
				c.drawText("Time: "+ lifetime, (30 + (this.getWidth()/2 - 500/2)), (this.getHeight()/2 - 220), paint);
				c.drawText("seconds.", (180 + (this.getWidth()/2 - 500/2)), (this.getHeight()/2 - 140), paint);
				resetTime = System.currentTimeMillis();
			}
			if (projectileArray[i].collision(x) == true  && alive == true)
			{
				endTime = System.currentTimeMillis();
				lifetime = time(endTime, startTime);
				alive = false;
				background = (BitmapDrawable) mContext.getResources().getDrawable(R.drawable.backgroundloss);
				for(int j = 0; j <= projectileNum; j++)
				{
					projectileArray[j].setSpeed(0);
				}
				//c.drawBitmap(background.getBitmap(), buttonWidth, 0, null);
				//c.drawBitmap(retry.getBitmap(), (25 + (this.getWidth()/2 - 500/2)), (this.getHeight()/2 - 200/2), null);
			}
		}

		rectangle.getPaint().setColor(Color.BLACK);
		rectangle.getPaint().setStyle(Paint.Style.FILL_AND_STROKE);
		rectangle.getPaint().setStrokeWidth(3);

		left.set(0, 0, buttonWidth, (this.getHeight()));
		rectangle.setBounds(left);
		left = rectangle.getBounds();
		rectangle.draw(c);

		right.set(this.getWidth() - buttonWidth, 0, this.getWidth(), (this.getHeight()));
		rectangle.setBounds(right);
		right = rectangle.getBounds();
		rectangle.draw(c);
		
		//heldx = this.getWidth() - buttonWidth*2;
		//heldy = this.getHeight();

		//paint.setColor(Color.WHITE); 
		//paint.setTextSize(20); 
		//c.drawText("L:" + lCount, 0, 50, paint); 
		//c.drawText("R:" + rCount, this.getwidth() - 100, 50, paint); 
		//c.drawText("Whoop:" + heldx + heldy, 0, 50, paint);
		//c.drawText("Coll:" + collision, 0, 50, paint);
		
		h.postDelayed(r, FRAME_RATE);
	}

	private class Projectile
	{
		private int projectilePositionX;
		private int projectilePositionY;
		private int projectileSpeed;

		public Projectile(int x, int y, int s)
		{
			projectilePositionX = x;
			projectilePositionY = y;
			projectileSpeed = s;
		}

		public int getX() { return projectilePositionX; }
		public int getY() { return projectilePositionY; }
		public int getSpeed() { return projectileSpeed; }

		public void setX(int x) { projectilePositionX = x; }
		public void setY(int y) { projectilePositionY = y; }
		public void setSpeed(int s) { projectileSpeed = s; }

		public void move()
		{
			if(getY() > height)
			{
				setY(-projectileSize);
				setX(rGen.nextInt(width));
			}
			setY(getY() + getSpeed());
		}

		public void reset()
		{
			setY((rGen.nextInt(delayCoefficient)*-1000) - projectileSize);
			setX(rGen.nextInt(width));
			setSpeed(rGen.nextInt(speedCoefficient + 10));
		}

		public boolean collision(int x)
		{
			if(getY() > (height - playerHeight) && getY() < (height - playerHeight/2) && getX() >= x - (playerWidth/2) && getX() <= x + (playerWidth/2) && getX() + (playerWidth/2) >= x - (playerWidth/2) && getX() - (playerWidth/2) <= x + (playerWidth/2))
			{
				return true;
			}
			else
				return false;
		}
	}
	private String time(long end, long start)
	{
		String time = "";
		StringBuilder sb = new StringBuilder();
		float seconds = 0;

		seconds = ((float)(end - start)/1000);

		sb.append(seconds);
		time = sb.toString();
		return time;
	}
}