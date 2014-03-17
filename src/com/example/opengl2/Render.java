package com.example.opengl2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.Button;
import android.widget.ToggleButton;

public class Render extends GLSurfaceView implements Renderer {


	/** Triangle instance /
	private Triangle triangle;
	/** Square instance /
	private Square square;
	
	/** Pyramid instance */
	private Techo techo;
	/** Cube instance */
	private Casa casa;
	private Puerta puerta;
	
	private Hojas hojas;
	private Tronco tronco;
	

	/* Rotation values */
	private float xrot;					//X Rotation
	private float yrot;					//Y Rotation

	/* Rotation speed values */
	private float xspeed;				//X Rotation Speed ( NEW )
	private float yspeed;				//Y Rotation Speed ( NEW )
	
	/* Move values */
	private float xmov;
	private float ymov;
	

	
	private float z = -5.0f;			//Depth Into The Screen ( NEW )
	
	private int filter = 0;				//Which texture filter? ( NEW )
	
	/** Is light enabled ( NEW ) */
	private boolean light = false;

	/* 
	 * The initial light values for ambient and diffuse
	 * as well as the light position ( NEW ) 
	 */
	private float[] lightAmbient = {0.5f, 0.5f, 0.5f, 1.0f};
	private float[] lightDiffuse = {1.0f, 1.0f, 1.0f, 1.0f};
	private float[] lightPosition = {0.0f, 0.0f, 2.0f, 1.0f};
		
	/* The buffers for our light values ( NEW ) */
	private FloatBuffer lightAmbientBuffer;
	private FloatBuffer lightDiffuseBuffer;
	private FloatBuffer lightPositionBuffer;
	
	/*
	 * These variables store the previous X and Y
	 * values as well as a fix touch scale factor.
	 * These are necessary for the rotation transformation
	 * added to this lesson, based on the screen touches. ( NEW )
	 */
	private float oldX;
    private float oldY;
	private final float TOUCH_SCALE = 0.01f;		//Proved to be good for normal rotation ( NEW )
	private final int NONE = -1;
	private final int ARBOL = 0;
	private final int CASA = 1;
	private int selected = NONE;
	
	private final int DOOR_CLOSED = 7;
	private final int DOOR_OPENED = 8;
	private int doorState = NONE;
	private boolean changeDoorState = false;
	
	private ScaleGestureDetector scaleGestureDetector;
	private float scaleFactor = 1.0f;
	
	private final MediaPlayer mpOpen;
	private final MediaPlayer mpClose;
	
	/** Angle For The Cube */
	private float rquad=0; 
	private float zquad=0;
	
	private Context context;
	/**
	 * Instance the Triangle and Square objects
	 */
	public Render(Context context) {
		super(context);
		this.context = context;
		this.setRenderer(this);
		this.requestFocus();
		this.setFocusableInTouchMode(true);
		scaleGestureDetector = new ScaleGestureDetector(context,
		        new ScaleListener());
		
		mpOpen = MediaPlayer.create(context, R.raw.open);
		mpClose = MediaPlayer.create(context, R.raw.close);
		//
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(lightAmbient.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		lightAmbientBuffer = byteBuf.asFloatBuffer();
		lightAmbientBuffer.put(lightAmbient);
		lightAmbientBuffer.position(0);
		
		byteBuf = ByteBuffer.allocateDirect(lightDiffuse.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		lightDiffuseBuffer = byteBuf.asFloatBuffer();
		lightDiffuseBuffer.put(lightDiffuse);
		lightDiffuseBuffer.position(0);
		
		byteBuf = ByteBuffer.allocateDirect(lightPosition.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		lightPositionBuffer = byteBuf.asFloatBuffer();
		lightPositionBuffer.put(lightPosition);
		lightPositionBuffer.position(0);
		
		
		
		
		techo = new Techo();
		casa = new Casa();
		hojas = new Hojas();
		tronco = new Tronco();
		puerta = new Puerta();
	}
	
	/**
	 * The Surface is created/init()
	 */
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {	
		//And there'll be light!
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, lightAmbientBuffer);		//Setup The Ambient Light ( NEW )
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightDiffuseBuffer);		//Setup The Diffuse Light ( NEW )
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPositionBuffer);	//Position The Light ( NEW )
		gl.glEnable(GL10.GL_LIGHT0);											//Enable Light 0 ( NEW )					
		
		gl.glDisable(GL10.GL_DITHER);
		gl.glEnable(GL10.GL_TEXTURE_2D);		
		gl.glShadeModel(GL10.GL_SMOOTH); 			//Enable Smooth Shading
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); 	//Black Background
		gl.glClearDepthf(1.0f); 					//Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); 			//Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); 			//The Type Of Depth Testing To Do
		
