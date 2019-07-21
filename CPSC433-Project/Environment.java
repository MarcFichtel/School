import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Vector;

public class Environment
{
    private final TimeSlot[] mLectureSlots;
    private final TimeSlot[] mEveningLectureSlots;
    private final TimeSlot[] mLabSlots;
    private final TimeSlot[] mEveningLabSlots;

    public final int NumSlots;
    private final Course[] mCourses;
    private final int mNumLectures;
    private final int mNumLabs;
    public final int ProblemSize;

    private final HashMap<Integer, Integer> mProhlemIndexToCourseIndexMap;
    private final HashMap<Integer, Integer> mCourseIndexToProblemIndexMap;
    private final HashMap<Integer, Integer> mPartAssignLookup;

    public final HardConstraint[] HardConstraints;
    public final SoftConstraint[] SoftConstraints;

    public final HashMap<Integer, Vector<HardConstraint>> CourseIndexToHardConstraintsMap;
    public final HashMap<Integer, Vector<SoftConstraint>> CourseIndexToSoftConstraintsMap;

    public Environment(TimeSlot[] lectureSlots, TimeSlot[] labSlots,
                       HardConstraint[] hardConstraints, SoftConstraint[] softConstraints,
                       Course[] courses, Course[] labs,
                       HashMap<Integer, Integer> partAssignMap)
    {
        mLectureSlots = lectureSlots;

        // Find all evening Lecture Slots
        ArrayList<TimeSlot> eveningSlots = new ArrayList<>();
        for(TimeSlot lectureSlot : lectureSlots)
        {
            if (lectureSlot.StartTime >= TimeSlot.EveningSlotStartTime)
            {
                eveningSlots.add(lectureSlot);
            }
        }
        TimeSlot[] eveningLectureSlots = new TimeSlot[eveningSlots.size()];
        eveningSlots.toArray(eveningLectureSlots);
        mEveningLectureSlots = eveningLectureSlots;

        // Find all evening Lab Slots
        eveningSlots.clear();
        for(TimeSlot labSlot : labSlots)
        {
            if (labSlot.StartTime >= TimeSlot.EveningSlotStartTime)
            {
                eveningSlots.add(labSlot);
            }
        }
        TimeSlot[] eveningLabSlots = new TimeSlot[eveningSlots.size()];
        eveningSlots.toArray(eveningLabSlots);
        mEveningLabSlots = eveningLabSlots;

        mLabSlots = labSlots;
        NumSlots = lectureSlots.length + labSlots.length;
        HardConstraints = hardConstraints;
        SoftConstraints = softConstraints;
        mNumLectures = courses.length;
        mNumLabs = labs.length;
        ProblemSize = mNumLectures + mNumLabs;

        Course[] mergedCourses = new Course[ProblemSize];
        System.arraycopy(courses,0, mergedCourses, 0, courses.length);
        System.arraycopy(labs,0, mergedCourses, courses.length, labs.length);
        mCourses = mergedCourses;

        mPartAssignLookup = partAssignMap;

        TreeMap<Integer, Vector<Integer>> sortedCourseListings = new TreeMap<>();
        // key = numConstraints per course, value = list of courseindex coresponding to the course that has that many constraints
        // TreeMap.foreach will return a sorted iteration of its keys saving us havering to sort later. Yay!

        HashMap<Integer, Vector<HardConstraint>> courseIndexToHardConstraints = new HashMap<>();
        HashMap<Integer, Vector<SoftConstraint>> courseIndexToSoftConstraints = new HashMap<>();
        // key = courseIndex, value = Vector<constraints> relevant for that course

        for (int courseIndex = 0; courseIndex < ProblemSize; ++courseIndex)
        {
            // Find all hard constraints that are relevant to Courses[courseIndex]
            Vector<HardConstraint> hard = new Vector<>();
            Vector<SoftConstraint> soft = new Vector<>();

            for (HardConstraint hardConstraint : HardConstraints)
            {
                if (hardConstraint.IsCourseRelevant(courseIndex))
                {
                    hard.add(hardConstraint);
                }
            }
            courseIndexToHardConstraints.put(courseIndex, hard);

            for (SoftConstraint softConstraint : SoftConstraints)
            {
                if (softConstraint.IsSoftConstraintRelevant(courseIndex))
                {
                    soft.add(softConstraint);
                }
            }
            courseIndexToSoftConstraints.put(courseIndex, soft);

            // add courseIndex to constraintNumber map
            if (!sortedCourseListings.containsKey(hard.size())) // no course has had that many constraints associated with it
            {
                Vector<Integer> courseIndexList = new Vector<Integer>();
                courseIndexList.add(courseIndex);
                sortedCourseListings.put(hard.size(), courseIndexList);
            }
            else // add course index to the list of courses that have that many hard constraints
            {
                Vector<Integer> courseIndexList = sortedCourseListings.get(hard.size());
                courseIndexList.add(courseIndex);
                sortedCourseListings.replace(hard.size(), courseIndexList);
            }
        }
        // Finalize hard constraints map
        CourseIndexToHardConstraintsMap = courseIndexToHardConstraints;
        CourseIndexToSoftConstraintsMap = courseIndexToSoftConstraints;

        // Create problem ordering based off the number of constraints associated with each course
        // From that ordering, create a translation between problemIndex and courseIndex
        HashMap<Integer,Integer> courseToProblemMap = new HashMap<Integer,Integer>();
        HashMap<Integer,Integer> problemToCourseMap = new HashMap<Integer,Integer>();
        int problemNumber = 0;

        // put part assigns into the problem ordering first
        for (int partAssignCourse : mPartAssignLookup.keySet())
        {
            courseToProblemMap.put(partAssignCourse, problemNumber);
            problemToCourseMap.put(problemNumber, partAssignCourse);
            ++problemNumber;
        }

        // iterate through each constraint size grouping in reverse order. ie start with the courses that have the
        // most constraints associated with them. Each course is added to the problem order and assigned a problem index
        // store the translation between problem index and course index
        for (int constraintSize : sortedCourseListings.descendingKeySet())
        {
            Vector<Integer> courseIndices = sortedCourseListings.get(constraintSize);
            for (int courseIndex : courseIndices)
            {
                // if the course was not already added in the initial part assign stage
                if (!mPartAssignLookup.containsKey(courseIndex))
                {
                    courseToProblemMap.put(courseIndex, problemNumber);
                    problemToCourseMap.put(problemNumber, courseIndex);
                    ++problemNumber;
                }
            }
        }
        mProhlemIndexToCourseIndexMap = problemToCourseMap;
        mCourseIndexToProblemIndexMap = courseToProblemMap;
    }

