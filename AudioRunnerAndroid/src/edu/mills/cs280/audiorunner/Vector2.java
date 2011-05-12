package edu.mills.cs280.audiorunner;


public class Vector2 implements Comparable<Object>{

	public int x;
	public int y;

	public Vector2(){
		x = 0;
		y = 0;
	}

	public Vector2(int x,int y){
		this.x = x;
		this.y = y;
	}

	public void set(int x,int y){
		this.x = x;
		this.y = y;
	}

	public int getX(){
		return x;
	}

	public void setX(int x){
		this.x = x;
	}

	public int getY(){
		return y;
	}

	public void setY(int y){
		this.y = y;
	}

	public float distanceFrom(Vector2 pos){
		return (float) Math.sqrt(Math.pow(this.x-pos.x,2) + Math.pow(this.y-pos.y,2));
	}

	public float distanceFrom(int x,int y){
		return (float) Math.sqrt(Math.pow(this.x-x,2) + Math.pow(this.y-y,2));
	}

	@Override
	public int compareTo(Object obj)
	{
		if(this.x < ((Vector2) obj).getX())
			return -1;
		else if(this.x > ((Vector2) obj).getX())
			return 1;
		else if(this.y < ((Vector2) obj).getY())
			return -1;
		else if(this.y > ((Vector2) obj).getY())
			return 1;
		else
			return 0;
	}

}