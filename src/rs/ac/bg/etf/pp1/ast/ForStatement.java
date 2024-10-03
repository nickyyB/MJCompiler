// generated with ast extension for cup
// version 0.8
// 25/7/2024 19:43:35


package rs.ac.bg.etf.pp1.ast;

public class ForStatement implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private ForCond ForCond;
    private ForInit ForInit;
    private CondFactFor CondFactFor;
    private ForPreDesignator ForPreDesignator;
    private ForCond ForCond1;
    private ForPostDesignator ForPostDesignator;

    public ForStatement (ForCond ForCond, ForInit ForInit, CondFactFor CondFactFor, ForPreDesignator ForPreDesignator, ForCond ForCond1, ForPostDesignator ForPostDesignator) {
        this.ForCond=ForCond;
        if(ForCond!=null) ForCond.setParent(this);
        this.ForInit=ForInit;
        if(ForInit!=null) ForInit.setParent(this);
        this.CondFactFor=CondFactFor;
        if(CondFactFor!=null) CondFactFor.setParent(this);
        this.ForPreDesignator=ForPreDesignator;
        if(ForPreDesignator!=null) ForPreDesignator.setParent(this);
        this.ForCond1=ForCond1;
        if(ForCond1!=null) ForCond1.setParent(this);
        this.ForPostDesignator=ForPostDesignator;
        if(ForPostDesignator!=null) ForPostDesignator.setParent(this);
    }

    public ForCond getForCond() {
        return ForCond;
    }

    public void setForCond(ForCond ForCond) {
        this.ForCond=ForCond;
    }

    public ForInit getForInit() {
        return ForInit;
    }

    public void setForInit(ForInit ForInit) {
        this.ForInit=ForInit;
    }

    public CondFactFor getCondFactFor() {
        return CondFactFor;
    }

    public void setCondFactFor(CondFactFor CondFactFor) {
        this.CondFactFor=CondFactFor;
    }

    public ForPreDesignator getForPreDesignator() {
        return ForPreDesignator;
    }

    public void setForPreDesignator(ForPreDesignator ForPreDesignator) {
        this.ForPreDesignator=ForPreDesignator;
    }

    public ForCond getForCond1() {
        return ForCond1;
    }

    public void setForCond1(ForCond ForCond1) {
        this.ForCond1=ForCond1;
    }

    public ForPostDesignator getForPostDesignator() {
        return ForPostDesignator;
    }

    public void setForPostDesignator(ForPostDesignator ForPostDesignator) {
        this.ForPostDesignator=ForPostDesignator;
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
        if(ForCond!=null) ForCond.accept(visitor);
        if(ForInit!=null) ForInit.accept(visitor);
        if(CondFactFor!=null) CondFactFor.accept(visitor);
        if(ForPreDesignator!=null) ForPreDesignator.accept(visitor);
        if(ForCond1!=null) ForCond1.accept(visitor);
        if(ForPostDesignator!=null) ForPostDesignator.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ForCond!=null) ForCond.traverseTopDown(visitor);
        if(ForInit!=null) ForInit.traverseTopDown(visitor);
        if(CondFactFor!=null) CondFactFor.traverseTopDown(visitor);
        if(ForPreDesignator!=null) ForPreDesignator.traverseTopDown(visitor);
        if(ForCond1!=null) ForCond1.traverseTopDown(visitor);
        if(ForPostDesignator!=null) ForPostDesignator.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ForCond!=null) ForCond.traverseBottomUp(visitor);
        if(ForInit!=null) ForInit.traverseBottomUp(visitor);
        if(CondFactFor!=null) CondFactFor.traverseBottomUp(visitor);
        if(ForPreDesignator!=null) ForPreDesignator.traverseBottomUp(visitor);
        if(ForCond1!=null) ForCond1.traverseBottomUp(visitor);
        if(ForPostDesignator!=null) ForPostDesignator.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ForStatement(\n");

        if(ForCond!=null)
            buffer.append(ForCond.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForInit!=null)
            buffer.append(ForInit.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CondFactFor!=null)
            buffer.append(CondFactFor.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForPreDesignator!=null)
            buffer.append(ForPreDesignator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForCond1!=null)
            buffer.append(ForCond1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForPostDesignator!=null)
            buffer.append(ForPostDesignator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ForStatement]");
        return buffer.toString();
    }
}
