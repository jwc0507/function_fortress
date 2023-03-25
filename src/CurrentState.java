import java.awt.Point;

enum STATE{die,shoot,move,wait,fall,attack};

public class CurrentState {
	private STATE currentState;
	private boolean powerShot;
	private boolean acidBoom;
	private boolean poisoned;
	private boolean shield;
	private boolean superVision;
	private int PLT;//poisoned left turn : poison ȿ���� ��������� ���� �� 
	private int SVLT;//superVision left turn : superVision ȿ���� ��������� ���� ��
	public CurrentState()
	{
		this.currentState = STATE.wait;
		// do nothing
	}

	public STATE getState()
	{
		return this.currentState;
	}
	public void setMove()
	{
		if(this.currentState != STATE.die)
			this.currentState = STATE.move;
	}
	public void setShoot()
	{
		if(this.currentState != STATE.die)
			this.currentState = STATE.shoot;
	}
	public void setWait()
	{
		if(this.currentState != STATE.die)
			this.currentState = STATE.wait;
	}
	public void setAttack()
	{
		if(this.currentState != STATE.die)
			this.currentState = STATE.attack;
	}
	public void setFall()
	{
		if(this.currentState != STATE.die)
			this.currentState = STATE.fall;
	}
	public void setDie()
	{
		this.currentState = STATE.die;
	}
	
	public void setPowerShot()
	{
		powerShot = true;
	}
	public void setAcidBoom()
	{
		acidBoom = true;
	}
	public void setPoisoned()
	{
		PLT =3;
		poisoned = true;
	}
	public void setShield()
	{
		this.shield = true;
	}
	public void setSuperVison()
	{
		SVLT = 3;
		this.superVision = true;
	}
	
	/*public Point getLocation()
	{
		return this.location;
	}*/
	
	//�Ͻ�ȿ�� -> ������ ȿ���� ���� ���ȸ� ����ǹǷ� ����� �� �ٷ� false�� �ٲ�
	public boolean isPowerShot()
	{
		boolean temp = this.powerShot;
		this.powerShot = false;
		
		return temp;
	}
	public boolean isAcidBoom()
	{
		boolean temp = this.acidBoom;
		this.acidBoom = false;
		
		return temp;
	}
	public boolean isShield()
	{
		boolean temp = this.shield;
		this.shield = false;
		
		return temp;
	}
	
	//����ȿ�� -> ȿ���� 3�ϵ��� ���� �ǹǷ� ���� ���� ������ ��� true����, ���� ���� ������ false
	public boolean isPoisoned()
	{
		if(PLT ==0 )
		{
			poisoned = false;
			return poisoned;
		}
		else
		{
			PLT--;
			return poisoned;
		}
	}

	public boolean isSuperVison()
	{
		if(SVLT ==0 )
		{
			superVision = false;
			return superVision;
		}
		else
		{
			SVLT--;
			return superVision;
		}
	}
}
