package com.sunrt.train.utils;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class SSLUtils {

    public static SSLConnectionSocketFactory trustAllHttpsCertificates() {
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new SSLUtils.TM()}, null);
            return new SSLConnectionSocketFactory(sc, NoopHostnameVerifier.INSTANCE);
        } catch (NoSuchAlgorithmException e) {
        } catch (KeyManagementException e){
        }
        throw new RuntimeException();
    }

    static class TM implements TrustManager, X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {}
        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {}
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
}