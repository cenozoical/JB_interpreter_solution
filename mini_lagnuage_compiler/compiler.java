package mini_lagnuage_compiler;

import java.io.FileNotFoundException;
import java.io.IOException;

public class compiler {
	
	//these constanst represent possible parser states
	final static int reading_left_operand = 0;
	final static int reading_equal_sign = 1;
	final static int reading_right_operand = 2;
	final static int reading_opening_bracket = 3;
	final static int reading_symbol_to_be_printed = 4;
	public compiler() {
		// TODO Auto-generated constructor stub
	}
	public static void main(String args[])
	{
		symbol left_op, right_op;
		symbolTable table;
		programReader rd = null;
		int parser_state;
		
		
		try
		{ 
		if(args.length > 1) throw new ArrayIndexOutOfBoundsException();
		rd = new programReader(args[0]);// throws ArrayIndexOutOfBoundsException if no args are passed
		table = new symbolTable();
		
		left_op = new symbol("");// this line only exists to avoid Java compiler complaints in line
		
		parser_state = reading_left_operand;
		
		while(true)
		{
			if(parser_state == reading_left_operand) // switch statement can be used instead
			{
				int c;
				StringBuilder left_op_name = new StringBuilder();
				rd.consumeBlanksAndNewlines();
				if(rd.peek() == -1 )
				{
					if(table.getScopesNum() != 1) throw new CustomException("opened scopes must be closed");
					break;
				}
				rd.readVariableName(left_op_name);
				
				
				if(left_op_name.toString().equals("scope")) 
				{
					parser_state = reading_opening_bracket;
					rd.consumeBlanks();
					continue;
				}
				else if(left_op_name.toString().equals("print"))
				{
					parser_state = reading_symbol_to_be_printed;
					rd.consumeBlanks();
					continue;
				}
				else if(left_op_name.toString().equals("}"))
				{
				   table.closeScope();
				  
				   if(rd.readRestOfTheLine(table))break;
					
					parser_state = reading_left_operand;
					continue;
				}
				else
				{
					
					left_op = table.getEntryCurrentScope(left_op_name.toString());
					rd.consumeBlanks();
					parser_state = reading_equal_sign;
					continue;
				}
				
			}
			else if(parser_state == reading_equal_sign)
			{
				
				
				if (rd.peek() != '=') throw new CustomException("expected '=' character");
				rd.read();
				rd.consumeBlanks();
				
				parser_state = reading_right_operand;
				continue;
			}
			else if(parser_state == reading_right_operand)
			{
				
				StringBuilder rvalueString = new StringBuilder();

				
				Integer temp;
				if((temp = rd.readRValue(rvalueString)) != null)	left_op.setValue(temp);
				else
				{
					right_op = table.getEntryAllScopes(rvalueString.toString());
					left_op.setValue(right_op);
				}
				
				if(rd.readRestOfTheLine(table))break;
				
				parser_state = reading_left_operand;
				continue;
				
				
				
			}
			else if(parser_state == reading_opening_bracket)
			{
			
				if(rd.peek() != '{') throw new CustomException("expected '{' character");
				rd.read();
				table.openScope();
				rd.readRestOfTheLine(table);//should never be true here
				parser_state = reading_left_operand;
				continue;
				
			}
			else if(parser_state == reading_symbol_to_be_printed)
			{
				
				StringBuilder rvalueString = new StringBuilder();
				
				Integer temp;
				if((temp = rd.readRValue(rvalueString)) != null)	System.out.println(temp);
				else
				{
					right_op = table.getEntryAllScopes(rvalueString.toString());
					temp = right_op.getValue();
					if(temp == null) System.out.println("null");
					else System.out.println(temp);
				}
				if(rd.readRestOfTheLine(table))break;
				
				parser_state = reading_left_operand;
				continue;
				
			}
			
			
			
		}
		
		
		rd.close();
		
		}
		catch (CustomException e)
		{
			System.out.println("Syntax error at row " +  Integer.toString(rd.getRowIndex()) + ", column "
					+Integer.toString(rd.getColumnIndex()) +": "  + e.getMessage());
		}
		catch (FileNotFoundException e)
		{
			System.out.println(e.getMessage());
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			System.out.println("Interpeter expects a single argument.");
		}
		
	}

}
