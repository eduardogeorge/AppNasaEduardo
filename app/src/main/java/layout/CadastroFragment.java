package layout;

import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.design.widget.Snackbar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import layout.Constants;
import eduardogarcia.appemiolo.R;
import model.ServerRequest;
import model.ServerResponse;
import model.Usuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CadastroFragment extends Fragment implements View.OnClickListener {
    private AppCompatButton btn_cadastro;
    private EditText edt_email, edt_senha, edt_nome;
    private TextView txt_login;
    private ProgressBar progress;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastro,container,false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        btn_cadastro = (AppCompatButton)view.findViewById(R.id.btn_cadastro);
        txt_login = (TextView)view.findViewById(R.id.txt_login);
        edt_nome = (EditText)view.findViewById(R.id.edt_nome);
        edt_email = (EditText)view.findViewById(R.id.edt_email);
        edt_senha = (EditText)view.findViewById(R.id.edt_senha);

        progress = (ProgressBar)view.findViewById(R.id.progress);

        btn_cadastro.setOnClickListener(this);
        txt_login.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_login:
                goToLogin();
                break;

            case R.id.btn_cadastro:

                String nome = edt_nome.getText().toString();
                String email = edt_email.getText().toString();
                String senha = edt_senha.getText().toString();

                if(!nome.isEmpty() && !email.isEmpty() && !senha.isEmpty()) {

                    progress.setVisibility(View.VISIBLE);
                    registerProcess(nome,email,senha);

                } else {

                    Snackbar.make(getView(), "Campos vazios !", Snackbar.LENGTH_LONG).show();
                }
                break;

        }
    }

    private void registerProcess(String nome, String email,String senha){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SyncStateContract.Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        Usuario usuario = new Usuario();
        usuario.setNome_usuario(nome);
        usuario.setEmail_usuario(email);
        usuario.setSenha_usuario(senha);
        ServerRequest request = new ServerRequest();
        request.setOperacao(SyncStateContract.Constants.REGISTER_OPERATION);
        request.setUsuario(usuario);
        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();
                Snackbar.make(getView(), resp.getMensagem(), Snackbar.LENGTH_LONG).show();
                progress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                progress.setVisibility(View.INVISIBLE);
                Log.d(Constants.TAG,"failed");
                Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();

            }
        });
    }

    private void goToLogin(){

        Fragment login = new LoginFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame,login);
        ft.commit();
    }
}
