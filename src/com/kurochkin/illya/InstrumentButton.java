package com.kurochkin.illya;

import java.awt.Color;

import com.kurochkin.illya.componentmanager.InstrumentsManager;
import com.kurochkin.illya.componentmanager.MenuManager;

import acm.graphics.GRect;

@SuppressWarnings("serial")
public class InstrumentButton extends GRect {

	public static final int DEFAULT_HEIGHT = 40;
	public static final int DEFAULT_MARGIN = 7;

	public static final Color SELECTED_BACKGROUND_COLOR = MenuManager.BACKGROUND_COLOR;
	public static final Color BACKGROUND_COLOR = InstrumentsManager.BACKGROUND_COLOR;
	public static final Color BORDER_COLOR = PaintProgram.PINK_TEXT_COLOR;

	private boolean isSelected = false;

	public InstrumentButton(double x, double y, double width, double height) {
		super(x, y, width, height);
		setFilled(true);
		setColor(BORDER_COLOR);
		setFillColor(BACKGROUND_COLOR);
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void unselect() {
		if (isSelected) {
			isSelected = false;
			setFillColor(BACKGROUND_COLOR);
		}
	}

	public void select() {
		if (!isSelected) {
			isSelected = true;
			setFillColor(SELECTED_BACKGROUND_COLOR);
		}
	}
}
