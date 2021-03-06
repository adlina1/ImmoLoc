package com.example.immoloc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Lifecycle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.immoloc.database.AdDao;
import com.example.immoloc.database.AdTable;
import com.example.immoloc.database.AppDatabase;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    public TextView search, profile, home;
    public Button mesAnnonces, ajouterAnnonce;
    String valUserName;
    public int valUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setNavigationViewListener();

        // On récupère le nom de l'utilisateur que l'Activité Login nous a envoyé pour l'afficher
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Intent mIntent = getIntent();
            valUserId = mIntent.getIntExtra("getUserId", 0);
            valUserName = extras.getString("getUN");
            TextView tv = findViewById(R.id.titleHomePage);
            tv.setText(String.format(getString(R.string.titlePage), valUserName));
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawer = findViewById(R.id.drawer_layout);
        // Rendre les items du groupe cliquables
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Cette fois, utilisation du mécanisme de sharedpref pour envoyer l'id de l'user pour la modification d'une activité
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("TheIdOfUser", String.valueOf(valUserId));
        editor.commit();

        // Au clic sur le bouton recherche du footer
        search = findViewById(R.id.searchBtn);
        search.setOnClickListener(v -> {
            Intent redirection = new Intent(v.getContext(), SearchActivity.class);
            startActivity(redirection);
        });
        // Au clic sur le bouton accueil du footer
        home = findViewById(R.id.homeBtn);
        home.setOnClickListener(v -> {
            Toast.makeText(this, "Vous êtes déjà dans la page d'accueil", Toast.LENGTH_SHORT).show();
        });
        // Au clic sur le bouton profil du footer
        profile = findViewById(R.id.profileBtn);
        profile.setOnClickListener(v -> {
            Intent redirection = new Intent(v.getContext(), ProfileActivity.class);
            redirection.putExtra("getUN",valUserName);
            redirection.putExtra("userId",valUserId);
            startActivity(redirection);
        });

      // Pour la déconnexion
      if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

      // Gestion des clics sur le menu latéral
      navigationView.bringToFront();
      navigationView.setNavigationItemSelectedListener(item -> {
          switch (item.getItemId()) {

              case R.id.nav_logout: {
                  Intent intent = new Intent(this, MainActivity.class);
                  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                  intent.putExtra("EXIT", true);
                  startActivity(intent);
                  Toast.makeText(this, "Vous êtes maintenant déconnecté", Toast.LENGTH_SHORT).show();
                  return true;
              }
              case R.id.nav_msg:
                  Intent intent = new Intent(this, MessageActivity.class);
                  startActivity(intent);
                  Toast.makeText(this, "Vous n'avez pas de messages", Toast.LENGTH_SHORT).show();
                  return true;

              case R.id.nav_profile:
                  Intent redirection = new Intent(this, ProfileActivity.class);
                  redirection.putExtra("userId",valUserId);
                  startActivity(redirection);
                  //Intent intent = new Intent(this, Profile.class);
          }
          return true;
      });

      // Au clic sur mes annonces
        mesAnnonces = findViewById(R.id.voirMesAnnonces);
        mesAnnonces.setOnClickListener(view -> {
            Intent redir = new Intent(this, MyAdsActivity.class);
            redir.putExtra("userId",valUserId);
            startActivity(redir);
        });

      // Au clic sur l'ajout d'une annonce
      ajouterAnnonce = findViewById(R.id.ajouterAnnonce);
      ajouterAnnonce.setOnClickListener(view -> {
          Intent redir = new Intent(this, AddAd.class);
          redir.putExtra("getUN",valUserName);
          redir.putExtra("userId",valUserId);
          startActivity(redir);
      });

    } // fin onCreate


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed(); // ferme l'activité quand click sur retour
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }

    private void setNavigationViewListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


}