package appmoviles.com.practicouno;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Random;

public class Canje extends AppCompatActivity {

    private int points = 0;
    private TextView textPoints;
    private Button action;
    private RadioGroup options;

    private String prize = "";
    private int prize_value = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canje);

        Intent intent = getIntent();
        points = intent.getIntExtra("Points", 0);

        textPoints = findViewById(R.id.tv_points);
        action = findViewById(R.id.btn_claim);
        options = findViewById(R.id.rg_options);

        options.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.lapicero) {
                    prize = "Lapicero ICESI";
                    prize_value = 0;
                } else if (checkedId == R.id.cuaderno) {
                    prize = "Cuaderno";
                    prize_value = 30;
                } else if (checkedId == R.id.libreta) {
                    prize = "Libreta ICESI";
                    prize_value = 40;
                } else if (checkedId == R.id.camiseta) {
                    prize = "Camiseta ICESI";
                    prize_value = 80;
                } else if (checkedId == R.id.saco) {
                    prize = "Saco ICESI";
                    prize_value = 100;
                }
            }
        });

        textPoints.setText(String.valueOf(points));
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!prize.equals("") || prize_value > 0) {
                    if (prize_value > points) {
                        Toast.makeText(getApplicationContext(), "Error, puntos insufucientes", Toast.LENGTH_LONG).show();
                    } else {
                        final AlertDialog.Builder alertF = new AlertDialog.Builder(Canje.this);
                        alertF.setTitle("Codigo generado");
                        alertF.setMessage("Guarda este codigo para poder reclamar \nCodigo: " + generateCode());
                        alertF.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                                        intent.putExtra("Points", points);
                                        startActivity(intent);
                                    }
                                });


                        AlertDialog.Builder alert = new AlertDialog.Builder(Canje.this);
                        alert.setTitle("Confirmar elección");
                        alert.setMessage("Estas seguro que quieres reclamar " + prize + "?");
                        alert.setPositiveButton("Si",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        points -= prize_value;
                                        alertF.show();
                                    }
                                });
                        alert.setNegativeButton("NO",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                                        intent.putExtra("Points", points);
                                        startActivity(intent);
                                    }
                                });
                        alert.show();
                    }


                }

            }
        });
    }

    public String generateCode() {
        Random rnd = new Random(System.currentTimeMillis());
        int a = rnd.nextInt(1000);
        int b = rnd.nextInt(1000);
        String code = String.valueOf(b) + prize + "-" + String.valueOf(a);
        byte[] data = code.getBytes(StandardCharsets.UTF_8);
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        return base64;
    }

}


