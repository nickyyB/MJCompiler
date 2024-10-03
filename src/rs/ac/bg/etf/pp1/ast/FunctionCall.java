// generated with ast extension for cup
// version 0.8
// 25/7/2024 19:43:35


package rs.ac.bg.etf.pp1.ast;

public class FunctionCall extends OptionalFunctionCall {

    private ActualParamsStart ActualParamsStart;
    private ActualParams ActualParams;
    private ActualParamsEnd ActualParamsEnd;

    public FunctionCall (ActualParamsStart ActualParamsStart, ActualParams ActualParams, ActualParamsEnd ActualParamsEnd) {
        this.ActualParamsStart=ActualParamsStart;
        if(ActualParamsStart!=null) ActualParamsStart.setParent(this);
        this.ActualParams=ActualParams;
        if(ActualParams!=null) ActualParams.setParent(this);
        this.ActualParamsEnd=ActualParamsEnd;
        if(ActualParamsEnd!=null) ActualParamsEnd.setParent(this);
    }

    public ActualParamsStart getActualParamsStart() {
        return ActualParamsStart;
    }

    public void setActualParamsStart(ActualParamsStart ActualParamsStart) {
        this.ActualParamsStart=ActualParamsStart;
    }

    public ActualParams getActualParams() {
        return ActualParams;
    }

    public void setActualParams(ActualParams ActualParams) {
        this.ActualParams=ActualParams;
    }

    public ActualParamsEnd getActualParamsEnd() {
        return ActualParamsEnd;
    }

    public void setActualParamsEnd(ActualParamsEnd ActualParamsEnd) {
        this.ActualParamsEnd=ActualParamsEnd;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ActualParamsStart!=null) ActualParamsStart.accept(visitor);
        if(ActualParams!=null) ActualParams.accept(visitor);
        if(ActualParamsEnd!=null) ActualParamsEnd.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ActualParamsStart!=null) ActualParamsStart.traverseTopDown(visitor);
        if(ActualParams!=null) ActualParams.traverseTopDown(visitor);
        if(ActualParamsEnd!=null) ActualParamsEnd.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ActualParamsStart!=null) ActualParamsStart.traverseBottomUp(visitor);
        if(ActualParams!=null) ActualParams.traverseBottomUp(visitor);
        if(ActualParamsEnd!=null) ActualParamsEnd.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FunctionCall(\n");

        if(ActualParamsStart!=null)
            buffer.append(ActualParamsStart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActualParams!=null)
            buffer.append(ActualParams.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActualParamsEnd!=null)
            buffer.append(ActualParamsEnd.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FunctionCall]");
        return buffer.toString();
    }
}
