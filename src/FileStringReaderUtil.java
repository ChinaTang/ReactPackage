/**
 * FileStringReaderUtil.java
 * author: yujiakui
 * 2017年12月18日
 * 上午8:31:25
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;

/**
 * @author yujiakui
 *
 *         上午8:31:25
 *
 *         将文件读成字符串工具
 */
public class FileStringReaderUtil {

	public static String readContentFromFileForBase64(String fileName) {

		Base64 base64 = new Base64();
		byte[] content = new byte[1024];
		String fileStr = "";
		File fil = new File(fileName);

		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(fil);
			while ((fileInputStream.read(content)) != -1) {
				fileStr += (base64.encodeAsString(content));
			}

			fileInputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return fileStr;
	}

	public static void writeContentInFileForBase64(String content, String fileName) {

		Base64 base64 = new Base64();
		byte[] tempContent = base64.decode(content);
		FileOutputStream fileOutputStream;
		try {
			fileOutputStream = new FileOutputStream(new File(fileName));
			fileOutputStream.write(tempContent);
			fileOutputStream.flush();
			fileOutputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void writeContentInFile(String content, String fileName) {

		BufferedWriter bufferedWriter;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(new File(fileName)));
			bufferedWriter.write(content);
			bufferedWriter.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static String readContentFromFile(String fileName) {
		StringBuilder strPatchCnt = new StringBuilder();
		// 从差异文件中读取差异内容
		BufferedReader bufferedReader;
		try {
			bufferedReader = new BufferedReader(new FileReader(new File(fileName)));
			String tempStr = "";
			while ((tempStr = bufferedReader.readLine()) != null) {

				strPatchCnt.append(tempStr);
				strPatchCnt.append("\n");
			}

			bufferedReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return strPatchCnt.toString();
	}

	public static String readContentFromFile1(String fileName) {

		byte[] content = new byte[1024];
		StringBuilder fileStr = new StringBuilder();
		File fil = new File(fileName);
		int b = -1;
		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(fil);
			while ((b = fileInputStream.read(content)) != -1) {
				fileStr.append(new String(content, 0, b));
			}

			fileInputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return fileStr.toString();
	}

	public static void writeContentInFile1(String content, String fileName) {

		byte[] tempContent = content.getBytes();
		FileOutputStream fileOutputStream;
		try {
			fileOutputStream = new FileOutputStream(new File(fileName));
			fileOutputStream.write(tempContent);
			fileOutputStream.flush();
			fileOutputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void writeContentInFileForBase64_1(String content, String fileName) {

		Base64 base64 = new Base64();
		byte[] tempContent = Base64.decodeBase64(content);// base64.decode(content);
		FileOutputStream fileOutputStream;

		try {
			fileOutputStream = new FileOutputStream(new File(fileName));
			fileOutputStream.write(tempContent);
			fileOutputStream.flush();
			fileOutputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static String readContentFromFileForBase64_1(String fileName) {

		Base64 base64 = new Base64();
		byte[] content = new byte[1024];
		String fileStr = "";
		File fil = new File(fileName);
		int b = -1;
		FileInputStream fileInputStream;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try {
			fileInputStream = new FileInputStream(fil);
			while ((b = fileInputStream.read(content)) != -1) {
				// fileStr += (base64.encodeAsString(content));
				byteArrayOutputStream.write(content, 0, b);
			}

			content = byteArrayOutputStream.toByteArray();
			fileStr = Base64.encodeBase64String(content);// .encodeAsString(content);
			byteArrayOutputStream.close();
			fileInputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return fileStr;
	}

}
