package com.kurochkin.illya.componentmanager;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.Set;

import com.kurochkin.illya.InstrumentButton;
import com.kurochkin.illya.PaintProgram;

import acm.graphics.GRect;

public class InstrumentsManager extends ComponentManager {

	public static final Font FONT = PaintProgram.VERDANA_FONT;
	public static final Color TEXT_COLOR = PaintProgram.PINK_TEXT_COLOR;
	public static final Color BACKGROUND_COLOR = PaintProgram.BRIGHTER_BACKGROUND_COLOR;
	public static final int MARGIN = InstrumentButton.DEFAULT_MARGIN;
	public static final int BUTTON_HEIGHT = InstrumentButton.DEFAULT_HEIGHT;
	public static final int WIDTH = 150;

	private TextManager textManager;
	private SizeManager sizeManager;
	private SignetManager signetManager;
	private LineManager lineManager;

	private GRect instrumentsRect;

	public InstrumentsManager(PaintProgram program) {
		super(program);
		textManager = new TextManager(program);
		sizeManager = new SizeManager(program);
		lineManager = new LineManager(program);
	}

	public TextManager getTextManager() {
		return textManager;
	}

	public SizeManager getSizeManager() {
		return sizeManager;
	}

	public SignetManager getSignetManager() {
		return signetManager;
	}

	public LineManager getLineManager() {
		return lineManager;
	}

	@Override
	public void render(double x, double y, double width, double height) {
		instrumentsRect = new GRect(x, y, width, height);

		instrumentsRect.setFilled(true);
		instrumentsRect.setFillColor(BACKGROUND_COLOR);
		instrumentsRect.setColor(BACKGROUND_COLOR);

		program.add(instrumentsRect);

		sizeManager = new SizeManager(program);
		textManager = new TextManager(program);
		signetManager = new SignetManager(program);
		lineManager = new LineManager(program);

		sizeManager.render(instrumentsRect.getX(), instrumentsRect.getY(), instrumentsRect.getWidth(), 0);

		textManager.render(instrumentsRect.getX(),
				sizeManager.getSizeRect().getY() + sizeManager.getSizeRect().getHeight() + 40,
				instrumentsRect.getWidth(), 0);
		signetManager.render(instrumentsRect.getX(),
				textManager.getTextRect().getY() + textManager.getTextRect().getHeight() + 40,
				instrumentsRect.getWidth(), 0);
		lineManager.render(instrumentsRect.getX(),
				signetManager.getSignetRect().getY() + signetManager.getSignetRect().getHeight() + 40,
				instrumentsRect.getWidth(), 0);

		MouseListener instrumentButtonsListener = new MouseAdapter() {
			public void mousePressed(MouseEvent event) {
				InstrumentButton button = (InstrumentButton) event.getSource();
				if (!button.isSelected()) {
					button.select();
					getInstrumentButtons().stream().filter(instrumentButton -> instrumentButton != button)
							.forEach(InstrumentButton::unselect);
				}
			}
		};

		getInstrumentButtons().forEach(button -> button.addMouseListener(instrumentButtonsListener));
	}

	private Set<InstrumentButton> getInstrumentButtons() {
		Set<InstrumentButton> buttons = new HashSet<>();

		buttons.add(textManager.getTextTypingButton());

		buttons.add(signetManager.getCircleSignetButton());
		buttons.add(signetManager.getSquareSignetButton());
		buttons.add(signetManager.getTriangleSignetButton());

		buttons.add(lineManager.getSingleLineButton());
		// buttons.add(lineManager.getDoubleLineButton());
		buttons.add(lineManager.getDotLineButton());

		return buttons;
	}
}
