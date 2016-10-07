package cn.tedu.shooter;

import java.awt.Graphics;

public class BigPlaneAward 
	extends BigPlane
	implements Award{
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawRect((int)x, (int)y, 
				(int)width, (int)height);
	}
	public int getAward() {
		return DOUBLE_FIRE;
	}
}
