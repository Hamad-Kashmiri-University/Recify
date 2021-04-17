package com.example.recify;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ModelAdapter extends RecyclerView.Adapter<ModelAdapter.Holder> {

    //vars
    private Context context;
    private ArrayList<ModelData> dataList;

    private DBHelper dbHelper;


    public ModelAdapter(Context context, ArrayList<ModelData> dataList) {
        this.context = context;
        this.dataList = dataList;
        dbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //parse layout using inflate
        View view = LayoutInflater.from(context).inflate(R.layout.activity_my_recipes, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        //get,set and handle clicks
        ModelData model = dataList.get(position);
        String id = model.getId();
        String name = model.getName();
        String image = model.getImage();
        String time = model.getTime();
        String ingredients = model.getIngredients();
        String instructions = model.getInstructions();
        String addedTime = model.getTimeAdded();
        String updatedTime = model.getTimeUpdated();

        holder.recipeName.setText(name);
        holder.recipeTime.setText(time + " Minutes");



        if(image.equals("null")){
            //iuf no image
            holder.foodImage.setImageResource(R.drawable.person);

        }
        else{
            holder.foodImage.setImageURI(Uri.parse(image));

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RecipeDetail.class);
                intent.putExtra("RECIPE_ID", id);
                context.startActivity(intent);


            }
        });

        holder.detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moreDetails(""+position,""+id,""+name,""+image,""+time,""+ingredients,""+instructions,""+addedTime,""+updatedTime);

            }
        });

    }

    private void moreDetails(String position, String id, String name, String image, String time, String ingredients, String instructions, String addedTime, String updatedTime) {
        //options for details
        String[] options = {"Edit", "Delete"};
        //alert
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i==0) {
                    //if edit
                    Log.d("onclickdoalog","success");
                    Intent intent = new Intent(context, AddUpdateRecipe.class);
                    intent.putExtra("ID",id);
                    intent.putExtra("NAME", name);
                    intent.putExtra("IMAGE", image);
                    intent.putExtra("TIME", time);
                    intent.putExtra("INSTRUCTIONS", instructions);
                    intent.putExtra("INGREDIENTS", ingredients);
                    intent.putExtra("TIME_ADDED", addedTime);
                    intent.putExtra("TIME_UPDATED", updatedTime);
                    intent.putExtra("EditMode", true);
                    context.startActivity(intent);

                }
                else if (i==1){
                    //if delete
                    dbHelper.deleteByID(id);
                    Toast.makeText(context, "Recipe has been deleted", Toast.LENGTH_LONG).show();
                    //refreshes
                    ((UploadRecipe)context).onResume();
                }
            }
        });
        builder.create().show();
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class Holder extends RecyclerView.ViewHolder{

        //views
        ImageView foodImage;
        TextView recipeName, recipeTime;
        ImageButton detailsButton;

        public Holder(@NonNull View itemView) {
            super(itemView);

            //hooks
            foodImage = itemView.findViewById(R.id.foodCardImage);
            recipeName = itemView.findViewById(R.id.recipeCardName);
            recipeTime = itemView.findViewById(R.id.recipeCardTime);
            detailsButton = itemView.findViewById(R.id.detailsButton);

        }
    }

}
