package rs.ac.bg.etf.pp1.util;

import rs.etf.pp1.symboltable.concepts.Obj;

public class Assignment {
	Obj promenjiva;
	int vrednost;
	
	public Assignment() {}
	
	public void setPromenjiva(Obj obj) {
		this.promenjiva=obj;
	}
	public Obj getPromenjiva() {
		return promenjiva;
	}
	
	public void setVrednost(int v) {
		this.vrednost=v;
	}
	public int getVrednost() {
		return vrednost;
	}
}
