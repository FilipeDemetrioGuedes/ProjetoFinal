package util;

import totalcross.io.IOException;
import totalcross.ui.image.Image;
import totalcross.ui.image.ImageException;

public class Images {

	public Images () {
		
	}
	
	public static Image background;
	
	public static void loadImages() {
		try {
			background = new Image("resources/menu.jpg");
		} catch (ImageException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
