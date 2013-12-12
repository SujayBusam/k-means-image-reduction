import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.text.*;
import java.util.ArrayList;

/**
 * A class that represents a picture.  This class inherits from 
 * SimplePicture and allows the student to add functionality to
 * the Picture class.  
 * 
 * This version modified by Scot Drysdale to demonstrate arraylists.
 * (used in reduceTo8).  See end of listing.  
 * Modified 1/12/13 to change main.
 *  
 * Copyright Georgia Institute of Technology 2004-2008
 * @author Barbara Ericson ericson@cc.gatech.edu
 * 
 * @author Sujay Busam
 * Changes made for CS10 PS-1
 * Added methods at bottom and modified main method
 * Created methods for reducing image colors to a given list of colors,
 * and a random list of colors. K means methods incomplete.
 */
public class Picture extends SimplePicture { 

	///////////////////// constructors //////////////////////////////////

	/**
	 * Constructor that takes no arguments 
	 */
	public Picture () {

		/* not needed but use it to show students the implicit call to super()
		 * child constructors always call a parent constructor 
		 */
		super();  
	}

	/**
	 * Constructor that takes a file name and creates the picture 
	 * @param fileName the name of the file to create the picture from
	 */
	public Picture(String fileName) {
		// let the parent class handle this fileName
		super(fileName);
	}

	/**
	 * Constructor that takes the width and height
	 * @param width the width of the desired picture
	 * @param height the height of the desired picture
	 */
	public Picture(int width, int height) {
		// let the parent class handle this width and height
		super(width,height);
	}

	/**
	 * Constructor that takes a picture and creates a 
	 * copy of that picture
	 */
	public Picture(Picture copyPicture) {
		// let the parent class do the copy
		super(copyPicture);
	}

	/**
	 * Constructor that takes a buffered image
	 * @param image the buffered image to use
	 */
	public Picture(BufferedImage image) {
		super(image);
	}

	////////////////////// methods ///////////////////////////////////////

	/**
	 * Method to return a string with information about this picture.
	 * @return a string with information about the picture such as fileName,
	 * height and width.
	 */
	public String toString() {
		String output = "Picture, filename " + getFileName() + 
		" height " + getHeight() 
		+ " width " + getWidth();
		return output;

	}

	/**
	 * Class method to let the user pick a file name and then create the picture 
	 * and show it
	 * @return the picture object
	 */
	public static Picture pickAndShow() {
		String fileName = FileChooser.pickAFile();
		Picture picture = new Picture(fileName);
		picture.show();
		return picture;
	}

	/**
	 * Class method to create a picture object from the passed file name and 
	 * then show it
	 * @param fileName the name of the file that has a picture in it
	 * @return the picture object
	 */
	public static Picture showNamed(String fileName) {
		Picture picture = new Picture(fileName);
		picture.show();
		return picture;
	}

	/**
	 * A method create a copy of the current picture and return it
	 * @return the copied picture
	 */
	public Picture copy()
	{
		return new Picture(this);
	}

	/**
	 * Method to increase the red in a picture.
	 */
	public void increaseRed() {
		Pixel [] pixelArray = this.getPixels();
		for (Pixel pixelObj : pixelArray) {
			pixelObj.setRed(pixelObj.getRed()*2);
		}
	}

	/**
	 * Method to negate a picture
	 */
	public void negate() {
		Pixel [] pixelArray = this.getPixels();
		int red,green,blue;

		for (Pixel pixelObj : pixelArray) {
			red = pixelObj.getRed();
			green = pixelObj.getGreen();
			blue = pixelObj.getBlue();
			pixelObj.setColor(new Color(255-red, 255-green, 255-blue));
		}
	}

	/**
	 * Method to flip a picture 
	 */
	public Picture flip() {
		Pixel currPixel = null;
		Pixel targetPixel = null;
		Picture target = 
			new Picture(this.getWidth(),this.getHeight());

		for (int srcX = 0, trgX = getWidth()-1; 
		srcX < getWidth();
		srcX++, trgX--) {
			for (int srcY = 0, trgY = 0; 
			srcY < getHeight();
			srcY++, trgY++) {

				// get the current pixel
				currPixel = this.getPixel(srcX,srcY);
				targetPixel = target.getPixel(trgX,trgY);

				// copy the color of currPixel into target
				targetPixel.setColor(currPixel.getColor());
			}
		}
		return target;
	}

