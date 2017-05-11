package io.laniakia.ui;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.laniakia.algo.BitSort;
import io.laniakia.algo.DataAsSound;
import io.laniakia.algo.FractalPixelSort;
import io.laniakia.algo.GlitchAlgorithm;
import io.laniakia.algo.GlitchController;
import io.laniakia.algo.HorizontalPixelSort;
import io.laniakia.algo.OffsetPixelSort;
import io.laniakia.algo.PixelSlice;
import io.laniakia.algo.PixelSort;
import io.laniakia.algo.VerticalPixelGlitch;
import io.laniakia.filter.BrightnessFilter;
import io.laniakia.filter.RGBShiftFilter;
import io.laniakia.util.GlitchTypes;
import io.laniakia.util.ImageUtil;

public class GlitchUIManager 
{
	private static final Logger logger = LogManager.getLogger(GlitchUIManager.class);
	private GlitchArt glitchArt;
	private byte[] modifiedImageBytes;
	private byte[] originalImageBytes;
	private GlitchController glitchController;
	private Map<GlitchTypes, JPanel> jPanelMapping = new HashMap<GlitchTypes, JPanel>();
	private StretchIcon icon;
	
	public GlitchUIManager(GlitchArt glitchArt)
	{
		this.glitchArt = glitchArt;
		this.glitchController = new GlitchController();
	}
	
	public void changeSliderValue(JComponent jComponent)
	{
		if(jComponent instanceof JSlider && !((JSlider) jComponent).getValueIsAdjusting() && this.glitchArt.jAlgorithmList.getSelectedIndex() >= 0)
		{
			refreshSliderValues(true);
		}
		else if(jComponent instanceof JComboBox && this.glitchArt.jAlgorithmList.getSelectedIndex() >= 0)
		{
			refreshSliderValues(true);
		}
	}
	
	public void changeAlgorithmOrder(boolean up)
	{
		if(this.glitchController.getAlgorithmList().isEmpty() || StringUtils.isBlank(this.glitchArt.txtPath.getText()))
		{
			return;
		}
		
		int targetPosition = -1;
		if(up && this.glitchArt.jAlgorithmList.getSelectedIndex() > 0)
		{
			targetPosition = this.glitchArt.jAlgorithmList.getSelectedIndex() - 1;
			refreshPreviewImage();
		}
		else if(!up && this.glitchArt.jAlgorithmList.getSelectedIndex() < this.glitchController.getAlgorithmList().size() - 1)
		{
			targetPosition = this.glitchArt.jAlgorithmList.getSelectedIndex() + 1 ;
			refreshPreviewImage();
		}
		this.glitchController.moveAlgorithmAtIndex(this.glitchArt.jAlgorithmList.getSelectedIndex(), targetPosition);
		this.glitchArt.jAlgorithmList.setListData(glitchController.getGlitchAlgorithmChainList());
		this.glitchArt.jAlgorithmList.setSelectedIndex(targetPosition);
	}
	
	public void addAlgorithm()
	{
		try 
		{
			GlitchTypes glitchType = (GlitchTypes) this.glitchArt.jAlgorithmSelection.getSelectedItem();
			logger.debug("Adding algorithm. GlitchType=" + glitchType.toString());
			if(!this.glitchController.containsAlgorithmOfType(glitchType) && StringUtils.isNotBlank(this.glitchArt.txtPath.getText()))
			{
				this.glitchController.addAlgorithm(glitchType, null);
				this.glitchArt.jAlgorithmList.setListData(glitchController.getGlitchAlgorithmChainList());
				refreshSliderValues(true);
				resetPanelOptions();
			}
		} 
		catch (Exception e)
		{
			logger.debug("Error adding algorithm: " + e.getMessage());
		}
	}
	
	public void removeAlgorithm()
	{
		logger.debug("Removing algorithm...");
		if(this.glitchArt.jAlgorithmList.getSelectedIndex() >= 0)
		{
			this.glitchController.removeAlgorithm(this.glitchArt.jAlgorithmList.getSelectedIndex());
			this.glitchArt.jAlgorithmList.setListData(glitchController.getGlitchAlgorithmChainList());
			refreshSliderValues(true);
			resetPanelOptions();
		}
	}
	
