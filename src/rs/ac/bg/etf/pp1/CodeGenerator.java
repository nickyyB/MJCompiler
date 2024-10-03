package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.util.Assignment;
import rs.ac.bg.etf.pp1.util.CommonKeys;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

public class CodeGenerator extends VisitorAdaptor {
	
	Logger log = Logger.getLogger(getClass());
	
	private Struct newMatrDataType;
	boolean newMatrixDeclaration;
	
	public int mainPc;
	
	private boolean minusFound=false;

	private final Map<String, Integer> methodOffsets = new HashMap<>();
	
	Deque<List<Integer>> conditionOver = new ArrayDeque<>();
	Deque<List<Integer>> conditionNotOver = new ArrayDeque<>();
	
	Map<String, Obj> lastAssignment = new HashMap<>();
	
	public CodeGenerator() {
		// predefined functions		
		Tab.chrObj.setAdr(Code.pc);
		this.methodOffsets.put(CommonKeys.CHR, Code.pc);
		
		Code.put(Code.enter);
		Code.put(1); Code.put(1);
		// load0
		Code.put(Code.load_n);
		Code.put(Code.exit);
		Code.put(Code.return_);
		
		Tab.ordObj.setAdr(Code.pc);
		this.methodOffsets.put(CommonKeys.ORD, Code.pc);
		
		Code.put(Code.enter);
		Code.put(1); Code.put(1);
		Code.put(Code.load_n);
		Code.put(Code.exit);
		Code.put(Code.return_);
		
		Tab.lenObj.setAdr(Code.pc);
		this.methodOffsets.put(CommonKeys.LEN, Code.pc);
		
		Code.put(Code.enter);
		Code.put(1); Code.put(1);
		Code.put(Code.load_n);
		Code.put(Code.arraylength);
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	public void report_error(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message); 
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.info(msg.toString());
	}
	
	public void visit(FactNum number) {
		Obj dummyObj = new Obj(Obj.Con, "dummy", Tab.intType);
		dummyObj.setAdr(number.getN1());
		Code.load(dummyObj);
	}

	public void visit(FactCh constChar) {
		Obj dummyObj = new Obj(Obj.Con, "dummy", Tab.charType);
		dummyObj.setAdr(constChar.getC1());
		Code.load(dummyObj);
	}
	
	public void visit(FactB constBool) {
		Obj dummyObj = new Obj(Obj.Con, "dummy", CommonKeys.boolType);
		dummyObj.setAdr(constBool.getB1() ? 1 : 0);
		Code.load(dummyObj);
	}
	
	public void visit(DesAssign assignment){
		
		if(newMatrixDeclaration) {
			newMatrixDeclaration = false;
			//e1,e2
			Code.put(Code.dup_x1);
			Code.put(Code.pop);
			Code.put(Code.dup_x1);
			//e1,e2,e1
			Code.put(Code.newarray);
			Code.put(0);
			
			//e1,e2,mAdr
			//treba mi e1 kao brojac i e2 za svaki new array
			//ili pak arraylength
			Code.store(assignment.getDesignator().obj);
			//e1,e2
			Code.loadConst(0);
			//e1,e2,0
			int top = Code.pc;
			Code.put(Code.dup_x1);
			Code.put(Code.dup_x1);
			//e1,0,0,e2,0;
			Code.put(Code.pop);
			//e1,0,0,e2;
			Code.put(Code.dup_x2);
			//e1,e2,0,0,e2;
			
			Code.put(Code.newarray);
			if(newMatrDataType == Tab.charType) {
				Code.put(0);
			}else {
				Code.put(1);
			}
			//e1,e2,0,0,arrAdr
			Code.load(assignment.getDesignator().obj);
			Code.put(Code.dup_x2);
			Code.put(Code.pop);
			//e1,e2,0,madr,0,arradr
			Code.put(Code.astore);
			//e1,e2,0
			Code.loadConst(1);
			Code.put(Code.add);
			//e1,e2,1
			Code.put(Code.dup);
			Code.load(assignment.getDesignator().obj);
			Code.put(Code.arraylength);
			//e1,e2,1,1,mLen
			Code.putFalseJump(Code.ne, 0);
			int newAdr = Code.pc-2;
			
			//e1,e2,1
			Code.putJump(top);
			Code.fixup(newAdr);
			
			Code.put(Code.pop);
			Code.put(Code.pop);
			Code.put(Code.pop);
			//
			return;
			
		}
		Struct dataType = assignment.getDesignator().obj.getType();
		Designator des = assignment.getDesignator();
		
		if(!(des instanceof DesignatorDecl)) {
			if(dataType.equals(Tab.charType)) {
				Code.put(Code.bastore);
			} else {
				Code.put(Code.astore);
			}
		} else {		
			Code.store(des.obj);
			lastAssignment.put(des.obj.getName(), des.obj);
		}
	}
	
