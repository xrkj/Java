package cn.tedu.shooter;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public abstract class FlyingObject {
	/** double类型的坐标可以实现更加精细的飞机
	 * 位置控制 **/
	protected double x, y, width, height;

	/** 当前正在显示的图片引用 **/
	protected BufferedImage image;
	
	/** 飞行物每次(1/24秒)移动的距离 **/
	protected double step;
	
	/** 命 **/
	protected int life;
	
	/** 飞行物状态 **/
	protected int state;
	
	public static final int ACTIVE=0;
	public static final int DEAD=1;
	public static final int REMOVE=2;
		
	/** 小飞机\大飞机\蜜蜂等调用的构造器 **/
	public FlyingObject(
			int width, int height){
		this();
		this.width = width;
		this.height=height;
		x = Math.random()*(480-width);
		y = -height;
		step = Math.random()*3+0.8;
	}
	/** 子弹\英雄等调用的构造器 **/
	public FlyingObject(){
		life=1;
		state=ACTIVE;
	}
	
	/** 在FlyingObject中添加toString方法 **/
	public String toString(){
		return x+","+y+","+width+","+
				height+", "+life+","+
				state+", " +image;
	}
	
	/**
	 * 在FlyingObject类上添加 paint 方法
	 * 利用画笔对象 g 绘制当前图片
	 * @param g 传递来的画笔的引用
	 */
	public void paint(Graphics g){
		g.drawImage(
			image, (int)x, (int)y, null);
	}
	
	public void move(){
		/*
		 * 重构move, 实现播放销毁动画功能 
		 */
		if(state == ACTIVE){
			y += step;
			if(y>=852){
				state=REMOVE;
			}
			return;
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
	 * 子类中必须有的方法, 返回下一个要播放
	 * 的照片引用, 如果返回null表示没有可
	 * 播放的照片了.
	 * @return
	 */
	protected abstract BufferedImage 
		nextImage();
	
	/**
	 * 被打了一下
	 */
	public void hit(){
		if(life>0){
			life--;
		}
		if(life==0){
			state=DEAD;
		}
	}
	
//	/**
//	 * 出界检查方法
//	 */
//	public void outBounds(){
//		if(y>=852){
//			state=REMOVE;
//		}
//	}
	
	/**
	 * 在FlyingObject 方法中添加 碰撞检查方法
	 */
	public boolean duang(FlyingObject obj){
		double x1, x2, y1, y2;
		x1=this.x-obj.width;
		x2=this.x+this.width;
		y1=this.y-obj.height;
		y2=this.y+this.height;
		//System.out.println(x1.x2.y1,y2);
		return x1<obj.x && obj.x<x2 &&
				y1<obj.y && obj.y<y2;
	}
	
	/**
	 * 重构FlyingObject 添加状态检查方法
	 */
	/** 飞行物是否是活动的 */
	public boolean isActive(){
		return state == ACTIVE;
	}
	/** 检查飞行物死了吗? **/
	public boolean isDead(){
		return state == DEAD;
	}
	/** 检查飞行物是否可以被删除了 **/
	public boolean canRemove(){
		return state == REMOVE;
	}
	
	/** 飞行物添加 "去死" 方法  */
	public void goDead(){
		if(isActive()){
			state = DEAD;
		}
	}
}













