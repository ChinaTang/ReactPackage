import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtil {

    public static final String HDPI = "drawable-hdpi";

    public static final String MDPI = "drawable-mdpi";

    public static final String XHDPI = "drawable-xhdpi";

    public static final String XXHDPI = "drawable-xxhdpi";

    public static final String XXXHDPI = "drawable-xxxhdpi";

    public static final String FILE_PATH = "src/resource/JSBundle/";

    public static final String DIFF_FILE = "src/resource/diff/";


    public static void createDiffFile() throws IOException, NoSuchAlgorithmException {
        File diffile = new File(DIFF_FILE);
        File file = new File(FILE_PATH);

        String[] filelist = file.list();
        String oldfile, newfile;
        int result1 = filelist[0].compareTo(filelist[1]);
        if(result1 > 0){
            oldfile = filelist[1];
            newfile = filelist[0];
        }else{
            oldfile = filelist[0];
            newfile = filelist[1];
        }

        checkRes(FILE_PATH + oldfile + File.separator + HDPI, FILE_PATH + newfile + File.separator + HDPI, HDPI);
        checkRes(FILE_PATH + oldfile + File.separator + MDPI, FILE_PATH + newfile + File.separator + MDPI, MDPI);
        checkRes(FILE_PATH + oldfile + File.separator + XHDPI, FILE_PATH + newfile + File.separator + XHDPI, XHDPI);
        checkRes(FILE_PATH + oldfile + File.separator + XXHDPI, FILE_PATH + newfile + File.separator + XXHDPI, XXHDPI);
        checkRes(FILE_PATH + oldfile + File.separator + XXHDPI, FILE_PATH + newfile + File.separator + XXHDPI, XXHDPI);
    }

    public static void writeResFile(String srcpath, String destpath) throws IOException {
        File file = new File(srcpath);
        InputStream src = new FileInputStream(file);
        File file1 = new File(destpath);
        OutputStream dest = new FileOutputStream(file1);
        byte[] bytes = new byte[1024];
        int length = 0;
        while ((length = (src.read(bytes))) > 0){
            dest.write(bytes, 0, length);
            dest.flush();
        }
        src.close();
        dest.close();
    }

    public static void checkRes(String oldfilepath, String newfilepath, String resFile) throws IOException, NoSuchAlgorithmException {
        File file = new File(FILE_PATH);
        String[] filelist = file.list();
        String oldfile, newfile;
        int result1 = filelist[0].compareTo(filelist[1]);
        if(result1 > 0){
            oldfile = filelist[1];
            newfile = filelist[0];
        }else{
            oldfile = filelist[0];
            newfile = filelist[1];
        }

        File newHDPI = new File(newfilepath);
        File oldHDPI = new File(oldfilepath);
        for(String res : newHDPI.list()){
            if(Arrays.asList(oldHDPI.list()).contains(res)){
                //文件名相同,比较字节
                String oMD5 = MD5Util.getMD5(oldfilepath + File.separator + res);
                String nMD5 = MD5Util.getMD5(newfilepath + File.separator + res);
                if(!oMD5.equals(nMD5)){
                    if(!new File(DIFF_FILE + newfile + File.separator + resFile).exists()){
                        new File(DIFF_FILE + newfile + File.separator + resFile).mkdir();
                    }

                    writeResFile(newfilepath  + File.separator + res
                            , DIFF_FILE + newfile + File.separator + resFile + File.separator + res);
                }
            }else{
                if(!new File(DIFF_FILE + newfile + File.separator + resFile).exists()){
                    new File(DIFF_FILE + newfile + File.separator + resFile).mkdir();
                }

                writeResFile(newfilepath + File.separator  + res
                        , DIFF_FILE + newfile + File.separator + resFile + File.separator + res);
            }
        }
    }


    public static void CopyFile(String srcpath, String destpath) throws IOException {
        File file = new File(srcpath);
        File file1 = new File(destpath);
        InputStream inputStream = new FileInputStream(file);
        OutputStream outputStream = new FileOutputStream(file1);
        int length = 0;
        byte[] bytes = new byte[1024];
        while ((length = inputStream.read(bytes)) > 0){
            outputStream.write(bytes, 0, length);
            outputStream.flush();
        }
        inputStream.close();
        outputStream.close();
    }



    static final int BUFFER = 8192;

    public static void compress(String srcPath , String dstPath) throws IOException{
        File srcFile = new File(srcPath);
        File dstFile = new File(dstPath);
        if (!srcFile.exists()) {
            throw new FileNotFoundException(srcPath + "不存在！");
        }

        FileOutputStream out = null;
        ZipOutputStream zipOut = null;
        try {
            out = new FileOutputStream(dstFile);
            CheckedOutputStream cos = new CheckedOutputStream(out,new CRC32());
            zipOut = new ZipOutputStream(cos);
            String baseDir = "";
            compress(srcFile, zipOut, baseDir);
        }
        finally {
            if(null != zipOut){
                zipOut.close();
                out = null;
            }

            if(null != out){
                out.close();
            }
        }
    }

    private static void compress(File file, ZipOutputStream zipOut, String baseDir) throws IOException{
        if (file.isDirectory()) {
            compressDirectory(file, zipOut, baseDir);
        } else {
            compressFile(file, zipOut, baseDir);
        }
    }

    /** 压缩一个目录 */
    private static void compressDirectory(File dir, ZipOutputStream zipOut, String baseDir) throws IOException{
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            compress(files[i], zipOut, baseDir + dir.getName() + "/");
        }
    }

    /** 压缩一个文件 */
    private static void compressFile(File file, ZipOutputStream zipOut, String baseDir)  throws IOException{
        if (!file.exists()){
            return;
        }

        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            ZipEntry entry = new ZipEntry(baseDir + file.getName());
            zipOut.putNextEntry(entry);
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = bis.read(data, 0, BUFFER)) != -1) {
                zipOut.write(data, 0, count);
            }

        }finally {
            if(null != bis){
                bis.close();
            }
        }
    }


    //清除生成文件
    /**
     * 删除文件
     * @param file
     */
    public static void deleteFile(File file) {
        if (file.exists()) {//判断文件是否存在
            if (file.isFile()) {//判断是否是文件
                file.delete();//删除文件
            } else if (file.isDirectory()) {//否则如果它是一个目录
                File[] files = file.listFiles();//声明目录下所有的文件 files[];
                for (int i = 0;i < files.length;i ++) {//遍历目录下所有的文件
                    deleteFile(files[i]);//把每个文件用这个方法进行迭代
                }
                file.delete();//删除文件夹
            }
        } else {
            System.out.println("所删除的文件不存在");
        }
    }

    public static void deleteCreateFile(){
        File diff = new File("src/resource/diff/");
        File diffzip = new File("src/resource/diffzip/");
        File merge = new File("src/resource/merge/");
        File zip = new File("src/resource/zip/");
        deleteFile(diff);
        deleteFile(diffzip);
        deleteFile(merge);
        deleteFile(zip);

        diff.mkdir();
        diffzip.mkdir();
        merge.mkdir();
        zip.mkdir();

    }



}
