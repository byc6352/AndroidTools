package encrypt;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author jqorz
 * @since 2018/8/4
 */
public class AES {
    private static final String TAG = AES.class.getSimpleName();
    private static String mSeed = "dfdas7894513xc21asd878ds4c5x1v32df4g56wr7qw89d43c1324165wef4w";


    /**
     * ���ļ����ܲ����ĺ�׺ΪConsValue.LOCK_EXT
     */
    public static boolean lock(File file) {
        String sourcePath = file.getPath();
        return AESCipher(Cipher.ENCRYPT_MODE, sourcePath, file.getPath(), mSeed);
    }

    public static boolean unlock(File file) {
        String sourcePath = file.getPath();
        return AESCipher(Cipher.DECRYPT_MODE, sourcePath, file.getPath(), mSeed);
    }

    public static boolean AESCipher(int cipherMode, String sourceFilePath,
                                    String targetFilePath, String seed) {
        boolean result = false;
        FileChannel sourceFC = null;
        FileChannel targetFC = null;

        try {

            if (cipherMode != Cipher.ENCRYPT_MODE
                    && cipherMode != Cipher.DECRYPT_MODE) {
                return false;
            }

            Cipher mCipher = Cipher.getInstance("AES/CFB/NoPadding");

            byte[] rawkey = getRawKey(seed);
            File sourceFile = new File(sourceFilePath);
            File targetFile = new File(targetFilePath);

            sourceFC = new RandomAccessFile(sourceFile, "r").getChannel();
            targetFC = new RandomAccessFile(targetFile, "rw").getChannel();

            SecretKeySpec secretKey = new SecretKeySpec(rawkey, "AES");

            mCipher.init(cipherMode, secretKey, new IvParameterSpec(
                    new byte[mCipher.getBlockSize()]));

            ByteBuffer byteData = ByteBuffer.allocate(1024);
            while (sourceFC.read(byteData) != -1) {
                // ͨ��ͨ����д������С�
                // ��������׼��Ϊ���ݴ���״̬
                byteData.flip();

                byte[] byteList = new byte[byteData.remaining()];
                byteData.get(byteList, 0, byteList.length);
                //�˴�������ʹ��������ܽ��ܻ�ʧ�ܣ���Ϊ��byteData�ﲻ��1024��ʱ�����ܷ�ʽ��ͬ�Կհ��ֽڵĴ���Ҳ����ͬ���Ӷ����³ɹ���ʧ�ܡ�
                byte[] bytes = mCipher.doFinal(byteList);
                targetFC.write(ByteBuffer.wrap(bytes));
                byteData.clear();
            }

            result = true;
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException
                | InvalidAlgorithmParameterException
                | IllegalBlockSizeException | BadPaddingException
                | NoSuchPaddingException | InvalidKeySpecException e) {
            e.printStackTrace();
        } finally {
            try {
                if (sourceFC != null) {
                    sourceFC.close();
                }
                if (targetFC != null) {
                    targetFC.close();
                }
            } catch (IOException e) {
                e.printStackTrace();

            }
        }

        return result;
    }

    /**
     * ʹ��һ����ȫ�������������һ���ܳ�,�ܳ׼���ʹ�õ�
     *
     * @param seed ��Կ
     * @return �õ��İ�ȫ��Կ
     */
    private static byte[] getRawKey(String seed) throws NoSuchAlgorithmException, InvalidKeySpecException {

        // ��Կ�ı���λ����ע�������Ǳ���λ��
        // AES ֧�� 128��192 �� 256 ���س��ȵ���Կ
        int keyLength = 256;
        // ��ֵ���ֽ����鳤�ȣ�ע���������ֽ�����ĳ���
        // �䳤��ֵ��Ҫ�������������Կ�ֽ����鳤��һ��
        // ����������Կ�ĳ����� 256 ���أ���������Կ���� 256/8 = 32 λ���ȵ��ֽ��������
        // ������ֵ���ֽ����鳤��ҲӦ���� 32

        // �Ȼ�ȡһ���������ֵ
        // ����Ҫ���˴����ɵ���ֵ���浽�������´��ٴ��ַ���������Կʱ����
        // �����ֵ��һ�½����»������Կֵ��ͬ
        // ������Կ���߼��ٷ���ûд����Ҫ����ʵ��
//        int saltLength = 32;
//        SecureRandom random = new SecureRandom();
//        byte[] salt = new byte[saltLength];
//        random.nextBytes(salt);

        //Ϊ��ʡ�£�ֱ����������ֽ�
        byte[] salt = seed.getBytes();
        // ���������ġ���ֵ��ʹ���µķ���������Կ
        int iterationCount = 1000;
        KeySpec keySpec = new PBEKeySpec(seed.toCharArray(), salt,
                iterationCount, keyLength);
        SecretKeyFactory keyFactory = SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA1");
        // ������������õ�һ����ȫ����Կ��
        byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
        SecretKey key = new SecretKeySpec(keyBytes, "AES");
        return key.getEncoded();
    }


}


