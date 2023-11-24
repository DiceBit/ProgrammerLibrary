package com.example.webapp3.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.NumberUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DecimalFormat;

@Controller
@RequestMapping("/generate")
public class PasswordGenController {

    private static String passwordTrue = "";
    private static String textEncription = "";

    @GetMapping
    private String getGeneratePassword(Model model) {

        model.addAttribute("password", passwordTrue);
        passwordTrue = "";
        model.addAttribute("passwordEncryption", textEncription);
        textEncription = "";

        return "GenPassword";
    }

    @PostMapping
    private String postGenPassword(@RequestParam int passwordLength,
                                   @RequestParam int numberCount) {

        if (passwordLength < numberCount)
            numberCount = passwordLength;

        StringBuilder password = new StringBuilder();

        double P = Math.pow(10, -5);
        int passwordV = 30;
        int passwordT = 10;

        double S_1 = Math.ceil((passwordV * passwordT) / P);

        for (int i = 0; i < passwordLength; i++) {
            int A = (int) (Math.random() * 26);
            int S = (int) Math.pow(A, passwordLength);
            if (S_1 <= S) {
                A += 97;
                password.append((char) A);
            } else passwordLength++;

        }

        password.delete(0, numberCount);
        password.reverse();
        for (int i = 0; i < numberCount; i++) {
            password.append((int) (Math.random() * 10));
        }
        password.reverse();

        System.out.println("Password: " + password);
        passwordTrue += password;

        return "redirect:/generate";
    }



    @PostMapping("/lr2")
    private String postEncryption(@RequestParam String str,
                             @RequestParam String keyStr,
                             Model model){

        StringBuilder origStr = new StringBuilder(str);
        StringBuilder newString = new StringBuilder();

        //до заполнения матрицы
        while (origStr.length() % 3 != 0) {
            origStr.append(".");
        }

        //столбцы и строки массивов
        int key = keyStr.length();
        int colm = origStr.length() / key;

        char[][] square = new char[key][colm];
        char[][] transportMat = new char[colm][key];

        //добавление элементов в двумерный массив символов из строки
        for (int i = 0; i < square.length; i++) {
            for (int j = 0; j < square[i].length; j++) {
                square[i][j] = origStr.charAt(0);
                origStr.deleteCharAt(0);
            }
        }

        //транспонирование матрицы
        for (int i = 0; i < square.length; i++) {
            for (int j = 0; j < square[i].length; j++) {
                transportMat[j][i] = square[i][j];
            }
        }

        //удаление лишних символов
        for (char[] el : transportMat) {
            newString.append(el);
            int index = newString.indexOf(String.valueOf("."));
            if (index != -1) {
                newString.deleteCharAt(index);
            }
        }

        //вывод матрицы
        if (false) {
            for (int i = 0; i < square.length; i++) {
                for (int j = 0; j < square[i].length; j++) {
                    System.out.print(square[i][j] + " ");
                }
                System.out.println();
            }
        }
        System.out.println("str: " + newString);
        textEncription += newString;


        return "redirect:/generate";
    }

    @PostMapping("/lr13")
    private String lt13Test(@RequestParam String lr13Text,
                            Model model){

        if (lr13Text.equals("")){
            model.addAttribute("errorMessage", "пустое поле");
            return "GenPassword";
        }

        if (!isNumber(lr13Text)){
            model.addAttribute("errorMessage", "это не является числом");
            return "GenPassword";
        }

        if (isNumber(lr13Text) && Double.parseDouble(lr13Text) > 1000){
            model.addAttribute("errorMessage", "слишком «большое число»");
            return "GenPassword";
        }

        DecimalFormat format = new DecimalFormat("#.#");
        model.addAttribute("errorMessage", format.format(Double.parseDouble(lr13Text)));

        return "GenPassword";
    }
    private static boolean isNumber(String str) {
         try {
             Double.parseDouble(str);
             return true;
         } catch (Exception e){
             return false;
         }
    }
}
