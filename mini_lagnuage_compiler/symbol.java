package mini_lagnuage_compiler;

public class symbol {

	private String name;
	private int value;
	private boolean isNull;
	public symbol(String name) {
		this.isNull = true;
		this.name = name;
	}
	public void setValue(int value)
	{
		this.value = value;
		this.isNull = false;
	}
	public void setValue(symbol b)
	{
		if(b.isNull) this.isNull = true;
		else 
			{
			this.isNull = false;
			this.value = b.value;
			
			}
	}
	public String getName()
	{
		return this.name;
	}
	public Integer getValue()
	{
		if(this.isNull)return null;
		return Integer.valueOf(this.value);
	}

}
