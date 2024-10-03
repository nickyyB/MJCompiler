// generated with ast extension for cup
// version 0.8
// 25/7/2024 19:43:35


package rs.ac.bg.etf.pp1.ast;

public class IdentDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Struct struct = null;

    private String identName;
    private Specification Specification;

    public IdentDecl (String identName, Specification Specification) {
        this.identName=identName;
        this.Specification=Specification;
        if(Specification!=null) Specification.setParent(this);
    }

    public String getIdentName() {
        return identName;
    }

    public void setIdentName(String identName) {
        this.identName=identName;
    }

    public Specification getSpecification() {
        return Specification;
    }

    public void setSpecification(Specification Specification) {
        this.Specification=Specification;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Specification!=null) Specification.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Specification!=null) Specification.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Specification!=null) Specification.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("IdentDecl(\n");

        buffer.append(" "+tab+identName);
        buffer.append("\n");

        if(Specification!=null)
            buffer.append(Specification.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [IdentDecl]");
        return buffer.toString();
    }
}
