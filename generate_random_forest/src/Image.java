import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferUShort;
import java.io.ByteArrayOutputStream;
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
		
		try {
		    BufferedImage originalImage = ImageIO.read(new File(this.absolutePath));
		    ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
		    ImageIO.write(originalImage, MasterConstants.FILETYPE, imageStream);
		    this.imagePixalArray = imageStream.toByteArray();
		    
		} catch (IOException e) {
			System.out.println("Failed to load image "+this.absolutePath);
			System.exit(1);
		}
	}
}
