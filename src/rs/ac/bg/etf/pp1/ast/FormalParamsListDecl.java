// generated with ast extension for cup
// version 0.8
// 25/7/2024 19:43:35


package rs.ac.bg.etf.pp1.ast;

public class FormalParamsListDecl extends FormalParamsList {

    private FormalParamsList FormalParamsList;
    private SingleFormalParamDecl SingleFormalParamDecl;

    public FormalParamsListDecl (FormalParamsList FormalParamsList, SingleFormalParamDecl SingleFormalParamDecl) {
        this.FormalParamsList=FormalParamsList;
        if(FormalParamsList!=null) FormalParamsList.setParent(this);
        this.SingleFormalParamDecl=SingleFormalParamDecl;
        if(SingleFormalParamDecl!=null) SingleFormalParamDecl.setParent(this);
    }

    public FormalParamsList getFormalParamsList() {
        return FormalParamsList;
    }

    public void setFormalParamsList(FormalParamsList FormalParamsList) {
        this.FormalParamsList=FormalParamsList;
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
        if(FormalParamsList!=null) FormalParamsList.accept(visitor);
        if(SingleFormalParamDecl!=null) SingleFormalParamDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FormalParamsList!=null) FormalParamsList.traverseTopDown(visitor);
        if(SingleFormalParamDecl!=null) SingleFormalParamDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FormalParamsList!=null) FormalParamsList.traverseBottomUp(visitor);
        if(SingleFormalParamDecl!=null) SingleFormalParamDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormalParamsListDecl(\n");

        if(FormalParamsList!=null)
            buffer.append(FormalParamsList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(SingleFormalParamDecl!=null)
            buffer.append(SingleFormalParamDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormalParamsListDecl]");
        return buffer.toString();
    }
}
