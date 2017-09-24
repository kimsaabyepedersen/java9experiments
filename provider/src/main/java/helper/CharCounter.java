package helper;

public class CharCounter {

    private final String str;

    public CharCounter(String str) {
        this.str = str;
    }

    public int count(char c){
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c)
                count++;
        }
        return count;
    }

}
