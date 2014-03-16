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
	
	/** The buffer holding the vertices */
	private FloatBuffer vertexBuffer;
	/** The buffer holding the color values */
	private FloatBuffer textureBuffer;
	/** The buffer holding the indices */
	private ByteBuffer  indexBuffer;
	/** The buffer holding the normals */
	private FloatBuffer normalBuffer;	
	
	private int[] textures = new int[1];
	
	/** 
	 * The initial vertex definition
	 * 
	 * It defines the eight vertices a cube has
	 * based on the OpenGL coordinate system
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
			// Normals
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
    
    /** The initial color definition */	
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
						
						
						
			            /*0.0f,  1.0f,  0.0f,  1.0f,
			            0.0f,  1.0f,  0.0f,  1.0f,
			            1.0f,  0.5f,  0.0f,  1.0f,
			            1.0f,  0.5f,  0.0f,  1.0f,
			            1.0f,  0.0f,  0.0f,  1.0f,
			            1.0f,  0.0f,  0.0f,  1.0f,
			            0.0f,  0.0f,  1.0f,  1.0f,
			            1.0f,  0.0f,  1.0f,  1.0f*/
			    								};
	
	/** 
     * The initial indices definition
     * 
     * The indices define our triangles.
     * Always two define one of the six faces
     * a cube has.
     *	*/
	private byte indices[] = {
    					/*
    					 * Example: 
    					 * Face made of the vertices lower back left (lbl),
    					 * lfl, lfr, lbl, lfr, lbr
    					 **/
						0, 1, 2,	0, 2, 3,
						4, 5, 6,	4, 6, 7,
						8, 9, 10, 	8, 10, 11,
						12, 13, 14,	12, 14, 15,
						
						16, 17, 18,	16, 18, 25,
						20, 21, 22,	20, 22, 19,
						24, 25, 22,	24, 22, 23
						
			            /*0, 4, 9,    0, 9, 1,
			            //and so on...
			            1, 9, 11,    1, 11, 2,
			            2, 11, 12,    2, 12, 3,
			            3, 12, 4,    3, 4, 0,
						12, 13, 10,		12, 10, 11,
						13, 4, 5,		13, 5, 6,
						7, 8, 9,		7, 9, 10,												
			            3, 0, 1,    3, 1, 2*/
    										};
	
	/**
	 * The Cube constructor.
	 * 
	 * Initiate the buffers.
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
	 * The object own drawing function.
	 * Called from the renderer to redraw this instance
	 * with possible changes in values.
	 * 
	 * @param gl - The GL Context
	 */
	public void draw(GL10 gl) {		
		//Set the face rotation
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		
		gl.glFrontFace(GL10.GL_CCW);
		
		//Point to our buffers
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2,  GL10.GL_FLOAT, 0, textureBuffer);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);
		//gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
		
		//Enable the vertex and color state
		//gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		//gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		gl.glColor4f(0.933f, 0.910f, 0.667f, 1.0f);		
		
		//Draw the vertices as triangles, based on the Index Buffer information
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_BYTE, indexBuffer);
		//gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vertices.length / 3);
		
		//Disable the client state before leaving
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		//UNCOMMENT TO STOP TEXTURE
		//gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		//UNCOMMENT TO STOP LIGHT
		//gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
	}
	
	/**
	 * Load the textures
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
