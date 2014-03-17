package com.example.opengl2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

public class Tronco {
	
	/** Buffer de vertices */
	private FloatBuffer vertexBuffer;
	/** Buffer de indices */
	private ByteBuffer  indexBuffer;
	
	/** 
 	 * Vertices
	 */
	private float vertices[] = {
			            -0.3f, -1.0f, -0.3f,	//lower back left (0)
			            0.3f, -1.0f, -0.3f,		//lower back right (1)
			            0.3f,  1.0f, -0.3f,		//upper back right (2)
			            -0.3f, 1.0f, -0.3f,		//upper back left (3)
			            -0.3f, -1.0f,  0.3f,	//lower front left (4)
			            0.3f, -1.0f,  0.3f,		//lower front right (5)
			            0.3f,  1.0f,  0.3f,		//upper front right (6)
			            -0.3f,  1.0f,  0.3f		//upper front left (7)
			    							};
    
	/** 
	 * Definición de indices
     */	
	private byte indices[] = {
			            0, 4, 5,    0, 5, 1,
			            1, 5, 6,    1, 6, 2,
			            2, 6, 7,    2, 7, 3,
			            3, 7, 4,    3, 4, 0,
			            4, 7, 6,    4, 6, 5,
			            3, 0, 1,    3, 1, 2
    										};
	
	/**
	 * Tronco
	 */
	public Tronco() {
		//
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);		
		
		//
		indexBuffer = ByteBuffer.allocateDirect(indices.length);
		indexBuffer.put(indices);
		indexBuffer.position(0);
	}
	
	/**
	 * Método de dibujado
	 */
	public void draw(GL10 gl) {		
		gl.glFrontFace(GL10.GL_CW);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		//Colorear tronco
		gl.glColor4f(0.804f, 0.522f, 0.247f, 1);				
		gl.glDrawElements(GL10.GL_TRIANGLES, 36, GL10.GL_UNSIGNED_BYTE, indexBuffer);
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
}
