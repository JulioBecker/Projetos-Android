package com.example.thalindaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> mensagens = new ArrayList<>();
    Button btnMessage;
    TextView txtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtMessage = findViewById(R.id.txtMessage);
        btnMessage = findViewById(R.id.btnMessage);
        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtMessage.setText(mensagens.get(new Random().nextInt(mensagens.size())));
            }
        });

        mensagens.add("Você é o amor da minha vida!");
        mensagens.add("Te amo para sempre!");
        mensagens.add("Amo quando cozinha pra mim. Sua comida sempre com aquele toque especial: seu amor.");
        mensagens.add("Amo dormir ao seu lado. Não resisto de ficar grudadinho com você, desculpa :(");
        mensagens.add("Quero que nosso filho(a) nasça lindo(a) e com uma alma maravilhosa igual à sua. Não sei se vou aguentar tanto amor s2");
        mensagens.add("Amo sair com você, de a pé, de carro, de ônibus, do jeito que for, estando você ao meu lado estarei completo.");
        mensagens.add("Você me faz feliz de uma maneira que nunca imaginei que seria.");
        mensagens.add("Nunca se esqueça disso: você é maravilhosa!");
        mensagens.add("Quer casar comigo? Sonho muito em ver você de vestido, véu e buquê vindo na minha direção.");
        mensagens.add("Amo saber que você é minha mulher e que você me ama. Obrigado por tudo.");
        mensagens.add("Quero ser seu eterno namorado, webnamorado, marido, homem e companheiro!");
        mensagens.add("Amo seus beijos. Me dá um agora?");
        mensagens.add("Quero ser o seu melhor remédio, conta comigo sempre, tá?");
        mensagens.add("Você é o meu maior tesouro. Agradeço sempre ao universo por ter nos unido.");
        mensagens.add("Amo ficar no seu colinho, e ganhar aquele carinho gostoso seu. Não quero perder isso nunca!");
        mensagens.add("Amo ver você deitada no meu colinho. Ver você dormindo no meu peito é uma das melhores sensações que já senti na minha vida.");
        mensagens.add("Amo seu sorriso, me derreto sempre que o vejo.");
        mensagens.add("As vezes eu me pergunto, como pode eu ter uma mulher tão linda.");
        mensagens.add("Adoro o seu abraço, nele eu me sinto acolhido e protegido.");
        mensagens.add("Nunca vou me esquecer da nossa primeira noite juntos, sentindo seu corpo no meu, meu Deus que sensação maravilhosa.");
        mensagens.add("Já não sei mais o que é uma vida sem você.");
        mensagens.add("Você me faz uma pessoa melhor todos os dias.");
        mensagens.add("Tenho muito orgulho de você!");
        mensagens.add("Quer namorar comigo?");
        mensagens.add("Você é muito gostosa, puta que pariu!");
        mensagens.add("Amo a sua raba! :P");
        mensagens.add("Amo foder com você, deliciosa!");
        mensagens.add("Adoro quando você me acorda com beijinhos de manhã.");
        mensagens.add("Fico muito feliz em ver como nós nos damos bem com nossas famílias, adoro estar com todos eles e com você ao meu lado, meu orgulho!");
        mensagens.add("Você é a pessoa mais incrível que já encontrei na minha vida!");
        mensagens.add("Desejo pra você somente felicidade. Não há nada que me deixe mais feliz do que te ver feliz.");
        mensagens.add("Eu amo amar você!");
        mensagens.add("Amo o seu carinho, o seu toque me faz bem.");
        mensagens.add("Adoro nossos rolês aleatórios. Nada melhor do passear com você ao meu lado.");
        mensagens.add("Amo tomar banho com você (você sabe bem disso hihi).");
        mensagens.add("Thalita 10. By Carlos Vasconcelos");
        mensagens.add("Você é muito fofinha!");
        mensagens.add("Já parou pra pensar em nós dois juntos? Que casalzão da porra!)");
    }

}
