// generated with ast extension for cup
// version 0.8
// 25/7/2024 19:43:35


package rs.ac.bg.etf.pp1.ast;

public class StaticInitializer implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private StaticInitializerStart StaticInitializerStart;
    private StatementList StatementList;

    public StaticInitializer (StaticInitializerStart StaticInitializerStart, StatementList StatementList) {
        this.StaticInitializerStart=StaticInitializerStart;
        if(StaticInitializerStart!=null) StaticInitializerStart.setParent(this);
        this.StatementList=StatementList;
        if(StatementList!=null) StatementList.setParent(this);
    }

    public StaticInitializerStart getStaticInitializerStart() {
        return StaticInitializerStart;
    }

    public void setStaticInitializerStart(StaticInitializerStart StaticInitializerStart) {
        this.StaticInitializerStart=StaticInitializerStart;
    }

    public StatementList getStatementList() {
        return StatementList;
    }

    public void setStatementList(StatementList StatementList) {
        this.StatementList=StatementList;
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
        if(StaticInitializerStart!=null) StaticInitializerStart.accept(visitor);
        if(StatementList!=null) StatementList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(StaticInitializerStart!=null) StaticInitializerStart.traverseTopDown(visitor);
        if(StatementList!=null) StatementList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(StaticInitializerStart!=null) StaticInitializerStart.traverseBottomUp(visitor);
        if(StatementList!=null) StatementList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StaticInitializer(\n");

        if(StaticInitializerStart!=null)
            buffer.append(StaticInitializerStart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(StatementList!=null)
            buffer.append(StatementList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StaticInitializer]");
        return buffer.toString();
    }
}
