package com.example.opengl2;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class Casa {
	
	/** Buffer para vertices */
	private FloatBuffer vertexBuffer;
	/** Buffer para la textura*/
	private FloatBuffer textureBuffer;
	/** Buffer para los indexes (dibujo de triángulos) */
	private ByteBuffer  indexBuffer;
	/** Buffer para las normales (luz) */
	private FloatBuffer normalBuffer;	
	
	private int[] textures = new int[1];
	
	/** 
	 * Definición de vértices. 4 por cara y 6 para enfrente (hoyo d ela puerta)
	 **/
	private float vertices[] = {
			            -1.0f, -1.0f, -1.0f,	//lower back left (0)
			            1.0f, -1.0f, -1.0f,		//lower back right (1)
			            1.0f,  1.0f, -1.0f,		//upper back right (2)
			            -1.0f, 1.0f, -1.0f,		//upper back left (3)
			            
			            -1.0f, -1.0f, -1.0f,	//lower left left (4)
			            -1.0f, -1.0f, 1.0f,		//lower left right (5)
			            -1.0f, 1.0f, 1.0f, 		//upper left right (6)
			            -1.0f, 1.0f, -1.0f,		//upper left left (7)
			            
			            1.0f, -1.0f, -1.0f,		//lower right right (8)
			            1.0f, -1.0f, 1.0f,		//lower right left (9)
			            1.0f, 1.0f, 1.0f, 		//upper right left (10)
			            1.0f, 1.0f, -1.0f,		//upper right right (11)
			            
			            -1.0f, -1.0f, -1.0f,	//upper floor left (12)
			            -1.0f, -1.0f, 1.0f,		//lower floor left (13)
			            1.0f, -1.0f, 1.0f, 		//lower floor right (14)
			            1.0f, -1.0f, -1.0f,		//upper floor right (15)
			            
			            -1.0f, -1.0f,  1.0f,	//lower front left (16)	
			            -0.35f, -1.0f, 1.0f,	//lower door left (17)
			            -0.35f, 0.25f, 1.0f,	//upper door left (18)
			            0.35f, 0.25f, 1.0f,		//upper door right (19)
			            0.35f, -1.0f, 1.0f,		//lower door right (20)			            
			            1.0f, -1.0f,  1.0f,		//lower front right (21)
			            1.0f, 0.25f, 1.0f,		//mid wall right (22)			            
			            1.0f,  1.0f,  1.0f,		//upper front right (23)
			            -1.0f,  1.0f,  1.0f,	//upper front left (24)
			            -1.0f, 0.25f, 1.0f,		//mid wall left (25)
			    							};		
	
	private float normals[] = {
			// Normales
			0.0f, 0.0f, 1.0f, 						
			0.0f, 0.0f, -1.0f, 
			0.0f, 1.0f, 0.0f, 
			0.0f, -1.0f, 0.0f, 
			
			0.0f, 0.0f, 1.0f, 
			0.0f, 0.0f, -1.0f, 
			0.0f, 1.0f, 0.0f, 
			0.0f, -1.0f, 0.0f,
			
			0.0f, 0.0f, 1.0f, 
			0.0f, 0.0f, -1.0f, 
			0.0f, 1.0f, 0.0f, 
			0.0f, -1.0f, 0.0f,
			
			0.0f, 0.0f, 1.0f, 
			0.0f, 0.0f, -1.0f, 
			0.0f, 1.0f, 0.0f, 
			0.0f, -1.0f, 0.0f,
			
			0.0f, 0.0f, 1.0f, 
			0.0f, 0.0f, -1.0f, 
			0.0f, 1.0f, 0.0f, 
			0.0f, -1.0f, 0.0f,
			
			0.0f, 0.0f, 1.0f, 
			0.0f, 0.0f, -1.0f, 
			0.0f, 1.0f, 0.0f, 
			0.0f, -1.0f, 0.0f,
								};
    
    /** Definición del mapeo de la textura */	
	private float texture[] = {
						0.0f, 0.0f,
						1.0f, 0.0f,
						1.0f, 1.0f,
						0.0f, 1.0f,
						
						0.0f, 0.0f,
						1.0f, 0.0f,
						1.0f, 1.0f,
						0.0f, 1.0f,						
						
						1.0f, 0.0f,
						0.0f, 0.0f,
						0.0f, 1.0f,
						1.0f, 1.0f,
						
						0.0f, 1.0f,
						0.0f, 0.0f,
						1.0f, 0.0f,
						1.0f, 1.0f, 
						
						0.0f, 0.0f,
						0.325f, 0.0f,
						0.325f, 0.625f,
						0.675f, 0.625f,
						0.675f, 0.0f, 
						1.0f, 0.0f,
						1.0f, 0.625f,
						1.0f, 1.0f, 
						0.0f, 1.0f,
						0.0f, 0.625f						
			    								};
	
	/** 
     * Definición de indices, se cierran triángulos para dibujarlos
     * 
     *	*/
	private byte indices[] = {
						0, 1, 2,	0, 2, 3,
						4, 5, 6,	4, 6, 7,
						8, 9, 10, 	8, 10, 11,
						12, 13, 14,	12, 14, 15,
						
						16, 17, 18,	16, 18, 25,
						20, 21, 22,	20, 22, 19,
						24, 25, 22,	24, 22, 23
	};
	
	/**
	 * Constructor de la casa.
	 * 
	 * Inicialización de los buffers.
	 */
	public Casa() {
		//
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		
		//
		byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);
		
		//
		byteBuf = ByteBuffer.allocateDirect(normals.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		normalBuffer = byteBuf.asFloatBuffer();
		normalBuffer.put(normals);
		normalBuffer.position(0);
		
		/**/
		indexBuffer = ByteBuffer.allocateDirect(indices.length);
		indexBuffer.put(indices);
		indexBuffer.position(0);
	}
	
	/**
	 * La función que se utiliza para dibujar
	 * 
	 * @param gl - GL Context
	 */
	public void draw(GL10 gl) {		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		
		gl.glFrontFace(GL10.GL_CCW);
		
		//Point to our buffers
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2,  GL10.GL_FLOAT, 0, textureBuffer);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);
		
		//Habilitar color state
		gl.glColor4f(0.933f, 0.910f, 0.667f, 1.0f);		
		
		//Se dibujan los vertices basados en las matrices definidas anteriormente
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_BYTE, indexBuffer);
		
		//Disable the client state before leaving
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		//UNCOMMENT TO STOP TEXTURE
		//gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		//UNCOMMENT TO STOP LIGHT
		//gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
	}
	
	/**
	 * Cargar las texturas
	 * 
	 * @param gl - The GL Context
	 * @param context - The Activity context
	 */
	public void loadGLTexture(GL10 gl, Context context) {
		//Get the texture from the Android resource directory
		InputStream is = context.getResources().openRawResource(R.drawable.nehe);
		Bitmap bitmap = null;
		try {
			//BitmapFactory is an Android graphics utility for images
			bitmap = BitmapFactory.decodeStream(is);

		} finally {
			//Always clear and close
			try {
				is.close();
				is = null;
			} catch (IOException e) {
			}
		}

		//Generate one texture pointer...
		gl.glGenTextures(1, textures, 0);
		//...and bind it to our array
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		
		//Create Nearest Filtered Texture
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

		//Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
		
		//Use the Android GLUtils to specify a two-dimensional texture image from our bitmap
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		
		//Clean up
		bitmap.recycle();
	}
}
