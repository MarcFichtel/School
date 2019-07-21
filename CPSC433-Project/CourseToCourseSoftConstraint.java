/*
    Usage: Pair(courseA, courseB) = new CourseToCourseSoftConstraint(courseA.index, courseB.index, 1, true)
    Usage: SectionDifference(courseA, courseB) = new CourseToCourseSoftConstraint(courseA.index, courseB.index, 1, false)
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class CourseToCourseSoftConstraint extends SoftConstraint
{
    private static HashMap<Integer, HashMap<Boolean, HashSet<Integer>>> sCourseToCourseCache; // key = courseA, <key = (not)wanted, list<int> (not)wanted courses>

    static {
        sCourseToCourseCache = new HashMap<>();
    }
    private int mCourseIndexA;
    private int mCourseIndexB;
    private float mWeightedPenalty;
    private boolean mIsWanted;


    public CourseToCourseSoftConstraint(int courseIndexA, int courseIndexB, int penalty, boolean isWanted)
    {
        super(SoftConstraint.PairType);

        mCourseIndexA = courseIndexA;
        mCourseIndexB = courseIndexB;
        mWeightedPenalty = penalty * ConstraintWeightings[ConstraintType];
        mIsWanted = isWanted;
        Cache(courseIndexA, courseIndexB, isWanted);
    }

    @Override
    public boolean IsSoftConstraintRelevant(int courseIndex) {
        return courseIndex == mCourseIndexA || courseIndex == mCourseIndexB;
    }

    @Override
    public float GetInitialPenalty(Environment environment, Problem problem) {
        assert problem.CurrentProblemIndex == 0;
        // courses haven't been assigned together
        return mIsWanted ? mWeightedPenalty : 0;
    }

    @Override
    public float GetDeltaPenalty(Environment environment, Problem problem, TimeSlot slot) {

        int courseIndex = environment.GetCourseIndexAtProblemIndex(problem.CurrentProblemIndex);
        assert IsSoftConstraintRelevant(courseIndex); // TODO: REMOVE

        // check if the other course in the constraint has already been assigned to
        int otherCourseIndex = courseIndex == mCourseIndexA ? mCourseIndexB : mCourseIndexA;
        int otherProblemIndex = environment.GetProblemIndexForCourseIndex(otherCourseIndex);

        // Has the other course been assigned earlier in the search order
        if (otherProblemIndex < problem.CurrentProblemIndex)
        {
            int otherSlotIndex = problem.Assignments[otherProblemIndex];

            // if they are in the same slot return 0. else return penalty
            if (slot.OverlapsWith(environment.GetSlotAt(otherSlotIndex)))
            {
                // If it overlaps and we want it to overlap, it would have been a penalty before so we reduce the total penalty
                if (mIsWanted)
                {
                    return -mWeightedPenalty;
                }
                else // we didn't want it to overlap, which we gave a 0 penalty to at the start, so now we add to the penalty
                {
                    return mWeightedPenalty;
                }
            }
            else
            {
                // if it doesn't overlap, and we wanted it to overlap, we would have already assigned a penalty in the initial penalty
                // so nothing has changed
                if (mIsWanted)
                {
                    return 0;
                }
                // if it doesn't overlap and we didn't want it to overlap we would have already given it a 0 penalty in the initial penalty
                // so nothing has changed
                else
                {
                    return 0;
                }
            }
//            return  ?
//                    mIsWanted ? -mWeightedPenalty : 0 // In the same slot and we want them in the same slot, reduce total penalty
//                    : mIsWanted ? 0 : mWeightedPenalty; // Not in the same slot and we don't want them in the same slot
        }
        // only one of the courses in the pair has been assigned, return the penalty for now
        return 0;
    }

    public static boolean IsCached(int courseA, int courseB, boolean wanted)
    {
        // cache once in sorted order between courseA and courseB
        int keyCourse = courseA < courseB ? courseA : courseB;
        int valueCourse = courseA < courseB ? courseB : courseA;

        if (sCourseToCourseCache.containsKey(keyCourse))
        {
            if (sCourseToCourseCache.get(keyCourse).containsKey(wanted))
            {
                if (sCourseToCourseCache.get(keyCourse).get(wanted).contains(valueCourse))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private static void Cache(int courseA, int courseB, boolean wanted)
    {
        // cache once in sorted order between courseA and courseB
        int keyCourse = courseA < courseB ? courseA : courseB;
        int valueCourse = courseA < courseB ? courseB : courseA;

        HashMap<Boolean, HashSet<Integer>> cache = sCourseToCourseCache.containsKey(keyCourse) ?
                sCourseToCourseCache.get(keyCourse)
                : new HashMap<>();

        HashSet<Integer> otherCourses = cache.containsKey(wanted) ?
                cache.get(wanted)
                : new HashSet<>();

        otherCourses.add(valueCourse);
        sCourseToCourseCache.replace(keyCourse, cache);
    }
}
