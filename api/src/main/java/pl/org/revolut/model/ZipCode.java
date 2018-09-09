package pl.org.revolut.model;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.org.revolut.exception.ZipCodeException;
import lombok.Getter;

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
