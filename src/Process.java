import java.util.ArrayList;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import Character.*;
import Item.*;
import Map.*;

public class Process {
	private ArrayList<UserData> userDataList = new ArrayList<UserData>();
	private Map map;
	private Client client;
	private ArrayList<Point> itemPoint = new ArrayList<Point>(); //map �� �ִ� �������� ��ǥ
	private int numOfItem; //map ���� ������ ����
	private boolean flag;
	private boolean isPlay; //������ ���� ���θ� ��Ÿ���� flag
	//private String command; //������ �Է��� Ŀ�ǵ�.
	private int player; //Ŀ�ǵ带 �Է��� �÷��̾�
	private int numOfUser;
	private Point missilePoint;
	private String eventType; 
	private String eventOption;
	private int seed;
	private Random r;
	private int createItem;
	
	public Process(int n, String selectedMapName,int seed, Client c)
	{
		this.numOfUser = n;
		this.numOfItem = 0;
		this.client = c;
		flag = true; 
		this.seed = seed;
		if(selectedMapName.compareTo("IceAge")==0)
		{
			this.map = new IceAge(this.seed);
			System.out.println("[Process class] selected map : IceAge");
		}
		else if(selectedMapName.compareTo("MarianaTrench")==0)
		{
			this.map = new MarianaTrench(this.seed);
			System.out.println("[Process class] selected map : MarianaTrench");
		}

		// ���� ���÷��̿� ����
		this.reMap(this.map.getMapImage());
		
		for(int i=0; i<this.numOfUser; i++)
		{
			if(i<2)
				this.userDataList.add(new UserData(i,map.getStartLandPoint()));
			else
				this.userDataList.add(new UserData(i,map.getStartSkyPoint()));
			System.out.println(userDataList.get(i).getPoint());
			client.setChar(userDataList.get(i).getPoint(),i);//������ ��Ÿ�� ����Ʈ display�� ������
		}	
		
		upNDown(numOfUser);
		
		//client.setChar(userDataList.get(0).getPoint());//������ ��Ÿ�� ����Ʈ display�� ������
		
		
		r = new Random();
		r.setSeed(this.seed); //�� ��Ÿ������Ʈ �õ�� ������ �õ� ���
		//������ ������ flag�� false�� �ٲ۴�.
	}

	// 10�� �̻��� ����
	private boolean isLandCollision()
	{
		// ���� ����� ��� ���
		if(this.missilePoint.x > this.map.getW() - 50
			|| this.missilePoint.x < 0
			|| this.missilePoint.y < 0 
			|| this.missilePoint.y > this.map.getH() - 50)
			// �ʹ� ��� ���
			if(this.missilePoint.x > this.map.getW()+100
					|| this.missilePoint.x < -150
					|| this.missilePoint.y < -1000 
					|| this.missilePoint.y > this.map.getH()+1000)
				return true;
			else
				return false;
		
		int result = map.getRangePiexels(this.missilePoint.x, this.missilePoint.y, 50, 50);
		
		System.out.println(result);
		
		if((result & 1) == 0)
		{
			this.map.bomb(this.missilePoint);
			this.reMap(this.map.getMapImage());
			for(int i=0; i<numOfUser; i++)
			{
				if(missilePoint.x-150<= userDataList.get(i).getPoint().x 
						&& missilePoint.x+150 >= userDataList.get(i).getPoint().x 
						&& missilePoint.y-125<= userDataList.get(i).getPoint().y
						&& missilePoint.y+125 >= userDataList.get(i).getPoint().y)
				{
					System.out.println("land damage");
					if(userDataList.get(player).checkPowerShot()) //�Ŀ����������� ���� ��� ������ 2��
					{
						userDataList.get(i).damaged(2 * userDataList.get(player).getDamage());
						System.out.println("[land]damged hp : "+userDataList.get(i).getHp());
					}
					else if(userDataList.get(player).checkAcidBoom()) //���õ�վ������� ���� ��� �ߵ�
					{
						userDataList.get(i).damaged(userDataList.get(player).getDamage());
						userDataList.get(i).poisoned();
						System.out.println("[land]damged hp : "+userDataList.get(i).getHp());
					}
					else if(userDataList.get(i).checkShiled()) //����������� ���� ��� ���ط� 1/2
					{
						userDataList.get(i).damaged(userDataList.get(player).getDamage()/2);
						System.out.println("[land]damged hp : "+userDataList.get(i).getHp());
					}
					else
					{
						userDataList.get(i).damaged(userDataList.get(player).getDamage());
						System.out.println("[land]damged hp : "+userDataList.get(i).getHp());
					}
				}
			}
			return true;
		}
		else
			return false;
	}
	
