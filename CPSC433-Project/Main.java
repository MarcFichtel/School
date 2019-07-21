import java.io.FileNotFoundException;
import java.util.Arrays;

//TODO: Everything XD
public class Main
{	
	//
    private static int[] penaltyArray ={1,1,1,1};
    private static float[] weightArray={1,1,1,1};

    /*
     * 1. 1 parameters: filename String,
     * 4. 9 parameters: 1. filename String,
     * 		2. pen_MinLecture, 3. pen_MinLab, 4. pen_NotPaired, 5. pen_SectionOvelap,
     * 		6. weight_Preference, 7. weight_MinFilled, 8. weight_Pair, 9. weight_Section
     */
	public static void main(String[] args) throws FileNotFoundException
    {
		String fileName = null;
		
    	if(args.length==1) {
			fileName = args[0];
		} else if (args.length == 9){
    		fileName = args[0];
			for(int i=1;i<5;i++){
				penaltyArray[i-1]=Integer.parseInt(args[i]);
			}
			for(int i=5;i<9;i++){
				weightArray[i-5]=Float.parseFloat(args[i]);
			}   		
    	}else {
    		System.out.println("Invalid argument format, the argument should like on of the following:\n"+
    							"1. file name\n"+
    							"parameters: 1. filename String," +
					" 2. pen_MinLecture, 3. pen_MinLab, 4. pen_NotPaired, 5. pen_SectionOvelap," +
					" 6. weight_Preference, 7. weight_MinFilled, 8. weight_Pair, 9. weight_Section");
    		System.out.println("System exit.");
    		return;
    	}
    	startSearch(fileName);
    }

    public static void startSearch(String fileName) throws FileNotFoundException{
    	
    	/**
    	 * weight and penalty may need to be set correctly
    	 * they are all default set to 1
    	 * weight value are stored in the weightArray
    	 * penalty value are stored in the penaltyArray
    	 */      	
    	for(int i=0;i<weightArray.length;i++){
    		SoftConstraint.ConstraintWeightings[i]=weightArray[i];
    	}

    	try
		{
			Environment environment = FileParser.ParseFile(fileName, penaltyArray);
			SearchManager searchManager = new SearchManager(environment);
			long millisInDay = (long)(8.64 * Math.pow(10, 7));
			Problem bestSolution = searchManager.Search(millisInDay);
			if (bestSolution != null)
			{
				String result = bestSolution.ToString(environment);
				String eval = Integer.toString(searchManager.mBestSolutionValue);
				ResultOutputer out = new ResultOutputer(result,eval);
				out.getFile(); // output a file
				System.out.println(out.getString()); // output a string and print it on the console
			}
			else
			{
				System.out.println("Assignment not solvable. Pretend to hang forever until user unplugs computer");
			}
		}
		catch (Exception e)
		{
			System.out.println("Error in creating search environment. Aborting search");
			System.out.println(e.getMessage());
		}
    }
}
