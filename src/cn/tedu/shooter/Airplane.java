package cn.tedu.shooter;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Airplane extends FlyingObject
	implements Enemy{
	
	private static BufferedImage[] imgs;
	static{
		try{
			imgs = new BufferedImage[5];
			for(int i=0;i<imgs.length; i++){
				String png =
					"cn/tedu/shooter/airplane"+i+".png";
				imgs[i]=ImageIO.read(Airplane.class
					.getClassLoader()
					.getResourceAsStream(png));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public Airplane() {
		//飞机的出场位置是是随机的
		super(49, 36);
		image = imgs[0];
	}
	
	private int index=0;//下标序号
	protected BufferedImage nextImage() {
		index++;
		if(index >= imgs.length){
			return null;
		}
		return imgs[index];
	}
	
	@Override
	public int getScore() {
		return 1;
	}
}

