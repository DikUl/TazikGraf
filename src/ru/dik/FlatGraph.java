package ru.dik;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FlatGraph {

	public class Point {

		public int x;

		public int y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public boolean equals(Object obj) {
			Point object = (Point) obj;
			return this.x == object.x && this.y == object.y;
		}

		@Override
		public String toString() {
			return "[" + x + "," + y + "]";
		}

	}

	private int width;

	private int height;

	private int[][] field;
	
	private int[][] originalField;
	
	private boolean filled = false;

	public FlatGraph() {
		this(8, 8); // default table is 8x8
	}

	public FlatGraph(int width, int height) {
		super();
		this.width = width;
		this.height = height;
		originalField = new int[this.width][this.height];
		field = new int[this.width][this.height];
		
		Random rnd = new Random();
		for (int x = 0; x < this.width; x++) {
			for (int y = 0; y < this.height; y++) {
				if ((x == 0 && y == 0) || (x == this.width - 1 && y == this.height - 1)) {
					this.originalField[x][y] = 1;
				} else {
					this.originalField[x][y] = rnd.nextInt(2);
				}
			}
		}
		
		copyField(originalField, field);
	}

	@Override
	public String toString() {
		StringBuilder sbl = new StringBuilder();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				sbl.append(this.field[x][y] + " ");
			}
			sbl.append("\n");
		}
		return sbl.toString();
	}
	
	public void sythesizeWaveOnGraph() {
		List<Point> points = new ArrayList<>();

		points.add(new Point(0, 0)); // add start field to checkList

		int wave = 2;
		
		Point endPoint = new Point(width, height);

		while (!points.contains(endPoint) && thereIsPointsToCheck(points)) {
			markPointsByWave(this.field, points, wave);
			points = makeNewPointsList(this.field, points);
			wave++;
		}
		markPointsByWave(this.field, points, wave);
		
		filled = true;

	}

	private void copyField(int[][] originalField2, int[][] field2) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				field2[x][y] = originalField2[x][y];
			}
		}
		
	}

	private List<Point> makeNewPointsList(int[][] fieldInner, List<Point> points) {
		List<Point> result = new ArrayList<>();
		for (Point point : points) {
			result.addAll(getNearAvailablePoints(fieldInner, point));
		}
		return result;
	}

	private List<Point> getNearAvailablePoints(int[][] fieldInner, Point point) {
		List<Point> result = new ArrayList<>();
		if (point.x - 1 >= 0 && point.y - 1 >= 0 && fieldInner[point.x - 1][point.y - 1] == 1)
			result.add(new Point(point.x - 1, point.y - 1));
		if (point.y - 1 >= 0 && fieldInner[point.x][point.y - 1] == 1)
			result.add(new Point(point.x, point.y - 1));
		if (point.x + 1 < width && point.y - 1 >= 0 && fieldInner[point.x + 1][point.y - 1] == 1)
			result.add(new Point(point.x + 1, point.y - 1));
		if (point.x - 1 >= 0 && fieldInner[point.x - 1][point.y] == 1)
			result.add(new Point(point.x - 1, point.y));
		if (point.x + 1 < width && fieldInner[point.x + 1][point.y] == 1)
			result.add(new Point(point.x + 1, point.y));
		if (point.x - 1 >= 0 && point.y + 1 < height && fieldInner[point.x - 1][point.y + 1] == 1)
			result.add(new Point(point.x - 1, point.y + 1));
		if (point.y + 1 < height && fieldInner[point.x][point.y + 1] == 1)
			result.add(new Point(point.x, point.y + 1));
		if (point.x + 1 < width && point.y + 1 < height && fieldInner[point.x + 1][point.y + 1] == 1)
			result.add(new Point(point.x + 1, point.y + 1));
		return result;
	}

	private void markPointsByWave(int[][] fieldInner, List<Point> points, int wave) {
		for (Point point : points) {
			fieldInner[point.x][point.y] = wave;
		}
	}

	public boolean thereIsPointsToCheck(List<Point> points) {
		return points.size() > 0;
	}
	
	public List<Point> getShortestTrackReverse() {
		if (!this.filled) this.sythesizeWaveOnGraph();
		
		List<Point> result = new ArrayList<>();
		
		Point startingPoint = new Point(0, 0);
		Point point = new Point(width - 1, height - 1);
		
		int wave = this.field[point.x][point.y];
		
		if (wave == 1) return result;
		
		while (!point.equals(startingPoint)) {
			result.add(point);
			point = getNearestTrackPoint(this.field, point, wave);
			if (point == null) {
				System.out.println("error");
				return new ArrayList<>(); // no path found, error in wave function
			}
			wave = this.field[point.x][point.y];
		}
		result.add(point);
		
		return result;
	}

	private Point getNearestTrackPoint(int[][] fieldInner, Point point, int wave) {
		if (point.x - 1 >= 0 && point.y - 1 >= 0 && fieldInner[point.x - 1][point.y - 1] == wave - 1) {
			return new Point(point.x - 1, point.y - 1);
		}
		if (point.y - 1 >= 0 && fieldInner[point.x][point.y - 1] == wave - 1) {
			return new Point(point.x, point.y - 1);
		}
		if (point.x + 1 < width && point.y - 1 >= 0 && fieldInner[point.x + 1][point.y - 1] == wave - 1) {
			return new Point(point.x + 1, point.y - 1);
		}
		if (point.x - 1 >= 0 && fieldInner[point.x - 1][point.y] == wave - 1) {
			return new Point(point.x - 1, point.y);
		}
		if (point.x + 1 < width && fieldInner[point.x + 1][point.y] == wave - 1) {
			return new Point(point.x + 1, point.y);
		}
		if (point.x - 1 >= 0 && point.y + 1 < height && fieldInner[point.x - 1][point.y + 1] == wave - 1) {
			return new Point(point.x - 1, point.y + 1);
		}
		if (point.y + 1 < height && fieldInner[point.x][point.y + 1] == wave - 1) {
			return new Point(point.x, point.y + 1);
		}
		if (point.x + 1 < width && point.y + 1 < height && fieldInner[point.x + 1][point.y + 1] == wave - 1) {
			return new Point(point.x + 1, point.y + 1);
		}
		return null;
	}
	
	public String combineGraphAndRoute(List<Point> route) {
		StringBuilder sbl = new StringBuilder();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (route.contains(new Point(x, y))) {
					sbl.append("2 ");
				} else {
					sbl.append(this.originalField[x][y] + " ");
				}
			}
			sbl.append("\n");
		}
		return sbl.toString();
	}

}
