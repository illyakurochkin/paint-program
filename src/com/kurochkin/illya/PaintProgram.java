package com.kurochkin.illya;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.kurochkin.illya.componentmanager.InfoManager;
import com.kurochkin.illya.componentmanager.InstrumentsManager;
import com.kurochkin.illya.componentmanager.MenuManager;
import com.kurochkin.illya.componentmanager.PaletteManager;
import com.kurochkin.illya.componentmanager.PaperManager;

import acm.program.GraphicsProgram;

@SuppressWarnings("serial")
public class PaintProgram extends GraphicsProgram {

	public static final String TITLE = "paint by @illyakurochkin";
	public static final int WIDTH = 780;
	public static final int HEIGHT = 600;

	public static final Font VERDANA_FONT = new Font("Verdana", Font.PLAIN, 13);
	public static final Color PINK_TEXT_COLOR = new Color(225, 199, 192);
	public static final Color DARKER_BACKGROUND_COLOR = new Color(25, 31, 46);
	public static final Color BRIGHTER_BACKGROUND_COLOR = new Color(49, 55, 71);

	private MenuManager menuManager = new MenuManager(this);
	private PaperManager paperManager = new PaperManager(this);
	private InstrumentsManager instrumentsManager = new InstrumentsManager(this);
	private PaletteManager paletteManager = new PaletteManager(this);
	private InfoManager infoManager = new InfoManager(this);

	public boolean isSaved = true;

	@Override
	public void exit() {
		getDialog().setAllowCancel(false);
		if (!isSaved && getDialog().readBoolean("do you want to save this picture ?", "yes", "no")) {
			menuManager.saveMenuItemListener.actionPerformed(null);
			if (!isSaved) {
				return;
			}
		}
		System.exit(0);
	}

	@Override
	public void run() {
		setTitle(TITLE);
		setBackground(InstrumentsManager.BACKGROUND_COLOR);

		// setSize(WIDTH + 1, HEIGHT + 31); // size for applet
		setSize(WIDTH + 17, HEIGHT + 70); // size for application

		menuManager.render(0, 0, WIDTH, MenuManager.HEIGHT);
		paperManager.render(0, PaletteManager.HEIGHT, WIDTH - InstrumentsManager.WIDTH,
				HEIGHT - PaletteManager.HEIGHT - InfoManager.HEIGHT);
		paletteManager.render(0, 0, WIDTH, PaletteManager.HEIGHT);
		instrumentsManager.render(WIDTH - InstrumentsManager.WIDTH, PaletteManager.HEIGHT, InstrumentsManager.WIDTH,
				HEIGHT - PaletteManager.HEIGHT);
		infoManager.render(0, HEIGHT - InfoManager.HEIGHT, WIDTH, InfoManager.HEIGHT);
	}

	public static void main(String[] args) {
		new PaintProgram().start();
	}

	public PaperManager getPaperManager() {
		return paperManager;
	}

	public InstrumentsManager getInstrumentsManager() {
		return instrumentsManager;
	}

	public Color getCurrentColor() {
		return paletteManager.getCurrentColor();
	}

	public int getCurrentSize() {
		return instrumentsManager.getSizeManager().getCurrentSize();
	}

	public Graphics getImageGraphics() {
		return paperManager.getImageGraphics();
	}
}
