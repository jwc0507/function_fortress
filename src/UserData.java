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
	private Point currentPoint; //������ġ
	private int lastDirection; //v1.1���� �߰�. ĳ���Ͱ� ���� �ٶ󺸰� �ִ� ����, �ʱⰪ�� 1
	// n : �÷��̾� ��ȣ
	//������ ����
	//�÷��̾� ��ȣ��� ĳ���� ������ �������� ������ 1�� �÷��̾�: �䳢, 2��: �ź���, 3�� ��, 4�� ����
	public UserData(int n,Point startPoint)
	{
		//ĳ���� �����ʹ� ���Ŀ� �ٽ� ����
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
	
	// Char, Item, Shoot, State�� ���� �� �޼ҵ��
	//������ ����
	
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
	
	//v1.1���� ����. ������ �������� ������ ���� ������� �ѹ����Ѵ�. 
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

	//index : item�� index
	public void applyItem(int index)
	{
	
		String itemName = new String(itemList.get(index).getItemName());
		
		//������ ����
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
		//����� ������ ����
		itemList.remove(index);
	}
	
	//�̻��� ��ǥ return
	public Point passPoint()
	{
		return shoot.getPoint();
	}
	
	//ĳ���� move. v1.1���� ����. ������ ������ parameter�� �޵��� ����. Ű���� �Է��� �������̸� 1 �����̸� -1 
	public void setCurrentPoint(Point newPoint,int lastDirection)
	{
		currentPoint = newPoint;
		this.lastDirection = lastDirection;
	}
	
	public int getDamage()
	{
		return character.getDamage();
	}
	
	//v1.1���� �߰�
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
	
	//�Լ� �Է�. v1.1���� ����
	public void setFunction(int exp3, int exp2, int exp1, int exp0)
	{
		shoot.setFunction(exp3, exp2, exp1, exp0);
		shoot.setZeroPoint(this.lastDirection, this.currentPoint); // ���� �����ִ� ������ parameter�� �ѱ�
	}
	
	//����,���ۺ����� ���� ���ӵǴ� ���¿� ���ؼ� üũ��.
	public void checkContinuousState()
	{
	
		if(state.isPoisoned())
			character.damaged(10);//�ߵ� ���¸� hp -10
		else if(state.isSuperVison())
		{	//��ǥǥ��
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