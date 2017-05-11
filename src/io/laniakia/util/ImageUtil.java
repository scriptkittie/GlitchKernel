package io.laniakia.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.apache.tika.Tika;

public class ImageUtil 
{
	
	public static int[] getCanvasFormatPixels(BufferedImage inputImage)
	{
		int[] bufferedImageRGBArray = new int[inputImage.getWidth() * inputImage.getHeight() * 4];
		int pixelTracker = 0;
		for(int i = 0; i < inputImage.getHeight(); i++)
		{
			for(int j = 0; j < inputImage.getWidth(); j++)
			{
				Color c = new Color(inputImage.getRGB(j, i));
				bufferedImageRGBArray[pixelTracker] = c.getRed();
				bufferedImageRGBArray[pixelTracker + 1] = c.getGreen();
				bufferedImageRGBArray[pixelTracker + 2] = c.getBlue();
				bufferedImageRGBArray[pixelTracker + 3] = c.getAlpha();
				pixelTracker+= 4;
			}
		}
		return bufferedImageRGBArray;
	}
	
	public static BufferedImage getImageFromCanvasPixelArray(int[] pixelArray, BufferedImage imageMetadata)
	{
		BufferedImage outputImage = new BufferedImage(imageMetadata.getWidth(), imageMetadata.getHeight(), imageMetadata.getType());
		int xCount = 0;
		int yCount = 0;
		for(int i = 0; i < pixelArray.length; i+= 4)
		{
			if(xCount >= outputImage.getWidth())
			{
				xCount = 0;
				yCount++;
			}
			outputImage.setRGB(xCount, yCount, new Color(pixelArray[i], pixelArray[i+1], pixelArray[i+2], pixelArray[i+3]).getRGB());
			xCount++;
		}
		return outputImage;
	}
	
	public static BufferedImage changeBrightness(BufferedImage inputImage, float brightness)
	{
		RescaleOp op = new RescaleOp(brightness, 0, null);
		BufferedImage outputImage = op.filter(inputImage, inputImage);	
		return outputImage;
	}
	public static byte[] setGlitchQuality(byte[] inputImageBytes, int quality) throws Exception
	{
		Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("png");
		ImageWriter writer = (ImageWriter)iter.next();  
		ImageWriteParam iwp = writer.getDefaultWriteParam(); 
	    BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(inputImageBytes)); 
	    IIOImage image = new IIOImage(originalImage, null, null);
	    ByteArrayOutputStream out=new ByteArrayOutputStream();
	    ImageOutputStream imageOut=ImageIO.createImageOutputStream(out);
	    writer.setOutput(imageOut);  
	    writer.write(null, image, iwp);  
	    byte[] qualityImageBytes = out.toByteArray();
	    return qualityImageBytes;
	}
	
	public static BufferedImage getImageFromBytes(byte[] inputImageBytes) throws Exception
	{
		return ImageIO.read(new ByteArrayInputStream(inputImageBytes));
	}
	
	public static byte[] getImageBytes(BufferedImage inputImage) throws Exception
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();;
		ImageIO.write(inputImage, "png", baos);
		byte[] imageBytes = baos.toByteArray();
		baos.close();
		return imageBytes;
	}
	
	public static String getImageType(byte[] inputByteArray) throws Exception
	{
		Tika tika = new Tika();
		return tika.detect(inputByteArray);
	}
}