	public void visit(DesignatorDecl designator) {
		if(designator.getParent() instanceof CopyArray ) {
			return;
		}
		
		if(designator.getParent().getParent().getParent().getParent().getParent() instanceof CopyArray) {
			Stack<Assignment> stek = SemanticPass.lastAss.get(designator.getIdentDecl().getIdentName());
			if(Objects.isNull(stek) || stek.size() == 0) {
				report_error("STEK JE PRAZAN A NE TREBA FAK!!!", designator);
			}
			int size = stek.pop().getVrednost();
			
			for(int i=size; i>0; i--) {
			    Obj dummyObj = new Obj(Obj.Con, "dummy", Tab.intType);
			    dummyObj.setAdr(i-1);
			    Code.load(designator.obj);  // Load the base array reference (niz2)
			    Code.load(dummyObj);        // Load the current index
			    Code.put(Code.aload);       // Load niz2[i]
			}
			return;
		}
		
		if(!(designator.getParent() instanceof DesAssign || designator.getParent() instanceof ReadStat)) {
			Code.load(designator.obj);
		}
	}
	
	public void visit(AddopList tl) {
		//if(tl.getParent() instanceof NegatedTerm)Code.put(Code.neg);
		Addop mul = tl.getAddop();
    	if(mul instanceof Add) Code.put(Code.add);
    	else Code.put(Code.sub);
	}
	
	/*  CP   */
	
	public void visit(ReadStat readst) {	
	     Obj target = readst.getDesignator().obj;
	        Struct type = target.getType();

	        if (type != Tab.charType) {
	            Code.put(Code.read);
	        } else {
	            Code.put(Code.bread);
	        }
	        Code.store(target);
	}
	
	@Override
	public void visit(PrintStat printStmt) {
        super.visit(printStmt);
        
        
        if(printStmt.getExpr() instanceof ExprTermL) {
        	ExprTermL ex = (ExprTermL)printStmt.getExpr();
        	if(ex.getTermList() instanceof TermSingle) {
        		TermSingle t = (TermSingle)ex.getTermList();
        		if(t.getTerm() instanceof FactorSingle) {
        			FactorSingle f = (FactorSingle)t.getTerm();	
        			if(f.getFactor() instanceof DesignatorFactor) {
        				DesignatorFactor d = (DesignatorFactor)f.getFactor();
        				if(d.getDesignator() instanceof DesignatorDecl) {
        					DesignatorDecl dd = (DesignatorDecl)d.getDesignator();

        			        if (SemanticPass.lastAss.containsKey(dd.getIdentDecl().getIdentName())) {
        			        	Stack<Assignment> s = SemanticPass.lastAss.get(dd.getIdentDecl().getIdentName());
        			        	if(Objects.nonNull(s) && !s.isEmpty()) {
        			        		Assignment a = s.pop();
//        			        		Obj dummyObj = new Obj(Obj.Con, "dummy", Tab.intType);
//            			    		dummyObj.setAdr(a.getVrednost());
//            			        	Code.load(dummyObj);
//            			        	Code.store(lastAssignment.get(dd.getIdentDecl().getIdentName()));
//            			        	Code.load(lastAssignment.get(dd.getIdentDecl().getIdentName()));
            			        	SemanticPass.lastAss.remove(dd.getIdentDecl().getIdentName());
        			        	}        			    		
        			        }
        				}
        			}
        		}
        	}
        }
        
        
        Struct exprType = printStmt.getExpr().struct;
        // Initialization constant specifies DEFAULT_WIDTH
        int width = Tab.charType.equals(exprType) ? 1 : 5;
        if (printStmt.getNumConstList() instanceof WithNum) {
        	WithNum widthSpecifier = (WithNum) printStmt.getNumConstList();
            width = widthSpecifier.getVal();
        }

        Code.loadConst(width);

        if (Tab.intType.equals(exprType)) {
            // Signature: void print(int x, int width = DEFAULT_WIDTH);
            Code.put(Code.print);
        }
        else if (Tab.charType.equals(exprType)) {
            // Signature: void print(char x, int width = DEFAULT_WIDTH);
            Code.put(Code.bprint);
        }
	}

	
	public void visit(TermSingle ts) {
		if(minusFound)
			Code.put(Code.neg);
		minusFound=false;
	}