	public void resetUI()
	{
		resetPanelOptions();
		this.glitchArt.txtPath.setText("");
		refreshPreviewImage();
		for(int i = 0; i < this.glitchController.getAlgorithmList().size(); i++)
		{
			this.glitchController.getAlgorithmList().remove(i);
		}
		this.glitchArt.jAlgorithmList.setListData(glitchController.getGlitchAlgorithmChainList());
		this.glitchArt.lblImageRender.setIcon(null);
		this.glitchController = new GlitchController();
		this.glitchArt.selectionPointManager.setGlitchController(this.glitchController);
		this.originalImageBytes = null;
		this.modifiedImageBytes = null;
	}
	
	public void changeAlgorithmSelection()
	{
		if(this.glitchArt.jAlgorithmList.getSelectedIndex() >= 0 && this.glitchArt.jAlgorithmList.getSelectedIndex() < this.glitchController.getAlgorithmList().size())
		{
			resetPanelOptions();
			this.jPanelMapping.get(this.glitchArt.jAlgorithmList.getSelectedValue().getName()).setVisible(true);
			this.jPanelMapping.get(this.glitchArt.jAlgorithmList.getSelectedValue().getName()).setLocation(this.glitchArt.pixelSortPanel.getLocation());
			try 
			{
				setSliderValues(this.glitchArt.jAlgorithmList.getSelectedValue(), false);
			} 
			catch (Exception e)
			{
				logger.debug("Error changing algorithm selection:" + e.getMessage());
			}
		}
	}
	
