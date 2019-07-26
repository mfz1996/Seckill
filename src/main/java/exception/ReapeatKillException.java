package exception;

public class ReapeatKillException extends SeckillException{
    public ReapeatKillException(String message) {
        super(message);
    }

    public ReapeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
