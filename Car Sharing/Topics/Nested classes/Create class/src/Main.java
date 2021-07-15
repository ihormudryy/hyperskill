class StringOperations {

    public static class EngString {
        boolean isEnglishLang;
        String string;

        public EngString(boolean isEnglishLang, String string) {
            this.isEnglishLang = isEnglishLang;
            this.string = string;
        }

        public boolean isEnglish() {
            return isEnglishLang;
        }

        public String getString() {
            return string;
        }
    }

}