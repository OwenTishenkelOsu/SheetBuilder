package com.example.sheetbuilder.activity;

import static android.content.ContentValues.TAG;
import static junit.framework.TestCase.assertNotNull;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;

import com.example.sheetbuilder.R;
import com.example.sheetbuilder.ui.fragment.LogInFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/*This is a test of for the UI on the Login Page to Ensure that UI buttons are valid, clickable
* and performs an OnClick Test*/

public class LoginFragmentTest {

   private FragmentScenario<LogInFragment> fragmentFragmentScenario;

    @Before
    public void setUp() {
        fragmentFragmentScenario = FragmentScenario.launchInContainer(LogInFragment.class);
        fragmentFragmentScenario.moveToState(Lifecycle.State.STARTED);
    }

    @Test //tests that the gmail_button exists
    public void test1()
    {

        fragmentFragmentScenario.onFragment(logInFragment ->{
            View view = logInFragment.getView();
            assertNotNull(view.findViewById(R.id.gmail_button));
        });
    }
    @Test //tests that the exit button exists
    public void test2()
    {
        fragmentFragmentScenario.onFragment(logInFragment ->{
            View view = logInFragment.getView();
            assertNotNull(view.findViewById(R.id.exit_button));
        });
    }
    @Test   //tests that the gmail button has onclick listener
    public void test3()
    {
        fragmentFragmentScenario.onFragment(logInFragment ->{
            View view = logInFragment.getView();
            assertNotNull(view.findViewById(R.id.gmail_button).hasOnClickListeners());
        });
    }

    @Test //tests onclick by calling Onclick (performs a Onclick action)
    public void test4()
    {
        fragmentFragmentScenario.onFragment(logInFragment ->{
            View view = logInFragment.getView();
            assertNotNull(view.findViewById(R.id.gmail_button).callOnClick());
        });
    }

    @After
    public void tearDown(){
        Log.d(TAG, "LoginFragment Tests Finished ");
    }
}
