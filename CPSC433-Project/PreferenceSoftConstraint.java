import java.util.HashMap;
import java.util.HashSet;

public class PreferenceSoftConstraint  extends SoftConstraint {

    private static HashMap<Integer, HashSet<Integer>> sCacheMap; // key = courseA, list<int>> slot preferences>

    static {
      sCacheMap = new HashMap<>();
    }

    private int mCourseIndex;
    private int mPreredSlotIndex;
    private float mWeightedPenalty;

    public PreferenceSoftConstraint(int courseIndex, int slotIndex, int penalty)
    {
        super(SoftConstraint.PreferenceType);
        mCourseIndex = courseIndex;
        mPreredSlotIndex = slotIndex;
        mWeightedPenalty = penalty * ConstraintWeightings[ConstraintType];
        Cache(courseIndex, slotIndex);
    }

    @Override
    public boolean IsSoftConstraintRelevant(int courseIndex) {
        return mCourseIndex == courseIndex;
    }

    @Override
    public float GetInitialPenalty(Environment environment, Problem problem)
    {
        // Should only be called on initial problem
        assert problem.CurrentProblemIndex == 0;
        return mWeightedPenalty;
    }

    @Override
    public float GetDeltaPenalty(Environment environment, Problem problem, TimeSlot slot) {

        // initial penalty sum assumes that course HASN"T been assigned to a slot
        // if the assignment of slot to course matches our soft constraint, remove penalty from sum,
        // ie return -penalty
        return (slot.SlotIndex == mPreredSlotIndex ?
                -1 : 0) * mWeightedPenalty;
    }

    public static boolean IsCached(int courseIndex, int slotIndex)
    {
        if (sCacheMap.containsKey(courseIndex))
        {
            if (sCacheMap.get(courseIndex).contains(slotIndex))
            {
                return true;
            }
        }
        return false;
    }

    private static void Cache(int courseIndex, int slotIndex)
    {
        if (!sCacheMap.containsKey(courseIndex))
        {
            HashSet<Integer> cache = new HashSet<>();
            cache.add(slotIndex);
            sCacheMap.put(courseIndex, cache);
        }
        else
        {
            sCacheMap.get(courseIndex).add(slotIndex);
        }
    }
}
