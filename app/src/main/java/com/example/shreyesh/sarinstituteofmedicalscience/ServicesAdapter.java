package com.example.shreyesh.sarinstituteofmedicalscience;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ViewHolder> {

    Context context;
    private List<Services> servicesList;

    public ServicesAdapter(List<Services> servicesList) {
        this.servicesList = servicesList;
    }

    @NonNull
    @Override
    public ServicesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.services_single_layout, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesAdapter.ViewHolder holder, int position) {

        String title = servicesList.get(position).getTitle();
        String img = servicesList.get(position).getImage();
        holder.setTitle(title);
        holder.setImage(img);


        switch (position) {

            //Blood tests
            case 0:
                final String[] bloodTest = {"TSH", "ABO Typing", "Liver Enzymes", "Lipid Test", "ESR", "hCG Test"};
                final ArrayList selecteditem = new ArrayList();

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Select Tests");
                builder.setMultiChoiceItems(bloodTest, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b) {
                            selecteditem.add(i);
                        } else if (selecteditem.contains(i)) {
                            selecteditem.remove(Integer.valueOf(i));
                        }

                    }
                });


                builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        HashMap<String, Integer> testMap = new HashMap<>();
                        testMap.put("TSH", 500);
                        testMap.put("ABO Typing", 650);
                        testMap.put("Liver Enzymes", 600);
                        testMap.put("Lipid Test", 1200);
                        testMap.put("ESR", 1500);
                        testMap.put("hCG Test", 3500);

                        Integer total = 0;
                        for (Object s : selecteditem) {

                            if (testMap.containsKey(bloodTest[Integer.valueOf(s.toString())])) {
                                total = total + testMap.get(bloodTest[Integer.valueOf(s.toString())]);
                            }
                        }
                        Intent intent = new Intent(context, ConfirmServicesActivity.class);
                        intent.putExtra("total", total.toString());
                        intent.putStringArrayListExtra("itemSelectList", selecteditem);
                        intent.putExtra("itemMap", testMap);
                        ArrayList<String> itemList = new ArrayList<>(Arrays.asList(bloodTest));
                        intent.putStringArrayListExtra("itemList", itemList);
                        intent.putExtra("serviceType", "bloodTest");
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selecteditem.clear();

                    }
                });
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog dialog = builder.create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                    }
                });

                break;

            //Imaging
            case 1:
                final String[] imaging = {"X-Ray", "CT Scan", "MRI", "Ultrasound", "Lung Screening", "Fluoroscopy", "Mammography"};
                final ArrayList selectedImaging = new ArrayList();

                final AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                builder2.setTitle("Select ");
                builder2.setMultiChoiceItems(imaging, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b) {
                            selectedImaging.add(i);
                        } else if (selectedImaging.contains(i)) {
                            selectedImaging.remove(Integer.valueOf(i));
                        }

                    }
                });

                builder2.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        HashMap<String, Integer> imgMap = new HashMap<>();
                        imgMap.put("X-Ray", 600);
                        imgMap.put("CT Scan", 800);
                        imgMap.put("MRI", 10000);
                        imgMap.put("Ultrasound", 1800);
                        imgMap.put("Lung Screening", 1500);
                        imgMap.put("Fluoroscopy", 2000);
                        imgMap.put("Mammography", 2500);

                        int total = 0;
                        for (Object s : selectedImaging) {
                            if (imgMap.containsKey(imaging[Integer.valueOf(s.toString())])) {

                                total = total + imgMap.get(imaging[Integer.valueOf(s.toString())]);
                            }
                        }

                        Intent intent = new Intent(context, ConfirmServicesActivity.class);
                        intent.putExtra("total", String.valueOf(total));
                        intent.putExtra("itemMap", imgMap);
                        intent.putStringArrayListExtra("itemSelectList", selectedImaging);
                        ArrayList<String> iTest = new ArrayList<>(Arrays.asList(imaging));
                        intent.putStringArrayListExtra("itemList", iTest);
                        intent.putExtra("serviceType", "imaging");
                        context.startActivity(intent);
                        ((Activity) context).finish();

                    }
                });

                builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedImaging.clear();

                    }
                });
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog dialog = builder2.create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                    }
                });

                break;


            case 3:
                final String[] roomServices = {"Request Consultant Visit", "Diet Meal", "Drinking Water"};
                final ArrayList selectedRoomService = new ArrayList();

                final AlertDialog.Builder roomBuilder = new AlertDialog.Builder(context);
                roomBuilder.setTitle("Select");
                roomBuilder.setMultiChoiceItems(roomServices, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b) {
                            selectedRoomService.add(i);
                        } else if (selectedRoomService.contains(i)) {
                            selectedRoomService.remove(Integer.valueOf(i));
                        }
                    }
                });

                roomBuilder.setPositiveButton("Request", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AlertDialog.Builder reqBuilder = new AlertDialog.Builder(context);
                        reqBuilder.setMessage("Your request received");
                        reqBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        });
                        Dialog dialog = reqBuilder.create();
                        dialog.show();
                        dialog.setCanceledOnTouchOutside(false);

                    }
                });

                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Dialog dialog = roomBuilder.create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                    }
                });
                break;

            case 2:
                final String[] medicineList = {"Amoxicillin", "Azithromycin", "Generic Glucophage", "Lisinopril", "Singulair", "Hydrocodone", "Crestor",
                        "Paracetamol", "Hydrochlorothiazide"};
                Arrays.sort(medicineList);
                final ArrayList selectedMedicines = new ArrayList();
                final AlertDialog.Builder medicineBuilder = new AlertDialog.Builder(context);
                medicineBuilder.setTitle("Select");
                medicineBuilder.setMultiChoiceItems(medicineList, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b) {
                            selectedMedicines.add(i);
                        } else if (selectedMedicines.contains(i)) {
                            selectedMedicines.remove(Integer.valueOf(i));
                        }
                    }
                });

                medicineBuilder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        HashMap<String, Integer> medMap = new HashMap<>();
                        medMap.put("Amoxicillin", 350);
                        medMap.put("Azithromycin", 500);
                        medMap.put("Lisinopril", 250);
                        medMap.put("Generic Glucophage", 320);
                        medMap.put("Singulair", 450);
                        medMap.put("Hydrocodone", 300);
                        medMap.put("Crestor", 200);
                        medMap.put("Paracetamol", 150);
                        medMap.put("Hydrochlorothiazide", 220);

                        int total = 0;
                        for (Object s : selectedMedicines) {
                            if (medMap.containsKey(medicineList[Integer.valueOf(s.toString())])) {
                                total = total + medMap.get(medicineList[Integer.valueOf(s.toString())]);
                            }
                        }

                        Intent intent = new Intent(context, ConfirmServicesActivity.class);
                        intent.putExtra("total", String.valueOf(total));
                        intent.putExtra("itemMap", medMap);
                        intent.putStringArrayListExtra("itemSelectList", selectedMedicines);
                        ArrayList<String> iTest = new ArrayList<>(Arrays.asList(medicineList));
                        intent.putStringArrayListExtra("itemList", iTest);
                        intent.putExtra("serviceType", "medicines");
                        context.startActivity(intent);
                        ((Activity) context).finish();

                    }
                });

                medicineBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedMedicines.clear();
                        return;
                    }
                });

                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Dialog dialog = medicineBuilder.create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                    }
                });
                break;
        }


    }

    @Override
    public int getItemCount() {
        return servicesList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        public void setImage(String img) {
            ImageView imageView = (ImageView) view.findViewById(R.id.servicesSingleImage);
            Picasso.get().load(img).into(imageView);
        }

        public void setTitle(String t) {
            TextView title = (TextView) view.findViewById(R.id.servicesSingleTitle);
            title.setText(t);

        }
    }
}
