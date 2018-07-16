package com.kurochkin.illya.componentmanager;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import com.kurochkin.illya.ColorConstants;
import com.kurochkin.illya.InstrumentButton;
import com.kurochkin.illya.PaintProgram;

import acm.graphics.GLabel;
import acm.graphics.GObject;
import acm.graphics.GOval;
import acm.graphics.GPoint;
import acm.graphics.GPolygon;
import acm.graphics.GRect;

public class SignetManager extends ComponentManager {

	public static final Color BACKGROUND_COLOR = InstrumentsManager.BACKGROUND_COLOR;
	public static final Color TEXT_COLOR = InstrumentsManager.TEXT_COLOR;
	public static final Font FONT = InstrumentsManager.FONT;
	public static final int MARGIN = InstrumentsManager.MARGIN;
	public static final int BUTTON_HEIGHT = InstrumentsManager.BUTTON_HEIGHT;

	public static final int SIGNET_MAX_SIZE = 50;
	public static final int SIGNET_MIN_SIZE = 5;

	private GRect signetRect;
	private InstrumentButton circleSignetButton;
	private InstrumentButton squareSignetButton;
	private InstrumentButton triangleSignetButton;

	public SignetManager(PaintProgram program) {
		super(program);
	}

	public InstrumentButton getCircleSignetButton() {
		return circleSignetButton;
	}

	public InstrumentButton getSquareSignetButton() {
		return squareSignetButton;
	}

	public InstrumentButton getTriangleSignetButton() {
		return triangleSignetButton;
	}

	public GRect getSignetRect() {
		return signetRect;
	}

	private Set<GObject> tempObjects = new HashSet<>();

	private Consumer<MouseEvent> circleSignetListener = event -> {
		GRect paperRect = program.getPaperManager().getPaperRect();
		int radius = program.getCurrentSize() * SIGNET_MAX_SIZE / SizeManager.MAX_VALUE;
		int xx = event.getX() - radius;
		int yy = event.getY() - radius;
		if (xx < paperRect.getX()) {
			xx = (int) paperRect.getX();
		} else if (xx + 2 * radius > paperRect.getX() + paperRect.getWidth()) {
			xx = (int) (paperRect.getX() + paperRect.getWidth() - 2 * radius);
		}
		if (yy < paperRect.getY()) {
			yy = (int) paperRect.getY();
		} else if (yy + 2 * radius > paperRect.getY() + paperRect.getHeight()) {
			yy = (int) (paperRect.getY() + paperRect.getHeight() - 2 * radius);
		}

		GOval circle = new GOval(xx, yy, radius * 2, radius * 2);
		circle.setFilled(true);
		circle.setFillColor(program.getCurrentColor());
		circle.setColor(ColorConstants.TRANSPARENT);
		program.add(circle);
		tempObjects.add(circle);
	};

	private MouseListener circleSignetMouseListener = new MouseAdapter() {
		public void mousePressed(MouseEvent event) {
			circleSignetListener.accept(event);
		}

		public void mouseReleased(MouseEvent event) {
			Graphics imageGraphics = program.getImageGraphics();
			GRect paperRect = program.getPaperManager().getPaperRect();

			imageGraphics.setColor(program.getCurrentColor());
			tempObjects.forEach(circle -> {
				imageGraphics.fillOval((int) (circle.getX() - paperRect.getX()),
						(int) (circle.getY() - paperRect.getY()), (int) circle.getWidth(), (int) circle.getHeight());
			});
			tempObjects.clear();
			program.getPaperManager().cacheCurrentImage();
		}
	};

	private MouseMotionListener circleSignetMouseMotionListener = new MouseMotionAdapter() {
		public void mouseDragged(MouseEvent event) {
			circleSignetListener.accept(event);
		}
	};

