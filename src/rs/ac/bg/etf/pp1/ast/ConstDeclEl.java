// generated with ast extension for cup
// version 0.8
// 25/7/2024 19:43:35


package rs.ac.bg.etf.pp1.ast;

public class ConstDeclEl extends ConstDeclList {

    private ConstDeclElement ConstDeclElement;

    public ConstDeclEl (ConstDeclElement ConstDeclElement) {
        this.ConstDeclElement=ConstDeclElement;
        if(ConstDeclElement!=null) ConstDeclElement.setParent(this);
    }

    public ConstDeclElement getConstDeclElement() {
        return ConstDeclElement;
    }

    public void setConstDeclElement(ConstDeclElement ConstDeclElement) {
        this.ConstDeclElement=ConstDeclElement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstDeclElement!=null) ConstDeclElement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstDeclElement!=null) ConstDeclElement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstDeclElement!=null) ConstDeclElement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDeclEl(\n");

        if(ConstDeclElement!=null)
            buffer.append(ConstDeclElement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDeclEl]");
        return buffer.toString();
    }
}
