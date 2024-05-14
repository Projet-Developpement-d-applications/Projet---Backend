package projet.conquerants.Service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ValidationService {

    private final DatabaseService databaseService;

    public ValidationService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

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

    public boolean valideStringOfCharWithSpace(String value) {
        boolean isValid = false;
        String regex = "[\\p{L} '\\p{M}]+";
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
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);

        if (!value.isEmpty() && matcher.matches()) {
            isValid = true;
        }

        return isValid;
    }
}