	private Consumer<MouseEvent> squareSignetListener = event -> {
		int size = program.getCurrentSize() * SIGNET_MAX_SIZE / SizeManager.MAX_VALUE;
		GRect paperRect = program.getPaperManager().getPaperRect();

		int xx = event.getX() - size;
		int yy = event.getY() - size;

		if (xx < paperRect.getX()) {
			xx = (int) paperRect.getX();
		} else if (xx + 2 * size > paperRect.getX() + paperRect.getWidth()) {
			xx = (int) (paperRect.getX() + paperRect.getWidth() - 2 * size);
		}
		if (yy < paperRect.getY()) {
			yy = (int) paperRect.getY();
		} else if (yy + 2 * size > paperRect.getY() + paperRect.getHeight()) {
			yy = (int) (paperRect.getY() + paperRect.getHeight() - 2 * size);
		}

		GRect square = new GRect(xx, yy, size * 2, size * 2);
		square.setFilled(true);
		square.setFillColor(program.getCurrentColor());
		square.setColor(ColorConstants.TRANSPARENT);
		program.add(square);
		tempObjects.add(square);
		// }

	};

	private MouseListener squareSignetMouseListener = new MouseAdapter() {
		public void mousePressed(MouseEvent event) {
			squareSignetListener.accept(event);
		}

		public void mouseReleased(MouseEvent event) {
			Graphics imageGraphics = program.getImageGraphics();
			GRect paperRect = program.getPaperManager().getPaperRect();

			imageGraphics.setColor(program.getCurrentColor());
			tempObjects.forEach((square) -> {
				imageGraphics.fillRect((int) (square.getX() - paperRect.getX()),
						(int) (square.getY() - paperRect.getY()), (int) square.getWidth(), (int) square.getHeight());
			});
			tempObjects.clear();
			program.getPaperManager().cacheCurrentImage();
		}
	};
	private MouseMotionListener squareSignetMouseMotionListener = new MouseMotionAdapter() {
		public void mouseDragged(MouseEvent event) {
			squareSignetListener.accept(event);
		}
	};

	private Set<GPoint[]> triangles = new HashSet<>();

	private Consumer<MouseEvent> triangleSignetListener = (event) -> {
		int size = program.getCurrentSize() * SIGNET_MAX_SIZE / SizeManager.MAX_VALUE;
		int xx = event.getX() - size;
		int yy = event.getY() - size * 4 / 3;
		GRect paperRect = program.getPaperManager().getPaperRect();

		if (xx < paperRect.getX()) {
			xx = (int) paperRect.getX();
		} else if (xx + 2 * size > paperRect.getX() + paperRect.getWidth()) {
			xx = (int) (paperRect.getX() + paperRect.getWidth() - 2 * size);
		}
		if (yy < paperRect.getY()) {
			yy = (int) paperRect.getY();
		} else if (yy + 2 * size > paperRect.getY() + paperRect.getHeight()) {
			yy = (int) (paperRect.getY() + paperRect.getHeight() - 2 * size);
		}

		GPoint point1 = new GPoint(xx + size, yy);
		GPoint point2 = new GPoint(xx, yy + size * 2);
		GPoint point3 = new GPoint(xx + size * 2, yy + size * 2);

		GPoint[] points = { point1, point2, point3 };
		GPolygon triangle = new GPolygon(points);
		triangle.setFilled(true);
		triangle.setFillColor(program.getCurrentColor());
		triangle.setColor(ColorConstants.TRANSPARENT);
		program.add(triangle);
		triangles.add(points);
	};

	private MouseListener triangleSignetMouseListener = new MouseAdapter() {
		public void mousePressed(MouseEvent event) {
			triangleSignetListener.accept(event);
		}

		public void mouseReleased(MouseEvent event) {
			Graphics imageGraphics = program.getImageGraphics();
			GRect paperRect = program.getPaperManager().getPaperRect();

			imageGraphics.setColor(program.getCurrentColor());
			triangles.forEach(triangle -> {
				int[] xPoints = Arrays.stream(triangle).mapToInt(point -> (int) (point.getX() - paperRect.getX()))
						.toArray();
				int[] yPoints = Arrays.stream(triangle).mapToInt(point -> (int) (point.getY() - paperRect.getY()))
						.toArray();
				imageGraphics.fillPolygon(xPoints, yPoints, 3);
			});
			triangles.clear();
			program.getPaperManager().cacheCurrentImage();
		}
	};

	private MouseMotionListener triangleSignetMouseMotionListener = new MouseMotionAdapter() {
		public void mouseDragged(MouseEvent event) {
			triangleSignetListener.accept(event);
		}
	};

