package net.alexf1789.swinghash.services;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import net.alexf1789.swinghash.models.FileResource;
import net.alexf1789.swinghash.models.Resource;
import net.alexf1789.swinghash.models.StringResource;

public class Hasher {

    private Resource resource;
    private String expectedHash;
    private Map<String, Hash> hashes;
    private boolean computed;
    private boolean resourceIsFile;
    
    public Hasher(Collection<String> algorithms, Resource resource, String expectedHash) {
        this.computed = false;
        this.resource = resource;
        this.resourceIsFile = resource instanceof FileResource;
        this.expectedHash = (expectedHash != null && expectedHash.isEmpty()) ? null : expectedHash;
        this.hashes = new HashMap<String, Hash>(algorithms.size());
        
        for(String algorithm : algorithms) {
            this.hashes.put(algorithm, new Hash(resource, algorithm));
        }
    }
    
    public Hasher(String[] algorithms, Resource resource, String expectedHash) {
        this(Arrays.asList(algorithms), resource, expectedHash);
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
    public Map<String, String> getHashesResult() throws InterruptedException {
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
    
    public Map<String, Hash> getHashes() throws InterruptedException {
        if(!this.computed)
            compute();
        
        return this.hashes;
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
    
    /**
     * Validates the passed hash with the computed ones
     * 
     * @param hash is the expected hash
     * @return a String representing the algorithm matching the hash, or null if none
     * @throws InterruptedException if the process is interrupted while threads are still working
     */
    public String validate() throws InterruptedException {
        return this.getHashes().values().parallelStream()
            .filter(computedHash -> computedHash.getHash().equals(expectedHash))
            .map(computedHash -> computedHash.getAlgorithm())
            .findAny()
            .orElse(null);
    }
    
    /**
     * Indicates if the resource is a File
     * 
     * @return a boolean which is true if the resource is a file, false if it's a String
     */
    public boolean isFileResource() {
        return resourceIsFile;
    }
    
    /**
     * Returns the Resource the Hasher has been initialized on
     * 
     * @return the Resource the hash will be computed on
     */
    public Resource getResource() {
        return resource;
    }
    
    public String getExpectedHash() {
        return expectedHash;
    }
    
    /**
     * Indicates if the user provided an expected hash for the resource
     * 
     * @return true if the user did, false otherwise
     */
    public boolean hasExpectedHash() {
        return expectedHash != null;
    }
    
}
