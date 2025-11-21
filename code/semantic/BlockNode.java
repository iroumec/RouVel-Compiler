package semantic;

public class BlockNode { //los nodos del arbol son funciones (la raiz) y los hijos son bloques if / else
    private String id;
    private BlockNode izq;
    private BlockNode der;
    private boolean returnSentence;

    public BlockNode () {
        this.izq = null;
        this.der = null;
        this.returnSentence = false;
    }

    public boolean hasReturn() {
        return (returnSentence && (izq == null && der == null) || !returnSentence && (izq.hasReturn() && der.hasReturn()));
    }
}