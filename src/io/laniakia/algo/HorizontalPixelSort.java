package io.laniakia.algo;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import io.laniakia.util.GlitchTypes;
import io.laniakia.util.ImageUtil;

public class HorizontalPixelSort extends GlitchAlgorithm 
{
	public HorizontalPixelSort()
	{
		setName(GlitchTypes.HORIZONTAL_PIXEL_SORT);
		setDescription("Horizontal pixel displacement.");
	}

	@Override
	public byte[] glitchPixels(byte[] inputImageBytes) throws Exception
	{
		int interval = ((Integer) getPixelGlitchParameters().get("interval")).intValue();
		BufferedImage inputImage = ImageUtil.getImageFromBytes(inputImageBytes);	
		int[] bufferedImageOutputRGBArray = ImageUtil.getCanvasFormatPixels(inputImage);

		for (int i = 0, j = ThreadLocalRandom.current().nextInt(1,interval); i < j; i++) 
		{
			int pixelEndOffset = Double.valueOf(Math.floor(Math.random() * (inputImage.getWidth() * inputImage.getHeight() * 4))).intValue();
			int pixelStartOffset = Double.valueOf(pixelEndOffset -  (Math.round(Math.random() * 1000) + 5100)).intValue();
			int[] pixelSubArray = Arrays.copyOfRange(bufferedImageOutputRGBArray, pixelStartOffset, pixelEndOffset);
			System.arraycopy(pixelSubArray,0,bufferedImageOutputRGBArray, Double.valueOf(Math.floor(Math.random() * ((inputImage.getWidth() * inputImage.getHeight() * 4) - pixelSubArray.length))).intValue(), pixelSubArray.length);
		}
		return ImageUtil.getImageBytes(ImageUtil.getImageFromCanvasPixelArray(bufferedImageOutputRGBArray, inputImage)); 
	}
}
