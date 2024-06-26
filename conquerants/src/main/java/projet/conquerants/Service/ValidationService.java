package projet.conquerants.Service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ValidationService {

    public boolean valideStringOfChar(String value) {
        boolean isValid = false;
        String regex = "\\p{L}+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);

        if (!value.isEmpty() && matcher.matches()) {
            isValid = true;
        }

        return isValid;
    }

    public boolean valideStringOfNomPrenom(String value) {
        boolean isValid = false;
        String regex = "[\\p{L}-]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);

        if (!value.isEmpty() && matcher.matches()) {
            isValid = true;
        }

        return isValid;
    }

    public boolean valideStringOfCharAndDigits(String value) {
        boolean isValid = false;
        String regex = "[\\p{L}\\p{Nd}]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);

        if (!value.isEmpty() && matcher.matches()) {
            isValid = true;
        }

        return isValid;
    }

    public boolean valideEmail(String value) {
        boolean isValid = false;
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);

        if (!value.isEmpty() && matcher.matches()) {
            isValid = true;
        }

        return isValid;
    }

    public boolean valideStringOfCharAndDigitsWithSpace(String value) {
        boolean isValid = false;
        String regex = "[\\p{L} \\p{M}\\p{Nd}]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);

        if (!value.isEmpty() && matcher.matches()) {
            isValid = true;
        }

        return isValid;
    }

    public boolean validePasswordString(String value) {
        boolean isValid = false;
        String regex = "^(?=.*[0-9])(?=.*[a-zA-Z@#$%^&+=!]).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);

        if (!value.isEmpty() && matcher.matches()) {
            isValid = true;
        }

        return isValid;
    }
}
