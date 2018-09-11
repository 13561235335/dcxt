package cn.wzgzs.springboot.utils;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon; 

public class WaterMarkUtils {
	
	 private static final Integer degree = 0;
	 /**
	  * 指定水印的x轴点位
	  */
	 private static final Integer x = 420;
	 /**
	  * 指定水印的y轴点位
	  */
	 private static final Integer y = 400;
	 /**
	  * 指定图片的宽度
	  */
	 private static final Integer IMAGE_WIDTH = 500;
	 /**
	  * 指定图片的高度
	  */
	 private static final Integer IMAGE_HEIGHT = 500;

	/** 
	 * 把图片印刷到图片上 
	 * 
	 * @param iconPath -- 水印文件 
	 * @param is -- 目标文件 
	 * @param targerPath  保存位置          
	 * @param x  --x坐标 
	 * @param y  --y坐标 
	 */
	public static void markImageByIcon(String iconPath, InputStream is, String targerPath) {
		OutputStream os = null;
		try {
			BufferedImage buffImg = ImageIO.read(is);

			// new一张指定宽高的画布
			BufferedImage newImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, buffImg.getType());
			// 得到画布上的画笔对象
			Graphics2D g = newImage.createGraphics();
			// 将之前的图片画到新画布上
			g.drawImage(buffImg, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, null);
			// 设置对线段的锯齿状边缘处理
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

			Image image = newImage.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
			g.drawImage(image, 0, 0, null);

			if (null != degree) {
				// 设置水印旋转
				g.rotate(Math.toRadians(degree), (double) newImage.getWidth() / 2, (double) newImage.getHeight() / 2);
			}

			// 水印图象的路径 水印一般为gif或者png的，这样可设置透明度
			ImageIcon imgIcon = new ImageIcon(iconPath);

			// 得到Image对象。
			Image img = imgIcon.getImage();

			float alpha = 0.5f; // 透明度
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));

			// 表示水印图片的位置
			g.drawImage(img, x, y, null);

			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

			g.dispose();

			os = new FileOutputStream(targerPath);

			// 生成图片
			ImageIO.write(newImage, "JPG", os);

			System.out.println("图片完成添加Icon。。。。。。");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != is)
					is.close();
				if (null != os)
					os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

//	  /**  
//     * 添加文字水印  
//     * @param targetImg 目标图片路径，如：D://fj.jp  
//     * @param pressText 水印文字， 如:XingKong22star 
//     * @param fontName 字体名称，    如：宋体  
//     * @param fontStyle 字体样式，如：粗体和斜体(Font.BOLD|Font.ITALIC)  
//     * @param fontSize 字体大小，单位为像素  
//     * @param color 字体颜色  
//     * @param x 水印文字距离目标图片左侧的偏移量，如果x<0, 则在正中间  
//     * @param y 水印文字距离目标图片上侧的偏移量，如果y<0, 则在正中间  
//     * @param alpha 透明度(0.0 -- 1.0, 0.0为完全透明，1.0为完全不透明)  
//     */    
//    public static boolean createMark(String filePath,String newFilePath) {  
//        float watermarkAlpha = (float) 0.5;  
//        // 读取原图片  
//        ImageIcon imgIcon = new ImageIcon(filePath);  
//          
//        Image theImg = imgIcon.getImage();  
//          
//        int width = theImg.getWidth(null);  
//        int height = theImg.getHeight(null);  
//  
//        // 创建一个和原图片同大小的新空白图片  
//        BufferedImage bimage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  
//        Graphics2D g = bimage.createGraphics();  
//  
//        // bimage = g.getDeviceConfiguration().createCompatibleImage(width,  
//        // height, Transparency.TRANSLUCENT);  
//        // g.dispose();  
//        // g = bimage.createGraphics();  
//  
//         //设置字体  
//         Font font = new Font("SansSerif", Font.BOLD, 18);  
//         g.setFont(font);  
//        // 设置前景色  
//         g.setColor(Color.white);  
//        // 设置背景色  
//        g.setBackground(Color.white);  
//        // 画原图  
//        g.drawImage(theImg, 0, 0, null);  
//  
//        // 值从0f-1.0f  
//        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, watermarkAlpha));  
//  
//        // 画字  
//         g.drawString("XingKong22star", imgIcon.getIconWidth() - 150 ,imgIcon.getIconHeight() - 20);  
//  
//        // 透明度设置 结束  
//        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));  
//  
//        g.dispose();  
//        FileOutputStream out = null;  
//        try {  
//            String newWaterFile = newFilePath;  
//            out = new FileOutputStream(newWaterFile);  
//            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);  
//            JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bimage);  
//            param.setQuality(50f, true);  
//            encoder.encode(bimage, param);  
//        } catch (Exception e) {  
//            System.out.println("---生成失败---");  
//            return false;  
//        } finally {  
//            if (out != null) {  
//                try {  
//                    out.close();  
//                    out = null;  
//                } catch (Exception e) {  
//                }  
//            }  
//        }  
//        System.out.println("水印文字加载完毕");  
//        return true;  
//    }  
//      
//    /** 
//     * 把图片印刷到图片上 
//     * 
//     * @param iconPath -- 
//     *            水印文件 
//     * @param srcImgPath -- 
//     *            目标文件 
//     * @param targerPath 
//     *            保存位置          
//     * @param x 
//     *            --x坐标 
//     * @param y 
//     *            --y坐标 
//     */  
//    public static void markImageByIcon(String iconPath, String srcImgPath,   
//            String targerPath) {   
//        OutputStream os = null;   
//        try {   
//            Image srcImg = ImageIO.read(new File(srcImgPath));   
//   
//            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null),   
//                    srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);   
//           
//            // 得到画笔对象   
//            // Graphics g= buffImg.getGraphics();   
//            Graphics2D g = buffImg.createGraphics();   
//   
//            // 设置对线段的锯齿状边缘处理   
//            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,   
//                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);   
//   
//            g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg   
//                    .getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);   
//   
//            if (null != degree) {   
//                // 设置水印旋转   
//                g.rotate(Math.toRadians(degree),   
//                        (double) buffImg.getWidth() / 2, (double) buffImg   
//                                .getHeight() / 2);   
//            }   
//   
//            // 水印图象的路径 水印一般为gif或者png的，这样可设置透明度   
//            ImageIcon imgIcon = new ImageIcon(iconPath);   
//   
//            // 得到Image对象。   
//            Image img = imgIcon.getImage();   
//   
//            float alpha = 0.9f; // 透明度   
//            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,   
//                    alpha));   
//   
//            // 表示水印图片的位置   
//            g.drawImage(img, x, y, null);   
//   
//            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));   
//   
//            g.dispose();   
//   
//            os = new FileOutputStream(targerPath);   
//   
//            // 生成图片   
//            ImageIO.write(buffImg, "JPG", os);   
//   
//            System.out.println("图片完成添加Icon。。。。。。");   
//        } catch (Exception e) {   
//            e.printStackTrace();   
//        } finally {   
//            try {   
//                if (null != os)   
//                    os.close();   
//            } catch (Exception e) {   
//                e.printStackTrace();   
//            }   
//        }   
//    }   
   
	public static void main(String[] args) {
//		String iconPath = "E:/test/shuiyin.png";     
//        String srcImgPath = "E:/test/2.jpg";   
//        String targerPath = "E:/test/12.jpg";   
//        // 给图片添加水印   
//        WaterMarkUtils.markImageByIcon(iconPath, srcImgPath, targerPath); 
	}
} 