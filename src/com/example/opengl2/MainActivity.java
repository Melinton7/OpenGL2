package com.example.opengl2;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ToggleButton;

import android.opengl.GLSurfaceView;


public class MainActivity extends Activity {

	/** The OpenGL View */
	private Render render;
	private ToggleButton toggleButton1, toggleButton2;
	private Button btnDisplay;
		
	
	/**
	 * Initiate the OpenGL View and set our own
	 * Renderer (@see Lesson02.java)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		//Create an Instance with this Activity
		//glSurface = new GLSurfaceView(this);
		//Set our own Renderer
		//glSurface.setRenderer(new Render(this));
		//Set the GLSurface as View to this Activity
		render = new Render(this);
		setContentView(render);
	}

	/**
	 * Remember to resume the glSurface
	 */
	@Override
	protected void onResume() {
		super.onResume();
		render.onResume();
	}

	/**
	 * Also pause the glSurface
	 */
	@Override
	protected void onPause() {
		super.onPause();
		render.onPause();
	}

}
