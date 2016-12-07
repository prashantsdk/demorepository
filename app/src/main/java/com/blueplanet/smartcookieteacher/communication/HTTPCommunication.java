package com.blueplanet.smartcookieteacher.communication;


import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by dhanashree.ghayal on 30-06-2015.
 * This class is responsible for sending request to the server and getting response.
 */

public class HTTPCommunication {

    private final static String _TAG = "HTTPCommunication";

    private DefaultHttpClient _httpClient = null;

    private final int CONNECTION_TIMEOUT = 60000;

    public final static int MAX_RETRIES = 2;

    private int _errorCode;

    public final static int RETRY_SLEEP = 8000;

    private HttpUriRequest _httpUriRequest = null;

    /**
     * Private default constructor
     */
    public HTTPCommunication() {
        _httpClient = new DefaultHttpClient();
        // _httpClient = AndroidHttpClient.newInstance( "Android" );
        _httpClient.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT,
                CONNECTION_TIMEOUT);
        _httpClient.getParams().setParameter(HttpConnectionParams.SO_TIMEOUT, CONNECTION_TIMEOUT);

        _httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "GG-MOBILE-APP"/*
                                                                                              * System.
                                                                                              * getProperty
                                                                                              * (
                                                                                              * "http.agent"
                                                                                              * )
                                                                                              */);
    }

    /**
     * Method to perform http communication with some application specific values are to be added to
     * http header
     *
     * @param requestUrl url for communication
     * @param headers    map of key-value pairs for http header
     * @return http response
     */
    public ServerResponse performOperation(int httpRequestType, String requestUrl,
                                           Map<String, String> headers, String requestBody) {

        _httpUriRequest = null;
        String jsonResponse = null;
        ServerResponse serverResponse = null;
        StringEntity se = null;
        try {

            if (requestBody != null) {
                se = new StringEntity(requestBody.toString(), "UTF-8");
            }
            switch (httpRequestType) {

                case HTTPRequestType.HTTP_POST:
                    _httpUriRequest = new HttpPost(new URI(requestUrl));
                    if (se != null) {
                        ((HttpPost) _httpUriRequest).setEntity(se);
                    }
                    break;

                case HTTPRequestType.HTTP_GET:
                    _httpUriRequest = new HttpGet(new URI(requestUrl));
                    break;
            }
            //_httpUriRequest.setHeader( "Content-Type", "application/json" );

            /*
             *
             * //fill request object with the header if(headers != null && headers.size( ) > 0){
             *
             * for (Map.Entry<String, String> entry : headers.entrySet()){ httpUriRequest.setHeader(
             * entry.getKey( ), entry.getValue( )); } }
             */

            serverResponse = communicate(_httpUriRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return serverResponse;
    }

    /**
     * Method to actually send request and retry if it fails.
     * <p/>
     * http request string
     *
     * @return server response if successful, null otherwise.
     * @throws Exception
     */
    private ServerResponse communicate(HttpUriRequest httpUriRequest) throws Exception {

        HttpResponse response = null;
        Exception exception = null;

        String responseJSONString = null;
        ServerResponse serverResponse = null;

        for (int retries = 0; retries < MAX_RETRIES; retries++) {
            try {
                // Log.i( _TAG, "Executing Request" );

                if (retries > 1) {
                    // Log.i( _TAG, "HTTPCommunication.performOperation() Retrying => " +
                    // httpUriRequest.getRequestLine( ) );
                }

                response = _httpClient.execute(httpUriRequest);

                responseJSONString = EntityUtils.toString(response.getEntity(), "UTF-8");

                response.getEntity().consumeContent();

                // Log.i( _TAG, "responseJSONString = " + responseJSONString);

                switch (response.getStatusLine().getStatusCode()) {

                    case HttpStatus.SC_OK:
                        serverResponse =
                                new ServerResponse(com.blueplanet.smartcookieteacher.webservices.WebserviceConstants.SUCCESS, responseJSONString);
                        return serverResponse;

                    default:
                        serverResponse =
                                new ServerResponse(com.blueplanet.smartcookieteacher.webservices.WebserviceConstants.FAILURE, new com.blueplanet.smartcookieteacher.communication.ErrorInfo(
                                        response.getStatusLine().getStatusCode(), null, null));
                        switch (response.getStatusLine().getStatusCode()) {
                            case HttpStatus.SC_REQUEST_TIMEOUT:
                            case HttpStatus.SC_GATEWAY_TIMEOUT:
                                break;

                            default:
                                return serverResponse;
                        }
                        break;
                /*
                 * case HttpStatus.SC_REQUEST_TIMEOUT: case HttpStatus.SC_GATEWAY_TIMEOUT:
                 * responseJSONString = null; break; default: return responseJSONString;
                 */
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Log.i( _TAG, "HTTPCommunication.performOperation() Exception : " + e +
                // " => RequestUrl : " + requestUrl);

                // wait for RETRY_SLEEP time
                if (e instanceof ConnectTimeoutException) {
                    serverResponse =
                            new ServerResponse(com.blueplanet.smartcookieteacher.webservices.WebserviceConstants.FAILURE, new com.blueplanet.smartcookieteacher.communication.ErrorInfo(
                                    HttpStatus.SC_REQUEST_TIMEOUT, null, null));
                } else {
                    serverResponse =
                            new ServerResponse(com.blueplanet.smartcookieteacher.webservices.WebserviceConstants.FAILURE, new com.blueplanet.smartcookieteacher.communication.ErrorInfo(
                                    com.blueplanet.smartcookieteacher.communication.HTTPConstants.HTTP_DESC_NOT_FOUND, null, null));
                }
                Thread.sleep(RETRY_SLEEP);
            } finally {
                _httpClient.getConnectionManager().closeIdleConnections(CONNECTION_TIMEOUT,
                        TimeUnit.MILLISECONDS);
            }
        }
        // throw exception
        if (exception != null) {
            throw exception;
        }
        return serverResponse;

    }

     /** Get the error code received after executing the http request.
     *
     * @return error code
     */
    public int getErrorCode() {
        return _errorCode;
    }

    /**
     * Method to get HTTPClient object for making https connections.
     *
     * @return HTTPClient reference.
     */
    private HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new SWSSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    /**
     * Custom SSLFactory class for accepting SSL certificate from Intelli Vision server.
     */
    private class SWSSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("SSL");

        public SWSSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException,
                KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[]{tm}, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
                throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }


}
