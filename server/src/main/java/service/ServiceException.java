package service;

public class ServiceException extends Exception {
    Integer status;

    public ServiceException(String message, Integer status) {
        super(message);
        this.status = status;
    }

    public int getStatus(){
        return this.status;
    }

}

//how can I make a subclass of this?