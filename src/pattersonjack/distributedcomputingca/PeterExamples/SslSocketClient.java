/* SslSocketClient.java
 - Copyright (c) 2014, HerongYang.com, All Rights Reserved.
 */
 /*
 Since my SSL socket server does not require client authentication, we can create a SSL socket client with the default SSL socket factory. Here is my sample program,
 SslSocketClient.java, which can be used to communicate with SslReverseEchoer.java:
 */
import java.io.*;
import java.net.*;
import javax.net.ssl.*;
public class SslSocketClient {
   public static void main(String[] args) {
      BufferedReader in = new BufferedReader(
         new InputStreamReader(System.in));
      PrintStream out = System.out;
      SSLSocketFactory f = 
         (SSLSocketFactory) SSLSocketFactory.getDefault();
      try {
         SSLSocket c =
           (SSLSocket) f.createSocket("localhost", 8888);
         printSocketInfo(c);
         c.startHandshake();
         BufferedWriter w = new BufferedWriter(
            new OutputStreamWriter(c.getOutputStream()));
         BufferedReader r = new BufferedReader(
            new InputStreamReader(c.getInputStream()));
         String m = null;
         while ((m=r.readLine())!= null) {
            out.println(m);
            m = in.readLine();
            w.write(m,0,m.length());
            w.newLine();
            w.flush();
         }
         w.close();
         r.close();
         c.close();
      } catch (IOException e) {
         System.err.println(e.toString());
      }
   }
   private static void printSocketInfo(SSLSocket s) {
      System.out.println("Socket class: "+s.getClass());
      System.out.println("   Remote address = "
         +s.getInetAddress().toString());
      System.out.println("   Remote port = "+s.getPort());
      System.out.println("   Local socket address = "
         +s.getLocalSocketAddress().toString());
      System.out.println("   Local address = "
         +s.getLocalAddress().toString());
      System.out.println("   Local port = "+s.getLocalPort());
      System.out.println("   Need client authentication = "
         +s.getNeedClientAuth());
      SSLSession ss = s.getSession();
      System.out.println("   Cipher suite = "+ss.getCipherSuite());
      System.out.println("   Protocol = "+ss.getProtocol());
   }
}/*Obviously, there is a problem. The TCP/IP layer connection was ok, but the SSL handshake process failed. The error message says that server certificate received has no valid path from any existing certificate authority.

This is an expected error, because the server certificate is a self-signed certificate, not signed by any trusted certificate authorities directly or indirectly. See the next section on how to resolve this problem.
*/
/*Need to make self signed certificate trusted */