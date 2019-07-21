public class MinFilledSoftConstraint extends SoftConstraint
{
    private final float mLecWeightedPenalty;
    private final float mLabWeightedPenalty;

    public MinFilledSoftConstraint(int lecturePenalty, int labPenalty)
    {
        super(SoftConstraint.MinFilledType);
        mLecWeightedPenalty = SoftConstraint.ConstraintWeightings[SoftConstraint.MinFilledType] * lecturePenalty;
        mLabWeightedPenalty = SoftConstraint.ConstraintWeightings[SoftConstraint.MinFilledType] * lecturePenalty;
    }

    // All courses are relevant
    @Override
    public boolean IsSoftConstraintRelevant(int courseIndex) {
        return true;
    }

    @Override
    public float GetInitialPenalty(Environment environment, Problem problem ) {
        int penalty = 0;
        for (int i = 0; i < environment.NumSlots; ++i)
        {
            TimeSlot slot = environment.GetSlotAt(i);
            penalty += slot.MinAssignment * (slot.IsLectureSlot ? mLecWeightedPenalty : mLabWeightedPenalty);
        }
        return penalty * ConstraintWeightings[ConstraintType];
    }

    @Override
    public float GetDeltaPenalty(Environment environment, Problem problem, TimeSlot slot) {

        int numAssignmentsToSlot = problem.CoursesPerSlot.containsKey(slot.SlotIndex) ?
                problem.CoursesPerSlot.get(slot.SlotIndex) : 0;

        int deltaMin = slot.MinAssignment - numAssignmentsToSlot;

        // If deltaMin <= 0, we've already satisfied this soft constraint so delta will not change
        // else we will reduce the total penalty of minSlot for this slot by one course == mWeightedPenalty
        if (deltaMin > 0)
        {
            return -(slot.IsLectureSlot ? mLecWeightedPenalty : mLabWeightedPenalty);
        }

        return 0;
    }
}
