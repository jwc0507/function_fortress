import java.util.ArrayList;
import java.awt.*;

import Character.*;
import Item.*;
import Map.*;

//enum Ability {blind,push,none}

public class UserData {
	
	private Character_Type character;
	private ArrayList<Item> itemList = new ArrayList<Item>();
	private Shoot shoot;
	private CurrentState state;
	private Point currentPoint; //현재위치
	private int lastDirection; //v1.1에서 추가. 캐릭터가 현재 바라보고 있는 방향, 초기값은 1
	// n : 플레이어 번호
	//최진원 구현
	//플레이어 번호대로 캐릭터 종류가 고정으로 설정됨 1번 플레이어: 토끼, 2번: 거북이, 3번 해, 4번 구름
	public UserData(int n,Point startPoint)
	{
		//캐릭터 데이터는 추후에 다시 설정
		switch(n)
		{
		case 0 :
			character = new Rabbit();
			break;
		case 1 :
			character = new Turtle();
			break;
		case 2 :
			character = new Sun();
			break;
		case 3: 
			character = new Cloud();
			break;
		}
		this.lastDirection = 1;
		this.currentPoint = startPoint;
		//this.currentPoint = new Point(0,0);
		
		System.out.println("[UserData class] startPoint : "+currentPoint);
		this.shoot = new Shoot(currentPoint);//
		
		this.state = new CurrentState();
	}
	
	// Char, Item, Shoot, State에 관한 콜 메소드들
	//최진원 구현
	
	public STATE getState()
	{
		return state.getState();
	}
	public void setMove()
	{
		state.setMove();
	}
	public void setShoot()
	{
		state.setShoot();
	}
	public void setWait()
	{
		state.setWait();
	}
	public void setAttack()
	{
		state.setAttack();
	}
	public void setFall()
	{
		state.setFall();
	}
	public void setDie()
	{
		state.setDie();
	}

	public int getLastDirection()
	{
		return this.lastDirection;
	}
	
	//v1.1에서 수정. 유저가 아이템을 먹으면 먹은 순서대로 넘버링한다. 
	public void getItem(String itemName)
	{
		Item getItem;
		if(itemName.compareTo("powerShot")==0)
		{
			getItem = new PowerShot();
			itemList.add(getItem);
			System.out.println("[UserData class] get powerShot");
		}
		else if(itemName.compareTo("acidBoom")==0)
		{
			getItem = new AcidBoom();
			itemList.add(getItem);
			System.out.println("[UserData class] get acidBoom");
		}
		else if(itemName.compareTo("shield")==0)
		{
			getItem = new Shield();
			itemList.add(getItem);
			System.out.println("[UerData class] get shield");
		}
		else if(itemName.compareTo("superVision")==0)
		{
			getItem = new SuperVision();
			itemList.add(getItem);
			System.out.println("[UserData class] get superVision");
		}
	}

	//index : item의 index
	public void applyItem(int index)
	{
	
		String itemName = new String(itemList.get(index).getItemName());
		
		//아이템 적용
		if(itemName.compareTo("powerShot")==0)
		{	state.setPowerShot();
			System.out.println("[UserData class] use powerShot");
		}
		else if(itemName.compareTo("acidBoom")==0)
		{
			state.setAcidBoom();
			System.out.println("[UserData class] use acidBoom");
		}
		else if(itemName.compareTo("shield")==0)
		{
			state.setShield();
			System.out.println("[UserData class] use shield");
		}
		else if(itemName.compareTo("superVision")==0)
		{
			state.setSuperVison();
			System.out.println("[UserData class] use superVision");
		}
		//사용한 아이템 삭제
		itemList.remove(index);
	}
	
	//미사일 좌표 return
	public Point passPoint()
	{
		return shoot.getPoint();
	}
	
	//캐릭터 move. v1.1에서 수정. 마지막 방향을 parameter로 받도록 수정. 키보드 입력이 오른쪽이면 1 왼쪽이면 -1 
	public void setCurrentPoint(Point newPoint,int lastDirection)
	{
		currentPoint = newPoint;
		this.lastDirection = lastDirection;
	}
	
	public int getDamage()
	{
		return character.getDamage();
	}
	
	//v1.1에서 추가
	public void damaged(int damage)
	{
		if(this.character.getHp() > 0)
			character.damaged(damage);
		if(this.character.getHp() <= 0)
			this.setDie();		
	}
	public int getHp()
	{
		return character.getHp();
	}
	
	//함수 입력. v1.1에서 수정
	public void setFunction(int exp3, int exp2, int exp1, int exp0)
	{
		shoot.setFunction(exp3, exp2, exp1, exp0);
		shoot.setZeroPoint(this.lastDirection, this.currentPoint); // 현재 보고있는 방향을 parameter로 넘김
	}
	
	//독뎀,슈퍼비전과 같이 지속되는 상태에 대해서 체크함.
	public void checkContinuousState()
	{
	
		if(state.isPoisoned())
			character.damaged(10);//중독 상태면 hp -10
		else if(state.isSuperVison())
		{	//좌표표시
		}	
	}
	
	public boolean checkPowerShot()
	{
		return state.isPowerShot();
	}
	public boolean checkAcidBoom()
	{
		return state.isAcidBoom();
	}
	public boolean checkShiled()
	{
		return state.isShield();
	}
	public void poisoned()
	{
		state.setPoisoned();
	}
	public Point getPoint()
	{
		return this.currentPoint;
	}
	public void setPoint(int dir, int updown)
	{
		this.currentPoint.setLocation(this.getPoint().x + dir, this.getPoint().y + updown);
	}
	public void setLastDirection(int dir)
	{
		this.lastDirection = dir;
	}
	public int getSpeed()
	{
		return character.getSpeed();
	}
	public int getNumOfItem()
	{
		return this.itemList.size();
	}
	public String getItemName(int n)
	{
		return this.itemList.get(n).getItemName();
	}
}