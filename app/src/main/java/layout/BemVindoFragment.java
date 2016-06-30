package layout;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import eduardogarcia.appemiolo.R;
import model.ServerRequest;
import model.ServerResponse;
import model.Usuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class BemVindoFragment extends Fragment implements View.OnClickListener {
    private TextView txt_nome,txt_email,txt_mensagem;
    private SharedPreferences pref;
    private AppCompatButton btn_mudar_senha,btn_logout;
    private EditText edt_senha_antiga,edt_nova_senha;
    private AlertDialog dialog;
    private ProgressBar progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bem_vindo,container,false);
        initViews(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        pref = getActivity().getPreferences(0);
        txt_nome.setText("Bem vindo : "+pref.getString(Constants.NAME,""));
        txt_email.setText(pref.getString(Constants.EMAIL,""));

    }

    private void initViews(View view){

        txt_nome = (TextView)view.findViewById(R.id.txt_nome);
        txt_email = (TextView)view.findViewById(R.id.txt_email);
        btn_mudar_senha = (AppCompatButton)view.findViewById(R.id.btn_mudar_senha);
        btn_logout = (AppCompatButton)view.findViewById(R.id.btn_logout);
        btn_mudar_senha.setOnClickListener(this);
        btn_logout.setOnClickListener(this);

    }

    private void showDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_alterar_senha, null);
        edt_senha_antiga = (EditText)view.findViewById(R.id.edt_senha_antiga);
        edt_nova_senha = (EditText)view.findViewById(R.id.edt_senha_nova);
        txt_mensagem = (TextView)view.findViewById(R.id.txt_mensagem);
        progress = (ProgressBar)view.findViewById(R.id.progress);
        builder.setView(view);
        builder.setTitle("Mudar senha");
        builder.setPositiveButton("Mudar senha", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old_password = edt_senha_antiga.getText().toString();
                String new_password = edt_senha_antiga.getText().toString();
                if(!old_password.isEmpty() && !new_password.isEmpty()){

                    progress.setVisibility(View.VISIBLE);
                    changePasswordProcess(pref.getString(Constants.EMAIL,""),old_password,new_password);

                }else {

                    txt_mensagem.setVisibility(View.VISIBLE);
                    txt_mensagem.setText("Campos vazios");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_mudar_senha:
                showDialog();
                break;
            case R.id.btn_logout:
                logout();
                break;
        }
    }

    private void logout() {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(Constants.IS_LOGGED_IN,false);
        editor.putString(Constants.EMAIL,"");
        editor.putString(Constants.NAME,"");
        editor.putString(Constants.UNIQUE_ID,"");
        editor.apply();
        goToLogin();
    }

    private void goToLogin(){

        android.app.Fragment login = new LoginFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame,login);
        ft.commit();
    }

    private void changePasswordProcess(String email,String senhaAntiga,String senhaNova){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        Usuario usuario = new Usuario();
        usuario.setEmail_usuario(email);
        usuario.setSenha_antiga_usuario(senhaAntiga);
        usuario.setSenha_nova_usuario(senhaNova);
        ServerRequest request = new ServerRequest();
        request.setOperacao(Constants.CHANGE_PASSWORD_OPERATION);
        request.setUsuario(usuario);
        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();
                if(resp.getResultado().equals(Constants.SUCCESS)){
                    progress.setVisibility(View.GONE);
                    txt_mensagem.setVisibility(View.GONE);
                    dialog.dismiss();
                    Snackbar.make(getView(), resp.getMensagem(), Snackbar.LENGTH_LONG).show();

                }else {
                    progress.setVisibility(View.GONE);
                    txt_mensagem.setVisibility(View.VISIBLE);
                    txt_mensagem.setText(resp.getMensagem());

                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                Log.d(Constants.TAG,"failed");
                progress.setVisibility(View.GONE);
                txt_mensagem.setVisibility(View.VISIBLE);
                txt_mensagem.setText(t.getLocalizedMessage());

            }
        });
    }
}
