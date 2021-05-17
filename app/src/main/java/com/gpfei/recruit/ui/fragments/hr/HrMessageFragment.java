package com.gpfei.recruit.ui.fragments.hr;

import androidx.fragment.app.Fragment;

import com.hyphenate.easeui.ui.EaseConversationListFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HrMessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HrMessageFragment extends EaseConversationListFragment {
    @Override
    protected void initView() {
        super.initView();
        hideTitleBar();
        initData();
    }



    private void initData() {
        // run in a second
        final long timeInterval = 1000;
        Runnable runnable = new Runnable() {
            public void run() {
                while (true) {
                    // ------- code for task to run
                    conversationListView.refresh();
                    // ------- ends here
                    try {
                        Thread.sleep(timeInterval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
