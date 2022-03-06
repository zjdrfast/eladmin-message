package me.zhengjie;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

public class tess4j {

    public static void main(String[] args) {
        // 识别图片的路径（修改为自己的图片路径）
        String path = "C:\\Users\\Administrator\\Pictures\\Screenshots\\2.jpg";

        // 语言库位置（修改为跟自己语言库文件夹的路径）
        String lagnguagePath = "E:\\tessdata";

        File file = new File(path);
        ITesseract instance = new Tesseract();

        //设置训练库的位置
        instance.setDatapath(lagnguagePath);

        //chi_sim ：简体中文， eng    根据需求选择语言库
        instance.setLanguage("eng");
        String result = null;
        try {
            long startTime = System.currentTimeMillis();
            result =  instance.doOCR(file);
            long endTime = System.currentTimeMillis();
            System.out.println("Time is：" + (endTime - startTime) + " 毫秒");
        } catch (TesseractException e) {
            e.printStackTrace();
        }

        System.out.println("result: ");
        System.out.println(result);

    }

}