	/**
	 * Method to decrease the red by half in the current picture
	 */
	public void decreaseRed() {

		Pixel pixel = null; // the current pixel
		int redValue = 0;       // the amount of red

		// get the array of pixels for this picture object
		Pixel[] pixels = this.getPixels();

		// start the index at 0
		int index = 0;

		// loop while the index is less than the length of the pixels array
		while (index < pixels.length) {

			// get the current pixel at this index
			pixel = pixels[index];
			// get the red value at the pixel
			redValue = pixel.getRed();
			// set the red value to half what it was
			redValue = (int) (redValue * 0.5);
			// set the red for this pixel to the new value
			pixel.setRed(redValue);
			// increment the index
			index++;
		}
	}

	/**
	 * Method to decrease the red by an amount
	 * @param amount the amount to change the red by
	 */
	public void decreaseRed(double amount) {

		Pixel[] pixels = this.getPixels();
		Pixel p = null;
		int value = 0;

		// loop through all the pixels
		for (int i = 0; i < pixels.length; i++) {

			// get the current pixel
			p = pixels[i];
			// get the value
			value = p.getRed();
			// set the red value the passed amount time what it was
			p.setRed((int) (value * amount));
		}
	}

	/**
	 * Method to compose (copy) this picture onto a target picture
	 * at a given point.
	 * @param target the picture onto which we copy this picture
	 * @param targetX target X position to start at
	 * @param targetY target Y position to start at
	 */
	public void compose(Picture target, int targetX, int targetY) {

		Pixel currPixel = null;
		Pixel newPixel = null;

		// loop through the columns
		for (int srcX=0, trgX = targetX; srcX < this.getWidth();
		srcX++, trgX++) {

			// loop through the rows
			for (int srcY=0, trgY=targetY; srcY < this.getHeight();
			srcY++, trgY++) {

				// get the current pixel
				currPixel = this.getPixel(srcX,srcY);

				/* copy the color of currPixel into target,
				 * but only if it'll fit.
				 */
				if (trgX < target.getWidth() && trgY < target.getHeight()) {
					newPixel = target.getPixel(trgX,trgY);
					newPixel.setColor(currPixel.getColor());
				}
			}
		}
	}

	/**
	 * Method to scale the picture by a factor, and return the result
	 * @param factor the factor to scale by (1.0 stays the same,
	 *    0.5 decreases each side by 0.5, 2.0 doubles each side)
	 * @return the scaled picture
	 */
	public Picture scale(double factor) {

		Pixel sourcePixel, targetPixel;
		Picture canvas = new Picture(
				(int) (factor*this.getWidth())+1,
				(int) (factor*this.getHeight())+1);
		// loop through the columns
		for (double sourceX = 0, targetX=0;
		sourceX < this.getWidth();
		sourceX+=(1/factor), targetX++) {

			// loop through the rows
			for (double sourceY=0, targetY=0;
			sourceY < this.getHeight();
			sourceY+=(1/factor), targetY++) {

				sourcePixel = this.getPixel((int) sourceX,(int) sourceY);
				targetPixel = canvas.getPixel((int) targetX, (int) targetY);
				targetPixel.setColor(sourcePixel.getColor());
			}
		}
		return canvas;
	}

	/**
	 * Method to do chromakey using an input color for the background
	 * and a point for the upper left corner of where to copy
	 * @param target the picture onto which we chromakey this picture
	 * @param bgColor the color to make transparent
	 * @param threshold within this distance from bgColor, make transparent
	 * @param targetX target X position to start at
	 * @param targetY target Y position to start at
	 */
	public void chromakey(Picture target, Color bgColor, int threshold,
			int targetX, int targetY) {

		Pixel currPixel = null;
		Pixel newPixel = null;

		// loop through the columns
		for (int srcX=0, trgX=targetX;
		srcX<getWidth() && trgX<target.getWidth();
		srcX++, trgX++) {

			// loop through the rows
			for (int srcY=0, trgY=targetY;
			srcY<getHeight() && trgY<target.getHeight();
			srcY++, trgY++) {

				// get the current pixel
				currPixel = this.getPixel(srcX,srcY);

				/* if the color at the current pixel is within threshold of
				 * the input color, then don't copy the pixel
				 */
				if (currPixel.colorDistance(bgColor)>threshold) {
					target.getPixel(trgX,trgY).setColor(currPixel.getColor());
				}
			}
		}
	}

