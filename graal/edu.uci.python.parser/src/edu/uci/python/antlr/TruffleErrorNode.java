package edu.uci.python.antlr;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;

import edu.uci.python.nodes.*;

/**
 * A node representing erroneous token range in token stream
 */
public class TruffleErrorNode extends PNode {

    private CommonErrorNode errorNode;

    public TruffleErrorNode(TokenStream input, Token start, Token stop, RecognitionException e) {
        this.errorNode = new CommonErrorNode(input, start, stop, e);
    }

    public TruffleErrorNode(CommonErrorNode errorNode) {
        this.errorNode = errorNode;
    }

    @Override
    public boolean isNil() {
        return errorNode.isNil();
    }

    @Override
    public int getAntlrType() {
        return errorNode.getType();
    }

    @Override
    public String getText() {
        return errorNode.getText();
    }

    @Override
    public String toString() {
        return errorNode.toString();
    }
}
