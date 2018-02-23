package moviles2018itesm.quinelapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class LobbyActive extends Fragment {
    private TextView userScore, league, owner, lobbyID;
    private Button history, play;
    SharedPreferences sharedPreferences;

    public LobbyActive() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_lobby_active, container, false);

        //Initializing textviews and buttons
        userScore = (TextView)v.findViewById(R.id.score);
        league = (TextView)v.findViewById(R.id.league);
        owner = (TextView)v.findViewById(R.id.owner);
        lobbyID = (TextView)v.findViewById(R.id.lobbyID);
        history = (Button)v.findViewById(R.id.history);
        play = (Button)v.findViewById(R.id.play);

        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    assert currentUser != null;
                    assert user != null;
                    if (Objects.equals(currentUser.getEmail(), user.name)){
                        userScore.setText("Score: " + user.score);
                        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("game", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("game", user.game);
                        editor.apply();
                        Log.w("ACTIVE", user.game);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("LOBBY", "Failed to read value.", error.toException());
            }
        });

        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("game", Context.MODE_PRIVATE);
        String game = sharedPreferences.getString("game", null);

        Log.w("ACTIVE", game + " ");

        myRef = database.getReference("games");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Game game = snapshot.getValue(Game.class);
                    sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("game", Context.MODE_PRIVATE);
                    String gameSec = sharedPreferences.getString("game", null);

                    assert currentUser != null;
                    assert game != null;
                    if (Objects.equals(gameSec, game.id)){
                        league.setText("League: " + game.league);
                        owner.setText("Owner: " + game.owner);
                        lobbyID.setText("Lobby: " + game.id);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("LOBBY", "Failed to read value.", error.toException());
            }
        });

        //-----------------------------------
        //DE ALGUNA FORMA LLENAR LISTVIEW CON PARTICIPANTES AQUI
        //--------------------------------------

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //PASAR A LA ACTIVIDAD DE HISTORIA
                Intent intent = new Intent();//PONER AQUI INCIALIZADOR DE ACTIVIDAD
                startActivity(intent);
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //PASAR A LA ACTIVIDAD DE JUEGO
                Intent intent = new Intent();//PONER AQUI INCIALIZADOR DE ACTIVIDAD
                startActivity(intent);
            }
        });

        return v;
    }

}