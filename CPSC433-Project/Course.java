
//TODO: Austin is there anything else here you want? Otherwise remove abstract and ship
public class Course
{
    private static int sCourseIndex;
    public final int CourseIndex;
    public final String OutputString;
    public final boolean IsEveningCourse;

    public Course(String outputString, boolean isEveningCourse)
    {
        OutputString = outputString;
        CourseIndex = sCourseIndex++;
        IsEveningCourse = isEveningCourse;
    }
}
