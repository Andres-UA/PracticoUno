package appmoviles.com.practicouno;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Preguntas extends AppCompatActivity {


    public ArrayList<String> operandos = new ArrayList<String>();

    private TextView question;
    private Button action;
    private int points = 0;

    private RadioButton option_a;
    private RadioButton option_b;
    private RadioButton option_c;
    private RadioButton option_d;

    private RadioGroup answer_options;

    private String respuesta = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguntas);

        Intent intent = getIntent();
        points = intent.getIntExtra("Points", 0);

        question = findViewById(R.id.tv_question);
        action = findViewById(R.id.btn_send_answer);

        answer_options = findViewById(R.id.rg_answer_options);

        option_a = findViewById(R.id.answer_a);
        option_b = findViewById(R.id.answer_b);
        option_c = findViewById(R.id.answer_c);
        option_d = findViewById(R.id.answer_d);

        operandos.add("+");
        operandos.add("-");
        operandos.add("/");
        operandos.add("*");


        Random rnd = new Random(System.currentTimeMillis());

        int intRnd = rnd.nextInt(2);
        int difficulty = 0;
        if (intRnd == 0) difficulty = 1;
        if (intRnd == 1) difficulty = 2;

        String[] set = makeQuestion(difficulty);
        String textQuestion = set[0];
        final String correctAnswer = set[1];
        String answerOptions = set[2];

        String[] options = answerOptions.split(";");
        List<String> list = Arrays.asList(options);
        Collections.shuffle(list);

        option_a.setText(list.get(0));
        option_b.setText(list.get(1));
        option_c.setText(list.get(2));
        option_d.setText(list.get(3));

        question.setText(textQuestion);

        action.setEnabled(false);

        answer_options.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                action.setEnabled(true);
                if (checkedId == R.id.answer_a) {
                    respuesta = option_a.getText().toString();
                } else if (checkedId == R.id.answer_b) {
                    respuesta = option_b.getText().toString();
                } else if (checkedId == R.id.answer_c) {
                    respuesta = option_c.getText().toString();
                } else if (checkedId == R.id.answer_d) {
                    respuesta = option_d.getText().toString();
                }
            }
        });

        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text_answer = respuesta;
                if (!text_answer.equals("") && text_answer != null) {
                    if (text_answer.equals(correctAnswer)) {
                        Toast.makeText(getApplicationContext(), "Correcto +5 puntos!!", Toast.LENGTH_LONG).show();
                        points += 5;
                    } else {
                        Toast.makeText(getApplicationContext(), "Incorrecto -5 puntos!!", Toast.LENGTH_LONG).show();
                        points -= 5;
                    }
                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    intent.putExtra("Points", points);
                    startActivity(intent);
                }
            }
        });

    }

    public String[] makeQuestion(int difficulty) {
        int n = 0;
        if (difficulty == 1) n = 21;
        if (difficulty == 2) n = 101;

        Random rnd = new Random(System.currentTimeMillis());

        int a = rnd.nextInt(n);
        int b = rnd.nextInt(n);

        String op = operandos.get(rnd.nextInt(4));

        String question = "" + a + " " + op + " " + b;
        String answer = answer(op, a, b);

        String[] set = new String[4];

        for (int i = 0; i < 3; i++) {
            int x = rnd.nextInt(11);
            if (x == 0) {
                x = 7;
            }
            int sign = rnd.nextInt(2);
            int correct = Integer.parseInt(answer);
            int other = 0;
            if (sign == 0) {
                other = correct + x;
            } else {
                other = correct - x;
            }
            set[i] = String.valueOf(other);
        }

        set[3] = answer;

        String otherSet = "";
        for (int i = 0; i < set.length; i++) {
            otherSet += "" + set[i] + ";";
        }

        String[] ret = {question, answer, otherSet};

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
