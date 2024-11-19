package todo.list.api.domain.exceptions;

public class AlreadyRegisteredUserException extends RuntimeException {
    public AlreadyRegisteredUserException() {super("There is already a registered user with this email. Check the authentication method used.");}
    public AlreadyRegisteredUserException(String msg) {super(msg);}
}
