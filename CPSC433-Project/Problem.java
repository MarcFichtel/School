import java.util.HashMap;

public class Problem
{
    //TODO: Add FEval value?
    public final int[] Assignments; // Assignments[i] = j, where: i == ProblemIndex, j == SlotIndex
    public final int CurrentProblemIndex; // Next problemIndex to assign a course to. Assert: Assignments[CurrentProblemIndex] = -1
    public final HashMap<Integer, Integer> CoursesPerSlot; // key = TimeSlot.SlotIndex, Value = num courses added to that slot

    // Constructor for initial problem state
    public Problem(int problemSize)
    {
        Assignments = new int[problemSize];
        for(int i = 0; i < problemSize; ++i)
        {
            Assignments[i] = -1; // no assignment has been made yet
        }

        CoursesPerSlot = new HashMap<Integer, Integer>();
        CurrentProblemIndex = 0;
    }

    // Constructor for a sub problem of parent after solving subproblem
    public Problem(Problem parentProblem, int slotIndex)
    {
        // Copy problem description from parent
        int[] assignments = parentProblem.Assignments.clone();
        // assign slotIndex to subproblem
        assignments[parentProblem.CurrentProblemIndex] = slotIndex;
        // finalize problem description
        Assignments = assignments;

        // Copy TimeSlot counters
        HashMap<Integer, Integer> coursesPerSlot = (HashMap)parentProblem.CoursesPerSlot.clone();

        // Increment count at slotIndex to reflect new course being added to that slot
        if (!coursesPerSlot.containsKey(slotIndex))
        {
            coursesPerSlot.put(slotIndex, 1);
        }
        else
        {
            coursesPerSlot.replace(slotIndex, coursesPerSlot.get(slotIndex) + 1);
        }
        // Finalize Slot counter
        CoursesPerSlot = coursesPerSlot;
        // Set next sub problem to be solved
        CurrentProblemIndex = parentProblem.CurrentProblemIndex + 1;
    }

    // returns if the last course in the assignments array has been assigned to
    // ASSUMPTION: Assignment array is filled in the same order as {Courses + Labs} is ordered ie left to right
    public boolean IsSolved()
    {
        return CurrentProblemIndex == Assignments.length;
    }

    // Print all assignments of courses to slots
    public String ToString(Environment environment)
    {
        String output = "";

        // Assignments[probIndex] = slotIndex
        for (int probIndex = 0; probIndex < Assignments.length; ++probIndex) {
            int slotIndex = Assignments[probIndex];
            String courseString = environment.GetCourseStringFromProblemIndex(probIndex);
            String slotString = environment.GetSlotAt(slotIndex).OutputString;

            output += courseString;

            // Course is a lecture: Add 3 tabs
            if (environment.IsProblemNumberALecture(probIndex)) {
                output += "\t\t\t: ";
            }

            // Course is a lab without section number: Add 2 tabs
            else if (!courseString.contains("LEC"))
            {
                output += "\t\t\t: ";
            }

            // Course is a lab with section number: Add 1 tab
            else
            {
                output += "\t\t: ";
            }

            output += slotString;
            output += "\n";
        }
        return output;
    }
}
