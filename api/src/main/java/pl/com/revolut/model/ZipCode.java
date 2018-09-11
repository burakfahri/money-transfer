package pl.com.revolut.model;

import lombok.NoArgsConstructor;

import lombok.Getter;
import pl.com.revolut.exception.ZipCodeException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@NoArgsConstructor
public class ZipCode {
    private String zipCode;

    public static final String ZIP_CODE_REGEX="^[0-9]{5}(?:-[0-9]{4})?$";

    public ZipCode(String zipCode) throws ZipCodeException {
        Pattern pattern = Pattern.compile(ZIP_CODE_REGEX);
        Matcher matcher = pattern.matcher(zipCode);
        if(!matcher.matches())
            throw new ZipCodeException();

        this.zipCode = zipCode;
    }

    public void setZipCode(String zipCode) throws ZipCodeException {
        Pattern pattern = Pattern.compile(ZIP_CODE_REGEX);
        Matcher matcher = pattern.matcher(zipCode);
        if(!matcher.matches())
            throw new ZipCodeException();
        this.zipCode = zipCode;
    }
}
