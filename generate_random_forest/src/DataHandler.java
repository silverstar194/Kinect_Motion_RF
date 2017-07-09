import java.io.File;
import java.util.ArrayList;
/**
 * @author Admin
 *
 */
public class DataHandler {

	/**
	 * Contains some methods to list files and folders from a directory
	 * https://dzone.com/articles/java-example-list-all-files
	 *
	 * @author Loiane Groner
	 * http://loiane.com (Portuguese)
	 * http://loianegroner.com (English)
	 */
	/**
	 * List all the files and folders from a directory
	 * @param directoryName to be listed
	 */
	public void listFilesAndFolders(String directoryName){
		File directory = new File(directoryName);
		//get all the files from a directory
		File[] fList = directory.listFiles();
		for (File file : fList){
			System.out.println(file.getName());
		}
	}

	/**
	 * List all the folder under a directory
	 * @param directoryName to be listed
	 */
	public void listFolders(String directoryName){
		File directory = new File(directoryName);
		//get all the files from a directory
		File[] fList = directory.listFiles();
		for (File file : fList){
			if (file.isDirectory()){
				System.out.println(file.getName());
			}
		}
	}
	/**
	 * List all files from a directory and its subdirectories
	 * @param directoryName to be listed
	 */
	ArrayList<String> imageFiles = new ArrayList<String>(MasterConstants.ESTIMATED_IMAGE_AMOUNT);
	public void listFilesAndFilesSubDirectories(String directoryName, String fileType, String filterBy){
		File directory = new File(directoryName);
	
		//get all the files from a directory
		File[] fList = directory.listFiles();
		for (File file : fList){
			if (file.isFile() && file.getName().contains(fileType) && file.getName().contains(filterBy)){
				imageFiles.add(file.getAbsolutePath());
			} else if (file.isDirectory()){
				listFilesAndFilesSubDirectories(file.getAbsolutePath(), fileType, filterBy);
			}
		}
	}
	
}

