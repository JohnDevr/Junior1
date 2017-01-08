import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {

    private static ArrayList<String> sResult;

    public static void main(String[] args) {

        int menu = 1;
        String inputText = new String();
        String openBlock = null, closeBlock = null;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            menu_loop:
            while (true) {
                switch (menu) {
                    case 1:
                        System.out.println("Введите исходный текст:");
                        inputText = reader.readLine();
                        if (inputText.length() == 0) {
                            System.out.println("Текст не был введен, повторите ввод.");
                            menu = 1;
                            break;
                        }
                    case 2:
                        System.out.println("Введите открывающий блок:");
                        openBlock = reader.readLine();
                        if (openBlock.length() == 0 || openBlock.length() > 1) {
                            System.out.println("Блок не был введен,либо больше одного символа. Повторите ввод.");
                            menu = 2;
                            break;
                        }
                    case 3:
                        System.out.println("Введите закрывающий блок:");
                        closeBlock = reader.readLine();
                        if (closeBlock.length() == 0 || closeBlock.length() > 1) {
                            System.out.println("Блок не был введен,либо больше одного символа. Повторите ввод.");
                            menu = 3;
                            break;
                        }
                    default:
                        break menu_loop;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            return;
        }
        sResult = SearchBlocksFromText(inputText, openBlock, closeBlock);

        if (sResult.size() != 0) {
            CommonResult(sResult);
            DetailResult(sResult);
        } else System.out.println("Не найдено блоков текста.");
    }

    private static ArrayList<String> SearchBlocksFromText(String inputText, String openBlock, String closeBlock) {
        ArrayList<String> detectedBlocks = new ArrayList<>();
        int indexOpenBlock;
        int i = 0;

        for (; i < inputText.length(); i++) {
            if ((inputText.substring(i, i + 1)).equals(openBlock)) {
                indexOpenBlock = i;
                i++;
                for (; i < inputText.length(); i++) {
                    if ((inputText.substring(i, i + 1)).equals(openBlock)) {
                        i--;
                        break;
                    } else if ((inputText.substring(i, i + 1)).equals(closeBlock)) {
                        if (i != (indexOpenBlock + 1)) detectedBlocks.add(inputText.substring(indexOpenBlock + 1, i));
                        break;
                    }
                }
            }
        }

        sResult = detectedBlocks;
        return sResult;
    }

    private static void CommonResult(ArrayList<String> result) {
        JSONObject resultJson = new JSONObject();
        String minLength = (String) result.get(0);
        String maxLength = (String) result.get(0);
        int midLength = 0;

        System.out.println("Колличесво блоков текста: " + result.size());

        for (int i = 0; i < result.size(); ++i) {
            if (minLength.length() > ((String) result.get(i)).length()) {
                minLength = (String) result.get(i);
            } else if (maxLength.length() < ((String) result.get(i)).length()) {
                maxLength = (String) result.get(i);
            }

            midLength += ((String) result.get(i)).length();
        }

        resultJson.put("Максимальная длина блока текста: ", maxLength.length());
        resultJson.put("Минимальная длина блока текста: ", minLength.length());
        resultJson.put("Средняя длина блока текста: ", midLength / result.size());
        System.out.println("Общие результаты:");
        System.out.println(resultJson.toString());
    }

    private static void DetailResult(ArrayList<String> result) {
        JSONObject resultJson = new JSONObject();
        System.out.println("Результаты для каждого блока:");

        for (int i = 0; i < result.size(); ++i) {
            int numericSymbolsNumber = 0;
            int latinSymbolsNumber = 0;
            int cyrillicSymbolsNumber = 0;
            int otherSymbolsNumber = 0;

            for (int j = 0; j < ((String) result.get(i)).length(); ++j) {
                char symbol = ((String) result.get(i)).charAt(j);
                if (symbol >= 48 && symbol <= 57) {
                    ++numericSymbolsNumber;
                } else if (symbol >= 97 && symbol <= 122 || symbol >= 65 && symbol <= 90) {
                    ++latinSymbolsNumber;
                } else if ((symbol < 1072 || symbol > 1103) && (symbol < 1040 || symbol > 1071)) {
                    ++otherSymbolsNumber;
                } else {
                    ++cyrillicSymbolsNumber;
                }
            }

            resultJson.put("Обнаруженный текст:", result.get(i));
            resultJson.put("Длина текста: ", (result.get(i)).length());
            resultJson.put("Кол-во символов [aA-zZ]: ", latinSymbolsNumber);
            resultJson.put("Кол-во символов [аА-яЯ]: ", cyrillicSymbolsNumber);
            resultJson.put("Кол-во символов [0-9]: ", numericSymbolsNumber);
            resultJson.put("Кол-во символов другие: ", otherSymbolsNumber);

            System.out.println(resultJson.toString());

        }
    }
}
