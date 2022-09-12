package com.software.agenda;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.software.bancoDados.ContatoDB;
import com.software.bancoDados.DBHelper;
import com.software.entidades.Contato;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Contato> informacoesContato;
    ListView listaContatos;

    Contato contato;
    Boolean edicaoContato;

    EditText textNome;
    EditText textTelefone;
    Button btnSalvar;
    Button btnCancelar;
    
    DBHelper dbHelper;
    ContatoDB contatoDB;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(MainActivity.this);
        contatoDB = new ContatoDB(dbHelper);

        edicaoContato = false;

        textNome = findViewById(R.id.campoNome);
        textTelefone = findViewById(R.id.campoTelefone);
        btnSalvar = findViewById(R.id.botaoSalvar);
        btnCancelar = findViewById(R.id.botaoCancelar);
        listaContatos = findViewById(R.id.listagemContatos);

        informacoesContato = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.support.constraint.R.layout.support_simple_spinner_dropdown_item, informacoesContato);

        listaContatos.setAdapter(adapter);
        contatoDB.listar(informacoesContato);
        acao();
    }

    private void acao() {
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edicaoContato) {
                    edicaoContato = false;
                    contato = new Contato();
                    textNome.setText("");
                    textTelefone.setText("");
                }
            }
        });

        listaContatos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                new AlertDialog.Builder(view.getContext())
                        .setMessage("Selecione uma Opção:")
                        .setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {
                                edicaoContato = true;

                                contato = new Contato();
                                contato.setId(informacoesContato.get(i).getId());

                                textNome.setText(informacoesContato.get(i).getNome());
                                textTelefone.setText(informacoesContato.get(i).getTelefone());
                            }
                        })
                        .setNegativeButton("Remover", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {
                                new AlertDialog.Builder(view.getContext())
                                        .setMessage("Remover o contato?")
                                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int k) {
                                                contatoDB.remover(informacoesContato.get(i).getId());
                                                contatoDB.listar(informacoesContato);
                                                adapter.notifyDataSetChanged();
                                            }
                                        })
                                        .setNegativeButton("Cancelar", null)
                                        .create().show();
                            }
                        })
                        .create().show();

                return false;
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textNome.getText().toString().isEmpty() || textTelefone.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Contato Inválido!", Toast.LENGTH_SHORT).show();
                } else {
                    if (edicaoContato == false) {
                        contato = new Contato();
                    }

                    contato.setNome(textNome.getText().toString());
                    contato.setTelefone(textTelefone.getText().toString());

                    if (edicaoContato) {
                        contatoDB.atualizar(contato);
                    } else {
                        contatoDB.inserir(contato);
                    }

                    contatoDB.listar(informacoesContato);
                    adapter.notifyDataSetChanged();

                    edicaoContato = false;
                    contato = new Contato();
                    textNome.setText("");
                    textTelefone.setText("");

                    Toast.makeText(MainActivity.this, "Salvo!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}