package com.dario.mensapp;

        import android.content.Intent;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.os.Handler;
        import android.support.v4.app.Fragment;
        import android.support.v4.widget.SwipeRefreshLayout;
        import android.view.KeyEvent;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.inputmethod.EditorInfo;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.EditText;
        import android.widget.ImageButton;
        import android.widget.ListView;
        import android.widget.Spinner;
        import android.widget.TextView;
        import android.widget.Toast;

        import org.json.JSONArray;
        import org.json.JSONObject;

        import java.io.UnsupportedEncodingException;
        import java.net.URLEncoder;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.Date;
        import java.util.LinkedList;
        import java.util.List;
        import java.util.Locale;

public class HomeFragment extends Fragment {
    private ListView mialista;
   public List<String> listaMense;

    private boolean daRicaricare;
    private SwipeRefreshLayout refresh;
    private boolean chiamataRecenti;
    private boolean chiamataRicerca;
    private Spinner mensa;
    private Spinner pasto;
    private EditText cerca;
    private ArrayAdapter<String> adapterMensa;
    private ArrayAdapter<CharSequence> adapterPasto;
    private String zonaCane;
    private String menseParam;
    private String pastoParam;
    private boolean listaCaricata;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        listaMense=new ArrayList<String>();
        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mensa = (Spinner) rootView.findViewById(R.id.filtroMense);
        pasto = (Spinner) rootView.findViewById(R.id.filtroPasto);
        cerca = (EditText) rootView.findViewById(R.id.cerca);     //campo per il nome del cane
        final ImageButton bottoneRicerca = (ImageButton) rootView.findViewById(R.id.ricerca);
        refresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeLayout);
        mialista = (ListView) rootView.findViewById(R.id.listView1);

        //caricamento cani recenti
        chiamataRecenti = true;
        chiamataRicerca = false;

        adapterPasto = ArrayAdapter.createFromResource(
                getActivity(), R.array.tipopasto, android.R.layout.simple_spinner_dropdown_item);
        pasto.setAdapter(adapterPasto);
    new GetMense().execute(new HttpCalls());


        adapterMensa = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, listaMense);
        mensa.setAdapter(adapterMensa);


/*
        cerca.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    handled = true;
                }
                return handled;
            }
        });
        bottoneRicerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menseParam = (String) mensa.getSelectedItem();
                pastoParam = (String) pasto.getSelectedItem();

                    chiamataRicerca = true;
                    chiamataRecenti = false;
                    new Touched().execute(new ExternalDatabase());
               }
        });

        refresh.setColorSchemeResources(android.R.color.holo_red_dark, R.color.red,
                android.R.color.holo_red_light, R.color.red);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                chiamataRecenti = true;
                chiamataRicerca = false;
                new Touched().execute(new ExternalDatabase());

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh.setRefreshing(false);
                    }
                }, 7000);
            }
        });
*/
        return rootView;

    }
