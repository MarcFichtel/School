public class TimeSlot
{
    public static int EveningSlotStartTime = 1800;
    public static final byte MWF = 0;
    public static final byte MW = 1;
    public static final byte F = 2;
    public static final byte TTh = 3;

    public final byte Type;
    public final int StartTime; // 6:00 = 600, 18:30 = 1830
    public final int EndTime;
    public final int MaxAssignment;
    public final int MinAssignment;
    public final int SlotIndex;
    public final String OutputString;
    public final boolean IsLectureSlot;

    private static int sSlotIndex;

    public TimeSlot(byte type, int startTime, int maxAssignment, int minAssignment, String outputString, boolean isLecture)
    {
        Type = type;
        StartTime = startTime;
        MaxAssignment = maxAssignment;
        MinAssignment = minAssignment;
        OutputString = outputString;
        SlotIndex = sSlotIndex++;
        if(isLecture){
        	if(Type==TTh){
        		EndTime = StartTime + (StartTime % 30 == 0 ? 170 : 130);
        	}else{
        		EndTime = StartTime+100;
        	}
        }else{
        	if(Type==F){
        		EndTime = StartTime+200;
        	}else{
        		EndTime = StartTime+100;
        	}       	
        }
        IsLectureSlot = isLecture;
    }

    public boolean OverlapsWith(TimeSlot slot)
    {
        // TODO: Come up with a more restrictive definition of overlaps based off duration and lab/lecture start time differences
        // ie 9:00 TT Lab overlaps with 9:30 TT lecture
    	// lab in Friday have 2 hours long
        // Observe that current implimentation has 11:00 = 1100, 9:30 = 930.
    	
    	boolean typeOverlap = (Type == slot.Type // same slot
                || Type == MWF && slot.Type < TTh // mwf conflicting with mw or f
                || slot.Type == MWF && Type < TTh);
    	
    	boolean startTimeOverlap = (StartTime >= slot.StartTime && StartTime < slot.EndTime)||
    							   (slot.StartTime >= StartTime && slot.StartTime < EndTime);
    	   	
        return typeOverlap && startTimeOverlap;
    }
}
