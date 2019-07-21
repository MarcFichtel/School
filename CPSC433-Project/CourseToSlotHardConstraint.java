import java.util.HashMap;
import java.util.HashSet;

/* Usage:
  * partAssign(course,slot) = new CourseToSlotHardConstraint(course.index, slot.index, true)
  * unwanted(course,slot) = new CourseToSlotHardConstraint(course.index, slot.index, false)
*/
public class CourseToSlotHardConstraint extends HardConstraint
{
    private static HashMap<Integer, HashMap<Boolean, HashSet<Integer>>> sCourseToSlotCache;

    static
    {
        sCourseToSlotCache = new HashMap<>();
    }

    private final int mCourseIndex;
    private final int mSlotIndex;
    private final boolean mIsWanted;

    public CourseToSlotHardConstraint(int courseIndex, int slotIndex, boolean isWanted)
    {
        mCourseIndex = courseIndex;
        mSlotIndex = slotIndex;
        mIsWanted = isWanted;
        Cache(courseIndex, slotIndex, isWanted);
    }

    @Override
    public boolean IsCourseRelevant(int courseIndex)
    {
        return mCourseIndex == courseIndex;
    }

    /*
        if IsCourseRelevant(courseIndex): returns if slot being assigned is the one called for in constraint and if we
        want it or not. ie partassign(course,slot) == true unwanted(course,slot) == false
        else return true because not applicable to this constraint
     */
    @Override
    public boolean IsAssignmentValid(Environment environment, Problem problem, TimeSlot timeSlot)
    {
        int courseIndex = environment.GetCourseIndexAtProblemIndex(problem.CurrentProblemIndex);
        assert IsCourseRelevant(courseIndex); // TODO: REMOVE

        return !(timeSlot.SlotIndex == mSlotIndex ^ mIsWanted);
    }

    public static boolean IsCached(int courseIndex, int slotIndex, boolean isWanted)
    {
        if (sCourseToSlotCache.containsKey(courseIndex))
        {
            if (sCourseToSlotCache.get(courseIndex).containsKey(isWanted))
            {
                if (sCourseToSlotCache.get(courseIndex).get(isWanted).contains(slotIndex))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private static void Cache(int courseIndex, int slotIndex, boolean isWanted)
    {
        HashMap<Boolean, HashSet<Integer>> cache = sCourseToSlotCache.containsKey(courseIndex) ?
                sCourseToSlotCache.get(courseIndex)
                : new HashMap<>();

        HashSet<Integer> otherCourses = cache.containsKey(isWanted) ?
                cache.get(isWanted)
                : new HashSet<>();

        otherCourses.add(slotIndex);
        sCourseToSlotCache.replace(courseIndex, cache);
    }
}
