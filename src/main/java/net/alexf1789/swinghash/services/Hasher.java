package net.alexf1789.swinghash.services;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import net.alexf1789.swinghash.models.Resource;

public class Hasher {

    private Resource resource;
    private Map<String, Hash> hashes;
    private boolean computed;
    
    public Hasher(Collection<String> algorithms, Resource resource) {
        this.computed = false;
        this.resource = resource;
        this.hashes = new HashMap<String, Hash>(algorithms.size());
        
        for(String algorithm : algorithms) {
            this.hashes.put(algorithm, new Hash(resource, algorithm));
        }
    }
    
    public Hasher(String[] algorithms, Resource resource) {
        this(Arrays.asList(algorithms), resource);
    }
    
    /**
     * Computes the hash in a parallel way and waits for its termination
     * 
     * @throws InterruptedException if the process is interrupted while threads are still working
     */
    public void compute() throws InterruptedException {
        this.computed = true;
        
        // let's start the various threads
        for(Hash hash : hashes.values())
            hash.start();
        
        // let's join them
        for(Hash hash : hashes.values())
            hash.join();
        
        this.hashes = this.hashes.entrySet().stream()
                .filter(entry -> entry.getValue().getHash() != null)
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }
    
    /**
     * Returns (and computes if necessary) the hashes and returns them as a map
     * 
     * @return a Map associating each hash to the relative result
     * @throws InterruptedException if the process is interrupted while threads are still working during the hash computation
     */
    public Map<String, String> getHashes() throws InterruptedException {
        // let's check if the hashes have been computed
        if(!this.computed)
            compute();
        
        return hashes.entrySet().stream()
                .collect(
                        Collectors.toMap(
                                Entry::getKey, 
                                entry -> entry.getValue().getHash()
                        ));
    }
    
    /**
     * Returns the result of a specific hash algorithm computation
     * 
     * @param algorithm is the algorithm used for the calculation
     * @return the relative result or null if the algorithm doesn't exist or the result wasn't computed for it
     */
    public Hash getSpecificHash(String algorithm) {
        return hashes.get(algorithm);
    }
    
    /**
     * Recomputes the hashes, even though they were already computed before
     * 
     * @throws InterruptedException if the process is interrupted while threads are still working
     */
    public void recompute() throws InterruptedException {
        this.computed = false;
        compute();
    }
    
}
