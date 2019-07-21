import java.util.*;

public class SearchManager
{
    private Stack<Node> mOpenLeafs;
    public Problem mBestSolution;
    public int mBestSolutionValue;

    private long mSearchMaxDuration;

    private final Environment mEnvironment;

    public SearchManager(Environment environment)
    {
        mEnvironment = environment;
        mOpenLeafs = new Stack<>();
        mBestSolution = null;
        mBestSolutionValue = -1;
    }

    // Returns the best schedule assignment possible or null if no assignment is possible
    public Problem Search(long searchDuration)
    {
        Problem initialProblem = new Problem(mEnvironment.ProblemSize);
        float initialPenalty = 0;
        for (SoftConstraint softConstraint : mEnvironment.SoftConstraints)
        {
            initialPenalty += softConstraint.GetInitialPenalty(mEnvironment, initialProblem);
        }
        assert initialPenalty >= 0;

        mOpenLeafs.push(new Node(initialProblem, (int)initialPenalty));
        mSearchMaxDuration = System.currentTimeMillis() + searchDuration;
        while (mOpenLeafs.size() > 0 && (searchDuration == -1 || mSearchMaxDuration > System.currentTimeMillis()))
        {
            Node toConsider = mOpenLeafs.pop();

            if (toConsider.Prob.IsSolved())
            {
                // All solutions are valid solutions becase each subproblem forming the solution is valid
                // Check if valid solution is the best solution thus far
                int solutionValue = toConsider.Penalty;
                if (mBestSolutionValue == -1 /*No solution found yet*/ || solutionValue < mBestSolutionValue)
                {
                    mBestSolutionValue = solutionValue;
                    mBestSolution =  toConsider.Prob;
                }
            }
            else // Divide problem into sub problems
            {
                Vector<TimeSlot> slotAssignments = FDiv(toConsider.Prob);
                for (TimeSlot slotAssignment : slotAssignments) {

                    // calculate soft constraint penalty for assignment
                    float deltaPenalty = 0;
                    for (SoftConstraint softConstraint : mEnvironment.GetSoftConstraintsForProblemIndex(toConsider.Prob.CurrentProblemIndex))
                    {
                        deltaPenalty += softConstraint.GetDeltaPenalty(mEnvironment, toConsider.Prob, slotAssignment);
                    }
                    // Discard all partial solutions that are
                    mOpenLeafs.push(new Node(toConsider, slotAssignment.SlotIndex, (int)deltaPenalty));
                }
                // if slotAssignments.length == 0, reference for @toConsider is lost and node is garbage collected.
                // ERW* is therefore implemented by default
            }
        }

        return mBestSolution;
    }

    // Returns all VALID slot assignments for the current problemIndex
    private Vector<TimeSlot> FDiv(Problem subProblem)
    {
        Vector<TimeSlot> slotAssignments = new Vector<>(); // to return

        TimeSlot[] potentialTimeSlots = mEnvironment.GetSlotsForProblemIndex(subProblem.CurrentProblemIndex);
        Vector<HardConstraint> hardConstraintsForCourse = mEnvironment.GetHardConstraintsForProblemIndex(subProblem.CurrentProblemIndex);

        for(TimeSlot timeSlot : potentialTimeSlots)
        {
            boolean isValidSlot = true;
            for (HardConstraint hardConstraint : hardConstraintsForCourse)
            {
                isValidSlot &= hardConstraint.IsAssignmentValid(mEnvironment, subProblem, timeSlot);
            }
            if (isValidSlot)
            {
                slotAssignments.add(timeSlot);
            }
        }

        return slotAssignments;
    }
}
