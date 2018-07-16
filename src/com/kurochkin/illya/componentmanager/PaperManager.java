package com.kurochkin.illya.componentmanager;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import com.kurochkin.illya.ColorConstants;
import com.kurochkin.illya.PaintProgram;

import acm.graphics.GImage;
import acm.graphics.GRect;

public class PaperManager extends ComponentManager {

	public static final String CACHE_PATH = "/../../cache";
	public static final String CACHE_FORMAT = "png";

	public static final Color BACKGROUND_COLOR = ColorConstants.LIGHT_WHITE;

	private GRect paperRect;
	private MouseListener currentMouseListener;
	private MouseMotionListener currentMouseMotionListener;

	private BufferedImage image;
	private Graphics imageGraphics;

	private LinkedList<File> cache = new LinkedList<>();
	public int cachePointer = -1;

	public PaperManager(PaintProgram program) {
		super(program);
	}

	public GRect getPaperRect() {
		return paperRect;
	}

	public Graphics getImageGraphics() {
		return imageGraphics;
	}

	public void resetCachePointer() {
		cachePointer = -1;
		cacheCurrentImage();
	}

	public void updatePaperMouseListeners(MouseListener mouseListener, MouseMotionListener mouseMotionListener) {
		if (currentMouseListener != null) {
			paperRect.removeMouseListener(currentMouseListener);
		}

		if (currentMouseMotionListener != null) {
			paperRect.removeMouseMotionListener(currentMouseMotionListener);
		}

		if (currentMouseListener == program.getInstrumentsManager().getTextManager().textTypingMouseListener) {
			program.getInstrumentsManager().getTextManager().stopTyping();
		}

		currentMouseListener = mouseListener;
		currentMouseMotionListener = mouseMotionListener;

		paperRect.addMouseListener(currentMouseListener);
		paperRect.addMouseMotionListener(currentMouseMotionListener);
	}

	@Override
	public void render(double x, double y, double width, double height) {
		image = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_RGB);
		imageGraphics = image.createGraphics();
		imageGraphics.setColor(BACKGROUND_COLOR);
		imageGraphics.fillRect(0, 0, (int) width, (int) height);
		cacheCurrentImage();
		program.isSaved = true;
		paperRect = new GRect(x, y, width, height);
		paperRect.setFilled(true);
		paperRect.setFillColor(BACKGROUND_COLOR);
		paperRect.setColor(BACKGROUND_COLOR);

		paperRect.addMouseListener(currentMouseListener);
		paperRect.addMouseMotionListener(currentMouseMotionListener);

		program.add(paperRect);
	}

	public void writeCurrentImage(String format, File file) {
		try {
			ImageIO.write(image, format, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void cacheCurrentImage() {
		if (cache.size() - 1 > cachePointer) {
			while (cache.size() - 1 != cachePointer) {
				File file = cache.removeLast();
				file.delete();
			}
		}
		cachePointer++;

		File dir = new File(CACHE_PATH);
		dir.mkdir();
		dir.deleteOnExit();

		File file = new File(CACHE_PATH + "/" + cachePointer + "." + CACHE_FORMAT);
		file.deleteOnExit();

		writeCurrentImage(CACHE_FORMAT, file);
		cache.add(file);
		program.isSaved = false;
	}

	public void setCachedImage(boolean undo) {
		if (undo ? cachePointer > 0 : cachePointer < cache.size() - 1) {
			File file = cache.get(undo ? --cachePointer : ++cachePointer);
			try {
				image = ImageIO.read(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			imageGraphics = image.getGraphics();
			program.isSaved = false;
			GImage gimage = new GImage(file.getPath(), paperRect.getX(), paperRect.getY());
			program.add(gimage);
		}
	}

}
