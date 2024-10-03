// generated with ast extension for cup
// version 0.8
// 25/7/2024 19:43:35


package rs.ac.bg.etf.pp1.ast;

public class ConstValueB extends ConstDeclElement {

    private String ConstName;
    private Boolean value;

    public ConstValueB (String ConstName, Boolean value) {
        this.ConstName=ConstName;
        this.value=value;
    }

    public String getConstName() {
        return ConstName;
    }

    public void setConstName(String ConstName) {
        this.ConstName=ConstName;
    }

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value=value;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstValueB(\n");

        buffer.append(" "+tab+ConstName);
        buffer.append("\n");

        buffer.append(" "+tab+value);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstValueB]");
        return buffer.toString();
    }
}
