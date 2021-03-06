package cn.tedu.shooter;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Bullet extends FlyingObject {
	
	private static BufferedImage img;
	static{
		String png=
			"cn/tedu/shooter/bullet.png";
		try{
			img=ImageIO.read(
				Bullet.class.getClassLoader()
				.getResourceAsStream(png));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public Bullet(int x, int y) {
		this.x = x;
		this.y = y;
		width = 8;
		height = 14;
		this.image = img;
	}
	
	public void move() {
		if(state == ACTIVE){
			y-=8;
			if(y<=-height){
				state=REMOVE;
			}
		}
	}
 
	protected BufferedImage nextImage() {
		return null;
	}

	/**
	 * 在Bullet中重写hit方法, 在子弹被打击的
	 * 时候, 就变成REMOVE状态
	 */
	public void hit() {
		state = REMOVE;
	}
}








