package com.kurochkin.illya.componentmanager;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.function.Consumer;

import com.kurochkin.illya.PaintProgram;

import acm.graphics.GLabel;
import acm.graphics.GRect;

public class InfoManager extends ComponentManager {

	public static final Font FONT = PaintProgram.VERDANA_FONT;
	public static final Color TEXT_COLOR = PaintProgram.PINK_TEXT_COLOR;
	public static final Color BACKGROUND_COLOR = PaintProgram.BRIGHTER_BACKGROUND_COLOR;
	public static final int HEIGHT = 35;
	public static final String LOCATION_INFO_FORMAT = "location : ( %s : %s )";

	private GRect infoRect;

	public InfoManager(PaintProgram program) {
		super(program);
	}

	public GRect getInfoRect() {
		return infoRect;
	}

	@Override
	public void render(double x, double y, double width, double height) {
		infoRect = new GRect(x, y, width, height);
		infoRect.setFilled(true);
		infoRect.setFillColor(BACKGROUND_COLOR);
		infoRect.setColor(BACKGROUND_COLOR);
		program.add(infoRect);

		GLabel locationLabel = new GLabel(String.format(LOCATION_INFO_FORMAT, "-", "-"));
		locationLabel.setColor(TEXT_COLOR);
		locationLabel.setFont(FONT);
		locationLabel.setLocation(infoRect.getX() + infoRect.getWidth() / 10,
				infoRect.getY() + infoRect.getHeight() - 13);

		GRect paperRect = program.getPaperManager().getPaperRect();

		Consumer<MouseEvent> infoListener = event -> {
			locationLabel
					.setLabel(String.format(LOCATION_INFO_FORMAT, String.valueOf(event.getX() - (int) paperRect.getX()),
							String.valueOf(event.getY() - (int) paperRect.getY())));
		};

		paperRect.addMouseMotionListener(new MouseMotionListener() {
			public void mouseMoved(MouseEvent event) {
				infoListener.accept(event);
			}

			public void mouseDragged(MouseEvent event) {
				GRect paperRect = program.getPaperManager().getPaperRect();
				if (event.getX() >= paperRect.getX() && event.getX() <= paperRect.getX() + paperRect.getWidth()
						&& event.getY() >= paperRect.getY()
						&& event.getY() <= paperRect.getY() + paperRect.getHeight()) {
					infoListener.accept(event);
				} else {
					locationLabel.setLabel(String.format(LOCATION_INFO_FORMAT, "-", "-"));
				}
			}
		});

		paperRect.addMouseListener(new MouseAdapter() {
			public void mouseExited(MouseEvent event) {
				locationLabel.setLabel(String.format(LOCATION_INFO_FORMAT, "-", "-"));
			}
		});
		program.add(locationLabel);
	}
}
