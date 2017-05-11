package io.laniakia.algo;

import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

import io.laniakia.util.GlitchTypes;
import io.laniakia.util.ImageUtil;

public class PixelSlice extends GlitchAlgorithm
{
	public PixelSlice()
	{
		setName(GlitchTypes.PIXEL_SLICE);
		setDescription("Sort pixels in a random order by iterations");
	}
	
	@Override
	public byte[] glitchPixels(byte[] inputImageBytes) throws Exception 
	{
		int distortionAmount = ((Integer) getPixelGlitchParameters().get("distortionAmount")).intValue();
		
		if(distortionAmount < 1 || distortionAmount > 9)
		{
			return null;
		}
		
		BufferedImage inputImage = ImageUtil.getImageFromBytes(inputImageBytes);
		int[] pixelCanvasArray = ImageUtil.getCanvasFormatPixels(inputImage);
		int randomSliceAmount = -1;
		for (int i = 0; i < inputImage.getWidth(); i++) 
		{
			if(Math.random() > 0.95)
			{
				float distortion = (float) distortionAmount / 10;
				randomSliceAmount = (int) Math.floor(((1.0 - distortion) * ThreadLocalRandom.current().nextFloat() + distortion) * inputImage.getHeight());
			}
			if(Math.random() > 0.95)
			{
				randomSliceAmount = 0;
			}
			for (int j = 0; j < inputImage.getHeight(); j++) 
			{
				int pixelCanvasPosition = (i + j * inputImage.getWidth()) * 4; 
				int rowDistortionOffsetStart = j + randomSliceAmount;
				if(rowDistortionOffsetStart > inputImage.getHeight() - 1)
				{
					rowDistortionOffsetStart = rowDistortionOffsetStart - inputImage.getHeight();
				}
				
				int rowDistortionOffsetEnd = (i +rowDistortionOffsetStart * inputImage.getWidth()) * 4; 
				for(int k = 0; k < 4; k++)
				{
					if((rowDistortionOffsetEnd + k) < 0 || (rowDistortionOffsetEnd + k) > pixelCanvasArray.length)
					{
						continue;
					}
					pixelCanvasArray[rowDistortionOffsetEnd + k] = pixelCanvasArray[pixelCanvasPosition + k];
				}
			}
		}
		BufferedImage outputImage = ImageUtil.getImageFromCanvasPixelArray(pixelCanvasArray, inputImage);
		return ImageUtil.getImageBytes(outputImage);
	}
}
