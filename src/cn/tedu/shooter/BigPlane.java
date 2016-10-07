package cn.tedu.shooter;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class BigPlane extends FlyingObject
	implements Enemy{

	private static BufferedImage[] imgs;
	static{
		try{
			imgs = new BufferedImage[5];
			for(int i=0; i<imgs.length; i++){
				String png=
					"cn/tedu/shooter/bigplane"+i+".png";
				imgs[i]=ImageIO.read(
					Bee.class.getClassLoader()
					.getResourceAsStream(png));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public BigPlane() {
		super(69, 99);
		this.image=imgs[0];
		life=3;
	}
	
	private int index=0;//下标序号
	protected BufferedImage nextImage() {
		index++;
		if(index >= imgs.length){
			return null;
		}
		return imgs[index];
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		if(isDead()){
			g.drawString(
				""+getScore(), (int)x, (int)y);
		}
	}
	
	@Override
	public int getScore() {
		return 5;
	}
}