	private boolean isSkyCollision()
	{
		for(int i=0; i<numOfItem; i++)
		{	
			if(missilePoint.x>=itemPoint.get(i).x-25
					&& missilePoint.y>=itemPoint.get(i).y-25
					&& missilePoint.x<=itemPoint.get(i).x+75
					&& missilePoint.y<=itemPoint.get(i).y+75)
			{
				System.out.println("item collision");
				String[] itemName = new String[4];
				itemName[0] = "powerShot";
				itemName[1] = "shield";
				itemName[2] = "acidBoom";
				itemName[3] = "superVision";
				Random r = new Random();
				int ranNum = r.nextInt(4);
				getItem(itemName[ranNum],player);
				
				deleteItem(i);
				return true;
			}			
		}
		//�̻��ϰ� ĳ������ �浹
		for(int i=0; i<numOfUser; i++)
		{	
			if(player == i)
				continue;
			if(missilePoint.x>=userDataList.get(i).getPoint().x-25
					&& missilePoint.y>=userDataList.get(i).getPoint().y-25
					&& missilePoint.x<=userDataList.get(i).getPoint().x+125
					&& missilePoint.y<=userDataList.get(i).getPoint().y+125)
			{
				System.out.println("character collision" + i);
				
				if(userDataList.get(player).checkPowerShot()) //�Ŀ����������� ���� ��� ������ 2��
				{
					userDataList.get(i).damaged(2 * userDataList.get(player).getDamage());
					System.out.println("[sky]damged hp : "+userDataList.get(i).getHp());
				}
				else if(userDataList.get(player).checkAcidBoom()) //���õ�վ������� ���� ��� �ߵ�
				{
					userDataList.get(i).damaged(userDataList.get(player).getDamage());
					userDataList.get(i).poisoned();
					System.out.println("[sky]damged hp : "+userDataList.get(i).getHp());
				}
				else if(userDataList.get(i).checkShiled()) //����������� ���� ��� ���ط� 1/2
				{
					userDataList.get(i).damaged(userDataList.get(player).getDamage()/2);
					System.out.println("[sky]damged hp : "+userDataList.get(i).getHp());
				}
				else
				{
					userDataList.get(i).damaged(userDataList.get(player).getDamage());
					System.out.println("[sky]damged hp : "+userDataList.get(i).getHp());
				}
				return true;
			}
		}
		
		//�浹�� ���� ��� false
		return false;
		
	}
	public void sendItemName(String itemName,int index)
	{
		client.sendItemName(itemName,index);
	}
	//missile��ǥ ��� method call�Ҷ����� �̻��� ��ġ�� ���ŵ�
	public Point getMissilePoint(int player)
	{
		return userDataList.get(player).passPoint();		
	}


	// ������ ���� �� �ִ��� ���� �޼ҵ� + : right/- : left
	private boolean canGo(int dir)
	{
		boolean result = true;
		Point charPoint = userDataList.get(player).getPoint();
		
		if(dir > 0)
		{
			int front = map.getRangePiexels(charPoint.x + 80, charPoint.y, dir, 30);			
			if((front & 1) == 0)
				return false;
		}
		else
		{
			int back = map.getRangePiexels(charPoint.x + 20 - dir, charPoint.y, dir, 30);
			if((back & 1) == 0)
				return false;
		}
		
		return result;
	}
	
