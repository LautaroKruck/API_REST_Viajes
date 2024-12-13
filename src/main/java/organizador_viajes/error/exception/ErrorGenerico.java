package organizador_viajes.error.exception;

public class ErrorGenerico {

    /*
    Clase que almacena informacion sobre el error acontecido
     */
    private String message;
    private String uri;

    public ErrorGenerico(String message, String uri) {
        this.message = message;
        this.uri = uri;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
