package com.github.heisdanielade.pamietampsa.exception.media;

import com.github.heisdanielade.pamietampsa.exception.CustomException;

public class FileSizeTooLargeException extends CustomException {
    public FileSizeTooLargeException(String message){
        super(message);
    }


}
