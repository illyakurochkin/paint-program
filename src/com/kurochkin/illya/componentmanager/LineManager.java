package com.kurochkin.illya.componentmanager;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.kurochkin.illya.ColorConstants;
import com.kurochkin.illya.DrawLineUtilities;
import com.kurochkin.illya.DrawLineUtilities.Point;
import com.kurochkin.illya.InstrumentButton;
import com.kurochkin.illya.PaintProgram;

import acm.graphics.GLabel;
import acm.graphics.GLine;
import acm.graphics.GOval;
import acm.graphics.GRect;

public class LineManager extends ComponentManager {

	public static final Color BACKGROUND_COLOR = InstrumentsManager.BACKGROUND_COLOR;
	public static final Color TEXT_COLOR = InstrumentsManager.TEXT_COLOR;
	public static final Font FONT = InstrumentsManager.FONT;
	public static final int BUTTON_HEIGHT = InstrumentButton.DEFAULT_HEIGHT;
	public static final int MARGIN = InstrumentsManager.MARGIN;
	public static final int LINE_MAX_SIZE = 50;
	public static final int LINE_MIN_SIZE = 5;

	private GRect lineRect;

	private InstrumentButton singleLineButton;
	// private InstrumentButton doubleLineButton;
	private InstrumentButton dotLineButton;

	public InstrumentButton getSingleLineButton() {
		return singleLineButton;
	}

	// public InstrumentButton getDoubleLineButton() {
	// return doubleLineButton;
	// }

	public InstrumentButton getDotLineButton() {
		return dotLineButton;
	}

	public GRect getLineRect() {
		return lineRect;
	}

	public LineManager(PaintProgram program) {
		super(program);
	}

	private Point currentStartLinePoint;

	private Set<GOval> tempLine = new HashSet<>();
	private MouseListener singleLineMouseListener = new MouseAdapter() {
		public void mousePressed(MouseEvent event) {
			int radius = program.getCurrentSize() * LINE_MAX_SIZE / SizeManager.MAX_VALUE;
			GRect paperRect = program.getPaperManager().getPaperRect();
			int xx = event.getX();
			int yy = event.getY();
			if (xx - radius < paperRect.getX()) {
				xx = (int) paperRect.getX() + radius;
			} else if (xx + radius > paperRect.getX() + paperRect.getWidth()) {
				xx = (int) (paperRect.getX() + paperRect.getWidth() - radius);
			}

			if (yy - radius < paperRect.getY()) {
				yy = (int) paperRect.getY() + radius;
			} else if (yy + radius > paperRect.getY() + paperRect.getHeight()) {
				yy = (int) (paperRect.getY() + paperRect.getHeight() - radius);
			}
			currentStartLinePoint = new DrawLineUtilities.Point(xx, yy);
		}

		public void mouseReleased(MouseEvent event) {
			Graphics imageGraphics = program.getImageGraphics();
			GRect paperRect = program.getPaperManager().getPaperRect();

			tempLine.forEach(dot -> {
				imageGraphics.setColor(program.getCurrentColor());
				imageGraphics.fillOval((int) (dot.getX() - paperRect.getX()), (int) (dot.getY() - paperRect.getY()),
						(int) dot.getWidth(), (int) dot.getHeight());
			});
			tempLine.clear();
			program.getPaperManager().cacheCurrentImage();
		}
	};

	private MouseMotionListener singleLineMouseMotionListener = new MouseMotionAdapter() {
		public void mouseDragged(MouseEvent event) {
			int radius = program.getCurrentSize() * LINE_MAX_SIZE / SizeManager.MAX_VALUE;
			GRect paperRect = program.getPaperManager().getPaperRect();
			int xx = event.getX();
			int yy = event.getY();
			if (xx - radius < paperRect.getX()) {
				xx = (int) paperRect.getX() + radius;
			} else if (xx + radius > paperRect.getX() + paperRect.getWidth()) {
				xx = (int) (paperRect.getX() + paperRect.getWidth() - radius);
			}

			if (yy - radius < paperRect.getY()) {
				yy = (int) paperRect.getY() + radius;
			} else if (yy + radius > paperRect.getY() + paperRect.getHeight()) {
				yy = (int) (paperRect.getY() + paperRect.getHeight() - radius);
			}

			tempLine.forEach(dot -> program.remove(dot));
			tempLine.clear();
			tempLine.addAll(getSingleLine(currentStartLinePoint.x, currentStartLinePoint.y, xx, yy, radius));
			tempLine.forEach(dot -> {
				dot.setColor(ColorConstants.TRANSPARENT);
				dot.setFilled(true);
				dot.setFillColor(program.getCurrentColor());
				program.add(dot);
			});
		}
	};

