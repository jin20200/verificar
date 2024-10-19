package com.easyplexdemoapp.ui.splash;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.easyplexdemoapp.databinding.ItemLangBinding;
import com.easyplexdemoapp.util.Tools;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for Movie.
 *
 * @author Yobex.
 */
public class ConfigurationAdapter extends RecyclerView.Adapter<ConfigurationAdapter.MainViewHolder>{


    private List<String> languages;

    private Activity activity;

    private SharedPreferences.Editor sharedPreferencesEditor;


    // Initialize the language mapping outside the onBind method
    HashMap<String, String> languageCodeToNameMapping = new HashMap<>();


    @SuppressLint("NotifyDataSetChanged")
    public void addMain(List<String> languages, Activity activity,SharedPreferences.Editor sharedPreferencesEditor) {
        this.languages = languages;
        this.activity = activity;
        this.sharedPreferencesEditor = sharedPreferencesEditor;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ConfigurationAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemLangBinding binding = ItemLangBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);


        return new ConfigurationAdapter.MainViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {

        holder.onBind(position);

    }



    @Override
    public int getItemCount() {
        if (languages != null) {
            return languages.size();
        } else {
            return 0;
        }
    }





    class MainViewHolder extends RecyclerView.ViewHolder {

        private final ItemLangBinding binding;

        MainViewHolder(@NonNull ItemLangBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }


        void onBind(final int position) {
            // Assuming 'languages' is a List<String> containing language codes
            String languageCode = languages.get(position);


            Context context = binding.langFlag.getContext();

            languageCodeToNameMapping.put("en", "English");
            languageCodeToNameMapping.put("fr", "French");
            languageCodeToNameMapping.put("ar", "العربية");
            languageCodeToNameMapping.put("es", "Spanish");
            languageCodeToNameMapping.put("es-rMX", "Mexican Spanish");
            languageCodeToNameMapping.put("pt", "Portuguese");
            languageCodeToNameMapping.put("pt-rBR", "Portugués Brasileño");
            languageCodeToNameMapping.put("tr", "Türkçe");
            languageCodeToNameMapping.put("sr", "Serbian");

            // Set the movie title based on the current language code
            String currentLanguageName = languageCodeToNameMapping.get(languageCode);
            binding.langName.setText(currentLanguageName);

            // Set click listener to change language
            binding.rootLayout.setOnClickListener(v -> changeLanguage(languageCode));

            // Set long-click listener to display the language code
            binding.rootLayout.setOnLongClickListener(v -> {
                Toast.makeText(activity, "Selected Language Code: " + languageCode, Toast.LENGTH_SHORT).show();
                return false;
            });

            String langCode = getLangCode(languageCode);

            Tools.onLoadMediaCoverAdapters(context,binding.langFlag, langCode);
        }

        @NonNull
        private String getLangCode(String languageCode) {
            String fixCountryCode = languageCode;


            switch (languageCode) {
                case "en":

                    fixCountryCode = "us";

                    break;
                case "ar":


                    fixCountryCode = "ma";

                    break;
                case "pt-rBR":


                    fixCountryCode = "br";
                    break;
                case "es-rMX":


                    fixCountryCode = "mx";
                    break;
                case "sr":


                    fixCountryCode = "rs";
                    break;
            }

            return "https://flagcdn.com/256x192/"+fixCountryCode+".png";
        }

        private void changeLanguage(String languageCode) {
            // Construct Locale based on the language code
            Locale locale;
            switch (languageCode) {
                case "pt-rBR":
                    // Brazilian Portuguese
                    locale = new Locale("pt", "BR");
                    break;
                case "es-rMX":
                    // Mexican Spanish
                    locale = new Locale("es", "MX");
                    break;
                case "fa":
                case "fa-IR":
                    // Farsi (Persian)
                    locale = new Locale("fa", "IR");
                    break;
                case "fr":
                case "fr-FR":
                    // French (France)
                    locale = new Locale("fr", "FR");
                    break;
                case "fr-CA":
                    // French (Canada)
                    locale = new Locale("fr", "CA");
                    break;
                default:
                    locale = new Locale(languageCode);
                    break;
            }

            // Set the default locale
            Locale.setDefault(locale);

            // Set configuration
            Configuration configuration = new Configuration();
            configuration.setLocale(locale);

            // Update resources
            Resources resources = activity.getResources();
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());

            // Recreate the activity to apply the new configuration
            activity.recreate();

            // Save the selected language in SharedPreferences
            sharedPreferencesEditor.putString("selectedLanguage", languageCode).apply();
        }


    }
}
