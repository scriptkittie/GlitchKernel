package io.laniakia.algo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

import io.laniakia.util.GlitchTypes;
import io.laniakia.util.ImageUtil;

public class OffsetPixelSort extends GlitchAlgorithm
{
	public OffsetPixelSort()
	{
		setName(GlitchTypes.OFFSET_PIXEL_SORT);
		setDescription("Sort pixels by image shape displacement of random sub images and RGB shifts");
	}
	
	@Override
	public byte[] glitchPixels(byte[] inputImageBytes) throws Exception 
	{
		int renderBrightness = ((Integer) getPixelGlitchParameters().get("renderBrightness")).intValue();
		int distortionAmount = ((Integer) getPixelGlitchParameters().get("distortionAmount")).intValue();
		boolean scanLines = (Boolean) getPixelGlitchParameters().get("scanlines");
		
		if(renderBrightness < 1 || renderBrightness > 10 || distortionAmount < 1 || distortionAmount > 10)
		{
			return null;
		}
		BufferedImage imageMetadata = ImageUtil.getImageFromBytes(inputImageBytes);
		BufferedImage outputImage = new BufferedImage(imageMetadata.getWidth(), imageMetadata.getHeight(), imageMetadata.getType());
		Graphics2D g2d = outputImage.createGraphics();
		g2d.drawImage(imageMetadata, 0, 0, null);
		int maxOffsetMarker = (int) (((double)distortionAmount * distortionAmount / 100) * imageMetadata.getWidth());
		for (int i = 0; i < distortionAmount * 2; i++) 
		{
			int distortionOffsetY = ThreadLocalRandom.current().nextInt(0, imageMetadata.getHeight() + 1);
			int offsetDistortionHeight = Math.min(ThreadLocalRandom.current().nextInt(1, (imageMetadata.getHeight() / 4) + 1), imageMetadata.getHeight() - distortionOffsetY);
			int offsetRenderDistance = ThreadLocalRandom.current().nextInt(-maxOffsetMarker, maxOffsetMarker + 1);
			if (offsetRenderDistance == 0)
			{
				continue;
			}
			else if (offsetRenderDistance < 0) 
			{
				g2d.drawImage(imageMetadata.getSubimage(-offsetRenderDistance, distortionOffsetY, imageMetadata.getWidth() + offsetRenderDistance, offsetDistortionHeight), 0, distortionOffsetY, null);
				g2d.drawImage(imageMetadata.getSubimage(0, distortionOffsetY, -offsetRenderDistance, offsetDistortionHeight), imageMetadata.getWidth() + offsetRenderDistance, distortionOffsetY, null);
			}
			else if(offsetRenderDistance >=0)
			{
				g2d.drawImage(imageMetadata.getSubimage(0, distortionOffsetY, imageMetadata.getWidth(), offsetDistortionHeight), offsetRenderDistance, distortionOffsetY, null);
				g2d.drawImage(imageMetadata.getSubimage(imageMetadata.getWidth() - offsetRenderDistance, distortionOffsetY, offsetRenderDistance, offsetDistortionHeight), 0, distortionOffsetY, null);
			}
		}
		int[] bufferedImageOutputRGBArray = ImageUtil.getCanvasFormatPixels(outputImage);
		int[] bufferedImageInputRGBArray = ImageUtil.getCanvasFormatPixels(imageMetadata);
		int rgbCopyShift = ThreadLocalRandom.current().nextInt(0, 3);
		int distortionStartPointColumn = ThreadLocalRandom.current().nextInt(-distortionAmount * 2, (distortionAmount * 2) + 1);
		int distortionStartPointRow = ThreadLocalRandom.current().nextInt(-distortionAmount * 2, (distortionAmount * 2) + 1);
		for (int i = 0; i < imageMetadata.getHeight(); i++) 
		{
			for (int j = 0; j < imageMetadata.getWidth(); j++) 
			{
				int distortionStartColumnX = distortionStartPointColumn + j;
				int distortionStartRowY = distortionStartPointRow + i;
				int pixelCanvasPosition = (distortionStartColumnX + distortionStartRowY * imageMetadata.getWidth()) * 4;
				int randomRGBChannelColor = -1;
				Color inputImageRGBColor = getBufferedArrayPixel(j, i, bufferedImageInputRGBArray, imageMetadata);
				Color outputRGBColor = getBufferedArrayPixel(distortionStartColumnX, distortionStartRowY, bufferedImageOutputRGBArray, imageMetadata);
				Color copyColorOutput = null;
				if(rgbCopyShift == 0)
				{
					randomRGBChannelColor = inputImageRGBColor.getRed();
					copyColorOutput = new Color(randomRGBChannelColor, outputRGBColor.getGreen(), outputRGBColor.getBlue(), outputRGBColor.getAlpha());
				}
				else if(rgbCopyShift == 1)
				{
					randomRGBChannelColor = inputImageRGBColor.getGreen();
					copyColorOutput = new Color(outputRGBColor.getRed(), randomRGBChannelColor, outputRGBColor.getBlue(), outputRGBColor.getAlpha());
				}
				else if(rgbCopyShift == 2)
				{
					randomRGBChannelColor = inputImageRGBColor.getBlue();
					copyColorOutput = new Color(outputRGBColor.getRed(), outputRGBColor.getGreen(), randomRGBChannelColor, outputRGBColor.getAlpha());
				}
				if(pixelCanvasPosition < 0 || pixelCanvasPosition + 4 > bufferedImageOutputRGBArray.length)
				{
					continue;
				}
				bufferedImageOutputRGBArray[pixelCanvasPosition] = copyColorOutput.getRed();
				bufferedImageOutputRGBArray[pixelCanvasPosition + 1] = copyColorOutput.getGreen();
				bufferedImageOutputRGBArray[pixelCanvasPosition + 2] = copyColorOutput.getBlue();
				bufferedImageOutputRGBArray[pixelCanvasPosition + 3] = copyColorOutput.getAlpha();
			}
		}
		outputImage = ImageUtil.getImageFromCanvasPixelArray(bufferedImageOutputRGBArray, imageMetadata);
		float brightnessLevel = 1 + (float) renderBrightness / 10;
		outputImage = ImageUtil.changeBrightness(outputImage, brightnessLevel);
		if(scanLines)
		{
			g2d = outputImage.createGraphics();

			for (int i = 0; i < imageMetadata.getWidth(); i++) 
			{
				if (i % 2 == 0)
				{
					g2d.setColor(Color.BLACK);
					g2d.fillRect(0, i, imageMetadata.getWidth(), 1);
				}
			}
		}
		g2d.dispose();
		return ImageUtil.getImageBytes(outputImage);
	}
	
	public Color getBufferedArrayPixel(int x, int y, int[] bufferedImageArray, BufferedImage imageMetadata)
	{
		int pixelCanvasPosition = (x + y * imageMetadata.getWidth()) * 4;
		if(pixelCanvasPosition < 0 || pixelCanvasPosition + 4 > bufferedImageArray.length)
		{
			return new Color(0, 0, 0, 0);
		}
		return new Color(bufferedImageArray[pixelCanvasPosition], bufferedImageArray[pixelCanvasPosition + 1], bufferedImageArray[pixelCanvasPosition+2], bufferedImageArray[pixelCanvasPosition+3]);
	}
}
