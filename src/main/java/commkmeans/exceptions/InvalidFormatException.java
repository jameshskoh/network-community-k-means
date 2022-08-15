package commkmeans.exceptions;

import java.io.IOException;

public class InvalidFormatException extends IOException {
    public InvalidFormatException() {
        super();
    }

    public InvalidFormatException(String err) {
        super(err);
    }
}