	/**
	 * Method to do chromakey assuming a blue background 
	 * @param target the picture onto which we chromakey this picture
	 * @param targetX target X position to start at
	 * @param targetY target Y position to start at
	 */
	public void blueScreen(Picture target,
			int targetX, int targetY) {

		Pixel currPixel = null;
		Pixel newPixel = null;

		// loop through the columns
		for (int srcX=0, trgX=targetX;
		srcX<getWidth() && trgX<target.getWidth();
		srcX++, trgX++) {

			// loop through the rows
			for (int srcY=0, trgY=targetY;
			srcY<getHeight() && trgY<target.getHeight();
			srcY++, trgY++) {

				// get the current pixel
				currPixel = this.getPixel(srcX,srcY);

				/* if the color at the current pixel mostly blue (blue value is
				 * greater than red and green combined), then don't copy pixel
				 */
				if (currPixel.getRed() + currPixel.getGreen() > currPixel.getBlue()) {
					target.getPixel(trgX,trgY).setColor(currPixel.getColor());
				}
			}
		}
	}

	/**
	 * Method to change the picture to gray scale with luminance
	 */
	public void grayscaleWithLuminance()
	{
		Pixel[] pixelArray = this.getPixels();
		Pixel pixel = null;
		int luminance = 0;
		double redValue = 0;
		double greenValue = 0;
		double blueValue = 0;

		// loop through all the pixels
		for (int i = 0; i < pixelArray.length; i++)
		{
			// get the current pixel
			pixel = pixelArray[i];

			// get the corrected red, green, and blue values
			redValue = pixel.getRed() * 0.299;
			greenValue = pixel.getGreen() * 0.587;
			blueValue = pixel.getBlue() * 0.114;

			// compute the intensity of the pixel (average value)
			luminance = (int) (redValue + greenValue + blueValue);

			// set the pixel color to the new color
			pixel.setColor(new Color(luminance,luminance,luminance));

		}
	}

	/** 
	 * Method to do an oil paint effect on a picture
	 * @param dist the distance from the current pixel 
	 * to use in the range
	 * @return the new picture
	 */
	public Picture oilPaint(int dist) {

		// create the picture to return
		Picture retPict = new Picture(this.getWidth(),this.getHeight());

		// declare pixels
		Pixel currPixel = null;
		Pixel retPixel = null;

		// loop through the pixels
		for (int x = 0; x < this.getWidth(); x++) {
			for (int y = 0; y < this.getHeight(); y++) {
				currPixel = this.getPixel(x,y);
				retPixel = retPict.getPixel(x,y);
				retPixel.setColor(currPixel.getMostCommonColorInRange(dist));
			}
		}
		return retPict;
	}


	/***********
	 * Methods added by Scot Drysdale to demonstrate ArrayLists
	 **********/

	/**
	 * Reduces the number of colors to 8 by picking two values for red,
	 * two for green, and two for blue.  The two red values chosen are the
	 * average of the pixel red value that are greater than a threshold
	 * and the average of the pixel red values less than or equal to the threshold.
	 * The same is done for green and blue
	 */
	public void reduceTo8() {
		Pixel [] pixelArray = this.getPixels();  // Array of all pixels in the image
		final int THRESHOLD = 126;     // Dividing line between low and high color values

		for(int colorNum = 1; colorNum <= 3; colorNum++) {
			ArrayList<Pixel> lowValues = new ArrayList<Pixel>();
			ArrayList<Pixel> highValues = new ArrayList<Pixel>();

			for(Pixel pixel : pixelArray) {
				// Split the pixels into low and high color values for color colorNum
				if(getColor(pixel, colorNum) <= THRESHOLD)
					lowValues.add(pixel);
				else
					highValues.add(pixel);
			} 

			int lowAve = Math.round(averageColors(lowValues, colorNum));
			int highAve = Math.round(averageColors(highValues, colorNum));

			// Reset the color values to the average values
			for (Pixel lowPix : lowValues)
				setColor(lowPix, lowAve, colorNum);
			for (Pixel highPix : highValues)
				setColor(highPix, highAve, colorNum);                                 
		}
	}

