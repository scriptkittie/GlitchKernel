package io.laniakia.algo;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

import io.laniakia.util.GlitchTypes;
import io.laniakia.util.ImageUtil;

public class FractalPixelSort extends GlitchAlgorithm
{
	public FractalPixelSort()
	{
		setName(GlitchTypes.FRACTAL_PIXEL_DISPERSE);
		setDescription("Sort pixels by generating Fractals at random points on image map");
	}
	
	@Override
	public byte[] glitchPixels(byte[] inputImageBytes) throws Exception 
	{
		int distortionAmount = ((Integer) getPixelGlitchParameters().get("distortionAmount")).intValue();
		
		BufferedImage outputImage = ImageUtil.getImageFromBytes(inputImageBytes);
		int[] pixelImageBytes = ImageUtil.getCanvasFormatPixels(outputImage);
		//you *could* make this random
		//int randomFractalDistortionAmount = ThreadLocalRandom.current().nextInt(2, distortionAmount + 1);
		distortionAmount = distortionAmount++;
		for (int i = pixelImageBytes.length -1; i > 0 ; i--) 
		{
			if(pixelImageBytes[(i * distortionAmount) % pixelImageBytes.length ] < pixelImageBytes[i])
			{
				pixelImageBytes[i] = pixelImageBytes[(i * distortionAmount) % pixelImageBytes.length ];
			}
		}
		outputImage = ImageUtil.getImageFromCanvasPixelArray(pixelImageBytes, ImageUtil.getImageFromBytes(inputImageBytes));
		int leftSideFractal = (int) Math.round(ThreadLocalRandom.current().nextFloat() * (outputImage.getWidth() - 10) + 10);
		int rightSideFractal = (int) Math.round(ThreadLocalRandom.current().nextFloat() * (outputImage.getWidth() - 10) + leftSideFractal);
		for (int i = 0; i < outputImage.getHeight(); i++) 
		{
			for (int j = 0; j < outputImage.getWidth(); j++) 
			{
				int pixelCanvasPosition = (j + i * outputImage.getWidth()) * 4;
				Color currentPixel = getBufferedArrayPixel(j, i, pixelImageBytes, outputImage);
				int shiftDirection = ThreadLocalRandom.current().nextInt(0, 2);
				if(shiftDirection == 0)
				{
					if(pixelCanvasPosition + leftSideFractal + 1 > pixelImageBytes.length -1)
					{
						continue;
					}
					if(rightSideFractal % 3 == 0)
					{
						pixelImageBytes[pixelCanvasPosition] = currentPixel.getBlue();
						pixelImageBytes[pixelCanvasPosition + leftSideFractal] = currentPixel.getRed();
						pixelImageBytes[pixelCanvasPosition + leftSideFractal + 1] = currentPixel.getGreen();
					}
					else if(rightSideFractal % 3 == 1)
					{
						pixelImageBytes[pixelCanvasPosition] = currentPixel.getRed();
						pixelImageBytes[pixelCanvasPosition + leftSideFractal] = currentPixel.getBlue();
						pixelImageBytes[pixelCanvasPosition + leftSideFractal + 1] = currentPixel.getGreen();
					}
					else
					{
						pixelImageBytes[pixelCanvasPosition] = currentPixel.getRed();
						pixelImageBytes[pixelCanvasPosition + leftSideFractal] = currentPixel.getBlue();
					}
				}
				else
				{
					if((pixelCanvasPosition - leftSideFractal) <0)
					{
						continue;
					}
					if(rightSideFractal % 3 == 0)
					{
						pixelImageBytes[pixelCanvasPosition] = currentPixel.getBlue();
						pixelImageBytes[pixelCanvasPosition - leftSideFractal] = currentPixel.getGreen();
						pixelImageBytes[pixelCanvasPosition - leftSideFractal + 1] = currentPixel.getRed();
					}
					else if(rightSideFractal % 3 == 1)
					{
						pixelImageBytes[pixelCanvasPosition + 1] = currentPixel.getBlue();
						pixelImageBytes[pixelCanvasPosition - leftSideFractal] = currentPixel.getBlue();

					}
					else
					{
						pixelImageBytes[pixelCanvasPosition] = currentPixel.getGreen();
						pixelImageBytes[pixelCanvasPosition - leftSideFractal] = currentPixel.getBlue();
						pixelImageBytes[pixelCanvasPosition - leftSideFractal + 1] = currentPixel.getRed();
					}
				}
			}
		}
		outputImage = ImageUtil.getImageFromCanvasPixelArray(pixelImageBytes, ImageUtil.getImageFromBytes(inputImageBytes));
		return ImageUtil.getImageBytes(outputImage);
	}
	
	public Color getBufferedArrayPixel(int x, int y, int[] bufferedImageArray, BufferedImage imageMetadata)
	{
		int pixelCanvasPosition = (x + y * imageMetadata.getWidth()) * 4;
		if(pixelCanvasPosition < 0 || pixelCanvasPosition+4 > bufferedImageArray.length)
		{
			return new Color(0, 0, 0, 0);
		}
		return new Color(bufferedImageArray[pixelCanvasPosition], bufferedImageArray[pixelCanvasPosition+1], bufferedImageArray[pixelCanvasPosition+2], bufferedImageArray[pixelCanvasPosition+3]);
	}
}
