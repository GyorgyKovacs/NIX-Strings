import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        String text = "12345678901234567890.1234567890123456789";
        //   System.out.println(toggleScientificNotation(text));
        String text2 = "0.98765432109876543210987E2";
        System.out.println(toggleScientificNotation(format(2345.34212213345, "en")));
        System.out.println(toggleScientificNotation(format(2345.34212213345, "de")));
        //   System.out.println(toggleScientificNotation(format(2345.34212213345, "HU")));
        // System.out.println(toggleScientificNotation(format(2345.34212213345, "RU")));

    }

    private String format(double n, String language) {
        Locale locale;
        switch (language) {
            case "en":
                locale = Locale.ENGLISH;
                break;
            case "de":
                locale = Locale.GERMAN;
                break;
            default:
                throw new IllegalArgumentException("Invalid language code");
        }

        NumberFormat formatter = NumberFormat.getInstance(locale);
        return formatter.format(n);
    }

    private String toggleScientificNotation(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        if (s.contains("E") || s.contains("e")) {
            try {
                BigDecimal bigDecimal = new BigDecimal(s);
                return bigDecimal.setScale(16, RoundingMode.HALF_UP).toPlainString();
            } catch (NumberFormatException e) {
                return s;
            }
        } else {
            try {
                BigDecimal bigDecimal = new BigDecimal(s);
                return new DecimalFormat("0.################E0").format(bigDecimal);
            } catch (NumberFormatException e) {
                return s;
            }
        }
    }
}