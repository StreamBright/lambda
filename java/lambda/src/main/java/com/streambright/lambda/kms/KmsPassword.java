package com.streambright.lambda.kms;
//
//import com.amazonaws.services.kms.AWSKMS;
//import com.amazonaws.services.kms.AWSKMSClientBuilder;
//import com.amazonaws.services.kms.model.DecryptRequest;
//import com.amazonaws.util.Base64;
//
//import java.nio.ByteBuffer;
//import java.nio.charset.Charset;
//
//public class KmsPassword {
//
//    public static String decryptKey(String name) {
//
//        byte[] encryptedKey = Base64.decode(System.getenv(name));
//        System.out.println("encryptedKey " + encryptedKey);
//        AWSKMS client = AWSKMSClientBuilder.defaultClient();
//        System.out.println("client " + client);
//        DecryptRequest request = new DecryptRequest().withCiphertextBlob(ByteBuffer.wrap(encryptedKey));
//        System.out.println("request " + request);
//        ByteBuffer plainTextKey = client.decrypt(request).getPlaintext();
//        System.out.println("plainTextKey " + plainTextKey);
//
//        return new String(plainTextKey.array(), Charset.forName("UTF-8"));
//    }
//}
//