		//Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST); 
		
		casa.loadGLTexture(gl, this.context);
	}
	
	/**
	 * Here we do our drawing
	 */
	public void onDrawFrame(GL10 gl) {
		//Clear Screen And Depth Buffer
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);	
		gl.glLoadIdentity();					//Reset The Current Modelview Matrix
		
		if(light) {
			gl.glEnable(GL10.GL_LIGHTING);
		} else {
			gl.glDisable(GL10.GL_LIGHTING);
		}
				
		/*
		 * Minor changes to the original tutorial
		 * 
		 * Instead of drawing our objects here,
		 * we fire their own drawing methods on
		 * the current instance
		 */
		gl.glTranslatef(-2.0f, -1.2f, -10.0f);	//Move down 1.2 Unit And Into The Screen 6.0
		//gl.glRotatef(rquad, 1.0f, 1.0f, 0.0f);
		
		if(selected == CASA)
		{
			gl.glTranslatef(xmov, ymov, 0.0f);
			
			//SCALATION
			gl.glScalef(scaleFactor, scaleFactor, scaleFactor);		
			
			//Rotate around the axis based on the rotation matrix (rotation, x, y, z)
			gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);	//X
			gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);	//Y
		}
		
		casa.draw(gl);						//Draw the square
				
		//Reset Modelview
		//gl.glLoadIdentity();
		//gl.glTranslatef(0.0f, 0.0f, 0.0f);
		//puerta.draw(gl);
		
		//Techo
		gl.glTranslatef(0.0f, 2.0f, 0.0f);//-2.0f, 0.8f, -10.0f);		
		//gl.glRotatef(rtri, 0.0f, 1.0f, 0.0f);
		techo.draw(gl);						//Draw the triangle
		
		
		//gl.glTranslatef(-2.0f, -1.2f, -10.0f);//-2.0f, -1.2f, -10.0f);
		gl.glTranslatef(0.0f, -2.0f, 0f);
		
		
		if(doorState == DOOR_OPENED)
		{
			/*if(rquad < 135)
				rquad += 5.0f;
			if(zquad < 1.980f)
				zquad += 0.740f;*/
			rquad = 135;
			gl.glTranslatef(0.0f, 0.0f, 1.980f);// 1.980f);
			gl.glRotatef(rquad, 0.0f, -1.0f, 0.0f);
			//gl.glTranslatef(-0.7f, -2.0f, 0.0f);
		}
		else if(doorState != NONE)
		{
			/*if(rquad > 0)
				rquad -= 5.0f;
			if(zquad > 0)
				zquad -= 0.740f;*/
			//gl.glTranslatef(-0.1f, -0.0f, 0.0f);
			//rquad=0;
			gl.glRotatef(0.0f, 0.0f, 1.0f, 0.0f);
			//gl.glTranslatef(0.0f, -2.0f, 0.0f);
		}		
		if(changeDoorState)
		{
			System.out.println("cambio de estado, actual:" + doorState);
			
			switch (doorState)
			{
				case(DOOR_CLOSED):
					//gl.glTranslatef(-0.5f, 0.0f, 0.0f);
					mpOpen.start();
					changeDoorState = false;
					doorState = DOOR_OPENED;
					break;
				case(DOOR_OPENED):
					//gl.glTranslatef(0.5f,  0.0f, 0.0f);
					mpClose.start();
					changeDoorState = false;
					doorState = DOOR_CLOSED;
					break;
				case(NONE):
					//gl.glTranslatef(0.5f,  0.0f, 0.0f);
					mpOpen.start();
					changeDoorState = false;
					doorState = DOOR_OPENED;
					break;
			}

		}
		
		puerta.draw(gl);
		
		//Reset
		gl.glLoadIdentity();		
		
		//Tronco
		gl.glTranslatef(2.0f, -1.2f, -10.0f);
		
		if(selected == ARBOL)
		{
			gl.glTranslatef(xmov, ymov, 0.0f);		
			//	SCALATION
			gl.glScalef(scaleFactor, scaleFactor, scaleFactor);
			//Rotate around the axis based on the rotation matrix (rotation, x, y, z)
			gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);	//X
			gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);	//Y
		}
		
		//gl.glRotatef(rquad, 1.0f, 1.0f, 0.0f);
		//gl.glScalef(scaleFactor, scaleFactor, scaleFactor);
		tronco.draw(gl);		
		
		//Hojas
		gl.glTranslatef(0.0f, 2.0f, 0.0f);
		hojas.draw(gl);
		
		
		xrot += xspeed;
		yrot  += yspeed;
		
		//rtri +=0.5f;
		//rquad += 1.5f;
	}	
	
	/**
	 * If the surface changes, reset the view
	 */
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if(height == 0) { 						//Prevent A Divide By Zero By
			height = 1; 						//Making Height Equal One
		}

		gl.glViewport(0, 0, width, height); 	//Reset The Current Viewport
		gl.glMatrixMode(GL10.GL_PROJECTION); 	//Select The Projection Matrix
		gl.glLoadIdentity(); 					//Reset The Projection Matrix

		//Calculate The Aspect Ratio Of The Window
		GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);

		gl.glMatrixMode(GL10.GL_MODELVIEW); 	//Select The Modelview Matrix
		gl.glLoadIdentity(); 					//Reset The Modelview Matrix
	}
	
	/* ***** Listener Events ***** */	
	/**
	 * Override the touch screen listener.
	 * 
	 * React to moves and presses on the touchscreen.
	 */
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getPointerCount() == 2)
		{	
				scaleGestureDetector.onTouchEvent(event);								
		}	
		else if(event.getPointerCount() == 3)
		{
			if(event.getAction() == MotionEvent.ACTION_MOVE)
			{
				float x = event.getX();
		        float y = event.getY();
				
				float dx = x - oldX;
				float dy = y - oldY;
				
				ymov -= dy*TOUCH_SCALE;
				xmov += dx*TOUCH_SCALE;
				
				oldX = x;
				oldY = y;
			}	
		}		
		else
		{
			//
			float x = event.getX();
	        float y = event.getY();
	        
	        //A press on the screen
	        if(event.getAction() == MotionEvent.ACTION_UP) {
	        	int leftArea = this.getWidth() / 2;
	        	int upperArea = this.getHeight() / 10;
	        	int lowerArea = this.getHeight() - upperArea;
	        	if(y > lowerArea)
	        	{
	        		if(x < leftArea)
	        			selected = CASA;
	        		else
	        			selected = ARBOL;	        			
	        	}
	        	else if( y < upperArea )
	        	{
	        		if( x < leftArea)
	        			selected = NONE;
	        		else
	        		{
	        			changeDoorState = true;
	        		}
	        	}
	        	//light = !light;
	        	
	        }
	        if(event.getAction() == MotionEvent.ACTION_MOVE)
	        {
	        	float dx = x - oldX;
	        	float dy = y - oldY;
	        	
	        	xrot += dy;
	        	yrot += dx;
	        	
	        }
	        oldX = x;
	        oldY = y;
		}
        

        //We handled the event
		return true;
	}
	
	 
	/**
	 * Clase que maneja la escala para escalar objetos
	 * @author Melinton
	 *
	 */
	private class ScaleListener extends
     ScaleGestureDetector.SimpleOnScaleGestureListener {
   @Override
   public boolean onScale(ScaleGestureDetector detector) {
     scaleFactor *= detector.getScaleFactor();

     // don't let the object get too small or too large.
     scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));

     invalidate();
     return true;
   }
 }
	
	

}
