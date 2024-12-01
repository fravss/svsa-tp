package gaian.svsa.ep.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class CriptografiaUtil {

    private static final String ALGORITMO_AES = "AES";
    private static final String CHAVE = "chaveSecreta1234"; // tem que tirar daqui

  
    public static String descriptografar(String dadosCriptografados) throws Exception {
        SecretKey secretKey = new SecretKeySpec(CHAVE.getBytes(), ALGORITMO_AES);
        Cipher cipher = Cipher.getInstance(ALGORITMO_AES);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] dadosDescriptografados = cipher.doFinal(DatatypeConverter.parseBase64Binary(dadosCriptografados));
        return new String(dadosDescriptografados);
    }
}