// generated with ast extension for cup
// version 0.8
// 25/7/2024 19:43:35


package rs.ac.bg.etf.pp1.ast;

public class MethodSignature implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private MethodSignatureWithoutParams MethodSignatureWithoutParams;
    private MethodParams MethodParams;

    public MethodSignature (MethodSignatureWithoutParams MethodSignatureWithoutParams, MethodParams MethodParams) {
        this.MethodSignatureWithoutParams=MethodSignatureWithoutParams;
        if(MethodSignatureWithoutParams!=null) MethodSignatureWithoutParams.setParent(this);
        this.MethodParams=MethodParams;
        if(MethodParams!=null) MethodParams.setParent(this);
    }

    public MethodSignatureWithoutParams getMethodSignatureWithoutParams() {
        return MethodSignatureWithoutParams;
    }

    public void setMethodSignatureWithoutParams(MethodSignatureWithoutParams MethodSignatureWithoutParams) {
        this.MethodSignatureWithoutParams=MethodSignatureWithoutParams;
    }

    public MethodParams getMethodParams() {
        return MethodParams;
    }

    public void setMethodParams(MethodParams MethodParams) {
        this.MethodParams=MethodParams;
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
        if(MethodSignatureWithoutParams!=null) MethodSignatureWithoutParams.accept(visitor);
        if(MethodParams!=null) MethodParams.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MethodSignatureWithoutParams!=null) MethodSignatureWithoutParams.traverseTopDown(visitor);
        if(MethodParams!=null) MethodParams.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MethodSignatureWithoutParams!=null) MethodSignatureWithoutParams.traverseBottomUp(visitor);
        if(MethodParams!=null) MethodParams.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodSignature(\n");

        if(MethodSignatureWithoutParams!=null)
            buffer.append(MethodSignatureWithoutParams.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethodParams!=null)
            buffer.append(MethodParams.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethodSignature]");
        return buffer.toString();
    }
}
