public abstract class SoftConstraint
{


    // TODO: Design soft constraints
    /*
    1. Preference
    2. MinFIlled
    3. EvalPair
    4. Eval SectionDifference
     */
    public static final int PreferenceType = 0;
    public static final int MinFilledType = 1;
    public static final int PairType = 2;
    public static final int SectionType = 3;


    public final int ConstraintType;

    public static float[] ConstraintWeightings = {1f,1f,1f,1f};

    protected SoftConstraint(int type)
    {
        ConstraintType = type;
    }

    public abstract boolean IsSoftConstraintRelevant(int courseIndex);
    public abstract float GetInitialPenalty(Environment environment, Problem problem);
    public abstract  float GetDeltaPenalty(Environment environment, Problem problem, TimeSlot slot);
}
