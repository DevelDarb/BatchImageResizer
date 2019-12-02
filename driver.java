import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class driver {

// the number you want to down/up size by, multiplicative
static Double ratio = .1;
static ArrayList<BufferedImage> beforeImages = new ArrayList<>();
static ArrayList<BufferedImage> afterImages = new ArrayList<>();
static String importFilePath = "C:\\Users\\Darby\\Desktop\\PictureImport\\";
static File importDirectory = new File(importFilePath);

public static void main(String[] args) throws IOException {

// get the images from the dir
beforeImages = getFilesFromDirectoryAndReturnBufferedImages(importDirectory);
// scale the images
// takes buffered image array, scales it by ratio, then gives back
// buffered image array
afterImages = progressiveScaling(beforeImages);
// write the buffered image array to a A file for each image in the
// specified directory.
saveBufferedImagesToFiles(afterImages);
}

private static void saveBufferedImagesToFiles(ArrayList<BufferedImage> afterImages2) {

for (BufferedImage image : afterImages2) {

for (int i = 0; i <= afterImages2.size(); i++) {

try {
BufferedImage bi = image;
File outputfile = new File(importFilePath + "saved" + i + ".jpg");
ImageIO.write(bi, "jpg", outputfile);
}

catch (IOException e) {

// nada
}
}
}
}

public static ArrayList<BufferedImage> getFilesFromDirectoryAndReturnBufferedImages(File folder)
throws IOException {

ArrayList<BufferedImage> picsToProcess = new ArrayList<BufferedImage>();
File[] listOfFiles = folder.listFiles();

for (File file : listOfFiles) {
if (file.isFile() && !file.getPath().contains("ini")) {
picsToProcess.add(ImageIO.read(file));
}
}

return picsToProcess;
}

private static ArrayList<BufferedImage> progressiveScaling(ArrayList<BufferedImage> beforeImages2) {

ArrayList<BufferedImage> returnList = new ArrayList<>(beforeImages2.size());

for (BufferedImage picture : beforeImages2) {

if (picture != null) {
Integer w = picture.getWidth();
Integer h = picture.getHeight();

// Double ratio = h > w ? w.doubleValue() / h : w.doubleValue()
// / w;

// Multi Step Rescale operation
// This technique is describen in Chris Campbell’s blog The
// Perils
// of Image.getScaledInstance(). As Chris mentions, when
// downscaling
// to something less than factor 0.5, you get the best result by
// doing multiple downscaling with a minimum factor of 0.5 (in
// other
// words: each scaling operation should scale to maximum half
// the
// size).
// while (ratio < 1.0) {
// BufferedImage tmp = scale(picture, 0.5);
// picture = tmp;
// w = picture.getWidth();
// h = picture.getHeight();
// ratio = h > w ? w.doubleValue() / h : w.doubleValue() / w;
// }

BufferedImage after = scale(picture, ratio);
returnList.add(after);
}
}
return returnList;
}

private static Integer getLongestSideLength(ArrayList<BufferedImage> images) {

for (BufferedImage picture : images) {
if (picture.getHeight() > picture.getWidth()) {
return picture.getHeight();
} else {
return picture.getWidth();
}
}

return 1000;
}

private static BufferedImage scale(BufferedImage imageToScale, Double ratio) {
// get the Images length and width and scale them up/down
Integer dWidth = ((Double) (imageToScale.getWidth() * ratio)).intValue();
Integer dHeight = ((Double) (imageToScale.getHeight() * ratio)).intValue();

// create the new image from the scaled length and width
BufferedImage scaledImage = new BufferedImage(dWidth, dHeight, BufferedImage.TYPE_INT_RGB);

// create a graphic
Graphics2D graphics2D = scaledImage.createGraphics();
graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
graphics2D.drawImage(imageToScale, 0, 0, dWidth, dHeight, null);

return scaledImage;
}

public static boolean isValidPath(String path) {
try {
Paths.get(path);
} catch (InvalidPathException | NullPointerException ex) {
return false;
}

return true;
}

}

