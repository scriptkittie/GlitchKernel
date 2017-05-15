package io.laniakia.util;

public enum GlitchTypes 
{
	DATA_AS_SOUND ("Data-As-Sound"),
	FRACTAL_PIXEL_DISPERSE ("Fractal Pixel Disperse"),
	BIT_SORT ("Bit Sort"),
	VERTICAL_PIXEL_GLITCH ("Vertical Glitch Sort"),
	OFFSET_PIXEL_SORT ("Offset Pixel Sort"),
	GENERIC_PIXEL_SORT_V1 ("Pixel Sort V1"),
	HORIZONTAL_PIXEL_SORT ("Horizontal Pixel Sort"),
	PIXEL_SLICE ("Pixel Slice"),
	RGB_SHIFT_FILTER ("[Filter] RGBShiftFilter"),
	BRIGHTNESS_FILTER ("[Filter] BrightnessFilter");
	
	private final String name;       

    private GlitchTypes(String s)
    {
        name = s;
    }

    public boolean equalsName(String otherName) 
    {
        return name.equals(otherName);
    }

    public String toString() 
    {
       return this.name;
    }
}
