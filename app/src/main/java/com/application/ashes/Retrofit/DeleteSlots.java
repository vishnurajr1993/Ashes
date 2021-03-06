package com.application.ashes.Retrofit;

public class DeleteSlots {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;
    public DeleteSlots.success getSuccess() {
        return success;
    }

    public void setSuccess(DeleteSlots.success success) {
        this.success = success;
    }

    success success;
   public class success
   {
       public String getMessage() {
           return message;
       }

       public void setMessage(String message) {
           this.message = message;
       }

       public String getStatusCode() {
           return statusCode;
       }

       public void setStatusCode(String statusCode) {
           this.statusCode = statusCode;
       }

       String message;
       String statusCode;
   }

}
