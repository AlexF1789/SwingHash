package net.alexf1789.swinghash.models;

public class FileResource implements Resource {

    private String path;
    
    public FileResource(String path) {
        this.path = path;
    }
    
    @Override
    public String getResource() {
        return path;
    }

}
