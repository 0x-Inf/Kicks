package com.diablo.jayson.kicksv1.UI.SignUp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.diablo.jayson.kicksv1.Models.User;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.databinding.FragmentSetProfilePicBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import timber.log.Timber;

public class SetProfilePicBottomSheet extends BottomSheetDialogFragment {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GET = 2;
    private FragmentSetProfilePicBottomSheetBinding binding;
    private NavController navController;
    private SignUpViewModel signUpViewModel;
    private String currentPhotoPath;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSetProfilePicBottomSheetBinding.inflate(inflater, container, false);
        navController = Navigation.findNavController(requireActivity(), R.id.sign_up_nav_host_fragment);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        signUpViewModel = new ViewModelProvider(requireActivity()).get(SignUpViewModel.class);

        binding.cameraTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });

        binding.photosTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPhotos();
            }
        });

        binding.cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack();
            }
        });
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void openPhotos() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(requireContext().getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }


    }

    private void openCamera() {
        // Create the camera_intent ACTION_IMAGE_CAPTURE
        // it will open the camera for capture the image
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        camera_intent.putExtra(MediaStore.EXTRA_OUTPUT,
//                Uri.withAppendedPath(getExternalFilesDir, targetFilename));


        // Start the activity with camera_intent,
        // and request pic id
        if (camera_intent.resolveActivity(requireContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Timber.e(ex);
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(requireContext(),
                        "com.diablo.jayson.kicksv1.fileprovider",
                        photoFile);
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(camera_intent, REQUEST_IMAGE_CAPTURE);
            }


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                assert data != null;
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                Uri imageUri = data.getData();
                Timber.e(imageUri.toString());
                updateViewModel(imageUri);
                navigateOut();
            case REQUEST_IMAGE_GET:
//                Bitmap thumbnail = data.getParcelable("data");
                assert data != null;
                Uri fullPhotoUri = data.getData();
                Timber.e(fullPhotoUri.toString());
                updateViewModel(fullPhotoUri);
                navigateOut();
        }
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
//            assert data != null;
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            Uri imageUri = data.getData();
//            Timber.e(imageUri.toString());
//            updateViewModel(imageUri);
//            navigateOut();
//
//        }
//        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
//            assert data != null;
//            Bitmap thumbnail = data.getParcelable("data");
//            Uri fullPhotoUri = data.getData();
//            // Do work with photo saved at fullPhotoUri
//        }

    }

    private void navigateOut() {
        navController.popBackStack();
    }

    private void updateViewModel(Uri imageUri) {
        User mainUser = signUpViewModel.getUser().getValue();
        assert mainUser != null;
        mainUser.setPhotoUrl(imageUri.toString());
    }
}
