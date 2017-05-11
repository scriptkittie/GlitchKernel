package io.laniakia.algo;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.concurrent.ThreadLocalRandom;

import javax.imageio.ImageIO;

import io.laniakia.util.GlitchTypes;
import io.laniakia.util.ImageUtil;

public class PixelSort extends GlitchAlgorithm
{
	public PixelSort()
	{
		setName(GlitchTypes.GENERIC_PIXEL_SORT_V1);
		setDescription("Sort pixels in a random order by color displacement");
	}
	
	@Override
	public byte[] glitchPixels(byte[] inputImageBytes) throws Exception 
	{
		int distortion = ((Integer) getPixelGlitchParameters().get("brightness")).intValue();
		double distortionLength = ((Double) getPixelGlitchParameters().get("distortionLength")).doubleValue();
		distortion = ThreadLocalRandom.current().nextInt(distortion, 100);
		int pixelBrightnessChoice = 0;
		distortionLength = ThreadLocalRandom.current().nextDouble(distortionLength, 11.0D);
		double brightnessModification = 0;
		BufferedImage image = ImageIO.read(new ByteArrayInputStream(inputImageBytes));
		int[][] pixel2DArray = new int[image.getWidth()][image.getHeight()];
		int displacedVerticalRow = 0;
		int maximumLuminosity = 0;
		int maximumVerticalDithering = 1;
		int nextPixelIndex = 0;
		for(int i = 0; i < image.getWidth(); i++) 
		{
			for(int j = 0; j < image.getHeight(); j++) 
			{
				Color currentPixel = new Color(image.getRGB(i, j));
				int red = currentPixel.getRed();
				int green = currentPixel.getGreen();
				int blue = currentPixel.getBlue();
				if(j != 0) 
				{
					if(pixelBrightnessChoice == 0) 
					{
						if(nextPixelIndex != i)
						{
							displacedVerticalRow = 0;
						}
						Color nextPixelColor = null;
						if(displacedVerticalRow == 0) 
						{
							nextPixelColor = new Color(image.getRGB(i, j - 1));
						} 
						else 
						{
							nextPixelColor = new Color(image.getRGB(i, j - (1 + displacedVerticalRow)));
						}
						if(displacedVerticalRow != 0) 
						{
							displacedVerticalRow++;
						}
						int meanColorRGB = Math.round((nextPixelColor.getRed() + nextPixelColor.getGreen() + nextPixelColor.getBlue()) / 3);
						if((meanColorRGB >= distortion) && (displacedVerticalRow == 0)) 
						{
							displacedVerticalRow = 1;
							nextPixelIndex = i;
							maximumLuminosity = meanColorRGB - distortion;
							maximumVerticalDithering = (int) Math.round(maximumLuminosity * distortionLength);
						}

						if((displacedVerticalRow > 0) && (displacedVerticalRow < maximumVerticalDithering)) 
						{
							red = nextPixelColor.getRed();
							green = nextPixelColor.getGreen();
							blue = nextPixelColor.getBlue();
						} 
						else 
						{
							displacedVerticalRow = 0;
						}
					}
					else if(i != 0) 
					{
						int meanColorRGB = 0;
						Color previousPixelColumnColor = new Color(image.getRGB(i - 1, j));
						meanColorRGB = Math.round((previousPixelColumnColor.getRed() + previousPixelColumnColor.getGreen() + previousPixelColumnColor.getBlue()) / 3);
						if((meanColorRGB >= distortion) && (displacedVerticalRow == 0)) 
						{
							displacedVerticalRow = 1;
							maximumLuminosity = meanColorRGB - distortion;
							maximumVerticalDithering = (int) Math.round(maximumLuminosity * distortionLength);
							if(brightnessModification > 0.0D) 
							{
								maximumLuminosity = (int) Math.round(maximumLuminosity * brightnessModification);
							}
						}

						if((displacedVerticalRow > 0) && (displacedVerticalRow < maximumVerticalDithering)) 
						{
							if(red + maximumLuminosity <= 255)
							{
								red += maximumLuminosity;
							}
							else
							{
								red = 255;
							}
							if(green + maximumLuminosity <= 255) 
							{
								green += maximumLuminosity;
							} 
							else 
							{
								green = 255;
							}
							if(blue + maximumLuminosity <= 255) 
							{
								blue += maximumLuminosity;
							} 
							else 
							{
								blue = 255;
							}
							displacedVerticalRow++;
						} 
						else 
						{
							displacedVerticalRow = 0;
						}
					}
				}
				Color nextPixelColorOutput = new Color(red, green, blue);
				pixel2DArray[i][j] = nextPixelColorOutput.getRGB();
			}
		}
		for(int i = 0; i < image.getWidth(); i++) 
		{
			for(int j = 0; j < image.getHeight(); j++) 
			{
				image.setRGB(i, j, pixel2DArray[i][j]);
			}
		}
		return ImageUtil.getImageBytes(image);
	}
}
