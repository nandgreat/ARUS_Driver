package com.silexsecure.arusdriver.driverregistration;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
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
import com.silexsecure.arusdriver.OtpVerificationActivity;
import com.silexsecure.arusdriver.R;


import com.silexsecure.arusdriver.RegisterActivity;
import com.silexsecure.arusdriver.model.RegisterDriverRequest;
import com.silexsecure.arusdriver.model.RegisterResponse;
import com.silexsecure.arusdriver.service.APIService;
import com.silexsecure.arusdriver.service.ClientAPI;
import com.silexsecure.arusdriver.util.CustomViewPager;
import com.silexsecure.arusdriver.util.FirebaseUploader;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.silexsecure.arusdriver.ImagePickerActivity.INTENT_ASPECT_RATIO_X;
import static com.silexsecure.arusdriver.ImagePickerActivity.INTENT_ASPECT_RATIO_Y;
import static com.silexsecure.arusdriver.ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION;
import static com.silexsecure.arusdriver.ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO;
import static com.silexsecure.arusdriver.ImagePickerActivity.REQUEST_GALLERY_IMAGE;

public class PhotoLicenseFragment extends Fragment {

    private static final int REQUEST_IMAGE = 898;
    private static final String TAG = "PhotoLicenseTAG";
    private static final int REQUEST_LICENSE_IMAGE = 811;
    Button bPrevious, bContinue;
    CircleImageView driverProfileImage;
    ImageView ivDriverLicense;
    CustomViewPager viewPager;
    ProgressBar pbProgress, pbLicenseProgress;

    ProgressDialog progressDialog;

