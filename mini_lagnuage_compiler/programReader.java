package mini_lagnuage_compiler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class programReader extends BufferedReader {
private int rowIndex, columnIndex;

	public programReader(String fileName) throws FileNotFoundException{
		super(new FileReader(fileName),1);
		rowIndex = 1;
		columnIndex = 1;
	}
	public int peek () throws IOException
	{
		
		 this.mark(1);
		 int c = super.read();
		 this.reset();
		 
		 return c;
	}
	public int read() throws IOException
	{
		
		int c = super.read();
		if(c == System.lineSeparator().charAt(System.lineSeparator().length()-1))
		{
			rowIndex ++;
			columnIndex = 1;
		}
		else columnIndex++;
		return c;
	}
	public int getRowIndex()
	{
		return rowIndex;
	}
	public int getColumnIndex()
	{
		return columnIndex;
	}
	private boolean isClosingBracket(StringBuilder targetString) throws IOException, CustomException
	{
		int c;
		if( (c=this.peek()) != '}') return false;
		targetString.append((char)this.read());
		if((c=this.peek()) != ' ' && c!= '\t' && c!= -1 && c != System.lineSeparator().charAt(0))throw new CustomException("variable name can only contain letters, digits, '_' and '$' character");
		return true;
	}
	public void readVariableName(StringBuilder targetString) throws IOException, CustomException
	{
		int c;
		if(isClosingBracket(targetString)) return;
		while(Character.isDigit((c= this.peek())) || Character.isLetter(c)|| c == '$' || c== '_')
		{
			
			targetString.append(Character.toString(c));
			this.read();
		}
		
		if((c= this.peek()) != ' ' && c!='\t' && c!= System.lineSeparator().charAt(0) && c!=-1) throw new CustomException("variable name can only contain letters, digits, '_' and '$' character");
		else if(targetString.length() == 0) throw new CustomException("expected a variable name");
		else if(targetString.toString().matches("[0-9]+")) throw new CustomException("variable name cannot be comprised of digits only");
	}
	
	//returns null but modifies object referenced by targetString, when rvalue is a variable name
	public Integer readRValue(StringBuilder targetString)throws IOException, CustomException
	{
		int c;
		boolean hasArithmeticSign = false;
		
		if((c= this.peek())== '+' || c == '-') // in case integer starts with a sign
		{
			hasArithmeticSign = true;
			this.read();
			targetString.append(Character.toString(c));
		}
		
		
		this.consumeBlanks();
		
		while(Character.isDigit((c= this.peek())) || Character.isLetter(c)|| c == '$' || c== '_')
		{
			targetString.append( Character.toString(c));
			this.read();
		}
		if((c= this.peek()) != ' ' && c!='\t' && c!= System.lineSeparator().charAt(0) && c!=-1 ) throw new CustomException("rvalue can either be an integer value or a variable name");
		else if(targetString.length() == 0) throw new CustomException("expected rvalue");
		else if(targetString.toString().matches("(\\+|-)?[0-9]+")) return  Integer.parseInt(targetString.toString());
		else if(hasArithmeticSign)
		{
			this.columnIndex -= targetString.length();
			throw new CustomException("rvalue can either be an integer value or a variable name");
		}
		else return null;
	}
	
	//returns true if end of the file is reached
	public boolean readRestOfTheLine(symbolTable table) throws IOException, CustomException
	{
		
		this.consumeBlanks();
		if(this.peek() == -1)
		{
			if(table.getScopesNum() != 1) throw new CustomException("opened scopes must be closed");
			return true;
		}
		if(this.peek() != System.lineSeparator().charAt(0)) throw new CustomException("unexpected character");
		for(int i = 0;i < System.lineSeparator().length(); i ++ ) this.read();
		return false;
	}
	public void consumeBlanksAndNewlines() throws IOException
	{
		int c;
		while((c= this.peek())== System.lineSeparator().charAt(0) || c == ' ' || c == '\t') 
			{
			int count = 1;
			if(c == System.lineSeparator().charAt(0))count = System.lineSeparator().length();
			for(int i =0;i< count;i++) this.read();
			}
	}
	public void consumeBlanks() throws IOException
	{
		int c;
		while((c= this.peek())== ' ' || c == '\t') this.read();
	}

}
