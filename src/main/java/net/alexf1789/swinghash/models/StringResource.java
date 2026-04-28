package net.alexf1789.swinghash.models;

public class StringResource implements Resource {

    private String resource;
    
    public StringResource(String resource) {
        this.resource = resource;
    }
    
    @Override
    public String getResource() {
        return resource;
    }

}
