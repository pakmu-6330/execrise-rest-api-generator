package cn.dyr.rest.generator.converter.name;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用于判断某个元素是否为系统保留的关键字，用于类名或者字段名称转换时进行判断
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class KeywordHolder {

    private static KeywordHolder holder;
    private static Logger logger;

    static {
        logger = LoggerFactory.getLogger(KeywordHolder.class);

        try {
            holder = new KeywordHolder("cn/dyr/rest/generator/config/keyword.xml");
        } catch (DocumentException e) {
            logger.error("failed to load keyword data file", e);
        }
    }

    public static KeywordHolder getHolder() {
        return holder;
    }

    private Set<String> keywordList;

    private KeywordHolder() {
        this.keywordList = new HashSet<>();
    }

    private KeywordHolder(String dataFile) throws DocumentException {
        this();

        InputStream dataInputStream =
                KeywordHolder.class.getClassLoader().getResourceAsStream("cn/dyr/rest/generator/config/keyword.xml");
        SAXReader reader = new SAXReader();
        Document document = reader.read(dataInputStream);

        List keywordNodes = document.selectNodes("/keywords/keyword");
        for (Object keywordNode : keywordNodes) {
            Element element = (Element) keywordNode;
            String keyword = element.getText().toLowerCase();
            keywordList.add(keyword);
        }
    }

    /**
     * 判断指定的字符串是否为关键字
     *
     * @param word 要进行判断的字符串
     * @return 如果这个字符串属于关键字，则返回 true；否则返回 false
     */
    public boolean isKeyword(String word) {
        return keywordList.contains(word.toLowerCase());
    }

}
