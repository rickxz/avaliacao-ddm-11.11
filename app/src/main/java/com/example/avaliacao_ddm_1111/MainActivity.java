package com.example.avaliacao_ddm_1111;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    // Feito por Herick Victor Rodrigues | SC301018X
    // Garçom
    private EditText txtMesa;
    private EditText txtItem;
    private EditText txtProduto;
    private EditText txtPreco;
    private Button btnInserir;
    private Button btnListar;
    private Button btnCalcular;
    private Button btnZerar;

    // Cozinha
    private EditText txtMesaCoz;
    private EditText txtItemCoz;
    private Button btnAtender;

    // Database
    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("restaurante");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Garçom
        txtMesa = findViewById(R.id.txtMesa);
        txtItem = findViewById(R.id.txtItem);
        txtProduto = findViewById(R.id.txtProduto);
        txtPreco = findViewById(R.id.txtPreco);
        btnInserir = findViewById(R.id.btnInserir);
        btnListar = findViewById(R.id.btnListar);
        btnCalcular = findViewById(R.id.btnCalcular);
        btnZerar = findViewById(R.id.btnZerar);

        // Cozinha
        txtMesaCoz = findViewById(R.id.txtMesaCoz);
        txtItemCoz = findViewById(R.id.txtItemCoz);
        btnAtender = findViewById(R.id.btnAtender);

        // Listeners
        btnInserir.setOnClickListener(new InserirItemListener());
        btnListar.setOnClickListener(new ListarItensListener());
        btnCalcular.setOnClickListener(new CalcularContaListener());
        btnZerar.setOnClickListener(new ZerarMesaListener());
        btnAtender.setOnClickListener(new AtenderPedidoListener());
    }

    // ================
    // INSERIR ITEM
    // ================
    private class InserirItemListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String mesa = txtMesa.getText().toString();
            String codItem = txtItem.getText().toString();
            String produto = txtProduto.getText().toString();
            double preco = Double.parseDouble(txtPreco.getText().toString());

            Item item = new Item(produto, preco);
            db.child(mesa).child(codItem).setValue(item);

            txtMesa.setText("");
            txtItem.setText("");
            txtProduto.setText("");
            txtPreco.setText("");
        }
    }

    // ================
    // LISTAR ITENS
    // ================
    private class ListarItensListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String mesaString = txtMesa.getText().toString();
            DatabaseReference mesa = db.child(mesaString);
            mesa.addListenerForSingleValueEvent(new FirebaseListarItens());
            txtMesa.setText("");
        }
    }

    private class FirebaseListarItens implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {
                String produto;
                double preco;
                boolean atendido;
                for (DataSnapshot item : snapshot.getChildren()) {
                    Item i = item.getValue(Item.class);
                    produto = i.getProduto();
                    preco = i.getPreco();
                    atendido = i.isAtendido();
                    Toast.makeText(MainActivity.this, "Produto: " + produto + "\nPreço: R$" + preco + "\nAtendido: " + atendido , Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    }

    // ================
    // CALCULAR CONTA
    // ================
    private class CalcularContaListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String mesaString = txtMesa.getText().toString();
            DatabaseReference mesa = db.child(mesaString);
            mesa.addListenerForSingleValueEvent(new FirebaseCalcularConta());
            txtMesa.setText("");
        }
    }

    // ================
    // ZERAR MESA1
    // ================
    private class FirebaseCalcularConta implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {
                double total = 0;
                for (DataSnapshot item : snapshot.getChildren()) {
                    Item i = item.getValue(Item.class);
                    total += i.getPreco();
                }
                Toast.makeText(MainActivity.this, "Total da conta: R$" + total, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    }

    // ================
    // ZERAR MESA
    // ================
    private class ZerarMesaListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String mesaString = txtMesa.getText().toString();
            DatabaseReference mesa = db.child(mesaString);
            mesa.setValue(null);
            txtMesa.setText("");
        }
    }


    // =~=~=~=~=~=~=~=~=~=~=~=~=~
    // COZINHA
    // =~=~=~=~=~=~=~=~=~=~=~=~=~


    // ================
    // ATENDER PEDIDO
    // ================
    private class AtenderPedidoListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String mesaString = txtMesaCoz.getText().toString();
            String itemString = txtItemCoz.getText().toString();
            DatabaseReference mesa = db.child(mesaString).child(itemString);
            mesa.addListenerForSingleValueEvent(new FirebaseAtenderPedido());
        }
    }

    private class FirebaseAtenderPedido implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {
                Item i = snapshot.getValue(Item.class);
                i.setAtendido(true);
                String mesaString = txtMesaCoz.getText().toString();
                String itemString = txtItemCoz.getText().toString();
                db.child(mesaString).child(itemString).setValue(i);

                txtMesaCoz.setText("");
                txtItemCoz.setText("");
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    }
}