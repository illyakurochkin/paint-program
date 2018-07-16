package com.kurochkin.illya.componentmanager;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.kurochkin.illya.InstrumentButton;
import com.kurochkin.illya.KeyAdapter;
import com.kurochkin.illya.PaintProgram;

import acm.graphics.GImage;
import acm.graphics.GLabel;
import acm.graphics.GRect;

public class TextManager extends ComponentManager {

	public static final Color BACKGROUND_COLOR = InstrumentsManager.BACKGROUND_COLOR;
	public static final Color TEXT_COLOR = InstrumentsManager.TEXT_COLOR;
	public static final Font FONT = InstrumentsManager.FONT;

	public static final int FONT_MAX_SIZE = 70;
	public static final int FONT_MIN_SIZE = 15;

	public static final int BUTTON_HEIGHT = InstrumentsManager.BUTTON_HEIGHT;
	public static final int MARGIN = InstrumentsManager.MARGIN;

	public TextManager(PaintProgram program) {
		super(program);
	}

	private GRect textRect;
	private InstrumentButton textTypingButton;
	private InstrumentButton italicButton;
	private InstrumentButton boldButton;

	private boolean isTextTyping = false;
	private GLabel currentTextLabel;
	private GRect caretteRect;

	private Thread caretteThread;

	public void stopTyping() {
		PaperManager paperManager = program.getPaperManager();
		Graphics imageGraphics = paperManager.getImageGraphics();
		GRect paperRect = paperManager.getPaperRect();

		imageGraphics.setFont(currentTextLabel.getFont());
		imageGraphics.setColor(currentTextLabel.getColor());
		imageGraphics.drawString(currentTextLabel.getLabel(), (int) (currentTextLabel.getX() - paperRect.getX()),
				(int) (currentTextLabel.getY() - paperRect.getY()));
		caretteThread.interrupt();
		program.remove(caretteRect);
		paperManager.cacheCurrentImage();
		isTextTyping = false;
	}

	public final KeyListener textTypeKeyListener = new KeyAdapter() {
		public void keyPressed(KeyEvent event) {
			if (isTextTyping) {

				String text = currentTextLabel.getLabel();
				Character keyChar = event.getKeyChar();

				if (event.getKeyCode() == 8 && text.length() > 0) { // backspace
					currentTextLabel.setLabel(text.substring(0, text.length() - 1));
					caretteRect.setLocation(currentTextLabel.getX() + currentTextLabel.getWidth(), caretteRect.getY());

				} else if (currentTextLabel.getFont().canDisplay((int) keyChar)) {
					if (keyChar == '\n') {
						stopTyping();

					} else {
						GRect paperRect = program.getPaperManager().getPaperRect();
						String oldLabel = currentTextLabel.getLabel();
						currentTextLabel.setLabel(oldLabel + keyChar);

						if (currentTextLabel.getX() + currentTextLabel.getWidth() > paperRect.getX()
								+ paperRect.getWidth()) {
							currentTextLabel.setLabel(oldLabel);
						} else {
							caretteRect.setLocation(currentTextLabel.getX() + currentTextLabel.getWidth(),
									caretteRect.getY());
						}
					}
				}
			}
		}
	};

	public final MouseListener textTypingMouseListener = new MouseAdapter() {
		public void mousePressed(MouseEvent event) {
			int currentSize = program.getInstrumentsManager().getSizeManager().getCurrentSize();
			if (!isTextTyping) {
				isTextTyping = true;
				currentTextLabel = new GLabel("");

				Font font = new Font("Verdanam", (italic() ? Font.ITALIC : 0) + (bold() ? Font.BOLD : 0),
						currentSize * FONT_MAX_SIZE / SizeManager.MAX_VALUE + 10);
				currentTextLabel.setFont(font);
				currentTextLabel.setColor(program.getCurrentColor());

				int xx = event.getX();
				int yy = event.getY();

				GRect paperRect = program.getPaperManager().getPaperRect();

				if (xx + 1 > paperRect.getX() + paperRect.getWidth()) {
					xx--;
				}

				if (yy - currentTextLabel.getFontMetrics().getMaxAscent() * 0.75 < paperRect.getY()) {
					yy = (int) (paperRect.getY() + currentTextLabel.getFontMetrics().getMaxAscent() * 0.75);
				} else if (yy + currentTextLabel.getFontMetrics().getDescent() > paperRect.getY()
						+ paperRect.getHeight()) {
					yy = (int) (paperRect.getY() + paperRect.getHeight()
							- currentTextLabel.getFontMetrics().getDescent());
				}

				currentTextLabel.setLocation(xx, yy);
				program.add(currentTextLabel);

				caretteRect = new GRect(currentTextLabel.getBounds().getX() + currentTextLabel.getBounds().getWidth(),
						yy - currentTextLabel.getFontMetrics().getHeight() / 2, 1,
						currentTextLabel.getBounds().getHeight() / 2);
				caretteRect.setFilled(true);
				caretteRect.setFillColor(program.getCurrentColor());
				caretteRect.setColor(program.getCurrentColor());
				program.add(caretteRect);

				caretteThread = createCaretteThread();
				caretteThread.start();
			} else {
				stopTyping();
				mousePressed(event);
			}
		}
	};

