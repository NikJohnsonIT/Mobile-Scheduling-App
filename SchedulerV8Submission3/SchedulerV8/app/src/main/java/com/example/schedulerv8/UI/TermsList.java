package com.example.schedulerv8.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.schedulerv8.Adapters.TermAdapter;
import com.example.schedulerv8.Database.Repository;
import com.example.schedulerv8.Entities.Term;
import com.example.schedulerv8.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class TermsList extends AppCompatActivity {
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_list);
        RecyclerView termListRV = findViewById(R.id.termsListAllTermsRV);
        final TermAdapter termAdapter = new TermAdapter(this);
        termListRV.setAdapter(termAdapter);
        termListRV.setLayoutManager(new LinearLayoutManager(this));
        repository = new Repository(getApplication());
        List<Term> allTerms = repository.getAllTerms();
        termAdapter.setTerms(allTerms);


        FloatingActionButton addTermFAB = findViewById(R.id.termListAddTermFAB);
        //termAdapter.setTerms(allTerms);

        addTermFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TermsList.this, TermDetails.class);
               //intent.putExtra("term_id", );
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onResume(){
        super.onResume();
        List<Term> allTerms = repository.getAllTerms();
        RecyclerView termsListRV=findViewById(R.id.termsListAllTermsRV);
        final TermAdapter termAdapter = new TermAdapter(this);
        termsListRV.setAdapter(termAdapter);
        termsListRV.setLayoutManager(new LinearLayoutManager(this));
        termAdapter.setTerms(allTerms);
    }

}
