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
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

public class Render extends GLSurfaceView implements Renderer {

	
	/** Instancia del techo */
	private Techo techo;
	
	/** Instancia de la casa y puerta */
	private Casa casa;
	private Puerta puerta;	
	private Hojas hojas;
	private Tronco tronco;
	
	private int fogFilter = 0;			//Filtro para definir cu�l se va a utilizar
	private int fogMode[]= { 			//Opciones de fog
			GL10.GL_EXP, 
			GL10.GL_EXP2, 
			GL10.GL_LINEAR 
						};		
	private float[] fogColor = {0.5f, 0.5f, 0.5f, 1.0f};
	private FloatBuffer fogColorBuffer;	

	/* Valores de rotaci�n */
	private float xrot;					//X Rotation
	private float yrot;					//Y Rotation

	/* Valores de velocidad de rotaci�n */
	private float xspeed;				
	private float yspeed;				
	
	/* Move values */
	private float xmov;
	private float ymov;
	
	
	/** Booleano para definir activaci�n de luz */
	private boolean light = false;

	/* 
	* Valores iniciales para la posici�n de luz
	 */
	private float[] lightAmbient = {0.5f, 0.5f, 0.5f, 1.0f};
	private float[] lightDiffuse = {1.0f, 1.0f, 1.0f, 1.0f};
	private float[] lightPosition = {0.0f, 0.0f, 2.0f, 1.0f};
		
	/* Buffers para los valores de la luz */
	private FloatBuffer lightAmbientBuffer;
	private FloatBuffer lightDiffuseBuffer;
	private FloatBuffer lightPositionBuffer;
	
	private float oldX;
    private float oldY;
	private final float TOUCH_SCALE = 0.01f;		//Define una escala de crecimiento para la modificaci�n
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
	
	/** Media player para manejar el audio */
	private final MediaPlayer mpOpen;
	private final MediaPlayer mpClose;
	
	/** �ngulo para manejar la rotaci�n  */
	private float rquad=0; 
	
	private Context context;

	
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
		
		//Build the new Buffer ( NEW )
		byteBuf = ByteBuffer.allocateDirect(fogColor.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		fogColorBuffer = byteBuf.asFloatBuffer();
		fogColorBuffer.put(fogColor);
		fogColorBuffer.position(0);						
		
		
		techo = new Techo();
		casa = new Casa();
		hojas = new Hojas();
		tronco = new Tronco();
		puerta = new Puerta();
	}
	
	/**
	 * Se inicia la superficie
	 */
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {	
		//Luz, luz, luz
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, lightAmbientBuffer);		//Ambient Light 
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightDiffuseBuffer);		//Diffuse Light 
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPositionBuffer);	//Position The Light 
		gl.glEnable(GL10.GL_LIGHT0);																
		
		gl.glDisable(GL10.GL_DITHER);
		gl.glEnable(GL10.GL_TEXTURE_2D);		
		gl.glShadeModel(GL10.GL_SMOOTH); 			
		gl.glClearColor(0.45f, 0.45f, 0.45f, 1.0f); 	//Foggy Background
		gl.glClearDepthf(1.0f); 					
		gl.glEnable(GL10.GL_DEPTH_TEST); 			
		gl.glDepthFunc(GL10.GL_LEQUAL); 			
		
		//The Fog
		gl.glFogf(GL10.GL_FOG_MODE, fogMode[fogFilter]);	//Fog Mode 
		gl.glFogfv(GL10.GL_FOG_COLOR, fogColorBuffer);		//Fog Color
		gl.glFogf(GL10.GL_FOG_DENSITY, 0.05f);				//How Dense 
		gl.glHint(GL10.GL_FOG_HINT, GL10.GL_DONT_CARE);		
		gl.glFogf(GL10.GL_FOG_START, 1.0f);					
		gl.glFogf(GL10.GL_FOG_END, 5.0f);					
		gl.glEnable(GL10.GL_FOG);							//Enables GL_FOG ( NEW )		

		
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST); 
		
		casa.loadGLTexture(gl, this.context);
	}
	
	/**
	 * Dibujamos cada frame
	 */
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);	
		gl.glLoadIdentity();					//Reset The Current Modelview Matrix
		
		if(light) {
			gl.glEnable(GL10.GL_LIGHTING);
		} else {
			gl.glDisable(GL10.GL_LIGHTING);
		}
		
		//Set Fog Mode 
		gl.glFogf(GL10.GL_FOG_MODE, fogMode[fogFilter]);		
				
		//Se posiciona en el espacio referencia
		gl.glTranslatef(-2.0f, -1.2f, -10.0f);	
		
		if(selected == CASA)
		{
			gl.glTranslatef(xmov, ymov, 0.0f);
			
			//SCALATION
			gl.glScalef(scaleFactor, scaleFactor, scaleFactor);		
			
			//Rotacion
			gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);	//X
			gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);	//Y
		}
		
		casa.draw(gl);						
					
		//Techo
		gl.glTranslatef(0.0f, 2.0f, 0.0f);	
		techo.draw(gl);						

		gl.glTranslatef(0.0f, -2.0f, 0f);			
		if(doorState == DOOR_OPENED)
		{
			rquad = 135;
			gl.glTranslatef(0.0f, 0.0f, 1.980f);
			gl.glRotatef(rquad, 0.0f, -1.0f, 0.0f);
		}
		else if(doorState != NONE)
		{
			gl.glRotatef(0.0f, 0.0f, 1.0f, 0.0f);
		}		
		if(changeDoorState)
		{	
			switch (doorState)
			{
				case(DOOR_CLOSED):
					mpOpen.start();
					changeDoorState = false;
					doorState = DOOR_OPENED;
					break;
				case(DOOR_OPENED):
					mpClose.start();
					changeDoorState = false;
					doorState = DOOR_CLOSED;
					break;
				case(NONE):
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
			gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);	//X
			gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);	//Y
		}
		
		tronco.draw(gl);		
		
		//Hojas
		gl.glTranslatef(0.0f, 2.0f, 0.0f);
		hojas.draw(gl);
		
		
		xrot += xspeed;
		yrot  += yspeed;

	}	
	
	/**
	 * Si la superficie cambia, se actulizan valores
	 */
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if(height == 0) { 						//Prevent A Divide By Zero
			height = 1; 						
		}

		gl.glViewport(0, 0, width, height); 	
		gl.glMatrixMode(GL10.GL_PROJECTION); 	
		gl.glLoadIdentity(); 					

		
		GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);

		gl.glMatrixMode(GL10.GL_MODELVIEW); 	
		gl.glLoadIdentity(); 					
	}
	
	/* ***** Listener Events ***** */	
	/**
	* Manejo de toques
	 */
	public boolean onTouchEvent(MotionEvent event) {
		//Si hay 2 dedos, escalar
		if(event.getPointerCount() == 2)
		{	
				scaleGestureDetector.onTouchEvent(event);								
		}	
		//Si hay 3, mover
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
			//Si solo hay uno, ver donde toc�
			//
			float x = event.getX();
	        float y = event.getY();
	        
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
	        		{
	        			fogFilter += 1; 	//Aumentar el filtro del fog					
						if(fogFilter > 2) {
							fogFilter = 0; 	
						}
	        		}
	        		else
	        		{
	        			changeDoorState = true;
	        		}
	        	}	        	
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
     scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 10.0f));

     invalidate();
     return true;
   }
 }
	
	

}
