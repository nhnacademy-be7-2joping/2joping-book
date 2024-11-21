package com.nhnacademy.bookstore.common.error.exception.imageset;

public class ImageUploadException extends RuntimeException {
    public static final String MESSAGE = "이미지 업로드 중 오류가 발생했습니다.";
    public ImageUploadException() {
        super(MESSAGE);
    }
}