package com.silexsecure.arusdriver.driverregistration;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.silexsecure.arusdriver.ImagePickerActivity;
import com.silexsecure.arusdriver.R;
import com.silexsecure.arusdriver.RegisterActivity;
import com.silexsecure.arusdriver.util.CustomViewPager;
import com.silexsecure.arusdriver.util.FirebaseUploader;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.silexsecure.arusdriver.ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION;
import static com.silexsecure.arusdriver.ImagePickerActivity.REQUEST_GALLERY_IMAGE;

public class VehicleInformationFragment extends Fragment {

    CustomViewPager viewPager;
    Button bPrevious, bNext;
    ImageView ivTowingVanImage;
    ProgressBar pbProgress;
    public static final int REQUEST_IMAGE = 100;
    int selectedCarType = -1;

    EditText etVehicleType;
    EditText etVehicleMake;
    EditText etVehicleNumber;
    EditText etYear, etCarColor;
    private String TAG = "VehicleInfoTAG";

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_vehicle_info, container, false);
        // Inflate the layout for this fragment
//        RegisterActivity.Companion.setCurrentTab(2);

        viewPager = Objects.requireNonNull(getActivity()).findViewById(R.id.viewpager_main);

        etVehicleType = view.findViewById(R.id.etVehicleType);
        etVehicleMake = view.findViewById(R.id.etVehicleMake);
        etVehicleNumber = view.findViewById(R.id.etVehicleNumber);
        etYear = view.findViewById(R.id.etYear);
        etCarColor = view.findViewById(R.id.etCarColor);

        bPrevious = view.findViewById(R.id.bPrevious);
        bNext = view.findViewById(R.id.bNext);
        pbProgress = view.findViewById(R.id.pbProgress);
        ivTowingVanImage = view.findViewById(R.id.ivTowingVanImage);

        etVehicleType.setFocusable(false);
        etVehicleType.setOnClickListener(v -> {
            showAlertDialog();
        });

        ivTowingVanImage.setOnClickListener(v -> Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        } else if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check());

        bNext.setOnClickListener(v -> {

            if (etVehicleType.getText().toString().isEmpty() ||
                    etVehicleMake.getText().toString().isEmpty() ||
                    etVehicleNumber.getText().toString().isEmpty() ||
                    etYear.getText().toString().isEmpty() || etCarColor.getText().toString().isEmpty()) {

                Log.d("MYTAG", "onCreateView: empty");
                etVehicleType.setError(etVehicleType.getText().toString().isEmpty() ? "Vehicle type is required" : null);
                etVehicleMake.setError(etVehicleMake.getText().toString().isEmpty() ? "Vehicle make is required" : null);
                etVehicleNumber.setError(etVehicleNumber.getText().toString().isEmpty() ? "Vehicle number is required" : null);
                etYear.setError(etYear.getText().toString().isEmpty() ? "Vehicle year is required" : null);
                etCarColor.setError(etCarColor.getText().toString().isEmpty() ? "Vehicle Color is required" : null);

            } else if (RegisterActivity.Companion.getRegisterRequest().getCarPic() == null) {
                Log.d("MYTAG", "onCreateView: vehicleimage");
                Toast.makeText(getContext(), "Please select vehicle Image to continue", Toast.LENGTH_LONG).show();
            } else {
                Log.d("MYTAG", "onCreateView: not empty");
                RegisterActivity.Companion.getRegisterRequest().setCarType(etVehicleType.getText().toString());
                RegisterActivity.Companion.getRegisterRequest().setCarMake(etVehicleMake.getText().toString());
                RegisterActivity.Companion.getRegisterRequest().setPlateNumber(etVehicleNumber.getText().toString());
                RegisterActivity.Companion.getRegisterRequest().setYear(etYear.getText().toString());
                RegisterActivity.Companion.getRegisterRequest().setColour(etCarColor.getText().toString());

                viewPager.setCurrentItem(2, true);
            }
        });

        bPrevious.setOnClickListener(v -> {
            viewPager.setCurrentItem(0, true);
        });

        return view;
    }


    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(getContext(), new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }


    private void launchGalleryIntent() {
        Intent intent = new Intent(getContext(), ImagePickerActivity.class);
        intent.putExtra(INTENT_IMAGE_PICKER_OPTION, REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
//        intent.putExtra(INTENT_LOCK_ASPECT_RATIO, false);
//        intent.putExtra(INTENT_ASPECT_RATIO_X, 16); // 16x9, 1x1, 3:4, 3:2
//        intent.putExtra(INTENT_ASPECT_RATIO_Y, 9);

        // setting maximum bitmap width and height
//        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, false);
//        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
//        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }


    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }


    private void launchCameraIntent() {
        Intent intent = new Intent(getContext(), ImagePickerActivity.class);
        intent.putExtra(INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
//        intent.putExtra(INTENT_LOCK_ASPECT_RATIO, true);
//        intent.putExtra(INTENT_ASPECT_RATIO_X, 3); // 16x9, 1x1, 3:4, 3:2
//        intent.putExtra(INTENT_ASPECT_RATIO_Y, 4);
//
//        // setting maximum bitmap width and height
//        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
//        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
//        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(VehicleInformationFragment.this)
//                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
//            }
//        });
    }


    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), R.style.MaterialThemeDialog);
        alertDialog.setTitle("Select Vehicle Type");
        String[] items = new String[]{"Drag Truck", "Low Bed"};

        alertDialog.setSingleChoiceItems(items, selectedCarType, (dialog, which) -> {

            switch (which) {
                case 0:
                case 1:
                    etVehicleType.setText(items[which]);
                    RegisterActivity.Companion.getRegisterRequest().setCarType(items[which]);
                    selectedCarType = which;
                    break;
            }
            dialog.dismiss();

        });

        AlertDialog alertDialog1 = alertDialog.create();
        alertDialog1.setCanceledOnTouchOutside(false);
        alertDialog1.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {

                    pbProgress.setVisibility(View.VISIBLE);
                    bNext.setEnabled(false);
                    bPrevious.setEnabled(false);

                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

                    File fileToUpload = new File(Uri.parse(uri.getPath()).toString());

                    userImageUploadTask(fileToUpload);

                    // loading profile image from local cache
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void userImageUploadTask(final File fileToUpload) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(getString(R.string.app_name)).child("VehicleImage").child(RegisterActivity.Companion.getRegisterRequest().getPhone());
        FirebaseUploader firebaseUploader = new FirebaseUploader(new FirebaseUploader.UploadListener() {
            @Override
            public void onUploadFail(String message) {
                if (!pbProgress.isIndeterminate())
                    pbProgress.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "Failed to upload Vehicle Image", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onUploadSuccess(String downloadUrl) {
                if (pbProgress.isIndeterminate()) {
                    pbProgress.setVisibility(View.GONE);
                    bNext.setEnabled(true);
                    bPrevious.setEnabled(true);

                }

                RegisterActivity.Companion.getRegisterRequest().setCarPic(downloadUrl);

                Glide.with(Objects.requireNonNull(getContext())).load(downloadUrl).into(ivTowingVanImage);

            }

            @Override
            public void onUploadProgress(int progress) {
                Log.d(TAG, "onUploadProgress: " + progress);

            }

            @Override
            public void onUploadCancelled() {
                if (pbProgress != null) {
                    pbProgress.setVisibility(View.GONE);
                }
                Toast.makeText(getContext(), "Vehicle Image Upload Cancelled", Toast.LENGTH_SHORT).show();
            }
        }, storageReference);
        firebaseUploader.setReplace(true);
        firebaseUploader.uploadImage(getContext(), fileToUpload);
        pbProgress.setVisibility(View.VISIBLE);
    }

}