	public void visit(MinusTerm mt) {
		minusFound=true;
	}
	
	public void visit(FactExpr fl) {

		if(fl.getMulop() instanceof Divide)
		{
			Code.put(Code.div);
		}
		else if (fl.getMulop() instanceof Modulo)
		{
			Code.put(Code.rem);
		}
		else
		{
			Code.put(Code.mul);
		}
	}
	
	private void leaveMethod() {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	
	public void visit(OrOp opp) {
	    Code.putJump(0);
	    if(conditionNotOver.isEmpty()) {
	    	log.error("Check or operator, condition dequee empty");
	    	return;
	    }
	    List<Integer> adrSkoka = conditionNotOver.pollLast();
	    adrSkoka.add(Code.pc - 2);
	    conditionNotOver.offerLast(adrSkoka);
	    if(conditionOver.isEmpty()) {
	    	return;
	    }
	    List<Integer> fixups = conditionOver.pollLast().stream()
		    	.filter(elem -> elem > -9999)
		    	.map(elem -> {Code.fixup(elem); return elem;})
		    	.collect(Collectors.toList()); 
	    List<Integer> fixups2 = new ArrayList<>();
	    conditionOver.offerLast(fixups2);
	}

	public void visit(IfStart ifst) {
	    conditionNotOver.offerLast(new ArrayList<>());
	    conditionOver.offerLast(new ArrayList<>());
	}

	public void visit(ElseStart els) {
	    Code.putJump(0);
	    if(conditionNotOver.isEmpty()) {
	    	log.error("Check or operator, condition dequee empty");
	    	return;
	    }
	    List<Integer> adrSkoka = conditionNotOver.pollLast();
	    adrSkoka.add(Code.pc - 2);
	    conditionNotOver.offerLast(adrSkoka);
	    if(conditionOver.isEmpty()) {
	    	log.error("Check or operator, condition dequee empty");
	    	return;
	    }
	    List<Integer> fixups = conditionOver.pollLast().stream()
		    	.filter(elem -> elem > -9999)
		    	.map(elem -> {Code.fixup(elem); return elem;})
		    	.collect(Collectors.toList()); 
        conditionOver.offerLast(new ArrayList<>());
	}

	public void visit(IfCond ifc) {
	    if(conditionNotOver.isEmpty()) {
	    	log.error("Check or operator, condition dequee empty");
	    	return;
	    }
	    List<Integer> fixups = conditionNotOver.pollLast().stream()
		    	.filter(elem -> elem > -9999)
		    	.map(elem -> {Code.fixup(elem); return elem;})
		    	.collect(Collectors.toList()); 
        conditionNotOver.offerLast(new ArrayList<>());
	}

	public void visit(IfSingleStat unm) {
	    if(conditionOver.isEmpty()) {
	    	log.error("Check or operator, condition dequee empty");
	    	return;
	    }
	    List<Integer> fixups = conditionOver.pollLast().stream()
		    	.filter(elem -> elem > -9999)
		    	.map(elem -> {Code.fixup(elem); return elem;})
		    	.collect(Collectors.toList()); 
        conditionNotOver.pollLast();
	}

	public void visit(IfElseUnmatch iff) {
	    if(conditionNotOver.isEmpty()) {
	    	log.error("Check or operator, condition dequee empty");
	    	return;
	    }
		List<Integer> fixups = conditionNotOver.pollLast().stream()
		    	.filter(elem -> elem > -9999)
		    	.map(elem -> {Code.fixup(elem); return elem;})
		    	.collect(Collectors.toList());
        conditionOver.pollLast();
	}

	public void visit(IfElseStat iff) {
	    if(conditionNotOver.isEmpty()) {
	    	log.error("Check or operator, condition dequee empty");
	    	return;
	    }
		List<Integer> fixups = conditionNotOver.pollLast().stream()
		    	.filter(elem -> elem > -9999)
		    	.map(elem -> {Code.fixup(elem); return elem;})
		    	.collect(Collectors.toList());
        conditionOver.pollLast();
	}

	public void visit(CondFactExpr conf) {
	    Code.load(new Obj(Obj.Con, "$", Tab.intType, 1, 0));
	    Code.putFalseJump(Code.eq, 0);
	    List<Integer> lista = conditionOver.pollLast();
	    if(Objects.isNull(lista)) {
	    	log.error("CHECK COND FACT CUZ CONDITION DEQUEU IS EMPTY");
	    	lista = new ArrayList<>();
	    }
        lista.add(Code.pc - 2);
        conditionOver.offerLast(lista);
	}
	
	public void visit(RelopCondFact rel) {
	    Relop op = rel.getRelop();

	    if (op instanceof Equal) {
	        Code.putFalseJump(Code.eq, 0);
	    } else if (op instanceof NotEqual) {
	        Code.putFalseJump(Code.ne, 0);
	    } else if (op instanceof Greater) {
	        Code.putFalseJump(Code.gt, 0);
	    } else if (op instanceof GreaterOrEqual) {
	        Code.putFalseJump(Code.ge, 0);
	    } else if (op instanceof Less) {
	        Code.putFalseJump(Code.lt, 0);
	    } else if (op instanceof LessOrEqual) {
	        Code.putFalseJump(Code.le, 0);
	    }

	    List<Integer> lista = conditionOver.pollLast();
	    
	    if (Objects.nonNull(lista)) {	        
	        lista.add(Code.pc - 2);
	        conditionOver.offerLast(lista);
	    } else {
	    	log.error("RELOP FACT CONDITION IS EMPTY");
	    }
	}

	public void visit(DesInc inc) {
		generateIncDecStatement(inc);
	}
	
	public void visit(DesDec dec) {
		generateIncDecStatement(dec);
	}
	
	private void generateIncDecStatement(SyntaxNode node) {
		Obj obj = null;
		if(node instanceof DesInc) {
			obj = ((DesInc)node).getDesignator().obj;
		} else if (node instanceof DesDec) {
			obj = ((DesDec)node).getDesignator().obj;
		}
		
		if(Objects.isNull(obj)) {
			return;
		}
		
		if(obj.getKind()==Obj.Elem) {
			Code.put(Code.dup2);
		}
		
		Code.load(obj);
		Code.loadConst(1);
		
		if(node instanceof DesInc) {
			Code.put(Code.add);
		} else if (node instanceof DesDec) {
			Code.put(Code.sub);
		}
		
		Code.store(obj);
		
		//TODO Check if this is really needed!!!
		Code.put(Code.pop);
	}

	
	public void visit(DesignatorIdentEl desel) {
		Designator dd=desel.getDesignator();
		Code.load(dd.obj);
	}

	public void visit(FactorNewExpr fn) {
//		Code.put(Code.const_2);  // Push the constant 2 onto the stack
//		Code.put(Code.mul);
		
		
		Code.put(Code.newarray);
		if (fn.getType().struct.equals(Tab.intType)) {
			Code.put(1);
		} else {
			Code.put(0);
		}
	}
	
	public void visit(FactorNewMatrix factorNewMatrix) {
		newMatrixDeclaration = true;
		newMatrDataType = factorNewMatrix.struct.getElemType().getElemType();
	}
	
	public void visit(MatrixDesignator designator) {
		//pocetno stanje na ExprStack: e1,e2
		//ciljno stanje: arrayAdr,e2,mAdr,e1 za dvostruki aload.
		//za astore arrayadr,e2,arrayadr,e2,mAdr,e1;
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		//e2,e1
		Code.load(designator.getArrayName().obj);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		//e2,mAdr,e1
		Code.put(Code.aload);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);

		
		//nista lepse nego kad jednu te istu stvar sedam puta stavljam
		//arrayAdr,e2;
		if( designator.getParent() instanceof DesInc || designator.getParent() instanceof DesDec) {
			Code.put(Code.dup2);
			//arrayadr,e2,arrayadr,e2
		}else if (designator.getParent() instanceof DesAssign || designator.getParent() instanceof ReadStat) return;
		Struct dataType = designator.obj.getType();
		if(dataType != Tab.charType) {
			Code.put(Code.aload);
		}else {
			Code.put(Code.baload);
		}
	}
	
