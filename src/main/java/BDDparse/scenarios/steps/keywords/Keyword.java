package BDDparse.scenarios.steps.keywords;

import java.util.*;

public class Keyword {
    public static final Map<String, String> keywordPatterns;
    static {
        Map<String, String> patterns = new HashMap<>();
        patterns.put("E-mail", "((?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,}))");
        patterns.put("Airport Code", "([A-Z]{3})");
        patterns.put("Password", "password ([^ ]+) ?");
        patterns.put("Date", "([0-9]{2}-[0-9]{2}-2[0-9]{3})");
        keywordPatterns = Collections.unmodifiableMap(patterns);
    }
    String keyword;
    String description;
    List<RelatedKeyword> relatedKeywords;

    public Keyword(String keyword){
        this.keyword = keyword;
    }

    public Keyword(String keyword, String description){
        this(keyword);
        this.description = description;
    }

    public void addRelatedKeyword(Keyword keyword, String relDescription){
        if (relatedKeywords == null){
            relatedKeywords = new ArrayList<>();
        }
        relatedKeywords.add(new RelatedKeyword(keyword, relDescription));
    }

    public String getKeyword() {
        return keyword;
    }

    public String getDescription() {
        return description;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public boolean isDate(){
        return description.toLowerCase(Locale.ROOT).equals("date");
    }

    static class RelatedKeyword {
        Keyword keyword;
        String pairDescription;

        public RelatedKeyword(Keyword keyword, String pairDescription){
            this.keyword = keyword;
            this.pairDescription = pairDescription;
        }
    }
}
