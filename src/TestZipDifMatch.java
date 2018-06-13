/**
 * TestZipDifMatch.java
 * author: yujiakui
 * 2017年12月18日
 * 上午10:23:10
 */


import net.sf.json.JSON;
import net.sf.json.JSONObject;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;


/**
 * @author yujiakui
 *
 *         上午10:23:10
 *
 */
public class TestZipDifMatch {

	private diff_match_patch dmp;

	private String fileDir = "src/resource/JSBundle/";





	public TestZipDifMatch(diff_match_patch diff_match_patch){
		this.dmp = diff_match_patch;
	}


	public void test() {



		String zipCnt1;
		String zipCnt2;

		String oldFile;
		String newFile;

		File file = new File(fileDir);
		String[] fileList = file.list();
		int result1 = fileList[0].compareTo(fileList[1]);
		if(result1 > 0){
			oldFile = fileList[1];
			newFile = fileList[0];
			zipCnt1 = FileStringReaderUtil.readContentFromFile(fileDir + fileList[1] + File.separator + Const.BUNDLE_NAME);
			zipCnt2 = FileStringReaderUtil.readContentFromFile(fileDir + fileList[0] + File.separator + Const.BUNDLE_NAME);
		}else{
			oldFile = fileList[0];
			newFile = fileList[1];
			zipCnt1 = FileStringReaderUtil.readContentFromFile(fileDir + fileList[0] + File.separator + Const.BUNDLE_NAME);
			zipCnt2 = FileStringReaderUtil.readContentFromFile(fileDir + fileList[1] + File.separator + Const.BUNDLE_NAME);
		}



		// 读取zip文件中的内容

		// 求差异
		LinkedList<diff_match_patch.Diff> diffFile = dmp.diff_main(zipCnt1, zipCnt2, true);
		System.out.println("---------diff---------");
		System.out.println(diffFile);
		// LinkedList<Patch> patchs = dmp.patch_make(zipCnt1, diffFile);
		LinkedList<diff_match_patch.Patch> patchs = dmp.patch_make(diffFile);
		System.out.println("---------patch-------------");
		System.out.println(patchs);

		/*Patch tempPatch = new Patch();
		tempPatch.diffs = diffFile;
		ArrayList<Patch> tempArrayList = new ArrayList<diff_match_patch.Patch>();
		tempArrayList.add(tempPatch);
		String strTempPatch = dmp.patch_toText(tempArrayList);
		FileStringReaderUtil.writeContentInFile(strTempPatch, fileDir + "patchs_temp.txt");
		*/
		// 将差异写到文件中
		if(!new File("src/resource/diff" + File.separator + newFile).exists()){
			new File("src/resource/diff" + File.separator + newFile).mkdir();
		}
		FileStringReaderUtil.writeContentInFile(dmp.patch_toText(patchs),
				"src/resource/diff" + File.separator + newFile + File.separator + "index.txt");

		try {
			FileUtil.CopyFile(fileDir + newFile + File.separator + "Detail.json",
                    "src/resource/diff" + File.separator + newFile + File.separator + "Detail.json");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 从文件中读取差异文件
		String patchFileCnt = FileStringReaderUtil.readContentFromFile("src/resource/diff" + File.separator + newFile + File.separator + "index.txt");

		List<diff_match_patch.Patch> patchFromFile = dmp.patch_fromText(patchFileCnt);

		// 根据差异文件和原始文件--》合并到最新文件
		String result = dmp.patch_apply(new LinkedList<diff_match_patch.Patch>(patchFromFile),
				zipCnt1)[0].toString();

		// 将result写到压缩文件中
		FileStringReaderUtil.writeContentInFile(result,  "src/resource/merge/" + Const.BUNDLE_NAME);



		try {
			String md5 = MD5Util.getMD5("src/resource/merge/" + Const.BUNDLE_NAME);

			InputStream is = new FileInputStream(new File("src/resource/diff" + File.separator + newFile + File.separator + "Detail.json"));
			InputStreamReader streamReader = new InputStreamReader(is);
			BufferedReader reader = new BufferedReader(streamReader);
			String line;
			StringBuilder stringBuilder = new StringBuilder();
			try {
				while ((line = reader.readLine()) != null) {
					stringBuilder.append(line);
				}
				reader.close();
				reader.close();
				is.close();
				new File("src/resource/diff" + File.separator + newFile + File.separator + "Detail.json").delete();
			} catch (IOException e) {
				e.printStackTrace();
			}

			JSONObject jsonObject = JSONObject.fromObject(stringBuilder.toString());

			jsonObject.put("Bundle", md5);

			FileStringReaderUtil.writeContentInFile(jsonObject.toString(),
					"src/resource/diff" + File.separator + newFile + File.separator + "Detail.json");

			JSONObject jsonObject1 = new JSONObject();
			jsonObject1.put("Bundle", md5);

			FileStringReaderUtil.writeContentInFile(jsonObject1.toString(),
					"src/resource/merge/Detail.json");


		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 将patch文件进行压缩
		/*GZIPOutputStream zipOut = null;
		try {
			zipOut = new GZIPOutputStream(new FileOutputStream(fileDir + "patch.zip"));// 若文件不存在则创建
			ZipEntry entry = new ZipEntry("patch.txt");
			// zipOut.putNextEntry(entry);//
			// 此方法会清空zip文件原来存在的内容，然后创建新的文件1.txt，并将流定位到条目数据的开始处
			zipOut.write(dmp.patch_toText(patchs).getBytes());
			zipOut.finish();
			zipOut.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//mergePatAndAsset();

		/*String md51 = FileStringReaderUtil.readContentFromFile(fileDir + "18050602");
		String md52 = FileStringReaderUtil.readContentFromFile(fileDir + "2_temp.jsbundle");
		if(md51.equals(md52)){
			System.out.println("字符串相同" );
		}*/

		/*try {
			System.out.println("md51:" + MD5Util.getMD5(fileDir + "18050602"));
			System.out.println("md52:" + MD5Util.getMD5(fileDir + "2_temp.jsbundle"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/


		//System.out.println("md51:" + MD5Util.encrypt(md51, 32));
		//System.out.println("md52:" + MD5Util.encrypt(md52, 32));

	}


	private void mergePatAndAsset() {

		String assetsBundle = FileStringReaderUtil.readContentFromFile(fileDir + Const.BUNDLE_NAME);
		String patcheStr = FileStringReaderUtil.readContentFromFile(fileDir + "patchs.txt");
		// 3.初始化 dmp
		diff_match_patch dmp = new diff_match_patch();
		// 4.转换pat
		LinkedList<diff_match_patch.Patch> pathes = (LinkedList<diff_match_patch.Patch>) dmp.patch_fromText(patcheStr);
		// 5.与assets目录下的bundle合并，生成新的bundle
		Object[] bundleArray = dmp.patch_apply(pathes,assetsBundle);
		// 6.保存新的bundle

		BufferedWriter bufferedWriter;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(new File(fileDir + "index3.bundle")));
			bufferedWriter.write(bundleArray[0].toString());
			bufferedWriter.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String bytesRead(String filename) throws IOException {
		String fileDir = "src/resource/diff/";
		InputStream inputStream = new FileInputStream((fileDir + filename));
		byte[] bytes = new byte[1024];
		int i = 0;
		StringBuffer stringBuffer = new StringBuffer();
		while ((i = inputStream.read(bytes)) > 0){
			stringBuffer.append(new String(bytes, 0, i));
		}
		inputStream.close();
		return stringBuffer.toString();
	}

	//移动源文件的detail.json


}
