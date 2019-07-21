
// Usage: Only one SlotMaxHardConstraint is needed to be added to hard constraint list
public class SlotMaxHardConstraint extends HardConstraint
{
    public SlotMaxHardConstraint()
    {
        // empty
    }

    @Override
    public boolean IsCourseRelevant(int courseIndex)
    {
        // All courses are subject to slot max
        return true;
    }

    /** PreCondition:
     * the slot being assigned to the lab/lecture is a lab/lecture slot respectively. This allows us to not
     * differentiate between whether a course/slot is lab/lecture in the constraint
     */
    @Override
    public boolean IsAssignmentValid(Environment environment, Problem problem, TimeSlot timeSlot)
    {
        // check how many courses have already been assigned to this course slot
        int numCoursesAssignedToSlot = problem.CoursesPerSlot.containsKey(timeSlot.SlotIndex) ?
                problem.CoursesPerSlot.get(timeSlot.SlotIndex) : 0;

        // Return if there is room for one more course to be assigned to the slot
        return timeSlot.MaxAssignment >= numCoursesAssignedToSlot + 1;
    }
}