	public void visit(DesignatorExprEl des) {
		SyntaxNode node = des.getParent().getParent();
		node = node.getParent().getParent();
//		if((node.getParent() instanceof PrintStat)) { //Ovo je za print(niz[i]) = niz[i] = len(niz)
//		if(!(node.getParent() instanceof PrintStat)) { //Ovo je za niz[X] = gde vraca broj pristupa elementu niz[X-2] --- 0/2 1/3 2/4 3/5/ 4/6 ...
//			Code.put(Code.dup);
//			Code.put(Code.const_2);
//			Code.put(Code.add);
//			Code.put(Code.dup);
//			Code.put(Code.dup_x1);
//			Code.load(des.getArrayName().obj);
//			Code.put(Code.pop);
//			Code.put(Code.aload);
//			Code.put(Code.const_1);
//			Code.put(Code.add);
//			Code.load(des.getArrayName().obj);  // Load the current index
//		    Code.put(Code.dup_x1);
//		    Code.put(Code.pop);
//		    Code.put(Code.astore);
			
			// niz[i] + len(niz)
//			Code.load(des.getArrayName().obj);
//			Code.put(Code.dup_x1);
//			Code.put(Code.pop);
//			Code.put(Code.aload);
//			Code.load(des.getArrayName().obj);
//			generateFunctionCall(CommonKeys.LEN);
//			Code.put(Code.add);
//			return;
//		}
		

		
		Code.load(des.getArrayName().obj);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		//adr,e1
		if(des.getParent() instanceof DesInc || des.getParent() instanceof DesDec) {
			Code.put(Code.dup2);
			//adr,e1,adr,e1
		}else if(des.getParent() instanceof DesAssign || des.getParent() instanceof ReadStat) return;
		Struct dataType = des.obj.getType();
		if(dataType != Tab.charType) {
			Code.put(Code.aload);
		}else {
			Code.put(Code.baload);
		}
		
	}
	