	private MouseListener dotLineMouseListener = new MouseAdapter() {
		public void mousePressed(MouseEvent event) {
			int radius = program.getCurrentSize() * LINE_MAX_SIZE / SizeManager.MAX_VALUE;
			GRect paperRect = program.getPaperManager().getPaperRect();
			int xx = event.getX();
			int yy = event.getY();
			if (xx - radius < paperRect.getX()) {
				xx = (int) paperRect.getX() + radius;
			} else if (xx + radius > paperRect.getX() + paperRect.getWidth()) {
				xx = (int) (paperRect.getX() + paperRect.getWidth() - radius);
			}

			if (yy - radius < paperRect.getY()) {
				yy = (int) paperRect.getY() + radius;
			} else if (yy + radius > paperRect.getY() + paperRect.getHeight()) {
				yy = (int) (paperRect.getY() + paperRect.getHeight() - radius);
			}
			currentStartLinePoint = new Point(xx, yy);
		}

		public void mouseReleased(MouseEvent event) {
			Graphics imageGraphics = program.getImageGraphics();
			GRect paperRect = program.getPaperManager().getPaperRect();

			tempLine.forEach(dot -> {
				imageGraphics.setColor(program.getCurrentColor());
				imageGraphics.fillOval((int) (dot.getX() - paperRect.getX()), (int) (dot.getY() - paperRect.getY()),
						(int) dot.getWidth(), (int) dot.getHeight());
			});
			tempLine.clear();
			program.getPaperManager().cacheCurrentImage();
		}
	};

	private MouseMotionListener dotLineMouseMotionListener = new MouseMotionAdapter() {
		public void mouseDragged(MouseEvent event) {
			int radius = program.getCurrentSize() * LINE_MAX_SIZE / SizeManager.MAX_VALUE;
			GRect paperRect = program.getPaperManager().getPaperRect();

			int xx = event.getX();
			int yy = event.getY();

			if (xx - radius < paperRect.getX()) {
				xx = (int) paperRect.getX() + radius;
			} else if (xx + radius > paperRect.getX() + paperRect.getWidth()) {
				xx = (int) (paperRect.getX() + paperRect.getWidth() - radius);
			}

			if (yy - radius < paperRect.getY()) {
				yy = (int) paperRect.getY() + radius;
			} else if (yy + radius > paperRect.getY() + paperRect.getHeight()) {
				yy = (int) (paperRect.getY() + paperRect.getHeight() - radius);
			}

			// if (event.getX() - radius >= paperRect.getX()
			// && event.getX() + radius <= paperRect.getX() + paperRect.getWidth()
			// && event.getY() - radius >= paperRect.getY()
			// && event.getY() + radius <= paperRect.getY() + paperRect.getHeight()) {
			tempLine.forEach(dot -> program.remove(dot));
			tempLine.clear();
			tempLine.addAll(getDotLine(currentStartLinePoint.x, currentStartLinePoint.y, xx, yy, radius));
			tempLine.forEach(dot -> {
				dot.setColor(ColorConstants.TRANSPARENT);
				dot.setFilled(true);
				dot.setFillColor(program.getCurrentColor());
				program.add(dot);
			});
			// }
		}
	};

