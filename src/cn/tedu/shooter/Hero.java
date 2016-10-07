package cn.tedu.shooter;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Hero extends FlyingObject{
	
	private static BufferedImage[] imgs;
	static{
		imgs = new BufferedImage[6];
		try{
			for(int i=0; i<imgs.length; i++){
				
				//System.out.println(i);
				// hero0.png hero1.png
				// hero2.png hero3.png
				// hero4.png hero5.png  
				
				String png = 
				"cn/tedu/shooter/hero"+i+".png";
				//System.out.println(png);
				imgs[i]=ImageIO.read(Hero.class
					.getClassLoader()
					.getResourceAsStream(png)); 
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public Hero() {
		this.width = 97;
		this.height = 124;
		this.x = (480-width)/2;
		this.y = 500;
		this.image = imgs[0];
	}
	
	private int n = 0;
	public void move(){
		if(isActive()){
			n++;
			int i = n/2%2;
			image = imgs[i];
		}
		if(state==DEAD){
			//从子类对象中获取下一个照片
			BufferedImage img=nextImage();
			if(img==null){
				state=REMOVE;
			}else{
				image = img;
			}
		}
	}
	
	/**
	 * 在hero上添加射击方法
	 */
	public Bullet[] shoot(int type){
		int x=(int)(this.x+width/2-8/2);
		int y=(int)(this.y-15);
		if(type==1){//单枪
			return 
				new Bullet[]{new Bullet(x, y)};
		}
		if(type==2){//双枪
			return new Bullet[]{
					new Bullet(x-30, y),
					new Bullet(x+30, y)
			}; 
		}
		return new Bullet[0];
	}
	
	/**
	 * 在Hero中添加 move方法
	 */
	public void move(int x, int y){
		this.x = x-width/2;
		this.y = y-height/2;
	}
	
	/**
	 * HERO 中添加获取下一张图片的算法
	 * imgs = [0 1 2 3 4 5]
	 */
	private int index=1;//下标序号
	protected BufferedImage nextImage() {
		index++;
		if(index >= imgs.length){
			return null;
		}
		return imgs[index];
	}
	
	 
}













 