    private String imageToSelect;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_photo_license, container, false);

        initUI(view);

        driverProfileImage.setOnClickListener(v -> Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            imageToSelect = "profileImage";
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

        ivDriverLicense.setOnClickListener(v -> Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            imageToSelect = "licenseImage";

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
        bContinue.setOnClickListener(v -> {
//            viewPager.setCurrentItem(2, true);

            if (RegisterActivity.Companion.getRegisterRequest().getImage() == null || RegisterActivity.Companion.getRegisterRequest().getLicense() == null) {
                Toast.makeText(getContext(), "Profile Image is required", Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), "Driver License is required", Toast.LENGTH_SHORT).show();
            }

            progressDialog.setTitle("Please wait");
            progressDialog.setMessage("Registration in Progress");
            progressDialog.setCancelable(false);
            progressDialog.show();
            attemptRegister(RegisterActivity.Companion.getRegisterRequest());
        });

        bPrevious.setOnClickListener(v -> {
            viewPager.setCurrentItem(1, true);
        });

        // Inflate the layout for this fragment
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    private void showImagePickerOptions() {
        MyImagePickerActivity.showImagePickerOptions(getContext(), new MyImagePickerActivity.PickerOptionListener() {
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
        intent.putExtra(INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(INTENT_ASPECT_RATIO_Y, 1);
        if (imageToSelect.contentEquals("profileImage")) {
            // setting aspect ratio
            intent.putExtra(INTENT_LOCK_ASPECT_RATIO, true);
            intent.putExtra(INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
            intent.putExtra(INTENT_ASPECT_RATIO_Y, 1);
            startActivityForResult(intent, REQUEST_IMAGE);
            Log.d(TAG, "launchGalleryIntent: " + imageToSelect);
        } else {
            Log.d(TAG, "launchGalleryIntent: " + imageToSelect);
            // setting aspect ratio
            intent.putExtra(INTENT_LOCK_ASPECT_RATIO, false);
//            intent.putExtra(INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
//            intent.putExtra(INTENT_ASPECT_RATIO_Y, 1);
            startActivityForResult(intent, REQUEST_LICENSE_IMAGE);
        }

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
        if (imageToSelect.contentEquals("profileImage")) {

            Intent intent = new Intent(getContext(), ImagePickerActivity.class);
            intent.putExtra(INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

            // setting aspect ratio
            intent.putExtra(INTENT_LOCK_ASPECT_RATIO, true);
            intent.putExtra(INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
            intent.putExtra(INTENT_ASPECT_RATIO_Y, 1);

            // setting maximum bitmap width and height
            intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
            intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
            intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

            startActivityForResult(intent, REQUEST_IMAGE);

        } else {
            Intent intent = new Intent(getContext(), MyImagePickerActivity.class);
            intent.putExtra(INTENT_IMAGE_PICKER_OPTION, MyImagePickerActivity.REQUEST_IMAGE_CAPTURE);

            // setting maximum bitmap width and height
//            intent.putExtra(MyImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
//            intent.putExtra(MyImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
//            intent.putExtra(MyImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

//            startActivityForResult(intent, REQUEST_IMAGE);
            startActivityForResult(intent, REQUEST_LICENSE_IMAGE);
        }
    }

    private void userImageUploadTask(final File fileToUpload) {

        StorageReference storageReference = null;

        if (imageToSelect.contentEquals("profileImage"))
            storageReference = FirebaseStorage.getInstance().getReference().child(getString(R.string.app_name)).child("DriverProfileImage").child(RegisterActivity.Companion.getRegisterRequest().getPhone());
        else
            storageReference = FirebaseStorage.getInstance().getReference().child(getString(R.string.app_name)).child("LicenseImage").child(RegisterActivity.Companion.getRegisterRequest().getPhone());

        FirebaseUploader firebaseUploader = new FirebaseUploader(new FirebaseUploader.UploadListener() {
            @Override
            public void onUploadFail(String message) {
                if (pbProgress.getVisibility() == View.VISIBLE)
                    pbProgress.setVisibility(View.GONE);
                if (pbLicenseProgress.getVisibility() == View.VISIBLE)
                    pbProgress.setVisibility(View.GONE);
            }

            @Override
            public void onUploadSuccess(String downloadUrl) {
                if (pbProgress.getVisibility() == View.VISIBLE)
                    pbProgress.setVisibility(View.GONE);

                if (pbLicenseProgress.getVisibility() == View.VISIBLE) {
                    pbLicenseProgress.setVisibility(View.GONE);
                }

                bContinue.setEnabled(true);
                bPrevious.setEnabled(true);


                if (imageToSelect.contentEquals("profileImage")) {
                    RegisterActivity.Companion.getRegisterRequest().setImage(downloadUrl);
                    Glide.with(Objects.requireNonNull(getContext())).load(downloadUrl).into(driverProfileImage);
                } else {
                    RegisterActivity.Companion.getRegisterRequest().setLicense(downloadUrl);
                    Glide.with(Objects.requireNonNull(getContext())).load(downloadUrl).into(ivDriverLicense);
                }
            }

            @Override
            public void onUploadProgress(int progress) {

            }

            @Override
            public void onUploadCancelled() {
                if (pbProgress.getVisibility() == View.VISIBLE)
                    pbProgress.setVisibility(View.GONE);
                if (pbLicenseProgress.getVisibility() == View.VISIBLE) {
                    pbLicenseProgress.setVisibility(View.GONE);
                }
            }
        }, storageReference);
        firebaseUploader.setReplace(true);
        firebaseUploader.uploadImage(getContext(), fileToUpload);
//            pbProgress.setVisibility(View.VISIBLE);
    }


    private void attemptRegister(final RegisterDriverRequest request) {

        ClientAPI api = APIService.createDriverService(ClientAPI.class);

        Call<RegisterResponse> call = api.RegisterDriver(request);

        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                progressDialog.dismiss();
                if (response.isSuccessful()) {

                    if (response.code() == 200) {

                        if (Objects.requireNonNull(response.body()).getMessage().contentEquals("Account already exists")) {
                            Toast.makeText(getActivity(), "User already exists", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity(), "Registration Successful", Toast.LENGTH_LONG).show();

                            Intent otpIntent = new Intent(getActivity(), OtpVerificationActivity.class);
                            otpIntent.putExtra("otp", response.body().getOtp());
                            otpIntent.putExtra("userid", response.body().getUserid());
                            otpIntent.putExtra("phone", request.getPhone());
                            startActivity(otpIntent);
                            Objects.requireNonNull(getActivity()).finish();

                        }
                    } else if (response.code() == 409) {
                        Toast.makeText(getActivity(), "Mobile Number already exists", Toast.LENGTH_LONG).show();
                    }
                } else {

                    Toast.makeText(getActivity(), "Sorry Could not register you at the Moment", Toast.LENGTH_LONG).show();
                    Log.d("Registration Errors", response.message() + " " + response.code());
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {

                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Network Error.. Please try again", Toast.LENGTH_SHORT).show();

                Log.d("Registration Exception", t.getMessage());

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE || requestCode == REQUEST_LICENSE_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {

                    if (imageToSelect.contentEquals("profileImage"))
                        pbProgress.setVisibility(View.VISIBLE);
                    else
                        pbLicenseProgress.setVisibility(View.VISIBLE);

                    bContinue.setEnabled(false);
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

    public void initUI(View view) {

        bPrevious = view.findViewById(R.id.bPrevious);
        bContinue = view.findViewById(R.id.bContinue);
        pbProgress = view.findViewById(R.id.pbProgress);
        pbLicenseProgress = view.findViewById(R.id.pbLicenseProgress);
        driverProfileImage = view.findViewById(R.id.driverProfileImage);
        ivDriverLicense = view.findViewById(R.id.ivDriverLicense);
        viewPager = Objects.requireNonNull(getActivity()).findViewById(R.id.viewpager_main);

        progressDialog = new ProgressDialog(getContext());
    }
}