

import net.sf.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * 生成清单文件
 */
public class DetailUntil {

    public static final String BUNDLE = "index.android.bundle";

    public static final String HDPI = "drawable-hdpi";

    public static final String MDPI = "drawable-mdpi";

    public static final String XHDPI = "drawable-xhdpi";

    public static final String XXHDPI = "drawable-xxhdpi";

    public static final String XXXHDPI = "drawable-xxxhdpi";

    private String fileDir = "src" + File.separator +"resource" + File.separator + "JSBundle" + File.separator;
    public void createDetail(String JSversion) throws IOException, NoSuchAlgorithmException {
        String md5 = MD5Util.getMD5(fileDir + JSversion + File.separator + Const.BUNDLE_NAME);
        String[] hdpi = new File(fileDir + JSversion + File.separator + HDPI).list();
        String[] mdpi = new File(fileDir + JSversion + File.separator + MDPI).list();
        String[] xhdpi = new File(fileDir + JSversion + File.separator + XHDPI).list();
        String[] xxhdpi = new File(fileDir + JSversion + File.separator + XXHDPI).list();
        String[] xxxhdpi = new File(fileDir + JSversion + File.separator + XXXHDPI).list();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Bundle", md5);
        JSONObject resource = new JSONObject();
        resource.put(HDPI, hdpi);
        resource.put(MDPI, mdpi);
        resource.put(XHDPI, xhdpi);
        resource.put(XXHDPI, xxhdpi);
        resource.put(XXXHDPI, xxxhdpi);
        jsonObject.put("Resource", resource);
        FileStringReaderUtil.writeContentInFile(jsonObject.toString(),
                fileDir + JSversion + File.separator + "Detail.json");
    }

    public static void createZipDetail(String path) throws IOException, NoSuchAlgorithmException {
        File file = new File(path);
        JSONObject jsonObject = new JSONObject();
        String[] filelist = file.list();

        for(String fileName : filelist){
            jsonObject.put(fileName, MD5Util.getMD5(path + fileName));
        }
        FileStringReaderUtil.writeContentInFile(jsonObject.toString(),
                path + "ZipDetail.json");
    }

    public void getZipMD5(){
        String zip = FileStringReaderUtil.readContentFromFile("src/resource/1801081.zip");
        try {
            System.out.print(MD5Util.getMD5("src/resource/1801081.zip"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createIOSDetail(String JSversion){
        try {
            String md5 = MD5Util.getMD5("src" + File.separator +"resource" + File.separator  + "JSBundle" + File.separator + JSversion + File.separator + "assets");
            System.out.print(md5);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