	public void visit(CopyArray copyArray) {
		Designator des = copyArray.getDesignator();
		Stack<Assignment> stek = SemanticPass.lastAss.get(((DesignatorDecl)des).getIdentDecl().getIdentName());
		if(Objects.isNull(stek) || stek.size() == 0) {
			report_error("STEK JE PRAZAN A NE TREBA FAK!!!", copyArray);
		}
		int size = stek.pop().getVrednost();
		for (int i = 0; i < size; i++) {
			 Obj dummyObj = new Obj(Obj.Con, "dummy", Tab.intType);
		     dummyObj.setAdr(i);

		     // Store it in the destination array (niz1[i])
		     Code.load(des.obj);  // Load the base array reference (niz1)	
		     Code.put(Code.dup_x1);
		     Code.put(Code.pop);
		     Code.load(dummyObj);  // Load the current index
		     Code.put(Code.dup_x1);
		     Code.put(Code.pop);
		     //Code.put(Code.pop);
		     Code.put(Code.astore); // Store the value into niz1[i]
		}
	}
	
	/* CP 2*/
	
	@Override
	public void visit(WithDoubleColon TypeScoped) {
		super.visit(TypeScoped);
	}
	
	@Override
	public void visit(NamespaceName NamespaceName) {
		super.visit(NamespaceName);
	}
	@Override
	public void visit(NamespaceDecl Namespace) {
		super.visit(Namespace);
	}
	@Override
	public void visit(NoNamespace NamespaceListEnd) {
		super.visit(NamespaceListEnd);
	}
	@Override
	public void visit(NamespaceDeclLista NamespaceListItem) {
		super.visit(NamespaceListItem);
	}

	
    @Override
    public void visit(DesignatorFactor designatorFactor) {
        super.visit(designatorFactor);
        Designator designator = designatorFactor.getDesignator();
        
        if(!(designatorFactor.getOptionalFunctionCall() instanceof NoFunctionCall)) {
        	generateFunctionCall(designator.obj.getName());
        }
    }
    
