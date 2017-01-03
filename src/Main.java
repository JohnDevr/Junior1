import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import org.json.simple.JSONObject;

public class Main {

    private static ArrayList<String> res;

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int menu = 1;
        String s = new String();
        String b1=null,b2=null;

        for(; true;) {
           if (menu == 1) {
               System.out.println("Введите исходный текст:");
               s = reader.readLine();
               if (s.length() == 0) {
                  System.out.println("Текст не был введен, повторите ввод.");
               } else menu = 2;
           }
            else if (menu == 2) {
               System.out.println("Введите открывающий блок:");
               b1 = reader.readLine();
               if (b1.length() == 0) {
                  System.out.println("Блок не был введен, повторите ввод.");
               } else menu = 3;
           }
           else if( menu == 3) {
               System.out.println("Введите закрывающий блок:");
               b2 = reader.readLine();
               if (b2.length() == 0) {
                  System.out.println("Блок не был введен, повторите ввод.");
               } else menu = 4;
           }
            else break;
       }
     res=SearchRes(s,b1,b2);

        if( res.size()!=0)
        {
            Comm_res(res);
            Det_res(res);
        }
        else System.out.println("Не найдено блоков текста.");
    }
    private static ArrayList<String> SearchRes(String s, String b1, String b2){
        ArrayList<String> tx = new ArrayList<>();
        int ind_b1 ;
        int i=0;

        for ( ; i<s.length();i++)
        {
            if((s.substring(i,i+1)).equals(b1))
            {
                ind_b1=i;
                i++;
                for(;i<s.length();i++)
                {
                    if((s.substring(i,i+1)).equals(b1))
                    {
                        i--;
                        break;
                    }
                    else if((s.substring(i,i+1)).equals(b2)){
                        if(i!=(ind_b1+1))tx.add(s.substring(ind_b1+1,i));
                        break;
                    }
                }
            }
        }

        res=tx;
        return res;
    }
    private static void Comm_res(ArrayList<String> res){
        JSONObject resultJson = new JSONObject();
        String min = res.get(0);
        String max = res.get(0);
        String mid = res.get(0);

        System.out.println("Колличесво блоков текста: "+res.size());
        for(int i = 0; i < res.size(); i++)
        {
            if (min.length() > (res.get(i)).length()) min=res.get(i);
            else if (max.length() < (res.get(i)).length()) max=res.get(i);
            else mid=res.get(i);
        }

        resultJson.put("Максимальная длина блока текста: ",max.length());
        resultJson.put("Минимальная длина блока текста: ",min.length());
        resultJson.put("Средняя длина блока текста: ", mid.length());
        System.out.println("Общие результаты:");
        System.out.println(resultJson.toString());
    }
    private static void Det_res(ArrayList<String> res){
        JSONObject resultJson = new JSONObject();
        System.out.println("Результаты для каждого блока:");

        for (int i = 0;i < res.size(); i++ )
        {
            int res_num=0,res_lat=0,res_kir=0,res_oth=0;
            for (int j=0; j < (res.get(i)).length();j++)
            {
                if (res.get(i).charAt(j) > 47 & res.get(i).charAt(j) < 58) res_num++;
                else if (res.get(i).charAt(j) > 64 & res.get(i).charAt(j) < 91 | res.get(i).charAt(j) > 96 & res.get(i).charAt(j) < 123) res_lat++;
                else if (res.get(i).charAt(j) > 1039 & res.get(i).charAt(j) < 1104) res_kir++;
                else res_oth++;
            }
            resultJson.put("Обнаруженный текст:",res.get(i));
            resultJson.put("Длина текста: ",(res.get(i)).length());
            resultJson.put("Кол-во символов [aA-zZ]: ",res_lat);
            resultJson.put("Кол-во символов [аА-яЯ]: ",res_kir);
            resultJson.put("Кол-во символов [0-9]: ",res_num);
            resultJson.put("Кол-во символов другие: ",res_oth);

            System.out.println(resultJson.toString());

          }
    }
}
