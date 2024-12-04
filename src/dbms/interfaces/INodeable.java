package dbms.interfaces;

public interface INodeable {
    public int getNodeKey();

    public boolean lessThan(INodeable node);

    public boolean greaterThan(INodeable node);
}
