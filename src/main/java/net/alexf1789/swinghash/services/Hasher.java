package net.alexf1789.swinghash.services;

import java.util.ArrayList;
import java.util.List;

import net.alexf1789.swinghash.models.Resource;

public class Hasher {

    private Resource resource;
    private List<Hash> hashes;
    
    public Hasher(List<String> algorithms, Resource resource) {
        this.resource = resource;
        this.hashes = new ArrayList<Hash>(algorithms.size());
        
        for(String algorithm : algorithms) {
            this.hashes.add(new Hash(resource, algorithm));
        }
    }
    
}