	/**
	 * Gets the value of the color corresponding to colorNum.
	 * In an ideal world this would be added to the Pixel class
	 * Precondition - colorNum is 1, 2, or 3.  (We will learn to throw exceptions later.)
	 * 
	 * @param pixel the pixel whose color is returned
	 * @param colorNum the color to choose: 1 = red, 2 = green, 3 = blue
	 */
	public static int getColor(Pixel pixel, int colorNum) {
		if(colorNum == 1)
			return pixel.getRed();
		else if (colorNum == 2)
			return pixel.getGreen();
		else
			return pixel.getBlue();
	}

	/**
	 * Sets the value of the color corresponding to colorNum to newValue.
	 * In an ideal world this would be added to the Pixel class
	 * Precondition - colorNum is 1, 2, or 3.  (We will learn to throw exceptions later.)
	 * 
	 * @param pixel the pixel whose color is set
	 * @param newValue the new value for the color
	 * @param colorNum the color to choose: 1 = red, 2 = green, 3 = blue
	 */
	public static void setColor(Pixel pixel, int newValue, int colorNum) {
		if(colorNum == 1)
			pixel.setRed(newValue);
		else if (colorNum == 2)
			pixel.setGreen(newValue);
		else
			pixel.setBlue(newValue);
	}

	/**
	 * Averages the chosen color for all the pixels in an ArrayList.
	 * Returns 0 if ArrayList is empty.
	 * Precondition - colorNum is 1, 2, or 3.  (We will learn to throw exceptions later.)
	 * 
	 * @param pixels the list of pixels to be averaged
	 * @param colorNum the color to average: 1 = red, 2 = green, 3 = blue
	 * @return the average of the chosen color value
	 */
	public static float averageColors(ArrayList<Pixel> pixels, int colorNum) {
		float sum = 0.0f;

		for(int i = 0; i < pixels.size(); i++)
			sum += getColor(pixels.get(i), colorNum);

		if(pixels.size() > 0)
			return sum/pixels.size();
		else
			return 0.0f;
	}


	/** 
	 * @author Sujay Busam
	 * Method to return a new picture where each pixel in the original picture 
	 * is replace by the color in the passed arraylist that is nearest to it
	 * @param colors the arraylist containing Color objects
	 * @return the new picture
	 */

	public Picture mapToColorList(ArrayList<Color> colors) {
		Pixel currPixel = null; // Current pixel in this picture
		Pixel targetPixel = null; // Corresponding target pixel
		Color currColor = null; // Color of current pixel
		int targetIndex = 0; // index of Color of corresponding target pixel

		// Create target picture
		Picture target = new Picture(this.getWidth(), this.getHeight());

		// Run through each pixel in this picture
		for (int srcX = 0; srcX < this.getWidth(); srcX++) {
			for (int srcY = 0; srcY < this.getHeight(); srcY++) {

				// get the current pixel at current X and Y coordinates
				currPixel = this.getPixel(srcX,srcY);
				// get the target pixel at current X and Y coordinates
				targetPixel = target.getPixel(srcX,srcY);

				// Current color
				currColor = currPixel.getColor();

				// Get index of "nearest" Color object in arraylist
				targetIndex = findClosestColor(currColor, colors);

				// Set target pixel to corresponding color in arraylist index
				targetPixel.setColor(colors.get(targetIndex));
			}
		}

		return target; // Target picture
	}


	/** 
	 * @author Sujay Busam
	 * Method to generate and return a list of representative colors for a picture.
	 * Uses k-means to generate the colors.
	 * @param initColors the initial list of colors to start k-means
	 * @return the list of colors generated (may be shorter than initColors)
	 */

