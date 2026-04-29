package net.alexf1789.swinghash;

import java.util.Map.Entry;

import net.alexf1789.swinghash.models.StringResource;
import net.alexf1789.swinghash.services.Hasher;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        String[] algos = {"SHA-256", "SHA-512", "MD5", "SHA-3", "SHA-1", "CR32"};
        StringResource sr = new StringResource("Gianni");
        Hasher hasher = new Hasher(algos, sr);
        
        for(Entry<String, String> hash : hasher.getHashes().entrySet()) {
            System.out.println(String.format("%s: %s", hash.getKey(), hash.getValue()));
        }
    }

}
