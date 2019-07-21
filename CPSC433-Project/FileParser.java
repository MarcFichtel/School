import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class FileParser {
	// this IS NOT the final version, this file will need revisions/rewrites in
	// order to work for all inputs
	// for all of the inpuit files.
	// what if header but empty line?
	private static String fileName;

	public static String getFileName() {
		return fileName;
	}

	private static int[] MondayWensdayFridayLectureSlots = { 800, 900, 1000, 1100, 1200, 1300, 1400, 1500, 1600, 1700,
			1800, 1900, 2000 };
	private static int[] MondayWensdayLabSlots = { 800, 900, 1000, 1100, 1200, 1300, 1400, 1500, 1600, 1700, 1800, 1900,
			2000 };

	private static int[] TuesdayThursdayLectureSlots = { 800, 930, 1230, 1400, 1530, 1700, 1830 };

	private static int[] FridayTutorials = { 800, 1000, 1200, 1400, 1600, 1800 };
	private static String[] primaryHeaders = { "Name:", "Course slots:", "Lab slots:", "Courses:", "Labs:" };
	private static String[] secondaryHeaders = { "Not compatible:", "Pair:", "Unwanted:", "Partial assignments:",
			"Preferences:" };
	private static Scanner mscanner = null;

	private final static int fMinFilledLecIndex = 0;
	private final static int fMinFilledLabIndex = 1;
	private final static int fPairIndex = 2;
	private final static int fSectionIndex = 3;

	// Assumptions:
	// each header/identifier/thing has it's own line
	public static Environment ParseFile(String filePath, int[] penaltyArray) throws Exception {
		ArrayList<TimeSlot> LectureSlots = new ArrayList<TimeSlot>();
		ArrayList<TimeSlot> LabSlots = new ArrayList<TimeSlot>();
		ArrayList<Course> Lectures = new ArrayList<Course>();
		ArrayList<Course> Labs = new ArrayList<Course>();
		ArrayList<HardConstraint> hardConstraints = new ArrayList<HardConstraint>();
		ArrayList<SoftConstraint> softConstraints = new ArrayList<SoftConstraint>();
		HashMap<String, Integer> courseNumberLookup = new HashMap<>();
		HashMap<Integer, Integer> partAssignMap = new HashMap<>();

		ArrayList<Course> Level500Lectures = new ArrayList<Course>();

		ArrayList<Course> Level313Sections = new ArrayList<Course>();
		ArrayList<Course> Level413Sections = new ArrayList<Course>();
		ArrayList<Integer> Level313Incompatible = new ArrayList<Integer>();
		ArrayList<Integer> Level413Incompatible = new ArrayList<Integer>();

		boolean c813 = false;
		boolean c913 = false;
		boolean c313 = false;
		boolean c413 = false;
		boolean s1800 = false;
		int id813 = 0;
		int id913 = 0;
		int id1800 = 0;

		byte TTh = 3;

		try {
			mscanner = new Scanner(new File(filePath));
		} catch (FileNotFoundException e) {
			System.out.println("File not found, check file path.");
			System.exit(1);
		}
		String line = "";
		while (mscanner.hasNextLine()) {
			if (!lineContainsHeader(line, primaryHeaders)) {
				line = mscanner.nextLine();
			}
			if (line.startsWith(primaryHeaders[0])) {// File name
				line = getNextValidLine(line);
				fileName = line;
			} else if (line.startsWith(primaryHeaders[1])) {// Course slots
				// this parses the course slots field in the format of Day, Start time,
				// coursemax, coursemin
				// TODO: Verify that the time provided to us matches a valid start time as
				// listed in his description
				line = getNextValidLine(line);
				while (line != null && !lineContainsHeader(line, primaryHeaders) && !lineContainsHeader(line, secondaryHeaders)) {
					ArrayList<String> values = getStrings(line);
					if (values.size() == 4) {
						TimeSlot slot = generateNewTimeSlot(values, true);
						if (slot != null) {
							LectureSlots.add(slot);
						} else {
							System.out.println("Warning malformed line (invalid time slot): \"" + line + "\"");
						}
					} else {
						System.out.println("Warning malformed line: \"" + line + "\"");
					}
					line = getNextValidLine(line);
				}
			} else if (line.startsWith(primaryHeaders[2])) {// Lab slots
				line = getNextValidLine(line);
				while (line != null && !lineContainsHeader(line, primaryHeaders) && !lineContainsHeader(line, secondaryHeaders)) {
					ArrayList<String> values = getStrings(line);
					if (values.size() == 4) {
						TimeSlot slot = generateNewTimeSlot(values, false);
						if (slot != null) {
							LabSlots.add(slot);
							// If slot is 1800 start time on TU or TH flip boolean for 813/913 constraint
							if ((slot.StartTime == 1800) && (slot.Type == TTh)) {
								s1800 = true;
								id1800 = slot.SlotIndex;
							}
						} else {
							System.out.println("Warning malformed line (invalid time slot): \"" + line + "\"");
						}
					} else {
						System.out.println("Warning malformed line: \"" + line + "\"");
					}
					line = getNextValidLine(line);
				}
			} else if (line.startsWith(primaryHeaders[3])) { // Courses
				line = getNextValidLine(line);
				while (line != null && !lineContainsHeader(line, primaryHeaders) && !lineContainsHeader(line, secondaryHeaders)) {
					Course course = generateNewCourse(line);
					if (!courseNumberLookup.containsKey(course.OutputString)) {
						courseNumberLookup.put(course.OutputString, course.CourseIndex);
						Lectures.add(course);
						int courseNumber = Integer.parseInt(course.OutputString.split(" ")[1]);
						// If lecture is a 500-level lecture, add it to Level500Lectures
						if (courseNumber >= 500 && courseNumber < 600) {
							Level500Lectures.add(course);
						}
						// If a course is 813 flip boolean
						else if (courseNumber == 813) {
							c813 = true;
							id813 = course.CourseIndex;
						}
						// If a course is 813 flip boolean
						else if (courseNumber == 913) {
							c913 = true;
							id913 = course.CourseIndex;
						} else if (courseNumber == 313) {
							c313 = true;
							Level313Sections.add(course);
						} else if (courseNumber == 413) {
							c413 = true;
							Level413Sections.add(course);
						}
					} else {
						System.out.println("FileParser: Found Duplicate Lecture Entry: " + course.OutputString);
					}
					line = getNextValidLine(line);
				}
				// after parsing the courses, check if we need to add 813/913
				// Check for and add 813 if applicable
				if (c313 && !c813) {
					Course course = generateNewCourse("CPSC 813 LEC 01");
					id813 = course.CourseIndex;
					courseNumberLookup.put(course.OutputString, course.CourseIndex);
					Lectures.add(course);
					c813 = true;
				}

				// Check for and add 913 if applicable
				if (c413 && !c913) {
					Course course = generateNewCourse("CPSC 913 LEC 01");
					id913 = course.CourseIndex;
					courseNumberLookup.put(course.OutputString, course.CourseIndex);
					Lectures.add(course);
					c913 = true;
				}
			} else if (line.startsWith("Labs:")) {
				line = getNextValidLine(line);
				// TODO: Only add if the lecture exists already or add a place to remove labs
				// without a lecture later
				while (line != null && !lineContainsHeader(line, primaryHeaders) && !lineContainsHeader(line, secondaryHeaders)) {
					Course course = generateNewCourse(line);
					if (!courseNumberLookup.containsKey(course.OutputString)) {
						courseNumberLookup.put(course.OutputString, course.CourseIndex);
						Labs.add(course);
						int courseNumber = Integer.parseInt(course.OutputString.split(" ")[1]);
						if (courseNumber == 313) {
							c313 = true;
							Level313Sections.add(course);
						} else if (courseNumber == 413) {
							c413 = true;
							Level413Sections.add(course);
						}
					} else {
						System.out.println("FileParser: Found Duplicate Lab Entry: " + course.OutputString);
					}
					line = getNextValidLine(line);
				}
			}
		} // end file parse
		mscanner.close();
		try {
			mscanner = new Scanner(new File(filePath));
		} catch (FileNotFoundException e) {
			System.out.println("File not found, check file path.");
			System.exit(1);
		}
		while (mscanner.hasNextLine()) {
			if (!lineContainsHeader(line, secondaryHeaders)) {
				line = mscanner.nextLine();
			}
			if (line.startsWith("Not compatible:")) {
				line = getNextValidLine(line);
				while (line != null && !lineContainsHeader(line, secondaryHeaders) && !lineContainsHeader(line, primaryHeaders)) {
					String[] IDs = line.split(",");
					try {
						int ID0 = courseNumberLookup.get(normalizeID(IDs[0]));
						int ID1 = courseNumberLookup.get(normalizeID(IDs[1]));
						hardConstraints.add(new CourseToCourseHardConstraint(ID0, ID1, false));

						// TODO: Does this work?
						if (normalizeID(IDs[0]).contains("CPSC 313")) {
							Level313Incompatible.add(ID1);
						} else if (normalizeID(IDs[1]).contains("CPSC 313")) {
							Level313Incompatible.add(ID0);
						} else if (normalizeID(IDs[0]).contains("CPSC 413")) {
							Level413Incompatible.add(ID1);
						} else if (normalizeID(IDs[1]).contains("CPSC 413")) {
							Level413Incompatible.add(ID0);
						}
					} catch (Exception e) {
						System.out.println("Unknown Course found in Not Compatible: " + line);
					}
					line = getNextValidLine(line);
				}
			} else if (line.startsWith("Pair:")) {
				line = getNextValidLine(line);
				while (line != null && !lineContainsHeader(line, secondaryHeaders) && !lineContainsHeader(line, primaryHeaders)) {
					String[] IDs = line.split(",");
					try {
						int ID0 = courseNumberLookup.get(normalizeID(IDs[0]));
						int ID1 = courseNumberLookup.get(normalizeID(IDs[1]));
						softConstraints.add(new CourseToCourseSoftConstraint(ID0, ID1, penaltyArray[fPairIndex], true));
					} catch (Exception e) {
						System.out.println("Unknown Course found in Pair: " + line);
					}
					line = getNextValidLine(line);
				}
			} else if (line.startsWith("Unwanted:")) {
				line = getNextValidLine(line);
				while (line != null && !lineContainsHeader(line, secondaryHeaders) && !lineContainsHeader(line, primaryHeaders)) {
					String[] IDs = line.split(",");
					try {
						int ID0 = courseNumberLookup.get(normalizeID(IDs[0]));
						int ID1 = lookUpSlotID(IDs[1], IDs[2], LectureSlots, LabSlots, ID0 < Lectures.size());
						hardConstraints.add(new CourseToSlotHardConstraint(ID0, ID1, false));
					} catch (Exception e) {
						System.out.println("Unknown Course/Slot found in Unwanted: " + line);
					}
					line = getNextValidLine(line);
				}
			} else if (line.startsWith("Partial assignments:")) {
				line = getNextValidLine(line);
				while (line != null && !lineContainsHeader(line, secondaryHeaders) && !lineContainsHeader(line, primaryHeaders)) {
					String[] IDs = line.split(",");
					try {
						int ID0 = courseNumberLookup.get(normalizeID(IDs[0]));
						int ID1 = lookUpSlotID(IDs[1], IDs[2], LectureSlots, LabSlots, ID0 < Lectures.size());
						hardConstraints.add(new CourseToSlotHardConstraint(ID0, ID1, true));
						partAssignMap.put(ID0, ID1);
					} catch (Exception e) {
						System.out.println("Unknown Course/Slot found in Partial Assign: " + line);
					}

					line = getNextValidLine(line);
				}
			} else if (line.startsWith("Preferences:")) {
				line = getNextValidLine(line);
				while (line != null && !lineContainsHeader(line, secondaryHeaders) && !lineContainsHeader(line, primaryHeaders)) {
					String[] IDs = line.split(",");
					try {
						int ID0 = courseNumberLookup.get(normalizeID(IDs[2]));
						int ID1 = lookUpSlotID(IDs[0], IDs[1], LectureSlots, LabSlots, ID0 < Lectures.size());
						int penalty = Integer.parseInt(IDs[3].replace(" ", ""));
						softConstraints.add(new PreferenceSoftConstraint(ID0, ID1, penalty));
					} catch (Exception e) {
						System.out.println("Unknown Course found in Prefernces: " + line);
					}
					line = getNextValidLine(line);
				}
			}
		}

		/*
		 * Map structure mapping course names (i.e. CPSC) to Course numbers (i.e. 433)
		 * to Section numbers (i.e. LEC 1) to lists of Lab numbers (i.e. LAB 1). The
		 * number -1 was chosen as key for lists of labs that have no section. Since
		 * lectures are parsed before labs, all labs should belong to some lecture.
		 *
		 * Usage: To access all lectures of a course, isolate the course's name and
		 * number from the Output String, then iterate over Key-Value_Pairs, for example
		 * like so: ArrayList<Course> myCourses = new ArrayList<Course>(); String name =
		 * course.OutputString.substring(0, 4); int number =
		 * Integer.parseInt(course.OutputString.substring(5, 8)); for
		 * (Map.Entry<Integer, ArrayList<Integer>> entry :
		 * CourseMapping.get(name).get(number).entrySet()) { String id = name + " " +
		 * number + " LEC " + entry.getKey(); myCourses.add(Courses[lookUpCourseID(id,
		 * Courses, Labs))]; }
		 */
		HashMap<String, HashMap<Integer, HashMap<Integer, ArrayList<Integer>>>> CourseMapping = new HashMap<String, HashMap<Integer, HashMap<Integer, ArrayList<Integer>>>>();

		final int departmentBegin = 0, departmentEnd = 4, courseNumberBegin = 5, courseNumberEnd = 8,
				sectionNumberBegin = 13, sectionNumberEnd = 15, tutorialNumberWithoutSectionBegin = 13,
				tutorialNumberWithoutSectionEnd = 15, tutorialNumberWithSectionBegin = 20,
				tutorialNumberWithSectionEnd = 22;

		// First, iterate over all courses
		for (Course lecture : Lectures) {
			// Every course has a 4-letter name
			String department = lecture.OutputString.substring(departmentBegin, departmentEnd);
			HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> courseMap = CourseMapping.containsKey(department)
					? CourseMapping.get(department)
					: new HashMap<>();

			int courseNumber = Integer.parseInt(lecture.OutputString.substring(courseNumberBegin, courseNumberEnd));
			HashMap<Integer, ArrayList<Integer>> sectionMapping = courseMap.containsKey(courseNumber)
					? courseMap.get(courseNumber)
					: new HashMap<>();

			int sectionNumber = Integer.parseInt(lecture.OutputString.substring(sectionNumberBegin, sectionNumberEnd));

			// No duplicate section numbers to a course
			assert (sectionMapping.containsKey(sectionNumber));
			sectionMapping.put(sectionNumber, new ArrayList<>());
			courseMap.put(courseNumber, sectionMapping);
			CourseMapping.put(department, courseMap);
		}

		// Then, iterate over all labs
		for (Course lab : Labs) {

			/*
			 * It is assumed that all labs belong to some course (such as CPSC 433), so
			 * there is no need to check if the mapping contains a lab's course name or
			 * number since that would have been added previously.
			 */
			// Get map keys and lab number
			String departmentName = lab.OutputString.substring(departmentBegin, departmentEnd);
			int courseNumber = Integer.parseInt(lab.OutputString.substring(courseNumberBegin, courseNumberEnd));
			int sectionNumber, labNumber;
			// If lab has a section number, read it
			if (lab.OutputString.contains("LEC")) {
				sectionNumber = Integer.parseInt(lab.OutputString.substring(sectionNumberBegin, sectionNumberEnd));
				// Else lab is section-free
			} else {
				sectionNumber = -1; // Special value for section-free labs
			}
			// Add lab to map if necessary
			try {
				if (sectionNumber != -1) // only assign to it's associated lecture
				{
					CourseMapping.get(departmentName).get(courseNumber).get(sectionNumber).add(lab.CourseIndex);
				} else // assign to all section numbers
				{
					HashMap<Integer, ArrayList<Integer>> sectionMap = CourseMapping.get(departmentName)
							.get(courseNumber);
					for (ArrayList<Integer> tutorialsForSection : sectionMap.values()) {
						tutorialsForSection.add(lab.CourseIndex);
					}
				}
			} catch (Exception e) {
				System.out.println("Error finding Associated Lecture for Lab " + lab.OutputString);
			}
		}

		/*
		 * Parse the map and for each Course 1. Create a soft constraint between each
		 * section of the course being in the same slot 2. Create a hard constraint
		 * between each lab and it's associated section for the course
		 */

		for (String departmentName : CourseMapping.keySet()) {
			HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> courseMapping = CourseMapping.get(departmentName);
			for (Integer courseNumber : courseMapping.keySet()) {
				HashMap<Integer, ArrayList<Integer>> sectionMapping = courseMapping.get(courseNumber);
				// key = section number, Value = labs for that section. We only care about the
				// keys for this section
				Integer[] sectionNumbers = new Integer[sectionMapping.size()];
				sectionMapping.keySet().toArray(sectionNumbers);
				// cache coursenumbers that are searched for to prevent looking up a course
				// number n^2 times
				int[] courseNumberCache = new int[sectionNumbers.length];
				Arrays.fill(courseNumberCache, -1); // initialize to -1 to inidicate if a cache value is valid yet

				for (int i = 0; i < sectionNumbers.length; i++) {
					// Get course index for main course
					int mainCourseIndex = -1;
					if (courseNumberCache[i] != -1) {
						mainCourseIndex = courseNumberCache[i];
					} else {
						int sectionNumber = sectionNumbers[i];
						String sectionString = (sectionNumber < 10 ? "0" : "") + Integer.toString(sectionNumber); // pad
						// with
						// a
						// zero
						String courseLookupString = departmentName + " " + courseNumber + " LEC " + sectionString;
						mainCourseIndex = courseNumberLookup.get(courseLookupString);
						courseNumberCache[i] = mainCourseIndex;
					}

					// create a soft constraint between the main course and all other courses ahead
					// of it in the list.
					// List is parsed after current entry to prevent duplicates from being computed
					for (int j = i + 1; j < sectionNumbers.length; ++j) {
						int secondaryCourseIndex = -1;
						if (courseNumberCache[j] != -1) {
							secondaryCourseIndex = courseNumberCache[j];
						} else {
							int sectionNumber = sectionNumbers[j];
							String sectionString = (sectionNumber < 10 ? "0" : "") + Integer.toString(sectionNumber); // pad
							// with
							// a
							// zero
							String courseLookupString = departmentName + " " + courseNumber + " LEC " + sectionString;
							secondaryCourseIndex = courseNumberLookup.get(courseLookupString);
							courseNumberCache[j] = secondaryCourseIndex;
						}
						// Add Soft contraint
						if (!CourseToCourseSoftConstraint.IsCached(mainCourseIndex, secondaryCourseIndex, false)) {
							if (!CourseToCourseSoftConstraint.IsCached(mainCourseIndex, secondaryCourseIndex, true)) {
								softConstraints.add(new CourseToCourseSoftConstraint(mainCourseIndex, secondaryCourseIndex, penaltyArray[fSectionIndex], false));

							} else {
								System.out.println("Conflict between " + Lectures.get(mainCourseIndex).OutputString
										+ " and " + Lectures.get(secondaryCourseIndex).OutputString);
							}
						} else {
							System.out.println("Error in parsing logic for section number softconstraints");
						}
					}

					// Parse all labs/tutorials for this section and create a hard constraint
					// between them
					ArrayList<Integer> tutorialIndices = sectionMapping.get(sectionNumbers[i]);
					for (int tutorialIndice : tutorialIndices) {
						// tutorialIndice = Lab.CourseIndex
						if (!CourseToCourseHardConstraint.IsCached(mainCourseIndex, tutorialIndice, false)) {
							if (!CourseToCourseHardConstraint.IsCached(mainCourseIndex, tutorialIndice, true)) {
								hardConstraints
										.add(new CourseToCourseHardConstraint(mainCourseIndex, tutorialIndice, false));
							} else {
								System.out.println("Conflict between " + Lectures.get(mainCourseIndex).OutputString
										+ " and " + Labs.get(tutorialIndice).OutputString);
							}
						}
					}
				}
			}
		}

		// 3. Create a hard constraint between all 500 level courses in the same slot
		for (int i = 0; i < Level500Lectures.size() - 1; ++i) {
			for (int j = i + 1; j < Level500Lectures.size(); ++j) {
				hardConstraints.add(new CourseToCourseHardConstraint(Level500Lectures.get(i).CourseIndex,
						Level500Lectures.get(j).CourseIndex, false));
			}

		}



		if (c813 || c913) {
			if (s1800) {
				if (c813)
				{
					if (!CourseToSlotHardConstraint.IsCached(id813, id1800, true))
					{
						hardConstraints.add(new CourseToSlotHardConstraint(id813, id1800, true));
					}
					if (!partAssignMap.containsKey(id813))
					{
						partAssignMap.put(id813, id1800);
					}
					else if (partAssignMap.get(id813) != id1800)
					{
						throw new Exception("Conflict of partAssign for CPSC 813");
					}
				}
				if (c913)
				{
					if (!CourseToSlotHardConstraint.IsCached(id913, id1800, true))
					{
						hardConstraints.add(new CourseToSlotHardConstraint(id913, id1800, true));
					}
					if (!partAssignMap.containsKey(id913))
					{
						partAssignMap.put(id913, id1800);
					}
					else if (partAssignMap.get(id913) != id1800)
					{
						throw new Exception("Conflict of partAssign for CPSC 913");
					}
				}
			} else {
				throw new Exception("TU 18:00 slot doesn't exist for 313/413. No Solution Possible");
			}
		}

		if (c313) {
			// foreach constrant against 313 put against 813
			// Create hard constraint between all lec/lab sections of 313 with 813
			for (int j = 0; j < Level313Sections.size(); j++) {
				if (!CourseToCourseHardConstraint.IsCached(id813, Level313Sections.get(j).CourseIndex, false)) {
					hardConstraints.add(new CourseToCourseHardConstraint(id813, Level313Sections.get(j).CourseIndex, false));
				}
			}

			// Create hard constraint between all lec/labs not compatible with 313 with 813
			for(int j = 0; j < Level313Incompatible.size(); j++)
			{
				if (!CourseToCourseHardConstraint.IsCached(id813, Level313Incompatible.get(j), false)) {
					hardConstraints.add(new CourseToCourseHardConstraint(id813, Level313Incompatible.get(j), false));
				}
			}
		}
		if (c413)
		{
			// foreach constrant against 413 put against 913
			// Create hard constraint between all lec/lab sections of 413 with 913
			for (int j = 0; j < Level413Sections.size(); ++j)
			{
				if (!CourseToCourseHardConstraint.IsCached(id913, Level413Sections.get(j).CourseIndex, false)) {
					hardConstraints.add(new CourseToCourseHardConstraint(id913, Level413Sections.get(j).CourseIndex, false));
				}
			}

			// Create hard constraint between all lec/labs not compatible with 313 with 813
			for(int j = 0; j < Level413Incompatible.size(); j++) {
				if (!CourseToCourseHardConstraint.IsCached(id913,  Level413Incompatible.get(j), false)) {
					hardConstraints.add(new CourseToCourseHardConstraint(id913, Level413Incompatible.get(j), false));
				}
			}
		}

		// max slot
		hardConstraints.add(new SlotMaxHardConstraint());
		// min slot
		softConstraints.add(new MinFilledSoftConstraint(penaltyArray[fMinFilledLecIndex], penaltyArray[fMinFilledLabIndex]));

		TimeSlot courseSlotArray[] = new TimeSlot[LectureSlots.size()];
		TimeSlot LabSlotArray[] = new TimeSlot[LabSlots.size()];
		Course coursesArray[] = new Course[Lectures.size()];
		Course labsArray[] = new Course[Labs.size()];
		HardConstraint hardConstraintsArray[] = new HardConstraint[hardConstraints.size()];
		SoftConstraint softConstraintsArray[] = new SoftConstraint[softConstraints.size()];
		LectureSlots.toArray(courseSlotArray);
		LabSlots.toArray(LabSlotArray);
		Lectures.toArray(coursesArray);
		Labs.toArray(labsArray);
		hardConstraints.toArray(hardConstraintsArray);
		softConstraints.toArray(softConstraintsArray);
		mscanner.close();
		return new Environment(courseSlotArray, LabSlotArray, hardConstraintsArray, softConstraintsArray, coursesArray,
				labsArray, partAssignMap);
	}

	// will return null in the event that their is no next line
	private static String getNextValidLine(String line) {
		if (mscanner.hasNextLine()) {
			line = mscanner.nextLine();
			while (getNextValidPosition(line, 0) == -1) {
				if (mscanner.hasNextLine()) {
					line = mscanner.nextLine();
				} else {
					return null;
				}
			}
			return line;
		}

		return null;
	}

	private static TimeSlot generateNewTimeSlot(ArrayList<String> values, boolean isCourse) {
		String Day = values.get(0);
		byte timeSlotDay = getTimeSlotDay(Day, isCourse);
		String timeOriginal = values.get(1);
		String startTime = timeOriginal.replace(":", "");
		int startTimeInt = Integer.parseInt(startTime);
		boolean isValid = false;
		if (isCourse) {
			if (timeSlotDay == TimeSlot.MWF || timeSlotDay == TimeSlot.F) {
				isValid = isValidTimeSlot(startTimeInt, MondayWensdayFridayLectureSlots);
			} else {
				isValid = isValidTimeSlot(startTimeInt, TuesdayThursdayLectureSlots);
			}
		} else {
			if (timeSlotDay == TimeSlot.MW || timeSlotDay == TimeSlot.TTh) {
				isValid = isValidTimeSlot(startTimeInt, MondayWensdayLabSlots);
			} else {
				isValid = isValidTimeSlot(startTimeInt, FridayTutorials);
			}
		}
		if (!isValid) {
			return null;
		}
		int courseMaxInt = Integer.parseInt(values.get(2));
		int courseMinInt = Integer.parseInt(values.get(3));
		return new TimeSlot(timeSlotDay, startTimeInt, courseMaxInt, courseMinInt,
				Day.replace(",", "") + ", " + timeOriginal, isCourse);
	}

	private static boolean isValidTimeSlot(int startTimeInt, int[] validValues) {
		for (int time : validValues) {
			if (time == startTimeInt) {
				return true;
			}
		}
		return false;
	}

	private static Course generateNewCourse(String fileLine) {
		String normalizedId = normalizeID(fileLine);
		boolean isEveningCourse = normalizedId.charAt(normalizedId.length() - 2) == '9';

		return new Course(normalizedId, isEveningCourse);
	}

	private static ArrayList<String> getStrings(String line) {
		ArrayList<String> StringsToReturn = new ArrayList<String>();
		int pos = 0;
		while (pos < line.length()) {
			pos = getNextValidPosition(line, pos);
			if (pos == -1) {
				break;
			}
			String string = "";
			while (pos < line.length() && (line.charAt(pos) == ':' || Character.isAlphabetic(line.charAt(pos))
					|| Character.isDigit(line.charAt(pos)))) {
				string += line.charAt(pos);
				pos++;
			}
			StringsToReturn.add(string);
		}

		return StringsToReturn;
	}

	// String day should represent the day the slot is, for example: "MO" or "FR"
	// String timeString should reflect the time during that day that the slot
	// exists in, for example: "8:00" or "10:00"
	// ArrayList<TimeSlot> courseSlots is an array list with all of the current
	// Course Slots (lectures) in it
	// ArrayList<TimeSlot> labSlots is an array list with all of the lab slots in
	// it.
	// boolean isCourse is used to determine weither to look at the courseSlots or
	// the labSlots array
	private static int lookUpSlotID(String day, String timeString, ArrayList<TimeSlot> courseSlots,
			ArrayList<TimeSlot> labSlots, boolean isCourse) throws Exception {
		byte type = getTimeSlotDay(getStrings(day).get(0), isCourse);
		int time = Integer.parseInt(getStrings(timeString).get(0).replace(":", ""));
		if (isCourse) {
			for (TimeSlot ts : courseSlots) {
				if (ts.Type == type && time == ts.StartTime) {
					return ts.SlotIndex;
				}
			}
		} else {
			for (TimeSlot ts : labSlots) {
				if (ts.Type == type && time == ts.StartTime) {
					return ts.SlotIndex;
				}
			}
		}
		throw new Exception("LookupSlotId(): Slot " + day + " " + timeString + " in " +
				(isCourse ? " Lecture Slots " : " Lab Slots ") + " doesn't exist ");
	}

	private static void Update313_413(Course addedCourse)
	{

	}

	private static String normalizeID(String line) {
		ArrayList<String> strings = getStrings(line);
		String normalizedID = "";
		for (String string : strings) {
			normalizedID += string + " ";
		}
		return normalizedID.substring(0, normalizedID.length() - 1);
	}

	private static boolean lineContainsHeader(String line, String[] headers2) {
		for (String string : headers2) {
			if (line.contains(string)) {
				return true;
			}
		}
		return false;
	}

	private static byte getTimeSlotDay(String day, boolean isCourse) {
		if (day.toLowerCase().startsWith("m")) {
			if (isCourse) {
				return TimeSlot.MWF;
			} else {
				return TimeSlot.MW;
			}
		}
		if (day.toLowerCase().startsWith("t")) {
			return TimeSlot.TTh;
		}
		if (day.toLowerCase().startsWith("f")) {
			return TimeSlot.F;
		}
		return TimeSlot.TTh;
	}

	// usage:
	// line a string of characters in which to find the next number or character.
	// currPosition is a non character/number char that we are currently on
	// for example getNextValidPosition("aa, c", 2); will return 5
	public static int getNextValidPosition(String line, int currPosition) {
		while (currPosition != line.length()
				&& !(Character.isAlphabetic(line.charAt(currPosition)) || Character.isDigit(line.charAt(currPosition))))
			currPosition++;
		if (currPosition == line.length()) {
			return -1;// returns -1 if the next non white space char is not in line
		}
		return currPosition;
	}

	public static Environment TestEnvironment() {
//		TimeSlot[] lectureSlots = new TimeSlot[3];
//		lectureSlots[0] = new TimeSlot(TimeSlot.MWF, 800, 3, 2, "MO 8:00");
//		lectureSlots[1] = new TimeSlot(TimeSlot.MWF, 900, 3, 2, "MO 9:00");
//		lectureSlots[2] = new TimeSlot(TimeSlot.TTh, 930, 2, 1, "TU 9:30");
//
//		TimeSlot[] labSlots = new TimeSlot[3];
//		labSlots[0] = new TimeSlot(TimeSlot.MW, 800, 4, 2, "MO 8:00");
//		labSlots[1] = new TimeSlot(TimeSlot.TTh, 1000, 2, 1, "TU 10:00");
//		labSlots[2] = new TimeSlot(TimeSlot.F, 1000, 2, 1, "FR 10:00");
//
//		Course[] courses = new Course[4];
//		courses[0] = new Course("CPSC 433 LEC 01", false); // 0
//		courses[1] = new Course("CPSC 433 LEC 02",false); // 1
//		courses[2] = new Course("SENG 311 LEC 01",false); // 2
//		courses[3] = new Course("CPSC 567 LEC 01",false); // 3
//
//		Course[] labs = new Course[4];
//		labs[0] = new Course("CPSC 433 LEC 01 TUT 01",false); // 4
//		labs[1] = new Course("CPSC 433 LEC 02 LAB 02",false); // 5
//		labs[2] = new Course("SENG 311 LEC 01 TUT 01",false); // 6
//		labs[3] = new Course("CPSC 567 TUT 01",false); // 7
//
//		ArrayList<HardConstraint> hardConstraintList = new ArrayList<HardConstraint>();
//		ArrayList<SoftConstraint> softConstraintList = new ArrayList<SoftConstraint>();
//
//		// max slot
//		hardConstraintList.add(new SlotMaxHardConstraint());
//		// min slot
//		softConstraintList.add(new MinFilledSoftConstraint(1));
//
//		// not compatible
//		hardConstraintList.add(new CourseToCourseHardConstraint(4, 5, false));
//		hardConstraintList.add(new CourseToCourseHardConstraint(3, 0, false));
//		hardConstraintList.add(new CourseToCourseHardConstraint(3, 1, false));
//		hardConstraintList.add(new CourseToCourseHardConstraint(7, 1, false));
//		hardConstraintList.add(new CourseToCourseHardConstraint(0, 7, false));
//
//		// unwanted
//		hardConstraintList.add(new CourseToSlotHardConstraint(0, 0, false));
//
//		// preference
//		softConstraintList.add(new PreferenceSoftConstraint(4, 3, 3));
//		softConstraintList.add(new PreferenceSoftConstraint(4, 4, 5));
//		softConstraintList.add(new PreferenceSoftConstraint(5, 3, 1));
//
//		// pair
//		softConstraintList.add(new CourseToCourseSoftConstraint(2, 3, 1, true));
//
//		// part assign
//		hardConstraintList.add(new CourseToSlotHardConstraint(2, 0, true));
//		hardConstraintList.add(new CourseToSlotHardConstraint(6, 5, true));
//
//		// same slot as course
//		hardConstraintList.add(new CourseToCourseHardConstraint(0, 4, false));
//		hardConstraintList.add(new CourseToCourseHardConstraint(1, 5, false));
//		hardConstraintList.add(new CourseToCourseHardConstraint(2, 6, false));
//		hardConstraintList.add(new CourseToCourseHardConstraint(3, 7, false));
//
//		// section difference
//		softConstraintList.add(new CourseToCourseSoftConstraint(1, 2, 1, false));
//
//		HardConstraint[] hardConstraints = new HardConstraint[hardConstraintList.size()];
//		SoftConstraint[] softConstraints = new SoftConstraint[softConstraintList.size()];
//		hardConstraintList.toArray(hardConstraints);
//		softConstraintList.toArray(softConstraints);
//
//		return new Environment(lectureSlots, labSlots, hardConstraints, softConstraints, courses, labs);
		return null;
	}
}
