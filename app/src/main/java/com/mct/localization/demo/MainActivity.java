package com.mct.localization.demo;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mct.base.ui.BaseOverlayDialog;
import com.mct.localization.DarkMode;
import com.mct.localization.Language;
import com.mct.localization.Localization;
import com.mct.localization.Theme;
import com.mct.localization.demo.adapters.DarkModeAdapter;
import com.mct.localization.demo.adapters.LanguageAdapter;
import com.mct.localization.demo.adapters.ThemesAdapter;
import com.mct.localization.demo.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SingleChoiceDialog<?> dialog;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Localization.attachBaseContext(this, newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityMainBinding.inflate(getLayoutInflater())).getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        invalidate();
        initListener();
    }

    private void invalidate() {
        binding.tvTheme.setText(Localization.getTheme().getName());
        binding.tvDarkMode.setText(Localization.getDarkMode().getTitleRes());
        binding.tvLanguage.setText(Localization.getLanguage().getDisplayLanguageBySelf());
    }

    private void initListener() {
        binding.btnChangeTheme.setOnClickListener(v -> {
            dismissDialog();

            ThemesAdapter adapter = new ThemesAdapter(new ArrayList<>(Arrays.asList(Theme.values())), Localization.getTheme());
            dialog = new SingleChoiceDialog<Theme>(this)
                    .setSpanCount(4)
                    .setAdapter(adapter)
                    .setTitle(getString(R.string.txt_select_theme))
                    .setNegativeButton(getString(R.string.txt_cancel), BaseOverlayDialog::dismiss)
                    .setPositiveButton(getString(R.string.txt_save), d -> {
                        dismissDialog();
                        if (Localization.setTheme(d.getSelectedItem())) {
                            recreate();
                        }
                    });
            dialog.show();
        });
        binding.btnChangeDarkMode.setOnClickListener(v -> {
            dismissDialog();
            DarkModeAdapter adapter = new DarkModeAdapter(new ArrayList<>(Arrays.asList(DarkMode.values())), Localization.getDarkMode());
            dialog = new SingleChoiceDialog<DarkMode>(this)
                    .setSpanCount(1)
                    .setAdapter(adapter)
                    .setTitle(getString(R.string.txt_select_dark_mode))
                    .setNegativeButton(getString(R.string.txt_cancel), BaseOverlayDialog::dismiss)
                    .setPositiveButton(getString(R.string.txt_save), d -> {
                        dismissDialog();
                        if (Localization.setDarkMode(d.getSelectedItem())) {
                            recreate();
                        }
                    });
            dialog.show();
        });
        binding.btnChangeLanguage.setOnClickListener(v -> {
            dismissDialog();
            LanguageAdapter adapter = new LanguageAdapter(new ArrayList<>(Arrays.asList(Language.values())), Localization.getLanguage());
            dialog = new SingleChoiceDialog<Language>(this)
                    .setSpanCount(1)
                    .setAdapter(adapter)
                    .setTitle(getString(R.string.txt_select_language))
                    .setNegativeButton(getString(R.string.txt_cancel), BaseOverlayDialog::dismiss)
                    .setPositiveButton(getString(R.string.txt_save), d -> {
                        dismissDialog();
                        if (Localization.setLanguage(d.getSelectedItem())) {
                            recreate();
                        }
                    });
            dialog.show();
        });
    }

    private void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

}