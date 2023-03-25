import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class Display extends Frame
{
	private boolean ready;
	
	private Client client;

	// enum 순서대로
	private String[] ImageName = new String[6];
	// 캐릭터 이름
	private String[] CharName = new String[4];

	private Image[] img_character;
	private Image[] img_missile;
	private Image[] img_item;
	private Image[] img_miniHealth = new Image[4];
	private BufferedImage img_map_bw, img_map_img;
	
	private Image offscreen;
	private Graphics gr_offscreen;
	
	private Point[] mPoint;
	private Point[] cPoint;
	
	final int FRAME_WIDTH = 1200;
	final int FRAME_HEIGHT = 800;
	
	private int MAP_WIDTH;
	private int MAP_HEIGHT;
	
	private Image img_box;
	private Image img_back, img_frame, img_frame2 ,img_photo, img_health, img_none;

	private Image img_map;
	
	private int[] pixel_img;
	private int[] pixel_bw;
	private int[] pixel_map;
	
	private Character character = new Character();
	private Background background = new Background();
	
	private int numOfUser;
	private int mynumber;
	private int[] itemImgIndex = new int[2];
//	private Item item = new Item();
	
	public Display(Client c, int n, int mynum)
	{		
		JPanel p = new JPanel();
		p.setBackground(Color.black);
		p.setVisible(true);
		
		this.ready = false;

		this.mynumber = mynum;
		
		this.client = c;
		this.numOfUser = n;
		Toolkit tk = Toolkit.getDefaultToolkit();
		
		this.img_character = new Image[4];
		this.img_missile = new Image[4];
		this.cPoint = new Point[4];
		this.mPoint = new Point[4];
		//this.img_miniHealth = new Image[numOfUser];
		
		//파일 이름
		this.ImageName[0] = "_dead.gif";
		this.ImageName[1] = "_attack.gif";
		this.ImageName[2] = "_move.gif";
		this.ImageName[3] = "_wait.gif";
		this.ImageName[4] = "_fall.gif";
		this.ImageName[5] = "_wait.gif";
		
		//캐릭터 이름
		this.CharName[0] = "Rabbit";
		this.CharName[1] = "Turtle";
		this.CharName[2] = "Sun";
		this.CharName[3] = "Cloud";
				
		
		//캐릭터 이미지 넣기
		this.img_missile[0] =  tk.getImage(".\\Charater\\Rabbit\\Rabbit_bul.gif");
		this.img_missile[1] = tk.getImage(".\\Charater\\Turtle\\Turtle_bul.gif");
		this.img_missile[2] = tk.getImage(".\\Charater\\Sun\\Sun_bul.gif");
		this.img_missile[3] = tk.getImage(".\\Charater\\Cloud\\Cloud_bul.gif");

		this.img_miniHealth[0] = tk.getImage(".\\Charater\\mini_Health.gif");
		this.img_miniHealth[1] = tk.getImage(".\\Charater\\mini_Health.gif");
		this.img_miniHealth[2] = tk.getImage(".\\Charater\\mini_Health.gif");
		this.img_miniHealth[3] = tk.getImage(".\\Charater\\mini_Health.gif");
		
		//아이템 이미지
		this.img_box = tk.getImage(".\\Item\\box.gif");		
				
		//this.img_miniHealth = tk.getImage(".\\Charater\\mini_Health.gif");
		
		/*
		for(int a = 0; a < numOfUser; a++)
		{
			this.img_miniHealth[a] = tk.getImage(".\\Charater\\mini_Helth.gif");
		}*/
		
		/////////////////////////////////////////////////
		
		this.img_frame = tk.getImage(".\\Frame\\frame.gif");
		this.img_frame2 = tk.getImage(".\\Frame\\frame2.gif");
		this.img_health = tk.getImage(".\\Frame\\Health.gif");
		this.img_none = tk.getImage(".\\Frame\\none.gif");
		
		
		/////////////////////////////////////////////////
		
		
		/////////////////////////////////////////////////
		for(int i=0; i<numOfUser; i++)
			mPoint[i] = new Point(-100,-100);
		//클라이언트창 x 버튼 화렁화
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});	
	}
	
	public void paint(Graphics g)
	{
		
		if(!ready)
			return;

		//////////////////////////////////////////////

		int screen_x, screen_y;

		screen_x = cPoint[2].x - (FRAME_WIDTH/2);
		screen_y = cPoint[2].y - (FRAME_HEIGHT/2);

		if(screen_x < 0)
			screen_x = 0;
		else if(screen_x > MAP_WIDTH-FRAME_WIDTH)
			screen_x = MAP_WIDTH-FRAME_WIDTH;
		if(screen_y < 0)
			screen_y = 0;
		else if(screen_y > MAP_HEIGHT-FRAME_HEIGHT)
			screen_y = MAP_HEIGHT-FRAME_HEIGHT;
		
		
		gr_offscreen.clearRect(0, 0, MAP_WIDTH, MAP_HEIGHT);
		
		//// 배경 구현 필요 
		gr_offscreen.drawImage(img_back, screen_x, screen_y, screen_x + FRAME_WIDTH, screen_y + FRAME_HEIGHT, this);
		// 배경 이미지 출력
		gr_offscreen.drawImage(img_map, 0, 0, this);
		// 맵 이미지 출력
		
		/////////////////////////////////////////////////////////////////////
		
		gr_offscreen.drawImage(img_health, 200, 730, img_health.getWidth(this)*12/10*this.client.getUserHp(mynumber)/100, img_health.getHeight(this), this);
	
		gr_offscreen.drawImage(img_none,265,655,img_none.getWidth(this)*12/10,img_none.getHeight(this),this);
		gr_offscreen.drawImage(img_none,365,655,img_none.getWidth(this)*12/10,img_none.getHeight(this),this);

		// 아이템 출력
		img_item = new Image[this.client.getUserItemNum(mynumber)];
		
		for(int i=0; i<this.client.getUserItemNum(mynumber); i++)
		{
			this.img_item[i] = Toolkit.getDefaultToolkit().getImage(".\\Item\\" + this.client.getItemName(mynumber, i) + ".gif");
			gr_offscreen.drawImage(img_item[i],265+100*(i%2),655,img_item[i].getWidth(this)*12/10,img_item[i].getHeight(this),this);
		}
		
		this.img_photo = Toolkit.getDefaultToolkit().getImage(".\\Charater\\"+ this.CharName[mynumber]
				+"\\"+ this.CharName[mynumber] +"_photo2.gif");
		gr_offscreen.drawImage(img_photo, 90, 685, img_photo.getWidth(this)*12/10 , img_photo.getHeight(this), this);
		
		gr_offscreen.drawImage(img_frame, -15, 635, img_frame.getWidth(this)*12/10, img_frame.getHeight(this),this );
		gr_offscreen.drawImage(img_frame2, 600, 600, this);
		
		
		/////////////////////////////////////////////////////////////////////
		
		
		for(int i=0; i<numOfUser; i++)
		{
			if(this.client.getUserState(i) == STATE.shoot)
				gr_offscreen.drawImage(img_missile[i], mPoint[i].x, mPoint[i].y, this);
				// 미사일
			
			this.img_character[i] = Toolkit.getDefaultToolkit().getImage(".\\Charater\\"+ this.CharName[i]
					+"\\"+ this.CharName[i] + this.ImageName[this.client.getUserState(i).ordinal()]);
			
			// 캐릭터가 this.client.getUserDirection(i)에 따라 뒤집히는 것을 구현하기 위해 +50이랑 이상하게 좀 됨..
			gr_offscreen.drawImage(img_character[i], cPoint[i].x + 50 + -50*this.client.getUserDirection(i), cPoint[i].y, this.client.getUserDirection(i)*100, 100, this);
			gr_offscreen.drawImage(img_miniHealth[i], cPoint[i].x, cPoint[i].y, this.client.getUserHp(i), 5, this);
			
			// 캐릭터 \
		//	System.out.println("[Display class] cPoint"+i+" : " + cPoint[i]);
		}
		
		for(int i=0; i < this.client.getItemNum(); i++)
		{
			Point ip = this.client.getItems(i);
			gr_offscreen.drawImage(img_box, ip.x, ip.y, this);			
		}
		
//		System.out.println(screen_x + ", " + screen_y);	
		
		g.drawImage(offscreen, 0, 0, FRAME_WIDTH, FRAME_HEIGHT, screen_x, screen_y, screen_x+FRAME_WIDTH, screen_y+FRAME_HEIGHT, this);
		
		g.dispose();
	}
	
	public void update(Graphics g)
	{
		paint(g);
	}

	public void setMissilePoint(Point mp,int index)
	{	
		this.mPoint[index] = mp;
		repaint();
	}
	
	public void setCharPoint(Point cp,int index)
	{
		this.cPoint[index] = cp;
		repaint();
	}

	public void reMap(BufferedImage img)
	{
		this.img_map_bw = img;
		
		setMap();
	}

	public void setMap()
	{
		this.img_map_bw.getRGB(0, 0, MAP_WIDTH, MAP_HEIGHT, this.pixel_bw, 0, MAP_WIDTH);
		
		for(int i=0; i<MAP_HEIGHT ; i++)
			for(int j=0; j<MAP_WIDTH; j++)
				this.pixel_map[i*MAP_WIDTH + j] = (this.pixel_bw[i*MAP_WIDTH + j] ^ 0x00ffffff) & this.pixel_img[i*MAP_WIDTH + j];
		
		this.img_map = createImage(new MemoryImageSource(MAP_WIDTH,MAP_HEIGHT,this.pixel_map,0,MAP_WIDTH));
		repaint();
	}
	
	public void InitDisplay()
	{
		
		try {
			this.img_map_img = ImageIO.read(new File(".\\Map\\"+this.client.getMapName()+"\\"+this.client.getMapName()+".gif"));		
		} catch (Exception e) {
			System.out.println("map load error");
		}
	
		
		this.img_back = Toolkit.getDefaultToolkit().getImage(".\\Map\\"+this.client.getMapName()+"\\"+this.client.getMapName()+"_bg.jpg");
				
		this.MAP_WIDTH = this.img_map_bw.getWidth();
		this.MAP_HEIGHT = this.img_map_bw.getHeight();

		this.pixel_img = new int[MAP_WIDTH * MAP_HEIGHT];
		this.pixel_bw = new int[MAP_WIDTH * MAP_HEIGHT];
		this.pixel_map = new int[MAP_WIDTH * MAP_HEIGHT];

		this.img_map_img.getRGB(0, 0, MAP_WIDTH, MAP_HEIGHT, this.pixel_img, 0, MAP_WIDTH);
	
		
		this.offscreen = this.createImage(MAP_WIDTH,MAP_HEIGHT);
		this.gr_offscreen = this.offscreen.getGraphics();
		
		this.setMap();

		this.ready = true;
	}
	

	public void showItemImage(String itemName, int index)
	{
		if(itemName.compareTo("powerShot")==0)
		{
			System.out.println("get powerShot");
			itemImgIndex[index]=1;
		}
		else if(itemName.compareTo("acidBoom")==0)
		{
			itemImgIndex[index]=2;
		}
		else if(itemName.compareTo("shield")==0)
		{
			itemImgIndex[index]=3;
		}
		else if(itemName.compareTo("superVision")==0)
		{
			itemImgIndex[index]=4;
		}
		
	}
		
}