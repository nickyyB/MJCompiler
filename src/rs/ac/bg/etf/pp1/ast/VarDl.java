// generated with ast extension for cup
// version 0.8
// 25/7/2024 19:43:35


package rs.ac.bg.etf.pp1.ast;

public class VarDl extends VarDeclLista {

    private VarDeclLista VarDeclLista;
    private VarDecl VarDecl;

    public VarDl (VarDeclLista VarDeclLista, VarDecl VarDecl) {
        this.VarDeclLista=VarDeclLista;
        if(VarDeclLista!=null) VarDeclLista.setParent(this);
        this.VarDecl=VarDecl;
        if(VarDecl!=null) VarDecl.setParent(this);
    }

    public VarDeclLista getVarDeclLista() {
        return VarDeclLista;
    }

    public void setVarDeclLista(VarDeclLista VarDeclLista) {
        this.VarDeclLista=VarDeclLista;
    }

    public VarDecl getVarDecl() {
        return VarDecl;
    }

    public void setVarDecl(VarDecl VarDecl) {
        this.VarDecl=VarDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarDeclLista!=null) VarDeclLista.accept(visitor);
        if(VarDecl!=null) VarDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDeclLista!=null) VarDeclLista.traverseTopDown(visitor);
        if(VarDecl!=null) VarDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDeclLista!=null) VarDeclLista.traverseBottomUp(visitor);
        if(VarDecl!=null) VarDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDl(\n");

        if(VarDeclLista!=null)
            buffer.append(VarDeclLista.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDecl!=null)
            buffer.append(VarDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDl]");
        return buffer.toString();
    }
}
