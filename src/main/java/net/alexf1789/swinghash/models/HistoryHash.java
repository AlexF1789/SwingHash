package net.alexf1789.swinghash.models;

import java.util.Map;

import net.alexf1789.swinghash.services.Hasher;

/**
 * Class containing a previously computed hash, used for chronology
 */
public class HistoryHash {
    
    private String resourceContent, expected, correctAlgo;
    private boolean resourceIsFile;
    private Map<String, String> hashes;
    
    public HistoryHash(Hasher hasher) throws InterruptedException {        
        this.resourceContent = hasher.getResource().getResource();
        this.expected = hasher.getExpectedHash();
        this.resourceIsFile = hasher.isFileResource();
        this.hashes = hasher.getHashesResult();
        
        correctAlgo = hasher.validate();
    }

    public String getResource() {
        return resourceContent;
    }

    public String getExpected() {
        return expected;
    }

    public boolean resourceIsFile() {
        return resourceIsFile;
    }

    public Map<String, String> getHashes() {
        return hashes;
    }
    
    public String getCorrectAlgo() {
        return correctAlgo;
    }

}
