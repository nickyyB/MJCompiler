// generated with ast extension for cup
// version 0.8
// 25/7/2024 19:43:35


package rs.ac.bg.etf.pp1.ast;

public class ErroneousFormalParam extends FormalParamsList {

    private SingleFormalParamDecl SingleFormalParamDecl;

    public ErroneousFormalParam (SingleFormalParamDecl SingleFormalParamDecl) {
        this.SingleFormalParamDecl=SingleFormalParamDecl;
        if(SingleFormalParamDecl!=null) SingleFormalParamDecl.setParent(this);
    }

    public SingleFormalParamDecl getSingleFormalParamDecl() {
        return SingleFormalParamDecl;
    }

    public void setSingleFormalParamDecl(SingleFormalParamDecl SingleFormalParamDecl) {
        this.SingleFormalParamDecl=SingleFormalParamDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(SingleFormalParamDecl!=null) SingleFormalParamDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(SingleFormalParamDecl!=null) SingleFormalParamDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(SingleFormalParamDecl!=null) SingleFormalParamDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ErroneousFormalParam(\n");

        if(SingleFormalParamDecl!=null)
            buffer.append(SingleFormalParamDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ErroneousFormalParam]");
        return buffer.toString();
    }
}
