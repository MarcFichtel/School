import java.util.HashMap;
import java.util.HashSet;

/**
 * Usage:
 * Pair(CourseA, CourseB) = new CourseToCourseHardConstraint( CourseA.Index, CourseB.Index, true)
 * NotCompatible(CourseA, CourseB) = new CourseToCourseHardConstraint( CourseA.Index, CourseB.Index, false)
 * NoLectureLabOverlap(Lecture, Lab) = new CourseToCourseHardConstraint( Lecture.Index, Lab.Index, false)
 */
public class CourseToCourseHardConstraint extends HardConstraint
{
    private static HashMap<Integer, HashMap<Boolean, HashSet<Integer>>> sCourseToCourseCache;

    static {
        sCourseToCourseCache = new HashMap<>();
    }

    private final int mCourseIndexA;
    private final int mCourseIndexB;
    private final boolean mIsWanted;

    public CourseToCourseHardConstraint(int courseIndexA, int courseIndexB, boolean isWanted)
    {
        mCourseIndexA = courseIndexA;
        mCourseIndexB = courseIndexB;
        mIsWanted = isWanted;
        // assert HasCourseBeenAdded(...) == false;
        Cache(courseIndexA, courseIndexB, isWanted);
    }

    @Override
    public boolean IsCourseRelevant(int courseIndex)
    {
        return courseIndex == mCourseIndexA || courseIndex == mCourseIndexB;
    }

    @Override
    public boolean IsAssignmentValid(Environment environment, Problem problem, TimeSlot timeSlot)
    {
        int courseIndex = environment.GetCourseIndexAtProblemIndex(problem.CurrentProblemIndex);
        assert IsCourseRelevant(courseIndex); // TODO: REMOVE

        // check if the other course in the constraint has already been assigned to
        int otherCourseIndex = courseIndex == mCourseIndexA ? mCourseIndexB : mCourseIndexA;
        int otherProblemIndex = environment.GetProblemIndexForCourseIndex(otherCourseIndex);
        // Does the other course get assigned to earlier in the search order
        if (otherProblemIndex < problem.CurrentProblemIndex)
        {
            int otherSlotIndex = problem.Assignments[otherProblemIndex];

            // if we want them to be in the same slot and they are in slots that overlap
            return !(timeSlot.OverlapsWith(environment.GetSlotAt(otherSlotIndex)) ^ mIsWanted);
        }
        // else first course in the constraint return true

        return true;
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
