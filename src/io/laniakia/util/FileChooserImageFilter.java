package io.laniakia.util;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class FileChooserImageFilter extends FileFilter 
{
	private String fileExtension = "";
	private String fileExtensionDescription = "";

	public FileChooserImageFilter(String extension) 
	{
		fileExtension = extension;
	}

	public FileChooserImageFilter(String extension, String typeDescription)
	{
		fileExtension = extension;
		this.fileExtensionDescription = typeDescription;
	}

	@Override
	public boolean accept(File inputFile) 
	{
		if (inputFile.isDirectory())
		{
			return true;
		}
		return (inputFile.getName().toLowerCase().endsWith(fileExtension));
	}

	@Override
	public String getDescription() 
	{
		return fileExtensionDescription;
	}
}
