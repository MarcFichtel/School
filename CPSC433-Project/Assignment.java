public class Assignment
{
    public final int CourseIdx;
    public final int SlotIdx;
    public final boolean IsCourse;


    // Note: redundant, can be replaced by two parameters in function calls
    // Kept for now for readability of code
    // courseIdx is depth of search
    // isCourse may or may not be used
    // slotIdx = TimeSlot.SlotIdx
    public Assignment(int courseIdx, boolean isCourse, int slotIdx)
    {
        CourseIdx = courseIdx;
        IsCourse = isCourse;
        SlotIdx = slotIdx;
    }
}
