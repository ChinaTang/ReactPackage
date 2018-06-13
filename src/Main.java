import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class Main {

    public static void main(String[] args){


        FileUtil.deleteCreateFile();

        //生成清单

        createDetail();

        //生成差异文件
        TestZipDifMatch testZipDifMatch = new TestZipDifMatch(new diff_match_patch());
        testZipDifMatch.test();



        //生成差异图图片
        try {
            FileUtil.createDiffFile();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //压缩差异文件
        File file = new File("src/resource/diff/");
        String[] filelist = file.list();
        try {
            OutputStream outputStream = new FileOutputStream("src/resource/diffzip/" + filelist[0] + ".zip");
            FileUtil.compress( "src/resource/diff/" + filelist[0], "src/resource/diffzip/" + filelist[0] + ".zip");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //压缩源文件
        File file1 = new File("src/resource/JSBundle/");
        String[] filelist1 = file1.list();
        for(String name : filelist1){
            try {
                OutputStream outputStream = new FileOutputStream("src/resource/zip/" + name + ".zip");
                FileUtil.compress("src/resource/JSBundle/" + name, "src/resource/zip/" + name + ".zip");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //生成压缩文件MD5
        try {
            DetailUntil.createZipDetail("src/resource/diffzip/");
            DetailUntil.createZipDetail("src/resource/zip/");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }




    }
    public static void iso(){
        new DetailUntil().createIOSDetail("ios");

    }

    /**
     * Android 差异文件打包，Zip MD5生成
     */
    public static void createDiffZip(){
        TestZipDifMatch testZipDifMatch = new TestZipDifMatch(new diff_match_patch());
        testZipDifMatch.test();
        fileToZip("src/resource/merge", "src/resource/merge", "diff");
    }


    /**
     *
     *生成清单文件
     */
    public static void createDetail(){
        try {
            File file = new File("src" + File.separator +"resource" + File.separator + "JSBundle");
            String[] filelist = file.list();
            for(String filepath : filelist){
                new DetailUntil().createDetail(filepath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成zipMD5
     */
    public static void createZipJSON(){
        try {
            File file = new File("src" + File.separator +"resource" + File.separator + "JSBundle" + File.separator + "zip");
            String[] filelist = file.list();
            for(String filepath : filelist){
                new DetailUntil().createDetail(filepath);

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    public static void filePackage(){
        String src = "src/resource/JSBundle/";
        String dest = "src/resource/zip/";
        File file = new File(src);
        String[] filelist = file.list();
        for(String fileName : filelist){
            fileToZip(src + fileName, dest, fileName);
        }
    }


    /**
     * zip打包
     * @param sourceFilePath
     * @param zipFilePath
     * @param fileName
     * @return
     */
    public static boolean fileToZip(String sourceFilePath,String zipFilePath,String fileName){
        boolean flag = false;
        File sourceFile = new File(sourceFilePath);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        ZipOutputStream zos = null;

        if(sourceFile.exists() == false){
            System.out.println("待压缩的文件目录："+sourceFilePath+"不存在.");
        }else{
            try {
                File zipFile = new File(zipFilePath  + fileName +".zip");
                if(zipFile.exists()){
                    System.out.println(zipFilePath + "目录下存在名字为:" + fileName +".zip" +"打包文件.");
                }else{
                    File[] sourceFiles = sourceFile.listFiles();
                    if(null == sourceFiles || sourceFiles.length<1){
                        System.out.println("待压缩的文件目录：" + sourceFilePath + "里面不存在文件，无需压缩.");
                    }else{
                        fos = new FileOutputStream(zipFile);
                        zos = new ZipOutputStream(new BufferedOutputStream(fos));
                        byte[] bufs = new byte[1024*10];
                        for(int i=0;i<sourceFiles.length;i++){
                            //创建ZIP实体，并添加进压缩包
                            ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
                            zos.putNextEntry(zipEntry);
                            //读取待压缩的文件并写进压缩包里
                            fis = new FileInputStream(sourceFiles[i]);
                            bis = new BufferedInputStream(fis, 1024*10);
                            int read = 0;
                            while((read=bis.read(bufs, 0, 1024*10)) != -1){
                                zos.write(bufs,0,read);
                            }
                        }
                        flag = true;
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally{
                //关闭流
                try {
                    if(null != bis) bis.close();
                    if(null != zos) zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
        return flag;
    }

    public static void diffZip(){
        String fileDir = "src/resource/diff/";

        String fileName;
        File file = new File(fileDir);
        String[] fileList = file.list();
        int result1 = fileList[0].compareTo(fileList[1]);
        if(result1 > 0){
            fileName = fileList[0];
        }else{
            fileName = fileList[1];
        }

        File detail = new File("src/resource/JSBundle/" + fileName + File.separator + "Detail.json");
        File dest = new File("src/resource/merge/Detail.json");
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(detail);
            outputStream = new FileOutputStream(dest);
            byte[] bytes = new byte[1024];
            while (inputStream.read(bytes) > 0){
                outputStream.write(bytes);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        fileToZip("src/resource/merge", "src/resource/diffzip/", fileName);

    }



}
