/* * EasyPlex - Movies - Live Streaming - TV Series, Anime * * @author @Y0bEX * @package EasyPlex - Movies - Live Streaming - TV Series, Anime * @copyright Copyright (c) 2021 Y0bEX, * @license http://codecanyon.net/wiki/support/legal-terms/licensing-terms/ * @profile https://codecanyon.net/user/yobex * @link yobexd@gmail.com * @skype yobexd@gmail.com **/

package com.easyplexdemoapp.ui.downloadmanager.core;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSessionContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class TLSSocketFactory extends SSLSocketFactory
{
    private final SSLSocketFactory delegate;

    public TLSSocketFactory() throws KeyManagementException, NoSuchAlgorithmException
    {
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, null, null);
        SSLSessionContext sslSessionContext = context.getServerSessionContext();
        int sessionCacheSize = sslSessionContext.getSessionCacheSize();
        if (sessionCacheSize > 0) {
            sslSessionContext.setSessionCacheSize(0);
        }
        delegate = context.getSocketFactory();
    }

    @Override
    public String[] getDefaultCipherSuites()
    {
        return delegate.getDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites()
    {
        return delegate.getSupportedCipherSuites();
    }

    @Override
    public Socket createSocket() throws IOException
    {
        return enableTLSOnSocket(delegate.createSocket());
    }

    @Override
    public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException
    {
        return enableTLSOnSocket(delegate.createSocket(s, host, port, autoClose));
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException
    {
        return enableTLSOnSocket(delegate.createSocket(host, port));
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException
    {
        return enableTLSOnSocket(delegate.createSocket(host, port, localHost, localPort));
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException
    {
        return enableTLSOnSocket(delegate.createSocket(host, port));
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException
    {
        return enableTLSOnSocket(delegate.createSocket(address, port, localAddress, localPort));
    }

    private Socket enableTLSOnSocket(Socket socket)
    {
        if (socket instanceof SSLSocket)
            ((SSLSocket)socket).setEnabledProtocols(new String[] {"TLSv1.1", "TLSv1.2"});

        return socket;
    }
}