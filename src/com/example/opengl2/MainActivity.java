package com.example.opengl2;

import android.os.Bundle;
import android.app.Activity;

public class MainActivity extends Activity {

	/** The OpenGL View */
	private Render render;

		
	
	/**
	 * Initiate the OpenGL View and set our own
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
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
