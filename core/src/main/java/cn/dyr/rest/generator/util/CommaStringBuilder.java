package cn.dyr.rest.generator.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 建立以逗号区分的字符串
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class CommaStringBuilder {

    private List<String> values;

    public CommaStringBuilder() {
        this.values = new ArrayList<>();
    }

    public CommaStringBuilder addValue(String value) {
        this.values.add(value);
        return this;
    }

    @Override
    public String toString() {
        if (this.values == null || this.values.size() == 0) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (String value : this.values) {
            if (first) {
                first = false;
            } else {
                builder.append(", ");
            }

            builder.append(value);
        }

        return builder.toString();
    }
}
