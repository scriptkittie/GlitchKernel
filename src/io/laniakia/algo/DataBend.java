package io.laniakia.algo;

import java.awt.Color;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.util.concurrent.ThreadLocalRandom;

import io.laniakia.util.ImageUtil;

public class DataBend extends GlitchAlgorithm
{
	public DataBend()
	{
		//setName(GlitchTypes.DATABEND);
		setDescription("Sort pixels in a random order by iterations");
	}
	
	@Override
	public byte[] glitchPixels(byte[] inputImageBytes) throws Exception
	{
		int randomizationPercent = ((Integer) getPixelGlitchParameters().get("randomizationPercent")).intValue();
		int pixelBlockSize = ((Integer) getPixelGlitchParameters().get("pixelBlockSize")).intValue();
		
		BufferedImage outputImage = ImageUtil.getImageFromBytes(inputImageBytes);
		Color[][] pixelArray = new Color[outputImage.getWidth()][outputImage.getHeight()];
		for (int i = 0; i < outputImage.getWidth(); i++) 
		{
			for (int j = 0; j < outputImage.getHeight(); j++) 
			{
				int byteCorruptProbability = ThreadLocalRandom.current().nextInt(0, 101);
				if(byteCorruptProbability <= randomizationPercent && i > pixelBlockSize && j > pixelBlockSize && (outputImage.getWidth() -i ) > pixelBlockSize && (outputImage.getHeight() - j) > pixelBlockSize)
				{
					int expansionDirection = ThreadLocalRandom.current().nextInt(0, 2);
					if(expansionDirection == 0)
					{
						int x = ThreadLocalRandom.current().nextInt(pixelBlockSize, (outputImage.getWidth() - pixelBlockSize));
						int y = ThreadLocalRandom.current().nextInt(pixelBlockSize, (outputImage.getHeight() - pixelBlockSize));
						Color randPixel = new Color(outputImage.getRGB(x, y));
						pixelArray[i][j] = randPixel;
						for(int b = 0; b < pixelBlockSize; b++)
						{
							for(int a = 0; a < pixelBlockSize; b++)
							{
								if((i -b) >= 0 && (j -a) >=0)
									pixelArray[i - b][j - a] = new Color(outputImage.getRGB(x-b, y - a));
							}
						}
					}
					else
					{
						int x = ThreadLocalRandom.current().nextInt(pixelBlockSize, (outputImage.getWidth() - pixelBlockSize));
						int y = ThreadLocalRandom.current().nextInt(pixelBlockSize, (outputImage.getHeight() - pixelBlockSize));
						Color randPixel = new Color(outputImage.getRGB(x, y));
						pixelArray[i][j] = randPixel;
						for(int b = 0; b < pixelBlockSize; b++)
						{
							for(int a = 0; a < pixelBlockSize; b++)
							{
								pixelArray[i + b][j + a] = new Color(outputImage.getRGB(x + b, y + a));
							}
						}
					}
				}
				else
				{
					pixelArray[i][j] = new Color(outputImage.getRGB(i, j));
				}

			}
		}
		for (int i = 0; i < outputImage.getWidth(); i++) 
		{
			for (int e = 0; e < outputImage.getHeight(); e++) 
			{
				outputImage.setRGB(i, e, pixelArray[i][e].getRed() << 16 | pixelArray[i][e].getGreen() << 8 | pixelArray[i][e].getBlue());
			}
		}

		return ImageUtil.getImageBytes(outputImage);
	}
	
	private static BufferedImage createRGBImage(byte[] bytes, int width, int height) 
	{
	    DataBufferByte buffer = new DataBufferByte(bytes, bytes.length);
	    ColorModel cm = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[]{8, 8, 8}, false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
	    return new BufferedImage(cm, Raster.createInterleavedRaster(buffer, width, height, width * 3, 3, new int[]{0, 1, 2}, null), false, null);
	}
}
