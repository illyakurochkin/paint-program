package com.kurochkin.illya.componentmanager;

import com.kurochkin.illya.PaintProgram;

public abstract class ComponentManager {
	
	protected PaintProgram program;
	
	public ComponentManager(PaintProgram program) {
		this.program = program;
	}
	
	public abstract void render(double x, double y, double width, double height);
}
