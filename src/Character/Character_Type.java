package Character;
//enum Ability {blind,push,none}

public class Character_Type
{
	private String name;
	private int damage;
	private int hp;
	private int speed;
	private String ability;
	
	public Character_Type(String name, int damage, int hp, int speed, String ability)
	{
		this.name = name;
		this.damage = damage;
		this.hp = hp;
		this.speed = speed;
		this.ability = ability;
	}
	
	/*v1.1���� hp ��ȯ�ϴ� �Լ��� hp �� �ٲٴ� �Լ� ����. ��� damaged��� �Լ��� �߰���*/
	
	
	public String getName()
	{
		return name;
	}
	public int getDamage()
	{
		return damage;
	}

	public int getSpeed()
	{
		return speed;
	}
	
	public String getAbility()
	{
		return ability;
	}
	
	//v1.1���� �߰� 
	public void damaged(int damage)
	{
		this.hp = this.hp - damage;
	}
	public int getHp()
	{
		return this.hp;
	}
}