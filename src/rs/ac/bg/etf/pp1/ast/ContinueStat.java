// generated with ast extension for cup
// version 0.8
// 25/7/2024 19:43:35


package rs.ac.bg.etf.pp1.ast;

public class ContinueStat extends Matched {

    public ContinueStat () {
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
        buffer.append("ContinueStat(\n");

        buffer.append(tab);
        buffer.append(") [ContinueStat]");
        return buffer.toString();
    }
}