	@Override
	public void render(double x, double y, double width, double height) {

		lineRect = new GRect(x, y, width, height);
		lineRect.setFilled(true);
		lineRect.setFillColor(BACKGROUND_COLOR);
		lineRect.setColor(BACKGROUND_COLOR);

		program.add(lineRect);

		GLabel lineInstrumentsLabel = new GLabel("lines");
		lineInstrumentsLabel.setFont(FONT);
		lineInstrumentsLabel.setColor(TEXT_COLOR);
		lineInstrumentsLabel.setLocation(x + 10, y + 15);

		program.add(lineInstrumentsLabel);

		// singleLineButton = new InstrumentButton(lineRect.getX() + MARGIN,
		// lineInstrumentsLabel.getY() + lineInstrumentsLabel.getHeight(),
		// (lineRect.getWidth() - 4 * MARGIN) / 3,
		// BUTTON_HEIGHT);

		singleLineButton = new InstrumentButton(lineRect.getX() + MARGIN,
				lineInstrumentsLabel.getY() + lineInstrumentsLabel.getHeight(), (lineRect.getWidth() - 3 * MARGIN) / 2,
				BUTTON_HEIGHT);

		program.add(singleLineButton);

		singleLineButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent event) {
				program.getPaperManager().updatePaperMouseListeners(singleLineMouseListener,
						singleLineMouseMotionListener);
			}
		});

		double imageSize = singleLineButton.getHeight() * 0.3;

		GLine singleLineImage = new GLine(singleLineButton.getX() + (singleLineButton.getWidth() - imageSize) / 2,
				singleLineButton.getY() + (singleLineButton.getHeight() - imageSize) / 2 + imageSize,
				singleLineButton.getX() + (singleLineButton.getWidth() - imageSize) / 2 + imageSize,
				singleLineButton.getY() + (singleLineButton.getHeight() - imageSize) / 2);

		singleLineImage.setColor(TEXT_COLOR);
		program.add(singleLineImage);

		// doubleLineButton = new InstrumentButton(singleLineButton.getX() +
		// singleLineButton.getWidth() + MARGIN,
		// singleLineButton.getY(), singleLineButton.getWidth(),
		// singleLineButton.getHeight());
		//
		// program.add(doubleLineButton);
		//
		// GLine doubleLineImage1 = new GLine(doubleLineButton.getX() +
		// (doubleLineButton.getWidth() - imageSize) / 2,
		// singleLineImage.getStartPoint().getY() - 3,
		// doubleLineButton.getX() + (singleLineButton.getWidth() - imageSize) / 2 +
		// imageSize,
		// singleLineImage.getEndPoint().getY() - 3);
		// GLine doubleLineImage2 = new GLine(doubleLineImage1.getStartPoint().getX(),
		// singleLineImage.getStartPoint().getY() + 3,
		// doubleLineImage1.getEndPoint().getX(),
		// singleLineImage.getEndPoint().getY() + 3);
		// doubleLineImage1.setColor(TEXT_COLOR);
		// doubleLineImage2.setColor(TEXT_COLOR);
		// program.add(doubleLineImage1);
		// program.add(doubleLineImage2);

		// dotLineButton = new InstrumentButton(doubleLineButton.getX() +
		// doubleLineButton.getWidth() + MARGIN,
		// doubleLineButton.getY(), doubleLineButton.getWidth(),
		// doubleLineButton.getHeight());

		dotLineButton = new InstrumentButton(singleLineButton.getX() + singleLineButton.getWidth() + MARGIN,
				singleLineButton.getY(), singleLineButton.getWidth(), singleLineButton.getHeight());

		program.add(dotLineButton);

		getDotLine((int) (dotLineButton.getX() + (dotLineButton.getWidth() - imageSize) / 2),
				(int) (dotLineButton.getY() + (dotLineButton.getHeight() - imageSize) / 2 + imageSize),
				(int) (dotLineButton.getX() + (dotLineButton.getWidth() - imageSize) / 2 + imageSize),
				(int) (dotLineButton.getY() + (dotLineButton.getHeight() - imageSize) / 2), 1).forEach(dot -> {
					dot.setColor(ColorConstants.TRANSPARENT);
					dot.setFilled(true);
					dot.setFillColor(TEXT_COLOR);
					program.add(dot);
				});

		dotLineButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent event) {
				program.getPaperManager().updatePaperMouseListeners(dotLineMouseListener, dotLineMouseMotionListener);
			}
		});

		lineRect.setSize(lineRect.getWidth(),
				dotLineButton.getY() + dotLineButton.getHeight() + MARGIN - lineRect.getY());
	}

	private static Set<GOval> getDotLine(int x1, int y1, int x2, int y2, int radius) {
		List<DrawLineUtilities.Point> points = DrawLineUtilities.getPoints(x1, y1, x2, y2);
		List<DrawLineUtilities.Point> circleCenters = new ArrayList<>();
		for (int i = 0; i < points.size(); i++) {
			if (i % (radius * 4) == 0) {
				circleCenters.add(points.get(i));
			}
		}
		return circleCenters.stream()
				.map(point -> new GOval(point.x - radius, point.y - radius, radius * 2, radius * 2))
				.collect(Collectors.toSet());

	}

	private static Set<GOval> getSingleLine(int x1, int y1, int x2, int y2, int radius) {
		return DrawLineUtilities.getPoints(x1, y1, x2, y2).stream()
				.map(point -> new GOval(point.x - radius, point.y - radius, radius * 2, radius * 2))
				.collect(Collectors.toSet());

	}
}
