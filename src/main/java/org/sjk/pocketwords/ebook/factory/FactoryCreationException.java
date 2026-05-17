package org.sjk.pocketwords.ebook.factory;

/**
 * A Factory Creation Exception.
 *
 * <p>This exception indicates that a specific object cannot be created by a factory.
 *
 * @author Simon Josef Kreuzpointner
 */
public class FactoryCreationException extends Exception {

    @java.io.Serial
    static final long serialVersionUID = 1L;

    /**
     * Constructs an {@code FactoryCreationException} with no detail message. A detail message is a String that
     * describes this particular exception.
     */
    public FactoryCreationException() {
        super();
    }

    /**
     * Constructs an {@code FactoryCreationException} with the specified detail message.  A detail message is a String
     * that describes this particular exception.
     *
     * @param s the String that contains a detailed message
     */
    public FactoryCreationException(String s) {
        super(s);
    }

    /**
     * Constructs a new {@code FactoryCreationException} with the specified detail message and cause.
     *
     * <p>Note that the detail message associated with {@code cause} is
     * <i>not</i> automatically incorporated in this exception's detail
     * message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link Throwable#getMessage()}
     *                method).
     * @param cause   the cause (which is saved for later retrieval by the {@link Throwable#getCause()} method).  (A
     *                {@code null} value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public FactoryCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new exception with the specified cause and a detail message of
     * {@code (cause==null ? null : cause.toString())} (which typically contains the class and detail message of
     * {@code cause}). This constructor is useful for exceptions that are little more than wrappers for other throwables
     * (for example, {@link java.security.PrivilegedActionException}).
     *
     * @param cause the cause (which is saved for later retrieval by the {@link Throwable#getCause()} method).  (A
     *              {@code null} value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public FactoryCreationException(Throwable cause) {
        super(cause);
    }
}
