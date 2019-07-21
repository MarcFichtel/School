
// TODO: I think the TA or denzinger mentioned that there's a possibility for there to be a hard constraint without
// a course that exists in the provided courses. Add error checks in the constructors for each hardconstraint such that
// it checks if its parameters exist in environment. if not just always return true
abstract class HardConstraint
{
    protected HardConstraint()
    {
        // empty
    }

    public abstract boolean IsCourseRelevant(int courseIndex);
    public abstract boolean IsAssignmentValid(Environment environment, Problem problem, TimeSlot slot);
}
