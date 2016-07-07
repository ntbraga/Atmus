package atmus.atmus.token;

import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by ntbra on 03/07/2016.
 */
public class TokenAndroidController {
    private Token tk;
    private TextView token;
    private TextView remaining;
    private ProgressBar pBar;
    private Updater updater;
    Handler handler = new Handler();
    private TokenAndroidController(Token token, TextView tokenView, TextView remaining, ProgressBar bar){
        this.token = tokenView;
        this.remaining = remaining;
        this.pBar = bar;
        tk = token;
        pBar.setMax((int)tk.getTimeWindow());
        updater = new Updater();
    }

    private void begin(){
        handler.post(updater);
    }

    private class Updater implements Runnable{

        @Override
        public void run() {
            token.setText(tk.getSmallToken());
            remaining.setText(tk.getReaming()+" segundos restantes.");
            pBar.setProgress(tk.getReaming());
            handler.postDelayed(this, 500);
        }
    }

    public static void start(Token token, TextView tokenView, TextView remaining, ProgressBar bar){
        new TokenAndroidController(token, tokenView, remaining, bar).begin();
    }
}
