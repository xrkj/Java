package cn.tedu.shooter;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Bee extends FlyingObject
	implements Award{
	private static BufferedImage[] imgs;
	static{
		try{
			imgs = new BufferedImage[5];
			for(int i=0; i<imgs.length; i++){
				String png=
					"cn/tedu/shooter/bee"+i+".png";
				imgs[i]=ImageIO.read(
					Bee.class.getClassLoader()
					.getResourceAsStream(png));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public Bee() {
		super(60, 50);
		image = imgs[0];
		int[] dir={-2, 2};
		//0 1 
		direction=dir[(int)(Math.random()*2)];
	}
	
	private int direction;
	public void move(){
		super.move();
		if(state==ACTIVE){
			x+=direction;
			//y+=step;
			if(x>=480-width){
				direction = -2;
			}
			if(x<=0){
				direction = 2;
			}
		}
	}
	
	private int index=0;//下标序号
	protected BufferedImage nextImage() {
		index++;
		if(index >= imgs.length){
			return null;
		}
		return imgs[index];
	}
	
	/**
	 * 蜜蜂中的奖励算法
	 * 实现奖励方法
	 */
	@Override
	public int getAward() {
		int[] ary={LIFE, FIRE, DOUBLE_FIRE};
		int i=(int)(Math.random()*3);
		// i = 0 1 2 
		return ary[i];
	}
}