	@Override
	public void render(double x, double y, double width, double height) {
		program.addKeyListeners(textTypeKeyListener);

		textRect = new GRect(x, y, width, height);
		textRect.setFilled(true);
		textRect.setFillColor(BACKGROUND_COLOR);
		textRect.setColor(BACKGROUND_COLOR);

		program.add(textRect);

		GLabel textInstrumentsLabel = new GLabel("text");
		textInstrumentsLabel.setFont(FONT);
		textInstrumentsLabel.setColor(TEXT_COLOR);
		textInstrumentsLabel.setLocation(x + 10, y + 15);

		program.add(textInstrumentsLabel);

		textTypingButton = new InstrumentButton(textRect.getX() + MARGIN,
				textInstrumentsLabel.getY() + textInstrumentsLabel.getHeight(), width - 2 * MARGIN, BUTTON_HEIGHT);

		textTypingButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent event) {
				program.getPaperManager().updatePaperMouseListeners(textTypingMouseListener, null);
			}
		});

		program.add(textTypingButton);

		GImage typeTextImage = new GImage("type.png");
		typeTextImage.setSize(textTypingButton.getHeight() - 10, textTypingButton.getHeight() - 20);
		typeTextImage.setLocation(
				textTypingButton.getX() + (textTypingButton.getWidth() - typeTextImage.getWidth()) / 2,
				textTypingButton.getY() + 10);

		program.add(typeTextImage);

		italicButton = new InstrumentButton(textTypingButton.getX(),
				textTypingButton.getY() + textTypingButton.getHeight() + MARGIN, (width - 3 * MARGIN) / 2,
				BUTTON_HEIGHT);
		boldButton = new InstrumentButton(italicButton.getX() + italicButton.getWidth() + MARGIN, italicButton.getY(),
				italicButton.getWidth(), italicButton.getHeight());

		italicButton.addMouseListener(toggleButtonListener);
		boldButton.addMouseListener(toggleButtonListener);

		program.addKeyListeners(new KeyAdapter() {
			public void keyPressed(KeyEvent event) {
				InstrumentButton button = null;
				if (event.getKeyCode() == 73) { // 'i'
					button = italicButton;
				} else if (event.getKeyCode() == 66) { // 'b'
					button = boldButton;
				} else {
					return;
				}

				if (event.isControlDown()) {
					if (button.isSelected()) {
						button.unselect();
					} else {
						button.select();
					}
				}
			}
		});

		program.add(italicButton);
		program.add(boldButton);

		GImage italicTextImage = new GImage("italic-text.png");
		italicTextImage.setSize(italicButton.getHeight() - 20, italicButton.getHeight() - 20);
		italicTextImage.setLocation(italicButton.getX() + (italicButton.getWidth() - italicTextImage.getWidth()) / 2,
				italicButton.getY() + 10);

		GImage boldTextImage = new GImage("bold-text.png");
		boldTextImage.setSize(boldButton.getHeight() - 20, boldButton.getHeight() - 20);
		boldTextImage.setLocation(boldButton.getX() + (boldButton.getWidth() - boldTextImage.getWidth()) / 2,
				boldButton.getY() + 10);

		program.add(italicTextImage);
		program.add(boldTextImage);

		textRect.setSize(textRect.getWidth(),
				italicButton.getY() + italicButton.getHeight() + MARGIN - textRect.getY());
	}

	private Thread createCaretteThread() {
		return new Thread(() -> {
			while (true) {
				try {
					Thread.sleep(450);
					caretteRect.setVisible(false);
					Thread.sleep(500);
					caretteRect.setVisible(true);
				} catch (InterruptedException e) {
					// e.printStackTrace();
				}
			}
		});
	}

	public final MouseListener toggleButtonListener = new MouseAdapter() {
		public void mousePressed(MouseEvent event) {
			InstrumentButton button = (InstrumentButton) event.getSource();
			if (button.isSelected()) {
				button.unselect();
			} else {
				button.select();
			}
		}
	};

	private boolean italic() {
		return italicButton.isSelected();
	}

	private boolean bold() {
		return boldButton.isSelected();
	}

	public GRect getTextRect() {
		return textRect;
	}

	public InstrumentButton getTextTypingButton() {
		return textTypingButton;
	}
}
