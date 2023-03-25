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
	private ArrayList<Point> itemPoint = new ArrayList<Point>(); //map 상에 있는 아이템의 좌표
	private int numOfItem; //map 상의 아이템 개수
	private boolean flag;
	private boolean isPlay; //게임의 진행 여부를 나타내는 flag
	//private String command; //유저가 입력한 커맨드.
	private int player; //커맨드를 입력한 플레이어
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

		// 맵을 디스플레이에 보냄
		this.reMap(this.map.getMapImage());
		
		for(int i=0; i<this.numOfUser; i++)
		{
			if(i<2)
				this.userDataList.add(new UserData(i,map.getStartLandPoint()));
			else
				this.userDataList.add(new UserData(i,map.getStartSkyPoint()));
			System.out.println(userDataList.get(i).getPoint());
			client.setChar(userDataList.get(i).getPoint(),i);//유저의 스타팅 포인트 display로 보내기
		}	
		
		upNDown(numOfUser);
		
		//client.setChar(userDataList.get(0).getPoint());//유저의 스타팅 포인트 display로 보내기
		
		
		r = new Random();
		r.setSeed(this.seed); //맵 스타팅포인트 시드랑 동일한 시드 사용
		//게임이 끝나면 flag를 false로 바꾼다.
	}

	// 10은 미사일 구경
	private boolean isLandCollision()
	{
		// 맵의 사이즈를 벗어날 경우
		if(this.missilePoint.x > this.map.getW() - 50
			|| this.missilePoint.x < 0
			|| this.missilePoint.y < 0 
			|| this.missilePoint.y > this.map.getH() - 50)
			// 너무 벗어난 경우
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
					if(userDataList.get(player).checkPowerShot()) //파워샷아이템을 썼을 경우 데미지 2배
					{
						userDataList.get(i).damaged(2 * userDataList.get(player).getDamage());
						System.out.println("[land]damged hp : "+userDataList.get(i).getHp());
					}
					else if(userDataList.get(player).checkAcidBoom()) //에시드붐아이템을 썻을 경우 중독
					{
						userDataList.get(i).damaged(userDataList.get(player).getDamage());
						userDataList.get(i).poisoned();
						System.out.println("[land]damged hp : "+userDataList.get(i).getHp());
					}
					else if(userDataList.get(i).checkShiled()) //쉴드아이템을 썼을 경우 피해량 1/2
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
		//미사일과 캐릭터의 충돌
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
				
				if(userDataList.get(player).checkPowerShot()) //파워샷아이템을 썼을 경우 데미지 2배
				{
					userDataList.get(i).damaged(2 * userDataList.get(player).getDamage());
					System.out.println("[sky]damged hp : "+userDataList.get(i).getHp());
				}
				else if(userDataList.get(player).checkAcidBoom()) //에시드붐아이템을 썻을 경우 중독
				{
					userDataList.get(i).damaged(userDataList.get(player).getDamage());
					userDataList.get(i).poisoned();
					System.out.println("[sky]damged hp : "+userDataList.get(i).getHp());
				}
				else if(userDataList.get(i).checkShiled()) //쉴드아이템을 썼을 경우 피해량 1/2
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
		
		//충돌이 없는 경우 false
		return false;
		
	}
	public void sendItemName(String itemName,int index)
	{
		client.sendItemName(itemName,index);
	}
	//missile좌표 얻기 method call할때마다 미사일 위치가 갱신됨
	public Point getMissilePoint(int player)
	{
		return userDataList.get(player).passPoint();		
	}


	// 앞으로 나갈 수 있는지 보는 메소드 + : right/- : left
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
	
	// 떨어지거나 올라가는 처리
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
				// 캐릭터 발바닥 밑에 한 줄을 체크함
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
	
	//계수 입력
	public void passCoefficient(int player, int exp3, int exp2, int exp1, int exp0)
	{
		userDataList.get(player).setFunction(exp3, exp2, exp1, exp0);
	}

	//v1.1에서 수정. 지속효과 적용(모든 플레이어)
	public void applyContinuousEffect(int n)
	{
		for(int i=0; i<n; i++)
			userDataList.get(i).checkContinuousState();
	}
	//아이템 획득
	public void getItem(String itemName, int player)
	{
		userDataList.get(player).getItem(itemName);
	}
	
	//index : 아이템 번호 (1 2 3 4) 
	public void useItem(int player, int index)
	{
		userDataList.get(player).applyItem(index-1); //arrayList의 index는 0부터 시작하므로 index-1
		System.out.println("[Process class] use item");
	}
	
	//map 상에 새로운 아이템 추가
	public void addItem(Point itemPoint)
	{
		this.itemPoint.add(itemPoint);
	}
	
	//map 상의 아이템 삭제 
	public void deleteItem(int index)
	{
		itemPoint.remove(index);
		numOfItem--;
	}

	/*
	//v1.1에서 추가 . 사용자가 입력한 command를 받는 함수 
	public void receiveCommand(String command)
	{
		this.command = command;
		receive = true;
	}
	*/
	// 서버로 전송 요청1:move:r:11:move:r:1
	public void complete()
	{
		// 자료를 어떻게 구조화할지 정해서 다음 메소드의 인자로 넣어야함
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

		/* 자료구조 = 플레이어 : event type : event option : createItem
		 * 
		 * event type		event option    
		 * 1) move			l or r
		 * 2) shoot			각 항의 계수
		 * 3) item			사용한 아이템 번호
		 */
	
		
		//while(receive);	//커맨드 입력이 넘어오기 전까진 대기한다.
		String[] cmdArray = command.split(":"); //서버로부터 받은 command를 토큰 별로 자르기
		player = Integer.parseInt(cmdArray[0])-1;
		eventType = cmdArray[1];
		eventOption = cmdArray[2];
		
		try{
			createItem = Integer.parseInt(cmdArray[3]);
		} catch(Exception e)
		{
			System.out.println("메시지 양식 오류 - 디폴트값 처리");
			createItem = 0;
		}
		
		
		System.out.println("[Process class] Input command : "+player+":"+eventType+":"+eventOption);
		/*나중에 구현. 키보드 입력은 좌,우 두가지 밖에 없어서 x좌표같은 경우 +1 -1을 해주면 되지만,
		 *  y좌표는 지형에따라 변경되므로 지형을 고려해서 캐릭터의 좌표를 변형해야됨 
		 */
		
		this.userDataList.get(player).setWait();

		//아이템 생성
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
			//폭파 반경에 캐릭터가 있을 경우 데미지 적용
			for(int i=0; i<numOfUser; i++)
			{
				if(missilePoint.getX()-5<= getUserPoint(i).getX() && missilePoint.getX()+5>= getUserPoint(i).getX()) //폭파 범위는 미사일의 충돌지점으로부터 반경 5로 설정
				{
					if(userDataList.get(player).checkPowerShot()) //파워샷아이템을 썼을 경우 데미지 2배
						userDataList.get(i).damaged(2 * userDataList.get(player).getDamage());
					else if(userDataList.get(player).checkAcidBoom()) //에시드붐아이템을 썻을 경우 중독
					{
						userDataList.get(i).damaged(userDataList.get(player).getDamage());
						userDataList.get(i).poisoned();
					}
					else if(userDataList.get(i).checkShiled()) //쉴드아이템을 썼을 경우 피해량 1/2
						userDataList.get(i).damaged(userDataList.get(player).getDamage()/2);
				}
			}*/
			applyContinuousEffect(numOfUser);//미사일이 지형과 충돌하면 턴이 끝난 것이므로 캐릭터들에게 지속효과를 적용시킨다.
		}
		
		else if(eventType.compareTo("item")==0)
		{
			//test 용
			getItem("powerShot",player);
			getItem("acidBoom",player);
			System.out.println("[Process class] item"); 
			int index = Integer.parseInt(eventOption);
			useItem(player,index);
		}
		
		// 자리 보정
		upNDown(numOfUser);
	}
}
