package rs.ac.bg.etf.pp1.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class MethodDescriptor {

	private List<Obj> declaredParams;
	private String name;
	private int params;
	
	public MethodDescriptor() {}
	
	public MethodDescriptor(String name, List<Obj> declaredP) {
		this.name=name;
		this.declaredParams=declaredP;
		this.params=declaredP.size();
	}
	
	public MethodDescriptor(String name) {
		this.name=name;
		this.declaredParams = new ArrayList<>();
		this.params = 0;
	}
	
	public String getName() {
		return name;
	}
	
	public List<Obj> getDeclaredParams() {
		return declaredParams == null ? Collections.emptyList() : declaredParams;
	}
	
	public int getNumberOfParams() {
		return this.getDeclaredParams().size();
	}
	
	public void setParams(List<Obj> params) {
		if(params==null) {
			throw new RuntimeException();
		}
		this.declaredParams=params;
		this.params=params.size();
	}
	
	public void addParam(Obj parm) {
		this.declaredParams.add(parm);
	}
	
	
}