	public void openFile()
	{
		JFileChooser jFileChooser = new JFileChooser();
		if(StringUtils.isNotBlank(this.glitchArt.txtPath.getText()))
		{
			this.glitchArt.lblImageRender.setIcon(null);
			jFileChooser.setCurrentDirectory(new File(this.glitchArt.txtPath.getText()).getParentFile());
		}
		else
		{
			jFileChooser.setCurrentDirectory(new File("C:\\"));
		}
		
		int result = jFileChooser.showOpenDialog(new JFrame());
		if (result == JFileChooser.APPROVE_OPTION) 
		{
			File selectedFile = jFileChooser.getSelectedFile();
			this.glitchArt.txtPath.setText(selectedFile.getAbsolutePath());
			ImageIcon icon = null;
			try 
			{
				this.glitchController.clearSelectionPoints();
				originalImageBytes = Files.readAllBytes(selectedFile.toPath());
				icon = new StretchIcon(ImageIO.read(new ByteArrayInputStream(originalImageBytes)), this.glitchArt);
				this.glitchArt.lblImageRender.setIcon(icon);
				if(this.glitchController.getAlgorithmList().size() > 0)
				{
					refreshSliderValues(true);
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			} 
		}
	}
	
	public void exportFile()
	{
		if(StringUtils.isNotBlank(this.glitchArt.txtPath.getText()))
		{
			JFileChooser fileChooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Image", "png", "image");
			fileChooser.setFileFilter(filter);
			if (fileChooser.showSaveDialog(new JFrame()) == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				if (!FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("png"))
				{
					file = new File(file.toString() + ".png");
					file = new File(file.getParentFile(), FilenameUtils.getBaseName(file.getName()) + ".png");
				} 

				try 
				{
					BufferedImage outputImage = ImageUtil.getImageFromBytes(modifiedImageBytes);
					ImageIO.write(outputImage, "png", file);
					JOptionPane.showMessageDialog(null, "Exported Image to: " + file.toPath().toString(), "", JOptionPane.INFORMATION_MESSAGE);
				} 
				catch (Exception e1)
				{
					JOptionPane.showMessageDialog(null, "Failed to save image: " + e1.getMessage(), "", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
	}

	private void setSliderValues(GlitchAlgorithm algorithm, boolean refreshImage) throws Exception
	{
		logger.debug("Setting slider values. refreshImage = " + refreshImage + ", algorithm=" + algorithm.getName());
		if(algorithm instanceof PixelSort)
		{
			this.glitchArt.pixelSortBrightness.setValue(((Integer) algorithm.getPixelGlitchParameters().get("brightness")).intValue());
			this.glitchArt.pixelSortLength.setValue(((Double) algorithm.getPixelGlitchParameters().get("distortionLength")).intValue());
		} 
		else if(algorithm instanceof FractalPixelSort)
		{
			this.glitchArt.fractalSortDistortionAmount.setValue(((Integer) algorithm.getPixelGlitchParameters().get("distortionAmount")).intValue());
		}
		else if(algorithm instanceof OffsetPixelSort)
		{
			this.glitchArt.offsetDistortionAmount.setValue(((Integer) algorithm.getPixelGlitchParameters().get("renderBrightness")).intValue());
			this.glitchArt.offsetBrightness.setValue(((Integer) algorithm.getPixelGlitchParameters().get("distortionAmount")).intValue());
			if((Boolean) algorithm.getPixelGlitchParameters().get("scanlines"))
			{
				this.glitchArt.offsetScanLines.setSelectedItem(0);
			}
			else
			{
				this.glitchArt.offsetScanLines.setSelectedItem(1);
			}
		}
		else if(algorithm instanceof PixelSlice)
		{
			this.glitchArt.pixelSliceAmount.setValue(((Integer) algorithm.getPixelGlitchParameters().get("distortionAmount")).intValue());
		}
		else if(algorithm instanceof DataAsSound)
		{
			this.glitchArt.dataAsSoundBitRate.setValue((((Integer) algorithm.getPixelGlitchParameters().get("bitRateBlend")).intValue()));
		}
		else if(algorithm instanceof HorizontalPixelSort)
		{
			this.glitchArt.verticalInterval.setValue((((Integer) algorithm.getPixelGlitchParameters().get("interval")).intValue()));
		}
		else if(algorithm instanceof BrightnessFilter)
		{
			this.glitchArt.brightnessSlider.setValue(((Integer) algorithm.getPixelGlitchParameters().get("renderBrightness")).intValue());						
		}
		else if(algorithm instanceof RGBShiftFilter)
		{
			this.glitchArt.rgbShiftColor.setSelectedIndex(((Integer) algorithm.getPixelGlitchParameters().get("rgbShiftcolor")).intValue());
			this.glitchArt.rgbShiftAmount.setValue(((Integer) algorithm.getPixelGlitchParameters().get("rgbShiftAmount")).intValue());
		}
		else if(algorithm instanceof VerticalPixelGlitch)
		{
			this.glitchArt.randomGlitchDistortion.setValue(((Integer) algorithm.getPixelGlitchParameters().get("distortionAmount")).intValue());
		}
		else if(algorithm instanceof BitSort)
		{
			this.glitchArt.bitSortVerticalInterval.setValue(((Integer) algorithm.getPixelGlitchParameters().get("bitSortVerticalInterval")).intValue());
			this.glitchArt.bitSortHorizontalInterval.setValue(((Integer) algorithm.getPixelGlitchParameters().get("bitSortHorizontalInterval")).intValue());
			this.glitchArt.bitSortDistortion.setValue(((Integer) algorithm.getPixelGlitchParameters().get("bitSortDistortion")).intValue());
		}
		if(refreshImage)
		{
			refreshPreviewImage();
		}
	}

	private void refreshSliderValues(boolean refreshImage)
	{
		logger.debug("Syncing slider values. refreshImage = " + refreshImage + ", algorithms=" + this.glitchController.getAlgorithmList().size());
		for (GlitchAlgorithm algorithm : this.glitchController.getAlgorithmList()) 
		{
			algorithm.setPixelGlitchParameters(new HashMap<String, Object>());
			Map<String, Object> parameterMap = algorithm.getPixelGlitchParameters();
			if(algorithm instanceof PixelSort)
			{
				parameterMap.put("brightness", new Integer(this.glitchArt.pixelSortBrightness.getValue()));
				parameterMap.put("distortionLength", new Double(this.glitchArt.pixelSortLength.getValue()));
			}
			else if(algorithm instanceof FractalPixelSort)
			{
				parameterMap.put("distortionAmount", new Integer(this.glitchArt.fractalSortDistortionAmount.getValue()));
			}
			else if(algorithm instanceof OffsetPixelSort)
			{
				parameterMap.put("distortionAmount", new Integer(this.glitchArt.offsetDistortionAmount.getValue()));
				parameterMap.put("renderBrightness", new Integer(this.glitchArt.offsetBrightness.getValue()));
				parameterMap.put("scanlines", new Boolean(!(this.glitchArt.offsetScanLines.getSelectedIndex() == 0)));
			}
			else if(algorithm instanceof PixelSlice)
			{
				parameterMap.put("distortionAmount", new Integer(this.glitchArt.pixelSliceAmount.getValue()));
			}
			else if(algorithm instanceof BrightnessFilter)
			{	
				parameterMap.put("renderBrightness", new Integer(this.glitchArt.brightnessSlider.getValue()));
			}
			else if(algorithm instanceof RGBShiftFilter)
			{
				parameterMap.put("rgbShiftcolor", new Integer(this.glitchArt.rgbShiftColor.getSelectedIndex()));
				parameterMap.put("rgbShiftAmount", new Integer(this.glitchArt.rgbShiftAmount.getValue()));
			}
			else if(algorithm instanceof DataAsSound)
			{
				parameterMap.put("bitRateBlend", new Integer(this.glitchArt.dataAsSoundBitRate.getValue()));
			}
			else if(algorithm instanceof HorizontalPixelSort)
			{
				parameterMap.put("interval", new Integer(this.glitchArt.verticalInterval.getValue()));
			}
			else if(algorithm instanceof VerticalPixelGlitch)
			{
				parameterMap.put("distortionAmount", new Integer(this.glitchArt.randomGlitchDistortion.getValue()));
			}
			else if(algorithm instanceof BitSort)
			{
				parameterMap.put("bitSortVerticalInterval", new Integer(this.glitchArt.bitSortVerticalInterval.getValue()));
				parameterMap.put("bitSortHorizontalInterval", new Integer(this.glitchArt.bitSortHorizontalInterval.getValue()));
				parameterMap.put("bitSortDistortion", new Integer(this.glitchArt.bitSortDistortion.getValue()));
			}
			algorithm.setPixelGlitchParameters(parameterMap);
		}
		
		if(refreshImage)
		{
			refreshPreviewImage();
		}
	}
	
	public void refreshPreviewImage()
	{
		try 
		{
			if(StringUtils.isNotBlank(this.glitchArt.txtPath.getText()))
			{
				BufferedImage outputImage = this.glitchController.getGlitchChain(new ByteArrayInputStream(originalImageBytes));
				modifiedImageBytes = ImageUtil.getImageBytes(outputImage);
				icon = new StretchIcon(outputImage, this.glitchArt);
				this.glitchArt.lblImageRender.setIcon(icon);
			}
		} 
		catch (Exception e) 
		{
			logger.debug("Error refreshing preview image. e=" + e.getMessage());
		}
	}
	
	private void resetPanelOptions()
	{
		logger.debug("Reseting all panels...");
		this.glitchArt.pixelSortPanel.setVisible(false);
		this.glitchArt.fractalSortPanel.setVisible(false);
		this.glitchArt.offsetSortPanel.setVisible(false);
		this.glitchArt.pixelSlicePanel.setVisible(false);
		this.glitchArt.randomPixelPanel.setVisible(false);
		this.glitchArt.fractalSortPanel.setVisible(false);
		this.glitchArt.dataAsSoundPanel.setVisible(false);
		this.glitchArt.rgbShiftPanel.setVisible(false);
		this.glitchArt.brightnessPanel.setVisible(false);
		this.glitchArt.bitSortPanel.setVisible(false);
		this.glitchArt.verticalPixelSortPanel.setVisible(false);
		this.glitchArt.randomPixelGlitchPanel.setVisible(false);
	}

	public GlitchArt getGlitchArt() {
		return glitchArt;
	}

	public void setGlitchArt(GlitchArt glitchArt) {
		this.glitchArt = glitchArt;
	}

	public Map<GlitchTypes, JPanel> getjPanelMapping() {
		return jPanelMapping;
	}

	public void setjPanelMapping(Map<GlitchTypes, JPanel> jPanelMapping) {
		this.jPanelMapping = jPanelMapping;
	}

	public GlitchController getGlitchController() {
		return glitchController;
	}

	public void setGlitchController(GlitchController glitchController) {
		this.glitchController = glitchController;
	}
}
