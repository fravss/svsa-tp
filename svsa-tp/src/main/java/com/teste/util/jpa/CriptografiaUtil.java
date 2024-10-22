package com.teste.util.jpa;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class CriptografiaUtil {

    private static final String ALGORITMO_AES = "AES";
    private static final String CHAVE = "chaveSecreta1234";

    public static String criptografar(String dados) throws Exception {
        SecretKey secretKey = new SecretKeySpec(CHAVE.getBytes(), ALGORITMO_AES);
        Cipher cipher = Cipher.getInstance(ALGORITMO_AES);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] dadosCriptografados = cipher.doFinal(dados.getBytes());
        return DatatypeConverter.printBase64Binary(dadosCriptografados);
    }

  
    public static String descriptografar(String dadosCriptografados) throws Exception {
        SecretKey secretKey = new SecretKeySpec(CHAVE.getBytes(), ALGORITMO_AES);
        Cipher cipher = Cipher.getInstance(ALGORITMO_AES);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] dadosDescriptografados = cipher.doFinal(DatatypeConverter.parseBase64Binary(dadosCriptografados));
        return new String(dadosDescriptografados);
    }
}