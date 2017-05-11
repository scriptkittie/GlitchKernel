package io.laniakia.algo;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.concurrent.ThreadLocalRandom;

import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import io.laniakia.util.GlitchTypes;
import io.laniakia.util.ImageUtil;

public class DataAsSound extends GlitchAlgorithm
{
	public DataAsSound()
	{
		setName(GlitchTypes.DATA_AS_SOUND);
		setDescription("Glitch pixels by reading playing and reading through back through distorted WAV audio stream");
	}

	@Override
	public byte[] glitchPixels(byte[] inputImageBytes) throws Exception 
	{
		int audioBitRate = ((Integer) getPixelGlitchParameters().get("bitRateBlend")).intValue();
		float bitRateBlend = (float) audioBitRate / 10;
		if(bitRateBlend < 0.1F || bitRateBlend > 0.9F)
		{
			return null;
		}
		
		BufferedImage inputImage = ImageUtil.getImageFromBytes(inputImageBytes);
		InputStream imageInputStream = new ByteArrayInputStream(inputImageBytes);
		AudioInputStream distortionAudioStream = new AudioInputStream(imageInputStream, new AudioFormat(AudioFormat.Encoding.ULAW, ThreadLocalRandom.current().nextInt(8000,  20000), 8, 5, 9, ThreadLocalRandom.current().nextInt(8000,  20000), true), inputImageBytes.length);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		AudioSystem.write(distortionAudioStream, Type.WAVE, outputStream);
		BufferedImage outputImage = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		byte[] imageData = ((DataBufferByte) outputImage.getRaster().getDataBuffer()).getData();
		System.arraycopy(outputStream.toByteArray(),0,imageData,0,outputStream.toByteArray().length);
		int[] abgrOffsets = {3, 2, 1, 0}; 
		DataBuffer outputBuffer = new DataBufferByte(imageData, imageData.length);
	    WritableRaster raster = Raster.createInterleavedRaster(outputBuffer, inputImage.getWidth(), inputImage.getHeight(), 4 * inputImage.getWidth(), 4, abgrOffsets, null);
	    ColorModel colorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), true, false, Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE);
	    BufferedImage rasterizedImage = new BufferedImage(colorModel, raster, colorModel.isAlphaPremultiplied(), null);
	    rasterizedImage = resizeImage(rasterizedImage, inputImage.getWidth() * 4, inputImage.getHeight() * 4);
	    Graphics2D g2d = rasterizedImage.createGraphics();
	    g2d.setComposite(AlphaComposite.SrcOver.derive(bitRateBlend));
	    g2d.drawImage(inputImage, 0, 0, null);
	    g2d.dispose();
	    rasterizedImage = rasterizedImage.getSubimage(0, 0, inputImage.getWidth(), inputImage.getHeight());
		return ImageUtil.getImageBytes(rasterizedImage);
	}
	
    public BufferedImage resizeImage(BufferedImage inputImage, int width, int height)
    {
        java.awt.Image outputImage = inputImage.getScaledInstance(width, height, BufferedImage.SCALE_AREA_AVERAGING);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2d = bi.createGraphics( );
        g2d.drawImage(outputImage, 0, 0, null);
        g2d.dispose();
        return bi;
    }
}
