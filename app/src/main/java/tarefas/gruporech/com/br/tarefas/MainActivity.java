package tarefas.gruporech.com.br.tarefas;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity {

    //Auxiliar
    private ArrayList<String>listaTarefas;
    SQLiteDatabase db;

    //Components
    private EditText texto;
    private Button botao;
    private ListView lista;
    //Adapters
    private ArrayAdapter listaTarefasAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            //Inicia o banco
            db = openOrCreateDatabase("tarefas", MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS tarefas ( id INTEGER AUTO_INCREMENT , nome VARCHAR (30), PRIMARY KEY(ID) )");
            atualizarLista();
        }catch(Exception e){
            e.printStackTrace();
        }

        //Recupera as ID's
        botao = (Button) findViewById(R.id.botaoId);
        texto = (EditText) findViewById(R.id.textoId);

        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inserirLista();
                texto.setText("");
            }
        });
    }
    private void inserirLista(){
        try {
            String nomeTarefa = texto.getText().toString();
            db.execSQL("INSERT INTO tarefas(nome) VALUES ( '" + nomeTarefa + "')");
            atualizarLista();
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Não foi possível inserir a tarefa", Toast.LENGTH_SHORT).show();
        }

    }
    private void atualizarLista(){
        try{
            listaTarefas = new ArrayList<String>();
            //Faz a consulta na tabela
            Cursor cursor = db.rawQuery("SELECT * FROM tarefas ORDER BY id DESC", null);

            //Recupera índice

            int indice = cursor.getColumnIndex("nome");

            //Move o ponteiro para o início da tabela
            cursor.moveToFirst();

            //Cria o adaptador da ArrayList
            lista = (ListView) findViewById(R.id.listaId);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    MainActivity.this,
                    android.R.layout.simple_list_item_2,
                    android.R.id.text2,
                    listaTarefas
            );
            //Seta o adaptador
            lista.setAdapter(adapter);

            //Percorre até chegar o fim da linha, atribuindo as tarefas a ArrayList
            while (cursor != null) {
                listaTarefas.add( cursor.getString(indice));
                cursor.moveToNext();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
