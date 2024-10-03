// generated with ast extension for cup
// version 0.8
// 25/7/2024 19:43:35


package rs.ac.bg.etf.pp1.ast;

public class DesignatorDecl extends Designator {

    private IdentDecl IdentDecl;

    public DesignatorDecl (IdentDecl IdentDecl) {
        this.IdentDecl=IdentDecl;
        if(IdentDecl!=null) IdentDecl.setParent(this);
    }

    public IdentDecl getIdentDecl() {
        return IdentDecl;
    }

    public void setIdentDecl(IdentDecl IdentDecl) {
        this.IdentDecl=IdentDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(IdentDecl!=null) IdentDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(IdentDecl!=null) IdentDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(IdentDecl!=null) IdentDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorDecl(\n");

        if(IdentDecl!=null)
            buffer.append(IdentDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorDecl]");
        return buffer.toString();
    }
}
