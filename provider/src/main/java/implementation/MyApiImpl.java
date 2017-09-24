package implementation;

import api.MyApi;
import helper.CharCounter;
import helper.NullGuard;

public class MyApiImpl implements MyApi {

    public int count(String str) {
        if (NullGuard.isNull(str))
            return 0;
        return str.length();
    }

    public int count(String str, char c) {
        if (NullGuard.isNull(str))
            return 0;
        return new CharCounter(str).count(c);
    }

}
