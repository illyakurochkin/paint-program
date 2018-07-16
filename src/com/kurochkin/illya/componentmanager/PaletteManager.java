package com.kurochkin.illya.componentmanager;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.kurochkin.illya.ColorConstants;
import com.kurochkin.illya.PaintProgram;

import acm.graphics.GRect;

public class PaletteManager extends ComponentManager {

	public static final int HEIGHT = 35;
	public static final int INTERVAL = 9;
	public static final int COLOR_RECT_SIZE = 14;
	public static final int CURRENT_COLOR_RECT_SIZE = 17;
	public static final int LINE_HEIGHT = 20;
	public static final int LINE_WIDTH = 0;
	public static final Color LINE_COLOR = PaintProgram.PINK_TEXT_COLOR;
	public static final Color BACKGROUND_COLOR = PaintProgram.BRIGHTER_BACKGROUND_COLOR;

	public static final Color[] DEFAULT_COLORS = { ColorConstants.LIGHT_RED, ColorConstants.DARK_RED,
			ColorConstants.LIGHT_ORANGE, ColorConstants.DARK_ORANGE, ColorConstants.LIGHT_YELLOW,
			ColorConstants.DARK_YELLOW, ColorConstants.LIGHT_GREEN, ColorConstants.DARK_GREEN,
			ColorConstants.LIGHT_AQUA, ColorConstants.DARK_AQUA, ColorConstants.LIGHT_SKYBLUE,
			ColorConstants.DARK_SKYBLUE, ColorConstants.LIGHT_BLUE, ColorConstants.DARK_BLUE,
			ColorConstants.LIGHT_PURPLE, ColorConstants.DARK_PURPLE, ColorConstants.LIGHT_MAGENTA,
			ColorConstants.DARK_MAGENTA, ColorConstants.LIGHT_WHITE, ColorConstants.DARK_WHITE,
			ColorConstants.LIGHT_GREY, ColorConstants.DARK_GREY, ColorConstants.LIGHT_BLACK,
			ColorConstants.DARK_BLACK };

	private GRect paletteRect;
	private Color currentColor = ColorConstants.DARK_GREEN;

	public PaletteManager(PaintProgram program) {
		super(program);
	}

	public GRect getPaletteRect() {
		return paletteRect;
	}

	public Color getCurrentColor() {
		return currentColor;
	}

	@Override
	public void render(double x, double y, double width, double height) {
		paletteRect = new GRect(x, y, width, height);
		paletteRect.setFilled(true);
		paletteRect.setFillColor(BACKGROUND_COLOR);
		paletteRect.setColor(ColorConstants.TRANSPARENT);

		program.add(paletteRect);

		GRect currentColorRect = new GRect(INTERVAL, (HEIGHT - CURRENT_COLOR_RECT_SIZE) / 2, CURRENT_COLOR_RECT_SIZE,
				CURRENT_COLOR_RECT_SIZE);

		program.add(currentColorRect);

		currentColorRect.setFilled(true);
		currentColorRect.setColor(currentColor.darker());
		currentColorRect.setFillColor(currentColor);

		GRect lineRect = new GRect(currentColorRect.getX() + currentColorRect.getWidth() + INTERVAL,
				(HEIGHT - LINE_HEIGHT) / 2, LINE_WIDTH, LINE_HEIGHT);

		program.add(lineRect);

		lineRect.setFilled(true);
		lineRect.setFillColor(LINE_COLOR);
		lineRect.setColor(LINE_COLOR);

		double startX = INTERVAL + lineRect.getX() + lineRect.getWidth();

		GRect[] colorRects = new GRect[DEFAULT_COLORS.length];

		for (int i = 0; i < DEFAULT_COLORS.length; i++) {
			colorRects[i] = new GRect(startX + i * (INTERVAL + COLOR_RECT_SIZE), (HEIGHT - COLOR_RECT_SIZE) / 2,
					COLOR_RECT_SIZE, COLOR_RECT_SIZE);
			colorRects[i].setFilled(true);
			colorRects[i].setFillColor(DEFAULT_COLORS[i]);
			colorRects[i].setColor(DEFAULT_COLORS[i].darker());
			colorRects[i].addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent event) {
					GRect colorRect = (GRect) event.getSource();
					currentColorRect.setFillColor(colorRect.getFillColor());
					currentColorRect.setColor(colorRect.getColor());
					currentColor = currentColorRect.getFillColor();
				}
			});
			program.add(colorRects[i]);
		}
	}
}
