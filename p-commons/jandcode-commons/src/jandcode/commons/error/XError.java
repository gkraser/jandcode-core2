package jandcode.commons.error;

import java.text.*;
import java.util.*;

/**
 * runtime ошибка
 */
public class XError extends RuntimeException {

    private String _messageRaw;
    private ArrayList<Object> _params = new ArrayList<>();

    /**
     * Генерация ошибки
     *
     * @param message сообщение в формате MessageFormat
     * @param params  параметры для MessageFormat
     */
    public XError(String message, Object... params) {
        super();
        _messageRaw = message;
        for (int i = 0; i < params.length; i++) {
            _params.add(params[i]);
        }
    }


    /**
     * Генерация ошибки
     *
     * @param message сообщение в формате MessageFormat
     * @param params  параметры для MessageFormat
     */
    public XError(Throwable cause, String message, Object... params) {
        super(cause);
        _messageRaw = message;
        for (int i = 0; i < params.length; i++) {
            _params.add(params[i]);
        }
    }

    public String getMessageRaw() {
        return _messageRaw == null ? "" : _messageRaw;
    }

    public String getMessage() {
        try {
            return MessageFormat.format(getMessageRaw(), _params.toArray());
        } catch (Exception e) {
            return "(=> parse message error!) : message : " + getMessageRaw(); //NON-NLS
        }
    }
}
