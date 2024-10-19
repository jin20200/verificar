package com.easyplexdemoapp.data.remote;

import androidx.annotation.NonNull;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import okhttp3.Dns;
import timber.log.Timber;

public class LoggingDns implements Dns {
    @NonNull
    @Override
    public List<InetAddress> lookup(@NonNull String hostname) throws UnknownHostException {
        System.out.println("LoggingDns: Resolving DNS for: " + hostname);
        List<InetAddress> addresses = Dns.SYSTEM.lookup(hostname);
        for (InetAddress address : addresses) {
            System.out.println("LoggingDns: Resolved address: " + address.getHostAddress());
        }
        return addresses;
    }
}