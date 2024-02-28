package pattersonjack.distributedcomputingca.Shared;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

public abstract class AbstractSMPSSLHandler {

    protected SSLSocket getSSLSocket(HostData hostData) {
        SSLContext sslContext = getSSLContext();
        initializeSSLContext(sslContext);

        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        try {
            return (SSLSocket) sslSocketFactory.createSocket(hostData.hostname(), hostData.port());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected SSLServerSocketFactory getSSLServerSocketFactory() {
        SSLContext sslContext = getSSLContext();
        initializeSSLContext(sslContext);

        return sslContext.getServerSocketFactory();
    }

    protected SSLContext getSSLContext() {
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sslContext;
    }

    protected void initializeSSLContext(SSLContext sslContext) {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            try (InputStream trustStoreIS = new FileInputStream("ssl/dcca.jks")) {
                keyStore.load(trustStoreIS, "123456789".toCharArray());
            }

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, "123456789".toCharArray());

            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);
        } catch (KeyStoreException | KeyManagementException | NoSuchAlgorithmException | IOException |
                 CertificateException | UnrecoverableKeyException e) {
            e.printStackTrace();
        }
    }
}
