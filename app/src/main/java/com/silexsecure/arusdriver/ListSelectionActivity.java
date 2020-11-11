package com.silexsecure.arusdriver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.silexsecure.arusdriver.adapters.CarModelAdapter;
import com.silexsecure.arusdriver.adapters.ListAdapter;
import com.silexsecure.arusdriver.model.CarMake;
import com.silexsecure.arusdriver.model.CarModel;

import java.util.ArrayList;

public class ListSelectionActivity extends AppCompatActivity {

    private final String TAG = "ListActivityTAG";
    CarMake carMake;
    private ArrayList<CarMake> carMakeList;
    private ArrayList<CarModel> carModelList;

    RecyclerView recyclerView;

    ListAdapter listAdapter;
    CarModelAdapter carModelAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_selection);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        if (getIntent().getStringExtra("from").contentEquals("carmake")) {
            getSupportActionBar().setTitle("Select Car Make");
            listAdapter = new ListAdapter(this, populateCarMake());
            recyclerView.setAdapter(listAdapter);

        } else if (getIntent().getStringExtra("from").contentEquals("carmodel")) {
            int carmakeId = getIntent().getIntExtra("carmakeid", 0);
            getSupportActionBar().setTitle("Select Car Model");
            carModelAdapter = new CarModelAdapter(this, populateCarModel(carmakeId));
            recyclerView.setAdapter(carModelAdapter);

        }

    }

    public ArrayList<CarMake> populateCarMake() {

        ArrayList<CarMake> carMakeList = new ArrayList<>();

        carMake = new CarMake(1, "Toyota");
        carMakeList.add(carMake);
        carMake = new CarMake(2, "Honda");
        carMakeList.add(carMake);
        carMake = new CarMake(3, "Peogeout");
        carMakeList.add(carMake);
        carMake = new CarMake(4, "Nissan");
        carMakeList.add(carMake);
        carMake = new CarMake(5, "KIA");
        carMakeList.add(carMake);
        carMake = new CarMake(6, "Range Rover");
        carMakeList.add(carMake);
        carMake = new CarMake(7, "Mercedes Benz");
        carMakeList.add(carMake);
        carMake = new CarMake(8, "BMW");
        carMakeList.add(carMake);

        return carMakeList;
    }

    public ArrayList<CarModel> populateCarModel(final int carsmakeID) {

        ArrayList<CarModel> carModelList = new ArrayList<>();

        CarModel carModel = new CarModel(1, 1, "Corolla");
        carModelList.add(carModel);
        carModel = new CarModel(2, 1, "Camry");
        carModelList.add(carModel);
        carModel = new CarModel(3, 1, "Sienna");
        carModelList.add(carModel);
        carModel = new CarModel(4, 1, "Highlander");
        carModelList.add(carModel);
        carModel = new CarModel(5, 1, "RAV-4");
        carModelList.add(carModel);
        carModel = new CarModel(6, 1, "Venza");
        carModelList.add(carModel);
        carModel = new CarModel(7, 1, "Yaris");
        carModelList.add(carModel);
        carModel = new CarModel(8, 1, "Avalon");
        carModelList.add(carModel);
        carModel = new CarModel(9, 1, "Prius");

        carModel = new CarModel(10, 2, "Accord");
        carModelList.add(carModel);
        carModel = new CarModel(11, 2, "Civic");
        carModelList.add(carModel);
        carModel = new CarModel(12, 2, "CR-V");
        carModelList.add(carModel);
        carModel = new CarModel(13, 2, "Passport");
        carModelList.add(carModel);
        carModel = new CarModel(14, 2, "Pilot");
        carModelList.add(carModel);
        carModel = new CarModel(15, 2, "Crossover");
        carModelList.add(carModel);
        carModel = new CarModel(16, 2, "Venza");
        carModelList.add(carModel);
        carModel = new CarModel(17, 2, "Clarity");
        carModelList.add(carModel);
        carModel = new CarModel(18, 2, "Fit");
        carModelList.add(carModel);
        carModel = new CarModel(19, 2, "Odyssey");
        carModelList.add(carModel);

        carModel = new CarModel(20, 3, "206");
        carModelList.add(carModel);
        carModel = new CarModel(21, 3, "1007");
        carModelList.add(carModel);
        carModel = new CarModel(22, 3, "407");
        carModelList.add(carModel);
        carModel = new CarModel(23, 3, "205");
        carModelList.add(carModel);
        carModel = new CarModel(24, 3, "207");
        carModelList.add(carModel);
        carModel = new CarModel(25, 3, "504");
        carModelList.add(carModel);
        carModel = new CarModel(26, 3, "309");
        carModelList.add(carModel);
        carModel = new CarModel(27, 3, "4007");
        carModelList.add(carModel);
        carModel = new CarModel(28, 3, "Partner");
        carModelList.add(carModel);
        carModel = new CarModel(29, 3, "307");
        carModelList.add(carModel);
        carModel = new CarModel(30, 3, "3008");
        carModelList.add(carModel);
        carModel = new CarModel(31, 3, "Expert");
        carModelList.add(carModel);
        carModel = new CarModel(32, 3, "505");
        carModelList.add(carModel);
        carModel = new CarModel(33, 3, "107");
        carModelList.add(carModel);
        carModel = new CarModel(34, 3, "406");
        carModelList.add(carModel);
        carModel = new CarModel(35, 3, "301");
        carModelList.add(carModel);
        carModel = new CarModel(36, 3, "605");
        carModelList.add(carModel);
        carModel = new CarModel(37, 3, "607");
        carModelList.add(carModel);
        carModel = new CarModel(38, 3, "308");
        carModelList.add(carModel);
        carModel = new CarModel(39, 3, "608");
        carModelList.add(carModel);

        carModel = new CarModel(40, 4, "ALMERA");
        carModelList.add(carModel);
        carModel = new CarModel(41, 4, "Sentra");
        carModelList.add(carModel);
        carModel = new CarModel(42, 4, "Altima");
        carModelList.add(carModel);
        carModel = new CarModel(43, 4, "Kicks");
        carModelList.add(carModel);
        carModel = new CarModel(44, 4, "Qashqai");
        carModelList.add(carModel);
        carModel = new CarModel(45, 4, "X-Trail");
        carModelList.add(carModel);
        carModel = new CarModel(46, 4, "Pathfinder");
        carModelList.add(carModel);
        carModel = new CarModel(47, 4, "Patrol 62");
        carModelList.add(carModel);
        carModel = new CarModel(48, 4, "NV350 URVAN");
        carModelList.add(carModel);
        carModel = new CarModel(49, 4, "NP300 HARDBODY");
        carModelList.add(carModel);
        carModel = new CarModel(51, 4, "Murano");
        carModelList.add(carModel);
        carModel = new CarModel(52, 4, "Armada");
        carModelList.add(carModel);
        carModel = new CarModel(53, 4, "Primera");
        carModelList.add(carModel);
        carModel = new CarModel(54, 5, "Soul");
        carModelList.add(carModel);
        carModel = new CarModel(55, 5, "Seltos");
        carModelList.add(carModel);
        carModel = new CarModel(56, 5, "Sportage");
        carModelList.add(carModel);
        carModel = new CarModel(57, 5, "Niro");
        carModelList.add(carModel);
        carModel = new CarModel(58, 5, "Sorento");
        carModelList.add(carModel);
        carModel = new CarModel(59, 5, "Telluride");
        carModelList.add(carModel);
        carModel = new CarModel(60, 5, "Rio");
        carModelList.add(carModel);
        carModel = new CarModel(61, 5, "Forte");
        carModelList.add(carModel);
        carModel = new CarModel(62, 5, "Optima");
        carModelList.add(carModel);


        carModel = new CarModel(63, 5, "Stinger");
        carModelList.add(carModel);
        carModel = new CarModel(64, 5, "Cadenza");
        carModelList.add(carModel);
        carModel = new CarModel(65, 5, "K900");
        carModelList.add(carModel);
        carModel = new CarModel(66, 5, "Sedona");
        carModelList.add(carModel);
        carModel = new CarModel(67, 5, "Niro");
        carModelList.add(carModel);
        carModel = new CarModel(68, 5, "Sorento");
        carModelList.add(carModel);
        carModel = new CarModel(69, 5, "Telluride");
        carModelList.add(carModel);
        carModel = new CarModel(70, 5, "Rio");
        carModelList.add(carModel);
        carModel = new CarModel(71, 5, "Forte");
        carModelList.add(carModel);
        carModel = new CarModel(71, 5, "Optima");


        carModel = new CarModel(72, 6, "A-Class");
        carModelList.add(carModel);
        carModel = new CarModel(73, 6, "AMG GT");
        carModelList.add(carModel);
        carModel = new CarModel(74, 6, "C-Class");
        carModelList.add(carModel);
        carModel = new CarModel(75, 6, "E-Class");
        carModelList.add(carModel);
        carModel = new CarModel(76, 6, "G-Class");
        carModelList.add(carModel);
        carModel = new CarModel(77, 6, "S-Class");
        carModelList.add(carModel);
        carModel = new CarModel(78, 6, "GT-Class");
        carModelList.add(carModel);
        carModel = new CarModel(79, 6, "GLK-Class");
        carModelList.add(carModel);
        carModel = new CarModel(80, 6, "Maybach");
        carModelList.add(carModel);
        carModel = new CarModel(81, 6, "Compressor");
        carModelList.add(carModel);
        carModel = new CarModel(82, 6, "GLE");
        carModelList.add(carModel);
        carModel = new CarModel(83, 6, "GLA");
        carModelList.add(carModel);
        carModel = new CarModel(84, 6, "Metris");
        carModelList.add(carModel);
        carModel = new CarModel(85, 6, "GLS-Class");
        carModelList.add(carModel);

        ArrayList<CarModel> secondList = new ArrayList<CarModel>();

        for (CarModel a : carModelList) {
            if (a.getCarMakeID() == carsmakeID) {
                secondList.add(a);
                Log.d(TAG, "populateCarModel: " + a.getCarModel());
            }
        }


        return secondList;
    }
}
