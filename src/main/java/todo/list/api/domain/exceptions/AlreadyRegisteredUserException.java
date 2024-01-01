package todo.list.api.domain.exceptions;

public class AlreadyRegisteredUserException extends RuntimeException {
    public AlreadyRegisteredUserException() {super("already registered user. Check the authentication method used.");}
    public AlreadyRegisteredUserException(String msg) {super(msg);}
}
