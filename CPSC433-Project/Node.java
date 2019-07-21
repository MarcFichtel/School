public class Node
{
    public final Node Parent; // TODO: Not needed if all state information is stored in node
    public final int Depth;
    public final Problem Prob;
    public final int Penalty;

    public Node(Problem initialProblem, int penalty)
    {
        Parent = null;
        Depth = 0;
        Prob = initialProblem;
        Penalty = penalty;
    }

    public Node(Node parent, int slotIndex, int deltaPenalty)
    {
        Parent = parent;
        Prob = new Problem(parent.Prob, slotIndex);
        Depth = parent.Depth + 1;
        Penalty = parent.Penalty + deltaPenalty;
    }
}
