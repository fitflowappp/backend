package com.magpie.fm;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import org.springframework.stereotype.Service;


@Service
public class ImgService {

	private static final int THUMB_SIZE = 198;// 缩略图最短边长
	private static final int LARGE_SIZE = 720;// 大图最短边长

	public static void main(String[] args) {
		ImgService imgServiceImpl = new ImgService();
		ImageFile imageFile = new ImageFile();
		imageFile.setName("3.png");
		imageFile.setPath("d:/");
		imgServiceImpl.cutSizeImg(imageFile);
		imgServiceImpl.compressLargeImg(imageFile);
		imgServiceImpl.compressThumbImg(imageFile);
	}

	static {
		ImageIO.scanForPlugins();
		Iterator<ImageReader> ir = ImageIO.getImageReadersByFormatName("jpeg");
		while (ir.hasNext()) {
			ImageReader r = ir.next();
			System.out.println("can read raster: " + r.canReadRaster());
			System.out.println(r);
		}

	}

	/**
	 * 压缩缩略图
	 * 
	 * @param imgFile
	 */
	public void compressThumbImg(ImageFile imgFile) {
		this.initialSize(THUMB_SIZE, imgFile);
		compressImg(imgFile);
	}

	/**
	 * 压缩大图
	 * 
	 * @param imgFile
	 */
	public void compressLargeImg(ImageFile imgFile) {
		this.initialSize(LARGE_SIZE, imgFile);
		compressImg(imgFile);
	}

	private void initialSize(int size, ImageFile imgFile) {
		File originalFile = new File(imgFile.getPath() + imgFile.getName());
		Dimension dimension = getImageDimension(originalFile);
		if (dimension == null) {
			return;
		}

		double originalW = dimension.getWidth();
		double originalH = dimension.getHeight();
		double originalRatio = originalW / originalH;

		int hSize = 0;
		int wSize = 0;

		if (originalW >= originalH) {
			if (originalH > size) {
				hSize = size;// 高度为最小边
			}
		} else {
			if (originalW > size) {
				wSize = size;// 宽度为最小边
			}
		}

		if (hSize != 0) {
			wSize = (int) (originalRatio * hSize);
		} else if (wSize != 0) {
			hSize = (int) (wSize / originalRatio);
		}

		imgFile.setWidth(wSize);
		imgFile.setHeight(hSize);

	}

	/**
	 * 压缩图片，按指定尺寸压缩
	 * 
	 * @param imgFile
	 */
	public void compressImg(ImageFile imgFile) {

		File originalFile = new File(imgFile.getPath() + imgFile.getName());
		File destinationFile = new File(imgFile.getPath() + imgFile.getWidth() + "x" + imgFile.getHeight()
				+ File.separator + imgFile.getName());
		if (destinationFile.exists()) {
			imgFile.setSize(destinationFile.length());
			return;
		}

		if (!isValid(imgFile)) {
			return;
		}
		destinationFile.getParentFile().mkdirs();

		try {

			Thumbnails.of(originalFile).size(imgFile.getWidth(), imgFile.getHeight()).outputQuality(0.5f)
					.toFile(destinationFile);

			imgFile.setSize(destinationFile.length());

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * 等比压缩后，居中截取相应尺寸的图片
	 * 
	 * @param imgFile
	 */
	public void cutSizeImg(ImageFile imgFile) {

		if (!isValid(imgFile)) {
			return;
		}

		File originalFile = new File(imgFile.getPath() + imgFile.getName());
		File destinationFile = new File(imgFile.getPath() + imgFile.getWidth() + "x" + imgFile.getHeight()
				+ File.separator + imgFile.getName());
		if (destinationFile.exists()) {
			destinationFile.delete();
		}
		destinationFile.getParentFile().mkdirs();

		try {

			Dimension dimension = getImageDimension(originalFile);
			if (dimension == null) {
				return;
			}

			double originalW = dimension.getWidth();
			double originalH = dimension.getHeight();
			double originalRatio = originalW / originalH;
			// int tempS = originalW > originalH ? originalH : originalW;
			double ratio = (double) imgFile.getWidth() / imgFile.getHeight();

			if (originalRatio <= ratio) {
				int h = (int) (imgFile.getHeight() * (originalW / imgFile.getWidth()));
				Thumbnails.of(originalFile).size(imgFile.getWidth(), imgFile.getHeight())
						.sourceRegion(Positions.CENTER, (int) originalW, h).outputQuality(0.5f).toFile(destinationFile);
			} else {
				int w = (int) (imgFile.getWidth() * (originalH / imgFile.getHeight()));
				Thumbnails.of(originalFile).size(imgFile.getWidth(), imgFile.getHeight())
						.sourceRegion(Positions.CENTER, w, (int) originalH).outputQuality(0.5f).toFile(destinationFile);
			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Gets image dimensions for given file
	 * 
	 * @param imgFile
	 *            image file
	 * @return dimensions of image
	 * @throws IOException
	 *             if the file is not a known image
	 */
	public Dimension getImageDimension(File imgFile) {
		int pos = imgFile.getName().lastIndexOf(".");
		if (pos == -1) {
			return null;
		}
		// throw new IOException("No extension for file: "
		// + imgFile.getAbsolutePath());
		String suffix = imgFile.getName().substring(pos + 1);
		Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(suffix);
		if (iter.hasNext()) {
			ImageReader reader = iter.next();
			ImageInputStream stream = null;
			try {
				stream = new FileImageInputStream(imgFile);
				reader.setInput(stream);
				int width = reader.getWidth(reader.getMinIndex());
				int height = reader.getHeight(reader.getMinIndex());
				return new Dimension(width, height);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				reader.dispose();
			}
		}
		return null;

		// throw new IOException("Not a known image file: "
		// + imgFile.getAbsolutePath());
	}

	private boolean isValid(ImageFile imgFile) {
		if (imgFile.getHeight() == 0 || imgFile.getWidth() == 0) {
			return false;
		} else {
			return true;
		}

	}
	
	/**
	 * 从底部截取相应尺寸的图片
	 * 
	 * @param imgFile
	 */
	public boolean cutSizeImgFromBotom(String originfilePath,String newfilePath) {

		File originalFile = new File(originfilePath);
		Dimension dimension = getImageDimension(originalFile);
		if (dimension == null) {
			return false;
		}		
		
		try {
			Path pathDir = Paths.get(newfilePath.substring(0, newfilePath.lastIndexOf("/")));
			if (!Files.exists(pathDir)) {
				Files.createDirectories(pathDir);
			}
			File destinationFile = new File(newfilePath);			
			Thumbnails.of(originalFile).size((int)dimension.getWidth(), (int)dimension.getHeight()).sourceRegion(Positions.BOTTOM_LEFT, (int) dimension.getWidth(),60).outputQuality(0.5f).toFile(destinationFile);
			return true;
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
}