	// Returns correct / final arraylist of colors computed using k means
	public ArrayList<Color> computeColors(int number) {

		// ArrayList containing "numbers" amount of random colors
		ArrayList<Color> colors = createRandomList(number);

		// Initialize clusters list
		ArrayList<ArrayList<Color>> clusters = updateClusters(colors, number);
		ArrayList<Color> oldCentroids = colors;
		ArrayList<Color> newCentroids = updateCentroids(clusters);

		while (!oldCentroids.equals(newCentroids)) {
			// Update clusters list
			clusters = updateClusters(newCentroids, clusters.size());

			// Update lists of centroids
			oldCentroids = newCentroids;
			newCentroids = updateCentroids(clusters);
		}

		return newCentroids;
	}


	/**
	 * @author Sujay Busam
	 * Helper method to update 2D clusters array
	 * @param clusters the 2D clusters array
	 * @param colors the arraylist of original colors
	 * @return clusters the updated 2D clusters array
	 */
	public ArrayList<ArrayList<Color>> updateClusters(ArrayList<Color> colors, int number) {

		Pixel currPixel = null; // Current pixel in this picture
		Color currColor = null; // Color of current pixel
		int targetIndex = 0; // index of Color of corresponding target pixel

		// 2D arraylist to hold colors and corresponding pixel colors
		ArrayList<ArrayList<Color>> clusters = new ArrayList<ArrayList<Color>>();

		// Add "number" amount of arraylists to the 2D arraylist
		for (int i = 0; i < number; i++) {
			ArrayList<Color> newList = new ArrayList<Color>();
			clusters.add(newList);
		}

		// Run through each pixel in this picture
		for (int srcX = 0; srcX < this.getWidth(); srcX++) {
			for (int srcY = 0; srcY < this.getHeight(); srcY++) {

				// get the current pixel at current X and Y coordinates
				currPixel = this.getPixel(srcX,srcY);

				// Current color
				currColor = currPixel.getColor();

				// Get index of "nearest" Color object in arraylist
				targetIndex = findClosestColor(currColor, colors);

				// Add color object to corresponding index in clusters
				clusters.get(targetIndex).add(currColor);
			}
		}

		return clusters;
	}

	/**
	 * @author Sujay Busam
	 * Helper method to return an arraylist of updated centroids
	 * @param clusters
	 * @return centroids
	 */
	public ArrayList<Color> updateCentroids(ArrayList<ArrayList<Color>> clusters) {
		ArrayList<Color> centroids = new ArrayList<Color>();

		// Run through each cluster
		for (ArrayList<Color> cluster: clusters) {
			
			// If cluster is empty, don't compute centroid
			if (cluster.size() == 0) {
				continue;
			}

			int redSum = 0;
			int greenSum = 0;
			int blueSum = 0;
			int redAvg = 0;
			int greenAvg = 0;
			int blueAvg = 0;

			// Run through each color in current cluster
			for (Color color: cluster) {
				// Sum the color values
				redSum += color.getRed();
				greenSum += color.getGreen();
				blueSum += color.getBlue();
			}

			// Compute average color values for each cluster
			redAvg = (int)(redSum * 1.0 / cluster.size());
			greenAvg = (int)(greenSum * 1.0 / cluster.size());
			blueAvg = (int)(blueSum * 1.0 / cluster.size());

			// Create new centroid
			Color centroid = new Color(redAvg, greenAvg, blueAvg);

			// Add centroid to arraylist
			centroids.add(centroid);

		}

		return centroids;
	}

	/** 
	 * @author Sujay Busam
	 * Method to reduce the number of distinct colors in a picture.
	 * Uses k-means to generate a representative set of colors.
	 * @param number the number of colors to use
	 * @return the picture rendered in that many colors
	 */

	// Returns target picture using correct arraylist of colors
	public Picture reduceColors(int number) {
		// calls computeColors and passes the result to mapToColorList
		return mapToColorList(computeColors(number));
	}



	/** 
	 * @author Sujay Busam
	 * Helper method to return the index Color object "closest" to that of
	 * the source pixel
	 * @param currColor the color of the source pixel
	 * @param colors the original arraylist containing the possible colors
	 * @return the index of closest Color object
	 */

