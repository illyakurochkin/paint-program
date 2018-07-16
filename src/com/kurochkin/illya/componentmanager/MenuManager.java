package com.kurochkin.illya.componentmanager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

import com.kurochkin.illya.PaintProgram;

import acm.graphics.GImage;
import acm.graphics.GRect;
import acm.util.CancelledException;

public class MenuManager extends ComponentManager{

	public static final Font FONT = PaintProgram.VERDANA_FONT;
	public static final Color TEXT_COLOR = PaintProgram.PINK_TEXT_COLOR;
	public static final Color BACKGROUND_COLOR = PaintProgram.DARKER_BACKGROUND_COLOR;
	public static final int HEIGHT = 35;

	private JMenuBar menuBar;

	public MenuManager(PaintProgram program) {
		super(program);
	}

	private static final FileFilter pictureFileFilter = new FileFilter() {
		public boolean accept(File file) {
			String fileName = file.getName();
			String format = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
			return file.isDirectory() || format.equals("png") || format.equals("jpg") || format.equals("jpeg");
		}

		public String getDescription() {
			return "picture";
		}
	};

	public final ActionListener saveMenuItemListener = event -> {
		JFileChooser fileopen = new JFileChooser();
		fileopen.setFileFilter(pictureFileFilter);

		if (fileopen.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			File file = fileopen.getSelectedFile();
			String format = file.getName().substring(file.getName().lastIndexOf(".") + 1).toLowerCase();
			if (!"png".equals(format) && !"jpg".equals(format) && !"jpeg".equals(format)) {
				format = "png";
				file = new File(file.getAbsolutePath() + ".png");
			}
			program.getPaperManager().writeCurrentImage(format, file);
			program.isSaved = true;
		}

	};

	public final ActionListener newMenuItemListener = event -> {
		program.getDialog().setAllowCancel(true);
		try {
			if (!program.isSaved
					&& program.getDialog().readBoolean("do you want to save this picture ?", "yes", "no")) {
				saveMenuItemListener.actionPerformed(null);
				if (!program.isSaved) {
					return;
				}
			}
		} catch (CancelledException e) {
			return;
		}

		PaperManager paperManager = program.getPaperManager();
		Graphics imageGraphics = program.getImageGraphics();
		GRect paperRect = paperManager.getPaperRect();

		// allObjects.forEach(this::remove); // TODO change
		paperRect.sendToFront();
		imageGraphics.setColor(PaperManager.BACKGROUND_COLOR);
		imageGraphics.fillRect(0, 0, (int) paperRect.getWidth(), (int) paperRect.getHeight());
		paperManager.resetCachePointer();
	};

