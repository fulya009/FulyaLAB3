package com.example.poster1019;

import androidx.fragment.app.Fragment;

// A simple fourth page: shows a photo and a description.
// The layout (fragment_pet.xml) already contains the image and text,
// so this Fragment just needs to tell its parent which layout to use.
public class PetFragment extends Fragment {

    // Passes the layout file up to Fragment, which inflates it for this page.
    public PetFragment() {
        super(R.layout.fragment_pet);
    }
}