	// �������ų� �ö󰡴� ó��
	private void upNDown(int player_num)
	{
		boolean[] noComplete = new boolean[4];
		
		noComplete[0] = true;
		noComplete[1] = true;
		noComplete[2] = true;
		noComplete[3] = true;
		
		for(int i=4; i>player_num; i--)
			noComplete[i-1] = false;
		
		while(noComplete[0] || noComplete[1] || noComplete[2] || noComplete[3])
		{
			for(int i=0; i<player_num; i++)
			{
				// ĳ���� �߹ٴ� �ؿ� �� ���� üũ��
				Point charPoint = this.userDataList.get(i).getPoint();
				int down = map.getRangePiexels(charPoint.x + 40, charPoint.y + 70, 20, 5);
				int middle = map.getRangePiexels(charPoint.x + 40, charPoint.y + 30, 20, 40);
				
				if((middle & 1) == 0)
					this.userDataList.get(i).setPoint(0,-5);
				else if((down & 1) != 0)
					if(i == 0 || i == 1)
					{
						this.userDataList.get(i).setFall();
						this.userDataList.get(i).setPoint(0,5);
					}
					else
					{
						this.userDataList.get(i).setWait();
						noComplete[i] = false;
					}
				else
				{
					this.userDataList.get(i).setWait();
					noComplete[i] = false;
				}
				
				charPoint = this.userDataList.get(i).getPoint();
				this.client.setChar(charPoint,i);
			}
			try {
				Thread.sleep(70);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public Point getItems(int i)
	{
		return this.itemPoint.get(i);
	}
	
	public int getItemNum()
	{
		return this.itemPoint.size();
	}
	
	//��� �Է�
	public void passCoefficient(int player, int exp3, int exp2, int exp1, int exp0)
	{
		userDataList.get(player).setFunction(exp3, exp2, exp1, exp0);
	}

	//v1.1���� ����. ����ȿ�� ����(��� �÷��̾�)
	public void applyContinuousEffect(int n)
	{
		for(int i=0; i<n; i++)
			userDataList.get(i).checkContinuousState();
	}
	//������ ȹ��
	public void getItem(String itemName, int player)
	{
		userDataList.get(player).getItem(itemName);
	}
	
	//index : ������ ��ȣ (1 2 3 4) 
	public void useItem(int player, int index)
	{
		userDataList.get(player).applyItem(index-1); //arrayList�� index�� 0���� �����ϹǷ� index-1
		System.out.println("[Process class] use item");
	}
	
	//map �� ���ο� ������ �߰�
	public void addItem(Point itemPoint)
	{
		this.itemPoint.add(itemPoint);
	}
	
	//map ���� ������ ���� 
	public void deleteItem(int index)
	{
		itemPoint.remove(index);
		numOfItem--;
	}

	/*
	//v1.1���� �߰� . ����ڰ� �Է��� command�� �޴� �Լ� 
	public void receiveCommand(String command)
	{
		this.command = command;
		receive = true;
	}
	*/
	// ������ ���� ��û1:move:r:11:move:r:1
	public void complete()
	{
		// �ڷḦ ��� ����ȭ���� ���ؼ� ���� �޼ҵ��� ���ڷ� �־����
		this.client.sendData("complete");
	}	

	public int getUserHp(int i)
	{
		return this.userDataList.get(i).getHp();
	}
	
	public STATE getUserState(int i)
	{
		return this.userDataList.get(i).getState();
	}
	
	public int getUserDirection(int i)
	{
		return this.userDataList.get(i).getLastDirection();
	}
	
	public String getMapName()
	{
		return this.map.getName();
	}
	
	public String getItemName(int i, int n)
	{
		return this.userDataList.get(i).getItemName(n);
	}
	
	public int getUserItemNum(int i)
	{
		return this.userDataList.get(i).getNumOfItem();
	}
	
	public void reMap(BufferedImage img)
	{
		this.client.reMap(img);
	}
	
	public Point getUserPoint(int i)
	{
		return this.userDataList.get(i).getPoint();
	}
	
	public void processing(String command)
	{

		/* �ڷᱸ�� = �÷��̾� : event type : event option : createItem
		 * 
		 * event type		event option    
		 * 1) move			l or r
		 * 2) shoot			�� ���� ���
		 * 3) item			����� ������ ��ȣ
		 */
	
		
		//while(receive);	//Ŀ�ǵ� �Է��� �Ѿ���� ������ ����Ѵ�.
		String[] cmdArray = command.split(":"); //�����κ��� ���� command�� ��ū ���� �ڸ���
		player = Integer.parseInt(cmdArray[0])-1;
		eventType = cmdArray[1];
		eventOption = cmdArray[2];
		
		try{
			createItem = Integer.parseInt(cmdArray[3]);
		} catch(Exception e)
		{
			System.out.println("�޽��� ��� ���� - ����Ʈ�� ó��");
			createItem = 0;
		}
		
		
		System.out.println("[Process class] Input command : "+player+":"+eventType+":"+eventOption);
		/*���߿� ����. Ű���� �Է��� ��,�� �ΰ��� �ۿ� ��� x��ǥ���� ��� +1 -1�� ���ָ� ������,
		 *  y��ǥ�� ���������� ����ǹǷ� ������ ����ؼ� ĳ������ ��ǥ�� �����ؾߵ� 
		 */
		
		this.userDataList.get(player).setWait();

		//������ ����
		if(createItem == 1)
		{		
			int x = r.nextInt(this.map.getW());
			int y = r.nextInt(this.map.getH());
			int result = map.getRangePiexels(x, y, 1, 1);
			
			while((result & 1) == 0)
			{
				x = r.nextInt(this.map.getW());
				y = r.nextInt(this.map.getH());
				result = map.getRangePiexels(x, y, 1, 1);
			}
			Point item = new Point(x,y);
			itemPoint.add(item);
			numOfItem++;
			System.out.println("[Process class] item Point : " + item);
		}

		// move
		if(eventType.compareTo("move")==0)
		{
			System.out.println("[Process class] move");

			this.userDataList.get(player).setMove();
			
			if(eventOption.compareTo("r")==0)
			{
				System.out.println("[Process class] move right");
				int dir = userDataList.get(player).getSpeed();
				if(canGo(dir))
				{
					this.userDataList.get(player).setPoint(dir, 0);
				}
				this.userDataList.get(player).setLastDirection(1);
			}
			else if(eventOption.compareTo("l")==0)
			{
				int dir = -1 * userDataList.get(player).getSpeed();
				if(canGo(dir))
				{
					this.userDataList.get(player).setPoint(dir, 0);
				}
				this.userDataList.get(player).setLastDirection(-1);
			}
		}		
		
		else if(eventType.compareTo("shoot")==0)
		{
			System.out.println("[Process class] shoot");
			
			this.userDataList.get(player).setShoot();
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			String[] sExp = eventOption.split(",");
			int[] exp = new int[4];
			for(int i=0; i<4; i++)
				exp[i] = Integer.parseInt(sExp[i]);
		
			passCoefficient(player,exp[0],exp[1],exp[2],exp[3]);
			
			missilePoint =getUserPoint(player);
			while(!isLandCollision() && !isSkyCollision())
			{
				System.out.println("[Process class] isn't collision");
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				missilePoint = getMissilePoint(player);
				this.client.setMissile(missilePoint,player);
				System.out.println("[Process class]" + missilePoint);
			}
			Point initMissilePoint = new Point (-100,-100);
			this.client.setMissile(initMissilePoint, player);
			
			/*
			//���� �ݰ濡 ĳ���Ͱ� ���� ��� ������ ����
			for(int i=0; i<numOfUser; i++)
			{
				if(missilePoint.getX()-5<= getUserPoint(i).getX() && missilePoint.getX()+5>= getUserPoint(i).getX()) //���� ������ �̻����� �浹�������κ��� �ݰ� 5�� ����
				{
					if(userDataList.get(player).checkPowerShot()) //�Ŀ����������� ���� ��� ������ 2��
						userDataList.get(i).damaged(2 * userDataList.get(player).getDamage());
					else if(userDataList.get(player).checkAcidBoom()) //���õ�վ������� ���� ��� �ߵ�
					{
						userDataList.get(i).damaged(userDataList.get(player).getDamage());
						userDataList.get(i).poisoned();
					}
					else if(userDataList.get(i).checkShiled()) //����������� ���� ��� ���ط� 1/2
						userDataList.get(i).damaged(userDataList.get(player).getDamage()/2);
				}
			}*/
			applyContinuousEffect(numOfUser);//�̻����� ������ �浹�ϸ� ���� ���� ���̹Ƿ� ĳ���͵鿡�� ����ȿ���� �����Ų��.
		}
		
		else if(eventType.compareTo("item")==0)
		{
			//test ��
			getItem("powerShot",player);
			getItem("acidBoom",player);
			System.out.println("[Process class] item"); 
			int index = Integer.parseInt(eventOption);
			useItem(player,index);
		}
		
		// �ڸ� ����
		upNDown(numOfUser);
	}
}