    @Override
    public void visit(MethodSignatureWithoutParams methodSignatureWithoutParams) {
        super.visit(methodSignatureWithoutParams);
        String methodName = methodSignatureWithoutParams.getMethodName();
        
		if(methodName.equals(CommonKeys.MAIN)){
			mainPc = Code.pc;
			Code.mainPc=Code.pc;
		}
		
		methodSignatureWithoutParams.obj.setAdr(Code.pc);
        this.methodOffsets.put(methodName, Code.pc);

        Obj methodObj = methodSignatureWithoutParams.obj;
        Code.put(Code.enter);
        Code.put(methodObj.getLevel());
        Code.put(methodObj.getLocalSymbols().size());
    }
    
    @Override
    public void visit(MethodDecl methodDecl) {
        super.visit(methodDecl);
        ReturnType returnType = methodDecl.getMethodSignature().getMethodSignatureWithoutParams().getReturnType();
        if (returnType instanceof VoidReturnType) {
            leaveMethod();
        }
        else {
            // traps method end without return
            Code.put(Code.trap);
            Code.put(0);
        }
    }
    
    private void generateFunctionCall(String functionName) {
        int functionOffset = methodOffsets.get(functionName);
        int pcRelativeOffset = functionOffset - Code.pc;
        Code.put(Code.call);
        // all functions must be defined before their calls,
        // so there is no need for back patching
        Code.put2(pcRelativeOffset);
    }
    
    @Override
    public void visit(FunctionCallStmt functionCallStmt) {
        super.visit(functionCallStmt);
        Designator designator = functionCallStmt.getDesignator();
        generateFunctionCall(designator.obj.getName());
        if (!Tab.noType.equals(designator.obj.getType())) {
            Code.put(Code.pop);
        }
    }
    
//    private void generateCodeChr() {
        // Signature: char chr(int i);
        //methodOffset.put
    	//enter
    	//1
    	//1
    	//load_n
    	//exitMethod
//   }

//    private void generateCodeOrd() {
        // Signature: int ord(char ch);
        //methodOffset.put
    	//enter
    	//1
    	//1
    	//load_n
    	//exitMethod
//    }

//    private void generateCodeLen() {
        // Signature: int len(void arr[]);
    	//methodOffset.put
    	//enter
    	//1
    	//1
    	//load_n
    	//arrayLeng
    	//exitMethod
//    }

    
    @Override
    public void visit(ProgName programHeader) {
        super.visit(programHeader);
    }
    
    @Override
    public void visit(ReturnStmt returnStmt) {
        super.visit(returnStmt);
		leaveMethod();
    }


}
