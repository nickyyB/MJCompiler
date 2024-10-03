package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;
import rs.ac.bg.etf.pp1.ast.*;

public class RuleVisitor extends VisitorAdaptor {

    int printCallCount = 0;
    int varDeclCount = 0;
    int arrDeclCount = 0;

    Logger log = Logger.getLogger(getClass());


    public void visit(VarDeclIdent vardecl) {
       varDeclCount++;
       log.info(vardecl.getVarName());
    }
    
    public void visit(VarDeclArray arrDecl) {
    	arrDeclCount++;
    	log.info(arrDecl.getArrName());
    }

    public void visit(PrintStat printStmt) {
        printCallCount++;
    }

}