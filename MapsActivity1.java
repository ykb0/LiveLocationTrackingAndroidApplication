package com.example.dell.map;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity1 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseReference mUsersDatabase;
  //  private DatabaseReference mRootRef;
    private FirebaseUser mCurrent_user;

    private String mCurrent_state;
    String display_name;
  static public  double dlat ;
    static public  double dlong ;
    MarkerOptions mo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps1);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        final String user_id = getIntent().getStringExtra("user_id");

     //   mRootRef = FirebaseDatabase.getInstance().getReference();

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

        mCurrent_user = FirebaseAuth.getInstance().getCurrentUser();
        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                display_name = (String) dataSnapshot.child("status").getValue();
                String[] seperated = display_name.split(",");
                String latipos = seperated[0].trim();
                String longipos = seperated[1].trim();
                 dlat = Double.parseDouble(latipos);
                 dlong = Double.parseDouble(longipos);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng mycoordinate = new LatLng(dlat,dlong);
       // String lat = Double.toString(dlat);
       // Toast.makeText(this,lat,Toast.LENGTH_SHORT).show();
        mMap.addMarker(new MarkerOptions().position(mycoordinate).title("user current location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mycoordinate,15.5f));

    }



}
