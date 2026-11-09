/*
 ===== ANDROID STUDIO HOTKEYS (Windows) =====

  --- FINDING STUFF ---
  Ctrl+Shift+F       Search text in whole project
  Alt+F7             Find everywhere this thing is used

  --- EDITING ---
  Ctrl+/             Comment / uncomment line
  Ctrl+Shift+/       Block comment
  Ctrl+Alt+L         Reformat code (fix indentation)
  Shift+F6           Rename everywhere (safe rename)
  Ctrl+P             Show what parameters a method takes

  --- RUNNING ---
  Shift+F10          Run
  Shift+F9           Debug
  Ctrl+Shift+F10     Run the test/file the cursor is in
  Ctrl+F2            Stop

 --- GIT ---
  Ctrl+T             Pull (update project)  <-- do this first, every time
  Ctrl+K             Commit
  Ctrl+Shift+K       Push

  --- WINDOWS & PANELS ---
  Alt+1              Project panel
  Alt+6              Logcat (app logs / crashes)
 Alt+F12            Terminal
 */

// package name
package com.onarc.app;

// Imports necessary, boilerplate android classes
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// Detects screen size, prevents system bar overlapping
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}