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
	
	/*v1.1에서 hp 반환하는 함수랑 hp 값 바꾸는 함수 삭제. 대신 damaged라는 함수를 추가함*/
	
	
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
	
	//v1.1에서 추가 
	public void damaged(int damage)
	{
		this.hp = this.hp - damage;
	}
	public int getHp()
	{
		return this.hp;
	}
}