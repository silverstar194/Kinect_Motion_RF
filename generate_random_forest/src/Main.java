/**
 * @author Admin
 *
 */
public class Main {
	
	public static void main(String[] args){
		
		DataHandler getImages = new DataHandler();
		
		getImages.listFilesAndFilesSubDirectories(MasterConstants.imageDirectory, MasterConstants.FILETYPE);
		for(String e : getImages.imageFiles){
			System.out.println(e);
			Image image = new Image(e);
			MasterConstants.images.put(e, image);
		}
	}

}
