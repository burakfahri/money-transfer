package pl.com.revolut.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.com.revolut.common.exception.PhoneNumberException;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class PhoneNumber {
    private int area;   // area code (3 digits)
    private int exch;   // exchange  (3 digits)
    private int ext;    // extension (4 digits)


    public PhoneNumber(int area,int exch,int ext) throws PhoneNumberException {
        if( area <200 || area > 999 || area == 911 ||
                exch < 200 || exch > 999 || exch == 911 ||
                ext<0 || ext > 999 )
            throw new PhoneNumberException();

        this.area = area;
        this.exch = exch;
        this.ext = ext;
    }

    public void setArea(int area) throws PhoneNumberException {
        if( area <200 || area > 999)
            throw new PhoneNumberException("Area must be between 200 and 999");
        this.area = area;
    }

    public void setExch(int exch) throws PhoneNumberException {
        if( exch <200 || exch > 999)
            throw new PhoneNumberException("Exch must be between 200 and 999");
        this.exch = exch;
    }

    public void setExt(int ext) throws PhoneNumberException {
        if( ext <200 || ext > 999)
            throw new PhoneNumberException("Exch must be between 0 and 999");
        this.ext = ext;
    }
}
