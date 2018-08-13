package io.laniakia.algo;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;

public class TestDataAlgorithms
{
	@SuppressWarnings("serial")
	@Test
	public void testFixed() throws Exception
	{
		GlitchController gc = new GlitchController();
		GlitchAlgorithm ga = new PixelSort();
		ga = new PixelSort();
		ga.setPixelGlitchParameters(new HashMap<String, Object>()
		{
			{
				put("brightness", new Integer(20));
				put("distortionLength", new Double(4));
			}
		});
		gc.addAlgorithm(ga);
		BufferedImage img = gc.getGlitchChain(new ByteArrayInputStream(Files.readAllBytes(new File("").toPath())));
		ImageIO.write(img, "png", new File(""));
	}

	@SuppressWarnings("serial")
	@Test
	public void testSimpleRandom() throws Exception
	{
		GlitchController gc = new GlitchController();
		GlitchAlgorithm ga = new HorizontalPixelSort();
		ga.setPixelGlitchParameters(new HashMap<String, Object>()
		{
			{
				put("interval", new Integer(75));
			}
		});
		gc.addAlgorithm(ga);
		BufferedImage img = gc.getGlitchChain(new ByteArrayInputStream(Files.readAllBytes(new File("").toPath())));
		ImageIO.write(img, "png", new File(""));
	}
}
