// generated with ast extension for cup
// version 0.8
// 25/7/2024 19:43:35


package rs.ac.bg.etf.pp1.ast;

public class OrList extends CondTerms {

    private CondTerms CondTerms;
    private OrOp OrOp;
    private CondTerm CondTerm;

    public OrList (CondTerms CondTerms, OrOp OrOp, CondTerm CondTerm) {
        this.CondTerms=CondTerms;
        if(CondTerms!=null) CondTerms.setParent(this);
        this.OrOp=OrOp;
        if(OrOp!=null) OrOp.setParent(this);
        this.CondTerm=CondTerm;
        if(CondTerm!=null) CondTerm.setParent(this);
    }

    public CondTerms getCondTerms() {
        return CondTerms;
    }

    public void setCondTerms(CondTerms CondTerms) {
        this.CondTerms=CondTerms;
    }

    public OrOp getOrOp() {
        return OrOp;
    }

    public void setOrOp(OrOp OrOp) {
        this.OrOp=OrOp;
    }

    public CondTerm getCondTerm() {
        return CondTerm;
    }

    public void setCondTerm(CondTerm CondTerm) {
        this.CondTerm=CondTerm;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(CondTerms!=null) CondTerms.accept(visitor);
        if(OrOp!=null) OrOp.accept(visitor);
        if(CondTerm!=null) CondTerm.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(CondTerms!=null) CondTerms.traverseTopDown(visitor);
        if(OrOp!=null) OrOp.traverseTopDown(visitor);
        if(CondTerm!=null) CondTerm.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(CondTerms!=null) CondTerms.traverseBottomUp(visitor);
        if(OrOp!=null) OrOp.traverseBottomUp(visitor);
        if(CondTerm!=null) CondTerm.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("OrList(\n");

        if(CondTerms!=null)
            buffer.append(CondTerms.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OrOp!=null)
            buffer.append(OrOp.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CondTerm!=null)
            buffer.append(CondTerm.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [OrList]");
        return buffer.toString();
    }
}
