package com.grocery.bhadoriashop.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.grocery.bhadoriashop.R;
import com.jama.carouselview.CarouselView;
import com.jama.carouselview.CarouselViewListener;
import com.jama.carouselview.enums.IndicatorAnimationType;
import com.jama.carouselview.enums.OffsetType;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;
    private int[] carouselImages = {R.drawable.carousel_image1,
            R.drawable.carousel_image2, R.drawable.carousel_image3};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        initView(rootView);
        return rootView;
    }
    private void initView(View rootView) {
//        recyclerView = (RecyclerView) rootView.findViewById(R.id.productlist_user_recyclerview);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
//        recyclerView.setLayoutManager(gridLayoutManager);
//        //send Query to FirebaseDatabase
//        mFirebaseDatabase = FirebaseDatabase.getInstance();
//        mRef = mFirebaseDatabase.getReference("Products");
        CarouselView carouselView = rootView.findViewById(R.id.carousel_view);
        carouselView.setSize(carouselImages.length);
        carouselView.setResource(R.layout.image_carousel_item);
        carouselView.setAutoPlay(true);
        carouselView.setIndicatorAnimationType(IndicatorAnimationType.THIN_WORM);
        carouselView.setCarouselOffset(OffsetType.CENTER);
        carouselView.setCarouselViewListener(new CarouselViewListener() {
            @Override
            public void onBindView(View view, int position) {
                // Example here is setting up a full image carousel
                ImageView imageView = view.findViewById(R.id.carousel_imageView);
                imageView.setImageDrawable(getResources().getDrawable(carouselImages[position]));
            }
        });
        // After you finish setting up, show the CarouselView
        carouselView.show();
    }

    @Override
    public void onStart() {
        super.onStart();
//
//        final FirebaseRecyclerOptions<AdminProductList> options =
//                new FirebaseRecyclerOptions.Builder<AdminProductList>()
//                        .setQuery(mRef, AdminProductList.class)
//                        .build();
//
//        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<AdminProductList, UserProductListViewHolder>(options) {
//            @Override
//            public UserProductListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.cardview_user_productlist, parent, false);
//
//                return new UserProductListViewHolder(view);
//            }
//            @Override
//            protected void onBindViewHolder(UserProductListViewHolder holder, int position, AdminProductList model) {
//                Log.d("GridView","Working 4");
//                // Bind the image_details object to the BlogViewHolder
//                holder.setDetails(getContext(), model.getProductName(), model.getProductCategory(), model.getProductSubCategory(),
//                        model.getProductBrand(), model.getProductImageURL(), model.getMeasureIn(), model.getTotalWeightIn(), model.getItemWeightIn(), model.getMRPPricePerUnit(), model.getSellingPricePerUnit(), model.getTotalWeight(), model.getItemWeight(), model.getItemCount());
//
//            }
//        };
//
//        //set adapter to recyclerview
//        recyclerView.setAdapter(firebaseRecyclerAdapter);
//        firebaseRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
       // firebaseRecyclerAdapter.stopListening();
    }
}
