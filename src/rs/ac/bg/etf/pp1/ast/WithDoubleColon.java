// generated with ast extension for cup
// version 0.8
// 25/7/2024 19:43:35


package rs.ac.bg.etf.pp1.ast;

public class WithDoubleColon extends Specification {

    private DblColon DblColon;
    private String name;

    public WithDoubleColon (DblColon DblColon, String name) {
        this.DblColon=DblColon;
        if(DblColon!=null) DblColon.setParent(this);
        this.name=name;
    }

    public DblColon getDblColon() {
        return DblColon;
    }

    public void setDblColon(DblColon DblColon) {
        this.DblColon=DblColon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DblColon!=null) DblColon.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DblColon!=null) DblColon.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DblColon!=null) DblColon.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("WithDoubleColon(\n");

        if(DblColon!=null)
            buffer.append(DblColon.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+name);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [WithDoubleColon]");
        return buffer.toString();
    }
}
