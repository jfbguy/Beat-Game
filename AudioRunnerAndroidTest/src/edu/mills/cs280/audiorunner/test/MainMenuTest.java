package edu.mills.cs280.audiorunner.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.TextView;
import edu.mills.cs280.audiorunner.MainMenu;

public class MainMenuTest extends ActivityInstrumentationTestCase2<MainMenu> {
	
	private MainMenu mActivity;  // the activity under test
	private TextView mTitle; 
	private Button mStartButton, mSettingsButton;
	
	public MainMenuTest(){
		super("edu.mills.cs280.audiorunner", MainMenu.class);
	}
	
	@Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = this.getActivity();
        mTitle = (TextView) mActivity.findViewById(edu.mills.cs280.audiorunner.R.id.title_text);
        mStartButton = (Button) mActivity.findViewById(edu.mills.cs280.audiorunner.R.id.Start_Button);
        mSettingsButton = (Button) mActivity.findViewById(edu.mills.cs280.audiorunner.R.id.Setting_Button);
    }
	
    public void testPreconditions() {
      assertNotNull(mTitle);
      assertNotNull(mStartButton);
      assertNotNull(mSettingsButton);
    }
	

}