/*
    private class Touched extends AsyncTask<HttpCalls, Long, JSONArray> {
        @Override
        protected JSONArray doInBackground(ExternalDatabase... params) {
            try {
                // eseguito in background
                if (chiamataRecenti)
                    return params[0].getData("http://" + ExternalDatabase.DOMAIN + "/search.php");
                else //chiamataRicerca
                    return params[0].getData("http://" + ExternalDatabase.DOMAIN + "/search.php?nome=" + URLEncoder.encode(nomeCane, "UTF-8") + "&razza=" + URLEncoder.encode(razzaCane, "UTF-8") + "&sesso=" + sessoCane + "&stato=" + URLEncoder.encode(statoCane, "UTF-8") + "&citta=" + URLEncoder.encode(zonaCane, "UTF-8"));


            } catch (UnsupportedEncodingException x) {
                return params[0].getData("http://" + ExternalDatabase.DOMAIN + "/search.php");
            }

        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            refresh.setRefreshing(false);
            try {
                JSONObject objectControllo = jsonArray.getJSONObject(0);
                List<Cane> listaCani = new LinkedList<>();
                if (objectControllo.has("output")) {
                    if (listaCaricata) {
                        if (chiamataRecenti) {
                            chiamataRecenti = false;

                        }
                        if (chiamataRicerca) {
                            chiamataRicerca = false;
                        }
                        adapter.clear();
                        adapter.notifyDataSetChanged();

                    }
                    Toast.makeText(getActivity(), "Cane non trovato", Toast.LENGTH_SHORT).show();
                } else {

                    JSONObject jsonObject;


                    String nomecane;
                    int idCane;
                    String zona;
                    String razza;
                    String stato;
                    String dataDiNascita;
                    String dataRegistrazione;
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ITALY);
                    Date birth;
                    Date registerDate;
                    String image;
                    String sesso;
                    int lungArray;
                    if (chiamataRecenti) {
                        lungArray = 10;
                    } else
                        lungArray = jsonArray.length();

                    for (int i = 0; i < lungArray; i++) {

                        jsonObject = jsonArray.getJSONObject(i);
                        idCane = jsonObject.getInt("id");
                        nomecane = jsonObject.getString("nome");

                        image = jsonObject.getString("img");
                        sesso = jsonObject.getString("sesso");
                        razza = jsonObject.getString("razza");

                        dataDiNascita = jsonObject.getString("datanascita");
                        dataRegistrazione = jsonObject.getString("datacreazione");
                        stato = jsonObject.getString("stato");
                        zona = jsonObject.getString("citta");
                        birth = formatter.parse(dataDiNascita);
                        registerDate = formatter.parse(dataRegistrazione);
                        Cane c = new Cane(idCane, nomecane, zona, razza, sesso,
                                stato, image, birth, isNewCane(registerDate));

                        if (chiamataRecenti) {
                            if (c.compareDogs(primo)) {
                                if (daRicaricare) {
                                    listaCani.add(c);
                                    continue;
                                } else                    // l'aggiornamento avviene solo se il primo cane della nuova lista è diverso dal cane della lista corrente
                                    return;
                            }
                            if (i == 0) {

                                primo = c;
                                if (daRicaricare) {
                                    listaCani.add(c);
                                    continue;
                                }
                            }

                            listaCani.add(c);


                        } else {//chiamataRicerca


                            daRicaricare = true;
                            listaCani.add(c);
                        }
                    }
                }

                adapter = new TestImmagineAdapter(getActivity(), R.layout.preview_cane, listaCani);

                // Getting adapter by passing xml data ArrayList

                mialista.setAdapter(adapter);
                listaCaricata = true;
                if (chiamataRecenti) {
                    chiamataRecenti = false;

                } else {
                    chiamataRicerca = false;
                }
                AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapter, View view,
                                            int position, long id) {
                        Cane caneSelezionato = (Cane) adapter.getItemAtPosition(position);

                        Intent openPage = new Intent(getActivity(), DogProfileActivity.class);
                        openPage.putExtra("idCaneSelezionato", caneSelezionato.getId());
                        openPage.putExtra("nomeCaneSelezionato", caneSelezionato.getNome());

                        startActivity(openPage);
                    }
                };
                mialista.setOnItemClickListener(clickListener);

                sesso.setAdapter(adapterSesso);

                stato.setAdapter(adapterStatoVuoto);
                razza.setAdapter(adapterRazza);
                zona.setText("");
                cerca.setText("");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



*/
    private class GetMense extends AsyncTask<HttpCalls, Long, String> {
        @Override
        protected String doInBackground(HttpCalls... params) {

                    return params[0].getData( HttpCalls.DOMAIN + "/getMense.php");


        }

        @Override
        protected void onPostExecute(String output) {
            try {
                JSONArray jsonArray = new JSONArray(output);


                    JSONObject jsonObject;


                    String indirizzo;
                 String citta;


                    for (int i = 0; i < jsonArray.length(); i++) {

                        jsonObject = jsonArray.getJSONObject(i);
                        indirizzo = jsonObject.getString("Indirizzo");

                        citta = jsonObject.getString("Citta");
                        listaMense.add(indirizzo+" , "+citta);

                    }



            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}