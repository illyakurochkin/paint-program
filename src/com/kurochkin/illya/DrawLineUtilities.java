package com.kurochkin.illya;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DrawLineUtilities {

	private static List<Point> points = new ArrayList<>();

	public static List<Point> getPoints(int x1, int y1, int x2, int y2) {
		fillPoints(new Point(x1, y1), new Point(x2, y2));
		points.sort(Comparator.comparing(point -> Math.sqrt(Math.pow(point.x - x1, 2) + Math.pow(point.y - y1, 2))));
		List<Point> result = new ArrayList<>();
		points.forEach(result::add);
		points.clear();
		return result;
	}

	private static void fillPoints(Point first, Point last) {
		if (first.equals(last)) {
			points.add(first);
			return;
		}
		Point mid = new Point((first.x + last.x) / 2, (first.y + last.y) / 2);
		fillPoints(first, mid);
		fillPoints(mid, last);
	}

	public static class Point {
		public int x;
		public int y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public boolean equals(Object o) {
			Point p = (Point) o;
			return Math.abs(p.x - x) <= 1 && Math.abs(p.y - y) <= 1;
		}
	}
}
