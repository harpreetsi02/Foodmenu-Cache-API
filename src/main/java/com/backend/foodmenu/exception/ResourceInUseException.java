package com.backend.foodmenu.exception;

public class ResourceInUseException extends RuntimeException{

    public ResourceInUseException(String resource, Long id, String usedBy){
        super(resource + " with id: " + id + " is in use by " + usedBy + " and cannot be deleted");
    }
}
