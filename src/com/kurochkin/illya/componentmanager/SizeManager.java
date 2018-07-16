package com.kurochkin.illya.componentmanager;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.util.function.Consumer;

import com.kurochkin.illya.PaintProgram;

import acm.graphics.GLabel;
import acm.graphics.GRect;

public class SizeManager extends ComponentManager {

	public static final Color BACKGROUND_COLOR = InstrumentsManager.BACKGROUND_COLOR;
	public static final Color TEXT_COLOR = InstrumentsManager.TEXT_COLOR;
	public static final Font FONT = InstrumentsManager.FONT;

	public static final int MARGIN = InstrumentsManager.MARGIN;

	public static final int SCROLLER_HEIGHT = 20;
	public static final int SCROLLER_WIDTH = 5;
	public static final int SCROLL_LINE_HEIGHT = 5;
	public static final int SCROLL_LINE_WIDTH = 130;

	public static final Color SCROLLER_COLOR = TEXT_COLOR;
	public static final Color SCROLL_LINE_COLOR = PaintProgram.DARKER_BACKGROUND_COLOR;

	public static final int MAX_VALUE = 1000;
	public static final int MIN_VALUE = 1;

	private int currentSize;
	private GRect sizeRect;

	public SizeManager(PaintProgram program) {
		super(program);
	}

	public int getCurrentSize() {
		return currentSize;
	}

	public GRect getSizeRect() {
		return sizeRect;
	}

	@Override
	public void render(double x, double y, double width, double height) {
		sizeRect = new GRect(x, y, width, height);
		sizeRect.setFilled(true);
		sizeRect.setFillColor(BACKGROUND_COLOR);
		sizeRect.setColor(BACKGROUND_COLOR);
		program.add(sizeRect);

		GLabel sizeLabel = new GLabel("size");
		sizeLabel.setFont(FONT);
		sizeLabel.setColor(TEXT_COLOR);
		sizeLabel.setLocation(x + 15, y + 15);
		program.add(sizeLabel);

		GRect scrollBarRect = new GRect(sizeRect.getX() + 10, sizeLabel.getY() + sizeLabel.getHeight(),
				SCROLL_LINE_WIDTH, SCROLLER_HEIGHT);
		scrollBarRect.setFilled(true);
		scrollBarRect.setFillColor(BACKGROUND_COLOR);
		scrollBarRect.setColor(BACKGROUND_COLOR);
		program.add(scrollBarRect);

		GRect scrollLineRect = new GRect(sizeRect.getX() + (sizeRect.getWidth() - SCROLL_LINE_WIDTH) / 2,
				scrollBarRect.getY() + (scrollBarRect.getHeight() - SCROLL_LINE_HEIGHT) / 2, SCROLL_LINE_WIDTH,
				SCROLL_LINE_HEIGHT);
		scrollLineRect.setFilled(true);
		scrollLineRect.setFillColor(SCROLL_LINE_COLOR);
		scrollLineRect.setColor(SCROLL_LINE_COLOR);
		program.add(scrollLineRect);

		GRect scrollerRect = new GRect(scrollBarRect.getX(), scrollBarRect.getY(), SCROLLER_WIDTH, SCROLLER_HEIGHT);
		scrollerRect.setFilled(true);
		scrollerRect.setFillColor(SCROLLER_COLOR);
		scrollerRect.setColor(SCROLLER_COLOR);

		Consumer<MouseEvent> scrollBarListener = event -> {
			if (event != null && event.getX() - scrollerRect.getWidth() / 2 >= scrollLineRect.getX() && event.getX()
					+ scrollerRect.getWidth() / 2 <= scrollLineRect.getX() + scrollLineRect.getWidth()) {
				scrollerRect.setLocation(event.getX() - scrollerRect.getWidth() / 2, scrollerRect.getY());
			}
			currentSize = (int) ((MAX_VALUE - 30) // TODO (-40) change
					* (scrollerRect.getX() + scrollerRect.getWidth() / 2 - scrollLineRect.getX())
					/ (scrollLineRect.getWidth() - scrollerRect.getWidth()));
		};

		MouseMotionListener mouseDraggedListener = new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent event) {
				scrollBarListener.accept(event);
			}
		};

		scrollerRect.addMouseMotionListener(mouseDraggedListener);
		scrollBarRect.addMouseMotionListener(mouseDraggedListener);

		scrollBarRect.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent event) {
				scrollBarListener.accept(event);
			}
		});

		program.add(scrollerRect);

		scrollerRect.move(20, 0);
		scrollBarListener.accept(null); // update current size variable

		sizeRect.setSize(sizeRect.getWidth(),
				scrollBarRect.getY() + scrollBarRect.getHeight() + MARGIN - sizeRect.getY());
	}
}
