public class StringTemplate {

    public static String getPointStringTemplate() {
        return  " 0\nPOINT\n 8\n{layerName}\n 10\n{positionE}\n 20\n{positionN}\n 30\n{positionH}";
    }

    public static String getSingleTextStringTemplate() {
        return " 0\nTEXT\n 8\n{layerName}\n 10\n{positionE}\n 20\n{positionN}\n 30\n{positionH}\n 40\n{textHeight}\n 1\n{text}";
    }
}