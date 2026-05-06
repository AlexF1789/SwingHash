package net.alexf1789.swinghash.services;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import com.google.gson.annotations.Expose;

import net.alexf1789.swinghash.models.Resource;
import net.alexf1789.swinghash.models.StringResource;

public class Hash extends Thread {
    
    private Resource resource;
    private String algorithm;
    private String hash;
    
    public Hash(Resource resource, String algorithm) {
        this.resource = resource;
        this.algorithm = algorithm;
    }
    
    @Override
    public void run() {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            
            // let's compute the digest in both cases of interest
            if(resource instanceof StringResource) {
                byte[] inputString = resource.getResource().getBytes(StandardCharsets.UTF_8);
                messageDigest.update(inputString);
            } else {
                byte[] readBuffer = new byte[8092];
                int count = 0;
                
                try(BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(resource.getResource()))) {
                    while((count = bufferedInputStream.read(readBuffer)) != -1) {
                        messageDigest.update(readBuffer, 0, count);
                    }
                }
            }
            
            // let's export the String containing the computed hash
            StringBuilder hashProduct = new StringBuilder();
            for(byte b : messageDigest.digest())
                hashProduct.append(String.format("%02x", b));
            
            this.hash = hashProduct.toString();
            
        } catch(Exception e) {
            hash = null;
        }
    }

    public Resource getResource() {
        return resource;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public String getHash() {
        return hash;
    }
    

}
