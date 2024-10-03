// generated with ast extension for cup
// version 0.8
// 25/7/2024 19:43:35


package rs.ac.bg.etf.pp1.ast;

public class DesignatorFactor extends Factor {

    private Designator Designator;
    private OptionalFunctionCall OptionalFunctionCall;

    public DesignatorFactor (Designator Designator, OptionalFunctionCall OptionalFunctionCall) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.OptionalFunctionCall=OptionalFunctionCall;
        if(OptionalFunctionCall!=null) OptionalFunctionCall.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public OptionalFunctionCall getOptionalFunctionCall() {
        return OptionalFunctionCall;
    }

    public void setOptionalFunctionCall(OptionalFunctionCall OptionalFunctionCall) {
        this.OptionalFunctionCall=OptionalFunctionCall;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(OptionalFunctionCall!=null) OptionalFunctionCall.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(OptionalFunctionCall!=null) OptionalFunctionCall.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(OptionalFunctionCall!=null) OptionalFunctionCall.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorFactor(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptionalFunctionCall!=null)
            buffer.append(OptionalFunctionCall.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorFactor]");
        return buffer.toString();
    }
}
