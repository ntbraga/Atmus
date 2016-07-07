package atmus.atmus.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import atmus.atmus.R;
import atmus.atmus.token.Token;
import atmus.atmus.token.TokenAndroidController;
import atmus.atmus.token.TokenManager;

/**
 * Created by ntbra on 03/07/2016.
 */
public class TokenAdapter extends RecyclerView.Adapter<TokenAdapter.TokenViweHolder>{
    List<Account> accounts;
    TextView remaining;
    ProgressBar bar;
    public TokenAdapter(List<Account> accounts, TextView remaining, ProgressBar bar){
        this.accounts = accounts;
        this.remaining = remaining;
        this.bar = bar;
    }

    @Override
    public TokenViweHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.token_rc_row, parent, false);
        return new TokenViweHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TokenViweHolder holder, int position) {
        holder.setIsRecyclable(false);
        Account account = accounts.get(position);
        holder.name.setText(account.getName());
        holder.account.setText(account.getAccount());
        TokenAndroidController.start(TokenManager.getTokenGenerator(account.getKey()), holder.token, remaining, bar);
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    public class TokenViweHolder extends RecyclerView.ViewHolder{
        TextView name, account, token;
        public TokenViweHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.name);
            account = (TextView)itemView.findViewById(R.id.account);
            token = (TextView)itemView.findViewById(R.id.token);
        }
    }
}
