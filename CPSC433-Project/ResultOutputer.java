
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * This class sorted the result string, and can 
 * output a result file and reuslt string in pretty format.
 * @author mingkunhe
 *
 */
public class ResultOutputer {
	
	private String result;
	private String eval;
	
	/**
	 * 
	 * @param result
	 * @param eval 
	 */
	public ResultOutputer(String result,String eval){
		this.result = result;
		this.eval = eval;
	}
	
	/**
	 * output the result as a text file
	 * @param result
	 * @throws FileNotFoundException 
	 */
	public void outputFile(ArrayList<String> result) throws FileNotFoundException{
		PrintWriter out = new PrintWriter("filename.txt");
		for(String s:result){
			out.println(s);
		}
		out.close();
	}
	
	/**
	 * output the result as a string
	 * @param result
	 * @return the result as a string
	 */
	public String outputString(ArrayList<String> result){
		String resultString = "";
		for(String s:result){
			resultString += s+"\n";
		}
		return resultString;
	}
	
	/**
	 * sort the array list into pretty format
	 * @param sArray the array list to be sorted
	 * @return sorted array list
	 */
	public ArrayList<String> prettyStringArray(ArrayList<String> sArray){
		ArrayList<String> resultList = new ArrayList<String>();	
		sArray.sort(String::compareToIgnoreCase);
		resultList=(ArrayList<String>) sArray.clone();
		
		for(int i=0;i<resultList.size();i++){
			String s = resultList.get(i);
			String[] parts = s.split(",");
			if(parts[1].length()==5){
				parts[1]=" "+parts[1];
			}
			resultList.set(i, parts[0]+parts[1]);
		}
		resultList.add(0,"Eval-value: "+this.eval);
		
		return resultList;
		
	}
	
	/**
	 * convert the a result string to a ArrayList.
	 * @param s :the string result
	 * @return the corresponding ArrayList
	 */
	public ArrayList<String> stringToArray(String s){
		ArrayList<String> stringList = new ArrayList<String>();
		String[] array = s.split("\n");
		for(String content: array){
			stringList.add(content);
		}
		return stringList;
	}
	
	/**
	 * Output the result File
	 * @throws FileNotFoundException
	 */
	public void getFile() throws FileNotFoundException{
		ArrayList<String> resultList = stringToArray(this.result);
		resultList = prettyStringArray(resultList);
		this.outputFile(resultList);
	}
	
	/**
	 * Output the result string
	 */
	public String getString(){
		ArrayList<String> resultList = stringToArray(this.result);
		resultList = prettyStringArray(resultList);
		return this.outputString(resultList);
	}
	


}
