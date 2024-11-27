package mini_lagnuage_compiler;

import java.util.ArrayList;
import java.util.ListIterator;

public class symbolTable {

	/* 
	 * ArrayList 'scopes' is essentially used as a stack, where each element represents a certain scope.
	 * Element found on top of the stack represents a current (the most nested) scope. 
	*/
	ArrayList <ArrayList<symbol>> scopes;
	public symbolTable() {
		this.scopes = new ArrayList<ArrayList<symbol>>();
		this.openScope();
	}
	public int getScopesNum()
	{
		return this.scopes.size();
	}
	public void openScope()
	{
		this.scopes.add(new ArrayList<symbol>());
	}
	public void closeScope() throws CustomException
	{
		if(this.scopes.size() == 1) throw new CustomException("scope has to be opened before being closed");
		this.scopes.removeLast();
	}
	public symbol getEntryAllScopes(String name_arg)
	{
		ListIterator<ArrayList<symbol>> scopes_iterator = this.scopes.listIterator(scopes.size());
		while(scopes_iterator.hasPrevious())
		{
			ArrayList <symbol> scope = scopes_iterator.previous();
			ListIterator <symbol> symbol_iterator = scope.listIterator();
			while(symbol_iterator.hasNext())
			{
				symbol sym = symbol_iterator.next();
				if(sym.getName().equals(name_arg)) return sym;
			}
		}
		//if none are found, next piece of code creates a new one
		symbol sym = new symbol(name_arg);
		this.scopes.getLast().add(sym);
		return sym;
	}
	public symbol getEntryCurrentScope(String name_arg)
	{
		
			
			ListIterator <symbol> symbol_iterator = this.scopes.getLast().listIterator();
			while(symbol_iterator.hasNext())
			{
				symbol sym = symbol_iterator.next();
				if(sym.getName().equals(name_arg)) return sym;
			}
		
		//if none are found, next piece of code creates a new one
		symbol sym = new symbol(name_arg);
		this.scopes.getLast().add(sym);
		return sym;
	} 

}
