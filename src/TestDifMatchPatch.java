/**
 * TestDifMatchPatch.java
 * author: yujiakui
 * 2017年12月15日
 * 上午11:37:30
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

/**
 * @author yujiakui
 *
 *         上午11:37:30
 *
 */

public class TestDifMatchPatch {

	private diff_match_patch dmp;
	private diff_match_patch.Operation DELETE = diff_match_patch.Operation.DELETE;
	private diff_match_patch.Operation EQUAL = diff_match_patch.Operation.EQUAL;
	private diff_match_patch.Operation INSERT = diff_match_patch.Operation.INSERT;

	public TestDifMatchPatch(diff_match_patch diff_match_patch){
		dmp = diff_match_patch;
	}


	public void test() throws IOException, ClassNotFoundException {
		// String a =
		// "1234567890\n1234567890\n1234567890\n1234567890\n1234567890\n1234567890\n1234567890\n1234567890\n1234567890\n1234567890\n1234567890\n1234567890\n1234567890\n";
		// String b =
		// "abcdefghij\nabcdefghij\nabcdefghij\nabcdefghij\nabcdefghij\nabcdefghij\nabcdefghij\nabcdefghij\nabcdefghij\nabcdefghij\nabcdefghij\nabcdefghij\nabcdefghijk\n";
		//
		// LinkedList<Diff> diffFile = dmp.diff_main(a, b, true);
		// assertEquals("diff_main: Simple line-mode.", dmp.diff_main(a, b,
		// true),
		// dmp.diff_main(a, b, false));
		// System.out.println(diffFile);
		//
		// LinkedList<Patch> patchs = dmp.patch_make(a, diffFile);
		// System.out.println(patchs);
		// // System.out.println(dmp.patch_toText(patchs));
		//
		// System.out.println(dmp.patch_apply(patchs, a)[0]);
		// String result = dmp.patch_apply(patchs, a)[0].toString();
		// assertEquals(result, b);
		String fileDir = "D:\\Users\\lenovo\\workspace\\MyDiffTest\\src\\main\\java\\com\\test\\yjk\\MyDiffTest\\";

		File fil = new File(fileDir + "1.zip");

		FileInputStream fileInputStream = new FileInputStream(fil);
		Base64 base64 = new Base64();
		byte[] content = new byte[1024];
		int b = 0;
		String fileStr = "";
		while ((b = fileInputStream.read(content)) != -1) {
			fileStr += base64.encodeAsString(content);
		}

		fileInputStream.close();
		System.out.println(fileStr);

		// String strBinCnt = base64.encodeAsString(content);
		System.out.println("######################");
		// System.out.println(strBinCnt);

		byte[] tempContent = base64.decode(fileStr);
		FileOutputStream fileOutputStream = new FileOutputStream(new File(fileDir + "temp1.zip"));
		fileOutputStream.write(tempContent);
		fileOutputStream.flush();
		fileOutputStream.close();

		String fileName1 = "1.txt";
		String fileContent1 = readContentFromFile(fileDir + fileName1);
		System.out.println("file1-------------------------");
		System.out.println(fileContent1);

		String fileName2 = "2.txt";
		String fileContent2 = readContentFromFile(fileDir + fileName2);
		System.out.println("file2-------------------------");
		System.out.println(fileContent2);

		LinkedList<diff_match_patch.Patch> patchs = printPatch(fileContent1, fileContent2);

		// 将差异内容写入到文件中
		BufferedWriter bufferedWriter = new BufferedWriter(
				new FileWriter(new File(fileDir + "t.txt")));
		bufferedWriter.write(dmp.patch_toText(patchs));
		bufferedWriter.close();

		// 从差异文件中读取差异内容
		BufferedReader bufferedReader = new BufferedReader(
				new FileReader(new File(fileDir + "t.txt")));
		StringBuilder strPatchCnt = new StringBuilder();
		String tempStr = "";
		while ((tempStr = bufferedReader.readLine()) != null) {

			strPatchCnt.append(tempStr);
			strPatchCnt.append("\n");
		}

		System.out.println("===================");
		System.out.println(strPatchCnt);
		bufferedReader.close();
		String patchToText = dmp.patch_toText(patchs);
		String patchTextFromFile = strPatchCnt.toString();
		System.out.println(patchToText);

		List<diff_match_patch.Patch> patchFromFile = dmp.patch_fromText(patchTextFromFile);
		System.out.println("patchFromFile---------------------");
		System.out.println(patchFromFile);

		String result = dmp.patch_apply(new LinkedList<diff_match_patch.Patch>(patchFromFile),
				fileContent1)[0].toString();
		System.out.println("result----------------------");
		System.out.println(result);

	}

	public static String readContentFromFile(String fileName) {
		String str = "";
		File file = new File(fileName);
		try {
			FileInputStream in = new FileInputStream(file);
			// size 为字串的长度 ，这里一次性读完
			int size = in.available();
			byte[] buffer = new byte[size];
			in.read(buffer);
			in.close();
			str = new String(buffer, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return str;
	}

	private LinkedList<diff_match_patch.Patch> printPatch(String a, String b) {
		LinkedList<diff_match_patch.Diff> diffFile = dmp.diff_main(a, b, true);

		System.out.println(diffFile);

		LinkedList<diff_match_patch.Patch> patchs = dmp.patch_make(a, diffFile);
		System.out.println(patchs);
		// System.out.println(dmp.patch_toText(patchs));

		return patchs;
	}
}
