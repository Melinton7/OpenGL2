package com.example.opengl2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

public class Hojas {
	/** Buffer de los vertices */
	private FloatBuffer vertexBuffer;
		
	/** Definición inicial de vertices */
	private float vertices[] = { 
					 	0.0f,  1.5f,  0.0f,		//Top Of Triangle (Front)
						-1.0f, -1.0f, 1.0f,		//Left Of Triangle (Front)
						 1.0f, -1.0f, 1.0f,		//Right Of Triangle (Front)
						 0.0f,  1.5f, 0.0f,		//Top Of Triangle (Right)
						 1.0f, -1.0f, 1.0f,		//Left Of Triangle (Right)
						 1.0f, -1.0f, -1.0f,	//Right Of Triangle (Right)
						 0.0f,  1.5f, 0.0f,		//Top Of Triangle (Back)
						 1.0f, -1.0f, -1.0f,	//Left Of Triangle (Back)
						-1.0f, -1.0f, -1.0f,	//Right Of Triangle (Back)
						 0.0f,  1.5f, 0.0f,		//Top Of Triangle (Left)
						-1.0f, -1.0f, -1.0f,	//Left Of Triangle (Left)
						-1.0f, -1.0f, 1.0f,		//Right Of Triangle (Left)
						-1.0f, -1.0f, 1.0f, 	//Upper left (bottom)
						-1.0f, -1.0f, -1.0f,	//Lower left (bottom)
						1.0f, -1.0f, -1.0f,		//Lower right (bottom)
						-1.0f, -1.0f, 1.0f, 	//Upper left (bottom)
						1.0f, -1.0f, -1.0f,		//Lower right (bottom)
						1.0f, -1.0f, 1.0f		//Upper right (bottom)
						
											};


	/**
	 * Constructor de las hojas.
	 * 
	 */
	public Hojas() {
		//
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);		
	}
	
	/**
	/*Función de dibujas
	 */
	public void draw(GL10 gl) {	
		gl.glFrontFace(GL10.GL_CW);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		
		//Enable the vertex and color state
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		
		//Se pintan las hojas
		gl.glColor4f(0.180f,0.545f,0.341f, 1f);		
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vertices.length / 3);
		
		//Disable the client state before leaving
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
}
