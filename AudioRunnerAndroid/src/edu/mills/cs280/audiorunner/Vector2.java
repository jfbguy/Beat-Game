package edu.mills.cs280.audiorunner;

/**
 * Vector2 is a class that holds x and y coordinates
 * 
 * @author Joseph
 * 
 */
public class Vector2 implements Comparable<Object> {

	public int x;
	public int y;

	/**
	 * Assigns x and y to start at position 0
	 */
	public Vector2() {
		x = 0;
		y = 0;
	}

	/**
	 * Assigns x and y variables
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 */
	public Vector2(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Sets the x and y coordinates
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 */
	public void set(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns the position of x
	 * 
	 * @return x the x coordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the position of x
	 * 
	 * @param x
	 *            the x coordinate
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Returns the position of y
	 * 
	 * @return y the y coordinate
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the position of y
	 * 
	 * @param y
	 *            the y coordinate
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Returns distance as a float between the two points which will be the
	 * square root + raised to the specified power.
	 * 
	 * @param pos
	 *            the current position coordinate
	 * @return returns distance of the two points
	 */
	public float distanceFrom(Vector2 pos) {
		return (float) Math.sqrt(Math.pow(this.x - pos.x, 2)
				+ Math.pow(this.y - pos.y, 2));
	}

	/**
	 * Returns distance as a float between x and y
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @return the distance as a float between the two points which will be the
	 *         square root + raised to the specified power
	 */
	public float distanceFrom(int x, int y) {
		return (float) Math.sqrt(Math.pow(this.x - x, 2)
				+ Math.pow(this.y - y, 2));
	}

	@Override
	public int compareTo(Object obj) {
		if (this.x < ((Vector2) obj).getX())
			return -1;
		else if (this.x > ((Vector2) obj).getX())
			return 1;
		else if (this.y < ((Vector2) obj).getY())
			return -1;
		else if (this.y > ((Vector2) obj).getY())
			return 1;
		else
			return 0;
	}

}