    //----------------------------------------------------------------------------------------------------//
    //----------------------------------------UTIL FUNCTIONS----------------------------------------------//
    //----------------------------------------------------------------------------------------------------//

    // Converts problemIndex to courseIndex, determines if course[courseIndex] is a lecture/lab and returns
    // lecture/lab slots accordingly
    public TimeSlot[] GetSlotsForProblemIndex(int problemIndex)
    {
        int courseIndex = GetCourseIndexAtProblemIndex(problemIndex);

        // If in the part assign section of the problem ordering
        // Return the time slot that the course must be put in
        if (problemIndex < mPartAssignLookup.size())
        {
            TimeSlot[] partAssign = new TimeSlot[1];
            int slotIndex = mPartAssignLookup.get(courseIndex);
            partAssign[0] = slotIndex < mLectureSlots.length ? mLectureSlots[slotIndex] : mLabSlots[slotIndex - mLectureSlots.length];
            return partAssign;
        }
        // Else return based on what type of course it is
        else if (courseIndex < mNumLectures)
        {
            return mCourses[courseIndex].IsEveningCourse ? mEveningLectureSlots : mLectureSlots;
        }
        else
        {
            return mCourses[courseIndex].IsEveningCourse ? mEveningLabSlots : mLabSlots;
        }
    }

    public Vector<HardConstraint> GetHardConstraintsForProblemIndex(int problemIndex)
    {
        int courseIndex = GetCourseIndexAtProblemIndex(problemIndex);
        return CourseIndexToHardConstraintsMap.get(courseIndex);
    }

    public Vector<SoftConstraint> GetSoftConstraintsForProblemIndex(int problemIndex)
    {
        int courseIndex = GetCourseIndexAtProblemIndex(problemIndex);
        return CourseIndexToSoftConstraintsMap.get(courseIndex);
    }

    // Expects TimeSlot.SlotIndex as param
    public TimeSlot GetSlotAt(int slotIdx)
    {
        if (slotIdx < mLectureSlots.length)
        {
            return mLectureSlots[slotIdx];
        }
        else
        {
            return mLabSlots[slotIdx - mLectureSlots.length];
        }
    }

    public String GetCourseStringFromProblemIndex(int probIndex)
    {
        return mCourses[mProhlemIndexToCourseIndexMap.get(probIndex)].OutputString;
    }

    public boolean IsProblemNumberALecture(int problemIndex)
    {
        return mProhlemIndexToCourseIndexMap.get(problemIndex) < mNumLectures;
    }

    // Translate ProblemIndex to CourseIndex
    public int GetCourseIndexAtProblemIndex(int probIndex)
    {
        return mProhlemIndexToCourseIndexMap.get(probIndex);
    }

    // Translate CourseIndex to Problem Index
    public int GetProblemIndexForCourseIndex(int courseIndex)
    {
        return mCourseIndexToProblemIndexMap.get(courseIndex);
    }
    
    
}
