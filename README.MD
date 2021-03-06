# Glitch Kernel

[![Build Status](https://travis-ci.org/scriptkittie/GlitchKernel.svg?branch=master)](https://travis-ci.org/scriptkittie/GlitchKernel) [![codecov](https://codecov.io/gh/scriptkittie/GlitchKernel/branch/master/graph/badge.svg)](https://codecov.io/gh/scriptkittie/GlitchKernel)


## Overview
Glitch Kernel is an art tool for data bending, glitching, and distorting static images through *Pixel Sorting* and other image distortion techniques. The result of distorting an image in these ways is called *Glitch art*. Pixel Sorting algorithms and image distortion techniques **"glitch"** images by rearranging clusters of pixels, blocks of pixels, or individual pixels within an image with respect to one or more arbitrary properties. Some examples of such properties are **hue**, **brightness**, **saturation**, and **vibrance**. Image distortion techniques may also edit the binary data of an image, corrupt bytes of an image, or randomly shift bytes around within the binary data of an image to distort the image.  Some popular pixel sorting algorithms/data bending techniques that use these properties are:

+ ASDFPixelSort
+ Datamoshing
+ Databending
+ 2D Dithering

Glitch Kernel ships with eight unique pixel sorting algorithms that can be applied and stacked together that allows for thousands of unique combinations of pixel sorting that can be applied to an image. The most recent update, 1.1.0, adds support for these algorithms to be applied to GIF images. The algorithms included are:

+ **Data-as-Sound** - An algorithm that glitches images by interpreting as sound. The input image is transformed into a  variable bitrate WAV audio stream with a custom audio codec, overlayed and composited with the original image.

+ **Fractal Pixel Disperse** - Here, the input image is broken down into randomized regions using Möbius transformations, where each regions three different color channels are separated and distorted (commonly known as RGB Shifting). This process is repeated recursively the higher the distortion level.

+ **Bit Sort** - An algorithm for sorting a collection of pixels in arbitrary order. For each pixel in an image, the next pixel is randomly displaced into another location, vertically or horizontally,  on the images (x, y) coordinate system.

+ **Vertical Pixel Sort** - Similar to Bit Sort, pixels within the input image are rearranged, but only vertically. Pixels are grouped into clusters according to their brightness in order to create a vertically displaced effect. Pixels are subtly horizontally distorted. The horizontal direction of the pixel displacement is determined by the brightness of the pixels adjacent to the current pixel analyzed. This process is repeated for each vertical cluster column of pixels in the image.

+ **Offset Pixel Sort** - Image is converted into special byte array format for easier manipulation. Within the image byte array there are four bits of information for each pixel: The red, green, and blue RGB values, and the value of the Alpha channel. Blocks of pixels of arbitrary width and height are replicated and displaced into random parts of image, while simultaneously having the three different color channels of each block randomly RGB shifted up or down. Optional scanlines for this algorithm are generated by looping through each row and drawing a thin black line for every even row of the image.

+ **Pixel Sort Version 1** - Pixels in the source image are randomly shifted up or down according to an arbitrary range that is determined by pixel hue and brightness. Depending on the hue and brightness, the pixels may or may not be RGB shifted.

+ **Horizontal Pixel Sort** - This algorithm loops for a user-defined interval and distorts clusters of pixels horizontally within the image according to completely random math operations. The starting and ending offsets for each pixel cluster are random. The placement of the distorted pixel cluster are also random. and can be placed at any row of the image width.

+ **Pixel Slice** - Clusters of pixels are created by taking random pixels regardless of RGB values throughout the source image and grouping them together in an arbitrary fashion, then displacing them horizontally throughout the image. The result can sometimes be similar to Horizontal Pixel sort, even though the underlying mathematical logic for each algorithm is completely different.

### Filters

Glitch Kernel provides several helpful filters to help improve the results of the pixel sorting algorithms. Depending on the combination of algorithms used together, the resulting image can sometimes have a bad color palette or be to dark or to light. This is where the concept of filters can be used. Glitch Kernel includes two filters:

+ **RGB Shift Filter** - This filter will allow you to enhance either the red, green, or blue color ranges between any two pixel sorting algorithms, or after the end result of an algorithm running. In addition, the digital vibrance of the image can be enhanced if the color range of your image is yielding mediocre results.

+ **Brightness Filter** - This filter increases or lowers the brightness level of the image at any stage in the pixel sorting process.

### Stacking Algorithms
Glitch Kernel provides the ability to combine or *stack* multiple algorithms together to create a unique result. Algorithms are interpreted in the order they are displayed in the list. The order of algorithms can be changed with the arrow keys on the right side of the algorithm list display. The list of glitching algorithms is interpreted from the top-down. The result of an algorithm above another one is piped into the input of the algorithm below it. In this way, you can combine multiple algorithms together. Example: **Offset Pixel Sort + Vertical Pixel Sort** will yield different results than **Vertical Pixel Sort + Offset Pixel Sort**

![alt text](http://i.imgur.com/iYTxvxT.png "Stacking Algorithms and changing Algorithm Order")


### Selection Regions
Glitch Kernel provides the ability to pixel sort only a selected region of an image per algorithm. Once an algorithm has been added to the list of algorithm list, and the algorithm in question has been selected, the ability to drag and select a region of the input image on the right hand of the application will be available. There are two selection modes to select a region, **Freehand** and **Rectangle**. 

+ **Freehand** - Allows for freehand selection of the source image to be selected. The two points must be connected for a freehand selected region to be valid.
+ **Rectangle** - Creates a bounded region that expands and contracts in a rectangular shape.

When the regenerate button is clicked, and for any subsequent pixel sorting actions, only that region of the image will be pixel sorted, for **only** that algorithm. Each algorithm that is added to the list can have its own selection region to pixel sort. Example: **Offset Pixel Sort** and **Data-as-Sound** algorithms are in the list. **Offset Pixel Sort** can have the upper right hand corner of the image pixel sorted, and **Data-as-Sound** can have the lower left hand corner of the image pixel sorted.

#### Note
In order to create a selected with freehand selection, the two endpoints of the shape draw must connect together. When the two endpoints of a freehand region are connected, the shape draw will be rendered in *red*. This signifies that a valid freehand shape has been drawn.

![alt text](http://i.imgur.com/9EdWjOK.png "Freehand Selection with region connected together")

To remove selection region and be able to pixel sort the image in its entirety, click the "Clear Selection" button by the top right of the algorithm list.

![alt text](http://i.imgur.com/6J1lTwM.png "Changing and clearing Selection Regions")

### Regenerating Results
Each pixel sorting algorithm has an arbitrary level of randomness in addition to user-set options. To create a new image result from the randomness of each algorithm, click the "Regenerate" button to loop through each algorithm within the algorithm list and generate a new result.

![alt text](http://i.imgur.com/867iXdx.png "Regenerating results")

### User Options
Glitch Kernel provides user options for each algorithm and filter in addition to arbitrary randomness that exists in each algorithm. Even if user parameters are unchanged, regenerating a new result will yield a different result due to this. Each time a user option is changed, the image will automatically be regenerated with the new user options applied. The user options do apply to selection regions.

### GIF Support
Glitch Kernel now has support for Graphics Interchange Formatted images. The "GIF Speed" option determines the frame delay between GIF frames. When a GIF is exported, the delay between frames will also determine the speed of which the GIF is displayed. This options also determines how fast the algorithm effects appear to be moving on the GIF image. The "Frame" slider controls what frame you are viewing of the GIF. This ability lets you preview what each frame looks like when an algorithm is run on that particular frame. When exporting an image, Glitch Kernel performs exactly as it would for a static image. Adding the extension ".GIF" will export the particular image as a GIF image.

### Exporting Images
Images can be exported to PNG format by clicking on the Export button on the top right of the application. Once the path has been chosen in the dialogue, the file will be saved to that location.


## Installation
To use the application, download a release version and run it as an executable file. The file will be a **.JAR** file.

### Install and Build from Source

Clone from remote repository then `mvn install`. All of the modules will be installed to your local maven repository.

~~~bash
git clone https://github.com/scriptkittie/glitchKernel.git
cd glitchKernel
mvn install
mvn clean compile assembly:single
~~~

### Install from Maven Central

Just add the following dependencies to your maven pom.xml

```xml
<dependency>
    <groupId>io.laniakia</groupId>
    <artifactId>glitchKernel</artifactId>
    <version>1.0.2</version>
</dependency>
```
### Example Usage
**Get the output image result of an algorithm**

```java
import io.laniakia.algo;
...
GlitchController gc = new GlitchController();
//This algorithm can be any of the included algorithms within the io.laniakia.algo package
GlitchAlgorithm ga = new PixelSort();
ga = new PixelSort();
ga.setPixelGlitchParameters(new HashMap<String, Object>() {{
    put("brightness", new Integer(20));
    put("distortionLength",new Double(4));
}});
gc.addAlgorithm(ga);
BufferedImage img = gc.getGlitchChain(new ByteArrayInputStream(Files.readAllBytes(new File("").toPath())));
ImageIO.write(img, "jpg", new File("C:\\testImage.png"));
```


### Changelog

<details> 
  <summary>Open spoiler to view changelog </summary>
  
### 1.0.2
- Freehand selection mode available for drawing of custom selection regions
- Fixed bug where bounds of rectangular selection not render properly
- File selection now  allows for image selection of JPG, JPEG, and PNG format images.
  
### 1.0.1
- Include resources folder in correct location for packed JAR file.
- Fix reset panel bug.
  
### 1.0.0
- Initial release.
</details>

## Issues/Forks
Please report any issues to the issues section & as always if you have any functionality requests go ahead and open an issue containing your suggestions. The goal is to include as many unique and useful pixel sorting algorithms as possible in the program.

If you have an addition to the project, fork it and submit a pull request. Any type of contributions are welcome.

## Credits

Information the wonderful world of Glitch Art: [GlitchEt](http://www.glitchet.com/)

Darryl Burke for [StretchIcon.java](https://tips4java.wordpress.com/2012/03/31/stretch-icon/)

Microsoft for [Color.cs](http://referencesource.microsoft.com/#System.Drawing/commonui/System/Drawing/Color.cs)

### UI Samples

![alt text](https://i.imgur.com/BVpVkhm.png "UI example of Glitch Kernel")

### Static Samples

![alt text](http://i.imgur.com/fm98H5Y.png "Sample image generated with Glitch Kernel")

### GIF Samples

![alt text](https://i.imgur.com/d8vu87S.gif "Sample image generated with Glitch Kernel")
![alt text](https://i.imgur.com/n6bLlKy.gif "Sample image generated with Glitch Kernel")
![alt text](http://i.imgur.com/1VYamKm.gif "Sample image generated with Glitch Kernel")
