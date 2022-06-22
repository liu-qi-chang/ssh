package src.main.java.com.captcha;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CaptchaRecognizer {

    public static void main(String[] args) throws IOException
    {
//        HttpClient httpClient = new HttpClient();
//        GetMethod getMethod = new GetMethod("https://img2020.cnblogs.com/blog/1039974/202011/1039974-20201119224011928-1654538410.png"); // 验证码链接
//        for (int i = 0; i < 5; i++) {
//            try {
//                // 执行get请求
//                int statusCode = httpClient.executeMethod(getMethod);
//                if (statusCode != HttpStatus.SC_OK) {
//                    System.err.println("Method failed: " + getMethod.getStatusLine());
//                } else {
//                    File captcha = File.createTempFile("ybt", ".png");
//                    OutputStream outStream = new FileOutputStream(captcha);
//                    InputStream inputStream = getMethod.getResponseBodyAsStream();
//                    IOUtils.copy(inputStream, outStream);
//                    outStream.close();
//
//                    BufferedImage image = ImageIO.read(captcha);
//                    System.out.println("======= print and recognize captchapic  =======");
//                    printImage(image);
//                    System.out.printf("recognize: %s\n", recognizeCaptcha(image));
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                // 释放连接
//                getMethod.releaseConnection();
//            }
//        }
        InputStream inputStream = CaptchaRecognizer.class.getClassLoader().getResourceAsStream("img.png");
        assert inputStream != null;
        BufferedImage image = ImageIO.read(inputStream);
        System.out.println("======= print and recognize captchapic  =======");
        printImage(image);
        System.out.printf("recognize: %s\n", recognizeCaptcha(image));
    }

    /**
     * @param colorInt 像素点的RGB值
     * @return
     */
    private static boolean isBlack(int colorInt) {
        Color color = new Color(colorInt);
        if (color.getRed() + color.getGreen() + color.getBlue() <= 10) {
            return true;
        }
        return false;
    }

    /**
     * @param image 需要打印的图像
     * @throws IOException
     */
    private static void printImage(BufferedImage image) {
        int h = image.getHeight();
        int w = image.getWidth();

        // 矩阵打印
        for (int y = 0; y < h; y++) {
            System.out.printf("\"");
            for (int x = 0; x < w; x++) {
                if (isBlack(image.getRGB(x, y))) {
                    System.out.print("#");
                } else {
                    System.out.print(".");
                }
            }
            System.out.printf("%s", y == h-1 ? "\"" : "\",");
            System.out.println();
        }
    }

    /**
     * @param image 待识别的符号图片
     * @return
     */
    private static char recognizeSymbol(BufferedImage image) {
        int h = image.getHeight();
        int w = image.getWidth();

        int minDiff = 999999;
        char symAns = 0;
        // 对于某个给定数值
        for (int i = 0; i < 10; i++) {
            int curDiff = 0;
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    boolean pixel1 = digitals[i][y].charAt(x) == '#';
                    boolean pixel2 = isBlack(image.getRGB(x, y));
                    if (pixel1 != pixel2) {
                        ++curDiff;
                    }
                }
            }
            if (curDiff < minDiff) {
                minDiff = curDiff;
                symAns = (char) ('0' + i);
            }
            if (minDiff == 0) {
                return symAns;
            }
        }

        // 对于某个给定字母
        for (int i = 0; i < 26; i++) {
            int curDiff = 0;
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    boolean pixel1 = alphas[i][y].charAt(x) == '#';
                    boolean pixel2 = isBlack(image.getRGB(x, y));
                    if (pixel1 != pixel2) {
                        ++curDiff;
                    }
                }
            }
            if (curDiff < minDiff) {
                minDiff = curDiff;
                symAns = (char) ('a' + i);
            }
            if (minDiff == 0) {
                return symAns;
            }
        }

        return symAns;
    }

    /**
     * @param image 需要被分割的验证码
     * @return
     */
    private static List<BufferedImage> splitImage(BufferedImage image) {
        List<BufferedImage> subImgs = new ArrayList<BufferedImage>();
        subImgs.add(image.getSubimage(10, 3, 8, 12));
        subImgs.add(image.getSubimage(19, 3, 8, 12));
        subImgs.add(image.getSubimage(28, 3, 8, 12));
        subImgs.add(image.getSubimage(37, 3, 8, 12));
        return subImgs;
    }

    /**
     * @param image 待识别的验证码
     * @return
     */
    public static String recognizeCaptcha(BufferedImage image) {
        StringBuilder ans = new StringBuilder();

        List<BufferedImage> subImgs = splitImage(image);
        for (BufferedImage subImg : subImgs) {
            // 依次识别子图片
            ans.append(recognizeSymbol(subImg));
        }
        return ans.toString();
    }

    private static String[][] digitals = new String[][]{
            {
                    "........",
                    "........",
                    "........",
                    "........",
                    "........",
                    "........",
                    "........",
                    "........",
                    "........",
                    "........",
                    "........",
                    "........"
            },
            {
                    "........",
                    "........",
                    "........",
                    "........",
                    "........",
                    "........",
                    "........",
                    "........",
                    "........",
                    "........",
                    "........",
                    "........"
            },
            {
                    "..####..",
                    ".##..##.",
                    "##....##",
                    "......##",
                    ".....##.",
                    "....##..",
                    "...##...",
                    "..##....",
                    ".##.....",
                    "########",
                    "........",
                    "........"
            },
            {
                    ".#####..",
                    "##...##.",
                    "......##",
                    ".....##.",
                    "...###..",
                    ".....##.",
                    "......##",
                    "......##",
                    "##...##.",
                    ".#####..",
                    "........",
                    "........"
            },
            {
                    ".....##.",
                    "....###.",
                    "...####.",
                    "..##.##.",
                    ".##..##.",
                    "##...##.",
                    "########",
                    ".....##.",
                    ".....##.",
                    ".....##.",
                    "........",
                    "........"
            },
            {
                    "#######.",
                    "##......",
                    "##......",
                    "##.###..",
                    "###..##.",
                    "......##",
                    "......##",
                    "##....##",
                    ".##..##.",
                    "..####..",
                    "........",
                    "........"
            },
            {
                    "..####..",
                    ".##..##.",
                    "##....#.",
                    "##......",
                    "##.###..",
                    "###..##.",
                    "##....##",
                    "##....##",
                    ".##..##.",
                    "..####..",
                    "........",
                    "........"
            },
            {
                    "########",
                    "......##",
                    "......##",
                    ".....##.",
                    "....##..",
                    "...##...",
                    "..##....",
                    ".##.....",
                    "##......",
                    "##......",
                    "........",
                    "........"
            },
            {
                    "..####..",
                    ".##..##.",
                    "##....##",
                    ".##..##.",
                    "..####..",
                    ".##..##.",
                    "##....##",
                    "##....##",
                    ".##..##.",
                    "..####..",
                    "........",
                    "........"
            },
            {
                    "..####..",
                    ".##..##.",
                    "##....##",
                    "##....##",
                    ".##..###",
                    "..###.##",
                    "......##",
                    ".#....##",
                    ".##..##.",
                    "..####..",
                    "........",
                    "........"
            }
    };

    private static String[][] alphas = new String[][]{
            {
                    "........",
                    "........",
                    "........",
                    "..#####.",
                    ".##...##",
                    "......##",
                    ".#######",
                    "##....##",
                    "##...###",
                    ".####.##",
                    "........",
                    "........"
            },
            {
                    "##......",
                    "##......",
                    "##......",
                    "##.###..",
                    "###..##.",
                    "##....##",
                    "##....##",
                    "##....##",
                    "###..##.",
                    "##.###..",
                    "........",
                    "........"
            },
            {
                    "........",
                    "........",
                    "........",
                    "..#####.",
                    ".##...##",
                    "##......",
                    "##......",
                    "##......",
                    ".##...##",
                    "..#####.",
                    "........",
                    "........"
            },
            {
                    "......##",
                    "......##",
                    "......##",
                    "..###.##",
                    ".##..###",
                    "##....##",
                    "##....##",
                    "##....##",
                    ".##..###",
                    "..###.##",
                    "........",
                    "........"
            },
            {
                    "........",
                    "........",
                    "........",
                    "..####..",
                    ".##..##.",
                    "##....##",
                    "########",
                    "##......",
                    ".##...##",
                    "..#####.",
                    "........",
                    "........"
            },
            {
                    "...####.",
                    "..##..##",
                    "..##..##",
                    "..##....",
                    "..##....",
                    "######..",
                    "..##....",
                    "..##....",
                    "..##....",
                    "..##....",
                    "........",
                    "........"
            },
            {
                    "........",
                    "........",
                    "........",
                    ".#####.#",
                    "##...###",
                    "##...##.",
                    "##...##.",
                    ".#####..",
                    "##......",
                    ".######.",
                    "##....##",
                    ".######."
            },
            {
                    "##......",
                    "##......",
                    "##......",
                    "##.###..",
                    "###..##.",
                    "##....##",
                    "##....##",
                    "##....##",
                    "##....##",
                    "##....##",
                    "........",
                    "........"
            },
            {
                    "...##...",
                    "...##...",
                    "........",
                    "..###...",
                    "...##...",
                    "...##...",
                    "...##...",
                    "...##...",
                    "...##...",
                    ".######.",
                    "........",
                    "........"
            },
            {
                    ".....##.",
                    ".....##.",
                    "........",
                    "....###.",
                    ".....##.",
                    ".....##.",
                    ".....##.",
                    ".....##.",
                    ".....##.",
                    "##...##.",
                    "##...##.",
                    ".#####.."
            },
            {
                    ".##.....",
                    ".##.....",
                    ".##.....",
                    ".##..##.",
                    ".##.##..",
                    ".####...",
                    ".####...",
                    ".##.##..",
                    ".##..##.",
                    ".##...##",
                    "........",
                    "........"
            },
            {
                    "........",
                    "........",
                    "........",
                    "........",
                    "........",
                    "........",
                    "........",
                    "........",
                    "........",
                    "........",
                    "........",
                    "........"
            },
            {
                    "........",
                    "........",
                    "........",
                    "#.##.##.",
                    "##.##.##",
                    "##.##.##",
                    "##.##.##",
                    "##.##.##",
                    "##.##.##",
                    "##.##.##",
                    "........",
                    "........"
            },
            {
                    "........",
                    "........",
                    "........",
                    "##.###..",
                    "###..##.",
                    "##....##",
                    "##....##",
                    "##....##",
                    "##....##",
                    "##....##",
                    "........",
                    "........"
            },
            {
                    "........",
                    "........",
                    "........",
                    "........",
                    "........",
                    "........",
                    "........",
                    "........",
                    "........",
                    "........",
                    "........",
                    "........"
            },
            {
                    "........",
                    "........",
                    "........",
                    "##.###..",
                    "###..##.",
                    "##....##",
                    "##....##",
                    "##....##",
                    "###..##.",
                    "##.###..",
                    "##......",
                    "##......"
            },
            {
                    "........",
                    "........",
                    "........",
                    "..###.##",
                    ".##..###",
                    "##....##",
                    "##....##",
                    "##....##",
                    ".##..###",
                    "..###.##",
                    "......##",
                    "......##"
            },
            {
                    "........",
                    "........",
                    "........",
                    "##.####.",
                    ".###..##",
                    ".##.....",
                    ".##.....",
                    ".##.....",
                    ".##.....",
                    ".##.....",
                    "........",
                    "........"
            },
            {
                    "........",
                    "........",
                    "........",
                    ".######.",
                    "##....##",
                    "##......",
                    ".######.",
                    "......##",
                    "##....##",
                    ".######.",
                    "........",
                    "........"
            },
            {
                    "........",
                    "..##....",
                    "..##....",
                    "######..",
                    "..##....",
                    "..##....",
                    "..##....",
                    "..##....",
                    "..##..##",
                    "...####.",
                    "........",
                    "........"
            },
            {
                    "........",
                    "........",
                    "........",
                    "##....##",
                    "##....##",
                    "##....##",
                    "##....##",
                    "##....##",
                    ".##..###",
                    "..###.##",
                    "........",
                    "........"
            },
            {
                    "........",
                    "........",
                    "........",
                    "##....##",
                    "##....##",
                    ".##..##.",
                    ".##..##.",
                    "..####..",
                    "..####..",
                    "...##...",
                    "........",
                    "........"
            },
            {
                    "........",
                    "........",
                    "........",
                    "##....##",
                    "##....##",
                    "##.##.##",
                    "##.##.##",
                    "##.##.##",
                    "########",
                    ".##..##.",
                    "........",
                    "........"
            },
            {
                    "........",
                    "........",
                    "........",
                    "##....##",
                    ".##..##.",
                    "..####..",
                    "...##...",
                    "..####..",
                    ".##..##.",
                    "##....##",
                    "........",
                    "........"
            },
            {
                    "........",
                    "........",
                    "........",
                    "##....##",
                    "##....##",
                    "##....##",
                    "##....##",
                    "##....##",
                    ".##..###",
                    "..###.##",
                    "#.....##",
                    ".######."
            },
            {
                    "........",
                    "........",
                    "........",
                    ".######.",
                    ".....##.",
                    "....##..",
                    "...##...",
                    "..##....",
                    ".##.....",
                    ".######.",
                    "........",
                    "........"
            }
    };
}
