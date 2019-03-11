package appmoviles.com.practicouno;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Random;

public class Preguntas extends AppCompatActivity {


    public ArrayList<String> operandos = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguntas);
        operandos.add("+");
        operandos.add("-");
        operandos.add("/");
        operandos.add("*");
    }

    public String[] makeQuestion(int difficulty) {
        int n = 0;
        if (difficulty == 1) n = 21;

        Random rnd = new Random(System.currentTimeMillis());

        int a = rnd.nextInt(n);
        int b = rnd.nextInt(n);

        String op = operandos.get(rnd.nextInt(4));

        String question = "" + a + " " + op + " " + b;
        String answer = answer(op, a, b);
        String[] ret = {question, answer};

        return ret;
    }

    public String answer(String op, int a, int b) {
        int answer = 0;
        switch (op) {
            case "+":
                answer = a + b;
                break;
            case "-":
                answer = a - b;
                break;
            case "/":
                answer = a / b;
                break;
            case "*":
                answer = a * b;
                break;
        }
        return String.valueOf(answer);
    }

}