	@Override
	public void render(double x, double y, double width, double height) {

		signetRect = new GRect(x, y, width, height);
		signetRect.setFilled(true);
		signetRect.setFillColor(BACKGROUND_COLOR);
		signetRect.setColor(BACKGROUND_COLOR);

		program.add(signetRect);

		GLabel signetInstrumentsLabel = new GLabel("signets");
		signetInstrumentsLabel.setFont(FONT);
		signetInstrumentsLabel.setColor(TEXT_COLOR);
		signetInstrumentsLabel.setLocation(x + 10, y + 15);

		program.add(signetInstrumentsLabel);

		circleSignetButton = new InstrumentButton(signetRect.getX() + InstrumentButton.DEFAULT_MARGIN,
				signetInstrumentsLabel.getY() + signetInstrumentsLabel.getHeight(),
				(signetRect.getWidth() - 4 * InstrumentButton.DEFAULT_MARGIN) / 3, InstrumentButton.DEFAULT_HEIGHT);

		// set default paper listeners
		program.getPaperManager().updatePaperMouseListeners(circleSignetMouseListener, circleSignetMouseMotionListener);

		circleSignetButton.select();

		circleSignetButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent event) {
				program.getPaperManager().updatePaperMouseListeners(circleSignetMouseListener,
						circleSignetMouseMotionListener);
			}
		});

		program.add(circleSignetButton);

		double imageSize = circleSignetButton.getHeight() * 0.3;

		GOval circleSignetImage = new GOval(circleSignetButton.getX() + (circleSignetButton.getWidth() - imageSize) / 2,
				circleSignetButton.getY() + (circleSignetButton.getHeight() - imageSize) / 2, imageSize, imageSize);
		circleSignetImage.setColor(TEXT_COLOR);
		circleSignetImage.setFilled(true);
		circleSignetImage.setFillColor(TEXT_COLOR);

		program.add(circleSignetImage);

		squareSignetButton = new InstrumentButton(circleSignetButton.getX() + circleSignetButton.getWidth() + MARGIN,
				circleSignetButton.getY(), circleSignetButton.getWidth(), circleSignetButton.getHeight());

		squareSignetButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent event) {
				program.getPaperManager().updatePaperMouseListeners(squareSignetMouseListener,
						squareSignetMouseMotionListener);
			}
		});

		program.add(squareSignetButton);

		GRect squareSignetImage = new GRect(squareSignetButton.getX() + (squareSignetButton.getWidth() - imageSize) / 2,
				squareSignetButton.getY() + (squareSignetButton.getHeight() - imageSize) / 2, imageSize, imageSize);
		squareSignetImage.setFilled(true);
		squareSignetImage.setFillColor(TEXT_COLOR);
		squareSignetImage.setColor(TEXT_COLOR);

		program.add(squareSignetImage);

		triangleSignetButton = new InstrumentButton(squareSignetButton.getX() + squareSignetButton.getWidth() + MARGIN,
				squareSignetButton.getY(), squareSignetButton.getWidth(), squareSignetButton.getHeight());

		triangleSignetButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent event) {
				program.getPaperManager().updatePaperMouseListeners(triangleSignetMouseListener,
						triangleSignetMouseMotionListener);
			}
		});

		program.add(triangleSignetButton);

		GPoint triangleSignetPoint1 = new GPoint(
				triangleSignetButton.getX() + (triangleSignetButton.getWidth() - imageSize) / 2,
				triangleSignetButton.getY() + triangleSignetButton.getHeight()
						- (triangleSignetButton.getHeight() - imageSize) / 2);

		GPoint triangleSignetPoint2 = new GPoint(triangleSignetPoint1.getX() + imageSize, triangleSignetPoint1.getY());

		GPoint triangleSignetPoint3 = new GPoint(triangleSignetButton.getX() + triangleSignetButton.getWidth() / 2,
				triangleSignetPoint1.getY() - imageSize);

		GPolygon triangleSignetImage = new GPolygon(
				new GPoint[] { triangleSignetPoint1, triangleSignetPoint2, triangleSignetPoint3 });

		triangleSignetImage.setFilled(true);
		triangleSignetImage.setFillColor(TEXT_COLOR);
		triangleSignetImage.setColor(TEXT_COLOR);

		program.add(triangleSignetImage);

		signetRect.setSize(signetRect.getWidth(),
				triangleSignetButton.getY() + triangleSignetButton.getHeight() + MARGIN - signetRect.getY());
	}
}
