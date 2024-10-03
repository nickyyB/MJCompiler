package rs.ac.bg.etf.pp1;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.util.Assignment;
import rs.ac.bg.etf.pp1.util.CommonKeys;
import rs.ac.bg.etf.pp1.util.CommonUtil;
import rs.ac.bg.etf.pp1.util.Constants;
import rs.ac.bg.etf.pp1.util.MethodDescriptor;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class SemanticPass extends VisitorAdaptor {
	
	private Logger log = Logger.getLogger(getClass());

	private boolean errorDetected = false;
	
	private Struct currType = null;
	private Obj currMethod = null;
	
	private String currNamespace = null;
	
	private boolean inMethodDeclaration = false;
	private boolean inMethodSignature = false;
	private final Stack<Struct> functionCallParamTypes = new Stack<>();
	
	public static final Map<String, Stack<Assignment>> lastAss = new HashMap<>();
	private int vr;
	
	Map<String, Integer> arraysDeclared = new HashMap<>();
	
	int nVars;
	
    int printCallCount = 0;
    int varDeclCount = 0;
    int arrDeclCount = 0;
	
	int globalVarCnt;
	int localVarCnt;
	
	public SemanticPass() {
		MyTab.init();
		CommonUtil.declareMethod(Constants.CHR, Arrays.asList(new Obj(Obj.Var, "$", new Struct(Struct.Int))));
		CommonUtil.declareMethod(Constants.ORD, Arrays.asList(new Obj(Obj.Var, "$", new Struct(Struct.Char))));
		CommonUtil.declareMethod(Constants.LEN, Arrays.asList(new Obj(Obj.Var, "$", new Struct(Struct.Array, new Struct(Struct.Array, currType)))));
	}
	
	/*
	 * Common Methods For Info and Error Reports 
	 * @Vezbe
	 */
	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
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
	
	public static boolean isMethValue(Obj currObj) {
		return currObj.getKind() == Obj.Meth;
	}
	
	/*
	 *
	 * Visit Program node
	 * @nbrkovic 
	 */
	public void visit(Program program) {
		globalVarCnt = Tab.currentScope().getnVars();
		nVars = Tab.currentScope().getnVars();
		
		if(!CommonUtil.objExist(CommonKeys.MAIN)) {
			log.error("MISSIN MAIN METHOD!!!");
		}
		
		Obj obj = CommonUtil.getObjByName(CommonKeys.MAIN, program.getLine());
    	Tab.chainLocalSymbols(program.getProgName().obj);
    	Tab.closeScope();
	}
	
	/*
	 *
	 * Visit ProgName node
	 * @nbrkovic 
	 */
    public void visit(ProgName progName){
    	log.info("PROGRAM NAME : " + progName.getIdentValue());
    	progName.obj = Tab.insert(Obj.Prog, progName.getIdentValue(), Tab.noType);
    	Tab.openScope();
    }
    
	/*
	 *
	 * Visit NamespaceName node
	 * @nbrkovic 
	 */
   public void visit(NamespaceName namespaceNode){
		super.visit(namespaceNode);
		String name = namespaceNode.getIdentValue();
		Struct struct = new Struct(Struct.None);
		Obj obj = new Obj(CommonKeys.NAMESPACE_KIND, name, struct, Obj.NO_VALUE, 0); obj.setFpPos(Obj.NO_VALUE);
		if (!MyTab.insert(obj)) {
			report_error("Ime " + name + " vec postoji.", namespaceNode);
		}
		MyTab.openScopeAndChain(struct);
   }
   
	@Override
	public void visit(NamespaceDecl NamespaceDecl) {
		super.visit(NamespaceDecl);
		NamespaceDecl.obj = NamespaceDecl.getNamespaceName().obj;
		Tab.closeScope();
	}
	
	@Override
	public void visit(NoNamespace emptyNamespace) {
		super.visit(emptyNamespace);
	}
	
	@Override
	public void visit(NamespaceDeclLista namespaceDeclList) {
		super.visit(namespaceDeclList);
	}
	
    public void visit(DeclConst constdecl) {
    	currType=constdecl.getType().struct;
    }
    
    /*CONST DECLARATIONS*/
    
	public void visit(ConstValueN intConstIdentValue)
	{

    	if(CommonUtil.objExist(intConstIdentValue.getConstName()))
    		report_error("Const with name "+ intConstIdentValue.getConstName() + " already declared", intConstIdentValue);
    	
		if(Objects.isNull(currType)|| currType != Tab.intType) {
			report_error("Greska: tip i vrednost konstante se slazu", intConstIdentValue);
			return;
		}
		
		Obj newConst = Tab.insert(Obj.Con, intConstIdentValue.getConstName(), Tab.intType);
		newConst.setAdr(intConstIdentValue.getValue());
	}
	
	public void visit(ConstValueB boolConstIdentValue)
	{
		
    	if(CommonUtil.objExist(boolConstIdentValue.getConstName()))
    		report_error("Const with name "+ boolConstIdentValue.getConstName() + " already declared", boolConstIdentValue);
    	
		if(Objects.isNull(currType) || currType != CommonKeys.boolType) {
			report_error("Greska: tip i vrednost konstante se slazu", boolConstIdentValue);
			return;
		}

		Obj newConst = Tab.insert(Obj.Con, boolConstIdentValue.getConstName(), CommonKeys.boolType);
		newConst.setAdr(boolConstIdentValue.getValue() ? 1 : 0);
	}
	
	public void visit(ConstValueC charConstIdentValue)
	{
		
    	if(CommonUtil.objExist(charConstIdentValue.getConstName()))
    		report_error("Const with name "+ charConstIdentValue.getConstName() + " already declared", charConstIdentValue);
    	
		if(Objects.isNull(currType) || currType != Tab.charType)  {
			report_error("Greska: tip i vrednost konstante se slazu", charConstIdentValue);
			return;
		}

		Obj newConst = Tab.insert(Obj.Con, charConstIdentValue.getConstName(), Tab.charType);
		newConst.setAdr(charConstIdentValue.getValue());
	}

	
	public void visit(VarDeclIdent varDecl){
		String varName = varDecl.getVarName();
		int level = inMethodDeclaration ? 1 : 0;
		if(!CommonUtil.objExist(varName) || Tab.find(varName).getLevel()!=level) {
			varDeclCount++;
			Obj obj = Tab.insert(Obj.Var, varName, currType);
			report_info("Declared variable "+ varDecl.getVarName(), varDecl);
			
	        if (inMethodSignature) {	          
	        	MethodDescriptor m = CommonUtil.getDeclaredMethod(currMethod.getName());
	        	if(Objects.isNull(m)) {
	        		m = new MethodDescriptor(currMethod.getName());
	        	}
	        	m.addParam(obj);
	        	m = CommonUtil.declareMethod(currMethod.getName(), m);
	        	obj.setFpPos(m.getDeclaredParams().size());
	        }
			
			return;
		}
		report_error("Variable "+ varName + " already declared", varDecl);
	}
	
    public void visit(VarDeclArray varUnit) {
        int level = inMethodDeclaration ? 1 : 0;
        if (Objects.isNull(CommonUtil.getSymbolInScope(varUnit.getArrName())) || varUnit.obj.getLevel() != level) {
            if (Objects.nonNull(currType) && currType != Tab.noType) {
                Obj obj = null;
                if (varUnit.getArray() instanceof Matrix) {
                    obj = Tab.insert(Obj.Var, varUnit.getArrName(), new Struct(Struct.Array, new Struct(Struct.Array, currType)));
                    report_info("Deklarisana matrica " + varUnit.getArrName(), varUnit);

                } else if (varUnit.getArray() instanceof Arr) {
                    obj = Tab.insert(Obj.Var, varUnit.getArrName(), new Struct(Struct.Array, currType));
                    report_info("Deklarisan niz " + varUnit.getArrName(), varUnit);
                }

                if (Objects.nonNull(obj) && inMethodSignature) {
                    MethodDescriptor m = CommonUtil.getDeclaredMethod(currMethod.getName());
                    if (Objects.isNull(m)) {
                        m = new MethodDescriptor(currMethod.getName());
                    }
                    m.addParam(obj);
                    m = CommonUtil.declareMethod(currMethod.getName(), m);
                    obj.setFpPos(m.getDeclaredParams().size());
                }
                return;
            }
            log.error("Check declaration of array! Something is wrong");
            return;
        }
        report_error("Ime " + varUnit.getArrName() + " je vec dekliarisano.", varUnit);
    }
    
	/*
	 * Type Namespace Scope
	 */
	@Override
	public void visit(WithDoubleColon TypeScoped) {
		super.visit(TypeScoped);
		IdentDecl parent = (IdentDecl) TypeScoped.getParent();
		String name = TypeScoped.getName();
		Obj namespaceObj = Tab.find(parent.getIdentName());
		if (namespaceObj.getKind() != CommonKeys.NAMESPACE_KIND) {
			report_error("Identifikator " + parent.getIdentName() + " ne oznacava prostor imena.", TypeScoped);
			TypeScoped.struct = Tab.noType;
		}
		else {
			Obj obj = namespaceObj.getType().getMembersTable().searchKey(name);
			if (obj == null) {
				report_error("Identifikator " + name + " ne postoji u prostoru imena " + parent.getIdentName() + ".", TypeScoped);
				TypeScoped.struct = Tab.noType;
			}
			else if(obj.getType().getKind() == Obj.Var || obj.getType().getKind() == Obj.Con) {
				TypeScoped.struct = obj.getType();
			}
			else if(obj.getType().getKind() == 3) {
				TypeScoped.struct = obj.getType().getElemType();
			}
			else if (obj.getKind() != Obj.Type) {
				report_error("Identifikator " + parent.getIdentName() + "::" + name + " ne oznacava tip.", TypeScoped);
				TypeScoped.struct = Tab.noType;
			}
			else {
				TypeScoped.struct = obj.getType();
			}
		}
	}

    public void visit(ReadStat readst) {
    	Designator designator = readst.getDesignator();
    	int designatorKind = designator.obj.getKind();
        int designatorType = designator.obj.getType().getKind();
    	
        if(CommonUtil.isValidReadObjType(designatorKind)) {
        	if(!CommonUtil.isValidIOOperand(designatorType)) {
        		log.error("Error in Read Statement, designator has a wrong type!");
        	}
        	return;
        	//SVE OK
        }
    	report_error("Error in Read Statement, check read designator", readst);
    }

    public void visit(PrintStat printst) {
    	if(!CommonUtil.isValidIOOperand(printst.getExpr().struct.getKind())) {
    		report_error("Error inside Print statement", printst);
    	}
    }
    
    public void visit(Condition con) {
    	con.struct=con.getCondTerms().struct;
    }
    
    public void visit(OrList ct) {
    	CondTerms left = ct.getCondTerms();
    	CondTerm right = ct.getCondTerm();
    	ct.struct=CommonKeys.boolType;
    	if (left.struct != CommonKeys.boolType || right.struct != CommonKeys.boolType) {
    		ct.struct=Tab.noType;
    	}
    }
    
    public void visit(CondTermSingle cnl) {
    	cnl.struct=cnl.getCondTerm().struct;
    }
    
    public void visit(CondTerm ct) {
    	ct.struct=ct.getCondFacts().struct;
    }
    
    public void visit(AndList ct) {
    	CondFacts left = ct.getCondFacts();
    	CondFact right = ct.getCondFact();
    	ct.struct=CommonKeys.boolType;
    	if (left.struct != CommonKeys.boolType || right.struct != CommonKeys.boolType) {
    		ct.struct=Tab.noType;
    	}
    }
    
    public void visit(CondFactSingle cl) {
    	cl.struct=cl.getCondFact().struct;
    }
    
    public void visit(CondFactExpr cone) {
    	cone.struct=cone.getExpr().struct;
    }
    
    public void visit(RelopCondFact relcon) {
    	
    	Struct left = relcon.getExpr().struct;
    	Struct right = relcon.getExpr1().struct;
    	if (!right.compatibleWith(left)) {
    	    report_error("Operands are not compatible in relational operation, line " + relcon.getLine(), relcon);
    	    relcon.struct = Tab.noType;  // Early exit to ensure struct is set
    	    return;
    	}
    	
    	boolean isClassOrArray = (left.getKind() == Struct.Class || left.getKind() == Struct.Array);

    	if(isClassOrArray) {
    	    if (relcon.getRelop() instanceof Equal || relcon.getRelop() instanceof NotEqual) {
    	        relcon.struct = CommonKeys.boolType;
    	    } else {
    	        relcon.struct = Tab.noType;
    	        report_error("Only '==' or '!=' are allowed for classes or arrays, line " + relcon.getLine(), relcon);
    	    }
    	} else {
    	    relcon.struct = CommonKeys.boolType;  // For all other types, assume boolean result
    	}
    }

    public void visit(DesAssign assdes) {
    	Obj dessObj = assdes.getDesignator().obj;
    	if(dessObj.getType() == Tab.noType) {
    		report_error("Error in designator assigment, designator object has no type", assdes);
    		return;
    	}
    	if(!CommonUtil.isValidReadObjType(dessObj.getKind())) {
    		report_error("Semanticka greska na liniji " + assdes.getLine() + " designator pogresnog tipa ", assdes);
    		return;
    	}
    	
        Struct tt = assdes.getExpr().struct;
        Struct dd = assdes.getDesignator().obj.getType();
        
        Struct desgn, ex;
        if (tt.getKind() == Struct.Array && dd.getKind() == Struct.Array) {
            ex = tt.getElemType();
            desgn = dd.getElemType();
        } else if (dd.getKind() == Struct.Array) {
            desgn = dd.getElemType();
            ex = tt;
        } else {
            desgn = assdes.getDesignator().obj.getType();
            ex = tt;
        }

        if (!ex.assignableTo(desgn)) {        
            report_error("Greska na liniji " + assdes.getLine() + " : " + "nekompatibilni tipovi u dodeli vrednosti! dess=expr ", assdes);
        }

        if (assdes.getDesignator() instanceof DesignatorDecl) {
            String varName = ((DesignatorDecl) assdes.getDesignator()).getIdentDecl().getIdentName();
            Stack<Assignment> stek = lastAss.get(varName);
            Assignment ass = new Assignment();
            ass.setPromenjiva(assdes.getDesignator().obj);
            ass.setVrednost(vr);
            if (stek == null) {
                stek = new Stack<Assignment>();
            }
            stek.push(ass);
            lastAss.put(varName, stek);

            /*kopiranje niza */
//	 		  if(assdes.getExpr() instanceof ExprTermL) {
//	 			  ExprTermL e = (ExprTermL)assdes.getExpr();
//	 			  if(e.getTermList() instanceof TermSingle) {
//	 				 TermSingle t = (TermSingle)e.getTermList();
//	 				 if(t.getTerm() instanceof FactorSingle) {
//	 					FactorSingle f = (FactorSingle)t.getTerm();
//	 					if(f.getFactor() instanceof FactorNewExpr) {
//	 						FactorNewExpr fne = (FactorNewExpr)f.getFactor();
//	 						arraysDeclared.put(varName, vr);
//	 					}
//	 				 }
//	 			  }
//	 		   }
        }
    }
    
    public void visit(DesInc indes) {
    	if(!Tab.intType.equals(indes.getDesignator().obj.getType()) || !CommonUtil.isValidReadObjType(indes.getDesignator().obj.getKind())) {
    		report_error("Designator has to be INTEGER", indes);
    	}
    }
    
   public void visit(DesDec indes) {
	   	if(!Tab.intType.equals(indes.getDesignator().obj.getType()) || !CommonUtil.isValidReadObjType(indes.getDesignator().obj.getKind())) {
			report_error("Designator has to be INTEGER", indes);
		}
   }
   
   
   //Kopiranje niza probam moguce modifikacije
   public void visit(CopyArray copyArray) {
		   Struct tt = copyArray.getExpr().struct;
		   Struct dd = copyArray.getDesignator().obj.getType();
		  // report_info("exp je "+tt.getKind()+" a des je "+dd.getKind(), ee);
		   Struct desgn,ex;
		   if(tt.getKind()==Struct.Array && dd.getKind()==Struct.Array) {
			   ex=tt.getElemType();
			   desgn=dd.getElemType();
			   //report_info("exp1 je "+ex.getKind()+" a des1 je "+desgn.getKind(), ee);
		   }
		   else if(dd.getKind()==Struct.Array) {
			  desgn= dd.getElemType();
			  ex=tt;
		   }else {
			   desgn=copyArray.getDesignator().obj.getType();
			   ex=tt;
		   }
		   boolean ep=ex.assignableTo(desgn);
		   if(!ep) {
			   //if(!assdes.getDesignator().equals(null) || !assdes.getExpr().struct.isRefType()) {
				  report_error("Greska na liniji " + copyArray.getLine() + " : " + "nekompatibilni tipovi u dodeli vrednosti! dess=expr ", copyArray);
			  // }
		   }
   }
   
   public void visit(DesignatorExprEl desexp) {
	Struct tipdes = desexp.getArrayName().obj.getType();  
   	Struct tipexp =desexp.getExpr().struct;
   	
   	if(tipdes.getKind()!=Struct.Array) {
   		report_error("des mora biti niz, linija "+desexp.getLine(), desexp);
   		desexp.obj=Tab.noObj;
   		return;
   	}
   	
   	desexp.obj = new Obj(Obj.Elem, desexp.getArrayName().obj.getName(), tipdes.getElemType());
   	
   	if(tipexp.getKind()!=Struct.Int) {
   		desexp.obj=Tab.noObj;
   		report_error("exp mora biti int, linija "+desexp.getLine(), desexp);
   	}
   }
   
	public void visit(ArrayName arrName) {
		arrName.obj = Tab.find(arrName.getArrayName());
	}
   
	public void visit(MatrixDesignator designator) {
		Obj desig = Tab.find(designator.getArrayName().getArrayName());
		if(desig!=Tab.noObj) {
			if(desig.getType().getElemType().getKind() != Struct.Array) {
				report_error(designator.getArrayName().getArrayName() + " nije matrica.", designator);
				return;
			}
			if(designator.getExpr().struct != Tab.intType || designator.getExpr1().struct != Tab.intType) {
				report_error("Matrica nije indeksirana celobrojnim vrednostima", designator);
				return;
			}
			designator.obj = new Obj(Obj.Elem, desig.getName(), desig.getType().getElemType().getElemType());
			return;
		}
		report_error("Ime "+designator.getArrayName().getArrayName()+ " nije deklarisano.",designator);
	}
   
   public void visit(OneActPar act) {
   	act.struct=act.getExpr().struct;
   }

   public void visit(ExprTermL exdec) {
   	exdec.struct=exdec.getTermList().struct;
   }
   
   public void visit(NegatedTerm minusterm) {
   	Struct term = minusterm.getTermList().struct;
   	
   	if(term != Tab.intType) {
   		report_error("Greska na liniji "+ minusterm.getLine()+" : nekompatibilni tipovi u izrazu za - term.", null);
		minusterm.struct = Tab.noType;
		return;
   	}
   	minusterm.struct = term;
   		
   }
   
   public void visit(AddopList tlist) {
	   log.info("TERMLIST ON LINE " +tlist.getLine());
	   tlist.struct=tlist.getTerm().struct;
	   Struct tipList;
	   	if(tlist.getTermList().struct.getKind()==Struct.Array) {
	   		tipList = tlist.getTermList().struct.getElemType();
	   	}else {
	   		tipList=tlist.getTermList().struct;
	   	}
	   	if(!(tipList.compatibleWith(tlist.getTerm().struct))) {
	   		report_error("expr i term nisu kompatibilni tipovi, linija "+tlist.getLine(), tlist);
	   	}
   }
   
   public void visit(TermSingle etl) {
   	etl.struct=etl.getTerm().struct;
   }
   
   public void visit(FactExpr flist) {   	
   	
   	Struct te = flist.getTerm().struct;
	Struct f = flist.getFactor().struct;
	if (!te.equals(f) || te != Tab.intType) {
		report_error("Greska na liniji "+ flist.getLine()+" : nekompatibilni tipovi u izrazu za mnozenje.", null);
		flist.struct = Tab.noType;
	}
	
	flist.struct = te;
   	
   }
   
   public void visit(FactorSingle fone) {
   	fone.struct=fone.getFactor().struct;
   }

   public void visit(FactNum cnst){
   	cnst.struct = Tab.intType;
   	vr = cnst.getN1();
   }
   
   public void visit(FactCh cnst){
   	cnst.struct = Tab.charType;
   }
   
   public void visit(FactB cnstbool){
   	cnstbool.struct=CommonKeys.boolType;
   }
   
   public void visit(FactorNew fn) {
   	fn.struct=fn.getType().struct;
   }
   
   public void visit(FactorNewExpr newex) {
   	if(newex.getExpr().struct != Tab.intType) {
   		newex.struct=Tab.noType;
   		report_error("Velicina niza mora biti tipa int.", newex);
   		return;
   	}
	newex.struct=new Struct(Struct.Array, newex.getType().struct);
   }
   
   public void visit(FactorNewMatrix factorNewMatrix) {	
	if(factorNewMatrix.getExpr1().struct != Tab.intType || factorNewMatrix.getExpr().struct != Tab.intType) {
		report_error("Velicina niza mora biti tipa int.", factorNewMatrix);
	}
	factorNewMatrix.struct=new Struct(Struct.Array, new Struct(Struct.Array, currType));
   }
   
   public void visit(FactorNewNoPars fn) {
   	fn.struct=fn.getType().struct;
   }
   
   public void visit(FactorExpr fn) {
   	fn.struct=fn.getExpr().struct;
   }
   
   public void visit(DesignatorDecl identdes) {
	   IdentDecl ident = identdes.getIdentDecl();
	   String name = ident.getIdentName();
	   Obj des;
	   
	   if(ident.getSpecification() != null && ident.getSpecification() instanceof WithDoubleColon) {
		   Obj namespaceObj = Tab.find(name);
		   WithDoubleColon specification = (WithDoubleColon) ident.getSpecification();
		   des = namespaceObj.getType().getMembersTable().searchKey(specification.getName());
	   } else {
		   des = Tab.find(name);
	   }	 
	   
	   if(des == Tab.noObj){
			report_error("Greska na liniji " + identdes.getLine()+ " : ime "+name+" nije deklarisano! ", null);
   		}
	   identdes.obj = des;
   }
   
   
   public void visit(Type type){
	   IdentDecl ident = type.getIdentDecl();
	   String name = ident.getIdentName();
	   
	   if(Objects.isNull(name)) {
		   report_error("IdentName is null", type);
		   return;
	   }
	   
	   Obj typeNode = CommonUtil.getObjByName(name, type.getLine());   	
	   
	   if(CommonKeys.BOOLEAN.equals(name)) {
	   		type.struct=CommonKeys.boolType;
	   		typeNode = Tab.insert(Obj.Type, CommonKeys.BOOLEAN, CommonKeys.boolType);
	   		currType= typeNode.getType();
	   		return;
	   }
	   
	   if(typeNode == Tab.noObj){
      		type.struct = Tab.noType;
      		return;
       }
      
	   if(Obj.Type == typeNode.getKind()){
      		type.struct = typeNode.getType();
      		currType=type.struct;
      		return;
	   }
	   
	   report_error("Greska: Ime " + name + " ne predstavlja tip!", type);
	   type.struct = Tab.noType;
   }
   
   public boolean passed(){
   	return !errorDetected;
   }
   
   
   @Override
   public void visit(ReturnStmt returnStmt) {
       super.visit(returnStmt);
       if (!inMethodDeclaration) {
           report_error("return statement must be inside of a function", returnStmt);
           return;
       }

       if (returnStmt.getOptionalExpr() instanceof Expression) {
           Expr expr = ((Expression) returnStmt.getOptionalExpr()).getExpr();
           if (!Tab.noType.equals(expr.struct) && !expr.struct.equals(currMethod.getType())) {
               report_error("Type mismatch between return type and expression in return", returnStmt);
           }
       }
       else if (returnStmt.getOptionalExpr() instanceof NoExpression) {
           if (!Tab.noType.equals(currMethod.getType())) {
               report_error("empty return can only be placed in void functions",returnStmt);
           }
       }
   }
   
   @Override
   public void visit(ActualParamsStart actualParamsStart) {
       super.visit(actualParamsStart);
   }
   
   @Override
   public void visit(ActualParamsEnd actualParamsEnd) {
       super.visit(actualParamsEnd);
       CommonUtil.validateMethod(currMethod.getName(), functionCallParamTypes);
       functionCallParamTypes.clear();
   }

   @Override
   public void visit(FirstActualParamDecl firstActualParamDecl) {
       super.visit(firstActualParamDecl);
       functionCallParamTypes.add(firstActualParamDecl.getExpr().struct);
   }

   @Override
   public void visit(ActualParamsListDecl actualParamsListDecl) {
       super.visit(actualParamsListDecl);
       functionCallParamTypes.add(actualParamsListDecl.getExpr().struct);
   }
   
   private String getLastIdentifier(Designator designator) {
       if (designator instanceof DesignatorDecl) {
           return ((DesignatorDecl) designator).getIdentDecl().getIdentName();
       }
       else if (designator instanceof DesignatorExprEl) {
           return ((DesignatorExprEl) designator).getArrayName().getArrayName();
       }
       else if (designator instanceof MatrixDesignator) {
           return ((MatrixDesignator) designator).getArrayName().getArrayName();
       }
       return "";
   }
   
   @Override
   public void visit(FunctionCallStmt functionCallStmt) {
       super.visit(functionCallStmt);
       String name = getLastIdentifier(functionCallStmt.getDesignator());
       CommonUtil.checkFunctionParamTypes(name, functionCallStmt, functionCallParamTypes);
       report_info("Found call of function '" + name + "'", functionCallStmt);
   }
   
   @Override
   public void visit(MethodSignatureWithoutParams methodSignatureWithoutParams) {
       super.visit(methodSignatureWithoutParams);
       String methodName = methodSignatureWithoutParams.getMethodName();
       if (CommonUtil.isDoubleDeclaration(methodName, 0, inMethodSignature)) {
           report_error("Identifier '" + methodName + "' already defined", methodSignatureWithoutParams);
           return;
       }

       inMethodDeclaration = true;
       inMethodSignature = true;
       Struct returnTypeStruct = new Struct(Struct.None);
       if (methodSignatureWithoutParams.getReturnType() instanceof NonVoidReturnType) {
           NonVoidReturnType returnType = (NonVoidReturnType) methodSignatureWithoutParams.getReturnType();
           returnTypeStruct = returnType.getType().struct;
       }

       currMethod = Tab.insert(Obj.Meth, methodName, returnTypeStruct);
       methodSignatureWithoutParams.obj = currMethod;
       Tab.openScope();
   }

   @Override
   public void visit(ValidMethodParams validMethodParams) {
       super.visit(validMethodParams);
       if (!inMethodDeclaration)
           return;

       Tab.chainLocalSymbols(currMethod);
       
       MethodDescriptor m = CommonUtil.getDeclaredMethod(currMethod.getName());
       int params = m == null ? 0 : m.getDeclaredParams().size();
       
       currMethod.setLevel(params);
   }

   @Override
   public void visit(MethodSignature MethodSignature) {
       super.visit(MethodSignature);
       inMethodSignature = false;
   }

   @Override
   public void visit(MethodDecl methodDecl) {
       super.visit(methodDecl);
       if (!inMethodDeclaration)
           return;

       Tab.chainLocalSymbols(currMethod);
       Tab.closeScope();
       currMethod = null;
       inMethodDeclaration = false;
   }
   
   @Override
   public void visit(DesignatorFactor designatorFactor) {
       super.visit(designatorFactor);
       Designator designator = designatorFactor.getDesignator();
       designatorFactor.struct = designator.obj.getType();

       //Maybe we can do smth here like chFpar
   }
   
}
