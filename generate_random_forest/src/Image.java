import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @author Admin
 *
 */
public class Image {
	
	private String absolutePath;
	private byte[] imagePixalArray;

	
	public Image(String absolutePath){
		
		if(!absolutePath.contains(MasterConstants.FILETYPE)){
			System.out.println("Fle type violated "+absolutePath);
			System.exit(1);
		}
		this.absolutePath = absolutePath;
	}
	
	
	public void loadImage(){
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File(this.absolutePath));
		    this.imagePixalArray = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
		} catch (IOException e) {
			System.out.println("Failed to load image "+this.absolutePath);
			System.exit(1);
		}
	}
}
