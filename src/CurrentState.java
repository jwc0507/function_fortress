import java.awt.Point;

enum STATE{die,shoot,move,wait,fall,attack};

public class CurrentState {
	private STATE currentState;
	private boolean powerShot;
	private boolean acidBoom;
	private boolean poisoned;
	private boolean shield;
	private boolean superVision;
	private int PLT;//poisoned left turn : poison 효과가 끝나기까지 남은 턴 
	private int SVLT;//superVision left turn : superVision 효과가 끝나기까지 남은 턴
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
	
	//일시효과 -> 아이템 효과과 한턴 동안만 적용되므로 사용한 뒤 바로 false로 바꿈
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
	
	//지속효과 -> 효과가 3턴동안 지속 되므로 남은 턴이 있으면 계속 true상태, 남은 턴이 없으면 false
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
