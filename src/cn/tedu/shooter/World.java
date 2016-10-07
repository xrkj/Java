package cn.tedu.shooter;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class World extends JPanel{
	/** 一片天空 */
	private Sky sky;
	/** 一个英雄飞机 **/
	private Hero hero;
	/** 英雄打出的多个子弹 **/
	private Bullet[] bullets;
	/** 飞行的物体(大飞机,小飞机,蜜蜂等) **/
	private FlyingObject[] flyingObjects;
	
	/** 计分变量 */
	private int score=0, life=3, fireType=1; 
	
	public static final int READY=0;
	public static final int RUNNING=1;
	public static final int PAUSE=2;
	public static final int GAME_OVER=3;
	
	private int state = READY;
	
	private static BufferedImage pause;
	private static BufferedImage ready;
	private static BufferedImage gameOver;
	
	static{
		try {
			String png = "cn/tedu/shooter/start.png";
			ready = ImageIO.read(World.class.getClassLoader().getResourceAsStream(png));
			png = "cn/tedu/shooter/pause.png";
			pause = ImageIO.read(World.class.getClassLoader().getResourceAsStream(png));
			png = "cn/tedu/shooter/gameover.png";
			gameOver = ImageIO.read(World.class.getClassLoader().getResourceAsStream(png));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public World() {
		sky = new Sky();
		hero = new Hero();
		bullets = new Bullet[]{
			new Bullet(50,650),
			new Bullet(50,700)};
		flyingObjects = 
			new FlyingObject[]{
				new Bee(),
				new Airplane(),
				new BigPlane()};
		nextTime = 
			System.currentTimeMillis()+1000;
	}
	private long nextTime;
	/** nextOne 方法被定时(1/24秒)调用 **/
	public void nextOne(){
		long now = System.currentTimeMillis();
		if(now>=nextTime){
			nextTime = now+1000;
			FlyingObject obj=randomOne();
			flyingObjects=Arrays.copyOf(
				flyingObjects, 
				flyingObjects.length+1);
			flyingObjects[flyingObjects.length-1]=obj;
		}
	}
	
	private static FlyingObject randomOne(){
		int n = (int)(Math.random()*25);
		switch(n){
		case 0: return new Bee();
		case 1: 
		case 2: return new BigPlane();
		case 3: return new BigPlaneAward();
		default: return new Airplane();
		}
	}
	
	
	public String toString(){
		return  sky + ", \n" + 
			hero + ", \n" +
			Arrays.toString(bullets)+", \n"+
			Arrays.toString(flyingObjects); 				
	}
	
	/** 
	 * 在World类中修改JPanel
	 * 重写JPanel 提供的绘制方法, 修改其绘制
	 * 功能. 替换了原有的绘制功能. 
	 **/
	public void paint(Graphics g){
		//Graphics g 是一个画笔对象
		//由Java Swing提供的画笔对象
		//g.drawString(
		//	"Hello World!", 100, 150); 
		//g.drawLine(10, 10, 80, 20); 
		
		sky.paint(g);
		hero.paint(g); 
		for(int i=0; i<bullets.length; i++){
			bullets[i].paint(g);
		}
		for(int i=0; 
			i<flyingObjects.length; i++){
			// i = 0 1 2
			//flyingObjects[i].y=100;//测试代码
			flyingObjects[i].paint(g);
		}
		//在Word 的 paint方法中增加分数显示
		g.drawString("SCORE:"+score,20,30);
		g.drawString("LIFE:"+life,20,50);
		g.drawString("FIRE:"+fireType,20,70);
	
		switch(state){
		case PAUSE:
			g.drawImage(pause, 0, 0, null);
			break;
		case READY:
			g.drawImage(ready, 0, 0, null);
			break;
		case GAME_OVER:
			g.drawImage(gameOver, 0, 0, null);
			break;
		}
	}
	/**
	 * 在World 类中添加定时器和启动定时器的方法
	 */
	private Timer timer;
	public void action(){
		
		//开始鼠标的监听
		MouseAdapter l=new MouseAdapter(){
			@Override
			public void mouseMoved(MouseEvent e){
				if(state==RUNNING){
					//e.getWhen()// long
					int x = e.getX();
					int y = e.getY();
					hero.move(x, y);
					//System.out.println("HI");
				}
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				if(state==RUNNING){
					state = PAUSE;
				}
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				if(state==PAUSE){
					state = RUNNING;
				}
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(state==READY){
					state = RUNNING;
				}
				if(state==GAME_OVER){
					score=0;
					life=3;
					fireType=1;
					hero = new Hero();
					bullets=new Bullet[0];
					flyingObjects=new FlyingObject[0];
					state=READY;
				}
			}
		};
		//将监听器加入到 当前面板中
		addMouseListener(l);
		addMouseMotionListener(l); 
		
		timer = new Timer();
		timer.schedule(new TimerTask(){
			public void run(){
				if(state==RUNNING){
					nextOne();
					move();
					shoot();//射击封装到shoot方法
					//重新绘制jpanel
					//删除掉那些需要删除的对象
					//凡是状态是 REMOVE 的子弹/飞机
					//都需要删除
					removeObjects();
					//检查碰撞情况
					duangDuang();
					//控制英雄的生命周期
					heroLifeCircle();
				}
				//System.out.println(flyingObjects.length);
				repaint();//来自JPanel的方法
			}


		},0,1000/24);
	}
	
	/**
	 * 在World中添加英雄生命周期控制方法
	 */
	public void heroLifeCircle(){
		if(hero.isActive()){
			for(FlyingObject plane:
				flyingObjects){
				if(plane.isActive() && 
					plane.duang(hero)){
					hero.goDead();
					plane.goDead();
				}
			}
		}
		if(hero.canRemove()){
			if(life>0){
				life--;
				hero = new Hero();
				//清场
				for(FlyingObject plane:
					flyingObjects){
					if(plane.isActive()&&
						plane.duang(hero)){
						plane.goDead();
					}
				}
			}else{
				state=GAME_OVER;
			}
		}
	}
	
	
	/**
	 * 在World里面添加碰撞检查方法
	 */
	public void duangDuang(){
		for(FlyingObject plane:flyingObjects){
			if(plane.isActive()){
				if(shootByBullet(plane)){
					plane.hit();
					if(plane.isDead()){
						//计分
						//转换为子类型才能进行计分
						if(plane instanceof Enemy){
							Enemy enemy=(Enemy)plane;
							int s = enemy.getScore();
							score+=s;
							//System.out.println(score);
						}
						if(plane instanceof Award){
							Award award=(Award)plane;
							int awd = award.getAward();
							if(awd==Award.LIFE){
								life++;
							}else if(awd==Award.FIRE){
								fireType = 1;
							}else if(awd==Award.DOUBLE_FIRE){
								fireType = 2;
							}
						}
					}
				}
			}
		}
	}
	
	public boolean shootByBullet(
			FlyingObject plane){
		for(Bullet bullet:bullets){
			//如果子弹是活动的时候
			if(bullet.isActive()){
				//检查子弹打到飞机没有
				if(bullet.duang(plane)){
					//打到以后,子弹打掉
					bullet.hit();
					return true;
				}	
			}
		}
		return false;
	}
	
	
	
	
	
	
	
	/**
	 * 删除掉没有用的子弹和飞机
	 */
	public void removeObjects() {
		Bullet[] ary = {};
		for(Bullet b:bullets){
			if(b.canRemove()){
				//忽略需要删除的子弹
				continue;
			}
			ary=Arrays.copyOf(
					ary, ary.length+1);
			ary[ary.length-1]=b;
		}
		bullets = ary;
		
		//删除掉废弃的飞行物
		FlyingObject[] ary1={};
		for(FlyingObject obj:flyingObjects){
			if(obj.canRemove()){
				continue;
			}
			ary1=Arrays.copyOf(
				ary1, ary1.length+1);
			ary1[ary1.length-1]=obj;
		}
		flyingObjects=ary1;
	}
	
	/*
	 * 测试删除掉一个子弹和一个飞机
	 */
	public void testRemove(){
		bullets[0].hit();
		flyingObjects[0].hit();
		flyingObjects[0].state=
				FlyingObject.REMOVE;
	}
	
	/**
	 * 射击控制方法
	 * 被主定时器定时调用
	 */
	private long nextShootTime;
	public void shoot(){
		//控制射击速度
		long now=System.currentTimeMillis();
		if(now>nextShootTime){
			nextShootTime=now+300;
			Bullet[] ary=hero.shoot(fireType);
			//将子弹 ary 添加到 bullets数组
			bullets=Arrays.copyOf(bullets,
				bullets.length+ary.length);
			System.arraycopy(
				ary, 0, bullets,
				bullets.length-ary.length , 
				ary.length);
		}
	}
	public void move() {
		sky.move();
		hero.move();
		//每个飞机移动一下
		for(int i=0; 
			i<flyingObjects.length; 
			i++){
			flyingObjects[i].move();
		}
		for(Bullet b:bullets){
			b.move();
		}
	}

	/** 在World中添加main方法, 显示图形界面 **/
	public static void main(String[] args){
		JFrame frame = new JFrame();
		frame.setSize(480, 652);
		frame.setDefaultCloseOperation(
			JFrame.EXIT_ON_CLOSE);
		//窗口居中
		frame.setLocationRelativeTo(null);
		//JPanel panel = new JPanel();
		//panel.setBackground(Color.BLACK);
		//frame.add(panel);
		World world = new World();
		frame.add(world);
		//frame窗口在显示时候, 会自动调用
		// paint方法, 如果重写paint, 则显示
		// 显示时候自动执行 重写以后的paint
		frame.setVisible(true); 
		world.action();//启动定时器
	}
	

}