	public final ActionListener openMenuItemListener = event -> {
		JFileChooser fileopen = new JFileChooser();
		fileopen.setFileFilter(pictureFileFilter);

		if (fileopen.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File file = fileopen.getSelectedFile();
			try {
				GRect paperRect = program.getPaperManager().getPaperRect();

				BufferedImage image = ImageIO.read(file);
				// program.getPaperManager().updateBufferedImage(image);
				Graphics imageGraphics = program.getImageGraphics();

				int width = image.getWidth(null);
				int height = image.getHeight(null);

				int newWidth = (int) paperRect.getWidth() + 1;
				int newHeight = newWidth * height / width;

				if (newHeight >= paperRect.getHeight()) {
					newHeight = (int) paperRect.getHeight() + 1;
					newWidth = newHeight * width / height;
				}

				int newX = ((int) paperRect.getWidth() - newWidth) / 2;
				int newY = ((int) paperRect.getHeight() - newHeight) / 2;

				newMenuItemListener.actionPerformed(null);

				imageGraphics.setColor(PaperManager.BACKGROUND_COLOR);
				imageGraphics.fillRect(0, 0, (int) paperRect.getWidth(), (int) paperRect.getHeight());
				imageGraphics.drawImage(image, newX, newY, newWidth, newHeight, null);
				program.getPaperManager().cacheCurrentImage();

				GImage gimage = new GImage(file.getPath(), paperRect.getX() + newX, paperRect.getY() + newY);
				gimage.setSize(newWidth, newHeight);
				program.add(gimage);
				program.isSaved = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	public final ActionListener exitMenuItemListener = event -> {
		program.getDialog().setAllowCancel(true);
		try {
			if (!program.isSaved
					&& program.getDialog().readBoolean("do you want to save this picture ?", "yes", "no")) {
				saveMenuItemListener.actionPerformed(null);
				if (program.isSaved) {
					System.exit(0);
				}
			} else {
				System.exit(0);
			}
		} catch (CancelledException e) {
			return;
		}
	};

	public final ActionListener undoMenuItemListener = event -> {
		program.getPaperManager().setCachedImage(true);
	};

	public final ActionListener redoMenuItemListener = event -> {
		program.getPaperManager().setCachedImage(false);
	};

	public final ActionListener helpMenuItemListener = event -> {
		program.getDialog().println("this is paint program.\n"
				+ "you can use it for editing photos or painting your pictures.");
	};
	public final ActionListener aboutMenuItemListener = event -> {
		program.getDialog().println("by Illya Kurochkin\n"
				+ "all rights reserved\n" + "2018.");
	};

	@Override
	public void render(double x, double y, double width, double height) {
		menuBar = program.getMenuBar();
		menuBar.removeAll();
		menuBar.setBorder(BorderFactory.createEmptyBorder());
		menuBar.setBackground(BACKGROUND_COLOR);
		menuBar.setPreferredSize(new Dimension(30, 30));

		JMenu fileMenu = new JMenu("file");
		JMenuItem newMenuItem = new JMenuItem("new");
		JMenuItem openMenuItem = new JMenuItem("open");
		JMenuItem saveMenuItem = new JMenuItem("save");
		JMenuItem exitMenuItem = new JMenuItem("exit");
		JMenu helpMenu = new JMenu("help");
		JMenuItem helpMenuItem = new JMenuItem("help");
		JMenuItem aboutMenuItem = new JMenuItem("about");
		JMenu editMenu = new JMenu("edit");
		JMenuItem undoMenuItem = new JMenuItem("undo");
		JMenuItem redoMenuItem = new JMenuItem("redo");

		// FILE MENU
		fileMenu.setForeground(TEXT_COLOR);
		fileMenu.setBackground(BACKGROUND_COLOR);
		fileMenu.setBorder(BorderFactory.createEmptyBorder());
		fileMenu.setMnemonic('F');
		fileMenu.setFont(FONT);

		JMenuItem[] fileMenuItems = { newMenuItem, openMenuItem, saveMenuItem, exitMenuItem };

		for (JMenuItem temp : fileMenuItems) {
			temp.setFont(FONT);
			fileMenu.add(temp);
			temp.setBackground(BACKGROUND_COLOR);
			temp.setForeground(TEXT_COLOR);
			temp.setBorder(BorderFactory.createEmptyBorder());
			temp.setPreferredSize(new Dimension(100, 30));
		}

		newMenuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
		openMenuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
		saveMenuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl W"));

		newMenuItem.setMnemonic('N');
		openMenuItem.setMnemonic('O');
		saveMenuItem.setMnemonic('S');
		exitMenuItem.setMnemonic('E');

		openMenuItem.addActionListener(openMenuItemListener);
		newMenuItem.addActionListener(newMenuItemListener);
		saveMenuItem.addActionListener(saveMenuItemListener);
		exitMenuItem.addActionListener(exitMenuItemListener);

		// EDIT MENU
		editMenu.setForeground(TEXT_COLOR);
		editMenu.setBackground(BACKGROUND_COLOR);
		editMenu.setBorder(BorderFactory.createEmptyBorder());
		editMenu.setMnemonic('E');
		editMenu.setFont(FONT);

		JMenuItem[] editMenuItems = { undoMenuItem, redoMenuItem };

		for (JMenuItem temp : editMenuItems) {
			temp.setFont(FONT);
			editMenu.add(temp);
			temp.setBackground(BACKGROUND_COLOR);
			temp.setForeground(TEXT_COLOR);
			temp.setPreferredSize(new Dimension(100, 30));
			temp.setBorder(BorderFactory.createEmptyBorder());
		}

		undoMenuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl Z"));
		redoMenuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl Y"));

		undoMenuItem.setMnemonic('U');
		redoMenuItem.setMnemonic('R');

		undoMenuItem.addActionListener(undoMenuItemListener);

		redoMenuItem.addActionListener(redoMenuItemListener);

		// HELP MENU
		helpMenu.setBackground(BACKGROUND_COLOR);
		helpMenu.setForeground(TEXT_COLOR);
		helpMenu.setBorder(BorderFactory.createEmptyBorder());
		helpMenu.setMnemonic('H');
		helpMenu.setFont(FONT);

		JMenuItem[] helpMenuItems = { helpMenuItem, aboutMenuItem };

		for (JMenuItem temp : helpMenuItems) {
			temp.setFont(FONT);
			helpMenu.add(temp);
			temp.setBackground(BACKGROUND_COLOR);
			temp.setForeground(TEXT_COLOR);
			temp.setPreferredSize(new Dimension(100, 30));
			temp.setBorder(BorderFactory.createEmptyBorder());
		}

		helpMenuItem.setMnemonic('H');
		aboutMenuItem.setMnemonic('A');

		helpMenuItem.addActionListener(helpMenuItemListener);

		aboutMenuItem.addActionListener(aboutMenuItemListener);

		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(helpMenu);
	}
}