	private static int findClosestColor(Color currColor, ArrayList<Color> colors) {

		// Create an arraylist that stores distance values
		ArrayList<Double> colorDistance = new ArrayList<Double>();

		// Get distance between this color and each color in arraylist. Store each distance
		for (int i = 0; i < colors.size(); i++) {
			colorDistance.add(i, Pixel.colorDistance(currColor, colors.get(i)));
		}

		// Run through colorDistance arraylist and find lowest value
		double lowestDistance = colorDistance.get(0);
		for (int i = 1; i < colorDistance.size(); i++) {
			if (colorDistance.get(i) < lowestDistance) {
				lowestDistance = colorDistance.get(i);
			}
		}

		// Return index of lowestDistance
		return colorDistance.indexOf(lowestDistance);
	}

	/** 
	 * @author Sujay Busam
	 * Helper method to return an arraylist of requested length containing
	 * Color objects of random colors
	 * @param arrayLength the requested length of the arraylist
	 * @return the new arraylist
	 */

	private static ArrayList<Color> createRandomList(int arrayLength) {

		// Arraylist to be returned
		ArrayList<Color> color = new ArrayList<Color>();

		// Run through arraylist and fill list with Color objects of random colors
		for (int i = 0; i < arrayLength; i++) {

			// Random colors
			int redValue = (int)(Math.random() * 255);
			int greenValue = (int)(Math.random() * 255);
			int blueValue = (int)(Math.random() * 255);

			// Add color object containing random colors to arraylist
			color.add(new Color(redValue, greenValue, blueValue));
		}

		return color;
	}

	/** 
	 * @author Sujay Busam
	 * Helper method to take the first k unique colors from the 
	 * list of pixels in the image
	 * @param k the number of unique colors
	 * @return a list of colors
	 */

	public ArrayList<Color> getKColors(int k){

		// ArrayList that will hold the k colors
		ArrayList<Color> kList = new ArrayList<Color>();

		Pixel currPixel = null;
		Color currColor = null;


		while (kList.size() < k)	{ 

			// Run through every pixel
			for (int currX = 0; currX < this.getWidth(); currX++) {
				for (int currY = 0; currY < this.getHeight(); currY++) {

					// Current pixel and color of that pixel
					currPixel = this.getPixel(currX, currY);
					currColor = currPixel.getColor();

					// If color is unique, add it to the list
					if(!kList.contains(currColor)){
						kList.add(currColor);
					}
				}
			}
		}

		return kList;

	}	



	public static void main(String[] args)
	{
		Picture p = 
			new Picture(FileChooser.pickAFile());
		p.explore();

		// Each of the three parts have the line(s) containing the explore call commented out
		// To run any of the parts, uncomment respective lines

		ArrayList<Color> color = new ArrayList<Color>();
		color.add(Color.red);
		color.add(Color.green);
		color.add(Color.blue);
		color.add(Color.cyan);
		color.add(Color.orange);
		color.add(Color.yellow);
		color.add(Color.black);
		color.add(Color.white);
		//		 p.mapToColorList(color).explore();



		ArrayList<Color> color2 = new ArrayList<Color>();
		Color skyBlue = new Color(140, 183, 225);
		Color lightBlue = new Color(172, 203, 223);
		Color oceanBlue = new Color(98, 146, 146);
		Color sand = new Color(231, 210, 165);
		Color darkSand = new Color(70, 48, 24);
		Color mountain = new Color(117, 136, 153);
		Color lightGreen = new Color(74, 92, 52);
		Color darkGreen = new Color(16, 35, 13);

		color2.add(skyBlue);
		color2.add(lightBlue);
		color2.add(oceanBlue);
		color2.add(sand);
		color2.add(darkSand);
		color2.add(mountain);
		color2.add(lightGreen);
		color2.add(darkGreen);
//				 p.mapToColorList(color2).explore();





		ArrayList<Color> color3 = createRandomList(8);
		ArrayList<Color> color4 = createRandomList(256);

		//		 p.mapToColorList(color3).explore();
		//		 p.mapToColorList(color4).explore();

		p.reduceColors(8).explore();


	}
} // this } is the end of class Picture, put all new methods before this
