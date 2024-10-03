package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.util.CommonKeys;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class MyTab extends Tab {
	
	private static Logger log = Logger.getLogger(MyTab.class);
	
	public static boolean insert(Obj obj) {
		int adr = obj.getAdr();
		boolean ret = currentScope.addToLocals(obj);
		obj.setAdr(adr);
		return ret;
	}

	public static void init() {
		Tab.init();
		currentScope.addToLocals(new Obj(Obj.Type, CommonKeys.BOOLEAN, CommonKeys.boolType, 0, 0));		
		Tab.lenObj.setLevel(0); Tab.lenObj.setFpPos(0); Tab.lenObj.setAdr(Obj.NO_VALUE);
		Tab.chrObj.setLevel(0); Tab.chrObj.setFpPos(0); Tab.chrObj.setAdr(Obj.NO_VALUE);
		Tab.ordObj.setLevel(0); Tab.ordObj.setFpPos(0); Tab.ordObj.setAdr(Obj.NO_VALUE);
	}

	public static void openScopeAndChain(Struct struct) {
		openScope();
		try {
			currentScope.addToLocals(null);
		} catch(NullPointerException e) {}
		chainLocalSymbols(struct);
	}
}
